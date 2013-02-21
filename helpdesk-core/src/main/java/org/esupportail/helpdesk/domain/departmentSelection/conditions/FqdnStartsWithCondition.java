/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;

/**
 * A condition that is matched when the client's FQDN starts with a given prefix.
 */
public final class FqdnStartsWithCondition extends AbstractClientCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6498571942665320989L;

	/**
	 * The prefix the client should start with.
	 */
	private String prefix;
	
	/**
	 * Empty constructor (for Digester).
	 */
	public FqdnStartsWithCondition() {
		super();
	}

	/**
	 * Set the prefix.
	 * @param prefix a string
	 */
	public void setPrefix(final String prefix) {
		this.prefix = prefix;
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
		return client.getHostName().startsWith(prefix);
	}
	
	/**
	 * @throws DepartmentSelectionCompileError 
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
	 */
	@Override
	public void checkInternal() throws DepartmentSelectionCompileError {
		if (prefix == null) {
			throw new DepartmentSelectionCompileError(
					"<fqdn-starts-with> tags should have a 'prefix' attribute");
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<fqdn-starts-with prefix=\"" + prefix + "\" />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "fqdnStartsWith";
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

}
