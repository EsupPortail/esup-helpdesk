/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

/** 
 * A provider for auth types i18n keys.
 */ 
public class AuthTypeI18nKeyProvider extends HashMap<String, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8547193363759898524L;

	/**
	 * The i18n prefix.
	 */
	private static final String PREFIX = "DOMAIN.AUTH.";
	
	/**
	 * Bean constructor.
	 */
	public AuthTypeI18nKeyProvider() {
		super();
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object key) {
		return PREFIX + key.toString().toUpperCase();
	}
	
	/**
	 * @param key 
	 * @return the i18n key that corresponds to the auth type. 
	 */
	public static String getI18nKey(final String key) {
		return new AuthTypeI18nKeyProvider().get(key);
	}

}

