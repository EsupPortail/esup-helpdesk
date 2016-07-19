/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.ArchivedFileInfo;

/** 
 * An entry of the archived ticket files.
 */ 
public class ArchivedFileInfoEntry implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 4695770884659785779L;

	/**
	 * The ArchivedFileInfo.
	 */
	private ArchivedFileInfo archivedFileInfo;

	/**
	 * True if the file can be viewed by the user.
	 */
	private boolean canView;
	
	/**
	 * @param archivedFileInfo
	 * @param canView
	 */
	public ArchivedFileInfoEntry(
			final ArchivedFileInfo archivedFileInfo, 
			final boolean canView) {
		super();
		this.archivedFileInfo = archivedFileInfo;
		this.canView = canView;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "["
		+ "archivedFileInfo=" + archivedFileInfo
		+ ", canView=[" + canView + "]"
		+ "]";
	}

	/**
	 * @return the canView
	 */
	public boolean isCanView() {
		return canView;
	}

	/**
	 * @return the archivedFileInfo
	 */
	public ArchivedFileInfo getArchivedFileInfo() {
		return archivedFileInfo;
	}

}

