/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.reporting;

import java.util.Locale;

import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The basic implementation of InvitationSender.
 */
public class InvitationSenderImpl extends AbstractAlertSender implements InvitationSender {

	/**
	 * Bean constructor.
	 */
	public InvitationSenderImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.InvitationSender#sendInvitationEmail(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.User,
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public boolean sendInvitationEmail(
			final User author,
			final User invitedUser,
			final Ticket ticket) {
		if (invitedUser.equals(author)) {
			return false;
		}
		Locale locale = getDomainService().getUserStore().getUserLocale(invitedUser);
		String subject = getI18nService().getString(
				"EMAIL.TICKET.INVITATION.SUBJECT", locale,
				ticket.getDepartment().getLabel(), ticket.getId(), ticket.getLabel());
		String ticketUrl = getUrlBuilder().getTicketViewUrl(invitedUser.getAuthType(), ticket.getId());
		String htmlHeader = getI18nService().getString(
				"EMAIL.TICKET.INVITATION.HEADER", locale,
				getUserFormattingService().format(author, locale),
				ticket.getId(), ticketUrl);
		return ticketMonitoringSendAlert(null, AuthUtils.NONE, invitedUser, ticket, subject, htmlHeader, "");
	}

}
