package org.esupportail.helpdesk.services.feed.imap;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.Part;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.ActionScope;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.services.feed.ErrorHolder;
import org.esupportail.helpdesk.services.feed.imap.replytocleaner.ReplyToMessageCleaner;

import org.springframework.util.StringUtils;

/**
 * @author lusl0338
 * Reads email message and creates action (comments) from it.
 */
public class ActionMessageReaderImpl extends TicketMessageReaderImpl implements ActionMessageReader {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8704592124129262036L;

	/**
	 * The sender of email.
	 */
	private User sender;

	/**
	 * The content is set (only the first one should be set as comment).
	 */
	private Boolean contentPartSet;

    /**
     * Message cleaner used for stripping original message in reply.
     */
    private ReplyToMessageCleaner messageCleaner;
	
	/**
	 * Bean constructor.
	 */
	public ActionMessageReaderImpl() {
		super();
	}
	
	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.TicketMessageReaderImpl#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(messageCleaner, 
				"property messageCleaner of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.TicketMessageReaderImpl#getTextPart(
	 * javax.mail.Part, org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String, 
	 * org.esupportail.helpdesk.services.feed.ErrorHolder)
	 */
	@Override
	protected void getTextPart(
			final Part part, 
			final Ticket ticket,
			@SuppressWarnings("unused")
			final String partIndex,
			final ErrorHolder errorHolder) {
		if (contentPartSet) {
			return;
		}
		errorHolder.addInfo("analysing text/plain part " + part + "...");
		String content = getPartContentAsString(part, errorHolder);
		if (!StringUtils.hasText(content)) {
			return;
		}
		byte [] contentByteArray = contentToByteArray(content, errorHolder);
		if (contentByteArray == null) {
			return;
		}
        content = messageCleaner.clean(content);        
        content = content.replaceAll("\"", "&quot;");
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		content = content.replaceAll("[\\r\\n]+", "<br />");

        getDomainService().giveInformation(
				sender, ticket, content, 
				ActionScope.OWNER, false);
		contentPartSet = true;
	}
	
	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.TicketMessageReaderImpl#getHtmlPart(
	 * javax.mail.Part, org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String, 
	 * org.esupportail.helpdesk.services.feed.ErrorHolder)
	 */
	@Override
	protected void getHtmlPart(
			final Part part, 
			final Ticket ticket,
			@SuppressWarnings("unused")
			final String partIndex,
			final ErrorHolder errorHolder) {
		if (contentPartSet) {
			return;
		}
		errorHolder.addInfo("analysing text/html part " + part + "...");
		String content = getPartContentAsString(part, errorHolder);
		if (!StringUtils.hasText(content)) {
			return;
		}
		byte [] contentByteArray = contentToByteArray(content, errorHolder);
		if (contentByteArray == null) {
			return;
		}
        content = messageCleaner.clean(content);
        MessageHtmlCleaner cleaner = new MessageHtmlCleaner(content);
		String output;
		try {
			cleaner.clean();
			output = cleaner.getXmlAsString();
		} catch (IOException e) {
			errorHolder.addInfo("could not clean the HTML: " + e.getMessage());
			return;
		}

        getDomainService().giveInformation(
				sender, ticket, output, 
				ActionScope.OWNER, false);
		contentPartSet = true;
	}
	
	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.ActionMessageReader#readMessage(
	 * javax.mail.Message, org.esupportail.helpdesk.domain.beans.Ticket, 
	 * org.esupportail.helpdesk.services.feed.ErrorHolder)
	 */
	@Override
	public User readMessage(
			final Message message,
			final Ticket ticket,
			final ErrorHolder errorHolder) {
		sender = extractSender(message, errorHolder);
		if (errorHolder.hasErrors()) {
			errorHolder.addInfo(errorHolder.getErrorNumber() + " error(s) found, skiping the mail");
		} else {
            contentPartSet = false;
            getMessageParts(message, ticket, errorHolder);
		}
		return sender;
	}

    /**
     * Gets message cleaner object.
     * @return the message cleaner
     */
    protected ReplyToMessageCleaner getMessageCleaner() {
        return messageCleaner;
    }

    /**
     * Sets the message cleaner object.
     * @param messageCleaner the message cleaner
     */
    public void setMessageCleaner(final ReplyToMessageCleaner messageCleaner) {
        this.messageCleaner = messageCleaner;
    }
}
