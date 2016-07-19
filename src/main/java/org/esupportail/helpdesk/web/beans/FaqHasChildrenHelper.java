/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.springframework.beans.factory.InitializingBean;

/** 
 * A helper to know whether FAQs have children or not.
 */ 
public class FaqHasChildrenHelper extends HashMap<Faq, Boolean> implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3231285048430835603L;
	
	/**
	 * The domain service.
	 */
	private DomainService domainService;
	
	/**
	 * Bean constructor.
	 */
	public FaqHasChildrenHelper() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(domainService, 
				"property domainService of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	@RequestCache
	public Boolean get(final Object faq) {
		return domainService.hasSubFaqs((Faq) faq);
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

