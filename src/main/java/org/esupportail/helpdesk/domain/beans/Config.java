/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;


import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A class to store the config of the application in the database.
 */
public class Config implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3572339244755039277L;

	/**
	 * The primary key.
	 */
	private long id;

	/**
	 * The time when the index was updated for the last time (for tickets).
	 */
	private Timestamp ticketsLastIndexTime;

	/**
	 * The time when the index was updated for the last time (for FAQs).
	 */
	private Timestamp faqsLastIndexTime;

	/**
	 * The time when the index was updated for the last time (for archived tickets).
	 */
	private Timestamp archivedTicketsLastIndexTime;

	/**
	 * The default department icon.
	 */
	private Icon defaultDepartmentIcon;

	/**
	 * The default category icon.
	 */
	private Icon defaultCategoryIcon;

	/**
	 * The install date.
	 */
	private Timestamp installDate;

	/**
	 * The department selection context time.
	 */
	private Timestamp departmentSelectionContextTime;

	/**
	 * Bean constructor.
	 */
	public Config() {
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
		if (!(obj instanceof Config)) {
			return false;
		}
		return ((Config) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int) getId();
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
	 */
	public Timestamp getTicketsLastIndexTime() {
		return ticketsLastIndexTime;
	}

	/**
	 * @param ticketsLastIndexTime the ticketsLastIndexTime to set
	 */
	public void setTicketsLastIndexTime(final Timestamp ticketsLastIndexTime) {
		this.ticketsLastIndexTime = ticketsLastIndexTime;
	}

	/**
	 * @return the faqsLastIndexTime
	 */
	public Timestamp getFaqsLastIndexTime() {
		return faqsLastIndexTime;
	}

	/**
	 * @param faqsLastIndexTime the faqsLastIndexTime to set
	 */
	public void setFaqsLastIndexTime(final Timestamp faqsLastIndexTime) {
		this.faqsLastIndexTime = faqsLastIndexTime;
	}

	/**
	 * @return the archivedTicketsLastIndexTime
	 */
	public Timestamp getArchivedTicketsLastIndexTime() {
		return archivedTicketsLastIndexTime;
	}

	/**
	 * @param archivedTicketsLastIndexTime the archivedTicketsLastIndexTime to set
	 */
	public void setArchivedTicketsLastIndexTime(
			final Timestamp archivedTicketsLastIndexTime) {
		this.archivedTicketsLastIndexTime = archivedTicketsLastIndexTime;
	}

	/**
	 * @return the defaultDepartmentIcon
	 */
	public Icon getDefaultDepartmentIcon() {
		return defaultDepartmentIcon;
	}

	/**
	 * @param defaultDepartmentIcon the defaultDepartmentIcon to set
	 */
	public void setDefaultDepartmentIcon(final Icon defaultDepartmentIcon) {
		this.defaultDepartmentIcon = defaultDepartmentIcon;
	}

	/**
	 * @return the defaultCategoryIcon
	 */
	public Icon getDefaultCategoryIcon() {
		return defaultCategoryIcon;
	}

	/**
	 * @param defaultCategoryIcon the defaultCategoryIcon to set
	 */
	public void setDefaultCategoryIcon(final Icon defaultCategoryIcon) {
		this.defaultCategoryIcon = defaultCategoryIcon;
	}

	/**
	 * @return the installDate
	 */
	public Timestamp getInstallDate() {
		return installDate;
	}

	/**
	 * @param installDate the installDate to set
	 */
	public void setInstallDate(final Timestamp installDate) {
		this.installDate = installDate;
	}

	/**
	 * @return the departmentSelectionContextTime
	 */
	public Timestamp getDepartmentSelectionContextTime() {
		return departmentSelectionContextTime;
	}

	/**
	 * @param departmentSelectionContextTime the departmentSelectionContextTime to set
	 */
	public void setDepartmentSelectionContextTime(
			final Timestamp departmentSelectionContextTime) {
		this.departmentSelectionContextTime = departmentSelectionContextTime;
	}

}
