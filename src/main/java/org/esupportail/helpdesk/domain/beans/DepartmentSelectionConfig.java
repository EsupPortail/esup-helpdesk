/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class is used to store the configuration of department selection.
 */
public final class DepartmentSelectionConfig implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5374546803789303257L;

	/**
	 * Primary key.
	 */
	private Long id;

	/**
	 * The user.
	 */
	private User user;

	/**
	 * The config itself.
	 */
	private String data;
	
	/**
	 * The date.
	 */
	private Timestamp date;

	/**
	 * Bean constructor.
	 */
	public DepartmentSelectionConfig() {
		super();
	}

	/**
	 * Constructor.
	 * @param user
	 * @param data
	 * @param date
	 */
	public DepartmentSelectionConfig(
			final User user, 
			final String data, 
			final Timestamp date) {
		super();
		this.user = user;
		this.data = data;
		this.date = date;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DepartmentSelectionConfig)) {
			return false;
		}
		return ((DepartmentSelectionConfig) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		if (getId() == null) {
			return 0;
		}
		return getId().intValue();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[" 
		+ ", id=[" + id + "]"
		+ ", date=[" + date + "]"
		+ ", data=" + data + ""
		+ ", user=" + user + ""
		+ "]";
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
	public void setDate(final Timestamp date) {
		this.date = date;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(final User user) {
		this.user = user;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(final String data) {
		this.data = data;
	}
	
}