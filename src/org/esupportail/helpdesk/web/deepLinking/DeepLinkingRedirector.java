/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.deepLinking;


/**
 * The interface of esup-helpdesk redirector (for deep linking).
 */
public interface DeepLinkingRedirector extends org.esupportail.commons.web.deepLinking.DeepLinkingRedirector {
	
	/** A parameter name. */
	String PAGE_PARAM = "page";
	/** A parameter value. */
	String TICKET_VIEW_PAGE_VALUE = "ticketView";
	/** A parameter value. */
	String TICKET_TAKE_PAGE_VALUE = "ticketTake";
	/** A parameter value. */
	String TICKET_POSTPONE_PAGE_VALUE = "ticketPostpone";
	/** A parameter value. */
	String TICKET_ASSIGN_PAGE_VALUE = "ticketAssign";
	/** A parameter value. */
	String TICKET_REFUSE_CLOSURE_PAGE_VALUE = "ticketRefuseClosure";
	/** A parameter value. */
	String TICKET_APPROVE_CLOSURE_PAGE_VALUE = "ticketApproveClosure";
	/** A parameter value. */
	String TICKET_REFUSE_PAGE_VALUE = "ticketRefuse";
	/** A parameter value. */
	String TICKET_REOPEN_PAGE_VALUE = "ticketReopen";
	/** A parameter value. */
	String TICKET_GIVE_INFORMATION_PAGE_VALUE = "ticketGiveInformation";
	/** A parameter value. */
	String TICKET_REQUEST_INFORMATION_PAGE_VALUE = "ticketRequestInformation";
	/** A parameter value. */
	String TICKET_FREE_PAGE_VALUE = "ticketFree";
	/** A parameter value. */
	String TICKET_CLOSE_PAGE_VALUE = "ticketClose";
	/** A parameter value. */
	String TICKET_TAKE_AND_CLOSE_PAGE_VALUE = "ticketTakeAndClose";
	/** A parameter value. */
	String TICKET_TAKE_AND_REQUEST_INFORMATION_PAGE_VALUE = "ticketTakeAndRequestInformation";
	/** A parameter name. */
	String TICKET_ID_PARAM = "ticketId";
	/** A parameter value. */
	String PREFERENCES_PAGE_VALUE = "preferences";
	/** A parameter value. */
	String MANAGER_PREFERENCES_PAGE_VALUE = "managerPreferences";
	/** A parameter value. */
	String CHANGE_PASSWORD_PAGE_VALUE = "changePassword";
	/** A parameter value. */
	String WELCOME_PAGE_VALUE = "welcome";
	/** A parameter value. */
	String ABOUT_PAGE_VALUE = "about";
	/** A parameter value. */
	String SEARCH_PAGE_VALUE = "search";
	/** A parameter name. */
	String SEARCH_TOKENS_PARAM = "tokens";
	/** A parameter name. */
	String SEARCH_EXPR_TOKENS_PARAM = "exprTokens";
	/** A parameter name. */
	String SEARCH_OR_TOKENS_PARAM = "orTokens";
	/** A parameter name. */
	String SEARCH_NOT_TOKENS_PARAM = "notTokens";
	/** A parameter name. */
	String ADVANCED_SEARCH_PARAM = "advancedSearch";
	/** A parameter name. */
	String SEARCH_TYPE_PARAM = "searchType";
	/** A parameter name. */
	String SEARCH_SORT_BY_DATE_PARAM = "searchSortByDate";
	/** A parameter name. */
	String SEARCH_MANAGER_ID_PARAM = "searchManagerId";
	/** A parameter name. */
	String SEARCH_OWNER_ID_PARAM = "searchOwnerId";
	/** A parameter name. */
	String SEARCH_USER_ID_PARAM = "searchUserId";
	/** A parameter value. */
	String CONTROL_PANEL_PAGE_VALUE = "controlPanel";
	/** A parameter value. */
	String VIEW_DEPARTMENT_PAGE_VALUE = "viewDepartment";	
	/** A parameter name. */
	String DEPARTMENT_ID_PARAM = "departmentId";
	/** A parameter value. */
	String ADMINISTRATORS_PAGE_VALUE = "administrators";
	/** A parameter value. */
	String DEPARTMENTS_PAGE_VALUE = "departments";
	/** A parameter value. */
	String ADD_TICKET_PAGE_VALUE = "addTicket";
	/** A parameter name. */
	String USER_PARAM = "userId";
	/** A parameter name. */
	String PASSWORD_PARAM = "password";
	/** A parameter name. */
	String CATEGORY_ID_PARAM = "categoryId";
	/** A parameter value. */
	String JOURNAL_PAGE_VALUE = "journal";
	/** A parameter value. */
	String FAQ_PAGE_VALUE = "faq";
	/** A parameter name. */
	String FAQ_ID_PARAM = "faqId";
	/** A parameter value. */
	String STATISTICS_PAGE_VALUE = "statistics";
	/** A parameter value. */
	String BOOKMARKS_PAGE_VALUE = "bookmarks";
	/** A parameter value. */
	String RESPONSES_PAGE_VALUE = "responses";
	/** A parameter name. */
	String TOGGLE_TICKET_REPORTS_PARAM = "toggleTicketReports";
	/** A parameter name. */
	String TOGGLE_FAQ_REPORTS_PARAM = "toggleFaqReports";
	
}
