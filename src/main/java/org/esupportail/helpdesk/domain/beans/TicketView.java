/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class is used to store the dates when users view tickets (the last time).
 */
public final class TicketView implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3290929936020967515L;

	/**
	 * Primary key.
	 */
	private Long id;

	/**
	 * The user.
	 */
	private User user;

	/**
	 * The ticket.
	 */
	private Ticket ticket;
	
	/**
	 * The date.
	 */
	private Timestamp date;

	/**
	 * Bean constructor.
	 */
	public TicketView() {
		super();
	}

	/**
	 * Create a new object.
	 * @param user the user
	 * @param ticket the ticket
	 * @param date the date
	 */
	public TicketView(final User user, final Ticket ticket, final Timestamp date) {
		this();
		this.user = user;
		this.ticket = ticket;
		this.date = date;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TicketView)) {
			return false;
		}
		return ((TicketView) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		if (getId() == null) {
			return 0;
		}
		return getId().intValue();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[" 
		+ ", id=[" + id + "]"
		+ ", date=[" + date + "]"
		+ ", ticket=" + ticket + ""
		+ ", user=" + user + ""
		+ "]";
	}

	/**
	 * @return the date
	 */
	public Timestamp getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(final Timestamp date) {
		this.date = date;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the ticket
	 */
	public Ticket getTicket() {
		return ticket;
	}

	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(final Ticket ticket) {
		this.ticket = ticket;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(final User user) {
		this.user = user;
	}
	
}