/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.application; 

import java.io.Serializable;


/**
 * the interface of the beans for versionning management.
 */
public interface VersionningService extends Serializable {

	/**
	 * Initialize the database.
	 */
	void initDatabase();
	
	/**
	 * check the database version, silently upgrade if possible.
	 * @param throwException 
	 * @param printLatestVersion 
	 * @throws VersionException 
	 */
	void checkVersion(
			final boolean throwException,
			final boolean printLatestVersion) throws VersionException;
	
	/**
	 * Upgrade the database.
	 * @return true if the method should be called again, false otherwise.
	 */
	boolean upgradeDatabase();
	
}
