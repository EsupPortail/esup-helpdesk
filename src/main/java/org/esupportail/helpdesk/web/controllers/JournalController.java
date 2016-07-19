/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.web.beans.Paginator;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The journal controller.
 */
public class JournalController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2199813366288386290L;

	/**
     * The paginator.
     */
    private Paginator<Action> paginator;

    /**
     * The ticket to view.
     */
    private Ticket ticketToView;

    /**
     * The ticket Controller.
     */
    private TicketController ticketController;

	/**
	 * Bean constructor.
	 */
	public JournalController() {
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
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		ticketToView = null;
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
	@RequestCache
	public boolean isPageAuthorized() {
		if (getCurrentUser() == null) {
			return false;
		}
		if (!getDomainService().isDepartmentManager(getCurrentUser())) {
			return false;
		}
		return true;
	}

	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			return null;
		}
		getSessionController().setShowShortMenu(false);
		User currentUser = getCurrentUser();
		if (currentUser.getJournalPageSize() == null || currentUser.getJournalPageSize() <= 0) {
			currentUser.setJournalPageSize(paginator.getDefaultPageSize());
		}
		getDomainService().updateUser(getCurrentUser());
		paginator.setPageSize(getCurrentUser().getJournalPageSize());
		paginator.forceReload();
		return "navigationJournal";
	}

	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String viewTicket() {
		ticketToView = getDomainService().reloadTicket(ticketToView);
		if (!getDomainService().userCanViewTicket(getCurrentUser(), getClient(), ticketToView)) {
			addUnauthorizedActionMessage();
			return null;
		}
		ticketController.setTicket(ticketToView);
		return "view";
	}

	/**
	 * @return the paginator.
	 */
	public Paginator<Action> getPaginator() {
		return paginator;
	}

	/**
	 * @return the departmentItems
	 */
	public List<SelectItem> getDepartmentItems() {
		List<SelectItem> departmentItems = new ArrayList<SelectItem>();
		departmentItems.add(
				new SelectItem("", getString("JOURNAL.DEPARTMENT_FILTER.ANY")));
		for (Department dep : getDomainService().getManagedDepartments(getCurrentUser())) {
			departmentItems.add(new SelectItem(dep, dep.getLabel()));
		}
		return departmentItems;
	}

	/**
	 * @return a permanent link to the page for application users.
	 */
	public String getApplicationPermLink() {
		return getUrlBuilder().getJournalUrl(AuthUtils.APPLICATION);
	}

	/**
	 * @return a permanent link to the page for CAS users.
	 */
	public String getCasPermLink() {
		return getUrlBuilder().getJournalUrl(AuthUtils.CAS);
	}

	/**
	 * @return a permanent link to the page for Shibboleth users.
	 */
	public String getShibbolethPermLink() {
		return getUrlBuilder().getJournalUrl(AuthUtils.SHIBBOLETH);
	}

	/**
	 * @return a permanent link to the page for specific users.
	 */
	public String getSpecificPermLink() {
		return getUrlBuilder().getJournalUrl(AuthUtils.SPECIFIC);
	}

	/**
	 * @param paginator the paginator to set
	 */
	public void setPaginator(final Paginator<Action> paginator) {
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

}
