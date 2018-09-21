/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.actions;

import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.departmentSelection.Result;

/**
 * an IAction implementation that returns nothing but says that the next rules should not be evaluated.
 */
public class StopAction extends AbstractAction {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2246710599623079598L;

	/**
	 * Constructor.
	 */
	public StopAction() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.AbstractAction#evalInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.departmentSelection.Result)
	 */
	@Override
	public List<Department> evalInternal(
			@SuppressWarnings("unused")
			final DomainService domainService, 
			final Result result,
			final boolean evaluateCondition) {
		if(evaluateCondition == false) {
			return null;
		}
		result.stopAfterThisRule();
		return null;
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
		return "<stop" + forToString() + " />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "stop";
	}

}
