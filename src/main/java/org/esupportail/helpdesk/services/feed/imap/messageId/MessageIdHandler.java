/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 University of Pardubice.
 */
package org.esupportail.helpdesk.services.feed.imap.messageId;

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.Ticket;

/**
 * The interface of messageId handlers.
 */
public interface MessageIdHandler extends Serializable {

    /**
     * @param ticket ticket for which the messageId should be generated, can be null
     * @return Message-ID according to RFC 822 in form 
     * "&lt;xxx@yyy&gt;" or null for automatic generation
     */
    String genMessageId(final Ticket ticket);

    /**
     * @return Message-ID according to RFC 822 or null for automatic generation
     */
    String genMessageId();

    /**
     * @param messageId
     * @return the ticket id that corresponds to a Message-ID
     * @throws MessageIdException 
     */
    Long getTicketIdFromMessageId(String messageId) throws MessageIdException;

}
