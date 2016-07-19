/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.List;

import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentInvitation;

/** 
 * A paginator for department invitations.
 */ 
public class DepartmentInvitationPaginator extends AbstractDomainAwareListPaginator<DepartmentInvitation> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 9188778978106797355L;

	/**
	 * The department.
	 */
	private Department department;
	
	/**
	 * Constructor.
	 */
	public DepartmentInvitationPaginator() {
		super();
	}

	/**
	 * @see org.esupportail.commons.web.beans.ListPaginator#getData()
	 */
	@Override
	protected List<DepartmentInvitation> getData() {
		return getDomainService().getDepartmentInvitations(department);
	}

	/**
	 * @param department
	 * @return the object itself
	 */
	public DepartmentInvitationPaginator setDepartment(final Department department) {
		this.department = department;
		forceReload();
		return this;
	}

}

