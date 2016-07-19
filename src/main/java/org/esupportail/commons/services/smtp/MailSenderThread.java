/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.smtp;

import java.io.File;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.smtp.signing.MessageSigner;


/**
 * Send a mail to an InternetAddress throught an asynchroneous thread.
 */
public class MailSenderThread extends Thread {

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(MailSenderThread.class);
	
	/**
	 * The SMTP servers to use.
	 */
	private List<SmtpServer> smtpServers;
	/**
	 * The sender.
	 */
	private InternetAddress from;
	/**
	 * The recipient.
	 */
	private InternetAddress to;
	/**
	 * The subject.
	 */
	private String subject;
	/**
	 * The HTML body.
	 */
	private String htmlBody;
	/**
	 * The plain text body.
	 */
	private String textBody;
	
	/**
	 * The list attach file.
	 */
	private List<File> files;
	
	/**
	 * The charset used to encode the headers.
	 */
	private String charset;

	/**
	 * The message signer.
	 */
	private MessageSigner messageSigner;
	
	/**
	 * Message-ID set to sending email.
	 */
	private String messageId;
	
	/**
	 * Constructor.
	 * @param smtpServers
	 * @param from
	 * @param to
	 * @param subject
	 * @param htmlBody
	 * @param textBody
	 * @param files 
	 * @param charset
	 * @param messageSigner 
	 * @param messageId
	 * @throws SmtpException 
	 */
	public MailSenderThread(
			final List<SmtpServer> smtpServers,
			final InternetAddress from, 
			final InternetAddress to, 
			final String subject,
			final String htmlBody,
			final String textBody,
			final List<File> files,
			final String charset,
			final MessageSigner messageSigner,
			final String messageId) throws SmtpException {
		super();
		if (logger.isDebugEnabled()) {
			logger.debug("starting a new thread for '" + to + "'...");
		}
		this.smtpServers = smtpServers;
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.htmlBody = htmlBody;
		this.textBody = textBody;
		this.files = files;
		this.charset = charset;
		this.setPriority(Thread.MIN_PRIORITY);
		this.messageSigner = messageSigner;
		this.messageId = messageId;
		logger.debug("thread started.");
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final void run() {
		SmtpUtils.sendEmail(smtpServers, from, to, subject, htmlBody, textBody, files, charset, messageSigner, messageId);
	}

}
