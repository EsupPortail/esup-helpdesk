/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;


/**
 * A condition that is matched when the user's LDAP attributes matches a given name/value pattern.
 */
public class LdapAttributeLikeCondition extends AbstractAttributeCondition {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5049042748143228800L;

	/**
	 * Empty constructor (for digester).
	 */
	public LdapAttributeLikeCondition() {
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
	 * #hasPatternAttribute()
	 */
	@Override
	protected boolean hasPatternAttribute() {
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
		return "ldapAttributeLike";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAttributeCondition#getTagName()
	 */
	@Override
	protected String getTagName() {
		return "ldap-attribute-like";
	}

}
