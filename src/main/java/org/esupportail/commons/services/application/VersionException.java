/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.application; 

import org.esupportail.commons.exceptions.EsupException;

/**
 * An class to represent the exceptions thrown when dealing with application versions.
 */
public class VersionException extends EsupException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 8458207243695689742L;

	/**
	 * Bean constructor.
	 */
	public VersionException() {
		super();
	}

	/**
	 * @param message
	 */
	public VersionException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public VersionException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VersionException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
