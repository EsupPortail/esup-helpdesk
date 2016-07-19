/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.util.List;

import org.esupportail.commons.exceptions.UserNotFoundException;

/**
 * The interface of LDAP user services.
 */
public interface LdapUserService extends BasicLdapService {

	/**
	 * Search a user in the LDAP directory from a unique identifier.
	 * @param id the identifier
	 * @return the LdapUser that corresponds to the given id.
	 * @throws LdapException 
	 * @throws UserNotFoundException
	 */
	LdapUser getLdapUser(String id) throws LdapException, UserNotFoundException;

	/**
	 * Tell if a user matches a filter.
	 * @param id the user's unique identifier in the LDAP directory
	 * @param filter the filter
	 * @return true if the user matches the filter.
	 * @throws LdapException
	 */
	boolean userMatchesFilter(String id, String filter) throws LdapException;

	/**
	 * @param token
	 * @return The list of LdapUser that corresponds to a token.
	 * @throws LdapException 
	 */
	List<LdapUser> getLdapUsersFromToken(String token) throws LdapException;
	
	/**
	 * @param filterExpr
	 * @return The list of LdapUser that corresponds to a filter.
	 * @throws LdapException 
	 */
	List<LdapUser> getLdapUsersFromFilter(String filterExpr) throws LdapException;
	
	/**
	 * @return the attributes to display when searching for users.
	 */
	List<String> getSearchDisplayedAttributes();

}
