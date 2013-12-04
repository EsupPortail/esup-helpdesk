package org.esupportail.helpdesk.services.i18n;

import java.util.Locale;
import java.util.Map;

import org.esupportail.commons.services.i18n.AbstractI18nService;
import org.esupportail.commons.utils.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;

/**
 * An implementation of I18nService that loads several bundles.
 * See /properties/i18n/i18n.xml for details.
 *
 * See /properties/i18n/i18n-example.xml.
 */
public class ResourceBundleMessageSourceI18nServiceImpl extends AbstractI18nService implements InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 4294880275369021655L;

	/**
	 * The basename of the properties files that holds the bundles.
	 */
	private ReloadableResourceBundleMessageSource messageSource;

	/**
	 * Bean constructor.
	 */
	public ResourceBundleMessageSourceI18nServiceImpl() {
		super();
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(messageSource,
				"property messageSource of class " + getClass().getName() + " can not be null");
		Assert.isInstanceOf(ReloadableResourceBundleMessageSource.class, messageSource, "property messageSource of class " + getClass().getName() + ": ");
	}

	@Override
	public Map<String, String> getStrings(final Locale locale) {
		return messageSource.getStrings(locale);
	}

	@Override
	public String getString(String key, Locale locale) {
		return messageSource.getMessage(key, null, locale);
	}

	@Override
	public String getString(String key) {
		return messageSource.getMessage(key, null, getDefaultLocale());
	}

	@Override
	public String getString(String key, Locale locale, Object... args) {
		return messageSource.getMessage(key, args, locale);
	}

	@Override
	public String getString(String key, Object... args) {
		return messageSource.getMessage(key, args, getDefaultLocale());
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = (ReloadableResourceBundleMessageSource) messageSource;
	}

}
