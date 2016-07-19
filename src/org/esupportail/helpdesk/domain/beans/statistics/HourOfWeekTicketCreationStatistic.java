 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import org.esupportail.helpdesk.domain.beans.Department;

/**
 * An class for the statistics of ticket creations by day of week and hour. */
public class HourOfWeekTicketCreationStatistic extends DayOfWeekTicketCreationStatistic {

	/**
     * The hour of day.
     */
    private int hourOfDay;

	/**
	 * Default constructor.
	 */
	protected HourOfWeekTicketCreationStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param dayOfWeek 
	 * @param hourOfDay
	 * @param department
	 * @param origin
	 * @param number 
	 */
	public HourOfWeekTicketCreationStatistic(
			final int dayOfWeek,
			final int hourOfDay,
			final Department department,
			final String origin,
			final int number) {
		super(dayOfWeek, department, origin, number);
		this.hourOfDay = hourOfDay;
	}

	/**
	 * Constructor.
	 * @param dayOfWeek 
	 * @param hourOfDay
	 * @param department
	 * @param origin 
	 */
	public HourOfWeekTicketCreationStatistic(
			final int dayOfWeek,
			final int hourOfDay,
			final Department department,
			final String origin) {
		this(dayOfWeek, hourOfDay, department, origin, 0);
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
