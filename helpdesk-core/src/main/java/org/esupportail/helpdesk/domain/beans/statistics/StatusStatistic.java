/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;


/**
 * A class for status statistics. */
public class StatusStatistic extends AbstractIntegerStatistic {
	
	/**
	 * The department id.
	 */
	private Long departmentId;

	/**
	 * The status.
	 */
	private String status;

	/**
	 * @param departmentId
	 * @param status
	 * @param number
	 */
	public StatusStatistic(
			final Long departmentId, 
			final String status,
			final int number) {
		super(number);
		this.departmentId = departmentId;
		this.status = status;
	}

	/**
	 * @return the departmentId
	 */
	public Long getDepartmentId() {
		return departmentId;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

}
