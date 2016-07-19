/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A FAQ event, used to send reports. */
public final class FaqEvent implements Serializable {

	/** An update action. */
    public static final String UPDATE = "UPDATE";

    /** An update action. */
    public static final String CREATE = "CREATE";

    /** An update action. */
    public static final String DELETE = "DELETE";

    /** An update action. */
    public static final String MOVE_TO = "MOVE_TO";

    /** An update action. */
    public static final String MOVE_FROM = "MOVE_FROM";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5138181415262950015L;

	/**
     * Primary key.
     */
    private long id;
    
    /**
     * The action.
     */
    private String action;
    
    /**
     * The author.
     */
    private User author;

    /**
     * The label (for deletions).
     */
    private String label;

    /**
     * The department.
     */
    private Department department;
    
    /**
     * The from department.
     */
    private Department fromDepartment;
    
    /**
     * The to department.
     */
    private Department toDepartment;
    
    /**
     * The FAQ id.
     */
    private long faqId;

    /**
     * The date.
     */
    private Timestamp date;

	/**
	 * Constructor.
	 */
	public FaqEvent() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param action
	 * @param author
	 * @param faq
	 * @param department
	 * @param fromDepartment
	 * @param toDepartment
	 */
	protected FaqEvent(
			final String action, 
			final User author, 
			final Faq faq,
			final Department department, 
			final Department fromDepartment,
			final Department toDepartment) {
		super();
		this.action = action;
		this.author = author;
		this.label = faq.getLabel();
		this.department = department;
		this.fromDepartment = fromDepartment;
		this.toDepartment = toDepartment;
		this.faqId = faq.getId();
		this.date = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @param action
	 * @param author
	 * @param faq
	 * @return a simple event.
	 */
	protected static FaqEvent simple(
			final String action,
			final User author, 
			final Faq faq) {
		return new FaqEvent(action, author, faq, faq.getDepartment(), null, null);
	}

	/**
	 * @param author
	 * @param faq
	 * @return an update event.
	 */
	public static FaqEvent update(
			final User author, 
			final Faq faq) {
		return simple(UPDATE, author, faq);
	}

	/**
	 * @param author
	 * @param faq
	 * @return a create event.
	 */
	public static FaqEvent create(
			final User author, 
			final Faq faq) {
		return simple(CREATE, author, faq);
	}

	/**
	 * @param author
	 * @param faq
	 * @return a delete event.
	 */
	public static FaqEvent delete(
			final User author, 
			final Faq faq) {
		return simple(DELETE, author, faq);
	}

	/**
	 * @param author
	 * @param faq
	 * @param fromDepartment
	 * @return a moveTo event for a FAQ.
	 */
	public static FaqEvent moveTo(
			final User author, 
			final Faq faq,
			final Department fromDepartment) {
		return new FaqEvent(
				MOVE_TO, author, faq, fromDepartment, null, faq.getDepartment());
	}

	/**
	 * @param author
	 * @param faq
	 * @param fromDepartment
	 * @return a moveFrom event for a FAQ.
	 */
	public static FaqEvent moveFrom(
			final User author, 
			final Faq faq,
			final Department fromDepartment) {
		return new FaqEvent(
				MOVE_FROM, author, faq, faq.getDepartment(), fromDepartment, null);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FaqEvent)) {
			return false;
		}
		return ((FaqEvent) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
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
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(final String action) {
		this.action = action;
	}

	/**
	 * @return the author
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(final User author) {
		this.author = author;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
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
	 * @return the fromDepartment
	 */
	public Department getFromDepartment() {
		return fromDepartment;
	}

	/**
	 * @param fromDepartment the fromDepartment to set
	 */
	public void setFromDepartment(final Department fromDepartment) {
		this.fromDepartment = fromDepartment;
	}

	/**
	 * @return the toDepartment
	 */
	public Department getToDepartment() {
		return toDepartment;
	}

	/**
	 * @param toDepartment the toDepartment to set
	 */
	public void setToDepartment(final Department toDepartment) {
		this.toDepartment = toDepartment;
	}

	/**
	 * @return the date
	 */
	public Timestamp getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(final Timestamp date) {
		this.date = date;
	}

	/**
	 * @return the faqId
	 */
	public long getFaqId() {
		return faqId;
	}

	/**
	 * @param faqId the faqId to set
	 */
	public void setFaqId(final long faqId) {
		this.faqId = faqId;
	}

}
