/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement.specific;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.userManagement.UserManager;

/**
 * The interface of specific user managers.
 */
public interface SpecificUserManager extends UserManager {

	/**
	 * Set the information of a user, used at creation and on each connection.
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
			String realId);

	/**
	 * @param email
	 * @return the id of the user with the given email or null.
	 */
	String getUserIdWithEmail(String email);

	/**
	 * @param id
	 * @param password
	 * @return true if successful
	 */
	boolean authenticate(
			String id,
			String password);

}
