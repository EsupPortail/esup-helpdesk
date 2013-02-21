/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;

import org.esupportail.helpdesk.domain.beans.Department;

/**
 * An abstract class for the statistics of ticket creations. */
public abstract class AbstractTicketCreationStatistic extends AbstractIntegerStatistic {

    /**
     * The department.
     */
    private Department department;

    /**
     * The origin.
     */
    private String origin;

	/**
	 * Default constructor.
	 */
	protected AbstractTicketCreationStatistic() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param department
	 * @param origin
	 * @param number 
	 */
	public AbstractTicketCreationStatistic(
			final Department department,
			final String origin,
			final int number) {
		super(number);
		this.department = department;
		this.origin = origin;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(final String origin) {
		this.origin = origin;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(final Department department) {
		this.department = department;
	}

}
