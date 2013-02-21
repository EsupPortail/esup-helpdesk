package org.esupportail.helpdesk.domain.reporting;

import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The interface of invitation senders.
 */
public interface InvitationSender extends DomainServiceSettable {

	/**
	 * Send an invitation to a user for a ticket.
	 * @param author
	 * @param invitedUser
	 * @param ticket
	 * @return true if the invitation email has been sent.
	 */
	boolean sendInvitationEmail(
			User author,
			User invitedUser,
			Ticket ticket);

}