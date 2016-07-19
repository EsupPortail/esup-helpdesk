/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement;

import java.util.Locale;

import org.esupportail.commons.domain.UserPasswordManager;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.reporting.PasswordSender;
import org.springframework.util.StringUtils;

/**
 * An application user manager.
 */
public class ApplicationUserManagerImpl extends AbstractUserManager implements ApplicationUserManager {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3808299458808657525L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The user password manager.
	 */
	private UserPasswordManager userPasswordManager;

	/**
	 * The password sender.
	 */
	private PasswordSender passwordSender;

	/**
	 * Constructor.
	 */
	public ApplicationUserManagerImpl() {
		super(true);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.userPasswordManager,
				"property userPasswordManager of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(this.passwordSender,
				"property passwordSender of class " + this.getClass().getName()
				+ " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.AbstractUserManager#getDatabasePrefix()
	 */
	@Override
	protected String getDatabasePrefix() {
		return USER_ID_PREFIX;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#getAuthType()
	 */
	@Override
	public String getAuthType() {
		return AuthUtils.APPLICATION;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.AbstractUserManager#getUserEmail(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public String getUserEmail(
			final User user) {
		return user.getRealId();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.ApplicationUserManager#setUserInfo(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean setUserInfo(
			final User user) {
		if (!StringUtils.hasText(user.getDisplayName())) {
			user.setDisplayName(user.getRealId());
			return true;
		}
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.ApplicationUserManager#createUser(
	 * java.lang.String, java.lang.String, java.util.Locale)
	 */
	@Override
	public User createUser(
			final String realId,
			final String displayName,
			final Locale locale) {
		User user = newUser(realId);
		user.setPassword(userPasswordManager.generate());
		if (displayName != null) {
			user.setDisplayName(displayName);
		}
		if (locale != null) {
			user.setLanguage(locale.toString());
		}
		setUserInfo(user);
		getDaoService().addUser(user);
		logger.info("application user [" + user.getRealId() + "] has been added to the database");
		passwordSender.sendPasswordEmail(user, locale);
		return user;
	}

	/**
	 * @return the userPasswordManager
	 */
	protected UserPasswordManager getUserPasswordManager() {
		return userPasswordManager;
	}

	/**
	 * @param userPasswordManager the userPasswordManager to set
	 */
	public void setUserPasswordManager(final UserPasswordManager userPasswordManager) {
		this.userPasswordManager = userPasswordManager;
	}

	/**
	 * @return the passwordSender
	 */
	protected PasswordSender getPasswordSender() {
		return passwordSender;
	}

	/**
	 * @param passwordSender the passwordSender to set
	 */
	public void setPasswordSender(final PasswordSender passwordSender) {
		this.passwordSender = passwordSender;
	}

}
