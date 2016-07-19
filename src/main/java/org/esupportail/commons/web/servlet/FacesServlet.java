/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.servlet;

/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.el.VariableResolver;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esupportail.commons.aop.monitor.MonitorUtils;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.VersionException;
import org.esupportail.commons.services.application.VersionningUtils;
import org.esupportail.commons.services.database.DatabaseException;
import org.esupportail.commons.services.database.DatabaseUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionService;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.urlGeneration.AbstractUrlGenerator;
import org.esupportail.commons.services.urlGeneration.ServletUrlGeneratorImpl;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.web.deepLinking.DeepLinkingRedirector;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * A JSF-based servlet that catches exception and gives them to an exception service.
 * see Javadoc of <a href="http://java.sun.com/j2ee/javaserverfaces/1.1_01/docs/api/index.html">JSF Specification</a>
 */
public class FacesServlet extends HttpServlet {
	
	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 6668301918264753450L;

	/**
	 * The default name of the redirector.
	 */
	private static final String DEFAULT_REDIRECTOR_NAME = "deepLinkingRedirector";

	/**
	 * The name of the servlet parameter that gives the name of the redirector.
	 */
	private static final String REDIRECTOR_NAME_PARAM = "deep-linking-redirector";

	/**
	 * The default default view.
	 */
	private static final String DEFAULT_DEFAULT_VIEW = "/stylesheets/welcome.faces";

	/**
	 * The name of the servlet parameter that gives the name of the redirector.
	 */
	private static final String DEFAULT_VIEW_PARAM = "default-view";

	/**
	 * The name of the servlet parameter that tells if we should use a redirector.
	 */
	private static final String USE_REDIRECTOR_PARAM = "use-redirector";

	/**
	 * The servlet info.
	 */
    private static final String SERVLET_INFO = "FacesServlet";
    
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
    /**
     * The context factory.
     */
    private FacesContextFactory facesContextFactory;
    
    /**
     * The lifecycle.
     */
    private Lifecycle lifecycle;

	/**
	 * The name of the redirector.
	 */
	private String redirectorName;
	
	/**
	 * The default view.
	 */
	private String defaultView;
	
	/**
	 * True to use a redirector.
	 */
	private boolean useRedirector = true;

    /**
     * Constructor.
     */
    public FacesServlet() {
        super();
    }

    /**
     * @see javax.servlet.Servlet#destroy()
     */
    @Override
	public void destroy() {
        facesContextFactory = null;
        lifecycle = null;
    }

