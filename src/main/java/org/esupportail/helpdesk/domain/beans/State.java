/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;
 

import java.io.Serializable;
import java.sql.Timestamp;

import org.esupportail.commons.utils.strings.StringUtils;

/**
 * A class to store the state of the application in the database.
 */
public class State implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3760424325482798955L;

	/**
	 * The primary key.
	 */
	private long id;
	
	/**
	 * The state of the upgrade.
	 */
	private String upgradeState;
	
	/**
	 * The time when the index was updated for the last time (for tickets).
     * @deprecated
     */
	@Deprecated
	private Timestamp ticketsLastIndexTime;

	/**
	 * The time when the index was updated for the last time (for FAQ containers).
     * @deprecated
     */
	@Deprecated
	private Timestamp faqContainersLastIndexTime;

	/**
	 * The time when the index was updated for the last time (for FAQ entries).
     * @deprecated
     */
	@Deprecated
	private Timestamp faqEntriesLastIndexTime;

	/**
	 * The time when the index was updated for the last time (for archived tickets).
     * @deprecated
     */
	@Deprecated
	private Timestamp archivedTicketsLastIndexTime;
	
	/**
	 * Bean constructor.
	 */
	public State() {
		super();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof State)) {
			return false;
		}
		return ((State) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

	/**
	 * @return the upgradeState
	 */
	public String getUpgradeState() {
		return upgradeState;
	}

	/**
	 * @param upgradeState the upgradeState to set
	 */
	public void setUpgradeState(final String upgradeState) {
		this.upgradeState = StringUtils.nullIfEmpty(upgradeState);
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @return the ticketsLastIndexTime
     * @deprecated
     */
	@Deprecated
	public Timestamp getTicketsLastIndexTime() {
		return ticketsLastIndexTime;
	}

	/**
	 * @param ticketsLastIndexTime the ticketsLastIndexTime to set
     * @deprecated
     */
	@Deprecated
	public void setTicketsLastIndexTime(final Timestamp ticketsLastIndexTime) {
		this.ticketsLastIndexTime = ticketsLastIndexTime;
	}

	/**
	 * @return the faqContainersLastIndexTime
     * @deprecated
     */
	@Deprecated
	public Timestamp getFaqContainersLastIndexTime() {
		return faqContainersLastIndexTime;
	}

	/**
	 * @param faqContainersLastIndexTime the faqContainersLastIndexTime to set
     * @deprecated
     */
	@Deprecated
	public void setFaqContainersLastIndexTime(final Timestamp faqContainersLastIndexTime) {
		this.faqContainersLastIndexTime = faqContainersLastIndexTime;
	}

	/**
	 * @return the faqEntriesLastIndexTime
     * @deprecated
     */
	@Deprecated
	public Timestamp getFaqEntriesLastIndexTime() {
		return faqEntriesLastIndexTime;
	}

	/**
	 * @param faqEntriesLastIndexTime the faqEntriesLastIndexTime to set
     * @deprecated
     */
	@Deprecated
	public void setFaqEntriesLastIndexTime(final Timestamp faqEntriesLastIndexTime) {
		this.faqEntriesLastIndexTime = faqEntriesLastIndexTime;
	}

	/**
	 * @return the archivedTicketsLastIndexTime
     * @deprecated
     */
	@Deprecated
	public Timestamp getArchivedTicketsLastIndexTime() {
		return archivedTicketsLastIndexTime;
	}

	/**
	 * @param archivedTicketsLastIndexTime the archivedTicketsLastIndexTime to set
     * @deprecated
     */
	@Deprecated
	public void setArchivedTicketsLastIndexTime(
			final Timestamp archivedTicketsLastIndexTime) {
		this.archivedTicketsLastIndexTime = archivedTicketsLastIndexTime;
	}

}
