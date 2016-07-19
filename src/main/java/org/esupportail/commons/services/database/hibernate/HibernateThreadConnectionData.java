/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.database.hibernate;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * A class to store the Hibernate connexion data to have a single session
 * per thread.
 */

public class HibernateThreadConnectionData {

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * A flag set to true if only participating.
	 */
	private boolean participate;
	
	/**
	 * The hibernate session.
	 */
	private Session session;
	
	/**
	 * The name of the session factory bean.
	 */
	private String sessionFactoryBeanName;
	
	/**
	 * The session factory.
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * Bean constructor.
	 * @param sessionFactoryBeanName 
	 */
	HibernateThreadConnectionData(final String sessionFactoryBeanName) {
		super();
		sessionFactory = null;
		session = null;
		participate = false;
		this.sessionFactoryBeanName = sessionFactoryBeanName;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = getClass().getSimpleName() + "#" + hashCode() + "["
		+ "sessionFactoryBeanName=" + sessionFactoryBeanName
		+ ", sessionFactory=" + sessionFactory
		+ ", participate=" + participate
		+ ", session=";
		if (session == null) {
			str += null;
		} else {
			str += session.getClass().getSimpleName() + "#" + hashCode();
		}
		str += "]";
		return str;
	}

	/**
	 * Open the session.
	 */
	void openSession() {
		sessionFactory = (SessionFactory) BeanUtils.getBean(sessionFactoryBeanName);
		if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
			if (logger.isDebugEnabled()) {
				logger.debug("OPEN(" + sessionFactoryBeanName + ") ***** participate!");
			}
			participate = true;
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("OPEN(" + sessionFactoryBeanName + ")");
		}
		session = SessionFactoryUtils.getSession(sessionFactory, true);
		session.setFlushMode(FlushMode.ALWAYS);
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	}

	/**
	 * Begin a transaction.
	 */
	void beginTransaction() {
		if (participate) {
			if (logger.isDebugEnabled()) {
				logger.debug("BEGIN(" + sessionFactoryBeanName + ") ***** particpate!");
			}
			return;
		}
		if (session != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("BEGIN(" + sessionFactoryBeanName + ")");
			}
			session.beginTransaction();
		}
	}

	/**
	 * End a transaction.
	 * @param commit 
	 */
	void endTransaction(final boolean commit) {
		if (participate) {
			if (logger.isDebugEnabled()) {
				logger.debug("END(" + sessionFactoryBeanName + ", " 
						+ commit + ") ***** particpate!");
			}
			return;
		}
		if (session != null) {
			if (session.isOpen()) {
				if (logger.isDebugEnabled()) {
					logger.debug("END(" + sessionFactoryBeanName + ", " 
							+ commit + ")");
				}
				Transaction transaction = session.getTransaction();
				if (transaction != null && transaction.isActive()) {
					if (commit) {
						transaction.commit();
					} else {
						transaction.rollback();
					}
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("END(" + sessionFactoryBeanName + ", " 
							+ commit + ") ***** session is closed!");
				}
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("END(" + sessionFactoryBeanName + ", " 
						+ commit + ") ***** session is null!");
			}
		}
	}

	/**
	 * Close the session.
	 */
	void closeSession() {
		if (participate) {
			if (logger.isDebugEnabled()) {
				logger.debug("CLOSE(" + sessionFactoryBeanName + ") ***** participate!");
			}
			return;
		}
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		if (session != null) {
			if (session.isOpen()) {
				if (logger.isDebugEnabled()) {
					logger.debug("CLOSE(" + sessionFactoryBeanName + ")");
				}
				Transaction transaction = session.getTransaction();
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
				}
				SessionFactoryUtils.releaseSession(session, sessionFactory);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("CLOSE(" + sessionFactoryBeanName + ") ***** session is closed!");
				}
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("CLOSE(" + sessionFactoryBeanName + ") ***** session is null!");
			}
		}
		session = null;
	}

}
