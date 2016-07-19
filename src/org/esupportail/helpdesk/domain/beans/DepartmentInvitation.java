/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;


/**
 * A class to store department invitations.
 */
public class DepartmentInvitation implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5886149273756093523L;

	/**
	 * Primary key.
	 */
	private Long id;

	/**
	 * The department.
	 */
	private Department department;

	/**
	 * The user.
	 */
	private User user;
	
	/**
	 * Constructor.
	 */
	private DepartmentInvitation() {
		super();
	}
	
    /**
     * Constructor.
     * @param user the user
     * @param department the department
     */
    public DepartmentInvitation(final User user, final Department department) {
        this();
        this.user = user;
        this.department = department;
    }
    
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DepartmentInvitation)) {
			return false;
		}
		return ((DepartmentInvitation) obj).getId() == getId();
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
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(final User user) {
		this.user = user;
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

}
