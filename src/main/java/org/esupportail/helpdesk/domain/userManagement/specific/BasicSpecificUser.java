/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement.specific;

import java.io.Serializable;

import org.esupportail.commons.utils.Assert;
import org.springframework.beans.factory.InitializingBean;

/**
 * A basic specific user.
 */
public class BasicSpecificUser implements Serializable, InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7775751616916262009L;

	/**
	 * The id.
	 */
	private String id;

	/**
	 * The password.
	 */
	private String password;

	/**
	 * The displayName.
	 */
	private String displayName;

	/**
	 * The email.
	 */
	private String email;

	/**
	 * Constructor.
	 */
	public BasicSpecificUser() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.hasText(this.id,
				"property id of class " + this.getClass().getName()
				+ " can not be null");
		Assert.hasText(this.password,
				"property password of class " + this.getClass().getName()
				+ " can not be null");
		Assert.hasText(this.displayName,
				"property displayName of class " + this.getClass().getName()
				+ " can not be null");
		Assert.hasText(this.email,
				"property email of class " + this.getClass().getName()
				+ " can not be null");
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

}
