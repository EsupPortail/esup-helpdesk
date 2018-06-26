/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;


/**
 * An exception thrown when failing to bind to an LDAP directory
 * due to some wrong credentials.
 */
public class LdapBindFailedException extends LdapException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = -6304766264121185906L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected LdapBindFailedException(final String message, final Exception cause) {
		super(message, cause);
	}
	
	/**
	 * Bean constructor.
	 * @param message
	 */
	public LdapBindFailedException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	public LdapBindFailedException(final Exception cause) {
		super(cause);
	}
}
