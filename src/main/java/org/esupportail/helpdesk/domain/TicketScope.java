/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;


/**
 * Constants for tickets scopes.
 */
public abstract class TicketScope {

	/** ticket scope. */
	public static final String DEFAULT = "DEFAULT";
	/** ticket scope. */
	public static final String PUBLIC = "PUBLIC";
	/** ticket scope. */
	public static final String PRIVATE = "PRIVATE";
	/** ticket scope. */
	public static final String SUBJECT_ONLY = "SUBJECT_ONLY";
	/** ticket scope. */
	public static final String UNDEF = "?";

	/**
	 * Constructor.
	 */
	private TicketScope() {
		//
	}
	
}
