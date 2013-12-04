/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.reporting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.validator.UrlValidator;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.smtp.SmtpService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.services.feed.imap.messageId.MessageIdHandler;
import org.esupportail.helpdesk.services.urlGeneration.UrlBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

/**
 * An abstract email handler.
 */
public class AbstractSender implements InitializingBean, ApplicationContextAware {

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The URL builder.
	 */
	private UrlBuilder urlBuilder;

	/**
	 * {@link SmtpService}.
	 */
	private SmtpService smtpService;

	/**
	 * {@link I18nService}.
	 */
	private I18nService i18nService;

	/**
	 * {@link ApplicationService}.
	 */
	private ApplicationService applicationService;


	private ApplicationContext context;
	
	/**
	 * The message Id handler.
	 */
	private MessageIdHandler messageIdHandler;

	/**
	 * True to use the reply-to feature.
	 */
	private boolean useReplyTo;
	
	/**
	 * The email css path.
	 */
	private String emailCssPath;
	
	/**
	 * Bean constructor.
	 */
	protected AbstractSender() {
		super();
		useReplyTo = false;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.urlBuilder,
				"property urlBuilder of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.smtpService,
				"property smtpService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.i18nService,
				"property i18nService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.applicationService,
				"property applicationService of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(this.context,
				"property context of class " + this.getClass().getName()
				+ " can not be null");
		if (useReplyTo) {
            Assert.notNull(messageIdHandler,
                    "property messageIdHandler of class "
                    + this.getClass().getName() + " must be set");
		}
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________SEND() {
		//
	}

	/**
	 * @param ticket ticket for which the messageId should be generated
	 * @return Message-ID according to RFC 822 in form
	 * "&lt;xxx@yyy&gt;" or null for automatic generation
	 */
	protected String genMessageId(
			final Ticket ticket) {
    	if (!useReplyTo) {
    		// no messageID if reply-to is disabled
    		return null;
    	}
    	return messageIdHandler.genMessageId(ticket);
    }

	/**
	 * @return Message-ID according to RFC 822 or null for automatic generation
	 */
	protected String genMessageId() {
        return genMessageId(null);
    }

	/**
	 * Send something to an internet address.
	 * @param to
	 * @param locale
	 * @param messageId
	 * @param subject
	 * @param content
	 */
	protected void send(
			final InternetAddress to,
			final Locale locale,
			final String messageId,
			final String subject,
			final String content) {
		Locale theLocale = locale;
		if (theLocale == null) {
			theLocale = getI18nService().getDefaultLocale();
		}
		smtpService.send(
				to, subject,
				getEmailOrPrintHeader(theLocale, subject) + content
				+ getEmailOrPrintFooter(theLocale), null, messageId);
		if (logger.isDebugEnabled()) {
            logger.debug("email sent (MessageID: " + messageId + ")");
		}
	}

	/**
	 * Send something to an email address.
	 * @param email
	 * @param locale
	 * @param messageId
	 * @param subject
	 * @param content
	 * @return true if the message was sent.
	 */
	protected boolean send(
			final String email,
			final Locale locale,
			final String messageId,
			final String subject,
			final String content) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("sending an email to address [" + email + "]...");
			}
			InternetAddress to = new InternetAddress(email);
			send(to, locale, messageId, subject, content);
			return true;
		} catch (AddressException e) {
			logger.warn("invalid email [" + email + "]");
			return false;
		}
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________FORMATTING() {
		//
	}

	/**
	 * @param locale
	 * @param subject
	 * @return the HTML header for emails or prints.
	 */
	protected String getEmailOrPrintHeader(
			@SuppressWarnings("unused")
			final Locale locale,
			final String subject) {
		String htmlHeader = "<html><head><title>" + subject + "</title><style type=\"text/css\">\n<!--\n";
		try {
			InputStream is = new ClassPathResource("/properties/domain/email.css").getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = reader.readLine();
			while (line != null) {
				htmlHeader += line + "\n";
				line = reader.readLine();
			}
			is.close();
		} catch (IOException e) {
			throw new ConfigException(e);
		}
		if(StringUtils.hasText(getEmailCssPath())) {
			String emailResource  = new UrlValidator().isValid(getEmailCssPath()) ? getEmailCssPath() : "file:"+ getEmailCssPath();
			try {
				InputStream is = context.getResource(emailResource).getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line = reader.readLine();
				while (line != null) {
					htmlHeader += line + "\n";
					line = reader.readLine();
				}
				is.close();
			} catch (IOException e) {
				throw new ConfigException(e);
			}
		}
		htmlHeader += "\n-->\n</style></head><body>";
		return htmlHeader;
	}

	/**
	 * @param locale
	 * @return the HTML header for emails or prints.
	 */
	protected String getEmailOrPrintFooter(
			final Locale locale) {
		return i18nService.getString(
				"EMAIL.COMMON.COPYRIGHT", locale,
				getApplicationService().getName(),
				getApplicationService().getVersion(),
				getApplicationService().getCopyright())
				+ "</body></html>";
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________GETTERS_SETTERS() {
		//
	}

	/**
	 * @return the applicationService
	 */
	protected ApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @return the i18nService
	 */
	protected I18nService getI18nService() {
		return i18nService;
	}

	/**
	 * @return the smtpService
	 */
	protected SmtpService getSmtpService() {
		return smtpService;
	}

	/**
	 * @return the messageIdHandler
	 */
	protected MessageIdHandler getMessageIdHandler() {
		return messageIdHandler;
	}

	/**
	 * @return the useReplyTo
	 */
	protected boolean isUseReplyTo() {
		return useReplyTo;
	}

	/**
	 * @return the urlBuilder
	 */
	protected UrlBuilder getUrlBuilder() {
		return urlBuilder;
	}

	/**
	 * @return the emailCssPath
	 */
	protected String getEmailCssPath() {
		return emailCssPath;
	}

	/**
	 * @param emailCssPath the emailCssPath to set
	 */
	public void setEmailCssPath(final String emailCssPath) {
		this.emailCssPath = emailCssPath;
	}
	
	/**
	 * @param urlBuilder the urlBuilder to set
	 */
	public void setUrlBuilder(final UrlBuilder urlBuilder) {
		this.urlBuilder = urlBuilder;
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(final ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
	}
	
	/**
	 * @param smtpService the smtpService to set
	 */
	public void setSmtpService(final SmtpService smtpService) {
		this.smtpService = smtpService;
	}

	/**
	 * @param service the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}

	/**
	 * @param messageIdHandler the messageIdHandler to set
	 */
	public void setMessageIdHandler(final MessageIdHandler messageIdHandler) {
		this.messageIdHandler = messageIdHandler;
	}

	/**
	 * @param useReplyTo the useReplyTo to set
	 */
	public void setUseReplyTo(final boolean useReplyTo) {
		this.useReplyTo = useReplyTo;
	}

}
