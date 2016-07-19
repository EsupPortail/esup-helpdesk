/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;

/**
 * The class for icons.
 */
public class Icon implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2059584744402550118L;

	/**
	 * Primary key.
	 */
	private Long id;

	/**
	 * The name.
	 */
	private String name;
	
	/**
	 * The content type.
	 */
	private String contentType;
	
	/**
	 * The data.
	 */
	private byte[] data;

	/**
	 * Bean constructor.
	 */
	public Icon() {
		super();
	}

	/**
	 * Bean constructor.
	 * @param name 
	 * @param contentType 
	 * @param data 
	 */
	public Icon(
			final String name,
			final String contentType,
			final byte[] data) {
		this();
		setName(name);
		setContentType(contentType);
		setData(data);
	}

	/**
	 * Bean constructor (copy).
	 * @param c 
	 */
	public Icon(final Icon c) {
		this(c.getName(), c.getContentType(), c.getData());
		setId(c.getId());
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Icon)) {
			return false;
		}
		return ((Icon) obj).getId().equals(getId());
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		if (getId() == null) {
			return 0;
		}
		return getId().intValue();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = getClass().getSimpleName() + "#" + hashCode() + "[" 
		+ "id=[" + id + "]"
		+ ", name=[" + name + "]"
		+ ", data=";
		if (data == null) {
			result += data;
		} else {
			result += "[" + data.length + "b]";
		}
		result += "]";
		return result;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(final byte[] data) {
		this.data = data;
	}

}