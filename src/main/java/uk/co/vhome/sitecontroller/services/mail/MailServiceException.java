package uk.co.vhome.sitecontroller.services.mail;

import uk.co.vhome.sitecontroller.site.FailureCategory;

public class MailServiceException extends Exception
{
	MailServiceException(FailureCategory failureCategory, Throwable cause)
	{
		super(Integer.toString(failureCategory.ordinal()), cause);
	}
}
