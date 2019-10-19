package uk.co.vhome.sitecontroller.registration;

import freemarker.template.Configuration;
import io.micronaut.http.annotation.Controller;
import uk.co.vhome.sitecontroller.services.mail.MailService;

import java.io.IOException;
import java.util.Map;

@Controller("/clj/registration")
public class CLJRegistrationController extends AbstractRegistrationController
{
	private static final String DOMAIN = "caterhamladiesjoggers.co.uk";

	private static final String DOMAIN_ADMIN_USER = "admin";

	private static final String DOMAIN_DESCRIPTION = "Caterham Ladies Joggers";

	private static final Map<String, Object> ADDITIONAL_CONFIRMATION_MODEL = Map.of("facebookUrl", "https://www.facebook.com/caterhamjoggers");

	public CLJRegistrationController(Configuration configuration, MailService mailService) throws IOException
	{
		super(configuration, mailService, DOMAIN, DOMAIN_ADMIN_USER, DOMAIN_DESCRIPTION, ADDITIONAL_CONFIRMATION_MODEL);
	}
}
