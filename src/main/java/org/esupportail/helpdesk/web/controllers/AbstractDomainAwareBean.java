/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.net.InetAddress;
import java.util.Locale;

import org.esupportail.commons.beans.AbstractApplicationAwareBean;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.userManagement.UserStore;
import org.esupportail.helpdesk.services.urlGeneration.UrlBuilder;

/**
 * An abstract class inherited by all the beans for them to get:
 * - the domain service (domainService).
 * - the application service (applicationService).
 * - the i18n service (i18nService).
 */
@SuppressWarnings("serial")
public abstract class AbstractDomainAwareBean extends AbstractApplicationAwareBean {

//	/**
//	 * A logger.
//	 */
//	private final Logger logger = new LoggerImpl(this.getClass());

	/**
	 * see {@link DomainService}.
	 */
	private DomainService domainService;

	/**
	 * The URL builder.
	 */
	private UrlBuilder urlBuilder;

	/**
	 * Constructor.
	 */
	protected AbstractDomainAwareBean() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public final void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.domainService,
				"property domainService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(urlBuilder,
				"property urlBuilder of class " + this.getClass().getName()
				+ " can not be null");
		afterPropertiesSetInternal();
	}

	/**
	 * This method is run once the object has been initialized, just before reset().
	 */
	protected void afterPropertiesSetInternal() {
		// override this method
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @return the domainService
	 */
	protected DomainService getDomainService() {
		return domainService;
	}

	/**
	 * @return the current user.
	 */
	protected User getCurrentUser() {
		return null;
	}

	/**
	 * @return the client.
	 */
	protected InetAddress getClient() {
		return null;
	}

	/**
	 * @return the current user's locale.
	 */
	@Override
	public Locale getCurrentUserLocale() {
		return getUserStore().getUserLocale(getCurrentUser());
	}

	/**
	 * @return the userStore
	 */
	protected UserStore getUserStore() {
		return getDomainService().getUserStore();
	}

	/**
	 * @return the urlBuilder
	 */
	protected UrlBuilder getUrlBuilder() {
		return urlBuilder;
	}

	/**
	 * @param urlBuilder the urlBuilder to set
	 */
	public void setUrlBuilder(final UrlBuilder urlBuilder) {
		this.urlBuilder = urlBuilder;
	}

}
