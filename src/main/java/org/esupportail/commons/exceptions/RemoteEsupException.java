/**
 * ESUP-Portail Commons - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-commons
 */
package org.esupportail.commons.exceptions; 

import java.rmi.RemoteException;

/**
 * An abstract class that should be inherited by all the remote services exceptions.
 */
@SuppressWarnings("serial")
public abstract class RemoteEsupException extends RemoteException {

	/**
	 * Constructor.
	 */
	protected RemoteEsupException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message 
	 */
	protected RemoteEsupException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause 
	 */
	protected RemoteEsupException(final Throwable cause) {
		super(cause.getMessage(), cause);
	}

	/**
	 * Constructor.
	 * @param message 
	 * @param cause 
	 */
	protected RemoteEsupException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
