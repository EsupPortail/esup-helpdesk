/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils.lock; 

import java.io.File;
import java.io.IOException;


/**
 * A cross-JVMs file lock.
 */
public class FileLockImpl implements Lock {
	
	/**
	 * The lock filename.
	 */
	private String filename;

	/**
	 * Constructor.
	 * @param filename 
	 */
	public FileLockImpl(final String filename) {
		super();
		this.filename = filename;
	}
	
	/**
	 * @see org.esupportail.commons.utils.lock.Lock#lock()
	 */
	@Override
	public void lock() throws AlreadyLockedException {
	    try {
	        File file = new File(filename);
	        if (!file.createNewFile()) {
	        	throw new AlreadyLockedException("file [" + filename + "] is already locked");
	        }
	    } catch (IOException e) {
	    	throw new LockException("could not lock file [" + filename + "]");
	    }
	}

	/**
	 * @see org.esupportail.commons.utils.lock.Lock#tryLock()
	 */
	@Override
	public boolean tryLock() {
	    try {
	        lock();
	        return true;
	    } catch (AlreadyLockedException e) {
	    	return false;
	    }
	}

	/**
	 * @see org.esupportail.commons.utils.lock.Lock#unlock()
	 */
	@Override
	public void unlock() {
        File file = new File(filename);
        if (!file.delete()) {
        	throw new NotLockedException("file [" + filename + "] is not locked");
        }
	}

}
