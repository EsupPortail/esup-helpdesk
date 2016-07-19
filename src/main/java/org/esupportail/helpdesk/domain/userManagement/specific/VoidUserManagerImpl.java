/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement.specific;

import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.userManagement.AbstractUserManager;

/**
 * An void specific user manager.
 */
public class VoidUserManagerImpl extends AbstractUserManager implements SpecificUserManager {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5392800287166452386L;

	/**
	 * Constructor.
	 */
	public VoidUserManagerImpl() {
		super(false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.AbstractUserManager#getDatabasePrefix()
	 */
	@Override
	protected String getDatabasePrefix() {
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#getAuthType()
	 */
	@Override
	public String getAuthType() {
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.specific.SpecificUserManager#setUserInfo(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public boolean setUserInfo(
			@SuppressWarnings("unused") final User user) {
		return false;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.specific.SpecificUserManager#createUser(java.lang.String)
	 */
	@Override
	public User createUser(
			@SuppressWarnings("unused") final String realId) {
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.specific.SpecificUserManager#getUserIdWithEmail(
	 * java.lang.String)
	 */
	@Override
	public String getUserIdWithEmail(
			@SuppressWarnings("unused") final String email) {
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.specific.SpecificUserManager#authenticate(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticate(
			@SuppressWarnings("unused") final String id,
			@SuppressWarnings("unused") final String password) {
		return false;
	}
}
