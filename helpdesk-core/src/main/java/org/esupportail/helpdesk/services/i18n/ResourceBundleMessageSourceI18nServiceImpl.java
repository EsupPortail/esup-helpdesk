package org.esupportail.helpdesk.services.i18n;

import java.util.Locale;
import java.util.Map;

import org.apache.myfaces.shared_impl.util.LocaleUtils;
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
public class ResourceBundleMessageSourceI18nServiceImpl extends AbstractI18nService {
	private final ReloadableResourceBundleMessageSource messageSource;
    private final Map<String, String> mergedProps;

    private static ResourceBundleMessageSourceI18nServiceImpl instance = null;
    private Locale defaultLocale;

    private ResourceBundleMessageSourceI18nServiceImpl(ReloadableResourceBundleMessageSource messageSource, String defLocale) {
        setDefaultLocale(defLocale);
        this.messageSource = messageSource;
        mergedProps = messageSource.getStrings(getDefaultLocale());
    }

    public static ResourceBundleMessageSourceI18nServiceImpl create(ReloadableResourceBundleMessageSource messageSource, String defLocale) {
        if (instance == null)
            instance = new ResourceBundleMessageSourceI18nServiceImpl(messageSource, defLocale);
        return instance;
    }

    @Override
	public Map<String, String> getStrings(final Locale locale) {
		return mergedProps;
	}

	@Override
	public String getString(String key, Locale locale) {
		return messageSource.getMessage(key, null, locale);
	}

	@Override
	public String getString(String key) {
		return messageSource.getMessage(key, null, defaultLocale);
	}

	@Override
	public String getString(String key, Locale locale, Object... args) {
		return messageSource.getMessage(key, args, locale);
	}

	@Override
	public String getString(String key, Object... args) {
		return messageSource.getMessage(key, args, defaultLocale);
	}

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = LocaleUtils.toLocale(defaultLocale);
    }
}
