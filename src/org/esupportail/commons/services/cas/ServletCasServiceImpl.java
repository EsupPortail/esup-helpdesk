/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.cas;

import java.io.IOException;
import java.net.URLEncoder;

import org.esupportail.commons.utils.ContextUtils;
import org.springframework.beans.factory.InitializingBean;

import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.filter.CASFilter;
import edu.yale.its.tp.cas.proxy.ProxyTicketReceptor;

/** 
 * The implementation of CasService for setvlet.
 */
public class ServletCasServiceImpl implements InitializingBean, CasService {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3376709081615445608L;

	/**
	 * Bean constructor.
	 */
	public ServletCasServiceImpl() {
		//nothing to do
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		// nothing to check
	}

	/**
	 * @see org.esupportail.commons.services.cas.CasService#getProxyTicket(java.lang.String)
	 */
	@Override
	public String getProxyTicket(final String targetService) throws CasException {
		CASReceipt receipt = (CASReceipt) ContextUtils.getSessionAttribute(CASFilter.CAS_FILTER_RECEIPT);
		String pgtIou = receipt.getPgtIou();
		if (pgtIou == null) {
			throw new CasException("pgtIou is null. Check your CAS proxy configuration.");
		}
		try {
			String url = URLEncoder.encode(targetService, "UTF-8"); 
			return ProxyTicketReceptor.getProxyTicket(pgtIou, url);
		} catch (IOException e) {
			throw new CasException("Unable to contact CAS serveur.", e);
		}
	}

	/**
	 * @see org.esupportail.commons.services.cas.CasService#validate()
	 */
	@Override
	public void validate() throws CasException {
		// nothing to validate
	}
	
}
