/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.comparators;

import java.util.Comparator;

import org.esupportail.helpdesk.domain.beans.Department;

/**
 * The class to sort departments by xlabel.
 */
public class DepartmentXlabelComparator implements Comparator<Department> {

	/**
	 * Constructor.
	 */
	public DepartmentXlabelComparator() {
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
		return d1.getXlabel().compareTo(d2.getXlabel());
	}

}
