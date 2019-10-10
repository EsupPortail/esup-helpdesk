/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed.imap;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.AuthenticationFailedException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.exceptions.CategoryNotFoundException;
import org.esupportail.helpdesk.exceptions.DepartmentNotFoundException;
import org.esupportail.helpdesk.services.feed.ErrorHolder;
import org.esupportail.helpdesk.services.feed.imap.spam.SpamFilter;

/**
 * A class to generate tickets from an email account.
 */
public final class TicketAccountReaderImpl extends AbstractImapAccountReader {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8768505047925270781L;

	/**
	 * The message reader.
	 */
	private TicketMessageReader messageReader;

	/** 
	 * The id of the creation department. 
	 */
	private Long creationDepartmentId;
	
	/** 
	 * The id of the target category.
	 */
	private Long categoryId;
	
	/**
	 * The spam filter.
	 */
	private SpamFilter spamFilter;

	/**
	 * True to delete spam, false to put the spam into the spam category.
	 */
	private boolean deleteSpam;
	
	/** 
	 * The id of the spam category.
	 */
	private Long spamCategoryId;
	
	/**
	 * The email address.
	 */
	private String address;

	/**
	 * Constructor.
	 */
	public TicketAccountReaderImpl() {
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
			Assert.notNull(categoryId, 
					"property categoryId of class " 
					+ this.getClass().getName() + " can not be null");
			Assert.notNull(spamFilter, 
					"property spamFilter of class " + this.getClass().getName() 
					+ " can not be null (you should probaly set property "
					+ "${feed.spamFilter} to nullSpamFilter instead of null)");
			if (!spamFilter.filters() || deleteSpam) {
				spamCategoryId = null;
			} else {
				Assert.notNull(spamCategoryId, "property spamCategoryId of class " 
						+ this.getClass().getName() + " can not be null");
			}
			Assert.notNull(address, 
					"property address of class " + this.getClass().getName() + " can not be null");
		}
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.AccountReader#read(
	 * org.esupportail.helpdesk.services.feed.ErrorHolder)
	 */
	@Override
	public boolean read(final ErrorHolder errorHolder) {
		Ticket ticket = null;
		Category category = null;
		if (!errorHolder.hasErrors()) {
			try {
				category = getDomainService().getCategory(categoryId);
				errorHolder.addInfo("Category: " + category.getLabel());
			} catch (CategoryNotFoundException e) {
				errorHolder.addError(e.getMessage());
			}
		}
		Department creationDepartment = null;
		if (!errorHolder.hasErrors()) {
			try {
				creationDepartment = category.getDepartment();
				if (creationDepartmentId != null) {
					creationDepartment = getDomainService().getDepartment(creationDepartmentId);
				}
				errorHolder.addInfo("Department: " + creationDepartment.getLabel());
			} catch (DepartmentNotFoundException e) {
				errorHolder.addError(e.getMessage());
			}
		}
		Category spamCategory = null;
		if (spamCategoryId != null && !errorHolder.hasErrors()) { 
			try {
				spamCategory = getDomainService().getCategory(spamCategoryId);
				errorHolder.addInfo("SpamCategory: " + spamCategory.getLabel());
			} catch (CategoryNotFoundException e) {
				errorHolder.addError(e.getMessage());
			}
		}
		Store store = null;
		if (!errorHolder.hasErrors()) {
			try {
				errorHolder.addInfo("Server: " + getServer());
				errorHolder.addInfo("Folder: " + getFolder());
				errorHolder.addInfo("Account: " + getAccount());
				errorHolder.addInfo("Password: " + "xxxxxxxxx");
				Integer timeoutVal = new Integer(getTimeout());
				Properties props = System.getProperties();
				props.put("mail.imaps.connectiontimeout", timeoutVal);
				props.put("mail.imaps.timeout", timeoutVal);
				Session session = Session.getInstance(props, null);
				store = session.getStore("imaps");
			} catch (NoSuchProviderException e) {
				errorHolder.addError(
						"invalid IMAPS account [imaps://" + getAccount()
						+ ":xxxxxxxx@" + getServer() + "]: " + e.getMessage());
			}
		}
		if (!errorHolder.hasErrors()) {
			try {
				errorHolder.addInfo("connecting to the imaps server...");
				store.connect(getServer(),getAccount(),getPassword());
			} catch (MessagingException e) {
				errorHolder.addError(
						"could not connect to [imaps://" + getAccount() 
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
				ticket = messageReader.read(
						message, address, creationDepartment, 
						category, spamFilter, deleteSpam, spamCategory,
						readErrorHolder);
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
					errorHolder.getErrorNumber() + " error(s) found for account [imaps://" 
					+ getAccount() + ":xxxxxxxx@" + getServer() + "]");
		} else {
			errorHolder.addInfo(
					"no error found for account [imaps://" 
					+ getAccount() + ":xxxxxxxx@" + getServer() + "]");
		}
		if (ticket == null) {
			return false;
		}
		getDomainService().ticketMonitoringSendAlerts(null, ticket, null, false);
		return true;
	}

	/**
	 * @return the creationDepartmentId
	 */
	protected Long getCreationDepartmentId() {
		return creationDepartmentId;
	}

	/**
	 * @param creationDepartmentId the creationDepartmentId to set
	 */
	public void setCreationDepartmentId(final Long creationDepartmentId) {
		this.creationDepartmentId = creationDepartmentId;
	}

	/**
	 * @return the categoryId
	 */
	protected Long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(final Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the messageReader
	 */
	protected TicketMessageReader getMessageReader() {
		return messageReader;
	}

	/**
	 * @param messageReader the messageReader to set
	 */
	public void setMessageReader(final TicketMessageReader messageReader) {
		this.messageReader = messageReader;
	}

	/**
	 * @return the address
	 */
	protected String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(final String address) {
		this.address = StringUtils.nullIfEmpty(address);
	}

	/**
	 * @return the spamCategoryId
	 */
	protected Long getSpamCategoryId() {
		return spamCategoryId;
	}

	/**
	 * @param spamCategoryId the spamCategoryId to set
	 */
	public void setSpamCategoryId(final Long spamCategoryId) {
		this.spamCategoryId = spamCategoryId;
	}

	/**
	 * @return the deleteSpam
	 */
	protected boolean isDeleteSpam() {
		return deleteSpam;
	}

	/**
	 * @param deleteSpam the deleteSpam to set
	 */
	public void setDeleteSpam(final boolean deleteSpam) {
		this.deleteSpam = deleteSpam;
	}

	/**
	 * @return the spamFilter
	 */
	protected SpamFilter getSpamFilter() {
		return spamFilter;
	}

	/**
	 * @param spamFilter the spamFilter to set
	 */
	public void setSpamFilter(final SpamFilter spamFilter) {
		this.spamFilter = spamFilter;
	}

}
