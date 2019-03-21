/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.TicketExtractor;
import org.esupportail.helpdesk.domain.TicketNavigation;
import org.esupportail.helpdesk.domain.TicketNavigator;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Bookmark;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.FileInfo;
import org.esupportail.helpdesk.domain.beans.Invitation;
import org.esupportail.helpdesk.domain.beans.Response;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.comparators.DepartmentManagerDisplayNameComparator;
import org.esupportail.helpdesk.domain.comparators.DepartmentManagerIdComparator;
import org.esupportail.helpdesk.domain.comparators.DepartmentManagerOrderComparator;
import org.esupportail.helpdesk.domain.userFormatting.UserFormattingService;
import org.esupportail.helpdesk.domain.userInfo.UserInfoProvider;
import org.esupportail.helpdesk.web.beans.ControlPanelEntry;
import org.esupportail.helpdesk.web.beans.FileInfoEntry;
import org.esupportail.helpdesk.web.beans.ResponseEntry;
import org.esupportail.helpdesk.web.beans.SpentTimeI18nFormatter;
import org.esupportail.helpdesk.web.beans.TicketHistoryEntry;

/**
 * This class holds the status of the current ticket of the ticket controller.
 */
@SuppressWarnings("serial")
public abstract class TicketControllerStateHolder extends AbstractContextAwareController {

	/**
	 * A bean to compare managers by display name.
	 */
	private static final Comparator<DepartmentManager> MANAGER_DISPLAY_NAME_COMPARATOR =
		new DepartmentManagerDisplayNameComparator();

	/**
	 * A bean to compare managers by id.
	 */
	private static final Comparator<DepartmentManager> MANAGER_ID_COMPARATOR =
		new DepartmentManagerIdComparator();

	/**
	 * A bean to compare managers by order.
	 */
	private static final Comparator<DepartmentManager> MANAGER_ORDER_COMPARATOR =
		new DepartmentManagerOrderComparator();

    /**
     * The current ticket.
     */
    private Ticket ticket;

    /**
     * The ticket extractor.
     */
    private TicketExtractor ticketExtractor;

    /**
     * The ticket navigator.
     */
    private TicketNavigator ticketNavigator;

    /**
     * The user info provider.
     */
    private UserInfoProvider userInfoProvider;

    /**
     * The user formatting service.
     */
    private UserFormattingService userFormattingService;

    /**
     * The ticket scope.
     */
    private String ticketScope;

    /**
     * The ticket label.
     */
    private String ticketLabel;

    /**
     * The ticket priority.
     */
    private int ticketPriority;

    /**
     * The ticket computer.
     */
    private String ticketComputer;

    /**
     * The ticket origin.
     */
    private String ticketOrigin;

    /**
     * The spent time (days).
     */
    private long  ticketSpentTimeDays;

    /**
     * The spent time (hours).
     */
    private long ticketSpentTimeHours;

    /**
     * The spent time (minutes).
     */
    private long ticketSpentTimeMinutes;

    /**
     * The sort order for target category members.
     */
    private String targetCategoryMembersPresentOrder;

    /**
     * The sort order for target non category members.
     */
    private String targetNonCategoryMembersPresentOrder;

