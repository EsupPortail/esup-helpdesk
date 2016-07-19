/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

/**
 * An exception thrown when failing to retrieve a user from a datasource.
 */
public class InvalidLdapAccountException extends LdapException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 8231331889551464620L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected InvalidLdapAccountException(final String message, final Exception cause) {
		super(message, cause);
	}
	
	/**
	 * Bean constructor.
	 * @param message
	 */
	public InvalidLdapAccountException(final String message) {
		super(message);
	}
	
	/**
	 * Bean constructor.
	 * @param cause
	 */
	public InvalidLdapAccountException(final Exception cause) {
		super(cause);
	}
	
}
