/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userFormatting; 

import java.io.Serializable;
import java.util.Locale;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.User;
import org.springframework.beans.factory.InitializingBean;

/** 
 * A basic implementation of UserFormattingService.
 */ 
public class UserFormattingServiceImpl 
implements Serializable, InitializingBean, UserFormattingService {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8862589904539018539L;

	/**
	 * True to print the display name and the id, false to print the display name only.
	 */
	private boolean printId;
	
	/**
	 * The max length for the display name (0 not to truncate). 
	 */
	private int displayNameMaxLength;
	
	/**
	 * The max length for the id (0 not to truncate). 
	 */
	private int idMaxLength;
	
	/**
	 * The i18n service.
	 */
	private I18nService i18nService;
	
	/**
	 * Bean constructor.
	 */
	public UserFormattingServiceImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(i18nService, 
				"property i18nService of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @param input
	 * @param maxLength 
	 * @param locale 
	 * @return a truncated string.
	 */
	protected String truncate(
			final String input,
			final int maxLength,
			final Locale locale) {
		if (input == null) {
			return null;
		}
		if (maxLength <= 0) {
			return input;
		}
		if (input.length() <= maxLength) {
			return input;
		}
		String suffix = i18nService.getString("USER.TRUNCATE_SUFFIX", locale);
		return input.substring(0, maxLength - suffix.length()) + suffix;
	}

	/**
	 * @param user
	 * @param locale 
	 * @return the truncated display name for the user.
	 */
	protected String truncateDisplayName(
			final User user,
			final Locale locale) {
		return truncate(user.getDisplayName(), displayNameMaxLength, locale);
	}

	/**
	 * @param user
	 * @param locale 
	 * @return the truncated id for the user.
	 */
	protected String truncateId(
			final User user,
			final Locale locale) {
		return truncate(user.getRealId(), idMaxLength, locale);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userFormatting.UserFormattingService#format(
	 * org.esupportail.helpdesk.domain.beans.User, java.util.Locale)
	 */
	@Override
	@RequestCache
	public String format(
			final User user, 
			final Locale locale) {
		if (user == null) {
			return null;
		}
		if (printId && !user.getRealId().equals(user.getDisplayName())) {
			return i18nService.getString(
					"USER.DISPLAY_NAME_AND_ID", locale, 
					truncateDisplayName(user, locale),
					truncateId(user, locale));
		}
		return i18nService.getString(
				"USER.DISPLAY_NAME", locale, 
				truncateDisplayName(user, locale));
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
	 * @return the printId
	 */
	protected boolean isPrintId() {
		return printId;
	}

	/**
	 * @param printId the printId to set
	 */
	public void setPrintId(final boolean printId) {
		this.printId = printId;
	}

	/**
	 * @return the displayNameMaxLength
	 */
	protected int getDisplayNameMaxLength() {
		return displayNameMaxLength;
	}

	/**
	 * @param displayNameMaxLength the displayNameMaxLength to set
	 */
	public void setDisplayNameMaxLength(final int displayNameMaxLength) {
		this.displayNameMaxLength = displayNameMaxLength;
	}

	/**
	 * @return the idMaxLength
	 */
	protected int getIdMaxLength() {
		return idMaxLength;
	}

	/**
	 * @param idMaxLength the idMaxLength to set
	 */
	public void setIdMaxLength(final int idMaxLength) {
		this.idMaxLength = idMaxLength;
	}
	
}

