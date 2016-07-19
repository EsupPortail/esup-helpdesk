/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.smtp;

import java.io.File;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

/**
 * An implementation of SmtpService that sends emails asynchronously to 
 * prevent from rendering timetouts.
 * 
 * The configuration of such a bean is exactly the same as SimpleSmtpServiceImpl.
 * 
 * Please note that, as a new thread is created each time an email is sent,
 * exceptions thrown by the threads are not caught at engine level. They are 
 * however logged.
 * 
 * See /properties/smtp/smtp-example.xml.
 */
public class AsynchronousSmtpServiceImpl extends SimpleSmtpServiceImpl {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6912061623396146367L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(AsynchronousSmtpServiceImpl.class);

	/**
	 * Bean constructor.
	 */
	public AsynchronousSmtpServiceImpl() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.smtp.SimpleSmtpServiceImpl#send(
	 * javax.mail.internet.InternetAddress, java.lang.String, java.lang.String, 
	 * java.lang.String, java.util.List, boolean, java.lang.String)
	 */
	@Override
	protected void send(
			final InternetAddress to, 
			final String subject, 
			final String htmlBody, 
			final String textBody, 
			final List<File> files,
			final boolean intercept,
			final String messageId) {
		InternetAddress recipient = getRealRecipient(to, intercept);
		if (logger.isDebugEnabled()) {
			logger.debug("launching a new thread for '" + recipient + "'...");
		}
		// start a new thread that will do the job asynchroneously
		new MailSenderThread(
				getServers(), getFromAddress(), recipient, 
				subject, htmlBody, textBody, files, getCharset(),
				this.getMessageSigner(), messageId).start();
		logger.debug("thread launched.");
	}	

}
