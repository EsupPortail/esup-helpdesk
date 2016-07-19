/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.ArchivedFileInfo;
import org.esupportail.helpdesk.domain.beans.FileInfo;
import org.esupportail.helpdesk.domain.beans.OldFileInfo;
import org.esupportail.helpdesk.exceptions.FileException;
import org.springframework.beans.factory.InitializingBean;

/**
 * A basic implementation of FileManager that stores the content of the files on the filesystem.
 */
public class FileManagerImpl implements FileManager, InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1399412434380258437L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The path where to store the content of the files.
	 */
	private String path;

	/**
	 * The path where the content of the old v2 files are stored.
	 */
	private String v2Path;

	/**
	 * Bean constructor.
	 */
	public FileManagerImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasLength(this.path,
				"property path of class " + this.getClass().getName() + " can not be null");
		while (path.endsWith("/")) {
			path = path.substring(0, path.length() - 2);
		}
		checkPath(false);
		if (org.springframework.util.StringUtils.hasLength(v2Path)) {
			while (v2Path.endsWith("/")) {
				v2Path = v2Path.substring(0, v2Path.length() - 2);
			}
			if (v2Path.equals(path)) {
				throw new ConfigException("properties path and v2Path must differ");
			}
		} else {
			v2Path = null;
			logger.info("property v2Path is not set, migration process will fail");
		}
	}

	/**
	 * Check a path.
	 * @param thePath
	 * @param create true to create the folder if it does not exist.
	 * @throws FileException
	 */
	protected void checkPath(final String thePath, final boolean create) throws FileException {
		File dir = new File(thePath);
		if (!dir.exists()) {
			if (!create) {
				throw new FileException("directory [" + thePath + "] does not exist");
			}
			if (!dir.mkdirs()) {
				throw new FileException("could not create directory [" + thePath + "]");
			}
			logger.info("directory [" + thePath + "] was successfully created.");
		}
	}

	/**
	 * Check the path.
	 * @param create true to create the folder if it does not exist.
	 * @throws FileException
	 */
	protected void checkPath(final boolean create) throws FileException {
		checkPath(path, create);
	}

	/**
	 * Check the v2 path.
	 * @param create true to create the folder if it does not exist.
	 * @throws FileException
	 */
	protected void checkV2Path(final boolean create) throws FileException {
		Assert.hasLength(v2Path,
				"property v2Path of class " + this.getClass().getName() + " can not be null");
		checkPath(v2Path, create);
	}

	/**
	 * @param fileInfo
	 * @return the name of the filesystem entry for a FileInfo.
	 */
	protected String getFilename(final FileInfo fileInfo) {
		return path + "/" + fileInfo.getId();
	}

	/**
	 * @param oldFileInfo
	 * @return the name of the filesystem entry for a OldFileInfo.
	 */
	protected String getFilename(final OldFileInfo oldFileInfo) {
		return v2Path + "/" + oldFileInfo.getId();
	}

	/**
	 * Write a content to the filesystem.
	 * @param filename
	 * @param content
	 */
	protected void writeContent(final String filename, final byte[] content) {
		File file = new File(filename);
		if (file.exists()) {
			throw new FileException("file [" + file.getAbsolutePath() + "] already exists");
		}
		try {
			FileOutputStream os = new FileOutputStream(file);
			os.write(content);
			os.close();
		} catch (IOException e) {
			throw new FileException("could not write file [" + file.getAbsolutePath() + "]", e);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.dao.FileManager#writeFileInfoContent(
	 * org.esupportail.helpdesk.domain.beans.FileInfo)
	 */
	@Override
	public void writeFileInfoContent(final FileInfo fileInfo) {
		checkPath(true);
		writeContent(getFilename(fileInfo), fileInfo.getContent());
	}

	/**
	 * @param filename
	 * @return the content read from the filesystem.
	 */
	protected byte[] readContent(final String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			throw new FileException("file [" + file.getAbsolutePath() + "] does not exist");
		}
		if (!file.isFile()) {
			throw new FileException("[" + file.getAbsolutePath() + "] is not a file");
		}
		byte[] content;
		try {
			InputStream is = new FileInputStream(file);
			long length = file.length();
			if (length > Integer.MAX_VALUE) {
				throw new FileException("file [" + file.getAbsolutePath() + "] is too large");
			}
			content = new byte[(int) length];
			int offset = 0;
			int numRead = is.read(content, 0, content.length);
			while (offset < content.length && numRead >= 0) {
				offset += numRead;
				numRead = is.read(content, offset, content.length - offset);
			}
			if (offset < content.length) {
				throw new FileException(
						"could not completly read file [" + file.getAbsolutePath() + "]");
			}
			is.close();
		} catch (IOException e) {
			throw new FileException("could not read file [" + file.getAbsolutePath() + "]");
		}
		return content;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.FileManager#readFileInfoContent(
	 * org.esupportail.helpdesk.domain.beans.FileInfo)
	 */
	@Override
	@RequestCache
	public byte[] readFileInfoContent(final FileInfo fileInfo) {
		if (fileInfo.getContent() == null) {
			checkPath(false);
			byte[] content = readContent(getFilename(fileInfo));
			fileInfo.setContent(content);
		}
		return fileInfo.getContent();
	}

	/**
	 * @return the path
	 */
	protected String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/**
	 * @param v2Path the v2Path to set
	 */
	public void setV2Path(final String v2Path) {
		this.v2Path = v2Path;
	}

	/**
	 * Delete a content.
	 * @param filename
	 */
	protected void deleteContent(final String filename) {
		File file = new File(filename);
		if (file.exists()) {
			if (!file.delete()) {
				logger.error("could not delete file [" + filename + "]");
			}
		} else {
			logger.error("file [" + filename + "] not found");
		}
	}

	/**
	 * @see org.esupportail.helpdesk.dao.FileManager#deleteFileInfoContent(
	 * org.esupportail.helpdesk.domain.beans.FileInfo)
	 */
	@Override
	public void deleteFileInfoContent(final FileInfo fileInfo) {
		checkPath(false);
		deleteContent(getFilename(fileInfo));
	}

	/**
	 * @param archivedFileInfo
	 * @return the name of the filesystem entry for a ArchivedFileInfo.
	 */
	protected String getFilename(final ArchivedFileInfo archivedFileInfo) {
		return path + "/" + archivedFileInfo.getFileInfoId();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.FileManager#readArchivedFileInfoContent(
	 * org.esupportail.helpdesk.domain.beans.ArchivedFileInfo)
	 */
	@Override
	public byte[] readArchivedFileInfoContent(final ArchivedFileInfo archivedFileInfo) {
		if (archivedFileInfo.getContent() == null) {
			checkPath(false);
			byte[] content = readContent(getFilename(archivedFileInfo));
			archivedFileInfo.setContent(content);
		}
		return archivedFileInfo.getContent();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.FileManager#deleteAllContents()
	 */
	@Override
	public void deleteAllContents() throws FileException {
		File dir = new File(path);
		for (File file : dir.listFiles()) {
			file.delete();
		}
	}

	/**
	 * @see org.esupportail.helpdesk.dao.FileManager#readOldFileInfoContent(
	 * org.esupportail.helpdesk.domain.beans.OldFileInfo)
	 */
	@Override
	public byte[] readOldFileInfoContent(final OldFileInfo oldFileInfo) {
		if (oldFileInfo.getContent() == null) {
			checkV2Path(false);
			byte[] content = readContent(getFilename(oldFileInfo));
			oldFileInfo.setContent(content);
		}
		return oldFileInfo.getContent();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.FileManager#deleteOldFileInfoContent(
	 * org.esupportail.helpdesk.domain.beans.OldFileInfo)
	 */
	@Override
	public void deleteOldFileInfoContent(final OldFileInfo oldFileInfo) {
		checkV2Path(false);
		deleteContent(getFilename(oldFileInfo));
	}

}