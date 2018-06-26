/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.ActionScope;
import org.esupportail.helpdesk.domain.TicketScope;

/**
 * An abstract class for ticket informations (inherited by Action and FileInfo). */
@SuppressWarnings("serial")
public abstract class AbstractTicketInfo implements Serializable {

    /**
     * Primary key.
     */
    private long id;

    /**
     * The ticket.
     */
    private Ticket ticket;

    /**
     * The user that did the action.
     */
    private User user;

    /**
     * The date of the action.
     */
    private Timestamp date;

    /**
     * The scope of the action.
     */
    private String scope;

	/**
	 * Default constructor.
	 */
	protected AbstractTicketInfo() {
		this.date = new java.sql.Timestamp(new Date().getTime());
		//suppression des millisecondes car entraine un bug 
		//lorsque l'on regarde si l'heure de dernière action du ticket est = au ticket courant afin de vérifier qu'une autre action n'a pas été réalisée en //, 
		//les secondes sont différentes car il y a un arrondi fait pour les données stockées en base
		this.date.setNanos(0);
	}
	
	/**
	 * General constructor (Set the needed not null attributes). 
	 * @param owner The owner to set.
	 * @param ticket The ticket to set.
	 * @param scope The privateAction to set.
	 */
	protected AbstractTicketInfo(
			final User owner, 
			final Ticket ticket, 
			final String scope) {
		this();
		this.user = owner;
		this.ticket = ticket;
		this.scope = scope;
	}
	
	/**
	 * Get the effective scope of a ticket (using default policy if needed).
	 * @return a string.
	 */
	public String getEffectiveScope() {
		if (!ActionScope.DEFAULT.equals(scope)) {
			return scope;
		}
		if (ticket.getEffectiveScope().equals(TicketScope.PUBLIC)) {
			return TicketScope.PUBLIC;
		} 
	    return ActionScope.OWNER;
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
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(final String scope) {
		this.scope = StringUtils.nullIfEmpty(scope);
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
