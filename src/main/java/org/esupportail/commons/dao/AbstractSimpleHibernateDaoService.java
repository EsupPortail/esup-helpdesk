/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.dao;

import org.esupportail.commons.utils.Assert;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * A simple abstract DAO implementation.
 */
public abstract class AbstractSimpleHibernateDaoService 
extends AbstractGenericHibernateDaoService {

	/**
	 * The Hibernate template. 
	 */
	private HibernateTemplate hibernateTemplate;

	/**
	 * Bean constructor.
	 */
	protected AbstractSimpleHibernateDaoService() {
		super();
	}

	/**
	 * @see org.esupportail.commons.dao.AbstractGenericHibernateDaoService#initDao()
	 */
	@Override
	public void initDao() {
		Assert.notNull(hibernateTemplate, 
				"property hibernateTemplate of class " + getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.commons.dao.AbstractGenericHibernateDaoService#getHibernateTemplate()
	 */
	@Override
	protected HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * @param hibernateTemplate the hibernateTemplate to set
	 */
	public void setHibernateTemplate(final HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
