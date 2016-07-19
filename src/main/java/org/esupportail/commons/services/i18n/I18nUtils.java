/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.i18n;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;

/**
 * An abstract class that should be inherited by all the implementations 
 * of I18nService.
 */
public class I18nUtils {
	
	/**
	 * The number of seconds per day.
	 */
	private static final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
	
	/**
	 * The number of seconds per day.
	 */
	private static final long NUMBER_OF_DAYS_TO_PRINT_DAY_OF_WEEK = 6;
	
	/**
	 * The name of the I18nService bean.
	 */
	private static final String I18N_SERVICE_BEAN = "i18nService";

	/**
	 * A logger.
	 */
	private static final Logger LOG = new LoggerImpl(I18nUtils.class);
	
	/**
	 * Bean constructor.
	 */
	private I18nUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return de default locale.
	 */
	public static Locale getDefaultLocale() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context == null) {
			return Locale.getDefault();
		}
		UIViewRoot viewRoot = null;
		try {
			viewRoot = context.getViewRoot();
		} catch (IllegalStateException e) {
			// context has probably been released, happens on exception handling
		}
		if (viewRoot != null) {
			return viewRoot.getLocale();
		}
		Application application = null;
		try {
			application = context.getApplication();
		} catch (IllegalStateException e) {
			// context has probably been released, happens on exception handling
		}
		if (application == null) {
			return Locale.getDefault();
		}
		return application.getDefaultLocale();
	}

	/**
	 * @param bundleBasename 
	 * @param locale
	 * @return The resource bundle corresponding to a Locale.
	 * @throws MissingResourceException 
	 */
	public static ResourceBundle getResourceBundle(
			final String bundleBasename,
			final Locale locale) {
		// be sure that a non null locale is passed to prevent from NPEs in ResourceBundle.getBundleImpl() 
		Locale theLocale = locale;
		if (theLocale == null) {
			theLocale = Locale.getDefault();
			if (theLocale == null) {
				throw new NullPointerException("null locale!");
			}
		}
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("looking for a bundle for locale [" + locale 
						+ "] and basename [" + bundleBasename + "]");
			}
			URL bundleUrl = I18nUtils.class.getResource("/");
			ResourceBundle bundle = ResourceBundle.getBundle(
					bundleBasename,
					locale,
					new URLClassLoader(new URL [] {bundleUrl}));
			if (LOG.isDebugEnabled()) {
				LOG.debug("bundle found for locale [" + locale 
						+ "] and basename [" + bundleBasename + "]");
			}
			return bundle;
		} catch (MissingResourceException e) {
			LOG.warn("no bundle found for locale [" + locale + "] and basename [" + bundleBasename + "]");
			return null;
		}
	}

	/**
	 * @param date 
	 * @param locale 
	 * @return the date in a printable and relative form.
	 */
	public static String printableRelativeDate(final long date, final Locale locale) {
		// get the current date
		long currentMillis = System.currentTimeMillis();
	    Calendar currentCalendar = new GregorianCalendar();
	    currentCalendar.setTimeInMillis(currentMillis);
	    // compute the given date
	    Calendar argCalendar = new GregorianCalendar();
	    argCalendar.setTimeInMillis(date);
	    // Test if the given date is the same day as the actual date.
	    long currentDayOfYear = currentCalendar.get(Calendar.DAY_OF_YEAR);
	    long currentYear = currentCalendar.get(Calendar.YEAR);
	    long argDayOfYear = argCalendar.get(Calendar.DAY_OF_YEAR);
	    long argYear = argCalendar.get(Calendar.YEAR);
	    if (currentYear == argYear && currentDayOfYear == argDayOfYear) {
			return new SimpleDateFormat("HH:mm", locale).format(new Timestamp(date));
	    }
	    // Test if the comparedDate is the same week as the actual date.
	    long currentDay = currentMillis / MILLISECONDS_PER_DAY; 
	    long argDay = date / MILLISECONDS_PER_DAY; 
	    if ((currentDay - argDay) <= NUMBER_OF_DAYS_TO_PRINT_DAY_OF_WEEK) {
			return new SimpleDateFormat("EEEE HH:mm", locale).format(new Timestamp(date));		
	    }
	    // The comparedDate is superious to one week as the actual date.
		return new SimpleDateFormat("dd-MM-yyyy", locale).format(new Timestamp(date));
	}

	/**
	 * @param date 
	 * @return the date in a printable and relative form.
	 */
	public static String printableRelativeDate(final long date) {
		return printableRelativeDate(date, getDefaultLocale());
	}

	/**
	 * @param date 
	 * @param locale 
	 * @return the date in a printable form.
	 */
	public static String printableDate(final long date, final Locale locale) {
		return new SimpleDateFormat("EEE dd-MM-yyyy HH:mm:ss", locale).format(new Timestamp(date));		
	}

	/**
	 * @param date 
	 * @return the date in a printable form.
	 */
	public static String printableDate(final long date) {
		return printableDate(date, getDefaultLocale());
	}

	/**
	 * @return the i18n service.
	 */
	public static I18nService createI18nService() {
		return (I18nService) BeanUtils.getBean(I18N_SERVICE_BEAN);
	}

}

