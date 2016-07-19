/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * A CAS user manager.
 */
public class CasUserManagerImpl extends AbstractPortalAwareUserManager implements CasUserManager {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1375450300982371234L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * {@link LdapUserService}.
	 */
	private LdapUserService ldapUserService;

	/**
	 * The LDAP attribute that contains the email.
	 */
	private String emailLdapAttribute;

	/**
	 * The LDAP attribute that contains the email alias.
	 */
	private String emailAliasLdapAttribute;

	/**
	 * The LDAP attribute that contains the display name.
	 */
	private String displayNameLdapAttribute;

	/**
	 * Constructor.
	 */
	public CasUserManagerImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.ldapUserService,
				"property ldapUserService of class " + this.getClass().getName() + " can not be null");
		Assert.hasText(this.displayNameLdapAttribute,
				"property displayNameLdapAttribute of class " + this.getClass().getName()
				+ " can not be null");
		Assert.hasText(this.emailLdapAttribute,
				"property emailLdapAttribute of class " + this.getClass().getName()
				+ " can not be null");
		Assert.hasText(this.emailAliasLdapAttribute,
				"property emailAliasLdapAttribute of class " + this.getClass().getName()
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
		return AuthUtils.CAS;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.AbstractUserManager#getUserEmail(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public String getUserEmail(
			final User user) {
		String email = null;
		LdapUser ldapUser;
		try {
			ldapUser = ldapUserService.getLdapUser(user.getRealId());
			List<String> emailLdapAttributes = ldapUser.getAttributes().get(emailLdapAttribute);
			if (emailLdapAttributes != null && !emailLdapAttributes.isEmpty()) {
				email = emailLdapAttributes.get(0);
			}
		} catch (UserNotFoundException e) {
			// the user was probably removed from the LDAP directory
		}
		return email;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.AbstractUserManager#getUserEmails(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public List<String> getUserEmails(
			final User user) {
		try {
			List<String> emails = new ArrayList<String>();
			LdapUser ldapUser = ldapUserService.getLdapUser(user.getRealId());
			List<String> values;
			values = ldapUser.getAttributes().get(emailLdapAttribute);
			if (values != null && !values.isEmpty()) {
				emails.addAll(values);
			}
			values = ldapUser.getAttributes().get(emailAliasLdapAttribute);
			if (values != null && !values.isEmpty()) {
				emails.addAll(values);
			}
			return emails;
		} catch (UserNotFoundException e) {
			// the user was probably removed from the LDAP directory
			return null;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.CasUserManager#setUserInfo(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean setUserInfo(
			final User user) {
		LdapUser ldapUser = this.ldapUserService.getLdapUser(user.getRealId());
		String displayName = null;
		List<String> displayNameLdapAttributes = ldapUser.getAttributes().get(displayNameLdapAttribute);
		if (displayNameLdapAttributes != null && !displayNameLdapAttributes.isEmpty()) {
			displayName = displayNameLdapAttributes.get(0);
		}
		if (displayName == null || !org.springframework.util.StringUtils.hasText(displayName)) {
			displayName = user.getRealId();
		}
		if (displayName.equals(user.getDisplayName())) {
			return false;
		}
		user.setDisplayName(displayName);
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.CasUserManager#createUser(java.lang.String)
	 */
	@Override
	public User createUser(
			final String realId) throws UserNotFoundException {
		User user = newUser(realId);
		setUserInfo(user);
		getDaoService().addUser(user);
		logger.info("CAS user [" + user.getRealId() + "] has been added to the database");
		return user;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#getLdapAttributes(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public Map<String, List<String>> getLdapAttributes(final User user) {
		try {
			return ldapUserService.getLdapUser(user.getRealId()).getAttributes();
		} catch (UserNotFoundException e) {
			return null;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.CasUserManager#getUserIdWithEmail(java.lang.String)
	 */
	@Override
	public String getUserIdWithEmail(final String email) {
		String filter;
		if (emailLdapAttribute == null) {
			return null;
		}
		filter = emailLdapAttribute + "=" + email;
		if (emailAliasLdapAttribute != null) {
			filter = "|(" + filter + ")(" + emailAliasLdapAttribute + "=" + email + ")";
		}
		List<LdapUser> ldapUsers = getLdapUserService().getLdapUsersFromFilter(filter);
		if (ldapUsers.size() == 0) {
			return null;
		}
		if (ldapUsers.size() > 1) {
			logger.warn("several users have the email [" + email + "]");
			return null;
		}
		return ldapUsers.get(0).getId();
	}

	/**
	 * @return the emailLdapAttribute
	 */
	protected String getEmailLdapAttribute() {
		return emailLdapAttribute;
	}

	/**
	 * @param emailLdapAttribute the emailLdapAttribute to set
	 */
	public void setEmailLdapAttribute(final String emailLdapAttribute) {
		this.emailLdapAttribute = emailLdapAttribute;
	}

	/**
	 * @return the emailAliasLdapAttribute
	 */
	protected String getEmailAliasLdapAttribute() {
		return emailAliasLdapAttribute;
	}

	/**
	 * @param emailAliasLdapAttribute the emailAliasLdapAttribute to set
	 */
	public void setEmailAliasLdapAttribute(final String emailAliasLdapAttribute) {
		this.emailAliasLdapAttribute = emailAliasLdapAttribute;
	}

	/**
	 * @return the ldapUserService
	 */
	protected LdapUserService getLdapUserService() {
		return ldapUserService;
	}

	/**
	 * @param ldapUserService the ldapUserService to set
	 */
	public void setLdapUserService(final LdapUserService ldapUserService) {
		this.ldapUserService = ldapUserService;
	}

	/**
	 * @return the displayNameLdapAttribute
	 */
	protected String getDisplayNameLdapAttribute() {
		return displayNameLdapAttribute;
	}

	/**
	 * @param displayNameLdapAttribute the displayNameLdapAttribute to set
	 */
	public void setDisplayNameLdapAttribute(final String displayNameLdapAttribute) {
		this.displayNameLdapAttribute = displayNameLdapAttribute;
	}

}
