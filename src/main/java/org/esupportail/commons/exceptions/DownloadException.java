/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.exceptions; 

/**
 * A class for download exceptions.
 */
public class DownloadException extends EsupException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 7165963791514558078L;

	/**
	 * @param message
	 */
	public DownloadException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DownloadException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DownloadException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
