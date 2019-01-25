/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.FaqScope;
import org.esupportail.helpdesk.domain.TicketScope;

/**
 * The class that represents departments.
 */
public class Department extends AbstractTicketContainer implements Comparable<Department> {

    /**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5313850626797320523L;

	/**
     * True if the department is enabled.
     */
    private boolean enabled;

    /**
     * True if the managers must fill the time spent when closing a ticket.
     */
    private boolean spentTimeNeeded;

    /**
     * Free field for filtering the departments seen by the users.
     */
    private String filter;
    
    /**
     * The default FAQ scope.
     */
    private String defaultFaqScope;

    /**
     * The effective default FAQ scope.
     */
    private String effectiveDefaultFaqScope;
    
    /**
     * The computer url builder name.
     */
    private String computerUrlBuilderName;
    
    /**
     * The real department.
     */
    private Department realDepartment;
    
    /**
     * List of the not visibles categories.
     */
    private List<Category> categoriesNotVisibles;
    

    /**
     * To set the service confidential.
     */
    private Boolean srvConfidential;


    /**
     * Bean constructor.
     */
    public Department() {
    	super();
    	this.categoriesNotVisibles = new ArrayList<Category>();
    }

    /**
     * Copy.
     * @param department the department to copy
     */
    public Department(final Department department) {
    	super(department);
    	this.enabled = department.enabled;
    	this.spentTimeNeeded = department.spentTimeNeeded;
    	this.filter = department.filter;
    	this.defaultFaqScope = department.defaultFaqScope;
    	this.effectiveDefaultFaqScope = department.effectiveDefaultFaqScope;
    	this.realDepartment = department.realDepartment;
    	this.computerUrlBuilderName = department.computerUrlBuilderName;
    	this.categoriesNotVisibles = department.categoriesNotVisibles;
    	this.srvConfidential = department.srvConfidential;

    }

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Department)) {
			return false;
		}
		return ((Department) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

	/**
	 * @param d 
	 * @return an int.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final Department d) {
		if (d == null) {
			return 0;
		}
		return getOrder() - d.getOrder();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[" + toStringInternal() 
		+ ", enabled=[" + enabled + "]"
		+ ", filter=[" + filter + "]"
		+ ", defaultFaqScope=[" + defaultFaqScope + "]"
		+ ", effectiveDefaultFaqScope=[" + effectiveDefaultFaqScope + "]"
		+ ", spentTimeNeeded=[" + spentTimeNeeded + "]"
		+ "]";
	}

	/**
	 * Compute the effective default ticket scope (using default policy if needed).
	 * @param defaultValue 
	 * @return true if the object needs to be updated.
	 */
	public boolean computeEffectiveDefaultTicketScope(final String defaultValue) {
		String oldEffectiveTicketScope = getEffectiveDefaultTicketScope();
		if (!getDefaultTicketScope().equals(TicketScope.DEFAULT)) {
			setEffectiveDefaultTicketScope(getDefaultTicketScope());
		} else {
			setEffectiveDefaultTicketScope(defaultValue);
		}
		return !(getEffectiveDefaultTicketScope().equals(oldEffectiveTicketScope));
	}
	
	/**
	 * Compute the effective default FAQ scope (using default policy if needed).
	 * @param defaultValue 
	 * @return true if the object needs to be updated.
	 */
	public boolean computeEffectiveDefaultFaqScope(final String defaultValue) {
		String oldScope = getDefaultFaqScope();
		String oldEffectiveScope = getEffectiveDefaultFaqScope();
		if (getDefaultFaqScope() == null) {
			setDefaultFaqScope(FaqScope.DEFAULT);
		}
		if (!getDefaultFaqScope().equals(FaqScope.DEFAULT)) {
			setEffectiveDefaultFaqScope(getDefaultFaqScope());
		} else {
			setEffectiveDefaultFaqScope(defaultValue);
		}
		return !(getDefaultFaqScope().equals(oldScope)) 
		|| !(getEffectiveDefaultFaqScope().equals(oldEffectiveScope));
	}
	
	/**
	 * @see org.esupportail.helpdesk.domain.beans.TicketContainer#getEffectiveIcon()
	 */
	@Override
	public Icon getEffectiveIcon() {
		return getIcon();
	}

	/**
	 * @return true if the category is virtual.
	 */
	public boolean isVirtual() {
		return realDepartment != null;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the spentTimeNeeded
	 */
	public boolean isSpentTimeNeeded() {
		return spentTimeNeeded;
	}

	/**
	 * @param spentTimeNeeded the spentTimeNeeded to set
	 */
	public void setSpentTimeNeeded(final boolean spentTimeNeeded) {
		this.spentTimeNeeded = spentTimeNeeded;
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(final String filter) {
		this.filter = StringUtils.nullIfEmpty(filter);
	}

	/**
	 * @return the defaultFaqScope
	 */
	public String getDefaultFaqScope() {
		return defaultFaqScope;
	}

	/**
	 * @param defaultFaqScope the defaultFaqScope to set
	 */
	public void setDefaultFaqScope(final String defaultFaqScope) {
		this.defaultFaqScope = defaultFaqScope;
	}

	/**
	 * @return the effectiveDefaultFaqScope
	 */
	public String getEffectiveDefaultFaqScope() {
		return effectiveDefaultFaqScope;
	}

	/**
	 * @param effectiveDefaultFaqScope the effectiveDefaultFaqScope to set
	 */
	public void setEffectiveDefaultFaqScope(final String effectiveDefaultFaqScope) {
		this.effectiveDefaultFaqScope = effectiveDefaultFaqScope;
	}

	/**
	 * @return the realDepartment
	 */
	public Department getRealDepartment() {
		return realDepartment;
	}

	/**
	 * @param realDepartment the realDepartment to set
	 */
	public void setRealDepartment(final Department realDepartment) {
		this.realDepartment = realDepartment;
	}

	/**
	 * @return the computerUrlBuilderName
	 */
	public String getComputerUrlBuilderName() {
		return computerUrlBuilderName;
	}

	/**
	 * @param computerUrlBuilderName the computerUrlBuilderName to set
	 */
	public void setComputerUrlBuilderName(final String computerUrlBuilderName) {
		this.computerUrlBuilderName = StringUtils.nullIfEmpty(computerUrlBuilderName);
	}

	public List<Category> getCategoriesNotVisibles() {
		if(this.categoriesNotVisibles == null){
			this.categoriesNotVisibles = new ArrayList<Category>();
		}
		return categoriesNotVisibles;
	}

	public void setCategoriesNotVisibles(List<Category> categoriesNotVisibles) {
		this.categoriesNotVisibles = categoriesNotVisibles;
	}

	/**
	 * Add departments to the result set.
	 * @param deps
	 */
	public void addCategorieNotVisible(final List<Category> categoriesNotVisible) {
		if(categoriesNotVisibles == null){
			categoriesNotVisibles = new ArrayList<Category>();
		}
		categoriesNotVisibles.addAll(categoriesNotVisible);
	}

	public Boolean getSrvConfidential() {
		return srvConfidential;
	}

	public void setSrvConfidential(Boolean srvConfidential) {
		this.srvConfidential = srvConfidential;
	}
}
