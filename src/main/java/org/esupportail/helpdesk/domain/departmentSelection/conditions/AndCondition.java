/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * This class represents the AND operator.
 */
public class AndCondition extends AbstractAndOrCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7614840442411541068L;

	/**
	 * Empty constructor (for Digester).
	 */
	public AndCondition() {
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
			final InetAddress client) {
		for (Condition condition : getSubConditions()) {
			if (!condition.isMatched(domainService, user, client)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAndOrCondition
	 * #getCheckErrorMessage()
	 */
	@Override
	protected String getCheckErrorMessage() {
		return "nested conditions should be used inside <and> tags";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractAndOrCondition
	 * #getToStringOperator()
	 */
	@Override
	protected String getToStringOperator() {
		return "and";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "and";
	}

}
