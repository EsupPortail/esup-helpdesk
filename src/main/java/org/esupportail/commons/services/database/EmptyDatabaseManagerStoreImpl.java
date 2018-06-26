/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.database;

import java.util.List;


/**
 * An empty implementation of the database manager store.
 */
public class EmptyDatabaseManagerStoreImpl extends AbstractDatabaseManagerStore {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7507435502515600809L;

	/**
	 * Bean constructor.
	 */
	public EmptyDatabaseManagerStoreImpl() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.database.AbstractDatabaseManagerStore#getDatabaseManagers()
	 */
	@Override
	protected List<DatabaseManager> getDatabaseManagers() {
		return null;
	}

}
