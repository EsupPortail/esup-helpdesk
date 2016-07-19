/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.esupportail.commons.exceptions.GroupNotFoundException;
import org.esupportail.commons.exceptions.UserNotFoundException;

/**
 * The interface of LDAP user services.
 */
public interface LdapUserAndGroupService extends Serializable {

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
	List<String> getUserSearchDisplayedAttributes();

	/**
	 * @param filterExpr
	 * @return null if the user filter is correct, an error message otherwise.
	 * @throws LdapException 
	 */
	String testUserLdapFilter(String filterExpr) throws LdapException;
	
	/**
	 * @return the user statistics, as a list of Strings.
	 * @param locale
	 */
	List<String> getUserStatistics(Locale locale);
	
	/**
	 * @return true if the class supports the retrieval of statictics for users. 
	 * If false, calls to methods getUserStatistics() and resetUserStatistics() 
	 * should throw an exception.
	 */
	boolean supportUserStatistics();
	
	/**
	 * Reset the user statistics.
	 */
	void resetUserStatistics();
	
	/**
	 * @return true if the class supports testing for users. If false, calls to method 
	 * userTest() should throw an exception.
	 */
	boolean supportsUserTest();
	
	/**
	 * Test the LDAP connection for users.
	 */
	void userTest();
	
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
	 * @param group 
	 * @return the ids of the members of a group (no LDAP request done).
	 */
	List<String> getMemberIds(LdapGroup group);
	
	/**
	 * @param group 
	 * @return the members of a group (a LDAP request for each member).
	 */
	List<LdapUser> getMembers(LdapGroup group);
	
	/**
	 * @return the attributes to display when searching for groups.
	 */
	List<String> getGroupSearchDisplayedAttributes();

	/**
	 * @param filterExpr
	 * @return null if the group filter is correct, an error message otherwise.
	 * @throws LdapException 
	 */
	String testGroupLdapFilter(String filterExpr) throws LdapException;
	
	/**
	 * @return the group statistics, as a list of Strings.
	 * @param locale
	 */
	List<String> getGroupStatistics(Locale locale);
	
	/**
	 * @return true if the class supports the retrieval of statictics for groups. 
	 * If false, calls to methods getGroupStatistics() and resetGroupStatistics() 
	 * should throw an exception.
	 */
	boolean supportGroupStatistics();
	
	/**
	 * Reset the group statistics.
	 */
	void resetGroupStatistics();
	
	/**
	 * @return true if the class supports testing for groups. If false, calls to method 
	 * groupTest() should throw an exception.
	 */
	boolean supportsGroupTest();
	
	/**
	 * Test the LDAP connection for groups.
	 */
	void groupTest();
	
}
