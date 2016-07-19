/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.recall; 

import java.io.Serializable;

/**
 * The interface of recallers.
 */
public interface Recaller extends Serializable {
	
	/**
	 * Recall postponed tickets.
	 */
	void recall();
	
	/**
	 * Unlock.
	 */
	void unlock();

}
