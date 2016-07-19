/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.exceptionHandling;
 
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.esupportail.commons.exceptions.ExceptionHandlingException;
import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.smtp.SmtpService;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.SystemUtils;
import org.esupportail.commons.utils.strings.StringUtils;

/**
 * An implementation of ExceptionService, that logs the exceptions, send
 * them to an email address and redirect to an exception page.
 * 
 * See /properties/exceptionHandling/exceptionHandling-example.xml.
 */
public class EmailExceptionServiceImpl extends SimpleExceptionServiceImpl {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -1899794329062663727L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(EmailExceptionServiceImpl.class);
	
	/**
	 * The SMTP service.
	 */
	private SmtpService smtpService;
	
	/**
	 * The email exception reports are sent to.
	 */
	private String recipientEmail;

	/**
	 * A flag set to true not to send exception reports to the developers (see getDevelEmail()).
	 */
	private boolean doNotSendExceptionReportsToDevelopers;
	
	/**
	 * The developers' email.
	 */
	private String develEmail;
	
	/**
	 * The exceptions that will generate no email.
	 */
	@SuppressWarnings("rawtypes")
	private List<Class> noEmailExceptions;

	/**
	 * Constructor.
	 * @param i18nService 
	 * @param applicationService 
	 * @param exceptionViews
	 * @param noEmailExceptions 
	 * @param authenticationService 
	 * @param smtpService 
	 * @param recipientEmail 
	 * @param doNotSendExceptionReportsToDevelopers 
	 * @param develEmail 
	 * @param logLevel 
	 */
	public EmailExceptionServiceImpl(
			final I18nService i18nService,
			final ApplicationService applicationService,
			@SuppressWarnings("rawtypes") final Map<Class, String> exceptionViews,
			@SuppressWarnings("rawtypes") final List<Class> noEmailExceptions,
			final AuthenticationService authenticationService,
			final SmtpService smtpService,
			final String recipientEmail,
			final boolean doNotSendExceptionReportsToDevelopers,
			final String develEmail,
			final String logLevel) {
		super(i18nService, applicationService, 
				exceptionViews, authenticationService, logLevel);
		this.smtpService = smtpService;
		this.recipientEmail = recipientEmail;
		this.doNotSendExceptionReportsToDevelopers = doNotSendExceptionReportsToDevelopers;
		this.develEmail = develEmail;
		this.noEmailExceptions = noEmailExceptions;
	}

	/**
	 * @return The email of the developpers.
	 */
	public String getDevelEmail() {
		return develEmail;
	}
	
