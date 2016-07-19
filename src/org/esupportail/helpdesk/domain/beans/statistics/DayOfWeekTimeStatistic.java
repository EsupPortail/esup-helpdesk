 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;


/**
 * An class for the statistics of time by day of week. */
public class DayOfWeekTimeStatistic extends MinAvgMaxNumberStatistic {

	/**
     * The day of week.
     */
    private int dayOfWeek;

	/**
	 * Default constructor.
	 */
	protected DayOfWeekTimeStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param dayOfWeek
	 * @param min 
	 * @param avg 
	 * @param max 
	 * @param number 
	 */
	public DayOfWeekTimeStatistic(
			final int dayOfWeek,
			final Integer min,
			final Integer avg,
			final Integer max,
			final Integer number) {
		super(min, avg, max, number);
		this.dayOfWeek = dayOfWeek;
	}

	/**
	 * Constructor.
	 * @param dayOfWeek
	 */
	public DayOfWeekTimeStatistic(
			final int dayOfWeek) {
		this(dayOfWeek, 0, 0, 0, 0);
	}

	/**
	 * @return the dayOfWeek
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * @param dayOfWeek the dayOfWeek to set
	 */
	public void setDayOfWeek(final int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

}
