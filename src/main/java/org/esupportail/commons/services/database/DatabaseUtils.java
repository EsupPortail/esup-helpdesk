/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.database;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;

/**
 * A simple utility class to create/update the Hibernate database structures.
 */
public class DatabaseUtils {

	/**
	 * The name of the bean that holds the database manager store.
	 */
	public static final String DATABASE_MANAGER_STORE = "databaseManagerStore"; 
	
	/**
	 * A logger.
	 */
	private static final Logger LOG = new LoggerImpl(DatabaseUtils.class);
	
	/**
	 * A thread local to store the state.
	 */
	private static final ThreadLocal<Boolean> STATE = new ThreadLocal<Boolean>();
	
	/**
	 * The database manager store.
	 */
	private static DatabaseManagerStore databaseManagerStore;
	
    /**
     * Private constructor, no instantiation.
     */
    private DatabaseUtils() {
    	throw new UnsupportedOperationException();
    }
    
    /**
     * @return the database manager store.
     */
    private static DatabaseManagerStore getDatabaseManagerStore() {
    	if (databaseManagerStore == null) {
    		databaseManagerStore = (DatabaseManagerStore) BeanUtils.getBean(DATABASE_MANAGER_STORE);
    	}
    	return databaseManagerStore;
    }
    
    /**
     * @return true if the database connection have already been opened.
     */
    private static boolean isOpened() {
    	Boolean opened = STATE.get();
    	return opened != null;
    }
    
    /**
     * Set the database connection as opened/closed at thread-level.
     * @param opened 
     */
    private static void setOpened(final boolean opened) {
    	if (opened) {
    		STATE.set(Boolean.TRUE);
    	} else {
    		STATE.set(null);
    	}
    }
    
	/**
	 * Open the database connections (web mode).
	 */
	public static void open() {
		if (!isOpened()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("OPEN");
			}
			getDatabaseManagerStore().open();
			setOpened(true);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("OPEN ***** already opened!");
			}
		}
	}

	/**
	 * Begin transactions (for transactionnal managers only).
	 */
	public static void begin() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("BEGIN");
		}
		getDatabaseManagerStore().begin();
	}

	/**
	 * End current transactions.
	 * @param commit true to commit the current transaction, false to rollback
	 */
	private static void end(final boolean commit) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("END(" + commit + ")");
		}
		getDatabaseManagerStore().end(commit);
	}

	/**
	 * Commit current transactions.
	 */
	public static void commit() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("COMMIT");
		}
		end(true);
	}

	/**
	 * Rollback current transactions.
	 */
	public static void rollback() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ROLLBACK");
		}
		end(false);
	}

	/**
	 * Close the connections, rollback transactions if any.
	 */
	public static void close() {
		if (isOpened()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("CLOSE");
			}
			getDatabaseManagerStore().close();
			setOpened(false);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("CLOSE ***** closed!");
			}
		}
	}

	/**
	 * Test the database.
	 * @throws ConfigException 
	 */
	public static void test() throws ConfigException {
		LOG.info("testing the databases, please wait...");
		getDatabaseManagerStore().test();
		LOG.info("done.");
	}
	
	/**
	 * Create the database.
	 * @throws ConfigException 
	 */
	public static void create() throws ConfigException {
		LOG.info("creating the databases, please wait...");
		LOG.info("this operation may take a few minutes, do not interrupt!");
		getDatabaseManagerStore().create();
		LOG.info("done.");
	}
	
	/**
	 * Update the database.
	 * @throws ConfigException 
	 */
	public static void update() throws ConfigException {
		LOG.info("updating the database, please wait...");
		LOG.info("this operation may take from a few seconds to a couple of hours for huge migrations.");
		LOG.info("do not interrupt!");
		getDatabaseManagerStore().update();
		LOG.info("done.");
	}

}
