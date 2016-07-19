/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

import java.io.Serializable;
import java.net.InetAddress;

import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * A utility bean to navigate through tickets.
 */
public interface TicketNavigator extends Serializable {

	/**
	 * @param user
	 * @param ticket
	 * @param client
	 * @return the navigation for the current user and ticket
	 */
	TicketNavigation getNavigation(
			User user,
			Ticket ticket,
			InetAddress client);

}

