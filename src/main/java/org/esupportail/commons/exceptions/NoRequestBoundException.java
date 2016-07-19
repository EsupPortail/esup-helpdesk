/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.exceptions; 


/**
 * An exception thrown when no request is bound to thread.
 */
public class NoRequestBoundException extends EsupException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -550549110130100610L;

	/**
	 * Constructor.
	 */
	public NoRequestBoundException() {
		super("no request bound to thread!");
	}

}
