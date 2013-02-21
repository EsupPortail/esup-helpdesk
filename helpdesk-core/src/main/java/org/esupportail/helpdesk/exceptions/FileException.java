/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.exceptions;

import org.esupportail.commons.exceptions.EsupException;

/**
 * An exception thrown when something wrong happens with uploaded files.
 */
public class FileException extends EsupException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = -8029078550895556819L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	public FileException(final String message, final Exception cause) {
		super(message, cause);
	}
	
	/**
	 * Bean constructor.
	 * @param message
	 */
	public FileException(final String message) {
		super(message);
	}
	
	/**
	 * Bean constructor.
	 * @param cause
	 */
	public FileException(final Exception cause) {
		super(cause);
	}
}
