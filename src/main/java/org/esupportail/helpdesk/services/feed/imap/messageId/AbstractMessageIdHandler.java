/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 University of Pardubice.
 */
package org.esupportail.helpdesk.services.feed.imap.messageId;

import org.esupportail.helpdesk.domain.beans.Ticket;

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

	public void afterPropertiesSet() {
		// TODO Auto-generated method stub
		
	}

	public String genMessageId(Ticket ticket) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getTicketIdFromMessageId(String messageId) throws MessageIdException {
		// TODO Auto-generated method stub
		return null;
	}

}
