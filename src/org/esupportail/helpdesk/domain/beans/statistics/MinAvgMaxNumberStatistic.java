/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;


/**
 * A class for the statistics of time. */
public class MinAvgMaxNumberStatistic extends AbstractIntegerStatistic {

	/**
	 * The min.
	 */
	private Integer min;
	
	/**
	 * The max.
	 */
	private Integer max;
	
	/**
	 * The average.
	 */
	private Integer avg;
	
	/**
	 * Default constructor.
	 */
	public MinAvgMaxNumberStatistic() {
		this(0, 0, 0, 0);
	}
	
	/**
	 * Constructor.
	 * @param min 
	 * @param avg 
	 * @param max 
	 * @param number 
	 */
	public MinAvgMaxNumberStatistic(
			final Integer min,
			final Integer avg,
			final Integer max,
			final Integer number) {
		super(number);
		this.min = min;
		this.max = max;
		this.avg = avg;
	}
	
	/**
	 * @return the min
	 */
	public Integer getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(final Integer min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public Integer getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(final Integer max) {
		this.max = max;
	}

	/**
	 * @return the avg
	 */
	public Integer getAvg() {
		return avg;
	}

	/**
	 * @param avg the avg to set
	 */
	public void setAvg(final Integer avg) {
		this.avg = avg;
	}

}
