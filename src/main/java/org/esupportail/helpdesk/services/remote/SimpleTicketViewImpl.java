/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.remote; 

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import org.esupportail.helpdesk.domain.beans.Ticket;


/**
 * The tickets used by the web service.
 */
public class SimpleTicketViewImpl implements Serializable, SimpleTicketView {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5508246461900979659L;

	/**
	 * The ticket label.
	 */
	private String label;

	/**
	 * The ticket department (label only).
	 */
	private String department;

	/**
	 * The ticket category (label only).
	 */
	private String category;
	
	/**
	 * The ticket subject.
	 */
	private String subject;
	
	/**
	 * The ticket creation date (as a string).
	 */
	private String creation;
	
	/**
	 * The ticket status (as a string).
	 */
	private String status;
	
	/**
	 * The ticket owner (as a string).
	 */
	private String owner;
	
	/**
	 * The ticket deep URL.
	 */
	private String deepLink;
	
	/**
	 * True when the ticket has been viewed by the user since its last change.
	 */
	private String isViewed;
	
	/**
	 * The actions for the ticket.
	 */
	private List<SimpleActionView> actions;

	/**
	 * True when using the user interface.
	 */
	private boolean userInterface;

	/**
	 * @param ticket
	 * @param actions
	 * @param viewUrl
	 * @param isViewed
	 * @param userInterface
	 */
	public SimpleTicketViewImpl(final Ticket ticket, final List<SimpleActionView> actions, final String viewUrl, final String isViewed, final boolean userInterface) {
		this.setActions(actions);
		this.setCategory(ticket.getCategory().getLabel());
		this.setLabel(ticket.getLabel());
		//this.setCreation(ticket.getCreationDate().toGMTString());
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("dd MMM yyyy HH:mm:ss z");
		this.setCreation(sdf.format(ticket.getCreationDate()));
		this.setOwner(ticket.getOwner().getDisplayName());
		this.setDepartment(ticket.getDepartment().getLabel());
		this.setStatus(ticket.getStatus());
		this.setViewed(isViewed);
		this.setDeepLink(viewUrl);
		this.setUserInterface(userInterface);
	}

	
	/**
	 * @see org.esupportail.helpdesk.services.remote.SimpleTicketView#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.SimpleTicketView#getDepartment()
	 */
	@Override
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department
	 */
	public void setDepartment(final String department) {
		this.department = department;
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.SimpleTicketView#getCategory()
	 */
	@Override
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 */
	public void setCategory(final String category) {
		this.category = category;
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.SimpleTicketView#getSubject()
	 */
	@Override
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.SimpleTicketView#getCreation()
	 */
	@Override
	public String getCreation() {
		return creation;
	}

	/**
	 * @param creation
	 */
	public void setCreation(final String creation) {
		this.creation = creation;
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.SimpleTicketView#getStatus()
	 */
	@Override
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	@Override
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 */
	public void setOwner(final String owner) {
		this.owner = owner;
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.SimpleTicketView#getDeepLink()
	 */
	@Override
	public String getDeepLink() {
		return deepLink;
	}

	/**
	 * @param deepLink
	 */
	public void setDeepLink(final String deepLink) {
		this.deepLink = deepLink;
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.SimpleTicketView#getViewed()
	 */
	@Override
	public String getViewed() {
		return isViewed;
	}

	/**
	 * @param viewedString
	 */
	public void setViewed(final String viewedString) {
		this.isViewed = viewedString;
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.SimpleTicketView#getActions()
	 */
	@Override
	public List<SimpleActionView> getActions() {
		return actions;
	}

	/**
	 * @param actions
	 */
	public void setActions(final List<SimpleActionView> actions) {
		this.actions = actions;
	}
	/**
	 * @see org.esupportail.helpdesk.services.remote.SimpleTicketView#isUserInterface()
	 */
	@Override
	public boolean isUserInterface() {
		return userInterface;
	}

	/**
	 * @param userInterface
	 */
	public void setUserInterface(final boolean userInterface) {
		this.userInterface = userInterface;
	}

}
