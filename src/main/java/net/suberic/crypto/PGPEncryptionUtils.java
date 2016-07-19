package net.suberic.crypto;


import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.security.GeneralSecurityException;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;

/**
 * Utilities for encrypting/decrypting messages.
 *
 * This class just handles the parsing of the OpenPGP message itself.
 * Actual PGP encoding/decoding is left to the PGPProviderImpl class itself.
 */
public class PGPEncryptionUtils extends EncryptionUtils {
  // public static final String BEGIN_PGP_MESSAGE = "-----BEGIN PGP MESSAGE-----";
  // public static final String BEGIN_PGP_SIGNED_MESSAGE = "-----BEGIN PGP SIGNED MESSAGE-----";
  // public static final String BEGIN_PGP_SIGNATURE = "-----BEGIN PGP SIGNATURE-----";

  PGPProviderImpl pgpImpl = null;
  /**
   * Returns the PGPProviderImpl.
   */
  public PGPProviderImpl getPGPProviderImpl() {
    return pgpImpl;
  }
  /**
   * Sets the PGPProviderImpl.
   */
  public void setPGPProviderImpl(PGPProviderImpl newPgpImpl) {
    pgpImpl = newPgpImpl;
  }

  /**
   * Decrypts a section of text using an java.security.Key.
   */
  public byte[] decrypt(java.io.InputStream encryptedStream, java.security.Key key) throws java.security.NoSuchAlgorithmException, java.io.IOException, java.security.GeneralSecurityException {
    return pgpImpl.decrypt(encryptedStream, key);
  }

  /**
   * Encrypts a section of text using an java.security.Key.
   */
  public byte[] encrypt(java.io.InputStream rawStream, java.security.Key[] keys) throws java.security.NoSuchAlgorithmException, java.io.IOException, GeneralSecurityException {
    return pgpImpl.encrypt(rawStream, keys);
  }

  /**
   * Signs a section of text.
   */
  public byte[] sign(InputStream rawStream, java.security.Key key) throws java.security.NoSuchAlgorithmException, java.io.IOException, GeneralSecurityException {
    return pgpImpl.sign(rawStream, key);
  }

  /**
   * Encrypts a Message.
   */
  public MimeMessage encryptMessage(Session s, MimeMessage msg, java.security.Key[] keys) throws MessagingException, IOException, java.security.GeneralSecurityException {
    MimeMessage encryptedMessage = new MimeMessage(s);

    java.util.Enumeration hdrLines = msg.getAllHeaderLines();
    while (hdrLines.hasMoreElements()) {
      encryptedMessage.addHeaderLine((String) hdrLines.nextElement());
    }

    //...sigh.
    UpdatableMBP ubp = new UpdatableMBP();
    ubp.setContent(msg.getContent(), msg.getContentType());
    ubp.updateMyHeaders();
    MimeBodyPart mbp = encryptPart(ubp, keys);

    encryptedMessage.setContent(mbp.getContent(), mbp.getContentType());
    //ContentType cType = new ContentType("multipart/encrypted");
    //cType.setParameter("protocol", "application/pgp-encrypted");
    //encryptedMessage.setContentType(cType.toString());

    return encryptedMessage;

  }

  /**
   * Decrypts a Message.
   */
  public javax.mail.internet.MimeMessage decryptMessage(Session s, javax.mail.internet.MimeMessage msg, java.security.Key key) throws MessagingException, IOException, java.security.GeneralSecurityException {
    Object o = msg.getContent();
    if (o instanceof Multipart) {
      BodyPart decryptedPart = decryptMultipart((MimeMultipart) o, key);
      MimeMessage m = new MimeMessage(s);
      java.util.Enumeration hdrLines = msg.getAllHeaderLines();
      while (hdrLines.hasMoreElements()) {
        m.addHeaderLine((String) hdrLines.nextElement());
      }
      m.setDataHandler(decryptedPart.getDataHandler());
      return m;
    }

    return null;
  }

  /**
   * Encrypts a MimeBodyPart;
   */
  public MimeBodyPart encryptPart(MimeBodyPart part, java.security.Key[] keys) throws MessagingException, java.security.GeneralSecurityException, IOException {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    part.writeTo(baos);
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

    return createEncryptedPart(bais, keys);

  }

