/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.smtp;

import java.io.File;
import java.util.List;

import javax.mail.internet.InternetAddress;

/**
 * An implementation of SmtpService that sends no email at all.
 */
public class VoidSmtpServiceImpl extends AbstractSmtpService {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1738559445155844511L;

	/**
	 * Constructor.
	 */
	public VoidSmtpServiceImpl() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.smtp.SmtpService#send(
	 * javax.mail.internet.InternetAddress, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void send(
			@SuppressWarnings("unused")
			final InternetAddress to, 
			@SuppressWarnings("unused")
			final String subject, 
			@SuppressWarnings("unused")
			final String htmlBody, 
			@SuppressWarnings("unused")
			final String textBody,
			@SuppressWarnings("unused")
			final String messageId) {
		// do nothing
	}	
	
	
	/**
	 * @see org.esupportail.commons.services.smtp.SmtpService#send(
	 * javax.mail.internet.InternetAddress, java.lang.String, java.lang.String, 
	 * java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public void send(
			@SuppressWarnings("unused")
			final InternetAddress to, 
			@SuppressWarnings("unused")
			final String subject, 
			@SuppressWarnings("unused")
			final String htmlBody, 
			@SuppressWarnings("unused")
			final String textBody,
			@SuppressWarnings("unused")
			final List<File> files,
			@SuppressWarnings("unused")
			final String messageId) {
		// do nothing
	}	
	
	/**
	 * @see org.esupportail.commons.services.smtp.SmtpService#sendtocc
	 * (javax.mail.internet.InternetAddress[], 
	 * javax.mail.internet.InternetAddress[], 
	 * javax.mail.internet.InternetAddress[], 
	 * java.lang.String, 
	 * java.lang.String, java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public void sendtocc(
			@SuppressWarnings("unused")
			final InternetAddress[] tos,
			@SuppressWarnings("unused")
			final InternetAddress[] ccs, 
			@SuppressWarnings("unused")
			final InternetAddress[] bccs, 
			@SuppressWarnings("unused")
			final String subject, 
			@SuppressWarnings("unused")
			final String htmlBody, 
			@SuppressWarnings("unused")
			final String textBody, 
			@SuppressWarnings("unused")
			final List<File> files,
			@SuppressWarnings("unused")
			final String messageId) {
		// do nothing
		
	}
	
	/**
	 * @see org.esupportail.commons.services.smtp.SmtpService#sendDoNotIntercept(
	 * javax.mail.internet.InternetAddress, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendDoNotIntercept(
			@SuppressWarnings("unused")
			final InternetAddress to, 
			@SuppressWarnings("unused")
			final String subject, 
			@SuppressWarnings("unused")
			final String htmlBody, 
			@SuppressWarnings("unused")
			final String textBody,
			@SuppressWarnings("unused")
			final String messageId) {
		// do nothing
	}
	
	
	
	/**
	 * @see org.esupportail.commons.services.smtp.SmtpService#sendDoNotIntercept(
	 * javax.mail.internet.InternetAddress, java.lang.String, java.lang.String, 
	 * java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public void sendDoNotIntercept(
			@SuppressWarnings("unused")
			final InternetAddress to,
			@SuppressWarnings("unused")
			final String subject,
			@SuppressWarnings("unused")
			final String htmlBody,
			@SuppressWarnings("unused")
			final String textBody,
			@SuppressWarnings("unused")
			final List<File> files,
			@SuppressWarnings("unused")
			final String messageId) {
		// do nothing
	}
	
}
