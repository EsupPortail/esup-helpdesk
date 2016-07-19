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

import java.io.Serializable;

import javax.servlet.ServletContext;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.handler.AbstractHandler;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;

/**
 * This servlet should be mapped to WS Xfire URLs to get a single Hibernate session.
 */
public class XFireServletController 
extends org.codehaus.xfire.transport.http.XFireServletController implements Serializable {
	
	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -9085512373620430084L;

	/**
     * Constructor.
     * @param xfire
     * @param servletContext
     */
    public XFireServletController(
    		final XFire xfire, 
    		final ServletContext servletContext) {
        super(xfire, servletContext);
        transport.addFaultHandler(new XFireExceptionHandler());
    }

    /**
     * The exception handler added to the contrroller.
     */
    public static class XFireExceptionHandler extends AbstractHandler {        

    	/**
    	 * Constructor.
    	 */
    	public XFireExceptionHandler() {
    		super();
    	}

    	/**
    	 * @see org.codehaus.xfire.handler.Handler#invoke(org.codehaus.xfire.MessageContext)
    	 * @param context
    	 */
    	@Override
		public void invoke(final MessageContext context) {
    		XFireFault fault = (XFireFault) context.getExchange().getFaultMessage().getBody();
    		ExceptionUtils.catchException(fault);
    	}    
    }
}
