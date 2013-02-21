/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;


/**
 * A condition that is matched when the user's LDAP attributes exactly matches a given name/value pair.
 */
public final class LdapAttributeEqCondition extends AbstractAttributeCondition {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6352905166322246046L;

	/**
	 * Empty constructor (for digester).
	 */
	public LdapAttributeEqCondition() {
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
	 * #isLdapBased()
	 */
	@Override
	protected boolean isLdapBased() {
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "ldapAttributeEq";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAttributeCondition#getTagName()
	 */
	@Override
	protected String getTagName() {
		return "ldap-attribute-eq";
	}

}
