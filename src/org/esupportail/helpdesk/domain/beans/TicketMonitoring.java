/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;


/**
 * A class to store ticket monitoring by users.
 */
public class TicketMonitoring implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1414531433355355601L;

	/**
	 * Primary key.
	 */
	private Long id;

	/**
	 * Represent the user in the user-ticket relation.
	 */
	private User user;

	/**
	 * Represent the ticket in the user-ticket relation.
	 */
	private Ticket ticket;
	
	/**
	 * Constructor.
	 */
	private TicketMonitoring() {
		super();
	}
	
    /**
     * Constructor.
     * @param user the user
     * @param ticket the ticket
     */
    public TicketMonitoring(final User user, final Ticket ticket) {
        this();
        this.user = user;
        this.ticket = ticket;
    }
    
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TicketMonitoring)) {
			return false;
		}
		return ((TicketMonitoring) obj).getId() == getId();
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
