package org.esupportail.helpdesk.domain.reporting;

import java.util.List;

import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;


/**
 * The interface of monitoring senders.
 */
public interface MonitoringSender extends DomainServiceSettable {

	/**
	 * Send alerts for a ticket.
	 * @param author
	 * @param ticket
	 * @param excludedUsers
	 * @param expiration 
	 */
	void ticketMonitoringSendAlerts(
			User author,
			Ticket ticket,
			List<User> excludedUsers,
			boolean expiration);

	/**
	 * Send alerts for three actions.
	 * @param author
	 * @param action1
	 * @param action2
	 * @param action3
	 * @param expiration 
	 */
	void ticketMonitoringSendAlerts(
			User author,
			Action action1,
			Action action2,
			Action action3,
			boolean expiration);

	/**
	 * Send alerts for two actions.
	 * @param author
	 * @param action1
	 * @param action2
	 * @param expiration 
	 */
	void ticketMonitoringSendAlerts(
			User author,
			Action action1,
			Action action2,
			boolean expiration);

	/**
	 * Send alerts for one action.
	 * @param author
	 * @param action
	 * @param expiration 
	 */
	void ticketMonitoringSendAlerts(
			User author,
			Action action,
			boolean expiration);

	/**
	 * @return the users that monitor a ticket.
	 * @param ticket
	 */
	List<User> getMonitoringUsers(Ticket ticket, Boolean onlyMandatoryUsers);

}