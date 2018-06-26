/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.exceptions;

/**
 * An exception thrown when failing to retrieve a user from a datasource.
 */
public class UserNotFoundException extends ObjectNotFoundException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 792347220128301517L;
	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected UserNotFoundException(final String message, final Exception cause) {
		super(message, cause);
	}
	/**
	 * Bean constructor.
	 * @param message
	 */
	public UserNotFoundException(final String message) {
		super(message);
	}
	/**
	 * Bean constructor.
	 * @param cause
	 */
	public UserNotFoundException(final Exception cause) {
		super(cause);
	}
}
