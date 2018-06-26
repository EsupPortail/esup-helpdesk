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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.xfire.spring.XFireSpringServlet;
import org.esupportail.commons.services.application.VersionningUtils;
import org.esupportail.commons.services.database.DatabaseUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.utils.BeanUtils;
import org.esupportail.commons.utils.ContextUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * This servlet should be mapped to WS Xfire URLs to get a single Hibernate session.
 */
public class XFireServlet extends XFireSpringServlet {
	
	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 5965503636039384518L;

//	/**
//	 * A logger.
//	 */
//	private final Logger logger = new LoggerImpl(getClass());
	
    /**
     * Constructor.
     */
    public XFireServlet() {
        super();
    }

    /**
	 * @see org.codehaus.xfire.transport.http.XFireServlet#createController()
	 */
	@Override
	public XFireServletController createController() {
        return new XFireServletController(createXFire(), getServletContext());
    }

	/**
	 * @see javax.servlet.http.HttpServlet#service(
	 * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(
			final HttpServletRequest request, 
			final HttpServletResponse response) 
	throws ServletException, IOException {
		boolean generateWsdl = request.getQueryString() != null 
		&& request.getQueryString().trim().equalsIgnoreCase("wsdl");
    	ServletRequestAttributes previousRequestAttributes = null;
        try {
        	previousRequestAttributes = ContextUtils.bindRequestAndContext(request, getServletContext());
			if (!generateWsdl) {
				BeanUtils.initBeanFactory(getServletContext());
				DatabaseUtils.open();
				DatabaseUtils.begin();
				VersionningUtils.checkVersion(true, false);
			}
			super.service(request, response);
			if (!generateWsdl) {
				DatabaseUtils.commit();
				DatabaseUtils.close();
			}
		} catch (Throwable t) {
			ExceptionUtils.catchException(t);
			if (!generateWsdl) {
				DatabaseUtils.close();
			}
        	ContextUtils.unbindRequest(previousRequestAttributes);
			if (t instanceof ServletException) {
				throw (ServletException) t;
			}
			if (t instanceof IOException) {
				throw (IOException) t;
			}
			throw new ServletException(t);
		}
	}
    
}
