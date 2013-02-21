/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.archiving; 

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
 * A basic implementation of Archiver.
 */
public class ArchiverImpl extends AbstractLockableArchiver {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4739392197480284286L;

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
	 * The lock path.
	 */
	private String lockPath;
	
	/**
	 * Bean constructor.
	 */
	public ArchiverImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
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
			} else if (timeLimitUnit == Calendar.HOUR) {
				logger.debug("timeLimitUnit=HOUR");
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
	 * @see org.esupportail.helpdesk.services.archiving.AbstractLockableArchiver#getLock()
	 */
	@Override
	protected Lock getLock() {
		return new FileLockImpl(lockPath + "/archiveTicketsLocked");
	}

	/**
	 * @see org.esupportail.helpdesk.services.archiving.AbstractLockableArchiver#archiveInternal()
	 */
	@Override
	protected boolean archiveInternal() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(timeLimitUnit, -timeLimitAmount);
		Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
		logger.info("archiving tickets closed before " + timestamp + "...");
		List<Ticket> tickets = getDomainService().getClosedTicketsBefore(timestamp, batchSize);
		if (tickets.isEmpty()) {
			logger.info("no more ticket to archive.");
			return false;
		}
		for (Ticket ticket : tickets) {
			logger.info("archiving ticket #" + ticket.getId() + " last modified on " 
					+ ticket.getLastActionDate() + "...");
			getDomainService().archiveTicket(ticket);
		}
		logger.info(tickets.size() + " ticket(s) archived.");
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
		} else if (timeLimitString.endsWith("h")) {
			timeLimitUnit = Calendar.HOUR;
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
