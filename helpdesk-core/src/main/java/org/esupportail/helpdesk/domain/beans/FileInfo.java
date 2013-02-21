/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

/**
 * This class handles uploaded files.
 */
public class FileInfo extends AbstractTicketInfo {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7456265291376859558L;

	/**
	 * The name of the file, as given by the client when uploading.
	 */
	private String filename;
	
	/**
	 * The size of the file.
	 */
	private int filesize;
	
	/**
	 * The content of the file (used when storing to filesystem).
	 */
	private byte[] content;
	
	/** 
	 * Default constructor, needed by Hibernate. 
	 */
	public FileInfo() {	
		super();
	}

	/**
	 * Bean constructor.
	 * @param filename
	 * @param content
	 * @param ticket
	 * @param user
	 * @param scope
	 */
	public FileInfo(
			final String filename, 
			final byte[] content,
			final Ticket ticket, 
			final User user, 
			final String scope) {
		super(user, ticket, scope);
		this.filename = filename;
		this.content = content;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FileInfo)) {
			return false;
		}
		return ((FileInfo) obj).getId() == getId();
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
	 * @return the filesize
	 */
	public int getFilesize() {
		return filesize;
	}

	/**
	 * @param filesize the filesize to set
	 */
	public void setFilesize(final int filesize) {
		this.filesize = filesize;
	}

}