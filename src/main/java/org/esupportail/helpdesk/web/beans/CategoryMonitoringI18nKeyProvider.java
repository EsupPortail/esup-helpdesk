/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

/** 
 * A provider of i18n keys for category monitoring levels.
 */ 
public class CategoryMonitoringI18nKeyProvider extends HashMap<Integer, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2594124038107818183L;
	
	/**
	 * The i18n prefix.
	 */
	private static final String PREFIX = "DOMAIN.CATEGORY_MONITORING.";
	
	/**
	 * Bean constructor.
	 */
	public CategoryMonitoringI18nKeyProvider() {
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
		return new CategoryMonitoringI18nKeyProvider().get(key);
	}

}

