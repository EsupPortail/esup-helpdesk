/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.controllers;

/**
 * This interface should be implemented by beans for them to be reset, for instance
 * after an exception has been thrown. 
 */
public interface Resettable {
	/**
	 * Reset the bean.
	 */
	void reset();
}
