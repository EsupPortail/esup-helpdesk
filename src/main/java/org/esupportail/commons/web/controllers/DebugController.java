/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.controllers;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.application.Version;
import org.esupportail.commons.services.authentication.AuthenticationService;
import org.esupportail.commons.services.authentication.info.AuthInfo;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.HttpUtils;
import org.esupportail.commons.utils.SystemUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * The debug controller, used by debug.jsp.
 */
public class DebugController implements InitializingBean, Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5561459250911861819L;

	/**
	 * The application service.
	 */
	private ApplicationService applicationService;

	/**
	 * The authentication service.
	 */
	private AuthenticationService authenticationService;

	/**
	 * Constructor.
	 */
	public DebugController() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(applicationService, "property applicationService of class " 
				+ this.getClass().getName() 
				+ " can not be null.");
		Assert.notNull(authenticationService, "property authenticationService of class " 
				+ this.getClass().getName() 
				+ " can not be null.");
	}

	/**
	 * @return the name of the application.
	 */
	public String getApplicationName() {
		return applicationService.getName();
	}

	/**
	 * @return the version of the application.
	 */
	public Version getApplicationVersion() {
		return applicationService.getVersion();
	}

	/**
	 * @return the server the application is running on.
	 */
	public String getServer() {
		return HttpUtils.getServerString();
	}

	/**
	 * @return the date when the exception was thrown.
	 */
	public String getDate() {
		return new SimpleDateFormat("EEE dd-MM-yyyy HH:mm:ss", Locale.getDefault())
		.format(new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * @return the id of the current user.
	 */
	public String getUserId() {
		AuthInfo authInfo = authenticationService.getAuthInfo();
		if (authInfo == null) {
			return null;
		}
		return authInfo.getId();
	}

	/**
	 * @return the portal the portlet is running on.
	 */
	public String getPortal() {
		return HttpUtils.getPortalInfo();
	}

	/**
	 * @return the portal the portlet is running on.
	 */
	public Boolean getQuickStart() {
		// TODO write DebugController.getQuickStart()
		return Boolean.FALSE;
	}

	/**
	 * @return the client.
	 */
	public String getClient() {
		return HttpUtils.getClientString();
	}

	/**
	 * @return the version of the application.
	 */
	public String getQueryString() {
		return HttpUtils.getQueryString();
	}

	/**
	 * @return the browser.
	 */
	public String getUserAgent() {
		return HttpUtils.getUserAgent();
	}

	/**
	 * @return the global session attributes.
	 */
	public Set<String> getGlobalSessionAttributes() {
		return ContextUtils.getGlobalSessionAttributesStrings();
	}

	/**
	 * @return the session attributes.
	 */
	public Set<String> getSessionAttributes() {
		return ContextUtils.getSessionAttributesStrings();
	}

	/**
	 * @return the request attributes.
	 */
	public Set<String> getRequestAttributes() {
		Set<String> strings = ContextUtils.getRequestAttributesStrings();
		Set<String> result = new TreeSet<String>();
		for (String string : strings) {
			if (!string.startsWith("msgs")) {
				result.add(string);
			}
		}
		return result;
	}

	/**
	 * @return the request headers.
	 */
	public Set<String> getRequestHeaders() {
		return HttpUtils.getRequestHeadersStrings();
	}

	/**
	 * @return the request parameters.
	 */
	public Set<String> getRequestParameters() {
		return HttpUtils.getRequestParametersStrings();
	}

	/**
	 * @return the cookies.
	 */
	public Set<String> getCookies() {
		return HttpUtils.getCookiesStrings();
	}

	/**
	 * @return the system properties.
	 */
	public Set<String> getSystemProperties() {
		return SystemUtils.getSystemPropertiesStrings();
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(final ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @param authenticationService the authenticationService to set
	 */
	public void setAuthenticationService(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

}
