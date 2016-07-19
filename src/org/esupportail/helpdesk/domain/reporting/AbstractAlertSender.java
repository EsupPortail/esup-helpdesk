/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.reporting;

import java.util.Locale;

import org.esupportail.commons.aop.monitor.Monitor;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * An abstract alert sender.
 */
@Monitor
public abstract class AbstractAlertSender extends AbstractSenderFormatter {

	/**
	 * Bean constructor.
	 */
	public AbstractAlertSender() {
		super();
	}

    /**
     * @param user
     * @param ticket
     * @return the reply-to HTML content.
     */
    protected String getReplyToContent(
			final User user,
			@SuppressWarnings("unused")
			final Ticket ticket) {
		Locale locale = getDomainService().getUserStore().getUserLocale(user);
		String result = "<hr/>" + getI18nService().getString(
				"EMAIL.TICKET.COMMON.EMAIL_TO_COMMENT", locale);
        return result;
    }

    /**
	 * Send an alert to one user for a ticket.
	 * @param email if not null, sent to anonymous managers.
	 * @param authTypeIfNullUser
	 * @param user
	 * @param ticket
	 * @param subject
	 * @param contentHeader
	 * @param contentFooter
	 * @return true if the email was sent.
	 */
	protected boolean ticketMonitoringSendAlert(
			final String email,
			final String authTypeIfNullUser,
			final User user,
			final Ticket ticket,
			final String subject,
			final String contentHeader,
			final String contentFooter) {
		String htmlContent = "";
		htmlContent += contentHeader;
		if (isUseReplyTo()) {
			htmlContent += getReplyToContent(user, ticket);
		}
		htmlContent += getEmailOrPrintHistory(user, ticket);
		htmlContent += getEmailQuickLinks(authTypeIfNullUser, user, ticket);
		htmlContent += getEmailOrPrintProperties(user, ticket);
		htmlContent += getEmailOrPrintFiles(user, ticket);
		htmlContent += getEmailOrPrintInvitations(user, ticket);
		htmlContent += getEmailOrPrintMonitoring(user, ticket);
		htmlContent += getEmailOrPrintOwnerInfo(user, ticket);
		htmlContent += contentFooter;
        String messageId = genMessageId(ticket);
        if (user == null) {
        	return send(email, null, messageId, subject, htmlContent);
        }
    	return send(user, messageId, subject, htmlContent);
	}

}