  /**
   * Makes an encrypted BodyPart from the content in the given
   * InputStream.
   */
  protected MimeBodyPart createEncryptedPart(java.io.InputStream stream, java.security.Key[] keys) throws MessagingException, java.io.IOException, java.security.GeneralSecurityException {
    ContentType cType = new ContentType("multipart/encrypted");
    cType.setParameter("protocol", "application/pgp-encrypted");
    MimeMultipart mm = new MultipartEncrypted(cType);

    MimeBodyPart idPart = new MimeBodyPart();
    DataSource ds = new ByteArrayDataSource(new String("Version: 1").getBytes(), "pgpapp", "application/pgp-encrypted");
    idPart.setDataHandler(new javax.activation.DataHandler(ds));

    //idPart.setContent("Version: 1", "application/pgp-encrypted");

    MimeBodyPart encryptedContent = new MimeBodyPart();
    byte[] encryptedBytes = encrypt(stream, keys);
    ByteArrayDataSource dataSource = new ByteArrayDataSource(encryptedBytes, "message", "application/octet-stream");

    javax.activation.DataHandler dh = new javax.activation.DataHandler(dataSource);

    encryptedContent.setFileName("message");
    encryptedContent.setDataHandler(dh);

    mm.addBodyPart(idPart);
    mm.addBodyPart(encryptedContent);

    UpdatableMBP returnValue = new UpdatableMBP();
    returnValue.setContent(mm);
    returnValue.updateMyHeaders();

    return returnValue;

  }

  /**
   * Decrypts a MimeBodyPart.
   */
  public MimeBodyPart decryptBodyPart(MimeBodyPart part, java.security.Key key) throws MessagingException, IOException, java.security.GeneralSecurityException {
    // check the type; should be multipart/encrypted

    String contentType = part.getContentType();
    ContentType ct = new ContentType(contentType);
    Object content = part.getContent();

    if (ct.getBaseType().equalsIgnoreCase("multipart/encrypted")) {
      MimeMultipart mpart = (MimeMultipart) content;
      return decryptMultipart(mpart, key);
      /*
    } else {
      if(content instanceof String) {
        String s = (String) content;
        if(s.startsWith(PGPEncryptionUtils.BEGIN_PGP_MESSAGE)){
          java.io.InputStream is = new ByteArrayInputStream(s.getBytes());

          byte[] value = decrypt(is, key);

          MimeBodyPart returnValue = new MimeBodyPart();
          returnValue.setText(new String(value));

          return returnValue;
        }      */
    }

    throw new GeneralSecurityException("invalid PGP encryption message");
    // FIXME:  check for protocol, too.

  }

  /**
   * Decrypts a Multipart.
   */
  public MimeBodyPart decryptMultipart(MimeMultipart mpart, java.security.Key key) throws MessagingException, IOException, java.security.GeneralSecurityException {

    // should have two internal parts.
    if (mpart.getCount() != 2) {
      throw new MessagingException ("error in content:  expected 2 parts, got " + mpart.getCount());
    }

    // first part should be application/pgp-encrypted
    BodyPart firstPart = mpart.getBodyPart(0);
    String firstPartType = firstPart.getContentType();
    ContentType firstCt = new ContentType(firstPartType);
    if (firstPartType == null || ! firstCt.getBaseType().equalsIgnoreCase("application/pgp-encrypted")) {
      throw new MessagingException ("error in content:  expected first part of type application/pgp-encrypted, got " + firstPartType);
    }

    // don't bother checking the version for now.

    BodyPart secondPart = mpart.getBodyPart(1);
    String secondPartType = secondPart.getContentType();
    ContentType secondCt = new ContentType(secondPartType);
    if (secondPartType == null || ! secondCt.getBaseType().equalsIgnoreCase("application/octet-stream")) {
      throw new MessagingException ("error in content:  expected second part of type application/octet-stream, got " + secondPartType);
    }

    java.io.InputStream is = secondPart.getInputStream();

    byte[] value = decrypt(is, key);

    ByteArrayInputStream bais = new ByteArrayInputStream(value);

    MimeBodyPart returnValue = new MimeBodyPart(bais);

    return returnValue;
  }

