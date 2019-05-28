/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.reporting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.InternetAddress;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.urlGeneration.UrlGenerator;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.ActionI18nTitleFormatter;
import org.esupportail.helpdesk.domain.ActionScope;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.TicketScope;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Bookmark;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.FileInfo;
import org.esupportail.helpdesk.domain.beans.Invitation;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.TicketMonitoring;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.userFormatting.UserFormattingService;
import org.esupportail.helpdesk.exceptions.DepartmentManagerNotFoundException;
import org.esupportail.helpdesk.web.beans.ElapsedTimeI18nFormatter;
import org.esupportail.helpdesk.web.beans.FileSizeI18nFormatter;
import org.esupportail.helpdesk.web.beans.OriginI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.PriorityI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.PriorityStyleClassProvider;
import org.esupportail.helpdesk.web.beans.SpentTimeI18nFormatter;
import org.esupportail.helpdesk.web.beans.TicketScopeI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.TicketStatusI18nKeyProvider;

/**
 * An abstract email handler.
 */
public class AbstractSenderFormatter extends AbstractSender implements DomainServiceSettable {

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * {@link DomainService}.
	 */
	private DomainService domainService;

	/**
	 * The action title formatter.
	 */
	private ActionI18nTitleFormatter actionI18nTitleFormatter;

	/**
	 * {@link UserFormattingService}.
	 */
	private UserFormattingService userFormattingService;

	/**
	 * {@link UrlGenerator}.
	 */
	private UrlGenerator urlGenerator;

