/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed;

import java.io.Serializable;

/**
 * The interface of email readers.
 */
public interface AccountReader extends Serializable {
	
	/**
	 * @return true if the reader is enabled.
	 */
	boolean isEnabled();
	
	/**
	 * Read an email account.
	 * @param errorHolder
	 * @return true if the database should be committed.
	 */
	boolean read(ErrorHolder errorHolder);
	
}