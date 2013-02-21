/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed.imap.spam;



/**
 * A basic SpamFilter implementation that considers a message as spam when the subject contains a pattern.
 */
public class SubjectContainsSpamFilterImpl extends AbstractPatternBasedSubjectSpamFilter {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2126991412246882759L;

	/**
	 * Constructor.
	 */
	public SubjectContainsSpamFilterImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.spam.AbstractPatternBasedSubjectSpamFilter#isSpam(
	 * java.lang.String)
	 */
	@Override
	public boolean isSpam(
			final String subject) {
		return subject.contains(getPattern());
	}

}
