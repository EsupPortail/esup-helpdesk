/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.application.Version;
import org.esupportail.commons.services.application.VersionException;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.utils.BeanUtils;

/**
 * The servlet to show the version number.
 */
public class VersionServlet extends HttpServlet {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -4582010858813700546L;
	
	/**
	 * The result.
	 */
	private String result;
	
	/**
	 * Constructor.
	 */
	public VersionServlet() {
		super();
	}
	
	/**
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(final ServletConfig config) throws ServletException {
		super.init(config);
		try {
			ApplicationService applicationService = 
				(ApplicationService) BeanUtils.getBean("applicationService");
			Version version = applicationService.getVersion();
			result = version.toString();
		} catch (Throwable t) {
			result = ExceptionUtils.getShortPrintableStackTrace(t);
		}
	}

	/**
	 * @throws ServletException 
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	public void service(
			@SuppressWarnings("unused")
			final ServletRequest servletRequest, 
			final ServletResponse servletResponse) 
	throws ServletException {
		try {
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			ServletOutputStream out = response.getOutputStream();
			out.write(result.getBytes());
		} catch (Throwable t) {
			Exception ve = new VersionException(t);
			ExceptionUtils.catchException(ve);
			throw new ServletException(ve);
		}
	}

}
