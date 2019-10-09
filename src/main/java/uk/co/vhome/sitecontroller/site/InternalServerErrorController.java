package uk.co.vhome.sitecontroller.site;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class InternalServerErrorController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(InternalServerErrorController.class);

	@Error(global = true)
	public HttpResponse internalServerErrorHandler(HttpRequest request, Throwable ex)
	{
		LOGGER.error(ex.getMessage());

		JsonError error = new JsonError("Code: " + FailureCategory.UNSPECIFIED.ordinal());

		return HttpResponse.serverError(error);
	}

}
