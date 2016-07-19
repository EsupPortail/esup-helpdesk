/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.mock;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * An external context mock.
 *
 */
public class MockExternalContext extends ExternalContext {

	/** 
	 * The servlet context. 
	 */
	private ServletContext context;

	/**
	 * The servlet request.
	 */
	private ServletRequest request;

	/**
	 * The servlet response.
	 */
	private ServletResponse response;

	/**
	 * The application map.
	 */
	@SuppressWarnings("rawtypes")
	private Map applicationMap;

	/**
	 * The session map.
	 */
	@SuppressWarnings("rawtypes")
	private Map sessionMap;

//	/**
//	 * The request map.
//	 */
//	@SuppressWarnings("unchecked")
//	private Map requestMap;

	/**
	 * The request parameter map.
	 */
	@SuppressWarnings("rawtypes")
	private Map requestParameterMap;

	/**
	 * Constructor.
	 * @param context
	 * @param request
	 * @param response
	 */
	public MockExternalContext(
			final ServletContext context, 
			final ServletRequest request, 
			final ServletResponse response) {
		this.context = context;
		this.request = request;
		this.response = response;
	}

	/**
	 * @see javax.faces.context.ExternalContext#getSession(boolean)
	 */
	@Override
	public Object getSession(
			@SuppressWarnings("unused") final boolean create) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getContext()
	 */
	@Override
	public Object getContext() {
		return context;
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequest()
	 */
	@Override
	public Object getRequest() {
		return request;
	}

	/**
	 * @param request
	 */
	public void setRequest(
			@SuppressWarnings("unused") final Object request) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getResponse()
	 */
	@Override
	public Object getResponse() {
		return response;
	}

	/**
	 * @param response
	 */
	public void setResponse(
			@SuppressWarnings("unused") 
			final Object response) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param encoding
	 */
	public void setResponseCharacterEncoding(
			@SuppressWarnings("unused") 
			final String encoding) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getApplicationMap()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getApplicationMap() {
		if (applicationMap == null) {
			return new HashMap();
		}
		return applicationMap;
	}

	/**
	 * @param applicationMap
	 */
	@SuppressWarnings("rawtypes")
	public void setApplicationMap(final Map applicationMap) {
		this.applicationMap = applicationMap;
	}

	/**
	 * @see javax.faces.context.ExternalContext#getSessionMap()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getSessionMap() {
		if (sessionMap == null) {
			sessionMap = new HashMap();
			// for PortletUtil : test in mode servlet so no PORTLET_REQUEST_FLAG
			// sessionMap.put(PortletUtil.PORTLET_REQUEST_FLAG, "dummy");
		}
		return sessionMap;

	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestMap()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getRequestMap() {
		/*
		 * if (requestMap == null) { requestMap = new MockRequestMap(request); }
		 * return (requestMap);
		 */
		return null;
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestParameterMap()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map getRequestParameterMap() {
		if (requestParameterMap != null) {
			return requestParameterMap;
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @param requestParameterMap
	 */
	@SuppressWarnings("rawtypes")
	public void setRequestParameterMap(final Map requestParameterMap) {
		this.requestParameterMap = requestParameterMap;
	}

	/**
	 * @param encoding
	 */
	public void setRequestCharacterEncoding(@SuppressWarnings("unused") final String encoding) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestParameterValuesMap()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getRequestParameterValuesMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestParameterNames()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Iterator getRequestParameterNames() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestHeaderMap()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map getRequestHeaderMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestHeaderValuesMap()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map getRequestHeaderValuesMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestCookieMap()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map getRequestCookieMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestLocale()
	 */
	@Override
	public Locale getRequestLocale() {
		return request.getLocale();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestLocales()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Iterator getRequestLocales() {
		return new LocalesIterator(request.getLocales());
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestPathInfo()
	 */
	@Override
	public String getRequestPathInfo() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestContextPath()
	 */
	@Override
	public String getRequestContextPath() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRequestServletPath()
	 */
	@Override
	public String getRequestServletPath() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return nothing
	 */
	public String getRequestCharacterEncoding() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return nothing
	 */
	public String getRequestContentType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return nothing
	 */
	public String getResponseCharacterEncoding() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return nothing
	 */
	public String getResponseContentType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getInitParameter(java.lang.String)
	 */
	@Override
	public String getInitParameter(final String name) {
		if (name.equals(javax.faces.application.StateManager.STATE_SAVING_METHOD_PARAM_NAME)) {
			return null;
		}
		if (name.equals(javax.faces.webapp.FacesServlet.LIFECYCLE_ID_ATTR)) {
			return null;
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getInitParameterMap()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map getInitParameterMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getResourcePaths(java.lang.String)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Set getResourcePaths(
			@SuppressWarnings("unused")
			final String path) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getResource(java.lang.String)
	 */
	@SuppressWarnings("unused")
	@Override
	public URL getResource(final String path) throws MalformedURLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getResourceAsStream(java.lang.String)
	 */
	@Override
	public InputStream getResourceAsStream(
			@SuppressWarnings("unused")
			final String path) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#encodeActionURL(java.lang.String)
	 */
	@Override
	public String encodeActionURL(
			@SuppressWarnings("unused")
			final String sb) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#encodeResourceURL(java.lang.String)
	 */
	@Override
	public String encodeResourceURL(
			@SuppressWarnings("unused")
			final String sb) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#encodeNamespace(java.lang.String)
	 */
	@Override
	public String encodeNamespace(
			@SuppressWarnings("unused")
			final String aValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#dispatch(java.lang.String)
	 */
	@Override
	public void dispatch(
			@SuppressWarnings("unused")
			final String requestURI) throws FacesException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#redirect(java.lang.String)
	 */
	@Override
	public void redirect(
			@SuppressWarnings("unused")
			final String requestURI) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see javax.faces.context.ExternalContext#log(java.lang.String)
	 */
	@Override
	public void log(final String message) {
		context.log(message);
	}

	/**
	 * @see javax.faces.context.ExternalContext#log(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void log(final String message, final Throwable throwable) {
		context.log(message, throwable);
	}

	/**
	 * @see javax.faces.context.ExternalContext#getAuthType()
	 */
	@Override
	public String getAuthType() {
		return ((HttpServletRequest) request).getAuthType();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getRemoteUser()
	 */
	@Override
	public String getRemoteUser() {
		return ((HttpServletRequest) request).getRemoteUser();
	}

	/**
	 * @see javax.faces.context.ExternalContext#getUserPrincipal()
	 */
	@Override
	public java.security.Principal getUserPrincipal() {
		return ((HttpServletRequest) request).getUserPrincipal();
	}

	/**
	 * @see javax.faces.context.ExternalContext#isUserInRole(java.lang.String)
	 */
	@Override
	public boolean isUserInRole(final String role) {
		return ((HttpServletRequest) request).isUserInRole(role);
	}

	/**
	 * An iterator for Locale.
	 */
	@SuppressWarnings("rawtypes")
	private class LocalesIterator implements Iterator {

		/**
		 * The locales.
		 */
		private Enumeration locales;

		/**
		 * Constructor.
		 * @param locales
		 */
		@SuppressWarnings({ })
		public LocalesIterator(final Enumeration locales) {
			this.locales = locales;
		}

		/**
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return locales.hasMoreElements();
		}

		/**
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Object next() {
			return locales.nextElement();
		}

		/**
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}