    /**
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
	public String getServletInfo() {
        return SERVLET_INFO;
    }

    /**
     * @return the lyfeCycle id.
     */
    private String getLifecycleId() {
        String lifecycleId = getServletConfig().getInitParameter(javax.faces.webapp.FacesServlet.LIFECYCLE_ID_ATTR);
        if (lifecycleId == null) {
        	lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
        }
        return lifecycleId;
    }

    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    @Override
	public void init(final ServletConfig servletConfig) throws ServletException {
    	try {
    		super.init(servletConfig);
    		facesContextFactory = 
    			(FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    		LifecycleFactory lifecycleFactory = 
    			(LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    		lifecycle = lifecycleFactory.getLifecycle(getLifecycleId());
    		defaultView = servletConfig.getInitParameter(DEFAULT_VIEW_PARAM);
    		if (!StringUtils.hasText(defaultView)) {
    			defaultView = DEFAULT_DEFAULT_VIEW;
    			logger.info("property " + DEFAULT_VIEW_PARAM
    					+ " is not set, using default value [" + defaultView + "]");
    		}
    		if (servletConfig.getInitParameter(USE_REDIRECTOR_PARAM) != null) {
    			useRedirector = Boolean.valueOf(servletConfig.getInitParameter(USE_REDIRECTOR_PARAM));
    		}
    		if (useRedirector) {
	    		redirectorName = servletConfig.getInitParameter(REDIRECTOR_NAME_PARAM);
	    		if (!StringUtils.hasText(redirectorName)) {
	    			redirectorName = DEFAULT_REDIRECTOR_NAME;
	    			logger.info("property " + REDIRECTOR_NAME_PARAM 
	    					+ " is not set, using default value [" + redirectorName + "]");
	    		}
    		}
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
	 * Check the version of the application and throw a ConfigException
	 * if the servlet can not be executed (for instance if the version
	 * of the application and the one of the database do not match).
	 * @throws ConfigException
	 */
	protected void checkVersion() throws ConfigException {
		// This method can be overriden by particular implementations
		VersionningUtils.checkVersion(true, false);
	}

	/**
	 * Wrap exceptions thrown when already catching an exception.
	 * @param t 
	 * @throws ServletException 
	 * @throws IOException 
	 */
	private void handleExceptionHandlingException(
			final Throwable t) 
	throws ServletException, IOException {
		logger.error(
				"An exception was thrown while already catching a previous exception:", 
				t); 
        if (t instanceof IOException) {
            throw (IOException) t;
        } else if (t instanceof ServletException) {
            throw (ServletException) t;
        } else if (t.getMessage() != null) {
            throw new ServletException(t.getMessage(), t);
        } else {
            throw new ServletException(t);
        }
	}

	/**
	 * Catch an exception.
	 * @param throwable
	 * @param request 
	 * @param response 
	 * @throws ServletException 
	 * @throws IOException 
	 */
	protected void catchException(
			final Throwable throwable, 
			final HttpServletRequest request, 
			final HttpServletResponse response) throws ServletException, IOException {
		ExceptionUtils.markExceptionCaught(); 
		ExceptionService exceptionService = null;
		try {
			exceptionService = ExceptionUtils.catchException(throwable);
		} catch (Throwable t) {
			handleExceptionHandlingException(t);
			// never reached, prevent from warnings
			return;
		}
		ExceptionUtils.markExceptionCaught(exceptionService); 
		try {
			String exceptionUrl = request.getContextPath() 
			+ exceptionService.getExceptionView().replace(".jsp", ".faces");
			response.sendRedirect(exceptionUrl);
			logger.info("redirected the browser to [" + exceptionUrl + "]");
			return;
		} catch (Throwable t) {
			handleExceptionHandlingException(t);
		}
	}

	/**
	 * @param facesContext 
	 * @return the redirector
	 */
	private DeepLinkingRedirector getRedirector(
			final FacesContext facesContext) {
        VariableResolver vr = facesContext.getApplication().getVariableResolver();
        DeepLinkingRedirector deepLinkingRedirector = 
        	(DeepLinkingRedirector) vr.resolveVariable(facesContext, redirectorName);
        if (deepLinkingRedirector == null) {
        	throw new ConfigException("bean [" + redirectorName + "] not found!");
        }
        return deepLinkingRedirector;
	}

	/**
	 * @param request 
	 * @return the parameters of the current request.
	 */
	private Map<String, String> getParams(
			final HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		Map<String, String>  params = AbstractUrlGenerator.decodeArgToParams(
				request.getParameter(ServletUrlGeneratorImpl.ARGS_PARAM));
		return params;
	}

    /**
     * @see javax.servlet.http.HttpServlet#service(
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
	public void service(final HttpServletRequest request,
                        final HttpServletResponse response) 
    throws IOException, ServletException {

    	long startTime = System.currentTimeMillis();
        FacesContext facesContext = null;
    	ServletRequestAttributes previousRequestAttributes = null;
        boolean openDatabases = true;
        try {
        	previousRequestAttributes = ContextUtils.bindRequestAndContext(request, getServletContext());
            String pathInfo = request.getPathInfo();

            // if it is a prefix mapping ...
            if (pathInfo != null && (pathInfo.startsWith("/WEB-INF") || pathInfo .startsWith("/META-INF"))) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(" Someone is trying to access a secure resource : " + pathInfo);
                buffer.append("\n remote address is " + request.getRemoteAddr());
                buffer.append("\n remote host is " + request.getRemoteHost());
                buffer.append("\n remote user is " + request.getRemoteUser());
                buffer.append("\n request URI is " + request.getRequestURI());
                logger.warn(buffer.toString());
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            boolean beanError = false;
            boolean exceptionAlreadyCaught = ExceptionUtils.exceptionAlreadyCaught();
            if (exceptionAlreadyCaught) {
            	ExceptionService exceptionService = ExceptionUtils.getMarkedExceptionService();
            	if (exceptionService != null) {
	            	Throwable throwable = exceptionService.getThrowable();
	            	if (throwable != null) {
	            		if (ExceptionUtils.hasCause(
	            				throwable, 
	            				new Class [] {
	            						DatabaseException.class, 
	            						VersionException.class, 
	            						BeansException.class, })) { 
	            			openDatabases = false;
	            		}
	            		if (ExceptionUtils.hasCause(
	            				throwable, 
	            				BeansException.class)) { 
	            			beanError = true;
	            		}
	            	}
            	}
            }
            if (openDatabases) {
	            DatabaseUtils.open();
	            DatabaseUtils.begin();
            	checkVersion();
			}
            facesContext = facesContextFactory.getFacesContext(
            		getServletConfig().getServletContext(), request, response, lifecycle);
            boolean firstCall;
			DeepLinkingRedirector redirector = null;
            if (beanError) {
            	firstCall = false;
            } else if (useRedirector)  {
				redirector = getRedirector(facesContext);
				firstCall = redirector.firstCall();
            } else {
            	firstCall = false;
            }
    		Map<String, String> params = getParams(request);
    		if (!exceptionAlreadyCaught && (params != null || firstCall)) {
    			String view = null;
    			if (redirector != null) {
    				view = redirector.redirect(params);
    			}
    			if (view == null) {
    				view = defaultView;
    			}
    	        Application application = facesContext.getApplication();
    	        ViewHandler viewHandler = application.getViewHandler();
    	        UIViewRoot viewRoot = viewHandler.createView(facesContext, view);
    	        viewRoot.setViewId(view);
    	        facesContext.setViewRoot(viewRoot);
    		} else {
    			lifecycle.execute(facesContext);
    		}
			if (openDatabases) {
				DatabaseUtils.commit();
			}
			lifecycle.render(facesContext);
		} catch (Throwable t) {
			catchException(t, request, response);
        } finally {
        	if (openDatabases) {
	            try {
					DatabaseUtils.close();
				} catch (DatabaseException e) {
					ExceptionUtils.catchException(e);
				}
        	}
        	MonitorUtils.log(startTime, "");
        	ContextUtils.unbindRequest(previousRequestAttributes);
        	if (facesContext != null) {
        		facesContext.release();
        	}
        }
    }
}
