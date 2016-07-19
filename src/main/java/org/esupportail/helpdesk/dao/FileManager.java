/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.dao;

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.ArchivedFileInfo;
import org.esupportail.helpdesk.domain.beans.FileInfo;
import org.esupportail.helpdesk.domain.beans.OldFileInfo;
import org.esupportail.helpdesk.exceptions.FileException;

/**
 * The interface of file managers (to deal with uploaded files).
 */
public interface FileManager extends Serializable {

	/**
	 * Store the data of a FileInfo.
	 * @param fileInfo
	 * @throws FileException
	 */
	void writeFileInfoContent(FileInfo fileInfo) throws FileException;

	/**
	 * Delete the data of a FileInfo.
	 * @param fileInfo
	 * @throws FileException
	 */
	void deleteFileInfoContent(FileInfo fileInfo) throws FileException;

	/**
	 * @return the content that corresponds to a FileInfo.
	 * @param fileInfo
	 * @throws FileException
	 */
	byte[] readFileInfoContent(FileInfo fileInfo) throws FileException;

	/**
	 * @param archivedFileInfo
	 * @return the content of an archived file.
	 */
	byte[] readArchivedFileInfoContent(ArchivedFileInfo archivedFileInfo);

	/**
	 * Delete all the data.
	 * @throws FileException
	 */
	void deleteAllContents() throws FileException;

	/**
	 * @return the content that corresponds to a OldFileInfo.
	 * @param oldFileInfo
	 * @throws FileException
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	byte[] readOldFileInfoContent(OldFileInfo oldFileInfo) throws FileException;

	/**
	 * Delete the data of a OldFileInfo.
	 * @param oldFileInfo
	 * @throws FileException
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void deleteOldFileInfoContent(OldFileInfo oldFileInfo) throws FileException;

}