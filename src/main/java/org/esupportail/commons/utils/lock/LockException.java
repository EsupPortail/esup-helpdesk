/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils.lock;

import org.esupportail.commons.exceptions.EsupException;


/**
 * An exception thrown when locking error occur.
 */
public class LockException extends EsupException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = -3726605483178308720L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	public LockException(final String message, final Exception cause) {
		super(message, cause);
	}

	/**
	 * Bean constructor.
	 * @param message
	 */
	public LockException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	public LockException(final Exception cause) {
		super(cause);
	}

}
