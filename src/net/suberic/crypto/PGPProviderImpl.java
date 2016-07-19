package net.suberic.crypto;


import java.io.InputStream;
import java.security.GeneralSecurityException;

import javax.mail.internet.MimeMultipart;

/**
 * Something which decrypts PGP streams.
 */
public interface PGPProviderImpl {

  /**
   * Decrypts a section of text using an EncryptionKey.
   */
  public byte[] decrypt(java.io.InputStream encryptedStream, java.security.Key key) throws java.security.NoSuchAlgorithmException, java.io.IOException, java.security.GeneralSecurityException;


  /**
   * Encrypts a section of text using an {@link EncryptionKey}.
   * @deprecated Use {@link #encrypt(InputStream, java.security.Key[])} instead.
   */
  public byte[] encrypt(java.io.InputStream rawStream, java.security.Key key) throws java.security.NoSuchAlgorithmException, java.io.IOException, GeneralSecurityException;

  /**
   * Encrypts a section of text using {@link EncryptionKey}s.
   */
  public byte[] encrypt(java.io.InputStream rawStream, java.security.Key[] keys) throws java.security.NoSuchAlgorithmException, java.io.IOException, GeneralSecurityException;

  /**
   * Signs a section of text.
   */
  //  public byte[] sign(InputStream rawStream, java.security.Key key)
  //    throws java.security.NoSuchAlgorithmException, java.io.IOException, GeneralSecurityException;


  /**
   * Signs a section of text.
   */
  public byte[] sign(InputStream rawStream, java.security.Key key)
    throws java.security.NoSuchAlgorithmException, java.io.IOException, GeneralSecurityException;

  /**
   * Checks a signature against a section of text.
   */
  /*
  public boolean checkSignature(String pgpSignedMessage, java.security.Key key) throws java.security.NoSuchAlgorithmException, java.io.IOException, GeneralSecurityException;
  */

  /**
   * Checks a signature against a section of text.
   */
  public boolean checkSignature(MimeMultipart mPart, java.security.Key key) throws java.security.NoSuchAlgorithmException, java.io.IOException, GeneralSecurityException;

  /**
   * Extracts public key information.
   */
  public java.security.Key[] extractKeys(InputStream rawStream) throws java.security.GeneralSecurityException, java.io.IOException;

  /**
   * Packages up the public keys in a form to be sent as a public key message.
   */
  public byte[] packageKeys(java.security.Key[] keys) throws java.security.GeneralSecurityException, java.io.IOException;

  /**
   * Returns a KeyStore provider.
   */
  public abstract EncryptionKeyManager createKeyManager();

  /**
   * Returns a KeyStore provider.
   */
  public abstract EncryptionKeyManager createKeyManager(java.io.InputStream inputStream, char[] password) throws java.io.IOException, java.security.KeyStoreException, java.security.NoSuchAlgorithmException, java.security.NoSuchProviderException, java.security.cert.CertificateException;

}
