package org.esupportail.helpdesk.services.remote;

import java.util.List;

/**
 * the tickets used by the web service.
 */
public interface SimpleTicketView {

	/**
	 * @return the ticket label.
	 */
	public String getLabel();

	/**
	 * @return the ticket department (label only).
	 */
	public String getDepartment();

	/**
	 * @return the ticket category (label only).
	 */
	public String getCategory();

	/**
	 * @return the ticket subject.
	 */
	public String getSubject();

	/**
	 * @return the ticket creation date (as a string).
	 */
	public String getCreation();

	/**
	 * @return the ticket status.
	 */
	public String getStatus();

	/**
	 * @return the ticket owner (as a string).
	 */
	public String getOwner();

	/**
	 * @return the ticket deep URL.
	 */
	public String getDeepLink();
	
	/**
	 * @return true if the ticket has been viewed by the user since its last change.
	 */
	public String getViewed();

	/**
	 * @return the ticket (simple) actions.
	 */
	public List<SimpleActionView> getActions();
	
	/**
	 * @return true when using the user interface.
	 */
	public boolean isUserInterface();

}