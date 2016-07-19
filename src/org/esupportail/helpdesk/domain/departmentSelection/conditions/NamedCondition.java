/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedCondition;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions;

/**
 * A named condition.
 */
public class NamedCondition extends AbstractCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7476576501336956133L;

	/**
	 * The name of the condition.
	 */
	private String name;
	
	/**
	 * the (real) user-defined condition.
	 */
	private UserDefinedCondition userDefinedCondition;
	
	/**
	 * Empty constructor (for Digester).
	 */
	public NamedCondition() {
		super();
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
		return userDefinedCondition.isMatched(domainService, user, client);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
	 */
	@Override
	public void checkInternal() throws DepartmentSelectionCompileError {
		if (name == null) {
			throw new DepartmentSelectionCompileError(
					"<named-condition> tags should have a 'name' attribute");
		}	
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<named-condition name=\"" + name + "\" />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#compileInternal(
	 * org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions)
	 */
	@Override
	public void compileInternal(
			final UserDefinedConditions userDefinedConditions) 
	throws DepartmentSelectionCompileError {
		userDefinedCondition = userDefinedConditions.getUserDefinedCondition(name);
		userDefinedCondition.setUsed();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition
	 * #refactorNamedConditions(java.lang.String, java.lang.String)
	 */
	@Override
	public void refactorNamedConditions(
			final String oldName, 
			final String newName) {
		if (oldName.equals(name)) {
			name = newName;
		}
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getSubConditions()
	 */
	@Override
	public List<Condition> getSubConditions() {
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "named";
	}

	/**
	 * @return the userDefinedCondition
	 */
	public UserDefinedCondition getUserDefinedCondition() {
		return userDefinedCondition;
	}

	/**
	 * @param userDefinedCondition the userDefinedCondition to set
	 */
	public void setUserDefinedCondition(final UserDefinedCondition userDefinedCondition) {
		this.userDefinedCondition = userDefinedCondition;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
