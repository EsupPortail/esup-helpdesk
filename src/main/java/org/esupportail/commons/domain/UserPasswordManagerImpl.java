/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.domain;

import java.util.Locale;

import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.exceptions.PasswordException;
import org.springframework.beans.factory.InitializingBean;

/**
 * a basic password manager implementation.
 */
public class UserPasswordManagerImpl implements UserPasswordManager, InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2486990059811696496L;

	/**
	 * The default minimum length for passwords.
	 */
	private static final int DEFAULT_PASSWORD_MIN_LENGTH = 6;

	/**
	 * The default maximum length for passwords.
	 */
	private static final int DEFAULT_PASSWORD_MAX_LENGTH = 8;
	
	/**
	 * The i18n service.
	 */
	private I18nService i18nService;
	
	/**
	 * The minimum length for passwords.
	 */
	private Integer passwordMinLength;

	/**
	 * The maximum length for passwords.
	 */
	private Integer passwordMaxLength;

	/**
	 * Bean constructor.
	 */
	public UserPasswordManagerImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(i18nService, "property i18nService can not be null");
		if (passwordMaxLength == null || passwordMaxLength < 1) {
			passwordMaxLength = DEFAULT_PASSWORD_MAX_LENGTH;
		}
		if (passwordMinLength == null || passwordMinLength < 1) {
			passwordMinLength = DEFAULT_PASSWORD_MIN_LENGTH;
		}
	}

	/**
	 * @see org.esupportail.commons.domain.UserPasswordManager#generate()
	 */
	@Override
	public String generate() {
		StringBuffer sb = new StringBuffer(8);
		Math.random();
		for (int i = 0; i < 8; i++) {
			char intChar = 0;
			while (intChar < 48 
					|| intChar > 122 
					|| (intChar > 57 && intChar < 65)
					|| (intChar > 90 && intChar < 97)) {
				Math.random();
				intChar = (char) (Math.random() * 100);
			}
			sb.append(intChar);
		}
		return sb.toString();
	}

	/**
	 * @see org.esupportail.commons.domain.UserPasswordManager#check(java.lang.String, java.util.Locale)
	 */
	@Override
	public void check(final String password, final Locale locale) throws PasswordException {
		if (password == null) {
			throw new PasswordException(i18nService.getString("PASSWORD_ERROR.NULL", locale));
		}
		if (password.length() > passwordMaxLength) {
			throw new PasswordException(i18nService.getString(
					"PASSWORD_ERROR.TOO_LONG", locale, passwordMaxLength));
		}
		if (password.length() < passwordMinLength) {
			throw new PasswordException(i18nService.getString(
					"PASSWORD_ERROR.TOO_SHORT", locale, passwordMinLength));
		}
	}

	/**
	 * @param passwordMaxLength the passwordMaxLength to set
	 */
	public void setPasswordMaxLength(final Integer passwordMaxLength) {
		this.passwordMaxLength = passwordMaxLength;
	}

	/**
	 * @param passwordMinLength the passwordMinLength to set
	 */
	public void setPasswordMinLength(final Integer passwordMinLength) {
		this.passwordMinLength = passwordMinLength;
	}

	/**
	 * @param service the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}

}
