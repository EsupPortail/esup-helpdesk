/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.RecallUpgradeException;
import org.esupportail.commons.services.application.Version;
import org.esupportail.commons.services.application.VersionException;
import org.esupportail.commons.services.application.VersionningService;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.database.DatabaseUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.NcrDecoder;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.ActionType;
import org.esupportail.helpdesk.domain.ControlPanel;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.FckEditorCodeCleaner;
import org.esupportail.helpdesk.domain.TicketScope;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Alert;
import org.esupportail.helpdesk.domain.beans.ArchivedAction;
import org.esupportail.helpdesk.domain.beans.ArchivedFileInfo;
import org.esupportail.helpdesk.domain.beans.ArchivedInvitation;
import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Bookmark;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.CategoryMember;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentInvitation;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.DepartmentSelectionConfig;
import org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer;
import org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry;
import org.esupportail.helpdesk.domain.beans.FileInfo;
import org.esupportail.helpdesk.domain.beans.HistoryItem;
import org.esupportail.helpdesk.domain.beans.Invitation;
import org.esupportail.helpdesk.domain.beans.OldFaqEntry;
import org.esupportail.helpdesk.domain.beans.OldFaqPart;
import org.esupportail.helpdesk.domain.beans.OldFileInfo;
import org.esupportail.helpdesk.domain.beans.OldTicketTemplate;
import org.esupportail.helpdesk.domain.beans.Response;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.TicketMonitoring;
import org.esupportail.helpdesk.domain.beans.TicketView;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentManagerConfiguration.DepartmentManagerConfigurator;
import org.esupportail.helpdesk.domain.departmentSelection.Actions;
import org.esupportail.helpdesk.domain.departmentSelection.Rules;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions;
import org.esupportail.helpdesk.domain.departmentSelection.actions.AddAllAction;
import org.esupportail.helpdesk.services.indexing.Indexer;
import org.esupportail.helpdesk.web.controllers.AbstractDomainAwareBean;

/**
 * A bean for versionning management.
 */
public class VersionningServiceImpl extends AbstractDomainAwareBean implements VersionningService {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4516112128287542945L;

	/**
	 * The number of tickets to load at the same time.
	 */
	private static final int TICKET_BATCH_SIZE = 500;

	/**
	 * The number of actions to load at the same time.
	 */
	private static final int ACTION_BATCH_SIZE = 2000;

	/**
	 * The number of files to load at the same time.
	 */
	private static final int FILES_BATCH_SIZE = 50;

	/**
	 * The number of users to load at the same time.
	 */
	private static final int USER_BATCH_SIZE = 500;

	/**
	 * Magic number.
	 */
	private static final int KILO = 1024;

	/**
	 * The max number of bytes to read before committing.
	 */
	private static final int FILES_READ_SIZE = 16 * KILO * KILO;

	/**
	 * The icons added by the upgrade to 3.14.0.
	 */
	private static final String [] ICONS_3_14_0 = {
		"application_view_list",
		"basket",
		"bell",
		"bomb",
		"book",
		"bug",
		"calculator",
		"calendar",
		"camera",
		"cancel",
		"car",
		"cart",
		"cd",
		"chart_bar",
		"chart_pie",
		"clock",
		"cog",
		"coins",
		"color_wheel",
		"compress",
		"computer",
		"connect",
		"contrast",
		"controller",
		"cross",
		"css",
		"cup",
		"database",
		"date",
		"defaultCategory",
		"defaultDepartment",
		"disconnect",
		"disk",
		"drink",
		"drive",
		"drive_cd",
		"dvd",
		"email",
		"email_delete",
		"emoticon_grin",
		"error",
		"exclamation",
		"eye",
		"feed",
		"female",
		"film",
		"find",
		"folder",
		"font",
		"group",
		"hand",
		"heart",
		"help",
		"home",
		"hourglass",
		"html",
		"image",
		"information",
		"ipod",
		"ipod_cast",
		"joystick",
		"key",
		"keyboard",
		"layout",
		"lightbulb",
		"lightbulb_off",
		"lightning",
		"lock",
		"magnifier",
		"male",
		"medal_gold_1",
		"medal_gold_2",
		"medal_gold_2",
		"money",
		"money_dollar",
		"money_euro",
		"money_pound",
		"money_yen",
		"monitor",
		"mouse",
		"music",
		"new",
		"overlays",
		"page",
		"page_copy",
		"page_edit",
		"page_excel",
		"page_white",
		"page_white_acrobat",
		"page_white_compressed",
		"page_white_text",
		"page_white_word",
		"paintbrush",
		"palette",
		"pencil",
		"phone",
		"photo",
		"pill",
		"plugin",
		"plugin_disabled",
		"printer",
		"printer_add",
		"printer_delete",
		"printer_empty",
		"printer_error",
		"rainbow",
		"rosette",
		"rss",
		"search",
		"script",
		"server",
		"server_connect",
		"sound",
		"sport_basketball",
		"star",
		"stop",
		"sun",
		"telephone",
		"television",
		"text_superscript",
		"tick",
		"time",
		"transmit",
		"tux",
		"user",
		"users",
		"wand",
		"weather_cloudy",
		"weather_sun",
		"web",
		"webcam",
		"wrench",
		"zoom",
	};

	/**
	 * The icons added by the upgrade to 3.15.2.
	 */
	private static final String [] ICONS_3_15_2 = {
		"wifi",
		"windows",
	};

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The id of the first administrator.
	 */
	private String firstAdministratorId;

	/**
	 * The indexer.
	 */
	private Indexer indexer;

	/**
	 * A bean to configure department managers.
	 */
	private DepartmentManagerConfigurator departmentManagerConfigurator;

	/**
	 * The FCK editor code cleaner.
	 */
	private FckEditorCodeCleaner fckEditorCodeCleaner;

