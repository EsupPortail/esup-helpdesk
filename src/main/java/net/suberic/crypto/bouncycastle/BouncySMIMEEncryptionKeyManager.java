package net.suberic.crypto.bouncycastle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.suberic.crypto.EncryptionKeyManager;

/**
 * This manages a set of Encryption keys for use with PGP or S/MIME.
 */
public class BouncySMIMEEncryptionKeyManager implements EncryptionKeyManager {

  KeyStore publicKeyStore = null;
  KeyStore privateKeyStore = null;

  
  /**
   * Creates a new BouncySMIMEEncryptionKeyManager.
   */
  public BouncySMIMEEncryptionKeyManager() {

  }
  
  /**
   * Creates the keystores.
   */
  public void initKeyStores() throws KeyStoreException, NoSuchProviderException {
    if (publicKeyStore == null){
      //publicKeyStore = KeyStore.getInstance("PKCS12", "BC");
      publicKeyStore = KeyStore.getInstance("PKCS12");
    }

    if (privateKeyStore == null){
      //privateKeyStore = KeyStore.getInstance("PKCS12", "BC");
    	privateKeyStore = KeyStore.getInstance("PKCS12");
    }
  }

  /**
   * Loads this KeyStore from the given input stream.
   *
   * <p>If a password is given, it is used to check the integrity of the
   * keystore data. Otherwise, the integrity of the keystore is not checked.
   *
   * <p>In order to create an empty keystore, or if the keystore cannot
   * be initialized from a stream (e.g., because it is stored on a hardware
   * token device), you pass <code>null</code>
   * as the <code>stream</code> argument.
   *
   * <p> Note that if this KeyStore has already been loaded, it is
   * reinitialized and loaded again from the given input stream.
   *
   * @param stream the input stream from which the keystore is loaded, or
   * null if an empty keystore is to be created.
   * @param password the (optional) password used to check the integrity of
   * the keystore.
   *
   * @exception IOException if there is an I/O or format problem with the
   * keystore data
   * @exception NoSuchAlgorithmException if the algorithm used to check
   * the integrity of the keystore cannot be found
   */
  public void loadPublicKeystore(InputStream stream, char[] password)
    throws IOException, java.security.NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, java.security.cert.CertificateException {

    if (publicKeyStore == null)
      initKeyStores();

    publicKeyStore.load(stream, password);

  }
  
  /*
   * Loads this KeyStore from the given input stream.
   *
   * <p>If a password is given, it is used to check the integrity of the
   * keystore data. Otherwise, the integrity of the keystore is not checked.
   *
   * <p>In order to create an empty keystore, or if the keystore cannot
   * be initialized from a stream (e.g., because it is stored on a hardware
   * token device), you pass <code>null</code>
   * as the <code>stream</code> argument.
   *
   * <p> Note that if this KeyStore has already been loaded, it is
   * reinitialized and loaded again from the given input stream.
   *
   * @param stream the input stream from which the keystore is loaded, or
   * null if an empty keystore is to be created.
   * @param password the (optional) password used to check the integrity of
   * the keystore.
   *
   * @exception IOException if there is an I/O or format problem with the
   * keystore data
   * @exception NoSuchAlgorithmException if the algorithm used to check
   * the integrity of the keystore cannot be found
   */
  public void loadPrivateKeystore(InputStream stream, char[] password)
    throws IOException, java.security.NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, java.security.cert.CertificateException {
    
    if (privateKeyStore == null)
      initKeyStores();

    privateKeyStore.load(stream, password);
    
  }
  
  /**
   * Stores this keystore to the given output stream, and protects its
   * integrity with the given password.
   *
   * @param stream the output stream to which this keystore is written.
   * @param password the password to generate the keystore integrity check
   *
   * @exception KeyStoreException if the keystore has not been initialized
   * (loaded).
   * @exception IOException if there was an I/O problem with data
   * @exception NoSuchAlgorithmException if the appropriate data integrity
   * algorithm could not be found
   */
  public void storePublicKeystore(OutputStream stream, char[] password)
    throws IOException, java.security.NoSuchAlgorithmException, KeyStoreException, java.security.cert.CertificateException {

    if (publicKeyStore != null) 
      publicKeyStore.store(stream, password);
    else
      throw new KeyStoreException("KeyStore not initialized.");
  }
  
  /**
   * Stores this keystore to the given output stream, and protects its
   * integrity with the given password.
   *
   * @param stream the output stream to which this keystore is written.
   * @param password the password to generate the keystore integrity check
   *
   * @exception KeyStoreException if the keystore has not been initialized
   * (loaded).
   * @exception IOException if there was an I/O problem with data
   * @exception NoSuchAlgorithmException if the appropriate data integrity
   * algorithm could not be found
   */
  public void storePrivateKeystore(OutputStream stream, char[] password)
    throws IOException, java.security.NoSuchAlgorithmException, KeyStoreException, java.security.cert.CertificateException {

    if (privateKeyStore != null)
      privateKeyStore.store(stream, password);
    else
      throw new KeyStoreException("KeyStore not initialized.");
  }
  
