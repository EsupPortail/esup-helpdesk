package org.esupportail.helpdesk.services.feed.imap;

import java.io.Serializable;

import javax.mail.Message;

import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.services.feed.ErrorHolder;

/**
 * @author lusl0338
 * Read email message and put it as action to ticket
 */
public interface ActionMessageReader extends Serializable {
	/**
	 * @param message Email message
	 * @param ticket Ticket
	 * @param errorHolder Error holder
	 * @return User who sent email
	 */
	User readMessage(
			Message message,
			Ticket ticket,
			ErrorHolder errorHolder);
}
