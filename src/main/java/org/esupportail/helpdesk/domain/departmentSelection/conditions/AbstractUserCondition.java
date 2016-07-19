/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * A condition that only relies on the user.
 */
@SuppressWarnings("serial")
public abstract class AbstractUserCondition extends AbstractFinalCondition {

	/**
	 * Constructor.
	 */
	protected AbstractUserCondition() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#isMatchedInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User, 
	 * java.net.InetAddress)
	 */
	@Override
	protected boolean isMatchedInternal(
			final DomainService domainService,
			final User user,
			@SuppressWarnings("unused")
			final InetAddress client) {
		if (user == null) {
			return false;
		}
		return isMatchedInternal(domainService, user);
	}

	/**
	 * is the condition matched?
	 * @param domainService
	 * @param user the user
	 * @return a boolean.
	 */
	protected abstract boolean isMatchedInternal(
			final DomainService domainService,
			final User user);

}
