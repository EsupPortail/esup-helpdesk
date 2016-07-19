/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 University of Pardubice.
 */
package org.esupportail.helpdesk.services.feed.imap.spam;



/**
 * A basic SpamFilter implementation that considers a message as spam 
 * when the subject matches regular expression pattern.
 */
public class SubjectRegexpSpamFilterImpl extends AbstractPatternBasedSubjectSpamFilter {

	/**
	 * The serialization id. 
	 */
	private static final long serialVersionUID = -4093512646927417265L;

	/**
	 * Constructor.
	 */
	public SubjectRegexpSpamFilterImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.spam.AbstractPatternBasedSubjectSpamFilter#isSpam(
	 * java.lang.String)
	 */
	@Override
	protected boolean isSpam(final String subject) {
		return subject.matches(getPattern());
	}
}
