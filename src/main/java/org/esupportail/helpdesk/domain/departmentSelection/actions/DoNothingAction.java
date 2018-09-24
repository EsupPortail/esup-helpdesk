/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.actions;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.departmentSelection.Result;

/**
 * an Action that does nothing.
 */
public class DoNothingAction implements Action {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7355669991698323494L;

	/**
	 * Constructor.
	 */
	public DoNothingAction() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#eval(
	 * org.esupportail.helpdesk.domain.DomainService, 
	 * org.esupportail.helpdesk.domain.departmentSelection.Result)
	 */
	@Override
	public final void eval(
			@SuppressWarnings("unused")
			final DomainService domainService, 
			@SuppressWarnings("unused")
			final Result result) {
		// do nothing
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#evalForType(int)
	 */
	@Override
	public boolean evalForType(
			@SuppressWarnings("unused")
			final int type) {
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#compile()
	 */
	@Override
	public void compile() {
		// do nothing
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<do-nothing />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "doNothing";
	}

}
