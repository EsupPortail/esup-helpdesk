/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.computerUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A basic implementation of ComputerUrlBuilderStore.
 */
public class ComputerUrlBuilderStoreImpl 
implements ComputerUrlBuilderStore {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5093319923619235250L;

	/**
	 * The internal map.
	 */
	private Map<String, ComputerUrlBuilder> map = new HashMap<String, ComputerUrlBuilder>();

	/**
	 * Constructor.
	 */
	public ComputerUrlBuilderStoreImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.computerUrl.ComputerUrlBuilderStore#getComputerUrlBuilder(
	 * java.lang.String)
	 */
	@Override
	public ComputerUrlBuilder getComputerUrlBuilder(final String name) {
		return map.get(name);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.computerUrl.ComputerUrlBuilderStore#getComputerUrlBuilderNames()
	 */
	@Override
	public List<String> getComputerUrlBuilderNames() {
		return new ArrayList<String>(map.keySet());
	}

	/**
	 * @return the map
	 */
	protected Map<String, ComputerUrlBuilder> getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(final Map<String, ComputerUrlBuilder> map) {
		this.map = map;
	}

}
