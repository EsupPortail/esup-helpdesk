/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.comparators;

import java.util.Comparator;

import org.esupportail.helpdesk.domain.beans.CategoryMember;

/**
 * The class to sort category members by id.
 */
public class CategoryMemberIdComparator implements Comparator<CategoryMember> {

	/**
	 * Constructor.
	 */
	public CategoryMemberIdComparator() {
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
		return cm1.getUser().getId().compareTo(cm2.getUser().getId());
	}

}
