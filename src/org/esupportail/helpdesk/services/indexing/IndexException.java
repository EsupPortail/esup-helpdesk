/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing;

import org.esupportail.commons.exceptions.EsupException;


/**
 * An exception thrown when indexing error occur.
 */
public class IndexException extends EsupException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 7034566702513063833L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	public IndexException(final String message, final Exception cause) {
		super(message, cause);
	}

	/**
	 * Bean constructor.
	 * @param message
	 */
	public IndexException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	public IndexException(final Exception cause) {
		super(cause);
	}

}