  /**
   * Retrieves the number of entries in this keystore.
   *
   * @return the number of entries in this keystore
   *
   * @exception KeyStoreException if the keystore has not been initialized
   * (loaded).
   */
  public int size()
    throws KeyStoreException {
    if (publicKeyStore != null) 
      return publicKeyStore.size() + privateKeyStore.size();
    else
      throw new KeyStoreException("KeyStore not initialized.");
  }
  
  /**
   * Returns the key associated with the given alias, using the given
   * password to recover it.
   *
   * @param alias the alias name
   * @param password the password for recovering the key
   *
   * @return the requested key, or null if the given alias does not exist
   * or does not identify a <i>key entry</i>.
   *
   * @exception KeyStoreException if the keystore has not been initialized
   * (loaded).
   * @exception NoSuchAlgorithmException if the algorithm for recovering the
   * key cannot be found
   * @exception UnrecoverableKeyException if the key cannot be recovered
   * (e.g., the given password is wrong).
   */
  public java.security.Key getPublicKey(String alias)
    throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
    if (publicKeyStore != null) {
      if (publicKeyStore.isCertificateEntry(alias) || publicKeyStore.isKeyEntry(alias)) {
		BouncySMIMEEncryptionKey ek = new BouncySMIMEEncryptionKey();
		Certificate[] chain;
		
		if(publicKeyStore.isKeyEntry(alias)){
			chain = publicKeyStore.getCertificateChain(alias);
		}else{
			chain = new Certificate[1];
			chain[0] = publicKeyStore.getCertificate(alias);
		}
		
		ek.setCertificateChain(chain);
		ek.setDisplayAlias(alias);
		return ek;
      }
      
      return null;
    } else
      throw new KeyStoreException("KeyStore not initialized.");

  }

  /**
   * Returns the key associated with the given alias, using the given
   * password to recover it.
   *
   * @param alias the alias name
   * @param password the password for recovering the key
   *
   * @return the requested key, or null if the given alias does not exist
   * or does not identify a <i>key entry</i>.
   *
   * @exception KeyStoreException if the keystore has not been initialized
   * (loaded).
   * @exception NoSuchAlgorithmException if the algorithm for recovering the
   * key cannot be found
   * @exception UnrecoverableKeyException if the key cannot be recovered
   * (e.g., the given password is wrong).
   */
  public java.security.Key getPrivateKey(String alias, char[] password) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
    if (privateKeyStore != null) {
      if (privateKeyStore.isKeyEntry(alias)) {
	BouncySMIMEEncryptionKey ek = new BouncySMIMEEncryptionKey();
	Certificate[] chain = privateKeyStore.getCertificateChain(alias);
	ek.setCertificateChain(chain);
	PrivateKey privKey = (PrivateKey)privateKeyStore.getKey(alias, password);
	ek.setDisplayAlias(alias);
	ek.setKey(privKey);
	return ek;
      }
      
      return null;
    } else
      throw new KeyStoreException("KeyStore not initialized.");

  }  
  
  /**
   * Assigns the given key to the given alias, protecting it with the given
   * password.
   *
   * <p>If the given key is of type <code>java.security.PrivateKey</code>,
   * it must be accompanied by a certificate chain certifying the
   * corresponding public key.
   *
   * <p>If the given alias already exists, the keystore information
   * associated with it is overridden by the given key (and possibly
   * certificate chain).
   *
   * @param alias the alias name
   * @param key the key to be associated with the alias
   * @param password the password to protect the key
   * @param chain the certificate chain for the corresponding public
   * key (only required if the given key is of type
   * <code>java.security.PrivateKey</code>).
   *
   * @exception KeyStoreException if the keystore has not been initialized
   * (loaded), the given key cannot be protected, or this operation fails
   * for some other reason
   */
  public void setPublicKeyEntry(String alias, java.security.Key key)
    throws KeyStoreException {

  }
  
  /**
   * Assigns the given key to the given alias, protecting it with the given
   * password.
   *
   * <p>If the given key is of type <code>java.security.PrivateKey</code>,
   * it must be accompanied by a certificate chain certifying the
   * corresponding public key.
   *
   * <p>If the given alias already exists, the keystore information
   * associated with it is overridden by the given key (and possibly
   * certificate chain).
   *
   * @param alias the alias name
   * @param key the key to be associated with the alias
   * @param password the password to protect the key
   * @param chain the certificate chain for the corresponding public
   * key (only required if the given key is of type
   * <code>java.security.PrivateKey</code>).
   *
   * @exception KeyStoreException if the keystore has not been initialized
   * (loaded), the given key cannot be protected, or this operation fails
   * for some other reason
   */
  public void setPrivateKeyEntry(String alias, java.security.Key key, char[] password)
    throws KeyStoreException {

  }
  
  /**
   * Deletes the entry identified by the given alias from this keystore.
   *
   * @param alias the alias name
   *
   * @exception KeyStoreException if the keystore has not been initialized,
   * or if the entry cannot be removed.
   */
  public void deletePublicKeyEntry(String alias)
    throws KeyStoreException {

  }
  
  /**
   * Deletes the entry identified by the given alias from this keystore.
   *
   * @param alias the alias name
   *
   * @exception KeyStoreException if the keystore has not been initialized,
   * or if the entry cannot be removed.
   */
  public void deletePrivateKeyEntry(String alias, char[] password)
    throws KeyStoreException {

  }
  
  /**
   * Lists all the alias names of this keystore.
   *
   * @param forSignature indicates whether the private keys are for signature
   * @return set of the alias names
   *
   * @exception KeyStoreException if the keystore has not been initialized
   * (loaded).
   */
  public Set publicKeyAliases(boolean forSignature)
    throws KeyStoreException {
	  //Liao: consider forSignature
    if (publicKeyStore != null) {
      Enumeration aliasEnum = publicKeyStore.aliases();
      HashSet returnValue = new HashSet();
      while (aliasEnum.hasMoreElements()) {
	String alias = (String) aliasEnum.nextElement();
	//if (pkcsKeyStore.isCertificateEntry(alias))
	returnValue.add(alias);
      }
      return returnValue;
    } else {
      throw new KeyStoreException("KeyStore not initialized.");
    }
  }

  /**
   * Lists all the alias names of this keystore.
   * @param forSignature indicates whether the private keys are for signature
   * @return set of the alias names
   *
   * @exception KeyStoreException if the keystore has not been initialized
   * (loaded).
   */
  public Set privateKeyAliases(boolean forSignature)
    throws KeyStoreException {
	  //Liao: Consider forSignature
	    if (privateKeyStore != null) {
	        Enumeration aliasEnum = privateKeyStore.aliases();
	        HashSet returnValue = new HashSet();
	        while (aliasEnum.hasMoreElements()) {
			  	String alias = (String) aliasEnum.nextElement();
			  	if (privateKeyStore.isKeyEntry(alias))
			  	  returnValue.add(alias);
	        }
	        
	        return returnValue;
	      } else {
	        throw new KeyStoreException("KeyStore not initialized.");
	      }
  }
  
  
  /**
   * Checks if the given alias exists in this keystore.
   *
   * @param alias the alias name
   *
   * @return true if the alias exists, false otherwise
   *
   * @exception KeyStoreException if the keystore has not been initialized
   * (loaded).
   */
  public boolean containsPublicKeyAlias(String alias)
    throws KeyStoreException {
    if (publicKeyStore != null)
      return publicKeyStore.isCertificateEntry(alias);
    else
      throw new KeyStoreException("KeyStore not initialized.");
  }

  /**
   * Checks if the given alias exists in this keystore.
   *
   * @param alias the alias name
   *
   * @return true if the alias exists, false otherwise
   *
   * @exception KeyStoreException if the keystore has not been initialized
   * (loaded).
   */
  public boolean containsPrivateKeyAlias(String alias)
    throws KeyStoreException {
    if (privateKeyStore != null) 
      return privateKeyStore.isKeyEntry(alias);
    else
      throw new KeyStoreException("KeyStore not initialized.");

  }
  /**
   * @see net.suberic.crypto.EncryptionKeyManager#privateKeyAliases()
   * @deprecated use {@link #privateKeyAliases(boolean)} instead.
   */
  public Set privateKeyAliases() throws KeyStoreException {
	Set aliases = privateKeyAliases(true);
	Set aliases2 = privateKeyAliases(false);
	
	Iterator it = aliases2.iterator();
	while(it.hasNext()){
		Object alias = it.next();
		if(!aliases.contains(alias)){
			aliases.add(alias);
		}
	}
	return aliases;
  }

  /**
   * @see net.suberic.crypto.EncryptionKeyManager#publicKeyAliases()
   * @deprecated use {@link #publicKeyAliases(boolean)} instead
   */
  public Set publicKeyAliases() throws KeyStoreException {
	Set aliases = publicKeyAliases(true);
	Set aliases2 = publicKeyAliases(false);
	
	Iterator it = aliases2.iterator();
	while(it.hasNext()){
		Object alias = it.next();
		if(!aliases.contains(alias)){
			aliases.add(alias);
		}
	}
	return aliases;
  }
}
