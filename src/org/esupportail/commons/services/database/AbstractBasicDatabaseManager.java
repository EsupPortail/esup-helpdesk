/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.database;

/**
 * An abstract class for non updatable database managers.
 */
@SuppressWarnings("serial")
public abstract class AbstractBasicDatabaseManager implements DatabaseManager {

	/**
	 * @see org.esupportail.commons.services.database.DatabaseManager#isUpgradable()
	 */
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	/**
	 * @see org.esupportail.commons.services.database.DatabaseManager#create()
	 */
	@Override
	public void create() {
		throw new UnsupportedOperationException(getClass().getCanonicalName() + ".create()");
	}
	
	/**
	 * @see org.esupportail.commons.services.database.DatabaseManager#upgrade()
	 */
	@Override
	public void upgrade() {
		throw new UnsupportedOperationException(getClass().getCanonicalName() + ".upgrade()");
	}	

}
