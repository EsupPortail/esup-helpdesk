/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.smtp;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

/**
 * @author lusl0338
 * MimeMessage that can has Message-ID set. 
 */
public class MessageIdChangingMimeMessage extends MimeMessage {
	
	/**
	 * A Logger. 
	 */
	private static final Logger LOG = new LoggerImpl(SmtpUtils.class);

	/**
	 * Message-ID to set.
	 */
	private String messageId;
	
	/**
	 * @param session Session
	 * @param messageId Message-ID to set
	 */
	public MessageIdChangingMimeMessage(final Session session, final String messageId) {
		this(session);
		this.setMessageId(messageId);
	}

	/**
	 * @see javax.mail.internet.MimeMessage#MimeMessage(javax.mail.Session)
	 */
	public MessageIdChangingMimeMessage(final Session session) {
		super(session);
	}

	/**
	 * @param messageId - Message-ID to set
	 */
	public void setMessageId(final String messageId) {
		this.messageId = messageId;
		LOG.debug("Setting messageID: " + messageId);
	}

	/**
	 * @see javax.mail.internet.MimeMessage#updateHeaders()
	 */
	@Override
	protected void updateHeaders() throws MessagingException {
		super.updateHeaders();
		if ((messageId != null) && (messageId.length() > 0)) {
			setHeader("Message-ID", messageId);
		}
	}
}
