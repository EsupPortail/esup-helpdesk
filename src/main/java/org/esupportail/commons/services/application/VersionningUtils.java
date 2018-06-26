/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.application; 

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.database.DatabaseUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;

/**
 * Utilities for versionning management.
 */
public class VersionningUtils {

	/**
	 * The initial version.
	 */
	public static final String VERSION_0 = "0.0.0";
	
	/**
	 * The name of the bean for the versionning service.
	 */
	private static final String VERSIONNING_SERVICE_BEAN = "versionningService";
	
	/**
	 * A logger.
	 */
	private static final Logger LOG = new LoggerImpl(VersionningUtils.class);
	
	/**
	 * Constructor.
	 */
	protected VersionningUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Print the syntax and exit.
	 */
	private static void syntax() {
		throw new IllegalArgumentException(
				"syntax: " + VersionningUtils.class.getSimpleName() + " <options>"
				+ "\nwhere option can be:"
				+ "\n- check-version: initialize the database"
				+ "\n- init: initialize the database"
				+ "\n- upgrade: upgrade the database");
	}
	
	/**
	 * @return the versionning service.
	 */
	public static VersionningService createVersionningService() {
		return (VersionningService) BeanUtils.getBean(VERSIONNING_SERVICE_BEAN);
	}

	/**
	 * @param t
	 * @throws ConfigException
	 */
	private static void closeAndRethrowException(final Throwable t) throws ConfigException {
		ConfigException ex = null;
		if (t instanceof ConfigException) {
			ex = (ConfigException) t;
		} else {
			ex = new ConfigException(t);
		}
		DatabaseUtils.close();
		throw ex;
	}
	
	/**
	 * Initialize the database.
	 */
	private static void doInitDatabase() {
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			createVersionningService().initDatabase();
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			closeAndRethrowException(t);
		}
		doUpgradeDatabase();
	}
	
	/**
	 * check the database version, silently upgrade if possible.
	 * @param throwException 
	 * @param printLatestVersion 
	 * @throws ConfigException 
	 */
	public static void checkVersion(
			final boolean throwException,
			final boolean printLatestVersion) throws ConfigException {
		createVersionningService().checkVersion(throwException, printLatestVersion);
	}
	
	/**
	 * check the database version, silently upgrade if possible.
	 * @param throwException 
	 * @param printLatestVersion 
	 * @throws ConfigException 
	 */
	private static void doCheckVersion(
			final boolean throwException,
			final boolean printLatestVersion) throws ConfigException {
		try {
			DatabaseUtils.open();
			DatabaseUtils.begin();
			checkVersion(throwException, printLatestVersion);
			DatabaseUtils.commit();
			DatabaseUtils.close();
		} catch (Throwable t) {
			closeAndRethrowException(t);
		}
	}
	
	/**
	 * Upgrade the database.
	 */
	private static void doUpgradeDatabase() {
		while (true) {
			try {
				DatabaseUtils.open();
				DatabaseUtils.begin();
				boolean recall = createVersionningService().upgradeDatabase();
				DatabaseUtils.commit();
				DatabaseUtils.close();
				if (!recall) {
					return;
				}
			} catch (Throwable t) {
				closeAndRethrowException(t);
			}
		}
	}
	
	/**
	 * Dispatch dependaing on the arguments.
	 * @param args
	 */
	protected static void dispatch(final String[] args) {
		switch (args.length) {
		case 0:
			syntax();
			break;
		case 1:
			if ("init".equals(args[0])) {
				doInitDatabase();
			} else if ("upgrade".equals(args[0])) {
				doUpgradeDatabase();
			} else if ("check-version".equals(args[0])) {
				doCheckVersion(false, true);
			} else {
				syntax();
			}
			break;
		default:
			syntax();
			break;
		}
	}

	/**
	 * The main method, called by ant.
	 * @param args
	 */
	public static void main(final String[] args) {
		try {
			ApplicationService applicationService = ApplicationUtils.createApplicationService();
			LOG.info(applicationService.getName() + " v" + applicationService.getVersion());
			dispatch(args);
		} catch (Throwable t) {
			ExceptionUtils.catchException(t);
		}
	}

}
