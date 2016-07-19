/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;


/**
 * A class for the spent time statistics. */
public class SpentTimeStatistic {

    /**
     * The creation departmentId.
     */
    private Long creationDepartmentId;

    /**
     * The final departmentId.
     */
    private Long finalDepartmentId;

    /**
     * The number of tickets.
     */
    private int number;

    /**
     * The spent time.
     */
    private long spentTime;

	/**
	 * Constructor.
	 * @param creationDepartmentId
	 * @param finalDepartmentId
	 * @param number
	 * @param spentTime
	 */
	public SpentTimeStatistic(
			final Long creationDepartmentId,
			final Long finalDepartmentId, 
			final int number, 
			final long spentTime) {
		super();
		this.creationDepartmentId = creationDepartmentId;
		this.finalDepartmentId = finalDepartmentId;
		this.number = number;
		this.spentTime = spentTime;
	}

	/**
	 * @return the creationDepartmentId
	 */
	public Long getCreationDepartmentId() {
		return creationDepartmentId;
	}

	/**
	 * @return the finalDepartmentId
	 */
	public Long getFinalDepartmentId() {
		return finalDepartmentId;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @return the spentTime
	 */
	public long getSpentTime() {
		return spentTime;
	}

}
