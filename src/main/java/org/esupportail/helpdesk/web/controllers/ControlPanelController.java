/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.faces.model.SelectItem;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.web.beans.Paginator;
import org.esupportail.helpdesk.domain.ControlPanel;
import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Bookmark;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.exceptions.ArchivedTicketNotFoundException;
import org.esupportail.helpdesk.exceptions.TicketNotFoundException;
import org.esupportail.helpdesk.web.beans.ControlPanelColumnOrderer;
import org.esupportail.helpdesk.web.beans.ControlPanelEntry;
import org.esupportail.helpdesk.web.beans.ControlPanelPaginator;

/**
 * The control panel controller.
 */
public class ControlPanelController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2407103812813627023L;

    /**
     * The paginator.
     */
    private Paginator<ControlPanelEntry> paginator;

    /**
     * The ticket to view.
     */
    private Ticket ticketToView;

    /**
     * The ticket to view.
     */
    private Ticket ticketToMarkReadUnread;

    /**
     * The ticket to bookmark.
     */
    private Ticket ticketToBookmark;

    /**
     * The ticket Controller.
     */
    private TicketController ticketController;

    /**
     * The archived ticket Controller.
     */
    private ArchivedTicketController archivedTicketController;

    /**
     * The columns orderer.
     */
    private ControlPanelColumnOrderer columnsOrderer;

    /**
     * The paginator.
     */    
    private ControlPanelPaginator panelPaginator;
    
	/**
     * True for the column edit mode.
     */
    private boolean editColumns;

    /**
     * The items for missing columns.
     */
    private List<SelectItem> addColumnItems;

    /**
     * The name of the column to add.
     */
    private String columnToAdd;

    /**
     * The string of the searched ticket.
     */
    private String ticketNumberString;


	/**
	 * Bean constructor.
	 */
	public ControlPanelController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.notNull(paginator,
				"property paginator of class " + getClass().getName()
				+ " can not be null");
		Assert.notNull(ticketController,
				"property ticketController of class " + getClass().getName()
				+ " can not be null");
		Assert.notNull(archivedTicketController,
				"property archivedTicketController of class " + getClass().getName()
				+ " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		ticketToView = null;
		editColumns = false;
		columnToAdd = null;
		ticketNumberString = null;
		ticketToMarkReadUnread = null;
		ticketToBookmark = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[paginator=" + paginator
		+ "]";
	}

	/**
	 * @return true if the current user is allowed to access the view.
	 */
	public boolean isPageAuthorized() {
		return getCurrentUser() != null;
	}

	/**
	 * @return the userDepartmentItems
	 */
	public boolean isManager() {
		return getDomainService().isDepartmentManager(getCurrentUser());
	}

	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			return null;
		}
		User currentUser = getCurrentUser();
		if (currentUser.getControlPanelPageSize() <= 0) {
			currentUser.setControlPanelPageSize(paginator.getDefaultPageSize());
		}
		if (currentUser.getControlPanelUserInterface()) {
			if (currentUser.getControlPanelUserDepartmentFilter() != null) {
				if (!getDomainService().isDepartmentVisibleForTicketView(
						currentUser, currentUser.getControlPanelUserDepartmentFilter(),
						getSessionController().getClient())) {
					currentUser.setControlPanelUserDepartmentFilter(null);
				}
			}
		} else {
			if (currentUser.getControlPanelManagerDepartmentFilter() != null) {
				 if (!getDomainService().isDepartmentManager(
						currentUser.getControlPanelManagerDepartmentFilter(), currentUser)) {
					currentUser.setControlPanelManagerDepartmentFilter(null);
				}
			}
		}
		getDomainService().updateUser(getCurrentUser());
		paginator.setPageSize(getCurrentUser().getControlPanelPageSize());
		refreshColumnOrderer();
		paginator.forceReload();
		return "navigationControlPanel";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doTakeAndClose() {
		enter();
		return ticketController.doTakeAndClose();
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doClose() {
		enter();
		return ticketController.doClose();
	}
	
	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String cancel() {
		enter();
		return ticketController.cancel();
	}
	
	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String viewTicket() {
		ticketToView = getDomainService().reloadTicket(ticketToView);
		if (getDomainService().hasTicketChangedSince(ticketToView, paginator.getLoadTime())) {
			addWarnMessage(
					null, "CONTROL_PANEL.MESSAGE.TICKET_HAS_CHANGED",
					String.valueOf(ticketToView.getId()));
			return null;
		}
		if (!getDomainService().userCanViewTicket(getCurrentUser(), getClient(), ticketToView)) {
			addUnauthorizedActionMessage();
			return null;
		}
		ticketController.setTicket(ticketToView);
		ticketController.setBackPage("controlPanel");
		return "view";
	}

	/**
	 * JSF callback.
	 */
	public void markAllRead() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		for (ControlPanelEntry cpe : paginator.getVisibleItems()) {
			getDomainService().setTicketLastView(getCurrentUser(), cpe.getTicket(), now);
		}
		paginator.forceReload();
	}

	/**
	 * JSF callback.
	 */
	public void markTicketRead() {
		getDomainService().setTicketLastView(
				getCurrentUser(), ticketToMarkReadUnread,
				new Timestamp(System.currentTimeMillis()));
		paginator.forceReload();
	}

	/**
	 * JSF callback.
	 */
	public void markTicketUnread() {
		getDomainService().setTicketLastView(getCurrentUser(), ticketToMarkReadUnread, null);
		paginator.forceReload();
	}

	/**
	 * JSF callback.
	 */
	public void bookmarkTicket() {
		getDomainService().addBookmark(getCurrentUser(), ticketToBookmark);
		paginator.forceReload();
	}

	/**
	 * JSF callback.
	 */
	public void unbookmarkTicket() {
		Bookmark bookmark = getDomainService().getBookmark(getCurrentUser(), ticketToBookmark);
		if (bookmark != null) {
			getDomainService().deleteBookmark(bookmark);
		}
		paginator.forceReload();
	}

	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String gotoTicket() {
		if (!org.springframework.util.StringUtils.hasText(ticketNumberString)) {
			addErrorMessage(null, "CONTROL_PANEL.MESSAGE.ENTER_TICKET_NUMBER");
			ticketNumberString = null;
			return null;
		}
		long ticketNumber;
		try {
			ticketNumberString = ticketNumberString.trim();
			ticketNumber = Long.parseLong(ticketNumberString);
		} catch (NumberFormatException e) {
			addErrorMessage(null, "CONTROL_PANEL.MESSAGE.INVALID_TICKET_NUMBER", ticketNumberString);
			return null;
		}
		Ticket ticket = null;
		ArchivedTicket archivedTicket = null;
		try {
			ticket = getDomainService().getTicket(ticketNumber);
		} catch (TicketNotFoundException e) {
			try {
				archivedTicket = getDomainService().getArchivedTicketByOriginalId(ticketNumber);
			} catch (ArchivedTicketNotFoundException e2) {
				addErrorMessage(null, "CONTROL_PANEL.MESSAGE.TICKET_NOT_FOUND",
						String.valueOf(ticketNumber));
				return null;
			}
		}
		if (ticket != null) {
			if (!getDomainService().userCanViewTicket(getCurrentUser(), getClient(), ticket)) {
				addErrorMessage(null, "CONTROL_PANEL.MESSAGE.TICKET_NOT_VISIBLE",
						String.valueOf(ticketNumber));
				return null;
			}
			ticketController.setTicket(ticket);
			return "gotoTicket";
		}
		if (!getDomainService().userCanViewArchivedTicket(getCurrentUser(), getClient(), archivedTicket)) {
			addErrorMessage(null, "CONTROL_PANEL.MESSAGE.TICKET_NOT_VISIBLE", String.valueOf(ticketNumber));
			return null;
		}
		archivedTicketController.setArchivedTicket(archivedTicket);
		return "gotoArchivedTicket";
	}

	/**
	 * @return true if the current user is a department manager.
	 */
	@RequestCache
	public boolean isCurrentUserDepartmentManager() {
		return getDomainService().isDepartmentManager(getCurrentUser());
	}

	/**
	 * @return the paginator.
	 */
	public Paginator<ControlPanelEntry> getPaginator() {
		return paginator;
	}

	/**
	 * @return the current user.
	 */
	@Override
	public User getCurrentUser() {
		return super.getCurrentUser();
	}

	/**
	 * @return the managerInvolvementItems
	 */
	@RequestCache
	public List<SelectItem> getManagerInvolvementItems() {
		List<SelectItem> managerInvolvementItems = new ArrayList<SelectItem>();
		managerInvolvementItems.add(new SelectItem(
				ControlPanel.MANAGER_INVOLVEMENT_FILTER_ANY,
				getString("CONTROL_PANEL.INVOLVEMENT_FILTER.ANY")));
		managerInvolvementItems.add(new SelectItem(
				ControlPanel.MANAGER_INVOLVEMENT_FILTER_FREE,
				getString("CONTROL_PANEL.INVOLVEMENT_FILTER.MANAGER.FREE")));
		managerInvolvementItems.add(new SelectItem(
				ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED,
				getString("CONTROL_PANEL.INVOLVEMENT_FILTER.MANAGER.MANAGED")));
		managerInvolvementItems.add(new SelectItem(
				ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_FREE,
				getString("CONTROL_PANEL.INVOLVEMENT_FILTER.MANAGER.MANAGED_OR_FREE")));
		managerInvolvementItems.add(new SelectItem(
				ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_INVITED,
				getString("CONTROL_PANEL.INVOLVEMENT_FILTER.MANAGER.MANAGED_OR_INVITED")));
		managerInvolvementItems.add(new SelectItem(
				ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE,
				getString("CONTROL_PANEL.INVOLVEMENT_FILTER.MANAGER.MANAGED_INVITED_OR_FREE")));
		return managerInvolvementItems;
	}

	/**
	 * @return the managerManagerItems
	 */
	@RequestCache
	public List<SelectItem> getManagerManagerItems() {
		List<SelectItem> managerInvolvementItems = new ArrayList<SelectItem>();
		TreeSet<DepartmentManager> listeDepartmentManagerTriee = new TreeSet<>();
		Department department = getCurrentUser().getControlPanelManagerDepartmentFilter();
		if (department != null) {
			//tickets libres : pas de choix sur le gestionnaire
			if(getCurrentUser().getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_FREE)) {
				   managerInvolvementItems.add(
						new SelectItem("", getString("CONTROL_PANEL.MANAGER_FILTER.ANY")));
				   return managerInvolvementItems;
			}
			//pas de "tous managers" pour les ...,invité
			if(!getCurrentUser().getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE) && 
			   !getCurrentUser().getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_INVITED)){
				   managerInvolvementItems.add(
						new SelectItem("", getString("CONTROL_PANEL.MANAGER_FILTER.ANY")));
			   
			} 
			for (DepartmentManager manager : getDomainService().getDepartmentManagers(department)) {
				listeDepartmentManagerTriee.add(manager);
				
			}
			for (DepartmentManager manager : listeDepartmentManagerTriee) {
				managerInvolvementItems.add(
					new SelectItem(manager.getUser(), manager.getUser().getDisplayName()));
			}
		} else {
			//cas ou le service = Tous : on alimente la liste des gestionnaires de tous les services ou l'on est gestionnaire 
			List<DepartmentManager> departments = getDomainService().getDepartmentManagers(getCurrentUser());
			if (departments != null) {
				for (DepartmentManager depaManager : departments) {
					for (DepartmentManager manager : getDomainService().getDepartmentManagers(depaManager.getDepartment())) {
						listeDepartmentManagerTriee.add(manager);
							
					}
				}
				for (DepartmentManager manager : listeDepartmentManagerTriee) {
					managerInvolvementItems.add(
							new SelectItem(manager.getUser(), manager.getUser().getDisplayName()));
				}
			}
		}
		return managerInvolvementItems;
	}

	/**
	 * @return the userInvolvementItems
	 */
	@RequestCache
	public List<SelectItem> getUserInvolvementItems() {
		List<SelectItem> userInvolvementItems = new ArrayList<SelectItem>();
		userInvolvementItems.add(new SelectItem(
				ControlPanel.USER_INVOLVEMENT_FILTER_ANY,
				getString("CONTROL_PANEL.INVOLVEMENT_FILTER.ANY")));
		userInvolvementItems.add(new SelectItem(
				ControlPanel.USER_INVOLVEMENT_FILTER_OWNER,
				getString("CONTROL_PANEL.INVOLVEMENT_FILTER.USER.OWNER")));
		userInvolvementItems.add(new SelectItem(
				ControlPanel.USER_INVOLVEMENT_FILTER_OWNER_OR_INVITED,
				getString("CONTROL_PANEL.INVOLVEMENT_FILTER.USER.OWNER_OR_INVITED")));
		userInvolvementItems.add(new SelectItem(
				ControlPanel.USER_INVOLVEMENT_FILTER_INVITED,
				getString("CONTROL_PANEL.INVOLVEMENT_FILTER.USER.INVITED")));
		userInvolvementItems.add(new SelectItem(
				ControlPanel.USER_INVOLVEMENT_FILTER_MONITORING,
				getString("CONTROL_PANEL.INVOLVEMENT_FILTER.USER.MONITORING")));
		return userInvolvementItems;
	}

	/**
	 * @return the interfaceItems
	 */
	@RequestCache
	public List<SelectItem> getInterfaceItems() {
		List<SelectItem> interfaceItems = new ArrayList<SelectItem>();
		interfaceItems.add(new SelectItem(
				Boolean.TRUE,
				getString("CONTROL_PANEL.INTERFACE.USER")));
		interfaceItems.add(new SelectItem(
				Boolean.FALSE,
				getString("CONTROL_PANEL.INTERFACE.MANAGER")));
		return interfaceItems;
	}

	/**
	 * @return the statusItems
	 */
	@RequestCache
	public List<SelectItem> getStatusItems() {
		List<SelectItem> statusItems = new ArrayList<SelectItem>();
		statusItems.add(new SelectItem(
				ControlPanel.STATUS_FILTER_ANY,
				getString("CONTROL_PANEL.STATUS_FILTER.ANY")));
		statusItems.add(new SelectItem(
				ControlPanel.STATUS_FILTER_OPENED,
				getString("CONTROL_PANEL.STATUS_FILTER.OPENED")));
		statusItems.add(new SelectItem(
				ControlPanel.STATUS_FILTER_CLOSED,
				getString("CONTROL_PANEL.STATUS_FILTER.CLOSED")));
		return statusItems;
	}

	/**
	 * @return the managerDepartmentItems
	 */
	@RequestCache
	public List<SelectItem> getManagerDepartmentItems() {
		List<SelectItem> managerDepartmentItems = new ArrayList<SelectItem>();
		managerDepartmentItems.add(
				new SelectItem("", getString("CONTROL_PANEL.DEPARTMENT_FILTER.ANY")));
		for (Department dep : getDomainService().getManagedDepartments(getCurrentUser())) {
			managerDepartmentItems.add(new SelectItem(dep, dep.getLabel()));
		}
		return managerDepartmentItems;
	}

	/**
	 * Add category items recursively.
	 * @param items
	 * @param category
	 * @param prefix
	 */
	protected void  addCategoryItems(
			final List<SelectItem> items,
			final Category category,
			final String prefix) {
		items.add(new SelectItem(
				String.valueOf(category.getId()),
				prefix + getString("CONTROL_PANEL.CATEGORY_FILTER.CATEGORY", category.getLabel())));
		for (Category subCategory : getDomainService().getSubCategories(category)) {
			addCategoryItems(items, subCategory, prefix + "- ");
		}
	}

	/**
	 * @return the managerDepartmentItems
	 */
	@RequestCache
	public List<SelectItem> getManagerCategorySpecItems() {
		if (getCurrentUser().getControlPanelManagerDepartmentFilter() == null) {
			return null;
		}
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem("*", getString("CONTROL_PANEL.CATEGORY_FILTER.ANY")));
		items.add(new SelectItem("member", getString("CONTROL_PANEL.CATEGORY_FILTER.MEMBER")));
		for (Category category : getDomainService().getRootCategories(
				getCurrentUser().getControlPanelManagerDepartmentFilter())) {
			addCategoryItems(items, category, "");
		}
		return items;
	}

	/**
	 * @return the managerCategoryFilterSpec
	 */
	@RequestCache
	public String getManagerCategoryFilterSpec() {
		User user = getCurrentUser();
		String spec = null;
		if (user.getControlPanelCategoryMemberFilter()) {
			spec = "member";
		} else if (user.getControlPanelCategoryFilter() == null) {
			spec = "*";
		} else {
			spec = String.valueOf(user.getControlPanelCategoryFilter().getId());
		}
		return spec;
	}

	/**
	 * Set the managerCategoryFilterSpec.
	 * @param managerCategorySpec
	 */
	public void setManagerCategoryFilterSpec(final String managerCategorySpec) {
		User user = getCurrentUser();
		if ("member".equals(managerCategorySpec)) {
			user.setControlPanelCategoryMemberFilter(true);
			user.setControlPanelCategoryFilter(null);
		} else {
			user.setControlPanelCategoryMemberFilter(false);
			if ("*".equals(managerCategorySpec)) {
				user.setControlPanelCategoryFilter(null);
			} else {
				user.setControlPanelCategoryFilter(
						getDomainService().getCategory(Long.valueOf(managerCategorySpec)));
			}
		}
	}

	/**
	 * @return the userDepartmentItems
	 */
	@RequestCache
	public List<SelectItem> getUserDepartmentItems() {
		List<SelectItem> userDepartmentItems = new ArrayList<SelectItem>();
		userDepartmentItems.add(
				new SelectItem("", getString("CONTROL_PANEL.DEPARTMENT_FILTER.ANY")));
		for (Department dep : getDomainService().getTicketViewDepartments(
				getCurrentUser(), getClient())) {
			userDepartmentItems.add(new SelectItem(dep, dep.getLabel()));
		}
		return userDepartmentItems;
	}

	/**
	 * @return the managerCategoryFilterSpec
	 */
	@RequestCache
	public String getUserCategoryFilterSpec() {
		User user = getCurrentUser();
		String spec = null;
		if (user.getControlPanelCategoryMemberFilter()) {
			spec = "member";
		} else if (user.getControlPanelCategoryFilter() == null) {
			spec = "*";
		} else {
			spec = String.valueOf(user.getControlPanelCategoryFilter().getId());
		}
		return spec;
	}

	/**
	 * Set the managerCategoryFilterSpec.
	 * @param managerCategorySpec
	 */
	public void setUserCategoryFilterSpec(final String userCategorySpec) {
		User user = getCurrentUser();
		if ("member".equals(userCategorySpec)) {
			user.setControlPanelCategoryMemberFilter(true);
			user.setControlPanelCategoryFilter(null);
		} else {
			user.setControlPanelCategoryMemberFilter(false);
			if ("*".equals(userCategorySpec)) {
				user.setControlPanelCategoryFilter(null);
			} else {
				user.setControlPanelCategoryFilter(
						getDomainService().getCategory(Long.valueOf(userCategorySpec)));
			}
		}
	}

	/**
	 * @return the managerDepartmentItems
	 */
	@RequestCache
	public List<SelectItem> getUserCategorySpecItems() {
		if (getCurrentUser().getControlPanelUserDepartmentFilter() == null) {
			return null;
		}
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem("*", getString("CONTROL_PANEL.CATEGORY_FILTER.ANY")));
		items.add(new SelectItem("member", getString("CONTROL_PANEL.CATEGORY_FILTER.MEMBER")));
		for (Category category : getDomainService().getRootCategories(
				getCurrentUser().getControlPanelUserDepartmentFilter())) {
			addCategoryItems(items, category, "");
		}
		return items;
	}

	/**
	 * JSF callback.
	 */
	public void refreshColumnOrderer() {
		columnsOrderer = new ControlPanelColumnOrderer(getCurrentUser());
		addColumnItems = new ArrayList<SelectItem>();
		List<String> missingColumnNames = columnsOrderer.getMissingColumnNames();
		if (!missingColumnNames.isEmpty()) {
			addColumnItems.add(new SelectItem("", getString("CONTROL_PANEL.COLUMNS.CHOOSE")));
			for (String missingColumnName : missingColumnNames) {
				addColumnItems.add(new SelectItem(
						missingColumnName,
						getString("CONTROL_PANEL.COLUMNS." + missingColumnName)));
			}
		}
	}

	/**
	 * @return the userDepartmentItems
	 */
	public List<SelectItem> getAddColumnItems() {
		return addColumnItems;
	}

	/**
	 * @return the number of columns
	 */
	public int getColumnsNumber() {
		return columnsOrderer.getColumnsNumber();
	}

	/**
	 * Move a column right.
	 * @param index the index of the column to move
	 */
	public void setColumnToMoveRight(final Long index) {
		columnsOrderer.moveColumnRight(index.intValue());
	}

	/**
	 * Move a column left.
	 * @param index the index of the column to move
	 */
	public void setColumnToMoveLeft(final Long index) {
		columnsOrderer.moveColumnLeft(index.intValue());
	}

	/**
	 * Remove a column.
	 * @param index the index of the column to remove
	 */
	public void setColumnToRemove(final Long index) {
		columnsOrderer.removeColumn(index.intValue());
	}

	/**
	 * JSF callback.
	 */
	public void addColumn() {
		if (columnToAdd != null) {
			columnsOrderer.addColumn(columnToAdd);
		}
		columnToAdd = null;
		updateColumns();
	}

	/**
	 * JSF callback.
	 */
	public void resetColumns() {
		getCurrentUser().setControlPanelColumns(null);
		getDomainService().updateUser(getCurrentUser());
		refreshColumnOrderer();
	}

	/**
	 * JSF callback.
	 */
	public void updateColumns() {
		getCurrentUser().setControlPanelColumns(columnsOrderer.asString());
		getDomainService().updateUser(getCurrentUser());
		refreshColumnOrderer();
	}

	/**
	 * @return the first part of the sort order.
	 */
	public String getFirstOrderPartSpec() {
		return getCurrentUser().getControlPanelOrder().getFirstOrderPart().toString();
	}

	/**
	 * JSF callback.
	 * @param partSpec
	 */
	public void setFirstOrderPartSpec(final String partSpec) {
		getCurrentUser().getControlPanelOrder().setFirstOrderPart(partSpec);
		getDomainService().updateUser(getCurrentUser());
		paginator.forceReload();
	}

	/**
	 * @return a permanent link to the page for application users.
	 */
	public String getApplicationPermLink() {
		return getUrlBuilder().getControlPanelUrl(AuthUtils.APPLICATION);
	}

	/**
	 * @return a permanent link to the page for CAS users.
	 */
	public String getCasPermLink() {
		return getUrlBuilder().getControlPanelUrl(AuthUtils.CAS);
	}

	/**
	 * @return a permanent link to the page for Shibboleth users.
	 */
	public String getShibbolethPermLink() {
		return getUrlBuilder().getControlPanelUrl(AuthUtils.SHIBBOLETH);
	}

	/**
	 * @return a permanent link to the page for specific users.
	 */
	public String getSpecificPermLink() {
		return getUrlBuilder().getControlPanelUrl(AuthUtils.SPECIFIC);
	}

	/**
	 * @param paginator the paginator to set
	 */
	public void setPaginator(final Paginator<ControlPanelEntry> paginator) {
		this.paginator = paginator;
	}

	/**
	 * @param ticketToView the ticketToView to set
	 */
	public void setTicketToView(final Ticket ticketToView) {
		this.ticketToView = ticketToView;
	}

	/**
	 * @param ticketController the ticketController to set
	 */
	public void setTicketController(final TicketController ticketController) {
		this.ticketController = ticketController;
	}

	/**
	 * @return the column orderer
	 */
	public ControlPanelColumnOrderer getColumnsOrderer() {
		return columnsOrderer;
	}

	/**
	 * @return the editColumns
	 */
	public boolean isEditColumns() {
		return editColumns;
	}

	/**
	 * Toggle the edit column mode.
	 */
	public void toggleEditColumns() {
		editColumns = !editColumns;
	}

	/**
	 * @return the columnToAdd
	 */
	public String getColumnToAdd() {
		return columnToAdd;
	}

	/**
	 * @param columnToAdd the columnToAdd to set
	 */
	public void setColumnToAdd(final String columnToAdd) {
		this.columnToAdd = StringUtils.nullIfEmpty(columnToAdd);
	}

	/**
	 * @return the ticketNumberString
	 */
	public String getTicketNumberString() {
		return ticketNumberString;
	}

	/**
	 * @param ticketNumberString the ticketNumberString to set
	 */
	public void setTicketNumberString(final String ticketNumberString) {
		this.ticketNumberString = ticketNumberString;
	}

	/**
	 * @return the archivedTicketController
	 */
	public ArchivedTicketController getArchivedTicketController() {
		return archivedTicketController;
	}

	/**
	 * @param archivedTicketController the archivedTicketController to set
	 */
	public void setArchivedTicketController(
			final ArchivedTicketController archivedTicketController) {
		this.archivedTicketController = archivedTicketController;
	}

	/**
	 * @param ticketToMarkReadUnread the ticketToMarkReadUnread to set
	 */
	public void setTicketToMarkReadUnread(final Ticket ticketToMarkReadUnread) {
		this.ticketToMarkReadUnread = ticketToMarkReadUnread;
	}

	/**
	 * @return the ticketToBookmark
	 */
	protected Ticket getTicketToBookmark() {
		return ticketToBookmark;
	}

	/**
	 * @param ticketToBookmark the ticketToBookmark to set
	 */
	public void setTicketToBookmark(final Ticket ticketToBookmark) {
		this.ticketToBookmark = ticketToBookmark;
	}
    
	public ControlPanelPaginator getPanelPaginator() {
		return panelPaginator;
	}

	public void setPanelPaginator(ControlPanelPaginator panelPaginator) {
		this.panelPaginator = panelPaginator;
	}
}
