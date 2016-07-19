/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.cas;

import java.io.Serializable;


/** 
 * The interface of the CAS service used to retrieve PTs from the CAs server.
 */
public interface CasService extends Serializable {
	
	/**
	 * @param targetService The service the PT should be sent to.
	 * @return a PT.
	 * @throws CasException 
	 */
	String getProxyTicket(String targetService) throws CasException;
	
	/**
	 * validate the ticket (ST or PT) to get a PGT. This method is 
	 * automatically called when retrieving the first PT for a remote
	 * service, but it can be called manually to prevent from the
	 * peremption of the ticket passed to the application.  
	 * @throws CasException 
	 */
	void validate() throws CasException;
	
}
