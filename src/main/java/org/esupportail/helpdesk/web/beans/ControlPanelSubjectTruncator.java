/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;
import java.util.Locale;

import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.web.controllers.SessionController;
import org.springframework.beans.factory.InitializingBean;

/** 
 * A truncator for the ticket subjects in the control panel.
 */ 
public class ControlPanelSubjectTruncator 
extends HashMap<String, String>
implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 4678244957710474402L;

	/**
	 * The default max length.
	 */
	private static final int DEFAULT_MAX_LENGTH = 80;
	
	/**
	 * The max length.
	 */
	private int maxLength = DEFAULT_MAX_LENGTH;
	
	/**
	 * The i18n service.
	 */
	private I18nService i18nService;
	
	/**
	 * The session controller.
	 */
	private SessionController sessionController;
	
	/**
	 * Bean constructor.
	 */
	public ControlPanelSubjectTruncator() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(sessionController, 
				"property sessionController of class " + this.getClass().getName() 
				+ " can not be null");
		Assert.notNull(i18nService, 
				"property i18nService of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @param maxLength
	 * @param service 
	 * @param value
	 * @param locale 
	 * @return a formatted elapsed time.
	 */
	public static String format(
			final int maxLength,
			final I18nService service, 
			final Object value, 
			final Locale locale) {
		if (value == null) {
			return null;
		}
		String subject = value.toString();
		if (subject.length() <= maxLength) {
			return subject;
		}
		String suffix = service.getString("CONTROL_PANEL.TRUNCATE_SUFFIX", locale);
		return subject.substring(0, maxLength - suffix.length()) + suffix;
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object o) {
		return format(maxLength, i18nService, o, sessionController.getLocale());
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

	/**
	 * @return the i18nService
	 */
	protected I18nService getI18nService() {
		return i18nService;
	}

	/**
	 * @param service the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}

	/**
	 * @return the maxLength
	 */
	protected int getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(final int maxLength) {
		this.maxLength = maxLength;
	}
	
}

