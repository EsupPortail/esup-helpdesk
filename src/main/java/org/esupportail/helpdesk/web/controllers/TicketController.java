/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;
import org.apache.myfaces.custom.tree2.TreeState;
import org.apache.myfaces.custom.tree2.TreeStateBase;
import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.web.beans.Paginator;
import org.esupportail.commons.web.beans.TreeModelBase;
import org.esupportail.commons.web.controllers.LdapSearchCaller;
import org.esupportail.helpdesk.domain.ActionScope;
import org.esupportail.helpdesk.domain.ActionType;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.TicketScope;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.CategoryMember;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.FaqLink;
import org.esupportail.helpdesk.domain.beans.FileInfo;
import org.esupportail.helpdesk.domain.beans.Invitation;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.exceptions.ArchivedTicketNotFoundException;
import org.esupportail.helpdesk.exceptions.DepartmentManagerNotFoundException;
import org.esupportail.helpdesk.exceptions.FileException;
import org.esupportail.helpdesk.exceptions.TicketNotFoundException;
import org.esupportail.helpdesk.web.beans.AbstractFirstLastNode;
import org.esupportail.helpdesk.web.beans.ActionScopeI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.CategoryNode;
import org.esupportail.helpdesk.web.beans.ControlPanelColumnOrderer;
import org.esupportail.helpdesk.web.beans.ControlPanelEntry;
import org.esupportail.helpdesk.web.beans.FaqNode;
import org.esupportail.helpdesk.web.beans.FaqTreeModel;
import org.esupportail.helpdesk.web.beans.OriginI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.PriorityI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.SpentTimeI18nFormatter;
import org.esupportail.helpdesk.web.beans.TempUploadedFile;
import org.esupportail.helpdesk.web.beans.TicketScopeI18nKeyProvider;

/**
 * The controller for tickets.
 */
public class TicketController extends TicketControllerStateHolder implements LdapSearchCaller {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7552300471316001385L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The max day for the spent time.
	 * The max day for the spent time.
	 */
	private static final long MAX_DAY_ITEM = 30;

	/**
	 * The step for the spent time minute selection.
	 */
	private static final long MINUTE_ITEM_STEP = 5;

	/**
	 * A back page value.
	 */
	private static final String BACK_PAGE_CONTROL_PANEL = "controlPanel";

	/**
	 * A back page value.
	 */
	private static final String BACK_PAGE_SEARCH = "search";

	/**
	 * A back page value.
	 */
	private static final String BACK_PAGE_JOURNAL = "journal";

	/**
	 * A back page value.
	 */
	private static final String BACK_PAGE_BOOKMARKS = "bookmarks";

	/**
	 * A back page value.
	 */
	private static final String BACK_PAGE_STATISTICS = "statistics";

	/**
	 * The max length of action messages.
	 */
	private static final int ACTION_MESSAGE_MAX_LENGTH = 8192;

	/**
	 * The max number of recent invitations.
	 */
	private static final int RECENT_INVITATIONS_MAX_NUMBER = 20;

    /**
     * The control panel paginator.
     */
    private Paginator<ControlPanelEntry> paginator;

	/**
     * True to show alerts.
     */
    private boolean showAlerts;

    /**
     * The message of the actions.
     */
    private String actionMessage;

    /**
     * The scope of the actions.
     */
    private String actionScope;

    /**
     * True not to send alerts on actions.
     */
    private boolean noAlert;

    /**
     * True not to send alerts on actions.
     */
    private boolean scopeNoAlert;

    /**
     * True not to send alerts on actions.
     */
    private boolean originNoAlert;

    /**
     * True not to send alerts on actions.
     */
    private boolean priorityNoAlert;

    /**
     * True not to send alerts on actions.
     */
    private boolean labelNoAlert;

    /**
     * True not to send alerts on actions.
     */
    private boolean computerNoAlert;

    /**
     * True not to send alerts on actions.
     */
    private boolean uploadNoAlert;

    /**
     * True not to send alerts on actions.
     */
    private boolean spentTimeNoAlert;

	/**
	 * The action to update.
	 */
	private Action actionToUpdate;

	/**
	 * The FileInfo to update.
	 */
	private FileInfo fileInfoToUpdate;

	/**
	 * The LDAP uid.
	 */
	private String ldapUid;

	/**
	 * The target category used when moving tickets.
	 */
	private Category moveTargetCategory;

	/**
	 * The target category used when adding tickets.
	 */
	private Category addTargetCategory;

	/**
	 * The target department.
	 */
	private Department addTargetDepartment;

	/**
	 * The tree for the FAQ links used when adding tickets.
	 */
	private FaqTreeModel addFaqTree;

	/**
	 * True to show the help page when entering a ticket.
	 */
	private boolean showAddHelp;

	/**
	 * The target ticket id.
	 */
	private Long targetTicketId;

	/**
	 * The target ticket.
	 */
	private Ticket targetTicket;

	/**
	 * The target archived ticket.
	 */
	private ArchivedTicket targetArchivedTicket;

	/**
	 * The target FAQ.
	 */
	private Faq targetFaq;

	/**
	 * The tree model to move tickets.
	 */
	private TreeModelBase moveTree;

	/**
	 * The tree model to move tickets.
	 */
	private TreeModelBase filteredTree;

	/**
	 * The tree model to add tickets.
	 */
	private TreeModelBase addTree;

	/**
	 * The uploaded file.
	 */
    private UploadedFile uploadedFile;

	/**
	 * The uploadeded files.
	 */
    private List<TempUploadedFile> tempUploadedFiles;

    /**
     * The index of the uploaded file to delete.
     */
    private int tempUploadedFileToDeleteIndex;

    /**
     * The FileInfo to download.
     */
    private FileInfo fileInfoToDownload;

    /**
     * The invitation to delete.
     */
    private Invitation invitationToDelete;

    /**
     * The target manager (to assign tickets).
     */
    private User targetManager;

    /**
     * The page to go when clicking on the 'back' button'.
     */
    private String backPage;

    /**
     * True to free the ticket after a closure.
     */
    private boolean freeTicketAfterClosure;

    /**
     * True when the postpone date has been set.
     */
    private boolean postponeDateSet;

    /**
     * The postpone date.
     */
    private Timestamp postponeDate;

    /**
     * True to monitor the ticket if the new department is not managed when moving a ticket.
     */
    private boolean looseTicketManagementMonitor;

    /**
     * True to invite if the new department is not managed when moving a ticket.
     */
    private boolean looseTicketManagementInvite;

    /**
     * True to show the connections on closure.
     */
    private boolean showConnectOnClosure;

	/**
	 * The tree model to connect to a FAQ entity.
	 */
	private FaqTreeModel faqTree;

	/**
	 * The action to return when finish with ticket connection.
	 */
	private String connectBackAction;

	/**
	 * The download id.
	 */
	private Long downloadId;

	/**
	 * The action scope to set.
	 */
	private String actionScopeToSet;

	/**
	 * The fileInfo scope to set.
	 */
	private String fileInfoScopeToSet;

	/**
	 * The fileInfo attached.
	 */
	private FileInfo fileInfoToDelete;

	/**
	 * The tree model to invite groups.
	 */
	private TreeModelBase inviteTree;

	/**
	 * True to free when moving a ticket.
	 */
	private Boolean freeTicket;

    /**
     * The string of the searched categorie.
     */
    private String cateFilter;

    /**
     * The string of the searched categories.
     */    
    private String categoryMoveMembers;

    /**
     * The columns orderer.
     */
    private ControlPanelColumnOrderer columnsOrderer;

    /**
     * The items for missing columns.
     */
    private List<SelectItem> addColumnItems;


