/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.recall; 

import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.lock.Lock;
import org.esupportail.commons.utils.lock.LockException;
import org.esupportail.helpdesk.domain.DomainService;
import org.springframework.beans.factory.InitializingBean;

/**
 * An abstract lockable implementation of Recaller.
 */
@SuppressWarnings("serial")
public abstract class AbstractLockableRecaller implements Recaller, InitializingBean {
	
	/**
	 * The domain service.
	 */
	private DomainService domainService;
	
	/**
	 * Bean constructor.
	 */
	public AbstractLockableRecaller() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(domainService, 
				"property domainService of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * Lock.
	 * @return true on success.
	 */
	protected boolean tryLock() {
		return getLock().tryLock();
	}

	/**
	 * @return the lock.
	 */
	protected abstract Lock getLock();

	/**
	 * @see org.esupportail.helpdesk.services.recall.Recaller#unlock()
	 */
	@Override
	public void unlock() {
		getLock().unlock();
	}

	/**
	 * @see org.esupportail.helpdesk.services.recall.Recaller#recall()
	 */
	@Override
	public void recall() {
    	if (!tryLock()) {
    		throw new LockException(
    				"task recall-tickets is already running, "
    				+ "please retry in a few minutes or run task unlock-recall-tickets "
    				+ "if you are sure that task recall-tickets is not running");
    	}
    	recallInternal();
    	unlock();
	}

	/**
	 * Recall postponed tickets.
	 */
	protected abstract void recallInternal();

	/**
	 * @return the domainService
	 */
	protected DomainService getDomainService() {
		return domainService;
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

}
