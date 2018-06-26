/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication;

import java.util.ArrayList;
import java.util.List;


/** 
 * A class to resolve the current user depending on the installation:
 * - for portlet installations, rely on the portal,
 * - for servlet installations, rely on the CAS filter. 
 * @deprecated Use DelegatingAuthenticationService instead.
 */
@Deprecated
public class PortalOrCasFilterAuthenticationService extends DelegatingAuthenticationService {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1030107540760290734L;
	
	/**
	 * Bean constructor.
	 */
	public PortalOrCasFilterAuthenticationService() {
		super();
		List<AuthenticationService> authenticationServices = new ArrayList<AuthenticationService>();
		PortalAuthenticationService portalAuthenticator = new PortalAuthenticationService();
		portalAuthenticator.setAuthType(AuthUtils.CAS);
		authenticationServices.add(portalAuthenticator);
		authenticationServices.add(new CasFilterAuthenticationService());
		super.setAuthenticationServices(authenticationServices);
	}

	/**
	 * @return the portal authenticator.
	 */
	protected PortalAuthenticationService getPortalAuthenticator() {
		return (PortalAuthenticationService) getAuthenticationServices().get(0);
	}

	/**
	 * @param uidPortalAttribute the uidPortalAttribute to set
	 */
	public void setUidPortalAttribute(final String uidPortalAttribute) {
		getPortalAuthenticator().setUidPortalAttribute(uidPortalAttribute);
	}

	/**
	 * @param portalAuthType the portalAuthType to set
	 */
	public void setPortalAuthType(final String portalAuthType) {
		getPortalAuthenticator().setAuthType(portalAuthType);
	}

	/**
	 * @see org.esupportail.commons.services.authentication.DelegatingAuthenticationService
	 * #setAuthenticationServices(List)
	 */
	@Override
	public void setAuthenticationServices(
			@SuppressWarnings("unused")
			final List<AuthenticationService> authenticators) {
		throw new UnsupportedOperationException(
				"method " + getClass() + ".setAuthenticators() should never be called.");
	}

}
