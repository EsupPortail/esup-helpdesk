/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.assignment;

import java.io.Serializable;
import java.util.List;


/**
 * The interface of assignment algorithm stores.
 */
public interface AssignmentAlgorithmStore extends Serializable {
	
	/**
	 * @return the names of the algorithms stored.
	 */
	List<String> getAlgorithmNames();
	
	/**
	 * @return the algorithm that corresponds to a name.
	 * @param name
	 */
	AssignmentAlgorithm getAlgorithm(String name);

}
