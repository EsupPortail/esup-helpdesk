/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.application; 


/**
 * An exception thrown when the database is not initialized.
 */
public class UninitializedDatabaseException extends VersionException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 8367761009673327628L;

	/**
	 * Bean constructor.
	 */
	public UninitializedDatabaseException() {
		super();
	}

	/**
	 * @param message
	 */
	public UninitializedDatabaseException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UninitializedDatabaseException(final Exception cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UninitializedDatabaseException(final String message, final Exception cause) {
		super(message, cause);
	}

}
