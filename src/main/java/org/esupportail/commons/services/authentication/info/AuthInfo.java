/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication.info;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/** 
 * The interface of authentication informations.
 */
public interface AuthInfo extends Serializable {
	
	/**
	 * @return the authenticated id.
	 */
	String getId();
	
	/**
	 * @return the authenticated type.
	 */
	String getType();
	
	/**
	 * @return the authenticated attributes.
	 */
	Map<String, List<String>> getAttributes();
	
}
