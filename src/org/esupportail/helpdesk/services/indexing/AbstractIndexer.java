/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing;

import java.sql.Timestamp;
import java.util.List;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.DeletedItem;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.web.controllers.AbstractDomainAwareBean;

/**
 * An abstract indexer to help in writing new indexers.
 */
@SuppressWarnings("serial")
public abstract class AbstractIndexer extends AbstractDomainAwareBean implements Indexer {

	/**
	 * The maximum number of documents to index at the same time.
	 */
	private static final int BATCH_SIZE = 500;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The index id provider.
	 */
	private IndexIdProvider indexIdProvider;

	/**
	 * Bean constructor.
	 */
	protected AbstractIndexer() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.notNull(indexIdProvider,
				"property indexIdProvider of class " + this.getClass().getName()
				+ " can not be null");
	}

	/**
	 * Update the index with the given elements.
	 * @param tickets
	 * @param removeBefore
	 */
	protected abstract void updateIndexTickets(
			List<Ticket> tickets,
			final boolean removeBefore);

	/**
	 * Update the index with the given elements.
	 * @param faqs
	 * @param removeBefore
	 */
	protected abstract void updateIndexFaqs(
			List<Faq> faqs,
			final boolean removeBefore);

	/**
	 * Update the index with the given elements.
	 * @param archivedTickets
	 * @param removeBefore
	 */
	protected abstract void updateIndexArchivedTickets(
			List<ArchivedTicket> archivedTickets,
			final boolean removeBefore);

	/**
	 * Optimize the index.
	 */
	protected abstract void optimizeIndex();

	/**
	 * Remove the index.
	 */
	protected abstract void removeIndexInternal();

	/**
	 * @see org.esupportail.helpdesk.services.indexing.Indexer#removeIndex()
	 */
	@Override
	public void removeIndex() {
		logger.info("removing any existing indexing data...");
		if (isIndexLocked()) {
			throw new IndexException("Lucene index is locked, try again later!");
		}
		removeIndexInternal();
		getDomainService().deleteAllDeletedItems();
		getDomainService().resetIndexTimes();
		logger.info("done.");
	}

	/**
	 * Remove from the index the documents that correspond to entities that have been deleted.
	 * @param deletedItems
	 */
	protected abstract void removeDeletedEntities(List<DeletedItem> deletedItems);

	/**
	 * @see org.esupportail.helpdesk.services.indexing.Indexer#updateIndex(boolean)
	 */
	@Override
	public int updateIndex(
			final boolean removeBefore) {
		if (isIndexLocked()) {
			throw new IndexException("Lucene index is locked, try again later!");
		}
		List<DeletedItem> deletedItems = getDomainService().getDeletedItems();
		if (!deletedItems.isEmpty()) {
			removeDeletedEntities(deletedItems);
			for (DeletedItem deletedItem : deletedItems) {
				getDomainService().deleteDeletedItem(deletedItem);
			}
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Timestamp lastUpdate;
		lastUpdate = getDomainService().getTicketsLastIndexTime();
		if (lastUpdate == null) {
			lastUpdate = new Timestamp(0);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("looking for tickets updated after [" + lastUpdate + "]...");
		}
		List<Ticket> tickets = getDomainService().getTicketsChangedAfter(lastUpdate, BATCH_SIZE);
		if (tickets.size() != BATCH_SIZE) {
			getDomainService().setTicketsLastIndexTime(now);
		} else {
			getDomainService().setTicketsLastIndexTime(
					tickets.get(tickets.size() - 1).getLastActionDate());
		}
		if (!tickets.isEmpty()) {
			logger.info("found " + tickets.size() + " ticket(s) updated after ["
					+ lastUpdate + "], indexing...");
			updateIndexTickets(tickets, removeBefore);
			return tickets.size();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("no ticket updated after [" + lastUpdate + "]");
		}
		lastUpdate = getDomainService().getFaqsLastIndexTime();
		if (lastUpdate == null) {
			lastUpdate = new Timestamp(0);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("looking for FAQs updated after [" + lastUpdate + "]...");
		}
		List<Faq> faqs = getDomainService().getFaqsChangedAfter(lastUpdate, BATCH_SIZE);
		if (faqs.size() != BATCH_SIZE) {
			getDomainService().setFaqsLastIndexTime(now);
		} else {
			getDomainService().setFaqsLastIndexTime(
					faqs.get(faqs.size() - 1).getLastUpdate());
		}
		if (!faqs.isEmpty()) {
			logger.info("found " + faqs.size() + " FAQ(s) updated after ["
					+ lastUpdate + "], indexing...");
			updateIndexFaqs(faqs, removeBefore);
			return faqs.size();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("no FAQ updated after [" + lastUpdate + "]");
		}
		getDomainService().setFaqsLastIndexTime(now);
		lastUpdate = getDomainService().getArchivedTicketsLastIndexTime();
		if (lastUpdate == null) {
			lastUpdate = new Timestamp(0);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("looking for archived tickets updated after [" + lastUpdate + "]...");
		}
		List<ArchivedTicket> archivedTickets = getDomainService().getTicketsArchivedAfter(lastUpdate, BATCH_SIZE);
		if (archivedTickets.size() != BATCH_SIZE) {
			getDomainService().setArchivedTicketsLastIndexTime(now);
		} else {
			getDomainService().setArchivedTicketsLastIndexTime(
					archivedTickets.get(archivedTickets.size() - 1).getArchivingDate());
		}
		if (!archivedTickets.isEmpty()) {
			logger.info("found " + archivedTickets.size() + " archived ticket(s) updated after ["
					+ lastUpdate + "], indexing...");
			updateIndexArchivedTickets(archivedTickets, removeBefore);
			return archivedTickets.size();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("no archived ticket updated after [" + lastUpdate + "]");
		}
		getDomainService().setArchivedTicketsLastIndexTime(now);
		logger.info("optimizing...");
		optimizeIndex();
		logger.info("indexing completed.");
		return 0;
	}

	/**
	 * @return True if the Lucene index is locked.
	 */
	protected abstract boolean isIndexLocked();

	/**
	 * @return the indexIdProvider
	 */
	protected IndexIdProvider getIndexIdProvider() {
		return indexIdProvider;
	}

	/**
	 * @param indexIdProvider the indexIdProvider to set
	 */
	public void setIndexIdProvider(final IndexIdProvider indexIdProvider) {
		this.indexIdProvider = indexIdProvider;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.Indexer#test()
	 */
	@Override
	public void test() {
		throw new UnsupportedOperationException();
	}

}
