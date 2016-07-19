/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;


/**
 * The class representing the membership of a user in a category.
 */
public final class CategoryMember implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -1560990233405759239L;

	/**
	 * Primary key.
	 */
	private Long id;

	/**
	 * Represent the user in the user-category relation.
	 */
	private User user;	
	
	/**
	 * Represent the category in the user-category relation.
	 */
	private Category category;
    
	/**
     * The order of the member in the category.
     */
    private Integer order;
    
	/**
	 * Empty constructor, needed by Hibernate.
	 */
	public CategoryMember() {
		super();
	}

	/**
     * Constructor.
     * @param user the user to set
     * @param category the category to set
     */
    public CategoryMember(
			final User user, 
			final Category category) {
        this();
        this.user = user;
        this.category = category;
    }	

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CategoryMember)) {
			return false;
		}
		return ((CategoryMember) obj).getId() == getId();
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
	 * @return Returns the id.
	 */
	public Long getId() {
		return this.id;
	}
	
	/**
	 * @param id The id to set.
	 */
	public void setId(final Long id) {
		this.id = id;
	}
	
	/**
	 * @return Returns the user.
	 */
	public User getUser() {
		return this.user;
	}
	
	/**
	 * @param user The user to set.
	 */
	public void setUser(final User user) {
		this.user = user;
	}
	
	/**
	 * @return Returns the category.
	 */
	public Category getCategory() {
		return this.category;
	}
	
	/**
	 * @param category The category to set.
	 */
	public void setCategory(final Category category) {
		this.category = category;
	}
    
	/**
     * @return Returns the order.
     */
    public Integer getOrder() {
        return this.order;
    }

    /**
     * @param order The order to set.
     */
    public void setOrder(final Integer order) {
        this.order = order;
    }

}
