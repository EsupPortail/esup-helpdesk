/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.i18n;

import java.util.HashMap;
import java.util.Locale;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

/**
 * A map to store bundle strings.
 */
public class BundleMap extends HashMap<String, String> {
	
	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 4329416691636820025L;
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The locale (used for error messages only).
	 */
	private Locale locale;

	/**
	 * Bean constructor.
	 * @param locale 
	 */
	public BundleMap(final Locale locale) {
		super();
		this.locale = locale;
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object key) {
		String result = super.get(key);
		if (result == null) {
			logger.warn("no string found for key '" + key + "' and locale '" + locale + "'");
			return "?????" + key + "?????"; 
		}
		return result;
	}
	
	

}
