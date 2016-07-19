/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.urlGeneration;

import java.io.Serializable;
import java.util.Map;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The URL builder interface.
 */
public interface UrlBuilder extends Serializable {

	/**
	 * @param authType
	 * @param params
	 * @return a URL.
	 */
	String getUrl(String authType, Map<String, String> params);

	/**
	 * @param pageParam
	 * @return a map for deep link parameters.
	 */
	Map<String, String> getNewDeepLinkParams(String pageParam);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getAboutUrl(String authType);

	/**
	 * @param applicationUser
	 * @return a URL.
	 */
	String getChangePasswordUrl(User applicationUser);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getWelcomeUrl(String authType);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getPreferencesUrl(String authType);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getManagerPreferencesUrl(String authType);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getToggleTicketReportsUrl(String authType);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getToggleFaqReportsUrl(String authType);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	String getTicketTakeUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	Object getTicketPostponeUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	Object getTicketAssignUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	Object getTicketRefuseClosureUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	Object getTicketApproveClosureUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	Object getTicketRefuseUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	Object getTicketReopenUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	@RequestCache
	Object getTicketGiveInformationUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	Object getTicketRequestInformationUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	Object getTicketFreeUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	Object getTicketCloseUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	Object getTicketTakeAndCloseUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	Object getTicketTakeAndRequestInformationUrl(
			String authType,
			long ticketId);

	/**
	 * @param authType
	 * @param ticketId
	 * @return a URL.
	 */
	String getTicketViewUrl(String authType, long ticketId);

	/**
	 * @param authType
	 * @param targetCategory
	 * @return a URL.
	 */
	String getTicketAddUrl(
			String authType,
			Category targetCategory);

	/**
	 * @param authType
	 * @param advancedSearch
	 * @param tokens
	 * @param exprTokens
	 * @param orTokens
	 * @param notTokens
	 * @param managerId
	 * @param ownerId
	 * @param userId
	 * @param user
	 * @return a URL.
	 */
	String getSearchUrl(
			String authType,
			boolean advancedSearch,
			String tokens,
			String exprTokens,
			String orTokens,
			String notTokens,
			String managerId,
			String ownerId,
			String userId,
			User user);

	/**
	 * @param authType
	 * @param departmentId
	 * @return a URL.
	 */
	String getDepartmentViewUrl(String authType, long departmentId);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getDepartmentsUrl(String authType);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getControlPanelUrl(String authType);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getAdministratorsUrl(String authType);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getJournalUrl(String authType);

	/**
	 * @param authType
	 * @param faq
	 * @return a URL.
	 */
	String getFaqsUrl(
			String authType,
			Faq faq);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getStatisticsUrl(String authType);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getBookmarksUrl(String authType);

	/**
	 * @param authType
	 * @return a URL.
	 */
	String getResponsesUrl(String authType);

}
