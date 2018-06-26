/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

/**
 * A class to feed the database with tickets.
 */
public class FeederImpl implements InitializingBean, Feeder {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 574701276585886191L;

	/**
	 * The mail readers.
	 */
	private List<AccountReader> mailReaders;

	/**
	 * Constructor.
	 */
	public FeederImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.Feeder#feed(
	 * org.esupportail.helpdesk.services.feed.ErrorHolder)
	 */
	public boolean feed(final ErrorHolder errorHolder) {
		if (mailReaders == null || mailReaders.isEmpty()) {
			errorHolder.addError("no reader set, exiting");
			return false;
		}
		for (AccountReader accountReader : mailReaders) {
			ErrorHolder accountErrorHolder = new ErrorHolder();
			boolean commit = accountReader.read(accountErrorHolder);
			errorHolder.add(accountErrorHolder);
			if (commit) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the mailReaders
	 */
	protected List<AccountReader> getMailReaders() {
		return mailReaders;
	}

	/**
	 * @param mailReaders the mailReaders to set
	 */
	public void setMailReaders(final List<AccountReader> mailReaders) {
		this.mailReaders = new ArrayList<AccountReader>();
		for (AccountReader accountReader : mailReaders) {
			if (accountReader.isEnabled()) {
				this.mailReaders.add(accountReader);
			}
		}
	}

}
