/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing;

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.Ticket;

/**
 * The interface of the providers of index ids.
 */
public interface IndexIdProvider extends Serializable {

	/**
	 * @return the index id for a ticket.
	 * @param ticket
	 */
	String getIndexId(Ticket ticket);

	/**
	 * @return the index id for a FAQ.
	 * @param faq
	 */
	String getIndexId(Faq faq);

	/**
	 * @return the index id for an archived ticket.
	 * @param archivedTicket
	 */
	String getIndexId(ArchivedTicket archivedTicket);

	/**
	 * @param indexId
	 * @return true if the given index id corresponds to a ticket.
	 */
	boolean isTicketIndexId(String indexId);

	/**
	 * @param indexId
	 * @return true if the given index id corresponds to an archived ticket.
	 */
	boolean isArchivedTicketIndexId(String indexId);

	/**
	 * @param indexId
	 * @return true if the given index id corresponds to a FAQ.
	 */
	boolean isFaqIndexId(String indexId);

	/**
	 * This method should only be called if isTicketIndexId() returns true.
	 * @param indexId
	 * @return the id of the ticket that corresponds to an index id or null of not well-formed.
	 */
	long getTicketIndexId(String indexId);

	/**
	 * This method should only be called if isFaqIndexId() returns true.
	 * @param indexId
	 * @return the id of the FAQ that corresponds to an index id or null of not well-formed.
	 */
	long getFaqIndexId(String indexId);

	/**
	 * This method should only be called if isArchivedTicketIndexId() returns true.
	 * @param indexId
	 * @return the id of the archived ticket that corresponds to an index id or null of not well-formed.
	 */
	long getArchivedTicketIndexId(String indexId);

}