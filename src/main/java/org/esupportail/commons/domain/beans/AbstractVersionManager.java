/**
 * ESUP-Portail Commons - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-commons
 */
package org.esupportail.commons.domain.beans;

import java.io.Serializable;

import org.esupportail.commons.utils.strings.StringUtils;

/**
 * An abstract class to store the version number in the database.
 */
@SuppressWarnings("serial")
public abstract class AbstractVersionManager implements Serializable {

	/**
	 * for Hibernate purposes only.
	 */
	private long id;

	/**
	 * The version number.
	 */
	private String version;

	/**
	 *
	 */
	protected AbstractVersionManager() {
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
		if (!(obj instanceof AbstractVersionManager)) {
			return false;
		}
		return ((AbstractVersionManager) obj).getId() == id;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[id=[" + id + "], version=[" + version + "]]";
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
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(final String version) {
		this.version = StringUtils.nullIfEmpty(version);
	}

}
