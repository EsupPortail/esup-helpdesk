/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.batch;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.mock.MockExternalContext;
import org.esupportail.commons.mock.MockFacesContext;
import org.esupportail.commons.mock.MockFacesContextHelper;
import org.esupportail.commons.utils.ContextUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.RequestContextFilter;

/**
 * A class used to create a Web Application Environment with Mock Objects like
 * - MockHttpServletResponse,
 * - MockHttpServletRequest,
 * - MockHttpSession,
 * - MockFacesContext, etc.
 * 
 * It loads spring config files (by default "classpath:properties/applicationContext.xml") in a WebApplicationContext
 * puts this WebApplicationContext in a MockServletContext
 * and binds this MockServletContext with the MockHttpServletRequest
 * 
 * @see WebApplicationFilter 
 * 
 */
public class WebApplicationEnvironment {
	
	/**
	 * the location of configs.
	 */
	public static final String[] CONFIG_LOCATIONS = new String[]{"classpath:properties/applicationContext.xml" };

	/**
	 * 
	 */
	private static MockHttpSession session;

	/**
	 * 
	 */
	private static RequestContextFilter contextFilter;

	/**
	 * 
	 */
	private static MockFacesContext facesContext;

	/**
	 * 
	 */
	private static MockServletContext context;

	/**
	 * 
	 */
	private final Log log = LogFactory.getLog(getClass());
	
	/**
	 * 
	 */
	private MockHttpServletResponse response;

	/**
	 * 
	 */
	private MockHttpServletRequest request;

	static {

		context = new MockServletContext();

		MockFilterConfig filterConfig = new MockFilterConfig(context, "filterConfig");
		contextFilter = new RequestContextFilter();
		try {
			contextFilter.init(filterConfig);
		} catch (ServletException e) {
			throw new BeanInitializationException("Could not initialize filter bean", e);
		}

		session = new MockHttpSession(context);

		facesContext = new MockFacesContext();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockExternalContext externalContext = new MockExternalContext(context, request, response);
		facesContext.setExternalContext(externalContext);

		MockFacesContextHelper.setMockContext(facesContext);
		
	}
	
	/**
	 * Constructor, reinitialize a new request/response.
	 * => make a new Instance of WebApplicationUtils when you want to reinitialize a new request/response 
	 */
	public WebApplicationEnvironment() {
		init(true);
	}

	/**
	 * Constructor, reinitialize a new request/response.
	 * => make a new Instance of WebApplicationUtils when you want to reinitialize a new request/response 
	 * @param preverseSession - True if you want preserve the current session across requests 
	 */
	public WebApplicationEnvironment(boolean preverseSession) {
		init(preverseSession);
	}

	/**
	 * @param preverseSession
	 */
	private void init(boolean preverseSession) {
		request = new MockHttpServletRequest(context);
		if (preverseSession) {
			request.setSession(session);			
		} else {
			session = new MockHttpSession(context);
		}
		response = new MockHttpServletResponse();
	}

	/**
	 * Load default context locations i.e. new String[]{ "classpath:properties/applicationContext.xml" }.
	 * @return the ApplicationContext loaded
	 * @throws Exception
	 */
	public ConfigurableApplicationContext loadDefaultContextLocations() throws Exception {

		return loadContextLocations(CONFIG_LOCATIONS);
	}

	/**
	 * Load context locations.
	 * @param locations : new String[]{ "classpath:properties/applicationContext.xml" } for example
	 * @return the ApplicationContext loaded
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ConfigurableApplicationContext loadContextLocations(
			final String[] locations) throws Exception {

		if (log.isInfoEnabled()) {
			log.info("Loading web application context for: " 
					+ StringUtils.arrayToCommaDelimitedString(locations));
		}

		XmlWebApplicationContext ctx = new XmlWebApplicationContext();
		ctx.setConfigLocations(locations);
		ctx.setServletContext(context);
		ctx.refresh();

		MockExternalContext externalContext = (MockExternalContext) facesContext.getExternalContext();
		@SuppressWarnings("rawtypes")
		Map applicationMap = new HashMap();
		applicationMap.put(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ctx);
		externalContext.setApplicationMap(applicationMap);

		context.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ctx);
		
		ContextUtils.bindRequestAndContext(request, context);
		
		return ctx;
	}

	/**
	 * @return MockServletContext
	 */
	public MockServletContext getContext() {
		return context;
	}

	/**
	 * @return MockHttpServletResponse
	 */
	public MockHttpServletResponse getResponse() {
		return response;
	}

	/**
	 * @return MockHttpServletRequest
	 */
	public MockHttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return RequestContextFilter
	 */
	public RequestContextFilter getContextFilter() {
		return contextFilter;
	}

	
}
