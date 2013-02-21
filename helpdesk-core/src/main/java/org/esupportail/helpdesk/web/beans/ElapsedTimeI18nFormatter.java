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
 * A formatter for elapsed times.
 */ 
public class ElapsedTimeI18nFormatter 
extends HashMap<Integer, String> 
implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 9104759717540503778L;

	/**
	 * Magic number.
	 */
	private static final int SECONDS_PER_MINUTE = 60;
	/**
	 * Magic number.
	 */
	private static final int MINUTES_PER_HOUR = 60;
	/**
	 * Magic number.
	 */
	private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
	/**
	 * Magic number.
	 */
	private static final int HOURS_PER_DAY = 24;
	/**
	 * Magic number.
	 */
	private static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;
	/**
	 * Magic number.
	 */
	private static final int DAYS_PER_WEEK = 7;
	/**
	 * Magic number.
	 */
	private static final int SECONDS_PER_WEEK = SECONDS_PER_DAY * DAYS_PER_WEEK;
	/**
	 * Magic number.
	 */
	private static final int DAYS_PER_MONTH = 30;
	/**
	 * Magic number.
	 */
	private static final int SECONDS_PER_MONTH = SECONDS_PER_DAY * DAYS_PER_MONTH;
	/**
	 * Magic number.
	 */
	private static final int DAYS_PER_YEAR = 365;
	/**
	 * Magic number.
	 */
	private static final int SECONDS_PER_YEAR = SECONDS_PER_DAY * DAYS_PER_YEAR;
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
	public ElapsedTimeI18nFormatter() {
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
	 * @return the years part
	 */
	protected static Integer getYears(final Long i) {
		if (i == null) {
			return null;
		}
		return new Long(i / SECONDS_PER_YEAR).intValue();
	}

	/**
	 * @param i 
	 * @return the months part
	 */
	protected static Integer getMonths(final Long i) {
		if (i == null) {
			return null;
		}
		return new Long((i - (getYears(i) * SECONDS_PER_YEAR)) / SECONDS_PER_MONTH).intValue();
	}

	/**
	 * @param i 
	 * @return the weeks part
	 */
	protected static Integer getWeeks(final Long i) {
		if (i == null) {
			return null;
		}
		return new Long((i - (getYears(i) * SECONDS_PER_YEAR) - (getMonths(i) * SECONDS_PER_MONTH)) 
				/ SECONDS_PER_WEEK).intValue();
	}

	/**
	 * @param i 
	 * @return the days part
	 */
	protected static Integer getDays(final Long i) {
		if (i == null) {
			return null;
		}
		return new Long((i / SECONDS_PER_DAY) % DAYS_PER_WEEK).intValue();
	}

	/**
	 * @param i 
	 * @return the hours part
	 */
	protected static Integer getHours(final Long i) {
		if (i == null) {
			return null;
		}
		return new Long((i / SECONDS_PER_HOUR) % HOURS_PER_DAY).intValue();
	}

	/**
	 * @param i 
	 * @return the minutes part
	 */
	protected static Integer getMinutes(final Long i) {
		if (i == null) {
			return null;
		}
		return new Long((i / SECONDS_PER_MINUTE) % MINUTES_PER_HOUR).intValue();
	}

	/**
	 * @param i 
	 * @return the minutes part
	 */
	protected static Integer getSeconds(final Long i) {
		if (i == null) {
			return null;
		}
		return new Long(i % SECONDS_PER_MINUTE).intValue();
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
		if (time < SECONDS_PER_MINUTE) {
			Integer seconds = getSeconds(time);
			return service.getString("ELAPSED_TIME.SECONDS", locale, seconds);
		}
		if (time < SECONDS_PER_HOUR) {
			Integer minutes = getMinutes(time);
			Integer seconds = getSeconds(time);
			if (minutes < STEP && seconds != 0) {
				return service.getString("ELAPSED_TIME.MINUTES_SECONDS", locale, minutes, seconds);
			}
			return service.getString("ELAPSED_TIME.MINUTES", locale, minutes);
		}
		if (time < SECONDS_PER_DAY) {
			Integer hours = getHours(time);
			Integer minutes = getMinutes(time);
			if (hours < STEP && minutes != 0) {
				return service.getString("ELAPSED_TIME.HOURS_MINUTES", locale, hours, minutes);
			}
			return service.getString("ELAPSED_TIME.HOURS", locale, hours);
		}
		if (time < SECONDS_PER_WEEK) {
			Integer days = getDays(time);
			Integer hours = getHours(time);
			if (days < STEP && hours != 0) {
				return service.getString("ELAPSED_TIME.DAYS_HOURS", locale, days, hours);
			}
			return service.getString("ELAPSED_TIME.DAYS", locale, days);
		}
		if (time < SECONDS_PER_MONTH) {
			Integer weeks = getWeeks(time);
			Integer days = getDays(time);
			if (weeks < STEP && days != 0) {
				return service.getString("ELAPSED_TIME.WEEKS_DAYS", locale, weeks, days);
			}
			return service.getString("ELAPSED_TIME.WEEKS", locale, weeks);
		}
		if (time < SECONDS_PER_YEAR) {
			Integer months = getMonths(time);
			Integer weeks = getWeeks(time);
			if (months < STEP && weeks != 0) {
				return service.getString("ELAPSED_TIME.MONTHS_WEEKS", locale, months, weeks);
			}
			return service.getString("ELAPSED_TIME.MONTHS", locale, months);
		}
		Integer years = getYears(time);
		Integer months = getMonths(time);
		if (years < STEP && months != 0) {
			return service.getString("ELAPSED_TIME.YEARS_MONTHS", locale, years, months);
		}
		return service.getString("ELAPSED_TIME.YEARS", locale, years);
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

