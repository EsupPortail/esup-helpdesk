/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.List;

import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;

/** 
 * A paginator for department manager.
 */ 
public class DepartmentManagerPaginator extends AbstractDomainAwareListPaginator<DepartmentManager> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2231011398236027865L;

	/**
	 * The department.
	 */
	private Department department;
	
	/**
	 * Constructor.
	 */
	public DepartmentManagerPaginator() {
		super();
	}

	/**
	 * @see org.esupportail.commons.web.beans.ListPaginator#getData()
	 */
	@Override
	protected List<DepartmentManager> getData() {
		return getDomainService().getDepartmentManagers(department);
	}

	/**
	 * @param department
	 * @return the object itself
	 */
	public DepartmentManagerPaginator setDepartment(final Department department) {
		this.department = department;
		forceReload();
		return this;
	}

}

