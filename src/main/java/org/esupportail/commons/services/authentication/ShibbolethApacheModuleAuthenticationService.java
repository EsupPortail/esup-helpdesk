/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.HttpUtils;
import org.esupportail.commons.utils.strings.StringUtils;


/** 
 * A CAS authenticator.
 */
public class ShibbolethApacheModuleAuthenticationService extends AbstractTypedAuthenticationService {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 943489018651202646L;
	
	/**
	 * The default expected end of the request URI.
	 */
	private static final String DEFAULT_ALLOWED_URI_END = "/stylesheets/shibboleth.faces";

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The header that holds the id.
	 */
	private String idHeader;

	/**
	 * The Shibboleth attribute headers.
	 */
	private List<String> attributeHeaders;
	
	/**
	 * The expected end of the request URI.
	 */
	private String allowedUriEnd = DEFAULT_ALLOWED_URI_END;

	/**
	 * Bean constructor.
	 */
	public ShibbolethApacheModuleAuthenticationService() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractTypedAuthenticationService#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.hasText(
				this.idHeader, 
				"property idHeader of class " + getClass() + " should not be null");
		Assert.notEmpty(
				this.attributeHeaders, 
				"property attributeHeaders of class " + getClass() + " should not be null");
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractTypedAuthenticationService#getAuthId()
	 */
	@Override
	protected String getAuthId() {
		Map<String, List<String>> headers = HttpUtils.getRequestHeaders();
		if (headers == null) {
			return null;
		}
		List<String> idValues = headers.get(getIdHeader());
		if (idValues == null || idValues.isEmpty()) {
			return null;
		}
		HttpServletRequest request = HttpUtils.getHttpServletRequest();
		if (request == null) {
			throw new ConfigException("Possible Shibooleth HTTP headers hacking (null request)");
		}
		String uri = request.getRequestURI();
		if (uri == null) {
			throw new ConfigException("Possible Shibooleth HTTP headers hacking (null URI)");
		}
		if (!uri.endsWith(allowedUriEnd)) {
			throw new ConfigException("Possible Shibooleth HTTP headers hacking (requestURI: " + uri + ")");
		}
		return isoToUTF8(idValues.get(0));
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractRealAuthenticationService#getAuthAttributes()
	 */
	@Override
	protected Map<String, List<String>> getAuthAttributes() {
		Map<String, List<String>> headers = HttpUtils.getRequestHeaders();
		if (headers == null) {
			return null;
		}
		Map<String, List<String>> attributes = new HashMap<String, List<String>>();
		for (String headerName : attributeHeaders) {
			List<String> values = headers.get(headerName);
			if (values != null && !values.isEmpty()) {
				for (int i = 0; i < values.size(); i++) {
					values.set(i, isoToUTF8(values.get(i)));
				}
				attributes.put(headerName, values);
			}
		}
		return attributes;
	}

	/**
	 * @see org.esupportail.commons.services.authentication.AbstractTypedAuthenticationService#getAuthType()
	 */
	@Override
	protected String getAuthType() {
		return AuthUtils.SHIBBOLETH;
	}

	/**
	 * @return the idHeader
	 */
	protected String getIdHeader() {
		return idHeader;
	}

	/**
	 * @param idHeader the idHeader to set
	 */
	public void setIdHeader(final String idHeader) {
		this.idHeader = StringUtils.nullIfEmpty(idHeader);
	}

	/**
	 * @param attributeHeaders the comma-separated attributeHeaders
	 */
	public void setAttributeHeaders(final String attributeHeaders) {
		if (attributeHeaders == null 
				|| !org.springframework.util.StringUtils.hasLength(attributeHeaders)) {
			return;
		}
		this.attributeHeaders = new ArrayList<String>();
		for (String attributeHeader : attributeHeaders.split(",")) {
			if (!this.attributeHeaders.contains(attributeHeader)) {
				this.attributeHeaders.add(attributeHeader);
			}
		}
		Collections.sort(this.attributeHeaders);
	}

	/**
	 * Recodes string from ISO-8859-1 to UTF-8.
	 * @param isoString String in ISO-8859-1
	 * @return String in UTF-8
	 */
	protected String isoToUTF8(final String isoString) {
		String utf8String = null;
		if (null != isoString) {
			try {
				utf8String = new String(isoString.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("Exception while coding HTTP headers from ISO to UTF-8", e);
				utf8String = isoString;
			}
		}
		return utf8String;
	}

	/**
	 * @return the allowedUriEnd
	 */
	protected String getAllowedUriEnd() {
		return allowedUriEnd;
	}

	/**
	 * @param allowedUriEnd the allowedUriEnd to set
	 */
	public void setAllowedUriEnd(final String allowedUriEnd) {
		this.allowedUriEnd = StringUtils.nullIfEmpty(allowedUriEnd);
	}
}