	/**
	 * Bean constructor.
	 */
	protected AbstractSenderFormatter() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.userFormattingService,
				"property userFormattingService of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(actionI18nTitleFormatter,
				"property actionI18nTitleFormatter of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(urlGenerator,
				"property urlGenerator of class " + this.getClass().getName()
				+ " can not be null");
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________SEND() {
		//
	}

	/**
	 * Send something to a user.
	 * @param user
	 * @param messageId
	 * @param subject
	 * @param content
	 * @return true if the message was sent.
	 */
	protected boolean send(
			final User user,
			final String messageId,
			final String subject,
			final String content) {
		if (logger.isDebugEnabled()) {
			logger.debug("sending an email to user [" + user.getRealId() + "]...");
		}
		InternetAddress to = getDomainService().getUserStore().getUserInternetAddress(user);
		if (to == null) {
			logger.warn("no email for user [" + user.getRealId() + "]");
			return false;
		}
		send(to, getDomainService().getUserStore().getUserLocale(user), messageId, subject, content);
		return true;
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________FORMATTING() {
		//
	}

	/**
	 * @param authTypeIfNullUser
	 * @param user
	 * @param ticket
	 * @return the quick links to include in a monitoring email.
	 */
	protected String getEmailQuickLinks(
			final String authTypeIfNullUser,
			final User user,
			final Ticket ticket) {
		String result = "";
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		String authType;
		if (user == null) {
			authType = authTypeIfNullUser;
		} else {
			authType = user.getAuthType();
		}
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.LINKS.HEADER", locale);
		if (user == null) {
			result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.LINKS.ANONYMOUS_INFO", locale);
		}
		if (ticket != null) {
			result += getI18nService().getString(
					"EMAIL.TICKET.COMMON.LINKS.TICKET_VIEW", locale,
					getUrlBuilder().getTicketViewUrl(authType, ticket.getId()),
					String.valueOf(ticket.getId()));
			if (user != null && getDomainService().userCanTake(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_TAKE", locale,
						getUrlBuilder().getTicketTakeUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanTakeAndClose(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_TAKE_AND_CLOSE", locale,
						getUrlBuilder().getTicketTakeAndCloseUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanTakeAndRequestInformation(user, ticket)) {
				result += getI18nService().getString(
                        "EMAIL.TICKET.COMMON.LINKS.TICKET_TAKE_AND_REQUEST_INFORMATION", locale,
                        getUrlBuilder().getTicketTakeAndRequestInformationUrl(authType, ticket.getId()),
    					String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanClose(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_CLOSE", locale,
						getUrlBuilder().getTicketCloseUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanFree(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_FREE", locale,
						getUrlBuilder().getTicketFreeUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanRequestInformation(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_REQUEST_INFORMATION", locale,
						getUrlBuilder().getTicketRequestInformationUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanGiveInformation(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_GIVE_INFORMATION", locale,
						getUrlBuilder().getTicketGiveInformationUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanReopen(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_REOPEN", locale,
						getUrlBuilder().getTicketReopenUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanRefuse(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_REFUSE", locale,
						getUrlBuilder().getTicketRefuseUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanApproveClosure(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_APPROVE_CLOSURE", locale,
						getUrlBuilder().getTicketApproveClosureUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanRefuseClosure(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_REFUSE_CLOSURE", locale,
						getUrlBuilder().getTicketRefuseClosureUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanAssign(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_ASSIGN", locale,
						getUrlBuilder().getTicketAssignUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
			if (user != null && getDomainService().userCanPostpone(user, ticket)) {
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.LINKS.TICKET_POSTPONE", locale,
						getUrlBuilder().getTicketPostponeUrl(authType, ticket.getId()),
						String.valueOf(ticket.getId()));
			}
		}
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.LINKS.CONTROL_PANEL", locale,
				getUrlBuilder().getControlPanelUrl(authType));
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.LINKS.BOOKMARKS", locale,
				getUrlBuilder().getBookmarksUrl(authType));
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.LINKS.FAQ", locale,
				getUrlBuilder().getFaqsUrl(authType, null));
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.LINKS.PREFERENCES", locale,
				getUrlBuilder().getPreferencesUrl(authType));
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.LINKS.ABOUT", locale,
				getUrlBuilder().getAboutUrl(authType),
				getApplicationService().getName(),
				getApplicationService().getVersion());
		return result;
	}

	/**
	 * @param action
	 * @return the URL of the effective scope of the action.
	 */
	protected String getActionScopeImageUrl(final Action action) {
		String scope = action.getEffectiveScope();
		String imageName;
		if (scope.equals(ActionScope.MANAGER)) {
			imageName = "private";
		} else if (scope.equals(ActionScope.OWNER)) {
			imageName = "protected";
		} else if (scope.equals(ActionScope.INVITED)) {
			imageName = "invited";
		} else { // DEFAULT
			imageName = "public";
		}
		return urlGenerator.getImageUrl("images/" + imageName + ".png");
	}
	
	/**
	 * @param ticket
	 * @return the URL of the effective scope of the ticket.
	 */
	protected String getTicketScopeImageUrl(final Ticket ticket) {
		String scope = ticket.getEffectiveScope();
		String imageName;
		if (scope.equals(TicketScope.PRIVATE)) {
			imageName = "private";
		} else if (scope.equals(TicketScope.SUBJECT_ONLY)) {
			imageName = "protected";
		} else { // PUBLIC et CAS
			imageName = "public";
		}
		return urlGenerator.getImageUrl("images/" + imageName + ".png");
	}
	
	/**
	 * @param user
	 * @param ticket
	 * @return the history to include in monitoring emails or prints.
	 */
	protected String getEmailOrPrintHistory(
			final User user,
			final Ticket ticket) {
		String result = "";
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.HISTORY.HEADER", locale,
				String.valueOf(ticket.getId()), ticket.getLabel(),
				getTicketScopeImageUrl(ticket));
		boolean invited = false;
		if (user != null) {
			invited = getDomainService().isInvited(user, ticket);
		}
		boolean alternateColor = false;
		for (Action action : getDomainService().getActions(ticket)) {
			String trClass = alternateColor ? "odd" : "even";
			String actionTitle = StringUtils.escapeHtml(
					actionI18nTitleFormatter.getActionTitle(getDomainService(), action, locale, null));
			String message = "";
			if (org.springframework.util.StringUtils.hasText(action.getMessage())) {
				if (action.getUser() == null) {
					message = "<em>" + getI18nService().getString(
							"TICKET_VIEW.HISTORY.NOT_IN_REPORTS", locale) + "</em>";
				} else if (user == null || getDomainService().userCanViewActionMessage(user, invited, action)) {
					message = action.getMessage();
				}
			}
			result += getI18nService().getString(
					"EMAIL.TICKET.COMMON.HISTORY.ENTRY", locale,
					trClass, getDomainService().getActionStyleClass(action), actionTitle, message, 
					getActionScopeImageUrl(action));
			alternateColor = !alternateColor;
		}
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.HISTORY.FOOTER", locale);
		return result;
	}

	/**
	 * @param user
	 * @param ticket
	 * @return the properties to include in monitoring emails or prints.
	 */
	protected String getEmailOrPrintProperties(
			final User user,
			final Ticket ticket) {
		String result = "";
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		String owner = "<strong>" + getI18nService().getString(
				"TICKET_VIEW.PROPERTIES.USER", locale,
				StringUtils.escapeHtml(userFormattingService.format(getDomainService(), ticket, ticket.getOwner(), false,locale, null)))
				+ "</strong>";
		String manager;
		if (ticket.getManager() != null) {
			manager = "<strong>" + getI18nService().getString(
					"TICKET_VIEW.PROPERTIES.USER", locale,
					StringUtils.escapeHtml(
							userFormattingService.format(getDomainService(), ticket, ticket.getManager(), false, locale, null)))
					+ "</strong>";
		} else {
			manager = "<em>" + getI18nService().getString(
					"TICKET_VIEW.PROPERTIES.NO_MANAGER", locale) + "</em>";
		}
		String status = "<strong>" + getI18nService().getString(
				TicketStatusI18nKeyProvider.getI18nKey(ticket.getStatus()), locale)
				+ "</strong>";
		String creationDepartmentLabel;
		if (ticket.getCreationDepartment() != null) {
			creationDepartmentLabel = "<strong>" + StringUtils.escapeHtml(
				ticket.getCreationDepartment().getLabel()) + "</strong>";
		} else {
			creationDepartmentLabel = "<em>" + getI18nService().getString(
					"TICKET_VIEW.PROPERTIES.DEPARTMENT_REMOVED", locale) + "</em>";
		}
		String categoryLabel = "<strong>" + getI18nService().getString(
				"TICKET_VIEW.PROPERTIES.CATEGORY_VALUE", locale,
				StringUtils.escapeHtml(ticket.getDepartment().getLabel()),
				StringUtils.escapeHtml(ticket.getCategory().getLabel())) + "</strong>";
		String scope = "<strong>";
		if (TicketScope.DEFAULT.equals(ticket.getScope())) {
			scope += getI18nService().getString(
							"TICKET_VIEW.PROPERTIES.SCOPE_VALUE_DEFAULT", locale,
							getI18nService().getString(TicketScopeI18nKeyProvider.getI18nKey(
									TicketScope.DEFAULT), locale),
							getI18nService().getString(TicketScopeI18nKeyProvider.getI18nKey(
									ticket.getEffectiveScope()), locale));
		} else {
			scope += getI18nService().getString(TicketScopeI18nKeyProvider.getI18nKey(ticket.getScope()));
		}
		scope += "</strong>";
		String priority = "<strong><span class=\""
			+ PriorityStyleClassProvider.getStyleClass(ticket.getEffectivePriorityLevel())
			+ "\">";
		if (ticket.getPriorityLevel() == 0) {
			priority += getI18nService().getString(
							"TICKET_VIEW.PROPERTIES.PRIORITY_VALUE_DEFAULT", locale,
							getI18nService().getString(PriorityI18nKeyProvider.getI18nKey(
									new Integer(0)), locale),
							getI18nService().getString(PriorityI18nKeyProvider.getI18nKey(
                                    new Integer(ticket.getEffectivePriorityLevel())),
									locale));
		} else {
			priority += getI18nService().getString(PriorityI18nKeyProvider.getI18nKey(
					new Integer(ticket.getEffectivePriorityLevel())), locale);
		}
		priority += "</span></strong>";
		String computer;
		if (ticket.getComputer() != null) {
			computer = "<strong>" + StringUtils.escapeHtml(ticket.getComputer()) + "</strong>";
		} else {
			computer = "<em>" + getI18nService().getString(
					"TICKET_VIEW.PROPERTIES.NO_COMPUTER", locale) + "</em>";
		}
		String spentTime;
		if (ticket.getSpentTime() == -1) {
			spentTime = "<em>" + getI18nService().getString(
					"TICKET_VIEW.PROPERTIES.NO_SPENT_TIME", locale) + "</em>";
		} else {
			spentTime = "<strong>"
				+ SpentTimeI18nFormatter.format(getI18nService(), ticket.getSpentTime(), locale)
				+ "</strong>";
		}
		String origin = "<strong>" + getI18nService().getString(
				OriginI18nKeyProvider.getI18nKey(ticket.getOrigin()), locale) + "</strong>";
		String creationDate = "<strong>" + ticket.getCreationDate() + "</strong>";
		String lastActionDate = "<strong>" + ticket.getLastActionDate() + "</strong>";
		String chargeTime;
		if (ticket.getChargeTime() == null) {
			chargeTime = "<em>" + ElapsedTimeI18nFormatter.format(
					getI18nService(), ticket.getChargeTime(), locale) + "</em>";
		} else {
			chargeTime = "<strong>" + ElapsedTimeI18nFormatter.format(
					getI18nService(), ticket.getChargeTime(), locale) + "</strong>";
		}
		String closureTime;
		if (ticket.getClosureTime() == null) {
			closureTime = "<em>" + ElapsedTimeI18nFormatter.format(
					getI18nService(), ticket.getClosureTime(), locale) + "</em>";
		} else {
			closureTime = "<strong>" + ElapsedTimeI18nFormatter.format(
					getI18nService(), ticket.getClosureTime(), locale) + "</strong>";
		}
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.PROPERTIES", locale,
				new Object [] {
				owner, manager, status, creationDepartmentLabel, categoryLabel,
				scope, priority, computer, spentTime, origin, creationDate, lastActionDate,
				chargeTime, closureTime,
				});
		return result;
	}

	/**
	 * @param user
	 * @param ticket
	 * @return the files to include in prints.
	 */
	protected String getEmailOrPrintFiles(
			final User user,
			final Ticket ticket) {
		String result = "";
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.FILES.TITLE", locale);
		List<FileInfo> fileInfos = getDomainService().getFileInfos(ticket);
		if (fileInfos.isEmpty()) {
			result += "<p>" + getI18nService().getString(
					"TICKET_VIEW.FILES.NO_FILE", locale) + "</p>";
		} else {
			result += getI18nService().getString(
					"EMAIL.TICKET.COMMON.FILES.HEADER", locale);
			boolean alternateColor = false;
			for (FileInfo fileInfo : fileInfos) {
				String trClass = "table-";
				if (alternateColor) {
					trClass = "odd";
				} else {
					trClass = "even";
				}
				String userString = "";
				if (fileInfo.getUser() != null) {
					userString = getI18nService().getString(
							"TICKET_VIEW.FILES.USER", locale,
							userFormattingService.format(getDomainService(),ticket, fileInfo.getUser(), false, locale, null));
				} else {
					userString = getI18nService().getString(
							"TICKET_VIEW.FILES.APPLICATION", locale);
				}
				String size = getI18nService().getString(
						"TICKET_VIEW.FILES.SIZE", locale,
						FileSizeI18nFormatter.format(
								getI18nService(),
								new Integer(fileInfo.getFilesize()),
								locale));
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.FILES.ENTRY", locale,
						trClass, fileInfo.getFilename(), size, fileInfo.getDate(), userString);
				alternateColor = !alternateColor;
			}
			result += getI18nService().getString(
					"EMAIL.TICKET.COMMON.FILES.FOOTER", locale);
		}
		return result;
	}

	/**
	 * @param user
	 * @param ticket
	 * @return the invited users to include in prints.
	 */
	protected String getEmailOrPrintInvitations(
			final User user,
			final Ticket ticket) {
		String result = "";
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.INVITATIONS.TITLE", locale);
		List<Invitation> invitations = getDomainService().getInvitations(ticket);
		if (invitations.isEmpty()) {
			result += "<p>" + getI18nService().getString(
					"TICKET_VIEW.INVITATIONS.NO_INVITATION", locale) + "</p>";
		} else {
			result += getI18nService().getString(
					"EMAIL.TICKET.COMMON.INVITATIONS.HEADER", locale);
			boolean alternateColor = false;
			for (Invitation invitation : invitations) {
				String trClass = "table-";
				if (alternateColor) {
					trClass = "odd";
				} else {
					trClass = "even";
				}
				String userString = getI18nService().getString(
							"TICKET_VIEW.INVITATIONS.INVITED_USER", locale,
							userFormattingService.format(getDomainService(), ticket, invitation.getUser(), false, locale, null));
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.INVITATIONS.ENTRY", locale,
						trClass, userString);
				alternateColor = !alternateColor;
			}
			result += getI18nService().getString(
					"EMAIL.TICKET.COMMON.INVITATIONS.FOOTER", locale);
		}
		return result;
	}

	/**
	 * @param user
	 * @param ticket
	 * @return the monitoring users to include in prints.
	 */
	protected String getEmailOrPrintMonitoring(
			final User user,
			final Ticket ticket) {
		String result = "";
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		result += getI18nService().getString(
				"EMAIL.TICKET.COMMON.MONITORING.TITLE", locale);
		List<User> monitoringUsers = getMonitoringUsers(ticket, false);
		if (monitoringUsers.isEmpty()) {
			result += "<p>" + getI18nService().getString(
					"TICKET_VIEW.MONITORING.NO_USER", locale) + "</p>";
		} else {
			result += getI18nService().getString(
					"EMAIL.TICKET.COMMON.MONITORING.HEADER", locale);
			boolean alternateColor = false;
			for (User monitoringUser : monitoringUsers) {
				String trClass = "table-";
				if (alternateColor) {
					trClass = "odd";
				} else {
					trClass = "even";
				}
				String userString = getI18nService().getString(
							"TICKET_VIEW.MONITORING.USER", locale,
							userFormattingService.format(getDomainService(), ticket, monitoringUser, false, locale, null));
				result += getI18nService().getString(
						"EMAIL.TICKET.COMMON.MONITORING.ENTRY", locale,
						trClass, userString);
				alternateColor = !alternateColor;
			}
			result += getI18nService().getString(
					"EMAIL.TICKET.COMMON.MONITORING.FOOTER", locale);
		}
		return result;
	}

	/**
	 * @param user
	 * @param ticket
	 * @return the owner info to include in prints.
	 */
	protected String getEmailOrPrintOwnerInfo(
			final User user,
			final Ticket ticket) {
		String result = "";
		if (getDomainService().isDepartmentManager(ticket.getDepartment(), user)) {
			Locale locale = getDomainService().getUserStore().getUserLocale(user);
			String info = getDomainService().getUserInfo(ticket.getOwner(), locale);
			result += getI18nService().getString(
					"EMAIL.TICKET.COMMON.OWNER_INFO.TITLE", locale);
			if (info != null) {
				result += info;
			} else {
				result += "<p>" + getI18nService().getString(
						"EMAIL.TICKET.COMMON.OWNER_INFO.NONE",
						locale, ticket.getOwner().getRealId()) + "</p>";
			}
		}
		return result;
	}

	/**
	 * @param user
	 * @param ticket
	 * @return the HTML string that corresponds to a ticket print.
	 */
	@RequestCache
	protected String getTicketPrintContent(
			final User user,
			final Ticket ticket) {
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		String htmlContent = "";
		String subject = getI18nService().getString(
				"PRINT.TICKET.SUBJECT", locale,
				String.valueOf(ticket.getId()), ticket.getLabel());
		htmlContent += getEmailOrPrintHeader(locale, subject);
		htmlContent += getEmailOrPrintHistory(user, ticket);
		htmlContent += getEmailOrPrintProperties(user, ticket);
		htmlContent += getEmailOrPrintFiles(user, ticket);
		htmlContent += getEmailOrPrintInvitations(user, ticket);
		htmlContent += getEmailOrPrintMonitoring(user, ticket);
		htmlContent += getEmailOrPrintOwnerInfo(user, ticket);
		htmlContent += getEmailOrPrintFooter(locale);
		htmlContent += "<script lang=\"javascript\">window.print();window.close();</script>\n";
		return htmlContent;
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________MONITORING() {
		//
	}

	/**
	 * Add a user to the list of the users to warn.
	 * @param users
	 * @param author
	 * @param user
	 */
	protected void ticketMonitoringAddUser(
			final List<User> users,
			final User author,
			final User user) {
		if (logger.isDebugEnabled()) {
			logger.debug("trying to add user [" + user.getRealId() + "]...");
		}
		if (user.equals(author)) {
			if (logger.isDebugEnabled()) {
				logger.debug("user [" + user.getRealId() + "] is the author, skipping");
			}
			return;
		}
		if (users.contains(user)) {
			if (logger.isDebugEnabled()) {
				logger.debug("user [" + user.getRealId() + "] already added, skipping");
			}
			return;
		}
		users.add(user);
		if (logger.isDebugEnabled()) {
			logger.debug("added user [" + user.getRealId() + "]");
		}
	}

	/**
	 * Add a department manager to the list of the users to warn.
	 * @param users
	 * @param author
	 * @param departmentManager
	 * @param createAction
	 * @param ticketMonitoringLevel
	 */
	protected void ticketMonitoringAddDepartmentManager(
			final List<User> users,
			final User author,
			final DepartmentManager departmentManager,
			final boolean createAction,
			final int ticketMonitoringLevel) {
		if (departmentManager == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("department manager is null");
			}
			return;
		}
		if (!departmentManager.getUser().getReceiveManagerMonitoring()) {
			if (logger.isDebugEnabled()) {
				logger.debug("manager monitoring is disabled");
			}
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("trying to add department manager [" + departmentManager.getUser().getRealId()
					+ "] (monitoringLevel=" + ticketMonitoringLevel
					+ ", createAction=" + createAction + ")...");
		}
		boolean send = false;
		if (ticketMonitoringLevel == DepartmentManager.TICKET_MONITORING_ALWAYS) {
			send = true;
		} else if (createAction
				&& ticketMonitoringLevel == DepartmentManager.TICKET_MONITORING_CREATION) {
			send = true;
		}
		if (send) {
			ticketMonitoringAddUser(users, author, departmentManager.getUser());
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("no need to add department manager ["
						+ departmentManager.getUser().getRealId() + "]");
			}
		}
	}

	/**
	 * Add the department managers of a department to the list of the users to warn.
	 * @param users
	 * @param author
	 * @param department
	 * @param createAction
	 */
	protected void ticketMonitoringAddDepartmentManagers(
			final List<User> users,
			final User author,
			final Department department,
			final boolean createAction) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"adding department managers of department [" + department.getLabel()
					+ "] (createAction=" + createAction + ")...");
		}
		for (DepartmentManager departmentManager : getDomainService().getDepartmentManagers(department)) {
			ticketMonitoringAddDepartmentManager(
					users, author, departmentManager,
					createAction, departmentManager.getTicketMonitoringAny());
		}
	}

	/**
	 * Add the members a category to the list of the users to warn.
	 * @param users
	 * @param author
	 * @param category
	 * @param createAction
	 */
	protected void ticketMonitoringAddCategoryMembers(
			final List<User> users,
			final User author,
			final Category category,
			final boolean createAction) {
		logger.debug("adding members of category [" + category.getLabel()
				+ "] (createAction=" + createAction + ")...");
		for (DepartmentManager manager : getDomainService().getEffectiveDepartmentManagers(category)) {
			ticketMonitoringAddDepartmentManager(
					users, author, manager,
					createAction, manager.getTicketMonitoringCategory());
		}
	}

	/**
	 * Add a ticket manager to the list of the users to warn.
	 * @param users
	 * @param author
	 * @param departmentManager
	 * @param createAction
	 */
	protected void ticketMonitoringAddTicketManager(
			final List<User> users,
			final User author,
			final DepartmentManager departmentManager,
			final boolean createAction) {
		logger.debug("adding ticket manager...");
		ticketMonitoringAddDepartmentManager(
				users, author, departmentManager,
				createAction, departmentManager.getTicketMonitoringManaged());
	}

	/**
	 * Add a category.
	 * @param monitoringCategories
	 * @param category
	 */
	protected void ticketMonitoringAddCategory(
			final List<Category> monitoringCategories,
			final Category category) {
		if (logger.isDebugEnabled()) {
			logger.debug("trying to add category [" + category + "]...");
		}
		if (monitoringCategories.contains(category)) {
			if (logger.isDebugEnabled()) {
				logger.debug("category [" + category + "] already added, skipping");
			}
			return;
		}
		monitoringCategories.add(category);
		if (logger.isDebugEnabled()) {
			logger.debug("added category [" + category + "]");
		}
	}

	/**
	 * @param ticket
	 * @return the users that should be warned for a ticket.
	 */
	protected List<User> getMonitoringUsers(final Ticket ticket, final Boolean onlyMandatoryUsers) {
		List<User> users = new ArrayList<User>();
		for (DepartmentManager departmentManager : getDomainService().getDepartmentManagers(ticket.getDepartment())) {
			if (departmentManager.getUser().getReceiveManagerMonitoring()) {
				if (departmentManager.getTicketMonitoringAny()
						== DepartmentManager.TICKET_MONITORING_ALWAYS) {
					if (!users.contains(departmentManager.getUser())) {
						users.add(departmentManager.getUser());
					}
				}
			}
		}
		for (DepartmentManager manager : getDomainService().getEffectiveDepartmentManagers(ticket.getCategory())) {
			if (manager.getUser().getReceiveManagerMonitoring()) {
				if (manager.getTicketMonitoringCategory()
						== DepartmentManager.TICKET_MONITORING_ALWAYS) {
					if (!users.contains(manager.getUser())) {
						users.add(manager.getUser());
					}
				}
			}
		}
		User user = ticket.getManager();
		if (user != null) {
			DepartmentManager departmentManager;
			try {
				departmentManager = getDomainService().getDepartmentManager(ticket.getDepartment(), user);
				if (departmentManager.getUser().getReceiveManagerMonitoring()) {
					if (departmentManager.getTicketMonitoringManaged()
							== DepartmentManager.TICKET_MONITORING_ALWAYS) {
						if (!users.contains(user)) {
							users.add(user);
						}
					}
				}
			} catch (DepartmentManagerNotFoundException e) {
				//
			}
		}
		//on ne prends pas les utilisateurs qui ont potentiellement été rajoutés à la surveillance du ticket
		if (! onlyMandatoryUsers) {
			for (TicketMonitoring ticketMonitoring : getDomainService().getTicketMonitorings(ticket)) {
				User monitoringUser = ticketMonitoring.getUser();
				if (!users.contains(monitoringUser)) {
					users.add(monitoringUser);
				}
			}
		}
		for (Invitation invitation : getDomainService().getInvitations(ticket)) {
			User invitedUser = invitation.getUser();
			if (invitedUser.getInvitedMonitoring() && !users.contains(invitedUser)) {
				users.add(invitedUser);
			}
		}
		for (Bookmark bookmark : getDomainService().getBookmarks(ticket)) {
			User bookmarkUser = bookmark.getUser();
			if (bookmarkUser.getBookmarkMonitoring() && !users.contains(bookmarkUser)) {
				users.add(bookmarkUser);
			}
		}
		User owner = ticket.getOwner();
		if (owner.getOwnerMonitoring() && !users.contains(owner)) {
			users.add(owner);
		}
		Collections.sort(users);
		return users;
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________GETTERS_SETTERS() {
		//
	}

	/**
	 * @return the domainService
	 */
	protected DomainService getDomainService() {
		Assert.notNull(
				domainService,
				"please call " + getClass() + ".setDomainService() before any other method");
		return domainService;
	}

	/**
	 * @return the userFormattingService
	 */
	protected UserFormattingService getUserFormattingService() {
		return userFormattingService;
	}

	/**
	 * @return the actionI18nTitleFormatter
	 */
	protected ActionI18nTitleFormatter getActionI18nTitleFormatter() {
		return actionI18nTitleFormatter;
	}

	/**
	 * @param actionI18nTitleFormatter the actionI18nTitleFormatter to set
	 */
	public void setActionI18nTitleFormatter(
			final ActionI18nTitleFormatter actionI18nTitleFormatter) {
		this.actionI18nTitleFormatter = actionI18nTitleFormatter;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.DomainServiceSettable#setDomainService(
	 * org.esupportail.helpdesk.domain.DomainService)
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @param userFormattingService the userFormattingService to set
	 */
	public void setUserFormattingService(final UserFormattingService userFormattingService) {
		this.userFormattingService = userFormattingService;
	}

	/**
	 * @return the urlGenerator.
	 */
	protected UrlGenerator getUrlGenerator() {
		return urlGenerator;
	}

	/**
	 * Return the urlGenerator.
	 * @param urlGenerator
	 */
	public void setUrlGenerator(final UrlGenerator urlGenerator) {
		this.urlGenerator = urlGenerator;
	}

}
