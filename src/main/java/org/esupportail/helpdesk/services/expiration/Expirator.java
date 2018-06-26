/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.expiration;

import java.io.Serializable;

/**
 * The interface of expirators.
 */
public interface Expirator extends Serializable {
	
	/**
	 * Expire non approved tickets.
	 * @param alerts 
	 * @return true if the method should be called again.
	 */
	boolean expire(boolean alerts);
	
	/**
	 * Unlock.
	 */
	void unlock();

	void afterPropertiesSet();

}
