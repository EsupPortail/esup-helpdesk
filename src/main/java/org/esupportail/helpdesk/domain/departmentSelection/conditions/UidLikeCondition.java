/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;


/**
 * A condition that is matched when the user's id matches a given pattern.
 */
public class UidLikeCondition extends AbstractAttributeCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4832410868190600996L;

	/**
	 * Empty constructor (for Digester).
	 */
	public UidLikeCondition() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAttributeCondition
	 * #hasPatternAttribute()
	 */
	@Override
	protected boolean hasPatternAttribute() {
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAttributeCondition
	 * #isUserIdBased()
	 */
	@Override
	protected boolean isUserIdBased() {
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "uidLike";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAttributeCondition#getTagName()
	 */
	@Override
	protected String getTagName() {
		return "uid-like";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAttributeCondition
	 * #setName(java.lang.String)
	 */
	@Override
	public void setName(
			@SuppressWarnings("unused")
			final String name) {
		throw new UnsupportedOperationException();
	}

}
