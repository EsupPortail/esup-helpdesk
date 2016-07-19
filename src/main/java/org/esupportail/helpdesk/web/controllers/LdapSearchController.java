/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.util.Locale;

import org.esupportail.commons.utils.Assert;

/**
 * The bean used for LDAP searches.
 */
public class LdapSearchController extends
		org.esupportail.commons.web.controllers.LdapSearchController {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8984228386570692395L;

	/**
	 * The context bean.
	 */
	private SessionController sessionController;

	/**
	 * Bean constructor.
	 */
	public LdapSearchController() {
		super();
	}

	/**
	 * @see org.esupportail.commons.web.controllers.LdapSearchController#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.sessionController, "property sessionController of class " 
				+ this.getClass().getName() + " can not be null");
	}

	/**
	 * @param sessionController the sessionController to set
	 */
	public void setSessionController(final SessionController sessionController) {
		this.sessionController = sessionController;
	}

	/**
	 * @see org.esupportail.commons.beans.AbstractI18nAwareBean#getCurrentUserLocale()
	 */
	@Override
	protected Locale getCurrentUserLocale() {
		return sessionController.getCurrentUserLocale();
	}

}
