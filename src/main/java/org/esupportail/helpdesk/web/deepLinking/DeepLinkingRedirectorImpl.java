/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.deepLinking;

import java.util.List;
import java.util.Map;

import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeState;
import org.apache.myfaces.custom.tree2.TreeStateBase;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.web.beans.TreeModelBase;
import org.esupportail.commons.web.deepLinking.AbstractDeepLinkingRedirector;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.exceptions.ArchivedTicketNotFoundException;
import org.esupportail.helpdesk.exceptions.CategoryNotFoundException;
import org.esupportail.helpdesk.exceptions.DepartmentNotFoundException;
import org.esupportail.helpdesk.exceptions.FaqNotFoundException;
import org.esupportail.helpdesk.exceptions.TicketNotFoundException;
import org.esupportail.helpdesk.web.beans.CategoryNode;
import org.esupportail.helpdesk.web.controllers.AdministratorsController;
import org.esupportail.helpdesk.web.controllers.ArchivedTicketController;
import org.esupportail.helpdesk.web.controllers.BookmarksController;
import org.esupportail.helpdesk.web.controllers.ControlPanelController;
import org.esupportail.helpdesk.web.controllers.DepartmentsController;
import org.esupportail.helpdesk.web.controllers.FaqsController;
import org.esupportail.helpdesk.web.controllers.JournalController;
import org.esupportail.helpdesk.web.controllers.PreferencesController;
import org.esupportail.helpdesk.web.controllers.ResponsesController;
import org.esupportail.helpdesk.web.controllers.SearchController;
import org.esupportail.helpdesk.web.controllers.SessionController;
import org.esupportail.helpdesk.web.controllers.StatisticsController;
import org.esupportail.helpdesk.web.controllers.TicketController;
import org.springframework.util.StringUtils;

/**
 * The esup-print implementation of the page redirector (for deep linking).
 */
public class DeepLinkingRedirectorImpl extends AbstractDeepLinkingRedirector implements DeepLinkingRedirector {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -864719862317795339L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The domain service.
	 */
	private DomainService domainService;

	/**
	 * The session controller.
	 */
	private SessionController sessionController;

	/**
	 * The ticket controller.
	 */
	private TicketController ticketController;

	/**
	 * The archived ticket controller.
	 */
	private ArchivedTicketController archivedTicketController;

	/**
	 * The preferences controller.
	 */
	private PreferencesController preferencesController;

	/**
	 * The search controller.
	 */
	private SearchController searchController;

	/**
	 * The control panel controller.
	 */
	private ControlPanelController controlPanelController;

	/**
	 * The administrators controller.
	 */
	private AdministratorsController administratorsController;

	/**
	 * The departments controller.
	 */
	private DepartmentsController departmentsController;

	/**
	 * The journal controller.
	 */
	private JournalController journalController;

	/**
	 * The FAQ controller.
	 */
	private FaqsController faqsController;

	/**
	 * The statistics controller.
	 */
	private StatisticsController statisticsController;

	/**
	 * The bookmarks controller.
	 */
	private BookmarksController bookmarksController;

	/**
	 * The responses controller.
	 */
	private ResponsesController responsesController;

	/**
	 * Bean constructor.
	 */
	public DeepLinkingRedirectorImpl() {
		super();
	}

	/**
	 * @see org.esupportail.commons.beans.AbstractI18nAwareBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(domainService,
				"property domainService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(sessionController,
				"property sessionController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(ticketController,
				"property ticketController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(archivedTicketController,
				"property archivedTicketController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(preferencesController,
				"property preferencesController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(searchController,
				"property searchController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(controlPanelController,
				"property controlPanelController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(administratorsController,
				"property administratorsController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(departmentsController,
				"property departmentsController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(journalController,
				"property journalController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(faqsController,
				"property faqsController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(statisticsController,
				"property statisticsController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(bookmarksController,
				"property bookmarksController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(responsesController,
				"property responsesController of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * Add an authentication required message.
	 */
	protected void addAuthenticationRequiredErrorMessage() {
		addErrorMessage(null, "DEEP_LINKS.MESSAGE.AUTHENTICATION_REQUIRED");
	}

