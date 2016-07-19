 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import org.esupportail.helpdesk.domain.beans.Department;

/**
 * An class for the statistics of ticket creations by hour of day. */
public class HourOfDayTicketCreationStatistic extends AbstractTicketCreationStatistic {

	/**
     * The hour of day.
     */
    private int hourOfDay;

	/**
	 * Default constructor.
	 */
	protected HourOfDayTicketCreationStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param hourOfDay
	 * @param department
	 * @param origin
	 * @param number 
	 */
	public HourOfDayTicketCreationStatistic(
			final int hourOfDay,
			final Department department,
			final String origin,
			final int number) {
		super(department, origin, number);
		this.hourOfDay = hourOfDay;
	}

	/**
	 * Constructor.
	 * @param hourOfDay
	 * @param department
	 * @param origin 
	 */
	public HourOfDayTicketCreationStatistic(
			final int hourOfDay,
			final Department department,
			final String origin) {
		this(hourOfDay, department, origin, 0);
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
