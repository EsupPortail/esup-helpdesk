 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import java.sql.Timestamp;

import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.services.statistics.StatisticsUtils;

/**
 * An class for the statistics of ticket creations by month. */
public class MonthTicketCreationStatistic extends YearTicketCreationStatistic {

	/**
     * The month (1-12).
     */
    private int month;

	/**
	 * Default constructor.
	 */
	protected MonthTicketCreationStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param date
	 * @param department
	 * @param origin
	 * @param number 
	 */
	public MonthTicketCreationStatistic(
			final Timestamp date,
			final Department department,
			final String origin,
			final int number) {
		super(date, department, origin, number);
		this.month = StatisticsUtils.getMonth(date);
	}

	/**
	 * Constructor.
	 * @param date
	 * @param department
	 * @param origin 
	 */
	public MonthTicketCreationStatistic(
			final Timestamp date,
			final Department department,
			final String origin) {
		this(date, department, origin, 0);
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
