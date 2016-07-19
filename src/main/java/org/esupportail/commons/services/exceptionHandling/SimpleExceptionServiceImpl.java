/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.exceptionHandling;
 
import java.util.Map;
import java.util.Set;

import org.esupportail.commons.beans.AbstractApplicationAwareBean;
import org.esupportail.commons.exceptions.ExceptionHandlingException;
import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.application.Version;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.services.authentication.info.AuthInfo;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.HttpUtils;
import org.esupportail.commons.utils.SystemUtils;
import org.esupportail.commons.utils.strings.StringUtils;

/**
 * A simple implementation of ExceptionService, that just logs the exceptions
 * and redirect to an throwable page.
 * 
 * See /properties/exceptionHandling/exceptionHandling-example.xml.
 */
public class SimpleExceptionServiceImpl 
extends AbstractApplicationAwareBean 
implements ExceptionService {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8092556694394385534L;

	/**
	 * The separator used in text reports.
	 */
	private static final String TEXT_HRULE = "\n-----------------------------------------------------------------"; 
	
	/**
	 * The separator used in text reports.
	 */
	private static final String TEXT_CR = "\n"; 
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The throwable caught.
	 */
	private Throwable throwable;
	
	/**
	 * The name of the application.
	 */
	private String applicationName;
	
	/**
	 * The version of the application.
	 */
	private Version applicationVersion;

	/**
	 * The server.
	 */
	private String server;
	
	/**
	 * The date.
	 */
	private Long date;
	
	/**
	 * The id of the current user.
	 */
	private String userId;
	
	/**
	 * The portal.
	 */
	private String portal;

	/**
	 * true if running a quick start installation.
	 */
	private Boolean quickStart;

	/**
	 * The client.
	 */
	private String client;

	/**
	 * The query strinng.
	 */
	private String queryString;

	/**
	 * The browser.
	 */
	private String userAgent;
	
	/**
	 * The global session attributes.
	 */
	private Set<String> globalSessionAttributes;
	
	/**
	 * The session attributes.
	 */
	private Set<String> sessionAttributes;
	
	/**
	 * The request attributes.
	 */
	private Set<String> requestAttributes;
	
	/**
	 * The headers of the request.
	 */
	private Set<String> requestHeaders;
	
	/**
	 * The parameters of the request.
	 */
	private Set<String> requestParameters;
	
	/**
	 * The cookies of the request.
	 */
	private Set<String> cookies;
	
	/**
	 * The system properties.
	 */
	private Set<String> systemProperties;
	
	/**
	 * The free memory.
	 */
	private long freeMemory;
	
	/**
	 * The max memory.
	 */
	private long maxMemory;
	
	/**
	 * The total memory.
	 */
	private long totalMemory;
	
	/**
	 * The views to redirect to.
	 */
	@SuppressWarnings("rawtypes")
	private Map<Class, String> exceptionViews;
	
	/**
	 * The log level.
	 */
	private String logLevel;
	
	/**
	 * The authentication service.
	 */
	private AuthenticationService authenticationService;
	
	/**
	 * Constructor.
	 * @param applicationService 
	 * @param i18nService 
	 * @param exceptionViews
	 * @param authenticationService 
	 * @param logLevel 
	 */
	public SimpleExceptionServiceImpl(
			final I18nService i18nService,
			final ApplicationService applicationService,
			@SuppressWarnings("rawtypes") final Map<Class, String> exceptionViews,
			final AuthenticationService authenticationService,
			final String logLevel) {
		super();
		setI18nService(i18nService);
		setApplicationService(applicationService);		
		this.exceptionViews = exceptionViews;
		this.authenticationService = authenticationService;
		this.logLevel = logLevel;
	}

	/**
	 * @param separator
	 * @return a StringBuffer.
	 */
	private StringBuffer getTextReportInformation(
			final String separator) {
		StringBuffer sb = new StringBuffer();
		sb.append(TEXT_HRULE);
		sb.append(TEXT_CR + getString("EXCEPTION.HEADER.INFORMATION"));
		sb.append(TEXT_HRULE);
		sb.append(TEXT_CR + getString("EXCEPTION.INFORMATION.APPLICATION") 
				+ separator + applicationName);
		sb.append(TEXT_CR + getString("EXCEPTION.INFORMATION.VERSION") 
				+ separator + applicationVersion);
		if (server == null) {
			server = getString("EXCEPTION.INFORMATION.SERVER.UNKNOWN");
		}
		sb.append(TEXT_CR + getString("EXCEPTION.INFORMATION.SERVER") 
				+ separator + server);
		String dateStr;
		if (date == null) { 
			dateStr = getString("EXCEPTION.INFORMATION.DATE.UNKNOWN");
		} else {
			dateStr = printableDate(date.longValue());
		}
		sb.append(TEXT_CR + getString("EXCEPTION.INFORMATION.DATE") 
				+ separator + dateStr);
		if (ContextUtils.isWeb()) {
			if (userId == null) {
				userId = getString("EXCEPTION.INFORMATION.USER_ID.UNKNOWN");
			}
			sb.append(TEXT_CR + getString("EXCEPTION.INFORMATION.USER_ID") 
					+ separator + userId);
			if (portal == null) {
				portal = getString("EXCEPTION.INFORMATION.PORTAL.UNKNOWN");
			} else {
				if (quickStart != null && quickStart.booleanValue()) {
					portal = portal + " " 
					+ getString("EXCEPTION.INFORMATION.PORTAL.QUICK_START");
				}
			}
			sb.append(TEXT_CR + getString("EXCEPTION.INFORMATION.PORTAL") 
					+ separator + portal);
			if (client == null) {
				client = getString("EXCEPTION.INFORMATION.CLIENT.UNKNOWN");
			}
			sb.append(TEXT_CR + getString("EXCEPTION.INFORMATION.CLIENT") 
					+ separator + client);
			if (queryString == null) {
				queryString = 
					getString("EXCEPTION.INFORMATION.QUERY_STRING.UNKNOWN");
			}
			sb.append(TEXT_CR + getString("EXCEPTION.INFORMATION.QUERY_STRING") 
					+ separator + queryString);
			if (userAgent == null) {
				userAgent = getString("EXCEPTION.INFORMATION.USER_AGENT.UNKNOWN");
			} else {
				userAgent = StringUtils.escapeHtml(userAgent);
			}
			sb.append(TEXT_CR + getString("EXCEPTION.INFORMATION.USER_AGENT") 
					+ separator + userAgent);
		}
		return sb;
	}

	/**
	 * @param separator 
	 * @return a StringBuffer.
	 */
	private StringBuffer getTextReportException(
			final String separator) {
		StringBuffer sb = new StringBuffer();
		if (throwable != null) {
			Throwable cause = ExceptionUtils.getRealCause(throwable);
			sb.append(TEXT_HRULE);
			sb.append(TEXT_CR + getString("EXCEPTION.HEADER.EXCEPTION"));
			sb.append(TEXT_HRULE);
			sb.append(TEXT_CR + getString("EXCEPTION.EXCEPTION.NAME") 
					+ separator + cause.getClass().getSimpleName());
			sb.append(TEXT_CR + getString("EXCEPTION.EXCEPTION.MESSAGE") 
					+ separator + cause.getMessage());
			sb.append(TEXT_CR + getString("EXCEPTION.EXCEPTION.SHORT_STACK_TRACE") 
					+ separator);
			sb.append(TEXT_CR + ExceptionUtils.getShortPrintableStackTrace(throwable));
			sb.append(TEXT_CR + getString("EXCEPTION.EXCEPTION.STACK_TRACE") 
					+ separator);
			sb.append(TEXT_CR + ExceptionUtils.getPrintableStackTrace(throwable));
		}
		return sb;
	}
	
	/**
	 * @param set 
	 * @return a printable String for text outputs.
	 */
	private String text(final Set<String> set) {
		if (set == null || set.isEmpty()) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		String separator = "";
		for (String string : set) {
			result.append(separator).append(string);
			separator = "\n";
		}
		return result.toString();
	}
	
	/**
	 * @return a StringBuffer.
	 */
	private StringBuffer getTextReportRequest() {
		String globalSessionAttributesStr = text(globalSessionAttributes);
		String sessionAttributesStr = text(sessionAttributes);
		String requestAttributesStr = text(requestAttributes);
		String requestHeadersStr = text(requestHeaders);
		String requestParametersStr = text(requestParameters);
		String cookiesStr = text(cookies);
		StringBuffer sb = new StringBuffer();
		sb.append(TEXT_HRULE);
		sb.append(TEXT_CR + getString("EXCEPTION.HEADER.REQUEST_ATTRIBUTES"));
		sb.append(TEXT_HRULE);
		if (requestAttributesStr == null) {
			requestAttributesStr = getString("EXCEPTION.REQUEST_ATTRIBUTES.NONE");
		}
		sb.append(TEXT_CR + requestAttributesStr);
		sb.append(TEXT_HRULE);
		sb.append(TEXT_CR + getString("EXCEPTION.HEADER.SESSION_ATTRIBUTES"));
		sb.append(TEXT_HRULE);
		if (sessionAttributesStr == null) {
			sessionAttributesStr = getString("EXCEPTION.SESSION_ATTRIBUTES.NONE");
		}
		sb.append(TEXT_CR + sessionAttributesStr);
		if (ContextUtils.isPortlet()) {
			sb.append(TEXT_HRULE);
			sb.append(TEXT_CR + getString("EXCEPTION.HEADER.GLOBAL_SESSION_ATTRIBUTES"));
			sb.append(TEXT_HRULE);
			if (globalSessionAttributesStr == null) {
				globalSessionAttributesStr = getString("EXCEPTION.GLOBAL_SESSION_ATTRIBUTES.NONE");
			}
			sb.append(TEXT_CR + globalSessionAttributesStr);
		}
		sb.append(TEXT_HRULE);
		sb.append(TEXT_CR + getString("EXCEPTION.HEADER.REQUEST_HEADERS"));
		sb.append(TEXT_HRULE);
		if (requestHeadersStr == null) {
			requestHeadersStr = getString("EXCEPTION.REQUEST_HEADERS.NONE");
		}
		sb.append(TEXT_CR + requestHeadersStr);
		sb.append(TEXT_HRULE);
		sb.append(TEXT_CR + getString("EXCEPTION.HEADER.REQUEST_PARAMETERS"));
		sb.append(TEXT_HRULE);
		if (requestParametersStr == null) {
			requestParametersStr = getString("EXCEPTION.REQUEST_PARAMETERS.NONE");
		}
		sb.append(TEXT_CR + requestParametersStr);
		sb.append(TEXT_HRULE);
		sb.append(TEXT_CR + getString("EXCEPTION.HEADER.COOKIES"));
		sb.append(TEXT_HRULE);
		if (cookiesStr == null) {
			cookiesStr = getString("EXCEPTION.COOKIES.NONE");
		}
		sb.append(TEXT_CR + cookiesStr);
		return sb;
	}
	
	/**
	 * @return a StringBuffer.
	 */
	private StringBuffer getTextReportProperties() {
		String systemPropertiesStr = text(systemProperties);
		StringBuffer sb = new StringBuffer();
		sb.append(TEXT_HRULE);
		sb.append(TEXT_CR + getString("EXCEPTION.HEADER.SYSTEM_PROPERTIES"));
		sb.append(TEXT_HRULE);
		sb.append(TEXT_CR + systemPropertiesStr);
		sb.append(TEXT_HRULE);
		sb.append(TEXT_CR + getString("EXCEPTION.HEADER.MEMORY"));
		sb.append(TEXT_HRULE);
		sb.append(TEXT_CR + getString("EXCEPTION.MEMORY.VALUES", freeMemory, totalMemory, maxMemory));
		return sb;
	}
	

	/**
	 * @return a plain/text report of the throwable.
	 */
	protected String getTextReport() {
		StringBuffer sb = new StringBuffer(getString("EXCEPTION.TITLE"));
		String separator = getString("EXCEPTION.SEPARATOR");
		sb.append(getTextReportInformation(separator));
		sb.append(getTextReportException(separator));
		if (ContextUtils.isWeb()) {
			sb.append(getTextReportRequest());
		}
		sb.append(getTextReportProperties());
		sb.append(TEXT_HRULE);
		return sb.toString();
	}
	
	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#setParameters(
	 * java.lang.Throwable)
	 */
	@Override
	public void setParameters(
			final Throwable t) throws ExceptionHandlingException {
		try {
			throwable = t;
			applicationName = getApplicationService().getName();
			applicationVersion = getApplicationService().getVersion();
			date = new Long(System.currentTimeMillis());
			if (ContextUtils.isPortlet()) {
				portal = HttpUtils.getPortalInfo();
			}
			quickStart = getApplicationService().isQuickStart();
			systemProperties = SystemUtils.getSystemPropertiesStrings();
			freeMemory = SystemUtils.getFreeMemory();
			maxMemory = SystemUtils.getMaxMemory();
			totalMemory = SystemUtils.getTotalMemory();
			if (ContextUtils.isWeb()) {
				server = HttpUtils.getServerString();
				if (authenticationService != null) {
					AuthInfo authInfo = null;
					try {
						authInfo = authenticationService.getAuthInfo();
						if (authInfo != null) {
							userId = authInfo.getId();
						}
					} catch (Throwable e) {
						// forget it...
					}
				}
				client = HttpUtils.getClientString();
				queryString = HttpUtils.getQueryString();
				userAgent = HttpUtils.getUserAgent();
				requestAttributes = ContextUtils.getRequestAttributesStrings();
				sessionAttributes = ContextUtils.getSessionAttributesStrings();
				if (ContextUtils.isPortlet()) {
					globalSessionAttributes = ContextUtils.getGlobalSessionAttributesStrings();
				}
				requestHeaders = HttpUtils.getRequestHeadersStrings();
				requestParameters = HttpUtils.getRequestParametersStrings();
				cookies = HttpUtils.getCookiesStrings();
			}
			if (server == null) {
				server = SystemUtils.getServer();
			}
		} catch (Throwable t2) {
			logger.error("The following error was thrown by the application:", t);
			logger.error("Another throwable was thrown while logging the first one (giving up):", t2);
			throw new ExceptionHandlingException(t2);
		}
	}

	/**
	 * Log a text report.
	 * @param t
	 * @param textReport
	 */
	protected void logTextReport(final Throwable t, final String textReport) {
		if (SimpleExceptionServiceFactoryImpl.ERROR.equals(logLevel.toLowerCase())) {
			logger.error(textReport);
		} else if (SimpleExceptionServiceFactoryImpl.WARN.equals(logLevel.toLowerCase())) {
			logger.error(ExceptionUtils.getShortPrintableStackTrace(t));
			logger.warn(textReport);
		} else if (SimpleExceptionServiceFactoryImpl.INFO.equals(logLevel.toLowerCase())) {
			logger.error(ExceptionUtils.getShortPrintableStackTrace(t));
			logger.info(textReport);
		} else if (SimpleExceptionServiceFactoryImpl.TRACE.equals(logLevel.toLowerCase())) {
			logger.error(ExceptionUtils.getShortPrintableStackTrace(t));
			if (logger.isTraceEnabled()) {
				logger.trace(textReport);
			}
		} else {
			logger.error(ExceptionUtils.getShortPrintableStackTrace(t));
			if (logger.isDebugEnabled()) {
				logger.debug(textReport);
			}
		}
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#handleException()
	 */
	@Override
	public void handleException() throws ExceptionHandlingException {
		// try to log the throwable
		try {
			logTextReport(getThrowable(), getTextReport());
		} catch (Throwable t) {
			logger.error("The following error was thrown by the application:", throwable);
			logger.error("Another throwable was thrown while logging the first one (giving up):", t);
			throw new ExceptionHandlingException(t);
		}
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getApplicationName()
	 */
	@Override
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getApplicationVersion()
	 */
	@Override
	public Version getApplicationVersion() {
		return applicationVersion;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getClient()
	 */
	@Override
	public String getClient() {
		return client;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getCookies()
	 */
	@Override
	public Set<String> getCookies() {
		return cookies;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getDate()
	 */
	@Override
	public Long getDate() {
		return date;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getPortal()
	 */
	@Override
	public String getPortal() {
		return portal;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getQueryString()
	 */
	@Override
	public String getQueryString() {
		return queryString;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getQuickStart()
	 */
	@Override
	public Boolean getQuickStart() {
		return quickStart;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getRequestHeaders()
	 */
	@Override
	public Set<String> getRequestHeaders() {
		return requestHeaders;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getRequestParameters()
	 */
	@Override
	public Set<String> getRequestParameters() {
		return requestParameters;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getServer()
	 */
	@Override
	public String getServer() {
		return server;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getRequestAttributes()
	 */
	@Override
	public Set<String> getRequestAttributes() {
		return requestAttributes;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getSessionAttributes()
	 */
	@Override
	public Set<String> getSessionAttributes() {
		return sessionAttributes;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getGlobalSessionAttributes()
	 */
	@Override
	public Set<String> getGlobalSessionAttributes() {
		return globalSessionAttributes;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getSystemProperties()
	 */
	@Override
	public Set<String> getSystemProperties() {
		return systemProperties;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getThrowable()
	 */
	@Override
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getUserAgent()
	 */
	@Override
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getUserId()
	 */
	@Override
	public String getUserId() {
		return userId;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getRecipientEmail()
	 */
	@Override
	public String getRecipientEmail() {
		return null;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionService#getExceptionView()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getExceptionView() {
		if (throwable != null) {
			for (@SuppressWarnings("rawtypes") Class clazz : exceptionViews.keySet()) {
				for (Throwable cause : ExceptionUtils.getCauses(throwable)) {
					@SuppressWarnings("rawtypes")
					Class causeClass = cause.getClass(); 
					if (clazz.isAssignableFrom(causeClass)) {
						return exceptionViews.get(clazz);
					}
				}
			}
		}
		return exceptionViews.get(Exception.class);
	}

}
