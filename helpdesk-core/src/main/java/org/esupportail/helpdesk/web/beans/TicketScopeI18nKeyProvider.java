/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

/** 
 * A provider for ticket scope i18n keys.
 */ 
public class TicketScopeI18nKeyProvider extends HashMap<String, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -717377663094225360L;
	
	/**
	 * The i18n prefix.
	 */
	private static final String PREFIX = "DOMAIN.TICKET_SCOPE.";
	
	/**
	 * Bean constructor.
	 */
	public TicketScopeI18nKeyProvider() {
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
	 * @return the i18n key that corresponds to a ticket scope. 
	 */
	public static String getI18nKey(final String key) {
		return new TicketScopeI18nKeyProvider().get(key);
	}

}

