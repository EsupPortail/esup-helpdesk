/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.database;

import java.io.Serializable;


/**
 * The interface of database managers.
 */
public interface DatabaseManager extends Serializable {

	/**
	 * Open the database session.
	 */
	void openSession();

	/**
	 * Close the database session, roolback the current transaction if any.
	 */
	void closeSession();
	
	/**
	 * @return true if the manager opens/closes transactions.
	 */
	boolean isTransactionnal();

	/**
	 * Begin a transaction.
	 */
	void beginTransaction();

	/**
	 * End a transaction.
	 * @param commit true to commit the current transaction, false to rollback
	 */
	void endTransaction(boolean commit);

	/**
	 * Test the database.
	 */
	void test();
	
	/**
	 * @return true if the structure of the database can be created/upgraded.
	 */
	boolean isUpgradable();
	
	/**
	 * Create the database structure.
	 */
	void create();
	
	/**
	 * Upgrade the database structures.
	 */
	void upgrade();

}
