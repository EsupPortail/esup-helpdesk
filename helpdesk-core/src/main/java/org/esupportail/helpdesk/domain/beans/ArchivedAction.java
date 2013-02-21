/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.sql.Timestamp;

import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.ActionScope;
import org.esupportail.helpdesk.domain.ActionType;
import org.esupportail.helpdesk.domain.TicketStatus;

/**
 * The representation of a ticket action. */
public class ArchivedAction extends AbstractAchivedTicketInfo {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4563738726460407434L;

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
     * Label of ticket before action.
     */
    private String scopeBefore;

    /**
     * Label of ticket after action.
     */
    private String scopeAfter;

    /**
     * Department of ticket before action.
     */
    private Department departmentBefore;

    /**
     * Department of ticket after action.
     */
    private Department departmentAfter;

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
    private String categoryBeforeLabel;

    /**
     * Category after action.
     */
    private String categoryAfterLabel;

    /**
     * Invited user action.
     */
    private User invitedUser;
    
    /**
     * The name of the uploaded file.
     */
    private String filename;

    /**
     * The date when the ticket should be recalled.
     */
    private Timestamp recallDate;
    
	/**
	 * Default constructor.
	 */
	protected ArchivedAction() {
		super();
	}
	
	/**
	 * General constructor of action. (Set the needed not null attributes). 
	 * @param action
	 * @param archivedTicket
	 */
	public ArchivedAction(
			final Action action,
			final ArchivedTicket archivedTicket) {
		super(action.getUser(), archivedTicket, action.getEffectiveScope());
		setActionType(action.getActionType());
		setComputerAfter(action.getComputerAfter());
		setComputerBefore(action.getComputerBefore());
		if (action.getCategoryAfter() != null) {
			setCategoryAfterLabel(action.getCategoryAfter().getLabel());
		}
		if (action.getCategoryBefore() != null) {
			setCategoryBeforeLabel(action.getCategoryBefore().getLabel());
		}
		setDate(action.getDate());
		setDepartmentAfter(action.getDepartmentAfter());
		setDepartmentBefore(action.getDepartmentBefore());
		setInvitedUser(action.getInvitedUser());
		setLabelAfter(action.getLabelAfter());
		setLabelBefore(action.getLabelBefore());
		setManagerAfter(action.getManagerAfter());
		setManagerBefore(action.getManagerBefore());
		setMessage(action.getMessage());
		setPriorityLevelAfter(action.getPriorityLevelAfter());
		setPriorityLevelBefore(action.getPriorityLevelBefore());
		setScopeAfter(action.getScopeAfter());
		setScopeBefore(action.getScopeBefore());
		setSpentTimeAfter(action.getSpentTimeAfter());
		setSpentTimeBefore(action.getSpentTimeBefore());
		setStatusAfter(action.getStatusAfter());
		setStatusBefore(action.getStatusBefore());
		setTicketOwnerAfter(action.getTicketOwnerAfter());
		setTicketOwnerBefore(action.getTicketOwnerBefore());
		setFilename(action.getFilename());
		setRecallDate(action.getRecallDate());
	}
	
	/**
	 * Constructor. 
	 * @param owner 
	 * @param archivedTicket
	 * @param effectiveScope
	 */
	private ArchivedAction(
			final User owner, 
			final ArchivedTicket archivedTicket, 
			final String effectiveScope) {
		super(owner, archivedTicket, effectiveScope);
	}
	
	/**
	 * @param archivedTicket
	 * @param targetDepartment
	 * @return an archived action to move a archived ticket to another department
	 */
	public static ArchivedAction changeDepartmentArchivedAction(
			final ArchivedTicket archivedTicket,
			final Department targetDepartment) {
		ArchivedAction archivedAction = new ArchivedAction(null, archivedTicket, ActionScope.DEFAULT);
		archivedAction.setActionType(ActionType.CHANGE_DEPARTMENT);
		archivedAction.setDepartmentAfter(targetDepartment);
		archivedAction.setDepartmentBefore(archivedTicket.getDepartment());
		archivedAction.setStatusBefore(TicketStatus.ARCHIVED);
		archivedAction.setStatusAfter(TicketStatus.ARCHIVED);
		return archivedAction;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ArchivedAction)) {
			return false;
		}
		return ((ArchivedAction) obj).getId() == getId();
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
		this.message = StringUtils.nullIfEmpty(message);
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
	 * @return the categoryBeforeLabel
	 */
	public String getCategoryBeforeLabel() {
		return categoryBeforeLabel;
	}

	/**
	 * @param categoryBeforeLabel the categoryBeforeLabel to set
	 */
	public void setCategoryBeforeLabel(final String categoryBeforeLabel) {
		this.categoryBeforeLabel = categoryBeforeLabel;
	}

	/**
	 * @return the categoryAfterLabel
	 */
	public String getCategoryAfterLabel() {
		return categoryAfterLabel;
	}

	/**
	 * @param categoryAfterLabel the categoryAfterLabel to set
	 */
	public void setCategoryAfterLabel(final String categoryAfterLabel) {
		this.categoryAfterLabel = categoryAfterLabel;
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
