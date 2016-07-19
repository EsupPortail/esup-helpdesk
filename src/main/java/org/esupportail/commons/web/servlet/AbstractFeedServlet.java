/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.servlet;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.esupportail.commons.services.application.VersionningUtils;
import org.esupportail.commons.services.database.DatabaseUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.utils.BeanUtils;

/**
 * This servlet serves RSS.
 */
@SuppressWarnings("serial")
public abstract class AbstractFeedServlet extends HttpServlet {
	
	/**
	 * The parameter for the feed type.
	 */
	public static final String FEED_TYPE_PARAM = "feedType";
	
	/**
	 * The mime type.
	 */
	public static final String MIME_TYPE = "application/xml; charset=UTF-8";
	
	/**
	 * The name of the cache manager bean.
	 */
	private static final String CACHE_MANAGER_BEAN = "cacheManager";
	
	/**
	 * the cacheManager.
	 */
	private CacheManager cacheManager;
	
	/**
	 * the cache.
	 */
	private Cache cache;
	
    /**
     * Constructor.
     */
    public AbstractFeedServlet() {
        super();
    }

	/**
	 * @return the cache name.
	 */
	protected String getCacheName() {
		return getClass().getName();
	}
	
	/**
	 * @return the cache manager.
	 */
	protected CacheManager getCacheManager() {
		if (cacheManager == null) {
			cacheManager = (CacheManager) BeanUtils.getBean(CACHE_MANAGER_BEAN);
		}
		return cacheManager;
	}

	/**
	 * @return the cache.
	 */
	protected Cache getCache() {
		if (cache == null) {
			String cacheName = getCacheName();
			if (!getCacheManager().cacheExists(cacheName)) {
				getCacheManager().addCache(cacheName);
			}
			cache = getCacheManager().getCache(cacheName);
		}
		return cache;
	}

    /**
     * @param req
     * @return the feed.
     * @throws FeedException
     */
    protected abstract SyndFeed getFeed(
    		final HttpServletRequest req) 
    throws FeedException;
    
    /**
     * Override this method to implement cache.
     * @param req
     * @return the cache key of a request.
     */
    protected String getCacheKey(@SuppressWarnings("unused")
	final HttpServletRequest req) {
    	return null;
    }

    /**
     * @param req
     * @return the feed with the cache.
     * @throws FeedException
     */
    protected SyndFeed getFeedWithCache(
    		final HttpServletRequest req) 
    throws FeedException {
    	String cacheKey = getCacheKey(req);
    	if (cacheKey != null) {
	    	Element element = getCache().get(cacheKey);
	    	if (element != null) {
	        	return (SyndFeed) element.getValue();
	    	}
    	}
		SyndFeed feed = getFeed(req);
    	if (cacheKey != null) {
    		getCache().put(new Element(cacheKey, feed));
    	}
		return feed;
    }
    
    /**
     * @return the default feed type.
     */
    protected String getDefaultFeedType() {
    	return "rss_2.0";
    }
    
	/**
	 * @param request 
	 * @return the feed type.
	 */
	protected String getFeedType(
		final HttpServletRequest request) {
        String feedType = request.getParameter(FEED_TYPE_PARAM);
        if (feedType == null) {
        	feedType = getDefaultFeedType();
        }
        return feedType;
	}

	/**
	 * @see javax.servlet.http.HttpServlet#service(
	 * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(
			final HttpServletRequest request, 
			final HttpServletResponse response) 
	throws ServletException, IOException {
		try {
			BeanUtils.initBeanFactory(getServletContext());
			DatabaseUtils.open();
			VersionningUtils.checkVersion(true, false);
			SyndFeed feed = getFeedWithCache(request);
            feed.setFeedType(getFeedType(request));
            response.setContentType(MIME_TYPE);
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, response.getWriter());
			DatabaseUtils.close();
		} catch (Throwable t) {
			ExceptionUtils.catchException(t);
			DatabaseUtils.close();
			if (t instanceof ServletException) {
				throw (ServletException) t;
			}
			if (t instanceof IOException) {
				throw (IOException) t;
			}
			throw new ServletException(ExceptionUtils.getRealCause(t).getMessage(), t);
		}
	}

}
