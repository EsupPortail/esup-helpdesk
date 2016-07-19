/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication;

import org.esupportail.commons.utils.HttpUtils;

/** 
 * A RemoteUser authenticator.
 */
public class RemoteUserAuthenticationService extends AbstractTypedAuthenticationService {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3616080509231674818L;

	/**
	 * Bean constructor.
	 */
	public RemoteUserAuthenticationService() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractRealAuthenticationService#getAuthId()
	 */
	@Override
	public String getAuthId() {
		return HttpUtils.getRemoteUser();
	}

}
