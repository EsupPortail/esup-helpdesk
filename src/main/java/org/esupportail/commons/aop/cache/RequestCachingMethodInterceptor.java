/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.aop.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.esupportail.commons.utils.ContextUtils;

/**
 * A caching interceptor that will check for results in request cache before calling methods.
 */
@Aspect
public class RequestCachingMethodInterceptor extends AbstractCachingMethodInterceptor {

	/**
	 * Constructor.
	 */
	public RequestCachingMethodInterceptor() {
		super();
	}

	/**
	 * @see org.esupportail.commons.aop.cache.AbstractCachingMethodInterceptor#getContextAttribute(java.lang.String)
	 */
	@Override
	protected Object getContextAttribute(final String name) {
		return ContextUtils.getRequestAttribute(name);
	}

	/**
	 * @see org.esupportail.commons.aop.cache.AbstractCachingMethodInterceptor#setContextAttribute(
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	protected void setContextAttribute(
			final String name, 
			final Object value) {
		ContextUtils.setRequestAttribute(name, value);
	}

	/**
	 * @see org.esupportail.commons.aop.cache.AbstractCachingMethodInterceptor#around(
	 * org.aspectj.lang.ProceedingJoinPoint)
	 */
	@Override
	@Around("@annotation(org.esupportail.commons.aop.cache.RequestCache)")
	public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
		return super.around(joinPoint);
	}
	
}