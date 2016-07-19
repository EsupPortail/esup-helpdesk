/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;


/**
 * This class handles uploaded files.
 * @deprecated
 */
@Deprecated
public class OldFileInfo implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7039821325929971513L;

	/**
	 * The primary key.
	 */
	private long id;
	
	/**
	 * The name of the file, as given by the client when uploading.
	 */
	private String filename;
	
	/**
	 * The content of the file (used when storing to filesystem).
	 */
	private byte[] content;
	
	/** 
	 * Default constructor, needed by Hibernate. 
	 */
	public OldFileInfo() {	
		super();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof OldFileInfo)) {
			return false;
		}
		return ((OldFileInfo) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(final byte[] content) {
		this.content = content;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(final String filename) {
		this.filename = filename;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final long id) {
		this.id = id;
	}
	
}