	/**
	 * @param params
	 * @return the ticket id that corresponds to params.
	 * @throws IllegalArgumentException
	 */
	protected Long getTicketId(final Map<String, String> params) throws IllegalArgumentException {
		if (params.get(TICKET_ID_PARAM) == null) {
			addErrorMessageMissingParameter(TICKET_ID_PARAM);
			throw new IllegalArgumentException(TICKET_ID_PARAM);
		}
		try {
			return Long.valueOf(params.get(TICKET_ID_PARAM));
		} catch (NumberFormatException e) {
			addErrorMessageInvalidParameter(TICKET_ID_PARAM, params.get(TICKET_ID_PARAM));
			throw new IllegalArgumentException(TICKET_ID_PARAM);
		}
	}

	/**
	 * @param params
	 * @return the ticket that corresponds to params.
	 * @throws IllegalArgumentException
	 */
	protected Ticket getTicket(final Map<String, String> params) throws IllegalArgumentException {
		try {
			return domainService.getTicket(getTicketId(params));
		} catch (TicketNotFoundException e) {
			return null;
		}
	}

	/**
	 * Redirect to the ticketView page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected ArchivedTicket getArchivedTicket(final Map<String, String> params) {
		try {
			return domainService.getArchivedTicketByOriginalId(getTicketId(params));
		} catch (ArchivedTicketNotFoundException e) {
			return null;
		}
	}

	/**
	 * Redirect to the ticketView page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectTicketAction(final Map<String, String> params) {
		try {
			Ticket ticket = getTicket(params);
			if (ticket != null) {
				ticketController.setTicket(ticket);
				if (!ticketController.isUserCanViewTicket()) {
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.VIEW_TICKET_NOT_ALLOWED", String.valueOf(ticket.getId()));
				}
				if (TICKET_TAKE_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanTake()) {
						ticketController.take();
						return "/stylesheets/ticketTake.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.TAKE_TICKET_NOT_ALLOWED", String.valueOf(ticket.getId()));
				}
				if (TICKET_POSTPONE_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanPostpone()) {
						ticketController.postpone();
						return "/stylesheets/ticketPostpone.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.POSTPONE_TICKET_NOT_ALLOWED",
							String.valueOf(ticket.getId()));
				}
				if (TICKET_ASSIGN_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanAssign()) {
						ticketController.assign();
						return "/stylesheets/ticketAssign.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.ASSIGN_TICKET_NOT_ALLOWED",
							String.valueOf(ticket.getId()));
				}
				if (TICKET_REFUSE_CLOSURE_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanRefuseClosure()) {
						ticketController.refuseClosure();
						return "/stylesheets/ticketRefuseClosure.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.REFUSE_CLOSURE_TICKET_NOT_ALLOWED",
							String.valueOf(ticket.getId()));
				}
				if (TICKET_APPROVE_CLOSURE_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanApproveClosure()) {
						ticketController.approveClosure();
						return "/stylesheets/ticketApproveClosure.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.APPROVE_CLOSURE_TICKET_NOT_ALLOWED",
							String.valueOf(ticket.getId()));
				}
				if (TICKET_REFUSE_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanRefuse()) {
						ticketController.refuse();
						return "/stylesheets/ticketRefuse.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.REFUSE_TICKET_NOT_ALLOWED",
							String.valueOf(ticket.getId()));
				}
				if (TICKET_REOPEN_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanReopen()) {
						ticketController.reopen();
						return "/stylesheets/ticketReopen.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.REOPEN_TICKET_NOT_ALLOWED",
							String.valueOf(ticket.getId()));
				}
				if (TICKET_GIVE_INFORMATION_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanGiveInformation()) {
						ticketController.giveInformation();
						return "/stylesheets/ticketGiveInformation.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.GIVE_INFORMATION_TICKET_NOT_ALLOWED",
							String.valueOf(ticket.getId()));
				}
				if (TICKET_REQUEST_INFORMATION_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanRequestInformation()) {
						ticketController.requestInformation();
						return "/stylesheets/ticketRequestInformation.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.REQUEST_INFORMATION_TICKET_NOT_ALLOWED",
							String.valueOf(ticket.getId()));
				}
				if (TICKET_FREE_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanFree()) {
						ticketController.free();
						return "/stylesheets/ticketFree.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.FREE_TICKET_NOT_ALLOWED", String.valueOf(ticket.getId()));
				}
				if (TICKET_CLOSE_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanClose()) {
						ticketController.close();
						controlPanelController.enter();
						return "/stylesheets/ticketClose.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.CLOSE_TICKET_NOT_ALLOWED",
							String.valueOf(ticket.getId()));
				}
				if (TICKET_TAKE_AND_CLOSE_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanTakeAndClose()) {
						ticketController.takeAndClose();
						controlPanelController.enter();
						return "/stylesheets/ticketTakeAndClose.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.TAKE_AND_CLOSE_TICKET_NOT_ALLOWED",
							String.valueOf(ticket.getId()));
				}
				if (TICKET_TAKE_AND_REQUEST_INFORMATION_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
					if (ticketController.isUserCanTakeAndRequestInformation()) {
						ticketController.takeAndRequestInformation();
						return "/stylesheets/ticketTakeAndRequestInformation.jsp";
					}
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.TAKE_AND_REQUEST_INFORMATION_TICKET_NOT_ALLOWED",
							String.valueOf(ticket.getId()));
				}
				return "/stylesheets/ticketView.jsp";
			}
			ArchivedTicket archivedTicket = getArchivedTicket(params);
			if (archivedTicket != null) {
				archivedTicketController.setArchivedTicket(archivedTicket);
				if (!archivedTicketController.isUserCanViewArchivedTicket()) {
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.VIEW_TICKET_NOT_ALLOWED",
							String.valueOf(archivedTicket.getTicketId()));
				}
				return "/stylesheets/archivedTicketView.jsp";
			}
			addErrorMessage(null, "DEEP_LINKS.MESSAGE.TICKET_NOT_FOUND", params.get(TICKET_ID_PARAM));
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Redirect to the ticketAdd page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectTicketAdd(final Map<String, String> params) {
		Department department = null;
		Category categoryFilter = null;
		TreeModelBase addTree = null;

		if (params.get(DEPARTMENT_ID_PARAM) != null) {
			Long departmentId = null;
			try {
				departmentId = Long.valueOf(params.get(DEPARTMENT_ID_PARAM));
			} catch (NumberFormatException e) {
				//on affiche l'arborescence complète
				ticketController.refreshAddTree();
				return "/stylesheets/ticketAdd.jsp";
			}
			if (departmentId != null) {
				try {
					department = getDomainService().getDepartment(departmentId);
				} catch (DepartmentNotFoundException e) {
					addWarnMessage(null, "DEEP_LINKS.MESSAGE.DEPARTMENT_NOT_FOUND", params.get(DEPARTMENT_ID_PARAM));
					ticketController.setAddTree(null);
					ticketController.setAddTargetCategory(null);
					ticketController.setAddTargetDepartment(null);
					return "/stylesheets/ticketAdd.jsp";
				}

				TreeNode rootNode = ticketController.buildRootAddNodeForDepartment(departmentId);

				//ouverture complète de l'arborescence
				if (rootNode.getChildCount() > 0) {
					addTree = new TreeModelBase(rootNode);
					TreeState treeState = new TreeStateBase();
					treeState.toggleExpanded("0");
					
					List<CategoryNode> nodesToCollapse = rootNode.getChildren();
					ticketController.expandAllTree(treeState, nodesToCollapse);
					addTree.setTreeState(treeState);
					ticketController.setAddTree(addTree);
				}
			}
		} else if (params.get(CATEGORY_ID_PARAM) != null) {
			Long categoryId = null;
			try {
				categoryId = Long.valueOf(params.get(CATEGORY_ID_PARAM));
			} catch (NumberFormatException e) {
				//on affiche l'arborescence complète
				ticketController.refreshAddTree();
				return "/stylesheets/ticketAdd.jsp";				
			}
			if (categoryId != null) {
				try {
					categoryFilter = getDomainService().getCategory(categoryId);
				} catch (CategoryNotFoundException e) {
					addWarnMessage(null, "DEEP_LINKS.MESSAGE.CATEGORY_NOT_FOUND", params.get(CATEGORY_ID_PARAM));
					ticketController.setAddTree(null);
					ticketController.setAddTargetCategory(null);
					ticketController.setAddTargetDepartment(null);
					return "/stylesheets/ticketAdd.jsp";
				}
				
				if (categoryFilter.isVirtual()) {
					try {
						categoryFilter = categoryFilter.getRealCategory();
					} catch (CategoryNotFoundException e) {
						addWarnMessage(null, "DEEP_LINKS.MESSAGE.CATEGORY_NOT_FOUND", params.get(CATEGORY_ID_PARAM));
						ticketController.setAddTree(null);
						ticketController.setAddTargetCategory(null);
						ticketController.setAddTargetDepartment(null);
						return "/stylesheets/ticketAdd.jsp";
					}
				}
				//si la catégorie n'a pas d'enfant, on affiche la création du ticket étape 2/2 
				if(domainService.getSubCategories(categoryFilter).isEmpty() || categoryFilter.getAddNewTickets()) {
					if (!ticketController.isUserCanAdd()) {
						addAuthenticationRequiredErrorMessage();
					} else {
						ticketController.add();
						department = categoryFilter.getDepartment();
						ticketController.setAddTargetCategory(categoryFilter);
						ticketController.setAddTargetDepartment(categoryFilter.getDepartment());
						ticketController.addChooseCategory();
						ticketController.setAddTargetDepartment(department);
					}
				//sinon on affiche l'arborescence
				} else {
					TreeNode rootNode = ticketController.buildRootAddNodeForCategory(categoryId);
					//ouverture complète de l'arborescence
					if (rootNode.getChildCount() > 0) {
						addTree = new TreeModelBase(rootNode);
						TreeState treeState = new TreeStateBase();
						treeState.toggleExpanded("0");
						
						List<CategoryNode> nodesToCollapse = rootNode.getChildren();
						ticketController.expandAllTree(treeState, nodesToCollapse);
						addTree.setTreeState(treeState);
						ticketController.setAddTree(addTree);
					}
				}
			}
		}
		return "/stylesheets/ticketAdd.jsp";
	}

	/**
	 * Redirect to the welcome page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectWelcome(@SuppressWarnings("unused") final Map<String, String> params) {
		return "/stylesheets/welcome.jsp";
	}

	/**
	 * Redirect to the about page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectAbout(@SuppressWarnings("unused") final Map<String, String> params) {
		return "/stylesheets/about.jsp";
	}

	/**
	 * Redirect to the preferences page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectPreferences(@SuppressWarnings("unused") final Map<String, String> params) {
		if (!preferencesController.isPageAuthorized()) {
			addAuthenticationRequiredErrorMessage();
		}
		preferencesController.enter();
		return "/stylesheets/preferences.jsp";
	}

	/**
	 * Redirect to the manager preferences page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectManagerPreferences(final Map<String, String> params) {
		if (!preferencesController.isPageAuthorized()) {
			addAuthenticationRequiredErrorMessage();
		} else if (!preferencesController.isUserManagerOrAdmin()) {
			addErrorMessage(null, "DEEP_LINKS.MESSAGE.MANAGER_PREFERENCES_NOT_ALLOWED");
		} else {
			if (params.get(TOGGLE_TICKET_REPORTS_PARAM) != null) {
				preferencesController.toggleTicketReports();
			}
			if (params.get(TOGGLE_FAQ_REPORTS_PARAM) != null) {
				preferencesController.toggleFaqReports();
			}
		}
		preferencesController.enter();
		return "/stylesheets/managerPreferences.jsp";
	}

	/**
	 * Redirect to the search page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectSearch(final Map<String, String> params) {
		if (!searchController.isPageAuthorized()) {
			addAuthenticationRequiredErrorMessage();
		}
		if (params != null) {
			searchController.setTokens(params.get(SEARCH_TOKENS_PARAM));
			searchController.setExprTokens(params.get(SEARCH_EXPR_TOKENS_PARAM));
			searchController.setOrTokens(params.get(SEARCH_OR_TOKENS_PARAM));
			searchController.setNotTokens(params.get(SEARCH_NOT_TOKENS_PARAM));
			searchController.setManagerId(params.get(SEARCH_MANAGER_ID_PARAM));
			searchController.setOwnerId(params.get(SEARCH_OWNER_ID_PARAM));
			searchController.setUserId(params.get(SEARCH_USER_ID_PARAM));
			User currentUser = sessionController.getCurrentUser();
			if (currentUser != null) {
				if (params.get(ADVANCED_SEARCH_PARAM) != null) {
					currentUser.setAdvancedSearch(Boolean.parseBoolean(params.get(ADVANCED_SEARCH_PARAM)));
				}
				if (params.get(SEARCH_SORT_BY_DATE_PARAM) != null) {
					currentUser.setSearchSortByDate(Boolean.parseBoolean(params.get(SEARCH_SORT_BY_DATE_PARAM)));
				}
				currentUser.setSearchTypeFilter(params.get(SEARCH_TYPE_PARAM));
				if (params.get(DEPARTMENT_ID_PARAM) != null) {
					Long departmentId;
					try {
						departmentId = Long.valueOf(params.get(DEPARTMENT_ID_PARAM));
					} catch (NumberFormatException e) {
						addErrorMessageInvalidParameter(DEPARTMENT_ID_PARAM, params.get(DEPARTMENT_ID_PARAM));
						return null;
					}
					try {
						Department department = domainService.getDepartment(departmentId);
						if (getDomainService().isDepartmentVisibleForSearch(currentUser, department,
								sessionController.getClient())) {
							currentUser.setSearchDepartmentFilter(department);
						}
					} catch (DepartmentNotFoundException e) {
						// set no department filter
					}
				}
				getDomainService().updateUser(currentUser);
			}
		}
		searchController.enter();
		return "/stylesheets/search.jsp";
	}

	/**
	 * Redirect to the control panel page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectControlPanel(@SuppressWarnings("unused") final Map<String, String> params) {
		if (!controlPanelController.isPageAuthorized()) {
			addAuthenticationRequiredErrorMessage();
		}
		controlPanelController.enter();
		return "/stylesheets/controlPanel.jsp";
	}

	/**
	 * Redirect to the departmentView page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectDepartmentView(final Map<String, String> params) {
		if (params.get(DEPARTMENT_ID_PARAM) == null) {
			addErrorMessageMissingParameter(DEPARTMENT_ID_PARAM);
			return null;
		}
		Long departmentId;
		try {
			departmentId = Long.valueOf(params.get(DEPARTMENT_ID_PARAM));
		} catch (NumberFormatException e) {
			addErrorMessageInvalidParameter(DEPARTMENT_ID_PARAM, params.get(DEPARTMENT_ID_PARAM));
			return null;
		}
		try {
			Department department = domainService.getDepartment(departmentId);
			departmentsController.enter();
			departmentsController.setDepartment(department);
			if (!departmentsController.isCurrentUserCanViewDepartment()) {
				addErrorMessage(null, "DEEP_LINKS.MESSAGE.VIEW_DEPARTMENT_NOT_ALLOWED", department.getLabel());
			} else {
				departmentsController.viewDepartment();
			}
			return "/stylesheets/departmentView.jsp";
		} catch (DepartmentNotFoundException e) {
			addErrorMessage(null, "DEEP_LINKS.MESSAGE.DEPARTMENT_NOT_FOUND", departmentId.toString());
			return null;
		}
	}

	/**
	 * Redirect to the departments page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectDepartments(@SuppressWarnings("unused") final Map<String, String> params) {
		if (sessionController.getCurrentUser() == null) {
			addAuthenticationRequiredErrorMessage();
		} else if (!departmentsController.isCurrentUserCanViewDepartments()) {
			addErrorMessage(null, "DEEP_LINKS.MESSAGE.DEPARTMENTS_NOT_ALLOWED");
		}
		departmentsController.enter();
		return "/stylesheets/departments.jsp";
	}

	/**
	 * Redirect to the administrators page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectAdministrators(@SuppressWarnings("unused") final Map<String, String> params) {
		if (sessionController.getCurrentUser() == null) {
			addAuthenticationRequiredErrorMessage();
		} else if (!administratorsController.isPageAuthorized()) {
			addErrorMessage(null, "DEEP_LINKS.MESSAGE.ADMINISTRATORS_NOT_ALLOWED");
			return null;
		}
		administratorsController.enter();
		return "/stylesheets/administrators.jsp";
	}

	/**
	 * Redirect to the journal page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectJournal(@SuppressWarnings("unused") final Map<String, String> params) {
		if (sessionController.getCurrentUser() == null) {
			addAuthenticationRequiredErrorMessage();
		} else if (!journalController.isPageAuthorized()) {
			addErrorMessage(null, "DEEP_LINKS.MESSAGE.JOURNAL_NOT_ALLOWED");
			return null;
		}
		journalController.enter();
		return "/stylesheets/journal.jsp";
	}

	/**
	 * Redirect to the FAQ page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectFaq(final Map<String, String> params) {
		faqsController.enter();
		if (params != null && params.get(FAQ_ID_PARAM) != null) {
			Long faqId = null;
			try {
				faqId = Long.valueOf(params.get(FAQ_ID_PARAM));
			} catch (NumberFormatException e) {
				addErrorMessageInvalidParameter(FAQ_ID_PARAM, params.get(FAQ_ID_PARAM));
			}
			if (faqId != null) {
				try {
					Faq faq = domainService.getFaq(faqId);
					if (!faqsController.userCanViewFaq(faq)) {
						addErrorMessage(null, "DEEP_LINKS.MESSAGE.VIEW_FAQ_NOT_ALLOWED", faqId.toString());
					} else {
						faqsController.setFaq(faq);
					}
				} catch (FaqNotFoundException e) {
					addErrorMessage(null, "DEEP_LINKS.MESSAGE.FAQ_NOT_FOUND", faqId.toString());
				}
			}
		}
		return "/stylesheets/faqs.jsp";
	}

	/**
	 * Redirect to the statistics page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectStatistics(@SuppressWarnings("unused") final Map<String, String> params) {
		if (sessionController.getCurrentUser() == null) {
			addAuthenticationRequiredErrorMessage();
		} else if (!statisticsController.isPageAuthorized()) {
			addErrorMessage(null, "DEEP_LINKS.MESSAGE.STATISTICS_NOT_ALLOWED");
			return null;
		}
		statisticsController.enter();
		return "/stylesheets/statistics.jsp";
	}

	/**
	 * Redirect to the bookmarks page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectBookmarks(@SuppressWarnings("unused") final Map<String, String> params) {
		if (sessionController.getCurrentUser() == null) {
			addAuthenticationRequiredErrorMessage();
		}
		bookmarksController.enter();
		return "/stylesheets/bookmarks.jsp";
	}

	/**
	 * Redirect to the responses page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectResponses(@SuppressWarnings("unused") final Map<String, String> params) {
		if (sessionController.getCurrentUser() == null) {
			addAuthenticationRequiredErrorMessage();
		} else if (!responsesController.isPageAuthorized()) {
			addErrorMessage(null, "DEEP_LINKS.MESSAGE.RESPONSES_NOT_ALLOWED");
			return null;
		}
		responsesController.enter();
		return "/stylesheets/responses.jsp";
	}

	/**
	 * Redirect to the changePassword page.
	 * 
	 * @param params
	 * @return a String.
	 */
	protected String redirectChangePassword(final Map<String, String> params) {
		if (logger.isDebugEnabled()) {
			logger.debug("redirectChangePassword()");
			logger.debug("params=" + params);
		}
		String userId = params.get(USER_PARAM);
		if (userId == null) {
			addErrorMessageMissingParameter(USER_PARAM);
		}
		String password = params.get(PASSWORD_PARAM);
		if (password == null) {
			addErrorMessageMissingParameter(PASSWORD_PARAM);
		}
		params.remove(USER_PARAM);
		params.remove(PASSWORD_PARAM);
		sessionController.setLoginParams(params);
		if (userId == null || password == null) {
			return null;
		}
		sessionController.unsetCurrentUser();
		sessionController.setEmail(userId);
		sessionController.setPassword(password);
		sessionController.applicationLoginFromRedirector();
		if (sessionController.getCurrentUser() == null) {
			addAuthenticationRequiredErrorMessage();
		} else {
			preferencesController.gotoChangePassword();
			preferencesController.setOldPassword(password);
		}
		return "/stylesheets/changePassword.jsp";
	}

