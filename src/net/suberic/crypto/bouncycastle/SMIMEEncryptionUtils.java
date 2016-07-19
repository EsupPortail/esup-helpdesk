package net.suberic.crypto.bouncycastle;

import java.io.IOException;
import java.security.Key;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;

import net.suberic.crypto.EncryptionKeyManager;
import net.suberic.crypto.EncryptionManager;
import net.suberic.crypto.EncryptionUtils;
import net.suberic.crypto.MimeUtils;
import net.suberic.crypto.UpdatableMBP;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.smime.SMIMECapabilitiesAttribute;
import org.bouncycastle.asn1.smime.SMIMECapability;
import org.bouncycastle.asn1.smime.SMIMECapabilityVector;
import org.bouncycastle.asn1.smime.SMIMEEncryptionKeyPreferenceAttribute;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.mail.smime.SMIMESignedGenerator;
import org.bouncycastle.mail.smime.SMIMEUtil;


/**
 * Utilities for encrypting/decrypting messages.
 */
public class SMIMEEncryptionUtils extends EncryptionUtils {

  public SMIMEEncryptionUtils() {

    MailcapCommandMap _mailcap =
      (MailcapCommandMap)CommandMap.getDefaultCommandMap();

    _mailcap.addMailcap("application/pkcs7-signature;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_signature");
    _mailcap.addMailcap("application/pkcs7-mime;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_mime");
    _mailcap.addMailcap("application/x-pkcs7-signature;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_signature");
    _mailcap.addMailcap("application/x-pkcs7-mime;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_mime");
    _mailcap.addMailcap("multipart/signed;;x-java-content-handler=org.bouncycastle.mail.smime.handlers.multipart_signed");

    CommandMap.setDefaultCommandMap(_mailcap);

    // register the BouncyCastle provider.
    if(Security.getProvider("BC") == null)
      Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  /**
   * Encrypts a Message.
   */
  public MimeMessage encryptMessage(Session s, MimeMessage msg, java.security.Key[] keys)
    throws MessagingException, IOException, java.security.GeneralSecurityException {
    MimeMessage encryptedMessage = new MimeMessage(s);

    java.util.Enumeration hdrEnum = msg.getAllHeaders();
    while (hdrEnum.hasMoreElements()) {
      Header current = (Header) hdrEnum.nextElement();
      encryptedMessage.setHeader(current.getName(), current.getValue());
    }

    try {
      SMIMEEnvelopedGenerator  gen = new SMIMEEnvelopedGenerator();

      for (int i = 0; i < keys.length; i++) {
    	  BouncySMIMEEncryptionKey bKey = (BouncySMIMEEncryptionKey) keys[i];
          gen.addKeyTransRecipient(bKey.getCertificate());
	  }

      MimeBodyPart mp = gen.generate(msg, SMIMEEnvelopedGenerator.AES256_CBC, "BC");
      encryptedMessage.setContent(mp.getContent(), mp.getContentType());
      java.util.Enumeration mpEnum = mp.getAllHeaders();
      while (mpEnum.hasMoreElements()) {
        Header current = (Header) mpEnum.nextElement();
        encryptedMessage.setHeader(current.getName(), current.getValue());
      }

      encryptedMessage.saveChanges();

      return encryptedMessage;
    } catch (org.bouncycastle.mail.smime.SMIMEException sme) {
      throw new MessagingException(sme.getMessage());
    }

  }

  /**
   * Encrypts a MimeBodyPart;
   */
  public MimeBodyPart encryptPart(MimeBodyPart part, java.security.Key[] keys)
    throws MessagingException, java.security.GeneralSecurityException {

    try {
      SMIMEEnvelopedGenerator  gen = new SMIMEEnvelopedGenerator();

      for (int i = 0; i < keys.length; i++) {
          BouncySMIMEEncryptionKey bKey = (BouncySMIMEEncryptionKey) keys[i];
          gen.addKeyTransRecipient(bKey.getCertificate());
      }

      MimeBodyPart mp = gen.generate((MimeBodyPart)part, SMIMEEnvelopedGenerator.AES256_CBC, "BC");

      return mp;
    } catch (org.bouncycastle.mail.smime.SMIMEException sme) {
      throw new MessagingException(sme.getMessage());
    }
  }

  /**
   * Decrypts a Message.
   */
  public  javax.mail.internet.MimeMessage decryptMessage(Session s, javax.mail.internet.MimeMessage msg, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException {
    try {

      BouncySMIMEEncryptionKey bKey = (BouncySMIMEEncryptionKey) key;

      RecipientId     recId = new RecipientId();
      X509Certificate cert = bKey.getCertificate();

      Key privateKey = bKey.getKey();

      recId.setSerialNumber(cert.getSerialNumber());
      recId.setIssuer(cert.getIssuerX500Principal().getEncoded());

      SMIMEEnveloped       m = new SMIMEEnveloped(msg);
      RecipientInformationStore   recipients = m.getRecipientInfos();
      RecipientInformation        recipient = recipients.get(recId);

      if (recipient == null)
        throw new MessagingException ("no recipient");

      MimeBodyPart mbp = SMIMEUtil.toMimeBodyPart(recipient.getContent(privateKey, "BC"));

      MimeMessage decryptedMessage = new MimeMessage(s);

      java.util.Enumeration hLineEnum = msg.getAllHeaderLines();
      while (hLineEnum.hasMoreElements()) {
        decryptedMessage.addHeaderLine((String) hLineEnum.nextElement());
      }

      decryptedMessage.setContent(mbp.getContent(), mbp.getContentType());

      return decryptedMessage;

    } catch (CMSException cmse) {
      throw new MessagingException(cmse.toString());
      // FIXME
    } catch (org.bouncycastle.mail.smime.SMIMEException sme) {
      sme.printStackTrace();
      throw new MessagingException(sme.getMessage());
    }
  }

  /**
   * Decrypts a BodyPart.
   */
  public  MimeBodyPart decryptBodyPart(MimeBodyPart part, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException {
    try {
        SMIMEEnveloped       m = new SMIMEEnveloped((MimeBodyPart) part);

        BouncySMIMEEncryptionKey bKey = (BouncySMIMEEncryptionKey) key;

      RecipientId     recId = new RecipientId();
      X509Certificate cert = bKey.getCertificate();

      Key privateKey = bKey.getKey();

      recId.setSerialNumber(cert.getSerialNumber());
      recId.setIssuer(cert.getIssuerX500Principal().getEncoded());

      RecipientInformationStore   recipients = m.getRecipientInfos();
      RecipientInformation        recipient = recipients.get(recId);

      MimeBodyPart mbp = SMIMEUtil.toMimeBodyPart(recipient.getContent(privateKey, "BC"));

      return mbp;
    } catch (CMSException cmse) {
      throw new MessagingException(cmse.toString());
      // FIXME
    } catch (org.bouncycastle.mail.smime.SMIMEException sme) {
      throw new MessagingException(sme.getMessage());
    }

  }

  /**
   * Decrypts a Multipart.
   */
  public  MimeBodyPart decryptMultipart(MimeMultipart mpart, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException {

    MimeBodyPart mbp = new MimeBodyPart();
    mbp.setContent(mpart);
    return decryptBodyPart(mbp,key);
  }

  /**
   * Signs a Part.
   * TODO: add header protection code
   */
  public MimeBodyPart signBodyPart(MimeBodyPart p, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException {

    try {
      MimeBodyPart cBp = MimeUtils.canonicalize(p);

      BouncySMIMEEncryptionKey bKey = (BouncySMIMEEncryptionKey) key;

      java.security.cert.Certificate[] certChain = bKey.getCertificateChain();
      X509Certificate cert = bKey.getCertificate();

      List certList = null;
      if (certChain != null && certChain.length > 0) {
        certList = Arrays.asList(certChain);
      } else {
        certList = new ArrayList();
        certList.add(cert);
      }

      CertStore certsAndcrls = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");

      SMIMESignedGenerator gen = new SMIMESignedGenerator();

      ASN1EncodableVector         signedAttrs = new ASN1EncodableVector();
      SMIMECapabilityVector       caps = new SMIMECapabilityVector();

      caps.addCapability(SMIMECapability.dES_EDE3_CBC);
      caps.addCapability(SMIMECapability.rC2_CBC, 128);
      caps.addCapability(SMIMECapability.dES_CBC);

      IssuerAndSerialNumber   issAndSer = new IssuerAndSerialNumber(new X509Name(cert.getIssuerDN().getName()), cert.getSerialNumber());

      signedAttrs.add(new SMIMEEncryptionKeyPreferenceAttribute(issAndSer));

      signedAttrs.add(new SMIMECapabilitiesAttribute(caps));      
  	  
      gen.addSigner(bKey.getKey(), bKey.getCertificate(), SMIMESignedGenerator.DIGEST_SHA1, new AttributeTable(signedAttrs), null);
      gen.addCertificatesAndCRLs(certsAndcrls);

      MimeMultipart mm = gen.generate(cBp, "BC");

      MimeBodyPart returnValue = new MimeBodyPart();
      returnValue.setContent(mm);
      return returnValue;
    } catch (org.bouncycastle.mail.smime.SMIMEException sme) {
      throw new MessagingException(sme.getMessage());
    }

  }

  /**
   * Signs a Message.
   */
  public MimeMessage signMessage(Session s, MimeMessage m, java.security.Key key) 
  throws MessagingException, IOException, java.security.GeneralSecurityException {
    Object content = m.getContent();
    UpdatableMBP mbp = new UpdatableMBP();
    if (content instanceof Multipart) {
      mbp.setContent((Multipart) content);
    } else {
      mbp.setContent(content, m.getContentType());
    }
    mbp.updateMyHeaders();

    MimeBodyPart signedPart = signBodyPart(mbp, key);

    MimeMessage signedMessage = new MimeMessage(s);

    java.util.Enumeration hLineEnum = m.getAllHeaderLines();
    while (hLineEnum.hasMoreElements()) {
      signedMessage.addHeaderLine((String) hLineEnum.nextElement());
    }

    signedMessage.setContent((MimeMultipart)signedPart.getContent());

    return signedMessage;
  }

  /**
   * Checks the signature on a Part.
   * @param key We use the embedded certificate to verify the signature, 
   *        hence this paramater will not be used. 
   */
  public  boolean checkSignature(MimePart p, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException {

    try {
      SMIMESigned s = new SMIMESigned(p);

      return checkSignature(s);
    } catch (CMSException cmse) {
      // FIXME
      throw new MessagingException(cmse.toString());
    } catch (org.bouncycastle.mail.smime.SMIMEException sme) {
      throw new MessagingException(sme.getMessage());
    }
  }

  /**
   * Checks the signature on a Message.
   * @param key We use the embedded certificate to verify the signature, 
   *        hence this paramater will not be used. 
   */
  public  boolean checkSignature(MimeMessage m, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException {	  
    try {
      if (m.isMimeType("multipart/signed")) {
        SMIMESigned s = new SMIMESigned((MimeMultipart)m.getContent());
        return checkSignature(s);
      } else if (m.isMimeType("application/pkcs7-mime") || m.isMimeType("application/x-pkcs7-mime")) {
        SMIMESigned s = new SMIMESigned(m);
        return checkSignature(s);
      } else {
        // FIXME
        return false;
      }
    } catch (CMSException cmse) {
      // FIXME
      throw new MessagingException(cmse.toString());
    } catch (org.bouncycastle.mail.smime.SMIMEException sme) {
      throw new MessagingException(sme.getMessage());
    }

  }

  /**
   * Checks the signature on a Multipart.
   * @param key We use the embedded certificate to verify the signature, 
   *        hence this paramater will not be used. 
   */
  public  boolean checkSignature(MimeMultipart m, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException {
    try {
      SMIMESigned s = new SMIMESigned(m);
      return checkSignature(s);
    } catch (CMSException cmse) {
      // FIXME
      throw new MessagingException(cmse.toString());
    }
  }

  /**
   * Checks a SMIMESigned to make sure that the signature matches.
   */
  private boolean checkSignature(SMIMESigned s)
    throws MessagingException, IOException, java.security.GeneralSecurityException {
    try {
      boolean sigValid = true;
      CertStore certs = s.getCertificatesAndCRLs("Collection", "BC");

      SignerInformationStore signers = s.getSignerInfos();

      Collection c = signers.getSigners();
      Iterator it = c.iterator();

      //while (sigValid == true && it.hasNext()) {
      // Liao: check only the first signer, since in the S/MIME usually only one signer
        SignerInformation signer = (SignerInformation)it.next();
        Collection certCollection = certs.getCertificates(signer.getSID());

        Iterator certIt = certCollection.iterator();
        while (certIt.hasNext()) {
          X509Certificate cert = (X509Certificate)certIt.next();

          if (! signer.verify(cert, "BC")) {
        	  sigValid = false;
          }
        }
        
        return sigValid;
    } catch (CMSException cmse) {
      // FIXME
      throw new MessagingException(cmse.toString());
    }
  }

  /**
   * Returns the signed body part.
   */
  public MimeBodyPart getSignedContent(MimePart mp)
    throws MessagingException, IOException {

    try {
      if (mp.isMimeType("multipart/signed")) {
        SMIMESigned s = new SMIMESigned((MimeMultipart)mp.getContent());
        return s.getContent();
      } else if (mp.isMimeType("application/pkcs7-mime")|| mp.isMimeType("application/x-pkcs7-mime")) {
        SMIMESigned s = new SMIMESigned(mp);
        return s.getContent();
      } else {
        // FIXME
        throw new MessagingException("expected part of type multipart/signed or application/pkcs7-mime, but got type " + mp.getContentType());
      }
    } catch (CMSException cmse) {
      // FIXME
      throw new MessagingException(cmse.toString());
    } catch (SMIMEException sme) {
      // FIXME
      throw new MessagingException(sme.toString());
    }
  }

  /**
   * Creates a public key body part.
   */
  public MimeBodyPart createPublicKeyPart(java.security.Key[] keys)
    throws MessagingException {
    // FIXME
    return null;
  }

  /**
   * Extracts keys from a public key body part.
   *
   */
  public java.security.Key[] extractKeys(MimeBodyPart mbp)
    throws MessagingException, IOException {
    // FIXME
    return null;
  }

  /**
   * Extracts keys from a public key body part.
   *
   */
  public java.security.Key[] extractKeys(MimeMessage m)
    throws MessagingException, IOException {
    // FIXME
    return null;
  }

  /**
   * Creates an empty EncryptionKeyManager that's appropriate for this
   * Encryption provider.
   */
  public EncryptionKeyManager createKeyManager() {
    return new BouncySMIMEEncryptionKeyManager();
  }

  /**
   * Creates and loads an EncryptionKeyManager that's appropriate for this
   * Encryption provider.
   */
  public  EncryptionKeyManager createKeyManager(java.io.InputStream inputStream, char[] passwd) throws java.io.IOException, java.security.NoSuchAlgorithmException, java.security.NoSuchProviderException, java.security.KeyStoreException, java.security.cert.CertificateException {
    BouncySMIMEEncryptionKeyManager mgr = new BouncySMIMEEncryptionKeyManager();
    mgr.loadPublicKeystore(inputStream, passwd);
    mgr.loadPrivateKeystore(inputStream, passwd);
    return mgr;
  }


  /**
   * Returns the encryption status of this MimeMessage:
   * ENCRYPTED, SIGNED, or NOT_ENCRYPTED.
   */
  public int getEncryptionStatus(MimePart m)
    throws MessagingException {
    // simple version
    if (m.isMimeType("application/pkcs7-mime")|| m.isMimeType("application/x-pkcs7-mime"))
      return ENCRYPTED;
    else if (m.isMimeType("multipart/signed"))
      return SIGNED;
    else
      return NOT_ENCRYPTED;
  }

  /**
   * Returns the encryption type that these utils are implementing (PGP
   * or SMIME).
   */
  public String getType() {
    return EncryptionManager.SMIME;
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
      if (baseType.equalsIgnoreCase("multipart/signed")) {
        String protocol = ct.getParameter("protocol");
        if (protocol != null && ( protocol.equalsIgnoreCase("application/pkcs7-signature") || protocol.equalsIgnoreCase("application/x-pkcs7-signature") )) {
          return EncryptionManager.SMIME;
        }
      } else if (baseType.equalsIgnoreCase("application/pkcs7-mime")|| baseType.equalsIgnoreCase("application/x-pkcs7-mime")) {
        return EncryptionManager.SMIME;
      }
    }

    return null;
  }

}
