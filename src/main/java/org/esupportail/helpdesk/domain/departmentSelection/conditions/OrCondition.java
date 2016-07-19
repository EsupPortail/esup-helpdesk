/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * This class represents the OR operator.
 */
public class OrCondition extends AbstractAndOrCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3300575778971763618L;

	/**
	 * Empty constructor (for Digester).
	 */
	public OrCondition() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#isMatchedInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User, 
	 * java.net.InetAddress)
	 */
	@Override
	public boolean isMatchedInternal(
			final DomainService domainService,
			final User user,
			final InetAddress client) {
		for (Condition condition : getSubConditions()) {
			if (condition.isMatched(domainService, user, client)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAndOrCondition
	 * #getCheckErrorMessage()
	 */
	@Override
	protected String getCheckErrorMessage() {
		return "nested conditions should be used inside <or> tags";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAndOrCondition
	 * #getToStringOperator()
	 */
	@Override
	protected String getToStringOperator() {
		return "or";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "or";
	}

}
