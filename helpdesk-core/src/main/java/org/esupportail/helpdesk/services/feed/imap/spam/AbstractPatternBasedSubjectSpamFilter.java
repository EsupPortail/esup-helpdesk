/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed.imap.spam;

import java.util.Set;

import javax.mail.Address;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.User;
import org.springframework.beans.factory.InitializingBean;

/**
 * An abstract implementation of SpamFilter that is based on a pattern in the subject of emails.
 */
@SuppressWarnings("serial")
public abstract class AbstractPatternBasedSubjectSpamFilter implements InitializingBean, SpamFilter {
	
	/**
	 * The pattern to look for at the beginning of email subjects.
	 */
	private String pattern;
	
	/**
	 * Constructor.
	 */
	public AbstractPatternBasedSubjectSpamFilter() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(pattern, 
				"property pattern of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.spam.SpamFilter#filters()
	 */
	@Override
	public boolean filters() {
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.spam.SpamFilter#isSpam(
	 * org.esupportail.helpdesk.domain.beans.User, java.util.Set, 
	 * java.lang.String, java.lang.String, byte[])
	 */
	@Override
	public boolean isSpam(
			@SuppressWarnings("unused")
			final User sender, 
			@SuppressWarnings("unused")
			final Set<Address> recipients, 
			final String subject,
			@SuppressWarnings("unused")
			final String contentType, 
			@SuppressWarnings("unused")
			final byte[] data) {
		return isSpam(subject);
	}

	/**
	 * @param subject
	 * @return true if the messsage should be considered as spam.
	 */
	protected abstract boolean isSpam(String subject);

	/**
	 * @return the pattern
	 */
	protected String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}
	
}
