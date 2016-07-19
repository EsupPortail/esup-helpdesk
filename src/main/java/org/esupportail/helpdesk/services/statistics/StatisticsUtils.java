/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.statistics;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;


/**
 * Utilities to compute statistics.
 */
public class StatisticsUtils {
	
	/**
	 * The number of hours per day.
	 */
    public static final int HOURS_PER_DAY = 24;

	/**
	 * Constructor.
	 */
	private StatisticsUtils() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Convert a calendar to a timestamp.
	 * @param calendar 
	 * @return a timestamp
	 */
	private static Timestamp calendarToTimestamp(final Calendar calendar) {
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * Convert a timestamp to a calendar.
	 * @param timestamp
	 * @return a calendar
	 */
	private static Calendar timestampToCalendar(final Timestamp timestamp) {
		Calendar calendar = (Calendar) Calendar.getInstance().clone();
		calendar.setTime(timestamp);
		return calendar;
	}

	/**
	 * Add some time to a timestamp.
	 * @param ts
	 * @param field 
	 * @param value 
	 * @return the changed timestamp.
	 */
	protected static Timestamp getDiffDate(
			final Timestamp ts,
			final int field,
			final int value) {
		Calendar calendar = timestampToCalendar(ts);
	    calendar.add(field, value);
	    return calendarToTimestamp(calendar);
	}

	/**
	 * @param year
	 * @param month 
	 * @param dayOfMonth 
	 * @param hour 
	 * @return the calendar for an hour.
	 */
	private static Calendar getHourCalendar(
			final int year,
			final int month,
			final int dayOfMonth,
			final int hour) {
		Calendar calendar = getDayCalendar(year, month, dayOfMonth);
	    calendar.set(Calendar.HOUR_OF_DAY, hour);
	    return calendar;
	}

	/**
	 * @param year
	 * @param month 
	 * @param dayOfMonth 
	 * @param hour 
	 * @return the date for an hour.
	 */
	public static Timestamp getHourDate(
			final int year,
			final int month,
			final int dayOfMonth,
			final int hour) {
		return calendarToTimestamp(getHourCalendar(year, month, dayOfMonth, hour));
	}

	/**
	 * @param ts
	 * @return the hour-rounded value of a date.
	 */
	public static Timestamp getHourRoundedDate(final Timestamp ts) {
		Calendar calendar = timestampToCalendar(ts);
	    return getHourDate(
	    		calendar.get(Calendar.YEAR), 
	    		calendar.get(Calendar.MONTH), 
	    		calendar.get(Calendar.DAY_OF_MONTH), 
	    		calendar.get(Calendar.HOUR_OF_DAY));
	}

	/**
	 * @param ts
	 * @return the upper hour-rounded value of a date.
	 */
	public static Timestamp getHourUpperRoundedDate(final Timestamp ts) {
		return getHourRoundedDate(getNextHourDate(ts));
	}

	/**
	 * @param ts
	 * @return the next hour value of a date.
	 */
	public static Timestamp getNextHourDate(final Timestamp ts) {
		return getDiffDate(ts, Calendar.HOUR, 1);
	}

	/**
	 * @param ts
	 * @return the previous hour value of a date.
	 */
	public static Timestamp getPreviousHourDate(final Timestamp ts) {
		return getDiffDate(ts, Calendar.HOUR, -1);
	}

	/**
	 * @param ts
	 * @return the hour
	 */
	public static int getHour(final Timestamp ts) {
		return timestampToCalendar(ts).get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * @param year
	 * @param month 
	 * @param dayOfMonth 
	 * @return the calendar for a day.
	 */
	public static Calendar getDayCalendar(
			final int year,
			final int month,
			final int dayOfMonth) {
		Calendar calendar = getMonthCalendar(year, month);
	    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	    return calendar;
	}

	/**
	 * @param year
	 * @param month 
	 * @param dayOfMonth 
	 * @return the date for a day.
	 */
	public static Timestamp getDayDate(
			final int year,
			final int month,
			final int dayOfMonth) {
		return calendarToTimestamp(getDayCalendar(year, month, dayOfMonth));
	}

	/**
	 * @param ts
	 * @return the day-rounded value of a date.
	 */
	public static Timestamp getDayRoundedDate(final Timestamp ts) {
		Calendar calendar = timestampToCalendar(ts);
	    return getDayDate(
	    		calendar.get(Calendar.YEAR), 
	    		calendar.get(Calendar.MONTH), 
	    		calendar.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * @param ts
	 * @return the upper day-rounded value of a date.
	 */
	public static Timestamp getDayUpperRoundedDate(final Timestamp ts) {
		return getDayRoundedDate(getNextDayDate(ts));
	}

	/**
	 * @param ts
	 * @return the next day value of a day-rounded date.
	 */
	public static Timestamp getNextDayDate(final Timestamp ts) {
		return getDiffDate(ts, Calendar.DAY_OF_MONTH, 1);
	}

	/**
	 * @param ts
	 * @return the previous day value of a date.
	 */
	public static Timestamp getPreviousDayDate(final Timestamp ts) {
		return getDiffDate(ts, Calendar.DAY_OF_MONTH, -1);
	}

	/**
	 * @param ts
	 * @return the day of month
	 */
	public static int getDay(final Timestamp ts) {
		return timestampToCalendar(ts).get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * @param dow
	 * @param locale 
	 * @return the name of the day of week
	 */
	public static String getDayOfWeekShortName(
			final Integer dow,
			final Locale locale) {
	    DateFormatSymbols dfs = new DateFormatSymbols(locale);
	    String[] weekDays = dfs.getShortWeekdays();
	    return weekDays[dow];
	}
	
	/**
	 * @param ts
	 * @param locale 
	 * @return the name of the day of week
	 */
	public static String getDayOfWeekShortName(
			final Timestamp ts,
			final Locale locale) {
		return getDayOfWeekShortName(timestampToCalendar(ts).get(Calendar.DAY_OF_WEEK), locale);
	}
	
	/**
	 * @param ts
	 * @return the next week value of a date.
	 */
	public static Timestamp getNextWeekDate(final Timestamp ts) {
		return getDiffDate(ts, Calendar.WEEK_OF_YEAR, 1);
	}

	/**
	 * @param ts
	 * @return the previous week value of a date.
	 */
	public static Timestamp getPreviousWeekDate(final Timestamp ts) {
		return getDiffDate(ts, Calendar.WEEK_OF_YEAR, -1);
	}

	/**
	 * @param ts
	 * @return the day of week
	 */
	public static int getDayOfWeek(final Timestamp ts) {
		return timestampToCalendar(ts).get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * @param year
	 * @param month 
	 * @return the calendar for a month.
	 */
	public static Calendar getMonthCalendar(
			final int year,
			final int month) {
		Calendar calendar = getYearCalendar(year);
	    calendar.set(Calendar.MONTH, month);
	    return calendar;
	}

	/**
	 * @param year
	 * @param month 
	 * @return the date for a month.
	 */
	public static Timestamp getMonthDate(
			final int year,
			final int month) {
		return calendarToTimestamp(getMonthCalendar(year, month));
	}

	/**
	 * @param ts
	 * @return the month-rounded value of a date.
	 */
	public static Timestamp getMonthRoundedDate(final Timestamp ts) {
		Calendar calendar = timestampToCalendar(ts);
	    return getMonthDate(
	    		calendar.get(Calendar.YEAR), 
	    		calendar.get(Calendar.MONTH));
	}

	/**
	 * @param ts
	 * @return the upper month-rounded value of a date.
	 */
	public static Timestamp getMonthUpperRoundedDate(final Timestamp ts) {
		return getMonthRoundedDate(getNextMonthDate(ts));
	}

	/**
	 * @param ts
	 * @return the next month value of a date.
	 */
	public static Timestamp getNextMonthDate(final Timestamp ts) {
		return getDiffDate(ts, Calendar.MONTH, 1);
	}

	/**
	 * @param ts
	 * @return the previous month value of a date.
	 */
	public static Timestamp getPreviousMonthDate(final Timestamp ts) {
		return getDiffDate(ts, Calendar.MONTH, -1);
	}

	/**
	 * @param ts
	 * @param locale 
	 * @return the name of the month
	 */
	public static String getMonthName(
			final Timestamp ts,
			final Locale locale) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ts);
		int month = calendar.get(Calendar.MONTH);
	    DateFormatSymbols dfs = new DateFormatSymbols(locale);
	    String[] months = dfs.getMonths();
	    return months[month];
	}
	
	/**
	 * @param ts
	 * @param locale 
	 * @return the short name of the month
	 */
	public static String getMonthShortName(
			final Timestamp ts,
			final Locale locale) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ts);
		int month = calendar.get(Calendar.MONTH);
	    DateFormatSymbols dfs = new DateFormatSymbols(locale);
	    String[] months = dfs.getShortMonths();
	    return months[month];
	}
	
	/**
	 * @param ts
	 * @return the month
	 */
	public static int getMonth(final Timestamp ts) {
		return timestampToCalendar(ts).get(Calendar.MONTH);
	}
	
	/**
	 * @param year
	 * @return the calendar for a year.
	 */
	public static Calendar getYearCalendar(final int year) {
		Calendar calendar = Calendar.getInstance();
	    calendar.clear();
	    calendar.set(Calendar.YEAR, year);
	    return calendar;
	}

	/**
	 * @param year
	 * @return the date for a year.
	 */
	public static Timestamp getYearDate(final int year) {
		return calendarToTimestamp(getYearCalendar(year));
	}

	/**
	 * @param ts
	 * @return the year-rounded value of a date.
	 */
	public static Timestamp getYearRoundedDate(final Timestamp ts) {
		Calendar calendar = timestampToCalendar(ts);
	    return getYearDate(
	    		calendar.get(Calendar.YEAR));
	}

	/**
	 * @param ts
	 * @return the upper year-rounded value of a date.
	 */
	public static Timestamp getYearUpperRoundedDate(final Timestamp ts) {
		return getYearRoundedDate(getNextYearDate(ts));
	}

	/**
	 * @param ts
	 * @return the next year value of a date.
	 */
	public static Timestamp getNextYearDate(final Timestamp ts) {
		return getDiffDate(ts, Calendar.YEAR, 1);
	}

	/**
	 * @param ts
	 * @return the previous year value of a date.
	 */
	public static Timestamp getPreviousYearDate(final Timestamp ts) {
		return getDiffDate(ts, Calendar.YEAR, -1);
	}

	/**
	 * @param ts
	 * @return the year
	 */
	public static int getYear(final Timestamp ts) {
		return timestampToCalendar(ts).get(Calendar.YEAR);
	}
	
	/**
	 * @param dow
	 * @param hour
	 * @return an integer that represents a hour of week.
	 */
	public static int hourOfWeek(final int dow, final int hour) {
		return dow * HOURS_PER_DAY + hour;
	}
	
	/**
	 * @param how
	 * @return the hour part of a hour of week.
	 */
	public static int getHourFromHourOfWeek(final int how) {
		return how % HOURS_PER_DAY;
	}
	
	/**
	 * @param how
	 * @return the day part of a hour of week.
	 */
	public static int getDayFromHourOfWeek(final int how) {
		return how / HOURS_PER_DAY;
	}
	
}
