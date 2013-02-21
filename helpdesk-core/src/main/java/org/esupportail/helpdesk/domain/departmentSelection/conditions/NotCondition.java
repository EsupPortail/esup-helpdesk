/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions;

/**
 * The negation of another condition.
 */
public class NotCondition extends AbstractCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2485027962336737721L;

	/**
	 * the condition to negate.
	 */
	private Condition condition;
	
	/**
	 * Empty constructor (for Digester).
	 */
	public NotCondition() {
		super();
	}

	/**
	 * Set the condition.
	 * @param cond the condition to negate
	 * @throws DepartmentSelectionCompileError 
	 */
	public void addCondition(final Condition cond) throws DepartmentSelectionCompileError {
		if (condition != null) {
			throw new DepartmentSelectionCompileError(
					"<not> tags should be used with one nested condition only");
		}
		this.condition = cond;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#isMatchedInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User, 
	 * java.net.InetAddress)
	 */
	@Override
	protected boolean isMatchedInternal(
			final DomainService domainService, 
			final User user, 
			final InetAddress client) {
		return !condition.isMatched(domainService, user, client);
	}

	/**
	 * @throws DepartmentSelectionCompileError 
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
	 */
	@Override
	public void checkInternal() throws DepartmentSelectionCompileError {
		if (condition == null) {
			throw new DepartmentSelectionCompileError(
					"a nested condition should be used inside <not> tags");
		}	
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<not>" + condition + "</not>";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#compileInternal(
	 * org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions)
	 */
	@Override
	public void compileInternal(final UserDefinedConditions userDefinedConditions) 
	throws DepartmentSelectionCompileError {
		this.condition.compile(userDefinedConditions);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getSubConditions()
	 */
	@Override
	public List<Condition> getSubConditions() {
		List<Condition> result = new ArrayList<Condition>();
		result.add(condition);
		return result;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "not";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition
	 * #refactorNamedConditions(java.lang.String, java.lang.String)
	 */
	@Override
	public void refactorNamedConditions(
			final String oldName, 
			final String newName) {
		if (condition != null) {
			condition.refactorNamedConditions(oldName, newName);
		}
	}


}
