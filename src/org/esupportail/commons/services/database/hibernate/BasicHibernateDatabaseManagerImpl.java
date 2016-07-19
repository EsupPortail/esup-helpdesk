/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.database.hibernate;


/**
 * An abstract class for non updatable database managers.
 */
public class BasicHibernateDatabaseManagerImpl 
extends AbstractHibernateDatabaseManagerImpl {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3991206876392195818L;

	/**
	 * true for a transactionnal manager.
	 */
	private boolean transactionnal;
	
	/**
	 * Bean constructor.
	 */
	public BasicHibernateDatabaseManagerImpl() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.database.DatabaseManager#isTransactionnal()
	 */
	@Override
	public boolean isTransactionnal() {
		return transactionnal;
	}

	/**
	 * @param transactionnal the transactionnal to set
	 */
	public void setTransactionnal(final boolean transactionnal) {
		this.transactionnal = transactionnal;
	}

}
