/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement.specific;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.userManagement.AbstractUserManager;

/**
 * An XML-based user manager.
 */
public class XmlUserManagerImpl extends AbstractUserManager implements SpecificUserManager {

	/**
	 * The prefix of users' id.
	 */
	private static String USER_ID_PREFIX = "xml-";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5201541435806972416L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The users.
	 */
	private List<BasicSpecificUser> specificUsers;

	/**
	 * Constructor.
	 */
	public XmlUserManagerImpl() {
		super(true);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
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
		return AuthUtils.SPECIFIC;
	}

	/**
	 * @param realId
	 * @return the internal user with the given id.
	 * @throws UserNotFoundException
	 */
	protected BasicSpecificUser getSpecificUser(
			final String realId) throws UserNotFoundException {
		if (specificUsers != null) {
			for (BasicSpecificUser specificUser : specificUsers) {
				if (realId.equals(specificUser.getId())) {
					return specificUser;
				}
			}
		}
		throw new UserNotFoundException("specific user [" + realId + "] not found");
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.AbstractUserManager#getUserEmail(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public String getUserEmail(
			final User user) {
		try {
			return getSpecificUser(user.getRealId()).getEmail();
		} catch (UserNotFoundException e) {
			return null;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.specific.SpecificUserManager#setUserInfo(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean setUserInfo(
			final User user) {
		BasicSpecificUser specificUser = getSpecificUser(user.getRealId());
		String displayName = specificUser.getDisplayName();
		if (displayName.equals(user.getDisplayName())) {
			return false;
		}
		user.setDisplayName(displayName);
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.specific.SpecificUserManager#createUser(java.lang.String)
	 */
	@Override
	public User createUser(final String realId) {
		User user = newUser(realId);
		setUserInfo(user);
		getDaoService().addUser(user);
		logger.info("Specific user [" + user.getRealId() + "] has been added to the database");
		return user;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.specific.SpecificUserManager#getUserIdWithEmail(
	 * java.lang.String)
	 */
	@Override
	public String getUserIdWithEmail(
			final String email) {
		if (specificUsers != null) {
			for (BasicSpecificUser specificUser : specificUsers) {
				if (email.equals(specificUser.getEmail())) {
					return specificUser.getId();
				}
			}
		}
		return null;
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
			emails.add(getSpecificUser(user.getRealId()).getEmail());
			return emails;
		} catch (UserNotFoundException e) {
			// the user was probably removed from the LDAP directory
			return null;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.specific.SpecificUserManager#authenticate(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticate(
			final String id,
			final String password) {
		if (specificUsers != null) {
			for (BasicSpecificUser specificUser : specificUsers) {
				if (id.equals(specificUser.getId()) && password.equals(specificUser.getPassword())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return the specificUsers
	 */
	protected List<BasicSpecificUser> getSpecificUsers() {
		return specificUsers;
	}

	/**
	 * @param specificUsers the specificUsers to set
	 */
	public void setSpecificUsers(final List<BasicSpecificUser> specificUsers) {
		this.specificUsers = specificUsers;
	}
}
