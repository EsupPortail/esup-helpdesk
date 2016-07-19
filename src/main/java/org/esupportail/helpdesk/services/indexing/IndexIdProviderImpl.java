/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing;

import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.Ticket;

/**
 * A basic implementation of IndexIdProvider.
 */
public class IndexIdProviderImpl implements IndexIdProvider {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7959513700027842403L;

	/**
	 * The prefix used to index tickets.
	 */
	private static final String TICKET_INDEX_PREFIX = "t";

	/**
	 * The prefix used to index FAQ.
	 */
	private static final String FAQ_INDEX_PREFIX = "f";

	/**
	 * The prefix used to index archived tickets.
	 */
	private static final String ARCHIVED_TICKET_INDEX_PREFIX = "a";

	/**
	 * Bean constructor.
	 */
	public IndexIdProviderImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.IndexIdProvider#getIndexId(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public String getIndexId(final Ticket ticket) {
		return TICKET_INDEX_PREFIX + ticket.getId();
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.IndexIdProvider#getIndexId(
	 * org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public String getIndexId(final Faq faq) {
		return FAQ_INDEX_PREFIX + faq.getId();
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.IndexIdProvider#getIndexId(
	 * org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	public String getIndexId(final ArchivedTicket archivedTicket) {
		return ARCHIVED_TICKET_INDEX_PREFIX + archivedTicket.getId();
	}

	/**
	 * @param indexId
	 * @param prefix
	 * @return true if the index starts with a given prefix, corresponding to a class.
	 */
	protected boolean isObjectIndexId(
			final String indexId,
			final String prefix) {
		if (indexId == null) {
			throw new IndexException("null index id");
		}
		return indexId.startsWith(prefix);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.IndexIdProvider#isTicketIndexId(java.lang.String)
	 */
	@Override
	public boolean isTicketIndexId(final String indexId) {
		return isObjectIndexId(indexId, TICKET_INDEX_PREFIX);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.IndexIdProvider#isFaqIndexId(java.lang.String)
	 */
	@Override
	public boolean isFaqIndexId(final String indexId) {
		return isObjectIndexId(indexId, FAQ_INDEX_PREFIX);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.IndexIdProvider#isArchivedTicketIndexId(java.lang.String)
	 */
	@Override
	public boolean isArchivedTicketIndexId(final String indexId) {
		return isObjectIndexId(indexId, ARCHIVED_TICKET_INDEX_PREFIX);
	}

	/**
	 * @param indexId
	 * @return the id of the object that corresponds to an index id or null of not well-formed.
	 */
	protected long getObjectIndexId(
			final String indexId) {
		try {
			return Long.valueOf(indexId.substring(1));
		} catch (NumberFormatException e) {
			throw new IndexException("ill-formed index id [" + indexId + "]");
		}
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.IndexIdProvider#getTicketIndexId(java.lang.String)
	 */
	@Override
	public long getTicketIndexId(final String indexId) {
		return getObjectIndexId(indexId);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.IndexIdProvider#getFaqIndexId(java.lang.String)
	 */
	@Override
	public long getFaqIndexId(final String indexId) {
		return getObjectIndexId(indexId);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.IndexIdProvider#getArchivedTicketIndexId(java.lang.String)
	 */
	@Override
	public long getArchivedTicketIndexId(final String indexId) {
		return getObjectIndexId(indexId);
	}

}
