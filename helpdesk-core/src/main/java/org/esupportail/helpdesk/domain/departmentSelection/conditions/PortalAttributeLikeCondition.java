/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;


/**
 * A condition that is matched when the user's uPortal attributes matches a given name/value pattern.
 */
public class PortalAttributeLikeCondition extends AbstractAttributeCondition {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2179263535855775048L;

	/**
	 * Empty constructor (for digester).
	 */
	public PortalAttributeLikeCondition() {
		super();
	}
	
	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "portalAttributeLike";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAttributeCondition
	 * #hasNameAttribute()
	 */
	@Override
	protected boolean hasNameAttribute() {
		return true;
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
	 * #isPortalBased()
	 */
	@Override
	protected boolean isPortalBased() {
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAttributeCondition#getTagName()
	 */
	@Override
	protected String getTagName() {
		return "portal-attribute-like";
	}

}
