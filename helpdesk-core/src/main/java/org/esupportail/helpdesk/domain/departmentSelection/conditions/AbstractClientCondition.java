/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * An abstract condition that relies on the client only (not the user).
 */
@SuppressWarnings("serial")
abstract class AbstractClientCondition extends AbstractFinalCondition {

	/**
	 * Constructor.
	 */
	protected AbstractClientCondition() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#isMatchedInternal(
	 * org.esupportail.helpdesk.domain.DomainService, 
	 * org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress)
	 */
	@Override
	protected boolean isMatchedInternal(
			final DomainService domainService,
			@SuppressWarnings("unused")
			final User user,
			final InetAddress client) {
    	if (client == null) {
    		return false;
    	}
		return isMatchedInternal(domainService, client);
	}
	
	/**
	 * is the condition matched?
	 * @param domainService
	 * @param client the client
	 * @return a boolean.
	 */
	protected abstract boolean isMatchedInternal(
			DomainService domainService,
			InetAddress client);
	
}
