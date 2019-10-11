package uk.co.vhome.sitecontroller.site;

public class FormFieldError
{
	private final String fieldName;

	private final String errorMessage;

	public FormFieldError(String fieldName, String errorMessage)
	{
		this.fieldName = fieldName;
		this.errorMessage = errorMessage;
	}

	@SuppressWarnings("unused") // Used during serialisation
	public String getFieldName()
	{
		return fieldName;
	}

	@SuppressWarnings("unused") // Used during serialisation
	public String getErrorMessage()
	{
		return errorMessage;
	}
}
