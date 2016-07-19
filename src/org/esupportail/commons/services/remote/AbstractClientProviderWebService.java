/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.remote; 

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.xfire.transport.http.XFireServletController;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

/**
 * An abstract web service that provides the client of the request.
 */
public class AbstractClientProviderWebService {
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * Bean constructor.
	 */
	public AbstractClientProviderWebService() {
		super();
	}

	/**
	 * @return the client.
	 */
	protected InetAddress getClient() {
		HttpServletRequest request = XFireServletController.getRequest();
		if (request == null) {
			logger.warn("could not get the incoming request");
			return null;
		}
		String remoteAddr = request.getRemoteAddr();
		if (remoteAddr == null) {
			logger.warn("could not get the remote address");
			return null;
		}
		try {
			return InetAddress.getByName(remoteAddr);
		} catch (UnknownHostException e) {
			return null;
		}
	}

}
