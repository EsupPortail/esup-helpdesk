/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * A condition that is matched for managers.
 */
public class DepartmentManagerCondition extends AbstractUserCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4656634272847334905L;

	/**
	 * a department label.
	 */
	private String label;
	
	/**
	 * Empty constructor (for Digester).
	 */
	public DepartmentManagerCondition() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractUserCondition
	 * #isMatchedInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	protected boolean isMatchedInternal(
			final DomainService domainService, 
			final User user) {
		if (user == null) {
			return false;
		}
		if (label == null) {
			return domainService.isDepartmentManager(user);
		}
		Department department = domainService.getDepartmentByLabel(label);
		if (department == null) {
			return false;
		}
		return domainService.isDepartmentManager(department, user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
	 */
	@Override
	public void checkInternal() {
		// nothing to check here
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "<department-manager";
		if (label != null) {
			str += " label=\"" + label + "\"";
		}
		str += " />";
		return str;
	}

	/**
	 * @param label The label to set.
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "departmentManager";
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

}
