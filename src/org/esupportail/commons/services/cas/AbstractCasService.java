/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.cas;

import edu.yale.its.tp.cas.client.ProxyTicketValidator;
import edu.yale.its.tp.cas.proxy.ProxyTicketReceptor;

import java.io.IOException;
import java.net.URLEncoder;

import javax.xml.parsers.ParserConfigurationException;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.xml.sax.SAXException;

/** 
 * An abstract implementation of CasService; inheriting classes 
 * should simply implement getProxyGrantingTicket() (each implementation
 * has its own way to know what the PGT is).
 */
@SuppressWarnings("serial")
public abstract class AbstractCasService implements CasService {

	/**
	 * Magic number.
	 */
	private static final int THOUSAND = 1000;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The proxy ticket validator.
	 */
	private ProxyTicketValidator validator;
	
	/**
	 * The CAS validate URL.
	 */
	private String casValidateUrl;

	/**
	 * The URL of the service itself.
	 */
	private String service;

	/**
	 * The proxy callback URL.
	 */
	private String proxyCallbackUrl;
	
	/**
	 * The number of retries.
	 */
	private Integer retries;
	
	/**
	 * The number of milliseconds to sleep between two tries.
	 */
	private Integer sleep;

	/**
	 * Bean constructor.
	 */
	public AbstractCasService() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		Assert.hasText(casValidateUrl, "property [casValidateUrl] of class [" 
				+ getClass() + "] can not be null");
		Assert.hasText(service, "property [service] of class [" 
				+ getClass() + "] can not be null");
		Assert.hasText(proxyCallbackUrl, "property [proxyCallbackUrl] of class [" 
				+ getClass() + "] can not be null");
		if (retries == null || retries < 0) {
			retries = 0;
		}
		if (sleep == null || sleep < 0) {
			sleep = 1;
		}
	}
	
	/**
	 * @return the service ticket that was passed to the application.
	 */
	protected abstract String getServiceTicket();

	/**
	 * @see org.esupportail.commons.services.cas.CasService#validate()
	 */
	@Override
	public void validate() throws CasException {
		String serviceTicket = getServiceTicket();
		validator = new ProxyTicketValidator();
		validator.setCasValidateUrl(casValidateUrl);
		validator.setService(service);
		validator.setServiceTicket(serviceTicket);
		validator.setProxyCallbackUrl(proxyCallbackUrl); 
		if (logger.isDebugEnabled()) {
			logger.debug("validating ticket [" + serviceTicket + "]...");
			logger.debug("casValidateUrl=[" + validator.getCasValidateUrl() + "]");
			logger.debug("service=[" + service + "]");
			logger.debug("proxyCallbackUrl=[" + validator.getProxyCallbackUrl() + "]");
		}
		int tryNumber = 0;
		Exception validateException = null;
		while (tryNumber < (1 + retries)) {
			tryNumber++;
			if (logger.isDebugEnabled()) {
				logger.debug("try #" + tryNumber);
			}
			try {
				validator.validate();
				if (logger.isDebugEnabled()) {
					logger.debug("response = [" + validator.getResponse() + "]");
					logger.debug("errorCode = [" + validator.getErrorCode() + "]");
					logger.debug("errorMessage = [" + validator.getErrorMessage() + "]");
				}
				if (validator.isAuthenticationSuccesful()) {
					return;
				}
				// no error, but authentication failed
				throw new CasException("authentication failed for ticket [" 
						+ serviceTicket + "] (" + validator.getErrorCode() + ": " 
						+ validator.getErrorMessage() + ")");
			} catch (IOException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("could not validate ticket [" 
							+ serviceTicket + "] (try #" + tryNumber + ")", 
							e);
				}
				if (tryNumber == retries) {
				      try {
				          Thread.sleep(sleep * THOUSAND);
				        } catch (InterruptedException ie) {
				        	validateException = ie;
				        }
				} else {
					validateException = e;
				}
			} catch (SAXException e) {
				validateException = e;
			} catch (ParserConfigurationException e) {
				validateException = e;
			}
		}
		CasException ce = new CasException(
				"could not validate ticket [" 
				+ serviceTicket + "]", validateException); 
		if (logger.isDebugEnabled()) {
			logger.debug(ce);
		}
		throw ce;
	}

	/**
	 * @param targetService 
	 * @return a proxy ticket.
	 * @throws CasException 
	 */
	private String retrieveProxyTicket(final String targetService) throws CasException {
		if (logger.isDebugEnabled()) {
			logger.debug("retrieving a proxy ticket for service [" + targetService + "]...");
			logger.debug("pgtIou=[" + validator.getPgtIou() + "]");
		}
		int tryNumber = 0;
		Exception exception = null;
		while (tryNumber < (1 + retries)) {
			tryNumber++;
			if (logger.isDebugEnabled()) {
				logger.debug("try #" + tryNumber);
			}
			try {
				String url = URLEncoder.encode(targetService, "UTF-8"); 
				return ProxyTicketReceptor.getProxyTicket(validator.getPgtIou(), url);
			} catch (IOException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("could not retrieve a proxy ticket for service [" 
							+ targetService + "] (try #" + tryNumber + ")", 
							e);
				}
				if (tryNumber == retries) {
				      try {
				          Thread.sleep(sleep * THOUSAND);
				        } catch (InterruptedException ie) {
				        	exception = ie;
				        }
				} else {
					exception = e;
				}
			}
		}
		CasException ce = new CasException(
				"could not get a proxy ticket for service [" 
				+ targetService + "]", exception); 
		if (logger.isDebugEnabled()) {
			logger.debug(ce);
		}
		throw ce;
	}

	/**
	 * @see org.esupportail.commons.services.cas.CasService#getProxyTicket(java.lang.String)
	 */
	@Override
	public String getProxyTicket(final String targetService) throws CasException {
		if (validator == null) {
			validate();
		}
		return retrieveProxyTicket(targetService);
	}

	/**
	 * @param casValidateUrl the casValidateUrl to set
	 */
	public void setCasValidateUrl(final String casValidateUrl) {
		this.casValidateUrl = casValidateUrl;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(final String service) {
		this.service = service;
	}

	/**
	 * @param proxyCallbackUrl the proxyCallbackUrl to set
	 */
	public void setProxyCallbackUrl(final String proxyCallbackUrl) {
		this.proxyCallbackUrl = proxyCallbackUrl;
	}

	/**
	 * @param retries the retries to set
	 */
	public void setRetries(final Integer retries) {
		this.retries = retries;
	}

	/**
	 * @param sleep the sleep to set
	 */
	public void setSleep(final Integer sleep) {
		this.sleep = sleep;
	}
	
}
