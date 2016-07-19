 /**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import java.sql.Timestamp;

import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.services.statistics.StatisticsUtils;

/**
 * An class for the statistics of ticket creations by year. */
public class YearTicketCreationStatistic extends AbstractDateTicketCreationStatistic {

	/**
     * The year.
     */
    private int year;

	/**
	 * Default constructor.
	 */
	protected YearTicketCreationStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param date
	 * @param department
	 * @param origin
	 * @param number 
	 */
	public YearTicketCreationStatistic(
			final Timestamp date,
			final Department department,
			final String origin,
			final int number) {
		super(date, department, origin, number);
		this.year = StatisticsUtils.getYear(date);
	}

	/**
	 * Constructor.
	 * @param date
	 * @param department
	 * @param origin 
	 */
	public YearTicketCreationStatistic(
			final Timestamp date,
			final Department department,
			final String origin) {
		this(date, department, origin, 0);
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
