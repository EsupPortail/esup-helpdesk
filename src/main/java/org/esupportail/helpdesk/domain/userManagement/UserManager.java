/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.esupportail.helpdesk.domain.beans.User;

/**
 * The interface of user managers.
 */
public interface UserManager extends Serializable {

	/**
	 * @return the auth type
	 */
	String getAuthType();

	/**
	 * @param realId
	 * @return the database id that corresponds to a real id.
	 */
	String getDatabaseId(String realId);

	/**
	 * @param user
	 * @return the locale of a user.
	 */
	Locale getUserLocale(User user);

	/**
	 * @param user
	 * @return the email of a user, or null.
	 */
	String getUserEmail(User user);

	/**
	 * @param user
	 * @return the emails of a user, or null.
	 */
	List<String> getUserEmails(User user);

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
	 * Set a new auth secret for the user.
	 * @param user
	 * @param authCookieName
	 * @param authCookieExpiry
	 * @return the cookie to send.
	 */
	Cookie renewAuthSecret(
			User user,
			String authCookieName,
			int authCookieExpiry);

	/**
	 * remove the auth secret.
	 * @param user
	 * @param authCookieName
	 * @return the cookie to send.
	 */
	Cookie removeAuthSecret(
			User user,
			String authCookieName);

}
