/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.remote; 

import org.esupportail.commons.exceptions.EsupException;

/**
 * A class for web services authorization exceptions.
 */
public class WebServiceAuthorizationException extends EsupException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 4118216554765981193L;

	/**
	 * @param message
	 */
	public WebServiceAuthorizationException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WebServiceAuthorizationException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public WebServiceAuthorizationException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
