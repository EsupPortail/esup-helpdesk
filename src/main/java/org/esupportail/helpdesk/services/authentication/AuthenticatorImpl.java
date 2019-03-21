/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.authentication;

import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.services.authentication.info.AuthInfo;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.helpdesk.domain.ControlPanel;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.web.controllers.AbstractDomainAwareBean;

/**
 * A basic authenticator implementation.
 */
public class AuthenticatorImpl extends AbstractDomainAwareBean implements Authenticator {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 4330641949516953679L;

	/**
	 * The session attribute to store the auth info.
	 */
	private static final String AUTH_INFO_ATTRIBUTE = AuthenticatorImpl.class.getName() + ".authInfo";

	/**
	 * The session attribute to store the user.
	 */
	private static final String USER_ATTRIBUTE = AuthenticatorImpl.class.getName() + ".user";

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The external authenticator.
	 */
	private AuthenticationService authenticationService;

	/**
	 * Bean constructor.
	 */
	public AuthenticatorImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractDomainAwareBean#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		Assert.notNull(authenticationService,
				"property authenticationService of class " + this.getClass().getName()
				+ " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.services.authentication.Authenticator#getUser()
	 */
	@Override
	public User getUser() {
		AuthInfo authInfo = (AuthInfo) ContextUtils.getSessionAttribute(AUTH_INFO_ATTRIBUTE);
		if (authInfo != null) {
			User user = (User) ContextUtils.getSessionAttribute(USER_ATTRIBUTE);
			if (logger.isDebugEnabled()) {
				logger.debug("found auth info in session: " + user);
			}
			return user;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("no auth info found in session");
		}
		authInfo = authenticationService.getAuthInfo();
		if (authInfo == null) {
			unsetUser();
			return null;
		}
		if (AuthUtils.CAS.equals(authInfo.getType())) {
			User user = getUserStore().getOrCreateCasUser(authInfo.getId(), true);
			if(user.getControlPanelUserInterface()) {
				user.setControlPanelUserDepartmentFilter(null);
				user.setControlPanelUserInvolvementFilter(ControlPanel.USER_INVOLVEMENT_FILTER_OWNER_OR_INVITED);
				user.setControlPanelUserStatusFilter(ControlPanel.STATUS_FILTER_ANY);
			}
			storeToSession(authInfo, user);
			return user;
		}
		if (AuthUtils.SHIBBOLETH.equals(authInfo.getType())) {
			User user = getUserStore().getOrCreateShibolethUser(authInfo.getId(), authInfo.getAttributes());
			storeToSession(authInfo, user);
			return user;
		}
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.services.authentication.Authenticator#unsetUser()
	 */
	@Override
	public void unsetUser() {
		storeToSession(null, null);
	}

	/**
	 * @see org.esupportail.helpdesk.services.authentication.Authenticator#setApplicationUser(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void setApplicationUser(final User user) {
		storeToSession(AuthUtils.applicationAuthInfo(user.getId()), user);
	}

	/**
	 * Store the authentication information to the session.
	 * @param authInfo
	 * @param user
	 */
	protected void storeToSession(
			final AuthInfo authInfo,
			final User user) {
		if (logger.isDebugEnabled()) {
			logger.debug("storing to session: " + authInfo);
		}
		ContextUtils.setSessionAttribute(AUTH_INFO_ATTRIBUTE, authInfo);
		ContextUtils.setSessionAttribute(USER_ATTRIBUTE, user);
	}

	/**
	 * @param authenticationService the authenticationService to set
	 */
	public void setAuthenticationService(
			final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * @return the authenticationService
	 */
	protected AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

}
