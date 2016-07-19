/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;

/**
 * This class is used to store the history items.
 */
public final class HistoryItem implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1864406442202369090L;

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
	 * The ticket.
	 */
	private ArchivedTicket archivedTicket;
	
	/**
	 * Bean constructor.
	 */
	public HistoryItem() {
		super();
	}

	/**
	 * Constructor.
	 * @param user 
	 */
	private HistoryItem(final User user) {
		this();
		this.user = user;
	}

	/**
	 * Constructor.
	 * @param user 
	 * @param ticket 
	 */
	public HistoryItem(final User user, final Ticket ticket) {
		this(user);
		this.ticket = ticket;
	}

	/**
	 * Constructor.
	 * @param user 
	 * @param archivedTicket 
	 */
	public HistoryItem(final User user, final ArchivedTicket archivedTicket) {
		this(user);
		this.archivedTicket = archivedTicket;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof HistoryItem)) {
			return false;
		}
		return ((HistoryItem) obj).getId() == getId();
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
		String result = getClass().getSimpleName() + "#" + hashCode() + "[" 
		+ " id=[" + id + "]";
		if (ticket != null) {
			result += ", ticket#" + ticket.getId();
		}
		if (archivedTicket != null) {
			result += ", archivedTicket#" + archivedTicket.getTicketId();
		}
		if (user != null) {
			result += ", user#" + user.getId();
		}
		return result + "]";
	}

	/**
	 * @return true if a ticket history item.
	 */
	public boolean isTicketHistoryItem() {
		return ticket != null;
	}

	/**
	 * @return true if an archived ticket history item.
	 */
	public boolean isArchivedTicketHistoryItem() {
		return archivedTicket != null;
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

	/**
	 * @return the archivedTicket
	 */
	public ArchivedTicket getArchivedTicket() {
		return archivedTicket;
	}

	/**
	 * @param archivedTicket the archivedTicket to set
	 */
	public void setArchivedTicket(final ArchivedTicket archivedTicket) {
		this.archivedTicket = archivedTicket;
	}
	
}