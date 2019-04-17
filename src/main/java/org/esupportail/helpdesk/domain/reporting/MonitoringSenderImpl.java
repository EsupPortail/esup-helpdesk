/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.reporting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.helpdesk.domain.ActionType;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Bookmark;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Invitation;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.TicketContainer;
import org.esupportail.helpdesk.domain.beans.TicketMonitoring;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.exceptions.DepartmentManagerNotFoundException;


/**
 * The basic implementation of MonitoringSender.
 */
public class MonitoringSenderImpl extends AbstractAlertSender implements MonitoringSender {

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * Bean constructor.
	 */
	public MonitoringSenderImpl() {
		super();
	}

	/**
	 * Add a category if needed.
	 * @param monitoringCategories
	 * @param category
	 * @param createAction
	 * @param freeAction
	 * @param freeTicket
	 */
	protected void ticketMonitoringAddCategory(
			final List<Category> monitoringCategories,
			final Category category,
			final boolean createAction,
			final boolean freeAction,
			final boolean freeTicket) {
		String categoryEmail = category.getEffectiveMonitoringEmail();
		if (categoryEmail == null) {
			return;
		}
		boolean add;
		switch (category.getEffectiveMonitoringLevel()) {
		case TicketContainer.MONITORING_ALWAYS:
			add = true;
			break;
		case TicketContainer.MONITORING_CREATION:
			add = createAction;
			break;
		case TicketContainer.MONITORING_CREATION_OR_RELEASE:
			add = createAction || freeAction;
			break;
		case TicketContainer.MONITORING_CREATION_OR_FREE:
			add = createAction || freeTicket;
			break;
		default:
			add = false;
		}
		if (add) {
			(new Exception()).printStackTrace();
			ticketMonitoringAddCategory(monitoringCategories, category);
		}
	}

	/**
	 * Add the categories to warn for an action.
	 * @param monitoringCategories
	 * @param action
	 */
	protected void ticketMonitoringAddActionCategories(
			final List<Category> monitoringCategories,
			final Action action) {
		boolean createAction = ActionType.CREATE.equals(action.getActionType())
		|| ActionType.REOPEN.equals(action.getActionType())
		|| ActionType.CHANGE_DEPARTMENT.equals(action.getActionType())
		|| ActionType.CANCEL_POSTPONEMENT.equals(action.getActionType());
		boolean freeAction = ActionType.FREE.equals(action.getActionType());
		boolean freeTicket = action.getTicket().isFree();
		Category category = action.getTicket().getCategory();
		ticketMonitoringAddCategory(
				monitoringCategories, category, createAction, freeAction, freeTicket);
		if (action.getCategoryBefore() != null) {
			ticketMonitoringAddCategory(
					monitoringCategories, action.getCategoryBefore(), false, false, freeTicket);
		}
	}

	/**
	 * Send an alert to one user or email for a ticket.
	 * @param email
	 * @param authTypeIfNullUser
	 * @param user
	 * @param ticket
	 * @param subjectKey 
	 */
	protected void ticketMonitoringSendAlertToUserOrEmail(
			final String email,
			final String authTypeIfNullUser,
			final User user,
			final Ticket ticket,
			final String subjectKey) {
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		String authType;
		if (email == null) {
			authType = user.getAuthType();
		} else {
			authType = authTypeIfNullUser;
		}
		String subject = getI18nService().getString(
				subjectKey, locale,
				ticket.getDepartment().getLabel(),
				String.valueOf(ticket.getId()),
				ticket.getLabel());
		String ticketUrl = getUrlBuilder().getTicketViewUrl(authType, ticket.getId());
		String htmlHeader = getI18nService().getString(
				"EMAIL.TICKET.MONITORING.HEADER", locale,
				ticketUrl, String.valueOf(ticket.getId()));
		String htmlFooter = getI18nService().getString(
				"EMAIL.TICKET.MONITORING.FOOTER", locale,
				getUrlBuilder().getPreferencesUrl(authType));
		if (ticketMonitoringSendAlert(
				email, authType, user, ticket, subject, htmlHeader, htmlFooter)) {
			if (user != null) {
				getDomainService().addAlert(getDomainService().getLastAction(ticket), user);
			} else {
				getDomainService().addAlert(getDomainService().getLastAction(ticket), email);
			}
		}
	}

