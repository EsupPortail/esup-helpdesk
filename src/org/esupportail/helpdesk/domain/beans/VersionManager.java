/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;
 

import org.esupportail.commons.domain.beans.AbstractVersionManager;

/**
 * A class to store the version number in the database.
 */
public class VersionManager extends AbstractVersionManager {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3189041357759453283L;

	/**
	 * Bean constructor.
	 */
	public VersionManager() {
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
		if (!(obj instanceof VersionManager)) {
			return false;
		}
		return ((VersionManager) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

}
