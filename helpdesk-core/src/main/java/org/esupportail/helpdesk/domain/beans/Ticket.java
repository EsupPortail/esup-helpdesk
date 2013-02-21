/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.sql.Timestamp;
import java.util.List;

import org.esupportail.helpdesk.domain.ActionType;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.TicketScope;
import org.esupportail.helpdesk.domain.TicketStatus;

/**
 * A class to store tickets. */
public final class Ticket extends AbstractTicket {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2744038522602147432L;

    /**
     * status of ticket.
     */
    private String status;
    
    /**
     * The scope.
     */
    private String scope;

    /**
     * A link to an old FAQ part.
     */
    @SuppressWarnings("deprecation")
	private OldFaqPart connectionOldFaqPart;
    
    /**
     * A link to an old FaqEntry, null if the ticket is not connected to a faqEntry.
     */
    @SuppressWarnings("deprecation")
	private OldFaqEntry connectionOldFaqEntry;

    /**
     * Date of last action on the ticket.
     */
    private Timestamp lastActionDate;

    /**
     * The category of the ticket.
     */
    private Category category;

    /**
     * The date when the ticket should be recalled.
     */
    private Timestamp recallDate;
    
    /**
     * The manager's display name.
     */
    private String managerDisplayName;
    
	/**
	 * Default constructor.
	 */
	protected Ticket() {
		super();
	}

	/**
	 * Constructor.
	 * @param t
	 */
	public Ticket(
			final Ticket t) {
		super(t);
		this.status = t.status;
		this.scope = t.scope;
		this.connectionOldFaqPart = t.connectionOldFaqPart;
		this.connectionOldFaqEntry = t.connectionOldFaqEntry;
		this.lastActionDate = t.lastActionDate;
		this.category = t.category;
		this.recallDate = t.recallDate;
		this.managerDisplayName = t.managerDisplayName;
	}

	/**
	 * Constructor.
	 * @param owner 
	 * @param origin
	 * @param creationDepartment 
	 * @param computer 
	 * @param label 
	 * @param priorityLevel 
	 * @param scope 
	 * @param category 
	 */
	public Ticket(
			final User owner, 
			final String origin, 
			final Department creationDepartment,
			final Category category, 
			final String label,
			final String computer, 
			final int priorityLevel,
			final String scope) {
		super(
				owner, origin, creationDepartment, category.getDepartment(),
				label, computer, priorityLevel);
		setCategory(category);
		setScope(scope);
		setLastActionDate(getCreationDate()); 
		setStatus(TicketStatus.FREE);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[" 
		+ "id=[" + getId() + "]" 
		+ ", department=[" + getDepartment() + "]" 
		+ ", manager=[" + getManager() + "]" 
		+ ", owner=[" + getOwner() + "]" 
		+ "]";
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Ticket)) {
			return false;
		}
		Ticket ticket = (Ticket) obj;
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
	 * @return true if the ticket is opened.
	 */
	public boolean isOpened() {
		return this.status.equals(TicketStatus.FREE)
		|| this.status.equals(TicketStatus.INPROGRESS)
		|| this.status.equals(TicketStatus.POSTPONED)
		|| this.status.equals(TicketStatus.INCOMPLETE);
	}
	
	/**
	 * @return true if the ticket is opened and assigned to a manager.
	 */
	public boolean isOpenAssigned() {
		return this.status.equals(TicketStatus.INPROGRESS)
		|| this.status.equals(TicketStatus.POSTPONED)
		|| this.status.equals(TicketStatus.INCOMPLETE);
	}
	
	/**
	 * @return true if the ticket is free.
	 */
	public boolean isFree() {
		return this.status.equals(TicketStatus.FREE);
	}
	
	/**
	 * @return true if the ticket is in progress.
	 */
	public boolean isInProgress() {
		return this.status.equals(TicketStatus.INPROGRESS);
	}
	
	/**
	 * @return true if the ticket is incomplete.
	 */
	public boolean isIncomplete() {
		return this.status.equals(TicketStatus.INCOMPLETE);
	}
	
	/**
	 * @return true if the ticket is postponed.
	 */
	public boolean isPostponed() {
		return this.status.equals(TicketStatus.POSTPONED);
	}
	
	/**
	 * @return true if the ticket is closed.
	 */
	public boolean isClosed() {
		return this.status.equals(TicketStatus.CLOSED);
	}
	
