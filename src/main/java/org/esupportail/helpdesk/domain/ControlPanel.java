/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain; 

/** 
 * An interface to declare the constants used by the control panel.
 */ 
public abstract class ControlPanel {
	
	/** A constant for the status filter. */
	public static final String STATUS_FILTER_ANY = "ANY";
	/** A constant for the status filter. */
	public static final String STATUS_FILTER_OPENED = "OPENED";
	/** A constant for the status filter. */
	public static final String STATUS_FILTER_CLOSED = "CLOSED";
	
	/** A constant for the involvement filter. */
	public static final String MANAGER_INVOLVEMENT_FILTER_ANY = "ANY";
	/** A constant for the involvement filter. */
	public static final String MANAGER_INVOLVEMENT_FILTER_FREE = "FREE";
	/** A constant for the involvement filter. */
	public static final String MANAGER_INVOLVEMENT_FILTER_MANAGED = "MANAGED";
	/** A constant for the involvement filter. */
	public static final String MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_FREE = "MANAGED_OR_FREE";
	/** A constant for the involvement filter. */
	public static final String MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_INVITED = "MANAGED_OR_INVITED";
	/** A constant for the involvement filter. */
	public static final String MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE = "MANAGED_INVITED_OR_FREE";
	
	/** A constant for the involvement filter. */
	public static final String USER_INVOLVEMENT_FILTER_ANY = "ANY";
	/** A constant for the involvement filter. */
	public static final String USER_INVOLVEMENT_FILTER_OWNER = "OWNER";
	/** A constant for the involvement filter. */
	public static final String USER_INVOLVEMENT_FILTER_INVITED = "INVITED";
	/** A constant for the involvement filter. */
	public static final String USER_INVOLVEMENT_FILTER_OWNER_OR_INVITED = "OWNER_OR_INVITED";
	/** A constant for the involvement filter. */
	public static final String USER_INVOLVEMENT_FILTER_MONITORING = "MONITORING";
	
	/**
	 * Constructor.
	 */
	private ControlPanel() {
		//
	}
	
}
