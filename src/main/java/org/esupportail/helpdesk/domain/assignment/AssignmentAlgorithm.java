/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.assignment;

import java.io.Serializable;
import java.util.Locale;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;


/**
 * The interface of assignment algorithms.
 */
public interface AssignmentAlgorithm extends Serializable {
	
	/**
	 * @param locale 
	 * @return a short description of the algorithm.
	 */
	String getDescription(Locale locale);
	
	/**
	 * @param domainService 
	 * @param ticket 
	 * @param excludedUser 
	 * @return the result for a ticket. 
	 */
	AssignmentResult getAssignmentResult(
			DomainService domainService, 
			Ticket ticket, 
			User excludedUser);
	
}
