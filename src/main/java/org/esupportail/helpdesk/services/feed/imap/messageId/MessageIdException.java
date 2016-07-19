/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 University of Pardubice.
 */
package org.esupportail.helpdesk.services.feed.imap.messageId;

import org.esupportail.commons.exceptions.EsupException;

/**
 * An exception thrown when dealing with Message ids.
 */
public class MessageIdException extends EsupException {
	
    /**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -500991152514599580L;

	/**
	 * Constructor.
	 */
	public MessageIdException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message
	 * @param cause
	 */
	public MessageIdException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 * @param message
	 */
	public MessageIdException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause
	 */
	public MessageIdException(final Throwable cause) {
		super(cause);
	}

}
