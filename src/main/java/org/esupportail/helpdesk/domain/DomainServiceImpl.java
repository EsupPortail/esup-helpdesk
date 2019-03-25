/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.axis.utils.StringUtils;
import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.aop.monitor.Monitor;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.application.Version;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.FileUtils;
import org.esupportail.helpdesk.batch.Batch;
import org.esupportail.helpdesk.dao.DaoService;
import org.esupportail.helpdesk.domain.assignment.AssignmentAlgorithm;
import org.esupportail.helpdesk.domain.assignment.AssignmentAlgorithmStore;
import org.esupportail.helpdesk.domain.assignment.AssignmentResult;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Alert;
import org.esupportail.helpdesk.domain.beans.ArchivedAction;
import org.esupportail.helpdesk.domain.beans.ArchivedFileInfo;
import org.esupportail.helpdesk.domain.beans.ArchivedInvitation;
import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Bookmark;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.CategoryMember;
import org.esupportail.helpdesk.domain.beans.Config;
import org.esupportail.helpdesk.domain.beans.DeletedItem;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentInvitation;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.DepartmentSelectionConfig;
import org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer;
import org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.FaqEvent;
import org.esupportail.helpdesk.domain.beans.FaqLink;
import org.esupportail.helpdesk.domain.beans.FileInfo;
import org.esupportail.helpdesk.domain.beans.HistoryItem;
import org.esupportail.helpdesk.domain.beans.Icon;
import org.esupportail.helpdesk.domain.beans.Invitation;
import org.esupportail.helpdesk.domain.beans.OldFaqEntry;
import org.esupportail.helpdesk.domain.beans.OldFaqPart;
import org.esupportail.helpdesk.domain.beans.OldFileInfo;
import org.esupportail.helpdesk.domain.beans.OldTicketTemplate;
import org.esupportail.helpdesk.domain.beans.Response;
import org.esupportail.helpdesk.domain.beans.State;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.TicketMonitoring;
import org.esupportail.helpdesk.domain.beans.TicketView;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.beans.VersionManager;
import org.esupportail.helpdesk.domain.categoryConfiguration.CategoryConfigurator;
import org.esupportail.helpdesk.domain.computerUrl.ComputerUrlBuilder;
import org.esupportail.helpdesk.domain.computerUrl.ComputerUrlBuilderStore;
import org.esupportail.helpdesk.domain.departmentConfiguration.DepartmentConfigurator;
import org.esupportail.helpdesk.domain.departmentManagerConfiguration.DepartmentManagerConfigurator;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelector;
import org.esupportail.helpdesk.domain.reporting.FaqReporter;
import org.esupportail.helpdesk.domain.reporting.InvitationSender;
import org.esupportail.helpdesk.domain.reporting.MonitoringSender;
import org.esupportail.helpdesk.domain.reporting.TicketPrinter;
import org.esupportail.helpdesk.domain.reporting.TicketReporter;
import org.esupportail.helpdesk.domain.userInfo.UserInfoProvider;
import org.esupportail.helpdesk.domain.userManagement.UserStore;
import org.esupportail.helpdesk.exceptions.ArchivedTicketNotFoundException;
import org.esupportail.helpdesk.exceptions.CategoryMemberNotFoundException;
import org.esupportail.helpdesk.exceptions.CategoryNotFoundException;
import org.esupportail.helpdesk.exceptions.DepartmentManagerNotFoundException;
import org.esupportail.helpdesk.exceptions.DepartmentNotFoundException;
import org.esupportail.helpdesk.exceptions.FaqNotFoundException;
import org.esupportail.helpdesk.exceptions.IconNotFoundException;
import org.esupportail.helpdesk.exceptions.TicketNotFoundException;
import org.esupportail.helpdesk.services.indexing.IndexIdProvider;
import org.springframework.beans.factory.InitializingBean;

/**
 * The basic implementation of DomainService.
 *
 * See /properties/domain/domain-example.xml
 */
