/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.expiration; 

import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.lock.Lock;
import org.esupportail.commons.utils.lock.LockException;
import org.esupportail.helpdesk.domain.DomainService;
import org.springframework.beans.factory.InitializingBean;

/**
 * A lockable abstract implementation of Expirator.
 */
@SuppressWarnings("serial")
public abstract class AbstractLockableExpirator implements Expirator, InitializingBean {
	
	/**
	 * The domain service.
	 */
	private DomainService domainService;
	
	/**
	 * Bean constructor.
	 */
	public AbstractLockableExpirator() {
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
	 * @see org.esupportail.helpdesk.services.expiration.Expirator#unlock()
	 */
	@Override
	public void unlock() {
		getLock().unlock();
	}

	/**
	 * @see org.esupportail.helpdesk.services.expiration.Expirator#expire(boolean)
	 */
	@Override
	public boolean expire(final boolean alerts) {
    	if (!tryLock()) {
    		throw new LockException(
    				"task expire-tickets is already running, "
    				+ "please retry in a few minutes or run task unlock-expire-tickets "
    				+ "if you are sure that task expire-tickets is not running");
    	}
    	boolean result;
    	try {
    		result = expireTicketsInternal(alerts);
    	} catch (RuntimeException e) {
    	   	try {
				unlock();
			} catch (RuntimeException e2) {
				// forget...
			}
			throw e;
    	}
    	unlock();
    	return result;
	}

	/**
	 * Expire non approved tickets.
	 * @param alerts 
	 * @return true if the method should be called again.
	 */
	protected abstract boolean expireTicketsInternal(
			boolean alerts);

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
