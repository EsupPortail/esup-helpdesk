/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils; 

/**
 * A class that provides facilities with portlet requests.
 */
public class PortletRequestUtils {

//	/**
//	 * The attribute to store the current user's id.
//	 */
//	private static final String USER_ID = "userId";
//	
//	/**
//	 * The attribute to store the state of the application.
//	 */
//	private static final String APPLICATION_STATE = "applicationState";
//	
//	/**
//	 * A logger.
//	 */
//	private static final LoggerImpl LOG = new LoggerImpl(PortletRequestUtils.class);
//
	/**
	 * Private constructor.
	 */
	private PortletRequestUtils() {
		throw new UnsupportedOperationException();
	}
//
//	/**
//	 * @param portletRequest
//	 * @return The HttpServletRequest instance that corresponds to a PortletRequest, or null if not possible.
//	 */
//	public static HttpServletRequest getHttpServletRequest(final PortletRequest portletRequest) {
//		if (!(portletRequest instanceof ServletRequestWrapper)) {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("portletRequest ('" + portletRequest.getClass().getName() 
//						+ "') is not a ServletRequestWrapper");
//			}
//			return null;
//		}
//		ServletRequestWrapper requestWrapper = (ServletRequestWrapper) portletRequest;
//		ServletRequest servletRequest = requestWrapper.getRequest();
//		if (servletRequest == null) {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("servletRequest is null");
//			}
//			return null;
//		}
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("retrieved a ServletRequest instance from portletRequest");
//		}
//		if (!(servletRequest instanceof HttpServletRequest)) {
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("servletRequest ('" + servletRequest.getClass().getName() 
//						+ "') is not a HttpServletRequest");
//			}
//			return null;
//		}
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("servletRequest ('" + servletRequest.getClass().getName() 
//					+ "') casted to HttpServletRequest");
//		}
//		return (HttpServletRequest) servletRequest;
//	}
//
//	/**
//	 * @param request
//	 * @return The portlet session attributes, as a printable string.
//	 */
//	public static String getPrintablePortletSessionAttributes(final PortletRequest request) {
//		Map<String, Object> sessionAttributes = getSessionAttributes(request);
//		Set<String> keys = sessionAttributes.keySet();
//		if (keys.isEmpty()) {
//			return null;
//		}
//		Set<String> sortedStrings = new TreeSet<String>();
//		for (String key : keys) {
//			Object obj = sessionAttributes.get(key);
//			if (obj != null) {
//				sortedStrings.add(key + " = [" + obj.toString() + "]");
//			}
//		}
//		StringBuffer printableSessionAttributes = new StringBuffer("");
//		String separator = "";
//		for (String string : sortedStrings) {
//			printableSessionAttributes.append(separator).append(string);
//			separator = "\n";
//		}
//		return printableSessionAttributes.toString();
//	}
//	
//	/**
//	 * @param request
//	 * @return The parameters of the request, as a printable string.
//	 */
//	@SuppressWarnings({ "cast", "unchecked" })
//	public static String getPrintablePortletRequestParameters(final PortletRequest request) {
//		Enumeration<String> names = (Enumeration<String>) request.getParameterNames();
//		if (!names.hasMoreElements()) {
//			return null;
//		}
//		Set<String> sortedStrings = new TreeSet<String>();
//		while (names.hasMoreElements()) {
//			String name = names.nextElement();
//			String [] values = request.getParameterValues(name);
//			for (String value : values) {
//				sortedStrings.add(name + " = [" + value + "]");
//			}
//		}
//		StringBuffer printableRequestParameters = new StringBuffer("");
//		String separator = "";
//		for (String string : sortedStrings) {
//			printableRequestParameters.append(separator).append(string);
//			separator = "\n";
//		}
//		return printableRequestParameters.toString();
//	}
//
//	/**
//	 * @param request
//	 * @return The properties of the request, as a printable string.
//	 */
//	@SuppressWarnings("unchecked")
//	public static String getPrintablePortletRequestProperties(final PortletRequest request) {
//		Enumeration<String> names = request.getPropertyNames();
//		if (!names.hasMoreElements()) {
//			return null;
//		}
//		Set<String> sortedStrings = new TreeSet<String>();
//		while (names.hasMoreElements()) {
//			String name = names.nextElement();
//			Enumeration<String> properties = request.getProperties(name);
//			while (properties.hasMoreElements()) {
//				String property = properties.nextElement();
//				sortedStrings.add(name + ": " + property);
//			}
//		}
//		StringBuffer printableRequestProperties = new StringBuffer("");
//		String separator = "";
//		for (String string : sortedStrings) {
//			printableRequestProperties.append(separator).append(string);
//			separator = "\n";
//		}
//		return printableRequestProperties.toString();
//	}
//
//	/**
//	 * @param request
//	 * @return The HTTP session attributes, as a printable string.
//	 */
//	public static String getPrintableHttpSessionAttributes(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			return null;
//		}
//		return HttpUtils.getPrintableSessionAttributes(httpServletRequest);
//	}
//	
//	/**
//	 * @param request
//	 * @return The cookies, as a printable string.
//	 */
//	public static String getPrintableCookies(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			return null;
//		}
//		return HttpUtils.getPrintableCookies(httpServletRequest);
//	}
//
//	/**
//	 * @param request
//	 * @return The headers of the request, as a printable string.
//	 */
//	public static String getPrintableHttpRequestHeaders(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			return null;
//		}
//		return HttpUtils.getPrintableRequestHeaders(httpServletRequest);
//	}
//
//	/**
//	 * @param request
//	 * @return The HTTP session attributes, as a printable string.
//	 */
//	public static String getPrintableHttpRequestParameters(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			return null;
//		}
//		return HttpUtils.getPrintableRequestParameters(httpServletRequest);
//	}
//	
//	/**
//	 * @param request
//	 * @return The client as a printable string.
//	 */
//	public static String getPrintableClient(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			LOG.debug("client address can not be retrieved from PortletRequest");
//			return null;
//		}
//		return HttpUtils.getPrintableClient(httpServletRequest);
//	}
//
//	/**
//	 * @param request
//	 * @return The server as a raw String.
//	 */
//	private static String getRawServerString(final PortletRequest request) {
//		return request.getServerName();
//	}
//
//	/**
//	 * @param request
//	 * @return The server as a printable string.
//	 */
//	public static String getPrintableServer(final PortletRequest request) {
//		String rawServerString = getRawServerString(request);
//		try {
//			InetAddress server = InetAddress.getByName(rawServerString);
//			return new StringBuffer(server.getHostAddress())
//			.append(" (").append(server.getHostName()).append(")").toString();
//		} catch (UnknownHostException e) {
//			return rawServerString;
//		}
//	}
//
//	/**
//	 * @param request
//	 * @return The user agent.
//	 */
//	public static String getUserAgent(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			// no servlet request available, try to get it from the properties of the request
//			return request.getProperty("user-agent");
//		}
//		return HttpUtils.getUserAgent(httpServletRequest);
//	}
//
//	/**
//	 * @param request
//	 * @return The query string.
//	 */
//	public static String getQueryString(final PortletRequest request) {
//		HttpServletRequest httpServletRequest = getHttpServletRequest(request);
//		if (httpServletRequest == null) {
//			return null;
//		}
//		return HttpUtils.getQueryString(httpServletRequest);
//	}
//
//	/**
//	 * @param request
//	 * @return The user's action.
//	 */
//	public static String getUserAction(final PortletRequest request) {
//		return request.getParameter("action");
//	}
//
//	/**
//	 * @param request
//	 * @return All the attributes of the current session.
//	 */
//	@SuppressWarnings("unchecked")
//	public static Map<String, Object> getSessionAttributes(final PortletRequest request) {
//		PortletSession session = request.getPortletSession();
//		Map<String, Object> attributes = new Hashtable<String, Object>();
//		if (session != null) {
//			Enumeration<String> names = session.getAttributeNames();
//			while (names.hasMoreElements()) {
//				String name = names.nextElement();
//				if (LOG.isDebugEnabled()) {
//					LOG.debug("name -> " + name);
//				}
//				Object obj = session.getAttribute(name);
//				if (obj != null) {
//					attributes.put(name, obj);
//				}
//			}
//		}
//		return attributes;
//	}
//
//	/**
//	 * @param request
//	 * @param name
//	 * @return The attribute that corresponds to a name.
//	 */
//	private static Object getSessionAttribute(final PortletRequest request, final String name) {
//		PortletSession session = request.getPortletSession();
//		Object obj = null;
//		if (session != null) {
//			obj = session.getAttribute(name);
//		}
//		return obj;
//	}
//
//	/**
//	 * @param request
//	 * @return The id of the current user.
//	 */
//	public static String getUserId(final PortletRequest request) {
//		return (String) getSessionAttribute(request, USER_ID);
//	}
//
//	/**
//	 * @param request
//	 * @return The state of the application.
//	 */
//	public static String getApplicationState(final PortletRequest request) {
//		return (String) getSessionAttribute(request, APPLICATION_STATE);
//	}
//	
}
