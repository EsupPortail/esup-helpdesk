/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication;

/** 
 * A casified portal authenticator.
 */
public class CasifiedPortalAuthenticationService extends PortalAuthenticationService {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3993849015420934582L;

	/**
	 * Bean constructor.
	 */
	public CasifiedPortalAuthenticationService() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractTypedAuthenticationService#getAuthType()
	 */
	@Override
	protected String getAuthType() {
		return AuthUtils.CAS;
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractTypedAuthenticationService#setAuthType(
	 * java.lang.String)
	 */
	@Override
	public void setAuthType(
			@SuppressWarnings("unused")
			final String authType) {
		throw new UnsupportedOperationException(
				"method " + getClass() + ".setAuthType() should never be called.");
	}

}
