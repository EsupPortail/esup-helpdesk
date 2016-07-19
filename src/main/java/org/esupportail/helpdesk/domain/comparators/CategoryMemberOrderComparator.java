/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.comparators;

import java.util.Comparator;

import org.esupportail.helpdesk.domain.beans.CategoryMember;

/**
 * The class to sort category members by order.
 */
public class CategoryMemberOrderComparator implements Comparator<CategoryMember> {

	/**
	 * Constructor.
	 */
	public CategoryMemberOrderComparator() {
		super();
	}

	/**
	 * @param cm1 
	 * @param cm2 
	 * @return an int
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(
			final CategoryMember cm1, 
			final CategoryMember cm2) {
		return cm1.getOrder().compareTo(cm2.getOrder());
	}

}
