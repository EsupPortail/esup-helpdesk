/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.authentication.info;

import java.util.List;
import java.util.Map;

import org.esupportail.commons.utils.strings.StringUtils;


/** 
 * An abstract implementation of AuthInfo.
 */
@SuppressWarnings("serial")
public abstract class AbstractAuthInfo implements AuthInfo {
	
	/**
	 * The authenticated id.
	 */
	private String id;
	
	/**
	 * The authenticated type.
	 */
	private String type;

	/**
	 * The attributes.
	 */
	private Map<String, List<String>> attributes;

	/**
	 * Constructor.
	 * @param id
	 * @param type
	 * @param attributes 
	 */
	protected AbstractAuthInfo(
			final String id, 
			final String type,
			final Map<String, List<String>> attributes) {
		super();
		this.id = id;
		this.type = type;
		this.attributes = attributes;
	}
	
	/**
	 * @see org.esupportail.commons.services.authentication.info.AuthInfo#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	protected void setId(final String id) {
		this.id = StringUtils.nullIfEmpty(id);
	}

	/**
	 * @see org.esupportail.commons.services.authentication.info.AuthInfo#getType()
	 */
	@Override
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	protected void setType(final String type) {
		this.type = type;
	}
	
	/**
	 * @see org.esupportail.commons.services.authentication.info.AuthInfo#getAttributes()
	 */
	@Override
	public Map<String, List<String>> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	protected void setAttributes(final Map<String, List<String>> attributes) {
		this.attributes = attributes;
	}

}
