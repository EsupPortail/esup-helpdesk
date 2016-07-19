/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.controllers;

/**
 * This interface should be implemented by the beans that call the ldapSearch view. 
 */
public interface LdapSearchCaller {

	/**
	 * Set the LDAP uid.
	 * @param ldapUid 
	 */
	void setLdapUid(String ldapUid);

	/**
	 * @return the LDAP uid. 
	 */
	String getLdapUid();

}
