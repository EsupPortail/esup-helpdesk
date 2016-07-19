/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;


/**
 * A class to store archived ticket invitations.
 */
public class ArchivedInvitation implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6528114808984757434L;

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
	private ArchivedTicket archivedTicket;
	
	/**
	 * Constructor.
	 */
	private ArchivedInvitation() {
		super();
	}
	
    /**
     * Constructor.
     * @param user
     * @param archivedTicket the ticket
     */
    public ArchivedInvitation(
    		final User user, 
    		final ArchivedTicket archivedTicket) {
        this();
        this.user = user;
        this.archivedTicket = archivedTicket;
    }
    
    /**
     * Constructor.
     * @param invitation
     * @param archivedTicket the ticket
     */
    public ArchivedInvitation(
    		final Invitation invitation, 
    		final ArchivedTicket archivedTicket) {
        this(invitation.getUser(), archivedTicket);
    }
    
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ArchivedInvitation)) {
			return false;
		}
		return ((ArchivedInvitation) obj).getId() == getId();
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
