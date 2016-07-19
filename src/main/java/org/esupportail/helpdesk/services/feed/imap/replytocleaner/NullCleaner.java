/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 University of Pardubice.
 */
package org.esupportail.helpdesk.services.feed.imap.replytocleaner;

/**
 * No change cleaner.
 */
public class NullCleaner implements ReplyToMessageCleaner {

    /**
	 * Constructor.
	 */
	public NullCleaner() {
		super();
	}

	/**
     * @see org.esupportail.helpdesk.services.feed.imap.replytocleaner.ReplyToMessageCleaner#clean(java.lang.String)
     */
    @Override
	public String clean(final String input) {
        return input;
    }
}
