/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.sql.Timestamp;

import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.TicketScope;

/**
 * The representation of a ticket action. */
public class Action extends AbstractTicketInfo {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2220086978750268044L;

	/**
     * The type of the action.
     */
    private String actionType;

    /**
     * Status before the action.
     */
    private String statusBefore;

    /**
     * Status after the action.
     */
    private String statusAfter;

	/**
     * The message of the action.
     */
    private String message;

    /**
     * A file bound to the action (optional).
     */
    private OldFileInfo oldFileInfo;

    /**
     * The name of the uploaded file.
     */
    private String filename;

    /**
     * ticketOwner of ticket before action.
     */
    private User ticketOwnerBefore;

	/**
	 * ticketOwner of ticket after action.
	 */
	private User ticketOwnerAfter;

    /**
     * Manager of ticket before action.
     */
    private User managerBefore;

	/**
	 * Manager of ticket after action.
	 */
	private User managerAfter;

    /**
     * Label of ticket before action.
     */
    private String labelBefore;

    /**
     * Label of ticket after action.
     */
    private String labelAfter;

    /**
     * Priority level of ticket before action.
     */
    private int priorityLevelBefore;

    /**
     * Priority level of ticket after action.
     */
    private int priorityLevelAfter;

    /**
     * Label of ticket before action.
     */
    private String computerBefore;

    /**
     * Label of ticket after action.
     */
    private String computerAfter;

    /**
     * Scope of ticket before action.
     */
    private String scopeBefore;

    /**
     * Scope of ticket after action.
     */
    private String scopeAfter;

    /**
     * Origin of ticket before action.
     */
    private String originBefore;

    /**
     * Origin of ticket after action.
     */
    private String originAfter;

    /**
     * Department of ticket before action.
     */
    private Department departmentBefore;

    /**
     * Department of ticket after action.
     */
    private Department departmentAfter;

    /**
     * Ticket connection.
     */
    private Ticket oldConnectionAfter;

    /**
     * old faqPart connection.
     */
    private OldFaqPart oldFaqPartConnectionAfter;

    /**
     * old faqEntry connection.
     */
    private OldFaqEntry oldFaqEntryConnectionAfter;

    /**
     * Spent time of ticket before action.
     */
    private long spentTimeBefore;

    /**
     * Spent time of ticket after action.
     */
    private long spentTimeAfter;
	
    /**
     * Category before action.
     */
    private Category categoryBefore;

    /**
     * Category after action.
     */
    private Category categoryAfter;

    /**
     * Invited user action.
     */
    private User invitedUser;

    /**
     * The date when the ticket should be recalled.
     */
    private Timestamp recallDate;
    
	/**
	 * Default constructor.
	 */
	protected Action() {
		super();
	}
	
