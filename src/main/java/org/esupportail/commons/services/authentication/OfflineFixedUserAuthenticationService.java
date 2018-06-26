/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication;

import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;

/** 
 * This authenticator is to be used when working offline, it always returns the same user.
 */
public class OfflineFixedUserAuthenticationService extends AbstractTypedAuthenticationService {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1397301690881143218L;

	/**
	 * The id of the fixed user.
	 */
	private String authId;

	/**
	 * Bean constructor.
	 */
	public OfflineFixedUserAuthenticationService() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.hasText(this.authId, 
				"property authId of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractRealAuthenticationService#getAuthId()
	 */
	@Override
	protected String getAuthId() {
		return authId;
	}

	/**
	 * @param authId the authId to set
	 */
	public void setAuthId(final String authId) {
		this.authId = StringUtils.nullIfEmpty(authId);
	}

}
