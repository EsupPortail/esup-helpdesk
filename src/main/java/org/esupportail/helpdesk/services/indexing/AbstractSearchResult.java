/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing; 

import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.Ticket;

/**
 * An abstract search result.
 */
@SuppressWarnings("serial")
public abstract class AbstractSearchResult implements SearchResult {

	/**
	 * The unique id of the search result.
	 */
	private String indexId;

	/**
	 * The score of the search result.
	 */
	private int score;

	/**
	 * Constructor.
	 * @param indexId the unique index id
	 * @param score the score
	 */
	protected AbstractSearchResult(
			final String indexId,
			final int score) {
		super();
		this.indexId = indexId;
		this.score = score;
	}

	/**
	 * @return the indexId
	 */
	protected String getIndexId() {
		return indexId;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.SearchResult#getScore()
	 */
	@Override
	public int getScore() {
		return score;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.SearchResult#isTicketSearchResult()
	 */
	@Override
	public boolean isTicketSearchResult() {
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.SearchResult#getTicket()
	 */
	@Override
	public Ticket getTicket() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.SearchResult#isFaqSearchResult()
	 */
	@Override
	public boolean isFaqSearchResult() {
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.SearchResult#getFaq()
	 */
	@Override
	public Faq getFaq() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.SearchResult#isArchivedTicketSearchResult()
	 */
	@Override
	public boolean isArchivedTicketSearchResult() {
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.SearchResult#getArchivedTicket()
	 */
	@Override
	public ArchivedTicket getArchivedTicket() {
		throw new UnsupportedOperationException();
	}

}
