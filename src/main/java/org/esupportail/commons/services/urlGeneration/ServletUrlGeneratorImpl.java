/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.urlGeneration; 

import java.util.Map;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.strings.StringUtils;

/**
 * A bean to generate URLs to the application.
 */
public class ServletUrlGeneratorImpl extends AbstractCasUrlGenerator {

	/**
	 * The name of the parameter for servlets.
	 */
	public static final String ARGS_PARAM = "args";
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6209718128108301346L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The servlet URL.
	 */
	private String servletUrl;
	
	/**
	 * The servlet CAS Login URL.
	 */
	private String servletCasLoginUrl;
	
	/**
	 * The servlet Shibboleth Login URL.
	 */
	private String servletShibbolethLoginUrl;
	
	/**
	 * The servlet Guest URL.
	 */
	private String servletGuestUrl;
	
	/**
	 * Bean constructor.
	 */
	public ServletUrlGeneratorImpl() {
		super();
	}
	
	/**
	 * @see org.esupportail.commons.services.urlGeneration.AbstractCasUrlGenerator#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (servletCasLoginUrl == null) {
			if (servletUrl == null) {
				logger.warn(
						"servletCasLoginUrl and servletUrl are null, "
						+ "servlet URLs for CAS users will not be available");
			} else {
				servletCasLoginUrl = servletUrl + "/stylesheets/cas.faces";
			}
		}
		if (servletShibbolethLoginUrl == null) {
			if (servletUrl == null) {
				logger.warn(
						"servletShibbolethLoginUrl and servletUrl are null, "
						+ "servlet URLs for Shibboleth users will not be available");
			} else {
				servletShibbolethLoginUrl = servletUrl + "/stylesheets/shibboleth.faces";
			}
		}
		if (servletGuestUrl == null) {
			if (servletUrl == null) {
				logger.warn(
						"servletGuestUrl and servletUrl are null, "
						+ "servlet URLs for application users will not be available");
			} else {
				servletGuestUrl = servletUrl + "/stylesheets/welcome.faces";
			}
		}
	}

	/**
	 * @see org.esupportail.commons.services.urlGeneration.AbstractUrlGenerator#url(String, java.util.Map)
	 */
	@Override
	protected String url(
			final String authType,
			final Map<String, String> params) {
		String arg = encodeParamsToArg(params);
		String url;
		if (AuthUtils.CAS.equals(authType)) {
			if (getCasLoginUrl() == null) {
				throw new ConfigException("casLoginUrl is null");
			}
			if (servletCasLoginUrl == null) {
				throw new ConfigException("servletCasLoginUrl is null");
			}
			url = servletCasLoginUrl;
		} else if (AuthUtils.SHIBBOLETH.equals(authType)) {
			if (servletShibbolethLoginUrl == null) {
				throw new ConfigException("servletShibbolethLoginUrl is null");
			}
			url = servletShibbolethLoginUrl;
		} else {
			if (servletGuestUrl == null) {
				throw new ConfigException("servletGuestUrl is null");
			}
			url = servletGuestUrl;
		}
		if (arg != null) {
			url = url + "?" + ARGS_PARAM 
			+ "=" + StringUtils.utf8UrlEncode(arg);
		}
		if (AuthUtils.CAS.equals(authType)) {
			url = String.format(getCasLoginUrl(), 
					StringUtils.utf8UrlEncode(url));
		}
		return url;
	}

	/**
	 * @see org.esupportail.commons.services.urlGeneration.AbstractUrlGenerator#getMediaUrl()
	 */
	@Override
	protected String getMediaUrl() {
		return getServletUrl() + "/stylesheets/" + getMediaPath();
	}

	/**
	 * @return the servletUrl
	 */
	protected String getServletUrl() {
		return servletUrl;
	}

	/**
	 * @param servletUrl the servletUrl to set
	 */
	public void setServletUrl(final String servletUrl) {
		this.servletUrl = StringUtils.nullIfEmpty(servletUrl);
		if (this.servletUrl != null) {
			while (this.servletUrl.endsWith("/")) {
				this.servletUrl = this.servletUrl.substring(0, this.servletUrl.length() - 1);
			}
		}
	}

	/**
	 * @return the servletCasLoginUrl
	 */
	protected String getServletCasLoginUrl() {
		return servletCasLoginUrl;
	}

	/**
	 * @param servletCasLoginUrl the servletCasLoginUrl to set
	 */
	public void setServletCasLoginUrl(final String servletCasLoginUrl) {
		this.servletCasLoginUrl = StringUtils.nullIfEmpty(servletCasLoginUrl);
	}

	/**
	 * @return the servletShibbolethLoginUrl
	 */
	protected String getServletShibbolethLoginUrl() {
		return servletShibbolethLoginUrl;
	}

	/**
	 * @param servletShibbolethLoginUrl the servletShibbolethLoginUrl to set
	 */
	public void setServletShibbolethLoginUrl(final String servletShibbolethLoginUrl) {
		this.servletShibbolethLoginUrl = StringUtils.nullIfEmpty(servletShibbolethLoginUrl);
	}

	/**
	 * @return the servletGuestUrl
	 */
	protected String getServletGuestUrl() {
		return servletGuestUrl;
	}

	/**
	 * @param servletGuestUrl the servletGuestUrl to set
	 */
	public void setServletGuestUrl(final String servletGuestUrl) {
		this.servletGuestUrl = StringUtils.nullIfEmpty(servletGuestUrl);
	}

}
