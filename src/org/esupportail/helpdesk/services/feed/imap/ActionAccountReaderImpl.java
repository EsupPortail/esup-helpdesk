package org.esupportail.helpdesk.services.feed.imap;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.exceptions.TicketNotFoundException;
import org.esupportail.helpdesk.services.feed.ErrorHolder;
import org.esupportail.helpdesk.services.feed.imap.messageId.MessageIdException;
import org.esupportail.helpdesk.services.feed.imap.messageId.MessageIdHandler;

/**
 * A class to generate tickets from an email account.
 */
public final class ActionAccountReaderImpl extends AbstractImapAccountReader {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3952279216168621460L;

	/**
	 * The message reader.
	 */
	private ActionMessageReader messageReader;

	/**
	 * The message id handler.
	 */
	private MessageIdHandler messageIdHandler;

	/**
	 * Constructor.
	 */
	public ActionAccountReaderImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.AbstractImapAccountReader#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (isEnabled()) {
			Assert.notNull(messageReader, 
					"property messageReader of class " 
					+ this.getClass().getName() + " can not be null");
            Assert.notNull(messageIdHandler,
                    "property messageIdHandler of class "
                    + this.getClass().getName() + " can not be null");
		}
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.AccountReader#read(
	 * org.esupportail.helpdesk.services.feed.ErrorHolder)
	 */
	@Override
	public boolean read(final ErrorHolder errorHolder) {
		Ticket ticket = null;
		Store store = null;
		User sender = null;
		if (!errorHolder.hasErrors()) {
			try {
				errorHolder.addInfo("Server: " + getServer());
				errorHolder.addInfo("Folder: " + getFolder());
				errorHolder.addInfo("Account: " + getAccount());
				errorHolder.addInfo("Password: " + "xxxxxxxxx");
				Integer timeoutVal = new Integer(getTimeout());
				Properties props = System.getProperties();
				props.put("mail.imap.class", "com.sun.mail.imap.IMAPStore");
				props.put("mail.imap.connectiontimeout", timeoutVal);
				props.put("mail.imap.timeout", timeoutVal);
				Session session = Session.getInstance(props, null);
				URLName urln = new URLName(
						"imap://" + getAccount() + ":" + getPassword() + "@" + getServer());
				store = session.getStore(urln);
			} catch (NoSuchProviderException e) {
				errorHolder.addError(
						"invalid IMAP account [imap://" + getAccount() 
						+ ":xxxxxxxx@" + getServer() + "]: " + e.getMessage());
			}
		}
		if (!errorHolder.hasErrors()) {
			try {
				errorHolder.addInfo("connecting to the server...");
				store.connect();
			} catch (MessagingException e) {
				errorHolder.addError(
						"could not connect to [imap://" + getAccount() 
						+ ":xxxxxxxx@" + getServer() + "]: " + e.getMessage());
			}
		}
		Folder folder = null;
		if (!errorHolder.hasErrors()) {
			try {
				errorHolder.addInfo("opening folder [" + getFolder() + "]...");
				folder = store.getFolder(getFolder());
				if (!folder.exists()) {
					String msg = "folder does not exist. Available folders are: ";
					Folder defaultFolder = store.getDefaultFolder();
					String separator = "";
					for (Folder folder2 : defaultFolder.list()) {
						msg += separator + folder2.getFullName();
						separator = ", ";
					}
					errorHolder.addError(msg + ".");
				}
			} catch (MessagingException e) {
				errorHolder.addError("could not find the folder: " + e.getMessage());
			}
		}
		if (!errorHolder.hasErrors()) {
			try {
				folder.open(Folder.READ_WRITE);
			} catch (MessagingException e) {
				errorHolder.addError("could not open the folder: " + e.getMessage());
			}
		}
		if (!errorHolder.hasErrors()) {
			try {
				errorHolder.addInfo("expunging the folder...");
				folder.expunge();
			} catch (MessagingException e) {
				errorHolder.addError("could not expunge the folder: " + e.getMessage());
			}
		}
		int messageCount = 0;
		if (!errorHolder.hasErrors()) {
			try {
				messageCount = folder.getMessageCount();
				if (messageCount == 0) {
					errorHolder.addInfo("no message found.");
				} else {
					errorHolder.addInfo(messageCount + " message(s) found.");
				}
			} catch (MessagingException e) {
				errorHolder.addError("could not get the number of messages: " + e.getMessage());
			}
		}
		if (!errorHolder.hasErrors()) {
			for (int i = 1; i <= messageCount; i++) {
				errorHolder.addInfo("getting message #" + i + "...");
				Message message;
				try {
					message = folder.getMessage(i);
					ticket = getTicketFromHeaders(message, errorHolder);
				} catch (MessagingException e) {
					errorHolder.addError(
							"could not get message #" + i + ": " + e.getMessage());
					break;
				}
				try {
					if (message.isSet(Flags.Flag.DELETED)) {
						errorHolder.addInfo("message is marked as deleted, skiping.");
						continue;
					}
				} catch (MessagingException e) {
					errorHolder.addError(
							"could not get flag for message #" + i + ": " + e.getMessage());
					break;
				}
				ErrorHolder readErrorHolder = new ErrorHolder();
				if (ticket != null) {
					sender = messageReader.readMessage(message, ticket, readErrorHolder);
				}
				try {
					message.setFlag(Flags.Flag.DELETED, true);
				} catch (MessagingException e) {
					errorHolder.addInfo("could not mark the message as deleted: " + e.getMessage());
				} 
				errorHolder.add(readErrorHolder);
				break;
			}
		}
		if (folder != null && folder.isOpen()) {
			try {
				errorHolder.addInfo("closing folder...");
				folder.close(true/* expunge on exit */);
			} catch (MessagingException e) {
				errorHolder.addError("could not close the folder: " + e.getMessage());
			}
		}
		if (store != null && store.isConnected()) {
			try {
				errorHolder.addInfo("closing connection...");
				store.close();
			} catch (MessagingException e) {
				errorHolder.addError("could not close the connection: " + e.getMessage());
			}
		}
		if (errorHolder.hasErrors()) {
			errorHolder.addInfo(
					errorHolder.getErrorNumber() + " error(s) found for account [imap://" 
					+ getAccount() + ":xxxxxxxx@" + getServer() + "]");
		} else {
			errorHolder.addInfo(
					"no error found for account [imap://" 
					+ getAccount() + ":xxxxxxxx@" + getServer() + "]");
		}
		if (ticket == null) {
			return false;
		}
		getDomainService().ticketMonitoringSendAlerts(sender, ticket, null, false);
		return true;
	}
	
	/**
	 * @param message Email message
	 * @param header Message header to examine
	 * @return Found ticket
	 * @throws MessagingException Error processing message
	 * @throws TicketNotFoundException Error getting ticket
	 * @throws MessagingException 
	 */
	private Ticket getTicketFromHeader(
			final Message message, 
			final String header) 
	throws TicketNotFoundException, MessagingException {
		String[] headers;
		headers = message.getHeader(header);
		if (headers != null) {
			for (String headerItem : headers) {
				Long ticketId = null;
				try {
					ticketId = messageIdHandler.getTicketIdFromMessageId(headerItem);
	                return getDomainService().getTicket(ticketId);
				} catch (MessageIdException e) {
					// error
				}
			}
		}
		return null;
	}

	/**
	 * Finds ticket id in email headers and returns the found ticket.
	 * @param message Email message
	 * @param errorHolder Error holder
	 * @return Found ticket
	 * @throws MessagingException Exception
	 */
	private Ticket getTicketFromHeaders(
			final Message message, 
			final ErrorHolder errorHolder) 
	throws MessagingException {
		Ticket ticket;
		try {
			ticket = getTicketFromHeader(message, "In-Reply-To");
			if (ticket != null) {
				errorHolder.addInfo("Found ticket #" + ticket.getId() + " in In-Reply-To");
				return ticket;
			}
		} catch (TicketNotFoundException e) {
			//Ticket not found. Probably missformed header. We try to find it in References.
		}
		try {
			ticket = getTicketFromHeader(message, "References");
			if (ticket != null) {
				errorHolder.addInfo("Found ticket #" + ticket.getId() + " in References");
				return ticket;
			}
		} catch (TicketNotFoundException e) {
			//Ticket not found. Probably missformed header..
		}
        //TODO LS: Extract ticket number from subject PA: what about SPAM?
		errorHolder.addError("No ticket number found in headers... => ignore message");
		return null;
	}

	/**
	 * @return the messageReader
	 */
	protected ActionMessageReader getMessageReader() {
		return messageReader;
	}

	/**
	 * @param messageReader the messageReader to set
	 */
	public void setMessageReader(final ActionMessageReader messageReader) {
		this.messageReader = messageReader;
	}

	/**
	 * @return the messageIdHandler
	 */
	protected MessageIdHandler getMessageIdHandler() {
		return messageIdHandler;
	}

	/**
	 * @param messageIdHandler the messageIdHandler to set
	 */
	public void setMessageIdHandler(final MessageIdHandler messageIdHandler) {
		this.messageIdHandler = messageIdHandler;
	}

}
