/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain; 

import java.io.Serializable;
import java.util.Locale;

import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.User;

/** 
 * The formatter of action i18n titles.
 */ 
public interface ActionI18nTitleFormatter extends Serializable {
	
	/**
	 * @param action 
	 * @param locale 
	 * @return the i18n title of an action. 
	 */
	String getActionTitle(DomainService domainService, Action action, Locale locale, User user);

}

