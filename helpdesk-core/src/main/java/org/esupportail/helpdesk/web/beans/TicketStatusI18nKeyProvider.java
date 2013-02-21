/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

/** 
 * A provider for priority i18n keys.
 */ 
public class TicketStatusI18nKeyProvider extends HashMap<String, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8295090872375707951L;

	/**
	 * The i18n prefix.
	 */
	private static final String PREFIX = "DOMAIN.TICKET_STATUS.";
	
	/**
	 * Bean constructor.
	 */
	public TicketStatusI18nKeyProvider() {
		super();
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object ticketStatus) {
		return PREFIX + ticketStatus;
	}
	
	/**
	 * @param key 
	 * @return the i18n key that corresponds to a ticket status. 
	 */
	public static String getI18nKey(final String key) {
		return new TicketStatusI18nKeyProvider().get(key);
	}

}

