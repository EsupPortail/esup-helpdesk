/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.io.Serializable;

/**
 * The interface of writeable LDAP user services.
 */
public interface WriteableLdapUserService extends Serializable {

	/** 
	 * Update a LDAP user in the LDAP directory.
	 * @param ldapUser
	 */
	void updateLdapUser(LdapUser ldapUser);
	
	/** 
	 * Create a LDAP user.
	 * @param ldapUser
	 */
	void createLdapUser(LdapUser ldapUser);
	
	/** 
	 * Delete a LDAP user.
	 * @param ldapUser
	 */
	void deleteLdapUser(LdapUser ldapUser);
	
	/** 
	 * Define credentials which will be used for LDAP binding.
	 * Usefull when you ask credentials via a forms in your application
	 * and you want to bind to LDAP with those credentials.
	 * @param userId
	 * @param password
	 */
	void setAuthenticatedContext(String userId, String password);
	
	/** 
	 * Disable WriteableLdapUserService write capabilities
	 * so that it can't write in LDAP directory in the sequel.
	 */
	void defineAnonymousContext();

}
