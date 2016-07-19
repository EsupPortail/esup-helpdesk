 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;


/**
 * An class for the statistics of time by hour of day. */
public class HourOfDayTimeStatistic extends MinAvgMaxNumberStatistic {

	/**
     * The hour of day.
     */
    private int hourOfDay;

	/**
	 * Default constructor.
	 */
	protected HourOfDayTimeStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param hourOfDay
	 * @param min 
	 * @param avg 
	 * @param max 
	 * @param number 
	 */
	public HourOfDayTimeStatistic(
			final int hourOfDay,
			final Integer min,
			final Integer avg,
			final Integer max,
			final Integer number) {
		super(min, avg, max, number);
		this.hourOfDay = hourOfDay;
	}

	/**
	 * Constructor.
	 * @param hourOfDay
	 */
	public HourOfDayTimeStatistic(
			final int hourOfDay) {
		this(hourOfDay, 0, 0, 0, 0);
	}

	/**
	 * @return the hourOfDay
	 */
	public int getHourOfDay() {
		return hourOfDay;
	}

	/**
	 * @param hourOfDay the hourOfDay to set
	 */
	public void setHourOfDay(final int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

}
