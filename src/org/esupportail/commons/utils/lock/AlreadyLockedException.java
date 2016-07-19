/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils.lock;

/**
 * An exception thrown when locking an already locked lock.
 */
public class AlreadyLockedException extends LockException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 8697744433598798124L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	public AlreadyLockedException(final String message, final Exception cause) {
		super(message, cause);
	}

	/**
	 * Bean constructor.
	 * @param message
	 */
	public AlreadyLockedException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	public AlreadyLockedException(final Exception cause) {
		super(cause);
	}

}
