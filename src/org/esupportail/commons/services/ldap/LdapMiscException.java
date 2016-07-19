/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

/**
 * This exception is used to wrap unexpected exceptions thrown by LDAP operations.
 */
public class LdapMiscException extends LdapException {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -5988861755477211527L;

	/**
	 * Bean constructor.
	 */
	public LdapMiscException() {
		super();
	}

	/**
	 * Bean constructor.
	 * @param message
	 */
	public LdapMiscException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	public LdapMiscException(final Exception cause) {
		super(cause);
	}

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	public LdapMiscException(final String message, final Exception cause) {
		super(message, cause);
	}

}
