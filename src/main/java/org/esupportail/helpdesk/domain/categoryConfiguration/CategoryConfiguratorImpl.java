/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.categoryConfiguration;

import org.esupportail.helpdesk.domain.beans.Category;


/**
 * A basic implementation of CategoryConfigurator.
 */
public class CategoryConfiguratorImpl implements CategoryConfigurator {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1933449338488863891L;

	/**
     * True if new categories inherit mambers.
     */
    private boolean inheritMembers;

    /**
     * Bean constructor.
     */
    public CategoryConfiguratorImpl() {
		super();
		this.inheritMembers = true;
	}
    
    /**
     * @see org.esupportail.helpdesk.domain.categoryConfiguration.CategoryConfigurator#configure(
     * org.esupportail.helpdesk.domain.beans.Category)
     */
    @Override
	public void configure(final Category category) {
    	category.setInheritMembers(inheritMembers);
    }

	/**
	 * @return the inheritMembers
	 */
	protected boolean isInheritMembers() {
		return inheritMembers;
	}

	/**
	 * @param inheritMembers the inheritMembers to set
	 */
	public void setInheritMembers(final boolean inheritMembers) {
		this.inheritMembers = inheritMembers;
	}

}
