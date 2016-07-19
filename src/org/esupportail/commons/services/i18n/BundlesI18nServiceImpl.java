/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.i18n;

import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.esupportail.commons.utils.Assert;
import org.springframework.beans.factory.InitializingBean;

/**
 * An implementation of I18nService that loads several bundles.
 * See /properties/i18n/i18n.xml for details.
 * 
 * See /properties/i18n/i18n-example.xml.
 */
public class BundlesI18nServiceImpl extends AbstractI18nService implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 4294880275369021655L;

	/**
	 * The basename of the properties files that holds the bundles.
	 */
	private List<String> bundleBasenames;
	
	/**
	 * Bean constructor.
	 */
	public BundlesI18nServiceImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notEmpty(bundleBasenames, 
				"property bundleBasenames of class " + getClass().getName() + " can not be empty");
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getStrings(java.util.Locale)
	 */
	@Override
	public Map<String, String> getStrings(final Locale locale) {
		Map<String, String> result = new BundleMap(locale);
		for (String bundleBasename : bundleBasenames) {
			ResourceBundle bundle = getResourceBundle(bundleBasename, locale);
			if (bundle != null) {
				Enumeration<String> keys = bundle.getKeys();
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					result.put(key, bundle.getString(key));
				}
			}
		}
		return result;
	}

	/**
	 * @param bundleBasenames the bundleBasenames to set
	 */
	public void setBundleBasenames(final List<String> bundleBasenames) {
		this.bundleBasenames = bundleBasenames;
	}

}

