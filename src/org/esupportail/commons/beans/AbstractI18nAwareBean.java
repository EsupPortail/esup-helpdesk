/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.beans;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.ContextUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * An abstract class inherited by all the beans for them to gain i18n features.
 */
@SuppressWarnings("serial")
public abstract class AbstractI18nAwareBean implements InitializingBean, Serializable {

	/** 
	 * The name of the session attribute that stores the current locale.
	 */
	public static final String LOCALE_ATTRIBUTE = "locale";
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(this.getClass());
	
	/**
	 * see {@link I18nService}.
	 */
	private I18nService i18nService;
	
	/**
	 * The Timezone.
	 */
	private String timezone;

	/**
	 * Constructor.
	 */
	protected AbstractI18nAwareBean() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.i18nService, 
				"property i18nService of class " + this.getClass().getName() 
				+ " can not be null");
		if (timezone == null) {
			timezone = TimeZone.getDefault().getDisplayName();
		}
	}

	/**
	 * @return the locale stored in session.
	 */
	protected Locale getSessionLocale() {
		if (ContextUtils.isWeb()) {
			return (Locale) ContextUtils.getSessionAttribute(LOCALE_ATTRIBUTE);
		}
		return LocaleContextHolder.getLocale();
	}

	/**
	 * Set the session locale.
	 * @param locale 
	 */
	protected void setSessionLocale(final Locale locale) {
		if (ContextUtils.isWeb()) {
			ContextUtils.setSessionAttribute(LOCALE_ATTRIBUTE, locale);
		} else {
			LocaleContextHolder.setLocale(locale);
		}
	}

	/**
	 * Reset the session locale.
	 */
	public void resetSessionLocale() {
		setSessionLocale(null);
	}

	/**
	 * This method should be overridden to return the current user's locale,
	 * depending on the logic of the application.
	 * @return the current user's locale.
	 */
	protected Locale getCurrentUserLocale() {
		return null;
	}

	/**
	 * @return the locale.
	 */
	public Locale getLocale() {
		if (logger.isDebugEnabled()) {
			logger.debug(this.getClass().getName() + ".getLocale()");
		}
		// try to get it from the session
		Locale locale = getSessionLocale(); 
		if (locale == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("no locale in session, look at the current user");
			}
			// try to get it from the current user
			locale = getCurrentUserLocale();
			if (locale == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("no locale set for the current user, look at JSF");
				}
				locale = i18nService.getDefaultLocale();
			}
			// store it in the session
			setSessionLocale(locale);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("locale = [" + locale + "]");
		}
		return locale;
	}
	
	/**
	 * @param service the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}

	/**
	 * @return the i18nService
	 */
	public I18nService getI18nService() {
		return i18nService;
	}

	/**
	 * @return a FacesMessage that corresponds to a message and a severity level.
	 * @param severity the severity
	 * @param message the message itself
	 */
	private FacesMessage getFacesFormattedMessage(
			final Severity severity, 
			final String message) {
		return new FacesMessage(severity, message, null);
	}

	/**
	 * @return a FacesMessage that corresponds to a message and a severity level.
	 * @param severity the severity
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 * @param i18nArg3 
	 */
	private FacesMessage getFacesMessage(
			final Severity severity, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2, 
			final Object i18nArg3) {
		return getFacesFormattedMessage(
				severity, i18nService.getString(i18nMessage, i18nArg0, i18nArg1, i18nArg2, i18nArg3));
	}

	/**
	 * @return a FacesMessage that corresponds to a message and a severity level.
	 * @param severity the severity
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 */
	private FacesMessage getFacesMessage(
			final Severity severity, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2) {
		return getFacesFormattedMessage(
				severity, i18nService.getString(i18nMessage, i18nArg0, i18nArg1, i18nArg2));
	}

	/**
	 * @return a FacesMessage that corresponds to a message and a severity level.
	 * @param severity the severity
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 */
	private FacesMessage getFacesMessage(
			final Severity severity, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1) {
		return getFacesFormattedMessage(severity, i18nService.getString(i18nMessage, i18nArg0, i18nArg1));
	}

	/**
	 * @return a FacesMessage that corresponds to a message and a severity level.
	 * @param severity the severity
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 */
	private FacesMessage getFacesMessage(
			final Severity severity, 
			final String i18nMessage, 
			final Object i18nArg0) {
		return getFacesFormattedMessage(severity, i18nService.getString(i18nMessage, i18nArg0));
	}

	/**
	 * @return a FacesMessage that corresponds to a message and a severity level.
	 * @param severity the severity
	 * @param i18nMessage the key of the message in the i18n bundles
	 */
	private FacesMessage getFacesMessage(
			final Severity severity, 
			final String i18nMessage) {
		return getFacesFormattedMessage(severity, i18nService.getString(i18nMessage));
	}

	/**
	 * @return a warn FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 * @param i18nArg3 
	 */
	public FacesMessage getFacesWarnMessage(
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2, 
			final Object i18nArg3) {
		return getFacesMessage(FacesMessage.SEVERITY_WARN, i18nMessage, i18nArg0, i18nArg1, i18nArg2, i18nArg3);
	}

	/**
	 * @return a warn FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 */
	public FacesMessage getFacesWarnMessage(
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2) {
		return getFacesMessage(FacesMessage.SEVERITY_WARN, i18nMessage, i18nArg0, i18nArg1, i18nArg2);
	}

	/**
	 * @return a warn FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 */
	public FacesMessage getFacesWarnMessage(
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1) {
		return getFacesMessage(FacesMessage.SEVERITY_WARN, i18nMessage, i18nArg0, i18nArg1);
	}

	/**
	 * @return a warn FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 */
	public FacesMessage getFacesWarnMessage(
			final String i18nMessage, 
			final Object i18nArg0) {
		return getFacesMessage(FacesMessage.SEVERITY_WARN, i18nMessage, i18nArg0);
	}

	/**
	 * @return a warn FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 */
	public FacesMessage getFacesWarnMessage(
			final String i18nMessage) {
		return getFacesMessage(FacesMessage.SEVERITY_WARN, i18nMessage);
	}

	/**
	 * @return an error FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 * @param i18nArg3 
	 */
	public FacesMessage getFacesErrorMessage(
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2, 
			final Object i18nArg3) {
		return getFacesMessage(
				FacesMessage.SEVERITY_ERROR, i18nMessage, i18nArg0, i18nArg1, i18nArg2, i18nArg3);
	}

	/**
	 * @return an error FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 */
	public FacesMessage getFacesErrorMessage(
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2) {
		return getFacesMessage(FacesMessage.SEVERITY_ERROR, i18nMessage, i18nArg0, i18nArg1, i18nArg2);
	}

	/**
	 * @return an error FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 */
	public FacesMessage getFacesErrorMessage(
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1) {
		return getFacesMessage(FacesMessage.SEVERITY_ERROR, i18nMessage, i18nArg0, i18nArg1);
	}

	/**
	 * @return an error FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 */
	public FacesMessage getFacesErrorMessage(
			final String i18nMessage, 
			final Object i18nArg0) {
		return getFacesMessage(FacesMessage.SEVERITY_ERROR, i18nMessage, i18nArg0);
	}

	/**
	 * @return an error FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 */
	public FacesMessage getFacesErrorMessage(
			final String i18nMessage) {
		return getFacesMessage(FacesMessage.SEVERITY_ERROR, i18nMessage);
	}

	/**
	 * @return an info FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 * @param i18nArg3 
	 */
	public FacesMessage getFacesInfoMessage(
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2, 
			final Object i18nArg3) {
		return getFacesMessage(FacesMessage.SEVERITY_INFO, i18nMessage, i18nArg0, i18nArg1, i18nArg2, i18nArg3);
	}

	/**
	 * @return an info FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 */
	public FacesMessage getFacesInfoMessage(
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2) {
		return getFacesMessage(FacesMessage.SEVERITY_INFO, i18nMessage, i18nArg0, i18nArg1, i18nArg2);
	}

	/**
	 * @return an info FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 */
	public FacesMessage getFacesInfoMessage(
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1) {
		return getFacesMessage(FacesMessage.SEVERITY_INFO, i18nMessage, i18nArg0, i18nArg1);
	}

	/**
	 * @return an info FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 */
	public FacesMessage getFacesInfoMessage(
			final String i18nMessage, 
			final Object i18nArg0) {
		return getFacesMessage(FacesMessage.SEVERITY_INFO, i18nMessage, i18nArg0);
	}

	/**
	 * @return an info FacesMessage that corresponds to a message and a severity level.
	 * @param i18nMessage the key of the message in the i18n bundles
	 */
	public FacesMessage getFacesInfoMessage(
			final String i18nMessage) {
		return getFacesMessage(FacesMessage.SEVERITY_INFO, i18nMessage);
	}

	/**
	 * Add to the current context a message.
	 * @param severity the severity
	 * @param clientId the id of client that should receive the message
	 * @param message the message itself
	 */
	private void addFormattedMessage(
			final Severity severity, 
			final String clientId, 
			final String message) {
		FacesMessage errorMessage = getFacesFormattedMessage(severity, message);
		FacesContext.getCurrentInstance().addMessage(clientId, errorMessage);
	}

	/**
	 * Add to the current context a formatted error message.
	 * @param clientId the id of client that should receive the message
	 * @param message the message itself
	 */
	public void addFormattedError(
			final String clientId, 
			final String message) {
		addFormattedMessage(FacesMessage.SEVERITY_ERROR, clientId, message);
	}

	/**
	 * Add to the current context a formatted info message.
	 * @param clientId the id of client that should receive the message
	 * @param message the message itself
	 */
	public void addFormattedInfo(
			final String clientId, 
			final String message) {
		addFormattedMessage(FacesMessage.SEVERITY_INFO, clientId, message);
	}

	/**
	 * Add to the current context a formatted warn message.
	 * @param clientId the id of client that should receive the message
	 * @param message the message itself
	 */
	public void addFormattedWarn(
			final String clientId, 
			final String message) {
		addFormattedMessage(FacesMessage.SEVERITY_WARN, clientId, message);
	}

	/**
	 * Add to the current context a message.
	 * @param severity the severity
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 * @param i18nArg3 
	 */
	private void addMessage(
			final Severity severity, 
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2, 
			final Object i18nArg3) {
		String message = i18nService.getString(
				i18nMessage, getLocale(), i18nArg0, i18nArg1, i18nArg2, i18nArg3);
		addFormattedMessage(severity, clientId, message);
	}

	/**
	 * Add to the current context a message.
	 * @param severity the severity
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 */
	private void addMessage(
			final Severity severity, 
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2) {
		String message = i18nService.getString(i18nMessage, getLocale(), i18nArg0, i18nArg1, i18nArg2);
		addFormattedMessage(severity, clientId, message);
	}

	/**
	 * Add to the current context a message.
	 * @param severity the severity
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 */
	private void addMessage(
			final Severity severity, 
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1) {
		String message = i18nService.getString(i18nMessage, getLocale(), i18nArg0, i18nArg1);
		addFormattedMessage(severity, clientId, message);
	}

	/**
	 * Add to the current context a message.
	 * @param severity the severity
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 */
	private void addMessage(
			final Severity severity, 
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0) {
		String message = i18nService.getString(i18nMessage, getLocale(), i18nArg0);
		addFormattedMessage(severity, clientId, message);
	}

	/**
	 * Add to the current context a message.
	 * @param severity the severity
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 */
	private void addMessage(
			final Severity severity, 
			final String clientId, 
			final String i18nMessage) {
		String message = i18nService.getString(i18nMessage, getLocale());
		addFormattedMessage(severity, clientId, message);
	}

	/**
	 * Add to the current context a error message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 * @param i18nArg3 
	 */
	protected void addWarnMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2, 
			final Object i18nArg3) {
		addMessage(FacesMessage.SEVERITY_WARN, clientId, i18nMessage, i18nArg0, i18nArg1, i18nArg2, i18nArg3);
	}

	/**
	 * Add to the current context a error message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 */
	protected void addWarnMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2) {
		addMessage(FacesMessage.SEVERITY_WARN, clientId, i18nMessage, i18nArg0, i18nArg1, i18nArg2);
	}

	/**
	 * Add to the current context a error message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 */
	protected void addWarnMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1) {
		addMessage(FacesMessage.SEVERITY_WARN, clientId, i18nMessage, i18nArg0, i18nArg1);
	}

	/**
	 * Add to the current context a error message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 */
	protected void addWarnMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0) {
		addMessage(FacesMessage.SEVERITY_WARN, clientId, i18nMessage, i18nArg0);
	}

	/**
	 * Add to the current context a error message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 */
	protected void addWarnMessage(
			final String clientId, 
			final String i18nMessage) {
		addMessage(FacesMessage.SEVERITY_WARN, clientId, i18nMessage);
	}

	/**
	 * Add to the current context a error message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 * @param i18nArg3 
	 */
	protected void addErrorMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2, 
			final Object i18nArg3) {
		addMessage(FacesMessage.SEVERITY_ERROR, clientId, i18nMessage, i18nArg0, i18nArg1, i18nArg2, i18nArg3);
	}

	/**
	 * Add to the current context a error message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 */
	protected void addErrorMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2) {
		addMessage(FacesMessage.SEVERITY_ERROR, clientId, i18nMessage, i18nArg0, i18nArg1, i18nArg2);
	}

	/**
	 * Add to the current context a error message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 */
	protected void addErrorMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1) {
		addMessage(FacesMessage.SEVERITY_ERROR, clientId, i18nMessage, i18nArg0, i18nArg1);
	}

	/**
	 * Add to the current context a error message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 */
	protected void addErrorMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0) {
		addMessage(FacesMessage.SEVERITY_ERROR, clientId, i18nMessage, i18nArg0);
	}

	/**
	 * Add to the current context a error message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 */
	protected void addErrorMessage(
			final String clientId, 
			final String i18nMessage) {
		addMessage(FacesMessage.SEVERITY_ERROR, clientId, i18nMessage);
	}

	/**
	 * Add to the current context an info message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 * @param i18nArg3 
	 */
	protected void addInfoMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2, 
			final Object i18nArg3) {
		addMessage(FacesMessage.SEVERITY_INFO, clientId, i18nMessage, i18nArg0, i18nArg1, i18nArg2, i18nArg3);
	}

	/**
	 * Add to the current context an info message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 * @param i18nArg2 
	 */
	protected void addInfoMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1, 
			final Object i18nArg2) {
		addMessage(FacesMessage.SEVERITY_INFO, clientId, i18nMessage, i18nArg0, i18nArg1, i18nArg2);
	}

	/**
	 * Add to the current context an info message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 * @param i18nArg1 
	 */
	protected void addInfoMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0, 
			final Object i18nArg1) {
		addMessage(FacesMessage.SEVERITY_INFO, clientId, i18nMessage, i18nArg0, i18nArg1);
	}

	/**
	 * Add to the current context an info message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 * @param i18nArg0 
	 */
	protected void addInfoMessage(
			final String clientId, 
			final String i18nMessage, 
			final Object i18nArg0) {
		addMessage(FacesMessage.SEVERITY_INFO, clientId, i18nMessage, i18nArg0);
	}

	/**
	 * Add to the current context an info message.
	 * @param clientId the id of client that should receive the message
	 * @param i18nMessage the key of the message in the i18n bundles
	 */
	protected void addInfoMessage(
			final String clientId, 
			final String i18nMessage) {
		addMessage(FacesMessage.SEVERITY_INFO, clientId, i18nMessage);
	}

	/**
	 * Add to the current context a message saying that the action is not authorized.
	 */
	protected void addUnauthorizedActionMessage() {
		addErrorMessage(null, "_.MESSAGE.UNAUTHORIZED_ACTION");
	}

	/**
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	protected String getString(
			final String key, final Object arg0, final Object arg1, 
			final Object arg2, final Object arg3, final Object arg4) {
		return getString(key, getLocale(), arg0, arg1, arg2, arg3, arg4);
	}

	/**
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	protected String getString(
			final String key, final Object arg0, final Object arg1, final Object arg2, final Object arg3) {
		return getString(key, getLocale(), arg0, arg1, arg2, arg3);
	}

	/**
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	protected String getString(final String key, final Object arg0, final Object arg1, final Object arg2) {
		return getString(key, getLocale(), arg0, arg1, arg2);
	}

	/**
	 * @param key
	 * @param arg0
	 * @param arg1
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object)
	 */
	protected String getString(final String key, final Object arg0, final Object arg1) {
		return getString(key, getLocale(), arg0, arg1);
	}

	/**
	 * @param key
	 * @param arg0
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(java.lang.String, java.lang.Object)
	 */
	protected String getString(final String key, final Object arg0) {
		return getString(key, getLocale(), arg0);
	}

	/**
	 * @param key
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(java.lang.String)
	 */
	protected String getString(final String key) {
		return getString(key, getLocale());
	}

	/**
	 * @param key
	 * @param locale 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	protected String getString(
			final String key, final Locale locale, final Object arg0, final Object arg1, 
			final Object arg2, final Object arg3, final Object arg4) {
		return i18nService.getString(key, locale, arg0, arg1, arg2, arg3, arg4);
	}

	/**
	 * @param key
	 * @param locale 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	protected String getString(
			final String key, final Locale locale, final Object arg0, 
			final Object arg1, final Object arg2, final Object arg3) {
		return i18nService.getString(key, locale, arg0, arg1, arg2, arg3);
	}

	/**
	 * @param key
	 * @param locale 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	protected String getString(final String key, final Locale locale, 
			final Object arg0, final Object arg1, final Object arg2) {
		return i18nService.getString(key, locale, arg0, arg1, arg2);
	}

	/**
	 * @param key
	 * @param locale 
	 * @param arg0
	 * @param arg1
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(
	 * java.lang.String, java.lang.Object, java.lang.Object)
	 */
	protected String getString(final String key, final Locale locale, final Object arg0, final Object arg1) {
		return i18nService.getString(key, locale, arg0, arg1);
	}

	/**
	 * @param key
	 * @param locale 
	 * @param arg0
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(java.lang.String, java.lang.Object)
	 */
	protected String getString(final String key, final Locale locale, final Object arg0) {
		return i18nService.getString(key, locale, arg0);
	}

	/**
	 * @param key
	 * @param locale 
	 * @return a formatted internationalized string
	 * @see org.esupportail.commons.services.i18n.I18nService#getString(java.lang.String)
	 */
	protected String getString(final String key, final Locale locale) {
		return i18nService.getString(key, locale);
	}

	/**
	 * @param locale
	 * @return A map of the strings corresponding to a locale.
	 * @see org.esupportail.commons.services.i18n.I18nService#getStrings(java.util.Locale)
	 */
	protected Map<String, String> getStrings(final Locale locale) {
		return i18nService.getStrings(locale);
	}

	/**
	 * @param date
	 * @return the date in a printable form.
	 * @see org.esupportail.commons.services.i18n.I18nService#printableDate(long)
	 */
	protected String printableDate(final long date) {
		return i18nService.printableDate(date, getLocale());
	}

	/**
	 * @param date
	 * @return the date in a printable and relative form.
	 * @see org.esupportail.commons.services.i18n.I18nService#printableRelativeDate(long)
	 */
	protected String printableRelativeDate(final long date) {
		return i18nService.printableRelativeDate(date, getLocale());
	}

	/**
	 * @return the timeZone
	 * @deprecated
	 */
	@Deprecated
	public String getTimeZone() {
		return getTimezone();
	}

	/**
	 * @param timeZone the timeZone to set
	 * @deprecated
	 */
	@Deprecated
	public void setTimeZone(final String timeZone) {
		setTimezone(timeZone);
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(final String timezone) {
		this.timezone = timezone;
	}

}
