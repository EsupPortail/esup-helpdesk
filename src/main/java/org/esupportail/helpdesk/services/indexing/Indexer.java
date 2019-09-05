/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Timestamp;

import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.User;


/**
 * The interface of indexers.
 */
public interface Indexer extends Serializable {

	/**
	 * The doctype used when indexing tickets.
	 */
	String OPENED_TICKET_INDEX_DOCTYPE = "openedticket";

	/**
	 * The doctype used when indexing tickets.
	 */
	String CLOSED_TICKET_INDEX_DOCTYPE = "closedticket";

	/**
	 * The doctype used when indexing archived tickets.
	 */
	String ARCHIVED_TICKET_INDEX_DOCTYPE = "archivedticket";

	/**
	 * The doctype used when indexing FAQ entities.
	 */
	String FAQ_INDEX_DOCTYPE = "faq";

	/**
	 * Update the index.
	 * @param removeBefore
	 * @return the number of entities indexed.
	 */
	int updateIndex(
			boolean removeBefore);

	/**
	 * Remove the index.
	 */
	void removeIndex();

	/**
	 * Unlock the index.
	 */
	void unlockIndex();

	/**
	 * Search the index for a user.
	 * @param user
	 * @param client
	 * @param department
	 * @param tokens
	 * @param exprTokens
	 * @param orTokens
	 * @param notTokens
	 * @param searchType
	 * @param managerId
	 * @param ownerId
	 * @param userId
	 * @param maxResults
	 * @param sortByDate
	 * @param date1
	 * @param date2
	 * @return the search results.
	 * @throws IndexException
	 */
	SearchResults search(
			User user,
			InetAddress client,
			Department department,
			String tokens,
			String exprTokens,
			String orTokens,
			String notTokens,
			String searchType,
			String managerId,
			String ownerId,
			String userId,
			int maxResults,
			boolean sortByDate,
			Timestamp date1,
			Timestamp date2) throws IndexException;

	/**
	 * @return the total number of documents indexed.
	 */
	int getDocumentsNumber();

	/**
	 * @return the number of tickets indexed.
	 */
	int getTicketsNumber();

	/**
	 * @return the number of archived tickets indexed.
	 */
	int getArchivedTicketsNumber();

	/**
	 * @return the number of FAQs indexed.
	 */
	int getFaqsNumber();

	/**
	 * Test.
	 */
	void test();

}
