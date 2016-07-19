/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.smtp; 

import org.esupportail.commons.exceptions.EsupException;

/**
 * An class to represent the exceptions thrown when sendin mails.
 */
public class SmtpException extends EsupException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 9093603917198471034L;

	/**
	 * Bean constructor.
	 */
	public SmtpException() {
		super();
	}

	/**
	 * @param message
	 */
	public SmtpException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SmtpException(final Exception cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SmtpException(final String message, final Exception cause) {
		super(message, cause);
	}

}
