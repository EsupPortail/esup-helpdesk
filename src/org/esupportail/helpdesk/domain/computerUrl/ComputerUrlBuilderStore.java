/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.computerUrl;

import java.io.Serializable;
import java.util.List;


/**
 * The interface of computer url builders stores.
 */
public interface ComputerUrlBuilderStore extends Serializable {
	
	/**
	 * @return the names of the computer url builders stored.
	 */
	List<String> getComputerUrlBuilderNames();
	
	/**
	 * @return the computer url builder that corresponds to a name.
	 * @param name
	 */
	ComputerUrlBuilder getComputerUrlBuilder(String name);

}
