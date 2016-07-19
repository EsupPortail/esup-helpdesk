/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.io.Serializable;

import org.esupportail.commons.utils.Assert;
import org.springframework.beans.factory.InitializingBean;

/**
 *  Store LDAP attributes names.
 *  This class provide a mapping between LDAP attribute name and java variable.
 *  It can be initialized via Spring context
 * TODO add a List of String field for the attribute names.
 */
public class LdapSchema implements InitializingBean, Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2183997962850760918L;

	/**
	 * the displayName attribute.
	 */
	private String displayName;
	
	/**
	 * the birthdate attribute.
	 */
	private String birthdate;
	
	/**
	 * the birthdateFormat attribute.
	 */
	private String birthdateFormat;
	
	/**
	 * the uid attribute.
	 */
	private String uid;
	
	/**
	 * the employeeId attribute.
	 */
	private String employeeId;
	
	/**
	 * the cn attribute.
	 */
	private String cn;
	
	/**
	 * the birthName attribute.
	 */
	private String birthName;
	
	/**
	 * the password attribute.
	 */
	private String password;
	
	/**
	 * the shadowLastChange attribute.
	 */
	private String shadowLastChange;
	
	/**
	 * the mail attribute.
	 */
	private String mail;

	/**
	 * Bean constructor.
	 */
	public LdapSchema() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.displayName, 
				"property displayName of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.birthdate, 
				"property birthdate of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.birthdateFormat, 
				"property birthdateFormat of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.uid, 
				"property uid of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.employeeId, 
				"property employeeId of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.cn, 
				"property cn of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.birthName, 
				"property sn of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.password, 
				"property password of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.shadowLastChange, 
				"property shadowLastChange of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.mail, 
				"property mail of class " + this.getClass().getName() + " can not be null");
	}
	
	/**
	 * @return Returns the birthdate.
	 */
	public String getBirthdate() {
		return birthdate;
	}

	/**
	 * @param birthdate The birthdate to set.
	 */
	public void setBirthdate(final String birthdate) {
		this.birthdate = birthdate;
	}
	
	/**
	 * @return Returns the birthdateFormat.
	 */
	public String getBirthdateFormat() {
		return birthdateFormat;
	}

	/**
	 * @param birthdateFormat The birthdate format to set.
	 */
	public void setBirthdateFormat(final String birthdateFormat) {
		this.birthdateFormat = birthdateFormat;
	}

	/**
	 * @return Returns the cn.
	 */
	public String getCn() {
		return cn;
	}

	/**
	 * @param cn The cn to set.
	 */
	public void setCn(final String cn) {
		this.cn = cn;
	}

	/**
	 * @return Returns the sn.
	 */
	public String getBirthName() {
		return birthName;
	}

	/**
	 * @param sn The sn to set.
	 */
	public void setBirthName(final String sn) {
		this.birthName = sn;
	}


	/**
	 * @return Returns the supannEmpId.
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId The employeeId to set.
	 */
	public void setEmployeeId(final String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return Returns the uid.
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid The uid to set.
	 */
	public void setUid(final String uid) {
		this.uid = uid;
	}
	
	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
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

	/**
	 * @return the shadowLastChange
	 */
	public String getShadowLastChange() {
		return shadowLastChange;
	}

	/**
	 * @param shadowLastChange the shadowLastChange to set
	 */
	public void setShadowLastChange(final String shadowLastChange) {
		this.shadowLastChange = shadowLastChange;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(final String mail) {
		this.mail = mail;
	}

}
