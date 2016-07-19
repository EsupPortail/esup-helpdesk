/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.application;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * A simple implementation of ApplicationService.
 * 
 * See properties/misc/application-example.xml.
 */
public class SimpleApplicationServiceImpl implements ApplicationService, InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4456027365130416127L;

	/**
	 * The default copyright.
	 */
	private static final String DEFAULT_COPYRIGHT = "Copyright (c) 2006 ESUP-Portail consortium";

	/**
	 * The default vendor.
	 */
	private static final String DEFAULT_VENDOR = "ESUP-Portail consortium";

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(SimpleApplicationServiceImpl.class);

	/**
	 * The name of the application.
	 */
	private String name;
	/**
	 * The version major number.
	 */
	private Integer versionMajorNumber;
	/**
	 * The version minor number.
	 */
	private Integer versionMinorNumber;
	/**
	 * The version update.
	 */
	private Integer versionUpdate;
	/**
	 * The copyright.
	 */
	private String copyright;
	/**
	 * The vendor.
	 */
	private String vendor;
	/**
	 * The URL where the latest version should be found.
	 */
	private String latestVersionBaseUrl;
	
	/**
	 * True for a quick-start installation.
	 */
	private Boolean quickStart;
	
	/**
	 * The deploy type.
	 */
	private String deployType;
	
	/**
	 * The database driver.
	 */
	private String databaseDriver;
	
	/**
	 * The database dialect.
	 */
	private String databaseDialect;
	
	/**
	 * True when using JNDI.
	 */
	private Boolean databaseUseJndi;
	
	/**
	 * Bean constructor.
	 */
	public SimpleApplicationServiceImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.name, "property name of class " + this.getClass().getName() 
				+ " can not be null");
		Assert.notNull(this.versionMajorNumber, "property versionMajorNumber of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.versionMinorNumber, "property versionMinorNumber of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.versionUpdate, "property versionUpdateNumber of class " 
				+ this.getClass().getName() + " can not be null");
		logger.info("starting " + getName() + " v" + getVersion() + "...");
		if (!StringUtils.hasText(copyright)) {
			copyright = DEFAULT_COPYRIGHT;
			logger.info(getClass() + ": no copyright set, using default [" + copyright + "]");
		} else {
			logger.info(copyright);
		}
		if (!StringUtils.hasText(vendor)) {
			vendor = DEFAULT_VENDOR;
			logger.info(getClass() + ": no vendor set, using default [" + vendor + "]");
		} else {
			logger.info(vendor);
		}
		if (!StringUtils.hasText(latestVersionBaseUrl)) {
			latestVersionBaseUrl = null;
			logger.warn(getClass() + ": no latestVersionBaseUrl set, " 
					+ "the latest version number will not be available");
		}
	}

	/**
	 * @see org.esupportail.commons.services.application.ApplicationService#getLatestVersion()
	 */
	@Override
	public Version getLatestVersion() {
		if (latestVersionBaseUrl == null) {
			return null;
		}
		URL url;
		try { 
			url = new URL(latestVersionBaseUrl + "/latestVersion.txt"); 
		} catch (MalformedURLException e) {
			logger.error("URL [" + latestVersionBaseUrl + "] is not correct");
			return null;
		}	
		URLConnection connection;
	     try {
			logger.info("retrieving the latest version from URL [" + url + "]...");
			connection = url.openConnection();
		} catch (IOException e) {
			logger.error("can not open URL [" + url + "]");
			return null;
		}
		BufferedReader data;
		try {
			data = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			logger.error("can not get input stream from URL [" + url + "]");
			return null;
		}
		Version version;
		try {
			version = new Version(data.readLine());
		} catch (IOException e) {
			logger.error("can not get input stream from URL [" + url + "]");
			return null;
		} catch (VersionException e) {
			logger.error(e.getMessage());
			return null;
		}
		logger.info("done.");
		return version;
	}

	/**
	 * @see org.esupportail.commons.services.application.ApplicationService#getVersion()
	 */
	@Override
	public Version getVersion() {
		return new Version(versionMajorNumber + "." + versionMinorNumber + "." + versionUpdate);
	}

	/**
	 * @param versionMajorNumber The versionMajorNumber to set.
	 */
	public void setVersionMajorNumber(final int versionMajorNumber) {
		this.versionMajorNumber = Integer.valueOf(versionMajorNumber);
	}

	/**
	 * @param versionMinorNumber The versionMinorNumber to set.
	 */
	public void setVersionMinorNumber(final int versionMinorNumber) {
		this.versionMinorNumber = Integer.valueOf(versionMinorNumber);
	}

	/**
	 * @param versionUpdate The versionUpdate to set.
	 */
	public void setVersionUpdate(final int versionUpdate) {
		this.versionUpdate = Integer.valueOf(versionUpdate);
	}

	/**
	 * @see org.esupportail.commons.services.application.ApplicationService#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @see org.esupportail.commons.services.application.ApplicationService#getCopyright()
	 */
	@Override
	public String getCopyright() {
		return copyright;
	}

	/**
	 * @param copyright the copyright to set
	 */
	public void setCopyright(final String copyright) {
		this.copyright = copyright;
	}

	/**
	 * @return the vendor
	 */
	@Override
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(final String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @param latestVersionBaseUrl the latestVersionBaseUrl to set
	 */
	public void setLatestVersionBaseUrl(final String latestVersionBaseUrl) {
		this.latestVersionBaseUrl = latestVersionBaseUrl;
	}

	/**
	 * @see org.esupportail.commons.services.application.ApplicationService#isQuickStart()
	 */
	@Override
	public boolean isQuickStart() {
		if (quickStart == null) {
			logger.error("property quickStart is not set!");
			return false;
		}
		return quickStart;
	}

	/**
	 * @param quickStart the quickStart to set
	 */
	public void setQuickStart(final boolean quickStart) {
		this.quickStart = quickStart;
	}

	/**
	 * @see org.esupportail.commons.services.application.ApplicationService#getDeployType()
	 */
	@Override
	public String getDeployType() {
		if (deployType == null) {
			logger.error("property deployType is not set!");
			return null;
		}
		return deployType;
	}

	/**
	 * @param deployType the deployType to set
	 */
	public void setDeployType(final String deployType) {
		this.deployType = deployType;
	}

	/**
	 * @see org.esupportail.commons.services.application.ApplicationService#getDatabaseDriver()
	 */
	@Override
	public String getDatabaseDriver() {
		if (databaseDriver == null) {
			logger.error("property databaseDriver is not set!");
			return null;
		}
		return databaseDriver;
	}

	/**
	 * @param databaseDriver the databaseDriver to set
	 */
	public void setDatabaseDriver(final String databaseDriver) {
		this.databaseDriver = databaseDriver;
	}

	/**
	 * @see org.esupportail.commons.services.application.ApplicationService#getDatabaseDialect()
	 */
	@Override
	public String getDatabaseDialect() {
		if (databaseDialect == null) {
			logger.error("property databaseDialect is not set!");
			return null;
		}
		return databaseDialect;
	}

	/**
	 * @param databaseDialect the databaseDialect to set
	 */
	public void setDatabaseDialect(final String databaseDialect) {
		this.databaseDialect = databaseDialect;
	}

	/**
	 * @see org.esupportail.commons.services.application.ApplicationService#isDatabaseUseJndi()
	 */
	@Override
	public boolean isDatabaseUseJndi() {
		if (databaseUseJndi == null) {
			logger.error("property databaseUseJndi is not set!");
			return false;
		}
		return databaseUseJndi;
	}

	/**
	 * @param databaseUseJndi the databaseUseJndi to set
	 */
	public void setDatabaseUseJndi(final boolean databaseUseJndi) {
		this.databaseUseJndi = databaseUseJndi;
	}

}
