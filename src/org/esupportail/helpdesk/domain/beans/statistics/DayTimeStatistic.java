 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import java.sql.Timestamp;

import org.esupportail.helpdesk.services.statistics.StatisticsUtils;

/**
 * An class for the statistics of charge time by day. */
public class DayTimeStatistic extends MonthTimeStatistic {

	/**
     * The day of month (1-31).
     */
    private int dayOfMonth;

	/**
	 * Default constructor.
	 */
	protected DayTimeStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param date
	 * @param min 
	 * @param avg 
	 * @param max 
	 * @param number 
	 */
	public DayTimeStatistic(
			final Timestamp date,
			final Integer min,
			final Integer avg,
			final Integer max,
			final Integer number) {
		super(date, min, avg, max, number);
		this.dayOfMonth = StatisticsUtils.getDay(date);
	}

	/**
	 * Constructor.
	 * @param date
	 */
	public DayTimeStatistic(
			final Timestamp date) {
		this(date, 0, 0, 0, 0);
	}

	/**
	 * @return the dayOfMonth
	 */
	public int getDayOfMonth() {
		return dayOfMonth;
	}

	/**
	 * @param dayOfMonth the dayOfMonth to set
	 */
	public void setDayOfMonth(final int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

}
