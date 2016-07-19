/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.reporting;

import java.util.Locale;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The basic implementation of TicketPrinter.
 */
public class TicketPrinterImpl extends AbstractSenderFormatter implements TicketPrinter {

	/**
	 * Bean constructor.
	 */
	public TicketPrinterImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.AbstractSenderFormatter#getTicketPrintContent(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public String getTicketPrintContent(
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

}
