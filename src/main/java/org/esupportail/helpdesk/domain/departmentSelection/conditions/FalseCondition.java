/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;


/**
 * A false condition.
 */
public final class FalseCondition extends AbstractConstantCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -675060294793913954L;

	/**
	 * Empty constructor (for Digester).
	 */
	public FalseCondition() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractConstantCondition
	 * #isMatchedInternal()
	 */
	@Override
	protected boolean isMatchedInternal() {
		return false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<false />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "false";
	}

}
