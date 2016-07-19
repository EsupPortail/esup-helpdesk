/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication;

/** 
 * An abstract authenticator.
 */
@SuppressWarnings("serial")
public abstract class AbstractAuthenticationService implements AuthenticationService {
	
	/**
	 * True if enabled.
	 */
	private boolean enabled = true;

	/**
	 * Bean constructor.
	 */
	protected AbstractAuthenticationService() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		// nothing to check
	}

	/**
	 * @return the enabled
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

}
