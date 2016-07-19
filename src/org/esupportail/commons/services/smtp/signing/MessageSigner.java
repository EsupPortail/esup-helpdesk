/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.smtp.signing;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/** 
 * The interface for signing email messages.
 */
public interface MessageSigner {
    /**
     * Sign the message.
     * @param session the session
     * @param mimeMessage the message that should be signed
     * @return the message with sign embedded
     */
    public MimeMessage sign(Session session, MimeMessage mimeMessage);
}
