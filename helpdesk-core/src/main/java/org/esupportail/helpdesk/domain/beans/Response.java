/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;

import org.esupportail.commons.utils.strings.StringUtils;

/**
 * The representation of a canned response. */
public class Response implements Serializable {

	/**
	 * The serialization id.
	 */
	public static final String SIGNATURE_TOKEN = "@SIGNATURE@";
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7984824891894428153L;
	
	/**
	 * The unique id.
	 */
	private long id;

	/**
     * The user.
     */
    private User user;

	/**
     * The department.
     */
    private Department department;

	/**
     * The label.
     */
    private String label;

	/**
     * The message.
     */
    private String message;

	/**
	 * Default constructor.
	 */
	public Response() {
		super();
	}
	
	/**
	 * Clone.
	 * @param response
	 */
	public Response(
			final Response response) {
		this();
		this.id = response.id;
		this.user = response.user;
		this.department = response.department;
		this.label = response.label;
		this.message = response.message;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Response)) {
			return false;
		}
		return ((Response) obj).getId() == getId();
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
		+ ", user=" + user + "" 
		+ ", department=" + department + "" 
		+ ", label=[" + label + "]"
		+ "]";
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(final String message) {
		this.message = StringUtils.nullIfEmpty(message);
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
	 * @return true if a department response.
	 */
	public boolean isDepartmentResponse() {
		return department != null;
	}

	/**
	 * @return true if a user response.
	 */
	public boolean isUserResponse() {
		return user != null;
	}

	/**
	 * @return true if a global response.
	 */
	public boolean isGlobalResponse() {
		return !isUserResponse() && !isDepartmentResponse();
	}

}
