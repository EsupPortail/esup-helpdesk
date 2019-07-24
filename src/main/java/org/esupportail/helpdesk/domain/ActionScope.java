/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;


/**
 * Constants for action scopes.
 */
public abstract class ActionScope {

	/** Action scope value. */
	public static final String DEFAULT = "DEFAULT";
	/** Action scope value. */
	public static final String INVITED = "INVITED";
	/** Action scope value. */
	public static final String INVITED_MANAGER = "INVITED_MANAGER";
	/** Action scope value. */
	public static final String OWNER = "OWNER";
	/** Action scope value. */
	public static final String MANAGER = "MANAGER";
	/** Action scope value. */
	public static final String PUBLIC = "PUBLIC";
	
	/**
	 * Constructor.
	 */
	private ActionScope() {
		//
	}
	
}
