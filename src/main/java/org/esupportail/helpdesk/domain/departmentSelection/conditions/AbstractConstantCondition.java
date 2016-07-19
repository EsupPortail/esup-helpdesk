/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * A false condition.
 */
@SuppressWarnings("serial")
public abstract class AbstractConstantCondition extends AbstractFinalCondition {

	/**
	 * Constructor.
	 */
	protected AbstractConstantCondition() {
		super();
	}

	/**
	 * @return true of false.
	 */
	protected abstract boolean isMatchedInternal();

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#isMatchedInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User, 
	 * java.net.InetAddress)
	 */
	@Override
	protected boolean isMatchedInternal(
			@SuppressWarnings("unused")
			final DomainService domainService, 
			@SuppressWarnings("unused")
			final User user, 
			@SuppressWarnings("unused")
			final InetAddress client) {
		return isMatchedInternal();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
	 */
	@Override
	public void checkInternal() {
		// nothing to check for constants
	}

}
