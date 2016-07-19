/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * A condition that is matched for CAS users.
 */
public class CasUserCondition extends AbstractUserCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 4383861153280248036L;

	/**
	 * Empty constructor (for Digester).
	 */
	public CasUserCondition() {
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
		return domainService.getUserStore().isCasUser(user);
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
		return "<cas-user />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "casUser";
	}

}
