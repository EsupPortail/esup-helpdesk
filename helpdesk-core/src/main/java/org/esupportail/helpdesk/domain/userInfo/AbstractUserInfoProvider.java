/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userInfo;

import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.DomainService;
import org.springframework.beans.factory.InitializingBean;

/**
 * An abstract domain-aware UserInfoProvider.
 */
@SuppressWarnings("serial")
public abstract class AbstractUserInfoProvider implements UserInfoProvider, InitializingBean {

	/**
	 * The domain service.
	 */
	private DomainService domainService;

	/**
	 * The i18n service.
	 */
	private I18nService i18nService;

	/**
	 * Constructor.
	 */
	public AbstractUserInfoProvider() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.i18nService,
				"property i18nService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @return the domainService
	 */
	protected DomainService getDomainService() {
		Assert.notNull(
				domainService,
				"please call " + getClass() + ".setDomainService() before any other method");
		return domainService;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userInfo.UserInfoProvider#setDomainService(
	 * org.esupportail.helpdesk.domain.DomainService)
	 */
	@Override
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @return the i18nService
	 */
	protected I18nService getI18nService() {
		return i18nService;
	}

	/**
	 * @param service the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}

}
