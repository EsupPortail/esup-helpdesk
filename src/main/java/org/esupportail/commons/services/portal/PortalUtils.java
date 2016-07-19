/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.portal;

import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.portal.ws.client.PortalService;

/**
 * A class that provides utilities to access portal services.
 */
public class PortalUtils {

	/**
	 * The name of the PortalService bean.
	 */
	private static final String PORTAL_SERVICE_BEAN = "portalService";

	/**
	 * No instanciation.
	 */
	private PortalUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the portal service.
	 */
	public static PortalService createPortalService() {
		return (PortalService) BeanUtils.getBean(PORTAL_SERVICE_BEAN);
	}

}
