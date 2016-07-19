/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

/** 
 * A provider for priority i18n keys.
 */ 
public class PriorityI18nKeyProvider extends HashMap<Integer, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2340332073915386440L;
	
	/**
	 * The i18n prefix.
	 */
	private static final String PREFIX = "DOMAIN.PRIORITY.";
	
	/**
	 * Bean constructor.
	 */
	public PriorityI18nKeyProvider() {
		super();
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object key) {
		return PREFIX + key;
	}
	
	/**
	 * @param key 
	 * @return the i18n key that corresponds to priority. 
	 */
	public static String getI18nKey(final Integer key) {
		return new PriorityI18nKeyProvider().get(key);
	}

}

