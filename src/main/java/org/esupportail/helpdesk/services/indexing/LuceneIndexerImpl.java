/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.FSDirectory;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.Search;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.ArchivedAction;
import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.DeletedItem;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.exceptions.ArchivedTicketNotFoundException;
import org.esupportail.helpdesk.exceptions.FaqNotFoundException;
import org.esupportail.helpdesk.exceptions.TicketNotFoundException;

/**
 * The authentication bean.
 */
public class LuceneIndexerImpl extends AbstractIndexer {

	/**
	 *
	 */
	private static final long serialVersionUID = 4530032772105072081L;

	/**
	 * Magic number.
	 */
	private static final float HUNDRED = 100;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The path used to store index files.
	 */
	private String path;

	/**
	 * The test userId.
	 */
	private String testUserId;

	/**
	 * The test tokens.
	 */
	private String testTokens;

	/**
	 * Bean constructor.
	 */
	public LuceneIndexerImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.hasLength(path,
				"property path of class " + this.getClass().getName()
				+ " can not be null");
		File dir = new File(path);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new IndexException("could not create directory [" + path + "]");
			}
			logger.info("directory [" + path + "] was successfully created.");
		}
		if (!dir.isDirectory()) {
			throw new IndexException("[" + path + "] is not a directory");
		}
	}

	/**
	 * Get a Lucene IndexWriter.
	 * @param createIndex true to create the index
	 * @return a IndexWriter instance.
	 */
	protected IndexWriter getIndexWriter(
			final boolean createIndex) {
		try {
			return new IndexWriter(path, new StandardAnalyzer(), createIndex);
		} catch (IOException e) {
			throw new IndexException("could not get a Lucence IndexWriter", e);
		}
	}

	/**
	 * Remove a document from the index.
	 * @param writer
	 * @param indexId
	 */
	protected void removeDocument(
			final IndexWriter writer,
			final String indexId) {
		try {
			Term term = new Term("id", indexId);
			writer.deleteDocuments(term);
		} catch (IOException e) {
			throw new IndexException("could not delete indexed document [" + indexId + "]", e);
		}
	}

	/**
	 * Add a document.
	 * @param writer
	 * @param doc the document
	 * @param removeBefore
	 */
	protected void addDocument(
			final IndexWriter writer,
			final Document doc,
			final boolean removeBefore) {
		try {
			if (removeBefore) {
				removeDocument(writer, doc.get("id"));
			}
			writer.addDocument(doc);
		} catch (IOException e) {
			throw new IndexException("could not index document [" + doc.get("id") + "]", e);
		}
	}

	/**
	 * Close a Lucene index writer.
	 * @param writer
	 */
	protected void closeIndexWriter(
			final IndexWriter writer) {
		try {
			writer.close();
		} catch (IOException e) {
			throw new IndexException("could not close a Lucence IndexWriter", e);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractIndexer#optimizeIndex()
	 */
	@Override
	protected void optimizeIndex() {
		try {
			IndexWriter writer = getIndexWriter(false);
			writer.optimize();
			closeIndexWriter(writer);
		} catch (IOException e) {
			throw new IndexException("could not optimize the Lucence index", e);
		}
	}

	/**
	 * Create a new Lucene document.
	 * @param type
	 * @param id
	 * @param content
	 * @param managerId
	 * @param ownerId
	 * @param userId
	 * @param date
	 * @return a Document instance.
	 */
	protected Document getDocument(
			final String type,
			final String id,
			final String content,
			final String managerId,
			final String ownerId,
			final String userId,
			final Timestamp date) {
		Document doc = new Document();
		doc.add(new Field("doc-type", type, Field.Store.NO, Field.Index.UN_TOKENIZED));
		doc.add(new Field("id", id, Field.Store.YES, Field.Index.UN_TOKENIZED));
		doc.add(new Field("date", date.toString(), Field.Store.NO, Field.Index.UN_TOKENIZED));
		doc.add(new Field(
				"text", StringUtils.removeUtf8Accents(content),
				Field.Store.NO, Field.Index.TOKENIZED));
		if (managerId != null) {
			doc.add(new Field(
					"manager", managerId,
					Field.Store.NO, Field.Index.TOKENIZED));
		}
		if (ownerId != null) {
			doc.add(new Field(
					"owner", ownerId,
					Field.Store.NO, Field.Index.TOKENIZED));
		}
		if (userId != null) {
			doc.add(new Field(
					"user", userId,
					Field.Store.NO, Field.Index.TOKENIZED));
		}
		return doc;
	}

	/**
	 * Convert a ticket into a document (ready to be indexed).
	 * @param ticket the ticket
	 * @return a Document instance.
	 */
	protected Document getDocument(
			final Ticket ticket) {
		String content = "";
		String managerId = "";
		String ownerId = "";
		String userId = "";
		content += ticket.getId() + "\n";
		content += ticket.getLabel() + "\n";
		content += ticket.getCategory().getLabel() + "\n";
		if (ticket.getConnectionTicket() != null) {
			content += ticket.getConnectionTicket().getId() + "\n";
		}
		if (ticket.getConnectionFaq() != null) {
			content += ticket.getConnectionFaq().getLabel() + "\n";
		}
		if (ticket.getManager() != null) {
			content += ticket.getManager().getDisplayName() + "\n";
			content += ticket.getManager().getId() + "\n";
			managerId += ticket.getManager().getId() + "\n";
			managerId += ticket.getManager().getDisplayName() + "\n";
		}
		content += ticket.getOwner().getDisplayName() + "\n";
		content += ticket.getOwner().getId() + "\n";
		ownerId += ticket.getOwner().getId() + "\n";
		ownerId += ticket.getOwner().getDisplayName() + "\n";
		content += ticket.getDepartment().getLabel() + "\n";
		if (ticket.getComputer() != null) {
			content += ticket.getComputer() + "\n";
		}
		for (Action action : getDomainService().getActions(ticket)) {
			if (action.getUser() != null) {
				content += action.getUser().getId() + "\n";
				content += action.getUser().getDisplayName() + "\n";
				userId += action.getUser().getId() + "\n";
				userId += action.getUser().getDisplayName() + "\n";
			}
			if (action.getInvitedUser() != null) {
				content += action.getInvitedUser().getId() + "\n";
				content += action.getInvitedUser().getDisplayName() + "\n";
				userId += action.getInvitedUser().getId() + "\n";
				userId += action.getInvitedUser().getDisplayName() + "\n";
			}
			if (action.getTicketOwnerBefore() != null) {
				content += action.getTicketOwnerBefore().getId() + "\n";
				content += action.getTicketOwnerBefore().getDisplayName() + "\n";
				ownerId += action.getTicketOwnerBefore().getId() + "\n";
				ownerId += action.getTicketOwnerBefore().getDisplayName() + "\n";
			}
			if (action.getManagerBefore() != null) {
				content += action.getManagerBefore().getId() + "\n";
				content += action.getManagerBefore().getDisplayName() + "\n";
				managerId += action.getManagerBefore().getId() + "\n";
				managerId += action.getManagerBefore().getDisplayName() + "\n";
			}
			if (action.getMessage() != null) {
				content += action.getMessage() + "\n";
			}
		}
		return getDocument(
				TICKET_INDEX_DOCTYPE, getIndexIdProvider().getIndexId(ticket),
				content, managerId, ownerId, userId, ticket.getLastActionDate());
	}

	/**
	 * Convert a ticket into a document (ready to be indexed).
	 * @param archivedTicket
	 * @return a Document instance.
	 */
	protected Document getDocument(
			final ArchivedTicket archivedTicket) {
		String content = "";
		String managerId = "";
		String ownerId = "";
		String userId = "";
		content += archivedTicket.getTicketId() + "\n";
		content += archivedTicket.getLabel() + "\n";
		content += archivedTicket.getCategoryLabel() + "\n";
		if (archivedTicket.getConnectionTicket() != null) {
			content += archivedTicket.getConnectionTicket().getId() + "\n";
			content += archivedTicket.getConnectionTicket().getLabel() + "\n";
		}
		if (archivedTicket.getConnectionArchivedTicket() != null) {
			content += archivedTicket.getConnectionArchivedTicket().getTicketId() + "\n";
			content += archivedTicket.getConnectionArchivedTicket().getLabel() + "\n";
		}
		if (archivedTicket.getConnectionFaq() != null) {
			content += archivedTicket.getConnectionFaq().getLabel() + "\n";
		}
		if (archivedTicket.getManager() != null) {
			content += archivedTicket.getManager().getDisplayName() + "\n";
			content += archivedTicket.getManager().getId() + "\n";
			managerId += archivedTicket.getManager().getId() + "\n";
			managerId += archivedTicket.getManager().getDisplayName() + "\n";
		}
		content += archivedTicket.getOwner().getDisplayName() + "\n";
		content += archivedTicket.getOwner().getId() + "\n";
		content += archivedTicket.getDepartment().getLabel() + "\n";
		ownerId += archivedTicket.getOwner().getId() + "\n";
		ownerId += archivedTicket.getOwner().getDisplayName() + "\n";
		if (archivedTicket.getComputer() != null) {
			content += archivedTicket.getComputer() + "\n";
		}
		for (ArchivedAction archivedAction : getDomainService().getArchivedActions(archivedTicket)) {
			if (archivedAction.getUser() != null) {
				content += archivedAction.getUser().getId() + "\n";
				content += archivedAction.getUser().getDisplayName() + "\n";
				userId += archivedAction.getUser().getId() + "\n";
				userId += archivedAction.getUser().getDisplayName() + "\n";
			}
			if (archivedAction.getInvitedUser() != null) {
				content += archivedAction.getInvitedUser().getId() + "\n";
				content += archivedAction.getInvitedUser().getDisplayName() + "\n";
				userId += archivedAction.getInvitedUser().getId() + "\n";
				userId += archivedAction.getInvitedUser().getDisplayName() + "\n";
			}
			if (archivedAction.getTicketOwnerBefore() != null) {
				content += archivedAction.getTicketOwnerBefore().getId() + "\n";
				content += archivedAction.getTicketOwnerBefore().getDisplayName() + "\n";
				ownerId += archivedAction.getTicketOwnerBefore().getId() + "\n";
				ownerId += archivedAction.getTicketOwnerBefore().getDisplayName() + "\n";
			}
			if (archivedAction.getManagerBefore() != null) {
				content += archivedAction.getManagerBefore().getId() + "\n";
				content += archivedAction.getManagerBefore().getDisplayName() + "\n";
				managerId += archivedAction.getManagerBefore().getId() + "\n";
				managerId += archivedAction.getManagerBefore().getDisplayName() + "\n";
			}
			if (archivedAction.getMessage() != null) {
				content += archivedAction.getMessage() + "\n";
			}
		}
		return getDocument(
				ARCHIVED_TICKET_INDEX_DOCTYPE,
				getIndexIdProvider().getIndexId(archivedTicket), content,
				managerId, ownerId, userId, archivedTicket.getArchivingDate());
	}

	/**
	 * Convert a FAQ into a document (ready to be indexed).
	 * @param faq
	 * @return a Document instance.
	 */

	protected Document getDocument(
			final Faq faq) {
		String content = "";
		content += faq.getLabel() + "\n";
		content += faq.getContent() + "\n";
		return getDocument(
				FAQ_INDEX_DOCTYPE, getIndexIdProvider().getIndexId(faq),
				content, null, null, null, faq.getLastUpdate());
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractIndexer#updateIndexTickets(
	 * java.util.List, boolean)
	 */
	@Override
	protected void updateIndexTickets(
			final List<Ticket> tickets,
			final boolean removeBefore) {
		IndexWriter writer = getIndexWriter(false);
		for (Ticket ticket : tickets) {
			if (logger.isDebugEnabled()) {
				logger.debug("indexing ticket #" + ticket.getId()
						+ " last modified on [" + ticket.getLastActionDate() + "]...");
			}
			addDocument(writer, getDocument(ticket), removeBefore);
		}
		closeIndexWriter(writer);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractIndexer#updateIndexFaqs(
	 * java.util.List, boolean)
	 */
	@Override
	protected void updateIndexFaqs(
			final List<Faq> faqs,
			final boolean removeBefore) {
		IndexWriter writer = getIndexWriter(false);
		for (Faq faq : faqs) {
			addDocument(writer, getDocument(faq), removeBefore);
			if (logger.isDebugEnabled()) {
				logger.debug("indexing FAQ #" + faq.getId()
						+ " last modified on [" + faq.getLastUpdate() + "]...");
			}
		}
		closeIndexWriter(writer);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractIndexer#updateIndexArchivedTickets(
	 * java.util.List, boolean)
	 */
	@Override
	protected void updateIndexArchivedTickets(
			final List<ArchivedTicket> archivedTickets,
			final boolean removeBefore) {
		IndexWriter writer = getIndexWriter(false);
		for (ArchivedTicket archivedTicket : archivedTickets) {
			if (logger.isDebugEnabled()) {
				logger.debug("indexing ticket #" + archivedTicket.getTicketId()
						+ " archived on [" + archivedTicket.getArchivingDate() + "]...");
			}
			addDocument(writer, getDocument(archivedTicket), removeBefore);
		}
		closeIndexWriter(writer);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractIndexer#removeIndexInternal()
	 */
	@Override
	protected void removeIndexInternal() {
		IndexWriter writer = getIndexWriter(true);
		closeIndexWriter(writer);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractIndexer#removeDeletedEntities(java.util.List)
	 */
	@Override
	protected void removeDeletedEntities(final List<DeletedItem> deletedItems) {
		IndexWriter writer = getIndexWriter(false);
		for (DeletedItem deletedItem : deletedItems) {
			removeDocument(writer, deletedItem.getIndexId());
			logger.info(
					"document [" + deletedItem.getIndexId()
					+ "] has been removed from the index.");
		}
		closeIndexWriter(writer);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractIndexer#isIndexLocked()
	 */
	@Override
	protected boolean isIndexLocked() {
		try {
			return IndexReader.isLocked(path);
		} catch (IOException e) {
			throw new IndexException("could not check if the Lucence index is locked", e);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.Indexer#unlockIndex()
	 */
@Override
	public void unlockIndex() {
		logger.info("unlocking the index...");
		if (!isIndexLocked()) {
			throw new IndexException("Lucene index is not locked");
		}
		try {
			IndexReader.unlock(FSDirectory.getDirectory(path));
		} catch (IOException e) {
			throw new IndexException("could not unlock the Lucence index", e);
		}
		logger.info("done.");
	}

	/**
	 * @return the path
	 */
	protected String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/**
	 * Filter search tokens.
	 * @param tokens
	 * @return a list of words.
	 */
	private List<String> filterTokens(final String tokens) {
		List<String> result = new ArrayList<String>();
		if (tokens != null) {
			String input = StringUtils.removeUtf8Accents(tokens);
			input = input.replace('[', ' ');
			input = input.replace(']', ' ');
			input = input.replace('(', ' ');
			input = input.replace(')', ' ');
			input = input.replace('+', ' ');
			input = input.replace('-', ' ');
			input = input.replace('!', ' ');
			input = input.replace(':', ' ');
			input = input.replace('^', ' ');
			input = input.replace('\\', ' ');
			StringTokenizer st = new StringTokenizer(input);
			while (st.hasMoreTokens()) {
				String word = st.nextToken();
				// remove leading stars and tildes
				while (word.length() > 0 && (word.charAt(0) == '*' || word.charAt(0) == '~')) {
					word = word.substring(1);
				}
				if (word.length() > 0) {
					result.add(word);
				}
			}
		}
		return result;
	}

	/**
	 * @param tokens
	 * @return a AND query string.
	 */
	private String andQueryString(final String tokens) {
		List<String> filteredTokens = filterTokens(tokens);
		if (filteredTokens.isEmpty()) {
			return null;
		}
		if (filteredTokens.size() == 1) {
			return filteredTokens.get(0);
		}
		String result = "";
		String separator = "(";
		for (String token : filteredTokens) {
			result = result + separator + token;
			separator = " AND ";
		}
		return result + ")";
	}

	/**
	 * @param tokens
	 * @return a query string for an expression.
	 */
	private String exprQueryString(final String tokens) {
		List<String> filteredTokens = filterTokens(tokens);
		if (filteredTokens.isEmpty()) {
			return null;
		}
		if (filteredTokens.size() == 1) {
			return filteredTokens.get(0);
		}
		String result = "";
		String separator = "\"";
		for (String token : filteredTokens) {
			result = result + separator + token;
			separator = " ";
		}
		return result + "\"";
	}

	/**
	 * @param tokens
	 * @return a OR query string.
	 */
	private String orQueryString(final String tokens) {
		List<String> filteredTokens = filterTokens(tokens);
		if (filteredTokens.isEmpty()) {
			return null;
		}
		if (filteredTokens.size() == 1) {
			return filteredTokens.get(0);
		}
		String result = "";
		String separator = "(";
		for (String token : filteredTokens) {
			result = result + separator + token;
			separator = " OR ";
		}
		return result + ")";
	}

	/**
	 * @param tokens
	 * @return a NOT query string.
	 */
	private String notQueryString(final String tokens) {
		List<String> filteredTokens = filterTokens(tokens);
		if (filteredTokens.isEmpty()) {
			return null;
		}
		if (filteredTokens.size() == 1) {
			return "NOT " + filteredTokens.get(0);
		}
		String result = "";
		String separator = "( NOT ";
		for (String token : filteredTokens) {
			result = result + separator + token;
			separator = " AND NOT ";
		}
		return result + ")";
	}

	/**
	 * @param tokens
	 * @param exprTokens
	 * @param orTokens
	 * @param notTokens
	 * @param managerId
	 * @param ownerId
	 * @param userId
	 * @param searchType
	 * @return the Lucene hits for the given words.
	 */
	protected Query getQuery(
			final String tokens,
			final String exprTokens,
			final String orTokens,
			final String notTokens,
			final String managerId,
			final String ownerId,
			final String userId,
			final String searchType) {
		String andQueryString = andQueryString(tokens);
		String exprQueryString = exprQueryString(exprTokens);
		String orQueryString = orQueryString(orTokens);
		String notQueryString = notQueryString(notTokens);
		String managerQueryString = orQueryString(managerId);
		String ownerQueryString = orQueryString(ownerId);
		String userQueryString = orQueryString(userId);
		if (andQueryString == null
				&& exprQueryString == null
				&& orQueryString == null
				&& notQueryString == null
				&& managerQueryString == null
				&& ownerQueryString == null
				&& userQueryString == null) {
			return null;
		}
		String queryString;
		if (Search.TYPE_FILTER_ACTIVE_TICKET_AND_FAQ.equals(searchType)) {
			queryString = "(doc-type:" + TICKET_INDEX_DOCTYPE + " OR doc-type:" + FAQ_INDEX_DOCTYPE + ")";
		} else if (Search.TYPE_FILTER_ACTIVE_TICKET.equals(searchType)) {
			queryString = "doc-type:" + TICKET_INDEX_DOCTYPE;
		} else if (Search.TYPE_FILTER_ARCHIVED_TICKET.equals(searchType)) {
			queryString = "doc-type:" + ARCHIVED_TICKET_INDEX_DOCTYPE;
		} else if (Search.TYPE_FILTER_TICKET.equals(searchType)) {
			queryString = "(doc-type:" + TICKET_INDEX_DOCTYPE
			+ " OR doc-type:" + ARCHIVED_TICKET_INDEX_DOCTYPE + ")";
		} else if (Search.TYPE_FILTER_FAQ.equals(searchType)) {
			queryString = "doc-type:" + FAQ_INDEX_DOCTYPE;
		} else {
			queryString = "";
		}
		if (andQueryString != null) {
			queryString += " " + andQueryString;
		}
		if (exprQueryString != null) {
			queryString += " " + exprQueryString;
		}
		if (orQueryString != null) {
			queryString += " " + orQueryString;
		}
		if (notQueryString != null) {
			queryString += " " + notQueryString;
		}
		if (managerQueryString != null) {
			queryString += " manager:(" + managerQueryString + ")";
		}
		if (ownerQueryString != null) {
			queryString += " owner:(" + ownerQueryString + ")";
		}
		if (userQueryString != null) {
			queryString += " user:(" + userQueryString + ")";
		}
		try {
			QueryParser queryParser = new QueryParser("text", new StandardAnalyzer());
			queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query query = queryParser.parse(queryString);
			if (logger.isDebugEnabled()) {
				logger.debug(new StringBuffer("query string: ").append(queryString));
				logger.debug(new StringBuffer("search query: ").append(query));
			}
			return query;
		} catch (ParseException e) {
			throw new IndexException(e);
		}
	}

	/**
	 * @param searcher
	 * @param query
	 * @param sortByDate
	 * @return the Lucene hits for the given query.
	 */
	protected Hits getHits(
			final Searcher searcher,
			final boolean sortByDate,
			final Query query) {
		if (query == null) {
			return null;
		}
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("searching the knowledge database...");
			}
			Sort sort;
			if (sortByDate) {
				sort = new Sort("date", true);
			} else {
				sort = Sort.RELEVANCE;
			}
			Hits hits = searcher.search(query, sort);
			if (logger.isDebugEnabled()) {
				logger.debug(hits.length() + " total matching documents");
			}
			return hits;
		} catch (CorruptIndexException e) {
			throw new IndexException(e);
		} catch (IOException e) {
			throw new IndexException(e);
		}
	}

	/**
	 * @param searcher
	 * @param sortByDate
	 * @param tokens
	 * @param exprTokens
	 * @param orTokens
	 * @param notTokens
	 * @param managerId
	 * @param ownerId
	 * @param userId
	 * @param searchType
	 * @return the Lucene hits for the given tokens.
	 */
	protected Hits getHits(
			final Searcher searcher,
			final boolean sortByDate,
			final String tokens,
			final String exprTokens,
			final String orTokens,
			final String notTokens,
			final String managerId,
			final String ownerId,
			final String userId,
			final String searchType) {
		Hits hits = getHits(
				searcher, sortByDate,
				getQuery(tokens, exprTokens, orTokens, notTokens,
						managerId, ownerId, userId, searchType));
		if (logger.isDebugEnabled()) {
			logger.debug("hits=[" + hits + "]");
		}
		return hits;
	}

	/**
	 * @param hits
	 * @param user
	 * @param client
	 * @param department
	 * @param date1
	 * @param date2
	 * @param maxResults
	 * @return the results that correspond to the given hits.
	 */
	protected SearchResults hitsToResults(
			final Hits hits,
			final User user,
			final InetAddress client,
			final Department department,
			final Timestamp date1,
			final Timestamp date2,
			final int maxResults) {
		if (hits == null) {
			return SearchResults.empty();
		}
		try {
			List<SearchResult> results = new ArrayList<SearchResult>();
			if (logger.isDebugEnabled()) {
				logger.debug("hits.length()=" + hits.length());
			}
			List<Department> ticketViewVisibleDepartments = null;
			List<Department> faqViewVisibleDepartments = null;
			for (int i = 0; i < hits.length(); i++) {
				Document doc = hits.doc(i);
				String resultId = doc.get("id");
				if (getIndexIdProvider().isTicketIndexId(resultId)) {
					long ticketId = getIndexIdProvider().getTicketIndexId(resultId);
					try {
						Ticket ticket = getDomainService().getTicket(ticketId);
						if (logger.isDebugEnabled()) {
							logger.debug("found ticket #" + ticket.getId());
						}
						if (department == null || department.equals(ticket.getDepartment())) {
							if (ticketViewVisibleDepartments == null) {
								ticketViewVisibleDepartments =
                                    getDomainService().getTicketViewDepartments(user, client);
							}
                            if (getDomainService().userCanViewTicket(user, ticket, ticketViewVisibleDepartments)) {
								if (!user.getAdvancedSearch() || date1 == null || date1.before(ticket.getLastActionDate())) {
									if (!user.getAdvancedSearch() || date2 == null || date2.after(ticket.getCreationDate())) {
										if (logger.isDebugEnabled()) {
		                                    logger.debug("**** adding ticket #" + ticket.getId()
													+ " to the search results");
										}
										int result100 = (int) (HUNDRED * hits.score(i));
										results.add(new TicketSearchResult(
												resultId, result100, ticket));
									} else if (logger.isDebugEnabled()) {
										logger.debug("ticket is out of date range (too new)");
									}
								} else if (logger.isDebugEnabled()) {
									logger.debug("ticket is out of date range (too old)");
								}
							} else if (logger.isDebugEnabled()) {
								logger.debug("ticket not visible");
							}
						} else if (logger.isDebugEnabled()) {
							logger.debug("department not matched");
						}
					} catch (TicketNotFoundException e) {
						ExceptionUtils.catchException(new IndexException(
								"indexed ticket #" + ticketId + " not found"));
					}
				} else if (getIndexIdProvider().isFaqIndexId(resultId)) {
					long faqId = getIndexIdProvider().getFaqIndexId(resultId);
					try {
						Faq faq = getDomainService().getFaq(faqId);
						if (logger.isDebugEnabled()) {
							logger.debug("found FAQ #" + faq.getId());
						}
                        if (department == null || department.equals(faq.getDepartment())) {
							if (faqViewVisibleDepartments == null) {
								faqViewVisibleDepartments =
                                    getDomainService().getFaqViewDepartments(user, client);
							}
							if (getDomainService().userCanViewFaq(
                                    user, faq, faqViewVisibleDepartments)) {
								if (logger.isDebugEnabled()) {
									logger.debug("**** adding FAQ #"
											+ faq.getId()
											+ " to the search results");
								}
								int result100 = (int) (HUNDRED * hits.score(i));
								results.add(new FaqSearchResult(
										resultId, result100, faq));
							} else if (logger.isDebugEnabled()) {
								logger.debug("FAQ not visible");
							}
						} else if (logger.isDebugEnabled()) {
							logger.debug("department not matched");
						}
					} catch (FaqNotFoundException e) {
						ExceptionUtils.catchException(new IndexException(
								"indexed FAQ #"
								+ faqId + " not found"));
					}
				} else if (getIndexIdProvider().isArchivedTicketIndexId(resultId)) {
					long archivedTicketId = getIndexIdProvider().getArchivedTicketIndexId(resultId);
					try {
						ArchivedTicket archivedTicket = getDomainService().getArchivedTicket(
								archivedTicketId);
						if (logger.isDebugEnabled()) {
							logger.debug("found archived ticket #"
									+ archivedTicket.getId());
						}
						if (department == null
								|| department.equals(archivedTicket.getDepartment())) {
							if (ticketViewVisibleDepartments == null) {
								ticketViewVisibleDepartments =
                                    getDomainService().getTicketViewDepartments(user, client);
							}
							if (getDomainService().userCanViewArchivedTicket(
										user, archivedTicket,
										ticketViewVisibleDepartments)) {
								if (!user.getAdvancedSearch() || date1 == null || date1.before(archivedTicket.getArchivingDate())) {
									if (!user.getAdvancedSearch() || date2 == null || date2.after(archivedTicket.getCreationDate())) {
										if (logger.isDebugEnabled()) {
											logger.debug("**** adding archived ticket #"
													+ archivedTicket.getId()
													+ " to the search results");
										}
										int result100 = (int) (HUNDRED * hits.score(i));
										results.add(new ArchivedTicketSearchResult(
												resultId, result100, archivedTicket));
									} else if (logger.isDebugEnabled()) {
										logger.debug("archived ticket is out of date range (too new)");
									}
								} else if (logger.isDebugEnabled()) {
									logger.debug("archived ticket is out of date range (too old)");
								}
							} else if (logger.isDebugEnabled()) {
								logger.debug("archived ticket not visible");
							}
						} else if (logger.isDebugEnabled()) {
							logger.debug("department not matched");
						}
					} catch (ArchivedTicketNotFoundException e) {
						ExceptionUtils.catchException(new IndexException(
								"indexed archived ticket #"
								+ archivedTicketId + " not found"));
					}
				} else {
					throw new IndexException("ill-formed index id [" + resultId + "]");
				}
				if (results.size() >= maxResults) {
					int estimatedResultsNumber;
					if (department == null) {
						estimatedResultsNumber = hits.length();
					} else {
						estimatedResultsNumber = maxResults * hits.length() / (i + 1);
					}
					return SearchResults.estimated(results, estimatedResultsNumber);
				}
			}
			return SearchResults.exact(results);
		} catch (CorruptIndexException e) {
			throw new IndexException(e);
		} catch (IOException e) {
			throw new IndexException(e);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.Indexer#search(
	 * org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress,
	 * org.esupportail.helpdesk.domain.beans.Department, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, int, boolean,
	 * java.sql.Timestamp, java.sql.Timestamp)
	 */
@Override
	public SearchResults search(
			final User user,
			final InetAddress client,
			final Department department,
			final String tokens,
			final String exprTokens,
			final String orTokens,
			final String notTokens,
			final String managerId,
			final String ownerId,
			final String userId,
			final String searchType,
			final int maxResults,
			final boolean sortByDate,
			final Timestamp date1,
			final Timestamp date2) throws IndexException {
		try {
			IndexReader reader = IndexReader.open(path);
			int docsNumber = reader.numDocs();
			reader.close();
			if (docsNumber == 0) {
				EmptyIndexException e = new EmptyIndexException();
				ExceptionUtils.catchException(e);
				throw e;
			}
			Searcher searcher = new IndexSearcher(path);
			if (logger.isDebugEnabled()) {
				logger.debug("searcher=[" + searcher + "]");
			}
			SearchResults results = hitsToResults(getHits(
					searcher, sortByDate, tokens, exprTokens, orTokens, notTokens,
					managerId, ownerId, userId, searchType),
					user, client, department, date1, date2, maxResults);
			searcher.close();
			return results;
		} catch (CorruptIndexException e) {
			throw new IndexException(e);
		} catch (IOException e) {
			throw new IndexException(e);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.Indexer#getDocumentsNumber()
	 */
@Override
	public int getDocumentsNumber() {
		int result = -1;
		try {
			IndexReader reader = IndexReader.open(path);
			result = reader.numDocs();
			reader.close();
		} catch (IOException e) {
			ExceptionUtils.catchException(new IndexException(e));
		}
		return result;
	}

	/**
	 * @return the number of hits for a docType query.
	 * @param docType
	 */
	protected int getDocTypeHitsNumber(final String docType) {
		int result = -1;
		try {
			String queryString = "doc-type:" + docType;
			QueryParser queryParser = new QueryParser("text", new StandardAnalyzer());
			queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query query = queryParser.parse(queryString);
			if (logger.isDebugEnabled()) {
				logger.debug(new StringBuffer("query string: ").append(queryString));
				logger.debug(new StringBuffer("search query: ").append(query));
			}
			Searcher searcher = new IndexSearcher(path);
			Hits hits = searcher.search(query);
			result = hits.length();
		} catch (ParseException e) {
			ExceptionUtils.catchException(new IndexException(e));
		} catch (IOException e) {
			ExceptionUtils.catchException(new IndexException(e));
		}
		return result;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.AbstractIndexer#test()
	 */
	@Override
	public void test() {
		if (!org.springframework.util.StringUtils.hasText(testUserId)) {
			logger.error("property [testUserId] is not set");
			return;
		}
		User user = null;
		try {
			user = getUserStore().getUserFromRealId(testUserId);
		} catch (UserNotFoundException e) {
			logger.error(e.getMessage());
			return;
		}
		if (!org.springframework.util.StringUtils.hasText(testTokens)) {
			logger.error("property [testTokens] is not set");
			return;
		}
		try {
			SearchResults results = search(
					user, null, null, testTokens, null, null, null,
					null, null, null, Search.TYPE_FILTER_ALL, Integer.MAX_VALUE, false, null, null);
			logger.info(results.getResultsNumber() + " results found.");
		} catch (IndexException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.Indexer#getTicketsNumber()
	 */
@Override
	public int getTicketsNumber() {
		return getDocTypeHitsNumber(TICKET_INDEX_DOCTYPE);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.Indexer#getArchivedTicketsNumber()
	 */
@Override
	public int getArchivedTicketsNumber() {
		return getDocTypeHitsNumber(ARCHIVED_TICKET_INDEX_DOCTYPE);
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.Indexer#getFaqsNumber()
	 */
@Override
	public int getFaqsNumber() {
		return getDocTypeHitsNumber(FAQ_INDEX_DOCTYPE);
	}

	/**
	 * @return the testUserId
	 */
	protected String getTestUserId() {
		return testUserId;
	}

	/**
	 * @param testUserId the testUserId to set
	 */
	public void setTestUserId(final String testUserId) {
		this.testUserId = StringUtils.nullIfEmpty(testUserId);
	}

	/**
	 * @return the testTokens
	 */
	protected String getTestTokens() {
		return testTokens;
	}

	/**
	 * @param testTokens the testTokens to set
	 */
	public void setTestTokens(final String testTokens) {
		this.testTokens = StringUtils.nullIfEmpty(testTokens);
	}

}
