/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import org.esupportail.commons.utils.BeanUtils;

/**
 * A class that provides utilities to access LDAP.
 */
public class LdapUtils {

	/**
	 * The name of the LdapUserService bean.
	 */
	private static final String LDAP_SERVICE_BEAN = "ldapUserService";

	/**
	 * No instanciation.
	 */
	private LdapUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the LDAP service.
	 */
	public static LdapUserService createLdapService() {
		return (LdapUserService) BeanUtils.getBean(LDAP_SERVICE_BEAN);
	}

}
