/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed.imap.spam;

import java.io.Serializable;
import java.util.Set;

import javax.mail.Address;

import org.esupportail.helpdesk.domain.beans.User;


/**
 * The interface of spam filters.
 */
public interface SpamFilter extends Serializable {

	/**
	 * @return True if the filter effectvely filters.
	 */
	boolean filters();

	/**
	 * @param sender
	 * @param recipients
	 * @param subject
	 * @param contentType
	 * @param data
	 * @return True if the message should be considered as spam, false otherwise.
	 */
	boolean isSpam(
			User sender, 
			Set<Address> recipients, 
			String subject,
			String contentType, 
			byte[] data);

}