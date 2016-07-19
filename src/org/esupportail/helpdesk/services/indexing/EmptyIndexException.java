/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing;

/**
 * An exception thrown when the index is empty.
 */
public class EmptyIndexException extends IndexException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = -9176805224842984802L;

	/**
	 * Bean constructor.
	 */
	public EmptyIndexException() {
		super("index is empty, run ant update-index");
	}

}
