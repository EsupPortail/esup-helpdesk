/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.ActionI18nTitleFormatter;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.web.controllers.SessionController;
import org.springframework.beans.factory.InitializingBean;

/** 
 * A provider for ticket action i18n titles.
 */ 
public class ActionI18nTitleProvider 
extends HashMap<String, String> implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2439295516343652431L;

	/**
	 * The formatter itself.
	 */
	private ActionI18nTitleFormatter actionI18nTitleFormatter;
	
	/**
	 * The session controller.
	 */
	private SessionController sessionController;
	
	/**
	 * Bean constructor.
	 */
	public ActionI18nTitleProvider() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.sessionController, 
				"property sessionController of class " + this.getClass().getName() 
				+ " can not be null");
		Assert.notNull(this.actionI18nTitleFormatter, 
				"property actionI18nTitleFormatter of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object action) {
		return actionI18nTitleFormatter.getActionTitle((Action) action, sessionController.getLocale());
	}

	/**
	 * @return the actionI18nTitleFormatter
	 */
	protected ActionI18nTitleFormatter getActionI18nTitleFormatter() {
		return actionI18nTitleFormatter;
	}

	/**
	 * @param actionI18nTitleFormatter the actionI18nTitleFormatter to set
	 */
	public void setActionI18nTitleFormatter(
			final ActionI18nTitleFormatter actionI18nTitleFormatter) {
		this.actionI18nTitleFormatter = actionI18nTitleFormatter;
	}

	/**
	 * @return the sessionController
	 */
	protected SessionController getSessionController() {
		return sessionController;
	}

	/**
	 * @param sessionController the sessionController to set
	 */
	public void setSessionController(final SessionController sessionController) {
		this.sessionController = sessionController;
	}
	
}

