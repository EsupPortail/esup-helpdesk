package org.esupportail.helpdesk.services.authentication;

import org.esupportail.helpdesk.domain.beans.User;

/**
 * The interface of authenticators.
 */
public interface Authenticator {

	/**
	 * @return the authenticated user.
	 */
	User getUser();

	/**
	 * Remove the authenticated user from the session.
	 */
	void unsetUser();

	/**
	 * Store the authenticated application user to the session.
	 * @param user
	 */
	void setApplicationUser(User user);

}