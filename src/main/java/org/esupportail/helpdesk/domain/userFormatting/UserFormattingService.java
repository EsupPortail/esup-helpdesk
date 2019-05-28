/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userFormatting;

import java.util.Locale;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The interface of user formatting services.
 */
public interface UserFormattingService {

	/**
	 * @param user
	 * @param action
	 * @param locale 
	 * @return a formatted and i18nez string.
	 */
	String format(DomainService domainService, Ticket ticket, User user, Boolean anonymous, Locale locale, User currentUser);

}