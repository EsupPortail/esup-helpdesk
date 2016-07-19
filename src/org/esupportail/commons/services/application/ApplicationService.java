/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.application;

import java.io.Serializable;


/** 
 * The interface of application services, which should inform about 
 * the current application (version, name...).
 */
public interface ApplicationService extends Serializable {
	
	/**
	 * @return the name of the application.
	 */
	String getName();
	
	/**
	 * @return the version.
	 */
	Version getVersion();

	/**
	 * @return the latest version.
	 */
	Version getLatestVersion();

	/**
	 * @return true for a quick start installation, false otherwise.
	 */
	boolean isQuickStart();

	/**
	 * @return the deploy type.
	 */
	String getDeployType();

	/**
	 * @return the copyright of the application.
	 */
	String getCopyright();
	
	/**
	 * @return the vendor of the application.
	 */
	String getVendor();
	
	/**
	 * @return the database driver
	 */
	String getDatabaseDriver();

	/**
	 * @return the database dialect
	 */
	String getDatabaseDialect();

	/**
	 * @return true when using JNDI
	 */
	boolean isDatabaseUseJndi();

}
