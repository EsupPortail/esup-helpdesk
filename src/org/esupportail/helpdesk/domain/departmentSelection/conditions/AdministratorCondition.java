/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * A condition that is matched for managers.
 */
public class AdministratorCondition extends AbstractUserCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7355376611701450819L;

	/**
	 * Empty constructor (for Digester).
	 */
	public AdministratorCondition() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractUserCondition
	 * #isMatchedInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	protected boolean isMatchedInternal(
			@SuppressWarnings("unused")
			final DomainService domainService, 
			final User user) {
		if (user == null) {
			return false;
		}
		return user.getAdmin();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
	 */
	@Override
	public void checkInternal() {
		// nothing to check here
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<administrator />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "administrator";
	}

}
