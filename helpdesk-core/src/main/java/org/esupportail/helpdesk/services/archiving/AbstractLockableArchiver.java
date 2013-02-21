/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.archiving; 

import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.lock.Lock;
import org.esupportail.commons.utils.lock.LockException;
import org.esupportail.helpdesk.domain.DomainService;
import org.springframework.beans.factory.InitializingBean;

/**
 * An abstract lockable implementation of Archiver.
 */
@SuppressWarnings("serial")
public abstract class AbstractLockableArchiver implements Archiver, InitializingBean {
	
	/**
	 * The domain service.
	 */
	private DomainService domainService;
	
	/**
	 * Bean constructor.
	 */
	public AbstractLockableArchiver() {
		super();
	}

	/**
	 * @throws Exception 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@SuppressWarnings("unused")
	@Override
	public void afterPropertiesSet() throws Exception {
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
	 * @see org.esupportail.helpdesk.services.archiving.Archiver#unlock()
	 */
	@Override
	public void unlock() {
		getLock().unlock();
	}

	/**
	 * @see org.esupportail.helpdesk.services.archiving.Archiver#archive()
	 */
	@Override
	public boolean archive() {
    	if (!tryLock()) {
    		throw new LockException(
    				"task archive-tickets is already running, "
    				+ "please retry in a few minutes or run task unlock-archive-tickets "
    				+ "if you are sure that task archive-tickets is not running");
    	}
    	boolean result = archiveInternal();
    	unlock();
    	return result;
	}

	/**
	 * Archive obsolete tickets.
	 * @return true if the method should be called again.
	 */
	protected abstract boolean archiveInternal();

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
