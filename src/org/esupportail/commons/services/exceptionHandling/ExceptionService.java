/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.exceptionHandling;

import java.io.Serializable;
import java.util.Set;

import org.esupportail.commons.exceptions.ExceptionHandlingException;
import org.esupportail.commons.services.application.Version;

/** 
 * The interface of exception services, used to handle unexpected exceptions.
 */
public interface ExceptionService extends Serializable {

	/**
	 * Set the parameters (used later).
	 * @param t a throwable
	 * @throws ExceptionHandlingException 
	 */
	void setParameters(Throwable t) throws ExceptionHandlingException;
	
	/**
	 * Handle an exception. Implementing classes can do anything with the exception,
	 * such as loggin it, send a report to an email address, ...
	 * @throws ExceptionHandlingException 
	 */
	void handleException() throws ExceptionHandlingException;
	
	/**
	 * @return the view to redirect to on exceptions.
	 * @throws ExceptionHandlingException 
	 */
	String getExceptionView();
	
	/**
	 * @return the applicationName
	 */
	String getApplicationName();

	/**
	 * @return the applicationVersion
	 */
	Version getApplicationVersion();

	/**
	 * @return the client
	 */
	String getClient();

	/**
	 * @return the cookies
	 */
	Set<String> getCookies();

	/**
	 * @return the date
	 */
	Long getDate();

	/**
	 * @return the portal
	 */
	String getPortal();

	/**
	 * @return the queryString
	 */
	String getQueryString();

	/**
	 * @return the quickStart
	 */
	Boolean getQuickStart();

	/**
	 * @return the requestHeaders
	 */
	Set<String> getRequestHeaders();

	/**
	 * @return the requestParameters
	 */
	Set<String> getRequestParameters();

	/**
	 * @return the server
	 */
	String getServer();

	/**
	 * @return the requestAttributes
	 */
	Set<String> getRequestAttributes();

	/**
	 * @return the sessionAttributes
	 */
	Set<String> getSessionAttributes();

	/**
	 * @return the globalSessionAttributes
	 */
	Set<String> getGlobalSessionAttributes();

	/**
	 * @return the systemProperties
	 */
	Set<String> getSystemProperties();

	/**
	 * @return the exception
	 */
	Throwable getThrowable();

	/**
	 * @return the userAgent
	 */
	String getUserAgent();

	/**
	 * @return the userId
	 */
	String getUserId();

	/**
	 * @return the userId
	 */
	String getRecipientEmail();

}
