/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.urlGeneration; 

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.strings.StringUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * An abstract class that implements UrlGenerator.
 */
@SuppressWarnings("serial")
public abstract class AbstractCasUrlGenerator extends AbstractUrlGenerator implements InitializingBean {

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The CAS URL.
	 */
	private String casUrl;
	
	/**
	 * The CAS login URL.
	 */
	private String casLoginUrl;
	
	/**
	 * Bean constructor.
	 */
	protected AbstractCasUrlGenerator() {
		super();
	}
	
 	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		if (casLoginUrl == null) {
			if (casUrl == null) {
				logger.warn("casLoginUrl and casUrl are null, URLs via CAS will not be available");
			} else {
				casLoginUrl = casUrl + "/login?service=%s";
			}
		}
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
	 * @return the casLoginUrl
	 */
	protected String getCasLoginUrl() {
		return casLoginUrl;
	}

	/**
	 * @param casLoginUrl the casLoginUrl to set
	 */
	public void setCasLoginUrl(final String casLoginUrl) {
		this.casLoginUrl = StringUtils.nullIfEmpty(casLoginUrl);
	}

}
