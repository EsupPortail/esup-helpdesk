/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.FileInfo;

/** 
 * An entry of the ticket files.
 */ 
public class FileInfoEntry implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7607634083455987567L;

	/**
	 * The FileInfo.
	 */
	private FileInfo fileInfo;

	/**
	 * True if the file can be viewed by the user.
	 */
	private boolean canView;
	
	/**
	 * True if the scope of the file can be changed by the user.
	 */
	private boolean canChangeScope;
	
	/**
	 * The new scope.
	 */
	private String newScope;

	/**
	 * @param fileInfo
	 * @param canView
	 * @param canChangeScope 
	 */
	public FileInfoEntry(
			final FileInfo fileInfo, 
			final boolean canView,
			final boolean canChangeScope) {
		super();
		this.fileInfo = fileInfo;
		this.canView = canView;
		this.canChangeScope = canChangeScope;
	}

	/**
	 * @return the fileInfo
	 */
	public FileInfo getFileInfo() {
		return fileInfo;
	}

	/**
	 * @return the canView
	 */
	public boolean isCanView() {
		return canView;
	}

	/**
	 * @return the canChangeScope
	 */
	public boolean isCanChangeScope() {
		return canChangeScope;
	}

	/**
	 * @return the newScope
	 */
	public String getNewScope() {
		return newScope;
	}

	/**
	 * @param newScope the newScope to set
	 */
	public void setNewScope(final String newScope) {
		this.newScope = newScope;
	}

}

