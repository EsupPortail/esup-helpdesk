/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.smtp;

import java.io.Serializable;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;


/**
 * A class that represents SMTP servers.
 */
public class SmtpServer implements InitializingBean, Serializable {
	
	/**
	 * The default host.
	 */
	public static final String DEFAULT_HOST = "localhost";

	/**
	 * The default port.
	 */
	public static final int DEFAULT_PORT = 25;

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3312752352514102329L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(SmtpServer.class);
	
	/**
	 * The host name or IP number of the server.
	 */
	private String host;
	
	/**
	 * The port number the SMTP server is running on.
	 */
	private int port;
	
	/**
	 * The name of the user used to connect to the SMTP server.
	 */
	private String user;

	/**
	 * The password of the user used to connect to the SMTP server.
	 */
	private String password;

	/**
	 * Constructor.
	 */
	public SmtpServer() {
		this.host = null;
		this.port = -1;
	}
	
	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		if (this.host == null) {
			logger.info(getClass() + ": host not set, " + DEFAULT_HOST + " will be used.");
			this.host = "localhost";
		}
		if (this.port == -1) {
			logger.info(getClass() + ": port not set, " + DEFAULT_PORT + " will be used for host '" 
					+ this.host + "'.");
			setDefaultPort();
		}
		if (!StringUtils.hasText(this.user)) {
			this.user = null;
			logger.info(getClass() + ": an anonymous connection will be used for SMTP server '" 
					+ this.host + ":" + this.port + "'.");
			setDefaultPort();
			if (StringUtils.hasText(this.password)) {
				logger.warn(getClass() + ": an anonymous connection will be used for SMTP server '" 
						+ this.host + ":" + this.port + "', password ignored.");
			}
		} else if (this.password == null) {
			this.password = "";		
		}
	}

	/**
	 * @return The host.
	 */
	public String getHost() {
		return this.host;
	}
	/**
	 * @param host The host to set.
	 */
	public void setHost(final String host) {
		this.host = host;
	}
	/**
	 * Set the default host.
	 */
	public void setDefaultHost() {
		setHost(DEFAULT_HOST);
	}

	/**
	 * @return The port.
	 */
	public int getPort() {
		return this.port;
	}
	/**
	 * @param port The port to set.
	 */
	public void setPort(final int port) {
		this.port = port;
	}
	/**
	 * Set the default port.
	 */
	public void setDefaultPort() {
		setPort(DEFAULT_PORT);
	}

	/**
	 * @return The password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * @return The user.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(final String user) {
		this.user = user;
	}

}

