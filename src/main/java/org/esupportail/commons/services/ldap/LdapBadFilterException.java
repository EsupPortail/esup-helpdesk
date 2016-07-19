/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

/**
 * This exception is thrown when using a bad LDAP filter.
 */
public class LdapBadFilterException extends LdapException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 9107125141399019396L;

	/**
	 * Bean constructor.
	 */
	public LdapBadFilterException() {
		super();
	}

	/**
	 * Bean constructor.
	 * @param message
	 */
	public LdapBadFilterException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	public LdapBadFilterException(final Exception cause) {
		super(cause);
	}

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	public LdapBadFilterException(final String message, final Exception cause) {
		super(message, cause);
	}

}
