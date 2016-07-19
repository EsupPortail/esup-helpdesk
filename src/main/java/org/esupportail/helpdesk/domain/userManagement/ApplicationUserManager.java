/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement;

import java.util.Locale;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The interface of application user managers.
 */
public interface ApplicationUserManager extends UserManager {

	/**
	 * The prefix of application users' id.
	 */
	String USER_ID_PREFIX = "app-";

	/**
	 * Set the information of a user from a data source, used at creation and on each connection.
	 * @param user
	 * @return true if the user should be saved.
	 */
	boolean setUserInfo(
			User user);

	/**
	 * @param realId
	 * @param displayName
	 * @param locale
	 * @return the newly created user.
	 * @throws UserNotFoundException
	 */
	User createUser(
			String realId,
			String displayName,
			Locale locale);

}
