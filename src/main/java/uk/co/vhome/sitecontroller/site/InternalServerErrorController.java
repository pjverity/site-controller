package uk.co.vhome.sitecontroller.site;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;

@Controller
public class InternalServerErrorController
{
	@Error(global = true)
	public HttpResponse internalServerErrorHandler(HttpRequest request, Throwable ex)
	{
		JsonError error = new JsonError("Code: " + FailureCategory.UNSPECIFIED.ordinal());

		return HttpResponse.serverError(error);
	}

}
