/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.assignment;

import java.util.List;
import java.util.Locale;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;


/**
 * A basic implementation of AssignmentAlgorithm that assigns to the first manager available.
 */
public class FirstAssignmentAlgorithmImpl 
extends AbstractAssignmentAlgorithm {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -9033286162905761298L;

	/**
	 * Constructor.
	 */
	public FirstAssignmentAlgorithmImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.assignment.AbstractAssignmentAlgorithm#getAssignmentResultInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.Ticket, 
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public AssignmentResult getAssignmentResultInternal(
			final DomainService domainService,
			final Ticket ticket,
			@SuppressWarnings("unused")
			final User excludedUser) {
		List<DepartmentManager> managers = 
			domainService.getEffectiveAvailableDepartmentManagers(ticket.getCategory());
		if (managers.isEmpty()) {
			return null;
		}
		return new AssignmentResult(managers.get(0).getUser(), null);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.assignment.AssignmentAlgorithm#getDescription(java.util.Locale)
	 */
	@Override
	public String getDescription(final Locale locale) {
		return getI18nService().getString("DOMAIN.ASSIGNMENT_ALGORITHM.FIRST", locale);
	}

}
