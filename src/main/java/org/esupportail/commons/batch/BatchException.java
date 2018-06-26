/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.batch; 

import org.esupportail.commons.exceptions.EsupException;

/**
 * A class for batch exceptions.
 */
public class BatchException extends EsupException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -2459187717319478671L;

	/**
	 * @param message
	 */
	public BatchException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public BatchException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BatchException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
