/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement;

import java.util.List;
import java.util.Map;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The interface of Shibboleth user managers.
 */
public interface ShibbolethUserManager extends UserManager {

	/**
	 * The prefix of Shibboleth users' id.
	 */
	String USER_ID_PREFIX = "shib-";

	/**
	 * Set the information of a user from attributes, used at creation and on each connection.
	 * @param user
	 * @param attributes
	 * @return true if the user should be saved.
	 */
	boolean setUserInfo(
			User user,
			Map<String, List<String>> attributes);

	/**
	 * @param realId
	 * @param attributes
	 * @return the newly created user.
	 * @throws UserNotFoundException
	 */
	User createUser(
			String realId,
			Map<String, List<String>> attributes);

	/**
	 * @param email
	 * @return the user with the given email or null.
	 */
	User getUserWithEmail(String email);

}
