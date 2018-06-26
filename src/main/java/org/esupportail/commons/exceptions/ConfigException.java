/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.exceptions; 

/**
 * A class for configuration exceptions.
 */
public class ConfigException extends EsupException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -5889410845815066871L;

	/**
	 * @param message
	 */
	public ConfigException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ConfigException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConfigException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
