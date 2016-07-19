/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.computerUrl;

import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.Assert;

/**
 * A basic implementation of ComputerUrlBuilder that always returns null.
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractComputerUrlBuilder implements ComputerUrlBuilder {

	/**
	 * The i18n service.
	 */
	private I18nService i18nService; 
	
	/**
	 * Constructor.
	 */
	public AbstractComputerUrlBuilder() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		Assert.notNull(this.i18nService, 
				"property i18nService of class " + this.getClass().getName() + " can not be null");
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
