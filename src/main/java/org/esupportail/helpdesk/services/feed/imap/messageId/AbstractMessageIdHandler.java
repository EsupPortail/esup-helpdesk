/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 University of Pardubice.
 */
package org.esupportail.helpdesk.services.feed.imap.messageId;

/**
 * An abstract messageId handler.
 */
@SuppressWarnings("serial")
public abstract class AbstractMessageIdHandler implements MessageIdHandler {

    /**
     * @see org.esupportail.helpdesk.services.feed.imap.messageId.MessageIdHandler#genMessageId()
     */
    @Override
	public String genMessageId() {
    	return genMessageId(null);
    }

}
