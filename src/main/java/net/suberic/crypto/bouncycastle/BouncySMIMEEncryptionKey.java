package net.suberic.crypto.bouncycastle;

import java.security.Key;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import net.suberic.crypto.EncryptionKey;
import net.suberic.crypto.EncryptionManager;
import net.suberic.crypto.EncryptionUtils;

/**
 * A wrapper around a Certificate and/or Key used to encrypt, decrypt, 
 * sign, etc. an email message in S/MIME.
 */
public class BouncySMIMEEncryptionKey implements EncryptionKey {
  X509Certificate mCert = null;
  Certificate[] mCertChain = null;
  PrivateKey mKey = null;
  String mDisplayAlias;
  String[] mAssocAddresses;

  public String getAlgorithm() {
    Key key = mKey;
    if (key == null && mCert != null)
      key = mCert.getPublicKey();
    
    if (key !=  null) 
      return key.getAlgorithm();
    else
      return null;
    
  }

  public String getFormat() {
    Key key = mKey;
    if (key == null && mCert != null)
      key = mCert.getPublicKey();
    
    if (key !=  null) 
      return key.getFormat();
    else
      return null;
  }

  public byte[] getEncoded() {
    try {
      return mCert.getEncoded();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Gets the X509Certificate for this Key.
   */
  public X509Certificate getCertificate() {
    if (mCertChain == null)
      return mCert;
    else
      return (X509Certificate) mCertChain[0];
  }

  /**
   * Sets the X509Certificate for this Key.
   */
  public void setCertificate(X509Certificate pCert) {
    mCert = pCert;
    mCertChain = null;
  }

  /**
   * Gets the CertificateChain for this Key.
   */
  public Certificate[] getCertificateChain() {
    return mCertChain;
  }

  /**
   * Sets the CertificateChain for this Key.
   */
  public void setCertificateChain(Certificate[] pCertChain) {
    mCertChain = pCertChain;
  }

  /**
   * Gets the PrivateKey for this Key.
   */
  public PrivateKey getKey() {
    return mKey;
  }

  /**
   * Sets the PrivateKey for this Key.
   */
  public void setKey(PrivateKey pKey) {
    mKey = pKey;
  }


  /**
   * Returns the EncryptionUtils that created this Key.
   */
  public EncryptionUtils getEncryptionUtils()  throws java.security.NoSuchProviderException {
    return EncryptionManager.getEncryptionUtils(EncryptionManager.SMIME);
  }

  /**
   * Returns the type (S/MIME, PGP) of this Key.
   */
  public String getType() {
    return EncryptionManager.SMIME;
  }

  

  /**
   * Returns the display information for this Key.
   */
  public String getDisplayAlias() {
    return mDisplayAlias;
  }

  /**
   * Sets the display information for this Key.
   */
  public void setDisplayAlias(String pDisplayAlias) {
    mDisplayAlias = pDisplayAlias;
  }

  /**
   * Returns the email address(es) associated with this key.
   */
  public String[] getAssociatedAddresses() {
    if (mAssocAddresses == null) {
      java.util.ArrayList addressList = new java.util.ArrayList();
      
      try {
    	X509Certificate cert = getCertificate();

	    if (cert != null) {
	      Principal p = cert.getSubjectDN();
	      if (p != null) {
	       String name = p.getName();
		    // get the email address from this name.
		    java.util.StringTokenizer tok = new java.util.StringTokenizer(name, ",");
		    while (tok.hasMoreTokens()) {
		      String next = tok.nextToken();
		      if (next.startsWith("E=")){
			    addressList.add(next.substring("E=".length()));
			    break;
		      }
		      else if (next.startsWith("EMAILADDRESS=")){
				addressList.add(next.substring("EMAILADDRESS=".length()));
				break;
		      }
		    }
		  }
		}
      } catch (Exception e) {
	
      }
      
      mAssocAddresses = (String[]) addressList.toArray(new String[0]);
    }
    return mAssocAddresses;
  }

public boolean isForEncryption() {
	//FIXME: check the keyusage 
	return true;
}

public boolean isForSignature() {
	//FIXME: check the keyusage
	return true;
}

public void setPassphrase(char[] passphrase) {
  // do nothing
}
}

