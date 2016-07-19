/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils.lock;

/**
 * An exception thrown when locking not locked locks.
 */
public class NotLockedException extends LockException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = -6034409513718174008L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	public NotLockedException(final String message, final Exception cause) {
		super(message, cause);
	}

	/**
	 * Bean constructor.
	 * @param message
	 */
	public NotLockedException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	public NotLockedException(final Exception cause) {
		super(cause);
	}

}
