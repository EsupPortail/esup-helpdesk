/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 University of Pardubice.
 */
package org.esupportail.helpdesk.services.feed.imap.replytocleaner;

/**
 * Interface for message cleaners of reply-to messages.
 */
public interface ReplyToMessageCleaner {
    /**
     * @param input input string
     * @return cleaned string
     */
    String clean(String input);
}
