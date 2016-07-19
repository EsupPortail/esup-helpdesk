/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * An abstract class for archived ticket informations (inherited by ArchivedAction and ArchivedFileInfo). */
@SuppressWarnings("serial")
public abstract class AbstractAchivedTicketInfo implements Serializable {

    /**
     * Primary key.
     */
    private long id;

    /**
     * The archived ticket.
     */
    private ArchivedTicket archivedTicket;

    /**
     * The user that did the action.
     */
    private User user;

    /**
     * The date of the action.
     */
    private Timestamp date;

    /**
     * The effective scope of the action.
     */
    private String effectiveScope;

	/**
	 * Default constructor.
	 */
	protected AbstractAchivedTicketInfo() {
		this.date = new java.sql.Timestamp(new Date().getTime());
	}
	
	/**
	 * General constructor (Set the needed not null attributes). 
	 * @param owner 
	 * @param archivedTicket
	 * @param effectiveScope
	 */
	protected AbstractAchivedTicketInfo(
			final User owner, 
			final ArchivedTicket archivedTicket, 
			final String effectiveScope) {
		this();
		this.user = owner;
		this.archivedTicket = archivedTicket;
		this.effectiveScope = effectiveScope;
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
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final long id) {
		this.id = id;
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

	/**
	 * @return the effectiveScope
	 */
	public String getEffectiveScope() {
		return effectiveScope;
	}

	/**
	 * @param effectiveScope the effectiveScope to set
	 */
	public void setEffectiveScope(final String effectiveScope) {
		this.effectiveScope = effectiveScope;
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
