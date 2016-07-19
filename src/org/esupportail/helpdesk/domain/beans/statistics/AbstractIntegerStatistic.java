/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;


/**
 * An abstract class for integer statistics. */
public abstract class AbstractIntegerStatistic {

    /**
     * The number of occurrences.
     */
    private Integer number;

	/**
	 * Default constructor.
	 */
	protected AbstractIntegerStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param number 
	 */
	public AbstractIntegerStatistic(
			final Integer number) {
		this();
		this.number = number;
	}

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(final Integer number) {
		this.number = number;
	}

}
