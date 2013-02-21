/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Icon;

/**
 * A servlet to serve the icons.
 */
public class IconServlet extends AbstractExceptionHandlingServlet {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -7699872250148542593L;

	/**
	 * The servlet info.
	 */
    private static final String SERVLET_INFO = "IconServlet";

	/**
	 * The name of the domain service bean.
	 */
	private static final String DOMAIN_SERVICE_BEAN = "domainService";

	/**
	 * The domain service.
	 */
	private DomainService domainService;

    /**
     * Constructor.
     */
    public IconServlet() {
        super();
    }

    /**
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
	public String getServletInfo() {
        return SERVLET_INFO;
    }

    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    @Override
	protected void initInternal(
			@SuppressWarnings("unused")
			final ServletConfig servletConfig) {
    	domainService = (DomainService) BeanUtils.getBean(DOMAIN_SERVICE_BEAN);
    }

	/**
	 * @see org.esupportail.helpdesk.web.servlet.AbstractExceptionHandlingServlet#serviceInternal(
	 * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void serviceInternal(final HttpServletRequest request,
                        final HttpServletResponse response) throws IOException {
    	String id = request.getParameter("id");
    	Icon icon = domainService.getIcon(Long.valueOf(id));
    	response.setContentType(icon.getContentType());
    	OutputStream os = response.getOutputStream();
    	if (icon.getData() != null) {
    		os.write(icon.getData());
    	}
    	os.close();
    }

	/**
	 * @see org.esupportail.helpdesk.web.servlet.AbstractExceptionHandlingServlet#checkVersion()
	 */
	@Override
	protected void checkVersion() throws ConfigException {
		// do no check the version
	}
}
