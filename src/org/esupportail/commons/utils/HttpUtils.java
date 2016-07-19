/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils; 

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortalContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esupportail.commons.exceptions.NoRequestBoundException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.portlet.context.PortletRequestAttributes;

/**
 * A class that provides facilities with HTTP requests.
 */
public class HttpUtils {

	/**
	 * A logger.
	 */
	private static final Logger LOG = new LoggerImpl(HttpUtils.class);

	/**
	 * Private constructor.
	 */
	private HttpUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return The current HttpServletRequest.
	 * @throws NoRequestBoundException 
	 */
	public static HttpServletRequest getHttpServletRequest() throws NoRequestBoundException {
		if (ContextUtils.isServlet()) {
			return ((ServletRequestAttributes) ContextUtils.getContextAttributes()).getRequest();
		}
		PortletRequest portletRequest = 
			((PortletRequestAttributes) ContextUtils.getContextAttributes()).getRequest();
		return ContextUtils.getHttpServletRequestFromPortletRequest(portletRequest);
	}

	/**
	 * @return The current PortletRequest.
	 */
	private static PortletRequest getPortletRequest() {
		RequestAttributes requestAttributes = ContextUtils.getContextAttributes();
		if (!ContextUtils.isPortlet()) {
			throw new UnsupportedOperationException(
			"call HttpUtils.getPortletRequest() in portlet mode only!");
		} 
		return ((PortletRequestAttributes) requestAttributes).getRequest();
	}

	/**
	 * @return The request headers.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, List<String>> getRequestHeaders() {
		HttpServletRequest request = getHttpServletRequest();
		if (request == null) {
			return null;
		}
		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames == null) {
			return null;
		}
		Map<String, List<String>>  result = new HashMap<String, List<String>>();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			List<String> values = new ArrayList<String>();
			result.put(headerName, values);
			Enumeration<String> headers = request.getHeaders(headerName); 
			while (headers.hasMoreElements()) {
				String header = headers.nextElement(); 
				values.add(header);
			}
		}
		return result;
	}

	/**
	 * @return The names of the request headers.
	 */
	@SuppressWarnings("unchecked")
	public static Enumeration<String> getRequestHeaderNames() {
		HttpServletRequest request = getHttpServletRequest();
		if (request == null) {
			return null;
		}
		return request.getHeaderNames();
	}

	/**
	 * @return the cookies.
	 */
	public static Cookie [] getCookies() {
		HttpServletRequest request = getHttpServletRequest();
		if (request == null) {
			return null;
		}
		return request.getCookies();
	}

