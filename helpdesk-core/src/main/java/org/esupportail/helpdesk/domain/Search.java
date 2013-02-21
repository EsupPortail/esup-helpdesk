/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain; 

/** 
 * The constants used by the search interface.
 */ 
public abstract class Search {
	
	/** A constant for the status filter. */
	public static final String TYPE_FILTER_ALL = "ALL";
	/** A constant for the status filter. */
	public static final String TYPE_FILTER_ACTIVE_TICKET_AND_FAQ = "ACTIVE_TICKET_AND_FAQ";
	/** A constant for the status filter. */
	public static final String TYPE_FILTER_ACTIVE_TICKET = "ACTIVE_TICKET";
	/** A constant for the status filter. */
	public static final String TYPE_FILTER_ARCHIVED_TICKET = "ARCHIVED_TICKET";
	/** A constant for the status filter. */
	public static final String TYPE_FILTER_TICKET = "TICKET";
	/** A constant for the status filter. */
	public static final String TYPE_FILTER_FAQ = "FAQ";
	
	/**
	 * Constructor.
	 */
	private Search() {
		//
	}
	
}