  /**
   * Signs a MimeBodyPart.
   * TODO: add header protect code
   */
  public MimeBodyPart signBodyPart(MimeBodyPart mbp, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException {

    MimeBodyPart p = MimeUtils.canonicalize(mbp);

    ContentType ct = new ContentType("multipart/signed");
    ct.setParameter("protocol", "application/pgp-signature");
    ct.setParameter("micalg", "pgp-sha1");
    MimeMultipart mpart = new MultipartEncrypted(ct);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    com.sun.mail.util.CRLFOutputStream los = new com.sun.mail.util.CRLFOutputStream(baos);
    OutputStreamWriter osw = new OutputStreamWriter(los);

    BufferedWriter bw = new BufferedWriter(osw);

    java.util.Enumeration hdrLines = ((MimeBodyPart)p).getAllHeaderLines();

    while (hdrLines.hasMoreElements()) {
      String nextLine = (String)hdrLines.nextElement();
      bw.write(nextLine);
      bw.newLine();
    }

    bw.newLine();
    bw.flush();

    p.getDataHandler().writeTo(javax.mail.internet.MimeUtility.encode(los, p.getEncoding()));

    InputStream is = new ByteArrayInputStream(baos.toByteArray());

    byte[] signature = sign(is, key);
    MimeBodyPart sigPart = new MimeBodyPart();

    ByteArrayDataSource dataSource = new ByteArrayDataSource(
                                                             signature, "signature", "application/pgp-signature");

    javax.activation.DataHandler dh = new javax.activation.DataHandler(dataSource);

    sigPart.setDataHandler(dh);

    mpart.addBodyPart(p);
    mpart.addBodyPart(sigPart);

    MimeBodyPart returnValue = new MimeBodyPart();
    returnValue.setContent(mpart);
    return returnValue;
  }

  /**
   * Checks the signature on a MimeMessage.
   */
  public boolean checkSignature(MimeMessage mm, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException {
    Object o = mm.getContent();

    if (o instanceof MimeMultipart) {
      return checkSignature((MimeMultipart) o, key);
    }

    return false;
  }

  /**
   * Signs a Message.
   */
  public MimeMessage signMessage(
                                 Session s, MimeMessage msg, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException {

    MimeMessage signedMessage = new MimeMessage(s);

    java.util.Enumeration hdrLines = msg.getAllHeaderLines();
    while (hdrLines.hasMoreElements()) {
      signedMessage.addHeaderLine((String) hdrLines.nextElement());
    }

    Object o = msg.getContent();

    UpdatableMBP mbp = new UpdatableMBP();
    mbp.setContent(o, msg.getContentType());
    mbp.updateMyHeaders();

    /*
      InputStream is = msg.getRawInputStream();

      UpdatableMBP mbp = new UpdatableMBP(is);
      mbp.updateMyHeaders();
    */

    BodyPart signedPart = signBodyPart(mbp, key);

    signedMessage.setContent((Multipart)signedPart.getContent());
    //signedMessage.setContent((Multipart)signedPart.getContent(), "multipart/signed");

    return signedMessage;
  }

  /*
  public boolean checkSignature(String pgpSignedMessage, java.security.Key key) throws IOException, java.security.GeneralSecurityException {
    return pgpImpl.checkSignature(pgpSignedMessage, key);
  }
  */

  /**
   * Checks the signature on a MimePart.
   *
   */
  public boolean checkSignature(MimePart part, java.security.Key key) throws MessagingException, IOException, java.security.GeneralSecurityException {
    // check the type; should be multipart/signed

    String contentType = part.getContentType();
    ContentType ct = new ContentType(contentType);
    if (contentType == null || ! ct.getBaseType().equalsIgnoreCase("multipart/signed")) {
      throw new MessagingException ("error in content type:  expected 'multipart/signed', got '" + contentType + "'");
    }

    Object content = part.getContent();

    MimeMultipart mpart = (MimeMultipart) content;
    return checkSignature(mpart, key);
  }

  /**
   * Checks the signature on a BodyPart.
   */
  public boolean checkSignature(MimeMultipart mMulti, java.security.Key key) throws MessagingException, IOException, java.security.GeneralSecurityException {
    return pgpImpl.checkSignature(mMulti, key);
  }

  /**
   * Returns the signed body part.
   */
  public MimeBodyPart getSignedContent(MimePart mp) throws MessagingException, IOException {

    String contentType = mp.getContentType();
    ContentType ct = new ContentType(contentType);
    if (contentType != null &&  ct.getBaseType().equalsIgnoreCase("multipart/signed")) {
      Object content = mp.getContent();
      MimeMultipart mpart = (MimeMultipart) content;
      MimeBodyPart contentPart = (MimeBodyPart)mpart.getBodyPart(0);
      return contentPart;
    } /* else if(contentType == null || ct.getBaseType().equalsIgnoreCase("text/plain")){
      String content = (String) mp.getContent();

      MimeBodyPart bp = new MimeBodyPart();
      bp.setText(getSignedContent(content));
      return bp;
      } */


    throw new MessagingException ("expected PGP signed message");
  }

  /**
   * Extracts public key information.
   */
  public java.security.Key[] extractKeys(MimeMessage m) throws MessagingException, IOException, java.security.GeneralSecurityException {
    throw new UnsupportedOperationException("createPublicKeyPart");
    /*Liao
    // we need a MimeBodyPart of type "application/pgp-keys"
    // make sure that we're of type application/pgp-keys

    ContentType ct = new ContentType(m.getContentType());
    if (ct.getBaseType().equalsIgnoreCase("application/pgp-keys")) {
    InputStream is = m.getInputStream();
    return extractKeys(is);
    } else {
    throw new MessagingException("invalid content type; expected application/pgp-keys, got " + ct.getBaseType());
    }
    */
  }

  /**
   * Extracts public key information.
   */
  public java.security.Key[] extractKeys(MimeBodyPart mbp) throws MessagingException, IOException, java.security.GeneralSecurityException {
    throw new UnsupportedOperationException("createPublicKeyPart");
    // make sure that we're of type application/pgp-keys
    /*    ContentType ct = new ContentType(mbp.getContentType());
          if (ct.getBaseType().equalsIgnoreCase("application/pgp-keys")) {
          InputStream is = mbp.getInputStream();
          return extractKeys(is);
          } else {
          throw new MessagingException("invalid content type; expected application/pgp-keys, got " + ct.getBaseType());
          }
    */
  }

  /**
   * Packages up the public keys in a form to be sent as a public key message.
   */
  public MimeBodyPart createPublicKeyPart(java.security.Key[] keys) throws MessagingException, java.io.IOException, java.security.GeneralSecurityException {
    throw new UnsupportedOperationException("createPublicKeyPart");
    /*Liao  MimeBodyPart mbp = new MimeBodyPart();
      byte[] keyData = packageKeys(keys);
      mbp.setContent(keyData, "application/pgp-keys");
      return mbp;
    */
  }

  /**
   * Returns a KeyStore provider.
   */
  public EncryptionKeyManager createKeyManager() {
    return getPGPProviderImpl().createKeyManager();
  }

  /**
   * Returns a KeyStore provider.
   */
  public EncryptionKeyManager createKeyManager(java.io.InputStream inputStream, char[] password) throws java.io.IOException, java.security.KeyStoreException, java.security.NoSuchAlgorithmException, java.security.NoSuchProviderException, java.security.cert.CertificateException {
    return getPGPProviderImpl().createKeyManager(inputStream, password);

  }

  /**
   * Returns the encryption status of this MimeMessage:
   * ENCRYPTED, SIGNED, or NOT_ENCRYPTED.
   */
  public int getEncryptionStatus(MimePart m) throws MessagingException {
    // simple version

    if (m.isMimeType("multipart/encrypted"))
      return ENCRYPTED;
    else if (m.isMimeType("multipart/signed"))
      return SIGNED;
    else if (m.isMimeType("application/pgp-keys"))
      return ATTACHED_KEYS;
    /*
    else {
      try{
        Object obj =  m.getContent();
        if (obj instanceof String){
          String s = (String) obj;
          if (s.indexOf(BEGIN_PGP_MESSAGE) == 0)
            return ENCRYPTED;
          else if (s.indexOf(BEGIN_PGP_SIGNED_MESSAGE) == 0)
            return SIGNED;
        }
      } catch(IOException ie) {
        // do nothing
      }

    }
    */
    return NOT_ENCRYPTED;
  }

  /**
   * Returns the encryption type that these utils are implementing (PGP
   * or SMIME).
   */
  public String getType() {
    return EncryptionManager.PGP;
  }

  /**
   * Identifies the encryption type of the given MimePart.  Returns
   * true if this MimePart has an encryption type (encrypted, signed,
   * etc.) that's handled by this EncryptionUtils.
   */
  public String checkEncryptionType(MimePart mp) throws MessagingException {
    String contentType = mp.getContentType();
    if (contentType != null) {
      ContentType ct = null;
      try {
        ct = new ContentType(contentType);
      } catch (javax.mail.internet.ParseException pe) {
        return null;
      }

      String baseType = ct.getBaseType();
      if (baseType.equalsIgnoreCase("multipart/encrypted")) {
        String protocol = ct.getParameter("protocol");
        if (protocol.equalsIgnoreCase("application/pgp-encrypted"))
          return EncryptionManager.PGP;
      } else if (baseType.equalsIgnoreCase("multipart/signed")) {
        String protocol = ct.getParameter("protocol");
        if (protocol != null && protocol.equalsIgnoreCase("application/pgp-signature")) {
          return EncryptionManager.PGP;
        }
      } else if (baseType.equalsIgnoreCase("application/pgp-keys")) {
        return EncryptionManager.PGP;
      }
    }

    /*
    // Check if the message is in text, namely after -----BEGIN PGP MESSAGE-----
    try {
      Object obj = mp.getContent();
      if (obj instanceof String){
        String s = (String) obj;
        if (s.indexOf(BEGIN_PGP_MESSAGE) == 0 ||
           s.indexOf(BEGIN_PGP_SIGNED_MESSAGE) == 0){
          return EncryptionManager.PGP;
        }
      }
    } catch (IOException ie){
    }
    */
    return null;
  }

  /*
  public static String getSignaturePart(String pgpSignedMessage){
    int idx = pgpSignedMessage.indexOf(PGPEncryptionUtils.BEGIN_PGP_SIGNATURE);
    if(idx >= 0){
      return pgpSignedMessage.substring(idx);
    }

    return null;
  }

  public static void main(String[] argv){
    String s =
      BEGIN_PGP_SIGNED_MESSAGE + "\r\n" +
      "abc\r\n" +
      "\r\n" +
      //"body" + "\r\n" +
      BEGIN_PGP_SIGNATURE + "\r\n" +
      "edf";
    System.out.println(getSignedContent(s));
    System.out.println(getSignaturePart(s));
  }
  */

  /*
  public static String getSignedContent(String pgpSignedMessage){
    if(!pgpSignedMessage.startsWith(PGPEncryptionUtils.BEGIN_PGP_SIGNED_MESSAGE))
      return null;

    int lastIdx = -1;
    int idx = 0;
    while((idx = pgpSignedMessage.indexOf('\n', lastIdx+1)) != -1){
      String line = pgpSignedMessage.substring(lastIdx+1, idx);
      if(line.charAt(line.length()-1) == '\r')
        line = line.substring(0, line.length()-1);
      if(line.trim().length() == 0){
        break;
      }

      lastIdx = idx;
    }

    if(idx != -1){
      idx++;

      int endIdx = pgpSignedMessage.indexOf(PGPEncryptionUtils.BEGIN_PGP_SIGNATURE, idx);
      if(endIdx == -1)
        return null;

      if(endIdx == idx){
        return "";
      }else{
        String text = pgpSignedMessage.substring(idx, endIdx-1);
        if(text.charAt(text.length()-1) == '\r')
          text = text.substring(0, text.length()-1);

        return text;
      }
    }else{
      return null;
    }
  }
  */


}
