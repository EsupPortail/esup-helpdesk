/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.exceptions;

/**
 * An exception thrown when failing to retrieve a group from a datasource.
 */
public class GroupNotFoundException extends ObjectNotFoundException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 4191757183244051147L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected GroupNotFoundException(final String message, final Exception cause) {
		super(message, cause);
	}
	/**
	 * Bean constructor.
	 * @param message
	 */
	public GroupNotFoundException(final String message) {
		super(message);
	}
	/**
	 * Bean constructor.
	 * @param cause
	 */
	public GroupNotFoundException(final Exception cause) {
		super(cause);
	}
}
