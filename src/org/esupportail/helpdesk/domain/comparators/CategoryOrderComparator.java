/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.comparators;

import java.util.Comparator;

import org.esupportail.helpdesk.domain.beans.Category;

/**
 * The class to sort categories by order.
 */
public class CategoryOrderComparator implements Comparator<Category> {

	/**
	 * Constructor.
	 */
	public CategoryOrderComparator() {
		super();
	}

	/**
	 * @param c1 
	 * @param c2 
	 * @return an int
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(
			final Category c1, 
			final Category c2) {
		return c1.getOrder().compareTo(c2.getOrder());
	}

}
