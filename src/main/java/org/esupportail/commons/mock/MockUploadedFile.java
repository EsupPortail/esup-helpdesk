/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.mock;

import java.io.InputStream;

import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 * An uploaded file mock.
 *
 */
public class MockUploadedFile implements UploadedFile {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6640876144695737829L;

	/**
	 * The input stream.
	 */
	private InputStream stream;

	/**
	 * The file name.
	 */
	private String name;

	/**
	 * @param stream
	 * @param name
	 */
	public MockUploadedFile(
			final InputStream stream, 
			final String name) {
		super();
		this.stream = stream;
		this.name = name;
	}

	/**
	 * @see org.apache.myfaces.custom.fileupload.UploadedFile#getBytes()
	 */
	@Override
	public byte[] getBytes() {
		return null;
	}

	/**
	 * @see org.apache.myfaces.custom.fileupload.UploadedFile#getContentType()
	 */
	@Override
	public String getContentType() {
		return null;
	}

	/**
	 * @see org.apache.myfaces.custom.fileupload.UploadedFile#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		return stream;
	}

	/**
	 * @see org.apache.myfaces.custom.fileupload.UploadedFile#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see org.apache.myfaces.custom.fileupload.UploadedFile#getSize()
	 */
	@Override
	public long getSize() {
		return 0;
	}

}