	/**
	 * Bean constructor.
	 */
	public TicketControllerStateHolder() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.notNull(this.ticketExtractor,
				"property ticketExtractor of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.ticketNavigator,
				"property ticketNavigator of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.userInfoProvider,
				"property userInfoProvider of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.userFormattingService,
				"property userFormattingService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		setTicket(null);
		targetCategoryMembersPresentOrder = null;
		targetNonCategoryMembersPresentOrder = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[ticket=" + ticket
		+ "]";
	}

	/**
	 * Set the current ticket.
	 * @param ticket the ticket to set
	 */
	protected void setTicket(final Ticket ticket) {
		if (ticket == null) {
			setNullTicket();
		} else {
			setNotNullTicket(ticket);
		}
	}

	/**
	 * Set a null ticket.
	 */
	protected void setNullTicket() {
		ticket = null;
		ticketScope = null;
		ticketPriority = 0;
		ticketComputer = null;
		ticketOrigin = null;
		ticketLabel = null;
		setSpentTime(-1);
	}

	/**
	 * Set a not null ticket.
	 * @param ticket the ticket to set
	 */
	protected void setNotNullTicket(final Ticket ticket) {
		this.ticket = new Ticket(ticket);
		ticketScope = ticket.getScope();
		ticketPriority = ticket.getPriorityLevel();
		ticketComputer = ticket.getComputer();
		ticketOrigin = ticket.getOrigin();
		ticketLabel = ticket.getLabel();
		setSpentTime(ticket.getSpentTime());
		User currentUser = getCurrentUser();
		if (currentUser != null) {
			getDomainService().setTicketLastView(
					currentUser, ticket,
					new Timestamp(System.currentTimeMillis()));
			getDomainService().addHistoryItem(currentUser, ticket);
		}
	}

	/**
	 * @return the ticketNavigation
	 */
	protected TicketNavigation getTicketNavigation() {
		return ticketNavigator.getNavigation(getCurrentUser(), ticket, getClient());
	}

	/**
	 * @return the previous unread ticket
	 */
	public Ticket getPreviousUnreadTicket() {
		if (ticket == null) {
			return null;
		}
		return getTicketNavigation().getPreviousUnread();
	}

	/**
	 * @return the previous visible ticket
	 */
	public Ticket getPreviousVisibleTicket() {
		if (ticket == null) {
			return null;
		}
		return getTicketNavigation().getPreviousVisible();
	}

	/**
	 * @return the next visible ticket
	 */
	public Ticket getNextVisibleTicket() {
		if (ticket == null) {
			return null;
		}
		return getTicketNavigation().getNextVisible();
	}

	/**
	 * @return the next unread ticket
	 */
	public Ticket getNextUnreadTicket() {
		if (ticket == null) {
			return null;
		}
		return getTicketNavigation().getNextUnread();
	}

	/**
	 * @param i18nKeySuffix
	 * @param theTicket
	 * @return a navigation ticket title
	 */
	protected String getTicketTitle(
			final String i18nKeySuffix,
			final Ticket theTicket) {
		if (theTicket == null) {
			return null;
		}
		return getString(
				"TICKET_VIEW.BUTTON.TICKET_NAVIGATION." + i18nKeySuffix,
				String.valueOf(theTicket.getId()), theTicket.getLabel());
	}


	/**
	 * @param i18nKeySuffix
	 * @param theTicket
	 * @return a navigation ticket title
	 */
	public List<ControlPanelEntry> getTicketsByOwner() {

		List<ControlPanelEntry> controlPanelEntries = new ArrayList<ControlPanelEntry>();
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		if (ticket == null) {
			return null;
		}
		tickets = getDomainService().getTicketsByOwner(ticket.getCreator());
		List<Department> visibleDepartments = 
				getDomainService().getTicketViewDepartments(getCurrentUser(), getClient());
		
		for (Ticket ticket : tickets) {
			controlPanelEntries.add(new ControlPanelEntry(
					ticket, 
					!getDomainService().hasTicketChangedSinceLastView(ticket, getCurrentUser()), 
					getDomainService().userCanViewTicket(
							getCurrentUser(), ticket, visibleDepartments),
					getDomainService().getBookmark(getCurrentUser(), ticket) != null));
		}
		
		return controlPanelEntries;
	}

	/**
	 * @return the previous unread ticket title
	 */
	public String getPreviousUnreadTicketTitle() {
		return getTicketTitle("PREVIOUS_UNREAD", getPreviousUnreadTicket());
	}

	/**
	 * @return the previous visible ticket title
	 */
	public String getPreviousVisibleTicketTitle() {
		return getTicketTitle("PREVIOUS", getPreviousVisibleTicket());
	}

	/**
	 * @return the next visible ticket title
	 */
	public String getNextVisibleTicketTitle() {
		return getTicketTitle("NEXT", getNextVisibleTicket());
	}

	/**
	 * @return the next unread ticket title
	 */
	public String getNextUnreadTicketTitle() {
		return getTicketTitle("NEXT_UNREAD", getNextUnreadTicket());
	}

	/**
	 * Split the spent time into days, hours and minutes.
	 * @param spentTime
	 */
	private void setSpentTime(final long spentTime) {
		ticketSpentTimeDays = SpentTimeI18nFormatter.getDays(spentTime);
		ticketSpentTimeHours = SpentTimeI18nFormatter.getHours(spentTime);
		ticketSpentTimeMinutes = SpentTimeI18nFormatter.getMinutes(spentTime);
	}

	/**
	 * @return the spent time from days, hours and minutes.
	 */
	protected long getSpentTime() {
		return SpentTimeI18nFormatter.getSpentTime(
				ticketSpentTimeDays, ticketSpentTimeHours, ticketSpentTimeMinutes);
	}

	/**
	 * @return the ticket
	 */
	public Ticket getTicket() {
		return ticket;
	}

	/**
	 * @param ticketExtractor the ticketExtractor to set
	 */
	public void setTicketExtractor(final TicketExtractor ticketExtractor) {
		this.ticketExtractor = ticketExtractor;
	}

	/**
	 * @return the historyEntries
	 */
	@RequestCache
	public List<TicketHistoryEntry> getHistoryEntries() {
		if (ticket == null) {
			return null;
		}
		List<TicketHistoryEntry> historyEntries = new ArrayList<TicketHistoryEntry>();
		boolean invited = isInvited();
		for (Action action : getDomainService().getActions(ticket)) {
			historyEntries.add(new TicketHistoryEntry(
					action,
					isCanUpdateInformation(ticket, action),
					getDomainService().userCanViewActionMessage(
							getCurrentUser(), invited, action),
					getDomainService().userCanChangeActionScope(
							getCurrentUser(), action),
					getDomainService().getAlerts(action),
					getDomainService().getActionStyleClass(action)));
		}
		return historyEntries;
	}

	/**
	 * @return the fileInfoEntries
	 */
	@RequestCache
	public List<FileInfoEntry> getFileInfoEntries() {
		if (ticket == null) {
			return null;
		}
		List<FileInfoEntry> fileInfoEntries = new ArrayList<FileInfoEntry>();
		boolean invited = isInvited();
		for (FileInfo fileInfo : getDomainService().getFileInfos(ticket)) {
			fileInfoEntries.add(new FileInfoEntry(
					fileInfo,
					getDomainService().userCanDownload(
							getCurrentUser(), invited, fileInfo),
							getDomainService().userCanChangeFileInfoScope(
									getCurrentUser(), fileInfo)));
		}
		return fileInfoEntries;
	}

	
	
	public boolean isCanUpdateInformation(Ticket ticket, Action action){
		//Récupération du paramètre en configuration qui propose ou non le service de modification des commentaires
		if(getDomainService().isTicketCommentModification() == false){
			return false;
		}
		//cas des admins : ils ont accès a tous les commentaires
		if(getCurrentUser().getAdmin()){
			if (action.getUser() != null){
				return true;
			}
		//pour les getsionnaires de service : ils ont accès a tous les commentaires de leur service
		}  else if (getDomainService().userCanEditDepartmentManagers(getCurrentUser(), ticket.getDepartment())){
			if (action.getUser() != null){
				return true;
			}
		//pour les autres : le commentaire de la dernière action est modification
		}else {
		
			Action lastAction = getDomainService().getLastActionWithoutUpload(ticket);
			if(lastAction.getId() == action.getId()) {
				if(lastAction.getUser() != null && lastAction.getUser().getId().equals(getCurrentUser().getId()) && action.getUser() != null){
					return true;
				 }
			 }
		}
		return false;
	}

	/**
	 * @return the fileInfoEntriesNumber
	 */
	@RequestCache
	public int getFileInfoEntriesNumber() {
		List<FileInfoEntry> fileInfoEntries = getFileInfoEntries();
		if (fileInfoEntries == null) {
			return 0;
		}
		return fileInfoEntries.size();
	}

	/**
	 * @return the responseEntries
	 */
	@RequestCache
	public List<ResponseEntry> getResponseEntries() {
		if (ticket == null) {
			return null;
		}
		String signature = userFormattingService.format(getCurrentUser(), getTicket().getAnonymous(), getLocale(), getCurrentUser());
		List<ResponseEntry> responseEntries = new ArrayList<ResponseEntry>();
		for (Response response : getDomainService().getUserResponses(getCurrentUser())) {
			responseEntries.add(new ResponseEntry(response, signature));
		}
		for (Response response : getDomainService().getDepartmentResponses(ticket.getDepartment())) {
			responseEntries.add(new ResponseEntry(response, signature));
		}
		for (Response response : getDomainService().getGlobalResponses()) {
			responseEntries.add(new ResponseEntry(response, signature));
		}
		return responseEntries;
	}

	/**
	 * @return the responseItems
	 */
	@RequestCache
	public List<SelectItem> getResponseItems() {
		if (ticket == null) {
			return null;
		}
		List<ResponseEntry> responseEntries = getResponseEntries();
		List<SelectItem> responseItems = new ArrayList<SelectItem>();
		if (!responseEntries.isEmpty()) {
			responseItems.add(new SelectItem(
					"0", getString("TICKET_ACTION.TEXT.SELECT_RESPONSE")));
			for (ResponseEntry responseEntry : responseEntries) {
				Response response = responseEntry.getResponse();
				String label;
				if (response.isGlobalResponse()) {
					label = getString(
							"RESPONSES.TEXT.GLOBAL_RESPONSE",
							response.getLabel());
				} else if (response.isDepartmentResponse()) {
					label = getString(
							"RESPONSES.TEXT.DEPARTMENT_RESPONSE",
							response.getDepartment().getLabel(),
							response.getLabel());
				} else {
					label = getString(
							"RESPONSES.TEXT.USER_RESPONSE",
							response.getUser().getDisplayName(),
							response.getLabel());
				}
				responseItems.add(new SelectItem(
						String.valueOf(response.getId()), label));
			}
		}
		return responseItems;
	}

	/**
	 * @return the ticketScope
	 */
	public String getTicketScope() {
		return ticketScope;
	}

	/**
	 * @param ticketScope the ticketScope to set
	 */
	public void setTicketScope(final String ticketScope) {
		this.ticketScope = StringUtils.nullIfEmpty(ticketScope);
	}

	/**
	 * @return the ticketPriority
	 */
	public int getTicketPriority() {
		return ticketPriority;
	}

	/**
	 * @param ticketPriority the ticketPriority to set
	 */
	public void setTicketPriority(final int ticketPriority) {
		this.ticketPriority = ticketPriority;
	}

	/**
	 * @return the ticketComputer
	 */
	public String getTicketComputer() {
		return ticketComputer;
	}

	/**
	 * @param ticketComputer the ticketComputer to set
	 */
	public void setTicketComputer(final String ticketComputer) {
		this.ticketComputer = StringUtils.nullIfEmpty(ticketComputer);
	}

	/**
	 * @return the ticketSpentTimeDays
	 */
	public long getTicketSpentTimeDays() {
		return ticketSpentTimeDays;
	}

	/**
	 * @param ticketSpentTimeDays the ticketSpentTimeDays to set
	 */
	public void setTicketSpentTimeDays(final long ticketSpentTimeDays) {
		this.ticketSpentTimeDays = ticketSpentTimeDays;
	}

	/**
	 * @return the ticketSpentTimeHours
	 */
	public long getTicketSpentTimeHours() {
		return ticketSpentTimeHours;
	}

	/**
	 * @param ticketSpentTimeHours the ticketSpentTimeHours to set
	 */
	public void setTicketSpentTimeHours(final long ticketSpentTimeHours) {
		this.ticketSpentTimeHours = ticketSpentTimeHours;
	}

	/**
	 * @return the ticketSpentTimeMinutes
	 */
	public long getTicketSpentTimeMinutes() {
		return ticketSpentTimeMinutes;
	}

	/**
	 * @param ticketSpentTimeMinutes the ticketSpentTimeMinutes to set
	 */
	public void setTicketSpentTimeMinutes(final long ticketSpentTimeMinutes) {
		this.ticketSpentTimeMinutes = ticketSpentTimeMinutes;
	}

	/**
	 * @return the userCanViewTicket
	 */
	@RequestCache
	public boolean isUserCanViewTicket() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanViewTicket(
				getCurrentUser(), getClient(), ticket);
	}

