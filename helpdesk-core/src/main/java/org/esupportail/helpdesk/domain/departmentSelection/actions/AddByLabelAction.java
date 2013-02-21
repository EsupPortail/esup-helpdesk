/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.actions;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.Result;

/**
 * an Action implementation that returns the departments corresponding to a label.
 */
public class AddByLabelAction extends AbstractAction {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8768334071452926323L;

	/**
	 * The label searched for.
	 */
	private String label;
	
	/**
	 * Empty constructor.
	 */
	public AddByLabelAction() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.AbstractAction#evalInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.departmentSelection.Result)
	 */
	@Override
	public List<Department> evalInternal(
			final DomainService domainService, 
			@SuppressWarnings("unused")
			final Result result) {
		List<Department> departments = new ArrayList<Department>();
		Department department = domainService.getDepartmentByLabel(label);
		if (department == null) {
			return null;
		}
		departments.add(department);
		return departments;
	}

	/**
	 * @param label The label to set.
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @throws DepartmentSelectionCompileError 
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.AbstractAction#compile()
	 */
	@Override
	public void compile() throws DepartmentSelectionCompileError {
		if (label == null) {
			throw new DepartmentSelectionCompileError(
					"<add-by-label> tags should have a 'label' attribute");
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<add-by-label label=\"" + label + "\"" + forToString() + " />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "addByLabel";
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

}
