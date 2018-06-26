/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * An abstract class that should be inherited by all the implementations 
 * of I18nService.
 */
@SuppressWarnings("serial")
public abstract class AbstractI18nService implements I18nService {
	
	/**
	 * Bean constructor.
	 */
	protected AbstractI18nService() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getDefaultLocale()
	 */
	@Override
	public Locale getDefaultLocale() {
		return I18nUtils.getDefaultLocale();
	}

	/**
	 * @param bundleBasename 
	 * @param locale
	 * @return The resource bundle corresponding to a Locale.
	 */
	protected synchronized ResourceBundle getResourceBundle(
			final String bundleBasename,
			final Locale locale) {
		return I18nUtils.getResourceBundle(bundleBasename, locale);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getStrings()
	 */
	@Override
	public Map<String, String> getStrings() {
		return getStrings(getDefaultLocale());
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#printableRelativeDate(long, java.util.Locale)
	 */
	@Override
	public String printableRelativeDate(final long date, final Locale locale) {
		return I18nUtils.printableRelativeDate(date, locale);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#printableRelativeDate(long)
	 */
	@Override
	public String printableRelativeDate(final long date) {
		return printableRelativeDate(date, getDefaultLocale());
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#printableDate(long, java.util.Locale)
	 */
	@Override
	public String printableDate(final long date, final Locale locale) {
		return I18nUtils.printableDate(date, locale);		
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#printableDate(long)
	 */
	@Override
	public String printableDate(final long date) {
		return printableDate(date, getDefaultLocale());		
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(java.lang.String, java.util.Locale)
	 */
	@Override
	public String getString(final String key, final Locale locale) {
		return getStrings(locale).get(key);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(java.lang.String)
	 */
	@Override
	public String getString(
			final String key) {
		return getString(key, getDefaultLocale());
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object[])
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object[] args) {
		String string = getString(key, locale);
		MessageFormat mf = new MessageFormat(string, locale);
		return mf.format(args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(java.lang.String, java.lang.Object[])
	 */
	@Override
	public String getString(
			final String key, 
			final Object[] args) {
		return getString(key, getDefaultLocale(), args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object arg0) {
		Object[] args = {arg0};
		return getString(key, locale, args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(java.lang.String, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Object arg0) {
		return getString(key, getDefaultLocale(), arg0);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object arg0, 
			final Object arg1) {
		Object[] args = {arg0, arg1};
		return getString(key, locale, args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Object arg0, 
			final Object arg1) {
		return getString(key, getDefaultLocale(), arg0, arg1);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2) {
		Object[] args = {arg0, arg1, arg2};
		return getString(key, locale, args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2) {
		return getString(key, getDefaultLocale(), arg0, arg1, arg2);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3) {
		Object[] args = {arg0, arg1, arg2, arg3};
		return getString(key, locale, args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3) {
		return getString(key, getDefaultLocale(), arg0, arg1, arg2, arg3);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4) {
		Object[] args = {arg0, arg1, arg2, arg3, arg4};
		return getString(key, locale, args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4) {
		return getString(key, getDefaultLocale(), arg0, arg1, arg2, arg3, arg4);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5) {
		Object[] args = {arg0, arg1, arg2, arg3, arg4, arg5};
		return getString(key, locale, args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5) {
		return getString(key, getDefaultLocale(), arg0, arg1, arg2, arg3, arg4, arg5);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5, 
			final Object arg6) {
		Object[] args = {arg0, arg1, arg2, arg3, arg4, arg5, arg6};
		return getString(key, locale, args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5, 
			final Object arg6) {
		return getString(key, getDefaultLocale(), arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5, 
			final Object arg6, 
			final Object arg7) {
		Object[] args = {arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7};
		return getString(key, locale, args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5, 
			final Object arg6, 
			final Object arg7) {
		return getString(key, getDefaultLocale(), arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5, 
			final Object arg6, 
			final Object arg7, 
			final Object arg8) {
		Object[] args = {arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8};
		return getString(key, locale, args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5, 
			final Object arg6, 
			final Object arg7, 
			final Object arg8) {
		return getString(key, getDefaultLocale(), arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5, 
			final Object arg6, 
			final Object arg7, 
			final Object arg8, 
			final Object arg9) {
		Object[] args = {arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9};
		return getString(key, locale, args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5, 
			final Object arg6, 
			final Object arg7, 
			final Object arg8, 
			final Object arg9) {
		return getString(key, getDefaultLocale(), arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.util.Locale, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Locale locale, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5, 
			final Object arg6, 
			final Object arg7, 
			final Object arg8, 
			final Object arg9, 
			final Object arg10) {
		Object[] args = {arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10};
		return getString(key, locale, args);
	}

	/**
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, 
	 * java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String getString(
			final String key, 
			final Object arg0, 
			final Object arg1, 
			final Object arg2, 
			final Object arg3, 
			final Object arg4, 
			final Object arg5, 
			final Object arg6, 
			final Object arg7, 
			final Object arg8, 
			final Object arg9, 
			final Object arg10) {
		return getString(key, getDefaultLocale(), 
				arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}

}

