/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.recall; 

import java.sql.Timestamp;
import java.util.Calendar;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.lock.FileLockImpl;
import org.esupportail.commons.utils.lock.Lock;
import org.springframework.util.StringUtils;

/**
 * A basic implementation of Recaller.
 */
public class RecallerImpl extends AbstractLockableRecaller {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8249895414377608606L;

	/**
	 * The default unit for the time limit.
	 */
	private static final int DEFAULT_TIME_LIMIT_UNIT = Calendar.MONTH;

	/**
	 * The default amount for the time limit.
	 */
	private static final int DEFAULT_TIME_LIMIT_AMOUNT = 3;
	
	/**
	 * The default maximum number of tickets to recall at the same time.
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
	 * The maximum number of tickets to recall at the same time.
	 */
	private int batchSize = DEFAULT_BATCH_SIZE;
	
	/**
	 * The lock path.
	 */
	private String lockPath;
	
	/**
	 * Bean constructor.
	 */
	public RecallerImpl() {
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
	 * @see org.esupportail.helpdesk.services.recall.AbstractLockableRecaller#getLock()
	 */
	@Override
	protected Lock getLock() {
		return new FileLockImpl(lockPath + "/recallTicketsLocked");
	}

	/**
	 * @see org.esupportail.helpdesk.services.recall.AbstractLockableRecaller#recallInternal()
	 */
	@Override
	protected void recallInternal() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(timeLimitUnit, -timeLimitAmount);
		Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
		logger.info("recalling tickets posponed until " + timestamp + "...");
		int n = getDomainService().recallPostponedTickets();
		if (n <= 0) {
			logger.info("no ticket to recall.");
		}
		logger.info(n + " ticket(s) recalled.");
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