	/**
	 * @see org.esupportail.commons.web.deepLinking.DeepLinkingRedirector#redirect(java.util.Map)
	 */
	public String redirect(final Map<String, String> params) {
		sessionController.resetSessionLocale();
		if (sessionController.getCurrentUser() == null) {
			sessionController.cookieLogin();
		}
		sessionController.setNormalState();
		sessionController.setLoginParams(params);
		User user = sessionController.getCurrentUser();
		if (user != null && user.getLanguage() == null) {
			user.setLanguage(sessionController.getLocale().toString());
			getDomainService().updateUser(user);
		}
		if (params != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("params is not null");
				for (String paramName : params.keySet()) {
					logger.debug("[" + paramName + "] => [" + params.get(paramName) + "]");
				}
			}
			if (params.containsKey(ENTER_PARAM)) {
				// force the authenticator to renew authentication
				sessionController.unsetCurrentUser();
			}
			user = sessionController.getCurrentUser();
			if (user != null) {
				getDomainService().transformEntitiesCreatedWithEmail(user);
				faqsController.reset();
			}
			if (CHANGE_PASSWORD_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectChangePassword(params);
			}
			if (TICKET_VIEW_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_TAKE_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_POSTPONE_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_ASSIGN_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_REFUSE_CLOSURE_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_APPROVE_CLOSURE_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_REFUSE_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_REOPEN_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_GIVE_INFORMATION_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_REQUEST_INFORMATION_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_FREE_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_CLOSE_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_TAKE_AND_CLOSE_PAGE_VALUE.equals(params.get(PAGE_PARAM))
					|| TICKET_TAKE_AND_REQUEST_INFORMATION_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectTicketAction(params);
			}
			if (ADD_TICKET_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectTicketAdd(params);
			}
			if (ABOUT_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectAbout(params);
			}
			if (WELCOME_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectWelcome(params);
			}
			if (PREFERENCES_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectPreferences(params);
			}
			if (MANAGER_PREFERENCES_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectManagerPreferences(params);
			}
			if (SEARCH_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectSearch(params);
			}
			if (CONTROL_PANEL_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectControlPanel(params);
			}
			if (VIEW_DEPARTMENT_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectDepartmentView(params);
			}
			if (DEPARTMENTS_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectDepartments(params);
			}
			if (ADMINISTRATORS_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectAdministrators(params);
			}
			if (JOURNAL_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectJournal(params);
			}
			if (FAQ_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectFaq(params);
			}
			if (STATISTICS_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectStatistics(params);
			}
			if (BOOKMARKS_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectBookmarks(params);
			}
			if (RESPONSES_PAGE_VALUE.equals(params.get(PAGE_PARAM))) {
				return redirectResponses(params);
			}
			if (StringUtils.hasText(params.get(PAGE_PARAM))) {
				addErrorMessageInvalidParameter(PAGE_PARAM, params.get(PAGE_PARAM));
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("params is null");
			}
		}
		if (user != null) {
			if (User.START_PAGE_CONTROL_PANEL.equals(user.getStartPage())) {
				return redirectControlPanel(params);
			}
			if (User.START_PAGE_ABOUT.equals(user.getStartPage())) {
				return redirectAbout(params);
			}
			if (User.START_PAGE_PREFERENCES.equals(user.getStartPage())) {
				return redirectPreferences(params);
			}
			if (User.START_PAGE_SEARCH.equals(user.getStartPage())) {
				return redirectSearch(params);
			}
			if (User.START_PAGE_JOURNAL.equals(user.getStartPage())) {
				return redirectJournal(params);
			}
			if (User.START_PAGE_FAQ.equals(user.getStartPage())) {
				return redirectFaq(params);
			}
			if (User.START_PAGE_STATISTICS.equals(user.getStartPage())) {
				return redirectStatistics(params);
			}
			if (User.START_PAGE_BOOKMARKS.equals(user.getStartPage())) {
				return redirectBookmarks(params);
			}
			if (User.START_PAGE_BOOKMARKS.equals(user.getStartPage())) {
				return redirectBookmarks(params);
			}
		}
		return redirectWelcome(params);
	}

	/**
	 * @param sessionController
	 *            the sessionController to set
	 */
	public void setSessionController(final SessionController sessionController) {
		this.sessionController = sessionController;
	}

	/**
	 * @return the archivedTicketController
	 */
	protected ArchivedTicketController getArchivedTicketController() {
		return archivedTicketController;
	}

	/**
	 * @param archivedTicketController
	 *            the archivedTicketController to set
	 */
	public void setArchivedTicketController(final ArchivedTicketController archivedTicketController) {
		this.archivedTicketController = archivedTicketController;
	}

	/**
	 * @return the sessionController
	 */
	protected SessionController getSessionController() {
		return sessionController;
	}

	/**
	 * @return the domainService
	 */
	protected DomainService getDomainService() {
		return domainService;
	}

	/**
	 * @param domainService
	 *            the domainService to set
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @return the ticketController
	 */
	protected TicketController getTicketController() {
		return ticketController;
	}

	/**
	 * @param ticketController
	 *            the ticketController to set
	 */
	public void setTicketController(final TicketController ticketController) {
		this.ticketController = ticketController;
	}

	/**
	 * @return the preferencesController
	 */
	protected PreferencesController getPreferencesController() {
		return preferencesController;
	}

	/**
	 * @param preferencesController
	 *            the preferencesController to set
	 */
	public void setPreferencesController(final PreferencesController preferencesController) {
		this.preferencesController = preferencesController;
	}

	/**
	 * @return the searchController
	 */
	protected SearchController getSearchController() {
		return searchController;
	}

	/**
	 * @param searchController
	 *            the searchController to set
	 */
	public void setSearchController(final SearchController searchController) {
		this.searchController = searchController;
	}

	/**
	 * @return the controlPanelController
	 */
	protected ControlPanelController getControlPanelController() {
		return controlPanelController;
	}

	/**
	 * @param controlPanelController
	 *            the controlPanelController to set
	 */
	public void setControlPanelController(final ControlPanelController controlPanelController) {
		this.controlPanelController = controlPanelController;
	}

	/**
	 * @return the administratorsController
	 */
	protected AdministratorsController getAdministratorsController() {
		return administratorsController;
	}

	/**
	 * @param administratorsController
	 *            the administratorsController to set
	 */
	public void setAdministratorsController(final AdministratorsController administratorsController) {
		this.administratorsController = administratorsController;
	}

	/**
	 * @return the departmentsController
	 */
	protected DepartmentsController getDepartmentsController() {
		return departmentsController;
	}

	/**
	 * @param departmentsController
	 *            the departmentsController to set
	 */
	public void setDepartmentsController(final DepartmentsController departmentsController) {
		this.departmentsController = departmentsController;
	}

	/**
	 * @return the journalController
	 */
	protected JournalController getJournalController() {
		return journalController;
	}

	/**
	 * @param journalController
	 *            the journalController to set
	 */
	public void setJournalController(final JournalController journalController) {
		this.journalController = journalController;
	}

	/**
	 * @return the faqsController
	 */
	protected FaqsController getFaqsController() {
		return faqsController;
	}

	/**
	 * @param faqsController
	 *            the faqsController to set
	 */
	public void setFaqsController(final FaqsController faqsController) {
		this.faqsController = faqsController;
	}

	/**
	 * @return the statisticsController
	 */
	protected StatisticsController getStatisticsController() {
		return statisticsController;
	}

	/**
	 * @param statisticsController
	 *            the statisticsController to set
	 */
	public void setStatisticsController(final StatisticsController statisticsController) {
		this.statisticsController = statisticsController;
	}

	/**
	 * @return the bookmarksController
	 */
	protected BookmarksController getBookmarksController() {
		return bookmarksController;
	}

	/**
	 * @param bookmarksController
	 *            the bookmarksController to set
	 */
	public void setBookmarksController(final BookmarksController bookmarksController) {
		this.bookmarksController = bookmarksController;
	}

	/**
	 * @return the responsesController
	 */
	protected ResponsesController getResponsesController() {
		return responsesController;
	}

	/**
	 * @param responsesController
	 *            the responsesController to set
	 */
	public void setResponsesController(final ResponsesController responsesController) {
		this.responsesController = responsesController;
	}

}
