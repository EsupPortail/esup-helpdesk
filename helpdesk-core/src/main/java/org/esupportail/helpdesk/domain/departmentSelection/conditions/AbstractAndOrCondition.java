/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions;

/**
 * This abstract class represents AND/OR operator.
 */
@SuppressWarnings("serial")
public abstract class AbstractAndOrCondition extends AbstractCondition {

	/**
	 * a list of conditions.
	 */
	private List<Condition> conditions;
	
	/**
	 * Empty constructor (for Digester).
	 */
	protected AbstractAndOrCondition() {
		super();
		this.conditions = new ArrayList<Condition>();
	}

	/**
	 * @return the operator used by toString()
	 */
	protected abstract String getToStringOperator();

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (conditions.isEmpty()) {
			return "<" + getToStringOperator() + " />";
		}
		if (conditions.size() == 1) {
			return conditions.get(0).toString();
		}
		String str = "<" + getToStringOperator() + ">";
		for (Condition condition : conditions) {
			str += condition;
		}
		str += "</" + getToStringOperator() + ">";
		return str;
	}

	/**
	 * Add a condition.
	 * @param condition the condition
	 */
	public void addCondition(
			final Condition condition) {
		conditions.add(condition);
	}

	/**
	 * @return the check error message
	 */
	protected abstract String getCheckErrorMessage();

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
	 */
	@Override
	public void checkInternal() throws DepartmentSelectionCompileError {
		if (conditions.isEmpty()) {
			throw new DepartmentSelectionCompileError(getCheckErrorMessage());
		}
	}

	/**
	 * @throws DepartmentSelectionCompileError 
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#compileInternal(
	 * org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions)
	 */
	@Override
	public void compileInternal(
			final UserDefinedConditions userDefinedConditions) 
	throws DepartmentSelectionCompileError {
		for (Condition condition : conditions) {
			condition.compile(userDefinedConditions);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getSubConditions()
	 */
	@Override
	public List<Condition> getSubConditions() {
		return conditions;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition
	 * #refactorNamedConditions(java.lang.String, java.lang.String)
	 */
	@Override
	public void refactorNamedConditions(
			final String oldName, 
			final String newName) {
		for (Condition condition : conditions) {
			condition.refactorNamedConditions(oldName, newName);
		}
	}

}