	/**
	 * Bean constructor.
	 */
	public TicketController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		showAlerts = false;
		resetAddTargetCategory();
		resetMoveTargetCategory();
		resetActionForm();
		moveTree = null;
		filteredTree = null;
		addTree = null;
		cateFilter = null;
		addFaqTree = null;
		uploadedFile = null;
		fileInfoToDownload = null;
		actionToUpdate = null;
		fileInfoToUpdate = null;
		invitationToDelete = null;
		backPage = BACK_PAGE_CONTROL_PANEL;
		scopeNoAlert = true;
		originNoAlert = true;
		priorityNoAlert = true;
		labelNoAlert = true;
		computerNoAlert = true;
		uploadNoAlert = false;
		spentTimeNoAlert = true;
		targetManager = null;
		faqTree = null;
		inviteTree = null;
		categoryMoveMembers = "";
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "["
		+ "ticket=[" + getTicket() + "]"
		+ ", ldapUid=[" + ldapUid + "]"
		+ ", addTargetCategory=[" + addTargetCategory + "]"
		+ ", addTargetDepartment=[" + addTargetDepartment + "]"
		+ "]";
	}

	/**
	 * @return true if the current user is allowed to access the view.
	 */
	public boolean isPageAuthorized() {
		return getDomainService().userCanViewTicket(getCurrentUser(), getClient(), getTicket());
	}

	/**
	 * Eclipse outline delimiter.
	 */
	public void ______________SELECT_ITEMS() {
		//
	}

	/**
	 * Add an action scope item.
	 * @param actionScopeItems
	 * @param scope
	 */
	private void addActionScopeItem(
			final List<SelectItem> actionScopeItems,
			final String scope) {
		actionScopeItems.add(new SelectItem(
				scope,
				getString(ActionScopeI18nKeyProvider.getI18nKey(scope))));
	}
	/**
	 * Add an action scope item.
	 * @param actionScopeItems
	 * @param actionScope
	 * @param ticketScope
	 */
	private void addActionScopeItem(
			final List<SelectItem> actionScopeItems,
			final String actionScope,
			final String ticketScope) {
		actionScopeItems.add(
				new SelectItem(actionScope,
				getString(ActionScopeI18nKeyProvider.getI18nKey(actionScope)) 
				+ " (" + 
				getString(TicketScopeI18nKeyProvider.getI18nKey(ticketScope)) 
				+ ")"));
	}

	
	/**
	 * @return the actionScopeItems for managers
	 */
	protected List<SelectItem> getManagerActionScopeItems() {
		List<SelectItem> managerActionScopeItems = getOwnerActionScopeItems();
		addActionScopeItem(managerActionScopeItems, ActionScope.MANAGER);
		return managerActionScopeItems;
	}

	/**
	 * @return the actionScopeItems for owners
	 */
	protected List<SelectItem> getOwnerActionScopeItems() {
		List<SelectItem> ownerActionScopeItems = getInvitedActionScopeItems();
		addActionScopeItem(ownerActionScopeItems, ActionScope.OWNER);
		return ownerActionScopeItems;
	}

	/**
	 * @return the actionScopeItems for invited users
	 */
	protected List<SelectItem> getInvitedActionScopeItems() {
		List<SelectItem> invitedActionScopeItems = new ArrayList<SelectItem>();
//		addActionScopeItem(invitedActionScopeItems, ActionScope.DEFAULT, getTicket().getEffectiveScope());
		addActionScopeItem(invitedActionScopeItems, ActionScope.INVITED);
		addActionScopeItem(invitedActionScopeItems, ActionScope.INVITED_MANAGER);
		return invitedActionScopeItems;
	}

	/**
	 * @return the actionScopeItems
	 */
	@RequestCache
	public List<SelectItem> getActionScopeItems() {
		Department department;
		if (getTicket() != null) {
			department = getTicket().getDepartment();
		} else {
			department = addTargetCategory.getDepartment();
		}
		if (getDomainService().isDepartmentManager(department, getCurrentUser())) {
			return getManagerActionScopeItems();
		}
		if (getTicket() == null || getTicket().getOwner().equals(getCurrentUser())) {
			return getOwnerActionScopeItems();
		}
		if (isInvited()) {
			return getInvitedActionScopeItems();
		}
		return null;
	}

	/**
	 * Add a ticket scope item.
	 * @param ticketScopeItems
	 * @param scope
	 */
	protected void addTicketScopeItem(
			final List<SelectItem> ticketScopeItems,
			final String scope) {
		ticketScopeItems.add(new SelectItem(
				scope,
				getString(TicketScopeI18nKeyProvider.getI18nKey(scope))));
	}

	/**
	 * @return the ticketScopeItems
	 */
	@RequestCache
	public List<SelectItem> getTicketScopeItems() {
		List<SelectItem> ticketScopeItems = new ArrayList<SelectItem>();
		addTicketScopeItem(ticketScopeItems, TicketScope.DEFAULT);
		addTicketScopeItem(ticketScopeItems, TicketScope.PUBLIC);
		addTicketScopeItem(ticketScopeItems, TicketScope.SUBJECT_ONLY);
		addTicketScopeItem(ticketScopeItems, TicketScope.PRIVATE);
		addTicketScopeItem(ticketScopeItems, TicketScope.CAS);
		return ticketScopeItems;
	}

	/**
	 * Add a ticket scope item.
	 * @param ticketPriorityItems
	 * @param priority
	 */
	protected void addTicketPriorityItem(
			final List<SelectItem> ticketPriorityItems,
			final Integer priority) {
		ticketPriorityItems.add(new SelectItem(
				priority,
				getString(PriorityI18nKeyProvider.getI18nKey(priority))));
	}

	/**
	 * @return the ticketPriorityItems
	 */
	@RequestCache
	public List<SelectItem> getTicketPriorityItems() {
		List<SelectItem> ticketPriorityItems = new ArrayList<SelectItem>();
		addTicketPriorityItem(ticketPriorityItems, DomainService.DEFAULT_PRIORITY_VALUE);
		for (Integer priority : getDomainService().getPriorities()) {
			addTicketPriorityItem(ticketPriorityItems, priority);
		}
		return ticketPriorityItems;
	}

	/**
	 * @return the ticketOriginItems
	 */
	@RequestCache
	public List<SelectItem> getOriginItems() {
		List<SelectItem> originItems = new ArrayList<SelectItem>();
		for (String origin : getDomainService().getOrigins()) {
			originItems.add(new SelectItem(
					origin,
					getString(OriginI18nKeyProvider.getI18nKey(origin))));
		}
		return originItems;
	}

	/**
	 * Add a day item.
	 * @param items
	 * @param day
	 */
	protected void addSpentTimeDayItem(
			final List<SelectItem> items,
			final long day) {
		items.add(new SelectItem(
				day, getString("TICKET_ACTION.SPENT_TIME.DAY_ITEM", day)));
	}

	/**
	 * Add a hour item.
	 * @param items
	 * @param hour
	 */
	protected void addSpentTimeHourItem(
			final List<SelectItem> items,
			final long hour) {
		items.add(new SelectItem(
				hour, getString("TICKET_ACTION.SPENT_TIME.HOUR_ITEM", hour)));
	}

	/**
	 * Add a minute item.
	 * @param items
	 * @param minute
	 */
	private void addSpentTimeMinuteItem(
			final List<SelectItem> items,
			final long minute) {
		items.add(new SelectItem(
				minute, getString("TICKET_ACTION.SPENT_TIME.MINUTE_ITEM", minute)));
	}

	/**
	 * @return the spentTimeDayItems
	 */
	@RequestCache
	public List<SelectItem> getSpentTimeDayItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
    	for (long day = 0; day < MAX_DAY_ITEM; day++) {
    		addSpentTimeDayItem(items, day);
    	}
    	return items;
	}

	/**
	 * @return the spentTimeHourItems
	 */
	@RequestCache
	public List<SelectItem> getSpentTimeHourItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
    	for (long h = 0; h < SpentTimeI18nFormatter.HOURS_PER_DAY; h++) {
    		addSpentTimeHourItem(items, h);
    	}
    	return items;
	}

	/**
	 * @return the spentTimeMinuteItems
	 */
	@RequestCache
	public List<SelectItem> getSpentTimeMinuteItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();

    	addSpentTimeMinuteItem(items, 1);
    	addSpentTimeMinuteItem(items, 2);
    	for (long m = MINUTE_ITEM_STEP; m < SpentTimeI18nFormatter.MINUTES_PER_HOUR; m += MINUTE_ITEM_STEP) {
    		addSpentTimeMinuteItem(items, m);
    	}
    	return items;
	}

	/**
	 * Eclipse outline delimiter.
	 */
	public void ______________ACTIONS() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.TicketControllerStateHolder#setTicket(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public void setTicket(final Ticket ticket) {
		resetMoveTargetCategory();
		setTargetFaq(null);
		resetTargetTicketInternal();
		super.setTicket(ticket);
		if (ticket != null && ticket.getConnectionTicket() != null) {
			targetTicket = ticket.getConnectionTicket();
			targetTicketId = targetTicket.getId();
		}
		if (ticket != null && ticket.getConnectionArchivedTicket() != null) {
			targetArchivedTicket = ticket.getConnectionArchivedTicket();
			targetTicketId = targetArchivedTicket.getTicketId();
		}
		if (ticket != null && ticket.getConnectionFaq() != null) {
			setTargetFaq(ticket.getConnectionFaq());
		}
	}

	/**
	 * refresh the current ticket.
	 */
	public void refreshTicket() {
		setTicket(getDomainService().reloadTicket(getTicket()));
	}

	/**
	 * @return true if the ticket has changed.
	 */
	protected boolean updateTicket() {
		Ticket upToDateTicket = getDomainService().reloadTicket(getTicket());
		if (getDomainService().hasTicketChangedSince(upToDateTicket, getTicket().getLastActionDate())) {
			addWarnMessage(
					null, "TICKET_VIEW.MESSAGE.TICKET_HAS_CHANGED",
					String.valueOf(getTicket().getId()));
			setTicket(upToDateTicket);
			return true;
		}
		return false;
	}

	/**
	 * @return false if the message is too long.
	 */
	protected boolean checkActionMessageLength() {
		if (actionMessage == null) {
			return true;
		}
		if (actionMessage.length() < ACTION_MESSAGE_MAX_LENGTH) {
			return true;
		}
		addWarnMessage(null, "TICKET_ACTION.MESSAGE.MESSAGE_TOO_LONG");
		return false;
	}

	/**
	 * Set the current ticket and update it.
	 * @param ticket
	 */
	public void setUpdatedTicket(final Ticket ticket) {
		Ticket upToDateTicket = getDomainService().reloadTicket(ticket);
		if (getDomainService().hasTicketChangedSince(upToDateTicket, ticket.getLastActionDate())) {
			addWarnMessage(
					null, "TICKET_VIEW.MESSAGE.TICKET_HAS_CHANGED",
					String.valueOf(getTicket().getId()));
		}
		setTicket(upToDateTicket);
	}

	/**
	 * Reset the action form.
	 */
	protected void resetActionForm() {
		actionMessage = null;
		actionScope = null;
		noAlert = false;
		ldapUid = null;
		freeTicketAfterClosure = false;
		tempUploadedFiles = null;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String back() {
		if (BACK_PAGE_SEARCH.equals(backPage)) {
			return "navigationSearch";
		}
		if (BACK_PAGE_JOURNAL.equals(backPage)) {
			return "navigationJournal";
		}
		if (BACK_PAGE_BOOKMARKS.equals(backPage)) {
			return "navigationBookmarks";
		}
		if (BACK_PAGE_STATISTICS.equals(backPage)) {
			return "navigationStatistics";
		}
		return "navigationControlPanel";
	}

	/**
	 * Limit the scope of the action depending on the user's role.
	 */
	protected void limitActionScope() {
		if (actionScope == null) {
			actionScope = ActionScope.DEFAULT;
		} else if (ActionScope.MANAGER.equals(actionScope)) {
			if (!getDomainService().isDepartmentManager(getTicket().getDepartment(), getCurrentUser())) {
				actionScope = ActionScope.DEFAULT;
			}
		} else if (ActionScope.OWNER.equals(actionScope)) {
			if (!getTicket().getOwner().equals(getCurrentUser())
					&& !getDomainService().isDepartmentManager(
							getTicket().getDepartment(), getCurrentUser())) {
				actionScope = ActionScope.DEFAULT;
			}
		} else if (ActionScope.INVITED.equals(actionScope) || ActionScope.INVITED_MANAGER.equals(actionScope)) {
			//on ne fait rien car tout le monde a au moins accès à ces deux choix 
		} else {
			actionScope = ActionScope.DEFAULT;
		}
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String cancelAction() {
		if (updateTicket() && !isPageAuthorized()) {
			addUnauthorizedActionMessage();
			return back();
		}
		resetActionForm();
		return "view";
	}

	/**
	 * Store a temp uploaded file.
	 * @return false on error.
	 */
	protected boolean storeTempUploadedFileInternal() {
		if (uploadedFile == null) {
			return true;
		}
		String filename = uploadedFile.getName();
		// a hack for IE
		if (filename.contains("\\")) {
			filename = filename.substring(filename.lastIndexOf('\\') + 1);
		}
 		if (uploadedFile.getSize() == 0) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.UPLOAD_ZERO");
			return false;
		}
		byte [] fileContent;
 		try {
			fileContent = uploadedFile.getBytes();
		} catch (IOException e) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.UPLOAD_ERROR", e.getMessage());
			return false;
		}
		if (tempUploadedFiles == null) {
			tempUploadedFiles = new ArrayList<TempUploadedFile>();
		}
		tempUploadedFiles.add(new TempUploadedFile(filename, fileContent));
		return true;
	}

	/**
	 * JSF callback.
	 */
	public void storeTempUploadedFile() {
		storeTempUploadedFileInternal();
	}

	/**
	 * JSF callback.
	 */
	public void deleteTempUploadedFile() {
		if (tempUploadedFiles == null) {
			return;
		}
		if (tempUploadedFileToDeleteIndex < 0 || tempUploadedFileToDeleteIndex > tempUploadedFiles.size() - 1) {
			return;
		}
		tempUploadedFiles.remove(tempUploadedFileToDeleteIndex);
	}

	/**
	 * Handle file upload.
	 * @param ticket
	 * @return false on error.
	 */
	protected boolean handleTempUploadedFiles(
			final Ticket ticket) {
		if (!storeTempUploadedFileInternal()) {
			return false;
		}
		if (tempUploadedFiles == null || tempUploadedFiles.isEmpty()) {
			return true;
		}
		Ticket theTicket;
		String theScope;
		if (ticket == null) {
			theTicket = getTicket();
			theScope = actionScope;
		} else {
			theTicket = ticket;
			theScope = ActionScope.DEFAULT;
		}
		for (TempUploadedFile tempUploadedFile : tempUploadedFiles) {
			getDomainService().uploadFile(
					getCurrentUser(), theTicket,
					tempUploadedFile.getName(), tempUploadedFile.getContent(),
					theScope);
		}
		tempUploadedFiles = null;
		return true;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String take() {
		boolean updated = updateTicket();
		if (!isUserCanTake()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "take";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doTake() {
		boolean updated = updateTicket();
		if (!isUserCanTake()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		getDomainService().takeTicket(getCurrentUser(), getTicket(), actionMessage, actionScope);
		resetActionForm();
		refreshTicket();
		return "taken";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String assign() {
		boolean updated = updateTicket();
		if (!isUserCanAssign()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		targetManager = null;
		return "assign";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doAssign() {
		boolean updated = updateTicket();
		if (!isUserCanAssign()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if (targetManager == null) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.ENTER_NEW_MANAGER");
			return null;
		}
		if (targetManager.equals(getTicket().getManager())) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.MANAGER_NOT_CHANGED");
			return null;
		}
		try {
			getDomainService().getDepartmentManager(getTicket().getDepartment(), targetManager);
		} catch (DepartmentManagerNotFoundException e) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.NOT_A_MANAGER", formatUser(targetManager));
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		getDomainService().assignTicket(
				getCurrentUser(), getTicket(), targetManager, actionMessage, actionScope);
		resetActionForm();
		refreshTicket();
		targetManager = null;
		return "assigned";
	}

	/**
	 * Reset the target ticket.
	 */
	public void resetTargetTicketInternal() {
		targetTicketId = null;
		targetTicket = null;
		targetArchivedTicket = null;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String resetTargetTicket() {
		boolean updated = updateTicket();
		if (!isUserCanConnect()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (!updated) {
			resetTargetTicketInternal();
		}
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String setTargetTicketFromId() {
		boolean updated = updateTicket();
		if (!isUserCanConnect()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if (targetTicketId == null || targetTicketId <= 0) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.ENTER_TARGET_TICKET_ID");
			return null;
		}
		try {
			targetTicket = getDomainService().getTicket(targetTicketId);
			if (!getDomainService().userCanViewTicket(getTicket().getOwner(), null, targetTicket)) {
				addWarnMessage(
						null, "TICKET_ACTION.MESSAGE.TARGET_TICKET_NOT_VISIBLE",
						String.valueOf(targetTicket.getId()),
						getTicket().getOwner().getId());
			}
			targetArchivedTicket = null;
			return connectBackAction;
		} catch (TicketNotFoundException e) {
			// will try the archived tickets
		}
		try {
			targetArchivedTicket = getDomainService().getArchivedTicketByOriginalId(targetTicketId);
			if (!getDomainService().userCanViewArchivedTicket(
					getTicket().getOwner(), null, targetArchivedTicket)) {
				addWarnMessage(
						null, "TICKET_ACTION.MESSAGE.TARGET_TICKET_NOT_VISIBLE",
						String.valueOf(targetArchivedTicket.getId()),
						getTicket().getOwner().getId());
			}
			targetTicket = null;
			return connectBackAction;
		} catch (ArchivedTicketNotFoundException e) {
			// will fail
		}
		addErrorMessage(null, "TICKET_ACTION.MESSAGE.TICKET_NOT_FOUND", Long.toString(targetTicketId));
		return null;
	}

	/**
	 * JSF callback (internal).
	 * @return a String.
	 */
	public String connectToTicket() {
		boolean updated = updateTicket();
		if (!isUserCanConnect()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "connectToTicket";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String resetTargetFaq() {
		boolean updated = updateTicket();
		if (!isUserCanConnect()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (!updated) {
			setTargetFaq(null);
		}
		return null;
	}

	/**
	 * Add FAQs to a FAQ tree.
	 * @param parentNode
	 * @param faqs
	 * @param visibleDepartments
	 */
	@SuppressWarnings("unchecked")
	protected void addFaqTreeFaqs(
			final FaqNode parentNode,
			final List<Faq> faqs,
			final List<Department> visibleDepartments) {
		for (Faq faq : faqs) {
			if (getDomainService().userCanViewFaq(
					getTicket().getOwner(), faq, visibleDepartments)) {
				FaqNode faqNode = new FaqNode(faq);
				addFaqTreeFaqs(faqNode, getDomainService().getSubFaqs(faq), visibleDepartments);
				AbstractFirstLastNode.markFirstAndLastChildNodes(faqNode);
				parentNode.getChildren().add(faqNode);
				parentNode.setLeaf(false);
			}
		}
	}

	/**
	 * @return the root node of the FAQ tree.
	 */
	@SuppressWarnings("unchecked")
	protected FaqNode buildFaqRootNode() {
		List<Department> visibleDepartments = getDomainService().getFaqViewDepartments(
				getTicket().getOwner(), null);
    	FaqNode rootNode = new FaqNode();
    	addFaqTreeFaqs(rootNode, getDomainService().getRootFaqs(), visibleDepartments);
    	for (Department department : getDomainService().getEnabledDepartments()) {
        	FaqNode departmentNode = new FaqNode(department);
        	addFaqTreeFaqs(
        			departmentNode,
        			getDomainService().getRootFaqs(department),
        			visibleDepartments);
    		if (departmentNode.getChildCount() > 0) {
            	rootNode.getChildren().add(departmentNode);
        		rootNode.setLeaf(false);
    		}
    	}
    	if (rootNode.getChildCount() == 0) {
    		return null;
    	}
    	AbstractFirstLastNode.markFirstAndLastChildNodes(rootNode);
    	return rootNode;
	}

	/**
	 * JSF callback (internal).
	 * @return a String.
	 */
	public String connectToFaq() {
		boolean updated = updateTicket();
		if (!isUserCanConnect()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		faqTree = null;
		FaqNode rootNode = buildFaqRootNode();
		if (rootNode != null) {
			faqTree = new FaqTreeModel(rootNode);
			TreeState treeState = new TreeStateBase();
			treeState.toggleExpanded("0");
			if (rootNode.getChildCount() == 1) {
				treeState.toggleExpanded("0:0");
			}
			faqTree.setTreeState(treeState);
		}
		return "connectToFaq";
	}

	/**
	 * JSF callback (internal).
	 * @param takeBefore
	 * @return a String.
	 */
	public String closeInternal(final boolean takeBefore) {
		boolean updated = updateTicket();
		boolean auth;
		if (takeBefore) {
			auth = isUserCanTakeAndClose();
		} else {
			auth = isUserCanClose();
		}
		if (!auth) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		showConnectOnClosure = false;
		if (takeBefore) {
			return "takeAndClose";
		}
		return "close";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String close() {
		return closeInternal(false);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String takeAndClose() {
		return closeInternal(true);
	}

	/**
	 * JSF callback (internal).
	 * @param takeBefore
	 * @return a String.
	 */
	protected String doCloseInternal(final boolean takeBefore) {
		boolean updated = updateTicket();
		boolean auth;
		if (takeBefore) {
			auth = isUserCanTakeAndClose();
		} else {
			auth = isUserCanClose();
		}
		if (!auth) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if(getCurrentUser().equals(getTicket().getOwner())) {
			doChangeTicketSpentTimeIfNeeded(
					false,false);
		}
		else if (!doChangeTicketSpentTimeIfNeeded(
				getTicket().getDepartment().isSpentTimeNeeded()
				&& getDomainService().isDepartmentManager(
						getTicket().getDepartment(), getCurrentUser()),
				false)) {
			return null;
		}
		if (targetTicket != null) {
			getDomainService().connectTicketToTicket(
					getTicket(), targetTicket);
		}
		if (targetArchivedTicket != null) {
			getDomainService().connectTicketToArchivedTicket(
					getTicket(), targetArchivedTicket);
		}
		if (targetFaq != null) {
			getDomainService().connectTicketToFaq(getTicket(), targetFaq);
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		if (takeBefore) {
			getDomainService().takeAndCloseTicket(
					getCurrentUser(), getTicket(),
					actionMessage, actionScope, freeTicketAfterClosure);
		} else {
			getDomainService().closeTicket(
					getCurrentUser(), getTicket(),
					actionMessage, actionScope, freeTicketAfterClosure);
		}
		resetActionForm();
		refreshTicket();
		if (!getDomainService().userShowsTicketAfterClosure(getCurrentUser())) {
			return back();
		}
		if (takeBefore) {
			return "takenAndClosed";
		}
		return "closed";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doClose() {
		return doCloseInternal(false);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doTakeAndClose() {
		return doCloseInternal(true);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String free() {
		boolean updated = updateTicket();
		if (!isUserCanFree()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "free";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doFree() {
		boolean updated = updateTicket();
		if (!isUserCanFree()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		doChangeTicketSpentTimeIfNeeded(false, false);
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		getDomainService().freeTicket(getCurrentUser(), getTicket(), actionMessage, actionScope);
		resetActionForm();
		refreshTicket();
		return "freed";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doChangeTicketScope() {
		boolean updated = updateTicket();
		if (!isUserCanChangeScope()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		
		
		if (getTicketScope() != null && !getTicketScope().equals(getTicket().getScope())) {
			if(getTicketScope().equals(TicketScope.PUBLIC)) {
				addWarnMessage(null, "TICKET_ACTION.MESSAGE.VISBILITE.TICKET.PUBLIC");
			}
			getDomainService().changeTicketScope(
					getCurrentUser(), getTicket(), getTicketScope(), !scopeNoAlert);
			resetActionForm();
			refreshTicket();
		}
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doChangeTicketLabel() {
		boolean updated = updateTicket();
		if (!isUserCanChangeLabel()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if (getTicketLabel() == null) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.LABEL_REQUIRED");
			return null;
		}
		if (!getTicketLabel().equals(getTicket().getLabel())) {
			getDomainService().changeTicketLabel(
					getCurrentUser(), getTicket(), getTicketLabel(), !labelNoAlert);
			resetActionForm();
			refreshTicket();
		}
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doChangeTicketOrigin() {
		boolean updated = updateTicket();
		if (!isUserCanChangeOrigin()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if (getTicketOrigin() != null && !getTicketOrigin().equals(getTicket().getOrigin())) {
			getDomainService().changeTicketOrigin(
					getCurrentUser(), getTicket(), getTicketOrigin(), !originNoAlert);
			resetActionForm();
			refreshTicket();
		}
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doChangeTicketPriority() {
		boolean updated = updateTicket();
		if (!isUserCanChangePriority()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if (getTicket().getPriorityLevel() != getTicketPriority()) {
			getDomainService().changeTicketPriority(
					getCurrentUser(), getTicket(), getTicketPriority(), !priorityNoAlert);
			refreshTicket();
		}
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doChangeTicketComputer() {
		boolean updated = updateTicket();
		if (!isUserCanChangeComputer()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if ((getTicket().getComputer() == null && getTicketComputer() != null)
				|| (getTicket().getComputer() != null
						&& !getTicket().getComputer().equals(getTicketComputer()))) {
			getDomainService().changeTicketComputer(
					getCurrentUser(), getTicket(), getTicketComputer(), !computerNoAlert);
			refreshTicket();
		}
		return null;
	}

	/**
	 * Update the spent time if needed.
	 * @param spentTimeRequired
	 * @param alerts
	 * @return false if the spent time was required and not set.
	 */
	protected boolean doChangeTicketSpentTimeIfNeeded(
			final boolean spentTimeRequired,
			final boolean alerts) {
		long spentTime = getSpentTime();
		if (spentTimeRequired && spentTime == -1) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.SPENT_TIME_REQUIRED");
			return false;
		}
		if (getTicket().getSpentTime() != spentTime) {
			getDomainService().changeTicketSpentTime(getCurrentUser(), getTicket(), spentTime, alerts);
		}
		return true;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doChangeTicketSpentTime() {
		boolean updated = updateTicket();
		if (!isUserCanChangeSpentTime()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		doChangeTicketSpentTimeIfNeeded(false, !spentTimeNoAlert);
		refreshTicket();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String changeOwner() {
		boolean updated = updateTicket();
		if (!isUserCanChangeOwner()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		ldapUid = getTicket().getOwner().getRealId();
		return "changeOwner";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doChangeOwner() {
		boolean updated = updateTicket();
		if (!isUserCanChangeOwner()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if (ldapUid == null) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.ENTER_NEW_OWNER");
			return null;
		}
		User owner;
		try {
			owner = getUserStore().getUserFromRealId(ldapUid);
		} catch (UserNotFoundException e) {
			addErrorMessage(null, "_.MESSAGE.USER_NOT_FOUND", ldapUid);
			return null;
		}
		if (getTicket().getOwner().equals(owner)) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.OWNER_NOT_CHANGED");
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		getDomainService().changeTicketOwner(
				getCurrentUser(), getTicket(), owner, actionMessage, actionScope, true);
		resetActionForm();
		refreshTicket();
		return "ownerChanged";
	}

	/**
	 * Add sub categories to the move tree.
	 * @param categoryNode
	 * @param subCategories
	 */
	@SuppressWarnings("unchecked")
	protected void addMoveTreeSubCategories(
			Department department,
			final CategoryNode categoryNode,
			final List<Category> subCategories,
			final List <Department> departmentsViewable) {
		for (Category subCategory : subCategories) {
    		Category realSubCategory;
    		if (!subCategory.isVirtual()) {
    			realSubCategory = subCategory;
    		} else {
    			realSubCategory = subCategory.getRealCategory();
    		}
    		
    		//on vérifie que la catégorie est visible 
    		//pour cela on récupère le déparement de la liste construite avec les visibilité
    		//et on vérifie que la catégorie n'est pas dans la black list
    		if(departmentsViewable.indexOf(realSubCategory.getDepartment()) >= 0){
	    		Department dept = departmentsViewable.get(departmentsViewable.indexOf(realSubCategory.getDepartment()));
	    		if(department.getCategoriesNotVisibles() != null && department.getCategoriesNotVisibles().contains(subCategory)){
	    			continue;
	    		}

    		}
    		
			List<Category> subSubCategories = getSubCategories(realSubCategory);
	    	if ((!subCategory.equals(getTicket().getCategory()) || !subSubCategories.isEmpty()) 
	    			&& realSubCategory.getDepartment().isEnabled()) {
    			if(getDomainService().getCheckVisiCateVirtual()) {
    				if(!departmentsViewable.contains(realSubCategory.getDepartment())){
    					continue;
    				}
    			}
	        	CategoryNode subCategoryNode = new CategoryNode(realSubCategory, subCategory.getXlabel());
            	categoryNode.getChildren().add(subCategoryNode);
        		categoryNode.setLeaf(false);
        		addMoveTreeSubCategories(department, subCategoryNode, subSubCategories, departmentsViewable);
	    	}
		}
	}

	/**
	 * Add sub categories to the move tree.
	 * @param categoryNode
	 * @param subCategories
	 */
	@SuppressWarnings("unchecked")
	protected void addMoveTreeSubCategories(
			Department department,
			final CategoryNode categoryNode,
			final List<Category> subCategories,
			final List <Department> departmentsViewable,
			final String filter,
			boolean matchFiltre) {
		for (Category subCategory : subCategories) {
    		Category realSubCategory;
    		if (!subCategory.isVirtual()) {
    			realSubCategory = subCategory;
    		} else {
    			realSubCategory = subCategory.getRealCategory();
    		}
    		
    		//on vérifie que la catégorie est visible 
    		//pour cela on récupère le déparement de la liste construite avec les visibilité
    		//et on vérifie que la catégorie n'est pas dans la black list
    		if(departmentsViewable.indexOf(realSubCategory.getDepartment()) >= 0){
	    		Department dept = departmentsViewable.get(departmentsViewable.indexOf(realSubCategory.getDepartment()));
	    		if(department.getCategoriesNotVisibles() != null && department.getCategoriesNotVisibles().contains(subCategory)){
	    			continue;
	    		}

    		}
    		
			List<Category> subSubCategories = getSubCategories(realSubCategory);
	    	if ((!subCategory.equals(getTicket().getCategory()) || !subSubCategories.isEmpty()) 
	    			&& realSubCategory.getDepartment().isEnabled()) {
    			if(getDomainService().getCheckVisiCateVirtual()) {
    				if(!departmentsViewable.contains(realSubCategory.getDepartment())){
    					continue;
    				}
    			}
	        	CategoryNode subCategoryNode = new CategoryNode(realSubCategory, subCategory.getXlabel());
            	categoryNode.getChildren().add(subCategoryNode);
        		categoryNode.setLeaf(false);
	    		if(subCategory.getXlabel().toLowerCase().contains(filter.toLowerCase()) || matchFiltre){
	    			matchFiltre = true;
	    		}
        		addMoveTreeSubCategories(
        				department, 
        				subCategoryNode, 
        				subSubCategories, 
        				departmentsViewable, 
	    				filter,
	    				matchFiltre);
	    		//on supprime la catégorie si son libellé ne correspond pas au filtre
	    		if(!subCategory.getXlabel().toLowerCase().contains(filter.toLowerCase()) && subCategoryNode.getChildCount() < 1 && !matchFiltre){
	    			categoryNode.getChildren().remove(subCategoryNode);
	    		}  
	    	}
		}
	}
	/**
	 * Add sub categories to the move tree.
	 * @param categoryNode
	 * @param subCategories
	 */
	@SuppressWarnings("unchecked")
	protected void addFilteredTreeSubCategories(
			Department department,
			final CategoryNode categoryNode,
			final List<Category> subCategories,
			final List <Department> departmentsViewable) {
		for (Category subCategory : subCategories) {
    		Category realSubCategory;
    		if (!subCategory.isVirtual()) {
    			realSubCategory = subCategory;
    		} else {
    			realSubCategory = subCategory.getRealCategory();
    		}
    		
    		//on vérifie que la catégorie est visible 
    		//pour cela on récupère le déparement de la liste construite avec les visibilité
    		//et on vérifie que la catégorie n'est pas dans la black list
    		if(departmentsViewable.indexOf(realSubCategory.getDepartment()) >= 0){
	    		Department dept = departmentsViewable.get(departmentsViewable.indexOf(realSubCategory.getDepartment()));
	    		if (dept != null){
		    		if(department.getCategoriesNotVisibles() != null && department.getCategoriesNotVisibles().contains(subCategory)){
		    			continue;
		    		}
	    		}
    		}
    		
			List<Category> subSubCategories = getSubCategories(realSubCategory);
	    	if (!subCategory.equals(!subSubCategories.isEmpty()) 
	    			&& realSubCategory.getDepartment().isEnabled()) {
				if(!departmentsViewable.contains(realSubCategory.getDepartment())){
					continue;
				}
	        	CategoryNode subCategoryNode = new CategoryNode(realSubCategory, subCategory.getXlabel());
            	categoryNode.getChildren().add(subCategoryNode);
        		categoryNode.setLeaf(false);
        		addFilteredTreeSubCategories(department, subCategoryNode, subSubCategories, departmentsViewable);
	    	}
		}
	}
	/**
	 * @return the root node of the move tree.
	 */
	@SuppressWarnings("unchecked")
	protected TreeNode buildRootMoveNode() {
		TreeNode rootNode = null;
		
		List <Department> departmentsViewable = getDomainService().getTicketViewDepartments(getCurrentUser(), getClient());
		
		DepartmentManager manager = getDomainService().getDepartmentManager(
				getTicket().getDepartment(), getCurrentUser());
		List<Department> departments;
		if (manager.getModifyTicketDepartment()) {
			departments = getDomainService().getManagedOrTicketViewVisibleDepartments(
					getCurrentUser(), getClient());
		} else {
			departments = getDomainService().getManagedDepartments(getCurrentUser());
		}
		rootNode = new TreeNodeBase("root", "root", true);
		for (Department department : departments) {
			Department realDepartment;
			if (!department.isVirtual()) {
				realDepartment = department;
			} else {
				realDepartment = department.getRealDepartment();
			}
			if(departmentsViewable.contains(realDepartment)){
		    	CategoryNode departmentNode = new CategoryNode(department);
		    	addMoveTreeSubCategories(department, departmentNode, getRootCategories(realDepartment), departmentsViewable);
		    	if (departmentNode.getChildCount() > 0) {
			    	rootNode.getChildren().add(departmentNode);
			    	rootNode.setLeaf(false);
		    	}
			}
		}
		return rootNode;
	}

	/**
	 * @return the root node of the move tree.
	 */
	@SuppressWarnings("unchecked")
	protected TreeNode buildRootMoveNode(final String filter) {
		TreeNode rootNode = null;
		
		List <Department> departmentsViewable = getDomainService().getTicketViewDepartments(getCurrentUser(), getClient());
		
		DepartmentManager manager = getDomainService().getDepartmentManager(
				getTicket().getDepartment(), getCurrentUser());
		List<Department> departments;
		if (manager.getModifyTicketDepartment()) {
			departments = getDomainService().getManagedOrTicketViewVisibleDepartments(
					getCurrentUser(), getClient());
		} else {
			departments = getDomainService().getManagedDepartments(getCurrentUser());
		}
		rootNode = new TreeNodeBase("root", "root", true);
		for (Department department : departments) {
			Department realDepartment;
			if (!department.isVirtual()) {
				realDepartment = department;
			} else {
				realDepartment = department.getRealDepartment();
			}
			if(departmentsViewable.contains(realDepartment)){
		    	CategoryNode departmentNode = new CategoryNode(department);
		    	boolean matchFiltre = false;
		    	addMoveTreeSubCategories(department, departmentNode, 
		    			getRootCategories(realDepartment), departmentsViewable,
		    			filter, matchFiltre);
		    	if (departmentNode.getChildCount() > 0) {
			    	rootNode.getChildren().add(departmentNode);
			    	rootNode.setLeaf(false);
		    	}
			}
		}
		return rootNode;
	}

	/**
	 * Refresh the category tree.
	 */
	public void refreshMoveTree() {
		moveTree = null;
		cateFilter = null;
		TreeNode rootNode = buildRootMoveNode();
		if (rootNode.getChildCount() > 0) {
			moveTree = new TreeModelBase(rootNode);
			TreeState treeState = new TreeStateBase();
			treeState.toggleExpanded("0");
			if (rootNode.getChildCount() == 1) {
				treeState.toggleExpanded("0:0");
			}
			moveTree.setTreeState(treeState);
		}
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String resetMoveTargetCategory() {
		setMoveTargetCategory(null);
		return "continue";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String move() {
		boolean updated = updateTicket();
		if (!isUserCanMove()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		resetMoveTargetCategory();
		refreshMoveTree();
		looseTicketManagementMonitor = false;
		if(getDomainService().getInviteManagerMoveTicket()) {
			looseTicketManagementInvite = true;
		} else {
			looseTicketManagementInvite = false;
		}
		freeTicket = null;
		return "move";
	}
	
	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String giveInformationMoveBack() {
		boolean updated = updateTicket();
		if (!isUserCanGiveInformation()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "giveInformationMoveBack";
	}
	
	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doMoveBack() {
		boolean updated = updateTicket();
		if (!isUserCanMoveBack()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}

		Action lastAction = null;
		Action lastActionCategory = getDomainService().getLastActionByActionType(getTicket(), ActionType.CHANGE_CATEGORY);			
		Action lastActionDepartment = getDomainService().getLastActionByActionType(getTicket(), ActionType.CHANGE_DEPARTMENT);
		//on prend l'action la plus récente s'il y en a
		if(lastActionCategory != null && lastActionDepartment != null){
			if(lastActionCategory.getDate().after(lastActionDepartment.getDate())){
				lastAction = lastActionCategory;
			} else {
				lastAction = lastActionDepartment;
			}
		} else {
			if(lastActionCategory != null){
				lastAction = lastActionCategory;
			} else {
				lastAction = lastActionDepartment;
			}
		}

		if(lastAction != null && lastAction.getCategoryBefore() != null) {
			//ajout de l'action de changement de catégorie
			getDomainService().moveTicket(
				getCurrentUser(), getTicket(), lastAction.getCategoryBefore(),
				actionMessage, actionScope, !noAlert,
				freeTicket, looseTicketManagementMonitor, looseTicketManagementInvite);
		
			//réassignation du ticket
			getDomainService().assignTicket(getCurrentUser(), getTicket(), lastAction.getUser(), null, actionScope);
		} else {
			return "view";
		}
		
		resetActionForm();
		refreshTicket();
		if (!getDomainService().userCanViewTicket(
				getCurrentUser(), getSessionController().getClient(), getTicket())) {
			return back();
		}
		return "moved";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doMove() {
		boolean updated = updateTicket();
		if (!isUserCanMove()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		DepartmentManager manager;
		try {
			manager = getDomainService().getDepartmentManager(
					getTicket().getDepartment(), getCurrentUser());
		} catch (DepartmentManagerNotFoundException e) {
			addUnauthorizedActionMessage();
			return "view";
		}
		if (!manager.getModifyTicketDepartment()) {
			Department moveDepartment = moveTargetCategory.getDepartment();
			if (!moveDepartment.equals(getTicket().getDepartment())
					&& !getDomainService().isDepartmentManager(
							moveDepartment, getCurrentUser())) {
				addUnauthorizedActionMessage();
				return "view";
			}
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		//cas d'un déplacement vers un service confidentiel : on n'invite pas l'ancien gestionnaire au ticket
		if(getMoveTargetCategory().getDepartment().getSrvConfidential() == true) {
			looseTicketManagementInvite = false;
		}
		getDomainService().moveTicket(
				getCurrentUser(), getTicket(), moveTargetCategory,
				actionMessage, actionScope, !noAlert,
				freeTicket, looseTicketManagementMonitor, looseTicketManagementInvite);
		resetActionForm();
		refreshTicket();
		if (!getDomainService().userCanViewTicket(
				getCurrentUser(), getSessionController().getClient(), getTicket())) {
			return back();
		}
		
		if(getTicket().getDepartment().getSrvAnonymous()){
			getTicket().setAnonymous(true);
		}
		return "moved";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String updateInformation(){
		
		boolean updated = updateTicket();
		if (updated) {
			return null;
		}
		setActionScope(actionToUpdate.getScope());
		
		return "updateInformation";
	}
	
	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doUpdateInformation(){
		
		boolean updated = updateTicket();
		logger.debug("doUpdateInformation : scope " + actionToUpdate.getScope());
		if (updated) {
			actionMessage = null;
			return "view";
		}
		limitActionScope();
		
		actionToUpdate.setMessage(actionMessage);
		actionToUpdate.setDate(new Timestamp(System.currentTimeMillis()));
		actionToUpdate.setScope(actionScope);
		getDomainService().updateAction(actionToUpdate);

		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		
		if (!noAlert) {
			getDomainService().ticketMonitoringSendAlerts(getCurrentUser(), getTicket(), null, false);
		}
		
		resetActionForm();
		refreshTicket();
		
		return "updated";
	}
	
	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doUpload() {
		boolean updated = updateTicket();
		if (!isUserCanGiveInformation()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if (uploadedFile == null) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.ENTER_FILE");
			return null;
		}
		limitActionScope();
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		if (!uploadNoAlert) {
			getDomainService().ticketMonitoringSendAlerts(getCurrentUser(), getTicket(), null, false);
		}
		refreshTicket();
		return "fileUploaded";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String deleteFileInfo() {
		boolean updated = updateTicket();
		if (!isUserCanDeleteFileInfo()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "deleteFileInfo";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doDeleteFileInfo() {
		boolean updated = updateTicket();
		if (!isUserCanDeleteFileInfo()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if (actionMessage == null) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.ENTER_MESSAGE_CONTENT");
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
 		getDomainService().deleteFileInfo(
				getCurrentUser(), getTicket(), actionMessage, actionScope, !noAlert, fileInfoToDelete, true);
		resetActionForm();
		refreshTicket();
		return "fileInfoDeleted";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String giveInformation() {
		boolean updated = updateTicket();
		if (!isUserCanGiveInformation()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "giveInformation";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doGiveInformation() {
		boolean updated = updateTicket();
		if (!isUserCanGiveInformation()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if (actionMessage == null) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.ENTER_MESSAGE_CONTENT");
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
 		getDomainService().giveInformation(
				getCurrentUser(), getTicket(), actionMessage, actionScope, !noAlert);
		resetActionForm();
		refreshTicket();
		return "informationGiven";
	}

	/**
	 * JSF callback.
	 * @param takeBefore
	 * @return a String.
	 */
	protected String requestInformationInternal(final boolean takeBefore) {
		boolean updated = updateTicket();
		boolean auth;
		if (takeBefore) {
			auth = isUserCanTakeAndRequestInformation();
		} else {
			auth = isUserCanRequestInformation();
		}
		if (!auth) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if (takeBefore) {
			return "takeAndRequestInformation";
		}
		return "requestInformation";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String requestInformation() {
		return requestInformationInternal(false);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String takeAndRequestInformation() {
		return requestInformationInternal(true);
	}

	/**
	 * JSF callback.
	 * @param takeBefore
	 * @return a String.
	 */
	protected String doRequestInformationInternal(final boolean takeBefore) {
		boolean updated = updateTicket();
		boolean auth;
		if (takeBefore) {
			auth = isUserCanTakeAndRequestInformation();
		} else {
			auth = isUserCanRequestInformation();
		}
		if (!auth) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (actionMessage == null) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.ENTER_MESSAGE_CONTENT");
			return null;
		}
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		if (takeBefore) {
			getDomainService().takeAndRequestTicketInformation(
					getCurrentUser(), getTicket(), actionMessage, actionScope);
		} else {
			getDomainService().requestTicketInformation(
					getCurrentUser(), getTicket(), actionMessage, actionScope);
		}
		resetActionForm();
		refreshTicket();
		if (takeBefore) {
			return "takenAndInformationRequested";
		}
		return "informationRequested";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doRequestInformation() {
		return doRequestInformationInternal(false);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doTakeAndRequestInformation() {
		return doRequestInformationInternal(true);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doUpdateActionScope() {
		boolean updated = updateTicket();
		if (!getDomainService().userCanChangeActionScope(getCurrentUser(), actionToUpdate)) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		actionToUpdate.setScope(actionScopeToSet);
		getDomainService().updateAction(actionToUpdate);
		refreshTicket();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doUpdateFileInfoScope() {
		boolean updated = updateTicket();
		if (!getDomainService().userCanChangeFileInfoScope(getCurrentUser(), fileInfoToUpdate)) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		fileInfoToUpdate.setScope(fileInfoScopeToSet);
		getDomainService().updateFileInfo(fileInfoToUpdate);
		refreshTicket();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String reopen() {
		boolean updated = updateTicket();
		if (!isUserCanReopen()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "reopen";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doReopen() {
		boolean updated = updateTicket();
		if (!isUserCanReopen()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		getDomainService().reopenTicket(getCurrentUser(), getTicket(), actionMessage, actionScope);
		resetActionForm();
		refreshTicket();
		return "reopened";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String cancel() {
		boolean updated = updateTicket();
		if (!isUserCanCancel()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "cancel";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doCancel() {
		boolean updated = updateTicket();
		if (!isUserCanCancel()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		getDomainService().cancelTicket(getCurrentUser(), getTicket(), actionMessage, actionScope);
		resetActionForm();
		refreshTicket();
		if (!getDomainService().userShowsTicketAfterClosure(getCurrentUser())) {
			return back();
		}
		return "cancelled";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String refuse() {
		boolean updated = updateTicket();
		if (!isUserCanRefuse()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "refuse";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doRefuse() {
		boolean updated = updateTicket();
		if (!isUserCanRefuse()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!doChangeTicketSpentTimeIfNeeded(
				getTicket().getDepartment().isSpentTimeNeeded()
				&& getDomainService().isDepartmentManager(
						getTicket().getDepartment(), getCurrentUser()),
				false)) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		getDomainService().refuseTicket(getCurrentUser(), getTicket(), actionMessage, actionScope);
		resetActionForm();
		refreshTicket();
		if (!getDomainService().userShowsTicketAfterClosure(getCurrentUser())) {
			return back();
		}
		return "refused";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String postpone() {
		boolean updated = updateTicket();
		if (!isUserCanPostpone()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		postponeDateSet = false;
		postponeDate = null;
		return "postpone";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doPostpone() {
		boolean updated = updateTicket();
		if (!isUserCanPostpone()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		doChangeTicketSpentTimeIfNeeded(false, false);
		Timestamp recallDate = null;
		if (postponeDate != null) {
			recallDate = new Timestamp(postponeDate.getTime());
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		getDomainService().postponeTicket(
				getCurrentUser(), getTicket(), actionMessage, actionScope, recallDate);
		resetActionForm();
		refreshTicket();
		if (!getDomainService().userShowsTicketAfterClosure(getCurrentUser())) {
			return back();
		}
		return "postponed";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String cancelPostponement() {
		boolean updated = updateTicket();
		if (!isUserCanCancelPostponement()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "cancelPostponement";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doCancelPostponement() {
		boolean updated = updateTicket();
		if (!isUserCanCancelPostponement()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		getDomainService().cancelTicketPostponement(
				getCurrentUser(), getTicket(), actionMessage, actionScope);
		resetActionForm();
		refreshTicket();
		return "postponementCancelled";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String refuseClosure() {
		boolean updated = updateTicket();
		if (!isUserCanRefuseClosure()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "refuseClosure";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doRefuseClosure() {
		boolean updated = updateTicket();
		if (!isUserCanRefuseClosure()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		getDomainService().refuseTicketClosure(
				getCurrentUser(), getTicket(), actionMessage, actionScope);
		resetActionForm();
		refreshTicket();
		return "closureRefused";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String approveClosure() {
		boolean updated = updateTicket();
		if (!isUserCanApproveClosure()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		return "approveClosure";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doApproveClosure() {
		boolean updated = updateTicket();
		if (!isUserCanApproveClosure()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		if (!handleTempUploadedFiles(null)) {
			return null;
		}
		getDomainService().approveTicketClosure(
				getCurrentUser(), getTicket(), actionMessage, actionScope);
		resetActionForm();
		refreshTicket();
		if (!getDomainService().userShowsTicketAfterClosure(getCurrentUser())) {
			return back();
		}
		return "closureApproved";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String download() {
		boolean updated = updateTicket();
		if (!getDomainService().userCanDownload(
				getCurrentUser(), isInvited(), fileInfoToDownload)) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		try {
			byte[] content = getDomainService().getFileInfoContent(fileInfoToDownload);
			downloadId = DownloadUtils.setDownload(
					content, fileInfoToDownload.getFilename(), "application/octet-stream");
		} catch (FileException e) {
			addErrorMessage(
					null, "TICKET_ACTION.MESSAGE.DOWNLOAD_ERROR",
					fileInfoToDownload.getFilename(), e.getMessage());
			ExceptionUtils.catchException(e);
		}
		return null;
	}

	/**
	 * @param category
	 * @return the root categories of the category
	 */
	protected List<Category> getSubCategories(final Category category) {
		Map<Category, List<Category>> subCategoriesMap = getDomainService().getSubCategoriesMap();
		List<Category> subCategories = subCategoriesMap.get(category);
		if (subCategories == null) {
			subCategories = new ArrayList<Category>();
		}
		return subCategories;
	}

	/**
	 * @param department
	 * @return the root categories of the department
	 */
	protected List<Category> getRootCategories(final Department department) {
		Map<Department, List<Category>> rootCategoriesMap = getDomainService().getRootCategoriesMap();
		List<Category> rootCategories = rootCategoriesMap.get(department);
		if (rootCategories == null) {
			rootCategories = new ArrayList<Category>();
		}
		return rootCategories;
	}

	/**
	 * Add sub categories to the add tree.
	 * @param creationDepartment
	 * @param categoryNode
	 * @param subCategories
	 * @param departmentsViewable
	 */
	@SuppressWarnings("unchecked")
	protected void addAddTreeSubCategories(
			final Department creationDepartment,
			final CategoryNode categoryNode,
			final List<Category> subCategories,
			final List <Department> departmentsViewable) {
		for (Category subCategory : subCategories) {
    		Category realSubCategory = subCategory;
    		while (realSubCategory.isVirtual()) {
    			realSubCategory = realSubCategory.getRealCategory();
    		}
    		//on vérifie que la catégorie est visible 
    		//pour cela on récupère le déparement de la liste construite avec les visibilité
    		//et on vérifie que la catégorie n'est pas dans la black list
    		if(departmentsViewable.indexOf(realSubCategory.getDepartment()) >= 0){
	    		Department dept = departmentsViewable.get(departmentsViewable.indexOf(realSubCategory.getDepartment()));
	    		if(creationDepartment.getCategoriesNotVisibles() != null && creationDepartment.getCategoriesNotVisibles().contains(subCategory)){
	    			continue;
	    		}
    		}
    		if ((!realSubCategory.getHideToExternalUsers()
    				|| !getDomainService().getUserStore().isApplicationUser(getCurrentUser()))
    				&& realSubCategory.getDepartment().isEnabled()) {
    			if(getDomainService().getCheckVisiCateVirtual()) {
    				if(!departmentsViewable.contains(realSubCategory.getDepartment())){
    					continue;
    				}
    			}
    			CategoryNode subCategoryNode = new CategoryNode(creationDepartment, realSubCategory, subCategory.getXlabel());
	        	categoryNode.getChildren().add(subCategoryNode);
	    		categoryNode.setLeaf(false);
	    		addAddTreeSubCategories(
	    				creationDepartment, subCategoryNode,
	    				getSubCategories(realSubCategory),
	    				departmentsViewable);
    		}
		}
	}

	/**
	 * Add sub categories to the add tree.
	 * @param creationDepartment
	 * @param categoryNode
	 * @param subCategories
	 * @param departmentsViewable
	 * @param filter
	 */
	@SuppressWarnings("unchecked")
	protected void addAddTreeSubCategories(
			final Department creationDepartment,
			final CategoryNode categoryNode,
			final List<Category> subCategories,
			final List <Department> departmentsViewable,
			final Long cateId,
			boolean matchFiltre) {
		for (Category subCategory : subCategories) {
    		Category realSubCategory = subCategory;
    		while (realSubCategory.isVirtual()) {
    			realSubCategory = realSubCategory.getRealCategory();
    		}

    		//on vérifie que la catégorie est visible 
    		//pour cela on récupère le déparement de la liste construite avec les visibilité
    		//et on vérifie que la catégorie n'est pas dans la black list
    		if(departmentsViewable.indexOf(realSubCategory.getDepartment()) >= 0){
	    		Department dept = departmentsViewable.get(departmentsViewable.indexOf(realSubCategory.getDepartment()));
	    		if(creationDepartment.getCategoriesNotVisibles() != null && creationDepartment.getCategoriesNotVisibles().contains(subCategory)){
	    			continue;
	    		}
    		}
    		
    		if ((!realSubCategory.getHideToExternalUsers()
    				|| !getDomainService().getUserStore().isApplicationUser(getCurrentUser()))
    				&& realSubCategory.getDepartment().isEnabled()) {
    			if(getDomainService().getCheckVisiCateVirtual()) {
    				if(!departmentsViewable.contains(realSubCategory.getDepartment())){
    					continue;
    				}
    			}
	        	CategoryNode subCategoryNode = new CategoryNode(creationDepartment, realSubCategory, subCategory.getXlabel());
	        	categoryNode.getChildren().add(subCategoryNode);
	    		categoryNode.setLeaf(false);
	    		
	    		if(cateId.equals(subCategory.getId()) || matchFiltre){
	    			matchFiltre = true;
	    		}
	    		addAddTreeSubCategories(
	    				creationDepartment, subCategoryNode,
	    				getSubCategories(realSubCategory),
	    				departmentsViewable, 
	    				cateId,
	    				matchFiltre);

		    	//on supprime la catégorie si son id ne correspond pas au filtre
		    	if(!cateId.equals(subCategory.getId()) && subCategoryNode.getChildCount() < 1 && !matchFiltre){
		    		categoryNode.getChildren().remove(subCategoryNode);
		    	}
		    	//on repasse l'indicateur de match à flase si on est remonté au niveau de la catégorie qui a matché 
		    	
	    		if(cateId.equals(subCategory.getId())){
	    			matchFiltre = false;
	    		}		    	
	    	}

		}
	}
	
	/**
	 * Add sub categories to the add tree.
	 * @param creationDepartment
	 * @param categoryNode
	 * @param subCategories
	 * @param departmentsViewable
	 * @param filter
	 */
	@SuppressWarnings("unchecked")
	protected void addAddTreeSubCategories(
			final Department creationDepartment,
			final CategoryNode categoryNode,
			final List<Category> subCategories,
			final List <Department> departmentsViewable,
			final String filter,
			boolean matchFiltre) {
		for (Category subCategory : subCategories) {
    		Category realSubCategory = subCategory;
    		while (realSubCategory.isVirtual()) {
    			realSubCategory = realSubCategory.getRealCategory();
    		}

    		//on vérifie que la catégorie est visible 
    		//pour cela on récupère le déparement de la liste construite avec les visibilité
    		//et on vérifie que la catégorie n'est pas dans la black list
    		if(departmentsViewable.indexOf(realSubCategory.getDepartment()) >= 0){
	    		Department dept = departmentsViewable.get(departmentsViewable.indexOf(realSubCategory.getDepartment()));
	    		if(creationDepartment.getCategoriesNotVisibles() != null && creationDepartment.getCategoriesNotVisibles().contains(subCategory)){
	    			continue;
	    		}
    		}
    		
    		if ((!realSubCategory.getHideToExternalUsers()
    				|| !getDomainService().getUserStore().isApplicationUser(getCurrentUser()))
    				&& realSubCategory.getDepartment().isEnabled()) {
    			if(getDomainService().getCheckVisiCateVirtual()) {
    				if(!departmentsViewable.contains(realSubCategory.getDepartment())){
    					continue;
    				}
    			}
	        	CategoryNode subCategoryNode = new CategoryNode(creationDepartment, realSubCategory, subCategory.getXlabel());
	        	categoryNode.getChildren().add(subCategoryNode);
	    		categoryNode.setLeaf(false);
	    		
	    		if(subCategory.getXlabel().toLowerCase().contains(filter.toLowerCase()) || matchFiltre){
	    			matchFiltre = true;
	    		}
	    		addAddTreeSubCategories(
	    				creationDepartment, subCategoryNode,
	    				getSubCategories(realSubCategory),
	    				departmentsViewable, 
	    				filter,
	    				matchFiltre);
		    		//on supprime la catégorie si son libellé ne correspond pas au filtre
		    		if(!subCategory.getXlabel().toLowerCase().contains(filter.toLowerCase()) && subCategoryNode.getChildCount() < 1 && !matchFiltre){
		    			categoryNode.getChildren().remove(subCategoryNode);
		    		}    		
	    		}

		}
	}
	/**
	 * @return the root node of the add tree.
	 */
	@SuppressWarnings("unchecked")
	protected TreeNode buildRootAddNode() {
		TreeNode rootNode = new TreeNodeBase("root", "root", true);
		
		List <Department> departmentsViewable = getDomainService().getTicketViewDepartments(getCurrentUser(), getClient());
		
		for (Department department : getDomainService().getTicketCreationDepartments(
				getCurrentUser(), getClient())) {
			Department realDepartment;
			if (department.isVirtual()) {
				realDepartment = department.getRealDepartment();
			} else {
				realDepartment = department;
			}
			if ((!realDepartment.getHideToExternalUsers()
					|| !getDomainService().getUserStore().isApplicationUser(getCurrentUser()))
					&& departmentsViewable.contains(realDepartment)) {
		    	CategoryNode departmentNode = new CategoryNode(department);
		    	addAddTreeSubCategories(
		    			department, departmentNode,
		    			getRootCategories(realDepartment),
		    			departmentsViewable);
		    	if (departmentNode.getChildCount() > 0) {
			    	rootNode.getChildren().add(departmentNode);
			    	rootNode.setLeaf(false);
		    	}
			}
		}
		return rootNode;
	}

	/**
	 * @return the root node of the add tree.
	 */
	@SuppressWarnings("unchecked")
	public TreeNode buildRootAddNodeForDepartment(Long departmentId) {
		TreeNode rootNode = new TreeNodeBase("root", "root", true);
		
		List <Department> departmentsViewable = getDomainService().getTicketViewDepartments(getCurrentUser(), getClient());
		List<Department> dpts = getDomainService().getTicketCreationDepartments(getCurrentUser(), getClient());
		for (Department department : getDomainService().getTicketCreationDepartments(
				getCurrentUser(), getClient())) {
			if(departmentId.equals(department.getId())) {
				Department realDepartment;
				if (department.isVirtual()) {
					realDepartment = department.getRealDepartment();
				} else {
					realDepartment = department;
				}
				if ((!realDepartment.getHideToExternalUsers()
						|| !getDomainService().getUserStore().isApplicationUser(getCurrentUser()))
						&& departmentsViewable.contains(realDepartment)) {
			    	CategoryNode departmentNode = new CategoryNode(department);
			    	addAddTreeSubCategories(
			    			department, departmentNode,
			    			getRootCategories(realDepartment),
			    			departmentsViewable);
			    	if (departmentNode.getChildCount() > 0) {
				    	rootNode.getChildren().add(departmentNode);
				    	rootNode.setLeaf(false);
			    	}
				}
			}
		}
		return rootNode;
	}

	/**
	 * @return the root node of the add tree.
	 */
	@SuppressWarnings("unchecked")
	public TreeNode buildRootAddNodeForCategory(Long categoryId) {
		TreeNode rootNode = new TreeNodeBase("root", "root", true);
		
		
		Category category = getDomainService().getCategory(categoryId);
		if(category != null) {
			Long departmentId = category.getDepartment().getId();
			
			List <Department> departmentsViewable = getDomainService().getTicketViewDepartments(getCurrentUser(), getClient());
			List<Department> dpts = getDomainService().getTicketCreationDepartments(getCurrentUser(), getClient());
			for (Department department : getDomainService().getTicketCreationDepartments(
					getCurrentUser(), getClient())) {
				if(departmentId.equals(department.getId())) {
					Department realDepartment;
					if (department.isVirtual()) {
						realDepartment = department.getRealDepartment();
					} else {
						realDepartment = department;
					}
					if ((!realDepartment.getHideToExternalUsers()
							|| !getDomainService().getUserStore().isApplicationUser(getCurrentUser()))
							&& departmentsViewable.contains(realDepartment)) {
				    	CategoryNode departmentNode = new CategoryNode(department);
				    	boolean matchFiltre = false;
				    	addAddTreeSubCategories(
				    			department, departmentNode,
				    			getRootCategories(realDepartment),
				    			departmentsViewable, categoryId, matchFiltre);
				    	if (departmentNode.getChildCount() > 0) {
					    	rootNode.getChildren().add(departmentNode);
					    	rootNode.setLeaf(false);
				    	}
					}
				}
			}
		}
		return rootNode;
	}

	/**
	 * @param filter
	 * @return the root node of the add tree.
	 */
	@SuppressWarnings("unchecked")
	protected TreeNode buildRootAddNode(final String filter) {
		TreeNode rootNode = new TreeNodeBase("root", "root", true);
		
		List <Department> departmentsViewable = getDomainService().getTicketViewDepartments(getCurrentUser(), getClient());
		
		for (Department department : getDomainService().getTicketCreationDepartments(
				getCurrentUser(), getClient())) {
			Department realDepartment;
			if (department.isVirtual()) {
				realDepartment = department.getRealDepartment();
			} else {
				realDepartment = department;
			}
			if ((!realDepartment.getHideToExternalUsers()
					|| !getDomainService().getUserStore().isApplicationUser(getCurrentUser()))
					&& departmentsViewable.contains(realDepartment)) {
		    	CategoryNode departmentNode = new CategoryNode(department);
		    	boolean matchFiltre = false;
		    	addAddTreeSubCategories(
		    			department, departmentNode,
		    			getRootCategories(realDepartment),
		    			departmentsViewable, filter, matchFiltre);
		    	if (departmentNode.getChildCount() > 0) {
			    	rootNode.getChildren().add(departmentNode);
			    	rootNode.setLeaf(false);
		    	}
			}
		}
		return rootNode;
	}
	/**
	 * Refresh the category tree.
	 */
	public void refreshAddTree() {
		addTree = null;
		cateFilter = null;
		TreeNode rootNode = buildRootAddNode();
		if (rootNode.getChildCount() > 0) {
			addTree = new TreeModelBase(rootNode);
			TreeState treeState = new TreeStateBase();
			treeState.toggleExpanded("0");
			if (rootNode.getChildCount() == 1) {
				treeState.toggleExpanded("0:0");
			}
			addTree.setTreeState(treeState);
		}
	}

	/**
	 * Refresh the category tree.
	 */
	public void filterAddTree() {
		addTree = null;
		TreeNode rootNode = buildRootAddNode(cateFilter);

		//ouverture complète de l'arborescence
		if (rootNode.getChildCount() > 0) {
			addTree = new TreeModelBase(rootNode);
			TreeState treeState = new TreeStateBase();
			treeState.toggleExpanded("0");
			
			List<CategoryNode> nodesToCollapse = rootNode.getChildren();
			expandAllTree(treeState, nodesToCollapse);
			
			addTree.setTreeState(treeState);
		}
	}

	/**
	 * Refresh the category tree.
	 */
	public void filterMoveTree() {
		moveTree = null;
		TreeNode rootNode = buildRootMoveNode(cateFilter);

		//ouverture complète de l'arborescence
		if (rootNode.getChildCount() > 0) {
			moveTree = new TreeModelBase(rootNode);
			TreeState treeState = new TreeStateBase();
			treeState.toggleExpanded("0");
			
			List<CategoryNode> nodesToCollapse = rootNode.getChildren();
			expandAllTree(treeState, nodesToCollapse);
			
			moveTree.setTreeState(treeState);
		}
	}
	public void expandAllTree(TreeState treeState, List<CategoryNode> nodesToCollapse){
		for (Object child : nodesToCollapse) {
			CategoryNode node = (CategoryNode) child;
			treeState.toggleExpanded(node.getIdentifier());
			if(node.getChildCount() > 0){
				expandAllTree(treeState, node.getChildren());
			}
		}
		
	}
	/**
	 * JSF callback.
	 */
	protected void resetAddTargetCategory() {
		setAddTargetCategory(null);
		setAddTargetDepartment(null);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String add() {
		setTicket(null);
		resetAddTargetCategory();
		refreshAddTree();
		ldapUid = null;
		showAddHelp = getCurrentUser().getShowAddTicketHelp();
		return "add";
	}

	/**
	 * Add a FAQ to the add FAQ tree.
	 * @param parentNode
	 * @param faq
	 * @param visibleDepartments
	 */
	@SuppressWarnings("unchecked")
	protected void addAddFaqTree(
			final FaqNode parentNode,
			final Faq faq,
			final List<Department> visibleDepartments) {
		if (!getDomainService().userCanViewFaq(getCurrentUser(), faq, visibleDepartments)) {
			return;
		}
    	FaqNode faqNode = new FaqNode(faq);
    	for (Faq subFaq : getDomainService().getSubFaqs(faq)) {
			addAddFaqTree(faqNode, subFaq, visibleDepartments);
		}
		AbstractFirstLastNode.markFirstAndLastChildNodes(faqNode);
		parentNode.getChildren().add(faqNode);
		parentNode.setLeaf(false);
    }

	/**
	 * Refresh the tree of the FAQ links.
	 */
	protected void refreshAddFaqTree() {
		addFaqTree = null;
		List<FaqLink> faqLinks = getDomainService().getEffectiveFaqLinks(addTargetCategory);
		if (faqLinks.isEmpty()) {
			return;
		}
    	FaqNode rootNode = new FaqNode();
		List<Department> visibleDepartments = getDomainService().getFaqViewDepartments(
				getCurrentUser(), getClient());
		for (FaqLink faqLink : faqLinks) {
			addAddFaqTree(rootNode, faqLink.getFaq(), visibleDepartments);
		}
    	if (rootNode.getChildCount() == 0) {
    		return;
    	}
    	AbstractFirstLastNode.markFirstAndLastChildNodes(rootNode);
		addFaqTree = new FaqTreeModel(rootNode);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String addChooseCategory() {
		if (!isUserCanAdd()) {
			addUnauthorizedActionMessage();
			return "navigationControlPanel";
		}
		setTicketLabel(addTargetCategory.getEffectiveDefaultTicketLabel());
		InetAddress client = getSessionController().getClient();
		if (client != null) {
			setTicketComputer(client.getCanonicalHostName());
		}
		setTicketOrigin(getDomainService().getWebOrigin());
		setTicketPriority(DomainService.DEFAULT_PRIORITY_VALUE);
		setTicketScope(TicketScope.DEFAULT);
		actionMessage = addTargetCategory.getEffectiveDefaultTicketMessage();
		noAlert = false;
		refreshAddFaqTree();
		return "continue";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doAdd() {
		if (!isUserCanAdd()) {
			return "navigationControlPanel";
		}
		if (getTicketLabel() == null) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.LABEL_REQUIRED");
			return null;
		}
		if (actionMessage == null) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.MESSAGE_REQUIRED");
			return null;
		}
		User owner = null;
		if (isUserCanSetOwner() && org.springframework.util.StringUtils.hasText(ldapUid)) {
			try {
				owner = getUserStore().getUserFromRealId(ldapUid);
			} catch (UserNotFoundException e) {
				addErrorMessage(null, "_.MESSAGE.USER_NOT_FOUND", ldapUid);
				return null;
			}
		}
		if (!checkActionMessageLength()) {
			return null;
		}
		String origin = null;
		if (isUserCanSetOrigin()) {
			origin = getTicketOrigin();
		}
		Ticket newTicket = getDomainService().addWebTicket(
				getCurrentUser(), owner, addTargetDepartment,
				addTargetCategory, getTicketLabel(), getTicketComputer(),
				getTicketPriority(), actionMessage,
				getTicketScope(), origin);
		if (!handleTempUploadedFiles(newTicket)) {
			return null;
		}
		getDomainService().ticketMonitoringSendAlerts(getCurrentUser(), newTicket, null, false);
		resetActionForm();
		if(newTicket.getDepartment().getSrvAnonymous()){
			newTicket.setAnonymous(true);
		} 
		else {
			newTicket.setAnonymous(false);
		}
		setTicket(newTicket);
		addInfoMessage(null, "TICKET_ACTION.MESSAGE.TICKET_ADDED");
		if(getTicketScope().equals(TicketScope.PUBLIC)) {
			addWarnMessage(null, "TICKET_ACTION.MESSAGE.VISBILITE.TICKET.PUBLIC");
		}
		return "created";
	}

	/**
	 * @param category
	 * @return the members of the category
	 */
	protected List<CategoryMember> getCategoryMembers(final Category category) {
		Map<Category, List<CategoryMember>> categoryMembersMap = getDomainService().getCategoryMembersMap();
		List<CategoryMember> categoryMembers = categoryMembersMap.get(category);
		if (categoryMembers == null) {
			categoryMembers = new ArrayList<CategoryMember>();
		}
		return categoryMembers;
	}

	/**
	 * @param category
	 * @return the node for a category in the invite tree.
	 */
	@SuppressWarnings("unchecked")
	protected CategoryNode buildCategoryInviteNode(
			final Category category) {
		if (category.isVirtual()) {
			return null;
		}
		List<CategoryNode> subCategoryNodes = new ArrayList<CategoryNode>();
		for (Category subCategory : getSubCategories(category)) {
			CategoryNode subCategoryNode = buildCategoryInviteNode(subCategory);
			if (subCategoryNode != null) {
				subCategoryNodes.add(subCategoryNode);
			}
		}
		List<CategoryMember> categoryMembers = getCategoryMembers(category);
		if (subCategoryNodes.isEmpty() && categoryMembers.isEmpty()) {
			return null;
		}
		CategoryNode categoryNode = new CategoryNode(category, category.getXlabel());
		for (CategoryNode subCategoryNode : subCategoryNodes) {
			categoryNode.getChildren().add(subCategoryNode);
    		categoryNode.setLeaf(false);
		}
		categoryNode.setManagersAsCategoryMembers(categoryMembers);
		return categoryNode;
	}

	/**
	 * @return the root node of the invite tree.
	 */
	@SuppressWarnings("unchecked")
	protected TreeNode buildRootInviteNode() {
		TreeNode rootNode = null;
		List<Department> departments = getDomainService().getManagedOrTicketViewVisibleDepartments(
					getCurrentUser(), getClient());
		rootNode = new TreeNodeBase("root", "root", true);
		for (Department department : departments) {
			if (!department.isVirtual()) {
		    	CategoryNode departmentNode = new CategoryNode(department);
		    	List<DepartmentManager> departmentManagers =
		    		getDomainService().getDepartmentManagers(department);
		    	if (!departmentManagers.isEmpty()) {
			    	departmentNode.setManagersAsDepartmentManagers(departmentManagers);
			    	for (Category category : getRootCategories(department)) {
			    		CategoryNode categoryNode = buildCategoryInviteNode(category);
			    		if (categoryNode != null) {
					    	departmentNode.getChildren().add(categoryNode);
					    	departmentNode.setLeaf(false);
			    		}
			    	}
			    	rootNode.getChildren().add(departmentNode);
			    	rootNode.setLeaf(false);
		    	}
			}
		}
		return rootNode;
	}

	/**
	 * Refresh the invite tree.
	 */
	protected void refreshInviteTree() {
		inviteTree = null;
		TreeNode rootNode = buildRootInviteNode();
		if (rootNode.getChildCount() > 0) {
			inviteTree = new TreeModelBase(rootNode);
			TreeState treeState = new TreeStateBase();
			treeState.toggleExpanded("0");
			if (rootNode.getChildCount() == 1) {
				treeState.toggleExpanded("0:0");
			}
			inviteTree.setTreeState(treeState);
		}
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String invite() {
		boolean updated = updateTicket();
		if (!isUserCanInvite()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		ldapUid = null;
		refreshInviteTree();
		return "invite";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doInvite() {
		boolean updated = updateTicket();
		if (!isUserCanInvite()) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		if (!org.springframework.util.StringUtils.hasText(ldapUid)) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.ENTER_INVITED_USER", ldapUid);
			ldapUid = null;
			return null;
		}
		boolean error = false;
		String [] invitedUserIds = ldapUid.split(",");
		ldapUid = "";
		String separator = "";
		List<User> invitedUsers = new ArrayList<User>();
		for (String id : invitedUserIds) {
			String invitedUserId = id.trim();
			if (org.springframework.util.StringUtils.hasText(invitedUserId)) {
				boolean keepUser = true;
				try {
					User invitedUser = getUserStore().getUserFromRealId(invitedUserId);
					if (getDomainService().isInvited(invitedUser, getTicket())) {
						addWarnMessage(null,
								"TICKET_ACTION.MESSAGE.ALREADY_INVITED",
								formatUser(invitedUser));
						keepUser = false;
						error = true;
					} else {
						invitedUsers.add(invitedUser);
					}
				} catch (UserNotFoundException e) {
					addErrorMessage(null, "_.MESSAGE.USER_NOT_FOUND", invitedUserId);
					error = true;
				}
				if (keepUser) {
					ldapUid += separator + invitedUserId;
					separator = ",";
				}
			}
			if (error) {
				return null;
			}
		}

		if (invitedUsers.isEmpty()) {
			addErrorMessage(null, "TICKET_ACTION.MESSAGE.ENTER_INVITED_USER");
			ldapUid = null;
			return null;
		}
		limitActionScope();
		if (!checkActionMessageLength()) {
			return null;
		}
		List<User> warnedUsers = new ArrayList<User>();
		for (User invitedUser : invitedUsers) {
			if (getDomainService().invite(
					getCurrentUser(), getTicket(), invitedUser, actionMessage, actionScope, true)) {
				warnedUsers.add(invitedUser);
			}
			actionMessage = null;
		}
		getDomainService().ticketMonitoringSendAlerts(getCurrentUser(), getTicket(), warnedUsers, false);
		resetActionForm();
		refreshTicket();
		return "invitationAdded";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doRemoveInvitation() {
		boolean updated = updateTicket();
		if (!isUserCanRemoveInvitations() && !getCurrentUser().equals(invitationToDelete.getUser())) {
			addUnauthorizedActionMessage();
			if (isPageAuthorized()) {
				return "view";
			}
			return back();
		}
		if (updated) {
			return null;
		}
		limitActionScope();
		getDomainService().removeInvitation(getCurrentUser(), invitationToDelete, true);
		refreshTicket();
		return "invitationRemoved";
	}

	/**
	 * JSF callback.
	 */
	public void addBookmark() {
		getDomainService().addBookmark(getCurrentUser(), getTicket());
		refreshTicket();
	}

	/**
	 * JSF callback.
	 */
	public void deleteBookmark() {
		getDomainService().deleteBookmark(getBookmark());
		refreshTicket();
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String print() {
		boolean updated = updateTicket();
		if (updated) {
			return null;
		}
		downloadId = DownloadUtils.setDownload(
				getDomainService().getTicketPrintContent(getCurrentUser(), getTicket()).getBytes(),
				getTicket().getId() + "-" + System.currentTimeMillis() + ".html",
				"text/html");
		return null;
	}

	/**
	 * Eclipse outline delimiter.
	 */
	public void ______________LINKS() {
		//
	}


	/**
	 * @return a permanent link to the page for any users.
	 */
	public String getPermLink() {
		if(getUserStore().isCasUser(getCurrentUser())) {
			return getPermLink(AuthUtils.CAS);
		}
		if(getUserStore().isApplicationUser(getCurrentUser())) {
			return getPermLink(AuthUtils.APPLICATION);
		}
		if(getUserStore().isShibbolethUser(getCurrentUser())) {
			return getPermLink(AuthUtils.SHIBBOLETH);
		}
		return getPermLink(AuthUtils.SPECIFIC);
	}
	
	/**
	 * @param authType
	 * @return a permanent link to the page.
	 */
	protected String getPermLink(final String authType) {
		if (getTicket() != null) {
			return getUrlBuilder().getTicketViewUrl(authType, getTicket().getId());
		}
		return getUrlBuilder().getTicketAddUrl(authType, addTargetCategory);
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
	 * Eclipse outline delimiter.
	 */
	public void ______________GETTERS_SETTERS() {
		//
	}

	/**
	 * @return the showAlerts
	 */
	public boolean isShowAlerts() {
		return showAlerts;
	}

	/**
	 * Toggle the showAlerts flag.
	 */
	public void toggleShowAlerts() {
		showAlerts = !showAlerts;
	}

	/**
	 * @return the actionMessage
	 */
	public String getActionMessage() {
		return actionMessage;
	}

	/**
	 * @param actionMessage the actionMessage to set
	 */
	public void setActionMessage(final String actionMessage) {
		this.actionMessage = StringUtils.nullIfEmpty(actionMessage);
	}

	/**
	 * @return the actionScope
	 */
	public String getActionScope() {
		if (actionScope == null) {
			return ActionScope.DEFAULT;
		}
		return actionScope;
	}

	/**
	 * @param actionScope the actionScope to set
	 */
	public void setActionScope(final String actionScope) {
		this.actionScope = actionScope;
	}

	/**
	 * @return the noAlert
	 */
	public boolean isNoAlert() {
		return noAlert;
	}

	/**
	 * @param noAlert the noAlert to set
	 */
	public void setNoAlert(final boolean noAlert) {
		this.noAlert = noAlert;
	}

	/**
	 * @return true if the user can add a ticket
	 */
	public boolean isUserCanAdd() {
		return getCurrentUser() != null;
	}

	/**
	 * @see org.esupportail.commons.web.controllers.LdapSearchCaller#getLdapUid()
	 */
	@Override
	public String getLdapUid() {
		return ldapUid;
	}

	/**
	 * @see org.esupportail.commons.web.controllers.LdapSearchCaller#setLdapUid(java.lang.String)
	 */
	@Override
	public void setLdapUid(final String ldapUid) {
		this.ldapUid = StringUtils.nullIfEmpty(ldapUid);
	}

	/**
	 * @return the moveTree
	 */
	public TreeModelBase getMoveTree() {
		return moveTree;
	}

	/**
	 * @param uploadedFile the uploadedFile to set
	 */
	public void setUploadedFile(final UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	/**
	 * @return the uploadedFile
	 */
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	/**
	 * @param fileInfoToDownload the fileInfoToDownload to set
	 */
	public void setFileInfoToDownload(final FileInfo fileInfoToDownload) {
		this.fileInfoToDownload = fileInfoToDownload;
	}

	/**
	 * @param actionToUpdate the actionToUpdate to set
	 */
	public void setActionToUpdate(final Action actionToUpdate) {
		this.actionToUpdate = actionToUpdate;
	}

	/**
	 * @return the backPage
	 */
	public String getBackPage() {
		return backPage;
	}

	/**
	 * @param backPage the backPage to set
	 */
	public void setBackPage(final String backPage) {
		this.backPage = backPage;
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.TicketControllerStateHolder#isUserCanSetNoAlert()
	 */
	@Override
	@RequestCache
	public boolean isUserCanSetNoAlert() {
		if (getTicket() != null) {
			return super.isUserCanSetNoAlert();
		}
		return getDomainService().isDepartmentManager(addTargetCategory.getDepartment(), getCurrentUser());
	}

	/**
	 * @return the userCanSetOrigin
	 */
	@RequestCache
	public boolean isUserCanSetOrigin() {
		return getDomainService().userCanSetOrigin(getCurrentUser(), addTargetCategory.getDepartment());
	}

	/**
	 * @return the userCanSetOwner
	 */
	@RequestCache
	public boolean isUserCanSetOwner() {
		return getDomainService().userCanSetOwner(getCurrentUser(), addTargetCategory.getDepartment())
		 || getDomainService().userCanSetOwner(getCurrentUser(), addTargetDepartment);
	}

	/**
	 * @return the recent invited users.
	 */
	public List<SelectItem> getRecentInvitationItems() {
		List<SelectItem> result = new ArrayList<SelectItem>();
		List<User> users = new ArrayList<User>();
		for (User invitedUser : getDomainService().getInvitedUsers(getCurrentUser())) {
			if (!users.contains(invitedUser) && !getDomainService().isInvited(invitedUser, getTicket())) {
				users.add(invitedUser);
				if (users.size() == RECENT_INVITATIONS_MAX_NUMBER) {
					break;
				}
			}
		}
		if (!users.isEmpty()) {
			result.add(new SelectItem("", getString("TICKET_ACTION.TEXT.INVITE.CHOOSE_RECENT")));
			for (User user : users) {
				result.add(new SelectItem(user.getRealId(), getUserFormattingService().format(user, getTicket().getAnonymous(), getLocale(), getCurrentUser())));
			}
		}
		return result;
	}

	/**
	 * @return the recent target categories items.
	 */
	public List<SelectItem> getRecentMoveItems() {
		DepartmentManager manager = getDomainService().getDepartmentManager(
				getTicket().getDepartment(), getCurrentUser());
		List<Department> departments;
		if (manager.getModifyTicketDepartment()) {
			departments = getDomainService().getManagedOrTicketViewVisibleDepartments(
					getCurrentUser(), getClient());
		} else {
			departments = getDomainService().getManagedDepartments(getCurrentUser());
		}
		List<SelectItem> result = new ArrayList<SelectItem>();
		List<Category> categories = new ArrayList<Category>();
		for (Category targetCategory : getDomainService().getTargetCategories(getCurrentUser())) {
			if (!categories.contains(targetCategory) && departments.contains(targetCategory.getDepartment())) {
				categories.add(targetCategory);
			}
		}
		if (!categories.isEmpty()) {
			result.add(new SelectItem("", getString("TICKET_ACTION.TEXT.MOVE.CHOOSE_RECENT")));
			for (Category category : categories) {
				result.add(new SelectItem(category, category.getDepartment().getLabel() + " - " + category.getLabel()));
			}
		}
		return result;
	}

	/**
	 * @param targetFaq the targetFaq to set
	 */
	public void setTargetFaq(final Faq targetFaq) {
		this.targetFaq = targetFaq;
	}

	/**
	 * @param fileInfoToUpdate the fileInfoToUpdate to set
	 */
	public void setFileInfoToUpdate(final FileInfo fileInfoToUpdate) {
		this.fileInfoToUpdate = fileInfoToUpdate;
	}

	/**
	 * @return the targetTicket
	 */
	public Ticket getTargetTicket() {
		return targetTicket;
	}

	/**
	 * @return the targetArchivedTicket
	 */
	public ArchivedTicket getTargetArchivedTicket() {
		return targetArchivedTicket;
	}

	/**
	 * @return the targetTicketId
	 */
	public Long getTargetTicketId() {
		return targetTicketId;
	}

	/**
	 * @param targetTicketId the targetTicketId to set
	 */
	public void setTargetTicketId(final Long targetTicketId) {
		this.targetTicketId = targetTicketId;
	}

	/**
	 * @return the addTree
	 */
	public TreeModelBase getAddTree() {
		return addTree;
	}
	
	public void setAddTree(TreeModelBase addTree) {
		this.addTree = addTree;
	}

	/**
	 * @return the moveTargetCategory
	 */
	public Category getMoveTargetCategory() {
		return moveTargetCategory;
	}

	/**
	 * Set the moveTargetCategory.
	 * @param moveTargetCategory
	 */
	public void setMoveTargetCategory(final Category moveTargetCategory) {
		this.moveTargetCategory = moveTargetCategory;
		freeTicket = false;
		if (moveTargetCategory == null) {
			return;
		}
		 for(CategoryMember categoryMember : getCategoryMembers(moveTargetCategory)){
			 categoryMoveMembers += categoryMember.getUser().getDisplayName() + "\n";
		 }		
		if (getTicket().getManager() == null) {
			return;
		}
		freeTicket = true;
		List<DepartmentManager> managers = getDomainService().getEffectiveDepartmentManagers(moveTargetCategory);
		for (DepartmentManager manager : managers) {
			if (manager.getUser().equals(getCurrentUser())) {
				freeTicket = false;
				break;
			}
		}
	}

	/**
	 * @return the addTargetCategory
	 */
	public Category getAddTargetCategory() {
		return addTargetCategory;
	}

	/**
	 * Set the addTargetCategory.
	 * @param addTargetCategory
	 */
	public void setAddTargetCategory(final Category addTargetCategory) {
		this.addTargetCategory = addTargetCategory;
	}

	/**
	 * @return the addTargetDepartment
	 */
	public Department getAddTargetDepartment() {
		return addTargetDepartment;
	}

	/**
	 * Set the addTargetDepartment.
	 * @param addTargetDepartment
	 */
	public void setAddTargetDepartment(final Department addTargetDepartment) {
		this.addTargetDepartment = addTargetDepartment;
	}

	/**
	 * @param invitationToDelete the invitationToDelete to set
	 */
	public void setInvitationToDelete(final Invitation invitationToDelete) {
		this.invitationToDelete = invitationToDelete;
	}

	/**
	 * @return the showAddHelp
	 */
	public boolean isShowAddHelp() {
		return showAddHelp;
	}

	/**
	 * @param showAddHelp the showAddHelp to set
	 */
	public void setShowAddHelp(final boolean showAddHelp) {
		this.showAddHelp = showAddHelp;
	}

	/**
	 * @return the skipAddHelp
	 */
	public boolean isSkipAddHelp() {
		return false;
	}

	/**
	 * Set the skipAddHelp.
	 * @param skipAddHelp
	 */
	public void setSkipAddHelp(final boolean skipAddHelp) {
		if (skipAddHelp) {
			getCurrentUser().setShowAddTicketHelp(false);
			getDomainService().updateUser(getCurrentUser());
		}
	}

	/**
	 * @return the scopeNoAlert
	 */
	public boolean isScopeNoAlert() {
		return scopeNoAlert;
	}

	/**
	 * @param scopeNoAlert the scopeNoAlert to set
	 */
	public void setScopeNoAlert(final boolean scopeNoAlert) {
		this.scopeNoAlert = scopeNoAlert;
	}

	/**
	 * @return the originNoAlert
	 */
	public boolean isOriginNoAlert() {
		return originNoAlert;
	}

	/**
	 * @param originNoAlert the originNoAlert to set
	 */
	public void setOriginNoAlert(final boolean originNoAlert) {
		this.originNoAlert = originNoAlert;
	}

	/**
	 * @return the priorityNoAlert
	 */
	public boolean isPriorityNoAlert() {
		return priorityNoAlert;
	}

	/**
	 * @param priorityNoAlert the priorityNoAlert to set
	 */
	public void setPriorityNoAlert(final boolean priorityNoAlert) {
		this.priorityNoAlert = priorityNoAlert;
	}

	/**
	 * @return the labelNoAlert
	 */
	public boolean isLabelNoAlert() {
		return labelNoAlert;
	}

	/**
	 * @param labelNoAlert the labelNoAlert to set
	 */
	public void setLabelNoAlert(final boolean labelNoAlert) {
		this.labelNoAlert = labelNoAlert;
	}

	/**
	 * @return the computerNoAlert
	 */
	public boolean isComputerNoAlert() {
		return computerNoAlert;
	}

	/**
	 * @param computerNoAlert the computerNoAlert to set
	 */
	public void setComputerNoAlert(final boolean computerNoAlert) {
		this.computerNoAlert = computerNoAlert;
	}

	/**
	 * @return the uploadNoAlert
	 */
	public boolean isUploadNoAlert() {
		return uploadNoAlert;
	}

	/**
	 * @param uploadNoAlert the uploadNoAlert to set
	 */
	public void setUploadNoAlert(final boolean uploadNoAlert) {
		this.uploadNoAlert = uploadNoAlert;
	}

	/**
	 * @return the spentTimeNoAlert
	 */
	public boolean isSpentTimeNoAlert() {
		return spentTimeNoAlert;
	}

	/**
	 * @param spentTimeNoAlert the spentTimeNoAlert to set
	 */
	public void setSpentTimeNoAlert(final boolean spentTimeNoAlert) {
		this.spentTimeNoAlert = spentTimeNoAlert;
	}

	/**
	 * @return the targetManager
	 */
	public User getTargetManager() {
		return targetManager;
	}

	/**
	 * @param targetManager the targetManager to set
	 */
	public void setTargetManager(final User targetManager) {
		this.targetManager = targetManager;
	}

	/**
	 * @return the freeTicketAfterClosure
	 */
	public boolean isFreeTicketAfterClosure() {
		return freeTicketAfterClosure;
	}

	/**
	 * @param freeTicketAfterClosure the freeTicketAfterClosure to set
	 */
	public void setFreeTicketAfterClosure(final boolean freeTicketAfterClosure) {
		this.freeTicketAfterClosure = freeTicketAfterClosure;
	}

	/**
	 * @return the postponeDateSet
	 */
	public boolean isPostponeDateSet() {
		return postponeDateSet;
	}

	/**
	 * @return the postponeDate
	 */
	public Date getPostponeDate() {
		return postponeDate;
	}

	/**
	 * @param postponeDate the postponeDate to set
	 */
	public void setPostponeDate(final Date postponeDate) {
		if (postponeDate == null) {
			this.postponeDate = null;
		} else {
			this.postponeDate = new Timestamp(postponeDate.getTime());
		}
	}

	/**
	 * @param postponeDateSet the postponeDateSet to set
	 */
	public void setPostponeDateSet(final boolean postponeDateSet) {
		this.postponeDateSet = postponeDateSet;
	}

	/**
	 * @return the looseTicketManagement
	 */
	public boolean isLooseTicketManagement() {
		if (moveTargetCategory == null) {
			return false;
		}
		return !getDomainService().isDepartmentManager(
				moveTargetCategory.getDepartment(), getCurrentUser());
	}

	/**
	 * @return the looseTicketManagementMonitor
	 */
	public boolean isLooseTicketManagementMonitor() {
		return looseTicketManagementMonitor;
	}

	/**
	 * @param looseTicketManagementMonitor the looseTicketManagementMonitor to set
	 */
	public void setLooseTicketManagementMonitor(final boolean looseTicketManagementMonitor) {
		this.looseTicketManagementMonitor = looseTicketManagementMonitor;
	}

	/**
	 * @return the looseTicketManagementInvite
	 */
	public boolean isLooseTicketManagementInvite() {
		return looseTicketManagementInvite;
	}

	/**
	 * @param looseTicketManagementInvite the looseTicketManagementInvite to set
	 */
	public void setLooseTicketManagementInvite(final boolean looseTicketManagementInvite) {
		this.looseTicketManagementInvite = looseTicketManagementInvite;
	}

	/**
	 * @return the showConnectOnClosure
	 */
	public boolean isShowConnectOnClosure() {
		return showConnectOnClosure;
	}

	/**
	 * @param showConnectOnClosure the showConnectOnClosure to set
	 */
	public void setShowConnectOnClosure(final boolean showConnectOnClosure) {
		this.showConnectOnClosure = showConnectOnClosure;
	}

	/**
	 * @return the targetFaq
	 */
	public Faq getTargetFaq() {
		return targetFaq;
	}

	/**
	 * @return the faqTree
	 */
	public TreeModelBase getFaqTree() {
		return faqTree;
	}

	/**
	 * @return the connectBackAction
	 */
	public String getConnectBackAction() {
		return connectBackAction;
	}

	/**
	 * @param connectBackAction the connectBackAction to set
	 */
	public void setConnectBackAction(final String connectBackAction) {
		this.connectBackAction = connectBackAction;
	}

	/**
	 * @param downloadId the downloadId to set
	 */
	protected void setDownloadId(final Long downloadId) {
		this.downloadId = downloadId;
	}

	/**
	 * @return the downloadId
	 */
	public Long getDownloadId() {
		Long id = downloadId;
		downloadId = null;
		return id;
	}

	/**
	 * @return the paginator
	 */
	protected Paginator<ControlPanelEntry> getPaginator() {
		return paginator;
	}

	/**
	 * @param paginator the paginator to set
	 */
	public void setPaginator(final Paginator<ControlPanelEntry> paginator) {
		this.paginator = paginator;
	}

	/**
	 * @return the actionScopeToSet
	 */
	public String getActionScopeToSet() {
		return actionScopeToSet;
	}

	/**
	 * @param actionScopeToSet the actionScopeToSet to set
	 */
	public void setActionScopeToSet(final String actionScopeToSet) {
		this.actionScopeToSet = actionScopeToSet;
	}

	/**
	 * @return the fileInfoScopeToSet
	 */
	public String getFileInfoScopeToSet() {
		return fileInfoScopeToSet;
	}

	/**
	 * @param fileInfoScopeToSet the fileInfoScopeToSet to set
	 */
	public void setFileInfoScopeToSet(final String fileInfoScopeToSet) {
		this.fileInfoScopeToSet = fileInfoScopeToSet;
	}

	/**
	 * @return the addFaqTree
	 */
	public FaqTreeModel getAddFaqTree() {
		return addFaqTree;
	}

	/**
	 * @return the inviteTree
	 */
	public TreeModelBase getInviteTree() {
		return inviteTree;
	}

	/**
	 * @param moveTree the moveTree to set
	 */
	public void setMoveTree(final TreeModelBase moveTree) {
		this.moveTree = moveTree;
	}

	/**
	 * @return the tempUploadedFiles
	 */
	public List<TempUploadedFile> getTempUploadedFiles() {
		return tempUploadedFiles;
	}

	/**
	 * @return the number of temporary uploaded files.
	 */
	public int getTempUploadedFilesNumber() {
		if (tempUploadedFiles == null) {
			return 0;
		}
		return tempUploadedFiles.size();
	}

	/**
	 * @param tempUploadedFileToDeleteIndex the tempUploadedFileToDeleteIndex to set
	 */
	public void setTempUploadedFileToDeleteIndex(final int tempUploadedFileToDeleteIndex) {
		this.tempUploadedFileToDeleteIndex = tempUploadedFileToDeleteIndex;
	}

	/**
	 * @return the freeTicket
	 */
	public boolean isFreeTicket() {
		return freeTicket;
	}

	/**
	 * @param freeTicket the freeTicket to set
	 */
	public void setFreeTicket(final boolean freeTicket) {
		this.freeTicket = freeTicket;
	}

	/**
	 * @return the fileInfoToDelete
	 */
	public FileInfo getFileInfoToDelete() {
		return fileInfoToDelete;
	}

	/**
	 * @param fileInfoToDelete the fileInfoToDelete to set
	 */
	public void setFileInfoToDelete(FileInfo fileInfoToDelete) {
		this.fileInfoToDelete = fileInfoToDelete;
	}

	public TreeModelBase getFilteredTree() {
		return filteredTree;
	}

	public void setFilteredTree(TreeModelBase filteredTree) {
		this.filteredTree = filteredTree;
	}

	public String getCateFilter() {
		return cateFilter;
	}

	public void setCateFilter(String cateFilter) {
		this.cateFilter = cateFilter;
	}

	public String getCategoryMoveMembers() {
		return categoryMoveMembers;
	}

	public void setCategoryMoveMembers(String categoryMoveMembers) {
		this.categoryMoveMembers = categoryMoveMembers;
	}

}
