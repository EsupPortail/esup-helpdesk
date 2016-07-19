package net.suberic.crypto;

import java.security.Key;
import java.security.NoSuchProviderException;

/**
 * An extension to java.security.Key that knows about the EncryptionUtils
 * that created it, as well as has some display info associated.
 */
public interface EncryptionKey extends Key {

  /**
   * Returns the EncryptionUtils that created this Key.
   */
  public EncryptionUtils getEncryptionUtils()  throws NoSuchProviderException;

  /**
   * Returns the type (S/MIME, PGP) of this Key.
   */
  public String getType();

  /**
   * Returns the display information for this Key.
   */
  public String getDisplayAlias();

  /**
   * Returns the email address(es) associated with this key.
   */
  public String[] getAssociatedAddresses();
  
  /**
   * @return Whether the key is for signature
   */
  public boolean isForSignature();
  
  /**
   * @return Whether the key is for encryption
   */
  public boolean isForEncryption();
  
  /**
   * Set the pass phrase to retrieve the key
   */
  public void setPassphrase(char[] passphrase);

}
