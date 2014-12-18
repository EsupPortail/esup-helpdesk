/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 University of Pardubice.
 */
package org.esupportail.helpdesk.services.feed.imap.messageId;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.springframework.beans.factory.InitializingBean;

/**
 * A simple messageId handler.
 */
public class SigningMessageIdHandlerImpl extends AbstractMessageIdHandler implements InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4788996241260919702L;

	/**
	 * Regular expression patter to find ticket number.
	 */
	private static final Pattern TICKET_ID_PATTERN = Pattern.compile(".*<ticketId\\.(\\d+)\\.([0-9a-z]+).*@[^>]*>.*", Pattern.DOTALL);

	/**
	 * The base used to hash.
	 */
	private static final int HASH_BASE = 32;

	/**
	 * The length of hashes.
	 */
	private static final int HASH_LENGTH = 16;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The domain of outgoing emails, used to build the MessageID headers.
	 */
	private String emailDomain;

    /**
     * Salt to sign.
     */
    private String salt;

    /**
	 * Constructor.
	 */
	public SigningMessageIdHandlerImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.hasText(emailDomain,
				"property emailDomain of class " + this.getClass().getName()
				+ " can not be null");
		if (salt == null) {
			logger.info("no salt provided, turning off reply-to functionalities!");
		}
	}

	/**
	 * @param ticketId
	 * @return a hash.
     */
    protected String getHash(
    		final long ticketId) {
		if (salt == null) {
			throw new ConfigException("no salt provided, reply-to functionalities are turned off!");
		}
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update((ticketId + salt).getBytes());
            //base-32 encoded hash; only 16 digits for shorter Message-Ids
            return new BigInteger(1, md.digest())
            .toString(HASH_BASE).substring(0, HASH_LENGTH).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new MessageIdException(e);
        }
    }

	/**
     * @see org.esupportail.helpdesk.services.feed.imap.messageId.MessageIdHandler#genMessageId(
     * org.esupportail.helpdesk.domain.beans.Ticket)
     */
    @Override
	public String genMessageId(final Ticket ticket) {
    	String uniqueId = new Date().getTime() + "-" + Math.abs(new Random().nextLong());
    	if (ticket == null) {
            // Message-ID according to RFC 822 in form "<yyyyyyyyy@domain>",
            // where yyyyyyyyy is a random number
            return "<" + uniqueId + "@" + emailDomain + ">";
    	}
        // Message-ID according to RFC 822 in form "<ticketId.xxx.yyy@domain>",
        // 		where xxx is the ticket id and and yyy is hash.
    	long ticketId = ticket.getId();
    	String hash = getHash(ticketId);
        return "<" + "ticketId." + ticketId + "." + hash + "." + uniqueId + "@" + emailDomain + ">";
    }

    /**
     * @see org.esupportail.helpdesk.services.feed.imap.messageId.MessageIdHandler#getTicketIdFromMessageId(
     * java.lang.String)
     */
    @Override
	public Long getTicketIdFromMessageId(
    		final String messageId) throws MessageIdException {
        logger.debug("Trying to get ticketId from [" + messageId + "]");
        Matcher messageIdMatcher = TICKET_ID_PATTERN.matcher(messageId);
    	if (!messageIdMatcher.matches()) {
    		throw new MessageIdException(
    				"messageId does not match pattern [ticketId.<id>.<hash>.<random>@"
    				+ emailDomain + "]");
    	}
		long ticketId;
		try {
			ticketId = Long.valueOf(messageIdMatcher.replaceFirst("$1"));
		} catch (NumberFormatException e1) {
			throw new MessageIdException("invalid ticketId", e1);
		}
		String hash = messageIdMatcher.replaceFirst("$2");
		if (!getHash(ticketId).equals(hash)) {
			throw new MessageIdException("invalid hash (was: [" + hash + "]; expected: [" + getHash(ticketId) + "]");
		}
                logger.info("Got ticket #" + ticketId + " from [" + messageId + "]");
		return ticketId;
    }

    /**
     * @return the salt
     */
    protected String getSalt() {
        return salt;
    }

    /**
     * @param salt the salt
     */
    public void setSalt(final String salt) {
        this.salt = StringUtils.nullIfEmpty(salt);
    }

	/**
	 * @return the emailDomain
	 */
	protected String getEmailDomain() {
		return emailDomain;
	}

	/**
	 * @param emailDomain the emailDomain to set
	 */
	public void setEmailDomain(final String emailDomain) {
		this.emailDomain = StringUtils.nullIfEmpty(emailDomain);
	}

}
