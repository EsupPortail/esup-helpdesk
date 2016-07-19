/*
 * Copyright 2005 Jenia org.
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
package org.fckfaces.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author srecinto
 */
public class Servlet extends HttpServlet {

	private static final long serialVersionUID = 7260045528613530636L;

	private static final String modify=calcModify();
	
	private volatile String customResourcePath;
	
	private static final String calcModify() {
		Date mod = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(mod);
	}
	
	public void init(ServletConfig config) throws ServletException { 
		super.init(config); 
		setCustomResourcePath(config.getInitParameter("customResourcePath"));
	} 

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // search the resource in classloader
        ClassLoader cl = this.getClass().getClassLoader();
        String uri = request.getRequestURI();
        String path = uri.substring(uri.indexOf(Util.FCK_FACES_RESOURCE_PREFIX)+Util.FCK_FACES_RESOURCE_PREFIX.length()+1);
        
        if(getCustomResourcePath() != null) { //Use custom path to FCKeditor
        	this.getServletContext().getRequestDispatcher(getCustomResourcePath() + path).forward(request,response);
        } else {  //Use default FCKeditor bundled up in the jar
        	if (uri.endsWith(".jsf") || uri.endsWith(".html")) {
	        	response.setContentType("text/html;charset=UTF-8");
	        } else {
	            response.setHeader("Cache-Control", "public");
	            response.setHeader("Last-Modified", modify);
	        }
	        if (uri.endsWith(".css")) {
	        	response.setContentType("text/css;charset=UTF-8");
	        } else if (uri.endsWith(".js")) {
	        	response.setContentType("text/javascript;charset=UTF-8");
	        } else if (uri.endsWith(".gif")) {
	        	response.setContentType("image/gif;");
	        } else if (uri.endsWith(".xml")) {
	        	response.setContentType("text/xml;charset=UTF-8");
	        } 
	        
	        InputStream is = cl.getResourceAsStream(path);
	        // if no resource found in classloader return nothing
	        if (is==null) return;
	        // resource found, copying on output stream
	        OutputStream out = response.getOutputStream();
	        byte[] buffer = new byte[2048];
	        BufferedInputStream bis = new BufferedInputStream(is);
	        try {
	        	int read = 0;
	        	read = bis.read(buffer);
	        	while (read!=-1) {
	        		out.write(buffer,0,read);
	        		read = bis.read(buffer);
	        	}
	        } finally {
	        	bis.close();
	        }
	        out.flush();
	        out.close();
        }
    }

	public String getCustomResourcePath() {
		return customResourcePath;
	}

	public void setCustomResourcePath(String customResourcePath) {
		synchronized (this) {
			this.customResourcePath = customResourcePath;
		}
	}

}