	/**
	 * @return the cookies as a set of readable strings.
	 */
	public static Set<String> getCookiesStrings() {
		Set<String> sortedStrings = new TreeSet<String>();
		Cookie [] cookies = getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				sortedStrings.add(cookie.getName() + " = [" + cookie.getValue() + "]");
			}
		}
		return sortedStrings;
	}

	/**
	 * @return The headers of the request, as a set of strings.
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> getRequestHeadersStrings() {
		Set<String> sortedStrings = new TreeSet<String>();
		HttpServletRequest request = getHttpServletRequest();
		if (request == null) {
			return null;
		}
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			Enumeration<String> headers = request.getHeaders(name);
			while (headers.hasMoreElements()) {
				String header = headers.nextElement();
				sortedStrings.add(name + ": " + header);
			}
		}
		return sortedStrings;
	}

	/**
	 * @param name
	 * @return The request parameter that corresponds to a name.
	 */
	public static Object getRequestParameter(final String name) {
		HttpServletRequest request = getHttpServletRequest();
		if (request == null) {
			return null;
		}
		return getHttpServletRequest().getParameter(name);
	}

	/**
	 * @return The parameters of the request, as a set of strings.
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> getRequestParametersStrings() {
		Set<String> sortedStrings = new TreeSet<String>();
		HttpServletRequest request = getHttpServletRequest();
		if (request == null) {
			return null;
		}
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String [] values = request.getParameterValues(name);
			for (String value : values) {
				sortedStrings.add(name + " = [" + value + "]");
			}
		}
		return sortedStrings;
	}

	/**
	 * @return The client as a raw String, or null if not found.
	 */
	private static String getRawClientString() {
		HttpServletRequest request = getHttpServletRequest();
		if (request == null) {
			return null;
		}
		String remoteAddr = request.getRemoteAddr();
		if (LOG.isDebugEnabled()) {
			LOG.debug("client address is: " + remoteAddr);
		}
		return remoteAddr;
	}
	
	/**
	 * @return The client as a resolved InetAddress.
	 * @throws UnknownHostException 
	 */
	public static InetAddress getClient() throws UnknownHostException {
		String rawClientString = getRawClientString();
		if (rawClientString == null) {
			throw new UnknownHostException("could not get client address!");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("resolving '" + rawClientString + "'");
		}
		return getRealInetAddress(rawClientString);
	}

	/**
	 * @return The client as a printable string.
	 */
	public static String getClientString() {
		String rawClientString = getRawClientString(); 
		if (rawClientString == null) {
			return null;
		}
		try {
			InetAddress client = getRealInetAddress(rawClientString);
			return new StringBuffer(client.getHostAddress())
			.append(" (").append(client.getHostName()).append(")").toString();
		} catch (UnknownHostException e) {
			LOG.error("UnknownHostException caught: " + e);
			return rawClientString;
		}
	}

	/**
	 * @return The server as a raw String, or null if not found.
	 */
	private static String getRawServerString() {
		HttpServletRequest request = getHttpServletRequest();
		if (request == null) {
			return null;
		}
		String serverAddr = request.getServerName();
		if (LOG.isDebugEnabled()) {
			LOG.debug("server address is: " + serverAddr);
		}
		return serverAddr;
	}
	
	/**
	 * @return The server as a resolved InetAddress.
	 * @throws UnknownHostException 
	 */
	public static InetAddress getServer() throws UnknownHostException {
		String rawServerString = getRawServerString();
		if (rawServerString == null) {
			throw new UnknownHostException("could not get server address!");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("resolving '" + rawServerString + "'");
		}
		return InetAddress.getByName(rawServerString);
	}
	
	/**
	 * @param name 
	 * @return the real IP address, not localhost.
	 * @throws UnknownHostException 
	 */
	private static InetAddress getRealInetAddress(final String name) 
	throws UnknownHostException {
		if (!name.equals("localhost") && !name.equals("127.0.0.1") && !name.equals("0:0:0:0:0:0:0:1")) {
			InetAddress host = InetAddress.getByName(name);
			host.getAddress();
			return host;
		}
		try {
			@SuppressWarnings("rawtypes")
			Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
			if (interfaces == null) {
				InetAddress host = InetAddress.getByName(name);
				return host;
			}	
			while (interfaces.hasMoreElements()) {
				NetworkInterface card = (NetworkInterface) interfaces.nextElement();
				@SuppressWarnings("rawtypes")
				Enumeration addresses = card.getInetAddresses();
				if (addresses == null) {
					continue;
				}
				while (addresses.hasMoreElements()) {
					InetAddress address = (InetAddress) addresses.nextElement();
					address.getAddress();
					String addressName = address.getHostName();
					if (!addressName.equals("localhost") 
							&& !addressName.equals("127.0.0.1") 
							&& !addressName.equals("0:0:0:0:0:0:0:1")) {
						return address;
					}
				}
			}
		} catch (SocketException e) {
			// fall back to default
		}
		return InetAddress.getByName(name);
	}

	/**
	 * @return The server as a printable string.
	 */
	public static String getServerString() {
		String rawServerString = getRawServerString();
		if (rawServerString == null) {
			return null;
		}
		try {
			InetAddress server = getRealInetAddress(rawServerString);
			return new StringBuffer(server.getHostAddress())
			.append(" (").append(server.getHostName()).append(")").toString();
		} catch (UnknownHostException e) {
			LOG.debug("UnknownHostException caught!");
			return rawServerString;
		}
	}

	/**
	 * @return The user agent.
	 */
	public static String getUserAgent() {
		HttpServletRequest request;
		try {
			request = getHttpServletRequest();
		} catch (NoRequestBoundException e) {
			// batch context
			return null;
		}
		if (request == null) {
			return null;
		}
		return request.getHeader("user-agent");
	}

	/**
	 * @return The remote user.
	 */
	public static String getRemoteUser() {
		HttpServletRequest request = getHttpServletRequest();
		if (request == null) {
			return null;
		}
		return request.getRemoteUser();
	}

	/**
	 * @return The query string.
	 */
	public static String getQueryString() {
		HttpServletRequest request = getHttpServletRequest();
		if (request == null) {
			return null;
		}
		return request.getQueryString();
	}
	
	/**
	 * @param prefName 
	 * @return a JSR-168 preference (in portlet mode only).
	 */
	public static String getPortalPref(final String prefName) {
		Map<String, Object> attrs = getPortalPrefs();
		if (attrs == null) {
			return null;
		}
		Object value = attrs.get(prefName); 
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	/**
	 * @return the JSR-168 preference (in portlet mode only).
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getPortalPrefs() {
		return (Map<String, Object>) ContextUtils.getRequestAttribute(PortletRequest.USER_INFO);
	}

	/**
	 * @return the portal info.
	 */
	public static String getPortalInfo() {
		PortletRequest request = getPortletRequest();
		if (request == null) {
			return null;
		}
		PortalContext portalContext = request.getPortalContext();
		if (portalContext == null) {
			return null;
		}
		return portalContext.getPortalInfo();
	}
	
	/**
	 * @return The current HttpServletResponse, null if not found.
	 */
	public static HttpServletResponse getHttpServletResponse() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("FacesContext.getCurrentInstance() returns null, " 
						+ "can not get the current HTTP servlet response.");
			}
			return null;
		}
		ExternalContext externalContext = facesContext.getExternalContext();
		if (externalContext == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("facesContext.getExternalContext() returns null, " 
						+ "can not get the current HTTP servlet response.");
			}
			return  null;
		}
		Object responseObject = externalContext.getResponse();
		if (responseObject == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("externalContext.getRequest() returns null, " 
						+ "can not get the current HTTP servlet response.");
			}
			return null;
		}
		if (!(responseObject instanceof HttpServletResponse)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("requestObject is not a HttpServletResponse, " 
						+ "can not get the current HTTP servlet response.");
			}
			return null;
		}
		return (HttpServletResponse) responseObject;
	}

}
