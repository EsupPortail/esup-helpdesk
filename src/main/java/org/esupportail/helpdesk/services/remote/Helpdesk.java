/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.remote; 

import java.io.Serializable;
import java.util.List;

/**
 * The interface of the remote service.
 */
public interface Helpdesk extends Serializable {
	
	/**
	 * Add a ticket.
	 * @param authorId
	 * @param creationDepartmentId 
	 * @param categoryId 
	 * @param origin 
	 * @param label 
	 * @param computer 
	 * @param priorityLevel 
	 * @param message 
	 * @param ticketScope 
	 * @return the number of the ticket created.
	 */
	long addTicket(
			String authorId, 
			long creationDepartmentId,
			long categoryId, 
			String origin,
			String label,
			String computer, 
			int priorityLevel,
			String message, 
			String ticketScope);

	/**
	 * Cancel a ticket.
	 * @param authorId
	 * @param ticketId 
	 * @param message 
	 */
	void cancelTicket(
			String authorId, 
			long ticketId,
			String message);

	/**
	 * Close a ticket.
	 * @param authorId
	 * @param ticketId 
	 * @param message 
	 */
	void closeTicket(
			String authorId, 
			long ticketId,
			String message);

	/**
	 * @return the version number of the application.
	 */
	String getVersion();
	

	/**
	 * @param userId
	 * @return the involvement filter for the user control panel. 
	 */
	public String getControlPanelUserInvolvementFilter(final String userId);

	/**
	 * @param userId
	 * @return the involvement filter for the manager control panel. 
	 */
	public String getControlPanelManagerInvolvementFilter(final String userId);
	
	/**
	 * @return the user involvement filters as a list of strings.
	 */
	public List<String> getUserInvolvementFilters();

	/**
	 * @return the user involvement filters as a list of strings.
	 */
	public List<String> getManagerInvolvementFilters();
	
	/**
	 * @param userId
	 * @return 'true' if the user is a manager.
	 */
	public boolean isDepartmentManager(final String userId);
	
	
}
