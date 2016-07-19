 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import java.sql.Timestamp;

import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.services.statistics.StatisticsUtils;

/**
 * An class for the statistics of ticket creations by hour. */
public class HourTicketCreationStatistic extends DayTicketCreationStatistic {

	/**
     * The hour of day (0-23).
     */
    private int hourOfDay;

	/**
	 * Default constructor.
	 */
	protected HourTicketCreationStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param date
	 * @param department
	 * @param origin
	 * @param number 
	 */
	public HourTicketCreationStatistic(
			final Timestamp date,
			final Department department,
			final String origin,
			final int number) {
		super(date, department, origin, number);
		this.hourOfDay = StatisticsUtils.getHour(date);
	}

	/**
	 * Constructor.
	 * @param date
	 * @param department
	 * @param origin 
	 */
	public HourTicketCreationStatistic(
			final Timestamp date,
			final Department department,
			final String origin) {
		this(date, department, origin, 0);
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
