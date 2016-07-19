/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.exceptions; 

/**
 * A class to represent the exceptions thrown when expected objects 
 * do not exists (either retrieved from the database or configuration files).
 */
public class ObjectNotFoundException extends EsupException {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3632596009604914042L;

	/**
	 * @param message
	 */
	public ObjectNotFoundException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ObjectNotFoundException(final Exception cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ObjectNotFoundException(final String message, final Exception cause) {
		super(message, cause);
	}

}
