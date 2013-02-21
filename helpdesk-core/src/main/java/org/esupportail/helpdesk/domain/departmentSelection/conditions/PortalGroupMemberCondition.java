/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;

/**
 * A condition that is matched when the user is member of a uPortal group.
 */
public class PortalGroupMemberCondition extends AbstractUserCondition {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8232261174137609716L;

	/**
	 * The name of the group.
	 */
	private String name;

	/**
	 * The id of the group.
	 */
	private String id;

	/**
	 * Empty constructor (for digester).
	 */
	public PortalGroupMemberCondition() {
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
		if (id != null) {
			return domainService.getUserStore().isMemberOfPortalGroup(user, id);
		}
		return domainService.getUserStore().isMemberOfPortalDistinguishedGroup(user, name);
	}

	/**
	 * @throws DepartmentSelectionCompileError
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.AbstractCondition#checkInternal()
	 */
	@Override
	public void checkInternal() throws DepartmentSelectionCompileError {
		if ((this.name == null && this.id == null) || (this.name != null && this.id != null)) {
			throw new DepartmentSelectionCompileError(
					"<portal-group-member> tags should have exactly "
					+ "one 'id' or one 'name' attribute");
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "<portal-group-member ";
		if (id != null) {
			str += "id=\"" + id + "\"";
		} else {
			str += "name=\"" + name + "\"";
		}
		return str + " />";
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "portalGroupMember";
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}
