/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The interface of CAS user managers.
 */
public interface CasUserManager extends UserManager {

	/**
	 * The prefix of CAS users' id.
	 */
	String USER_ID_PREFIX = "cas-";

	/**
	 * Set the information of a user from a LDAP user, used at creation and on each connection.
	 * @param user
	 * @return true if the user should be saved.
	 */
	boolean setUserInfo(
			User user);

	/**
	 * @param realId
	 * @return the newly created user.
	 * @throws UserNotFoundException
	 */
	User createUser(
			String realId) throws UserNotFoundException;

	/**
	 * @param email
	 * @return the user id with the given email or null.
	 */
	String getUserIdWithEmail(String email);

}
