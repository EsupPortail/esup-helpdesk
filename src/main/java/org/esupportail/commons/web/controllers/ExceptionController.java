/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.controllers;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.Version;
import org.esupportail.commons.services.exceptionHandling.ExceptionService;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * The exception bean, used by special view exception.jsp.
 */
public class ExceptionController implements InitializingBean, Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8543570306850756671L;

	/**
	 * A logger.
	 */
	private Logger logger = new LoggerImpl(getClass());
	
	/**
	 * When an exception is thrown, it is caught by 
	 * and stored as a request attribute named "exception".  
	 */
	public ExceptionController() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		//
	}

	/**
	 * @return the exceptionService that was stored in session when the exception was thrown.
	 */
	private ExceptionService getExceptionService() {
		return ExceptionUtils.getMarkedExceptionService();
	}

	/**
	 * @return the name of the application.
	 */
	public String getApplicationName() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getApplicationName();
	}

	/**
	 * @return the version of the application.
	 */
	public Version getApplicationVersion() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getApplicationVersion();
	}

	/**
	 * @return the server the application is running on.
	 */
	public String getServer() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getServer();
	}

	/**
	 * @return the date when the exception was thrown.
	 */
	public String getDate() {
		if (getExceptionService() == null) {
			return null;
		}
		return new SimpleDateFormat("EEE dd-MM-yyyy HH:mm:ss", Locale.getDefault())
		.format(new Timestamp(getExceptionService().getDate()));
	}

	/**
	 * @return the id of the current user.
	 */
	public String getUserId() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getUserId();
	}

	/**
	 * @return the portal the portlet is running on.
	 */
	public String getPortal() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getPortal();
	}

	/**
	 * @return the portal the portlet is running on.
	 */
	public Boolean getQuickStart() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getQuickStart();
	}

	/**
	 * @return the client.
	 */
	public String getClient() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getClient();
	}

	/**
	 * @return the version of the application.
	 */
	public String getQueryString() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getQueryString();
	}

	/**
	 * @return the browser.
	 */
	public String getUserAgent() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getUserAgent();
	}

	/**
	 * @return the global session attributes.
	 */
	public Set<String> getGlobalSessionAttributes() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getGlobalSessionAttributes();
	}

	/**
	 * @return the session attributes.
	 */
	public Set<String> getSessionAttributes() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getSessionAttributes();
	}

	/**
	 * @return the request attributes.
	 */
	public Set<String> getRequestAttributes() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getRequestAttributes();
	}

	/**
	 * @return the request headers.
	 */
	public Set<String> getRequestHeaders() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getRequestHeaders();
	}

	/**
	 * @return the request parameters.
	 */
	public Set<String> getRequestParameters() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getRequestParameters();
	}

	/**
	 * @return the cookies.
	 */
	public Set<String> getCookies() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getCookies();
	}

	/**
	 * @return the system properties.
	 */
	public Set<String> getSystemProperties() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getSystemProperties();
	}

	/**
	 * @return the throwable that was thrown.
	 */
	private Throwable getThrowable() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getThrowable();
	}

	/**
	 * @return the name of the exception that was thrown.
	 */
	public String getExceptionName() {
		Throwable t = getThrowable();
		if (t == null) {
			return null;
		}
		return ExceptionUtils.getRealCause(t).getClass().getSimpleName();
	}

	/**
	 * @return the message of the exception that was thrown.
	 */
	public String getExceptionMessage() {
		Throwable t = getThrowable();
		if (t == null) {
			return null;
		}
		return ExceptionUtils.getRealCause(t).getMessage();
	}

	/**
	 * @return the stack trace of the exception that was thrown.
	 */
	public List<String> getExceptionStackTrace() {
		Throwable t = getThrowable();
		if (t == null) {
			return null;
		}
		return ExceptionUtils.getStackTraceStrings(t);
	}

	/**
	 * @return the stack trace of the exception that was thrown.
	 */
	public List<String> getExceptionShortStackTrace() {
		Throwable t = getThrowable();
		if (t == null) {
			return null;
		}
		return ExceptionUtils.getShortStackTraceStrings(t);
	}

	/**
	 * @return the email the exception report was sent to.
	 */
	public String getRecipientEmail() {
		if (getExceptionService() == null) {
			return null;
		}
		return getExceptionService().getRecipientEmail();
	}
	
	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String restart() {
		Map<String, Object> resettables = BeanUtils.getBeansOfClass(Resettable.class);
		for (String name : resettables.keySet()) {
			if (logger.isDebugEnabled()) {
				logger.debug("trying to reset bean [" + name + "]...");
			}
			Object bean = resettables.get(name);
			if (bean == null) {
				throw new ConfigException("bean [" + name 
						+ "] is null, " 
						+ "application can not be restarted.");
			}
			if (!(bean instanceof Resettable)) {
				throw new ConfigException("bean [" + name 
						+ "] does not implement Resettable, " 
						+ "application can not be restarted.");
			}
			((Resettable) bean).reset();
			if (logger.isDebugEnabled()) {
				logger.debug("bean [" + name + "] was reset.");
			}
		}
		ExceptionUtils.unmarkExceptionCaught();
		return "applicationRestarted";
	}

	/**
	 * @param resettableNames the resettableNames to set
	 */
	public void setResettableNames(
			@SuppressWarnings("unused")
			final List<String> resettableNames) {
		throw new UnsupportedOperationException("property resettableNames is obsolete");
	}

}
