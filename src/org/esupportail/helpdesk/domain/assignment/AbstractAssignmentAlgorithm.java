/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.assignment;

import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.springframework.beans.factory.InitializingBean;


/**
 * A basic implementation of AssignmentAlgorithm that does no assignment.
 */
@SuppressWarnings("serial")
public abstract class AbstractAssignmentAlgorithm implements AssignmentAlgorithm, InitializingBean {
	
	/**
	 * The i18n service.
	 */
	private I18nService i18nService; 
	
	/**
	 * Constructor.
	 */
	public AbstractAssignmentAlgorithm() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.i18nService, 
				"property i18nService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @param domainService 
	 * @param ticket 
	 * @param excludedUser 
	 * @return the assignment result.
	 * @see org.esupportail.helpdesk.domain.assignment.AssignmentAlgorithm#getAssignmentResult(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.Ticket, 
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	protected abstract AssignmentResult getAssignmentResultInternal(
			DomainService domainService, Ticket ticket, User excludedUser);

	/**
	 * @see org.esupportail.helpdesk.domain.assignment.AssignmentAlgorithm#getAssignmentResult(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.Ticket, 
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public AssignmentResult getAssignmentResult(
			final DomainService domainService,
			final Ticket ticket,
			final User excludedUser) {
		AssignmentResult result = getAssignmentResultInternal(domainService, ticket, excludedUser);
		if (result == null) {
			return new AssignmentResult(null, null);
		}
		User user = result.getUser(); 
		if (user == null) {
			return result;
		}
		if (user.equals(excludedUser)) {
			return new AssignmentResult(null, null);
		}
		// this will throw an exception if the user is not a manager 
		// of the department of the ticket.
		domainService.getDepartmentManager(ticket.getDepartment(), user);
		return result;
	}
	
	/**
	 * @return the i18nService
	 */
	protected I18nService getI18nService() {
		return i18nService;
	}

	/**
	 * @param service the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}

}
