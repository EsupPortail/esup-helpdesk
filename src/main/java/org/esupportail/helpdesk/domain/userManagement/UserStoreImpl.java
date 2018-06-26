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

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.dao.DaoService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.userManagement.specific.SpecificUserManager;
import org.springframework.beans.factory.InitializingBean;

/**
 * The basic user store.
 */
public class UserStoreImpl extends AbstractUserStore implements InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6919358828071502969L;

	/**
	 * The default auth cookie expiry.
	 */
	private static final int DEFAULT_AUTH_COOKIE_EXPIRY = 60 * 60 * 24 * 7;

	/**
	 * The name of the auth cookie.
	 */
	private static final String DEFAULT_AUTH_COOKIE_NAME = "esup-helpdesk-auth";

	/**
	 * {@link DaoService}.
	 */
	private DaoService daoService;

	/**
	 * {@link I18nService}.
	 */
	private I18nService i18nService;

	/**
	 * True if application users are allowed.
	 */
	private boolean applicationAuthAllowed = true;

	/**
	 * True if CAS users are allowed.
	 */
	private boolean casAuthAllowed = true;

	/**
	 * True if Shibboleth users are allowed.
	 */
	private boolean shibbolethAuthAllowed;

	/**
	 * True if specific users are allowed.
	 */
	private boolean specificAuthAllowed = true;

	/**
	 * The CAS user manager.
	 */
	private CasUserManager casUserManager;

	/**
	 * The Shibboleth user manager.
	 */
	private ShibbolethUserManager shibbolethUserManager;

	/**
	 * The application user manager.
	 */
	private ApplicationUserManager applicationUserManager;

	/**
	 * The specific user manager.
	 */
	private SpecificUserManager specificUserManager;

	/**
	 * True if convert mail to cas enable.
	 */
	private boolean tryConvertMaillToCasUser;

	/**
	 * pattern of mail to convert to cas user.
	 */
	private String mailToConvertPattern;

	/**
	 * The auth cookie expiry.
	 */
	private int authCookieExpiry = DEFAULT_AUTH_COOKIE_EXPIRY;

	/**
	 * The name of the auth cookie.
	 */
	private String authCookieName = DEFAULT_AUTH_COOKIE_NAME;

	/**
	 * Constructor.
	 */
	public UserStoreImpl() {
		super();
		applicationAuthAllowed = true;
		casAuthAllowed = true;
		shibbolethAuthAllowed = false;
		specificAuthAllowed = false;
		tryConvertMaillToCasUser = false;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.isTrue(
				applicationAuthAllowed || casAuthAllowed || shibbolethAuthAllowed ||specificAuthAllowed,
				"at least one of the properties applicationAuthAllowed, "
				+ "casAuthAllowed, shibbolethAuthAllowed or specificAuthAllowed must be true");
		Assert.notNull(this.daoService,
				"property daoService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.i18nService,
				"property i18nService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.casUserManager,
				"property casUserManager of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(this.shibbolethUserManager,
				"property shibbolethUserManager of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(this.applicationUserManager,
				"property applicationUserManager of class " + this.getClass().getName()
				+ " can not be null");
		Assert.notNull(this.specificUserManager,
				"property specificUserManager of class " + this.getClass().getName()
				+ " can not be null");
	}

	/**
	 * Eclipse delimiter.
	 */
	protected void __________________________IS_XXX_AUTH_ALLOWED() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#isApplicationAuthAllowed()
	 */
	@Override
	public boolean isApplicationAuthAllowed() {
		return applicationAuthAllowed;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#isCasAuthAllowed()
	 */
	@Override
	public boolean isCasAuthAllowed() {
		return casAuthAllowed;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#isShibbolethAuthAllowed()
	 */
	@Override
	public boolean isShibbolethAuthAllowed() {
		return shibbolethAuthAllowed;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#isSpecificAuthAllowed()
	 */
	@Override
	public boolean isSpecificAuthAllowed() {
		return specificAuthAllowed;
	}

	/**
	 * Eclipse delimiter.
	 */
	protected void __________________________IS_XXX_USER() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#isApplicationUser(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean isApplicationUser(final User user) {
		return applicationUserManager.getAuthType().equals(user.getAuthType());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#isCasUser(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean isCasUser(final User user) {
		return AuthUtils.CAS.equals(user.getAuthType());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#isShibbolethUser(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean isShibbolethUser(final User user) {
		return AuthUtils.SHIBBOLETH.equals(user.getAuthType());
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#isSpecificUser(org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean isSpecificUser(final User user) {
		return AuthUtils.SPECIFIC.equals(user.getAuthType());
	}

	/**
	 * Eclipse delimiter.
	 */
	protected void __________________________GET_XXX_USER_ID() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getApplicationUserId(java.lang.String)
	 * TODO remove from the interface
	 */
	@Override
	public String getApplicationUserId(
			final String realId) {
		return applicationUserManager.getDatabaseId(realId);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getCasUserId(java.lang.String)
	 * TODO remove from the interface
	 */
	@Override
	public String getCasUserId(
			final String realId) {
		return casUserManager.getDatabaseId(realId);
	}

	/**
	 * @param realId
	 * @return the id of a Shibboleth user from its real id.
	 */
	protected String getShibbolethUserId(
			final String realId) {
		return shibbolethUserManager.getDatabaseId(realId);
	}

	/**
	 * @param realId
	 * @return the id of a specific user from its real id.
	 */
	protected String getSpecificUserId(
			final String realId) {
		return specificUserManager.getDatabaseId(realId);
	}

	/**
	 * Eclipse delimiter.
	 */
	protected void __________________________GET_USER_LOCALE_EMAILS() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getUserLocale(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public Locale getUserLocale(
			final User user) {
		if (user == null) {
			return getI18nService().getDefaultLocale();
		}
		return getManager(user).getUserLocale(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getUserInternetAddress(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public InternetAddress getUserInternetAddress(final User user) {
		String email = getManager(user).getUserEmail(user);
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
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getUserEmails(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public List<String> getUserEmails(final User user) {
		return getManager(user).getUserEmails(user);
	}

	/**
	 * Eclipse delimiter.
	 */
	protected void __________________________GET_USER_FROM_REAL_ID() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getExistingUserFromId(java.lang.String)
	 */
	@Override
	public User getExistingUserFromId(
			final String id) throws UserNotFoundException {
		User user = daoService.getUser(id);
		if (user != null) {
			return user;
		}
		throw new UserNotFoundException(
				"user [" + id + "] not found in the database");
	}

	/**
	 * @param user
	 * @return the user manager for a user.
	 */
	protected UserManager getManager(final User user) {
		if (isCasUser(user)) {
			return casUserManager;
		}
		if (isShibbolethUser(user)) {
			return shibbolethUserManager;
		}
		if (isApplicationUser(user)) {
			return applicationUserManager;
		}
		if (isSpecificUser(user)) {
			return specificUserManager;
		}
		throw new UnsupportedOperationException("unknown auth type [" + user.getAuthType() + "]");
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getOrCreateCasUser(
	 * java.lang.String, boolean)
	 */
	@Override
	public User getOrCreateCasUser(
			final String realId,
			final boolean updateInfo) throws UserNotFoundException {
		try {
			User user = getExistingUserFromId(getCasUserId(realId));
			if (updateInfo) {
				if (casUserManager.setUserInfo(user)) {
					daoService.updateUser(user);
				}
			}
			return user;
		} catch (UserNotFoundException e) {
			try {
				return casUserManager.createUser(realId);
			} catch (UserNotFoundException e2) {
				throw new UserNotFoundException(
						"could not create CAS user [" + realId
						+ "] (not found in the LDAP directory)");
			}
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getOrCreateShibolethUser(
	 * java.lang.String, java.util.Map)
	 */
	@Override
	public User getOrCreateShibolethUser(
			final String realId,
			final Map<String, List<String>> attributes) {
		try {
			User user = getExistingUserFromId(getShibbolethUserId(realId));
			if (shibbolethUserManager.setUserInfo(user, attributes)) {
				daoService.updateUser(user);
			}
			return user;
		} catch (UserNotFoundException e) {
			return shibbolethUserManager.createUser(realId, attributes);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getExistingApplicationUser(java.lang.String)
	 */
	@Override
	public User getExistingApplicationUser(
			final String realId) throws UserNotFoundException {
		return getExistingUserFromId(getApplicationUserId(realId));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#authenticateApplicationUser(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public User authenticateApplicationUser(
			final String email,
			final String password) {
		try {
			User user = getExistingApplicationUser(email);
			if (password.equals(user.getPassword())) {
				return user;
			}
		} catch (UserNotFoundException e) {
			//
		}
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#setApplicationUserInfo(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean setApplicationUserInfo(
			final User user) {
		return applicationUserManager.setUserInfo(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getOrCreateSpecificUser(
	 * java.lang.String, boolean)
	 */
	@Override
	public User getOrCreateSpecificUser(
			final String realId,
			final boolean updateInfo) throws UserNotFoundException {
		try {
			User user = getExistingUserFromId(getSpecificUserId(realId));
			if (updateInfo) {
				if (specificUserManager.setUserInfo(user)) {
					daoService.updateUser(user);
				}
			}
			return user;
		} catch (UserNotFoundException e) {
			try {
				return specificUserManager.createUser(realId);
			} catch (UserNotFoundException e2) {
				throw new UserNotFoundException(
						"could not create specific user [" + realId
						+ "] (not found)");
			}
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#authenticateSpecificUser(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public User authenticateSpecificUser(
			final String id,
			final String password) {
		if (specificUserManager.authenticate(id, password)) {
			return getOrCreateSpecificUser(id, true);
		}
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#createApplicationUser(
	 * java.lang.String, java.lang.String, java.util.Locale)
	 */
	@Override
	public User createApplicationUser(
			final String realId,
			final String displayName,
			final Locale locale) {
		return applicationUserManager.createUser(realId, displayName, locale);
	}

	/**
	 * @param realId
	 * @return true if the given realId is an email.
	 */
	protected boolean isEmailId(
			final String realId) {
		return realId != null && realId.contains("@");
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getUserFromRealId(java.lang.String)
	 */
	@Override
	public User getUserFromRealId(
			final String realId) throws UserNotFoundException {
		if (isShibbolethAuthAllowed()) {
            try {
                return getExistingUserFromId(getShibbolethUserId(realId));
            } catch (UserNotFoundException e) {
                // user is not shibboleth - no problem, try other possibilities
            }
		}
		if (isCasAuthAllowed()) {
            try {
                return getOrCreateCasUser(realId, false);
            } catch (UserNotFoundException e) {
            	if(tryConvertMaillToCasUser) {
            		if(mailToConvertPattern.length() != 0) {
            			if (realId.contains(mailToConvertPattern)) {
            				return getUserWithEmail(realId);
            			}
            		}
            	}
            }
		}
		if (isSpecificAuthAllowed()) {
            try {
                return getOrCreateSpecificUser(realId, false);
            } catch (UserNotFoundException e) {
            	// no such specific user, continue
            }
		}
		if (isApplicationAuthAllowed() && isEmailId(realId)) {
			try {
				return getExistingApplicationUser(realId);
			} catch (UserNotFoundException e) {
				return createApplicationUser(realId, null, null);
			}
		}
		throw new UserNotFoundException(
				"user [" + realId + "] not found in the database");
	}

	/**
	 * Eclipse delimiter.
	 */
	protected void __________________________GET_ATTRIBUTES() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getLdapAttributes(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public Map<String, List<String>> getLdapAttributes(
			final User user) {
		if (user == null) {
			return null;
		}
		return getManager(user).getLdapAttributes(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getPortalAttributes(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public Map<String, List<String>> getPortalAttributes(
			final User user) {
		if (user == null) {
			return null;
		}
		return getManager(user).getPortalAttributes(user);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#isMemberOfPortalGroup(
	 * org.esupportail.helpdesk.domain.beans.User, java.lang.String)
	 */
	@Override
	public boolean isMemberOfPortalGroup(
			final User user,
			final String groupId) {
		if (user == null) {
			return false;
		}
		return getManager(user).isMemberOfPortalGroup(user, groupId);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#isMemberOfPortalDistinguishedGroup(
	 * org.esupportail.helpdesk.domain.beans.User, java.lang.String)
	 */
	@Override
	public boolean isMemberOfPortalDistinguishedGroup(
			final User user,
			final String groupName) {
		if (user == null) {
			return false;
		}
		return getManager(user).isMemberOfPortalDistinguishedGroup(user, groupName);
	}

	/**
	 * Eclipse delimiter.
	 */
	protected void __________________________GET_USER_WITH_EMAIL() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getUserWithEmail(java.lang.String)
	 */
	@Override
	public User getUserWithEmail(final String email) {
		if (isShibbolethAuthAllowed()) {
			User user = shibbolethUserManager.getUserWithEmail(email);
			if (user != null) {
				return user;
			}
		}
		if (isCasAuthAllowed()) {
			String userId = casUserManager.getUserIdWithEmail(email);
			if (userId != null) {
				return getOrCreateCasUser(userId, false);
			}
		}
		if (isSpecificAuthAllowed()) {
			String userId = specificUserManager.getUserIdWithEmail(email);
			if (userId != null) {
				return getOrCreateSpecificUser(userId, false);
			}
		}
		if (isApplicationAuthAllowed()) {
			try {
				return getExistingApplicationUser(email);
			} catch (UserNotFoundException e) {
				return this.createApplicationUser(email, null, null);
			}
		}
		return null;
	}

	/**
	 * Eclipse delimiter.
	 */
	protected void __________________________AUTH_SECRET() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getAuthCookieName()
	 */
	@Override
	public String getAuthCookieName() {
		return authCookieName;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#getUserWithAuthSecret(java.lang.String)
	 */
	@Override
	@RequestCache
	public User getUserWithAuthSecret(final String authSecret) {
		User user = daoService.getUserWithAuthSecret(authSecret);
		if (user == null) {
			return null;
		}
		if (user.getAuthLimit().before(new Timestamp(System.currentTimeMillis()))) {
			removeAuthSecret(user);
			return null;
		}
		return user;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#renewAuthSecret(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public Cookie renewAuthSecret(
			final User user) {
		return getManager(user).renewAuthSecret(user, authCookieName, authCookieExpiry);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserStore#removeAuthSecret(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public Cookie removeAuthSecret(final User user) {
		return getManager(user).removeAuthSecret(user, authCookieName);
	}

	/**
	 * Eclipse delimiter.
	 */
	protected void __________________________GETTERS_SETTERS() {
		//
	}

	/**
	 * @return the daoService
	 */
	public DaoService getDaoService() {
		return daoService;
	}

	/**
	 * @param daoService the daoService to set
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	/**
	 * @param applicationAuthAllowed the applicationAuthAllowed to set
	 */
	public void setApplicationAuthAllowed(final boolean applicationAuthAllowed) {
		this.applicationAuthAllowed = applicationAuthAllowed;
	}

	/**
	 * @param casAuthAllowed the casAuthAllowed to set
	 */
	public void setCasAuthAllowed(final boolean casAuthAllowed) {
		this.casAuthAllowed = casAuthAllowed;
	}

	/**
	 * @param shibbolethAuthAllowed the shibbolethAuthAllowed to set
	 */
	public void setShibbolethAuthAllowed(final boolean shibbolethAuthAllowed) {
		this.shibbolethAuthAllowed = shibbolethAuthAllowed;
	}

	/**
	 * @return the casUserManager
	 */
	protected CasUserManager getCasUserManager() {
		return casUserManager;
	}

	/**
	 * @param casUserManager the casUserManager to set
	 */
	public void setCasUserManager(final CasUserManager casUserManager) {
		this.casUserManager = casUserManager;
	}

	/**
	 * @return the shibbolethUserManager
	 */
	protected ShibbolethUserManager getShibbolethUserManager() {
		return shibbolethUserManager;
	}

	/**
	 * @param shibbolethUserManager the shibbolethUserManager to set
	 */
	public void setShibbolethUserManager(final ShibbolethUserManager shibbolethUserManager) {
		this.shibbolethUserManager = shibbolethUserManager;
	}

	/**
	 * @return the applicationUserManager
	 */
	protected ApplicationUserManager getApplicationUserManager() {
		return applicationUserManager;
	}

	/**
	 * @param applicationUserManager the applicationUserManager to set
	 */
	public void setApplicationUserManager(
			final ApplicationUserManager applicationUserManager) {
		this.applicationUserManager = applicationUserManager;
	}

	/**
	 * @return the authCookieExpiry
	 */
	protected int getAuthCookieExpiry() {
		return authCookieExpiry;
	}

	/**
	 * @param authCookieExpiry the authCookieExpiry to set
	 */
	public void setAuthCookieExpiry(final int authCookieExpiry) {
		this.authCookieExpiry = authCookieExpiry;
	}

	/**
	 * @param authCookieName the authCookieName to set
	 */
	public void setAuthCookieName(final String authCookieName) {
		this.authCookieName = authCookieName;
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
	 * @return the specificUserManager
	 */
	protected SpecificUserManager getSpecificUserManager() {
		return specificUserManager;
	}

	/**
	 * @param specificUserManager the specificUserManager to set
	 */
	public void setSpecificUserManager(final SpecificUserManager specificUserManager) {
		this.specificUserManager = specificUserManager;
	}

	/**
	 * @param specificAuthAllowed the specificAuthAllowed to set
	 */
	public void setSpecificAuthAllowed(final boolean specificAuthAllowed) {
		this.specificAuthAllowed = specificAuthAllowed;
	}


	public String getMailToConvertPattern() {
		return mailToConvertPattern;
	}

	public void setMailToConvertPattern(String mailToConvertPattern) {
		this.mailToConvertPattern = mailToConvertPattern;
	}

	public void setTryConvertMaillToCasUser(boolean tryConvertMaillToCasUser) {
		this.tryConvertMaillToCasUser = tryConvertMaillToCasUser;
	}
	
}
