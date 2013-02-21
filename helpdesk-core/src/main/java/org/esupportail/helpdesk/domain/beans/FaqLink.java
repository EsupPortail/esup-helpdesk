/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;

/**
 * The class that represents FAQ links.
 */
public class FaqLink implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8121714850708653347L;

	/**
	 * The primary key.
	 */
	private long id;

    /**
     * The FAQ container.
     * @deprecated
     */
	@SuppressWarnings("deprecation")
	@Deprecated
    private DeprecatedFaqContainer deprecatedFaqContainer;

    /**
     * The FAQ entry.
     * @deprecated
     */
	@SuppressWarnings("deprecation")
	@Deprecated
    private DeprecatedFaqEntry deprecatedFaqEntry;
    
    /**
     * The FAQ.
     */
    private Faq faq;
    
    /**
     * The department.
     */
    private Department department;
    
    /**
     * The category.
     */
    private Category category;
    
    /**
     * The order.
     */
    private Integer order;

	/**
     * Bean constructor.
     */
    protected FaqLink() {
    	super();
    }

	/**
     * Bean constructor.
	 * @param department 
	 * @param category 
	 * @param faq
     */
    protected FaqLink(
    		final Department department,
    		final Category category,
    		final Faq faq) {
    	super();
    	this.department = department;
    	this.category = category;
    	this.faq = faq;
    }

	/**
     * Bean constructor.
	 * @param department 
	 * @param faq
     */
    public FaqLink(
    		final Department department,
    		final Faq faq) {
    	this(department, null, faq);
    }

	/**
     * Bean constructor.
	 * @param category 
	 * @param faq
     */
    public FaqLink(
    		final Category category,
    		final Faq faq) {
    	this(null, category, faq);
    }

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FaqLink)) {
			return false;
		}
		return ((FaqLink) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "["  
		+ "id=[" + id + "]"
		+ ", department=[" + department + "]"
		+ ", category=[" + category + "]"
		+ ", faq=[" + faq + "]"
		+ "]";
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
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public DeprecatedFaqContainer getFaqContainer() {
		return deprecatedFaqContainer;
	}

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setFaqContainer(final DeprecatedFaqContainer x) {
		this.deprecatedFaqContainer = x;
	}

	/**
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public DeprecatedFaqEntry getFaqEntry() {
		return deprecatedFaqEntry;
	}

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setFaqEntry(final DeprecatedFaqEntry x) {
		this.deprecatedFaqEntry = x;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(final Department department) {
		this.department = department;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(final Category category) {
		this.category = category;
	}

	/**
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(final Integer order) {
		this.order = order;
	}

	/**
	 * @return the faq
	 */
	public Faq getFaq() {
		return faq;
	}

	/**
	 * @param faq the faq to set
	 */
	public void setFaq(final Faq faq) {
		this.faq = faq;
	}

	/**
	 * @return the deprecatedFaqContainer
     * @deprecated
     */
	@SuppressWarnings("deprecation")
	@Deprecated
	public DeprecatedFaqContainer getDeprecatedFaqContainer() {
		return deprecatedFaqContainer;
	}

	/**
	 * @param deprecatedFaqContainer the deprecatedFaqContainer to set
     * @deprecated
     */
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setDeprecatedFaqContainer(
			final DeprecatedFaqContainer deprecatedFaqContainer) {
		this.deprecatedFaqContainer = deprecatedFaqContainer;
	}

	/**
	 * @return the deprecatedFaqEntry
     * @deprecated
     */
	@SuppressWarnings("deprecation")
	@Deprecated
	public DeprecatedFaqEntry getDeprecatedFaqEntry() {
		return deprecatedFaqEntry;
	}

	/**
	 * @param deprecatedFaqEntry the deprecatedFaqEntry to set
     * @deprecated
     */
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setDeprecatedFaqEntry(final DeprecatedFaqEntry deprecatedFaqEntry) {
		this.deprecatedFaqEntry = deprecatedFaqEntry;
	}

}
