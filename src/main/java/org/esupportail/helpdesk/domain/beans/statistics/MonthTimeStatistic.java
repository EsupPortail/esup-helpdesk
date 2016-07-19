 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import java.sql.Timestamp;

import org.esupportail.helpdesk.services.statistics.StatisticsUtils;

/**
 * An class for the statistics of time by month. */
public class MonthTimeStatistic extends YearTimeStatistic {

	/**
     * The month (1-12).
     */
    private int month;

	/**
	 * Default constructor.
	 */
	protected MonthTimeStatistic() {
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
	public MonthTimeStatistic(
			final Timestamp date,
			final Integer min,
			final Integer avg,
			final Integer max,
			final Integer number) {
		super(date, min, avg, max, number);
		this.month = StatisticsUtils.getMonth(date);
	}

	/**
	 * Constructor.
	 * @param date
	 */
	public MonthTimeStatistic(
			final Timestamp date) {
		this(date, 0, 0, 0, 0);
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(final int month) {
		this.month = month;
	}

}
