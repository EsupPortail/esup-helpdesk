/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.remote;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.remote.AbstractIpProtectedWebService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.dao.DaoService;
import org.esupportail.helpdesk.domain.ActionScope;
import org.esupportail.helpdesk.domain.ControlPanel;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.TicketExtractor;
import org.esupportail.helpdesk.domain.TicketScope;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.services.urlGeneration.UrlBuilder;
import org.hibernate.Query;
import org.springframework.util.StringUtils;

/**
 * The basic implementation of web services.
 */
public class HelpdeskImpl extends AbstractIpProtectedWebService implements Helpdesk {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7913386648525316504L;

	/**
	 * The domain service.
	 */
	private DomainService domainService;

	/**
	 * The application service.
	 */
	private ApplicationService applicationService;

	/**
	 * The bean used to generate URLs.
	 */
	private UrlBuilder urlBuilder;
	
	/**
	 * The DAO service.
	 */
	private DaoService daoService;
	
	/**
	 * The ticket extractor.
	 */
	private TicketExtractor ticketExtractor;
	
	/**
	 * Bean constructor.
	 */
	public HelpdeskImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(domainService,
				"property domainService of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(applicationService,
				"property applicationService of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(urlBuilder,
				"property urlBuilder of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(daoService,
				"property daoService of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(ticketExtractor,
				"property ticketExtractor of class " + this.getClass().getName()
				+ " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.Helpdesk#addTicket(
	 * java.lang.String, long, long, java.lang.String, java.lang.String,
	 * java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public long addTicket(
			final String authorId,
			final long creationDepartmentId,
			final long categoryId,
			final String origin,
			final String label,
			final String computer,
			final int priorityLevel,
			final String message,
			final String ticketScope) {
		checkClient();
		if (!StringUtils.hasText(authorId)) {
			throw new IllegalArgumentException("ownerId is null");
		}
		User author = getDomainService().getUserStore().getUserFromRealId(authorId);
		Category category = domainService.getCategory(categoryId);
		Department creationDepartment;
		if (creationDepartmentId <= 0) {
			creationDepartment = category.getDepartment();
		} else {
			creationDepartment = domainService.getDepartment(creationDepartmentId);
		}
		if (!StringUtils.hasText(label)) {
			throw new IllegalArgumentException("label is null");
		}
		String theComputer = org.esupportail.commons.utils.strings.StringUtils.nullIfEmpty(computer);
		if (domainService.getPriorities().get(new Integer(priorityLevel)) == null) {
			throw new IllegalArgumentException("priority level [" + priorityLevel + "] not found");
		}
		String theMessage = org.esupportail.commons.utils.strings.StringUtils.nullIfEmpty(message);
		Assert.contains(new String [] {
				TicketScope.DEFAULT,
				TicketScope.PUBLIC,
				TicketScope.SUBJECT_ONLY,
				TicketScope.PRIVATE,
				TicketScope.CAS,
		}, "ticketScope", ticketScope);
		Assert.contains(domainService.getOrigins(), "origin", origin);
		Ticket ticket = domainService.addWebTicket(
				author, null, creationDepartment, category, label,
				theComputer, priorityLevel, theMessage, ticketScope, origin);
		domainService.updateTicket(ticket);
		getDomainService().ticketMonitoringSendAlerts(author, ticket, null, false);
		return ticket.getId();
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.Helpdesk#cancelTicket(
	 * java.lang.String, long, java.lang.String)
	 */
	@Override
	public void cancelTicket(
			final String authorId,
			final long ticketId,
			final String message) {
		checkClient();
		Ticket ticket = domainService.getTicket(ticketId);
		User author = getDomainService().getUserStore().getUserFromRealId(authorId);
		if (!domainService.userCanCancel(author, ticket)) {
			throw new IllegalArgumentException(
					"user [" + authorId + "] is not allowed to cancel ticket #" + ticketId);
		}
		domainService.cancelTicket(author, ticket, message, ActionScope.DEFAULT);
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.Helpdesk#closeTicket(
	 * java.lang.String, long, java.lang.String)
	 */
	@Override
	public void closeTicket(
			final String authorId,
			final long ticketId,
			final String message) {
		checkClient();
		Ticket ticket = domainService.getTicket(ticketId);
		User author = getDomainService().getUserStore().getUserFromRealId(authorId);
		if (!domainService.userCanCancel(author, ticket)) {
			throw new IllegalArgumentException(
					"user [" + authorId + "] is not allowed to close ticket #" + ticketId);
		}
		domainService.closeTicket(author, ticket, message, ActionScope.DEFAULT, false);
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.Helpdesk#getVersion()
	 */
	@Override
	public String getVersion() {
		return applicationService.getVersion().toString();
	}


	/**
	 * @see org.esupportail.helpdesk.services.remote.Helpdesk#getControlPanelUserInvolvementFilter(java.lang.String)
	 */
	@Override
	public String getControlPanelUserInvolvementFilter(final String userId) {
		checkClient();
		User user = getDomainService().getUserStore().getUserFromRealId(userId);
		return user.getControlPanelUserInvolvementFilter();
	}
	
	/**
	 * @see org.esupportail.helpdesk.services.remote.Helpdesk#getControlPanelManagerInvolvementFilter(java.lang.String)
	 */
	@Override
	public String getControlPanelManagerInvolvementFilter(final String userId) {
		checkClient();
		User user = getDomainService().getUserStore().getUserFromRealId(userId);
		return user.getControlPanelManagerInvolvementFilter();
	}
	
	/**
	 * @see org.esupportail.helpdesk.services.remote.Helpdesk#getUserInvolvementFilters()
	 */
	@Override
	public List<String> getUserInvolvementFilters() {
		checkClient();
		String[] filters = {
			ControlPanel.USER_INVOLVEMENT_FILTER_ANY,
			ControlPanel.USER_INVOLVEMENT_FILTER_OWNER,
			ControlPanel.USER_INVOLVEMENT_FILTER_OWNER_OR_INVITED,
			ControlPanel.USER_INVOLVEMENT_FILTER_INVITED,
			ControlPanel.USER_INVOLVEMENT_FILTER_MONITORING
		};
		return Arrays.asList(filters);
	}

	/**
	 * @see org.esupportail.helpdesk.services.remote.Helpdesk#getManagerInvolvementFilters()
	 */
	@Override
	public List<String> getManagerInvolvementFilters() {
		checkClient();
		String[] filters = {
			ControlPanel.MANAGER_INVOLVEMENT_FILTER_ANY,
			ControlPanel.MANAGER_INVOLVEMENT_FILTER_FREE,
			ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED,
			ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_FREE,
			ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_INVITED,
			ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE
		};
		return Arrays.asList(filters);
	}
	
	
	
	/**
	 * @param userId
	 * @return 'true' if the user is a manager.
	 */
	@Override
	public boolean isDepartmentManager(final String userId){
		checkClient();
		User user = getDomainService().getUserStore().getUserFromRealId(userId);	
		return getDomainService().isDepartmentManager(user);
	}
	

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @return the domainService
	 */
	protected DomainService getDomainService() {
		return domainService;
	}

	/**
	 * @return the applicationService
	 */
	protected ApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(final ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @return the URL builder.
	 */
	public UrlBuilder getUrlBuilder() {
		return urlBuilder;
	}

	/**
	 * @param urlBuilder
	 */
	public void setUrlBuilder(UrlBuilder urlBuilder) {
		this.urlBuilder = urlBuilder;
	}
	
	/**
	 * @return the DAO service
	 */
	public DaoService getDaoService() {
		return daoService;
	}

	/**
	 * @param daoService
	 */
	public void setDaoService(DaoService daoService) {
		this.daoService = daoService;
	}
	
	/**
	 * @return the ticket extractor.
	 */
	public TicketExtractor getTicketExtractor() {
		return ticketExtractor;
	}

	/**
	 * @param ticketExtractor
	 */
	public void setTicketExtractor(TicketExtractor ticketExtractor) {
		this.ticketExtractor = ticketExtractor;
	}
}
