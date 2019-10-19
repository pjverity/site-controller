package uk.co.vhome.sitecontroller.registration;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.vhome.sitecontroller.services.mail.MailService;
import uk.co.vhome.sitecontroller.services.mail.MailServiceException;
import uk.co.vhome.sitecontroller.site.FormFieldError;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract class AbstractRegistrationController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRegistrationController.class);

	private static final String REGISTRATION_CONFIRMATION_MAIL_TEMPLATE_FILE = "registration-confirmation-mail.ftlh";

	private static final String REGISTRATION_NOTIFICATION_MAIL_TEMPLATE_FILE = "registration-notification-mail.ftlh";

	private static final String REGISTRATION_MODEL_FIELD_FIRST_NAME = "firstName";

	private static final String REGISTRATION_MODEL_FIELD_LAST_NAME = "lastName";

	private static final String REGISTRATION_MODEL_FIELD_PHONE_NUMBER = "phoneNumber";

	private static final String NOTIFICATION_MODEL_FIELD_EMAIL_ADDRESS = "emailAddress";

	private static final Map<String, String> NOTIFICATION_MODEL = Map.of("notificationCategory", "New Registration");

	private final Map<String, Object> confirmationModel = new HashMap<>();

	private final String domainDescription;

	private final String domainEmail;

	private final Template confirmationTemplate;

	private final Template notificationTemplate;

	private final MailService mailService;

	AbstractRegistrationController(Configuration configuration, MailService mailService,
	                               String domain, String domainAdminUser, String domainDescription,
	                               Map<String, Object> additionalConfirmationModel) throws IOException
	{
		this.confirmationTemplate = configuration.getTemplate(REGISTRATION_CONFIRMATION_MAIL_TEMPLATE_FILE);
		this.notificationTemplate = configuration.getTemplate(REGISTRATION_NOTIFICATION_MAIL_TEMPLATE_FILE);

		this.mailService = mailService;
		this.domainDescription = domainDescription;
		this.domainEmail = domainAdminUser + "@" + domain;

		 confirmationModel.putAll(Map.of("domain", domain,
		                           "domainEmail", domainEmail,
		                           "domainDescription", domainDescription));

		 confirmationModel.putAll(additionalConfirmationModel);
	}

	@Post(value = "/register/{emailAddress}", consumes = {"application/x-www-form-urlencoded"})
	Single<HttpResponse> register(@Email String emailAddress, @Valid @Body UserDetail user, @QueryValue(defaultValue = "false") boolean isSmokeTest)
	{
		return Single.create(emitter -> processRegistration(emitter, emailAddress, user, isSmokeTest));
	}

	@Error(exception = ConstraintViolationException.class)
	public List<FormFieldError> onRegistrationFailed(HttpRequest request, ConstraintViolationException ex)
	{
		return ex.getConstraintViolations().stream()
				       .map(cv -> new FormFieldError(cv.getPropertyPath().toString(), cv.getMessage()))
				       .collect(Collectors.toList());
	}

	private void processRegistration(SingleEmitter<HttpResponse> emitter, String recipientAddress, UserDetail user, boolean isSmokeTest) throws MailServiceException
	{
		var userModel = new HashMap<String, Object>();

		userModel.put(REGISTRATION_MODEL_FIELD_FIRST_NAME, user.getFirstName());
		userModel.put(REGISTRATION_MODEL_FIELD_LAST_NAME, user.getLastName());
		userModel.put(REGISTRATION_MODEL_FIELD_PHONE_NUMBER, user.getPhone());

		try
		{
			sendRegistrationConfirmation(recipientAddress, userModel, isSmokeTest);

			sendRegistrationNotification(recipientAddress, userModel, isSmokeTest);

			emitter.onSuccess(HttpResponse.noContent());
		}
		catch (TemplateException | IOException e)
		{
			LOGGER.error("Failed to process template", e);
			emitter.onSuccess(HttpResponse.serverError());
		}
		catch (MailServiceException e)
		{
			LOGGER.error("Failed to send email notifications", e);
			throw e;
		}

	}

	private void sendRegistrationNotification(String recipientAddress, Map<String, Object> body, boolean isSmokeTest) throws TemplateException, IOException, MailServiceException
	{
		body.putAll(NOTIFICATION_MODEL);
		body.put(NOTIFICATION_MODEL_FIELD_EMAIL_ADDRESS, recipientAddress);

		StringWriter templateOutput = new StringWriter();
		notificationTemplate.process(body, templateOutput);

		sendEMail(domainEmail, "New Registration", templateOutput.toString(), isSmokeTest);
	}

	private void sendRegistrationConfirmation(String recipientAddress, Map<String, Object> body, boolean isSmokeTest) throws TemplateException, IOException, MailServiceException
	{
		body.putAll(confirmationModel);

		StringWriter templateOutput = new StringWriter();
		confirmationTemplate.process(body, templateOutput);

		sendEMail(recipientAddress, "Thanks for your enquiry", templateOutput.toString(), isSmokeTest);
	}

	private void sendEMail(String recipientAddress, String subject, String mailContent, boolean isSmokeTest) throws MailServiceException
	{
		mailService.sendHtmlMail(domainEmail + "(" + domainDescription + ")", recipientAddress, subject, mailContent, isSmokeTest);
	}
}
