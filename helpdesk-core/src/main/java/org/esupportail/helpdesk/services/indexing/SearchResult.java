/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing; 

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.Ticket;


/**
 * The interface of search results.
 */
public interface SearchResult extends Serializable {
	
	/**
	 * @return the score of the search result.
	 */
	int getScore();

	/**
	 * @return true if the search result corresponds to a ticket.
	 */
	boolean isTicketSearchResult();

	/**
	 * @return the corresponding ticket.
	 */
	Ticket getTicket();

	/**
	 * @return true if the search result corresponds to a FAQ.
	 */
	boolean isFaqSearchResult();

	/**
	 * @return the corresponding FAQ.
	 */
	Faq getFaq();

	/**
	 * @return true if the search result corresponds to an archived ticket.
	 */
	boolean isArchivedTicketSearchResult();

	/**
	 * @return the corresponding archived ticket.
	 */
	ArchivedTicket getArchivedTicket();

}
