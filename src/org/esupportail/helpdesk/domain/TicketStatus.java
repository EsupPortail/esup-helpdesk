/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;


/**
 * Constants for ticket status.
 */
public abstract class TicketStatus {

	/** Ticket status. */
	public static final String FREE = "FREE";
	/** Ticket status. */
	public static final String INPROGRESS = "INPROGRESS";
	/** Ticket status. */
	public static final String CANCELLED = "CANCELLED";
	/** Ticket status. */
	public static final String INCOMPLETE = "INCOMPLETE";
	/** Ticket status. */
	public static final String POSTPONED = "POSTPONED";
	/** Ticket status. */
	public static final String CLOSED = "CLOSED";
	/** Ticket status. */
	public static final String APPROVED = "APPROVED";
	/** Ticket status. */
	public static final String EXPIRED = "EXPIRED";
	/** Ticket status. */
	public static final String CONNECTED_TO_TICKET = "CONNECTED_TO_TICKET";
	/** Ticket status. */
	public static final String CONNECTED_TO_FAQ = "CONNECTED_TO_FAQ";
	/** Ticket status. */
	public static final String REFUSED = "REFUSED";
	/** Ticket status. */
	public static final String ARCHIVED = "ARCHIVED";
	/** Ticket status. */
	public static final String UNDEF = "?";
	
	/**
	 * Constructor.
	 */
	private TicketStatus() {
		//
	}
	
}
