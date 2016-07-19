/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.aop.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.esupportail.commons.aop.AopUtils;
import org.esupportail.commons.utils.ContextUtils;

/**
 * An abstract caching interceptor that will check for results in cache before calling methods.
 */
public abstract class AbstractCachingMethodInterceptor {

	/**
	 * The request attribute to store the cache map.
	 */
	private static final String ATTRIBUTE = AbstractCachingMethodInterceptor.class.getName() + ".map";

//	/**
//	 * A logger.
//	 */
//	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * True if a web request (false if batch command).
	 */
	private Boolean web;

	/**
	 * Constructor.
	 */
	public AbstractCachingMethodInterceptor() {
		super();
	}

	/**
	 * @param name 
	 * @return a context attribute
	 */
	protected abstract Object getContextAttribute(String name);

	/**
	 * Set a context attribute.
	 * @param name 
	 * @param value 
	 */
	protected abstract void setContextAttribute(String name, Object value);

	/**
	 * @param create 
	 * @return the map used to store the call results.
	 */
	public CallResultMap getMap(final boolean create) {
		CallResultMap map = (CallResultMap) getContextAttribute(ATTRIBUTE);
		if (map == null && create) {
			map = new CallResultMap();
			setContextAttribute(ATTRIBUTE, map);
		}
		return map;
	}

	/**
	 * The method of the interceptor will be called instead of the original method.
	 * @param joinPoint
	 * @return a cached value or the original result.
	 * @throws Throwable
	 */
	protected Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
		if (!isWeb()) {
			return joinPoint.proceed();
		}
		String key = AopUtils.getCacheKey(joinPoint);
		CallResult callResult = null;
		CallResultMap map = getMap(true);
		if (map.containsKey(key)) {
			callResult = map.get(key);
		} else {
			long startTime = System.currentTimeMillis();
			try {
				Object result = joinPoint.proceed();
				callResult = new CallResult(result, null, System.currentTimeMillis() - startTime);
			} catch (Throwable t) {
				callResult = new CallResult(null, t, System.currentTimeMillis() - startTime);
			}
			map.put(key, callResult);
		}
		if (callResult.getThrowable() != null) {
			throw callResult.getThrowable();
		}
		return callResult.getResult();
	}

	/**
	 * @return true for web contexts.
	 */
	public boolean isWeb() {
		if (web == null) {
			web = ContextUtils.isWeb();
		}
		return web;
	}
	
	/**
	 * Clear all call results.
	 */
	public void clear() {
		CallResultMap map = getMap(false);
		if (map != null) {
			map.clear();
		}
	}

}