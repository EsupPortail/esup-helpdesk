/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.computerUrl;

import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;

/**
 * A CAS-protected pattern-based implementation of ComputerUrlBuilder.
 */
public class CasPatternBasedComputerUrlBuilderImpl extends PatternBasedComputerUrlBuilderImpl {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -163942319149828159L;

	/**
	 * The CAS URL.
	 */
	private String casUrl;
	
	/**
	 * The CAS service URL.
	 */
	private String casServiceUrl;
	
	/**
	 * Constructor.
	 */
	public CasPatternBasedComputerUrlBuilderImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (casServiceUrl == null) {
			Assert.notNull(
					casUrl, 
					"property casServiceUrl and casUrl of class " 
					+ getClass().getSimpleName() + " should not be both null");
			casServiceUrl = casUrl + "/login?service=%s";
		}
	}
	
	/**
	 * @see org.esupportail.helpdesk.domain.computerUrl.ComputerUrlBuilder#getUrl(java.lang.String)
	 */
	@Override
	public String getUrl(
			final String computer) {
		String url = super.getUrl(computer);
		if (url == null) {
			return null;
		}
		return String.format(casServiceUrl, StringUtils.utf8UrlEncode(url));
	}

	/**
	 * @return the casUrl
	 */
	protected String getCasUrl() {
		return casUrl;
	}

	/**
	 * @param casUrl the casUrl to set
	 */
	public void setCasUrl(final String casUrl) {
		this.casUrl = StringUtils.nullIfEmpty(casUrl);
		if (this.casUrl != null) {
			while (casUrl.endsWith("/")) {
				this.casUrl = this.casUrl.substring(0, this.casUrl.length() - 1);
			}
		}
	}

	/**
	 * @return the casServiceUrl
	 */
	protected String getCasServiceUrl() {
		return casServiceUrl;
	}

	/**
	 * @param casServiceUrl the casServiceUrl to set
	 */
	public void setCasServiceUrl(final String casServiceUrl) {
		this.casServiceUrl = StringUtils.nullIfEmpty(casServiceUrl);
	}

}
