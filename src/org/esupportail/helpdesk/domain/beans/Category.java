/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.TicketScope;
 


/**
 * The class that represents categories.
 */
public class Category extends AbstractTicketContainer {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 9200534915162339376L;

	/**
     * The department of the category.
     */
    private Department department;

    /**
     * The parent category.
     */
    private Category parent;

    /**
     * The real category.
     */
    private Category realCategory;

    /**
	 * The current state of the assignment algorithm.
     */
    private String assignmentAlgorithmState;
    
    /**
     * True if the members of the category are inherited (from its parent).
     */
    private Boolean inheritMembers;
    
    /**
     * v2 not null stuff.
     */
    @SuppressWarnings("unused")
	private Integer oldDefaultPriorityLevel;
    
    /**
     * True if users can put tickets in the category. 
     */
    private Boolean addNewTickets;

    /**
     * True if the FAQ links of the category are inherited (from its parent).
     */
    private Boolean inheritFaqLinks;
    
	/**
     * Bean constructor.
     */
    public Category() {
    	super();
    	this.oldDefaultPriorityLevel = 0;
    	this.inheritMembers = Boolean.TRUE;
    	this.inheritFaqLinks = Boolean.TRUE;
    }

    /**
     * Copy.
     * @param c 
     */
    public Category(final Category c) {
    	super(c);
    	this.oldDefaultPriorityLevel = 0;
    	this.department = c.department;
    	this.parent = c.parent;
    	this.realCategory = c.realCategory;
    	this.inheritMembers = c.inheritMembers;
    	this.inheritFaqLinks = c.inheritFaqLinks;
    	this.assignmentAlgorithmState = c.assignmentAlgorithmState;
    	this.addNewTickets = c.addNewTickets;
    }

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Category)) {
			return false;
		}
		return ((Category) obj).getId() == getId();
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
		return getClass().getSimpleName() + "#" + hashCode() + "[" + toStringInternal() 
		+ ", department=[" + department + "]"
		+ ", assignmentAlgorithmState=[" + assignmentAlgorithmState + "]"
		+ ", inheritMembers=[" + inheritMembers + "]"
		+ ", inheritFaqLinks=[" + inheritFaqLinks + "]"
		+ "]";
	}

	/**
	 * Compute the effective scope (using default policy if needed).
	 * @param updateObject true to update the object, false just to test
	 * @return true if the object needs to be updated.
	 */
	public boolean computeEffectiveDefaultTicketScope(final boolean updateObject) {
		String oldEffectiveTicketScope = getEffectiveDefaultTicketScope();
		String oldTicketScope = getDefaultTicketScope();
		String newEffectiveTicketScope = oldEffectiveTicketScope;
		String newTicketScope = oldTicketScope;
		if (newTicketScope == null) {
			newTicketScope = TicketScope.DEFAULT;
		}
		if (!newTicketScope.equals(TicketScope.DEFAULT)) {
			newEffectiveTicketScope = newTicketScope;
		} else if (parent == null) {
			newEffectiveTicketScope = getDepartment().getEffectiveDefaultTicketScope();
		} else {
			newEffectiveTicketScope = parent.getEffectiveDefaultTicketScope();
		}
		boolean updateNeeded = !newEffectiveTicketScope.equals(oldEffectiveTicketScope) 
		|| !newTicketScope.equals(oldTicketScope);
		if (updateNeeded && updateObject) {
			setDefaultTicketScope(newTicketScope);
			setEffectiveDefaultTicketScope(newEffectiveTicketScope);
		}
		return updateNeeded;
	}
	
	/**
	 * @return the default default scope.
	 */
	public String getDefaultDefaultTicketScope() {
		if (parent != null) {
			return parent.getEffectiveDefaultTicketScope();
		}
		return this.getDepartment().getEffectiveDefaultTicketScope();
	}
	
	/**
	 * @return the effective default priority (using default policy if needed).
	 */
	public int getEffectiveDefaultTicketPriority() {
		if (getDefaultTicketPriority() != DomainService.DEFAULT_PRIORITY_VALUE) {
			return getDefaultTicketPriority();
		}
		return getDefaultDefaultTicketPriority();
	}
	
	/**
	 * @return the default default priority.
	 */
	public int getDefaultDefaultTicketPriority() {
		if (parent != null) {
			return parent.getEffectiveDefaultTicketPriority();
		}
		return this.getDepartment().getDefaultTicketPriority();
	}
	
	/**
	 * @return the effective default message (using default policy if needed).
	 */
	public String getEffectiveDefaultTicketMessage() {
		if (getDefaultTicketMessage() != null) {
			return getDefaultTicketMessage();
		}
		if (parent != null) {
			return parent.getEffectiveDefaultTicketMessage();
		}
		return this.getDepartment().getDefaultTicketMessage();
	}
	
	/**
	 * @return the effective default label (using default policy if needed).
	 */
	public String getEffectiveDefaultTicketLabel() {
		if (getDefaultTicketLabel() != null) {
			return getDefaultTicketLabel();
		}
		if (parent != null) {
			return parent.getEffectiveDefaultTicketLabel();
		}
		return this.getDepartment().getDefaultTicketLabel();
	}
	
	/**
	 * @return the effective assignment algorithm name (using default policy if needed).
	 */
	public String getEffectiveAssignmentAlgorithmName() {
		if (getAssignmentAlgorithmName() != null) {
			return getAssignmentAlgorithmName();
		}
		if (parent != null) {
			return parent.getEffectiveAssignmentAlgorithmName();
		}
		return this.getDepartment().getAssignmentAlgorithmName();
	}
	
	/**
	 * @return the default monitoring email.
	 */
	public String getDefaultMonitoringEmail() {
		if (parent != null) {
			return parent.getEffectiveMonitoringEmail();
		}
		return this.getDepartment().getMonitoringEmail();
	}
	
	/**
	 * @return the effective monitoring email (using default policy if needed).
	 */
	public String getEffectiveMonitoringEmail() {
		if (!getInheritMonitoring()) {
			return getMonitoringEmail();
		}
		return getDefaultMonitoringEmail();
	}
	
	/**
	 * @return the default monitoring email auth type.
	 */
	public String getDefaultMonitoringEmailAuthType() {
		if (parent != null) {
			return parent.getEffectiveMonitoringEmailAuthType();
		}
		return this.getDepartment().getMonitoringEmailAuthType();
	}
	
	/**
	 * @return the effective monitoring email auth type (using default policy if needed).
	 */
	public String getEffectiveMonitoringEmailAuthType() {
		if (!getInheritMonitoring()) {
			return getMonitoringEmailAuthType();
		}
		return getDefaultMonitoringEmailAuthType();
	}
	
	/**
	 * @return the default monitoring level.
	 */
	public Integer getDefaultMonitoringLevel() {
		if (!getInheritMonitoring()) {
			return getMonitoringLevel();
		}
		if (parent != null) {
			return parent.getEffectiveMonitoringLevel();
		}
		return this.getDepartment().getMonitoringLevel();
	}
	
	/**
	 * @return the effective monitoring level (using default policy if needed).
	 */
	public Integer getEffectiveMonitoringLevel() {
		if (!getInheritMonitoring()) {
			return getMonitoringLevel();
		}
		return getDefaultMonitoringLevel();
	}
	
	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getEffectiveIcon()
	 */
	@Override
	public Icon getEffectiveIcon() {
		if (getIcon() != null) {
			return getIcon();
		}
		if (parent != null) {
			return parent.getEffectiveIcon();
		}
		return getDepartment().getEffectiveIcon();
	}

	/**
	 * @return true if the category is virtual.
	 */
	public boolean isVirtual() {
		return realCategory != null;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(final Department department) {
		this.department = department;
	}

	/**
	 * @return the parent
	 */
	public Category getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(final Category parent) {
		this.parent = parent;
	}

	/**
	 * @return the realCategory
	 */
	public Category getRealCategory() {
		return realCategory;
	}

	/**
	 * @param realCategory the realCategory to set
	 */
	public void setRealCategory(final Category realCategory) {
		this.realCategory = realCategory;
	}

	/**
	 * @return the assignmentAlgorithmState
	 */
	public String getAssignmentAlgorithmState() {
		return assignmentAlgorithmState;
	}

	/**
	 * @param assignmentAlgorithmState the assignmentAlgorithmState to set
	 */
	public void setAssignmentAlgorithmState(final String assignmentAlgorithmState) {
		this.assignmentAlgorithmState = assignmentAlgorithmState;
	}

	/**
	 * @return the inheritMembers
	 */
	public Boolean getInheritMembers() {
		return inheritMembers;
	}

	/**
	 * @param inheritMembers the inheritMembers to set
	 */
	public void setInheritMembers(final Boolean inheritMembers) {
		this.inheritMembers = inheritMembers;
	}

	/**
	 * @return the oldDefaultPriorityLevel
	 */
	public Integer getOldDefaultPriorityLevel() {
		return 0;
	}

	/**
	 * @param oldDefaultPriorityLevel the oldDefaultPriorityLevel to set
	 */
	public void setOldDefaultPriorityLevel(
			@SuppressWarnings("unused")
			final Integer oldDefaultPriorityLevel) {
		this.oldDefaultPriorityLevel = 0;
	}

    /**
	 * @return the addNewTickets
	 */
	public Boolean getAddNewTickets() {
		if (addNewTickets == null) {
			return true;
		}
		return addNewTickets;
	}

	/**
	 * @param addNewTickets the addNewTickets to set
	 */
	public void setAddNewTickets(final Boolean addNewTickets) {
		this.addNewTickets = addNewTickets;
	}

	/**
	 * @return the inheritFaqLinks
	 */
	public Boolean getInheritFaqLinks() {
		if (inheritFaqLinks == null) {
			return true;
		}
		return inheritFaqLinks;
	}

	/**
	 * @param inheritFaqLinks the inheritFaqLinks to set
	 */
	public void setInheritFaqLinks(final Boolean inheritFaqLinks) {
		this.inheritFaqLinks = inheritFaqLinks;
	}

}
