/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.exceptionHandling;

import java.io.Serializable;



/**
 * A factory for exception services.
 * 
 * See /properties/exceptionHandling/exceptionHandling-example.xml.
 */
public interface ExceptionServiceFactory extends Serializable {
	
	/**
	 * @return a new ExceptionService instance.
	 */
	ExceptionService getExceptionService();

}
