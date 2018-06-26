/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.exceptionHandling;
 
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.smtp.SmtpService;
import org.esupportail.commons.utils.Assert;
import org.springframework.util.StringUtils;

/**
 * An implementation of ExceptionService, that logs the exceptions, send
 * them to an email address and redirect to an exception page.
 * 
 * See /properties/exceptionHandling/exceptionHandling-example.xml.
 */
public class EmailExceptionServiceFactoryImpl extends SimpleExceptionServiceFactoryImpl {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 443761296903602049L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(EmailExceptionServiceFactoryImpl.class);
	
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
	 * No email sent for these exceptions.
	 */
	@SuppressWarnings({ "rawtypes" })
	private List<Class> noEmailExceptions;

	/**
	 * Bean constructor.
	 */
	@SuppressWarnings({ "rawtypes" })
	public EmailExceptionServiceFactoryImpl() {
		super();
		this.noEmailExceptions = new ArrayList<Class>();
	}

	/**
	 * @return The email of the developpers.
	 */
	public String getDevelEmail() {
		return null;
	}
	
	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@SuppressWarnings("unused")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.smtpService, 
				"property smtpService of class " + this.getClass().getName() + " can not be null");
		if (!StringUtils.hasText(recipientEmail)) {
			recipientEmail = null;
			logger.info(this.getClass().getName() + ".recipientEmail is null, no email will be sent.");
		} else {
			// test if the address is well-formed
			try {
				new InternetAddress(this.recipientEmail);
			} catch (AddressException e) {
				throw new IllegalArgumentException(
						getClass() + ".recipientEmail '" 
						+ this.recipientEmail + "' is not a valid email address");
			}
		}
	}
	
	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionServiceFactory#getExceptionService()
	 */
	@Override
	public ExceptionService getExceptionService() {
		return new EmailExceptionServiceImpl(
				getI18nService(), getApplicationService(), 
				getExceptionViews(), getNoEmailExceptions(), getAuthenticationService(), 
				smtpService, recipientEmail, doNotSendExceptionReportsToDevelopers, 
				getDevelEmail(), getLogLevel());
	}

	/**
	 * @param smtpService The smtpService to set.
	 */
	public void setSmtpService(final SmtpService smtpService) {
		this.smtpService = smtpService;
	}

	/**
	 * @param recipientEmail The recipientEmail to set.
	 */
	public void setRecipientEmail(final String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}
	
	/**
	 * @param doNotSendExceptionReportsToDevelopers The doNotSendExceptionReportsToDevelopers to set.
	 */
	public void setDoNotSendExceptionReportsToDevelopers(
			final boolean doNotSendExceptionReportsToDevelopers) {
		this.doNotSendExceptionReportsToDevelopers = doNotSendExceptionReportsToDevelopers;
	}

	/**
	 * @return the smtpService
	 */
	protected SmtpService getSmtpService() {
		return smtpService;
	}

	/**
	 * @return the doNotSendExceptionReportsToDevelopers
	 */
	protected boolean isDoNotSendExceptionReportsToDevelopers() {
		return doNotSendExceptionReportsToDevelopers;
	}

	/**
	 * @return the recipientEmail
	 */
	protected String getRecipientEmail() {
		return recipientEmail;
	}

	/**
	 * Add a 'no email' exception.
	 * @param className 
	 */
	private void addNoEmailException(final String className) {
		try {
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(className);
			if (!(Exception.class.isAssignableFrom(clazz))) {
				throw new ConfigException("class [" + className 
						+ "] is not a subclass of [" + Exception.class + "]");
			}
			this.noEmailExceptions.add(clazz);
		} catch (ClassNotFoundException e) {
			throw new ConfigException(e);
		}
	}

	/**
	 * @param noEmailExceptions the noEmailExceptions to set
	 */
	public void setNoEmailExceptions(final List<String> noEmailExceptions) {
		for (String className : noEmailExceptions) {
			addNoEmailException(className);
		}
	}

	/**
	 * @return the noEmailExceptions
	 */
	@SuppressWarnings("rawtypes")
	protected List<Class> getNoEmailExceptions() {
		return noEmailExceptions;
	}

}