	/**
	 * General constructor of action. (Set the needed not null attributes). 
	 * @param owner The owner to set.
	 * @param ticket The ticket to set.
	 * @param actionType The actionType to set.
	 * @param statusAfter The statusAfter to set.
	 * @param scope The privateAction to set.
	 * @param message The message to set.
	 */
	public Action(
			final User owner, 
			final Ticket ticket, 
			final String actionType, 
			final String statusAfter, 
			final String scope, 
			final String message) {
		super(owner, ticket, scope);
		setMessage(message);
		this.priorityLevelBefore = DomainService.DEFAULT_PRIORITY_VALUE;
		this.priorityLevelAfter = DomainService.DEFAULT_PRIORITY_VALUE;
		this.scopeBefore = TicketScope.UNDEF;
		this.scopeAfter = TicketScope.UNDEF;
		this.spentTimeBefore = -1;
		this.spentTimeAfter = -1;
		this.actionType = actionType;
		this.statusBefore = ticket.getStatus();
		this.statusAfter = statusAfter;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Action)) {
			return false;
		}
		return ((Action) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}

	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(final String actionType) {
		this.actionType = StringUtils.nullIfEmpty(actionType);
	}

	/**
	 * @return the categoryAfter
	 */
	public Category getCategoryAfter() {
		return categoryAfter;
	}

	/**
	 * @param categoryAfter the categoryAfter to set
	 */
	public void setCategoryAfter(final Category categoryAfter) {
		this.categoryAfter = categoryAfter;
	}

	/**
	 * @return the categoryBefore
	 */
	public Category getCategoryBefore() {
		return categoryBefore;
	}

	/**
	 * @param categoryBefore the categoryBefore to set
	 */
	public void setCategoryBefore(final Category categoryBefore) {
		this.categoryBefore = categoryBefore;
	}

	/**
	 * @return the computerAfter
	 */
	public String getComputerAfter() {
		return computerAfter;
	}

	/**
	 * @param computerAfter the computerAfter to set
	 */
	public void setComputerAfter(final String computerAfter) {
		this.computerAfter = StringUtils.nullIfEmpty(computerAfter);
	}

	/**
	 * @return the computerBefore
	 */
	public String getComputerBefore() {
		return computerBefore;
	}

	/**
	 * @param computerBefore the computerBefore to set
	 */
	public void setComputerBefore(final String computerBefore) {
		this.computerBefore = StringUtils.nullIfEmpty(computerBefore);
	}

	/**
	 * @return the connectionAfter
	 */
	public Ticket getOldConnectionAfter() {
		return oldConnectionAfter;
	}

	/**
	 * @param oldConnectionAfter the oldConnectionAfter to set
	 */
	public void setOldConnectionAfter(final Ticket oldConnectionAfter) {
		this.oldConnectionAfter = oldConnectionAfter;
	}

	/**
	 * @return the departmentAfter
	 */
	public Department getDepartmentAfter() {
		return departmentAfter;
	}

	/**
	 * @param departmentAfter the departmentAfter to set
	 */
	public void setDepartmentAfter(final Department departmentAfter) {
		this.departmentAfter = departmentAfter;
	}

	/**
	 * @return the departmentBefore
	 */
	public Department getDepartmentBefore() {
		return departmentBefore;
	}

	/**
	 * @param departmentBefore the departmentBefore to set
	 */
	public void setDepartmentBefore(final Department departmentBefore) {
		this.departmentBefore = departmentBefore;
	}

	/**
	 * @return the fileInfo
	 */
	public OldFileInfo getOldFileInfo() {
		return oldFileInfo;
	}

	/**
	 * @param oldFileInfo the oldFileInfo to set
	 */
	public void setOldFileInfo(final OldFileInfo oldFileInfo) {
		this.oldFileInfo = oldFileInfo;
	}

	/**
	 * @return the invitedUser
	 */
	public User getInvitedUser() {
		return invitedUser;
	}

	/**
	 * @param invitedUser the invitedUser to set
	 */
	public void setInvitedUser(final User invitedUser) {
		this.invitedUser = invitedUser;
	}

	/**
	 * @return the labelAfter
	 */
	public String getLabelAfter() {
		return labelAfter;
	}

	/**
	 * @param labelAfter the labelAfter to set
	 */
	public void setLabelAfter(final String labelAfter) {
		this.labelAfter = StringUtils.nullIfEmpty(labelAfter);
	}

	/**
	 * @return the labelBefore
	 */
	public String getLabelBefore() {
		return labelBefore;
	}

	/**
	 * @param labelBefore the labelBefore to set
	 */
	public void setLabelBefore(final String labelBefore) {
		this.labelBefore = StringUtils.nullIfEmpty(labelBefore);
	}

	/**
	 * @return the managerAfter
	 */
	public User getManagerAfter() {
		return managerAfter;
	}

	/**
	 * @param managerAfter the managerAfter to set
	 */
	public void setManagerAfter(final User managerAfter) {
		this.managerAfter = managerAfter;
	}

	/**
	 * @return the managerBefore
	 */
	public User getManagerBefore() {
		return managerBefore;
	}

	/**
	 * @param managerBefore the managerBefore to set
	 */
	public void setManagerBefore(final User managerBefore) {
		this.managerBefore = managerBefore;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(final String message) {
		this.message = StringUtils.filterFckInput(message);
	}

	/**
	 * @return the oldFaqEntryConnectionAfter
	 */
	public OldFaqEntry getOldFaqEntryConnectionAfter() {
		return oldFaqEntryConnectionAfter;
	}

	/**
	 * @param oldFaqEntryConnectionAfter the oldFaqEntryConnectionAfter to set
	 */
	public void setOldFaqEntryConnectionAfter(final OldFaqEntry oldFaqEntryConnectionAfter) {
		this.oldFaqEntryConnectionAfter = oldFaqEntryConnectionAfter;
	}

	/**
	 * @return the oldFaqPartConnectionAfter
	 */
	public OldFaqPart getOldFaqPartConnectionAfter() {
		return oldFaqPartConnectionAfter;
	}

	/**
	 * @param oldFaqPartConnectionAfter the oldFaqPartConnectionAfter to set
	 */
	public void setOldFaqPartConnectionAfter(final OldFaqPart oldFaqPartConnectionAfter) {
		this.oldFaqPartConnectionAfter = oldFaqPartConnectionAfter;
	}

	/**
	 * @return the priorityLevelAfter
	 */
	public int getPriorityLevelAfter() {
		return priorityLevelAfter;
	}

	/**
	 * @param priorityLevelAfter the priorityLevelAfter to set
	 */
	public void setPriorityLevelAfter(final int priorityLevelAfter) {
		this.priorityLevelAfter = priorityLevelAfter;
	}

	/**
	 * @return the priorityLevelBefore
	 */
	public int getPriorityLevelBefore() {
		return priorityLevelBefore;
	}

	/**
	 * @param priorityLevelBefore the priorityLevelBefore to set
	 */
	public void setPriorityLevelBefore(final int priorityLevelBefore) {
		this.priorityLevelBefore = priorityLevelBefore;
	}

	/**
	 * @return the scopeAfter
	 */
	public String getScopeAfter() {
		return scopeAfter;
	}

	/**
	 * @param scopeAfter the scopeAfter to set
	 */
	public void setScopeAfter(final String scopeAfter) {
		this.scopeAfter = StringUtils.nullIfEmpty(scopeAfter);
	}

	/**
	 * @return the scopeBefore
	 */
	public String getScopeBefore() {
		return scopeBefore;
	}

	/**
	 * @param scopeBefore the scopeBefore to set
	 */
	public void setScopeBefore(final String scopeBefore) {
		this.scopeBefore = StringUtils.nullIfEmpty(scopeBefore);
	}

	/**
	 * @return the spentTimeAfter
	 */
	public long getSpentTimeAfter() {
		return spentTimeAfter;
	}

	/**
	 * @param spentTimeAfter the spentTimeAfter to set
	 */
	public void setSpentTimeAfter(final long spentTimeAfter) {
		this.spentTimeAfter = spentTimeAfter;
	}

	/**
	 * @return the spentTimeBefore
	 */
	public long getSpentTimeBefore() {
		return spentTimeBefore;
	}

	/**
	 * @param spentTimeBefore the spentTimeBefore to set
	 */
	public void setSpentTimeBefore(final long spentTimeBefore) {
		this.spentTimeBefore = spentTimeBefore;
	}

	/**
	 * @return the statusAfter
	 */
	public String getStatusAfter() {
		return statusAfter;
	}

	/**
	 * @param statusAfter the statusAfter to set
	 */
	public void setStatusAfter(final String statusAfter) {
		this.statusAfter = StringUtils.nullIfEmpty(statusAfter);
	}

	/**
	 * @return the statusBefore
	 */
	public String getStatusBefore() {
		return statusBefore;
	}

	/**
	 * @param statusBefore the statusBefore to set
	 */
	public void setStatusBefore(final String statusBefore) {
		this.statusBefore = StringUtils.nullIfEmpty(statusBefore);
	}

	/**
	 * @return the ticketOwnerAfter
	 */
	public User getTicketOwnerAfter() {
		return ticketOwnerAfter;
	}

	/**
	 * @param ticketOwnerAfter the ticketOwnerAfter to set
	 */
	public void setTicketOwnerAfter(final User ticketOwnerAfter) {
		this.ticketOwnerAfter = ticketOwnerAfter;
	}

	/**
	 * @return the ticketOwnerBefore
	 */
	public User getTicketOwnerBefore() {
		return ticketOwnerBefore;
	}

	/**
	 * @param ticketOwnerBefore the ticketOwnerBefore to set
	 */
	public void setTicketOwnerBefore(final User ticketOwnerBefore) {
		this.ticketOwnerBefore = ticketOwnerBefore;
	}

	/**
	 * @return the originBefore
	 */
	public String getOriginBefore() {
		return originBefore;
	}

	/**
	 * @param originBefore the originBefore to set
	 */
	public void setOriginBefore(final String originBefore) {
		this.originBefore = StringUtils.nullIfEmpty(originBefore);
	}

	/**
	 * @return the originAfter
	 */
	public String getOriginAfter() {
		return originAfter;
	}

	/**
	 * @param originAfter the originAfter to set
	 */
	public void setOriginAfter(final String originAfter) {
		this.originAfter = StringUtils.nullIfEmpty(originAfter);
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(final String filename) {
		this.filename = filename;
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

}
