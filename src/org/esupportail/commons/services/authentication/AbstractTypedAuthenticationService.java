/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication;

import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;

/** 
 * An abstract typed authenticator.
 */
@SuppressWarnings("serial")
public abstract class AbstractTypedAuthenticationService extends AbstractRealAuthenticationService {

	/**
	 * The authentication type.
	 */
	private String authType;
	
	/**
	 * Bean constructor.
	 */
	protected AbstractTypedAuthenticationService() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(getAuthType(), "property authType of class " + getClass() + " should not be null");
	}
	
	/**
	 * @see org.esupportail.commons.services.authentication.AbstractRealAuthenticationService#getAuthType()
	 */
	@Override
	protected String getAuthType() {
		return authType;
	}

	/**
	 * @param authType the authType to set
	 */
	public void setAuthType(final String authType) {
		this.authType = StringUtils.nullIfEmpty(authType);
	}

}
