/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A LDAP entity, as returned by LDAP searches.
 */
public interface LdapEntity extends Serializable {

	/**
	 * @return the map of all the attributes.
	 */
	Map<String, List<String>> getAttributes();

	/**
	 * @param attributes the attributes to set
	 */
	void setAttributes(Map<String, List<String>> attributes);


	/**
	 * @return the id
	 */
	String getId();
	
	/**
	 * 
	 * @param id the id to set
	 */
	void setId(String id);
	
	/**
	 * @return the set of all the attribute names.
	 */
	List<String> getAttributeNames();
	
	/**
	 * @param name 
	 * @return the values for an attribute.
	 */
	List<String> getAttributes(String name);
	
	/**
	 * @param name 
	 * @return the value for an attribute.
	 */
	String getAttribute(String name);
	
}
