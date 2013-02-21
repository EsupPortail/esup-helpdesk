/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.net.InetAddress;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.aop.cache.SessionCache;
import org.esupportail.commons.aop.monitor.Monitor;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.web.controllers.Resettable;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.web.beans.UserFormatter;

/**
 * An abstract class inherited by all the beans for them to get:
 * - the context of the application (sessionController).
 * - the domain service (domainService).
 * - the application service (applicationService).
 * - the i18n service (i18nService).
 */
@SuppressWarnings("serial")
@Monitor
public abstract class AbstractContextAwareController extends AbstractDomainAwareBean implements Resettable {

	/**
	 * The SessionController.
	 */
	private SessionController sessionController;

	/**
	 * The user formatter.
	 */
	private UserFormatter userFormatter;

	/**
	 * Constructor.
	 */
	protected AbstractContextAwareController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractDomainAwareBean#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		reset();
		Assert.notNull(this.sessionController, "property sessionController of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.userFormatter, "property userFormatter of class "
				+ this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	@Override
	public void reset() {
		//
	}

	/**
	 * @param user
	 * @return the user formatted
	 */
	protected String formatUser(final User user) {
		return userFormatter.get(user);
	}

	/**
	 * @param sessionController the sessionController to set
	 */
	public void setSessionController(final SessionController sessionController) {
		this.sessionController = sessionController;
	}

	/**
	 * @return the sessionController
	 */
	public SessionController getSessionController() {
		return sessionController;
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractDomainAwareBean#getCurrentUser()
	 */
	@Override
	@RequestCache
	public User getCurrentUser() {
		return sessionController.getCurrentUser();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractDomainAwareBean#getClient()
	 */
	@Override
	@SessionCache
	protected InetAddress getClient() {
		return sessionController.getClient();
	}

	/**
	 * @return the userFormatter
	 */
	protected UserFormatter getUserFormatter() {
		return userFormatter;
	}

	/**
	 * @param userFormatter the userFormatter to set
	 */
	public void setUserFormatter(final UserFormatter userFormatter) {
		this.userFormatter = userFormatter;
	}

}
