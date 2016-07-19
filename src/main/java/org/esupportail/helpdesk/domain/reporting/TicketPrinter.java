package org.esupportail.helpdesk.domain.reporting;

import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;


/**
 * The interface of ticket printers.
 */
public interface TicketPrinter extends DomainServiceSettable {

	/**
	 * @return the ready-to-print content of a ticket.
	 * @param user
	 * @param ticket
	 */
	String getTicketPrintContent(
			User user,
			Ticket ticket);

}