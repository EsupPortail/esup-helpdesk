/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.exceptions;

import org.esupportail.commons.exceptions.ObjectNotFoundException;

/**
 * An exception thrown when trying to retrieving non-existent faq links.
 */
public class FaqLinkNotFoundException extends ObjectNotFoundException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = -3954066715083770980L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected FaqLinkNotFoundException(final String message, final Exception cause) {
		super(message, cause);
	}
	
	/**
	 * Bean constructor.
	 * @param message
	 */
	public FaqLinkNotFoundException(final String message) {
		super(message);
	}
	
	/**
	 * Bean constructor.
	 * @param cause
	 */
	public FaqLinkNotFoundException(final Exception cause) {
		super(cause);
	}
}
