package uk.co.vhome.sitecontroller.registration;

import freemarker.template.Configuration;
import io.micronaut.http.annotation.Controller;
import uk.co.vhome.sitecontroller.services.mail.MailService;

import java.io.IOException;
import java.util.Map;

@Controller("/rjj/registration")
public class RJJRegistrationController extends AbstractRegistrationController
{
	private static final String DOMAIN = "reigatejuniorjoggers.co.uk";

	private static final String DOMAIN_ADMIN_USER = "admin";

	private static final String DOMAIN_DESCRIPTION = "Reigate Junior Joggers";

	private static final Map<String, Object> ADDITIONAL_CONFIRMATION_MODEL = Map.of("facebookUrl", "https://www.facebook.com/reigatejuniorjoggers",
	                                                                                "twitterUrl","https://www.twitter.com/@juniorjoggers");

	RJJRegistrationController(Configuration configuration, MailService mailService) throws IOException
	{
		super(configuration, mailService, DOMAIN, DOMAIN_ADMIN_USER, DOMAIN_DESCRIPTION, ADDITIONAL_CONFIRMATION_MODEL);
	}

}
