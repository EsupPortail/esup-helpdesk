/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication;

import edu.yale.its.tp.cas.client.filter.CASFilter;

import org.esupportail.commons.utils.ContextUtils;

/** 
 * A CAS authenticator.
 */
public class CasFilterAuthenticationService extends AbstractTypedAuthenticationService {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 943489018651202646L;

	/**
	 * Bean constructor.
	 */
	public CasFilterAuthenticationService() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractTypedAuthenticationService#getAuthId()
	 */
	@Override
	protected String getAuthId() {
		return (String) ContextUtils.getGlobalSessionAttribute(CASFilter.CAS_FILTER_USER);
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractTypedAuthenticationService#getAuthType()
	 */
	@Override
	protected String getAuthType() {
		return AuthUtils.CAS;
	}

}
