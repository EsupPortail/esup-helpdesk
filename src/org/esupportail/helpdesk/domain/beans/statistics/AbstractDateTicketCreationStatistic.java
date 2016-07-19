/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import java.sql.Timestamp;

import org.esupportail.helpdesk.domain.beans.Department;

/**
 * An abstract class for the statistics of ticket creations. */
public abstract class AbstractDateTicketCreationStatistic extends AbstractTicketCreationStatistic {

    /**
     * The timestamp.
     */
    private Timestamp date;
    
	/**
	 * Default constructor.
	 */
	protected AbstractDateTicketCreationStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param date
	 * @param department
	 * @param origin
	 * @param number 
	 */
	public AbstractDateTicketCreationStatistic(
			final Timestamp date,
			final Department department,
			final String origin,
			final int number) {
		super(department, origin, number);
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public Timestamp getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	protected void setDate(final Timestamp date) {
		this.date = date;
	}

}