	/**
	 * Send an alert to a user for a ticket.
	 * @param user
	 * @param ticket
	 * @param subjectKey 
	 */
	protected void ticketMonitoringSendAlertToUser(
			final User user,
			final Ticket ticket,
			final String subjectKey) {
		ticketMonitoringSendAlertToUserOrEmail(null, AuthUtils.NONE, user, ticket, subjectKey);
	}

	/**
	 * Send an alert to an email for a ticket.
	 * @param email
	 * @param authType
	 * @param ticket
	 * @param subjectKey 
	 */
	protected void ticketMonitoringSendAlertToEmail(
			final String email,
			final String authType,
			final Ticket ticket,
			final String subjectKey) {
		ticketMonitoringSendAlertToUserOrEmail(email, authType, null, ticket, subjectKey);
	}

	/**
	 * Send the alerts for a set of actions.
	 * @param author
	 * @param actions
	 * @param excludedUsers
	 * @param expiration 
	 */
	protected void ticketMonitoringSendAlerts(
			final User author,
			final List<Action> actions,
			final List<User> excludedUsers,
			final boolean expiration) {
		List<User> users = new ArrayList<User>();
		List<Action> theActions = new ArrayList<Action>();
		Boolean restrictAssign = getDomainService().getSendEmailManagerAutoAssign();
		
		long now = System.currentTimeMillis();
		for (Action action : actions) {
			if (action != null) {
				if (now - action.getDate().getTime() > 5000) {
					break;
				}
				theActions.add(action);
			}
		}
		if (theActions.size() == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("no action, skipping");
			}
			return;
		}
		Ticket ticket = theActions.get(0).getTicket();
		boolean createLikeAction = false;
		boolean createAction = false;
		boolean freeAction = false;
		boolean takeAction = false;
		boolean postponeAction = false;
		boolean cancelPostponementAction = false;
		boolean cancelAction = false;
		boolean requestInformationAction = false;
		boolean giveInformationAction = false;
		boolean deleteFileInfo = false;
		boolean closeAction = false;
		boolean approveClosureAction = false;
		boolean refuseClosureAction = false;
		boolean expireAction = false;
		boolean refuseAction = false;
		boolean assignAction = false;
		boolean reopenAction = false;
		boolean inviteAction = false;
		for (Action act : theActions) {
			if (ActionType.CREATE.equals(act.getActionType())
					|| ActionType.REOPEN.equals(act.getActionType())
					|| ActionType.CHANGE_DEPARTMENT.equals(act.getActionType())
					|| ActionType.CANCEL_POSTPONEMENT.equals(act.getActionType())) {
				createLikeAction = true;
			}
			if (ActionType.CREATE.equals(act.getActionType())) {
				createAction = true;
			} else if (ActionType.FREE.equals(act.getActionType())) {
				freeAction = true;
			} else if (ActionType.TAKE.equals(act.getActionType())) {
				takeAction = true;
			} else if (ActionType.POSTPONE.equals(act.getActionType())) {
				postponeAction = true;
			} else if (ActionType.CANCEL_POSTPONEMENT.equals(act.getActionType())) {
				cancelPostponementAction = true;
			} else if (ActionType.CANCEL.equals(act.getActionType())) {
				cancelAction = true;
			} else if (ActionType.REQUEST_INFORMATION.equals(act.getActionType())) {
				requestInformationAction = true;
			} else if (ActionType.GIVE_INFORMATION.equals(act.getActionType())) {
				giveInformationAction = true;
			} else if (ActionType.CLOSE.equals(act.getActionType())
					|| ActionType.CLOSE_APPROVE.equals(act.getActionType())) {
				closeAction = true;
			} else if (ActionType.APPROVE_CLOSURE.equals(act.getActionType())) {
				approveClosureAction = true;
			} else if (ActionType.DELETE_FILE_INFO.equals(act.getActionType())) {
				deleteFileInfo = true;
			} else if (ActionType.REFUSE_CLOSURE.equals(act.getActionType())) {
				refuseClosureAction = true;
			} else if (ActionType.EXPIRE.equals(act.getActionType())) {
				expireAction = true;
			} else if (ActionType.REFUSE.equals(act.getActionType())) {
				refuseAction = true;
			} else if (ActionType.ASSIGN.equals(act.getActionType())) {
				assignAction = true;
			} else if (ActionType.REOPEN.equals(act.getActionType())) {
				reopenAction = true;
			} else if (ActionType.INVITE.equals(act.getActionType())) {
				inviteAction = true;
			}
		}
		String subjectKey;
		if (createAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.CREATE";
		} else if (closeAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.CLOSE";
		} else if (freeAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.FREE";
		} else if (cancelAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.CANCEL";
		} else if (expireAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.EXPIRE";
		} else if (refuseAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.REFUSE";
		} else if (reopenAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.REOPEN";
		} else if (approveClosureAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.APPROVE_CLOSURE";
		} else if (refuseClosureAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.REFUSE_CLOSURE";
		} else if (assignAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.ASSIGN";
		} else if (takeAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.TAKE";
		} else if (postponeAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.POSTPONE";
		} else if (cancelPostponementAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.CANCEL_POSTPONEMENT";
		} else if (requestInformationAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.REQUEST_INFORMATION";
		} else if (giveInformationAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.GIVE_INFORMATION";
		} else if (inviteAction) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.INVITE";
		} else if (deleteFileInfo) {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT.DELETE_FILE_INFO";
		} else {
			subjectKey = "EMAIL.TICKET.MONITORING.SUBJECT";
		}
		if (logger.isDebugEnabled()) {
			logger.debug("sending alerts for ticket #" + ticket.getId()
					+ " (createLikeAction=" + createLikeAction + ")...");
		}
		if(restrictAssign && !assignAction) {
			Department departmentBefore = ticket.getDepartment();
			for (Action action : theActions) {
				if (action.getDepartmentBefore() != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("action.getDepartmentBefore()=["
								+ action.getDepartmentBefore().getLabel() + "]");
					}
					departmentBefore = action.getDepartmentBefore();
					ticketMonitoringAddDepartmentManagers(users, author, departmentBefore, false);
					break;
				}
			}
			departmentBefore = ticket.getDepartment();
			for (Action action : theActions) {
				if (action.getDepartmentBefore() != null) {
					departmentBefore = action.getDepartmentBefore();
				}
				if (action.getCategoryBefore() != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("action.getCategoryBefore()=["
								+ action.getCategoryBefore().getLabel() + "]");
					}
					ticketMonitoringAddCategoryMembers(users, author, action.getCategoryBefore(), false);
				}
				if (action.getManagerBefore() != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("action.getManagerBefore()=["
								+ action.getManagerBefore().getRealId() + "]");
					}
					try {
						ticketMonitoringAddTicketManager(
								users, author,
								getDomainService().getDepartmentManager(
										departmentBefore, action.getManagerBefore()),
								false);
					} catch (DepartmentManagerNotFoundException e) {
						// do nothing
					}
				}
				User owner = action.getTicketOwnerBefore();
				if (owner != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("action.getTicketOwnerBefore()=["
								+ owner.getRealId() + "]");
					}
					if (owner.getOwnerMonitoring()) {
						ticketMonitoringAddUser(users, author, owner);
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("ticket.getDepartment()=[" + ticket.getDepartment().getLabel() + "]");
			}
			ticketMonitoringAddDepartmentManagers(users, author, ticket.getDepartment(), createLikeAction);
			if (logger.isDebugEnabled()) {
				logger.debug("ticket.getCategory()=[" + ticket.getCategory().getLabel() + "]");
			}
			ticketMonitoringAddCategoryMembers(users, author, ticket.getCategory(), createLikeAction);
			if (ticket.getManager() != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("ticket.getManager()=[" + ticket.getManager().getRealId() + "]");
				}
				try {
					ticketMonitoringAddTicketManager(
							users, author,
							getDomainService().getDepartmentManager(ticket.getDepartment(), ticket.getManager()),
							createLikeAction);
				} catch (DepartmentManagerNotFoundException e) {
					// nothing to do, the user is not managing the department any more
					// but he is still marked as in charge of the ticket
				}
			}
		}
		if(restrictAssign && assignAction) {
			users.add(ticket.getManager());
			if (logger.isDebugEnabled()) {
				logger.debug("ticket.getManager()=[" + ticket.getManager().getRealId() + "]");
			}		
		}
		for (Invitation invitation : getDomainService().getInvitations(ticket)) {
			User invitedUser = invitation.getUser();
			if (logger.isDebugEnabled()) {
				logger.debug("invitedUser=["
						+ invitedUser.getRealId() + "]");
			}
			if (invitedUser.getInvitedMonitoring()) {
				ticketMonitoringAddUser(users, author, invitedUser);
			}
		}
		for (Bookmark bookmark : getDomainService().getBookmarks(ticket)) {
			User bookmarkUser = bookmark.getUser();
			if (logger.isDebugEnabled()) {
				logger.debug("bookmarkUser=["
						+ bookmarkUser.getRealId() + "]");
			}
			if (bookmarkUser.getBookmarkMonitoring()) {
				ticketMonitoringAddUser(users, author, bookmarkUser);
			}
		}
		if(!assignAction) {

			for (TicketMonitoring ticketMonitoring : getDomainService().getTicketMonitorings(ticket)) {
				User monitoringUser = ticketMonitoring.getUser();
				if (getDomainService().userCanViewTicket(monitoringUser, null, ticket)) {
					ticketMonitoringAddUser(users, author, monitoringUser);
				}
			}
		}
		User owner = ticket.getOwner();
		if (logger.isDebugEnabled()) {
			logger.debug("ticket.getOwner()=["
					+ owner.getRealId() + "]");
		}
		if (owner.getOwnerMonitoring()) {
			ticketMonitoringAddUser(users, author, owner);
		}
		for (User user : users) {
			if (excludedUsers == null || !excludedUsers.contains(user)) {
				boolean send = false;
				boolean invited = getDomainService().isInvited(user, ticket);
				for (Action action : theActions) {
					if (getDomainService().actionMonitorable(user, invited, action)) {
						send = true;
						break;
					}
				}
				if (send) {
					if (!expiration || user.getExpirationMonitoring()) {
						ticketMonitoringSendAlertToUser(user, ticket, subjectKey);
					}
				}
			}
		}
		boolean send = false;
		for (Action action : theActions) {
			if (getDomainService().actionMonitorable(null, false, action)) {
				send = true;
				break;
			}
		}
		if (send) {
			List<Category> monitoringCategories = new ArrayList<Category>();
			for (Action action : theActions) {
				ticketMonitoringAddActionCategories(monitoringCategories, action);
			}
			List<Category> singleMonitoringCategories = new ArrayList<Category>();
			List<String> emails = new ArrayList<String>();
			for (Category category : monitoringCategories) {
				String categoryEmail = category.getEffectiveMonitoringEmail();
				if (!emails.contains(categoryEmail)) {
					emails.add(categoryEmail);
					singleMonitoringCategories.add(category);
				}
			}
			for (Category category : singleMonitoringCategories) {
				ticketMonitoringSendAlertToEmail(
						category.getEffectiveMonitoringEmail(), category.getEffectiveMonitoringEmailAuthType(), 
						ticket, subjectKey);
			}
		}
	}

	/**
	 * Send the alerts for a set of actions.
	 * @param author
	 * @param actions
	 * @param expiration 
	 */
	protected void ticketMonitoringSendAlerts(
			final User author,
			final List<Action> actions,
			final boolean expiration) {
		ticketMonitoringSendAlerts(author, actions, null, expiration);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.MonitoringSender#ticketMonitoringSendAlerts(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Action,
	 * org.esupportail.helpdesk.domain.beans.Action, org.esupportail.helpdesk.domain.beans.Action,
	 * boolean)
	 */
	@Override
	public void ticketMonitoringSendAlerts(
			final User author,
			final Action action1,
			final Action action2,
			final Action action3,
			final boolean expiration) {
		List<Action> actions = new ArrayList<Action>();
		actions.add(action1);
		actions.add(action2);
		actions.add(action3);
		ticketMonitoringSendAlerts(author, actions, expiration);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.MonitoringSender#ticketMonitoringSendAlerts(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Action,
	 * org.esupportail.helpdesk.domain.beans.Action, boolean)
	 */
	@Override
	public void ticketMonitoringSendAlerts(
			final User author,
			final Action action1,
			final Action action2,
			final boolean expiration) {
		ticketMonitoringSendAlerts(author, action1, action2, null, expiration);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.MonitoringSender#ticketMonitoringSendAlerts(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Action, boolean)
	 */
	@Override
	public void ticketMonitoringSendAlerts(
			final User author,
			final Action action,
			final boolean expiration) {
		ticketMonitoringSendAlerts(author, action, null, null, expiration);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.MonitoringSender#ticketMonitoringSendAlerts(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Ticket, java.util.List, boolean)
	 */
	@Override
	public void ticketMonitoringSendAlerts(
			final User author,
			final Ticket ticket,
			final List<User> excludedUsers,
			final boolean expiration) {
		ticketMonitoringSendAlerts(author, getDomainService().getActions(ticket), excludedUsers, expiration);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.MonitoringSender#getMonitoringUsers(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public List<User> getMonitoringUsers(final Ticket ticket, final Boolean onlyMandatoryUsers) {
		return super.getMonitoringUsers(ticket, onlyMandatoryUsers);
	}

}
