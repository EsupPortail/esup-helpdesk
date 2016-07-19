/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.computerUrl;

import java.io.Serializable;
import java.util.Locale;

/**
 * the interface of computer URL builders.
 */
public interface ComputerUrlBuilder extends Serializable {
	
	/**
	 * @param locale 
	 * @return a short description of the computer url builder.
	 */
	String getDescription(Locale locale);
	
    /**
     * @return the URL for a computer.
     * @param computer
     */
    String getUrl(
    		String computer);

}
