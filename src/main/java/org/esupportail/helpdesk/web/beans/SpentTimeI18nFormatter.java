/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;
import java.util.Locale;

import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.web.controllers.SessionController;
import org.springframework.beans.factory.InitializingBean;

/** 
 * A formatter for spent times.
 */ 
public class SpentTimeI18nFormatter 
extends HashMap<Long, String> 
implements InitializingBean {
	
	/**
	 * Magic number.
	 */
	public static final int MINUTES_PER_HOUR = 60;
	/**
	 * Magic number.
	 */
	public static final int HOURS_PER_DAY = 8;

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2293761006634106542L;

	/**
	 * Magic number.
	 */
	private static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;
	/**
	 * Magic number.
	 */
	private static final int STEP = 5;
	
	/**
	 * The i18n service.
	 */
	private I18nService i18nService;
	
	/**
	 * The session controller.
	 */
	private SessionController sessionController;
	
	/**
	 * Bean constructor.
	 */
	public SpentTimeI18nFormatter() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(sessionController, 
				"property sessionController of class " + this.getClass().getName() 
				+ " can not be null");
		Assert.notNull(i18nService, 
				"property i18nService of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @param i 
	 * @return the days part
	 */
	public static Long getDays(final Long i) {
		if (i == null) {
			return null;
		}
		return i / MINUTES_PER_DAY;
	}

	/**
	 * @param i 
	 * @return the hours part
	 */
	public static Long getHours(final Long i) {
		if (i == null) {
			return null;
		}
		return (i / MINUTES_PER_HOUR) % HOURS_PER_DAY;
	}

	/**
	 * @param i 
	 * @return the minutes part
	 */
	public static Long getMinutes(final Long i) {
		if (i == null) {
			return null;
		}
		return i % MINUTES_PER_HOUR;
	}

	/**
	 * @param daysValue 
	 * @param hoursValue 
	 * @param minutesValue 
	 * @return the spent time from days, hours and minutes.
	 */
	public static long getSpentTime(
			final long daysValue,
			final long hoursValue,
			final long minutesValue) {
		if (daysValue == 0 && hoursValue == 0 && minutesValue == 0) {
			return -1;
		}
		return daysValue * MINUTES_PER_DAY + hoursValue * MINUTES_PER_HOUR + minutesValue;
	}

	/**
	 * @param service 
	 * @param value
	 * @param locale 
	 * @return a formatted elapsed time.
	 */
	public static String format(
			final I18nService service, 
			final Object value, 
			final Locale locale) {
		if (value == null) {
			return service.getString("ELAPSED_TIME.NULL", locale);
		}
		Long time = Long.valueOf(value.toString());
		if (time <= 0) {
			return service.getString("ELAPSED_TIME.NULL", locale);
		}
		if (time < MINUTES_PER_HOUR) {
			Long minutes = getMinutes(time);
			return service.getString("ELAPSED_TIME.MINUTES", locale, minutes);
		}
		if (time < MINUTES_PER_DAY) {
			Long minutes = getMinutes(time);
			Long hours = getHours(time);
			if (hours < STEP && minutes != 0) {
				return service.getString("ELAPSED_TIME.HOURS_MINUTES", locale, hours, minutes);
			}
			return service.getString("ELAPSED_TIME.HOURS", locale, hours);
		}
		Long days = getDays(time);
		Long hours = getHours(time);
		if (days < STEP && hours != 0) {
			return service.getString("ELAPSED_TIME.DAYS_HOURS", locale, days, hours);
		}
		return service.getString("ELAPSED_TIME.DAYS", locale, days);
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object o) {
		return format(i18nService, o, sessionController.getLocale());
	}

	/**
	 * @return the sessionController
	 */
	protected SessionController getSessionController() {
		return sessionController;
	}

	/**
	 * @param sessionController the sessionController to set
	 */
	public void setSessionController(final SessionController sessionController) {
		this.sessionController = sessionController;
	}

	/**
	 * @return the i18nService
	 */
	protected I18nService getI18nService() {
		return i18nService;
	}

	/**
	 * @param service the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}
	
}

