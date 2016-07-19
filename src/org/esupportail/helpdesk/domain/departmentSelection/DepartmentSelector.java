/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The interface of department selectors.
 */
public interface DepartmentSelector extends Serializable {

	/**
	 * Used for parameter type when looking for departments visible on ticket creation.
	 */
	int TICKET_CREATION_SELECTION = 1;
	/**
	 * Used for parameter type when looking for departments visible on the control panel.
	 */
	int TICKET_VIEW_SELECTION = 2;
	/**
	 * Used for parameter type when looking for departments visible for FAQs.
	 */
	int FAQ_VIEW_SELECTION = 3;
	/**
	 * Used for parameter type when looking for departments visible for search.
	 */
	int SEARCH_SELECTION = 4;

	/**
	 * return the list of the departments that a user will see on ticket creation.
	 * @param domainService
	 * @param user the user
	 * @param client the client
	 * @return a list of departments.
	 */
	List<Department> getTicketCreationDepartments(
			DomainService domainService,
			User user,
			InetAddress client);

	/**
	 * return the list of the departments that a user will see on control panel.
	 * @param domainService
	 * @param user the user
	 * @param client the client
	 * @return a list of departments.
	 */
	List<Department> getTicketViewDepartments(
			DomainService domainService,
			User user,
			InetAddress client);

	/**
	 * return the list of the departments that a user will see for the FAQs.
	 * @param domainService
	 * @param user the user
	 * @param client the client
	 * @return a list of departments.
	 */
	List<Department> getFaqViewDepartments(
			DomainService domainService,
			User user,
			InetAddress client);

	/**
	 * @return the configuration as a string.
	 */
	String exportConfig();

}
