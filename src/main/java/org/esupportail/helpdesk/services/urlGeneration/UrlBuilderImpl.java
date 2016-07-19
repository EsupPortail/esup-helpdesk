/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.urlGeneration;

import java.util.HashMap;
import java.util.Map;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.urlGeneration.UrlGenerator;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.web.deepLinking.DeepLinkingRedirector;
import org.springframework.beans.factory.InitializingBean;

/**
 * The basic implementation of UrlBuilder.
 */
public class UrlBuilderImpl implements UrlBuilder, InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5793449965052333880L;

	/**
	 * The URL generator.
	 */
	private UrlGenerator urlGenerator;

	/**
	 * Constructor.
	 */
	public UrlBuilderImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.urlGenerator,
				"property urlGenerator of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getUrl(java.lang.String, java.util.Map)
	 */
	@Override
	@RequestCache
	public String getUrl(final String authType, final Map<String, String> params) {
		if (AuthUtils.SHIBBOLETH.equals(authType)) {
			return urlGenerator.shibbolethUrl(params);
		}
		if (AuthUtils.CAS.equals(authType)) {
			return urlGenerator.casUrl(params);
		}
		return urlGenerator.guestUrl(params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getNewDeepLinkParams(java.lang.String)
	 */
	@Override
	@RequestCache
	public Map<String, String> getNewDeepLinkParams(final String pageParam) {
		Map<String, String> params = new HashMap<String, String>();
		if (pageParam != null) {
			params.put(DeepLinkingRedirector.PAGE_PARAM, pageParam);
		}
		return params;
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getAboutUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getAboutUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.ABOUT_PAGE_VALUE);
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getChangePasswordUrl(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public String getChangePasswordUrl(final User applicationUser) {
		Map<String, String> params = new HashMap<String, String>();
		if (applicationUser != null) {
			params.put(DeepLinkingRedirector.PAGE_PARAM, DeepLinkingRedirector.CHANGE_PASSWORD_PAGE_VALUE);
			params.put(DeepLinkingRedirector.USER_PARAM, applicationUser.getRealId());
			params.put(DeepLinkingRedirector.PASSWORD_PARAM, applicationUser.getPassword());
		} else {
			params.put(DeepLinkingRedirector.PAGE_PARAM, DeepLinkingRedirector.WELCOME_PAGE_VALUE);
		}
		return getUrl(AuthUtils.APPLICATION, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getWelcomeUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getWelcomeUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.WELCOME_PAGE_VALUE);
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getPreferencesUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getPreferencesUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.PREFERENCES_PAGE_VALUE);
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getManagerPreferencesUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getManagerPreferencesUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.MANAGER_PREFERENCES_PAGE_VALUE);
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getToggleTicketReportsUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getToggleTicketReportsUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.MANAGER_PREFERENCES_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TOGGLE_TICKET_REPORTS_PARAM, "");
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getToggleFaqReportsUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getToggleFaqReportsUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.MANAGER_PREFERENCES_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TOGGLE_FAQ_REPORTS_PARAM, "");
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketTakeUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public String getTicketTakeUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.TICKET_TAKE_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketPostponeUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketPostponeUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.TICKET_POSTPONE_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketAssignUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketAssignUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.TICKET_ASSIGN_PAGE_VALUE
				);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketRefuseClosureUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketRefuseClosureUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(
				DeepLinkingRedirector.TICKET_REFUSE_CLOSURE_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketApproveClosureUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketApproveClosureUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(
				DeepLinkingRedirector.TICKET_APPROVE_CLOSURE_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketRefuseUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketRefuseUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.TICKET_REFUSE_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketReopenUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketReopenUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.TICKET_REOPEN_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketGiveInformationUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketGiveInformationUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(
				DeepLinkingRedirector.TICKET_GIVE_INFORMATION_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketRequestInformationUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketRequestInformationUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(
				DeepLinkingRedirector.TICKET_REQUEST_INFORMATION_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketFreeUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketFreeUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.TICKET_FREE_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketCloseUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketCloseUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.TICKET_CLOSE_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketTakeAndCloseUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketTakeAndCloseUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(
				DeepLinkingRedirector.TICKET_TAKE_AND_CLOSE_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketTakeAndRequestInformationUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public Object getTicketTakeAndRequestInformationUrl(
			final String authType,
			final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(
				DeepLinkingRedirector.TICKET_TAKE_AND_REQUEST_INFORMATION_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketViewUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public String getTicketViewUrl(final String authType, final long ticketId) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.TICKET_VIEW_PAGE_VALUE);
		params.put(DeepLinkingRedirector.TICKET_ID_PARAM, Long.toString(ticketId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getTicketAddUrl(
	 * java.lang.String, org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public String getTicketAddUrl(
			final String authType,
			final Category targetCategory) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.ADD_TICKET_PAGE_VALUE);
		if (targetCategory != null) {
			params.put(DeepLinkingRedirector.CATEGORY_ID_PARAM, Long.toString(targetCategory.getId()));
		}
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getSearchUrl(
	 * java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public String getSearchUrl(
			final String authType,
			final boolean advancedSearch,
			final String tokens,
			final String exprTokens,
			final String orTokens,
			final String notTokens,
			final String managerId,
			final String ownerId,
			final String userId,
			final User user) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.SEARCH_PAGE_VALUE);
		params.put(DeepLinkingRedirector.ADVANCED_SEARCH_PARAM, Boolean.toString(advancedSearch));
		if (tokens != null) {
			params.put(DeepLinkingRedirector.SEARCH_TOKENS_PARAM, tokens);
		}
		if (advancedSearch) {
			if (exprTokens != null) {
				params.put(DeepLinkingRedirector.SEARCH_EXPR_TOKENS_PARAM, exprTokens);
			}
			if (orTokens != null) {
				params.put(DeepLinkingRedirector.SEARCH_OR_TOKENS_PARAM, orTokens);
			}
			if (notTokens != null) {
				params.put(DeepLinkingRedirector.SEARCH_NOT_TOKENS_PARAM, notTokens);
			}
			if (managerId != null) {
				params.put(DeepLinkingRedirector.SEARCH_MANAGER_ID_PARAM, managerId);
			}
			if (ownerId != null) {
				params.put(DeepLinkingRedirector.SEARCH_OWNER_ID_PARAM, ownerId);
			}
			if (userId != null) {
				params.put(DeepLinkingRedirector.SEARCH_USER_ID_PARAM, userId);
			}
			if (user != null) {
				params.put(DeepLinkingRedirector.SEARCH_SORT_BY_DATE_PARAM,
						String.valueOf(user.getSearchSortByDate()));
			}
		}
		if (user != null) {
			params.put(DeepLinkingRedirector.SEARCH_TYPE_PARAM,
					String.valueOf(user.getSearchSortByDate()));
			if (user.getSearchDepartmentFilter() != null) {
				params.put(
						DeepLinkingRedirector.DEPARTMENT_ID_PARAM,
						Long.toString(user.getSearchDepartmentFilter().getId()));
			}
		}
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getDepartmentViewUrl(java.lang.String, long)
	 */
	@Override
	@RequestCache
	public String getDepartmentViewUrl(final String authType, final long departmentId) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.VIEW_DEPARTMENT_PAGE_VALUE);
		params.put(DeepLinkingRedirector.DEPARTMENT_ID_PARAM, Long.toString(departmentId));
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getDepartmentsUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getDepartmentsUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.DEPARTMENTS_PAGE_VALUE);
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getControlPanelUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getControlPanelUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.CONTROL_PANEL_PAGE_VALUE);
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getAdministratorsUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getAdministratorsUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.ADMINISTRATORS_PAGE_VALUE);
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getJournalUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getJournalUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.JOURNAL_PAGE_VALUE);
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getFaqsUrl(
	 * java.lang.String, org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	@RequestCache
	public String getFaqsUrl(
			final String authType,
			final Faq faq) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.FAQ_PAGE_VALUE);
		if (faq != null) {
			params.put(DeepLinkingRedirector.FAQ_ID_PARAM, Long.toString(faq.getId()));
		}
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getStatisticsUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getStatisticsUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.STATISTICS_PAGE_VALUE);
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getBookmarksUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getBookmarksUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.BOOKMARKS_PAGE_VALUE);
		return getUrl(authType, params);
	}

	/**
	 * @see org.esupportail.helpdesk.services.urlGeneration.UrlBuilder#getResponsesUrl(java.lang.String)
	 */
	@Override
	@RequestCache
	public String getResponsesUrl(final String authType) {
		Map<String, String> params = getNewDeepLinkParams(DeepLinkingRedirector.RESPONSES_PAGE_VALUE);
		return getUrl(authType, params);
	}

	/**
	 * @return the urlGenerator
	 */
	protected UrlGenerator getUrlGenerator() {
		return urlGenerator;
	}

	/**
	 * @param urlGenerator the urlGenerator to set
	 */
	public void setUrlGenerator(final UrlGenerator urlGenerator) {
		this.urlGenerator = urlGenerator;
	}

}
