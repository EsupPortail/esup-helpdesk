/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.cas; 

import org.esupportail.commons.exceptions.EsupException;

/**
 * A class for CAS exceptions.
 */
public class CasException extends EsupException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -8694711500120682967L;

	/**
	 * @param message
	 */
	public CasException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CasException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CasException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
