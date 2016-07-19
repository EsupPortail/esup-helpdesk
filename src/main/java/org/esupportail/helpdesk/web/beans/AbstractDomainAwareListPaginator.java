/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.web.beans.ListPaginator;
import org.esupportail.helpdesk.domain.DomainService;
import org.springframework.beans.factory.InitializingBean;

/** 
 * An abstract domain aware list paginator.
 * @param <E> 
 */ 
@SuppressWarnings("serial")
public abstract class AbstractDomainAwareListPaginator<E> 
extends ListPaginator<E> 
implements InitializingBean {
	
	/**
	 * The domain service.
	 */
	private DomainService domainService;
	
	/**
	 * Constructor.
	 */
	public AbstractDomainAwareListPaginator() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(domainService, 
				"property domainService of class " + this.getClass().getName() + " can not be null");
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

