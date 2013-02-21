/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.io.Serializable;

/** 
 * A class to temporarily store uploaded files.
 */ 
public class TempUploadedFile implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7642516098879073911L;

	/**
	 * The name.
	 */
	private String name;

	/**
	 * The content.
	 */
	private byte[] content;

	/**
	 * Bean constructor.
	 * @param name 
	 * @param content 
	 */
	public TempUploadedFile(
			final String name,
			final byte[] content) {
		super();
		this.name = name;
		this.content = content;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}
	
}

