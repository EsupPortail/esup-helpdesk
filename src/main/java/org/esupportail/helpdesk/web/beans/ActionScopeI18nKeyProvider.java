/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

/** 
 * A provider for priority i18n keys.
 */ 
public class ActionScopeI18nKeyProvider extends HashMap<String, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3030041125490152294L;

	/**
	 * The i18n prefix.
	 */
	private static final String PREFIX = "DOMAIN.ACTION_SCOPE.";
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * Bean constructor.
	 */
	public ActionScopeI18nKeyProvider() {
		super();
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object actionScope) {
		String result = PREFIX + actionScope;
		if (logger.isDebugEnabled()) {
			logger.debug(getClass() + ".get(" + actionScope + ") => [" + result + "]");
		}
		return result;
	}
	
	/**
	 * @param key 
	 * @return the i18n key that corresponds to a ticket scope. 
	 */
	public static String getI18nKey(final String key) {
		return new ActionScopeI18nKeyProvider().get(key);
	}

}

