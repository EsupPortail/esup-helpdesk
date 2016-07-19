/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.database;

import java.util.List;

import org.esupportail.commons.utils.Assert;
import org.springframework.beans.factory.InitializingBean;

/**
 * A basic implementation of the database manager store.
 */
public class BasicDatabaseManagerStoreImpl extends AbstractDatabaseManagerStore implements InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3937255340522415325L;

	/**
	 * The database managers.
	 */
	private List<DatabaseManager> databaseManagers;
	
	/**
	 * Bean constructor.
	 */
	public BasicDatabaseManagerStoreImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(databaseManagers, 
				"property [databaseManagers] of class [" 
				+ getClass().getName() + "] can not be null");
	}

	/**
	 * @param databaseManagers the databaseManagers to set
	 */
	public void setDatabaseManagers(final List<DatabaseManager> databaseManagers) {
		this.databaseManagers = databaseManagers;
	}

	/**
	 * @see org.esupportail.commons.services.database.AbstractDatabaseManagerStore#getDatabaseManagers()
	 */
	@Override
	protected List<DatabaseManager> getDatabaseManagers() {
		return databaseManagers;
	}

}
