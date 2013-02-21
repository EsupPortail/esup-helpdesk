/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;



/**
 * This class handles archived uploaded files.
 */
public class ArchivedFileInfo extends AbstractAchivedTicketInfo {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -259382586389707824L;

	/**
	 * The id of the original FileInfo.
	 */
	private long fileInfoId;
	
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
	public ArchivedFileInfo() {	
		super();
	}

	/**
	 * Bean constructor.
	 * @param fileInfo
	 * @param archivedTicket
	 */
	public ArchivedFileInfo(
			final FileInfo fileInfo, 
			final ArchivedTicket archivedTicket) {
		super(fileInfo.getUser(), archivedTicket, fileInfo.getEffectiveScope());
		setFileInfoId(fileInfo.getId());
		setDate(fileInfo.getDate());
		setFilename(fileInfo.getFilename());
		setFilesize(fileInfo.getFilesize());
		
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "["
		+ "id=" + getId()
		+ ", archivedTicket=[" + getArchivedTicket() + "]"
		+ ", fileInfoId=[" + fileInfoId + "]"
		+ ", filename=[" + filename + "]"
		+ ", filesize=[" + filesize + "]"
		+ "]";
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ArchivedFileInfo)) {
			return false;
		}
		return ((ArchivedFileInfo) obj).getId() == getId();
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

	/**
	 * @return the fileInfoId
	 */
	public long getFileInfoId() {
		return fileInfoId;
	}

	/**
	 * @param fileInfoId the fileInfoId to set
	 */
	public void setFileInfoId(final long fileInfoId) {
		this.fileInfoId = fileInfoId;
	}

}