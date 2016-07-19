/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.util.List;

import org.esupportail.commons.exceptions.GroupNotFoundException;

/**
 * The interface of LDAP group services.
 */
public interface LdapGroupService extends BasicLdapService {

	/**
	 * Search a group in the LDAP directory from a unique identifier.
	 * @param id the identifier
	 * @return the LdapGroup that corresponds to the given id.
	 * @throws LdapException 
	 * @throws GroupNotFoundException
	 */
	LdapGroup getLdapGroup(String id) throws LdapException, GroupNotFoundException;

	/**
	 * Tell if a group matches a filter.
	 * @param id the group's unique identifier in the LDAP directory
	 * @param filter the filter
	 * @return true if the group matches the filter.
	 * @throws LdapException
	 */
	boolean groupMatchesFilter(String id, String filter) throws LdapException;

	/**
	 * @param token
	 * @return The list of LdapGroup that corresponds to a token.
	 * @throws LdapException 
	 */
	List<LdapGroup> getLdapGroupsFromToken(String token) throws LdapException;
	
	/**
	 * @param filterExpr
	 * @return The list of LdapGroup that corresponds to a filter.
	 * @throws LdapException 
	 */
	List<LdapGroup> getLdapGroupsFromFilter(String filterExpr) throws LdapException;
	
	/**
	 * @return the attributes to display when searching for groups.
	 */
	List<String> getSearchDisplayedAttributes();

}
