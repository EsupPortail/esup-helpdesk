/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing; 

import org.esupportail.helpdesk.domain.beans.ArchivedTicket;

/**
 * A class that represents archived ticket search result.
 */
public class ArchivedTicketSearchResult extends AbstractSearchResult {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -288923512054147918L;

	/**
	 * The corresponding archived ticket.
	 */
	private ArchivedTicket archivedTicket;

	/**
	 * Constructor.
	 * @param indexId the unique index id
	 * @param score the score
	 * @param archivedTicket
	 */
	protected ArchivedTicketSearchResult(
			final String indexId,
			final int score,
			final ArchivedTicket archivedTicket) {
		super(indexId, score);
		this.archivedTicket = archivedTicket;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractSearchResult#isArchivedTicketSearchResult()
	 */
	@Override
	public boolean isArchivedTicketSearchResult() {
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractSearchResult#getArchivedTicket()
	 */
	@Override
	public ArchivedTicket getArchivedTicket() {
		return archivedTicket;
	}

}
