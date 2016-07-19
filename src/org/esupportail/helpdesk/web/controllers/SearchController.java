/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.Search;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.services.indexing.EmptyIndexException;
import org.esupportail.helpdesk.services.indexing.IndexException;
import org.esupportail.helpdesk.services.indexing.Indexer;
import org.esupportail.helpdesk.services.indexing.SearchResults;
import org.esupportail.helpdesk.services.statistics.StatisticsUtils;

/**
 * The search controller.
 */
public class SearchController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5085326706870445478L;

	/**
	 * The default max number of results.
	 */
	private static final int DEFAULT_MAX_RESULTS = 50;

	/**
	 * The indexer.
	 */
	private Indexer indexer;

	/**
	 * The tokens to search.
	 */
	private String tokens;

	/**
	 * The tokens to search.
	 */
	private String exprTokens;

	/**
	 * The tokens to search.
	 */
	private String orTokens;

	/**
	 * The tokens to search.
	 */
	private String notTokens;

	/**
	 * The manager id.
	 */
	private String managerId;

	/**
	 * The owner id.
	 */
	private String ownerId;

	/**
	 * The user id.
	 */
	private String userId;

	/**
	 * The search results.
	 */
	private SearchResults searchResults;

	/**
	 * The max number of results.
	 */
	private int maxResults = DEFAULT_MAX_RESULTS;

	/**
	 * Bean constructor.
	 */
	public SearchController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.notNull(this.indexer,
				"property indexer of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "["
		+ "tokens=[" + tokens + "]"
		+ ", exprTokens=[" + exprTokens + "]"
		+ ", orTokens=[" + orTokens + "]"
		+ ", notTokens=[" + notTokens + "]"
		+ ", managerId=[" + managerId + "]"
		+ ", ownerId=[" + ownerId + "]"
		+ ", userId=[" + userId + "]"
		+ "]";
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		tokens = null;
		exprTokens = null;
		orTokens = null;
		notTokens = null;
		managerId = null;
		ownerId = null;
		userId = null;
		searchResults = null;
	}

	/**
	 * @return true if the current user is allowed to access the view.
	 */
	public boolean isPageAuthorized() {
		return getCurrentUser() != null;
	}

	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			return null;
		}
		User user = getCurrentUser();
		try {
			if (user.getAdvancedSearch()) {
				searchResults = indexer.search(
						user, getClient(),
						user.getSearchDepartmentFilter(),
						tokens, exprTokens, orTokens, notTokens, managerId, ownerId, userId,
						user.getSearchTypeFilter(),
						maxResults, user.getSearchSortByDate(),
						user.getSearchDate1(), user.getSearchDate2());
			} else {
				searchResults = indexer.search(
						user, getClient(),
						user.getSearchDepartmentFilter(),
						tokens, null, null, null, null, null, null,
						user.getSearchTypeFilter(),
						maxResults, false, null, null);
			}
			if (searchResults.isTotalResultsNumberEstimated()) {
				addWarnMessage(null, "SEARCH.MESSAGE.ESTIMATED_RESULT_NUMBER",
						Integer.valueOf(maxResults));
			}
		} catch (EmptyIndexException e) {
			ExceptionUtils.catchException(e);
			addErrorMessage(null, "SEARCH.MESSAGE.EMPTY_INDEX");
		} catch (IndexException e) {
			ExceptionUtils.catchException(e);
			addErrorMessage(null, "SEARCH.MESSAGE.INDEX_ERROR", e.getMessage());
		}
		return "navigationSearch";
	}

	/**
	 * @return true if the current user is a department manager.
	 */
	@RequestCache
	public boolean isCurrentUserDepartmentManager() {
		return getDomainService().isDepartmentManager(getCurrentUser());
	}

	/**
	 * JSF callback.
	 */
	public void search() {
		getDomainService().updateUser(getCurrentUser());
		enter();
	}

	/**
	 * @return the advancedItems
	 */
	@RequestCache
	public List<SelectItem> getAdvancedItems() {
		List<SelectItem> advancedItems = new ArrayList<SelectItem>();
		advancedItems.add(new SelectItem(
				Boolean.FALSE,
				getString("SEARCH.ADVANCED.FALSE")));
		advancedItems.add(new SelectItem(
				Boolean.TRUE,
				getString("SEARCH.ADVANCED.TRUE")));
		return advancedItems;
	}

	/**
	 * @return the searchTypeItems
	 */
	@RequestCache
	public List<SelectItem> getSearchTypeItems() {
		List<SelectItem> searchTypeItems = new ArrayList<SelectItem>();
		searchTypeItems.add(new SelectItem(
				Search.TYPE_FILTER_ALL,
				getString("SEARCH.TYPE_FILTER.ANY")));
		searchTypeItems.add(new SelectItem(
				Search.TYPE_FILTER_ACTIVE_TICKET_AND_FAQ,
				getString("SEARCH.TYPE_FILTER.ACTIVE_TICKET_AND_FAQ")));
		searchTypeItems.add(new SelectItem(
				Search.TYPE_FILTER_ACTIVE_TICKET,
				getString("SEARCH.TYPE_FILTER.ACTIVE_TICKET")));
		searchTypeItems.add(new SelectItem(
				Search.TYPE_FILTER_ARCHIVED_TICKET,
				getString("SEARCH.TYPE_FILTER.ARCHIVED_TICKET")));
		searchTypeItems.add(new SelectItem(
				Search.TYPE_FILTER_FAQ,
				getString("SEARCH.TYPE_FILTER.FAQ")));
		return searchTypeItems;
	}

	/**
	 * @return the searchSortItems
	 */
	@RequestCache
	public List<SelectItem> getSearchSortItems() {
		List<SelectItem> searchSortItems = new ArrayList<SelectItem>();
		searchSortItems.add(new SelectItem(
				Boolean.FALSE,
				getString("SEARCH.SORT.BY_SCORE")));
		searchSortItems.add(new SelectItem(
				Boolean.TRUE,
				getString("SEARCH.SORT.BY_DATE")));
		return searchSortItems;
	}

	/**
	 * @return the departmentItems
	 */
	@RequestCache
	public List<SelectItem> getDepartmentItems() {
		List<SelectItem> departmentItems = new ArrayList<SelectItem>();
		departmentItems.add(new SelectItem("", getString("SEARCH.DEPARTMENT_FILTER.ANY")));
		for (Department dep : getDomainService().getSearchVisibleDepartments(
				getCurrentUser(), getClient())) {
			departmentItems.add(new SelectItem(dep, dep.getLabel()));
		}
		return departmentItems;
	}

	/**
	 * @return the date1Items
	 */
	@RequestCache
	public List<SelectItem> getDate1Items() {
		List<SelectItem> dateItems = new ArrayList<SelectItem>();
		Timestamp oldestTicketDate = getDomainService().getOldestTicketDate();
		if (oldestTicketDate == null) {
			oldestTicketDate = new Timestamp(System.currentTimeMillis());
		}
		Timestamp begin = StatisticsUtils.getMonthRoundedDate(oldestTicketDate);
		Timestamp end = StatisticsUtils.getMonthUpperRoundedDate(new Timestamp(System.currentTimeMillis()));
		dateItems.add(new SelectItem(
				"", getString("SEARCH.DATE_FILTER.BEGIN")));
		while (begin.before(end)) {
			dateItems.add(new SelectItem(
					begin, getString(
							"SEARCH.DATE_FILTER.DATE",
							String.valueOf(StatisticsUtils.getYear(begin)),
							StatisticsUtils.getMonthShortName(begin, getCurrentUserLocale()))));
			begin = StatisticsUtils.getNextMonthDate(begin);
		}
		return dateItems;
	}

	/**
	 * @return the date1Items
	 */
	@RequestCache
	public List<SelectItem> getDate2Items() {
		List<SelectItem> dateItems = new ArrayList<SelectItem>();
		Timestamp oldestTicketDate = getDomainService().getOldestTicketDate();
		if (oldestTicketDate == null) {
			oldestTicketDate = new Timestamp(System.currentTimeMillis());
		}
		Timestamp begin = StatisticsUtils.getMonthRoundedDate(oldestTicketDate);
		Timestamp end = StatisticsUtils.getMonthUpperRoundedDate(new Timestamp(System.currentTimeMillis()));
		while (begin.before(end)) {
			dateItems.add(new SelectItem(
					begin, getString(
							"SEARCH.DATE_FILTER.DATE",
							String.valueOf(StatisticsUtils.getYear(begin)),
							StatisticsUtils.getMonthShortName(begin, getCurrentUserLocale()))));
			begin = StatisticsUtils.getNextMonthDate(begin);
		}
		dateItems.add(new SelectItem(
				"", getString("SEARCH.DATE_FILTER.END")));
		return dateItems;
	}

	/**
	 * @param authType
	 * @return a permanent link to the page.
	 */
	protected String getPermLink(final String authType) {
		return getUrlBuilder().getSearchUrl(
				authType, getCurrentUser().getAdvancedSearch(),
				tokens, exprTokens, orTokens, notTokens,
				managerId, ownerId, userId,
				getCurrentUser());
	}

	/**
	 * @return a permanent link to the page for application users.
	 */
	public String getApplicationPermLink() {
		return getPermLink(AuthUtils.APPLICATION);
	}

	/**
	 * @return a permanent link to the page for CAS users.
	 */
	public String getCasPermLink() {
		return getPermLink(AuthUtils.CAS);
	}

	/**
	 * @return a permanent link to the page for Shibboleth users.
	 */
	public String getShibbolethPermLink() {
		return getPermLink(AuthUtils.SHIBBOLETH);
	}

	/**
	 * @return a permanent link to the page for specific users.
	 */
	public String getSpecificPermLink() {
		return getPermLink(AuthUtils.SPECIFIC);
	}

	/**
	 * @return the indexer
	 */
	protected Indexer getIndexer() {
		return indexer;
	}

	/**
	 * @param indexer the indexer to set
	 */
	public void setIndexer(final Indexer indexer) {
		this.indexer = indexer;
	}

	/**
	 * @return the tokens
	 */
	public String getTokens() {
		return tokens;
	}

	/**
	 * @param tokens the tokens to set
	 */
	public void setTokens(final String tokens) {
		this.tokens = StringUtils.nullIfEmpty(tokens);
	}

	/**
	 * @return the searchResults
	 */
	public SearchResults getSearchResults() {
		return searchResults;
	}

	/**
	 * @return the exprTokens
	 */
	public String getExprTokens() {
		return exprTokens;
	}

	/**
	 * @param exprTokens the exprTokens to set
	 */
	public void setExprTokens(final String exprTokens) {
		this.exprTokens = exprTokens;
	}

	/**
	 * @return the orTokens
	 */
	public String getOrTokens() {
		return orTokens;
	}

	/**
	 * @param orTokens the orTokens to set
	 */
	public void setOrTokens(final String orTokens) {
		this.orTokens = orTokens;
	}

	/**
	 * @return the notTokens
	 */
	public String getNotTokens() {
		return notTokens;
	}

	/**
	 * @param notTokens the notTokens to set
	 */
	public void setNotTokens(final String notTokens) {
		this.notTokens = notTokens;
	}

	/**
	 * @return the managerId
	 */
	public String getManagerId() {
		return managerId;
	}

	/**
	 * @param managerId the managerId to set
	 */
	public void setManagerId(final String managerId) {
		this.managerId = StringUtils.nullIfEmpty(managerId);
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(final String userId) {
		this.userId = StringUtils.nullIfEmpty(userId);
	}

	/**
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(final String ownerId) {
		this.ownerId = StringUtils.nullIfEmpty(ownerId);
	}

	/**
	 * @return the maxResults
	 */
	protected int getMaxResults() {
		return maxResults;
	}

	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(final int maxResults) {
		this.maxResults = maxResults;
	}

}
