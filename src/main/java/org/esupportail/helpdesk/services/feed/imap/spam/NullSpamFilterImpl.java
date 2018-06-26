/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed.imap.spam;

import java.util.Set;

import javax.mail.Address;

import org.esupportail.helpdesk.domain.beans.User;

/**
 * A null SpamFilter implementation.
 */
public class NullSpamFilterImpl implements SpamFilter {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6291600085817369568L;

	/**
	 * Constructor.
	 */
	public NullSpamFilterImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.spam.SpamFilter#filters()
	 */
	@Override
	public boolean filters() {
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.spam.SpamFilter#isSpam(
	 * org.esupportail.helpdesk.domain.beans.User, java.util.Set, java.lang.String, 
	 * java.lang.String, byte[])
	 */
	@Override
	public boolean isSpam(
			@SuppressWarnings("unused")
			final User sender, 
			@SuppressWarnings("unused")
			final Set<Address> recipients, 
			@SuppressWarnings("unused")
			final String subject,
			@SuppressWarnings("unused")
			final String contentType, 
			@SuppressWarnings("unused")
			final byte[] data) {
		return false;
	}

	public void afterPropertiesSet() {
		// TODO Auto-generated method stub
		
	}

}
