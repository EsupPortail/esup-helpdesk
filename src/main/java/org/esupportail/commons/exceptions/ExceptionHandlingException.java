/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.exceptions; 

/**
 * A class thrown while handling exceptions.
 */
public class ExceptionHandlingException extends EsupException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -1699726383757728261L;

	/**
	 * @param message
	 */
	public ExceptionHandlingException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ExceptionHandlingException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ExceptionHandlingException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
