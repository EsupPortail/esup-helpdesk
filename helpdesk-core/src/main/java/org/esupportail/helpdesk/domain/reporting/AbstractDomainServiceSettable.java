/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.reporting;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.DomainService;

/**
 * An abstract class to inherit from beans that need a domain service.
 */
public class AbstractDomainServiceSettable implements DomainServiceSettable {

	/**
	 * {@link DomainService}.
	 */
	private DomainService domainService;

	/**
	 * Bean constructor.
	 */
	protected AbstractDomainServiceSettable() {
		super();
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
	 * @see org.esupportail.helpdesk.domain.reporting.DomainServiceSettable#setDomainService(
	 * org.esupportail.helpdesk.domain.DomainService)
	 */
	@Override
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

}
