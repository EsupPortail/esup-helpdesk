/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;

/**
 * A condition that is matched when the client's FQDN equals a given value.
 */
public class FqdnEqCondition extends AbstractClientCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1763522981738520124L;

	/**
	 * The value.
	 */
	private String value;
	
	/**
	 * Empty constructor (for Digester).
	 */
	public FqdnEqCondition() {
		super();
	}

	/**
	 * Set the value.
	 * @param value a string
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractClientCondition
	 * #isMatchedInternal(
	 * org.esupportail.helpdesk.domain.DomainService, java.net.InetAddress)
	 */
	@Override
	public boolean isMatchedInternal(
			@SuppressWarnings("unused")
			final DomainService domainService,
			final InetAddress client) {
		return client.getHostName().equals(value);
	}
	
	/**
	 * @throws DepartmentSelectionCompileError 
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
	 */
	@Override
	public void checkInternal() throws DepartmentSelectionCompileError {
		if (value == null) {
			throw new DepartmentSelectionCompileError("<fqdn-eq> tags should have a 'value' attribute");
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<fqdn-eq value=\"" + value + "\" />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "fqdnEq";
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

}
