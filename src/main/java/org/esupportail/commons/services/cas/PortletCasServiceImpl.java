/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.cas;

import org.esupportail.commons.utils.HttpUtils;
import org.springframework.beans.factory.InitializingBean;


/** 
 * The implementation of CasService for portlets. The PGT is supposed 
 * to be passed to the portlet as a preferences attribute.
 */
public class PortletCasServiceImpl extends AbstractCasService implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7540349970957963666L;

	/**
	 * The default preferences attribute used to pass the PT to the portlet. 
	 */
	private static final String DEFAULT_CAS_PROXY_TICKET_PREF = "casProxyTicket";
	
	/**
	 * The preferences attribute used to pass the PT to the portlet. 
	 */
	private String casProxyTicketPref;

	/**
	 * Bean constructor.
	 */
	public PortletCasServiceImpl() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.cas.AbstractCasService#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (casProxyTicketPref == null) {
			casProxyTicketPref = DEFAULT_CAS_PROXY_TICKET_PREF;
		}
	}
	
	/**
	 * @return the PGT.
	 */
	@Override
	protected String getServiceTicket() {
		return HttpUtils.getPortalPref(casProxyTicketPref);
	}
	
}
