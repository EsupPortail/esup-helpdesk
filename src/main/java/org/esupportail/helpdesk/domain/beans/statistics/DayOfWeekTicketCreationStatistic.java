 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import org.esupportail.helpdesk.domain.beans.Department;

/**
 * An class for the statistics of ticket creations by day of week. */
public class DayOfWeekTicketCreationStatistic extends AbstractTicketCreationStatistic {

	/**
     * The day of week.
     */
    private int dayOfWeek;

	/**
	 * Default constructor.
	 */
	protected DayOfWeekTicketCreationStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param dayOfWeek
	 * @param department
	 * @param origin
	 * @param number 
	 */
	public DayOfWeekTicketCreationStatistic(
			final int dayOfWeek,
			final Department department,
			final String origin,
			final int number) {
		super(department, origin, number);
		this.dayOfWeek = dayOfWeek;
	}

	/**
	 * Constructor.
	 * @param dayOfWeek
	 * @param department
	 * @param origin 
	 */
	public DayOfWeekTicketCreationStatistic(
			final int dayOfWeek,
			final Department department,
			final String origin) {
		this(dayOfWeek, department, origin, 0);
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
