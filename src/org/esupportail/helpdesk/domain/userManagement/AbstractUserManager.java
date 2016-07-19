/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.Cookie;

import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.Base64;
import org.esupportail.helpdesk.dao.DaoService;
import org.esupportail.helpdesk.domain.beans.User;
import org.springframework.beans.factory.InitializingBean;

/**
 * An abstract user manager.
 */
@SuppressWarnings("serial")
public abstract class AbstractUserManager implements UserManager, InitializingBean {

	/**
	 * Magic number.
	 */
	private static final int CONST_1000 = 1000;

	/**
	 * {@link DaoService}.
	 */
	private DaoService daoService;

	/**
	 * {@link I18nService}.
	 */
	private I18nService i18nService;

	/**
	 * True if the manager uses auth secrets.
	 */
	private boolean useAuthSecrets;

	/**
	 * Constructor.
	 * @param useAuthSecrets
	 */
	public AbstractUserManager(final boolean useAuthSecrets) {
		super();
		this.useAuthSecrets = useAuthSecrets;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.daoService,
				"property daoService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.i18nService,
				"property i18nService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#getDatabaseId(java.lang.String)
	 */
	@Override
	public String getDatabaseId(final String realId) {
		return getDatabasePrefix() + realId;
	}

	/**
	 * @return the database prefix
	 */
	protected abstract String getDatabasePrefix();

	/**
	 * @param realId
	 * @return a new user.
	 */
	protected User newUser(
			final String realId) {
		return new User(getDatabaseId(realId), getAuthType(), realId);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#getUserLocale(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public Locale getUserLocale(final User user) {
		if (user != null && user.getLanguage() != null) {
			return new Locale(user.getLanguage());
		}
		return i18nService.getDefaultLocale();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#getUserEmail(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public String getUserEmail(
			@SuppressWarnings("unused") final User user) {
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#getUserEmails(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public List<String> getUserEmails(
			@SuppressWarnings("unused") final User user) {
		return null;
	}

	/**
	 * @param user
	 * @return the internet address of a user.
	 */
	protected InternetAddress getUserInternetAddress(final User user) {
		String email = getUserEmail(user);
		if (email == null) {
			return null;
		}
		try {
			return new InternetAddress(email, user.getDisplayName());
		} catch (UnsupportedEncodingException e) {
			try {
				return new InternetAddress(email);
			} catch (AddressException e1) {
				return null;
			}
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#getLdapAttributes(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public Map<String, List<String>> getLdapAttributes(
			@SuppressWarnings("unused") final User user) {
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#getPortalAttributes(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public Map<String, List<String>> getPortalAttributes(
			@SuppressWarnings("unused") final User user) {
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#isMemberOfPortalGroup(
	 * org.esupportail.helpdesk.domain.beans.User, java.lang.String)
	 */
	@Override
	public boolean isMemberOfPortalGroup(
			@SuppressWarnings("unused") final User user,
			@SuppressWarnings("unused") final String groupId) {
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#isMemberOfPortalDistinguishedGroup(
	 * org.esupportail.helpdesk.domain.beans.User, java.lang.String)
	 */
	@Override
	public boolean isMemberOfPortalDistinguishedGroup(
			@SuppressWarnings("unused") final User user,
			@SuppressWarnings("unused") final String groupName) {
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#renewAuthSecret(
	 * org.esupportail.helpdesk.domain.beans.User, java.lang.String, int)
	 */
	@Override
	public Cookie renewAuthSecret(
			final User user,
			final String authCookieName,
			final int authCookieExpiry) {
		if (!useAuthSecrets) {
			return null;
		}
		String str = user.getRealId() + "-" + Base64.encodeBytes(Double.toString(Math.random()).getBytes());
		user.setAuthSecret(str);
		user.setAuthLimit(new Timestamp(System.currentTimeMillis() + (CONST_1000 * authCookieExpiry)));
		daoService.updateUser(user);
		Cookie cookie = new Cookie(authCookieName, str);
		cookie.setMaxAge(authCookieExpiry);
		return cookie;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#removeAuthSecret(
	 * org.esupportail.helpdesk.domain.beans.User, java.lang.String)
	 */
	@Override
	public Cookie removeAuthSecret(
			final User user,
			final String authCookieName) {
		if (!useAuthSecrets) {
			return null;
		}
		user.setAuthSecret(null);
		user.setAuthLimit(null);
		daoService.updateUser(user);
		Cookie cookie = new Cookie(authCookieName, "");
		cookie.setMaxAge(0);
		return cookie;
	}

	/**
	 * @return the daoService
	 */
	protected DaoService getDaoService() {
		return daoService;
	}

	/**
	 * @param daoService the daoService to set
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
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

}
