/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.exceptionHandling;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.smtp.SmtpService;


/**
 * An implementation of ExceptionService, that logs the exceptions, send
 * them to an email address and redirect to an exception page. The difference 
 * with EmailExceptionServiceImpl is that exceptions are cached (to prevent 
 * from to several emails for the same exception).
 * 
 * See /properties/exceptionHandling/exceptionHandling-example.xml.
 */
public class CachingEmailExceptionServiceImpl extends EmailExceptionServiceImpl {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6187631528084902371L;

	/**
	 * the cache.
	 */
	private Cache cache;
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
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
	 * @param cache 
	 * @param logLevel 
	 */
	public CachingEmailExceptionServiceImpl(
			final I18nService i18nService,
			final ApplicationService applicationService,
			@SuppressWarnings("rawtypes") final Map<Class, String> exceptionViews,
			@SuppressWarnings("rawtypes") final List<Class> noEmailExceptions,
			final AuthenticationService authenticationService,
			final SmtpService smtpService,
			final String recipientEmail,
			final boolean doNotSendExceptionReportsToDevelopers,
			final String develEmail,
			final Cache cache,
			final String logLevel) {
		super(
				i18nService, applicationService,
				exceptionViews, noEmailExceptions, authenticationService, smtpService, 
				recipientEmail, doNotSendExceptionReportsToDevelopers, develEmail, logLevel);
		this.cache = cache;
	}
	
	/**
	 * @param t
	 * @param to 
	 * @param interceipt 
	 * @return the cache key that corresponds to a user and an exception.
	 */
	private String getCacheKey(
			final Throwable t, 
			final InternetAddress to,
			final boolean interceipt) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(ExceptionUtils.getPrintableStackTrace(t).getBytes());
			return to.toString() + " - " + new String(md.digest()) + interceipt;
		} catch (NoSuchAlgorithmException e2) {
			throw new ConfigException(e2);
		}
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.EmailExceptionServiceImpl#sendEmail(
	 * boolean, java.lang.Throwable, javax.mail.internet.InternetAddress, 
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected void sendEmail(
			final boolean intercept,
			final Throwable t, 
			final InternetAddress to, 
			final String emailSubject, 
			final String htmlReport, 
			final String textReport) {
		String cacheKey = getCacheKey(t, to, intercept);
		if (logger.isDebugEnabled()) {
			logger.debug("cacheKey = [" + cacheKey + "]");
		}
		if (cache.get(cacheKey) == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("not found in cache");
			}
			super.sendEmail(intercept, t, to, emailSubject, htmlReport, textReport);
			cache.put(new Element(cacheKey, cacheKey));
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("found in cache");
			}
		}
	}

}
