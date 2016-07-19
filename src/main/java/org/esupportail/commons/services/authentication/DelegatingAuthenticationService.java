/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.services.authentication.info.AuthInfo;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

/** 
 * An abstract typed authenticator.
 */
public class DelegatingAuthenticationService extends AbstractAuthenticationService {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -163359171428098265L;
	
	/**
	 * A logger.
	 */
	private Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The authenticators to delegate.
	 */
	private List<AuthenticationService> authenticationServices;
	
	/**
	 * Bean constructor.
	 */
	protected DelegatingAuthenticationService() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractAuthenticationService#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (authenticationServices == null || authenticationServices.isEmpty()) {
			logger.warn("no authenticator set or enabled!");
		}
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AuthenticationService#getAuthInfo()
	 */
	@Override
	public AuthInfo getAuthInfo() {
		for (AuthenticationService authenticationService : authenticationServices) {
			AuthInfo authInfo = authenticationService.getAuthInfo();
			if (authInfo != null) {
				return authInfo;
			}
		}
		return null;
	}

	/**
	 * @return the authenticators
	 */
	public List<AuthenticationService> getAuthenticationServices() {
		return authenticationServices;
	}

	/**
	 * @param authenticationServices the authenticationServices to set
	 */
	public void setAuthenticationServices(final List<AuthenticationService> authenticationServices) {
		this.authenticationServices = new ArrayList<AuthenticationService>();
		for (AuthenticationService authenticationService : authenticationServices) {
			if (authenticationService.isEnabled()) {
				this.authenticationServices.add(authenticationService);
			}
		}
	}

}
