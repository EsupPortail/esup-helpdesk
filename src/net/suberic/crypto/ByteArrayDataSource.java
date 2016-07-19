package net.suberic.crypto;

import javax.activation.DataSource;

/**
 * A DataSource based off of a byte array.
 */
public class ByteArrayDataSource implements DataSource {

  byte[] content;

  String name;

  String contentType;
  
  /**
   * Creates a ByteArrayDataSource from the given byte array.
   */
  public ByteArrayDataSource(byte[] newContent, String newName, String newContentType) {
    content = newContent;
    name = newName;
    contentType = newContentType;
  }

  /**
   * This method returns the MIME type of the data in the form of a string. 
   * For this implementation, this is the type that is set in the constructor.
   *
   * @return the MIME type 
   */
  public java.lang.String getContentType() {
    return contentType;
  }

  /**
   * Returns a new ByteArrayInputStream based off of the byte array given
   * as content.
   *
   * @return a ByteArrayInputStream
   */
  public java.io.InputStream getInputStream() {
    return new java.io.ByteArrayInputStream(content);
  }

  /**
   * Return the name of this object where the name of the object is dependant 
   * on the nature of the underlying objects.
   * 
   * @return the name given in the constructor
   */
  public java.lang.String getName() {
    return name;
  }

  /**
   * This method returns an OutputStream where the data can be written and 
   * throws the appropriate exception if it can not do so.
   * 
   * Not appropriate for this implementation; throws an IOException.
   * 
   * @return an OutputStream
   */
  public java.io.OutputStream getOutputStream() throws java.io.IOException {
    throw new java.io.IOException("Output stream not supported.");
  }
}
