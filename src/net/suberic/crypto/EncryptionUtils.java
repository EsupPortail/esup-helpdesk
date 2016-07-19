package net.suberic.crypto;

import java.io.IOException;
import java.util.Enumeration;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;

/**
 * Utilities for encrypting/decrypting messages.
 */
public abstract class EncryptionUtils {

  /**
   * Encrypted message.
   */
  public static int ENCRYPTED = 1;

  /**
   * Signed message.
   */
  public static int SIGNED = 5;

  /**
   * Contains encryption keys.
   */
  public static int ATTACHED_KEYS = 8;

  /**
   * Not encrypted or signed.
   */
  public static int NOT_ENCRYPTED = 10;

  /**
   * Encrypts a MimeMessage.
   * @deprecated Use {@link #encryptMessage(Session, MimeMessage, java.security.Key[])} instead.
   */
  public MimeMessage encryptMessage(Session s, MimeMessage msg, java.security.Key key) 
    throws MessagingException, IOException, java.security.GeneralSecurityException
    {
	  return encryptMessage(s, msg, new java.security.Key[]{key});
    }

  /**
   * Encrypts a MimeMessage.
   */
  public abstract MimeMessage encryptMessage(Session s, MimeMessage msg, java.security.Key[] keys) 
    throws MessagingException, IOException, java.security.GeneralSecurityException;

  /**
   * Decrypts a MimeMessage.
   */
  public abstract javax.mail.internet.MimeMessage decryptMessage(Session s, javax.mail.internet.MimeMessage msg, java.security.Key key) 
    throws MessagingException, IOException, java.security.GeneralSecurityException;

  /**
   * Encrypts a MimeBodyPart;
   * @deprecated Use {@link #encryptPart(MimeBodyPart, java.security.Key[])} instead.
   */
  public MimeBodyPart encryptPart(MimeBodyPart part, java.security.Key key) 
    throws MessagingException, java.security.GeneralSecurityException, IOException
    {
	  return encryptPart(part, new java.security.Key[]{key});
    }

  /**
   * Encrypts a MimeBodyPart;
   */
  public abstract MimeBodyPart encryptPart(MimeBodyPart part, java.security.Key[] keys) 
    throws MessagingException, java.security.GeneralSecurityException, IOException;

  /**
   * Decrypts a MimeBodyPart.
   */
  public abstract MimeBodyPart decryptBodyPart(MimeBodyPart part, java.security.Key key) 
    throws MessagingException, IOException, java.security.GeneralSecurityException;

  /**
   * Decrypts a Multipart.
   */
  public abstract MimeBodyPart decryptMultipart(MimeMultipart mpart, java.security.Key key) 
    throws MessagingException, IOException, java.security.GeneralSecurityException;

  /**
   * Signs a MimeBodyPart.
   */
  public abstract MimeBodyPart signBodyPart(MimeBodyPart p, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException;

  /**
   * Checks the signature on a Part.
   */
  public abstract boolean checkSignature(MimePart p, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException;

  /**
   * Signs a Message.
   */
  public abstract MimeMessage signMessage(Session s, MimeMessage m, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException;

  /**
   * Checks the signature on a Message.
   */
  public abstract boolean checkSignature(MimeMessage m, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException;

  /**
   * Checks the signature on a Multipart.
   */
  public abstract boolean checkSignature(MimeMultipart m, java.security.Key key)
    throws MessagingException, IOException, java.security.GeneralSecurityException;

  /**
   * Returns the signed body part.
   */
  public abstract MimeBodyPart getSignedContent(MimePart mp)
    throws MessagingException, IOException;
 
  /**
   * Creates a public key body part.
   */
  public abstract MimeBodyPart createPublicKeyPart(java.security.Key[] keys)
    throws MessagingException, java.io.IOException, java.security.GeneralSecurityException;


  /**
   * Extracts keys from a public key body part.
   *
   */
  public abstract java.security.Key[] extractKeys(MimeBodyPart mbp)
    throws MessagingException, IOException, java.security.GeneralSecurityException ;

  /**
   * Extracts keys from a public key body part.
   *
   */
  public abstract java.security.Key[] extractKeys(MimeMessage m)
    throws MessagingException, IOException, java.security.GeneralSecurityException ;

  /**
   * Creates an empty EncryptionKeyManager that's appropriate for this
   * Encryption provider.
   */
  public abstract EncryptionKeyManager createKeyManager();

  /**
   * Returns the encryption status of this MimePart:
   * ENCRYPTED, SIGNED, ATTACHED_KEYS, or NOT_ENCRYPTED.
   */
  public abstract int getEncryptionStatus(MimePart m)
    throws MessagingException;

  /**
   * Returns the encryption type that these utils are implementing (PGP 
   * or SMIME).
   */
  public abstract String getType();

  /**
   * Identifies the encryption type of the given MimePart.  Returns 
   * true if this MimePart has an encryption type (encrypted, signed, 
   * etc.) that's handled by this EncryptionUtils.
   */
  public abstract String checkEncryptionType(MimePart mp) throws MessagingException;

}
