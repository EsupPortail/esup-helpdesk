/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.exceptions; 

/**
 * A class for navigation exceptions.
 */
public class WebFlowException extends EsupException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -563780404193019787L;

	/**
	 * @param message
	 */
	public WebFlowException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WebFlowException(final Exception cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public WebFlowException(final String message, final Exception cause) {
		super(message, cause);
	}

}
