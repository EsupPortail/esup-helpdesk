/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import org.esupportail.commons.exceptions.EsupException;

/**
 * An abstract class for exceptions while performing LDAP operations.
 */
@SuppressWarnings("serial")
public abstract class LdapException extends EsupException {

	/**
	 * Bean constructor.
	 */
	protected LdapException() {
		super();
	}

	/**
	 * Bean constructor.
	 * @param message
	 */
	protected LdapException(final String message) {
		super(message);
	}

	/**
	 * Bean constructor.
	 * @param cause
	 */
	protected LdapException(final Exception cause) {
		super(cause);
	}

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected LdapException(final String message, final Exception cause) {
		super(message, cause);
	}

}