	/**
	 * @return the userCanApproveClosure
	 */
	@RequestCache
	public boolean isUserCanApproveClosure() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanApproveClosure(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanRefuseClosure
	 */
	@RequestCache
	public boolean isUserCanRefuseClosure() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanRefuseClosure(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanGiveInformation
	 */
	@RequestCache
	public boolean isUserCanGiveInformation() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanGiveInformation(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanDeleteFileInfo
	 */
	@RequestCache
	public boolean isUserCanDeleteFileInfo() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanDeleteFileInfo(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanCancel
	 */
	@RequestCache
	public boolean isUserCanCancel() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanCancel(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanRequestInformation
	 */
	@RequestCache
	public boolean isUserCanRequestInformation() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanRequestInformation(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanClose
	 */
	@RequestCache
	public boolean isUserCanClose() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanClose(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanRefuse
	 */
	@RequestCache
	public boolean isUserCanRefuse() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanRefuse(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanConnect
	 */
	@RequestCache
	public boolean isUserCanConnect() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanConnect(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanPostpone
	 */
	@RequestCache
	public boolean isUserCanPostpone() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanPostpone(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanCancelPostponement
	 */
	@RequestCache
	public boolean isUserCanCancelPostponement() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanCancelPostponement(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanReopen
	 */
	@RequestCache
	public boolean isUserCanReopen() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanReopen(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanMove
	 */
	@RequestCache
	public boolean isUserCanMove() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanMove(getCurrentUser(), ticket);
	}


	/**
	 * @return the userCanMoveBackCategorie
	 */
	@RequestCache
	public boolean isUserCanMoveBack() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanMoveBack(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanTake
	 */
	@RequestCache
	public boolean isUserCanTake() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanTake(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanTakeAndClose
	 */
	@RequestCache
	public boolean isUserCanTakeAndClose() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanTakeAndClose(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanTakeAndRequestInformation
	 */
	@RequestCache
	public boolean isUserCanTakeAndRequestInformation() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanTakeAndRequestInformation(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanFree
	 */
	@RequestCache
	public boolean isUserCanFree() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanFree(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanAssign
	 */
	@RequestCache
	public boolean isUserCanAssign() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanAssign(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanChangeOwner
	 */
	@RequestCache
	public boolean isUserCanChangeOwner() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanChangeOwner(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanChangeLabel
	 */
	@RequestCache
	public boolean isUserCanChangeLabel() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanChangeLabel(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanChangeScope
	 */
	@RequestCache
	public boolean isUserCanChangeScope() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanChangeScope(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanChangeOrigin
	 */
	@RequestCache
	public boolean isUserCanChangeOrigin() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanChangeOrigin(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanChangeComputer
	 */
	@RequestCache
	public boolean isUserCanChangeComputer() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanChangeComputer(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanChangePriority
	 */
	@RequestCache
	public boolean isUserCanChangePriority() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanChangePriority(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanChangeSpentTime
	 */
	@RequestCache
	public boolean isUserCanChangeSpentTime() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanChangeSpentTime(getCurrentUser(), ticket);
	}

	/**
	 * @return true if the user can change the manager
	 */
	@RequestCache
	public boolean isUserCanChangeManager() {
		return isUserCanFree() || isUserCanTake() || isUserCanTakeAndClose() || isUserCanAssign();
	}

	/**
	 * @return true if the user can invite
	 */
	@RequestCache
	public boolean isUserCanInvite() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanInvite(getCurrentUser(), ticket);
	}

	/**
	 * @return true if the user can invite a group
	 */
	@RequestCache
	public boolean isUserCanInviteGroup() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanInviteGroup(getCurrentUser(), ticket);
	}

	/**
	 * @return true if the user can remove invitations
	 */
	@RequestCache
	public boolean isUserCanRemoveInvitations() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanRemoveInvitations(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanUseCannedResponses
	 */
	@RequestCache
	public boolean isUserCanUseCannedResponses() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().userCanUseResponses(getCurrentUser(), ticket);
	}

	/**
	 * @return the userCanSetNoAlert
	 */
	@RequestCache
	public boolean isUserCanSetNoAlert() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().isDepartmentManager(ticket.getDepartment(), getCurrentUser());
	}

	/**
	 * @return the tickets visible on the control panel.
	 */
	protected List<Department> getTicketViewVisibleDepartments() {
		return getDomainService().getTicketViewDepartments(getCurrentUser(),  getClient());
	}

	/**
	 * @return the userCanViewConnectionTicket
	 */
	@RequestCache
	public boolean isUserCanViewConnectionTicket() {
		if (ticket == null) {
			return false;
		}
		if (ticket.getConnectionTicket() == null) {
			return false;
		}
		return getDomainService().userCanViewTicket(
				getCurrentUser(), ticket.getConnectionTicket(),
				getTicketViewVisibleDepartments());
	}

	/**
	 * @return the userCanViewConnectionArchivedTicket
	 */
	@RequestCache
	public boolean isUserCanViewConnectionArchivedTicket() {
		if (ticket == null) {
			return false;
		}
		if (ticket.getConnectionArchivedTicket() == null) {
			return false;
		}
		return getDomainService().userCanViewArchivedTicket(
				getCurrentUser(), ticket.getConnectionArchivedTicket(),
				getTicketViewVisibleDepartments());
	}

	/**
	 * @return the departments visible for the FAQ.
	 */
	protected List<Department> getFaqViewVisibleDepartments() {
		return getDomainService().getTicketViewDepartments(getCurrentUser(),  getClient());
	}

	/**
	 * @return the userCanViewConnectionFaq
	 */
	@RequestCache
	public boolean isUserCanViewConnectionFaq() {
		if (ticket == null) {
			return false;
		}
		if (ticket.getConnectionFaq() == null) {
			return false;
		}
		return getDomainService().userCanViewFaq(
				getCurrentUser(), ticket.getConnectionFaq(),
				getFaqViewVisibleDepartments());
	}

	/**
	 * @return true if the current user monitors the current ticket.
	 */
	public boolean isUserMonitorsTicket() {
		return getDomainService().userMonitorsTicket(getCurrentUser(), ticket);
	}

	/**
	 * Set or unset the monitoring of the current ticket by the current user.
	 * @param userMonitorsTicket
	 */
	public void setUserMonitorsTicket(final boolean userMonitorsTicket) {
		if (userMonitorsTicket) {
			getDomainService().setTicketMonitoring(getCurrentUser(), getTicket());
		} else {
			getDomainService().unsetTicketMonitoring(getCurrentUser(), getTicket());
		}
	}

	/**
	 * @return the ticketExtractor
	 */
	protected TicketExtractor getTicketExtractor() {
		return ticketExtractor;
	}

	/**
	 * @return the ticketOrigin
	 */
	public String getTicketOrigin() {
		return ticketOrigin;
	}

	/**
	 * @param ticketOrigin the ticketOrigin to set
	 */
	public void setTicketOrigin(final String ticketOrigin) {
		this.ticketOrigin = StringUtils.nullIfEmpty(ticketOrigin);
	}

	/**
	 * @return the ticketLabel
	 */
	public String getTicketLabel() {
		return ticketLabel;
	}

	/**
	 * @param ticketLabel the ticketLabel to set
	 */
	public void setTicketLabel(final String ticketLabel) {
		this.ticketLabel = StringUtils.nullIfEmpty(ticketLabel);
	}

	/**
	 * @return the invitations
	 */
	@RequestCache
	public List<Invitation> getInvitations() {
		if (ticket == null) {
			return null;
		}
		return getDomainService().getInvitations(ticket);
	}

	/**
	 * @return the invitationsNumber
	 */
	@RequestCache
	public int getInvitationsNumber() {
		List<Invitation> invitations = getInvitations();
		if (invitations == null) {
			return 0;
		}
		return invitations.size();
	}

	/**
	 * @return the monitoringUsers
	 */
	@RequestCache
	public List<User> getMonitoringUsers() {
		if (ticket == null) {
			return null;
		}
		return getDomainService().getMonitoringUsers(ticket, false);
	}

	/**
	 * @return the monitoringUsers
	 */
	private List<User> getMonitoringUsersMandatory() {
		if (ticket == null) {
			return null;
		}
		return getDomainService().getMonitoringUsers(ticket, true);
	}
	/**
	 * @return the monitoringUsers
	 */
	public boolean	isUserInMonitoringUsersMandatory(){
		if(getMonitoringUsersMandatory().contains(getCurrentUser())) {
			return true;
		}
		return false;
	}
	/**
	 * @return the monitoringUsersNumber
	 */
	@RequestCache
	public int getMonitoringUsersNumber() {
		List<User> monitoringUsers = getMonitoringUsers();
		if (monitoringUsers == null) {
			return 0;
		}
		return monitoringUsers.size();
	}

	/**
	 * @return the invited
	 */
	@RequestCache
	protected boolean isInvited() {
		if (ticket == null) {
			return false;
		}
		return getDomainService().isInvited(getCurrentUser(), ticket);
	}

	/**
	 * @return the targetNonCategoryMembers
	 */
	@RequestCache
	public List<DepartmentManager> getTargetNonCategoryMembers() {
		List<DepartmentManager> departmentManagers =
			getDomainService().getAvailableDepartmentManagers(ticket.getDepartment());
		List<DepartmentManager> targetCategoryMembers = getTargetCategoryMembers();
		List<DepartmentManager> targetNonCategoryMembers = new ArrayList<DepartmentManager>();
		for (DepartmentManager departmentManager : departmentManagers) {
			User user = departmentManager.getUser();
			if (!user.equals(getCurrentUser()) && !user.equals(ticket.getManager())
					&& !targetCategoryMembers.contains(departmentManager)) {
				targetNonCategoryMembers.add(departmentManager);
			}
		}
		Collections.sort(targetNonCategoryMembers, MANAGER_DISPLAY_NAME_COMPARATOR);
		return targetNonCategoryMembers;
	}

	/**
	 * @return the number of targetNonCategoryMembers
	 */
	@RequestCache
	public int getTargetNonCategoryMembersNumber() {
		List<DepartmentManager> targetNonCategoryMembers = getTargetNonCategoryMembers();
		if (targetNonCategoryMembers == null) {
			return 0;
		}
		return targetNonCategoryMembers.size();
	}

	/**
	 * @return the targetCategoryMembers
	 */
	@RequestCache
	public List<DepartmentManager> getTargetCategoryMembers() {
		if (ticket == null) {
			return null;
		}
		List<DepartmentManager> targetCategoryMembers = new ArrayList<DepartmentManager>();
		for (DepartmentManager departmentManager
				: getDomainService().getEffectiveAvailableDepartmentManagers(
						ticket.getCategory())) {
			User user = departmentManager.getUser();
			if (!user.equals(getCurrentUser()) && !user.equals(ticket.getManager())) {
				targetCategoryMembers.add(departmentManager);
			}
		}
		Collections.sort(
					targetCategoryMembers, MANAGER_DISPLAY_NAME_COMPARATOR);
		return targetCategoryMembers;
	}

	/**
	 * @return the number of targetCategoryMembers
	 */
	@RequestCache
	public int getTargetCategoryMembersNumber() {
		List<DepartmentManager> targetCategoryMembers = getTargetCategoryMembers();
		if (targetCategoryMembers == null) {
			return 0;
		}
		return targetCategoryMembers.size();
	}

	/**
	 * @return the ownerInfo
	 */
	@RequestCache
	public String getOwnerInfo() {
		String ownerInfo = null;
		if (getDomainService().isDepartmentManager(getCurrentUser())) {
			ownerInfo = userInfoProvider.getInfo(
					ticket.getOwner(),
					getSessionController().getLocale());
		}
		return ownerInfo;
	}

	private String addLabelCategoryPatent(Category categorie, String labelCategories){
		if(categorie.getParent() != null){
			labelCategories = labelCategories.concat(" -> " + categorie.getParent().getLabel());
			labelCategories = addLabelCategoryPatent(categorie.getParent(), labelCategories);
		}
		return labelCategories;
		
	}

	/**
	 * @return the ownerInfo
	 */
	@RequestCache
	public String getLabelCategories() {
		String labelCategories = "";
		return addLabelCategoryPatent(getTicket().getCategory(), labelCategories);
	}


	/**
	 * @return the managerInfo
	 */
	@RequestCache
	public String getManagerInfo() {
		String managerInfo = null;
		if (getDomainService().isDepartmentManager(getCurrentUser())) {
			if (ticket.getManager() != null) {
				managerInfo = userInfoProvider.getInfo(
						ticket.getManager(),
						getSessionController().getLocale());
			}
		}
		return managerInfo;
	}

	/**
	 * @return the bookmark
	 */
	@RequestCache
	public Bookmark getBookmark() {
		return getDomainService().getBookmark(getCurrentUser(), ticket);
	}

	/**
	 * @return the userInfoProvider
	 */
	protected UserInfoProvider getUserInfoProvider() {
		return userInfoProvider;
	}

	/**
	 * @param userInfoProvider the userInfoProvider to set
	 */
	public void setUserInfoProvider(final UserInfoProvider userInfoProvider) {
		this.userInfoProvider = userInfoProvider;
	}

	/**
	 * @return the targetCategoryMembersPresentOrder
	 */
	public String getTargetCategoryMembersPresentOrder() {
		return targetCategoryMembersPresentOrder;
	}

	/**
	 * @param targetCategoryMembersPresentOrder the targetCategoryMembersPresentOrder to set
	 */
	public void setTargetCategoryMembersPresentOrder(
			final String targetCategoryMembersPresentOrder) {
		this.targetCategoryMembersPresentOrder = targetCategoryMembersPresentOrder;
	}

	/**
	 * @return the targetNonCategoryMembersPresentOrder
	 */
	public String getTargetNonCategoryMembersPresentOrder() {
		return targetNonCategoryMembersPresentOrder;
	}

	/**
	 * @param targetNonCategoryMembersPresentOrder the targetNonCategoryMembersPresentOrder to set
	 */
	public void setTargetNonCategoryMembersPresentOrder(
			final String targetNonCategoryMembersPresentOrder) {
		this.targetNonCategoryMembersPresentOrder = targetNonCategoryMembersPresentOrder;
	}

	/**
	 * @return the userFormattingService
	 */
	protected UserFormattingService getUserFormattingService() {
		return userFormattingService;
	}

	/**
	 * @param userFormattingService the userFormattingService to set
	 */
	public void setUserFormattingService(final UserFormattingService userFormattingService) {
		this.userFormattingService = userFormattingService;
	}

	/**
	 * @return the ticketNavigator
	 */
	protected TicketNavigator getTicketNavigator() {
		return ticketNavigator;
	}

	/**
	 * @param ticketNavigator the ticketNavigator to set
	 */
	public void setTicketNavigator(final TicketNavigator ticketNavigator) {
		this.ticketNavigator = ticketNavigator;
	}

}
