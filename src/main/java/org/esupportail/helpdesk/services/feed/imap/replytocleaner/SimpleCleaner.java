/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 University of Pardubice.
 */
package org.esupportail.helpdesk.services.feed.imap.replytocleaner;

import java.util.regex.Pattern;

/**
 * Simply cleans message text. Supposes reply in the beginning of the message
 * and no changes in reply. Deletes all text from senders email address to the
 * end of the message.
 * @author lusl0338
 */
public class SimpleCleaner implements ReplyToMessageCleaner {

    /**
     * Senders email.
     */
    private String fromEmail;

    /**
	 * Constructor.
	 */
	public SimpleCleaner() {
		super();
	}

    /**
     * @see org.esupportail.helpdesk.services.feed.imap.replytocleaner.ReplyToMessageCleaner#clean(java.lang.String) 
     */
    @Override
	public String clean(final String content) {
        return Pattern.compile(
                fromEmail + ".*",
                Pattern.CASE_INSENSITIVE + Pattern.DOTALL + Pattern.MULTILINE).
                matcher(content).replaceAll("");
    }

    /**
     * @return the email
     */
    public String getFromEmail() {
        return fromEmail;
    }

    /**
     * @param fromEmail the email
     */
    public void setFromEmail(final String fromEmail) {
        this.fromEmail = fromEmail;
    }

}
