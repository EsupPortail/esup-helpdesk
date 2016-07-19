/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.archiving; 

import java.io.Serializable;

/**
 * The interface of archivers.
 */
public interface Archiver extends Serializable {
	
	/**
	 * Archive obsolete tickets.
	 * @return true if the method should be called again.
	 */
	boolean archive();
	
	/**
	 * Unlock.
	 */
	void unlock();

}