@Monitor
public class DomainServiceImpl implements DomainService, InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -839691561955400211L;

	/**
	 * The default history max length.
	 */
	private static final int DEFAULT_HISTORY_MAX_LENGTH = 30;

	/**
	 * The default min priority.
	 */
	private static final int DEFAULT_MIN_PRIORITY = 1;

	/**
	 * The default max priority.
	 */
	private static final int DEFAULT_MAX_PRIORITY = 5;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * {@link DaoService}.
	 */
	private DaoService daoService;

	/**
	 * {@link UserStore}.
	 */
	private UserStore userStore;

	/**
	 * {@link I18nService}.
	 */
	private I18nService i18nService;

	/**
	 * {@link ApplicationService}.
	 */
	private ApplicationService applicationService;

	/**
	 * The index id provider.
	 */
	private IndexIdProvider indexIdProvider;

	/**
	 * The min priority.
	 */
	private int minPriority = DEFAULT_MIN_PRIORITY;

	/**
	 * The max priority.
	 */
	private int maxPriority = DEFAULT_MAX_PRIORITY;

	/**
	 * The priorities.
	 */
	private List<Integer> priorities;

	/**
	 * The origins.
	 */
	private List<String> origins;

	/**
	 * The web origin.
	 */
	private String webOrigin;

	/**
	 * The email origin.
	 */
	private String emailOrigin;

	/**
	 * A bean to configure departments at their creation.
	 */
	private DepartmentConfigurator departmentConfigurator;

	/**
	 * A bean to configure department managers at their creation.
	 */
	private DepartmentManagerConfigurator departmentManagerConfigurator;

	/**
	 * A bean to configure categories at their creation.
	 */
	private CategoryConfigurator categoryConfigurator;

	/**
	 * The default scope for tickets.
	 */
	private String departmentDefaultTicketScope = TicketScope.PUBLIC;

	/**
	 * The default value for comment modification authorization.
	 */
	private boolean ticketCommentModification = false;

	/**
	 * The default scope for FAQs.
	 */
	private String departmentDefaultFaqScope = FaqScope.ALL;

	/**
	 * The default priority level for tickets.
	 */
	private int departmentDefaultTicketPriorityLevel = DEFAULT_PRIORITY_VALUE;

	/**
	 * The user info provider.
	 */
	private UserInfoProvider userInfoProvider;

	/**
	 * The department selector.
	 */
	private DepartmentSelector departmentSelector;

	/**
	 * The assignment algorithm store.
	 */
	private AssignmentAlgorithmStore assignmentAlgorithmStore;

	/**
	 * The default assignment algorithm name.
	 */
	private String defaultAssignmentAlgorithmName;

	/**
	 * The computer url builder store.
	 */
	private ComputerUrlBuilderStore computerUrlBuilderStore;

	/**
	 * The default computer url builder name.
	 */
	private String defaultComputerUrlBuilderName;

	/**
	 * The default control panel refresh delay.
	 */
	private Integer defaultControlPanelRefreshDelay;

	/**
	 * The max number of history items for a user..
	 */
	private int historyMaxLength = DEFAULT_HISTORY_MAX_LENGTH;

	/**
	 * The FCK editor code cleaner.
	 */
	private FckEditorCodeCleaner fckEditorCodeCleaner;

	/**
	 * The ticket printer.
	 */
	private TicketPrinter ticketPrinter;

	/**
	 * The invitation sender.
	 */
	private InvitationSender invitationSender;

	/**
	 * The monitoring sender.
	 */
	private MonitoringSender monitoringSender;

	/**
	 * The ticket reporter.
	 */
	private TicketReporter ticketReporter;

	/**
	 * The FAQ reporter.
	 */
	private FaqReporter faqReporter;

	/**
	 * True if convert mail to cas enable.
	 */
	private boolean tryConvertMaillToCasUser;

	/**
	 * indicator for manager invited.
	 */
	private Boolean inviteManagerMoveTicket;
	
	/**
	 * indicator for manager invited.
	 */
	private Boolean checkVisiCateVirtual;
	
	/**
	 * Bean constructor.
	 */
	public DomainServiceImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.daoService,
				"property daoService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.userStore, "property userStore of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.i18nService,
				"property i18nService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.applicationService,
				"property applicationService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(indexIdProvider,
				"property indexIdProvider of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(departmentConfigurator,
				"property departmentConfigurator of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(departmentManagerConfigurator,
				"property departmentManagerConfigurator of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(categoryConfigurator,
				"property categoryConfigurator of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(departmentSelector,
				"property departmentSelector of class " + this.getClass().getName() + " can not be null");
		Assert.contains(
				new String[] { TicketScope.PUBLIC, TicketScope.PRIVATE, TicketScope.SUBJECT_ONLY, TicketScope.CAS, },
				"departmentDefaultTicketScope", departmentDefaultTicketScope);
		Assert.isTrue(maxPriority > minPriority, "maxPriority <= minPriority");
		Assert.isTrue(minPriority > 0, "minPriority <= 0");
		priorities = new ArrayList<Integer>();
		for (int i = minPriority; i <= maxPriority; i++) {
			priorities.add(new Integer(i));
		}
		Assert.contains(priorities, "departmentDefaultTicketPriorityLevel",
				new Integer(departmentDefaultTicketPriorityLevel));
		Assert.notEmpty(origins, "property origins of class " + this.getClass().getName() + " can not be empty");
		Assert.contains(origins, "webOrigin", webOrigin);
		Assert.contains(origins, "emailOrigin", emailOrigin);
		Assert.contains(new String[] { FaqScope.ALL, FaqScope.AUTHENTICATED, FaqScope.DEPARTMENT, FaqScope.MANAGER, },
				"departmentDefaultFaqScope", departmentDefaultFaqScope);
		Assert.notNull(this.assignmentAlgorithmStore,
				"property assignmentAlgorithmStore of class " + this.getClass().getName() + " can not be null");
		Assert.notEmpty(this.assignmentAlgorithmStore.getAlgorithmNames(),
				"property assignmentAlgorithmStore of class " + this.getClass().getName() + " can not be empty");
		Assert.contains(assignmentAlgorithmStore.getAlgorithmNames(), "defaultAssignmentAlgorithName",
				defaultAssignmentAlgorithmName);
		Assert.notNull(this.computerUrlBuilderStore,
				"property computerUrlBuilderStore of class " + this.getClass().getName() + " can not be null");
		Assert.notEmpty(this.computerUrlBuilderStore.getComputerUrlBuilderNames(),
				"property computerUrlBuilderStore of class " + this.getClass().getName() + " can not be empty");
		Assert.contains(computerUrlBuilderStore.getComputerUrlBuilderNames(), "defaultComputerUrlBuilderName",
				defaultComputerUrlBuilderName);
		Assert.notNull(fckEditorCodeCleaner,
				"property fckEditorCodeCleaner of class " + this.getClass().getName() + " must be set");
		if (userInfoProvider != null) {
			userInfoProvider.setDomainService(this);
		}
		Assert.notNull(this.ticketPrinter,
				"property ticketPrinter of class " + this.getClass().getName() + " can not be null");
		ticketPrinter.setDomainService(this);
		Assert.notNull(this.invitationSender,
				"property invitationSender of class " + this.getClass().getName() + " can not be null");
		invitationSender.setDomainService(this);
		Assert.notNull(this.monitoringSender,
				"property monitoringSender of class " + this.getClass().getName() + " can not be null");
		monitoringSender.setDomainService(this);
		Assert.notNull(this.ticketReporter,
				"property ticketReporter of class " + this.getClass().getName() + " can not be null");
		ticketReporter.setDomainService(this);
		Assert.notNull(this.faqReporter,
				"property faqReporter of class " + this.getClass().getName() + " can not be null");
		faqReporter.setDomainService(this);
		Assert.notNull(this.tryConvertMaillToCasUser,
				"property tryConvertMaillToCasUser of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.inviteManagerMoveTicket,
				"property inviteManagerMoveTicket of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.checkVisiCateVirtual,
				"property checkVisiCateVirtual of class " + this.getClass().getName() + " can not be null");
		
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________PROPERTIES() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentDefaultTicketScope()
	 */
	@Override
	public String getDepartmentDefaultTicketScope() {
		return departmentDefaultTicketScope;
	}

	/**
	 * @param departmentDefaultTicketScope
	 *            the departmentDefaultTicketScope to set
	 */
	public void setDepartmentDefaultTicketScope(final String departmentDefaultTicketScope) {
		this.departmentDefaultTicketScope = departmentDefaultTicketScope;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentDefaultTicketPriorityLevel()
	 */
	@Override
	public int getDepartmentDefaultTicketPriorityLevel() {
		return departmentDefaultTicketPriorityLevel;
	}

	/**
	 * Set the departmentDefaultTicketPriorityLevel.
	 * 
	 * @param departmentDefaultTicketPriorityLevel
	 */
	public void setDepartmentDefaultTicketPriorityLevel(final int departmentDefaultTicketPriorityLevel) {
		this.departmentDefaultTicketPriorityLevel = departmentDefaultTicketPriorityLevel;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentDefaultFaqScope()
	 */
	@Override
	public String getDepartmentDefaultFaqScope() {
		return departmentDefaultFaqScope;
	}

	/**
	 * @param departmentDefaultFaqScope
	 *            the departmentDefaultFaqScope to set
	 */
	public void setDepartmentDefaultFaqScope(final String departmentDefaultFaqScope) {
		this.departmentDefaultFaqScope = departmentDefaultFaqScope;
	}

	//////////////////////////////////////////////////////////////
	// Priorities
	//////////////////////////////////////////////////////////////

	/**
	 * @return the minPriority
	 */
	protected int getMinPriority() {
		return minPriority;
	}

	/**
	 * @param minPriority
	 *            the minPriority to set
	 */
	public void setMinPriority(final int minPriority) {
		this.minPriority = minPriority;
	}

	/**
	 * @return the maxPriority
	 */
	protected int getMaxPriority() {
		return maxPriority;
	}

	/**
	 * @param maxPriority
	 *            the maxPriority to set
	 */
	public void setMaxPriority(final int maxPriority) {
		this.maxPriority = maxPriority;
	}

	/**
	 * @param priorities
	 *            the priorities to set
	 */
	protected void setPriorities(final List<Integer> priorities) {
		this.priorities = priorities;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getPriorities()
	 */
	@Override
	public List<Integer> getPriorities() {
		return priorities;
	}

	//////////////////////////////////////////////////////////////
	// Origin
	//////////////////////////////////////////////////////////////

	/**
	 * Set the origins.
	 * 
	 * @param originKeys
	 */
	public void setOriginKeys(final String originKeys) {
		this.origins = new ArrayList<String>();
		for (String origin : originKeys.split(",")) {
			this.origins.add(origin);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getOrigins()
	 */
	@Override
	public List<String> getOrigins() {
		return origins;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getWebOrigin()
	 */
	@Override
	public String getWebOrigin() {
		return webOrigin;
	}

	/**
	 * @param webOrigin
	 *            the webOrigin to set
	 */
	public void setWebOrigin(final String webOrigin) {
		this.webOrigin = webOrigin;
	}

	/**
	 * @return the origin id for ticket fed by email.
	 */
	protected String getEmailOrigin() {
		return emailOrigin;
	}

	/**
	 * @param emailOrigin
	 *            the emailOrigin to set
	 */
	public void setEmailOrigin(final String emailOrigin) {
		this.emailOrigin = emailOrigin;
	}

	//////////////////////////////////////////////////////////////
	// Misc
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getAssignmentAlgorithmNames()
	 */
	@Override
	public List<String> getAssignmentAlgorithmNames() {
		return assignmentAlgorithmStore.getAlgorithmNames();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getAssignmentAlgorithmDescription(
	 *      java.lang.String, java.util.Locale)
	 */
	@Override
	public String getAssignmentAlgorithmDescription(final String name, final Locale locale) {
		AssignmentAlgorithm algorithm = assignmentAlgorithmStore.getAlgorithm(name);
		if (algorithm == null) {
			return null;
		}
		return algorithm.getDescription(locale);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDefaultAssignmentAlgorithmName()
	 */
	@Override
	public String getDefaultAssignmentAlgorithmName() {
		return defaultAssignmentAlgorithmName;
	}

	/**
	 * @return the names of the computer url builders.
	 */
	@Override
	public List<String> getComputerUrlBuilderNames() {
		return computerUrlBuilderStore.getComputerUrlBuilderNames();
	}

	/**
	 * @param name
	 * @param locale
	 * @return the description of a computer url builder.
	 */
	@Override
	public String getComputerUrlBuilderDescription(final String name, final Locale locale) {
		ComputerUrlBuilder computerUrlBuilder = computerUrlBuilderStore.getComputerUrlBuilder(name);
		if (computerUrlBuilder == null) {
			return null;
		}
		return computerUrlBuilder.getDescription(locale);
	}

	/**
	 * @return the default computer url builder name.
	 */
	@Override
	public String getDefaultComputerUrlBuilderName() {
		return defaultComputerUrlBuilderName;
	}

	/**
	 * @return the useLdap
	 */
	@Override
	public boolean isUseLdap() {
		return getUserStore().isCasAuthAllowed();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________USER() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getUserStore()
	 */
	@Override
	public UserStore getUserStore() {
		return userStore;
	}

	/**
	 * Update the department selection context time for a user.
	 * 
	 * @param user
	 */
	protected void updateUserDepartmentSelectionContextTime(final User user) {
		user.updateDepartmentSelectionContextTime();
		updateUser(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getUsers()
	 */
	@Override
	public List<User> getUsers() {
		return this.daoService.getUsers();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getUsersNumber()
	 */
	@Override
	public int getUsersNumber() {
		return this.daoService.getUsersNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getCasUsersNumber()
	 */
	@Override
	public int getCasUsersNumber() {
		return this.daoService.getCasUsersNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getShibbolethUsersNumber()
	 */
	@Override
	public int getShibbolethUsersNumber() {
		return this.daoService.getShibbolethUsersNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getApplicationUsersNumber()
	 */
	@Override
	public int getApplicationUsersNumber() {
		return this.daoService.getApplicationUsersNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateUser(org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void updateUser(final User user) {
		if (user.getControlPanelManagerDepartmentFilter() == null && user.getControlPanelUserDepartmentFilter() == null) {
			user.setControlPanelCategoryFilter(null);
		}
		user.setStoredControlPanelOrder(user.getControlPanelOrder().toString());
		this.daoService.updateUser(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getAdmins()
	 */
	@Override
	@RequestCache
	public List<User> getAdmins() {
		return daoService.getAdmins();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addAdmin(org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void addAdmin(final User user) {
		user.setAdmin(true);
		user.updateDepartmentSelectionContextTime();
		updateUser(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteAdmin(org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void deleteAdmin(final User user) {
		user.setAdmin(false);
		user.updateDepartmentSelectionContextTime();
		updateUser(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#testUserInfo()
	 */
	@Override
	public void testUserInfo() {
		if (userInfoProvider != null) {
			userInfoProvider.test();
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getUserInfo(
	 *      org.esupportail.helpdesk.domain.beans.User, java.util.Locale)
	 */
	@Override
	@RequestCache
	public String getUserInfo(final User user, final Locale locale) {
		if (userInfoProvider == null) {
			return null;
		}
		return userInfoProvider.getInfo(user, locale);
	}

	/**
	 * @param email
	 * @return true if the given email is valid.
	 */
	public boolean isEmail(final String email) {
		return email != null && email.contains("@");
	}

	public boolean isFormatEmailValid (String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
	};
	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#transformEntitiesCreatedWithEmail(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void transformEntitiesCreatedWithEmail(final User user) {
		if (getUserStore().isApplicationUser(user)) {
			return;
		}
		List<String> emails = userStore.getUserEmails(user);
		if (emails == null) {
			return;
		}
		for (String email : emails) {
			if (!isEmail(email)) {
				logger.error("invalid email [" + email + "]");
				continue;
			}
			try {
				User applicationUser = userStore.getExistingApplicationUser(email);
				List<Ticket> tickets = getOwnedTickets(applicationUser);
				int i = 0;
				for (Ticket ticket : tickets) {
					changeTicketOwner(null, ticket, user, null, ActionScope.DEFAULT, false);
					i++;
				}
				if (i != 0) {
					logger.info(
							"changed the owner of " + i + " ticket(s) from [application/" + applicationUser.getRealId()
									+ "] to [" + user.getAuthType() + "/" + user.getRealId() + "]");
				}
				i = 0;
				for (Invitation invitation : daoService.getInvitations(applicationUser)) {
					removeInvitation(null, invitation, false);
					Ticket ticket = invitation.getTicket();
					if (!isInvited(user, ticket)) {
						invite(null, ticket, user, null, ActionScope.DEFAULT, false);
					}
					i++;
				}
				if (i != 0) {
					logger.info("changed " + i + " invitations(s) from [" + applicationUser.getId() + "] to ["
							+ user.getId() + "]");
				}
				// TODO totally remove the application user!
			} catch (UserNotFoundException e) {
				continue;
			}
		}
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________DEPARTMENT() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartment(long)
	 */
	@Override
	@RequestCache
	public Department getDepartment(final long id) throws DepartmentNotFoundException {
		Department department = this.daoService.getDepartment(id);
		if (department == null) {
			throw new DepartmentNotFoundException("no department found with id [" + id + "]");
		}
		return department;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartments()
	 */
	@Override
	public List<Department> getDepartments() {
		return this.daoService.getDepartments();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentsNumber()
	 */
	@Override
	public int getDepartmentsNumber() {
		return daoService.getDepartmentsNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getRealDepartmentsNumber()
	 */
	@Override
	public int getRealDepartmentsNumber() {
		return daoService.getRealDepartmentsNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getVirtualDepartmentsNumber()
	 */
	@Override
	public int getVirtualDepartmentsNumber() {
		return daoService.getVirtualDepartmentsNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addDepartment(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public void addDepartment(final Department department) {
		department.computeEffectiveDefaultTicketScope(getDepartmentDefaultTicketScope());
		department.computeEffectiveDefaultFaqScope(getDepartmentDefaultFaqScope());
		departmentConfigurator.configure(department);
		department.setOrder(daoService.getDepartmentsNumber());
		this.daoService.addDepartment(department);
		updateDepartmentSelectionContextTime();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateDepartment(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public void updateDepartment(final Department department) {
		boolean effectiveDefaultTicketScopeChanged = department
				.computeEffectiveDefaultTicketScope(getDepartmentDefaultTicketScope());
		boolean effectiveDefaultFaqScopeChanged = department
				.computeEffectiveDefaultFaqScope(getDepartmentDefaultFaqScope());
		this.daoService.updateDepartment(department);
		if (effectiveDefaultTicketScopeChanged) {
			for (Category category : getRootCategories(department)) {
				if (category.computeEffectiveDefaultTicketScope(false)) {
					updateCategory(category);
				}
			}
		}
		if (effectiveDefaultFaqScopeChanged) {
			for (DeprecatedFaqContainer faqContainer : getRootFaqContainers(department)) {
				if (faqContainer.computeEffectiveScope(false)) {
					updateFaqContainer(faqContainer);
				}
			}
			for (Faq faq : getRootFaqs(department)) {
				if (faq.computeEffectiveScope(false)) {
					updateFaq(faq);
				}
			}
		}
		updateDepartmentSelectionContextTime();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteDepartment(
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public void deleteDepartment(final Department department, final Department archivedTicketsNewDepartment) {
		if (archivedTicketsNewDepartment == null) {
			for (ArchivedTicket archivedTicket : daoService.getArchivedTickets(department)) {
				daoService.addDeletedItem(new DeletedItem(indexIdProvider.getIndexId(archivedTicket)));
				daoService.deleteArchivedTicket(archivedTicket);
			}
		} else {
			for (ArchivedTicket archivedTicket : daoService.getArchivedTickets(department)) {
				ArchivedAction archivedAction = ArchivedAction.changeDepartmentArchivedAction(archivedTicket,
						archivedTicketsNewDepartment);
				daoService.addArchivedAction(archivedAction);
				archivedTicket.setDepartment(archivedTicketsNewDepartment);
				daoService.updateArchivedTicket(archivedTicket);
			}
		}
		daoService.deleteDepartment(department);
		reorderDepartments(getDepartments());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#isDepartmentLabelUsed(java.lang.String)
	 */
	@Override
	public boolean isDepartmentLabelUsed(final String label) {
		return this.daoService.isDepartmentLabelUsed(label);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveDepartmentUp(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public void moveDepartmentUp(final Department department) {
		Department previousDepartment = daoService.getDepartmentByOrder(department.getOrder() - 1);
		if (previousDepartment != null) {
			department.setOrder(department.getOrder() - 1);
			updateDepartment(department);
			previousDepartment.setOrder(previousDepartment.getOrder() + 1);
			updateDepartment(previousDepartment);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveDepartmentDown(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public void moveDepartmentDown(final Department department) {
		Department nextDepartment = daoService.getDepartmentByOrder(department.getOrder() + 1);
		if (nextDepartment != null) {
			department.setOrder(department.getOrder() + 1);
			updateDepartment(department);
			nextDepartment.setOrder(nextDepartment.getOrder() - 1);
			updateDepartment(nextDepartment);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveDepartmentFirst(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public void moveDepartmentFirst(final Department departmentToMove) {
		for (Department department : getDepartments()) {
			if (department.getOrder() < departmentToMove.getOrder()) {
				department.setOrder(department.getOrder() + 1);
				updateDepartment(department);
			}
		}
		departmentToMove.setOrder(0);
		updateDepartment(departmentToMove);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveDepartmentLast(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public void moveDepartmentLast(final Department departmentToMove) {
		List<Department> departments = getDepartments();
		for (Department department : departments) {
			if (department.getOrder() > departmentToMove.getOrder()) {
				department.setOrder(department.getOrder() - 1);
				updateDepartment(department);
			}
		}
		departmentToMove.setOrder(departments.size() - 1);
		updateDepartment(departmentToMove);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#reorderDepartments(java.util.List)
	 */
	@Override
	public void reorderDepartments(final List<Department> departments) {
		int i = 0;
		for (Department department : departments) {
			if (department.getOrder() != i) {
				department.setOrder(i);
				daoService.updateDepartment(department);
			}
			i++;
		}
		updateDepartmentSelectionContextTime();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getVirtualDepartments(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public List<Department> getVirtualDepartments(final Department department) {
		return daoService.getVirtualDepartments(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getVirtualDepartments(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public List<Category> getVirtualCategories(final Department department) {

		List<Category> virtualCategories = new ArrayList<Category>();
		List<Category> categories = getCategories(department);

		for (Category category : categories) {
			virtualCategories.addAll(getVirtualCategories(category));
		}
		return virtualCategories;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasVirtualDepartments(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public boolean hasVirtualDepartments(final Department department) {
		return daoService.getVirtualDepartmentsNumber(department) > 0;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketCreationDepartments(
	 *      org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress)
	 */
	@Override
	public List<Department> getTicketCreationDepartments(final User user, final InetAddress client) {
		return departmentSelector.getTicketCreationDepartments(this, user, client);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketViewDepartments(
	 *      org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress)
	 */
	@Override
	public List<Department> getTicketViewDepartments(final User user, final InetAddress client) {
		return departmentSelector.getTicketViewDepartments(this, user, client);
	}

	@Override
	public List<Ticket> getTicketsByOwner(User user) {
		return daoService.getTicketsByOwner(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFaqViewDepartments(
	 *      org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress)
	 */
	@Override
	public List<Department> getFaqViewDepartments(final User user, final InetAddress client) {
		return departmentSelector.getFaqViewDepartments(this, user, client);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getEnabledDepartments()
	 */
	@Override
	public List<Department> getEnabledDepartments() {
		return daoService.getEnabledDepartments();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#isDepartmentVisibleForTicketCreation(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      java.net.InetAddress)
	 */
	@Override
	@RequestCache
	public boolean isDepartmentVisibleForTicketCreation(final User user, final Department department,
			final InetAddress client) {
		return getTicketCreationDepartments(user, client).contains(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#isDepartmentVisibleForTicketView(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      java.net.InetAddress)
	 */
	@Override
	@RequestCache
	public boolean isDepartmentVisibleForTicketView(final User user, final Department department,
			final InetAddress client) {
		return getTicketViewDepartments(user, client).contains(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#isDepartmentVisibleForFaqView(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      java.net.InetAddress)
	 */
	@Override
	@RequestCache
	public boolean isDepartmentVisibleForFaqView(final User user, final Department department,
			final InetAddress client) {
		return getFaqViewDepartments(user, client).contains(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getManagedOrTicketViewVisibleDepartments(
	 *      org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress)
	 */
	@Override
	@RequestCache
	public List<Department> getManagedOrTicketViewVisibleDepartments(final User user, final InetAddress client) {
		List<Department> result = getManagedDepartments(user);
		for (Department department : getTicketViewDepartments(user, client)) {
			if (!result.contains(department)) {
				result.add(department);
			}
		}
		return result;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getSearchVisibleDepartments(
	 *      org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress)
	 */
	@Override
	@RequestCache
	public List<Department> getSearchVisibleDepartments(final User user, final InetAddress client) {
		List<Department> result = getManagedDepartments(user);
		for (Department department : getTicketViewDepartments(user, client)) {
			if (!result.contains(department)) {
				result.add(department);
			}
		}
		for (Department department : getFaqViewDepartments(user, client)) {
			if (!result.contains(department)) {
				result.add(department);
			}
		}
		return result;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#isDepartmentVisibleForSearch(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      java.net.InetAddress)
	 */
	@Override
	@RequestCache
	public boolean isDepartmentVisibleForSearch(final User user, final Department department,
			final InetAddress client) {
		return getSearchVisibleDepartments(user, client).contains(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentsByFilter(java.lang.String)
	 */
	@Override
	public List<Department> getDepartmentsByFilter(final String filter) {
		return daoService.getDepartmentsByFilter(filter);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentByLabel(java.lang.String)
	 */
	@Override
	public Department getDepartmentByLabel(final String label) {
		return daoService.getDepartmentByLabel(label);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentEffectiveAssignmentAlgorithmName(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public String getDepartmentEffectiveAssignmentAlgorithmName(final Department department) {
		if (department.getAssignmentAlgorithmName() != null) {
			return department.getAssignmentAlgorithmName();
		}
		return getDefaultAssignmentAlgorithmName();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________DEPARTMENT_MANAGER() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentManager(
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public DepartmentManager getDepartmentManager(final Department department, final User user)
			throws DepartmentManagerNotFoundException {
		DepartmentManager manager = daoService.getDepartmentManager(department, user);
		if (manager == null) {
			throw new DepartmentManagerNotFoundException(
					"user [" + user.getRealId() + "] is not a manager of department [" + department.getLabel() + "]");
		}
		return manager;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#isDepartmentManager(
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean isDepartmentManager(final Department department, final User user) {
		if (user == null) {
			return false;
		}
		try {
			getDepartmentManager(department, user);
			return true;
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentManagers(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public List<DepartmentManager> getDepartmentManagers(final Department department) {
		return this.daoService.getDepartmentManagers(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentManagers(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public List<DepartmentManager> getDepartmentManagers(final User user) {
		return this.daoService.getDepartmentManagers(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getManagerUsersNumber()
	 */
	@Override
	@RequestCache
	public int getManagerUsersNumber() {
		List<User> users = new ArrayList<User>();
		for (DepartmentManager manager : daoService.getDepartmentManagers()) {
			if (!users.contains(manager.getUser())) {
				users.add(manager.getUser());
			}
		}
		return users.size();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getAvailableDepartmentManagers(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public List<DepartmentManager> getAvailableDepartmentManagers(final Department department) {
		return this.daoService.getAvailableDepartmentManagers(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addDepartmentManager(
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public DepartmentManager addDepartmentManager(final Department department, final User user) {
		DepartmentManager departmentManager = new DepartmentManager();
		departmentManager.setDepartment(department);
		departmentManager.setUser(user);
		departmentManager.setOrder(daoService.getDepartmentManagersNumber(department));
		departmentManagerConfigurator.configure(departmentManager);
		departmentManager.checkTicketMonitoringValues();
		this.daoService.addDepartmentManager(departmentManager);
		updateUserDepartmentSelectionContextTime(user);
		return departmentManager;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteDepartmentManager(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.DepartmentManager, boolean,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void deleteDepartmentManager(final User author, final DepartmentManager departmentManager,
			final boolean useAssignmentAlgorithm, final User newManager) {
		User oldManager = departmentManager.getUser();
		for (Ticket ticket : getOpenManagedTickets(departmentManager.getDepartment(), oldManager)) {
			if (useAssignmentAlgorithm) {
				callAssignmentAlgorithm(ticket, oldManager, true, true);
			} else if (newManager != null) {
				assignTicket(author, ticket, newManager, null, ActionScope.DEFAULT);
			} else {
				freeTicket(author, ticket, null, ActionScope.DEFAULT);
			}
		}
		for (CategoryMember categoryMember : daoService.getCategoryMembers(departmentManager)) {
			deleteCategoryMember(categoryMember);
		}
		this.daoService.deleteDepartmentManager(departmentManager);
		updateUserDepartmentSelectionContextTime(oldManager);
		if (!oldManager.getControlPanelUserInterface() && !isDepartmentManager(oldManager)) {
			oldManager.setControlPanelUserInterface(false);
			updateUser(oldManager);
		}

		List<DepartmentManager> departmentManagers = getDepartmentManagers(departmentManager.getDepartment());
		departmentManagers.remove(departmentManager);
		reorderDepartmentManagers(departmentManagers);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateDepartmentManager(
	 *      org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public void updateDepartmentManager(final DepartmentManager departmentManager) {
		departmentManager.checkTicketMonitoringValues();
		this.daoService.updateDepartmentManager(departmentManager);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveDepartmentManagerUp(
	 *      org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public void moveDepartmentManagerUp(final DepartmentManager departmentManager) {
		DepartmentManager previousDepartmentManager = daoService
				.getDepartmentManagerByOrder(departmentManager.getDepartment(), departmentManager.getOrder() - 1);
		if (previousDepartmentManager != null) {
			departmentManager.setOrder(departmentManager.getOrder() - 1);
			updateDepartmentManager(departmentManager);
			previousDepartmentManager.setOrder(previousDepartmentManager.getOrder() + 1);
			updateDepartmentManager(previousDepartmentManager);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveDepartmentManagerDown(
	 *      org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public void moveDepartmentManagerDown(final DepartmentManager departmentManager) {
		DepartmentManager nextDepartmentManager = daoService
				.getDepartmentManagerByOrder(departmentManager.getDepartment(), departmentManager.getOrder() + 1);
		if (nextDepartmentManager != null) {
			departmentManager.setOrder(departmentManager.getOrder() + 1);
			updateDepartmentManager(departmentManager);
			nextDepartmentManager.setOrder(nextDepartmentManager.getOrder() - 1);
			updateDepartmentManager(nextDepartmentManager);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveDepartmentManagerFirst(
	 *      org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public void moveDepartmentManagerFirst(final DepartmentManager departmentManagerToMove) {
		for (DepartmentManager departmentManager : getDepartmentManagers(departmentManagerToMove.getDepartment())) {
			if (departmentManager.getOrder() < departmentManagerToMove.getOrder()) {
				departmentManager.setOrder(departmentManager.getOrder() + 1);
				updateDepartmentManager(departmentManager);
			}
		}
		departmentManagerToMove.setOrder(0);
		updateDepartmentManager(departmentManagerToMove);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveDepartmentManagerLast(
	 *      org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public void moveDepartmentManagerLast(final DepartmentManager departmentManagerToMove) {
		List<DepartmentManager> departmentManagers = getDepartmentManagers(departmentManagerToMove.getDepartment());
		for (DepartmentManager departmentManager : departmentManagers) {
			if (departmentManager.getOrder() > departmentManagerToMove.getOrder()) {
				departmentManager.setOrder(departmentManager.getOrder() - 1);
				updateDepartmentManager(departmentManager);
			}
		}
		departmentManagerToMove.setOrder(departmentManagers.size() - 1);
		updateDepartmentManager(departmentManagerToMove);
	}

	/**
	 * @param user
	 * @return the departments the user is manager of.
	 */
	protected List<Department> getManagedDepartmentsInternal(final User user) {
		List<DepartmentManager> managers = daoService.getDepartmentManagers(user);
		List<Department> departments = new ArrayList<Department>();
		for (DepartmentManager manager : managers) {
			departments.add(manager.getDepartment());
		}
		return departments;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getManagedDepartments(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public List<Department> getManagedDepartments(final User user) {
		if (user == null) {
			return new ArrayList<Department>();
		}
		return getManagedDepartmentsInternal(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getManagedDepartmentsOrAllIfAdmin(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public List<Department> getManagedDepartmentsOrAllIfAdmin(final User user, final String departmentFilter) {
		if (user == null) {
			return new ArrayList<Department>();
		}
		if (user.getAdmin()) {
			if(departmentFilter != null) {
				return filterDepartment(getDepartments(), departmentFilter);
			}
			return getDepartments();
		}
		if(departmentFilter != null) {
			return filterDepartment(getManagedDepartmentsInternal(user), departmentFilter);
		}
		return getManagedDepartmentsInternal(user);
	}

	
	private List<Department> filterDepartment(List<Department> departments, final String departmentFilter) {

		List <Department> departmentsFiltered = new ArrayList<Department>();
		for (Department department : departments) {
    		if(department.getXlabel().toLowerCase().contains(departmentFilter.toLowerCase()) ||
    		   department.getLabel().toLowerCase().contains(departmentFilter.toLowerCase())){
    			departmentsFiltered.add(department);
    		} 
		}
		return departmentsFiltered;
	}
	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#isDepartmentManager(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean isDepartmentManager(final User user) {
		List<DepartmentManager> managers = daoService.getDepartmentManagers(user);
		return !managers.isEmpty();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#reorderDepartmentManagers(java.util.List)
	 */
	@Override
	public void reorderDepartmentManagers(final List<DepartmentManager> managers) {
		int i = 0;
		for (DepartmentManager departmentManager : managers) {
			if (departmentManager.getOrder() != i) {
				departmentManager.setOrder(i);
				daoService.updateDepartmentManager(departmentManager);
			}
			i++;
		}
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________CATEGORY() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getCategory(long)
	 */
	@Override
	@RequestCache
	public Category getCategory(final long id) throws CategoryNotFoundException {
		Category category = this.daoService.getCategory(id);
		if (category == null) {
			throw new CategoryNotFoundException("no category found with id [" + id + "]");
		}
		return category;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getCategories(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public List<Category> getCategories(final Department department) {
		return this.daoService.getCategories(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getCategories()
	 */
	@Override
	public List<Category> getCategories() {
		return this.daoService.getCategories();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getRootCategoriesMap()
	 */
	@Override
	@RequestCache
	public Map<Department, List<Category>> getRootCategoriesMap() {
		Map<Department, List<Category>> result = new HashMap<Department, List<Category>>();
		for (Category category : daoService.getCategories()) {
			if (category.getParent() == null) {
				Department department = category.getDepartment();
				List<Category> rootCategories = result.get(department);
				if (rootCategories == null) {
					rootCategories = new ArrayList<Category>();
					result.put(department, rootCategories);
				}
				rootCategories.add(category);
			}
		}
		return result;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getSubCategoriesMap()
	 */
	@Override
	@RequestCache
	public Map<Category, List<Category>> getSubCategoriesMap() {
		Map<Category, List<Category>> result = new HashMap<Category, List<Category>>();
		for (Category category : daoService.getCategories()) {
			Category parent = category.getParent();
			if (parent != null) {
				List<Category> subCategories = result.get(parent);
				if (subCategories == null) {
					subCategories = new ArrayList<Category>();
					result.put(parent, subCategories);
				}
				subCategories.add(category);
			}
		}
		return result;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addCategory(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void addCategory(final Category category) {
		category.computeEffectiveDefaultTicketScope(true);
		categoryConfigurator.configure(category);
		if (category.getParent() == null) {
			category.setOrder(daoService.getRootCategoriesNumber(category.getDepartment()));
		} else {
			category.setOrder(daoService.getSubCategoriesNumber(category.getParent()));
		}
		this.daoService.addCategory(category);
	}

	/**
	 * Update the scope of the children of a category if needed.
	 * 
	 * @param category
	 */
	protected void updateCategoryChildrenScope(final Category category) {
		daoService.updateTicketsEffectiveScope(category);
		for (Category subCategory : getSubCategories(category)) {
			if (subCategory.computeEffectiveDefaultTicketScope(true)) {
				daoService.updateCategory(subCategory);
				updateCategoryChildrenScope(subCategory);
			}
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateCategory(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void updateCategory(final Category category) {
		boolean effectiveScopeChanged = category.computeEffectiveDefaultTicketScope(true);
		daoService.updateCategory(category);
		if (effectiveScopeChanged) {
			updateCategoryChildrenScope(category);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteCategory(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void deleteCategory(final Category category) {
		this.daoService.deleteCategory(category);
		if (category.getParent() == null) {
			reorderCategories(daoService.getRootCategories(category.getDepartment()));
		} else {
			reorderCategories(daoService.getSubCategories(category.getParent()));
		}
	}

	@Override
	public boolean isMembersOfThirstParentCategory(Category category, User user) {

		// si la categorie hérite d'une categorie parente, on remonte sur la
		// categorie parente
		if (category.getInheritMembers() == true && category.getParent() != null) {
			return isMembersOfThirstParentCategory(category.getParent(), user);

			// cas ou l'on hérite du service
		} else if (category.getInheritMembers() == true && category.getParent() == null) {
			if (getDepartmentManager(category.getDepartment(), user) != null) {
				return true;
			}
		} else {
			return isCategoryMember(category, user);
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getRootCategories(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public List<Category> getRootCategories(final Department department) {
		return daoService.getRootCategories(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasRootCategories(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public boolean hasRootCategories(final Department department) {
		return daoService.getRootCategoriesNumber(department) > 0;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getSubCategories(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public List<Category> getSubCategories(final Category category) {
		return this.daoService.getSubCategories(category);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasSubCategories(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public boolean hasSubCategories(final Category category) {
		return daoService.getSubCategoriesNumber(category) > 0;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveCategoryUp(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void moveCategoryUp(final Category category) {
		Category previousCategory = daoService.getCategoryByOrder(category.getDepartment(), category.getParent(),
				category.getOrder() - 1);
		if (previousCategory != null) {
			category.setOrder(category.getOrder() - 1);
			updateCategory(category);
			previousCategory.setOrder(previousCategory.getOrder() + 1);
			updateCategory(previousCategory);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveCategoryDown(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void moveCategoryDown(final Category category) {
		Category nextCategory = daoService.getCategoryByOrder(category.getDepartment(), category.getParent(),
				category.getOrder() + 1);
		if (nextCategory != null) {
			category.setOrder(category.getOrder() + 1);
			updateCategory(category);
			nextCategory.setOrder(nextCategory.getOrder() - 1);
			updateCategory(nextCategory);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveCategoryFirst(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void moveCategoryFirst(final Category categoryToMove) {
		List<Category> categories;
		if (categoryToMove.getParent() == null) {
			categories = getRootCategories(categoryToMove.getDepartment());
		} else {
			categories = getSubCategories(categoryToMove.getParent());
		}
		for (Category category : categories) {
			if (category.getOrder() < categoryToMove.getOrder()) {
				category.setOrder(category.getOrder() + 1);
				updateCategory(category);
			}
		}
		categoryToMove.setOrder(0);
		updateCategory(categoryToMove);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveCategoryLast(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void moveCategoryLast(final Category categoryToMove) {
		List<Category> categories;
		if (categoryToMove.getParent() == null) {
			categories = getRootCategories(categoryToMove.getDepartment());
		} else {
			categories = getSubCategories(categoryToMove.getParent());
		}
		for (Category category : categories) {
			if (category.getOrder() > categoryToMove.getOrder()) {
				category.setOrder(category.getOrder() - 1);
				updateCategory(category);
			}
		}
		categoryToMove.setOrder(categories.size() - 1);
		updateCategory(categoryToMove);
	}

	/**
	 * Reorder a list of categories.
	 * 
	 * @param categories
	 */
	@Override
	public void reorderCategories(final List<Category> categories) {
		int i = 0;
		for (Category category : categories) {
			if (category.getOrder() != i) {
				category.setOrder(i);
				daoService.updateCategory(category);
			}
			i++;
		}
	}

	/**
	 * Change the department of the tickets of a category.
	 * 
	 * @param category
	 */
	protected void changeTicketsDepartment(final Category category) {
		for (Ticket ticket : getTickets(category)) {
			moveTicket(ticket, category);
		}
	}

	/**
	 * Remove the members of a category that are not managers of the department
	 * (used when moving a category from one deparment to another one).
	 * 
	 * @param category
	 */
	protected void removeNonManagerMembers(final Category category) {
		for (CategoryMember categoryMember : getCategoryMembers(category)) {
			if (!isDepartmentManager(category.getDepartment(), categoryMember.getUser())) {
				deleteCategoryMember(categoryMember);
			}
		}
		reorderCategoryMembers(getCategoryMembers(category));
	}

	/**
	 * Change the department of a category.
	 * 
	 * @param category
	 * @param targetDepartment
	 */
	protected void changeDepartmentRec(final Category category, final Department targetDepartment) {
		category.setDepartment(targetDepartment);
		updateCategory(category);
		removeNonManagerMembers(category);
		changeTicketsDepartment(category);
		for (Category subCategory : getSubCategories(category)) {
			changeDepartmentRec(subCategory, targetDepartment);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveCategory(
	 *      org.esupportail.helpdesk.domain.beans.Category,
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void moveCategory(final Category categoryToUpdate, final Department targetDepartment,
			final Category targetCategory) {
		Department oldDepartment = categoryToUpdate.getDepartment();
		Category oldParent = categoryToUpdate.getParent();
		boolean parentChanged;
		if (!oldDepartment.equals(targetDepartment)) {
			parentChanged = true;
		} else if (oldParent == null && targetCategory == null) {
			parentChanged = false;
		} else if (oldParent == null || targetCategory == null) {
			parentChanged = true;
		} else {
			parentChanged = !oldParent.equals(targetCategory);
		}
		if (parentChanged) {
			if (targetCategory == null) {
				categoryToUpdate.setOrder(daoService.getRootCategoriesNumber(targetDepartment));
			} else {
				categoryToUpdate.setOrder(daoService.getSubCategoriesNumber(targetCategory));
			}
			categoryToUpdate.setDepartment(targetDepartment);
			categoryToUpdate.setParent(targetCategory);
			updateCategory(categoryToUpdate);
			removeNonManagerMembers(categoryToUpdate);
			if (targetCategory == null) {
				reorderCategories(getRootCategories(targetDepartment));
			} else {
				reorderCategories(getSubCategories(targetCategory));
			}
			if (oldParent == null) {
				reorderCategories(getRootCategories(oldDepartment));
			} else {
				reorderCategories(getSubCategories(oldParent));
			}
		} else {
			moveCategoryLast(categoryToUpdate);
		}
		if (!oldDepartment.equals(targetDepartment)) {
			changeTicketsDepartment(categoryToUpdate);
			for (Category subCategory : getSubCategories(categoryToUpdate)) {
				changeDepartmentRec(subCategory, targetDepartment);
			}
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getVirtualCategories(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public List<Category> getVirtualCategories(final Category category) {
		return daoService.getVirtualCategories(category);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasVirtualCategories(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public boolean hasVirtualCategories(final Category category) {
		return daoService.getVirtualCategoriesNumber(category) > 0;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getCategoryEffectiveAssignmentAlgorithmName(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public String getCategoryEffectiveAssignmentAlgorithmName(final Category category) {
		if (category.getAssignmentAlgorithmName() != null) {
			return category.getAssignmentAlgorithmName();
		}
		return getCategoryDefaultAssignmentAlgorithmName(category);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getCategoryDefaultAssignmentAlgorithmName(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public String getCategoryDefaultAssignmentAlgorithmName(final Category category) {
		if (category.getParent() == null) {
			return getDepartmentEffectiveAssignmentAlgorithmName(category.getDepartment());
		}
		return getCategoryEffectiveAssignmentAlgorithmName(category.getParent());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#detectRedirectionLoop(
	 *      org.esupportail.helpdesk.domain.beans.Category,
	 *      org.esupportail.helpdesk.domain.beans.Category, java.util.Map)
	 */
	@Override
	@RequestCache
	public boolean detectRedirectionLoop(final Category category, final Category targetCategory,
			final Map<Category, Boolean> map) {
		Map<Category, Boolean> theMap = map;
		if (theMap == null) {
			theMap = new HashMap<Category, Boolean>();
		}
		if (theMap.get(targetCategory) != null) {
			return theMap.get(targetCategory);
		}
		boolean loop = false;
		if (category.equals(targetCategory)) {
			loop = true;
		} else if (targetCategory.isVirtual()) {
			loop = detectRedirectionLoop(category, targetCategory.getRealCategory(), theMap);
		} else {
			for (Category subCategory : getSubCategories(targetCategory)) {
				if (detectRedirectionLoop(category, subCategory, theMap)) {
					loop = true;
					break;
				}
			}
		}
		theMap.put(targetCategory, loop);
		return loop;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getCategoriesNumber()
	 */
	@Override
	public int getCategoriesNumber() {
		return daoService.getCategoriesNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getRealCategoriesNumber()
	 */
	@Override
	public int getRealCategoriesNumber() {
		return daoService.getRealCategoriesNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getVirtualCategoriesNumber()
	 */
	@Override
	public int getVirtualCategoriesNumber() {
		return daoService.getVirtualCategoriesNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTargetCategories(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public List<Category> getTargetCategories(final User author) {
		return daoService.getTargetCategories(author);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________CATEGORY_MEMBER() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getCategoryMember(
	 *      org.esupportail.helpdesk.domain.beans.Category,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public CategoryMember getCategoryMember(final Category category, final User user)
			throws CategoryMemberNotFoundException {
		CategoryMember member = daoService.getCategoryMember(category, user);
		if (member == null) {
			throw new CategoryMemberNotFoundException(
					"user [" + user.getRealId() + "] is not a member of category [" + category.getLabel() + "]");
		}
		return member;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#isCategoryMember(
	 *      org.esupportail.helpdesk.domain.beans.Category,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean isCategoryMember(final Category category, final User user) {
		try {
			getCategoryMember(category, user);
			return true;
		} catch (CategoryMemberNotFoundException e) {
			return false;
		}
	}


	@Override
	public Boolean isCategoryParentConfidential(Category categoryParent, Boolean isCondifential) {
		if (categoryParent.getCategoryConfidential()) {
			return true;
		} else {
			if(categoryParent.getParent() != null) {
				return isCategoryParentConfidential(categoryParent.getParent(), isCondifential);
			} else {
				return false;
			}
		}
	}
	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getCategoryMembers(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public List<CategoryMember> getCategoryMembers(final Category category) {
		return this.daoService.getCategoryMembers(category);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getCategoryMembersMap()
	 */
	@Override
	@RequestCache
	public Map<Category, List<CategoryMember>> getCategoryMembersMap() {
		Map<Category, List<CategoryMember>> result = new HashMap<Category, List<CategoryMember>>();
		for (CategoryMember categoryMember : daoService.getCategoryMembers()) {
			Category category = categoryMember.getCategory();
			List<CategoryMember> categoryMembers = result.get(category);
			if (categoryMembers == null) {
				categoryMembers = new ArrayList<CategoryMember>();
				result.put(category, categoryMembers);
			}
			categoryMembers.add(categoryMember);
		}
		return result;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addCategoryMember(
	 *      org.esupportail.helpdesk.domain.beans.Category,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public CategoryMember addCategoryMember(final Category category, final User user) {
		CategoryMember categoryMember = new CategoryMember();
		categoryMember.setCategory(category);
		categoryMember.setUser(user);
		categoryMember.setOrder(daoService.getCategoryMembersNumber(category));
		this.daoService.addCategoryMember(categoryMember);
		return categoryMember;
	}

	/**
	 * Reassign the opened tickets managed by a category member to be deleted.
	 * 
	 * @param author
	 * @param category
	 * @param user
	 * @param useAssignmentAlgorithm
	 *            true to use the assignment algorithm, ignored if
	 *            reassignTickets is false
	 * @param newManager
	 *            the new manager for the tickets (null to free the tickets,
	 *            ignored if reassignTicket is false or useAssignmentAlgorithm
	 *            is true
	 */
	protected void reassignTickets(final User author, final Category category, final User user,
			final boolean useAssignmentAlgorithm, final User newManager) {
		for (Ticket ticket : getOpenManagedTickets(category, user)) {
			if (useAssignmentAlgorithm) {
				callAssignmentAlgorithm(ticket, ticket.getManager(), true, true);
			} else if (newManager != null) {
				assignTicket(author, ticket, newManager, null, ActionScope.DEFAULT);
			} else {
				freeTicket(author, ticket, null, ActionScope.DEFAULT);
			}
		}
		for (Category subCategory : getSubCategories(category)) {
			if (subCategory.getInheritMembers()) {
				reassignTickets(author, subCategory, user, useAssignmentAlgorithm, newManager);
			}
		}
	}

	/**
	 * Delete a category member and reassign the opened tickets managed.
	 * 
	 * @param categoryMember
	 * @param reassignTickets
	 *            true to ressign the opened tickets managed by the user, false
	 *            to let the user manage them aven if (s)he is not a member of
	 *            the category anymore)
	 * @param useAssignmentAlgorithm
	 *            true to use the assignment algorithm, ignored if
	 *            reassignTickets is false
	 * @param newManager
	 *            the new manager for the tickets (null to free the tickets,
	 *            ignored if reassignTicket is false or useAssignmentAlgorithm
	 *            is true
	 */
	protected void deleteCategoryMember(final CategoryMember categoryMember, final boolean reassignTickets,
			final boolean useAssignmentAlgorithm, final User newManager) {

		daoService.deleteCategoryMember(categoryMember);
		reorderCategoryMembers(daoService.getCategoryMembers(categoryMember.getCategory()));
		if (reassignTickets) {
			reassignTickets(null, categoryMember.getCategory(), categoryMember.getUser(), useAssignmentAlgorithm,
					newManager);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteCategoryMember(
	 *      org.esupportail.helpdesk.domain.beans.CategoryMember, boolean,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void deleteCategoryMember(final CategoryMember categoryMember, final boolean useAssignmentAlgorithm,
			final User newManager) {
		deleteCategoryMember(categoryMember, true, useAssignmentAlgorithm, newManager);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteCategoryMember(
	 *      org.esupportail.helpdesk.domain.beans.CategoryMember)
	 */
	@Override
	public void deleteCategoryMember(final CategoryMember categoryMember) {
		deleteCategoryMember(categoryMember, false, false, null);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateCategoryMember(
	 *      org.esupportail.helpdesk.domain.beans.CategoryMember)
	 */
	@Override
	public void updateCategoryMember(final CategoryMember categoryMember) {
		daoService.updateCategoryMember(categoryMember);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveCategoryMemberUp(
	 *      org.esupportail.helpdesk.domain.beans.CategoryMember)
	 */
	@Override
	public void moveCategoryMemberUp(final CategoryMember categoryMember) {
		CategoryMember previousCategoryMember = daoService.getCategoryMemberByOrder(categoryMember.getCategory(),
				categoryMember.getOrder() - 1);
		if (previousCategoryMember != null) {
			categoryMember.setOrder(categoryMember.getOrder() - 1);
			updateCategoryMember(categoryMember);
			previousCategoryMember.setOrder(previousCategoryMember.getOrder() + 1);
			updateCategoryMember(previousCategoryMember);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveCategoryMemberDown(
	 *      org.esupportail.helpdesk.domain.beans.CategoryMember)
	 */
	@Override
	public void moveCategoryMemberDown(final CategoryMember categoryMember) {
		CategoryMember nextCategoryMember = daoService.getCategoryMemberByOrder(categoryMember.getCategory(),
				categoryMember.getOrder() + 1);
		if (nextCategoryMember != null) {
			categoryMember.setOrder(categoryMember.getOrder() + 1);
			updateCategoryMember(categoryMember);
			nextCategoryMember.setOrder(nextCategoryMember.getOrder() - 1);
			updateCategoryMember(nextCategoryMember);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveCategoryMemberFirst(
	 *      org.esupportail.helpdesk.domain.beans.CategoryMember)
	 */
	@Override
	public void moveCategoryMemberFirst(final CategoryMember categoryMemberToMove) {
		List<CategoryMember> categoryMembers = getCategoryMembers(categoryMemberToMove.getCategory());
		for (CategoryMember categoryMember : categoryMembers) {
			if (categoryMember.getOrder() < categoryMemberToMove.getOrder()) {
				categoryMember.setOrder(categoryMember.getOrder() + 1);
				updateCategoryMember(categoryMember);
			}
		}
		categoryMemberToMove.setOrder(0);
		updateCategoryMember(categoryMemberToMove);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveCategoryMemberLast(
	 *      org.esupportail.helpdesk.domain.beans.CategoryMember)
	 */
	@Override
	public void moveCategoryMemberLast(final CategoryMember categoryMemberToMove) {
		List<CategoryMember> categoryMembers = getCategoryMembers(categoryMemberToMove.getCategory());
		for (CategoryMember categoryMember : categoryMembers) {
			if (categoryMember.getOrder() > categoryMemberToMove.getOrder()) {
				categoryMember.setOrder(categoryMember.getOrder() - 1);
				updateCategoryMember(categoryMember);
			}
		}
		categoryMemberToMove.setOrder(categoryMembers.size() - 1);
		updateCategoryMember(categoryMemberToMove);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#reorderCategoryMembers(java.util.List)
	 */
	@Override
	public void reorderCategoryMembers(final List<CategoryMember> categoryMembers) {
		int i = 0;
		for (CategoryMember categoryMember : categoryMembers) {
			if (categoryMember.getOrder() != i) {
				categoryMember.setOrder(i);
				daoService.updateCategoryMember(categoryMember);
			}
			i++;
		}
	}

	/**
	 * @param category
	 * @return the real (not inherited) department managers of a category.
	 */
	protected List<DepartmentManager> getRealDepartmentManagers(final Category category) {
		List<DepartmentManager> managers = new ArrayList<DepartmentManager>();
		Department department = category.getDepartment();
		for (CategoryMember categoryMember : getCategoryMembers(category)) {
			managers.add(getDepartmentManager(department, categoryMember.getUser()));
		}
		return managers;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getInheritedDepartmentManagers(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public List<DepartmentManager> getInheritedDepartmentManagers(final Category category) {
		Category parent = category.getParent();
		if (parent == null) {
			return getDepartmentManagers(category.getDepartment());
		}
		if (parent.getInheritMembers()) {
			return getInheritedDepartmentManagers(parent);
		}
		return getRealDepartmentManagers(parent);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getEffectiveDepartmentManagers(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public List<DepartmentManager> getEffectiveDepartmentManagers(final Category category) {
		if (category.getInheritMembers()) {
			return getInheritedDepartmentManagers(category);
		}
		return getRealDepartmentManagers(category);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getEffectiveAvailableDepartmentManagers(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public List<DepartmentManager> getEffectiveAvailableDepartmentManagers(final Category category) {
		List<DepartmentManager> managers = new ArrayList<DepartmentManager>();
		for (DepartmentManager manager : getEffectiveDepartmentManagers(category)) {
			if (manager.getAvailable()) {
				managers.add(manager);
			}
		}
		return managers;
	}

	/**
	 * search a category if member (and recurse).
	 * 
	 * @param user
	 * @param category
	 * @param memberIfInherit
	 * @param result
	 */
	protected void searchMemberCategories(final User user, final Category category, final boolean memberIfInherit,
			final List<Category> result) {
		boolean member = false;
		if (category.getInheritMembers()) {
			member = memberIfInherit;
		} else {
			member = isCategoryMember(category, user);
		}
		if (member) {
			result.add(category);
		}
		for (Category subCategory : getSubCategories(category)) {
			searchMemberCategories(user, subCategory, member, result);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getMemberCategories(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public List<Category> getMemberCategories(final User user, final Department department) {
		List<Category> result = new ArrayList<Category>();
		for (Category category : getRootCategories(department)) {
			searchMemberCategories(user, category, true, result);
		}
		return result;
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________FAQ() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFaq(long)
	 */
	@Override
	public Faq getFaq(final long id) throws FaqNotFoundException {
		Faq faq = this.daoService.getFaq(id);
		if (faq == null) {
			throw new FaqNotFoundException("no FAQ found with id [" + id + "]");
		}
		return faq;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addFaq(
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void addFaq(final Faq faq) {
		fckEditorCodeCleaner.removeMaliciousTags(faq);
		faq.computeEffectiveScope(true);
		if (faq.getParent() == null) {
			faq.setOrder(daoService.getRootFaqsNumber(faq.getDepartment()));
		} else {
			faq.setOrder(daoService.getSubFaqsNumber(faq.getParent()));
		}
		daoService.addFaq(faq);
	}

	/**
	 * Update the scope of the children of a FAQ if needed.
	 * 
	 * @param faq
	 */
	protected void updateFaqChildrenScope(final Faq faq) {
		for (Faq subFaq : getSubFaqs(faq)) {
			if (subFaq.computeEffectiveScope(true)) {
				daoService.updateFaq(subFaq);
				updateFaqChildrenScope(subFaq);
			}
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateFaq(
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void updateFaq(final Faq faq) {
		fckEditorCodeCleaner.removeMaliciousTags(faq);
		boolean effectiveScopeChanged = faq.computeEffectiveScope(true);
		daoService.updateFaq(faq);
		if (effectiveScopeChanged) {
			updateFaqChildrenScope(faq);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteFaq(
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void deleteFaq(final Faq faq) {
		daoService.deleteFaq(faq);
		this.daoService.addDeletedItem(new DeletedItem(indexIdProvider.getIndexId(faq)));
		if (faq.getParent() == null) {
			reorderFaqs(daoService.getRootFaqs(faq.getDepartment()));
		} else {
			reorderFaqs(daoService.getSubFaqs(faq.getParent()));
		}
	}

	/**
	 * Reorder a list of FAQs.
	 * 
	 * @param faqs
	 */
	protected void reorderFaqs(final List<Faq> faqs) {
		int i = 0;
		for (Faq faq : faqs) {
			if (faq.getOrder() != i) {
				faq.setOrder(i);
				daoService.updateFaq(faq);
			}
			i++;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getRootFaqs(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public List<Faq> getRootFaqs(final Department department) {
		return this.daoService.getRootFaqs(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getRootFaqsNumber(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public int getRootFaqsNumber(final Department department) {
		return daoService.getRootFaqsNumber(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasRootFaqs(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public boolean hasRootFaqs(final Department department) {
		return getRootFaqsNumber(department) > 0;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getRootFaqs()
	 */
	@Override
	public List<Faq> getRootFaqs() {
		return getRootFaqs(null);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getSubFaqs(
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public List<Faq> getSubFaqs(final Faq faq) {
		return this.daoService.getSubFaqs(faq);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveFaqUp(
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void moveFaqUp(final Faq faq) {
		Faq previousFaq = daoService.getFaqByOrder(faq.getDepartment(), faq.getParent(), faq.getOrder() - 1);
		if (previousFaq != null) {
			faq.setOrder(faq.getOrder() - 1);
			updateFaq(faq);
			previousFaq.setOrder(previousFaq.getOrder() + 1);
			updateFaq(previousFaq);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveFaqDown(
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void moveFaqDown(final Faq faq) {
		Faq previousFaq = daoService.getFaqByOrder(faq.getDepartment(), faq.getParent(), faq.getOrder() + 1);
		if (previousFaq != null) {
			faq.setOrder(faq.getOrder() + 1);
			updateFaq(faq);
			previousFaq.setOrder(previousFaq.getOrder() - 1);
			updateFaq(previousFaq);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveFaqFirst(
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void moveFaqFirst(final Faq faqToMove) {
		List<Faq> faqs;
		if (faqToMove.getParent() == null) {
			faqs = getRootFaqs(faqToMove.getDepartment());
		} else {
			faqs = getSubFaqs(faqToMove.getParent());
		}
		for (Faq faq : faqs) {
			if (faq.getOrder() < faqToMove.getOrder()) {
				faq.setOrder(faq.getOrder() + 1);
				updateFaq(faq);
			}
		}
		faqToMove.setOrder(0);
		updateFaq(faqToMove);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveFaqLast(
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void moveFaqLast(final Faq faqToMove) {
		List<Faq> faqs;
		if (faqToMove.getParent() == null) {
			faqs = getRootFaqs(faqToMove.getDepartment());
		} else {
			faqs = getSubFaqs(faqToMove.getParent());
		}
		for (Faq faq : faqs) {
			if (faq.getOrder() > faqToMove.getOrder()) {
				faq.setOrder(faq.getOrder() - 1);
				updateFaq(faq);
			}
		}
		faqToMove.setOrder(faqs.size() - 1);
		updateFaq(faqToMove);
	}

	/**
	 * Change the department of FAQs.
	 * 
	 * @param faq
	 * @param targetDepartment
	 */
	protected void changeDepartmentRec(final Faq faq, final Department targetDepartment) {
		for (Faq subFaq : getSubFaqs(faq)) {
			subFaq.setDepartment(targetDepartment);
			updateFaq(subFaq);
			changeDepartmentRec(subFaq, targetDepartment);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveFaq(
	 *      org.esupportail.helpdesk.domain.beans.Faq,
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void moveFaq(final Faq faqToUpdate, final Department targetDepartment, final Faq targetFaq) {
		Department oldDepartment = faqToUpdate.getDepartment();
		Faq oldParent = faqToUpdate.getParent();
		boolean departmentChanged;
		if (oldDepartment == null && targetDepartment == null) {
			departmentChanged = false;
		} else if (oldDepartment == null || targetDepartment == null) {
			departmentChanged = true;
		} else {
			departmentChanged = !oldDepartment.equals(targetDepartment);
		}
		boolean parentChanged;
		if (departmentChanged) {
			parentChanged = true;
		} else if (oldParent == null && targetFaq == null) {
			parentChanged = false;
		} else if (oldParent == null || targetFaq == null) {
			parentChanged = true;
		} else {
			parentChanged = !oldParent.equals(targetFaq);
		}
		if (parentChanged) {
			faqToUpdate.setParent(targetFaq);
			if (targetFaq == null) {
				faqToUpdate.setOrder(daoService.getRootFaqsNumber(targetDepartment));
			} else {
				faqToUpdate.setOrder(daoService.getSubFaqsNumber(targetFaq));
			}
			faqToUpdate.setDepartment(targetDepartment);
			updateFaq(faqToUpdate);
			if (oldParent == null) {
				reorderFaqs(getRootFaqs(oldDepartment));
			} else {
				reorderFaqs(getSubFaqs(oldParent));
			}
		} else {
			moveFaqLast(faqToUpdate);
		}
		if (departmentChanged) {
			for (Faq subFaq : getSubFaqs(faqToUpdate)) {
				changeDepartmentRec(subFaq, targetDepartment);
			}
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasSubFaqs(
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public boolean hasSubFaqs(final Faq faq) {
		return daoService.getSubFaqsNumber(faq) > 0;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFaqsChangedAfter(
	 *      java.sql.Timestamp, int)
	 */
	@Override
	public List<Faq> getFaqsChangedAfter(final Timestamp lastUpdate, final int maxResults) {
		return daoService.getFaqsChangedAfter(lastUpdate, maxResults);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________FAQ_EVENT() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addFaqEvent(
	 *      org.esupportail.helpdesk.domain.beans.FaqEvent)
	 */
	@Override
	public void addFaqEvent(final FaqEvent faqEvent) {
		daoService.addFaqEvent(faqEvent);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFaqEvents()
	 */
	@Override
	public List<FaqEvent> getFaqEvents() {
		return daoService.getFaqEvents();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteFaqEvent(
	 *      org.esupportail.helpdesk.domain.beans.FaqEvent)
	 */
	@Override
	public void deleteFaqEvent(final FaqEvent faqEvent) {
		daoService.deleteFaqEvent(faqEvent);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________TICKET() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicket(long)
	 */
	@Override
	@RequestCache
	public Ticket getTicket(final long id) throws TicketNotFoundException {
		Ticket ticket = this.daoService.getTicket(id);
		if (ticket == null) {
			throw new TicketNotFoundException("no ticket found with id [" + id + "]");
		}
		return ticket;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTickets(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public List<Ticket> getTickets(final Category category) {
		return daoService.getTickets(category);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasTickets(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public boolean hasTickets(final Category category) {
		return daoService.getTicketsNumber(category) > 0;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTickets(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public List<Ticket> getTickets(final Department department) {
		return daoService.getTickets(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getOpenedTicketsByLastActionDate(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public List<Ticket> getOpenedTicketsByLastActionDate(final Department department) {
		return daoService.getOpenedTicketsByLastActionDate(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketsNumber(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public int getTicketsNumber(final Department department) {
		return daoService.getTicketsNumber(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateTicket(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public void updateTicket(final Ticket ticket) {
		//suppression des blancs en début de champ
		ticket.setLabel(StringUtils.stripStart(ticket.getLabel(), null));
		ticket.computeEffectiveDefaultTicketScope();
		this.daoService.updateTicket(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#reloadTicket(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public Ticket reloadTicket(final Ticket ticket) {
		return daoService.reloadTicket(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteTicket(
	 *      org.esupportail.helpdesk.domain.beans.Ticket, boolean)
	 */
	@Override
	public void deleteTicket(final Ticket ticket, final boolean deleteFiles) {
		this.daoService.deleteTicket(ticket, deleteFiles);
		this.daoService.addDeletedItem(new DeletedItem(indexIdProvider.getIndexId(ticket)));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteTicket(
	 *      org.esupportail.helpdesk.domain.beans.Ticket, boolean)
	 */
	@Override
	public void deleteFileInfo(final User author, final Ticket ticket, final String message, final String actionScope,
			final boolean alerts, final FileInfo file, final boolean deleteContent) {

		Action newAction = deleteFileInfo(author, ticket, message, actionScope);

		newAction.setFilename(file.getFilename());
		this.daoService.deleteFileInfo(file, deleteContent);
		if (alerts) {
			monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
		}

	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setCreator(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public void setCreator(final Ticket ticket) {
		List<Action> actions = daoService.getActions(ticket, true);
		if (!actions.isEmpty()) {
			Action createAction = actions.get(0);
			if (ActionType.CREATE.equals(createAction.getActionType())) {
				ticket.setCreator(createAction.getUser());
			}
			for (Action action : actions) {
				if (ActionType.CHANGE_DEPARTMENT.equals(action.getActionType())) {
					ticket.setCreationDepartment(action.getDepartmentBefore());
					break;
				}
			}
		}
		if (ticket.getCreator() == null) {
			ticket.setCreator(ticket.getOwner());
		}
		if (ticket.getCreationDepartment() == null) {
			ticket.setCreationDepartment(ticket.getDepartment());
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketsNumber()
	 */
	@Override
	public int getTicketsNumber() {
		return daoService.getTicketsNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTickets(long, int)
	 */
	@Override
	public List<Ticket> getTickets(final long startIndex, final int num) {
		return daoService.getTickets(startIndex, num);
	}

	/**
	 * @param category
	 * @param user
	 * @return The opended tickets of the category managed by the user.
	 */
	protected List<Ticket> getOpenManagedTickets(final Category category, final User user) {
		return daoService.getOpenManagedTickets(category, user);
	}

	/**
	 * @param department
	 * @param user
	 * @return The opended tickets of the department managed by the user.
	 */
	protected List<Ticket> getOpenManagedTickets(final Department department, final User user) {
		return daoService.getOpenManagedTickets(department, user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getOpenManagedTicketsNumber(
	 *      org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public int getOpenManagedTicketsNumber(final DepartmentManager departmentManager) {
		return daoService.getOpenManagedTicketsNumber(departmentManager.getDepartment(), departmentManager.getUser());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasOpenManagedTickets(
	 *      org.esupportail.helpdesk.domain.beans.Category,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean hasOpenManagedTickets(final Category category, final User user) {
		return daoService.getOpenManagedTicketsNumber(category, user) > 0;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasOpenManagedTickets(
	 *      org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public boolean hasOpenManagedTickets(final DepartmentManager departmentManager) {
		return daoService.getOpenManagedTicketsNumber(departmentManager.getDepartment(),
				departmentManager.getUser()) > 0;
	}

	/**
	 * @param user
	 * @return the tickets owned by a user.
	 */
	protected List<Ticket> getOwnedTickets(final User user) {
		return daoService.getOwnedTickets(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#archiveTicket(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public void archiveTicket(final Ticket ticket) {
		ArchivedTicket archivedTicket = new ArchivedTicket(ticket);
		daoService.addArchivedTicket(archivedTicket);
		for (Action action : daoService.getActions(ticket, true)) {
			ArchivedAction archivedAction = new ArchivedAction(action, archivedTicket);
			daoService.addArchivedAction(archivedAction);
		}
		for (FileInfo fileInfo : daoService.getFileInfos(ticket, true)) {
			ArchivedFileInfo archivedFileInfo = new ArchivedFileInfo(fileInfo, archivedTicket);
			daoService.addArchivedFileInfo(archivedFileInfo);
		}
		for (Invitation invitation : daoService.getInvitations(ticket)) {
			ArchivedInvitation archivedInvitation = new ArchivedInvitation(invitation, archivedTicket);
			daoService.addArchivedInvitation(archivedInvitation);
		}
		for (Bookmark bookmark : daoService.getBookmarks(ticket)) {
			bookmark.setTicket(null);
			bookmark.setArchivedTicket(archivedTicket);
			daoService.updateBookmark(bookmark);
		}
		for (HistoryItem historyItem : daoService.getHistoryItems(ticket)) {
			historyItem.setTicket(null);
			historyItem.setArchivedTicket(archivedTicket);
			daoService.updateHistoryItem(historyItem);
		}
		deleteTicket(ticket, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getClosedTicketsBefore(java.sql.Timestamp,
	 *      int)
	 */
	@Override
	public List<Ticket> getClosedTicketsBefore(final Timestamp timestamp, final int maxResults) {
		return daoService.getClosedTicketsBefore(timestamp, maxResults);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getNonApprovedTicketsClosedBefore(java.sql.Timestamp,
	 *      int)
	 */
	@Override
	public List<Ticket> getNonApprovedTicketsClosedBefore(final Timestamp timestamp, final int maxResults) {
		return daoService.getNonApprovedTicketsClosedBefore(timestamp, maxResults);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketsChangedAfter(
	 *      java.sql.Timestamp, int)
	 */
	@Override
	public List<Ticket> getTicketsChangedAfter(final Timestamp lastUpdate, final int maxResults) {
		return daoService.getTicketsChangedAfter(lastUpdate, maxResults);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketComputerUrl(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public String getTicketComputerUrl(final Ticket ticket) {
		if (ticket == null) {
			return null;
		}
		if (ticket.getComputer() == null) {
			return null;
		}
		Department department = ticket.getDepartment();
		String computerUrlBuilderName = department.getComputerUrlBuilderName();
		if (computerUrlBuilderName == null) {
			computerUrlBuilderName = getDefaultComputerUrlBuilderName();
		}
		ComputerUrlBuilder computerUrlBuilder = computerUrlBuilderStore.getComputerUrlBuilder(computerUrlBuilderName);
		if (computerUrlBuilder == null) {
			return null;
		}
		return computerUrlBuilder.getUrl(ticket.getComputer());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getOldestTicketDate()
	 */
	@Override
	public Timestamp getOldestTicketDate() {
		return daoService.getOldestTicketDate();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteAllTickets()
	 */
	@Override
	public void deleteAllTickets() {
		daoService.deleteAllTickets();
	}

	@Override
	public void deleteTicketById(final long ticketNumber) {
		Ticket ticket = daoService.getTicket(ticketNumber);
		if (ticket != null) {
			daoService.deleteTicket(ticket, true);
		} else {
			logger.info("no ticket found. Ticket number : " + ticketNumber);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteArchivedTickets()
	 */
	@Override
	public void deleteArchivedTickets(final Integer days) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar dateToDelete = Calendar.getInstance();
		dateToDelete.setTime(new Date()); // Now use today date.
		dateToDelete.add(Calendar.DATE, -days); // substract x days
		Integer compteur = 0;
		Integer compteurTotal = 0;

		logger.info("delete archiving ticket before : " + dateToDelete.getTime());
		List<ArchivedTicket> archivedTickets = daoService.getArchivedTicketsOlderThan(dateToDelete.getTime());
		logger.info("number of archiving ticket to be delete : " + archivedTickets.size());
		for (ArchivedTicket archivedTicket : archivedTickets) {
			daoService.deleteArchivedTicket(archivedTicket);
			compteur++;
			if (compteur >= 1000) {
				compteurTotal += compteur;
				compteur = 0;
				logger.info("nb messages traités : " + compteurTotal);
			}
		}
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________ACTION() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getActions(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public List<Action> getActions(final Ticket ticket) {
		return daoService.getActions(ticket, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getLastAction(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public Action getLastAction(final Ticket ticket) {
		return daoService.getLastAction(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getLastAction(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public Action getLastActionWithoutUpload(final Ticket ticket) {
		return daoService.getLastActionWithoutUpload(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getActions(long, int)
	 */
	@Override
	public List<Action> getActions(final long startIndex, final int num) {
		return daoService.getActions(startIndex, num);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getActions(long, int)
	 */
	@Override
	public Action getLastActionByActionType(final Ticket ticket, final String actionType) {
		return daoService.getLastActionByActionType(ticket, actionType);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateAction(
	 *      org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	public void updateAction(final Action action) {
		fckEditorCodeCleaner.removeMaliciousTags(action);
		daoService.updateAction(action);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addAction(
	 *      org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	public void addAction(final Action action) {
		fckEditorCodeCleaner.removeMaliciousTags(action);
		daoService.addAction(action);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getActionsNumber()
	 */
	@Override
	public int getActionsNumber() {
		return daoService.getActionsNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getActionStyleClass(
	 *      org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	@RequestCache
	public String getActionStyleClass(final Action action) {
		User user = action.getUser();
		if (user == null) {
			return "actionApplication";
		}
		Ticket ticket = action.getTicket();
		if (user.equals(ticket.getOwner())) {
			return "actionOwner";
		}
		if (isDepartmentManager(ticket.getDepartment(), user)) {
			return "actionManager";
		}
		if (isInvited(user, ticket)) {
			return "actionInvited";
		}
		return "actionUser";
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________FILEINFO() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFileInfos(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public List<FileInfo> getFileInfos(final Ticket ticket) {
		return daoService.getFileInfos(ticket, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addFileInfo(
	 *      org.esupportail.helpdesk.domain.beans.FileInfo)
	 */
	@Override
	public void addFileInfo(final FileInfo fileInfo) {
		fileInfo.setFilesize(fileInfo.getContent().length);
		daoService.addFileInfo(fileInfo);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateFileInfo(
	 *      org.esupportail.helpdesk.domain.beans.FileInfo)
	 */
	@Override
	public void updateFileInfo(final FileInfo fileInfo) {
		daoService.updateFileInfo(fileInfo);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFileInfoContent(
	 *      org.esupportail.helpdesk.domain.beans.FileInfo)
	 */
	@Override
	public byte[] getFileInfoContent(final FileInfo fileInfo) {
		return daoService.getFileInfoContent(fileInfo);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________ARCHIVED_TICKET() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedTicket(long)
	 */
	@Override
	@RequestCache
	public ArchivedTicket getArchivedTicket(final long id) throws ArchivedTicketNotFoundException {
		ArchivedTicket archivedTicket = this.daoService.getArchivedTicket(id);
		if (archivedTicket == null) {
			throw new ArchivedTicketNotFoundException("no archived ticket found with id [" + id + "]");
		}
		return archivedTicket;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketsArchivedAfter(
	 *      java.sql.Timestamp, int)
	 */
	@Override
	public List<ArchivedTicket> getTicketsArchivedAfter(final Timestamp lastUpdate, final int maxResults) {
		return daoService.getTicketsArchivedAfter(lastUpdate, maxResults);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedTicketByOriginalId(long)
	 */
	@Override
	@RequestCache
	public ArchivedTicket getArchivedTicketByOriginalId(final long id) throws ArchivedTicketNotFoundException {
		ArchivedTicket archivedTicket = daoService.getArchivedTicketByOriginalId(id);
		if (archivedTicket == null) {
			throw new ArchivedTicketNotFoundException("no archived ticket found with oringinal id [" + id + "]");
		}
		return archivedTicket;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedTicketsNumber()
	 */
	@Override
	public int getArchivedTicketsNumber() {
		return daoService.getArchivedTicketsNumber();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedTicketsNumber(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public int getArchivedTicketsNumber(final Department department) {
		return daoService.getArchivedTicketsNumber(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedTickets(long,
	 *      int)
	 */
	@Override
	public List<ArchivedTicket> getArchivedTickets(final long startIndex, final int num) {
		return daoService.getArchivedTickets(startIndex, num);
	}

	//////////////////////////////////////////////////////////////
	// ArchivedAction
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateArchivedAction(
	 *      org.esupportail.helpdesk.domain.beans.ArchivedAction)
	 */
	@Override
	public void updateArchivedAction(final ArchivedAction archivedAction) {
		fckEditorCodeCleaner.removeMaliciousTags(archivedAction);
		daoService.updateArchivedAction(archivedAction);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedActions(
	 *      org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	public List<ArchivedAction> getArchivedActions(final ArchivedTicket archivedTicket) {
		return daoService.getArchivedActions(archivedTicket, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedActions(long,
	 *      int)
	 */
	@Override
	public List<ArchivedAction> getArchivedActions(final long startIndex, final int num) {
		return daoService.getArchivedActions(startIndex, num);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedActionStyleClass(
	 *      org.esupportail.helpdesk.domain.beans.ArchivedAction)
	 */
	@Override
	@RequestCache
	public String getArchivedActionStyleClass(final ArchivedAction archivedAction) {
		User user = archivedAction.getUser();
		if (user == null) {
			return "actionApplication";
		}
		ArchivedTicket archivedTicket = archivedAction.getArchivedTicket();
		if (user.equals(archivedTicket.getOwner())) {
			return "actionOwner";
		}
		if (isDepartmentManager(archivedTicket.getDepartment(), user)) {
			return "actionManager";
		}
		if (isInvited(user, archivedTicket)) {
			return "actionInvited";
		}
		return "actionUser";
	}

	//////////////////////////////////////////////////////////////
	// ArchivedFileInfo
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedFileInfos(
	 *      org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	public List<ArchivedFileInfo> getArchivedFileInfos(final ArchivedTicket archivedTicket) {
		return daoService.getArchivedFileInfos(archivedTicket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedFileInfoContent(
	 *      org.esupportail.helpdesk.domain.beans.ArchivedFileInfo)
	 */
	@Override
	public byte[] getArchivedFileInfoContent(final ArchivedFileInfo archivedFileInfo) {
		return daoService.getArchivedFileInfoContent(archivedFileInfo);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________TICKET_VIEW() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketLastView(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public Timestamp getTicketLastView(final User user, final Ticket ticket) {
		TicketView ticketView = daoService.getTicketView(user, ticket);
		if (ticketView == null) {
			return null;
		}
		return ticketView.getDate();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setTicketLastView(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.sql.Timestamp)
	 */
	@Override
	public void setTicketLastView(final User user, final Ticket ticket, final Timestamp ts) {
		TicketView ticketView = daoService.getTicketView(user, ticket);
		if (ts != null) {
			if (ticketView == null) {
				daoService.addTicketView(new TicketView(user, ticket, ts));
			} else {
				ticketView.setDate(ts);
				daoService.updateTicketView(ticketView);
			}
		} else {
			if (ticketView != null) {
				daoService.deleteTicketView(ticketView);
			}
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasTicketChangedSince(
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.sql.Timestamp)
	 */
	@Override
	@RequestCache
	public boolean hasTicketChangedSince(final Ticket ticket, final Timestamp date) {
		if (date == null) {
			return true;
		}
		return date.before(ticket.getLastActionDate());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasTicketChangedSinceLastView(
	 *      org.esupportail.helpdesk.domain.beans.Ticket,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean hasTicketChangedSinceLastView(final Ticket ticket, final User user) {
		Timestamp lastView = getTicketLastView(user, ticket);
		if (lastView == null) {
			return true;
		}
		return lastView.before(ticket.getLastActionDate());
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________TICKET_MONITORING() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketMonitorings(org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public List<TicketMonitoring> getTicketMonitorings(final Ticket ticket) {
		return daoService.getTicketMonitorings(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userMonitorsTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public boolean userMonitorsTicket(final User user, final Ticket ticket) {
		return daoService.getTicketMonitoring(user, ticket) != null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setTicketMonitoring(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public void setTicketMonitoring(final User user, final Ticket ticket) {
		if (!userMonitorsTicket(user, ticket)) {
			daoService.addTicketMonitoring(new TicketMonitoring(user, ticket));
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#unsetTicketMonitoring(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public void unsetTicketMonitoring(final User user, final Ticket ticket) {
		TicketMonitoring ticketMonitoring = daoService.getTicketMonitoring(user, ticket);
		if (ticketMonitoring != null) {
			daoService.deleteTicketMonitoring(ticketMonitoring);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#ticketMonitoringSendAlerts(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.util.List,
	 *      boolean)
	 */
	@Override
	public void ticketMonitoringSendAlerts(final User author, final Ticket ticket, final List<User> excludedUsers,
			final boolean expiration) {
		monitoringSender.ticketMonitoringSendAlerts(author, ticket, excludedUsers, expiration);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getMonitoringUsers(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public List<User> getMonitoringUsers(final Ticket ticket, final Boolean onlyMandatoryUsers) {
		return monitoringSender.getMonitoringUsers(ticket, onlyMandatoryUsers);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________ALERT() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getAlerts(
	 *      org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	public List<Alert> getAlerts(final Action action) {
		return daoService.getAlerts(action);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addAlert(
	 *      org.esupportail.helpdesk.domain.beans.Action,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void addAlert(final Action action, final User user) {
		daoService.addAlert(new Alert(action, user));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addAlert(
	 *      org.esupportail.helpdesk.domain.beans.Action, java.lang.String)
	 */
	@Override
	public void addAlert(final Action action, final String email) {
		daoService.addAlert(new Alert(action, email));
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________INVITATION() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#isInvited(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public boolean isInvited(final User user, final Ticket ticket) {
		return daoService.getInvitation(user, ticket) != null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getInvitations(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public List<Invitation> getInvitations(final Ticket ticket) {
		return daoService.getInvitations(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#isInvited(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	public boolean isInvited(final User user, final ArchivedTicket archivedTicket) {
		return daoService.getArchivedInvitation(user, archivedTicket) != null;
	}

	/**
	 * Remove an invitation.
	 * 
	 * @param archivedInvitation
	 */
	protected void removeInvitation(final ArchivedInvitation archivedInvitation) {
		daoService.deleteArchivedInvitation(archivedInvitation);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedInvitations(
	 *      org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	public List<ArchivedInvitation> getArchivedInvitations(final ArchivedTicket archivedTicket) {
		return daoService.getArchivedInvitations(archivedTicket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentInvitations(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public List<DepartmentInvitation> getDepartmentInvitations(final Department department) {
		return daoService.getDepartmentInvitations(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#isDepartmentInvited(
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean isDepartmentInvited(final Department department, final User user) {
		return daoService.getDepartmentInvitation(user, department) != null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addDepartmentInvitation(
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void addDepartmentInvitation(final Department department, final User user) {
		daoService.addDepartmentInvitation(new DepartmentInvitation(user, department));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteDepartmentInvitation(
	 *      org.esupportail.helpdesk.domain.beans.DepartmentInvitation)
	 */
	@Override
	public void deleteDepartmentInvitation(final DepartmentInvitation departmentInvitation) {
		daoService.deleteDepartmentInvitation(departmentInvitation);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getInvitedUsers(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public List<User> getInvitedUsers(final User author) {
		return daoService.getInvitedUsers(author);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________DELETED_ITEM() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteAllDeletedItems()
	 */
	@Override
	public void deleteAllDeletedItems() {
		daoService.deleteAllDeletedItems();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteDeletedItem(
	 *      org.esupportail.helpdesk.domain.beans.DeletedItem)
	 */
	@Override
	public void deleteDeletedItem(final DeletedItem deletedItem) {
		daoService.deleteDeletedItem(deletedItem);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDeletedItems()
	 */
	@Override
	public List<DeletedItem> getDeletedItems() {
		return daoService.getDeletedItems();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________BOOKMARK() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getBookmarks(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public List<Bookmark> getBookmarks(final User user) {
		return daoService.getBookmarks(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getBookmarks(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public List<Bookmark> getBookmarks(final Ticket ticket) {
		return daoService.getBookmarks(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getBookmark(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public Bookmark getBookmark(final User user, final Ticket ticket) {
		return daoService.getBookmark(user, ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getBookmark(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	public Bookmark getBookmark(final User user, final ArchivedTicket archivedTicket) {
		return daoService.getBookmark(user, archivedTicket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteBookmark(
	 *      org.esupportail.helpdesk.domain.beans.Bookmark)
	 */
	@Override
	public void deleteBookmark(final Bookmark bookmark) {
		daoService.deleteBookmark(bookmark);
	}

	/**
	 * Add a bookmark.
	 * 
	 * @param bookmark
	 */
	protected void addBookmark(final Bookmark bookmark) {
		daoService.addBookmark(bookmark);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addBookmark(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public void addBookmark(final User user, final Ticket ticket) {
		if (getBookmark(user, ticket) == null) {
			addBookmark(new Bookmark(user, ticket));
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addBookmark(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	public void addBookmark(final User user, final ArchivedTicket archivedTicket) {
		if (getBookmark(user, archivedTicket) == null) {
			addBookmark(new Bookmark(user, archivedTicket));
		}
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________HISTORY_ITEM() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getHistoryItems(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public List<HistoryItem> getHistoryItems(final User user) {
		return daoService.getHistoryItems(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addHistoryItem(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public void addHistoryItem(final User user, final Ticket ticket) {
		List<HistoryItem> historyItems = getHistoryItems(user);
		int index = 0;
		for (HistoryItem hi : historyItems) {
			if (index > (historyMaxLength - 2) || ticket.equals(hi.getTicket())) {
				daoService.deleteHistoryItem(hi);
			} else {
				index++;
			}
		}
		daoService.addHistoryItem(new HistoryItem(user, ticket));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addHistoryItem(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	public void addHistoryItem(final User user, final ArchivedTicket archivedTicket) {
		List<HistoryItem> historyItems = getHistoryItems(user);
		int index = 0;
		for (HistoryItem hi : historyItems) {
			if (index > (historyMaxLength - 2) || archivedTicket.equals(hi.getArchivedTicket())) {
				daoService.deleteHistoryItem(hi);
			} else {
				index++;
			}
		}
		daoService.addHistoryItem(new HistoryItem(user, archivedTicket));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#clearHistoryItems(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void clearHistoryItems(final User user) {
		daoService.clearHistoryItems(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#clearHistoryItems()
	 */
	@Override
	public void clearHistoryItems() {
		daoService.clearHistoryItems();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________RESPONSE() {
		//
	}

	/**
	 * Add a response.
	 * 
	 * @param response
	 */
	@Override
	public void addResponse(final Response response) {
		daoService.addResponse(response);
	}

	/**
	 * Update a response.
	 * 
	 * @param response
	 */
	@Override
	public void updateResponse(final Response response) {
		daoService.updateResponse(response);
	}

	/**
	 * Delete a response.
	 * 
	 * @param response
	 */
	@Override
	public void deleteResponse(final Response response) {
		daoService.deleteResponse(response);
	}

	/**
	 * @param user
	 * @return the responses of a user.
	 */
	@Override
	public List<Response> getUserResponses(final User user) {
		return daoService.getUserResponses(user);
	}

	/**
	 * @param department
	 * @return the responses of a department.
	 */
	@Override
	public List<Response> getDepartmentResponses(final Department department) {
		return daoService.getDepartmentResponses(department);
	}

	/**
	 * @return the global responses.
	 */
	@Override
	public List<Response> getGlobalResponses() {
		return daoService.getGlobalResponses();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________ICONS() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getIcon(long)
	 */
	@Override
	@RequestCache
	public Icon getIcon(final long id) {
		Icon icon = daoService.getIcon(id);
		if (icon == null) {
			throw new IconNotFoundException("no icon found with id [" + id + "]");
		}
		return icon;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getIcons()
	 */
	@Override
	public List<Icon> getIcons() {
		return this.daoService.getIcons();
	}

	/**
	 * @param iconName
	 * @param contentType
	 * @param path
	 * @return an Icon
	 */
	protected Icon createIconFromLocalFile(final String iconName, final String contentType, final String path) {
		Icon icon = new Icon(iconName, contentType, FileUtils.getFileContent(path));
		daoService.addIcon(icon);
		return icon;
	}

	/**
	 * @param iconName
	 * @param fileBasename
	 * @return an Icon
	 */
	protected Icon createIconFromLocalPngFile(final String iconName, final String fileBasename) {
		String theIconName = iconName;
		int i = 2;
		while (getIconByName(theIconName) != null) {
			theIconName = iconName + i;
			i++;
		}
		return createIconFromLocalFile(theIconName, "image/png", "/properties/web/icons/" + fileBasename + ".png");
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addIcon(java.lang.String)
	 */
	@Override
	public Icon addIcon(final String name) {
		return createIconFromLocalPngFile(name, "new");
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#createIconFromLocalPngFile(java.lang.String)
	 */
	@Override
	public Icon createIconFromLocalPngFile(final String iconName) {
		return createIconFromLocalPngFile(iconName, iconName);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteIcon(
	 *      org.esupportail.helpdesk.domain.beans.Icon)
	 */
	@Override
	public void deleteIcon(final Icon icon) {
		daoService.deleteIcon(icon);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateIcon(
	 *      org.esupportail.helpdesk.domain.beans.Icon)
	 */
	@Override
	public void updateIcon(final Icon icon) {
		daoService.updateIcon(icon);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getIconByName(java.lang.String)
	 */
	@Override
	public Icon getIconByName(final String name) {
		return daoService.getIconByName(name);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________VERSION_MANAGER() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDatabaseVersion()
	 */
	@Override
	public Version getDatabaseVersion() throws ConfigException {
		VersionManager versionManager = daoService.getVersionManager();
		if (versionManager == null) {
			return null;
		}
		return new Version(versionManager.getVersion());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setDatabaseVersion(java.lang.String)
	 */
	@Override
	public void setDatabaseVersion(final String version) {
		if (logger.isDebugEnabled()) {
			logger.debug("setting database version to '" + version + "'...");
		}
		VersionManager versionManager = daoService.getVersionManager();
		versionManager.setVersion(version);
		daoService.updateVersionManager(versionManager);
		if (logger.isDebugEnabled()) {
			logger.debug("database version set to '" + version + "'.");
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setDatabaseVersion(
	 *      org.esupportail.commons.services.application.Version)
	 */
	@Override
	public void setDatabaseVersion(final Version version) {
		setDatabaseVersion(version.toString());
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________STATE() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setUpgradeState(java.lang.String)
	 */
	@Override
	public void setUpgradeState(final String upgradeState) {
		State state = daoService.getState();
		state.setUpgradeState(upgradeState);
		daoService.updateState(state);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getUpgradeState()
	 */
	@Override
	public String getUpgradeState() {
		return daoService.getState().getUpgradeState();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________CONFIG() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setTicketsLastIndexTime(
	 *      java.sql.Timestamp)
	 */
	@Override
	public void setTicketsLastIndexTime(final Timestamp lastIndexTime) {
		Config config = daoService.getConfig();
		config.setTicketsLastIndexTime(lastIndexTime);
		daoService.updateConfig(config);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketsLastIndexTime()
	 */
	@Override
	public Timestamp getTicketsLastIndexTime() {
		return daoService.getConfig().getTicketsLastIndexTime();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setFaqsLastIndexTime(
	 *      java.sql.Timestamp)
	 */
	@Override
	public void setFaqsLastIndexTime(final Timestamp lastIndexTime) {
		Config config = daoService.getConfig();
		config.setFaqsLastIndexTime(lastIndexTime);
		daoService.updateConfig(config);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFaqsLastIndexTime()
	 */
	@Override
	public Timestamp getFaqsLastIndexTime() {
		return daoService.getConfig().getFaqsLastIndexTime();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setArchivedTicketsLastIndexTime(
	 *      java.sql.Timestamp)
	 */
	@Override
	public void setArchivedTicketsLastIndexTime(final Timestamp lastIndexTime) {
		Config config = daoService.getConfig();
		config.setArchivedTicketsLastIndexTime(lastIndexTime);
		daoService.updateConfig(config);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getArchivedTicketsLastIndexTime()
	 */
	@Override
	public Timestamp getArchivedTicketsLastIndexTime() {
		return daoService.getConfig().getArchivedTicketsLastIndexTime();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#resetIndexTimes()
	 */
	@Override
	public void resetIndexTimes() {
		setTicketsLastIndexTime(null);
		setFaqsLastIndexTime(null);
		setArchivedTicketsLastIndexTime(null);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setDefaultDepartmentIcon(
	 *      org.esupportail.helpdesk.domain.beans.Icon)
	 */
	@Override
	public void setDefaultDepartmentIcon(final Icon icon) {
		Config config = daoService.getConfig();
		config.setDefaultDepartmentIcon(icon);
		daoService.updateConfig(config);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setDefaultCategoryIcon(
	 *      org.esupportail.helpdesk.domain.beans.Icon)
	 */
	@Override
	public void setDefaultCategoryIcon(final Icon icon) {
		Config config = daoService.getConfig();
		config.setDefaultCategoryIcon(icon);
		daoService.updateConfig(config);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDefaultCategoryIcon()
	 */
	@Override
	public Icon getDefaultCategoryIcon() {
		return daoService.getConfig().getDefaultCategoryIcon();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDefaultDepartmentIcon()
	 */
	@Override
	public Icon getDefaultDepartmentIcon() {
		return daoService.getConfig().getDefaultDepartmentIcon();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getInstallDate()
	 */
	@Override
	public Timestamp getInstallDate() {
		return daoService.getConfig().getInstallDate();
	}

	/**
	 * Set the department selection context time (the last time that the context
	 * of the department selection has changed).
	 */
	protected void updateDepartmentSelectionContextTime() {
		Config config = daoService.getConfig();
		config.setDepartmentSelectionContextTime(new Timestamp(System.currentTimeMillis()));
		daoService.updateConfig(config);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentSelectionContextTime()
	 */
	@Override
	public Timestamp getDepartmentSelectionContextTime() {
		return daoService.getConfig().getDepartmentSelectionContextTime();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________DEPARTMENT_SELECTION_CONFIG() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDepartmentSelectionConfig()
	 */
	@Override
	public DepartmentSelectionConfig getDepartmentSelectionConfig() {
		return daoService.getLatestDepartmentSelectionConfig();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setDepartmentSelectionConfig(
	 *      org.esupportail.helpdesk.domain.beans.User, java.lang.String)
	 */
	@Override
	public void setDepartmentSelectionConfig(final User author, final String data) {
		DepartmentSelectionConfig config = new DepartmentSelectionConfig(author, data,
				new Timestamp(System.currentTimeMillis()));
		daoService.addDepartmentSelectionConfig(config);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________FAQ_LINK() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFaqLinks(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public List<FaqLink> getFaqLinks(final Department department) {
		return daoService.getFaqLinks(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFaqLinks(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public List<FaqLink> getFaqLinks(final Category category) {
		return daoService.getFaqLinks(category);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addFaqLink(
	 *      org.esupportail.helpdesk.domain.beans.FaqLink)
	 */
	@Override
	public void addFaqLink(final FaqLink faqLink) {
		if (faqLink.getDepartment() != null) {
			faqLink.setOrder(daoService.getFaqLinksNumber(faqLink.getDepartment()));
		} else {
			faqLink.setOrder(daoService.getFaqLinksNumber(faqLink.getCategory()));
		}
		daoService.addFaqLink(faqLink);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateFaqLink(
	 *      org.esupportail.helpdesk.domain.beans.FaqLink)
	 */
	@Override
	public void updateFaqLink(final FaqLink faqLink) {
		daoService.updateFaqLink(faqLink);
	}

	/**
	 * Reorder a list of faq links.
	 * 
	 * @param faqLinks
	 */
	protected void reorderFaqLinks(final List<FaqLink> faqLinks) {
		int i = 0;
		for (FaqLink faqLink : faqLinks) {
			if (faqLink.getOrder() != i) {
				faqLink.setOrder(i);
				daoService.updateFaqLink(faqLink);
			}
			i++;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteFaqLink(
	 *      org.esupportail.helpdesk.domain.beans.FaqLink)
	 */
	@Override
	public void deleteFaqLink(final FaqLink faqLink) {
		daoService.deleteFaqLink(faqLink);
		if (faqLink.getDepartment() != null) {
			reorderFaqLinks(daoService.getFaqLinks(faqLink.getDepartment()));
		} else {
			reorderFaqLinks(daoService.getFaqLinks(faqLink.getCategory()));
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveFaqLinkUp(
	 *      org.esupportail.helpdesk.domain.beans.FaqLink)
	 */
	@Override
	public void moveFaqLinkUp(final FaqLink faqLink) {
		FaqLink previousFaqLink = daoService.getFaqLinkByOrder(faqLink.getDepartment(), faqLink.getCategory(),
				faqLink.getOrder() - 1);
		if (previousFaqLink != null) {
			faqLink.setOrder(faqLink.getOrder() - 1);
			updateFaqLink(faqLink);
			previousFaqLink.setOrder(previousFaqLink.getOrder() + 1);
			updateFaqLink(previousFaqLink);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveFaqLinkDown(
	 *      org.esupportail.helpdesk.domain.beans.FaqLink)
	 */
	@Override
	public void moveFaqLinkDown(final FaqLink faqLink) {
		FaqLink nextFaqLink = daoService.getFaqLinkByOrder(faqLink.getDepartment(), faqLink.getCategory(),
				faqLink.getOrder() + 1);
		if (nextFaqLink != null) {
			faqLink.setOrder(faqLink.getOrder() + 1);
			updateFaqLink(faqLink);
			nextFaqLink.setOrder(nextFaqLink.getOrder() - 1);
			updateFaqLink(nextFaqLink);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveFaqLinkFirst(
	 *      org.esupportail.helpdesk.domain.beans.FaqLink)
	 */
	@Override
	public void moveFaqLinkFirst(final FaqLink faqLinkToMove) {
		List<FaqLink> faqLinks;
		if (faqLinkToMove.getDepartment() != null) {
			faqLinks = getFaqLinks(faqLinkToMove.getDepartment());
		} else {
			faqLinks = getFaqLinks(faqLinkToMove.getCategory());
		}
		for (FaqLink faqLink : faqLinks) {
			if (faqLink.getOrder() < faqLinkToMove.getOrder()) {
				faqLink.setOrder(faqLink.getOrder() + 1);
				updateFaqLink(faqLink);
			}
		}
		faqLinkToMove.setOrder(0);
		updateFaqLink(faqLinkToMove);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveFaqLinkLast(
	 *      org.esupportail.helpdesk.domain.beans.FaqLink)
	 */
	@Override
	public void moveFaqLinkLast(final FaqLink faqLinkToMove) {
		List<FaqLink> faqLinks;
		if (faqLinkToMove.getDepartment() != null) {
			faqLinks = getFaqLinks(faqLinkToMove.getDepartment());
		} else {
			faqLinks = getFaqLinks(faqLinkToMove.getCategory());
		}
		for (FaqLink faqLink : faqLinks) {
			if (faqLink.getOrder() > faqLinkToMove.getOrder()) {
				faqLink.setOrder(faqLink.getOrder() - 1);
				updateFaqLink(faqLink);
			}
		}
		faqLinkToMove.setOrder(faqLinks.size() - 1);
		updateFaqLink(faqLinkToMove);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getInheritedFaqLinks(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public List<FaqLink> getInheritedFaqLinks(final Category category) {
		if (category.getParent() != null) {
			return getEffectiveFaqLinks(category.getParent());
		}
		return getFaqLinks(category.getDepartment());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getEffectiveFaqLinks(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public List<FaqLink> getEffectiveFaqLinks(final Category category) {
		if (!category.getInheritFaqLinks()) {
			return getFaqLinks(category);
		}
		return getInheritedFaqLinks(category);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________AUTHORIZATIONS() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanViewAdmins(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean userCanViewAdmins(final User user) {
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		return isDepartmentManager(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanAddAdmin(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean userCanAddAdmin(final User user) {
		if (user == null) {
			return false;
		}
		return user.getAdmin();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanDeleteAdmin(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean userCanDeleteAdmin(final User user, final User admin) {
		if (user == null) {
			return false;
		}
		if (!user.getAdmin()) {
			return false;
		}
		return !user.equals(admin);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanViewDepartments(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean userCanViewDepartments(final User user) {
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		return isDepartmentManager(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanManageDepartments(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean userCanManageDepartments(final User user) {
		if (user == null) {
			return false;
		}
		return user.getAdmin();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanViewDepartment(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public boolean userCanViewDepartment(final User user, final Department department) {
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		return isDepartmentManager(department, user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanDeleteDepartment(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public boolean userCanDeleteDepartment(final User user, final Department department) {
		return userCanManageDepartments(user) && !hasVirtualDepartments(department) && !hasRootCategories(department)
				&& !hasRootFaqs(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanEditDepartmentProperties(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public boolean userCanEditDepartmentProperties(final User user, final Department department) {
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		try {
			DepartmentManager manager = getDepartmentManager(department, user);
			return manager.getManageProperties();
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanEditDepartmentManagers(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public boolean userCanEditDepartmentManagers(final User user, final Department department) {
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		try {
			DepartmentManager manager = getDepartmentManager(department, user);
			return manager.getManageManagers();
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanEditDepartmentCategories(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public boolean userCanEditDepartmentCategories(final User user, final Department department) {
		if (user == null) {
			return false;
		}
		try {
			DepartmentManager manager = getDepartmentManager(department, user);
			return manager.getManageCategories();
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanSetAvailability(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	@RequestCache
	public boolean userCanSetAvailability(final User user, final DepartmentManager departmentManager) {
		if (user == null) {
			return false;
		}
		if (departmentManager == null) {
			return false;
		}
		try {
			DepartmentManager manager = getDepartmentManager(departmentManager.getDepartment(), user);
			if (manager.getManageManagers()) {
				return true;
			}
			if (!user.equals(departmentManager.getUser())) {
				return false;
			}
			return manager.getSetOwnAvailability();
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanViewTicket(
	 *      org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanViewTicket(final User user, final InetAddress client, final Ticket ticket) {
		return userCanViewTicket(user, ticket, getTicketViewDepartments(user, client));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanViewTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.util.List)
	 */
	@Override
	@RequestCache
	public boolean userCanViewTicket(final User user, final Ticket ticket, final List<Department> visibleDepartments) {

		if (user == null || ticket == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		if (isDepartmentManager(ticket.getDepartment(), user)) {
			return true;
		}
		if (user.equals(ticket.getOwner())) {
			return true;
		}
		if (isInvited(user, ticket)) {
			return true;
		}
		// on vérifie si le ticket appartient à un service dont la visibilité
		// inter service serait définie au sein d'un de ses services ou il est
		// manager
		if (ticket.getDepartment().getVisibilityInterSrv() != null
				&& ticket.getDepartment().getVisibilityInterSrv() != "") {
			for (Department department : getManagedDepartments(user)) {
				if (department.getVisibilityInterSrv() != null) {
					if (department.getVisibilityInterSrv().equals(ticket.getDepartment().getVisibilityInterSrv())) {
						return true;
					}
				}
			}
		}
		// si le scope du ticket n'est pas PUBLIC et que l'utilisateur n'est pas
		// de type cas => false
		if (!TicketScope.PUBLIC.equals(ticket.getEffectiveScope())
				&& !(TicketScope.CAS.equals(ticket.getEffectiveScope()) && getUserStore().isCasUser(user))) {
			return false;
		}
		if (visibleDepartments.contains(ticket.getDepartment())) {
			return true;
		}

		return false;
	}

	/**
	 * @param user
	 * @param ticket
	 * @param invited
	 * @param objectScope
	 * @param objectUser
	 * @return true if the user can see an Action of a FileInfo.
	 */
	protected boolean userCanViewActionOrFileInfo(final User user, final Ticket ticket, final boolean invited,
			final String objectScope, final User objectUser) {

		if (user == null) {
			return false;
		}
		// visible si admin
		if (user.getAdmin()) {
			return true;
		}
		// visible le user est le créateur de l'action
		if (user.equals(objectUser)) {
			return true;
		}
		if (ActionScope.MANAGER.equals(objectScope)) {
			// visible si user est gestionnaire du service ou si visibilité
			// inter service
			if (!isDepartmentManager(ticket.getDepartment(), user)) {
				// on vérifie si le ticket appartient à un service dont la
				// visibilité inter service serait définie au sein d'un de ses
				// services ou il est manager
				if (ticket.getDepartment().getVisibilityInterSrv() != null
						&& ticket.getDepartment().getVisibilityInterSrv() != "") {
					for (Department department : getManagedDepartments(user)) {
						if (department.getVisibilityInterSrv() != null) {
							if (department.getVisibilityInterSrv()
									.equals(ticket.getDepartment().getVisibilityInterSrv())) {
								return true;
							}
						}
					}
				}
			} else {
				return true;
			}
		}
		if (ActionScope.OWNER.equals(objectScope)) {
			// visible si user est gestionnaire du service
			if (isDepartmentManager(ticket.getDepartment(), user)) {
				return true;
			}
			// visible si user est propriétaire du ticket
			if (user.equals(ticket.getOwner())) {
				return true;
			}
			return false;
		}
		if (ActionScope.INVITED_MANAGER.equals(objectScope)) {
			// visible si user est gestionnaire du service
			if (isDepartmentManager(ticket.getDepartment(), user)) {
				return true;
			}
			// visible si user est invité
			if (invited) {
				return true;
			}
			return false;
		}
		if (ActionScope.INVITED.equals(objectScope)) {
			// visible si user est gestionnaire du service
			if (isDepartmentManager(ticket.getDepartment(), user)) {
				return true;
			}
			// visible si user est invité
			if (invited) {
				return true;
			}
			// visible si user est propriétaire du ticket
			if (user.equals(ticket.getOwner())) {
				return true;
			}
			return false;
		}
		if (ActionScope.DEFAULT.equals(objectScope)) {

			// visible si user est gestionnaire du service ou si visibilité
			// inter service
			if (!isDepartmentManager(ticket.getDepartment(), user)) {
				// on vérifie si le ticket appartient à un service dont la
				// visibilité inter service serait définie au sein d'un de ses
				// services ou il est manager
				if (ticket.getDepartment().getVisibilityInterSrv() != null
						&& ticket.getDepartment().getVisibilityInterSrv() != "") {
					for (Department department : getManagedDepartments(user)) {
						if (department.getVisibilityInterSrv() != null) {
							if (department.getVisibilityInterSrv().equals(ticket.getDepartment().getVisibilityInterSrv())) {
								return true;
							}
						}
					}
				}
			} 
			else { 
				return true;
			}
			// visible si user est invité
			if (invited) {
				return true;
			}
			// visible si user est propriétaire du ticket
			if (user.equals(ticket.getOwner())) {
				return true;
			}
		}
		return TicketScope.PUBLIC.equals(ticket.getEffectiveScope());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanViewActionMessage(
	 *      org.esupportail.helpdesk.domain.beans.User, boolean,
	 *      org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	@RequestCache
	public boolean userCanViewActionMessage(final User user, final boolean invited, final Action action) {
		return userCanViewActionOrFileInfo(user, action.getTicket(), invited, action.getScope(), action.getUser());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanDownload(
	 *      org.esupportail.helpdesk.domain.beans.User, boolean,
	 *      org.esupportail.helpdesk.domain.beans.FileInfo)
	 */
	@Override
	@RequestCache
	public boolean userCanDownload(final User user, final boolean invited, final FileInfo fileInfo) {
		return userCanViewActionOrFileInfo(user, fileInfo.getTicket(), invited, fileInfo.getScope(),
				fileInfo.getUser());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#actionMonitorable(
	 *      org.esupportail.helpdesk.domain.beans.User, boolean,
	 *      org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	@RequestCache
	public boolean actionMonitorable(final User user, final boolean invited, final Action action) {
		if (ActionType.APPROVE_CLOSURE.equals(action.getActionType())
				|| ActionType.APPROVE_CLOSURE.equals(action.getActionType())
				|| ActionType.ASSIGN.equals(action.getActionType()) || ActionType.CANCEL.equals(action.getActionType())
				|| ActionType.CANCEL_POSTPONEMENT.equals(action.getActionType())
				|| ActionType.CHANGE_OWNER.equals(action.getActionType())
				|| ActionType.CLOSE.equals(action.getActionType())
				|| ActionType.CLOSE_APPROVE.equals(action.getActionType())
				|| ActionType.CREATE.equals(action.getActionType()) || ActionType.EXPIRE.equals(action.getActionType())
				|| ActionType.FREE.equals(action.getActionType()) || ActionType.POSTPONE.equals(action.getActionType())
				|| ActionType.REFUSE.equals(action.getActionType())
				|| ActionType.REFUSE_CLOSURE.equals(action.getActionType())
				|| ActionType.REOPEN.equals(action.getActionType())
				|| ActionType.REQUEST_INFORMATION.equals(action.getActionType())
				|| ActionType.TAKE.equals(action.getActionType())) {
			return true;
		}
		if (action.getUser() == null) {
			return false;
		}
		if (ActionType.GIVE_INFORMATION.equals(action.getActionType())) {
			return userCanViewActionMessage(user, invited, action);
		}
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanViewArchivedTicket(
	 *      org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress,
	 *      org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	@RequestCache
	public boolean userCanViewArchivedTicket(final User user, final InetAddress client,
			final ArchivedTicket archivedTicket) {
		return userCanViewArchivedTicket(user, archivedTicket, getTicketViewDepartments(user, client));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanViewArchivedTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.ArchivedTicket,
	 *      java.util.List)
	 */
	@Override
	@RequestCache
	public boolean userCanViewArchivedTicket(final User user, final ArchivedTicket archivedTicket,
			final List<Department> visibleDepartments) {
		if (user == null || archivedTicket == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		if (isDepartmentManager(archivedTicket.getDepartment(), user)) {
			return true;
		}
		if (user.equals(archivedTicket.getOwner())) {
			return true;
		}
		if (isInvited(user, archivedTicket)) {
			return true;
		}
		if (!TicketScope.PUBLIC.equals(archivedTicket.getEffectiveScope())) {
			return false;
		}
		if (visibleDepartments.contains(archivedTicket.getDepartment())) {
			return true;
		}
		return false;
	}

	/**
	 * @param user
	 * @param archivedTicket
	 * @param invited
	 * @param objectScope
	 * @return true if the user can see the archived Action or FileInfo
	 */
	@RequestCache
	protected boolean userCanViewArchivedActionOrFileInfo(final User user, final ArchivedTicket archivedTicket,
			final boolean invited, final String objectScope) {
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		if (isDepartmentManager(archivedTicket.getDepartment(), user)) {
			return true;
		}
		if (ActionScope.MANAGER.equals(objectScope)) {
			return false;
		}
		if (user.equals(archivedTicket.getOwner())) {
			return true;
		}
		if (ActionScope.OWNER.equals(objectScope)) {
			return false;
		}
		if (invited) {
			return true;
		}
		if (ActionScope.INVITED.equals(objectScope)) {
			return false;
		}
		return TicketScope.PUBLIC.equals(objectScope);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanViewArchivedAction(
	 *      org.esupportail.helpdesk.domain.beans.User, boolean,
	 *      org.esupportail.helpdesk.domain.beans.ArchivedAction)
	 */
	@Override
	@RequestCache
	public boolean userCanViewArchivedAction(final User user, final boolean invited,
			final ArchivedAction archivedAction) {
		return userCanViewArchivedActionOrFileInfo(user, archivedAction.getArchivedTicket(), invited,
				archivedAction.getEffectiveScope());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanDownload(
	 *      org.esupportail.helpdesk.domain.beans.User, boolean,
	 *      org.esupportail.helpdesk.domain.beans.ArchivedFileInfo)
	 */
	@Override
	@RequestCache
	public boolean userCanDownload(final User user, final boolean invited, final ArchivedFileInfo archivedFileInfo) {
		return userCanViewArchivedActionOrFileInfo(user, archivedFileInfo.getArchivedTicket(), invited,
				archivedFileInfo.getEffectiveScope());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanApproveClosure(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanApproveClosure(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return ticket.isWaitingForApproval() && user.equals(ticket.getOwner());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanRefuseClosure(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanRefuseClosure(final User user, final Ticket ticket) {
		return userCanApproveClosure(user, ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanGiveInformation(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanGiveInformation(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		if (isDepartmentManager(ticket.getDepartment(), user)) {
			return true;
		}
		if (isInvited(user, ticket)) {
			return true;
		}
		if (user.equals(ticket.getOwner())) {
			return true;
		}
		if (!ticket.isOpened()) {
			return false;
		}
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanCancel(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanCancel(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return ticket.isOpened() && user.equals(ticket.getOwner());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanRequestInformation(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanRequestInformation(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return ticket.isOpened() && user.equals(ticket.getManager());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanClose(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanClose(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return ticket.isOpened() && (user.equals(ticket.getOwner()) || user.equals(ticket.getManager()));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanRefuse(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanRefuse(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		if (!ticket.isOpened() || !user.equals(ticket.getManager())) {
			return false;
		}
		try {
			DepartmentManager manager = getDepartmentManager(ticket.getDepartment(), user);
			return manager.getRefuseTicket();
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanConnect(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanConnect(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return ticket.isOpened() && isDepartmentManager(ticket.getDepartment(), user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanPostpone(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanPostpone(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return ticket.isOpened() && !ticket.isPostponed() && user.equals(ticket.getManager());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanCancelPostponement(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanCancelPostponement(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return ticket.isPostponed() && user.equals(ticket.getManager());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanReopen(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanReopen(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		if (ticket.isOpened()) {
			return false;
		}
		if (!ticket.isWaitingForApproval() && user.equals(ticket.getOwner())) {
			return true;
		}
		DepartmentManager manager = null;
		try {
			manager = getDepartmentManager(ticket.getDepartment(), user);
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
		if (manager.equals(ticket.getManager())) {
			return true;
		}
		return manager.getReopenAllTickets();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanMove(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanMove(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		if (ticket.isOpened() && isDepartmentManager(ticket.getDepartment(), user)) {
			return true;
		}
		if (user.equals(ticket.getManager())) {
			return true;
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanMoveBackCategorie(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanMoveBack(final User user, final Ticket ticket) {

		Action lastAction = null;
		Action lastActionCategory = getLastActionByActionType(ticket, ActionType.CHANGE_CATEGORY);
		Action lastActionDepartment = getLastActionByActionType(ticket, ActionType.CHANGE_DEPARTMENT);
		// on prend l'action la plus récente s'il y en a
		if (lastActionCategory != null && lastActionDepartment != null) {
			if (lastActionCategory.getDate().after(lastActionDepartment.getDate())) {
				lastAction = lastActionCategory;
			} else {
				lastAction = lastActionDepartment;
			}
		} else {
			if (lastActionCategory != null) {
				lastAction = lastActionCategory;
			} else {
				lastAction = lastActionDepartment;
			}
		}

		if (user == null) {
			return false;
		}
		if (ticket.isOpened() && isDepartmentManager(ticket.getDepartment(), user) && lastAction != null
				&& lastAction.getCategoryBefore() != null) {
			return true;
		}
		if (user.equals(ticket.getManager()) && lastAction != null && lastAction.getCategoryBefore() != null) {
			return true;
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanTake(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanTake(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		if (!ticket.isOpened() || user.equals(ticket.getManager())) {
			return false;
		}
		DepartmentManager manager;
		try {
			manager = getDepartmentManager(ticket.getDepartment(), user);
			if (ticket.isFree()) {
				return manager.getTakeFreeTicket();
			}
			return manager.getTakeAlreadyAssignedTicket();
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanTakeAndClose(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanTakeAndClose(final User user, final Ticket ticket) {
		return userCanTake(user, ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanTakeAndRequestInformation(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanTakeAndRequestInformation(final User user, final Ticket ticket) {
		return userCanTake(user, ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanFree(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanFree(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return user.equals(ticket.getManager())
				&& (ticket.isIncomplete() || ticket.isInProgress() || ticket.isPostponed());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanAssign(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanAssign(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		if (!ticket.isOpened()) {
			return false;
		}
		Department department = ticket.getDepartment();
		DepartmentManager manager;
		try {
			manager = getDepartmentManager(department, user);
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
		if (!manager.getAssignTicket()) {
			return false;
		}
		for (DepartmentManager dm : getAvailableDepartmentManagers(department)) {
			if (!dm.getUser().equals(user)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanSetOwner(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public boolean userCanSetOwner(final User user, final Department department) {
		return isDepartmentManager(department, user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanChangeOwner(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanChangeOwner(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return ticket.isOpenAssigned() && user.equals(ticket.getManager());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanChangeLabel(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanChangeLabel(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		if(user.equals(ticket.getManager()) || user.equals(ticket.getOwner())) {
			return true;
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanChangeScope(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanChangeScope(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return user.equals(ticket.getManager()) || (ticket.isOpened() && user.equals(ticket.getOwner()));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanSetOrigin(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public boolean userCanSetOrigin(final User user, final Department department) {
		if (user == null) {
			return false;
		}
		return isDepartmentManager(department, user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanChangeOrigin(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanChangeOrigin(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return user.equals(ticket.getManager());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanChangeComputer(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanChangeComputer(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return user.equals(ticket.getManager());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanChangePriority(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanChangePriority(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return user.equals(ticket.getManager()) || (ticket.isOpened() && user.equals(ticket.getOwner()));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanChangeSpentTime(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanChangeSpentTime(final User user, final Ticket ticket) {
		if (user == null) {
			return false;
		}
		return user.equals(ticket.getManager());
	}

	/**
	 * @param user
	 * @param ticket
	 * @param objectScope
	 * @param objectOwner
	 * @return true if the user can change the scope of an Action or a FileInfo.
	 */
	@RequestCache
	public boolean userCanChangeActionOrFileInfoScope(final User user, final Ticket ticket, final String objectScope,
			final User objectOwner) {
		if (user == null) {
			logger.info("user null ");
			return false;
		}
		if (user.equals(ticket.getManager())) {
			logger.info("getManager ");
			return true;
		}
		if (!ticket.isOpened()) {
			logger.info("isOpened ");
			return false;
		}
		if (ActionScope.MANAGER.equals(objectScope)) {
			logger.info("MANAGER ");
			return false;
		}
		if (user.equals(ticket.getOwner()) && user.equals(objectOwner)) {
			logger.info("getOwner ");
			return true;
		}
		if (ActionScope.OWNER.equals(objectScope)) {
			logger.info("OWNER ");
			return false;
		}
		if (!isInvited(user, ticket)) {
			logger.info("isInvited ");
			return false;
		}
		logger.info("user.equals(objectOwner) " + user.equals(objectOwner));
		return user.equals(objectOwner);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanChangeActionScope(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	@RequestCache
	public boolean userCanChangeActionScope(final User user, final Action action) {
		logger.info("userCanChangeActionScope : scope " + action.getScope());
		return userCanChangeActionOrFileInfoScope(user, action.getTicket(), action.getScope(), action.getUser());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanChangeFileInfoScope(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.FileInfo)
	 */
	@Override
	@RequestCache
	public boolean userCanChangeFileInfoScope(final User user, final FileInfo fileInfo) {
		return userCanChangeActionOrFileInfoScope(user, fileInfo.getTicket(), fileInfo.getScope(), fileInfo.getUser());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanViewFaq(
	 *      org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress,
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	@RequestCache
	public boolean userCanViewFaq(final User user, final InetAddress client, final Faq faq) {
		return userCanViewFaq(user, faq, getFaqViewDepartments(user, client));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanViewFaq(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Faq, java.util.List)
	 */
	@Override
	@RequestCache
	public boolean userCanViewFaq(final User user, final Faq faq, final List<Department> visibleDepartments) {
		if (faq == null) {
			return false;
		}
		String effectiveScope = faq.getEffectiveScope();
		if (FaqScope.ALL.equals(effectiveScope)) {
			return true;
		}
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		if (FaqScope.AUTHENTICATED.equals(effectiveScope)) {
			return true;
		}
		Department department = faq.getDepartment();
		if (department == null) {
			return user.getAdmin();
		}
		if (FaqScope.DEPARTMENT.equals(effectiveScope)) {
			return visibleDepartments.contains(department);
		}
		if (FaqScope.MANAGER.equals(effectiveScope)) {
			return isDepartmentManager(department, user);
		}
		throw new IllegalArgumentException("unexpected FAQ scope [" + effectiveScope + "]");
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanEditFaqs(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean userCanEditFaqs(final User user) {
		if (user == null) {
			return false;
		}
		if (user.getAdmin()) {
			return true;
		}
		if (daoService.isFaqDepartmentManager(user)) {
			return true;
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanEditDepartmentFaqs(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public boolean userCanEditDepartmentFaqs(final User user, final Department department) {
		if (user == null) {
			return false;
		}
		if (department == null) {
			return user.getAdmin();
		}
		try {
			DepartmentManager departmentManager = getDepartmentManager(department, user);
			return departmentManager.getManageFaq();
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanEditFaq(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	@RequestCache
	public boolean userCanEditFaq(final User user, final Faq faq) {
		return userCanEditDepartmentFaqs(user, faq.getDepartment());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanEditRootFaqs(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean userCanEditRootFaqs(final User user) {
		return userCanEditDepartmentFaqs(user, null);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasVisibleFaq(
	 *      org.esupportail.helpdesk.domain.beans.User, java.net.InetAddress)
	 */
	@Override
	@RequestCache
	public boolean hasVisibleFaq(final User user, final InetAddress client) {
		return daoService.hasVisibleFaq(user, getFaqViewDepartments(user, client));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanDeleteFileInfo(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanDeleteFileInfo(final User user, final Ticket ticket) {
		Department department = ticket.getDepartment();
		if (isDepartmentManager(department, user)) {
			return true;
		}
		if (ticket.getOwner().equals(user)) {
			return true;
		}
		if (user.getAdmin()) {
			return true;
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanInvite(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanInvite(final User user, final Ticket ticket) {
		Department department = ticket.getDepartment();
		if (isDepartmentManager(department, user)) {
			return true;
		}
		if (ticket.getOwner().equals(user)) {
			return true;
		}
		if (user.getAdmin()) {
			return true;
		}
		//cas d'un gestionnaire d'un autre service mais qui a accès via la visibilité interservice
		if(department.getVisibilityInterSrv() != null) {
			for (DepartmentManager departmentManager : getDepartmentManagers(user)) {
				if(departmentManager.getDepartment().getVisibilityInterSrv().equals(department.getVisibilityInterSrv())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanInviteGroup(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanInviteGroup(final User user, final Ticket ticket) {
		Department department = ticket.getDepartment();
		if (isDepartmentManager(department, user)) {
			return true;
		}
		if (user.getAdmin()) {
			return true;
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanRemoveInvitations(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanRemoveInvitations(final User user, final Ticket ticket) {
		if (ticket.getOwner().equals(user)) {
			return true;
		}
		if (isDepartmentManager(ticket.getDepartment(), user)) {
			return true;
		}
		if (user.getAdmin()) {
			return true;
		}

		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanEditDepartmentSelection(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean userCanEditDepartmentSelection(final User user) {
		if (user == null) {
			return false;
		}
		return user.getAdmin();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanUseResponses(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public boolean userCanUseResponses(final User user, final Ticket ticket) {
		return isDepartmentManager(ticket.getDepartment(), user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanManageGlobalResponses(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean userCanManageGlobalResponses(final User user) {
		if (user == null) {
			return false;
		}
		return user.getAdmin();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanManageDepartmentResponses(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public boolean userCanManageDepartmentResponses(final User user, final Department department) {
		if (user == null) {
			return false;
		}
		if (department == null) {
			return false;
		}
		try {
			DepartmentManager departmentManager = getDepartmentManager(department, user);
			return departmentManager.getManageResponses();
		} catch (DepartmentManagerNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userCanManageIcons(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean userCanManageIcons(final User user) {
		if (user == null) {
			return false;
		}
		return user.getAdmin();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________TICKET_ACTIONS() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#userShowsTicketAfterClosure(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean userShowsTicketAfterClosure(final User user) {
		if (user == null) {
			return false;
		}
		if (!user.getShowTicketAfterClosure()) {
			return false;
		}
		return isDepartmentManager(user);
	}

	/**
	 * Add an action to a ticket.
	 * 
	 * @param ticket
	 * @param action
	 */
	protected void addActionToTicket(final Ticket ticket, final Action action) {

		ticket.setLastActionDate(action.getDate());
		ticket.setStatus(action.getStatusAfter());
		ticket.computeEffectiveDefaultTicketScope();
		ticket.updateTicketChargeTime(daoService.getActions(ticket, true));
		ticket.updateTicketClosureTime(action);
		updateTicket(ticket);
	}

	/**
	 * Set the spentTime after/before according its value.
	 * 
	 * @param action
	 *            The action to updates
	 * @param ticket
	 * @param spentTime
	 *            The spentTime to set.
	 */
	protected void addSpentTime(final Action action, final Ticket ticket, final long spentTime) {
		action.setSpentTimeBefore(ticket.getSpentTime());
		action.setSpentTimeAfter(spentTime);
	}

	/**
	 * Add an action.
	 * 
	 * @param actionOwner
	 * @param ticket
	 * @param actionMessage
	 * @param actionScope
	 * @return the action created
	 */
	protected Action giveInformation(final User actionOwner, final Ticket ticket, final String actionMessage,
			final String actionScope) {
		String statusAfter = ticket.getStatus();
		if (ticket.isIncomplete() && actionOwner != null) {
			if (actionOwner.equals(ticket.getOwner())) {
				statusAfter = TicketStatus.INPROGRESS;
			} else if (ticket.getManager() != null && actionOwner.equals(ticket.getManager())) {
				statusAfter = TicketStatus.INPROGRESS;
			}
		}
		Action newAction = new Action(actionOwner, ticket, ActionType.GIVE_INFORMATION, statusAfter, actionScope,
				actionMessage);
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		return newAction;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#giveInformation(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String, boolean)
	 */
	@Override
	public void giveInformation(final User author, final Ticket ticket, final String message, final String actionScope,
			final boolean alerts) {
		Action newAction = giveInformation(author, ticket, message, actionScope);
		if (alerts) {
			monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
		}
	}

	/**
	 * Add an action.
	 * 
	 * @param ticket
	 * @return the action created
	 */
	protected Action userInfo(final Ticket ticket) {
		String userInfo = getUserInfo(ticket.getOwner(), i18nService.getDefaultLocale());
		if (userInfo == null) {
			return null;
		}
		return giveInformation(null /* not a user */, ticket, userInfo, ActionScope.MANAGER);
	}

	/**
	 * Call the assignment algorithm of the category.
	 * 
	 * @param ticket
	 * @param excludedUser
	 * @param freeAllowed
	 * @param alerts
	 * @return the action created
	 */
	protected Action callAssignmentAlgorithm(final Ticket ticket, final User excludedUser, final boolean freeAllowed,
			final boolean alerts) {
		Category category = ticket.getCategory();
		String algorithmName = category.getEffectiveAssignmentAlgorithmName();
		User user = null;
		String nextAlgorithmState = null;
		String assignmentMessage = null;
		if (algorithmName != null) {
			AssignmentAlgorithm algorithm = assignmentAlgorithmStore.getAlgorithm(algorithmName);
			if (algorithm != null) {
				AssignmentResult result = algorithm.getAssignmentResult(this, ticket, excludedUser);
				if (result != null) {
					user = result.getUser();
					if (user != null) {
						if (user.equals(excludedUser)) {
							assignmentMessage = "<em>Assignment algorithm ("
									+ algorithm.getDescription(Locale.getDefault()) + ") returned [" + user.getRealId()
									+ "] (rejected)</em>";
							user = null;
						} else {
							assignmentMessage = "<em>Assignment algorithm ("
									+ algorithm.getDescription(Locale.getDefault()) + ") returned [" + user.getRealId()
									+ "]</em>";
						}
					}
					nextAlgorithmState = result.getNextAlgorithmState();
				} else {
					assignmentMessage = "<br /><em>Assignment algorithm ("
							+ algorithm.getDescription(Locale.getDefault()) + ") returned no assignment</em>";
				}
			} else {
				assignmentMessage = "<em>Assignment algorithm name: " + algorithmName + " (not found)</em>";
			}
		}
		Action action = null;
		if (user == null && ticket.getManager() != null && freeAllowed) {
			action = freeTicketInternal(null, ticket, assignmentMessage, ActionScope.MANAGER);
		} else if (user != null && !user.equals(ticket.getManager())) {
			action = assignTicketInternal(null, ticket, user, assignmentMessage, ActionScope.MANAGER);
		}
		if (action != null) {
			category.setAssignmentAlgorithmState(nextAlgorithmState);
			daoService.updateCategory(category);
			if (alerts) {
				monitoringSender.ticketMonitoringSendAlerts(null, action, false);
			}
		}
		return action;
	}

	/**
	 * Add an action.
	 * 
	 * @param ticket
	 * @param actionScope
	 * @param actionMessage
	 * @return the action created
	 */
	protected Action create(final Ticket ticket, final String actionScope, final String actionMessage) {
		String statusAfter = TicketStatus.FREE;
		Action newAction = new Action(ticket.getOwner(), ticket, ActionType.CREATE, statusAfter, actionScope,
				actionMessage);
		addAction(newAction);
		newAction.setCategoryAfter(ticket.getCategory());
		newAction.setDepartmentAfter(ticket.getDepartment());
		newAction.setTicketOwnerAfter(ticket.getOwner());
		ticket.setCreationDate(newAction.getDate());
		addActionToTicket(ticket, newAction);
		return newAction;
	}

	/**
	 * Add the permanently invited user to a ticket.
	 * 
	 * @param creationDepartment
	 * @param ticket
	 */
	protected void addDepartmentInvitedUsers(final Department creationDepartment, final Ticket ticket) {
		for (DepartmentInvitation departmentInvitation : getDepartmentInvitations(creationDepartment)) {
			invite(null, ticket, departmentInvitation.getUser(), null, ActionScope.DEFAULT, false);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addWebTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      org.esupportail.helpdesk.domain.beans.Category, java.lang.String,
	 *      java.lang.String, int, java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Ticket addWebTicket(final User author, final User owner, final Department creationDepartment,
			final Category category, final String label, final String computer, final int priorityLevel,
			final String message, final String ticketScope, final String ticketOrigin) {
		String origin = ticketOrigin;
		if (origin == null) {
			origin = getWebOrigin();
		}
		//suppression des blancs en début de champ
		StringUtils.stripStart(label, null);
		Ticket ticket = new Ticket(author, origin, category.getDepartment(), category, label, computer, priorityLevel,
				ticketScope);
		ticket.computeEffectiveDefaultTicketScope();
		daoService.addTicket(ticket);
		create(ticket, ActionScope.DEFAULT, message);
		if (owner != null && !author.equals(owner)) {
			changeTicketOwnerInternal(author, ticket, owner, null, ActionScope.DEFAULT);
		}
//		userInfo(ticket);
		callAssignmentAlgorithm(ticket, null, false, false);
		addDepartmentInvitedUsers(creationDepartment, ticket);
		addDepartmentInvitedUsers(ticket.getDepartment(), ticket);
		return ticket;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addEmailTicket(
	 *      org.esupportail.helpdesk.domain.beans.User, java.lang.String,
	 *      org.esupportail.helpdesk.domain.beans.Department,
	 *      org.esupportail.helpdesk.domain.beans.Category, java.lang.String)
	 */
	@Override
	public Ticket addEmailTicket(final User sender, final String address, final Department creationDepartment,
			final Category category, final String label) {
		//suppression des blancs en début de champ
		StringUtils.stripStart(label, null);
		Ticket ticket = new Ticket(sender, emailOrigin, category.getDepartment(), category, label, null,
				category.getEffectiveDefaultTicketPriority(), TicketScope.PRIVATE);
		ticket.computeEffectiveDefaultTicketScope();
		daoService.addTicket(ticket);
		create(ticket, ActionScope.DEFAULT, i18nService.getString("TICKET_ACTION.EMAIL_FEED.CREATE", address));
		userInfo(ticket);
		if (category.isVirtual()) {
			changeCategory(null, ticket, null, category.getRealCategory(), ActionScope.DEFAULT);
		}
		callAssignmentAlgorithm(ticket, null, false, false);
		addDepartmentInvitedUsers(creationDepartment, ticket);
		addDepartmentInvitedUsers(ticket.getDepartment(), ticket);
		return ticket;
	}

	/**
	 * Add an action.
	 * 
	 * @param actionOwner
	 * @param ticket
	 * @param actionMessage
	 * @param actionScope
	 * @return the action created
	 */
	protected Action takeTicketInternal(final User actionOwner, final Ticket ticket, final String actionMessage,
			final String actionScope) {
		String statusAfter;
		if (ticket.isFree()) {
			statusAfter = TicketStatus.INPROGRESS;
		} else {
			statusAfter = ticket.getStatus();
		}
		Action newAction = new Action(actionOwner, ticket, ActionType.TAKE, statusAfter, actionScope, actionMessage);
		newAction.setManagerBefore(ticket.getManager());
		newAction.setManagerAfter(actionOwner);
		addAction(newAction);
		ticket.setManager(newAction.getManagerAfter());
		addActionToTicket(ticket, newAction);
		return newAction;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#takeTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void takeTicket(final User author, final Ticket ticket, final String message, final String actionScope) {
		Action action = takeTicketInternal(author, ticket, message, actionScope);
		monitoringSender.ticketMonitoringSendAlerts(author, action, false);
	}

	/**
	 * @param author
	 * @param ticket
	 * @param manager
	 * @param message
	 * @param actionScope
	 * @return the action created
	 */
	protected Action assignTicketInternal(final User author, final Ticket ticket, final User manager,
			final String message, final String actionScope) {
		String statusAfter;
		if (ticket.isFree()) {
			statusAfter = TicketStatus.INPROGRESS;
		} else {
			statusAfter = ticket.getStatus();
		}
		Action newAction = new Action(author, ticket, ActionType.ASSIGN, statusAfter, actionScope, message);
		newAction.setManagerBefore(ticket.getManager());
		newAction.setManagerAfter(manager);
		addAction(newAction);
		ticket.setManager(newAction.getManagerAfter());
		addActionToTicket(ticket, newAction);
		return newAction;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#assignTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket,
	 *      org.esupportail.helpdesk.domain.beans.User, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void assignTicket(final User author, final Ticket ticket, final User manager, final String message,
			final String actionScope) {
		Action action = assignTicketInternal(author, ticket, manager, message, actionScope);
		monitoringSender.ticketMonitoringSendAlerts(author, action, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#changeTicketLabel(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      boolean)
	 */
	@Override
	public void changeTicketLabel(final User author, final Ticket ticket, final String label, final boolean alerts) {
		Action newAction = new Action(author, ticket, ActionType.CHANGE_LABEL, ticket.getStatus(), ActionScope.DEFAULT,
				null);

		newAction.setLabelBefore(ticket.getLabel());
		newAction.setLabelAfter(label);
		addAction(newAction);
		ticket.setLabel(newAction.getLabelAfter());
		addActionToTicket(ticket, newAction);
		if (alerts) {
			monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
		}
	}

	/**
	 * Change the owner of a ticket.
	 * 
	 * @param author
	 * @param ticket
	 * @param owner
	 * @param message
	 * @param actionScope
	 * @return the action created.
	 */
	protected Action changeTicketOwnerInternal(final User author, final Ticket ticket, final User owner,
			final String message, final String actionScope) {
		Action newAction = new Action(author, ticket, ActionType.CHANGE_OWNER, ticket.getStatus(), actionScope,
				message);
		newAction.setTicketOwnerBefore(ticket.getOwner());
		newAction.setTicketOwnerAfter(owner);
		addAction(newAction);
		ticket.setOwner(owner);
		addActionToTicket(ticket, newAction);
		return newAction;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#changeTicketOwner(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket,
	 *      org.esupportail.helpdesk.domain.beans.User, java.lang.String,
	 *      java.lang.String, boolean)
	 */
	@Override
	public void changeTicketOwner(final User author, final Ticket ticket, final User owner, final String message,
			final String actionScope, final boolean alerts) {
		Action action1 = changeTicketOwnerInternal(author, ticket, owner, message, actionScope);
//		Action action2 = userInfo(ticket);
		if (alerts) {
//			monitoringSender.ticketMonitoringSendAlerts(author, action1, action2, false);
			monitoringSender.ticketMonitoringSendAlerts(author, action1, false);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#changeTicketPriority(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, int, boolean)
	 */
	@Override
	public void changeTicketPriority(final User author, final Ticket ticket, final int ticketPriority,
			final boolean alerts) {
		Action newAction = new Action(author, ticket, ActionType.CHANGE_PRIORITY, ticket.getStatus(),
				ActionScope.DEFAULT, null);
		newAction.setPriorityLevelBefore(ticket.getPriorityLevel());
		newAction.setPriorityLevelAfter(ticketPriority);
		addAction(newAction);
		ticket.setPriorityLevel(ticketPriority);
		addActionToTicket(ticket, newAction);
		if (alerts) {
			monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#changeTicketScope(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      boolean)
	 */
	@Override
	public void changeTicketScope(final User author, final Ticket ticket, final String ticketScope,
			final boolean alerts) {
		Action newAction = new Action(author, ticket, ActionType.CHANGE_SCOPE, ticket.getStatus(), ActionScope.DEFAULT,
				null);
		newAction.setScopeBefore(ticket.getScope());
		newAction.setScopeAfter(ticketScope);
		addAction(newAction);
		ticket.setScope(ticketScope);
		addActionToTicket(ticket, newAction);
		if (alerts) {
			monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#changeTicketOrigin(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      boolean)
	 */
	@Override
	public void changeTicketOrigin(final User author, final Ticket ticket, final String ticketOrigin,
			final boolean alerts) {
		Action newAction = new Action(author, ticket, ActionType.CHANGE_ORIGIN, ticket.getStatus(), ActionScope.DEFAULT,
				null);
		newAction.setOriginBefore(ticket.getOrigin());
		newAction.setOriginAfter(ticketOrigin);
		addAction(newAction);
		ticket.setOrigin(ticketOrigin);
		addActionToTicket(ticket, newAction);
		if (alerts) {
			monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#changeTicketComputer(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      boolean)
	 */
	@Override
	public void changeTicketComputer(final User author, final Ticket ticket, final String ticketComputer,
			final boolean alerts) {
		Action newAction = new Action(author, ticket, ActionType.CHANGE_COMPUTER, ticket.getStatus(),
				ActionScope.DEFAULT, null);
		newAction.setComputerBefore(ticket.getComputer());
		newAction.setComputerAfter(ticketComputer);
		addAction(newAction);
		ticket.setComputer(ticketComputer);
		addActionToTicket(ticket, newAction);
		if (alerts) {
			monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#changeTicketSpentTime(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, long, boolean)
	 */
	@Override
	public void changeTicketSpentTime(final User author, final Ticket ticket, final long ticketSpentTime,
			final boolean alerts) {
		Action newAction = new Action(author, ticket, ActionType.CHANGE_SPENT_TIME, ticket.getStatus(),
				ActionScope.DEFAULT, null);
		addSpentTime(newAction, ticket, ticketSpentTime);
		addAction(newAction);
		ticket.setSpentTime(ticketSpentTime);
		addActionToTicket(ticket, newAction);
		if (alerts) {
			monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
		}
	}

	/**
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 * @return the action created.
	 */
	protected Action freeTicketInternal(final User author, final Ticket ticket, final String message,
			final String actionScope) {
		String statusAfter = ticket.getStatus();
		if (ticket.isInProgress()) {
			statusAfter = TicketStatus.FREE;
		}
		Action newAction = new Action(author, ticket, ActionType.FREE, statusAfter, actionScope, message);
		newAction.setManagerBefore(ticket.getManager());
		newAction.setManagerAfter(null);
		addAction(newAction);
		ticket.setManager(null);
		addActionToTicket(ticket, newAction);
		return newAction;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#freeTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void freeTicket(final User author, final Ticket ticket, final String message, final String actionScope) {
		Action newAction = freeTicketInternal(author, ticket, message, actionScope);
		monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#cancelTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void cancelTicket(final User author, final Ticket ticket, final String message, final String actionScope) {
		Action newAction = new Action(author, ticket, ActionType.CANCEL, TicketStatus.CANCELLED, actionScope, message);
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
	}

	/**
	 * Request information for a ticket.
	 * 
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 * @return the newly created action
	 */
	protected Action requestTicketInformationInternal(final User author, final Ticket ticket, final String message,
			final String actionScope) {
		Action newAction = new Action(author, ticket, ActionType.REQUEST_INFORMATION, TicketStatus.INCOMPLETE,
				actionScope, message);
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		return newAction;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#requestTicketInformation(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void requestTicketInformation(final User author, final Ticket ticket, final String message,
			final String actionScope) {
		Action newAction = requestTicketInformationInternal(author, ticket, message, actionScope);
		monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#takeAndRequestTicketInformation(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void takeAndRequestTicketInformation(final User author, final Ticket ticket, final String message,
			final String actionScope) {
		Action action1 = takeTicketInternal(author, ticket, null, actionScope);
		Action action2 = requestTicketInformationInternal(author, ticket, message, actionScope);
		monitoringSender.ticketMonitoringSendAlerts(author, action1, action2, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#postponeTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String, java.sql.Timestamp)
	 */
	@Override
	public void postponeTicket(final User author, final Ticket ticket, final String message, final String actionScope,
			final Timestamp recallDate) {
		Action newAction = new Action(author, ticket, ActionType.POSTPONE, TicketStatus.POSTPONED, actionScope,
				message);
		newAction.setRecallDate(recallDate);
		addAction(newAction);
		ticket.setRecallDate(recallDate);
		addActionToTicket(ticket, newAction);
		monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#cancelTicketPostponement(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void cancelTicketPostponement(final User author, final Ticket ticket, final String message,
			final String actionScope) {
		Action newAction = new Action(author, ticket, ActionType.CANCEL_POSTPONEMENT, TicketStatus.INPROGRESS,
				actionScope, message);
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
	}

	/**
	 * @param actionOwner
	 * @param ticket
	 * @param actionMessage
	 * @param actionScope
	 * @return the action created
	 */
	protected Action closeTicketInternal(final User actionOwner, final Ticket ticket, final String actionMessage,
			final String actionScope) {
		String newTicketStatus;
		String actionType;
		if (actionOwner == null || ticket.getOwner().equals(actionOwner)) {
			newTicketStatus = TicketStatus.APPROVED;
			actionType = ActionType.CLOSE_APPROVE;
		} else {
			newTicketStatus = TicketStatus.CLOSED;
			actionType = ActionType.CLOSE;
		}
		Action newAction = new Action(actionOwner, ticket, actionType, newTicketStatus, actionScope, actionMessage);
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		return newAction;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#closeTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String, boolean)
	 */
	@Override
	public void closeTicket(final User author, final Ticket ticket, final String message, final String actionScope,
			final boolean freeTicketAfterClosure) {
		Action action1 = closeTicketInternal(author, ticket, message, actionScope);
		Action action2 = null;
		if (freeTicketAfterClosure) {
			action2 = freeTicketInternal(author, ticket, null, ActionScope.DEFAULT);
		}
		monitoringSender.ticketMonitoringSendAlerts(author, action1, action2, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#takeAndCloseTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String, boolean)
	 */
	@Override
	public void takeAndCloseTicket(final User author, final Ticket ticket, final String message,
			final String actionScope, final boolean freeTicketAfterClosure) {
		Action action1 = takeTicketInternal(author, ticket, null, actionScope);
		Action action2 = closeTicketInternal(author, ticket, message, actionScope);
		Action action3 = null;
		if (freeTicketAfterClosure) {
			action3 = freeTicketInternal(author, ticket, null, ActionScope.DEFAULT);
		}
		monitoringSender.ticketMonitoringSendAlerts(author, action1, action2, action3, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#refuseTicketClosure(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void refuseTicketClosure(final User author, final Ticket ticket, final String message,
			final String actionScope) {
		Action newAction = new Action(author, ticket, ActionType.REFUSE_CLOSURE, TicketStatus.INPROGRESS, actionScope,
				message);
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#approveTicketClosure(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void approveTicketClosure(final User author, final Ticket ticket, final String message,
			final String actionScope) {
		Action newAction = new Action(author, ticket, ActionType.APPROVE_CLOSURE, TicketStatus.APPROVED, actionScope,
				message);
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#expireTicket(
	 *      org.esupportail.helpdesk.domain.beans.Ticket, boolean)
	 */
	@Override
	public void expireTicket(final Ticket ticket, final boolean alerts) {
		Action newAction = new Action(null, ticket, ActionType.EXPIRE, TicketStatus.EXPIRED, ActionScope.DEFAULT, "");
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		if (alerts) {
			monitoringSender.ticketMonitoringSendAlerts(null, newAction, true);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#connectTicketToTicket(
	 *      org.esupportail.helpdesk.domain.beans.Ticket,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public void connectTicketToTicket(final Ticket ticket, final Ticket targetTicket) {
		ticket.setConnectionArchivedTicket(null);
		ticket.setConnectionTicket(targetTicket);
		updateTicket(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#connectTicketToArchivedTicket(
	 *      org.esupportail.helpdesk.domain.beans.Ticket,
	 *      org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	public void connectTicketToArchivedTicket(final Ticket ticket, final ArchivedTicket targetArchivedTicket) {
		ticket.setConnectionTicket(null);
		ticket.setConnectionArchivedTicket(targetArchivedTicket);
		updateTicket(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#connectTicketToFaq(
	 *      org.esupportail.helpdesk.domain.beans.Ticket,
	 *      org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void connectTicketToFaq(final Ticket ticket, final Faq targetFaq) {
		ticket.setConnectionFaq(targetFaq);
		updateTicket(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#refuseTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void refuseTicket(final User author, final Ticket ticket, final String message, final String actionScope) {
		Action newAction = new Action(author, ticket, ActionType.REFUSE, TicketStatus.REFUSED, actionScope, message);
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		monitoringSender.ticketMonitoringSendAlerts(author, newAction, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#reopenTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void reopenTicket(final User author, final Ticket ticket, final String message, final String actionScope) {
		Action freeAction = null;
		if (ticket.getManager() != null) {
			try {
				getDepartmentManager(ticket.getDepartment(), ticket.getManager());
			} catch (DepartmentManagerNotFoundException e) {
				freeAction = freeTicketInternal(null, ticket, null, ActionScope.DEFAULT);
				addAction(freeAction);
				addActionToTicket(ticket, freeAction);
			}
		}
		String status;
		if (ticket.getManager() == null) {
			status = TicketStatus.FREE;
		} else {
			status = TicketStatus.INPROGRESS;
		}
		Action reopenAction = new Action(author, ticket, ActionType.REOPEN, status, actionScope, message);
		addAction(reopenAction);
		addActionToTicket(ticket, reopenAction);
		monitoringSender.ticketMonitoringSendAlerts(author, reopenAction, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#invite(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket,
	 *      org.esupportail.helpdesk.domain.beans.User, java.lang.String,
	 *      java.lang.String, boolean)
	 */
	@Override
	public boolean invite(final User actionOwner, final Ticket ticket, User invitedUser, final String actionMessage,
			final String actionScope, final boolean alert) {
		if (isInvited(invitedUser, ticket)) {
			return true;
		}
		Action newAction = new Action(actionOwner, ticket, ActionType.INVITE, ticket.getStatus(), actionScope,
				actionMessage);
    	if(tryConvertMaillToCasUser) {
   			if (invitedUser.getDisplayName().contains("@")) {
   				invitedUser = userStore.getUserWithEmail(invitedUser.getDisplayName());
   			}
		}
		newAction.setInvitedUser(invitedUser);
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		daoService.addInvitation(new Invitation(invitedUser, ticket));
		if (alert && !(invitedUser.equals(actionOwner))) {
			return invitationSender.sendInvitationEmail(actionOwner, invitedUser, ticket);
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#removeInvitation(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Invitation, boolean)
	 */
	@Override
	public void removeInvitation(final User actionOwner, final Invitation invitation, final boolean alert) {
		Ticket ticket = invitation.getTicket();
		if (!isInvited(invitation.getUser(), ticket)) {
			return;
		}
		daoService.deleteInvitation(invitation);
		Action newAction = new Action(actionOwner, ticket, ActionType.REMOVE_INVITATION, ticket.getStatus(),
				ActionScope.DEFAULT, null);
		newAction.setInvitedUser(invitation.getUser());
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		if (alert) {
			ticketMonitoringSendAlerts(actionOwner, ticket, null, false);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#uploadFile(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket, java.lang.String,
	 *      byte[], java.lang.String)
	 */
	@Override
	public void uploadFile(final User author, final Ticket ticket, final String filename, final byte[] content,
			final String actionScope) {
		FileInfo fileInfo = new FileInfo(filename, content, ticket, author, actionScope);
		addFileInfo(fileInfo);
		Action action = new Action(author, ticket, ActionType.UPLOAD, ticket.getStatus(), actionScope, null);
		action.setFilename(fileInfo.getFilename());
		addAction(action);
		addActionToTicket(ticket, action);
	}

	/**
	 * @param actionOwner
	 * @param ticket
	 * @param actionMessage
	 * @param newCategory
	 * @param actionScope
	 * @return the action created
	 */
	protected Action changeCategory(final User actionOwner, final Ticket ticket, final String actionMessage,
			final Category newCategory, final String actionScope) {
		Action newAction = new Action(actionOwner, ticket, ActionType.CHANGE_CATEGORY, ticket.getStatus(), actionScope,
				actionMessage);
		newAction.setCategoryBefore(ticket.getCategory());
		newAction.setCategoryAfter(newCategory);
		addAction(newAction);
		ticket.setCategory(newCategory);
		addActionToTicket(ticket, newAction);
		return newAction;
	}

	/**
	 * @param actionOwner
	 * @param ticket
	 * @param actionMessage
	 * @param newDepartment
	 * @param actionScope
	 * @param newCategory
	 * @return the action created
	 */
	protected Action changeDepartment(final User actionOwner, final Ticket ticket, final String actionMessage,
			final Department newDepartment, final String actionScope, final Category newCategory) {
		Action newAction = new Action(actionOwner, ticket, ActionType.CHANGE_DEPARTMENT, ticket.getStatus(),
				actionScope, actionMessage);
		newAction.setDepartmentBefore(ticket.getDepartment());
		newAction.setDepartmentAfter(newDepartment);
		newAction.setCategoryBefore(ticket.getCategory());
		newAction.setCategoryAfter(newCategory);
		addAction(newAction);
		ticket.setCategory(newCategory);
		ticket.setDepartment(newDepartment);
		addActionToTicket(ticket, newAction);
		return newAction;
	}

	/**
	 * @param actionOwner
	 * @param ticket
	 * @param actionMessage
	 * @param newDepartment
	 * @param actionScope
	 * @param newCategory
	 * @return the action created
	 */
	protected Action deleteFileInfo(final User actionOwner, final Ticket ticket, final String actionMessage,
			final String actionScope) {
		Action newAction = new Action(actionOwner, ticket, ActionType.DELETE_FILE_INFO, ticket.getStatus(), actionScope,
				actionMessage);
		addAction(newAction);
		addActionToTicket(ticket, newAction);
		return newAction;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveTicket(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket,
	 *      org.esupportail.helpdesk.domain.beans.Category, java.lang.String,
	 *      java.lang.String, boolean, boolean, boolean, boolean)
	 */
	@Override
	public void moveTicket(final User author, final Ticket ticket, final Category targetCategory, final String message,
			final String actionScope, final boolean alerts, final boolean free, final boolean monitor,
			final boolean invite) {
		Department targetDepartment = targetCategory.getDepartment();
		if (monitor) {
			setTicketMonitoring(author, ticket);
		}
		if (invite) {
			invite(author, ticket, author, null, ActionScope.DEFAULT, alerts);
		}
		Action action1 = null;
		if (ticket.getDepartment().equals(targetDepartment)) {
			action1 = changeCategory(author, ticket, message, targetCategory, actionScope);
		} else {
			action1 = changeDepartment(author, ticket, message, targetDepartment, actionScope, targetCategory);
		}
		User oldManager = ticket.getManager();
		Action action2 = null;
		if (free || (oldManager != null && !isDepartmentManager(targetDepartment, oldManager))) {
			action2 = freeTicketInternal(null, ticket, null, ActionScope.DEFAULT);
		}
		Action action3 = null;
		if (ticket.getManager() == null) {
			action3 = callAssignmentAlgorithm(ticket, null, false, false);
		}
		if (alerts) {
			monitoringSender.ticketMonitoringSendAlerts(author, action1, action2, action3, false);
		}
		if (!ticket.getDepartment().equals(targetDepartment)) {
			addDepartmentInvitedUsers(ticket.getDepartment(), ticket);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#moveTicket(
	 *      org.esupportail.helpdesk.domain.beans.Ticket,
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void moveTicket(final Ticket ticket, final Category targetCategory) {
		moveTicket(null, ticket, targetCategory, null, ActionScope.DEFAULT, false, false, false, false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#recallPostponedTickets()
	 */
	@Override
	public int recallPostponedTickets() {
		List<Ticket> ticketsToRecall = daoService.getTicketsToRecall();
		if (ticketsToRecall.isEmpty()) {
			logger.info("no ticket to recall");
		} else {
			for (Ticket ticket : ticketsToRecall) {
				cancelTicketPostponement(null, ticket, null, ActionScope.DEFAULT);
				logger.info("recalled ticket #" + ticket.getId());
			}
		}
		return ticketsToRecall.size();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________EMAIL() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#sendTicketReports()
	 */
	@Override
	public void sendTicketReports() {
		ticketReporter.sendTicketReports();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#sendTicketReport(
	 *      org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public void sendTicketReport(final DepartmentManager manager) {
		ticketReporter.sendTicketReport(manager);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#sendFaqReports()
	 */
	@Override
	public void sendFaqReports() {
		faqReporter.sendFaqReports();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________PRINT() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketPrintContent(
	 *      org.esupportail.helpdesk.domain.beans.User,
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public String getTicketPrintContent(final User user, final Ticket ticket) {
		return ticketPrinter.getTicketPrintContent(user, ticket);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________DEPRECATED() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getInheritingMembersCategories()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<Category> getInheritingMembersCategories() {
		return daoService.getInheritingMembersCategories();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getOldTicketTemplates(
	 *      org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<OldTicketTemplate> getOldTicketTemplates(final Category category) {
		return this.daoService.getOldTicketTemplates(category);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteOldTicketTemplate(
	 *      org.esupportail.helpdesk.domain.beans.OldTicketTemplate)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteOldTicketTemplate(final OldTicketTemplate oldTicketTemplate) {
		this.daoService.deleteOldTicketTemplate(oldTicketTemplate);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getOldFaqParts(
	 *      org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<OldFaqPart> getOldFaqParts(final DeprecatedFaqContainer faqContainer) {
		return daoService.getOldFaqParts(faqContainer);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteOldFaqPart(
	 *      org.esupportail.helpdesk.domain.beans.OldFaqPart)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteOldFaqPart(final OldFaqPart oldFaqPart) {
		daoService.deleteOldFaqPart(oldFaqPart);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getOldFaqEntries(
	 *      org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<OldFaqEntry> getOldFaqEntries(final DeprecatedFaqContainer faqContainer) {
		return daoService.getOldFaqEntries(faqContainer);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getOldFaqEntries(
	 *      org.esupportail.helpdesk.domain.beans.OldFaqPart)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<OldFaqEntry> getOldFaqEntries(final OldFaqPart oldFaqPart) {
		return daoService.getOldFaqEntries(oldFaqPart);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteOldFaqEntry(
	 *      org.esupportail.helpdesk.domain.beans.OldFaqEntry)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteOldFaqEntry(final OldFaqEntry oldFaqEntry) {
		daoService.deleteOldFaqEntry(oldFaqEntry);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketsConnectedToOldFaqPart(
	 *      org.esupportail.helpdesk.domain.beans.OldFaqPart)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<Ticket> getTicketsConnectedToOldFaqPart(final OldFaqPart oldFaqPart) {
		return this.daoService.getTicketsConnectedToOldFaqPart(oldFaqPart);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getTicketsConnectedToOldFaqEntry(
	 *      org.esupportail.helpdesk.domain.beans.OldFaqEntry)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<Ticket> getTicketsConnectedToOldFaqEntry(final OldFaqEntry oldFaqEntry) {
		return this.daoService.getTicketsConnectedToOldFaqEntry(oldFaqEntry);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getOrphenTickets(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<Ticket> getOrphenTickets(final Department department) {
		return daoService.getOrphenTickets(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#hasOrphenTickets(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public boolean hasOrphenTickets(final Department department) {
		return daoService.getOrphenTicketsNumber(department) != 0;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getActionsConnectedToOldFaqEntry(
	 *      org.esupportail.helpdesk.domain.beans.OldFaqEntry)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<Action> getActionsConnectedToOldFaqEntry(final OldFaqEntry oldFaqEntry) {
		return daoService.getActionsConnectedToOldFaqEntry(oldFaqEntry);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getActionsConnectedToOldFaqPart(
	 *      org.esupportail.helpdesk.domain.beans.OldFaqPart)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<Action> getActionsConnectedToOldFaqPart(final OldFaqPart oldFaqPart) {
		return daoService.getActionsConnectedToOldFaqPart(oldFaqPart);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getV2ActionsToUpgradeToV3(long,
	 *      int)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<Action> getV2ActionsToUpgradeToV3(final long startIndex, final int num) {
		return daoService.getV2ActionsToUpgradeToV3(startIndex, num);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getV2Invitations()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<Action> getV2Invitations() {
		return daoService.getV2Invitations();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#migrateV2Invitation(
	 *      org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	@Deprecated
	public void migrateV2Invitation(final Action action) {
		Ticket ticket = action.getTicket();
		User owner = ticket.getOwner();
		if (owner.equals(action.getUser()) || isDepartmentManager(ticket.getDepartment(), action.getUser())) {
			User invitedUser = action.getInvitedUser();
			if (!isInvited(invitedUser, ticket)) {
				daoService.addInvitation(new Invitation(invitedUser, ticket));
				action.setActionType(ActionType.INVITE);
				daoService.updateAction(action);
			} else {
				daoService.deleteAction(action);
			}
		} else {
			daoService.deleteAction(action);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getOldFileInfoContent(
	 *      org.esupportail.helpdesk.domain.beans.OldFileInfo)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public byte[] getOldFileInfoContent(final OldFileInfo oldFileInfo) {
		return daoService.getOldFileInfoContent(oldFileInfo);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteOldFileInfo(
	 *      org.esupportail.helpdesk.domain.beans.OldFileInfo)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteOldFileInfo(final OldFileInfo oldFileInfo) {
		daoService.deleteOldFileInfo(oldFileInfo);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getActionsWithAttachedFile(int)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<Action> getActionsWithAttachedFile(final int maxResults) {
		return daoService.getV2ActionsWithAttachedFile(maxResults);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getV2ArchivedInvitations()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<ArchivedAction> getV2ArchivedInvitations() {
		return daoService.getV2ArchivedInvitations();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#migrateV2ArchivedInvitation(
	 *      org.esupportail.helpdesk.domain.beans.ArchivedAction)
	 */
	@Override
	@Deprecated
	public void migrateV2ArchivedInvitation(final ArchivedAction archivedAction) {
		ArchivedTicket archivedTicket = archivedAction.getArchivedTicket();
		User owner = archivedTicket.getOwner();
		if (owner.equals(archivedAction.getUser())
				|| isDepartmentManager(archivedTicket.getDepartment(), archivedAction.getUser())) {
			User invitedUser = archivedAction.getInvitedUser();
			if (!isInvited(invitedUser, archivedTicket)) {
				daoService.addArchivedInvitation(new ArchivedInvitation(invitedUser, archivedTicket));
				archivedAction.setActionType(ActionType.INVITE);
				daoService.updateArchivedAction(archivedAction);
			} else {
				daoService.deleteArchivedAction(archivedAction);
			}
		} else {
			daoService.deleteArchivedAction(archivedAction);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#upgradeTicketTo3d4d0(
	 *      org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@Deprecated
	public void upgradeTicketTo3d4d0(final Ticket ticket) {
		ticket.setChargeTime(null);
		ticket.setClosureTime(null);
		List<Action> actions = daoService.getActions(ticket, true);
		ticket.updateTicketChargeTime(actions);
		for (Action action : actions) {
			ticket.updateTicketClosureTime(action);
		}
		if (ticket.getCreationDepartment() == null) {
			for (Action action : actions) {
				if (ActionType.CHANGE_DEPARTMENT.equals(action.getActionType())) {
					ticket.setCreationDepartment(action.getDepartmentBefore());
					break;
				}
			}
		}
		if (ticket.getCreationDepartment() == null) {
			ticket.setCreationDepartment(ticket.getDepartment());
		}
		ticket.setCreationDate(ticket.getCreationDate());
		daoService.updateTicket(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getLastArchivedTicketId()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public long getLastArchivedTicketId() {
		return daoService.getLastArchivedTicketId();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#upgradeArchivedTicketTo3d4d0(
	 *      org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	@Deprecated
	public void upgradeArchivedTicketTo3d4d0(final ArchivedTicket archivedTicket) {
		archivedTicket.setChargeTime(null);
		archivedTicket.setClosureTime(null);
		List<ArchivedAction> archivedActions = daoService.getArchivedActions(archivedTicket, true);
		archivedTicket.updateTicketChargeTime(archivedActions);
		for (ArchivedAction archivedAction : archivedActions) {
			archivedTicket.updateTicketClosureTime(archivedAction);
		}
		if (archivedTicket.getCreationDepartment() == null) {
			for (ArchivedAction archivedAction : archivedActions) {
				if (ActionType.CHANGE_DEPARTMENT.equals(archivedAction.getActionType())) {
					archivedTicket.setCreationDepartment(archivedAction.getDepartmentBefore());
					break;
				}
			}
		}
		archivedTicket.setCreationDate(archivedTicket.getCreationDate());
		daoService.updateArchivedTicket(archivedTicket);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getLastTicketId()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public long getLastTicketId() {
		return daoService.getLastTicketId();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setDefaultOldPriorityLevelToCategories()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setDefaultOldPriorityLevelToCategories() {
		daoService.setDefaultOldPriorityLevelToCategories();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getLastActionId()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public long getLastActionId() {
		return daoService.getLastActionId();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getLastArchivedActionId()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public long getLastArchivedActionId() {
		return daoService.getLastArchivedActionId();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#setToNullEmpyActionMessages()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setToNullEmpyActionMessages() {
		daoService.setToNullEmpyActionMessages();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateBeanSequence(String,
	 *      String)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void updateBeanSequence(final String beanName, final String sequenceName) {
		daoService.updateBeanSequence(beanName, sequenceName);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getUsersWithNullAuthType(int)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<User> getUsersWithNullAuthType(final int maxResults) {
		return daoService.getUsersWithNullAuthType(maxResults);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addUser(
	 *      org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@Deprecated
	public void addUser(final User user) {
		daoService.addUser(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#upgradeUserKeys(String,
	 *      java.lang.String)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void upgradeUserKeys(final String classname, final String field) {
		daoService.upgradeUserKeys(classname, field);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteUsersWithNoneAuthType()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteUsersWithNoneAuthType() {
		daoService.deleteUsersWithNoneAuthType();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFaqContainers()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<DeprecatedFaqContainer> getFaqContainers() {
		return daoService.getFaqContainers();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addFaqContainer(
	 *      org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void addFaqContainer(final DeprecatedFaqContainer faqContainer) {
		fckEditorCodeCleaner.removeMaliciousTags(faqContainer);
		faqContainer.computeEffectiveScope(true);
		// if (faqContainer.getParent() == null) {
		// faqContainer.setOrder(daoService.getRootFaqContainersNumber(faqContainer.getDepartment()));
		// } else {
		// faqContainer.setOrder(daoService.getSubFaqContainersNumber(faqContainer.getParent()));
		// }
		daoService.addFaqContainer(faqContainer);
	}

	/**
	 * Update the scope of the children of a FAQ container if needed.
	 * 
	 * @param faqContainer
	 */
	@SuppressWarnings("deprecation")
	protected void updateFaqContainerChildrenScope(final DeprecatedFaqContainer faqContainer) {
		daoService.updateFaqEntriesEffectiveScope(faqContainer);
		for (DeprecatedFaqContainer subFaqContainer : getSubFaqContainers(faqContainer)) {
			if (subFaqContainer.computeEffectiveScope(true)) {
				daoService.updateFaqContainer(subFaqContainer);
				updateFaqContainerChildrenScope(subFaqContainer);
			}
		}
	}

	/**
	 * @param faqContainer
	 * @see org.esupportail.helpdesk.domain.DomainService#updateFaqContainer(
	 *      org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void updateFaqContainer(final DeprecatedFaqContainer faqContainer) {
		fckEditorCodeCleaner.removeMaliciousTags(faqContainer);
		boolean effectiveScopeChanged = faqContainer.computeEffectiveScope(true);
		daoService.updateFaqContainer(faqContainer);
		if (effectiveScopeChanged) {
			updateFaqContainerChildrenScope(faqContainer);
		}
	}

	/**
	 * @param faqContainer
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteFaqContainer(
	 *      org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteFaqContainer(final DeprecatedFaqContainer faqContainer) {
		daoService.deleteFaqContainer(faqContainer);
		// this.daoService.addDeletedItem(new
		// DeletedItem(indexIdProvider.getIndexId(faqContainer)));
		// if (faqContainer.getParent() == null) {
		// reorderFaqContainers(daoService.getRootFaqContainers(faqContainer.getDepartment()));
		// } else {
		// reorderFaqContainers(daoService.getSubFaqContainers(faqContainer.getParent()));
		// }
	}

	// /**
	// * Reorder a list of FAQ containers.
	// * @param faqContainers
	// */
	// @SuppressWarnings("deprecation")
	// protected void reorderFaqContainers(final List<DeprecatedFaqContainer>
	// faqContainers) {
	// int i = 0;
	// for (DeprecatedFaqContainer faqContainer : faqContainers) {
	// if (faqContainer.getOrder() != i) {
	// faqContainer.setOrder(i);
	// daoService.updateFaqContainer(faqContainer);
	// }
	// i++;
	// }
	// }

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getRootFaqContainers(
	 *      org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<DeprecatedFaqContainer> getRootFaqContainers(final Department department) {
		return this.daoService.getRootFaqContainers(department);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getRootFaqContainers()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<DeprecatedFaqContainer> getRootFaqContainers() {
		return getRootFaqContainers(null);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getSubFaqContainers(
	 *      org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<DeprecatedFaqContainer> getSubFaqContainers(final DeprecatedFaqContainer faqContainer) {
		return this.daoService.getSubFaqContainers(faqContainer);
	}

	/** Eclipse outline delimiter. */
	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFaqEntries()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<DeprecatedFaqEntry> getFaqEntries() {
		return daoService.getFaqEntries();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getFaqEntries(
	 *      org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public List<DeprecatedFaqEntry> getFaqEntries(final DeprecatedFaqContainer faqContainer) {
		return this.daoService.getFaqEntries(faqContainer);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#addFaqEntry(
	 *      org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void addFaqEntry(final DeprecatedFaqEntry faqEntry) {
		faqEntry.computeEffectiveScope();
		// faqEntry.setOrder(daoService.getFaqEntriesNumber(faqEntry.getParent()));
		this.daoService.addFaqEntry(faqEntry);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#updateFaqEntry(
	 *      org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void updateFaqEntry(final DeprecatedFaqEntry faqEntry) {
		faqEntry.computeEffectiveScope();
		this.daoService.updateFaqEntry(faqEntry);
	}

	// /**
	// * Reorder a list of FAQ entries.
	// * @param faqContainer
	// */
	// protected void reorderFaqEntries(final DeprecatedFaqContainer
	// faqContainer) {
	// int i = 0;
	// for (DeprecatedFaqEntry faqEntry : getFaqEntries(faqContainer)) {
	// if (faqEntry.getOrder() != i) {
	// faqEntry.setOrder(i);
	// daoService.updateFaqEntry(faqEntry);
	// }
	// i++;
	// }
	// }

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#deleteFaqEntry(
	 *      org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry)
	 */
	@Override
	@Deprecated
	public void deleteFaqEntry(final DeprecatedFaqEntry faqEntry) {
		this.daoService.deleteFaqEntry(faqEntry);
		// this.daoService.addDeletedItem(new
		// DeletedItem(indexIdProvider.getIndexId(faqEntry)));
		// reorderFaqEntries(faqEntry.getParent());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDeprecatedTicketsLastIndexTime()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public Timestamp getDeprecatedTicketsLastIndexTime() {
		return daoService.getState().getTicketsLastIndexTime();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDeprecatedFaqContainersLastIndexTime()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public Timestamp getDeprecatedFaqContainersLastIndexTime() {
		return daoService.getState().getFaqContainersLastIndexTime();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDeprecatedFaqEntriesLastIndexTime()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public Timestamp getDeprecatedFaqEntriesLastIndexTime() {
		return daoService.getState().getFaqEntriesLastIndexTime();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#getDeprecatedArchivedTicketsLastIndexTime()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public Timestamp getDeprecatedArchivedTicketsLastIndexTime() {
		return daoService.getState().getArchivedTicketsLastIndexTime();
	}

	/**
	 * Migrate a list of FAQ containers.
	 * 
	 * @param faqContainers
	 * @param parent
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	protected void migrateFaqContainers(final List<DeprecatedFaqContainer> faqContainers, final Faq parent) {
		for (DeprecatedFaqContainer faqContainer : faqContainers) {
			Faq faq = new Faq(faqContainer, parent);
			addFaq(faq);
			daoService.migrateFaqContainerRefs(faqContainer, faq);
			migrateFaqContainers(getSubFaqContainers(faqContainer), faq);
			for (DeprecatedFaqEntry faqEntry : getFaqEntries(faqContainer)) {
				Faq faq2 = new Faq(faqEntry, faq);
				addFaq(faq2);
				daoService.migrateFaqEntryRefs(faqEntry, faq2);
				deleteFaqEntry(faqEntry);
			}
			deleteFaqContainer(faqContainer);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.DomainService#migrateFaqContainers()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void migrateFaqContainers() {
		migrateFaqContainers(getRootFaqContainers(), null);
		for (Department department : getDepartments()) {
			migrateFaqContainers(getRootFaqContainers(department), null);
		}
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________GETTERS_SETTERS() {
		//
	}

	/**
	 * @param daoService
	 *            the daoService to set
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	/**
	 * @param service
	 *            the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}

	/**
	 * @return the applicationService
	 */
	protected ApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @param applicationService
	 *            the applicationService to set
	 */
	public void setApplicationService(final ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @return the daoService
	 */
	protected DaoService getDaoService() {
		return daoService;
	}

	/**
	 * @return the i18nService
	 */
	protected I18nService getI18nService() {
		return i18nService;
	}

	/**
	 * @return the departmentManagerConfigurator
	 */
	protected DepartmentManagerConfigurator getDepartmentManagerConfigurator() {
		return departmentManagerConfigurator;
	}

	/**
	 * @param departmentManagerConfigurator
	 *            the departmentManagerConfigurator to set
	 */
	public void setDepartmentManagerConfigurator(final DepartmentManagerConfigurator departmentManagerConfigurator) {
		this.departmentManagerConfigurator = departmentManagerConfigurator;
	}

	/**
	 * @return the departmentConfigurator
	 */
	protected DepartmentConfigurator getDepartmentConfigurator() {
		return departmentConfigurator;
	}

	/**
	 * @param departmentConfigurator
	 *            the departmentConfigurator to set
	 */
	public void setDepartmentConfigurator(final DepartmentConfigurator departmentConfigurator) {
		this.departmentConfigurator = departmentConfigurator;
	}

	/**
	 * @return the indexIdProvider
	 */
	protected IndexIdProvider getIndexIdProvider() {
		return indexIdProvider;
	}

	/**
	 * @param indexIdProvider
	 *            the indexIdProvider to set
	 */
	public void setIndexIdProvider(final IndexIdProvider indexIdProvider) {
		this.indexIdProvider = indexIdProvider;
	}

	/**
	 * @param userInfoProvider
	 *            the userInfoProvider to set
	 */
	public void setUserInfoProvider(final UserInfoProvider userInfoProvider) {
		this.userInfoProvider = userInfoProvider;
	}

	/**
	 * @return the userInfoProvider
	 */
	protected UserInfoProvider getUserInfoProvider() {
		return userInfoProvider;
	}

	/**
	 * @return the departmentSelector
	 */
	protected DepartmentSelector getDepartmentSelector() {
		return departmentSelector;
	}

	/**
	 * @param departmentSelector
	 *            the departmentSelector to set
	 */
	public void setDepartmentSelector(final DepartmentSelector departmentSelector) {
		this.departmentSelector = departmentSelector;
	}

	/**
	 * @return the assignmentAlgorithmStore
	 */
	protected AssignmentAlgorithmStore getAssignmentAlgorithmStore() {
		return assignmentAlgorithmStore;
	}

	/**
	 * @param assignmentAlgorithmStore
	 *            the assignmentAlgorithmStore to set
	 */
	public void setAssignmentAlgorithmStore(final AssignmentAlgorithmStore assignmentAlgorithmStore) {
		this.assignmentAlgorithmStore = assignmentAlgorithmStore;
	}

	/**
	 * @param defaultAssignmentAlgorithmName
	 *            the defaultAssignmentAlgorithmName to set
	 */
	public void setDefaultAssignmentAlgorithmName(final String defaultAssignmentAlgorithmName) {
		this.defaultAssignmentAlgorithmName = defaultAssignmentAlgorithmName;
	}

	/**
	 * @return the computerUrlBuilderStore
	 */
	protected ComputerUrlBuilderStore getComputerUrlBuilderStore() {
		return computerUrlBuilderStore;
	}

	/**
	 * @param computerUrlBuilderStore
	 *            the computerUrlBuilderStore to set
	 */
	public void setComputerUrlBuilderStore(final ComputerUrlBuilderStore computerUrlBuilderStore) {
		this.computerUrlBuilderStore = computerUrlBuilderStore;
	}

	/**
	 * @param defaultComputerUrlBuilderName
	 *            the defaultComputerUrlBuilderName to set
	 */
	public void setDefaultComputerUrlBuilderName(final String defaultComputerUrlBuilderName) {
		this.defaultComputerUrlBuilderName = defaultComputerUrlBuilderName;
	}

	/**
	 * @return the defaultControlPanelRefreshDelay
	 */
	public Integer getDefaultControlPanelRefreshDelay() {
		return defaultControlPanelRefreshDelay;
	}

	/**
	 * @param defaultControlPanelRefreshDelay
	 *            the defaultControlPanelRefreshDelay to set
	 */
	public void setDefaultControlPanelRefreshDelay(final Integer defaultControlPanelRefreshDelay) {
		this.defaultControlPanelRefreshDelay = defaultControlPanelRefreshDelay;
	}

	/**
	 * @return the categoryConfigurator
	 */
	protected CategoryConfigurator getCategoryConfigurator() {
		return categoryConfigurator;
	}

	/**
	 * @param categoryConfigurator
	 *            the categoryConfigurator to set
	 */
	public void setCategoryConfigurator(final CategoryConfigurator categoryConfigurator) {
		this.categoryConfigurator = categoryConfigurator;
	}

	/**
	 * @return the historyMaxLength
	 */
	protected int getHistoryMaxLength() {
		return historyMaxLength;
	}

	/**
	 * @param historyMaxLength
	 *            the historyMaxLength to set
	 */
	public void setHistoryMaxLength(final int historyMaxLength) {
		this.historyMaxLength = historyMaxLength;
	}

	/**
	 * @return the fckEditorCodeCleaner
	 */
	protected FckEditorCodeCleaner getFckEditorCodeCleaner() {
		return fckEditorCodeCleaner;
	}

	/**
	 * @param fckEditorCodeCleaner
	 *            the fckEditorCodeCleaner to set
	 */
	public void setFckEditorCodeCleaner(final FckEditorCodeCleaner fckEditorCodeCleaner) {
		this.fckEditorCodeCleaner = fckEditorCodeCleaner;
	}

	/**
	 * @param userStore
	 *            the userStore to set
	 */
	public void setUserStore(final UserStore userStore) {
		this.userStore = userStore;
	}

	/**
	 * @return the ticketPrinter
	 */
	protected TicketPrinter getTicketPrinter() {
		return ticketPrinter;
	}

	/**
	 * @param ticketPrinter
	 *            the ticketPrinter to set
	 */
	public void setTicketPrinter(final TicketPrinter ticketPrinter) {
		this.ticketPrinter = ticketPrinter;
	}

	/**
	 * @return the invitationSender
	 */
	protected InvitationSender getInvitationSender() {
		return invitationSender;
	}

	/**
	 * @param invitationSender
	 *            the invitationSender to set
	 */
	public void setInvitationSender(final InvitationSender invitationSender) {
		this.invitationSender = invitationSender;
	}

	/**
	 * @return the monitoringSender
	 */
	protected MonitoringSender getMonitoringSender() {
		return monitoringSender;
	}

	/**
	 * @param monitoringSender
	 *            the monitoringSender to set
	 */
	public void setMonitoringSender(final MonitoringSender monitoringSender) {
		this.monitoringSender = monitoringSender;
	}

	/**
	 * @return the ticketReporter
	 */
	protected TicketReporter getTicketReporter() {
		return ticketReporter;
	}

	/**
	 * @param ticketReporter
	 *            the ticketReporter to set
	 */
	public void setTicketReporter(final TicketReporter ticketReporter) {
		this.ticketReporter = ticketReporter;
	}

	/**
	 * @return the faqReporter
	 */
	protected FaqReporter getFaqReporter() {
		return faqReporter;
	}

	/**
	 * @param faqReporter
	 *            the faqReporter to set
	 */
	public void setFaqReporter(final FaqReporter faqReporter) {
		this.faqReporter = faqReporter;
	}

	/**
	 * @return the ticketCommentModification
	 */
	@Override
	public boolean isTicketCommentModification() {
		return ticketCommentModification;
	}

	/**
	 * @param ticketCommentModification
	 *            the ticketCommentModification to set
	 */
	public void setTicketCommentModification(boolean ticketCommentModification) {
		this.ticketCommentModification = ticketCommentModification;
	}
	

	public boolean isTryConvertMaillToCasUser() {
		return tryConvertMaillToCasUser;
	}

	public void setTryConvertMaillToCasUser(boolean tryConvertMaillToCasUser) {
		this.tryConvertMaillToCasUser = tryConvertMaillToCasUser;
	}

	public Boolean getInviteManagerMoveTicket() {
		return inviteManagerMoveTicket;
	}

	public void setInviteManagerMoveTicket(Boolean inviteManagerMoveTicket) {
		this.inviteManagerMoveTicket = inviteManagerMoveTicket;
	}
	
	public Boolean getCheckVisiCateVirtual() {
		return checkVisiCateVirtual;
	}

	public void setCheckVisiCateVirtual(Boolean checkVisiCateVirtual) {
		this.checkVisiCateVirtual = checkVisiCateVirtual;
	}
}
