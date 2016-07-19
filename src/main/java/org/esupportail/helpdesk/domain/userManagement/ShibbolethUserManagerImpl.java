/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement;

import java.util.List;
import java.util.Map;

import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * A Shibboleth user manager.
 */
public class ShibbolethUserManagerImpl extends AbstractPortalAwareUserManager implements ShibbolethUserManager {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6790867377876218322L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The Shibboleth attribute that contains the display name.
	 */
	private String displayNameShibbolethAttribute;

	/**
	 * The Shibboleth attribute that contains the email.
	 */
	private String emailShibbolethAttribute;

	/**
	 * Constructor.
	 */
	public ShibbolethUserManagerImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.hasText(this.displayNameShibbolethAttribute,
				"property displayNameShibbolethAttribute of class " + this.getClass().getName()
				+ " can not be null");
		Assert.hasText(this.emailShibbolethAttribute,
				"property emailShibbolethAttribute of class " + this.getClass().getName()
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
		return AuthUtils.SHIBBOLETH;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.AbstractUserManager#getUserEmail(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public String getUserEmail(
			final User user) {
		List<String> emails = user.getAttributes().get(emailShibbolethAttribute);
		if (emails.isEmpty()) {
			return null;
		}
		return emails.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.ShibbolethUserManager#setUserInfo(
	 * org.esupportail.helpdesk.domain.beans.User, java.util.Map)
	 */
	@Override
	public boolean setUserInfo(
			final User user,
			final Map<String, List<String>> attributes) {
		String displayName = null;
		String email = null;
		if (attributes != null && !attributes.isEmpty()) {
			user.setAttributes(attributes);
			List<String> displayNameShibbolethAttributes = attributes.get(displayNameShibbolethAttribute);
			if (displayNameShibbolethAttributes != null && !displayNameShibbolethAttributes.isEmpty()) {
				displayName = displayNameShibbolethAttributes.get(0);
			}
			List<String> emailShibbolethAttributes = attributes.get(emailShibbolethAttribute);
			if (emailShibbolethAttributes != null && !emailShibbolethAttributes.isEmpty()) {
				email = emailShibbolethAttributes.get(0);
			}
		}
		if (displayName == null || !org.springframework.util.StringUtils.hasText(displayName)) {
			displayName = user.getRealId();
		}
		boolean displayNameUpdated;
		if (displayName.equals(user.getDisplayName())) {
			displayNameUpdated = false;
		} else {
			displayNameUpdated = true;
			user.setDisplayName(displayName);
		}
		boolean emailUpdated = (email != null) && !email.equals(user.getEmail());
		return displayNameUpdated || emailUpdated;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.ShibbolethUserManager#createUser(
	 * java.lang.String, java.util.Map)
	 */
	@Override
	public User createUser(
			final String realId,
			final Map<String, List<String>> attributes) {
		User user = newUser(realId);
		setUserInfo(user, attributes);
		getDaoService().addUser(user);
		logger.info("Shibboleth user [" + user.getRealId() + "] has been added to the database");
		return user;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.ShibbolethUserManager#getUserWithEmail(java.lang.String)
	 */
	@Override
	public User getUserWithEmail(
			@SuppressWarnings("unused") final String email) {
		// TODO
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.AbstractUserManager#getUserEmails(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public List<String> getUserEmails(
			final User user) {
		Map<String, List<String>> attributes = user.getAttributes();
		if (attributes == null) {
			return null;
		}
		return attributes.get(emailShibbolethAttribute);
	}

	/**
	 * @return the displayNameShibbolethAttribute
	 */
	protected String getDisplayNameShibbolethAttribute() {
		return displayNameShibbolethAttribute;
	}

	/**
	 * @param displayNameShibbolethAttribute the displayNameShibbolethAttribute to set
	 */
	public void setDisplayNameShibbolethAttribute(
			final String displayNameShibbolethAttribute) {
		this.displayNameShibbolethAttribute = displayNameShibbolethAttribute;
	}

	/**
	 * @return the emailShibbolethAttribute
	 */
	protected  String getEmailShibbolethAttribute() {
		return emailShibbolethAttribute;
	}

	/**
	 * @param emailShibbolethAttribute the emailShibbolethAttribute to set
	 */
	public void setEmailShibbolethAttribute(final String emailShibbolethAttribute) {
		this.emailShibbolethAttribute = emailShibbolethAttribute;
	}

}
