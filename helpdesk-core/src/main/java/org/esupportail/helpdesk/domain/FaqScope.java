/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;


/**
 * Constants for FAQ scopes.
 */
public abstract class FaqScope {

	/** FAQ scope. */
	public static final String DEFAULT = "DEFAULT";
	/** FAQ scope. */
	public static final String ALL = "ALL";
	/** FAQ scope. */
	public static final String AUTHENTICATED = "AUTHENTICATED";
	/** FAQ scope. */
	public static final String DEPARTMENT = "DEPARTMENT";
	/** FAQ scope. */
	public static final String MANAGER = "MANAGER";
	/** FAQ scope. */
	public static final String ADMIN = "ADMIN";

	/**
	 * Constructor.
	 */
	private FaqScope() {
		// TODO Auto-generated constructor stub
	}
	
}
