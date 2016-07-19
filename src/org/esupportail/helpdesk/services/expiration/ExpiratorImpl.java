/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.expiration; 

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.lock.FileLockImpl;
import org.esupportail.commons.utils.lock.Lock;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.springframework.util.StringUtils;

/**
 * A basic implementation of Expirator.
 */
public class ExpiratorImpl extends AbstractLockableExpirator {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6406450889879636697L;

	/**
	 * The default unit for the time limit.
	 */
	private static final int DEFAULT_TIME_LIMIT_UNIT = Calendar.MONTH;

	/**
	 * The default amount for the time limit.
	 */
	private static final int DEFAULT_TIME_LIMIT_AMOUNT = 3;
	
	/**
	 * The default maximum number of tickets to archive at the same time.
	 */
	private static final int DEFAULT_BATCH_SIZE = 100;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The lock path.
	 */
	private String lockPath;
	
	/**
	 * The unit of the time limit.
	 */
	private int timeLimitUnit = DEFAULT_TIME_LIMIT_UNIT;
		
	/**
	 * The amount of the time limit.
	 */
	private int timeLimitAmount = DEFAULT_TIME_LIMIT_AMOUNT;
			
	/**
	 * The maximum number of tickets to archive at the same time.
	 */
	private int batchSize = DEFAULT_BATCH_SIZE;
	
	/**
	 * Bean constructor.
	 */
	public ExpiratorImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (lockPath != null) {
			while (lockPath.endsWith("/") || lockPath.endsWith("\\")) {
				lockPath = lockPath.substring(1);
			}
		}
		Assert.hasLength(lockPath, 
				"property lockPath of class " + this.getClass().getName() 
				+ " can not be null");
		if (logger.isDebugEnabled()) {
			if (timeLimitUnit == Calendar.MONTH) {
				logger.debug("timeLimitUnit=MONTH");
			} else if (timeLimitUnit == Calendar.DAY_OF_YEAR) {
				logger.debug("timeLimitUnit=DAY");
			} else {
				logger.debug("timeLimitUnit=?");
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("timeLimitAmount=" + timeLimitAmount);
		}
		if (batchSize <= 0) {
			throw new ConfigException("invalid batch size [" + batchSize + "]");
		}
	}

	/**
	 * @see org.esupportail.helpdesk.services.expiration.AbstractLockableExpirator#getLock()
	 */
	@Override
	protected Lock getLock() {
		return new FileLockImpl(lockPath + "/expireTicketsLocked");
	}

	/**
	 * @see org.esupportail.helpdesk.services.expiration.AbstractLockableExpirator#expireTicketsInternal(
	 * boolean)
	 */
	@Override
	protected boolean expireTicketsInternal(
			final boolean alerts) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(timeLimitUnit, -timeLimitAmount);
		Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
		logger.info("expiring non approved tickets closed before " + timestamp + "...");
		List<Ticket> tickets = getDomainService().getNonApprovedTicketsClosedBefore(timestamp, batchSize);
		if (tickets.isEmpty()) {
			logger.info("no more ticket to expire.");
			return false;
		}
		for (Ticket ticket : tickets) {
			logger.info("expiring ticket #" + ticket.getId() + " last modified on " 
					+ ticket.getLastActionDate() + "...");
			getDomainService().expireTicket(ticket, alerts);
		}
		logger.info(tickets.size() + " ticket(s) expired.");
		return true;
	}

	/**
	 * @return the timeLimitAmount
	 */
	protected int getTimeLimitAmount() {
		return timeLimitAmount;
	}

	/**
	 * @return the timeLimitUnit
	 */
	protected int getTimeLimitUnit() {
		return timeLimitUnit;
	}

	/**
	 * @param timeLimitString the timeLimit to set as a string
	 */
	protected void invalidTimeLimit(final String timeLimitString) {
		throw new ConfigException("invalid time limit specification [" + timeLimitString + "]");
	}

	/**
	 * @param timeLimitString the timeLimit to set as a string
	 */
	public void setTimeLimit(final String timeLimitString) {
		if (!StringUtils.hasLength(timeLimitString)) {
			throw new ConfigException("empty time limit");
		}
		if (timeLimitString.endsWith("m")) {
			timeLimitUnit = Calendar.MONTH;
		} else if (timeLimitString.endsWith("d")) {
			timeLimitUnit = Calendar.DAY_OF_YEAR;
		} else {
			throw new ConfigException("invalid time limit suffix [" 
					+ timeLimitString.substring(timeLimitString.length() - 1) + "]");
		}
		try {
			timeLimitAmount = Integer.parseInt(timeLimitString.substring(0, timeLimitString.length() - 1));
		} catch (NumberFormatException e) {
			throw new ConfigException("invalid time limit value [" 
					+ timeLimitString.substring(0, timeLimitString.length() - 1) + "]");
		}
	}

	/**
	 * @return the batchSize
	 */
	protected int getBatchSize() {
		return batchSize;
	}

	/**
	 * @param batchSize the batchSize to set
	 */
	public void setBatchSize(final int batchSize) {
		this.batchSize = batchSize;
	}

	/**
	 * @return the lockPath
	 */
	protected String getLockPath() {
		return lockPath;
	}

	/**
	 * @param lockPath the lockPath to set
	 */
	public void setLockPath(final String lockPath) {
		this.lockPath = lockPath;
	}

}
