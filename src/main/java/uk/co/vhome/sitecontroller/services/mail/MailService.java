package uk.co.vhome.sitecontroller.services.mail;

public interface MailService
{
	void sendHtmlMail(String senderAddress, String recipientAddress, String subject, String htmlBody, boolean isSmokeTest) throws MailServiceException;
}
