/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esupportail.commons.aop.monitor.MonitorUtils;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.VersionningUtils;
import org.esupportail.commons.services.database.DatabaseException;
import org.esupportail.commons.services.database.DatabaseUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.utils.ContextUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * A n abstract servlet that connects to the database and handles the exceptions.
 */
@SuppressWarnings("serial")
public abstract class AbstractExceptionHandlingServlet extends HttpServlet {
	
    /**
     * Constructor.
     */
    public AbstractExceptionHandlingServlet() {
        super();
    }

    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    @Override
	public void init(final ServletConfig servletConfig) throws ServletException {
    	try {
    		super.init(servletConfig);
    		initInternal(servletConfig);
    	} catch (Throwable t) {
    		ExceptionUtils.catchException(t);
    		if (t instanceof ServletException) {
    			throw (ServletException) t;
    		}
    		if (t.getMessage() != null) {
    			throw new ServletException(t.getMessage(), t);
    		}
    		throw new ServletException(t);
    	}
    }

    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 * @param servletConfig
     * @throws Exception 
	 */
	protected abstract void initInternal(
			final ServletConfig servletConfig) throws Exception;

	/**
	 * Check the version of the application and throw a ConfigException
	 * if the servlet can not be executed (for instance if the version
	 * of the application and the one of the database do not match).
	 * @throws ConfigException
	 */
	protected void checkVersion() throws ConfigException {
		VersionningUtils.checkVersion(true, false);
	}

	/**
	 * Wrap exceptions.
	 * @param t 
	 * @return a ServletException
	 */
	protected ServletException wrapException(
			final Throwable t) {
        if (t instanceof ServletException) {
            return (ServletException) t;
        } else if (t.getMessage() != null) {
            return new ServletException(t.getMessage(), t);
        } else {
            return new ServletException(t);
        }
	}

    /**
     * @see javax.servlet.http.HttpServlet#service(
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
	public void service(final HttpServletRequest request,
                        final HttpServletResponse response) 
    throws ServletException {

    	long startTime = System.currentTimeMillis();
    	ServletRequestAttributes previousRequestAttributes = null;
        try {
        	previousRequestAttributes = ContextUtils.bindRequestAndContext(request, getServletContext());
            DatabaseUtils.open();
           	checkVersion();
           	serviceInternal(request, response);
		} catch (Throwable t) {
			ExceptionUtils.catchException(t);
			throw wrapException(t);
        } finally {
            try {
				DatabaseUtils.close();
			} catch (DatabaseException e) {
				ExceptionUtils.catchException(e);
			}
        	MonitorUtils.log(startTime, "");
        	ContextUtils.unbindRequest(previousRequestAttributes);
        }
    }

    /**
     * @param request 
     * @param response 
     * @throws Exception 
     * @see javax.servlet.http.HttpServlet#service(
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	protected abstract void serviceInternal(final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception;
}
