/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.exceptionHandling;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.springframework.util.StringUtils;

/**
 * A factory that returns CachingEmailExceptionServiceImpl instances.
 * 
 * See /properties/exceptionHandling/exceptionHandling-example.xml.
 */
public class CachingEmailExceptionServiceFactoryImpl extends EmailExceptionServiceFactoryImpl {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4158068768973431048L;

	/**
	 * The default name for the cache.
	 */
	private static final String DEFAULT_CACHE_NAME = CachingEmailExceptionServiceFactoryImpl.class.getName();
	
	/**
	 * the cache.
	 */
	private Cache cache;
	
	/**
	 * the name of the cache.
	 */
	private String cacheName;
	
	/**
	 * the cacheManager.
	 */
	private CacheManager cacheManager;
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * Bean constructor.
	 */
	public CachingEmailExceptionServiceFactoryImpl() {
		super();
	}
	
	/**
	 * set the default cacheName.
	 */
	protected void setDefaultCacheName() {
		this.cacheName = DEFAULT_CACHE_NAME;
	}

	/**
	 * @see org.esupportail.commons.services.exceptionHandling.EmailExceptionServiceFactoryImpl#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (!StringUtils.hasText(cacheName)) {
			setDefaultCacheName();
			logger.info(getClass() + ": no cacheName attribute set, '" 
					+ cacheName + "' will be used");
		}
		Assert.notNull(cacheManager, 
				"property cacheManager of class " + getClass().getName() 
				+ " can not be null");
		if (!cacheManager.cacheExists(cacheName)) {
			cacheManager.addCache(cacheName);
		}
		cache = cacheManager.getCache(cacheName);
	}
	
	/**
	 * @see org.esupportail.commons.services.exceptionHandling.ExceptionServiceFactory#getExceptionService()
	 */
	@Override
	public ExceptionService getExceptionService() {
		return new CachingEmailExceptionServiceImpl(
				getI18nService(), getApplicationService(), 
				getExceptionViews(), getNoEmailExceptions(), getAuthenticationService(), 
				getSmtpService(), getRecipientEmail(), isDoNotSendExceptionReportsToDevelopers(),
				getDevelEmail(), cache, getLogLevel());
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * @param cacheName the cacheName to set
	 */
	public void setCacheName(final String cacheName) {
		this.cacheName = cacheName;
	}

	/**
	 * @return the cache
	 */
	protected Cache getCache() {
		return cache;
	}

}
