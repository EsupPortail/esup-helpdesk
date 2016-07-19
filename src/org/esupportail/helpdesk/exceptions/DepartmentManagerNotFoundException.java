/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.exceptions;

import org.esupportail.commons.exceptions.ObjectNotFoundException;

/**
 * An exception thrown when trying to retrieving non-existent department managers.
 */
public class DepartmentManagerNotFoundException extends ObjectNotFoundException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 3322685973392693843L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected DepartmentManagerNotFoundException(final String message, final Exception cause) {
		super(message, cause);
	}

	/**
	 * Bean constructor.
	 * @param message
	 */
	public DepartmentManagerNotFoundException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	public DepartmentManagerNotFoundException(final Exception cause) {
		super(cause);
	}
}
