/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.statistics;

import org.esupportail.commons.exceptions.EsupException;


/**
 * An exception thrown when statistics error occur.
 */
public class StatisticsException extends EsupException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = -5868041190831063729L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	public StatisticsException(final String message, final Exception cause) {
		super(message, cause);
	}

	/**
	 * Bean constructor.
	 * @param message
	 */
	public StatisticsException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	public StatisticsException(final Exception cause) {
		super(cause);
	}

}
