/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import java.sql.Timestamp;

/**
 * An abstract class for the statistics of time. */
public abstract class AbstractDateTimeStatistic extends MinAvgMaxNumberStatistic {

    /**
     * The timestamp.
     */
    private Timestamp date;
    
	/**
	 * Default constructor.
	 */
	protected AbstractDateTimeStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param date
	 * @param min 
	 * @param avg 
	 * @param max 
	 * @param number 
	 */
	public AbstractDateTimeStatistic(
			final Timestamp date,
			final Integer min,
			final Integer avg,
			final Integer max,
			final Integer number) {
		super(min, avg, max, number);
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
