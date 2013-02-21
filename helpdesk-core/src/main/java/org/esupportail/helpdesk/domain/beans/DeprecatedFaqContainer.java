/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import org.esupportail.helpdesk.domain.FaqScope;


/**
 * A class that represents a FAQ container.
 * @deprecated
 */
@SuppressWarnings("deprecation")
@Deprecated
public class DeprecatedFaqContainer extends AbstractDeprecatedFaqEntity {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 326770169288130798L;

	/**
     * The department, or null.
     */
    private Department department;
    
    /**
     * The old v2 content.
     */
    private String oldContent;

	/**
	 * Bean constructor.
	 */
	public DeprecatedFaqContainer() {
		super();
		oldContent = " ";
	}

	/**
	 * Bean constructor.
	 * @param fc 
	 */
	public DeprecatedFaqContainer(final DeprecatedFaqContainer fc) {
		super(fc);
		department = fc.department;
		oldContent = " ";
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DeprecatedFaqContainer)) {
			return false;
		}
		return ((DeprecatedFaqContainer) obj).getId() == getId();
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
		+ toStringInternal()
		+ ", department=" + department 
		+ "]";
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
		if (FaqScope.DEPARTMENT.equals(newScope) && getDepartment() == null) {
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
	 * @return the oldContent
	 */
	public String getOldContent() {
		return oldContent;
	}

	/**
	 * @param oldContent the oldContent to set
	 */
	public void setOldContent(final String oldContent) {
		this.oldContent = oldContent;
	}
	
}