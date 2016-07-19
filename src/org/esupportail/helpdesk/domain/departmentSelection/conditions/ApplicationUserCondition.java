/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * A condition that is matched for application users.
 */
public class ApplicationUserCondition extends AbstractUserCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6187995641999252224L;

	/**
	 * Empty constructor (for Digester).
	 */
	public ApplicationUserCondition() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractUserCondition
	 * #isMatchedInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	protected boolean isMatchedInternal(
			final DomainService domainService,
			final User user) {
		return domainService.getUserStore().isApplicationUser(user);
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
		return "<application-user />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "applicationUser";
	}

}
