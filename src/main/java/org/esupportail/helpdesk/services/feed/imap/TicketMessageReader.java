/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed.imap;

import java.io.Serializable;

import javax.mail.Message;

import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.services.feed.ErrorHolder;
import org.esupportail.helpdesk.services.feed.imap.spam.SpamFilter;

/**
 * The interface of ticket message readers.
 */
public interface TicketMessageReader extends Serializable {

	/**
	 * Extract a ticket from a message.
	 * @param message 
	 * @param address 
	 * @param creationDepartment 
	 * @param category 
	 * @param spamFilter 
	 * @param deleteSpam 
	 * @param spamCategory 
	 * @param errorHolder 
	 * @return the ticket created.
	 */
	Ticket read(
			Message message,
			String address,
			Department creationDepartment, 
			Category category,
			SpamFilter spamFilter,
			boolean deleteSpam,
			Category spamCategory,
			ErrorHolder errorHolder);

}