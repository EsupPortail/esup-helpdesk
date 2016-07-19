/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.database;

import java.io.Serializable;


/**
 * The interface of the database manager stores.
 */
public interface DatabaseManagerStore extends Serializable {
	
	/**
	 * Open the database connections.
	 * @throws DatabaseException 
	 */
	void open() throws DatabaseException;

	/**
	 * Begin a transaction for all the database managers.
	 * @throws DatabaseException 
	 */
	void begin() throws DatabaseException;

	/**
	 * End the current transaction.
	 * @param commit true to commit the current transaction, false to rollback
	 * @throws DatabaseException 
	 */
	void end(final boolean commit) throws DatabaseException;

	/**
	 * Close the database connections, rollback the current transaction if any.
	 * @throws DatabaseException 
	 */
	void close() throws DatabaseException;

	/**
	 * Test the database connections.
	 * @throws DatabaseException 
	 */
	void test() throws DatabaseException;
	
	/**
	 * Create the database structures.
	 * @throws DatabaseException 
	 */
	void create() throws DatabaseException;
	
	/**
	 * Update the database structures.
	 * @throws DatabaseException 
	 */
	void update() throws DatabaseException;

}
