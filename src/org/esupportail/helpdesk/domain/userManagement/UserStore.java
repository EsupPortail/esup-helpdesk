/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.Cookie;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The interface of user stores.
 */
public interface UserStore extends Serializable {

	/**
	 * @return true if application auth allowed
	 */
	boolean isApplicationAuthAllowed();

	/**
	 * @return true if CAS auth allowed
	 */
	boolean isCasAuthAllowed();

	/**
	 * @return true if Shibboleth auth allowed
	 */
	boolean isShibbolethAuthAllowed();

	/**
	 * @return true if specific auth allowed
	 */
	boolean isSpecificAuthAllowed();

	/**
	 * @param user
	 * @return true if an application user.
	 */
	boolean isApplicationUser(User user);

	/**
	 * @param user
	 * @return true if a CAS user.
	 */
	boolean isCasUser(User user);

	/**
	 * @param user
	 * @return true if a Shibboleth user.
	 */
	boolean isShibbolethUser(User user);

	/**
	 * @param user
	 * @return true if a specific user.
	 */
	boolean isSpecificUser(User user);

	/**
	 * @param realId
	 * @return the id of an application user from its real id.
	 */
	String getApplicationUserId(String realId);

	/**
	 * @param realId
	 * @return the id of a CAS user from its real id.
	 */
	String getCasUserId(String realId);

	/**
	 * @param id
	 * @return the existing user with the corresponding id.
	 * @throws UserNotFoundException
	 */
	public User getExistingUserFromId(String id);

	/**
	 * @param user
	 * @return the locale of a user.
	 */
	Locale getUserLocale(User user);

	/**
	 * @param user
	 * @return the internet address of a user.
	 */
	InternetAddress getUserInternetAddress(final User user);

	/**
	 * @param user
	 * @return the emails of a user, or null.
	 */
	List<String> getUserEmails(User user);

	/**
	 * @param email
	 * @param password
	 * @return the authenticated user, or null
	 */
	User authenticateApplicationUser(
			String email,
			String password);

	/**
	 * @param realId
	 * @return the application user with the given realId
	 * @throws UserNotFoundException
	 */
	User getExistingApplicationUser(
			String realId) throws UserNotFoundException;

	/**
	 * @param realId
	 * @param updateInfo
	 * @return the CAS user.
	 * @throws UserNotFoundException
	 */
	User getOrCreateCasUser(
			String realId,
			boolean updateInfo) throws UserNotFoundException;

	/**
	 * @param realId
	 * @param attributes
	 * @return the Shibboleth user.
	 */
	User getOrCreateShibolethUser(
			String realId,
			Map<String, List<String>> attributes);

	/**
	 * @param realId
	 * @param updateInfo
	 * @return the specific user with the given realId
	 * @throws UserNotFoundException
	 */
	User getOrCreateSpecificUser(
			String realId,
			boolean updateInfo) throws UserNotFoundException;

	/**
	 * @param id
	 * @param password
	 * @return the authenticated user, or null
	 */
	User authenticateSpecificUser(
			String id,
			String password);

	/**
	 * Set the information of an application user from a data source, used at creation and on each connection.
	 * @param user
	 * @return true if the user should be saved.
	 */
	boolean setApplicationUserInfo(
			User user);

	/**
	 * @param realId
	 * @param displayName
	 * @param locale
	 * @return the newly created application user.
	 * @throws UserNotFoundException
	 */
	User createApplicationUser(
			String realId,
			String displayName,
			Locale locale);

	/**
	 * @param realId
	 * @return the User that corresponds to a (real) id.
	 * @throws UserNotFoundException
	 */
	User getUserFromRealId(
			String realId) throws UserNotFoundException;

	/**
	 * @param user
	 * @return the LDAP attributes of a user.
	 */
	Map<String, List<String>> getLdapAttributes(User user);

	/**
	 * @param user
	 * @return the portal attributes of a user.
	 */
	Map<String, List<String>> getPortalAttributes(User user);

	/**
	 * @param user
	 * @param groupId
	 * @return true if the user is a member of the group with id groupId.
	 */
	boolean isMemberOfPortalGroup(User user, String groupId);

	/**
	 * @param user
	 * @param groupName
	 * @return true if the user is a member of the group with name groupName.
	 */
	boolean isMemberOfPortalDistinguishedGroup(User user, String groupName);

	/**
	 * @param email
	 * @return the user with the given email.
	 */
	User getUserWithEmail(String email);

	/**
	 * @return the authCookieName
	 */
	String getAuthCookieName();

	/**
	 * @param authSecret
	 * @return the user with the given auth secret.
	 */
	User getUserWithAuthSecret(String authSecret);

	/**
	 * Set a new auth secret for a user.
	 * @param user
	 * @return the cookie to send.
	 */
	Cookie renewAuthSecret(User user);

	/**
	 * remove the auth secret of a user.
	 * @param user
	 * @return the cookie to send.
	 */
	Cookie removeAuthSecret(User user);

}
