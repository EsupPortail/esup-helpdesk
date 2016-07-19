/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.comparators;

import java.util.Comparator;

import org.esupportail.helpdesk.domain.beans.Department;

/**
 * The class to sort departments by label.
 */
public class DepartmentLabelComparator implements Comparator<Department> {

	/**
	 * Constructor.
	 */
	public DepartmentLabelComparator() {
		super();
	}

	/**
	 * @param d1 
	 * @param d2 
	 * @return an int
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(
			final Department d1, 
			final Department d2) {
		return d1.getLabel().compareTo(d2.getLabel());
	}

}
