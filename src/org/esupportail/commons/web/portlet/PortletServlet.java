/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.portlet;

import javax.servlet.http.HttpServlet;

/**
 * @deprecated
 */
@Deprecated
public class PortletServlet extends HttpServlet {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -6996302907379712531L;
	
	/**
	 * Bean constructor.
	 */
	public PortletServlet() {
		throw new UnsupportedOperationException(
				getClass() + " is obsolete, use org.apache.pluto.core.PortletServlet instead!");
	}

}
