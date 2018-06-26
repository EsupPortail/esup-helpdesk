/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication.info;

import java.util.List;
import java.util.Map;

/** 
 * A basic AuthInfo implementation.
 */
public class AuthInfoImpl extends AbstractAuthInfo {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3087036179837477195L;

	/**
	 * Constructor.
	 * @param id
	 * @param type 
	 */
	public AuthInfoImpl(
			final String id,
			final String type) {
		this(id, type, null);
	}

	/**
	 * Constructor.
	 * @param id
	 * @param type 
	 * @param attributes 
	 */
	public AuthInfoImpl(
			final String id,
			final String type,
			final Map<String, List<String>> attributes) {
		super(id, type, attributes);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
		getClass().getSimpleName() + "#" + hashCode() + "["
		+ "id=[" + getId() + "]"
		+ ", type=[" + getType() + "]]";
	}

}
