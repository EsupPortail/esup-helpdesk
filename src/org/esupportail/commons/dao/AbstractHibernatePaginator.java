/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.dao; 

import java.util.List;

import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.web.beans.AbstractPaginator;
import org.springframework.beans.factory.InitializingBean;

/** 
 * A Hibernate paginator, i.e. that has a DaoService attribute to perform HQL queries.
 * @param <E> the class of the results
 */ 
@SuppressWarnings("serial")
public abstract class AbstractHibernatePaginator<E> extends AbstractPaginator<E> implements InitializingBean {
	
	/**
	 * The daoService.
	 */
	private HibernateDaoService daoService;
	
	/** 
	 * Constructor.
	 */ 
	public AbstractHibernatePaginator() { 
		super();
	} 
	
	/** 
	 * Constructor.
	 * @param daoService 
	 * @param pageSizeValues 
	 * @param pageSize the number of results to display on the page 
	 * @deprecated use setPageSizeValues() and setDefaultPageSize() 
	 */ 
	@Deprecated
	public AbstractHibernatePaginator(
			final HibernateDaoService daoService,
			final List<Integer> pageSizeValues,
			final int pageSize) { 
		super(pageSizeValues, pageSize);
		this.daoService = daoService;
	} 
	
	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.daoService, 
				"property daoService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @return the daoService
	 */
	protected HibernateDaoService getDaoService() {
		return daoService;
	}

	/**
	 * @param daoService the daoService to set
	 */
	public void setDaoService(final HibernateDaoService daoService) {
		this.daoService = daoService;
	}

} 

