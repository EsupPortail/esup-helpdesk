/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication;

import org.esupportail.commons.services.authentication.info.AuthInfo;

/** 
 * A null authentication service.
 */
@SuppressWarnings("serial")
public class NullAuthenticationService extends AbstractAuthenticationService {
	
	/**
	 * Bean constructor.
	 */
	public NullAuthenticationService() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AuthenticationService#getAuthInfo()
	 */
	@Override
	public AuthInfo getAuthInfo() {
		return null;
	}

}
