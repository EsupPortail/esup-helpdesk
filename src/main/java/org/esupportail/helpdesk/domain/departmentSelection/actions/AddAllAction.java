/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.actions;

import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.departmentSelection.Result;

/**
 * an Action implementation that returns all the enabled departments.
 */
public class AddAllAction extends AbstractAction {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4038208804020329285L;

	/**
	 * Constructor.
	 */
	public AddAllAction() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.AbstractAction#evalInternal(
	 * org.esupportail.helpdesk.domain.DomainService, 
	 * org.esupportail.helpdesk.domain.departmentSelection.Result)
	 */
	@Override
	public List<Department> evalInternal(
			final DomainService domainService, 
			final Result result) {
		result.stopAfterThisRule();
		return domainService.getEnabledDepartments();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#compile()
	 */
	@Override
	public void compile() {
		// nothing to check here
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<add-all" + forToString() + " />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "addAll";
	}

}