	/**
	 * @return true if the ticket is waiting for approval.
	 */
	public boolean isWaitingForApproval() {
		return this.status.equals(TicketStatus.CLOSED) 
		|| this.status.equals(TicketStatus.CONNECTED_TO_FAQ)
		|| this.status.equals(TicketStatus.CONNECTED_TO_TICKET);
	}
	
	/**
	 * @return true if the ticket is connected to another ticket.
	 */
	public boolean isTicketConnected() {
		return this.status.equals(TicketStatus.CONNECTED_TO_TICKET);
	}
	
	/**
	 * @return true if the ticket is connected to a faq component.
	 */
	public boolean isFaqConnected() {
		return this.status.equals(TicketStatus.CONNECTED_TO_FAQ);
	}
	
	/**
	 * Compute the effective scope (using default policy if needed).
	 * @return true if the category needs to be updated.
	 */
	public boolean computeEffectiveDefaultTicketScope() {
		String oldEffectiveScope = getEffectiveScope();
		if (!TicketScope.DEFAULT.equals(scope)) {
			setEffectiveScope(scope);
		} else if (category == null) {
			// needed only for v2 tickets
			setEffectiveScope(getDepartment().getEffectiveDefaultTicketScope());
		} else {
			setEffectiveScope(category.getEffectiveDefaultTicketScope());
		}
		return !(getEffectiveScope().equals(oldEffectiveScope));
	}
	
	/**
	 * @return the priorityLevel
	 */
	public int getEffectivePriorityLevel() {
		if (getPriorityLevel() == DomainService.DEFAULT_PRIORITY_VALUE) {
			return category.getEffectiveDefaultTicketPriority();
		}
		return getPriorityLevel();
	}

	/**
	 * Update the charge time.
	 * @param actions 
	 */
	public void updateTicketChargeTime(
			final List<Action> actions) {
		if (getChargeTime() != null) {
			return;
		}
		User manager = null;
		Timestamp lastCreationActionDate = null;
		boolean creationStage = true;
		for (Action action : actions) {
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
			final Action action) {
		updateTicketClosureTime(action.getActionType(), action.getDate());
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(final Category category) {
		this.category = category;
	}

	/**
	 * @return the connectionOldFaqEntry
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public OldFaqEntry getConnectionOldFaqEntry() {
		return connectionOldFaqEntry;
	}

	/**
	 * @param connectionOldFaqEntry the connectionOldFaqEntry to set
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setConnectionOldFaqEntry(final OldFaqEntry connectionOldFaqEntry) {
		this.connectionOldFaqEntry = connectionOldFaqEntry;
	}

	/**
	 * @return the connectionOldFaqPart
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public OldFaqPart getConnectionOldFaqPart() {
		return connectionOldFaqPart;
	}

	/**
	 * @param connectionOldFaqPart the connectionOldFaqPart to set
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setConnectionOldFaqPart(final OldFaqPart connectionOldFaqPart) {
		this.connectionOldFaqPart = connectionOldFaqPart;
	}

	/**
	 * @return the lastActionDate
	 */
	public Timestamp getLastActionDate() {
		return lastActionDate;
	}

	/**
	 * @param lastActionDate the lastActionDate to set
	 */
	public void setLastActionDate(final Timestamp lastActionDate) {
		this.lastActionDate = lastActionDate;
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
		this.scope = scope;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.AbstractTicket#getStatus()
	 */
	@Override
	public String getStatus() {
		return status;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.AbstractTicket#isArchived()
	 */
	@Override
	public boolean isArchived() {
		return false;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * @return the recallDate
	 */
	public Timestamp getRecallDate() {
		return recallDate;
	}

	/**
	 * @param recallDate the recallDate to set
	 */
	public void setRecallDate(final Timestamp recallDate) {
		this.recallDate = recallDate;
	}

	/**
	 * @return the managerDisplayName
	 */
	public String getManagerDisplayName() {
		return managerDisplayName;
	}

	/**
	 * @param managerDisplayName the managerDisplayName to set
	 */
	public void setManagerDisplayName(final String managerDisplayName) {
		this.managerDisplayName = managerDisplayName;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.beans.AbstractTicket#setManager(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void setManager(final User manager) {
		super.setManager(manager);
		if (manager == null) {
			setManagerDisplayName("");
		} else {
			setManagerDisplayName(manager.getDisplayName());
		}
	}

}
