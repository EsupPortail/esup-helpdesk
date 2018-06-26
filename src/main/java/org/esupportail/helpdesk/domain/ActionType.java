/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;


/**
 * Constants for FAQ scopes.
 */
public abstract class ActionType {

	/** Action type value. */
    public static final String CREATE = "CREATE";
	/** Action type value. */
    public static final String FREE = "FREE";
	/** Action type value. */
    public static final String TAKE = "TAKE";
	/** Action type value. */
    public static final String POSTPONE = "POSTPONE";
	/** Action type value. */
    public static final String CANCEL_POSTPONEMENT = "CANCEL_POSTPONEMENT";
	/** Action type value. */
    public static final String CANCEL = "CANCEL";
	/** Action type value. */
    public static final String REQUEST_INFORMATION = "REQUEST_INFORMATION";
	/** Action type value. */
    public static final String GIVE_INFORMATION = "GIVE_INFORMATION";
	/** Action type value. */
    public static final String DELETE_FILE_INFO = "DELETE_FILE_INFO";
	/** Action type value. */
    public static final String CLOSE = "CLOSE";
	/** Action type value. */
    public static final String APPROVE_CLOSURE = "APPROVE";
	/** Action type value. */
    public static final String CLOSE_APPROVE = "CLOSE_APPROVE";
	/** Action type value. */
    public static final String EXPIRE = "EXPIRE";
	/** Action type value. */
    public static final String REFUSE = "REFUSE";
	/** Action type value. */
    public static final String ASSIGN = "ASSIGN";
	/** Action type value. */
    public static final String CONNECT_TO_TICKET_V2 = "CONNECT_TO_TICKET"; 
	/** Action type value. */
    public static final String CONNECT_TO_TICKET_APPROVE_V2 = "CONNECT_TO_TICKET_APPROVE"; 
	/** Action type value. */
    public static final String CONNECT_TO_FAQ_V2 = "CONNECT_TO_FAQ"; 
	/** Action type value. */
    public static final String CONNECT_TO_FAQ_APPROVE_V2 = "CONNECT_TO_FAQ_APPROVE"; 
	/** Action type value. */
    public static final String CHANGE_LABEL = "CHANGE_LABEL";
	/** Action type value. */
    public static final String CHANGE_OWNER = "CHANGE_OWNER";
	/** Action type value. */
    public static final String CHANGE_PRIORITY = "CHANGE_PRIORITY";
	/** Action type value. */
    public static final String CHANGE_SCOPE = "CHANGE_SCOPE";
	/** Action type value. */
    public static final String CHANGE_ORIGIN = "CHANGE_ORIGIN";
	/** Action type value. */
    public static final String CHANGE_SPENT_TIME = "CHANGE_SPENT_TIME";
	/** Action type value. */
    public static final String CHANGE_COMPUTER = "CHANGE_COMPUTER";
	/** Action type value. */
    public static final String CHANGE_DEPARTMENT = "CHANGE_DEPARTMENT";
	/** Action type value. */
    public static final String CHANGE_CATEGORY = "CHANGE_CATEGORY";
	/** Action type value. */
    public static final String REFUSE_CLOSURE = "REFUSE_CLOSURE";
	/** Action type value. */
    public static final String REOPEN = "REOPEN";
    /** Action type value. */
    public static final String INVITE = "INVITE";
    /** Action type value. */
    public static final String REMOVE_INVITATION = "REMOVE_INVITATION";
    /** Action type value. */
    public static final String MONITORING_INVITE_V2 = "MONITORING_INVITE";
    /** Action type value. */
    public static final String UPLOAD = "UPLOAD";
    /** Action type value. */
    public static final String UNDEF = "?";
    
	/**
	 * Constructor.
	 */
	private ActionType() {
		//
	}
	
}
