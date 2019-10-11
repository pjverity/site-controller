package uk.co.vhome.sitecontroller.registration;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Introspected
class UserDetail
{
	@Size(min = 2, max = 32, message = "First name must be between {min} and {max} characters")
	@Pattern(regexp = "[a-zA-Z]+", message = "First name must contain only alphabetic characters")
	private String firstName;

	@Size(min = 2, max = 64, message = "Last name must be between {min} and {max} characters")
	@Pattern(regexp = "[a-zA-Z-]+", message = "Last name must contain only alphabetic characters or a hyphen")
	private String lastName;

	/**
	 * NOTE: It's named 'phone' as opposed to 'phoneNumber' as the {@code name} attribute on the HTML {@code <input>} element
	 * only seems to auto-complete when given the value 'phone', which then needs to bind to this field
	 */
	@Size(max = 19, message = "Phone numbers can not be greater than {max} characters, ie. '+44 (0)207 000 1234'")
	@Pattern(regexp = "(^$)|((\\+)?[0-9() ]+)", message = "Phone number can only contain digits, '+' or '()' ")
	private String phone;

	String getFirstName()
	{
		return firstName;
	}

	@SuppressWarnings("unused") // Used during marshalling
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	String getLastName()
	{
		return lastName;
	}

	@SuppressWarnings("unused") // Used during marshalling
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	String getPhone()
	{
		return phone;
	}

	@SuppressWarnings("unused") // Used during marshalling
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
}
