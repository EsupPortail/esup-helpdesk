/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing; 

import org.esupportail.helpdesk.domain.beans.Ticket;

/**
 * A class that represents ticket search result.
 */
public class TicketSearchResult extends AbstractSearchResult {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3089591292058654975L;

	/**
	 * The corresponding ticket.
	 */
	private Ticket ticket;

	/**
	 * Constructor.
	 * @param indexId the unique index id
	 * @param score the score
	 * @param ticket
	 */
	protected TicketSearchResult(
			final String indexId,
			final int score,
			final Ticket ticket) {
		super(indexId, score);
		this.ticket = ticket;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractSearchResult#isTicketSearchResult()
	 */
	@Override
	public boolean isTicketSearchResult() {
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractSearchResult#getTicket()
	 */
	@Override
	public Ticket getTicket() {
		return ticket;
	}

}
