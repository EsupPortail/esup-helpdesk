/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import org.esupportail.commons.exceptions.EsupException;

/**
 * A class for exceptions while parsing or rendering custom tags.
 */
public class TagException extends EsupException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 3760920420101524420L;

	/**
	 * Bean constructor.
	 */
	public TagException() {
		super();
	}

	/**
	 * Bean constructor.
	 * @param message
	 */
	public TagException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	public TagException(final Exception cause) {
		super(cause);
	}

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	public TagException(final String message, final Exception cause) {
		super(message, cause);
	}

}
