/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.assignment;

import java.util.Locale;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;


/**
 * A basic implementation of AssignmentAlgorithm that does no assignment.
 */
public class NobodyAssignmentAlgorithmImpl 
extends AbstractAssignmentAlgorithm {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6397726703195885041L;

	/**
	 * Constructor.
	 */
	public NobodyAssignmentAlgorithmImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.assignment.AbstractAssignmentAlgorithm#getAssignmentResultInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.Ticket, 
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public AssignmentResult getAssignmentResultInternal(
			@SuppressWarnings("unused")
			final DomainService domainService,
			@SuppressWarnings("unused")
			final Ticket ticket,
			@SuppressWarnings("unused")
			final User excludedUser) {
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.assignment.AssignmentAlgorithm#getDescription(java.util.Locale)
	 */
	@Override
	public String getDescription(final Locale locale) {
		return getI18nService().getString("DOMAIN.ASSIGNMENT_ALGORITHM.NOBODY", locale);
	}

}
