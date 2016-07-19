/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.esupportail.commons.exceptions.ConfigException;


/**
 * Utilities for files.
 */
public class FileUtils {
	
	/**
	 * The size of the buffer used to read files.
	 */
	private static final int BUFFER_SIZE = 512;
	
	/**
	 * Constructor.
	 */
	private FileUtils() {
		throw new UnsupportedOperationException();
	}
	
    /**
     * @param path
     * @return the content of a file.
     * @throws ConfigException 
     */
    public static byte[] getFileContent(
    		final String path) throws ConfigException {
		try {
			InputStream is = FileUtils.class.getResourceAsStream(path); 
			if (is == null) {
				throw new ConfigException("could not read [" + path + "]");
			}
			return getFileContent(is);
		} catch (IOException e) {
			throw new ConfigException(e);
		}
    }	
    
    /**
     * @param file
     * @return the content of a file.
     * @throws ConfigException 
     */
    public static byte[] getFileContent(final File file) throws ConfigException {
		InputStream is; 
		try {
			is = new FileInputStream(file); 
		} catch (IOException e) {
			throw new ConfigException("could not read [" + file.getName() + "]", e);
		}
		try {
			return getFileContent(is);
		} catch (IOException e) {
			throw new ConfigException(e);
		}
    }

	/**
	 * @param is
	 * @return the content of the input stream.
	 * @throws IOException
	 */
	private static byte[] getFileContent(InputStream is) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(2 * BUFFER_SIZE);
		byte[] buf = new byte[BUFFER_SIZE];
		int readBytes;
		while ((readBytes = is.read(buf)) > 0) {
		    os.write(buf, 0, readBytes);
		}
		byte[] result = os.toByteArray();
		is.close();
		os.close();
		return result;
	}
    
    

}
