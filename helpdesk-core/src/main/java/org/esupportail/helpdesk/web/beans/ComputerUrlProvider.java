/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.springframework.beans.factory.InitializingBean;

/** 
 * A provider for computer URLs.
 */ 
public class ComputerUrlProvider extends HashMap<Ticket, String> implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3155752577633677969L;
	
	/**
	 * The domain service.
	 */
	private DomainService domainService;
	
	/**
	 * Bean constructor.
	 */
	public ComputerUrlProvider() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(domainService, 
				"property domainService of class " + getClass().getSimpleName() 
				+ " should not be null");
	}
	
	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object ticket) {
		if (ticket == null) {
			return null;
		}
		return getDomainService().getTicketComputerUrl((Ticket) ticket);
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

}

