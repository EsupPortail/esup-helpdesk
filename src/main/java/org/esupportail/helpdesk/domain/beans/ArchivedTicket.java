/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.esupportail.helpdesk.domain.ActionType;
import org.esupportail.helpdesk.domain.TicketStatus;

/**
 * A class to store archived tickets. */
public final class ArchivedTicket extends AbstractTicket {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 4446434629686939744L;

	/**
	 * The id of the ticket before archived.
	 */
	private long ticketId;

    /**
     * Date of last action on the ticket.
     */
    private Timestamp archivingDate;
    
    /**
     * The category of the ticket.
     */
    private String categoryLabel;

	/**
	 * Constructor.
	 */
	protected ArchivedTicket() {
		super();
	}

	/**
	 * Constructor.
	 * @param t
	 */
	public ArchivedTicket(
			final ArchivedTicket t) {
		super(t);
		this.ticketId = t.ticketId;
		this.archivingDate = t.archivingDate;
		this.categoryLabel = t.categoryLabel;
	}

	/**
	 * Constructor.
	 * @param ticket 
	 */
	public ArchivedTicket(
			final Ticket ticket) {
		super();
		setArchivingDate(new java.sql.Timestamp(new Date().getTime()));
		setCategoryLabel(ticket.getCategory().getLabel());
		setComputer(ticket.getComputer());
		if (ticket.getConnectionFaq() != null) {
			setConnectionFaq(ticket.getConnectionFaq());
		}
		if (ticket.getConnectionTicket() != null) {
			setConnectionTicket(ticket.getConnectionTicket());
		}
		if (ticket.getConnectionArchivedTicket() != null) {
			setConnectionArchivedTicket(ticket.getConnectionArchivedTicket());
		}
		setCreationDate(ticket.getCreationDate());
		setCreationDepartment(ticket.getCreationDepartment());
		setCreator(ticket.getCreator());
		setDepartment(ticket.getDepartment());
		setEffectiveScope(ticket.getEffectiveScope());
		setLabel(ticket.getLabel());
		setManager(ticket.getManager());
		setOrigin(ticket.getOrigin());
		setOwner(ticket.getOwner());
		setPriorityLevel(ticket.getPriorityLevel());
		setSpentTime(ticket.getSpentTime());
		setTicketId(ticket.getId());
		setChargeTime(ticket.getChargeTime());
		setClosureTime(ticket.getClosureTime());
		setCreationDepartment(ticket.getCreationDepartment());
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ArchivedTicket)) {
			return false;
		}
		ArchivedTicket ticket = (ArchivedTicket) obj;
		return getId() == ticket.getId();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}
		
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[" 
		+ "id=[" + getId() + "]" 
		+ "ticketId=[" + getTicketId() + "]" 
		+ ", department=[" + getDepartment() + "]" 
		+ ", manager=[" + getManager() + "]" 
		+ ", owner=[" + getOwner() + "]" 
		+ "]";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.AbstractTicket#getStatus()
	 */
	@Override
	public String getStatus() {
		return TicketStatus.ARCHIVED;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.AbstractTicket#isArchived()
	 */
	@Override
	public boolean isArchived() {
		return true;
	}

	/**
	 * Update the charge time.
	 * @param actions 
	 */
	public void updateTicketChargeTime(
			final List<ArchivedAction> actions) {
		if (getChargeTime() != null) {
			return;
		}
		User manager = null;
		Timestamp lastCreationActionDate = null;
		boolean creationStage = true;
		for (ArchivedAction action : actions) {
			if (creationStage) {
				if (lastCreationActionDate != null 
						&& action.getDate().getTime() 
						> lastCreationActionDate.getTime() + CREATION_STAGE_MILLISECONDS) {
					creationStage = false;
				}
			}
			String actionType = action.getActionType();
			if (updateTicketChargeTime(
					creationStage, actionType, action.getDate(), 
					action.getUser(), manager)) {
				return;
			}
			if (creationStage) {
				lastCreationActionDate = action.getDate();
			}
			if (ActionType.ASSIGN.equals(actionType)
					|| ActionType.FREE.equals(actionType)
					|| ActionType.TAKE.equals(actionType)) {
				manager = action.getManagerAfter();
			}
		}
	}

	/**
	 * Update the closure time.
	 * @param action 
	 */
	public void updateTicketClosureTime(
			final ArchivedAction action) {
		updateTicketClosureTime(action.getActionType(), action.getDate());
	}

	/**
	 * @return the categoryLabel
	 */
	public String getCategoryLabel() {
		return categoryLabel;
	}

	/**
	 * @param categoryLabel the categoryLabel to set
	 */
	public void setCategoryLabel(final String categoryLabel) {
		this.categoryLabel = categoryLabel;
	}

	/**
	 * @return the ticketId
	 */
	public long getTicketId() {
		return ticketId;
	}

	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketId(final long ticketId) {
		this.ticketId = ticketId;
	}

	/**
	 * @return the archivingDate
	 */
	public Timestamp getArchivingDate() {
		return archivingDate;
	}

	/**
	 * @param archivingDate the archivingDate to set
	 */
	public void setArchivingDate(final Timestamp archivingDate) {
		this.archivingDate = archivingDate;
	}

}
