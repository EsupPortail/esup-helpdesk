/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.util.List;

import org.esupportail.commons.exceptions.ObjectNotFoundException;

/**
 * The interface of LDAP services for any entity.
 */
public interface LdapEntityService extends BasicLdapService {

	/**
	 * Search an entity in the LDAP directory from a unique identifier.
	 * @param id the identifier
	 * @return the LdapEntity that corresponds to the given id.
	 * @throws LdapException 
	 * @throws ObjectNotFoundException
	 */
	LdapEntity getLdapEntity(String id) throws LdapException, ObjectNotFoundException;

	/**
	 * Tell if an entity matches a filter.
	 * @param id the entity's unique identifier in the LDAP directory
	 * @param filter the filter
	 * @return true if the entity matches the filter.
	 * @throws LdapException
	 */
	boolean entityMatchesFilter(String id, String filter) throws LdapException;

	/**
	 * @param filterExpr
	 * @return The list of LdapEntity that corresponds to a filter.
	 * @throws LdapException 
	 */
	List<LdapEntity> getLdapEntitiesFromFilter(String filterExpr) throws LdapException;
	
}
