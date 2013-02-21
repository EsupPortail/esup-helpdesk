/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

/** 
 * A provider for faq scope i18n keys.
 */ 
public class FaqScopeI18nKeyProvider extends HashMap<String, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -9024913809090838025L;

	/**
	 * The i18n prefix.
	 */
	private static final String PREFIX = "DOMAIN.FAQ_SCOPE.";
	
	/**
	 * Bean constructor.
	 */
	public FaqScopeI18nKeyProvider() {
		super();
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object ticketScope) {
		return PREFIX + ticketScope;
	}
	
	/**
	 * @param key 
	 * @return the i18n key that corresponds to a faq scope. 
	 */
	public static String getI18nKey(final String key) {
		return new FaqScopeI18nKeyProvider().get(key);
	}

}

