/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.io.Serializable;
import java.sql.Timestamp;

import org.esupportail.helpdesk.domain.beans.AbstractTicket;
import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Ticket;

/** 
 * A ticket entry for statistics.
 */ 
public class StatisticsTicketEntry implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -1354659084322311616L;

	/**
	 * The ticket.
	 */
	private Ticket ticket;
	
	/**
	 * The archived ticket.
	 */
	private ArchivedTicket archivedTicket;
	
	/**
	 * True if the ticket can be viewed by the user.
	 */
	private boolean canView;
	
	/**
	 * The charge time.
	 */
	private Integer chargeTime;
	
	/**
	 * The closure time.
	 */
	private Integer closureTime;
	
	/**
	 * @param ticket
	 */
	public StatisticsTicketEntry(
			final Ticket ticket) {
		super();
		this.ticket = ticket;
	}
	
	/**
	 * @param archivedTicket
	 */
	public StatisticsTicketEntry(
			final ArchivedTicket archivedTicket) {
		super();
		this.archivedTicket = archivedTicket;
	}
	
	/**
	 * @return the ticketId
	 */
	public Long getTicketId() {
		if (ticket != null) {
			return ticket.getId();
		}
		return archivedTicket.getTicketId();
	}

	/**
	 * @return the abstractTicket
	 */
	protected AbstractTicket getAbstractTicket() {
		if (ticket != null) {
			return ticket;
		}
		return archivedTicket;
	}

	/**
	 * @return the creationDate
	 */
	public Timestamp getCreationDate() {
		return getAbstractTicket().getCreationDate();
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return getAbstractTicket().getStatus();
	}

	/**
	 * @return the creationDepartment
	 */
	public Department getCreationDepartment() {
		return getAbstractTicket().getCreationDepartment();
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return getAbstractTicket().getDepartment();
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return getAbstractTicket().getLabel();
	}

	/**
	 * @return the canView
	 */
	public boolean isCanView() {
		return canView;
	}

	/**
	 * @return true if an archived ticket.
	 */
	public boolean isArchived() {
		return archivedTicket != null;
	}

	/**
	 * @return the ticket
	 */
	public Ticket getTicket() {
		return ticket;
	}

	/**
	 * @return the archivedTicket
	 */
	public ArchivedTicket getArchivedTicket() {
		return archivedTicket;
	}

	/**
	 * @param canView the canView to set
	 */
	public void setCanView(final boolean canView) {
		this.canView = canView;
	}

	/**
	 * @return the chargeTime
	 */
	public Integer getChargeTime() {
		return chargeTime;
	}

	/**
	 * @param chargeTime the chargeTime to set
	 */
	public void setChargeTime(final Integer chargeTime) {
		this.chargeTime = chargeTime;
	}

	/**
	 * @return the closureTime
	 */
	public Integer getClosureTime() {
		return closureTime;
	}

	/**
	 * @param closureTime the closureTime to set
	 */
	public void setClosureTime(final Integer closureTime) {
		this.closureTime = closureTime;
	}

}

