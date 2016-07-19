/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 * An abstract DAO implementation (obsolete).
 * @deprecated
 */
@Deprecated
public abstract class AbstractHibernateDaoService 
implements HibernateDaoService {

	/**
	 * Bean constructor.
	 */
	protected AbstractHibernateDaoService() {
		throw exception();
	}

	/**
	 * @return an exception
	 */
	private UnsupportedOperationException exception() {
		return new UnsupportedOperationException("class " + getClass() + " is obsolete, "
				+ "use AbstractSimpleHibernateDaoService or "
				+ "AbstractJdbcJndiHibernateDaoService instead");
	}
	
	/**
	 * @see org.esupportail.commons.dao.HibernateDaoService#getQuery(java.lang.String)
	 */
	@Override
	public Query getQuery(
			@SuppressWarnings("unused") final String hqlQuery) {
		throw exception();
	}
	
	/**
	 * Count entries of the database.
	 * @param countQuery the query
	 * @return an integer.
	 */
	protected int getQueryIntResult(
			@SuppressWarnings("unused") final String countQuery) {
		throw exception();
	}
	
	/**
	 * do updates in the database.
	 * @param queryString
	 */
	protected void executeUpdate(
			@SuppressWarnings("unused") final String queryString) {
		throw exception();
	}
	
	/**
	 * @see org.esupportail.commons.dao.HibernateDaoService#getSqlQuery(java.lang.String)
	 */
	@Override
	public SQLQuery getSqlQuery(
			@SuppressWarnings("unused") final String sqlQuery) {
		throw exception();
	}
	
	//////////////////////////////////////////////////////////////
	// misc
	//////////////////////////////////////////////////////////////

	/**
	 * Add an object into the database.
	 * @param object 
	 */
	protected void addObject(
			@SuppressWarnings("unused") final Object object) {
		throw exception();
	}

	/**
	 * Update an object in the database.
	 * @param object 
	 */
	protected void updateObject(
			@SuppressWarnings("unused") final Object object) {
		throw exception();
	}

	/**
	 * Delete an object from the database.
	 * @param object 
	 */
	protected void deleteObject(
			@SuppressWarnings("unused") final Object object) {
		throw exception();
	}

	/**
	 * Delete a list of objects from the database.
	 * @param objects 
	 */
	protected void deleteObjects(
			@SuppressWarnings({ "rawtypes", "unused" }) final List objects) {
		throw exception();
	}

}
