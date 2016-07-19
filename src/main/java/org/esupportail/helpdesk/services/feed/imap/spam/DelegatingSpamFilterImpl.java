/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed.imap.spam;

import java.util.List;
import java.util.Set;

import javax.mail.Address;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.User;
import org.springframework.beans.factory.InitializingBean;

/**
 * An implementation of SpamFilter that delegates to other filters.
 */
public class DelegatingSpamFilterImpl implements InitializingBean, SpamFilter {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5883726634418902379L;

	/**
	 * The filters to delegate.
	 */
	private List<SpamFilter> filters;
	
	/**
	 * 
	 */
	public DelegatingSpamFilterImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notEmpty(filters,
				"property filters of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.spam.SpamFilter#isSpam(
	 * org.esupportail.helpdesk.domain.beans.User, java.util.Set, 
	 * java.lang.String, java.lang.String, byte[])
	 */
	@Override
	public boolean isSpam(
			final User sender, 
			final Set<Address> recipients, 
			final String subject,
			final String contentType, 
			final byte[] data) {
		for (SpamFilter filter : filters) {
			if (filter.isSpam(sender, recipients, subject, contentType, data)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.spam.SpamFilter#filters()
	 */
	@Override
	public boolean filters() {
		for (SpamFilter filter : filters) {
			if (filter.filters()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the filters
	 */
	protected List<SpamFilter> getFilters() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(final List<SpamFilter> filters) {
		this.filters = filters;
	}

}
