/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans.statistics;


/**
 * A set of status statistics. */
public class StatusStatisticSet {
	
	/**
	 * The number of archived tickets.
	 */
	private int archivedNumber;

	/**
	 * The number of closed tickets.
	 */
	private int closedNumber;

	/**
	 * The number of free tickets.
	 */
	private int freeNumber;

	/**
	 * The number of incomplete tickets.
	 */
	private int incompleteNumber;

	/**
	 * The number of in progress tickets.
	 */
	private int inProgressNumber;

	/**
	 * The number of postponed tickets.
	 */
	private int postponedNumber;

	/**
	 * Constructor.
	 */
	public StatusStatisticSet() {
		super();
		this.archivedNumber = 0;
		this.closedNumber = 0;
		this.freeNumber = 0;
		this.incompleteNumber = 0;
		this.inProgressNumber = 0;
		this.postponedNumber = 0;
	}

	/**
	 * @return the archivedNumber
	 */
	public int getArchivedNumber() {
		return archivedNumber;
	}

	/**
	 * @return the closedNumber
	 */
	public int getClosedNumber() {
		return closedNumber;
	}

	/**
	 * @return the freeNumber
	 */
	public int getFreeNumber() {
		return freeNumber;
	}

	/**
	 * @return the incompleteNumber
	 */
	public int getIncompleteNumber() {
		return incompleteNumber;
	}

	/**
	 * @return the inProgressNumber
	 */
	public int getInProgressNumber() {
		return inProgressNumber;
	}

	/**
	 * @return the postponedNumber
	 */
	public int getPostponedNumber() {
		return postponedNumber;
	}

	/**
	 * @param value
	 */
	public void incArchivedNumber(final int value) {
		this.archivedNumber += value;
	}

	/**
	 * @param value
	 */
	public void incClosedNumber(final int value) {
		this.closedNumber += value;
	}

	/**
	 * @param value
	 */
	public void incFreeNumber(final int value) {
		this.freeNumber += value;
	}

	/**
	 * @param value
	 */
	public void incIncompleteNumber(final int value) {
		this.incompleteNumber += value;
	}

	/**
	 * @param value
	 */
	public void incInProgressNumber(final int value) {
		this.inProgressNumber += value;
	}

	/**
	 * @param value
	 */
	public void incPostponedNumber(final int value) {
		this.postponedNumber += value;
	}

}