	/**
	 * Bean constructor.
	 */
	public VersionningServiceImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractDomainAwareBean#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		Assert.notNull(firstAdministratorId,
				"property firstAdministratorId of class " + getClass().getName()
				+ " can not be null");
		Assert.notNull(indexer,
				"property indexer of class " + getClass().getName()
				+ " can not be null");
		Assert.notNull(departmentManagerConfigurator,
				"property departmentManagerConfigurator of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(fckEditorCodeCleaner,
				"property fckEditorCodeCleaner of class " + this.getClass().getName()
				+ " can not be null");
	}

	/**
	 * print the last version available.
	 */
	protected void printLastVersion() {
		Version latestVersion = getApplicationService().getLatestVersion();
		if (latestVersion != null) {
			logger.info("Latest version available: " + latestVersion);
		}
	}

	/**
	 * Set the database version.
	 * @param version
	 * @param silent true to omit info messages
	 */
	public void setDatabaseVersion(
			final String version,
			final boolean silent) {
		getDomainService().setDatabaseVersion(version);
		if (!silent) {
			logger.info("database version set to " + version + ".");
		}
	}

	/**
	 * @return the database version.
	 */
	public Version getDatabaseVersion() {
		return getDomainService().getDatabaseVersion();
	}

	/**
	 * @see org.esupportail.commons.services.application.VersionningService#initDatabase()
	 */
	@Override
	public void initDatabase() {
		DatabaseUtils.create();
		logger.info("creating the first user of the application thanks to "
				+ getClass().getName() + ".firstAdministratorId...");
		User firstAdministrator = getUserStore().getUserFromRealId(firstAdministratorId);
		getDomainService().addAdmin(firstAdministrator);
		logger.info("the database has been created.");
		setDatabaseVersion("0.0.0", true);
	}

	/**
	 * @see org.esupportail.commons.services.application.VersionningService#checkVersion(boolean, boolean)
	 */
	@Override
	public void checkVersion(
			final boolean throwException,
			final boolean printLatestVersion) throws VersionException {
		Version databaseVersion = getDomainService().getDatabaseVersion();
		Version applicationVersion = getApplicationService().getVersion();
		if (databaseVersion == null) {
			String msg = "Your database is not initialized, please run 'ant init-data'.";
			if (throwException) {
				throw new VersionException(msg);
			}
			logger.error(msg);
			if (printLatestVersion) {
				printLastVersion();
			}
			return;
		}
		if (applicationVersion.equals(databaseVersion)) {
			String msg = "The database is up to date.";
			if (throwException) {
				if (logger.isDebugEnabled()) {
					logger.debug(msg);
				}
			} else {
				logger.info(msg);
			}
			if (printLatestVersion) {
				printLastVersion();
			}
			return;
		}
		String state = getDomainService().getUpgradeState();
		if (state != null) {
			throw new VersionException("upgrade currently running, retry in a few minutes...");
		}
		if (applicationVersion.isSameMajorAndMinor(databaseVersion)) {
			logger.info("Database version is " + databaseVersion + ", upgrading...");
			upgradeDatabase();
			if (printLatestVersion) {
				printLastVersion();
			}
			return;
		}
		if (databaseVersion.isOlderThan(applicationVersion)) {
			String msg = "Application version is " + applicationVersion
			+ ", the database is too old (" + databaseVersion + "), please run 'ant upgrade'.";
			if (throwException) {
				throw new VersionException(msg);
			}
			logger.error(msg);
			if (printLatestVersion) {
				printLastVersion();
			}
			return;
		}
		String msg = "Database version is " + databaseVersion + ", the application is too old ("
		+ applicationVersion + "), please upgrade.";
		if (throwException) {
			throw new VersionException(msg);
		}
		if (printLatestVersion) {
			printLastVersion();
		}
		logger.error(msg);
	}

	/**
	 * Print a message saying that the database version is older than ...
	 * @param version the new version
	 */
	protected void printOlderThanMessage(final String version) {
		logger.info(new StringBuffer("database version (")
				.append(getDomainService().getDatabaseVersion())
				.append(") is older than ")
				.append(version)
				.append(", upgrading..."));
	}

	/**
	 * Charges \r and \n to <br />.
	 * @param input
	 * @return a String.
	 */
	protected String crToBr(final String input) {
		return input
		.replaceAll("[\\r\\n]+", "<br />")
		.replaceAll("[\\n]+", "<br />")
		.replaceAll("[\\r]+", "<br />");
	}

	/**
	 * Upgrade the database to version 3.0.0 (update FAQ entries).
	 * @param parent
	 * @param oldFaqEntries
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d0d0FaqEntries(
			final DeprecatedFaqContainer parent,
			final List<OldFaqEntry> oldFaqEntries) {
		for (OldFaqEntry oldFaqEntry : oldFaqEntries) {
			DeprecatedFaqEntry newFaqEntry = new DeprecatedFaqEntry();
			newFaqEntry.setLabel(NcrDecoder.decode(oldFaqEntry.getLabel()));
			if (oldFaqEntry.getContent() != null) {
				newFaqEntry.setContent(NcrDecoder.decode(
						oldFaqEntry.getContent())
						.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
						.replaceAll("&amp;", "&"));
			}
			newFaqEntry.setLastUpdate(oldFaqEntry.getLastUpdate());
			newFaqEntry.setParent(parent);
			getDomainService().addFaqEntry(newFaqEntry);
			logger.info("created FaqEntry [" + newFaqEntry.getId()
					+ "] (" + newFaqEntry.getLabel() + ") from OldFaqEntry ["
					+ oldFaqEntry.getId() + "] (" + oldFaqEntry.getLabel() + ")");
			for (Ticket ticket
					: getDomainService().getTicketsConnectedToOldFaqEntry(
							oldFaqEntry)) {
				ticket.setConnectionOldFaqEntry(null);
				ticket.setDeprecatedConnectionFaqEntry(newFaqEntry);
				getDomainService().updateTicket(ticket);
				logger.info("updated Ticket [" + ticket.getId()
						+ "] -> FaqEntry [" + newFaqEntry.getId() + "]");
			}
			for (Action action
					: getDomainService().getActionsConnectedToOldFaqEntry(
							oldFaqEntry)) {
				action.setOldFaqEntryConnectionAfter(null);
				getDomainService().updateAction(action);
				logger.info("updated Action [" + action.getId()
						+ "] -> FaqEntry [" + newFaqEntry.getId() + "]");
			}
			getDomainService().deleteOldFaqEntry(oldFaqEntry);
			logger.info("deleted OldFaqEntry [" + oldFaqEntry.getId() + "]");
		}
	}

	/**
	 * Upgrade the database to version 3.0.0 (upgrade FAQ containers and recover old FAQ parts).
	 * @param faqContainers
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d0d0FaqContainers(
			final List<DeprecatedFaqContainer> faqContainers) {
		for (DeprecatedFaqContainer faqContainer : faqContainers) {
			faqContainer.setLabel(NcrDecoder.decode(faqContainer.getLabel()));
			if (faqContainer.getOldContent() != null) {
				faqContainer.setContent(NcrDecoder.decode(
						faqContainer.getOldContent()));
				faqContainer.setOldContent(" ");
			}
			getDomainService().updateFaqContainer(faqContainer);
			logger.info("updated FAQ container [" + faqContainer.getId() + "] ("
					+ faqContainer.getLabel() + ")");
			for (OldFaqPart oldFaqPart : getDomainService().getOldFaqParts(faqContainer)) {
				DeprecatedFaqContainer newFaqContainer = new DeprecatedFaqContainer();
				newFaqContainer.setDepartment(faqContainer.getDepartment());
				newFaqContainer.setLabel(NcrDecoder.decode(oldFaqPart.getLabel()));
				if (oldFaqPart.getContent() != null) {
					newFaqContainer.setContent(NcrDecoder.decode(
							oldFaqPart.getContent())
							.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
							.replaceAll("&amp;", "&"));
				}
				newFaqContainer.setLastUpdate(oldFaqPart.getLastUpdate());
				newFaqContainer.setParent(faqContainer);
				getDomainService().addFaqContainer(newFaqContainer);
				logger.info("created FaqContainer [" + newFaqContainer.getId()
						+ "] (" + newFaqContainer.getLabel() + ") from OldFaqPart ["
						+ oldFaqPart.getId() + "] (" + oldFaqPart.getLabel() + ")");
				for (Ticket ticket
						: getDomainService().getTicketsConnectedToOldFaqPart(
								oldFaqPart)) {
					ticket.setConnectionOldFaqPart(null);
					ticket.setDeprecatedConnectionFaqContainer(newFaqContainer);
					getDomainService().updateTicket(ticket);
					logger.info("updated Ticket [" + ticket.getId()
							+ "] -> FaqContainer [" + newFaqContainer.getId() + "]");
				}
				for (Action action
						: getDomainService().getActionsConnectedToOldFaqPart(
								oldFaqPart)) {
					logger.info("found action [" + action.getId()
							+ "] -> oldFaqPart [" + oldFaqPart.getId() + "]");
					action.setOldFaqPartConnectionAfter(null);
					getDomainService().updateAction(action);
					logger.info("updated Action [" + action.getId()
							+ "] -> FaqContainer [" + newFaqContainer.getId() + "]");
				}
				upgrade3d0d0FaqEntries(
						newFaqContainer,
						getDomainService().getOldFaqEntries(oldFaqPart));
				getDomainService().deleteOldFaqPart(oldFaqPart);
				logger.info("deleted OldFaqPart [" + oldFaqPart.getId() + "]");
			}
			upgrade3d0d0FaqEntries(
					faqContainer,
					getDomainService().getOldFaqEntries(faqContainer));
		}
	}

	/**
	 * Upgrade the database to version 3.0.0 (update the structures).
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d0d0Structures() throws RecallUpgradeException {
		if (logger.isDebugEnabled()) {
			logger.debug("upgrade3d0d0Structures()");
		}
		List<Department> departments = getDomainService().getDepartments();
		if (logger.isDebugEnabled()) {
			logger.debug(departments.size() + " departments found");
		}
		for (Department department : departments) {
			department.setLabel(NcrDecoder.decode(department.getLabel()));
			department.setXlabel(NcrDecoder.decode(department.getXlabel()));
			department.setFilter(NcrDecoder.decode(department.getFilter()));
			if (department.getDefaultTicketMessage() != null) {
				department.setDefaultTicketMessage(
						crToBr(NcrDecoder.decode(department.getDefaultTicketMessage())));
			}
			department.setDefaultTicketLabel(NcrDecoder.decode(department.getDefaultTicketLabel()));
			getDomainService().updateDepartment(department);
			logger.info("updated Department [" + department.getId() + "] ("
					+ department.getLabel() + ")");
			List<DepartmentManager> departmentManagers =
				getDomainService().getDepartmentManagers(department);
			if (logger.isDebugEnabled()) {
				logger.debug(departmentManagers.size() + " department managers found for department ["
						+ department.getLabel() + "]");
			}
			for (DepartmentManager departmentManager
					: departmentManagers) {
				departmentManager.setManageProperties(departmentManager.getManageManagers());
				departmentManagerConfigurator.configureTicketMonitoring(departmentManager);
				getDomainService().updateDepartmentManager(departmentManager);
				logger.info("updated DepartmentManager [" + departmentManager.getUser().getId()
						+ "] for Department [" + department.getLabel() + ")");
			}
			getDomainService().reorderDepartmentManagers(
					getDomainService().getDepartmentManagers(department));
			List<Category> rootCategories = getDomainService().getRootCategories(department);
			if (logger.isDebugEnabled()) {
				logger.debug(rootCategories.size() + " root categories found for department ["
						+ department.getLabel() + "]");
			}
			for (Category category : rootCategories) {
				category.setLabel(NcrDecoder.decode(category.getLabel()));
				category.setXlabel(NcrDecoder.decode(category.getXlabel()));
				category.setAssignmentAlgorithmState(null);
				category.setDefaultTicketScope(TicketScope.DEFAULT);
				if (category.getDefaultTicketMessage() != null) {
					category.setDefaultTicketMessage(
							crToBr(NcrDecoder.decode(category.getDefaultTicketMessage())));
				}
				category.setDefaultTicketLabel(
						NcrDecoder.decode(category.getDefaultTicketLabel()));
				category.setInheritMembers(false);
				getDomainService().updateCategory(category);
				logger.info("updated Category [" + category.getId() + "] ("
						+ category.getLabel() + ")");
				List<OldTicketTemplate> oldTicketTemplates =
					getDomainService().getOldTicketTemplates(category);
				if (logger.isDebugEnabled()) {
					logger.debug(oldTicketTemplates.size()
							+ " old ticket templates found for category ["
							+ category.getLabel() + "]");
				}
				for (OldTicketTemplate oldTicketTemplate : oldTicketTemplates) {
					Category newCategory = new Category();
					newCategory.setDepartment(department);
					newCategory.setParent(category);
					newCategory.setDefaultTicketLabel(
							NcrDecoder.decode(oldTicketTemplate.getTicketTemplateLabel()));
					if (!oldTicketTemplate.getUseCategoryMessage()
							&& oldTicketTemplate.getTicketTemplateMessage() != null) {
						newCategory.setDefaultTicketMessage(
								crToBr(NcrDecoder.decode(
										oldTicketTemplate
										.getTicketTemplateMessage())));
					}
					int priority = DomainService.DEFAULT_PRIORITY_VALUE;
					if (!oldTicketTemplate.getUseCategoryPriority()) {
						priority = oldTicketTemplate.getTicketTemplatePriorityLevel();
					}
					newCategory.setDefaultTicketPriority(priority);
					newCategory.setDefaultTicketScope(TicketScope.DEFAULT);
					newCategory.setLabel(NcrDecoder.decode(oldTicketTemplate.getLabel()));
					newCategory.setXlabel(NcrDecoder.decode(oldTicketTemplate.getXlabel()));
					newCategory.setInheritMembers(true);
					getDomainService().addCategory(newCategory);
					logger.info("created Category [" + newCategory.getLabel() + "] ("
							+ newCategory.getId() + ") from OldTicketTemplate ["
							+ oldTicketTemplate.getId() + "] ("
							+ oldTicketTemplate.getLabel() + ")");
					getDomainService().deleteOldTicketTemplate(oldTicketTemplate);
					logger.info("deleted OldTicketTemplate ["
							+ oldTicketTemplate.getId() + "]");
				}
			}
			if (getDomainService().hasOrphenTickets(department)) {
				Category newCategory = new Category();
				newCategory.setDepartment(department);
				newCategory.setLabel("@@@ UPGRADE @@@");
				newCategory.setXlabel("added by the upgrade to 3.0");
				newCategory.setInheritMembers(true);
				getDomainService().addCategory(newCategory);
				logger.info("created Category [" + newCategory.getId() + "] ("
						+ newCategory.getLabel()
						+ ") for orphen tickets of Department ["
						+ department.getLabel() + "]");
				for (Ticket ticket : getDomainService().getOrphenTickets(department)) {
					getDomainService().moveTicket(ticket, newCategory);
					logger.info("moved ticket [" + ticket.getId() + "] ("
							+ ticket.getLabel() + ") to Category ["
							+ newCategory.getId() + "]");
				}
			}
			upgrade3d0d0FaqContainers(getDomainService().getRootFaqContainers(department));
		}
		upgrade3d0d0FaqContainers(getDomainService().getRootFaqContainers(null));
	}

	/**
	 * Upgrade the database to version 3.0.0 (update the users).
	 * @throws RecallUpgradeException
	 */
	protected void upgrade3d0d0Users() throws RecallUpgradeException {
		if (logger.isDebugEnabled()) {
			logger.debug("upgrade3d0d0Users()");
		}
		List<User> users = getDomainService().getUsers();
		if (logger.isDebugEnabled()) {
			logger.debug(users.size() + " users found");
		}
		for (User user : users) {
			if (getDomainService().getUserStore().isApplicationUser(user)) {
				if (user.getDisplayName() != null) {
					user.setDisplayName(NcrDecoder.decode(user.getDisplayName()));
				} else {
					user.setDisplayName(user.getId());
				}
				logger.info("updated user " + user.getId() + " (" + user.getDisplayName() + ")");
				user.setControlPanelUserDepartmentFilter(null);
				user.setControlPanelManagerDepartmentFilter(null);
				user.setControlPanelManagerInvolvementFilter(
						ControlPanel.MANAGER_INVOLVEMENT_FILTER_ANY);
				user.setControlPanelUserInvolvementFilter(
						ControlPanel.USER_INVOLVEMENT_FILTER_ANY);
				user.setControlPanelUserInterface(true);
				user.setControlPanelUserStatusFilter(ControlPanel.STATUS_FILTER_ANY);
				user.setControlPanelManagerStatusFilter(ControlPanel.STATUS_FILTER_ANY);
				user.setLanguage(getI18nService().getDefaultLocale().getLanguage());
				getDomainService().updateUser(user);
			}
		}
	}

	/**
	 * Upgrade the database to version 3.0.0 (update the tickets).
	 * @param firstTicket
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d0d0Tickets(final long firstTicket) throws RecallUpgradeException {
		if (logger.isDebugEnabled()) {
			logger.debug("upgrade3d0d0Tickets()");
		}
		if (firstTicket > getDomainService().getLastTicketId()) {
			logger.info("no more ticket to upgrade.");
			return;
		}
		long nextTicket = firstTicket;
		for (Ticket ticket : getDomainService().getTickets(firstTicket, TICKET_BATCH_SIZE)) {
			if (logger.isDebugEnabled()) {
				logger.debug("updating ticket #" + ticket.getId() + "...");
			}
			ticket.setLabel(NcrDecoder.decode(ticket.getLabel()));
			getDomainService().setCreator(ticket);
			getDomainService().updateTicket(ticket);
			logger.info("updated ticket [" + ticket.getId() + "] (" + ticket.getLabel() + ")");
			nextTicket = ticket.getId() + 1;
		}
		throw new RecallUpgradeException("t" + String.valueOf(nextTicket));
	}

	/**
	 * Upgrade the database to version 3.0.0 (update the actions).
	 * @param firstAction
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d0d0Actions(final long firstAction) throws RecallUpgradeException {
		if (logger.isDebugEnabled()) {
			logger.debug("upgrade3d0d0Actions()");
		}
		List<Action> actions =
			getDomainService().getV2ActionsToUpgradeToV3(firstAction, ACTION_BATCH_SIZE);
		if (actions.isEmpty()) {
			return;
		}
		long nextAction = firstAction;
		for (Action action : actions) {
			if (action.getMessage() != null) {
				action.setMessage(crToBr(NcrDecoder.decode(action.getMessage())));
			}
			action.setLabelBefore(NcrDecoder.decode(action.getLabelBefore()));
			action.setLabelAfter(NcrDecoder.decode(action.getLabelAfter()));
			if (ActionType.CONNECT_TO_TICKET_V2.equals(action.getActionType())
					|| ActionType.CONNECT_TO_FAQ_V2.equals(action.getActionType())) {
				action.setActionType(ActionType.CLOSE);
			}
			if (ActionType.CONNECT_TO_TICKET_APPROVE_V2.equals(action.getActionType())
					|| ActionType.CONNECT_TO_FAQ_APPROVE_V2.equals(action.getActionType())) {
				action.setActionType(ActionType.CLOSE_APPROVE);
			}
			action.setOldConnectionAfter(null);
			action.setOldFaqPartConnectionAfter(null);
			action.setOldFaqEntryConnectionAfter(null);
			getDomainService().updateAction(action);
			logger.info("updated action [" + action.getId() + "]");
			nextAction = action.getId() + 1;
		}
		throw new RecallUpgradeException("a" + String.valueOf(nextAction));
	}

	/**
	 * Upgrade the database to version 3.0.0 (files attached to tickets).
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d0d0Files() throws RecallUpgradeException {
		if (logger.isDebugEnabled()) {
			logger.debug("upgrade3d0d0Files()");
		}
		logger.info("looking for actions with attached files...");
		List<Action> actions = getDomainService().getActionsWithAttachedFile(FILES_BATCH_SIZE);
		if (actions.isEmpty()) {
			logger.info("no action found");
			return;
		}
		logger.info(actions.size() + " action(s) found");
		List<OldFileInfo> oldFileInfos = new ArrayList<OldFileInfo>();
		int totalBytesRead = 0;
		for (Action action : actions) {
			OldFileInfo oldFileInfo = action.getOldFileInfo();
			logger.info("found Action #" + action.getId() + " with OldFileInfo #" + oldFileInfo.getId());
			getDomainService().getOldFileInfoContent(oldFileInfo);
			int bytesRead = oldFileInfo.getContent().length;
			totalBytesRead += bytesRead;
			logger.info(bytesRead + " bytes read, total " + totalBytesRead);
			FileInfo fileInfo = new FileInfo(
					NcrDecoder.decode(oldFileInfo.getFilename()),
					oldFileInfo.getContent(),
					action.getTicket(),
					action.getUser(),
					action.getScope());
			fileInfo.setDate(action.getDate());
			getDomainService().addFileInfo(fileInfo);
			logger.info("created FileInfo #" + fileInfo.getId());
			action.setOldFileInfo(null);
			getDomainService().updateAction(action);
			oldFileInfos.add(oldFileInfo);
			if (totalBytesRead > FILES_READ_SIZE) {
				logger.info("flushing...");
				break;
			}
		}
		logger.info("deleting old files...");
		for (OldFileInfo oldFileInfo : oldFileInfos) {
			getDomainService().deleteOldFileInfo(oldFileInfo);
			logger.info("deleted OldFileInfo #" + oldFileInfo.getId());
		}
		throw new RecallUpgradeException("f");
	}

	/**
	 * Upgrade the database to version 3.0.0 (remove the index).
	 */
	protected void upgrade3d0d0Index() {
		if (logger.isDebugEnabled()) {
			logger.debug("upgrade3d0d0Index()");
		}
		indexer.removeIndex();
	}

	/**
	 * Upgrade the database to version 3.0.0 (major upgrade).
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d0d0() throws RecallUpgradeException {
		logger.info("upgradeState = " + getDomainService().getUpgradeState());
		if (getDomainService().getUpgradeState() == null) {
			throw new RecallUpgradeException("s");
		}
		if ("s".equals(getDomainService().getUpgradeState())) {
			upgrade3d0d0Structures();
			throw new RecallUpgradeException("u");
		}
		if ("u".equals(getDomainService().getUpgradeState())) {
			upgrade3d0d0Users();
			throw new RecallUpgradeException("t0");
		}
		if (getDomainService().getUpgradeState().startsWith("t")) {
			try {
				int firstTicket = Integer.valueOf(getDomainService().getUpgradeState().substring(1));
				if (logger.isDebugEnabled()) {
					logger.debug("firstTicket = " + firstTicket);
				}
				upgrade3d0d0Tickets(firstTicket);
			} catch (NumberFormatException e) {
				// see below
			}
			throw new RecallUpgradeException("a0");
		}
		if (getDomainService().getUpgradeState().startsWith("a")) {
			try {
				int firstAction = Integer.valueOf(getDomainService().getUpgradeState().substring(1));
				if (logger.isDebugEnabled()) {
					logger.debug("firstAction = " + firstAction);
				}
				upgrade3d0d0Actions(firstAction);
			} catch (NumberFormatException e) {
				// see below
			}
			throw new RecallUpgradeException("f");
		}
		if ("f".equals(getDomainService().getUpgradeState())) {
			upgrade3d0d0Files();
			throw new RecallUpgradeException("i");
		}
		if ("i".equals(getDomainService().getUpgradeState())) {
			upgrade3d0d0Index();
			getDomainService().setUpgradeState(null);
			return;
		}
		throw new ConfigException("bad upgrade state ["
				+ getDomainService().getUpgradeState() + "]");
	}

	/**
	 * Upgrade the database to version 3.1.0.
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	public void upgrade3d1d0() throws RecallUpgradeException {
		logger.info("looking for v2 invitations...");
		List<Action> actions = getDomainService().getV2Invitations();
		if (actions.isEmpty()) {
			logger.info("no v2 invitation found");
		} else {
			logger.info(actions.size() + " invitation(s) found");
			for (Action action : actions) {
				getDomainService().migrateV2Invitation(action);
			}
		}
		logger.info("looking for v2 archived invitations...");
		List<ArchivedAction> archivedActions = getDomainService().getV2ArchivedInvitations();
		if (archivedActions.isEmpty()) {
			logger.info("no v2 archived invitation found");
		} else {
			logger.info(archivedActions.size() + " archived invitation(s) found");
			for (ArchivedAction archivedAction : archivedActions) {
				getDomainService().migrateV2ArchivedInvitation(archivedAction);
			}
		}
	}

	/**
	 * Upgrade the database to version 3.2.3.
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	public void upgrade3d2d3() throws RecallUpgradeException {
		logger.info("looking for categories that inherit members...");
		List<Category> categories = getDomainService().getInheritingMembersCategories();
		if (categories.isEmpty()) {
			logger.info("no category found");
		} else {
			logger.info(categories.size() + " categories(s) found");
			for (Category category : categories) {
				if (!getDomainService().getCategoryMembers(category).isEmpty()) {
					category.setInheritMembers(false);
					getDomainService().updateCategory(category);
				}
			}
		}
	}

	/**
	 * Upgrade the database to version 3.4.0 (update the charge and closure time of tickets).
	 * @param firstTicket
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d4d0Tickets(final long firstTicket) throws RecallUpgradeException {
		logger.info("upgradeState = " + getDomainService().getUpgradeState());
		if (firstTicket > getDomainService().getLastTicketId()) {
			logger.info("no more ticket to upgrade.");
			return;
		}
		long nextTicket = firstTicket;
		for (Ticket ticket : getDomainService().getTickets(firstTicket, TICKET_BATCH_SIZE)) {
			if (logger.isDebugEnabled()) {
				logger.debug("updating ticket #" + ticket.getId() + "...");
			}
			getDomainService().upgradeTicketTo3d4d0(ticket);
			logger.info(
					"updated ticket [" + ticket.getId()
					+ "] (chargeTime=" + ticket.getChargeTime()
					+ ", closureTime=" + ticket.getClosureTime() + ")");
			nextTicket = ticket.getId() + 1;
		}
		throw new RecallUpgradeException("t" + String.valueOf(nextTicket));
	}

	/**
	 * Upgrade the database to version 3.4.0 (update the charge and closure time of archived tickets).
	 * @param firstTicket
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d4d0ArchivedTickets(final long firstTicket) throws RecallUpgradeException {
		if (logger.isDebugEnabled()) {
			logger.debug("upgrade3d4d0ArchivedTickets()");
		}
		if (firstTicket > getDomainService().getLastArchivedTicketId()) {
			logger.info("no more archived ticket to upgrade.");
			return;
		}
		long nextTicket = firstTicket;
		for (ArchivedTicket archivedTicket : getDomainService().getArchivedTickets(
				firstTicket, TICKET_BATCH_SIZE)) {
			if (logger.isDebugEnabled()) {
				logger.debug("updating archived ticket #" + archivedTicket.getTicketId() + "...");
			}
			getDomainService().upgradeArchivedTicketTo3d4d0(archivedTicket);
			logger.info(
					"updated archived ticket [" + archivedTicket.getTicketId()
					+ "] (chargeTime=" + archivedTicket.getChargeTime()
					+ ", closureTime=" + archivedTicket.getClosureTime() + ")");
			nextTicket = archivedTicket.getId() + 1;
		}
		throw new RecallUpgradeException("a" + String.valueOf(nextTicket));
	}

	/**
	 * Upgrade the database to version 3.4.0 (charge and closure time of tickets).
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d4d0() throws RecallUpgradeException {
		logger.info("upgradeState = " + getDomainService().getUpgradeState());
		if (getDomainService().getUpgradeState() == null) {
			throw new RecallUpgradeException("t0");
		}
		if (getDomainService().getUpgradeState().startsWith("t")) {
			try {
				int firstTicket = Integer.valueOf(getDomainService().getUpgradeState().substring(1));
				if (logger.isDebugEnabled()) {
					logger.debug("firstTicket = " + firstTicket);
				}
				upgrade3d4d0Tickets(firstTicket);
			} catch (NumberFormatException e) {
				// see below
			}
			throw new RecallUpgradeException("a0");
		}
		if (getDomainService().getUpgradeState().startsWith("a")) {
			try {
				int firstTicket = Integer.valueOf(getDomainService().getUpgradeState().substring(1));
				if (logger.isDebugEnabled()) {
					logger.debug("firstTicket = " + firstTicket);
				}
				upgrade3d4d0ArchivedTickets(firstTicket);
			} catch (NumberFormatException e) {
				// see below
			}
			getDomainService().setUpgradeState(null);
			return;
		}
		throw new ConfigException("bad upgrade state ["
				+ getDomainService().getUpgradeState() + "]");
	}

	/**
	 * Upgrade the database to version 3.5.0 (update the manager display name of tickets).
	 * @param firstTicket
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d5d0Tickets(final long firstTicket) throws RecallUpgradeException {
		if (logger.isDebugEnabled()) {
			logger.debug("upgrade3d4d0Tickets()");
		}
		if (firstTicket > getDomainService().getLastTicketId()) {
			logger.info("no more ticket to upgrade.");
			return;
		}
		long nextTicket = firstTicket;
		for (Ticket ticket : getDomainService().getTickets(firstTicket, TICKET_BATCH_SIZE)) {
			if (logger.isDebugEnabled()) {
				logger.debug("updating ticket #" + ticket.getId() + "...");
			}
			ticket.setManager(ticket.getManager());
			getDomainService().updateTicket(ticket);
			logger.info("updated ticket [" + ticket.getId() + "]");
			nextTicket = ticket.getId() + 1;
		}
		throw new RecallUpgradeException("t" + String.valueOf(nextTicket));
	}

	/**
	 * Upgrade the database to version 3.5.0 (manager display name of tickets).
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d5d0() throws RecallUpgradeException {
		if (getDomainService().getUpgradeState() == null) {
			throw new RecallUpgradeException("t0");
		}
		if (getDomainService().getUpgradeState().startsWith("t")) {
			try {
				int firstTicket = Integer.valueOf(getDomainService().getUpgradeState().substring(1));
				if (logger.isDebugEnabled()) {
					logger.debug("firstTicket = " + firstTicket);
				}
				upgrade3d5d0Tickets(firstTicket);
			} catch (NumberFormatException e) {
				// see below
			}
			getDomainService().setUpgradeState(null);
			return;
		}
		throw new ConfigException("bad upgrade state ["
				+ getDomainService().getUpgradeState() + "]");
	}

	/**
	 * Upgrade the database to version 3.5.7 (property oldDefaultPriorityLevel of categories).
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	public void upgrade3d5d7() throws RecallUpgradeException {
		logger.info("upgradeState = " + getDomainService().getUpgradeState());
		getDomainService().setDefaultOldPriorityLevelToCategories();
	}

	/**
	 * Upgrade the database to version 3.6.0 (reorder categories).
	 * @param categories
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d6d0Categories(final List<Category> categories) {
		getDomainService().reorderCategories(categories);
		for (Category category : categories) {
			upgrade3d6d0Categories(getDomainService().getSubCategories(category));
		}
	}

	/**
	 * Upgrade the database to version 3.6.0 (reorder categories).
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d6d0() throws RecallUpgradeException {
		for (Department department : getDomainService().getDepartments()) {
			getDomainService().reorderCategories(getDomainService().getRootCategories(department));
		}
	}

	/**
	 * Upgrade the database to version 3.9.0 (reorder departments).
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d9d0() throws RecallUpgradeException {
		getDomainService().reorderDepartments(getDomainService().getDepartments());
	}

	/**
	 * Upgrade the database to version 3.10.0 (insert default department selection config).
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d10d0() throws RecallUpgradeException {
		String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		data += "<department-selection>";
		data += new UserDefinedConditions();
		data += new Rules();
		Actions actions = new Actions();
		actions.addAction(new AddAllAction());
		data += actions;
		data += "</department-selection>";
		getDomainService().setDepartmentSelectionConfig(null, data);
	}

	/**
	 * Upgrade the database to version 3.10.3 (restore the consistency for moved tickets).
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d10d3() throws RecallUpgradeException {
		for (Ticket ticket : getDomainService().getTickets(0, Integer.MAX_VALUE)) {
			if (!ticket.getDepartment().equals(ticket.getCategory().getDepartment())) {
				logger.info("deleting inconsistent ticket #" + ticket.getId() + "...");
				getDomainService().deleteTicket(ticket, true);
			}
		}
	}

	/**
	 * Upgrade the database to version 3.14.0 (added icons).
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	public void upgrade3d14d0() throws RecallUpgradeException {
		getDomainService().setTicketsLastIndexTime(
				getDomainService().getDeprecatedTicketsLastIndexTime());
		getDomainService().setArchivedTicketsLastIndexTime(
				getDomainService().getDeprecatedArchivedTicketsLastIndexTime());
		getDomainService().setFaqsLastIndexTime(
				getDomainService().getDeprecatedFaqContainersLastIndexTime());
		for (String iconName : ICONS_3_14_0) {
			getDomainService().createIconFromLocalPngFile(iconName);
		}
		getDomainService().setDefaultDepartmentIcon(
				getDomainService().getIconByName("defaultDepartment"));
		getDomainService().setDefaultCategoryIcon(
				getDomainService().getIconByName("defaultCategory"));

	}

	/**
	 * Upgrade the database to version 3.15.2 (added a few icons).
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d15d2() throws RecallUpgradeException {
		for (String iconName : ICONS_3_15_2) {
			getDomainService().createIconFromLocalPngFile(iconName);
		}
		for (Category category : getDomainService().getCategories()) {
			if (category.getInheritFaqLinks() == null) {
				category.setInheritFaqLinks(true);
				getDomainService().updateCategory(category);
			}
		}
	}

	/**
	 * Upgrade the database to version 3.16.0 (update the FAQ containers).
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d16d0FaqContainers() throws RecallUpgradeException {
		for (DeprecatedFaqContainer faqContainer : getDomainService().getFaqContainers()) {
			if (logger.isDebugEnabled()) {
				logger.debug("updating FAQ container #" + faqContainer.getId() + "...");
			}
			if (fckEditorCodeCleaner.removeMaliciousTags(faqContainer)) {
				getDomainService().updateFaqContainer(faqContainer);
				logger.info("updated FAQ container [" + faqContainer.getId() + "]");
			}
		}
	}

	/**
	 * Upgrade the database to version 3.16.0 (update the FAQ entries).
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d16d0FaqEntries() throws RecallUpgradeException {
		for (DeprecatedFaqEntry faqEntry : getDomainService().getFaqEntries()) {
			if (logger.isDebugEnabled()) {
				logger.debug("updating FAQ entry #" + faqEntry.getId() + "...");
			}
			if (fckEditorCodeCleaner.removeMaliciousTags(faqEntry)) {
				getDomainService().updateFaqEntry(faqEntry);
				logger.info("updated FAQ entry [" + faqEntry.getId() + "]");
			}
		}
	}

	/**
	 * Upgrade the database to version 3.16.0 (update the actions).
	 * @param firstAction
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d16d0Actions(final long firstAction) throws RecallUpgradeException {
		if (firstAction > getDomainService().getLastActionId()) {
			logger.info("no more action to upgrade.");
			return;
		}
		long nextAction = firstAction;
		for (Action action : getDomainService().getActions(firstAction, ACTION_BATCH_SIZE)) {
			if (logger.isDebugEnabled()) {
				logger.debug("updating action #" + action.getId() + "...");
			}
			if (fckEditorCodeCleaner.removeMaliciousTags(action)) {
				getDomainService().updateAction(action);
				logger.info("updated action [" + action.getId() + "]");
			}
			nextAction = action.getId() + 1;
		}
		throw new RecallUpgradeException("a" + String.valueOf(nextAction));
	}

	/**
	 * Upgrade the database to version 3.16.0 (update the archived actions).
	 * @param firstAction
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	protected void upgrade3d16d0ArchivedActions(final long firstAction) throws RecallUpgradeException {
		if (firstAction > getDomainService().getLastArchivedActionId()) {
			logger.info("no more archived action to upgrade.");
			return;
		}
		long nextAction = firstAction;
		for (ArchivedAction archivedAction
				: getDomainService().getArchivedActions(firstAction, ACTION_BATCH_SIZE)) {
			if (logger.isDebugEnabled()) {
				logger.debug("updating archived action #" + archivedAction.getId() + "...");
			}
			if (fckEditorCodeCleaner.removeMaliciousTags(archivedAction)) {
				getDomainService().updateArchivedAction(archivedAction);
				logger.info("updated archived action [" + archivedAction.getId() + "]");
			}
			nextAction = archivedAction.getId() + 1;
		}
		throw new RecallUpgradeException("b" + String.valueOf(nextAction));
	}

	/**
	 * Upgrade the database to version 3.16.0 (fixed script tags).
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d16d0() throws RecallUpgradeException {
		logger.info("upgradeState = " + getDomainService().getUpgradeState());
		if (getDomainService().getUpgradeState() == null) {
			throw new RecallUpgradeException("fe");
		}
		if ("fc".equals(getDomainService().getUpgradeState())) {
			upgrade3d16d0FaqContainers();
			throw new RecallUpgradeException("fe");
		}
		if ("fe".equals(getDomainService().getUpgradeState())) {
			upgrade3d16d0FaqEntries();
			throw new RecallUpgradeException("a0");
		}
		if (getDomainService().getUpgradeState().startsWith("a")) {
			try {
				int firstAction = Integer.valueOf(getDomainService().getUpgradeState().substring(1));
				if (logger.isDebugEnabled()) {
					logger.debug("firstAction = " + firstAction);
				}
				upgrade3d16d0Actions(firstAction);
				throw new RecallUpgradeException("b0");
			} catch (NumberFormatException e) {
				// see below
			}
		}
		if (getDomainService().getUpgradeState().startsWith("b")) {
			try {
				int firstAction = Integer.valueOf(getDomainService().getUpgradeState().substring(1));
				if (logger.isDebugEnabled()) {
					logger.debug("firstArchivedAction = " + firstAction);
				}
				upgrade3d16d0ArchivedActions(firstAction);
				getDomainService().setUpgradeState(null);
				return;
			} catch (NumberFormatException e) {
				// see below
			}
		}
		throw new ConfigException("bad upgrade state ["
				+ getDomainService().getUpgradeState() + "]");
	}

	/**
	 * Upgrade the database to version 3.17.0 (delete empty action messages).
	 */
	@SuppressWarnings("deprecation")
	public void upgrade3d17d0() {
		getDomainService().setToNullEmpyActionMessages();
	}

	/**
	 * Upgrade the database to version 3.17.3 (set Category.inheritFaqLinks).
	 */
	public void upgrade3d17d3() {
		for (Category category : getDomainService().getCategories()) {
			if (category.getInheritFaqLinks()) {
				category.setInheritFaqLinks(true);
				getDomainService().updateCategory(category);
			}
		}
	}

	/**
	 * Upgrade the database to version 3.18.0 (clear all the history items).
	 */
	public void upgrade3d18d0() {
		getDomainService().clearHistoryItems();
	}

	/**
	 * Upgrade the database to version 3.19.2 (clear empty default ticket messages).
	 */
	public void upgrade3d19d2() {
		for (Department department : getDomainService().getDepartments()) {
			department.setDefaultTicketMessage(department.getDefaultTicketMessage());
			getDomainService().updateDepartment(department);
		}
		for (Category category : getDomainService().getCategories()) {
			category.setDefaultTicketMessage(category.getDefaultTicketMessage());
			getDomainService().updateCategory(category);
		}
	}

	/**
	 * Upgrade the database to version 3.21.0 (update sequence IDs for PostgreSQL).
	 */
	@SuppressWarnings("deprecation")
	public void upgrade3d21d0() {
		getDomainService().updateBeanSequence("Action", "s_acti");
		getDomainService().updateBeanSequence("Alert", "s_aler");
		getDomainService().updateBeanSequence("ArchivedAction", "s_arch_acti");
		getDomainService().updateBeanSequence("ArchivedFileInfo", "s_arch_file");
		getDomainService().updateBeanSequence("ArchivedInvitation", "s_arch_invi");
		getDomainService().updateBeanSequence("ArchivedTicket", "s_arch_tick");
		getDomainService().updateBeanSequence("Bookmark", "s_book");
		getDomainService().updateBeanSequence("Category", "s_cate");
		getDomainService().updateBeanSequence("CategoryMember", "s_cate_memb");
		getDomainService().updateBeanSequence("Config", "s_conf");
		getDomainService().updateBeanSequence("DeletedItem", "s_dele_item");
		getDomainService().updateBeanSequence("Department", "s_depa");
		getDomainService().updateBeanSequence("DepartmentInvitation", "s_depa_invi");
		getDomainService().updateBeanSequence("DepartmentManager", "s_depa_mana");
		getDomainService().updateBeanSequence("DepartmentSelectionConfig", "s_depa_sele_conf");
		getDomainService().updateBeanSequence("FaqContainer", "s_faq");
		getDomainService().updateBeanSequence("FaqEntry", "s_faq_entr2");
		getDomainService().updateBeanSequence("FaqLink", "s_faq_link");
		getDomainService().updateBeanSequence("FileInfo", "s_file2");
		getDomainService().updateBeanSequence("HistoryItem", "s_hist_item");
		getDomainService().updateBeanSequence("Icon", "s_icon");
		getDomainService().updateBeanSequence("Invitation", "s_invi");
		getDomainService().updateBeanSequence("Response", "s_resp");
		getDomainService().updateBeanSequence("State", "s_stat");
		getDomainService().updateBeanSequence("Ticket", "s_tick");
		getDomainService().updateBeanSequence("TicketMonitoring", "s_tick_moni");
		getDomainService().updateBeanSequence("TicketView", "s_tick_view");
		getDomainService().updateBeanSequence("VersionManager", "s_vers_mana");
	}

	/**
	 * Upgrade the database to version 3.22.0 (update report preferences).
	 */
	public void upgrade3d22d0() {
		for (Department department : getDomainService().getDepartments()) {
			for (DepartmentManager manager : getDomainService().getDepartmentManagers(department)) {
				String reportType = manager.getReportType();
				if ("FREE".equals(reportType)) {
					manager.setReportType(DepartmentManager.REPORT_MF);
					getDomainService().updateDepartmentManager(manager);
				} else if ("CATEGORY_MEMBER".equals(reportType)) {
					manager.setReportType(DepartmentManager.REPORT_MCF);
					getDomainService().updateDepartmentManager(manager);
				} else if ("ALL".equals(reportType)) {
					manager.setReportType(DepartmentManager.REPORT_MCFO);
					getDomainService().updateDepartmentManager(manager);
				}
			}
		}
	}

	/**
	 * Upgrade the database to version 3.24.0 (update the users).
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	public void upgrade3d24d0() throws RecallUpgradeException {
		logger.info("upgradeState = " + getDomainService().getUpgradeState());
		if (getDomainService().getUpgradeState() == null) {
			throw new RecallUpgradeException("cu");
		}
		if ("cu".equals(getDomainService().getUpgradeState())) {
			List<User> users = getDomainService().getUsersWithNullAuthType(USER_BATCH_SIZE);
			if (users.isEmpty()) {
				throw new RecallUpgradeException("1");
			}
			for (User user : users) {
				User newUser = new User(user);
				if (user.getId().contains("@")) {
					newUser.setAuthType(AuthUtils.APPLICATION);
					newUser.setId(getUserStore().getApplicationUserId(user.getId()));
				} else {
					newUser.setAuthType(AuthUtils.CAS);
					newUser.setId(getUserStore().getCasUserId(user.getId()));
				}
				newUser.setRealId(user.getId());
				getDomainService().addUser(newUser);
				logger.info("added user " + newUser.getId());
				user.setAuthType(AuthUtils.NONE);
				getDomainService().updateUser(user);
			}
			throw new RecallUpgradeException("cu");
		}
		if ("1".equals(getDomainService().getUpgradeState())) {
			logger.info("updating actions...");
			getDomainService().upgradeUserKeys(
					Action.class.getSimpleName(), "managerBefore");
			getDomainService().upgradeUserKeys(
					Action.class.getSimpleName(), "managerAfter");
			getDomainService().upgradeUserKeys(
					Action.class.getSimpleName(), "invitedUser");
			getDomainService().upgradeUserKeys(
					Action.class.getSimpleName(), "user");
			getDomainService().upgradeUserKeys(
					Action.class.getSimpleName(), "ticketOwnerBefore");
			getDomainService().upgradeUserKeys(
					Action.class.getSimpleName(), "ticketOwnerAfter");
			throw new RecallUpgradeException("2");
		}
		if ("2".equals(getDomainService().getUpgradeState())) {
			logger.info("updating alerts...");
			getDomainService().upgradeUserKeys(
					Alert.class.getSimpleName(), "user");
			throw new RecallUpgradeException("3");
		}
		if ("3".equals(getDomainService().getUpgradeState())) {
			logger.info("updating tickets...");
			getDomainService().upgradeUserKeys(
					Ticket.class.getSimpleName(), "owner");
			getDomainService().upgradeUserKeys(
					Ticket.class.getSimpleName(), "creator");
			getDomainService().upgradeUserKeys(
					Ticket.class.getSimpleName(), "manager");
			throw new RecallUpgradeException("4");
		}
		if ("4".equals(getDomainService().getUpgradeState())) {
			logger.info("updating archived actions...");
			getDomainService().upgradeUserKeys(
					ArchivedAction.class.getSimpleName(), "managerBefore");
			getDomainService().upgradeUserKeys(
					ArchivedAction.class.getSimpleName(), "managerAfter");
			getDomainService().upgradeUserKeys(
					ArchivedAction.class.getSimpleName(), "invitedUser");
			getDomainService().upgradeUserKeys(
					ArchivedAction.class.getSimpleName(), "user");
			getDomainService().upgradeUserKeys(
					ArchivedAction.class.getSimpleName(), "ticketOwnerBefore");
			getDomainService().upgradeUserKeys(
					ArchivedAction.class.getSimpleName(), "ticketOwnerAfter");
			throw new RecallUpgradeException("5");
		}
		if ("5".equals(getDomainService().getUpgradeState())) {
			logger.info("updating archived tickets...");
			getDomainService().upgradeUserKeys(
					ArchivedTicket.class.getSimpleName(), "creator");
			getDomainService().upgradeUserKeys(
					ArchivedTicket.class.getSimpleName(), "manager");
			getDomainService().upgradeUserKeys(
					ArchivedTicket.class.getSimpleName(), "owner");
			throw new RecallUpgradeException("6");
		}
		if ("6".equals(getDomainService().getUpgradeState())) {
			logger.info("updating ticket views...");
			getDomainService().upgradeUserKeys(
					TicketView.class.getSimpleName(), "user");
			throw new RecallUpgradeException("7");
		}
		if ("7".equals(getDomainService().getUpgradeState())) {
			logger.info("updating files...");
			getDomainService().upgradeUserKeys(
					FileInfo.class.getSimpleName(), "user");
			logger.info("updating archived files...");
			getDomainService().upgradeUserKeys(
					ArchivedFileInfo.class.getSimpleName(), "user");
			logger.info("updating archived invitations...");
			getDomainService().upgradeUserKeys(
					ArchivedInvitation.class.getSimpleName(), "user");
			logger.info("updating archived files...");
			getDomainService().upgradeUserKeys(
					ArchivedFileInfo.class.getSimpleName(), "user");
			logger.info("updating bookmarks...");
			getDomainService().upgradeUserKeys(
					Bookmark.class.getSimpleName(), "user");
			logger.info("updating categoryMembers...");
			getDomainService().upgradeUserKeys(
					CategoryMember.class.getSimpleName(), "user");
			logger.info("updating department invitations...");
			getDomainService().upgradeUserKeys(
					DepartmentInvitation.class.getSimpleName(), "user");
			logger.info("updating department managers...");
			getDomainService().upgradeUserKeys(
					DepartmentManager.class.getSimpleName(), "user");
			logger.info("updating department selection...");
			getDomainService().upgradeUserKeys(
					DepartmentSelectionConfig.class.getSimpleName(), "user");
			logger.info("updating history items...");
			getDomainService().upgradeUserKeys(
					HistoryItem.class.getSimpleName(), "user");
			logger.info("updating invitations...");
			getDomainService().upgradeUserKeys(
					Invitation.class.getSimpleName(), "user");
			logger.info("updating responses...");
			getDomainService().upgradeUserKeys(
					Response.class.getSimpleName(), "user");
			logger.info("updating ticket monitoring...");
			getDomainService().upgradeUserKeys(
					TicketMonitoring.class.getSimpleName(), "user");
			throw new RecallUpgradeException("du");
		}
		if ("du".equals(getDomainService().getUpgradeState())) {
			logger.info("deleting old users...");
			getDomainService().deleteUsersWithNoneAuthType();
			throw new RecallUpgradeException("cd");
		}
		if ("cd".equals(getDomainService().getUpgradeState())) {
			logger.info("updating departments and categories...");
			for (Department department : getDomainService().getDepartments()) {
				if (department.getMonitoringLocalEmails()) {
					department.setMonitoringEmailAuthType(AuthUtils.CAS);
				} else {
					department.setMonitoringEmailAuthType(AuthUtils.APPLICATION);
				}
				getDomainService().updateDepartment(department);
			}
			for (Category category : getDomainService().getCategories()) {
				if (category.getMonitoringLocalEmails()) {
					category.setMonitoringEmailAuthType(AuthUtils.CAS);
				} else {
					category.setMonitoringEmailAuthType(AuthUtils.APPLICATION);
				}
				getDomainService().updateCategory(category);
			}
			getDomainService().deleteUsersWithNoneAuthType();
			throw new RecallUpgradeException("ds");
		}
		if ("ds".equals(getDomainService().getUpgradeState())) {
			logger.info("updating the department selection configuration...");
			getDomainService().setDepartmentSelectionConfig(
					null,
					getDomainService().getDepartmentSelectionConfig().getData());
			getDomainService().setUpgradeState(null);
			return;
		}
		throw new ConfigException("bad upgrade state ["
				+ getDomainService().getUpgradeState() + "]");
	}

	/**
	 * Upgrade the database to version 3.26.0 (update the FAQs).
	 * @throws RecallUpgradeException
	 */
	@SuppressWarnings("deprecation")
	public void upgrade3d26d0() throws RecallUpgradeException {
		logger.info("upgradeState = " + getDomainService().getUpgradeState());
		if (getDomainService().getUpgradeState() == null) {
			throw new RecallUpgradeException("f");
		}
		if ("f".equals(getDomainService().getUpgradeState())) {
			getDomainService().migrateFaqContainers();
			getDomainService().setUpgradeState(null);
			return;
		}
		throw new ConfigException("bad upgrade state ["
				+ getDomainService().getUpgradeState() + "]");
	}

	/**
	 * Upgrade the database to version 3.27.2 (remove the index).
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d27d2() throws RecallUpgradeException {
		indexer.removeIndex();
	}

	/**
	 * Upgrade the database to version 3.28.3 (remove the deprecated tags of the visibility rules).
	 * @throws RecallUpgradeException
	 */
	public void upgrade3d28d3() throws RecallUpgradeException {
		DepartmentSelectionConfig config = getDomainService().getDepartmentSelectionConfig();
		getDomainService().setDepartmentSelectionConfig(null, config.getData());
	}

	/**
	 * Upgrade the database to a given version, if needed.
	 * @param version
	 * @throws RecallUpgradeException when the transaction should be committed
	 * and the upgrade method be called again.
	 */
	protected void upgradeDatabaseIfNeeded(final String version) throws RecallUpgradeException {
		if (!getDatabaseVersion().isOlderThan(version)) {
			return;
		}
		printOlderThanMessage(version);
		String methodName = "upgrade" + version.replace('.', 'd');
		@SuppressWarnings("rawtypes")
		Class [] methodArgs = new Class [] {};
		Method method;
		try {
			method = getClass().getMethod(methodName, methodArgs);
		} catch (SecurityException e) {
			throw new ConfigException(
					"access to the information of class " + getClass() + " was denied", e);
		} catch (NoSuchMethodException e) {
			throw new ConfigException(
					"could no find method " + getClass() + "." + methodName + "()", e);
		}
		Exception invocationException = null;
		try {
			method.invoke(this, new Object[] {});
			setDatabaseVersion(version, false);
			return;
		} catch (IllegalArgumentException e) {
			invocationException = e;
		} catch (IllegalAccessException e) {
			invocationException = e;
		} catch (InvocationTargetException e) {
			if (e.getCause() == null) {
				invocationException = e;
			} else if (e.getCause() instanceof RecallUpgradeException) {
				RecallUpgradeException rue = (RecallUpgradeException) e.getCause();
				if (logger.isDebugEnabled()) {
					logger.debug("upgradeDatabaseIfNeeded(): caught RecallUpgradeException("
							+ rue.getNewUpgradeState() + ")");
				}
				getDomainService().setUpgradeState(rue.getNewUpgradeState());
				throw rue;
			} else if (e.getCause() instanceof Exception) {
				invocationException = (Exception) e.getCause();
			} else {
				invocationException = e;
			}
		}
		throw new ConfigException(
				"could no invoke method " + getClass() + "." + methodName + "()",
				invocationException);
	}

	/**
	 * @see org.esupportail.commons.services.application.VersionningService#upgradeDatabase()
	 */
	@Override
	public boolean upgradeDatabase() {
		if (getDatabaseVersion().equals(getApplicationService().getVersion())) {
			logger.info("The database is up to date, no need to upgrade.");
			return false;
		}
		DatabaseUtils.update();
		try {
			upgradeDatabaseIfNeeded("3.0.0");
			upgradeDatabaseIfNeeded("3.1.0");
			upgradeDatabaseIfNeeded("3.2.3");
			upgradeDatabaseIfNeeded("3.4.0");
			upgradeDatabaseIfNeeded("3.5.0");
			upgradeDatabaseIfNeeded("3.5.7");
			upgradeDatabaseIfNeeded("3.6.0");
			upgradeDatabaseIfNeeded("3.9.0");
			upgradeDatabaseIfNeeded("3.10.0");
			upgradeDatabaseIfNeeded("3.10.3");
			upgradeDatabaseIfNeeded("3.14.0");
			upgradeDatabaseIfNeeded("3.15.2");
			upgradeDatabaseIfNeeded("3.16.0");
			upgradeDatabaseIfNeeded("3.17.0");
			upgradeDatabaseIfNeeded("3.17.3");
			upgradeDatabaseIfNeeded("3.18.0");
			upgradeDatabaseIfNeeded("3.19.2");
			upgradeDatabaseIfNeeded("3.21.0");
			upgradeDatabaseIfNeeded("3.22.0");
			upgradeDatabaseIfNeeded("3.24.0");
			upgradeDatabaseIfNeeded("3.26.0");
			upgradeDatabaseIfNeeded("3.27.2");
			upgradeDatabaseIfNeeded("3.28.3");
		} catch (RecallUpgradeException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("upgradeDatabase(): caught RecallUpgradeException("
						+ e.getNewUpgradeState() + ")");
			}
			return true;
		}
		if (!getDatabaseVersion().equals(getApplicationService().getVersion())) {
			setDatabaseVersion(getApplicationService().getVersion().toString(), false);
		}
		return false;
	}

	/**
	 * @return the firstAdministratorId
	 */
	public String getFirstAdministratorId() {
		return firstAdministratorId;
	}

	/**
	 * @param firstAdministratorId the firstAdministratorId to set
	 */
	public void setFirstAdministratorId(final String firstAdministratorId) {
		this.firstAdministratorId = StringUtils.nullIfEmpty(firstAdministratorId);
	}

	/**
	 * @param indexer the indexer to set
	 */
	public void setIndexer(final Indexer indexer) {
		this.indexer = indexer;
	}

	/**
	 * @return the departmentManagerConfigurator
	 */
	protected DepartmentManagerConfigurator getDepartmentManagerConfigurator() {
		return departmentManagerConfigurator;
	}

	/**
	 * @param departmentManagerConfigurator the departmentManagerConfigurator to set
	 */
	public void setDepartmentManagerConfigurator(
			final DepartmentManagerConfigurator departmentManagerConfigurator) {
		this.departmentManagerConfigurator = departmentManagerConfigurator;
	}

	/**
	 * @return the fckEditorCodeCleaner
	 */
	protected FckEditorCodeCleaner getFckEditorCodeCleaner() {
		return fckEditorCodeCleaner;
	}

	/**
	 * @param fckEditorCodeCleaner the fckEditorCodeCleaner to set
	 */
	public void setFckEditorCodeCleaner(final FckEditorCodeCleaner fckEditorCodeCleaner) {
		this.fckEditorCodeCleaner = fckEditorCodeCleaner;
	}

}
