/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;


/**
 * A false condition.
 */
public class TrueCondition extends AbstractConstantCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6508198448941673375L;

	/**
	 * Empty constructor (for Digester).
	 */
	public TrueCondition() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractConstantCondition
	 * #isMatchedInternal()
	 */
	@Override
	protected boolean isMatchedInternal() {
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<true />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "true";
	}

}
