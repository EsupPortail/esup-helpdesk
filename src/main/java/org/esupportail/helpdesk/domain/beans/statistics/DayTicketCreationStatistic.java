 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import java.sql.Timestamp;

import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.services.statistics.StatisticsUtils;

/**
 * An class for the statistics of ticket creations by day. */
public class DayTicketCreationStatistic extends MonthTicketCreationStatistic {

	/**
     * The day of month (1-31).
     */
    private int dayOfMonth;

	/**
	 * Default constructor.
	 */
	protected DayTicketCreationStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param date
	 * @param department
	 * @param origin
	 * @param number 
	 */
	public DayTicketCreationStatistic(
			final Timestamp date,
			final Department department,
			final String origin,
			final int number) {
		super(date, department, origin, number);
		this.dayOfMonth = StatisticsUtils.getDay(date);
	}

	/**
	 * Constructor.
	 * @param date
	 * @param department
	 * @param origin 
	 */
	public DayTicketCreationStatistic(
			final Timestamp date,
			final Department department,
			final String origin) {
		this(date, department, origin, 0);
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
