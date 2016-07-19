/**
 * ESUP-Portail Commons - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-commons
 */
package org.esupportail.commons.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * This class is used to update a Hibernate sequence.
 */
public class HibernateSequenceUpdater implements HibernateCallback {

	/** The name of the sequence. */
	private String sequenceName;

	/** The value to set. */
	private Long sequenceId;

	/**
	 * Constructor.
	 * @param sequenceName
	 * @param sequenceId
	 */
	public HibernateSequenceUpdater(final String sequenceName, final Long sequenceId) {
		this.sequenceId = sequenceId;
		this.sequenceName = sequenceName;
	}

	/**
	 * Hibernate callback.
	 * @param session
	 * @return null.
	 * @throws HibernateException
	 */
	@Override
	public Object doInHibernate(final Session session) throws HibernateException {
		if (sequenceId != null) {
			session.createSQLQuery(
                    "SELECT pg_catalog.setval('" + sequenceName + "', " + sequenceId + ", true)"
                    ).list();
		}
		return null;
	}
}

