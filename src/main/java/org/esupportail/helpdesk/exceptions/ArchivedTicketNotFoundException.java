/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.exceptions;

import org.esupportail.commons.exceptions.ObjectNotFoundException;

/**
 * An exception thrown when trying to retrieve non-existent archived tickets.
 */
public class ArchivedTicketNotFoundException extends ObjectNotFoundException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 1325644113185030813L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected ArchivedTicketNotFoundException(final String message, final Exception cause) {
		super(message, cause);
	}
	
	/**
	 * Bean constructor.
	 * @param message
	 */
	public ArchivedTicketNotFoundException(final String message) {
		super(message);
	}
	
	/**
	 * Bean constructor.
	 * @param cause
	 */
	public ArchivedTicketNotFoundException(final Exception cause) {
		super(cause);
	}
}
