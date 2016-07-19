 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;


/**
 * An class for the statistics of time by day of week and hour. */
public class HourOfWeekTimeStatistic extends DayOfWeekTimeStatistic {

	/**
     * The hour of day.
     */
    private int hourOfDay;

	/**
	 * Default constructor.
	 */
	protected HourOfWeekTimeStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param dayOfWeek 
	 * @param hourOfDay
	 * @param min 
	 * @param avg 
	 * @param max 
	 * @param number 
	 */
	public HourOfWeekTimeStatistic(
			final int dayOfWeek,
			final int hourOfDay,
			final Integer min,
			final Integer avg,
			final Integer max,
			final Integer number) {
		super(dayOfWeek, min, avg, max, number);
		this.hourOfDay = hourOfDay;
	}

	/**
	 * Constructor.
	 * @param dayOfWeek 
	 * @param hourOfDay
	 */
	public HourOfWeekTimeStatistic(
			final int dayOfWeek,
			final int hourOfDay) {
		this(dayOfWeek, hourOfDay, 0, 0, 0, 0);
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
