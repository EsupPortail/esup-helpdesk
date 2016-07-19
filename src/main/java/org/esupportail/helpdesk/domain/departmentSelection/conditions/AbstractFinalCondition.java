/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.util.List;

import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions;

/**
 * An abstract condition that is final (i.e. does not rely on any other condition).
 */
@SuppressWarnings("serial")
abstract class AbstractFinalCondition extends AbstractCondition {

	/**
	 * Constructor.
	 */
	protected AbstractFinalCondition() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#compileInternal(
	 * org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions)
	 */
	@Override
	public final void compileInternal(
			@SuppressWarnings("unused")
			final UserDefinedConditions userDefinedConditions) {
		// nothing to compile here
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getSubConditions()
	 */
	@Override
	public List<Condition> getSubConditions() {
		return null;
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
		// nothing to refactor
	}
}
