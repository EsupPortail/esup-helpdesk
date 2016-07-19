/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.List;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.web.controllers.SessionController;

/** 
 * A paginator for managed departments.
 */ 
public class ManagedDepartmentPaginator extends AbstractDomainAwareListPaginator<Department> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8073857299553971568L;

	/**
	 * The session controller.
	 */
	private SessionController sessionController;
	
	/**
	 * Constructor.
	 */
	public ManagedDepartmentPaginator() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.beans.AbstractDomainAwareListPaginator#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(sessionController, 
				"property sessionController of class " 
				+ this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.commons.web.beans.ListPaginator#getData()
	 */
	@Override
	protected List<Department> getData() {
		return getDomainService().getManagedDepartmentsOrAllIfAdmin(sessionController.getCurrentUser());
	} 
	
	/**
	 * @return the sessionController
	 */
	protected SessionController getSessionController() {
		return sessionController;
	}

	/**
	 * @param sessionController the sessionController to set
	 */
	public void setSessionController(final SessionController sessionController) {
		this.sessionController = sessionController;
	}
	
}

