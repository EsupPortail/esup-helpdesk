/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.remote; 

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * An abstract web service that provides the client of the request.
 */
public class AbstractIpProtectedWebService extends AbstractClientProviderWebService implements InitializingBean {
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The names of the authorized clients.
	 */
	private List<String> authorizedClientNames;
	
	/**
	 * Bean constructor.
	 */
	public AbstractIpProtectedWebService() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		if (authorizedClientNames == null || authorizedClientNames.isEmpty()) {
			authorizedClientNames = null;
			logger.warn("property authorizedClients is not set, no access control will be done.");
		}
		if (logger.isDebugEnabled()) {
			for (String authorizedClientName : authorizedClientNames) {
				logger.debug("authorized client: " + authorizedClientName);
			}
		}
	}

	/**
	 * Check if the client is authorized.
	 * @throws ConfigException 
	 */
	protected void checkClient() throws ConfigException {
		if (authorizedClientNames == null) {
			logger.warn("no access control on web services calls, potential security hole!");
			return;
		}
		InetAddress client = getClient();
		if (client == null) {
			throw new WebServiceAuthorizationException(
					"could not resolve the client of the web service");
		}
		for (String authorizedClientName : authorizedClientNames) {
			try {
				if (client.equals(InetAddress.getByName(authorizedClientName))) {
					return;
				}
			} catch (UnknownHostException e) {
				logger.warn("could not resolve authorized client [" + authorizedClientName + "]");
			}
		}
		throw new WebServiceAuthorizationException(
				"client [" + client.getHostName() + "] is not authorized");
	}
	
	/**
	 * Set the authorized clients with a list of comma-separated host names.
	 * @param authorizedClientNames 
	 */
	public void setAuthorizedClientNames(final String authorizedClientNames) {
		if (!StringUtils.hasText(authorizedClientNames)) {
			return;
		}
		this.authorizedClientNames = Arrays.asList(authorizedClientNames.split(","));
	}

	/**
	 * @return the authorizedClients
	 */
	protected List<String> getAuthorizedClientNames() {
		return authorizedClientNames;
	}

}
