/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils; 

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.commons.exceptions.DownloadException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.web.servlet.DownloadServlet;

/**
 * A utility class to send files to the client.
 */
public final class DownloadUtils {  
	
	/**
	 * A logger.
	 */
	private static final Logger LOGGER = new LoggerImpl(DownloadUtils.class);
	
	/**
	 * Private constructor.
	 */
	private DownloadUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Set a download attribute for the download servlet.
	 * @param name 
	 * @param value 
	 */
	private static void setDownloadAttribute(
			final String name,
			final Object value) {
		ContextUtils.setGlobalSessionAttribute(name, value);
	}

	/**
	 * @param name 
	 * @return an attribute previously set by setDownloadAttribute().
	 */
	public static Object getDownloadAttribute(
			final String name) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getDownloadAttribute()");
			for (String string : ContextUtils.getSessionAttributesStrings()) {
				LOGGER.debug(string);
			}
		}
		return ContextUtils.getSessionAttribute(name);
	}

	/**
	 * @param id 
	 * @return the download URL (to redirect to).
	 * @throws DownloadException 
	 */
	public static String getDownloadUrl(
			final long id) throws DownloadException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		String downloadUrl;
		if (PortletUtil.isPortletRequest(facesContext)) {
			downloadUrl = externalContext.getRequestContextPath() + "/download"; 
		} else {
			String path = externalContext.getRequestContextPath();
			downloadUrl = path + externalContext.getRequestServletPath().replaceFirst(
					"/stylesheets/[^/]*$", "/download");
			
		}
		downloadUrl += "?" + DownloadServlet.ID_ATTRIBUTE + "=" + id;
		return downloadUrl;
	}

	/**
	 * Set download data.
	 * @param bytes
	 * @param filename 
	 * @param contentType 
	 * @param contentDisposition 
	 * @return the download id
	 * @throws DownloadException 
	 */
	public static long setDownload(
			final byte [] bytes,
			final String filename,
			final String contentType,
			final String contentDisposition) throws DownloadException {
		try {
			long id = System.currentTimeMillis();
			setDownloadAttribute(DownloadServlet.DATA_ATTRIBUTE + id, bytes);
			if (contentType != null) {
				setDownloadAttribute(DownloadServlet.CONTENT_TYPE_ATTRIBUTE + id, contentType);
			}
			if (filename != null) {
				setDownloadAttribute(DownloadServlet.FILENAME_ATTRIBUTE + id, filename);
			}
			if (contentDisposition != null) {
				setDownloadAttribute(DownloadServlet.CONTENT_DISPOSITION_ATTRIBUTE, contentDisposition);
			}
			return id;
		} catch (DownloadException e) {
			throw e;
		} catch (Throwable t) {
			throw new DownloadException(t);
		}
	}
	
	/**
	 * Set download data.
	 * @param bytes
	 * @param filename 
	 * @param contentType 
	 * @return the download id
	 * @throws DownloadException 
	 */
	public static long setDownload(
			final byte [] bytes,
			final String filename,
			final String contentType) throws DownloadException {
		return setDownload(bytes, filename, contentType, null);
	}

	/**
	 * Set download data.
	 * @param bytes
	 * @param filename 
	 * @return the download id
	 * @throws DownloadException 
	 */
	public static long setDownload(
			final byte [] bytes,
			final String filename) throws DownloadException {
		return setDownload(bytes, filename, null);
	}

	/**
	 * Set download data.
	 * @param bytes
	 * @return the download id
	 * @throws DownloadException 
	 */
	public static long setDownload(
			final byte [] bytes) throws DownloadException {
		return setDownload(bytes, null);
	}

	/**
	 * @param file 
	 * @return the contents of a file in a byte array.
	 * @throws DownloadException
	 */
	protected static byte[] getBytesFromFile(final File file) throws DownloadException {
		try {
			InputStream is = new FileInputStream(file);
			long length = file.length();
			if (length > Integer.MAX_VALUE) {
				throw new IOException("file [" + file.getName() + "] is too large for download");
			}
			byte[] bytes = new byte[(int) length];
			int offset = 0;
			int numRead = is.read(bytes, offset, bytes.length - offset);
			while (offset < bytes.length && numRead >= 0) {
				offset += numRead;
				numRead = is.read(bytes, offset, bytes.length - offset);
			}
			if (offset < bytes.length) {
				throw new IOException("could not completely read file [" + file.getName() + "]");
			}
			is.close();
			return bytes;
		} catch (Throwable t) {
			throw new DownloadException(t);
		}
	}
	
	/**
	 * Set download data.
	 * @param file
	 * @param contentType 
	 * @param contentDisposition 
	 * @return the download id
	 * @throws DownloadException 
	 */
	public static long setDownload(
			final File file,
			final String contentType,
			final String contentDisposition) throws DownloadException {
		return setDownload(getBytesFromFile(file), file.getName(), contentType, contentDisposition);
	}

	/**
	 * Set download data.
	 * @param file
	 * @param contentType 
	 * @return the download id
	 * @throws DownloadException 
	 */
	public static long setDownload(
			final File file,
			final String contentType) throws DownloadException {
		return setDownload(file, contentType, null);
	}

	/**
	 * Set download data.
	 * @param file
	 * @return the download id
	 * @throws DownloadException 
	 */
	public static long setDownload(
			final File file) throws DownloadException {
		return setDownload(file, null);
	}

	/**
	 * Set download data.
	 * @param path
	 * @param contentType 
	 * @param contentDisposition 
	 * @return the download id
	 * @throws DownloadException 
	 */
	public static long  setDownload(
			final String path,
			final String contentType,
			final String contentDisposition) throws DownloadException {
		return setDownload(new File(path), contentType, contentDisposition);
	}
	
	/**
	 * Set download data.
	 * @param path
	 * @param contentType 
	 * @return the download id
	 * @throws DownloadException 
	 */
	public static long  setDownload(
			final String path,
			final String contentType) throws DownloadException {
		return setDownload(path, contentType, null);
	}
	
	/**
	 * Set download data.
	 * @param path
	 * @return the download id
	 * @throws DownloadException 
	 */
	public static long  setDownload(
			final String path) throws DownloadException {
		return setDownload(path, null);
	}
	
}
