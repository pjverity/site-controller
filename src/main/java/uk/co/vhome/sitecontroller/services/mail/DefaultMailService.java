package uk.co.vhome.sitecontroller.services.mail;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.vhome.sitecontroller.site.FailureCategory;

import javax.activation.DataHandler;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@Singleton
public class DefaultMailService implements MailService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMailService.class);

	private static final String MAIL_PROPERTY_PREFIX = "mail";

	private static final String SMOKE_TEST_RECIPIENT_ADDRESS = "admin@v-home.co.uk";

	@Property(name = MAIL_PROPERTY_PREFIX)
	private Map<String, String> mailProperties;

	private Session session;

	private final boolean isDevelopmentEnv;

	public DefaultMailService(Environment environment)
	{
		this.isDevelopmentEnv = environment.getActiveNames().contains(Environment.DEVELOPMENT);
	}

	@PostConstruct
	public void initialise(@Value("${MAIL_USERNAME}") String username, @Value("${MAIL_PASSWORD}") String password)
	{
		Properties mailProperties = new Properties();

		this.mailProperties.forEach((k, v) -> mailProperties.put(MAIL_PROPERTY_PREFIX + "." + k, v));

		session = Session.getInstance(mailProperties, new javax.mail.Authenticator()
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(username, password);
			}
		});
	}

	@Override
	public void sendHtmlMail(String senderAddress, String recipientAddress, String subject, String htmlBody, boolean isSmokeTest) throws MailServiceException
	{
		try
		{
			var senderInternetAddress = new InternetAddress(senderAddress);
			var recipientInternetAddress = getRecipientInternetAddress(recipientAddress, isSmokeTest);

			Message message = new MimeMessage(session);
			message.setFrom(senderInternetAddress);
			message.setRecipient(Message.RecipientType.TO, recipientInternetAddress);

			message.setSubject(isATest(isSmokeTest) ? subject + " (Smoke Test)" : subject);
			message.setDataHandler(new DataHandler(new ByteArrayDataSource(htmlBody, "text/html")));

			Transport.send(message);

			LOGGER.info("Sent email from: {}, to: {}, with content: {}", senderInternetAddress, recipientInternetAddress, htmlBody);
		}
		catch (MessagingException | IOException e)
		{
			LOGGER.error("Failed to send email" , e);
			throw new MailServiceException(FailureCategory.MAIL_DELIVERY, e);
		}
	}

	private InternetAddress getRecipientInternetAddress(String recipientAddress, boolean isSmokeTest) throws AddressException
	{
		var useTestAddress = isATest(isSmokeTest);
		return new InternetAddress(useTestAddress ? SMOKE_TEST_RECIPIENT_ADDRESS + "(" + recipientAddress + ")" : recipientAddress);
	}

	private boolean isATest(boolean isSmokeTest)
	{
		return isDevelopmentEnv || isSmokeTest;
	}
}
