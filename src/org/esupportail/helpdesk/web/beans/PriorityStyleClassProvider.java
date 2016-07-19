/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

/** 
 * A provider for priority style classes.
 */ 
public class PriorityStyleClassProvider extends HashMap<Integer, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8169334489538411629L;

	/**
	 * The prefix.
	 */
	private static final String PREFIX = "priority";
	
	/**
	 * Bean constructor.
	 */
	public PriorityStyleClassProvider() {
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
	public static String getStyleClass(final Integer key) {
		return new PriorityStyleClassProvider().get(key);
	}

}

