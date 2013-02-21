package org.esupportail.helpdesk.domain.reporting;

import java.util.Locale;

import org.esupportail.helpdesk.domain.beans.User;

/**
 * The interface of password senders.
 */
public interface PasswordSender {

	/**
	 * @param user
	 * Send a password email to a user.
	 * @param locale
	 */
	void sendPasswordEmail(User user, Locale locale);

}