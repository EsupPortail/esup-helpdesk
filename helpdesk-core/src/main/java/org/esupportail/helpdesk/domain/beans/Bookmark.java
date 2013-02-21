/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;

/**
 * This class is used to store the bookmarks.
 */
public final class Bookmark implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3256367100138207823L;

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
	public Bookmark() {
		super();
	}

	/**
	 * Constructor.
	 * @param user 
	 */
	private Bookmark(final User user) {
		this();
		this.user = user;
	}

	/**
	 * Constructor.
	 * @param user 
	 * @param ticket 
	 */
	public Bookmark(final User user, final Ticket ticket) {
		this(user);
		this.ticket = ticket;
	}

	/**
	 * Constructor.
	 * @param user 
	 * @param archivedTicket 
	 */
	public Bookmark(final User user, final ArchivedTicket archivedTicket) {
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
		if (!(obj instanceof Bookmark)) {
			return false;
		}
		return ((Bookmark) obj).getId() == getId();
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
		+ ", ticket=" + ticket + ""
		+ ", archivedTicket=" + archivedTicket + ""
		+ ", user=" + user + ""
		+ "]";
	}

	/**
	 * @return true if a ticket bookmark.
	 */
	public boolean isTicketBookmark() {
		return ticket != null;
	}

	/**
	 * @return true if an archived ticket bookmark.
	 */
	public boolean isArchivedTicketBookmark() {
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