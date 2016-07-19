/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.comparators;

import java.util.Comparator;

import org.esupportail.helpdesk.domain.beans.DepartmentManager;

/**
 * The class to sort managers by display name.
 */
public class DepartmentManagerDisplayNameComparator implements Comparator<DepartmentManager> {

	/**
	 * Constructor.
	 */
	public DepartmentManagerDisplayNameComparator() {
		super();
	}

	/**
	 * @param m1 
	 * @param m2 
	 * @return an int
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(
			final DepartmentManager m1, 
			final DepartmentManager m2) {
		return m1.getUser().getDisplayName().compareTo(m2.getUser().getDisplayName());
	}

}
