/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions;

/**
 * This abstract condition logs the evaluation.
 */
@SuppressWarnings("serial")
public abstract class AbstractDeprecatedCondition implements Condition {

	/**
	 * Constructor.
	 */
	protected AbstractDeprecatedCondition() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#isMatched(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User, 
	 * java.net.InetAddress)
	 */
	@Override
	public final boolean isMatched(
			@SuppressWarnings("unused")
			final DomainService domainService, 
			@SuppressWarnings("unused")
			final User user,
			@SuppressWarnings("unused")
			final InetAddress client) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#compile(
	 * org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions)
	 */
	@Override
	public void compile(
			@SuppressWarnings("unused")
			final UserDefinedConditions userDefinedConditions) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getSubConditions()
	 */
	@Override
	public List<Condition> getSubConditions() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition
	 * #refactorNamedConditions(java.lang.String, java.lang.String)
	 */
	@Override
	public void refactorNamedConditions(
			@SuppressWarnings("unused")
			final String oldName, 
			@SuppressWarnings("unused")
			final String newName) {
		throw new UnsupportedOperationException();
	}

}
