/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.beans;

import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.utils.Assert;

/**
 * An abstract class inherited by all the beans for them to get a reference to the domain service.
 */
@SuppressWarnings("serial")
public abstract class AbstractApplicationAwareBean extends AbstractI18nAwareBean {

	/**
	 * see {@link ApplicationService}.
	 */
	private ApplicationService applicationService;

	/**
	 * Constructor.
	 */
	protected AbstractApplicationAwareBean() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.applicationService, 
				"property applicationService of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @return the applicationService
	 */
	public ApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(final ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

}