	/**
	 * @return a HTML String that represents a row of one 2-cols header cell.
	 * @param title
	 */
	private String header(final String title) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n<tr>");
		sb.append("\n<th colspan=\"2\" align=\"left\">");
		sb.append(title);
		sb.append("\n</th>");
		sb.append("\n</tr>");
		return sb.toString();
	}

	/**
	 * @param str1 
	 * @param str2 
	 * @return a HTML String that represents a row of two cells.
	 */
	private String row2(final String str1, final String str2) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n<tr>");
		sb.append("\n<td valign=\"top\" width=\"10%\" nowrap=\"true\" >");
		sb.append(str1);
		sb.append("\n</td>");
		sb.append("\n<td width=\"100%\" >");
		sb.append(str2);
		sb.append("\n</td>");
		sb.append("\n</tr>");
		return sb.toString();
	}

	/**
	 * @param str 
	 * @return a HTML String that represents a row of one 2-cols cell.
	 */
	private String row(final String str) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n<tr>");
		sb.append("\n<td colspan=\"2\" align=\"left\">");
		sb.append(str);
		sb.append("\n</td>");
		sb.append("\n</tr>");
		return sb.toString();
	}
	
	/**
	 * Emphasize a HTML string.
	 * @param str 
	 * @return a String.
	 */
	private String em(final String str) {
		return new StringBuffer("<em>").append(str).append("</em>").toString();
	}

	/**
	 * wrap a string into a H1 tag.
	 * @param str 
	 * @return a String.
	 */
	private String h1(final String str) {
		return new StringBuffer("<h1>").append(str).append("</h1>").toString();
	}

	/**
	 * @return a StringBuffer.
	 */
	private StringBuffer getHtmlReportInformation() {
		StringBuffer sb = new StringBuffer();
		sb.append(header(getString("EXCEPTION.HEADER.INFORMATION")));
		sb.append(row2(getString("EXCEPTION.INFORMATION.APPLICATION"), 
				StringUtils.escapeHtml(getApplicationName())));
		sb.append(row2(getString("EXCEPTION.INFORMATION.VERSION"), 
				StringUtils.escapeHtml(getApplicationVersion().toString())));
		String serverStr;
		if (getServer() == null) {
			serverStr = em(getString("EXCEPTION.INFORMATION.SERVER.UNKNOWN"));
		} else {
			serverStr = StringUtils.escapeHtml(getServer());
		}
		sb.append(row2(getString("EXCEPTION.INFORMATION.SERVER"), serverStr));
		String dateStr = null;
		if (getDate() == null) {
			dateStr = em(getString("EXCEPTION.INFORMATION.DATE.UNKNOWN"));
		} else {
			dateStr = StringUtils.escapeHtml(printableDate(getDate()));
		}
		sb.append(row2(getString("EXCEPTION.INFORMATION.DATE"), dateStr));
		if (ContextUtils.isWeb()) {
			String userIdStr;
			if (getUserId() == null) {
				userIdStr = em(getString("EXCEPTION.INFORMATION.USER_ID.UNKNOWN"));
			} else {
				userIdStr = StringUtils.escapeHtml(getUserId());
			}
			sb.append(row2(getString("EXCEPTION.INFORMATION.USER_ID"), userIdStr));
			String portalStr;
			if (getPortal() == null) {
				portalStr = 
					em(getString("EXCEPTION.INFORMATION.PORTAL.UNKNOWN"));
			} else {
				portalStr = StringUtils.escapeHtml(getPortal());
				if (getQuickStart() != null && getQuickStart().booleanValue()) {
					portalStr = portalStr + " " 
					+ getString("EXCEPTION.INFORMATION.PORTAL.QUICK_START");
				}
				sb.append(row2(getString(
						"EXCEPTION.INFORMATION.PORTAL"), portalStr));
				String clientStr;
				if (getClient() == null) {
					clientStr = em(getString(
							"EXCEPTION.INFORMATION.CLIENT.UNKNOWN"));
				} else {
					clientStr = StringUtils.escapeHtml(getClient());
				}
				sb.append(row2(getString("EXCEPTION.INFORMATION.CLIENT"), clientStr));
				String queryStringStr;
				if (getQueryString() == null) {
					queryStringStr = 
						em(getString(
								"EXCEPTION.INFORMATION.QUERY_STRING.UNKNOWN"));
				} else {
					queryStringStr = StringUtils.escapeHtml(getQueryString());
				}
				sb.append(row2(
						getString("EXCEPTION.INFORMATION.QUERY_STRING"), 
						queryStringStr));
				String userAgentStr;
				if (getUserAgent() == null) {
					userAgentStr = em(getString(
							"EXCEPTION.INFORMATION.USER_AGENT.UNKNOWN"));
				} else {
					userAgentStr = StringUtils.escapeHtml(getUserAgent());
				}
				sb.append(row2(getString(
						"EXCEPTION.INFORMATION.USER_AGENT"), userAgentStr));
			}
		}
		return sb;
	}

	/**
	 * @param strings
	 * @return a printable String for text outputs.
	 */
	private String html(final Collection<String> strings) {
		if (strings == null || strings.isEmpty()) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		String separator = "";
		for (String string : strings) {
			result.append(separator).append(StringUtils.escapeHtml(string));
			separator = "<br>";
		}
		return result.toString();
	}
	
	/**
	 * @return a StringBuffer.
	 */
	private StringBuffer getHtmlReportException() {
		StringBuffer sb = new StringBuffer();
		sb.append(header(getString("EXCEPTION.HEADER.EXCEPTION")));
		String name = "";
		String message = "";
		String stackTrace = "";
		String shortStackTrace = "";
		Throwable t = getThrowable();
		if (t != null) {
			Throwable cause = ExceptionUtils.getRealCause(t);
			name = StringUtils.escapeHtml(cause.getClass().getSimpleName());
			message = StringUtils.escapeHtml(cause.getMessage());
			shortStackTrace = html(ExceptionUtils.getShortStackTraceStrings(t));
			stackTrace = html(ExceptionUtils.getStackTraceStrings(t));
		}
		sb.append(row2(getString("EXCEPTION.EXCEPTION.NAME"), 
				name));
		sb.append(row2(getString("EXCEPTION.EXCEPTION.MESSAGE"), 
				message));
		sb.append(row2(getString("EXCEPTION.EXCEPTION.SHORT_STACK_TRACE"), 
				shortStackTrace));
		sb.append(row2(getString("EXCEPTION.EXCEPTION.STACK_TRACE"), 
				stackTrace));
		return sb;
	}

	/**
	 * @return a StringBuffer.
	 */
	private StringBuffer getHtmlReportRequest() {
		String requestAttributesStr = html(getRequestAttributes());
		String sessionAttributesStr = html(getSessionAttributes());
		String globalSessionAttributesStr = html(getGlobalSessionAttributes());
		String requestHeadersStr = html(getRequestHeaders());
		String requestParametersStr = html(getRequestParameters());
		String cookiesStr = html(getCookies());
		StringBuffer sb = new StringBuffer();
		sb.append(header(getString("EXCEPTION.HEADER.REQUEST_ATTRIBUTES")));
		if (requestAttributesStr == null) {
			requestAttributesStr = 
				em(getString("EXCEPTION.REQUEST_ATTRIBUTES.NONE"));
		}
		sb.append(row(requestAttributesStr));
		sb.append(header(getString("EXCEPTION.HEADER.SESSION_ATTRIBUTES")));
		if (sessionAttributesStr == null) {
			sessionAttributesStr = 
				em(getString("EXCEPTION.SESSION_ATTRIBUTES.NONE"));
		}
		sb.append(row(sessionAttributesStr));
		sb.append(header(getString("EXCEPTION.HEADER.GLOBAL_SESSION_ATTRIBUTES")));
		if (globalSessionAttributesStr == null) {
			globalSessionAttributesStr = 
				em(getString("EXCEPTION.GLOBAL_SESSION_ATTRIBUTES.NONE"));
		}
		sb.append(row(globalSessionAttributesStr));
		sb.append(header(getString("EXCEPTION.HEADER.REQUEST_HEADERS")));
		if (requestHeadersStr == null) {
			requestHeadersStr = em(getString("EXCEPTION.REQUEST_HEADERS.NONE"));
		}
		sb.append(row(requestHeadersStr));
		sb.append(header(getString("EXCEPTION.HEADER.REQUEST_PARAMETERS")));
		if (requestParametersStr == null) {
			requestParametersStr = 
				em(getString("EXCEPTION.REQUEST_PARAMETERS.NONE"));
		}
		sb.append(row(requestParametersStr));
		sb.append(header(getString("EXCEPTION.HEADER.COOKIES")));
		if (cookiesStr == null) {
			cookiesStr = em(getString("EXCEPTION.COOKIES.NONE"));
		}
		sb.append(row(cookiesStr));
		return sb;
	}

	/**
	 * @return a StringBuffer.
	 */
	private StringBuffer getHtmlReportProperties() {
		String systemPropertiesStr = html(getSystemProperties());
		StringBuffer sb = new StringBuffer();
		sb.append(header(getString("EXCEPTION.HEADER.SYSTEM_PROPERTIES")));
		if (systemPropertiesStr == null) {
			systemPropertiesStr = 
				"\n" + em(getString("EXCEPTION.SYSTEM_PROPERTIES.NONE"));
		}
		sb.append(row(systemPropertiesStr));
		sb.append(header(getString("EXCEPTION.HEADER.MEMORY")));
		sb.append(row2(
				getString("EXCEPTION.MEMORY.FREE"), 
				getString("EXCEPTION.MEMORY.VALUE", SystemUtils.getFreeMemory())));
		sb.append(row2(
				getString("EXCEPTION.MEMORY.TOTAL"), 
				getString("EXCEPTION.MEMORY.VALUE", SystemUtils.getTotalMemory())));
		sb.append(row2(
				getString("EXCEPTION.MEMORY.MAX"), 
				getString("EXCEPTION.MEMORY.VALUE", SystemUtils.getMaxMemory())));
		return sb;
	}

	/**
	 * @return a HTML report of the exception.
	 */
	public String getHtmlReport() {
		StringBuffer sb = new StringBuffer(
				h1(getString("EXCEPTION.TITLE")));
		sb.append("\n<table border=\"1\">");
		sb.append(getHtmlReportInformation());
		sb.append(getHtmlReportException());
		if (ContextUtils.isWeb()) {
			sb.append(getHtmlReportRequest());
		}
		sb.append(getHtmlReportProperties());
		sb.append("\n</table>");
		return sb.toString();
	}

	/**
	 * @return the subject of the email.
	 */
	public String getReportSubject() {
		String serverStr = getServer();
		if (serverStr == null) {
			serverStr = getString("EXCEPTION.INFORMATION.SERVER.UNKNOWN");
		}
		String className = null;
		if (getThrowable() != null) {
			className = ExceptionUtils.getRealCause(getThrowable()).getClass().getSimpleName();
		}
		return getString(
				"EXCEPTION.EMAIL_SUBJECT", 
				getApplicationName(), 
				getApplicationVersion(), 
				className, 
				serverStr);
	}

	/**
	 * Send an exception report. This method has been added to allow 
	 * it to be overriden (see org.esupportail.commons.services.exceptionHandling.CachingEmailExceptionServiceImpl).
	 * @param intercept
	 * @param t
	 * @param to the recipient
	 * @param emailSubject the subject of the mail
	 * @param htmlReport the HTML body
	 * @param textReport the plain text body
	 */
	@SuppressWarnings("unchecked")
	protected void sendEmail(
			final boolean intercept,
			final Throwable t,
			final InternetAddress to,
			final String emailSubject,
			final String htmlReport,
			final String textReport) {
		for (Throwable cause : ExceptionUtils.getCauses(t)) {
			@SuppressWarnings("rawtypes")
			Class causeClass = cause.getClass(); 
			for (@SuppressWarnings("rawtypes") Class clazz : noEmailExceptions) {
				if (clazz.isAssignableFrom(causeClass)) {
					if (logger.isDebugEnabled()) {
						logger.debug("no email for exception class [" + causeClass + "]");
					}
					return;
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("sending the exception report to '" + to + "'...");
		}
		if (intercept) {
			smtpService.send(to, emailSubject, htmlReport, textReport);
		} else {
			smtpService.sendDoNotIntercept(to, emailSubject, htmlReport, textReport);
		}
		logger.debug("done.");
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.SimpleExceptionServiceImpl#handleException()
	 */
	@Override
	public void handleException() throws ExceptionHandlingException {
		String textReport = null;
		// try to logger the exception
		try {
			textReport = getTextReport();
			logTextReport(getThrowable(), textReport);
		} catch (Exception e) {
			logger.error("The following error was thrown by the application:", getThrowable());
			logger.error("Another exception was thrown while logging the first one (giving up):", e);
			throw new ExceptionHandlingException(e);
		}
		// finally try to send emails
		try {
			String htmlReport = getHtmlReport();
			String emailSubject = getReportSubject();
			if (this.recipientEmail != null) {
				InternetAddress to = null;
				try {
					to = new InternetAddress(this.recipientEmail);
				} catch (AddressException e) {
					// already tested when the bean was created
				}
				sendEmail(true, getThrowable(), to, emailSubject, htmlReport, textReport);
			}
			if (!this.doNotSendExceptionReportsToDevelopers && getDevelEmail() != null) {
				InternetAddress to = null;
				try {
					to = new InternetAddress(getDevelEmail());
				} catch (AddressException e) {
					// already tested when the bean was created
				}
				sendEmail(false, getThrowable(), to, emailSubject, htmlReport, textReport);
			}
		} catch (Exception e) {
			logger.error("The following error was thrown by the application:", getThrowable());
			logger.error("another exception was thrown while sending an email alert (giving up):", e);
			throw new ExceptionHandlingException(e);
		}
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.SimpleExceptionServiceImpl#getRecipientEmail()
	 */
	@Override
	public String getRecipientEmail() {
		return recipientEmail;
	}

}
