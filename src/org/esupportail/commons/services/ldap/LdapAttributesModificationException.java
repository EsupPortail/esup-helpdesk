/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;


/**
 * An exception thrown when failing to retrieve a user from a datasource.
 */
public class LdapAttributesModificationException extends LdapException {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -810284543160833628L;

	/**
	 * Bean constructor.
	 * @param message
	 * @param cause
	 */
	protected LdapAttributesModificationException(final String message, final Exception cause) {
		super(message, cause);
	}
	
	/**
	 * Bean constructor.
	 * @param message
	 */
	public LdapAttributesModificationException(final String message) {
		super(message);
	}
	
	/**
	 * Bean constructor.
	 * @param cause
	 */
	public LdapAttributesModificationException(final Exception cause) {
		super(cause);
	}
}
