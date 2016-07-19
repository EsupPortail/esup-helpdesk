/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans;

import org.esupportail.helpdesk.domain.beans.Department;


/**
 * A visual class for the spent time statistics. */
public class SpentTimeStatisticsEntry {

    /**
     * The creation department.
     */
    private Department creationDepartment;

    /**
     * The final department.
     */
    private Department finalDepartment;

    /**
     * The number of tickets.
     */
    private int number;

    /**
     * The spent time.
     */
    private long spentTime;
    
    /**
     * The spent time days.
     */
    private long spentTimeDays;
    
    /**
     * The spent time hours.
     */
    private long spentTimeHours;
    
    /**
     * The spent time minutes.
     */
    private long spentTimeMinutes;
    
    /**
     * true to print the final department.
     */
    private boolean printFinalDepartment;

	/**
	 * Constructor.
	 * @param creationDepartment
	 * @param finalDepartment
	 * @param number
	 * @param spentTime
	 * @param printFinalDepartment 
	 */
	public SpentTimeStatisticsEntry(
			final Department creationDepartment,
			final Department finalDepartment, 
			final int number, 
			final long spentTime,
			final boolean printFinalDepartment) {
		super();
		this.creationDepartment = creationDepartment;
		this.finalDepartment = finalDepartment;
		this.number = number;
		this.spentTime = spentTime;
		this.printFinalDepartment = printFinalDepartment;
	}

	/**
	 * @return the creationDepartment
	 */
	public Department getCreationDepartment() {
		return creationDepartment;
	}

	/**
	 * @return the finalDepartment
	 */
	public Department getFinalDepartment() {
		return finalDepartment;
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

	/**
	 * @return the printFinalDepartment
	 */
	public boolean isPrintFinalDepartment() {
		return printFinalDepartment;
	}

	/**
	 * @return the spentTimeDays
	 */
	public long getSpentTimeDays() {
		return spentTimeDays;
	}

	/**
	 * @return the spentTimeHours
	 */
	public long getSpentTimeHours() {
		return spentTimeHours;
	}

	/**
	 * @return the spentTimeMinutes
	 */
	public long getSpentTimeMinutes() {
		return spentTimeMinutes;
	}

}
