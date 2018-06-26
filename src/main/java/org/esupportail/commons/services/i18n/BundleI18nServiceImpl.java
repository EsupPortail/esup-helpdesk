/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.i18n;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.esupportail.commons.utils.Assert;
import org.springframework.beans.factory.InitializingBean;

/**
 * A simple implementation of I18nService.
 * 
 * See /properties/i18n/i18n-example.xml.
 */
public class BundleI18nServiceImpl extends AbstractI18nService implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 674871167329599584L;
	
	/**
	 * The basename of the properties files that holds the bundles.
	 */
	private String bundleBasename;
	
	/**
	 * Bean constructor.
	 */
	public BundleI18nServiceImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.hasText(bundleBasename, 
				"property bundleBasename of class " + getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getStrings(java.util.Locale)
	 */
	@Override
	public Map<String, String> getStrings(final Locale locale) {
		Map<String, String> map = new BundleMap(locale);
		ResourceBundle bundle = getResourceBundle(bundleBasename, locale);
		if (bundle != null) {
			Enumeration<String> keys = bundle.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				map.put(key, bundle.getString(key));
			}
		}
		return map;
	}

	/**
	 * @return The bundleBasename.
	 */
	public String getBundleBasename() {
		return bundleBasename;
	}
	/**
	 * @param bundleBasename The bundleBasename to set.
	 */
	public void setBundleBasename(final String bundleBasename) {
		this.bundleBasename = bundleBasename;
	}

}

