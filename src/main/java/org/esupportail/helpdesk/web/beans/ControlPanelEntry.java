/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.Ticket;

/** 
 * An entry of the control panel.
 */ 
public class ControlPanelEntry implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6791861852052635460L;

	/**
	 * The ticket.
	 */
	private Ticket ticket;

	/**
	 * True if the ticket was viewed by the user.
	 */
	private boolean viewed;

	/**
	 * True if the ticket can be read by the user.
	 */
	private boolean canRead;

	/**
	 * True if the ticket is bookmarked by the user.
	 */
	private boolean bookmarked;

	/**
	 * Bean constructor.
	 * @param ticket 
	 * @param viewed 
	 * @param canRead 
	 * @param bookmarked 
	 */
	public ControlPanelEntry(
			final Ticket ticket, 
			final boolean viewed, 
			final boolean canRead,
			final boolean bookmarked) {
		super();
		this.ticket = ticket;
		this.viewed = viewed;
		this.canRead = canRead;
		this.bookmarked = bookmarked;
	}

	/**
	 * @return the canRead
	 */
	public boolean isCanRead() {
		return canRead;
	}

	/**
	 * @return the ticket
	 */
	public Ticket getTicket() {
		return ticket;
	}

	/**
	 * @return the viewed
	 */
	public boolean isViewed() {
		return viewed;
	}

	/**
	 * @return the bookmarked
	 */
	public boolean isBookmarked() {
		return bookmarked;
	}

}

