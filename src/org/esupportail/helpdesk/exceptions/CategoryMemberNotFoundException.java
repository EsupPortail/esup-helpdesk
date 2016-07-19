/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.exceptions;

import org.esupportail.commons.exceptions.ObjectNotFoundException;

/**
 * An exception thrown when trying to retrieving non-existent category members.
 */
public class CategoryMemberNotFoundException extends ObjectNotFoundException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 2860234789754686110L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected CategoryMemberNotFoundException(final String message, final Exception cause) {
		super(message, cause);
	}

	/**
	 * Bean constructor.
	 * @param message
	 */
	public CategoryMemberNotFoundException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	public CategoryMemberNotFoundException(final Exception cause) {
		super(cause);
	}
}
