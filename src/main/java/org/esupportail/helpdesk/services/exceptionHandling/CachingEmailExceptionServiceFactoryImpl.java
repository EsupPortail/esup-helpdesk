/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.exceptionHandling;

/**
 * An application-specific exception service.
 * 
 * See /properties/exceptionHandling/exceptionHandling-example.xml.
 */
public class CachingEmailExceptionServiceFactoryImpl 
extends org.esupportail.commons.services.exceptionHandling.CachingEmailExceptionServiceFactoryImpl {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7183521853906759894L;

	/**
	 * The developer's list for bugs.
	 */
	private static final String DEVEL_EMAIL = "helpdesk-bugs@esup-portail.org";

	/**
	 * Bean constructor.
	 */
	public CachingEmailExceptionServiceFactoryImpl() {
		super();
	}

	/**
	 * @return The developer's list for bugs.
	 */
	@Override
	public String getDevelEmail() {
		return DEVEL_EMAIL;
	}
	
}
