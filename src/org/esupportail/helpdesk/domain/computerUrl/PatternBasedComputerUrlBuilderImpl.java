/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.computerUrl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.springframework.util.StringUtils;

/**
 * A basic implementation of ComputerUrlBuilder based on a URL pattern.
 *
 */
public class PatternBasedComputerUrlBuilderImpl extends AbstractComputerUrlBuilder {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5278549483038055077L;
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The URL pattern.
	 */
	private String pattern;
	
	/**
	 * The i18n description key.
	 */
	private String i18nDescriptionKey;
	
	/**
	 * Constructor.
	 */
	public PatternBasedComputerUrlBuilderImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.hasText(
				pattern, 
				"property pattern of class " + getClass().getSimpleName() + " should not be null");
		Assert.hasText(
				i18nDescriptionKey, 
				"property i18nDescriptionKey of class " 
				+ getClass().getSimpleName() + " should not be null");
	}
	
	/**
	 * @see org.esupportail.helpdesk.domain.computerUrl.ComputerUrlBuilder#getUrl(java.lang.String)
	 */
	@Override
	public String getUrl(
			final String computer) {
		if (!StringUtils.hasText(computer)) {
			return null;
		}
		String hostname = computer;
		if (hostname.contains(" ")) {
			hostname = hostname.substring(0, hostname.indexOf(" "));
		}
		try {
			hostname = InetAddress.getByName(hostname).getHostName();
		} catch (UnknownHostException e) {
			logger.error(e);
		}
		boolean digitsOnly = true;
		for (int i = 0; i < hostname.length(); i++) {
			char c = hostname.charAt(i);
			if (!Character.isDigit(c) && c != '.') {
				digitsOnly = false;
				break;
			}
		}
		if (!digitsOnly && hostname.contains(".")) {
			hostname = hostname.substring(0, hostname.indexOf("."));
		}
		if (!StringUtils.hasText(hostname)) {
			return null;
		}
		return String.format(
				pattern, 
				org.esupportail.commons.utils.strings.StringUtils.utf8UrlEncode(hostname));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.computerUrl.ComputerUrlBuilder#getDescription(java.util.Locale)
	 */
	@Override
	public String getDescription(final Locale locale) {
		return getI18nService().getString(i18nDescriptionKey, locale);
	}

	/**
	 * @return the i18nDescriptionKey
	 */
	protected String getI18nDescriptionKey() {
		return i18nDescriptionKey;
	}

	/**
	 * @param descriptionKey the i18nDescriptionKey to set
	 */
	public void setI18nDescriptionKey(final String descriptionKey) {
		i18nDescriptionKey = descriptionKey;
	}

	/**
	 * @return the pattern
	 */
	protected String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

}
