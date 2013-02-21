/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.dao.DaoService;
import org.springframework.beans.factory.InitializingBean;

/**
 * An abstract ticket extractor.
 */
@SuppressWarnings("serial")
public abstract class AbstractTicketExtractor implements TicketExtractor, InitializingBean {

	/**
	 * The domain service.
	 */
	private DomainService domainService;

	/**
	 * The DAO service.
	 */
	private DaoService daoService;

	/**
	 * Bean constructor.
	 */
	public AbstractTicketExtractor() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.domainService,
				"property domainService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.daoService,
				"property daoService of class " + this.getClass().getName() + " can not be null");
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
	 * @param daoService the daoService to set
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	/**
	 * @return the daoService
	 */
	protected DaoService getDaoService() {
		return daoService;
	}

}

