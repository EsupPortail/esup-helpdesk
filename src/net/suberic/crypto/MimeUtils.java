package net.suberic.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

/**
 * Utilities for handling Mime messages from JavaMail.
 */
public class MimeUtils {

  static MimeUtils utilsInstance = new MimeUtils();

  /**
   * Translates a MimeMessage into its MIME-canonical form.
   */
  public static MimeMessage canonicalize(MimeMessage mm, Session s) 
  throws javax.mail.MessagingException, java.io.IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    java.io.OutputStream os = utilsInstance.new MimeCanonicalOutputStream(baos);
    //mm.writeTo(MimeUtility.encode(os, mm.getEncoding()));;
    mm.writeTo(os);
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    
    MimeMessage returnValue = new MimeMessage(s, bais);

    return returnValue;
  }

  /**
   * Translates a MimeBodyPart into its MIME-canonical form.
   */
  public static MimeBodyPart canonicalize(MimeBodyPart mm) 
  throws javax.mail.MessagingException, java.io.IOException {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    java.io.OutputStream os = utilsInstance.new MimeCanonicalOutputStream(baos);
    //mm.writeTo(MimeUtility.encode(os, mm.getEncoding()));
    mm.writeTo(os);

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    
    MimeBodyPart returnValue = new MimeBodyPart(bais);

    return returnValue;
  }

  /**
   * A stream which filters bytes, converting bare \n's to \r\n's.
   */
  class MimeCanonicalOutputStream extends java.io.FilterOutputStream {
    int lastReadByte = -1;
    byte[] crlf = new byte[] { (byte)'\r', (byte)'\n' };
    
    public MimeCanonicalOutputStream(java.io.OutputStream os) {
      super(os);
    }
    
    public void write(int b) throws java.io.IOException {
       if (b == '\r') {
	 out.write(crlf);
       } else if (b == '\n') {
	 if (lastReadByte != '\r')
	   out.write(crlf);
       } else {
	 out.write(b);
       }
       lastReadByte = b;
    }
    
    public void write(byte b[]) throws java.io.IOException {
      write(b, 0, b.length);
    }
    
    public void write(byte b[], int off, int len) throws java.io.IOException {
      int start = off;
      
      len = off + len;
      for (int i = start; i < len ; i++) {
	if (b[i] == '\r') {
	  out.write(b, start, i - start);
	  out.write(crlf);
	  start = i + 1;
	} else if (b[i] == '\n') {
	  if (lastReadByte != '\r') {
	    out.write(b, start, i - start);
	    out.write(crlf);
	  }
	  start = i + 1;
	 }
	 lastReadByte = b[i];
       }
       if ((len - start) > 0)
	 out.write(b, start, len - start);
     }
     
   }
}
