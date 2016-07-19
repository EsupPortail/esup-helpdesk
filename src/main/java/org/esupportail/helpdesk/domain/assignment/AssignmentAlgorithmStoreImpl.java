/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A basic implementation of AssignmentAlgorithmStore.
 */
public class AssignmentAlgorithmStoreImpl 
implements AssignmentAlgorithmStore {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 968717490119098192L;

	/**
	 * The internal map.
	 */
	private Map<String, AssignmentAlgorithm> map = new HashMap<String, AssignmentAlgorithm>();

	/**
	 * Constructor.
	 */
	public AssignmentAlgorithmStoreImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.assignment.AssignmentAlgorithmStore#getAlgorithm(java.lang.String)
	 */
	@Override
	public AssignmentAlgorithm getAlgorithm(final String name) {
		return map.get(name);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.assignment.AssignmentAlgorithmStore#getAlgorithmNames()
	 */
	@Override
	public List<String> getAlgorithmNames() {
		return new ArrayList<String>(map.keySet());
	}

	/**
	 * @return the map
	 */
	protected Map<String, AssignmentAlgorithm> getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(final Map<String, AssignmentAlgorithm> map) {
		this.map = map;
	}

}
