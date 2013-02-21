 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import java.sql.Timestamp;

import org.esupportail.helpdesk.services.statistics.StatisticsUtils;

/**
 * An class for the statistics of time by year. */
public class YearTimeStatistic extends AbstractDateTimeStatistic {

	/**
     * The year.
     */
    private int year;

	/**
	 * Default constructor.
	 */
	protected YearTimeStatistic() {
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
	public YearTimeStatistic(
			final Timestamp date,
			final Integer min,
			final Integer avg,
			final Integer max,
			final Integer number) {
		super(date, min, avg, max, number);
		this.year = StatisticsUtils.getYear(date);
	}

	/**
	 * Constructor.
	 * @param date
	 */
	public YearTimeStatistic(
			final Timestamp date) {
		this(date, 0, 0, 0, 0);
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(final int year) {
		this.year = year;
	}

}
