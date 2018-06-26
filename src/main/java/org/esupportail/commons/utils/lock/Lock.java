/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils.lock; 

/**
 * A cross-JVMs file lock.
 */
public interface Lock {
	
	/**
	 * acquire the lock.
	 * @throws AlreadyLockedException 
	 */
	void lock() throws AlreadyLockedException;

	/**
	 * Try to lock.
	 * @return true if locked, false if already locked before.
	 */
	boolean tryLock();

	/**
	 * Release the lock.
	 * @throws NotLockedException 
	 */
	void unlock() throws NotLockedException;

}
