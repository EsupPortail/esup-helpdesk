/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;


/**
 * A condition that is matched when the user's uPortal attributes exactly matches a given name/value pair.
 */
public final class PortalAttributeEqCondition extends AbstractAttributeCondition {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6765328180485346491L;

	/**
	 * Empty constructor (for digester).
	 */
	public PortalAttributeEqCondition() {
		super();
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
	 * #hasValueAttribute()
	 */
	@Override
	protected boolean hasValueAttribute() {
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
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "portalAttributeEq";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAttributeCondition#getTagName()
	 */
	@Override
	protected String getTagName() {
		return "portal-attribute-eq";
	}

}
