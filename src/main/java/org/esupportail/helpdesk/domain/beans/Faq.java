/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.FaqScope;

/**
 * A class that represents FAQ.
 */
public class Faq implements Serializable {

    /**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6293258427308921468L;

	/**
     * Primary key.
     */
    private long id;

    /**
     * The parent, or null.
     */
    private Faq parent;

	/**
     * The department (null for a global root faq).
     */
    private Department department;
    
	/**
	 * The label.
	 */
	private String label;
	
	/**
	 * The scope.
	 */
	private String scope;
	
	/**
	 * The order.
	 */
	private int order;
	
	/**
	 * The content.
	 */
	private String content;

    /**
     * Date of last modification.
     */
    private Timestamp lastUpdate;
    
    /**
     * The effective scope.
     */
    private String effectiveScope;
	
	/**
	 * Bean constructor.
	 */
	public Faq() {
		super();
		lastUpdate = new Timestamp(new Date().getTime());
		scope = FaqScope.DEFAULT;
	}

	/**
	 * Bean constructor.
	 * @param faq 
	 */
	public Faq(final Faq faq) {
		this();
		this.id = faq.id;
		this.parent = faq.parent;
		this.department = faq.department;
		this.label = faq.label;
		this.scope = faq.scope;
		this.order = faq.order;
		this.content = faq.content;
		this.lastUpdate = faq.lastUpdate;
		this.effectiveScope = faq.effectiveScope;
	}

	/**
	 * Bean constructor.
	 * @param faqContainer
	 * @param parent 
	 */
	@SuppressWarnings("deprecation")
	public Faq(
			final DeprecatedFaqContainer faqContainer,
			final Faq parent) {
		super();
		this.parent = parent;
		this.department = faqContainer.getDepartment();
		this.label = faqContainer.getLabel();
		this.scope = faqContainer.getScope();
		this.content = faqContainer.getContent();
		this.lastUpdate = faqContainer.getLastUpdate();
		this.effectiveScope = faqContainer.getEffectiveScope();
	}

	/**
	 * Bean constructor.
	 * @param faqEntry
	 * @param parent 
	 */
	@SuppressWarnings("deprecation")
	public Faq(
			final DeprecatedFaqEntry faqEntry,
			final Faq parent) {
		super();
		this.parent = parent;
		this.department = faqEntry.getParent().getDepartment();
		this.label = faqEntry.getLabel();
		this.scope = faqEntry.getScope();
		this.content = faqEntry.getContent();
		this.lastUpdate = faqEntry.getLastUpdate();
		this.effectiveScope = faqEntry.getEffectiveScope();
	}

	/**
	 * @return the object converted to string.
	 */
	@Override
	public String toString() {
		String result = getClass().getSimpleName() + "#" + hashCode() + "[";
		result += "id=[" + id + "]";
		result += ", department=";
		if (department == null) {
			result += "null";
		} else {
			result += "[" + department.getLabel() + "]";
		}
		result += ", label=[" + label + "]"
		+ ", scope=[" + scope + "]"
		+ ", order=[" + order + "]"
		+ ", content=[" + content + "]"
		+ ", lastUpdate=[" + lastUpdate + "]";
		result += "]";
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Faq)) {
			return false;
		}
		return ((Faq) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

	/**
	 * Compute the effective scope (using default policy if needed).
	 * @param updateObject true to update the object, false otherwise.
	 * @return true if the object needs to be updated.
	 */
	public boolean computeEffectiveScope(final boolean updateObject) {
		String oldScope = getScope();
		String oldEffectiveScope = getEffectiveScope();
		String newScope = oldScope;
		String newEffectiveScope = oldEffectiveScope;
		// get the parent effective scope
		String parentEffectiveScope;
		if (getParent() == null) {
			if (getDepartment() == null) {
				parentEffectiveScope = FaqScope.ALL;
			} else {
				parentEffectiveScope = getDepartment().getEffectiveDefaultFaqScope();
			}
		} else {
			parentEffectiveScope = getParent().getEffectiveScope();
		}
		// scope DEPARTMENT is set to MANAGER for root FAQs
		if (FaqScope.DEPARTMENT.equals(newScope) && getParent() == null && getDepartment() == null) {
			newScope = FaqScope.MANAGER;
		}
		// limit the scope regarding to the parent effective scope
		if (FaqScope.MANAGER.equals(parentEffectiveScope)) {
			if (!FaqScope.DEFAULT.equals(newScope)) {
				newScope = FaqScope.MANAGER;
			}
		} else if (FaqScope.DEPARTMENT.equals(parentEffectiveScope)) {
			if (!FaqScope.DEFAULT.equals(newScope) && !FaqScope.MANAGER.equals(newScope)) {
				newScope = FaqScope.DEPARTMENT;
			}
		} else if (FaqScope.AUTHENTICATED.equals(parentEffectiveScope)) {
			if (!FaqScope.DEFAULT.equals(newScope) 
					&& !FaqScope.MANAGER.equals(newScope) 
					&& !FaqScope.DEPARTMENT.equals(newScope)) {
				newScope = FaqScope.AUTHENTICATED;
			}
		}
		// now compute the effective scope
		if (!newScope.equals(FaqScope.DEFAULT)) {
			newEffectiveScope = newScope;
		} else {
			newEffectiveScope = parentEffectiveScope;
		}
		boolean updateNeeded = !(newEffectiveScope.equals(oldEffectiveScope)) || !(newScope.equals(oldScope));
		if (updateNeeded && updateObject) {
			setScope(newScope);
			setEffectiveScope(newEffectiveScope);
		}
		return updateNeeded;
	}
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(final String content) {
		this.content = StringUtils.filterFckInput(content);
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
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @return the lastUpdate
	 */
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(final Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * Set the last update now!
	 */
	public void setLastUpdateNow() {
		this.lastUpdate = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(final int order) {
		this.order = order;
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
	 * @return the parent
	 */
	public Faq getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(final Faq parent) {
		this.parent = parent;
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

}