/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.dao.AbstractHibernatePaginator;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.ControlPanel;
import org.esupportail.helpdesk.domain.ControlPanelOrder;
import org.esupportail.helpdesk.domain.ControlPanelOrderPart;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.TicketExtractor;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Invitation;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.web.controllers.SessionController;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;

/** 
 * A paginator for the control panel.
 */ 
public class ControlPanelPaginator 
extends AbstractHibernatePaginator<ControlPanelEntry> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1442322276601816512L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The domain service.
	 */
	private DomainService domainService;
	
	/**
	 * The ticket extractor.
	 */
	private TicketExtractor ticketExtractor;
	
	/**
	 * The session controller.
	 */
	private SessionController sessionController;
	
	/**
	 * The selected manager.
	 */
	private User selectedManager;

	/**
	 * Constructor.
	 */
	public ControlPanelPaginator() {
		super();
	}
	
	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.domainService, 
				"property domainService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.ticketExtractor, 
				"property ticketExtractor of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.sessionController, 
				"property sessionController of class " + this.getClass().getName() 
				+ " can not be null");
	}
	
	/**
	 * @see org.esupportail.commons.web.beans.AbstractPaginator#loadItemsInternal()
	 */
	@SuppressWarnings("unchecked")
	protected void loadItemsInternal() {

		List<ControlPanelEntry> controlPanelEntries = new ArrayList<ControlPanelEntry>();
		List<Department> visibleDepartments = 
			getDomainService().getTicketViewDepartments(getCurrentUser(), getClient());
		String queryStringInvitation = null;
		String queryStringLibre = null;

		//on récupère le user manager stocké au niveau du user courant
		if((selectedManager == null) && getCurrentUser().getControlPanelManagerManagerFilter() != null){
			selectedManager = domainService.getUserStore().getExistingUserFromId(getCurrentUser().getControlPanelManagerManagerFilter());
		}
		//cas ou l'on passe a 'Implication = ...libres...', il faut gérer le cas de 'Gestionnaire = Tous' qui n'est plus proposé dans la liste
		//dans ce cas on force le currentUser
		if((selectedManager == null) && getCurrentUser().getControlPanelManagerInvolvementFilter() != null &&
				(getCurrentUser().getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE) 
				|| 
				getCurrentUser().getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_INVITED))
			){
				selectedManager = getCurrentUser();
		}
		//récupération des tickets invité
		if(getCurrentUser().getControlPanelManagerInvolvementFilter() != null 
		&& (getCurrentUser().getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE) 
			|| 
			getCurrentUser().getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_INVITED)) 
		){
			queryStringInvitation = ticketExtractor.getControlPanelQueryString(
					getCurrentUser(), selectedManager!=null ? selectedManager : getCurrentUser(), visibleDepartments, "INVITE", null, null);
		}
		//récupération des tickets libres du filtre 'Gérés invité ou libres'
		if(getCurrentUser().getControlPanelManagerInvolvementFilter() != null 
		&& getCurrentUser().getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE) ){
			queryStringLibre = ticketExtractor.getControlPanelQueryString(
					getCurrentUser(), selectedManager!=null ? selectedManager : getCurrentUser(), visibleDepartments, "FREE", null, null);
		}
		String queryString = ticketExtractor.getControlPanelQueryString(
				getCurrentUser(), selectedManager, visibleDepartments, "OTHER", queryStringInvitation, queryStringLibre);
		if (queryString == null) {
			setVisibleItems(controlPanelEntries);
			setCurrentPageInternal(0);
			setTotalItemsCount(0);
			return;
		}
		Query query = getDaoService().getQuery(queryString);
		ScrollableResults scrollableResults = query.scroll(); 
		/* 
		 * We set the max results to one more than the specified pageSize to 
		 * determine if any more results exist (i.e. if there is a next page 
		 * to display). The result set is trimmed down to just the pageSize 
		 * before being displayed later (in getList()). 
		 */ 
		if (logger.isDebugEnabled()) {
			logger.debug("executing " + query.getQueryString() + "...");
		}
		

		query.setFirstResult(getCurrentPage() * getPageSize());
		query.setMaxResults(getPageSize());
		List<Ticket> tickets = query.list();
		
		for (Ticket ticket : tickets) {
			controlPanelEntries.add(new ControlPanelEntry(
					ticket, 
					!domainService.hasTicketChangedSinceLastView(ticket, getCurrentUser()), 
					domainService.userCanViewTicket(
							getCurrentUser(), ticket, visibleDepartments),
					domainService.getBookmark(getCurrentUser(), ticket) != null));
		}
		setVisibleItems(controlPanelEntries);
		// the total number of results is computed here since scrolling is not allowed when rendering
		scrollableResults.last(); 
		setTotalItemsCount(scrollableResults.getRowNumber() + 1);
		if (logger.isDebugEnabled()) {
			logger.debug("done.");
		}
		if (getVisibleItemsCountInternal() == 0 && getTotalItemsCountInternal() != 0) {
			setCurrentPageInternal((getTotalItemsCountInternal() - 1) / getPageSize());
			loadItemsInternal();
		}
		updateLoadTime();
	}

	/**
	 * @see org.esupportail.commons.web.beans.AbstractPaginator#getPageSizeInternal()
	 */
	@Override
	public int getPageSizeInternal() {
		return sessionController.getCurrentUser().getControlPanelPageSize();
	}

	/**
	 * @see org.esupportail.commons.web.beans.AbstractPaginator#setPageSizeInternal(int)
	 */
	@Override
	protected void setPageSizeInternal(final int pageSize) {
		sessionController.getCurrentUser().setControlPanelPageSize(pageSize);
		domainService.updateUser(sessionController.getCurrentUser());
	}

	/**
	 * @return the current user.
	 */
	public User getCurrentUser() {
		return sessionController.getCurrentUser();
	}

	/**
	 * @return the current user.
	 */
	public InetAddress getClient() {
		return sessionController.getClient();
	}

	/**
	 * @param sessionController the sessionController to set
	 */
	public void setSessionController(final SessionController sessionController) {
		this.sessionController = sessionController;
	}

	/**
	 * @param ticketExtractor the ticketExtractor to set
	 */
	public void setTicketExtractor(final TicketExtractor ticketExtractor) {
		this.ticketExtractor = ticketExtractor;
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @return the ticketExtractor
	 */
	protected TicketExtractor getTicketExtractor() {
		return ticketExtractor;
	}

	/**
	 * @return the selectedManager
	 */
	public User getSelectedManager() {
		if (selectedManager != null) {
			Department department = getCurrentUser().getControlPanelManagerDepartmentFilter();
			if(department != null){
				if (!domainService.isDepartmentManager(department, selectedManager)) {
					selectedManager = null;
				}
			}
		} 
		else {
			//on récupère le user manager stocké au niveau du user courant
			if(getCurrentUser().getControlPanelManagerManagerFilter() != null){
				selectedManager = domainService.getUserStore().getExistingUserFromId(getCurrentUser().getControlPanelManagerManagerFilter());
			}
		}
		return selectedManager;
	}

	/**
	 * @param selectedManager the selectedManager to set
	 */
	public void setSelectedManager(final User selectedManager) {
		this.selectedManager = selectedManager;
		//maj du gestionnaire pour le user courant
		if(selectedManager != null)	{
			getCurrentUser().setControlPanelManagerManagerFilter(selectedManager.getId());
		} else {
			getCurrentUser().setControlPanelManagerManagerFilter(null);
		}
	}

	/**
	 * @return the domainService
	 */
	protected DomainService getDomainService() {
		return domainService;
	}

}

