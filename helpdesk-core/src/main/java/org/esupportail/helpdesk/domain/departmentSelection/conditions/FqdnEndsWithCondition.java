/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;

/**
 * A condition that is matched when the client's FQDN ends with a given suffix.
 */
public class FqdnEndsWithCondition extends AbstractClientCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8907939809530328878L;

	/**
	 * The suffix the client should end with.
	 */
	private String suffix;
	
	/**
	 * Empty constructor (for Digester).
	 */
	public FqdnEndsWithCondition() {
		super();
	}

	/**
	 * Set the suffix.
	 * @param suffix a string
	 */
	public void setSuffix(final String suffix) {
		this.suffix = suffix;
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
		return client.getHostName().endsWith(suffix);
	}
	
	/**
	 * @throws DepartmentSelectionCompileError 
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
	 */
	@Override
	public void checkInternal() throws DepartmentSelectionCompileError {
		if (suffix == null) {
			throw new DepartmentSelectionCompileError(
					"<fqdn-ends-with> tags should have a 'suffix' attribute");
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<fqdn-ends-with suffix=\"" + suffix + "\" />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "fqdnEndsWith";
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

}
