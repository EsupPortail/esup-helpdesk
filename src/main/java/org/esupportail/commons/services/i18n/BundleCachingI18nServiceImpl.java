/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.i18n;

import java.util.Locale;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.springframework.util.StringUtils;

/**
 * This class should be preferred to BundleI18nServiceImpl since it caches 
 * resource bundles.
 * 
 * See /properties/i18n/i18n-example.xml.
 */
public class BundleCachingI18nServiceImpl extends BundleI18nServiceImpl {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3892992778809096939L;

	/**
	 * The default name for the cache.
	 */
	private static final String DEFAULT_CACHE_NAME = BundleCachingI18nServiceImpl.class.getName();
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(BundleCachingI18nServiceImpl.class);
	
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
	 * Bean constructor.
	 */
	public BundleCachingI18nServiceImpl() {
		super();
	}

	/**
	 * @see org.esupportail.commons.services.i18n.BundleI18nServiceImpl#getStrings(java.util.Locale)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getStrings(
			final Locale locale) {
		Element element = cache.get(locale);
		if (element != null) {
			return (Map<String, String>) element.getObjectValue();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("no map cached for locale '" + locale + "'");
		}
		Map<String, String> map = super.getStrings(locale);
		cache.put(new Element(locale, map));
		return map;
	}

	/**
	 * set the default cacheName.
	 */
	protected void setDefaultCacheName() {
		this.cacheName = DEFAULT_CACHE_NAME;
	}

	/**
	 * @see org.esupportail.commons.services.i18n.BundlesI18nServiceImpl#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
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
	
}

