/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

/** 
 * A provider for ticket monitoring i18n keys.
 */ 
public class TicketMonitoringI18nKeyProvider extends HashMap<String, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3219365216548337863L;

	/**
	 * The i18n prefix.
	 */
	private static final String PREFIX = "DOMAIN.TICKET_MONITORING.";
	
	/**
	 * Bean constructor.
	 */
	public TicketMonitoringI18nKeyProvider() {
		super();
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object ticketMonitoring) {
		return PREFIX + ticketMonitoring;
	}
	
	/**
	 * @param key 
	 * @return the i18n key that corresponds to a ticket scope. 
	 */
	public static String getI18nKey(final Integer key) {
		return new TicketMonitoringI18nKeyProvider().get(key);
	}

}

