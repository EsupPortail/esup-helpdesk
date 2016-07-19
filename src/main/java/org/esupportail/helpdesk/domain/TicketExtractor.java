/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain; 

import java.io.Serializable;
import java.util.List;

import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.User;

/** 
 * A utility bean to filter tickets.
 */ 
public interface TicketExtractor extends Serializable {
	
	/**
	 * @param user 
	 * @param selectedManager 
	 * @param visibleDepartments
	 * @return the HQL query to use for the control panel.
	 */
	String getControlPanelQueryString(
			User user, 
			User selectedManager,
			List<Department> visibleDepartments,
			Boolean isInvitation);
	
	String getAllTicketsUserInvited(
			User user);

}

