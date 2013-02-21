/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.aop.monitor.Monitor;
import org.esupportail.commons.dao.HqlUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.dao.DaoService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Invitation;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.TicketView;
import org.esupportail.helpdesk.domain.beans.User;
import org.hibernate.Query;
import org.springframework.beans.factory.InitializingBean;

/**
 * A bean to navigate through tickets.
 */
@Monitor
public class TicketNavigatorImpl implements TicketNavigator, InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7567021164410661278L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The domain service.
	 */
	private DomainService domainService;

	/**
	 * The DAO service.
	 */
	private DaoService daoService;

	/**
	 * Bean constructor.
	 */
	public TicketNavigatorImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.domainService,
				"property domainService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.daoService,
				"property daoService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @param user
	 * @return the condition for the user to own the tickets.
	 */
	protected String getOwnerCondition(final User user) {
		String condition = HqlUtils.equals("ticket.owner.id", HqlUtils.quote(user.getId()));
		if (logger.isDebugEnabled()) {
			logger.debug("ownerCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition regarding invitations.
	 */
	protected String getInvitedCondition(
			final User user) {
		String condition = HqlUtils.exists(
				HqlUtils.selectFromWhere(
						"invitation",
						Invitation.class.getSimpleName()
						+ HqlUtils.AS_KEYWORD
						+ "invitation",
						HqlUtils.and(
								HqlUtils.equals(
										"invitation.ticket.id",
								"ticket.id"),
								HqlUtils.equals(
										"invitation.user.id",
										HqlUtils.quote(user.getId()))
						)
				)
		);
		if (logger.isDebugEnabled()) {
			logger.debug("invitedCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition regarding the managed departments.
	 */
	protected String getManagedDepartmentCondition(
			final User user) {
		List<Long> departmentsIds = new ArrayList<Long>();
		for (Department department : getDomainService().getManagedDepartments(user)) {
			departmentsIds.add(new Long(department.getId()));
		}
		String condition = HqlUtils.longIn("ticket.department.id", departmentsIds);
		if (logger.isDebugEnabled()) {
			logger.debug("managedDepartmentCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param visibleDepartments
	 * @return the condition regarding the department for non managers.
	 */
	protected String getVisibleDepartmentCondition(
			final List<Department> visibleDepartments) {
		String condition;
		List<Long> departmentsIds = new ArrayList<Long>();
		for (Department department : visibleDepartments) {
			departmentsIds.add(new Long(department.getId()));
		}
		condition = HqlUtils.longIn("ticket.department.id", departmentsIds);
		if (logger.isDebugEnabled()) {
			logger.debug("userVisibleDepartmentCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @return true if the user can read the ticket.
	 */
	protected String getPublicTicketCondition() {
		String condition = HqlUtils.equals(
						"ticket.effectiveScope",
						HqlUtils.quote(TicketScope.PUBLIC));
		if (logger.isDebugEnabled()) {
			logger.debug("publicTicketCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param ticket
	 * @param next true for greater tickets,
	 * @return the condition regarding the id of the ticket.
	 */
	protected String getTicketIdCondition(
			final Ticket ticket,
			final boolean next) {
		String condition;
		if (next) {
			condition = HqlUtils.gt("ticket.id", ticket.getId());
		} else {
			condition = HqlUtils.lt("ticket.id", ticket.getId());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("ticketIdCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param next
	 * @return the order by clause
	 */
	protected String getOrderBy(
			final boolean next) {
		String orderBy;
		if (next) {
			orderBy = HqlUtils.asc("ticket.id");
		} else {
			orderBy = HqlUtils.desc("ticket.id");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("orderBy = " + orderBy);
		}
		return orderBy;
	}

	/**
	 * @param user
	 * @param unread true to omit already read tickets
	 * @return the condition regarding the date of the ticket.
	 */
	protected String getDateCondition(
			final User user,
			final boolean unread) {
		String condition;
		if (!unread) {
			condition = HqlUtils.alwaysTrue();
		} else {
			String unreadSubquery = HqlUtils.selectFromWhere(
					"ticketView",
					TicketView.class.getSimpleName()
					+ HqlUtils.AS_KEYWORD
					+ "ticketView",
					HqlUtils.and(
							HqlUtils.equals(
									"ticketView.ticket",
									"ticket"),
							HqlUtils.equals(
									"ticketView.user.id",
									HqlUtils.quote(user.getId())),
							HqlUtils.gt(
									"ticketView.date",
									"ticket.lastActionDate")
							)
					);
			condition = HqlUtils.not(
					HqlUtils.exists(unreadSubquery)
					);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("dateCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @param ticket
	 * @param visibleDepartments
	 * @param next
	 * @param unread
	 * @return the query string for managers.
	 */
	protected String getQueryString(
			final User user,
			final Ticket ticket,
			final List<Department> visibleDepartments,
			final boolean next,
			final boolean unread) {
		if (user == null) {
			return null;
		}
		String visibleCondition = HqlUtils.or(
				getOwnerCondition(user),
				getInvitedCondition(user),
				getManagedDepartmentCondition(user),
				HqlUtils.and(
						getPublicTicketCondition(),
						getVisibleDepartmentCondition(visibleDepartments)));
		if (logger.isDebugEnabled()) {
			logger.debug("visibleCondition = " + visibleCondition);
		}
		return HqlUtils.fromWhereOrderBy(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				HqlUtils.and(
						visibleCondition,
						getTicketIdCondition(ticket, next),
						getDateCondition(user, unread)),
				getOrderBy(next));
	}

	/**
	 * @param user
	 * @param ticket
	 * @param visibleDepartments
	 * @param next
	 * @param unread
	 * @return a ticket or null.
	 */
	@SuppressWarnings("unchecked")
	protected Ticket getTicket(
			final User user,
			final Ticket ticket,
			final List<Department> visibleDepartments,
			final boolean next,
			final boolean unread) {
		String queryString = getQueryString(user, ticket, visibleDepartments, next, unread);
		Query query = getDaoService().getQuery(queryString);
		query.setMaxResults(1);
		List<Ticket> tickets = query.list();
		if (tickets.isEmpty()) {
			return null;
		}
		return tickets.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.TicketNavigator#getNavigation(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Ticket, java.net.InetAddress)
	 */
	@Override
	@RequestCache
	public TicketNavigation getNavigation(
			final User user,
			final Ticket ticket,
			final InetAddress client) {
		List<Department> visibleDepartments =
			getDomainService().getManagedOrTicketViewVisibleDepartments(user, client);
		Ticket previousUnread = getTicket(user, ticket, visibleDepartments, false, true);
		Ticket previousVisible = getTicket(user, ticket, visibleDepartments, false, false);
		if (previousUnread != null && previousUnread.equals(previousVisible)) {
			previousVisible = null;
		}
		Ticket nextVisible = getTicket(user, ticket, visibleDepartments, true, false);
		Ticket nextUnread = getTicket(user, ticket, visibleDepartments, true, true);
		if (nextUnread != null && nextUnread.equals(nextVisible)) {
			nextVisible = null;
		}
		return new TicketNavigation(previousUnread, previousVisible, nextVisible, nextUnread);
	}

	/**
	 * @return the domainService
	 */
	protected DomainService getDomainService() {
		return domainService;
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @param daoService the daoService to set
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	/**
	 * @return the daoService
	 */
	protected DaoService getDaoService() {
		return daoService;
	}

}

