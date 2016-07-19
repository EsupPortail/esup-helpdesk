/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userFormatting;

import java.util.Locale;

import org.esupportail.helpdesk.domain.beans.User;

/**
 * The interface of user formatting services.
 */
public interface UserFormattingService {

	/**
	 * @param user
	 * @param locale 
	 * @return a formatted and i18nez string.
	 */
	String format(User user, Locale locale);

}