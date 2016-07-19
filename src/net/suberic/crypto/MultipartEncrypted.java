package net.suberic.crypto;

import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;

/**
 * A subclass of MimeMultipart which allows for a custom content type.
 */
public class MultipartEncrypted extends MimeMultipart {

  ContentType cType = null;

  /**
   * Makes a Multipart that uses the given content type, but otherwise
   * is exactly like a multipart/mixed.
   */
  public MultipartEncrypted(ContentType ct) {
    super("mixed");
    cType = ct;
  }
  
  /**
   * Returns the content type given in the constructor.
   */
  public String getContentType() {
    try {
      ContentType newCtype = new ContentType(super.getContentType());
      ContentType retCtype = new ContentType(cType.toString());
      retCtype.setParameter("boundary", newCtype.getParameter("boundary"));
      return retCtype.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "null";
  }

}
