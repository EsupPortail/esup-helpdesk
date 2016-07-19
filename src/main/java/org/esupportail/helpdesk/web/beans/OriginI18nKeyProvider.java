/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

/** 
 * A provider for priority i18n keys.
 */ 
public class OriginI18nKeyProvider extends HashMap<String, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -178469522351170894L;

	/**
	 * The i18n prefix.
	 */
	private static final String PREFIX = "DOMAIN.TICKET_ORIGIN.";
	
	/**
	 * Bean constructor.
	 */
	public OriginI18nKeyProvider() {
		super();
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object origin) {
		return PREFIX + origin;
	}
	
	/**
	 * @param key 
	 * @return the i18n key that corresponds to a ticket scope. 
	 */
	public static String getI18nKey(final String key) {
		return new OriginI18nKeyProvider().get(key);
	}

}

