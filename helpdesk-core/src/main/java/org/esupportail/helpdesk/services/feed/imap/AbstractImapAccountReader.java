/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed.imap;

import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.services.feed.AbstractAccountReader;
import org.springframework.beans.factory.InitializingBean;

/**
 * A class to generate tickets from an email account.
 */
@SuppressWarnings("serial")
public abstract class AbstractImapAccountReader extends AbstractAccountReader implements InitializingBean {

	/**
	 * The default timeout.
	 */
	private static final int DEFAULT_TIMEOUT = 10; 
	
	/**
	 * The default folder.
	 */
	private static final String DEFAULT_FOLDER_NAME = "INBOX"; 
	
	/**
	 * The domain service.
	 */
	private DomainService domainService;

	/** 
	 * The IMAP server to connect to. 
	 */
	private String server;

	/** 
	 * The folder to read. 
	 */
	private String folderName = DEFAULT_FOLDER_NAME;

	/** 
	 * The IMAP account.
	 */
	private String account;
	
	/** 
	 * The IMAP password. 
	 */
	private String password;
	
	/**
	 * The timeout in seconds.
	 */
	private int timeout = DEFAULT_TIMEOUT;

	/**
	 * Constructor.
	 */
	public AbstractImapAccountReader() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		if (isEnabled()) {
			Assert.notNull(domainService, 
					"property domainService of class " + this.getClass().getName() + " can not be null");
			Assert.notNull(server, 
					"property server of class " + this.getClass().getName() + " can not be null");
			Assert.notNull(folderName, 
					"property folder of class " + this.getClass().getName() + " can not be null");
			Assert.notNull(account, 
					"property account of class " + this.getClass().getName() + " can not be null");
			Assert.notNull(password, 
					"property password of class " + this.getClass().getName() + " can not be null");
		}
	}

	/**
	 * @return the password
	 */
	protected String getPassword() {
		return password;
	}

	/**
	 * @return the server
	 */
	protected String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(final String server) {
		this.server = StringUtils.nullIfEmpty(server);
	}

	/**
	 * @return the account
	 */
	protected String getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(final String account) {
		this.account = StringUtils.nullIfEmpty(account);
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		this.password = StringUtils.nullIfEmpty(password);
	}

	/**
	 * @return the domainService
	 */
	protected DomainService getDomainService() {
		return domainService;
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @return the timeout
	 */
	protected int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the folderName
	 */
	protected String getFolder() {
		return folderName;
	}

	/**
	 * @param folder the folder to set
	 */
	public void setFolder(final String folder) {
		this.folderName = StringUtils.nullIfEmpty(folder);
	}

}
