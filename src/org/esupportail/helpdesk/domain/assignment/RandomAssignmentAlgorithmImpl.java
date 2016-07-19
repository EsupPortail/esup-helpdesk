/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;


/**
 * An implementation of AssignmentAlgorithm that randomly assigns.
 */
public class RandomAssignmentAlgorithmImpl 
extends AbstractAssignmentAlgorithm {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4126354662422491343L;

	/**
	 * Constructor.
	 */
	public RandomAssignmentAlgorithmImpl() {
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
			final User excludedUser) {
		List<DepartmentManager> managers = 
			domainService.getEffectiveAvailableDepartmentManagers(ticket.getCategory());
		if (managers.isEmpty()) {
			return null;
		}
		List<DepartmentManager> allowedManagers = new ArrayList<DepartmentManager>();
		for (DepartmentManager manager : managers) {
			if (!manager.getUser().equals(excludedUser)) {
				allowedManagers.add(manager);
			}
		}
		if (allowedManagers.isEmpty()) {
			return null;
		}
		int selection = (int) Math.floor(allowedManagers.size() * Math.random());
		return new AssignmentResult(allowedManagers.get(selection).getUser(), null);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.assignment.AssignmentAlgorithm#getDescription(java.util.Locale)
	 */
	@Override
	public String getDescription(final Locale locale) {
		return getI18nService().getString("DOMAIN.ASSIGNMENT_ALGORITHM.RANDOM", locale);
	}

}
