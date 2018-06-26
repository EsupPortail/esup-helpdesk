/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.database; 

import org.esupportail.commons.exceptions.EsupException;

/**
 * A class for database exceptions.
 */
public class DatabaseException extends EsupException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 8197090501242229324L;

	/**
	 * @param message
	 */
	public DatabaseException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DatabaseException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DatabaseException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
