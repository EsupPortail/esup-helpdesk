/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.dao.AbstractHibernateQueryPaginator;
import org.esupportail.commons.dao.HqlUtils;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.web.controllers.SessionController;

/** 
 * A paginator for the journal.
 */ 
public class JournalPaginator 
extends AbstractHibernateQueryPaginator<Action> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6049383923323536348L;

	/**
	 * The domain service.
	 */
	private DomainService domainService;
	
	/**
	 * The session controller.
	 */
	private SessionController sessionController;
	
	/**
	 * Constructor.
	 */
	public JournalPaginator() {
		super();
	}
	
	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.domainService, 
				"property domainService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.sessionController, 
				"property sessionController of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @see org.esupportail.commons.web.beans.AbstractPaginator#getPageSizeInternal()
	 */
	@Override
	public int getPageSizeInternal() {
		return sessionController.getCurrentUser().getJournalPageSize();
	}

	/**
	 * @see org.esupportail.commons.web.beans.AbstractPaginator#setPageSizeInternal(int)
	 */
	@Override
	protected void setPageSizeInternal(final int pageSize) {
		sessionController.getCurrentUser().setJournalPageSize(pageSize);
		domainService.updateUser(sessionController.getCurrentUser());
	}

	/**
	 * @see org.esupportail.commons.dao.AbstractHibernateQueryPaginator#getQueryString()
	 */
	@Override
	protected String getQueryString() {
		User currentUser = sessionController.getCurrentUser();
		if (currentUser == null) {
			return null;
		}
		List<Department> managedDepartments = domainService.getManagedDepartments(currentUser);
		if (managedDepartments.isEmpty()) {
			return null;
		}
		String departmentCondition;
		if (currentUser.getJournalDepartmentFilter() == null) {
			List<Long> departmentIds = new ArrayList<Long>();
			for (Department department : managedDepartments) {
				departmentIds.add(department.getId());
			}
			departmentCondition = HqlUtils.longIn("action.ticket.department.id", departmentIds);
		} else {
			departmentCondition = HqlUtils.equals(
					"action.ticket.department.id", 
					currentUser.getJournalDepartmentFilter().getId());
		}
		return HqlUtils.fromWhereOrderByDesc(
				Action.class.getSimpleName() + HqlUtils.AS_KEYWORD + "action",
				HqlUtils.and(
					departmentCondition,
					HqlUtils.alwaysTrue()),
					"action.date");
	}

	/**
	 * @return the current user.
	 */
	public User getCurrentUser() {
		return sessionController.getCurrentUser();
	}

	/**
	 * @param sessionController the sessionController to set
	 */
	public void setSessionController(final SessionController sessionController) {
		this.sessionController = sessionController;
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

}

