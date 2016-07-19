package net.suberic.crypto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.NoSuchProviderException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.internet.MimePart;

/**
 * Manager for the JavaMail Encryption package.
 */
public class EncryptionManager {

  // the default capitalization of 'S/MIME'
  public static String SMIME = "S/MIME";

  // the default capitalization of 'PGP'
  public static String PGP = "PGP";

  static HashMap providerMap = null;

  static {
    loadProviders();
  }

  /**
   * Loads the configured providers.
   */
  static void loadProviders() {
    providerMap = new HashMap();

    try {
      ClassLoader cl = new EncryptionManager().getClass().getClassLoader();
/*
      Enumeration providersEnum = cl.getResources("META-INF/javamail-crypto.providers");
      while (providersEnum.hasMoreElements()) {
        java.net.URL url = (java.net.URL) providersEnum.nextElement();
        java.io.InputStream stream = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String nextLine = reader.readLine();
        while (nextLine != null) {
          loadProvider(nextLine);
          nextLine = reader.readLine();
        }

        reader.close();
      }
*/
      loadProvider("protocol=S/MIME; class=net.suberic.crypto.bouncycastle.SMIMEEncryptionUtils");
    } catch (Exception e) {
      // FIXME ignore for now.
      System.err.println("error loading provider info:  " + e);
      e.printStackTrace();
    }

  }

  /**
   * Loads a provider definition from a String.
   */
  static void loadProvider(String line) {
    String protocol = null;
    String className = null;
    StringTokenizer st = new StringTokenizer(line, ";");
    while (st.hasMoreTokens()) {
      String currentToken = st.nextToken().trim();
      if (currentToken.startsWith("protocol="))
        protocol = currentToken.substring(9);
      else if (currentToken.startsWith("class="))
        className = currentToken.substring(6);
    }

    if (protocol != null && className != null) {
      providerMap.put(protocol, className);
    }
  }

  /**
   * Returns an EncryptionUtils for the given encryption method, usually
   * 'S/MIME' or 'PGP'.
   */
  public static EncryptionUtils getEncryptionUtils(String type)
    throws NoSuchProviderException {
    String className = (String) providerMap.get(type);
    if (className != null) {
      try {
        Class utilsClass = Class.forName(className);
        EncryptionUtils returnValue = (EncryptionUtils) utilsClass.newInstance();
        return returnValue;
      } catch (Throwable e) {
    	  throw new NoSuchProviderException("Error while loading the provider for " + type + ": " + e.getMessage());
      }
    }

    throw new NoSuchProviderException("No provider configured for " + type);
  }

  /**
   * Returns an EncryptionUtils object for the given MimePart.  Returns null
   * if the part is not a recognized encrypted part.
   */
  public static EncryptionUtils getEncryptionUtils(MimePart mp) throws MessagingException, NoSuchProviderException {
    String encryptionType = checkEncryptionType(mp);

    if (encryptionType != null)
      return getEncryptionUtils(encryptionType);
    else
      return null;
  }

  /**
   * Identifies the encryption type of the given MimePart.  Returns
   * null if no encryption, or the encryption string if the message is
   * encrypted or signed.
   */
  public static String checkEncryptionType(MimePart mp) throws MessagingException {

    Set providerSet = providerMap.keySet();
    Iterator iter = providerSet.iterator();
    String match = null;
    while (match == null && iter.hasNext()) {
      String type = (String) iter.next();
      try {
        EncryptionUtils utils = getEncryptionUtils(type);
        if (utils.checkEncryptionType(mp) != null) {
          match = type;
        }
      } catch (Exception e) {
        // ignore exceptions here; if we can't access the provider, we
        // can't identify messages that match its encryption type.
      }
    }

    return match;

  }

  /**
   * Main method for command-line usage and testing.
   */
/*  public static void main(String[] argv) {
    try {
      if (argv.length != 6) {
        printUsage();
        System.exit(-1);
      }

      String type = argv[0];
      String action = argv[1];
      String fileName = argv[2];
      String keyStore = argv[3];
      String password = argv[4];
      String keyName = argv[5];

      EncryptionUtils utils = getEncryptionUtils(type);
      EncryptionKeyManager mgr = utils.createKeyManager();
      FileInputStream fis = new FileInputStream(new File(keyStore));
      char[] passwordChars = password.toCharArray();

      Session mailSession = Session.getDefaultInstance(System.getProperties());

      // create the message.

      MimeMessage msg = new MimeMessage(mailSession, new FileInputStream(new File(fileName)));

      if (action.equals("encrypt")) {
        mgr.loadPublicKeystore(fis, passwordChars);
        Key publicKey = mgr.getPublicKey(keyName);

        if (publicKey == null) {
          Set keyAliases = mgr.publicKeyAliases();
          Iterator iter = keyAliases.iterator();
          System.err.println("unable to find public key with alias '" + keyName + "'.  Available aliases:");
          while (iter.hasNext()) {
            System.err.println(iter.next());
          }
          System.exit(-1);
        }

        MimeMessage encryptedMessage = utils.encryptMessage(mailSession, msg, publicKey);
        encryptedMessage.writeTo(System.out);
      } else if (action.equals("sign")) {
        mgr.loadPrivateKeystore(fis, passwordChars);
        Key privateKey = mgr.getPrivateKey(keyName, passwordChars);

        if (privateKey == null) {
          Set keyAliases = mgr.privateKeyAliases();
          Iterator iter = keyAliases.iterator();
          System.err.println("unable to find private key with alias '" + keyName + "'.  Available aliases:");
          while (iter.hasNext()) {
            System.err.println(iter.next());
          }
          System.exit(-1);
        }

        MimeMessage signedMessage = utils.signMessage(mailSession, msg, privateKey, null);
        signedMessage.writeTo(System.out);
      } else if (action.equals("decrypt")) {
        mgr.loadPrivateKeystore(fis, passwordChars);
        Key privateKey = mgr.getPrivateKey(keyName, passwordChars);

        if (privateKey == null) {
          Set keyAliases = mgr.privateKeyAliases();
          Iterator iter = keyAliases.iterator();
          System.err.println("unable to find private key with alias '" + keyName + "'.  Available aliases:");
          while (iter.hasNext()) {
            System.err.println(iter.next());
          }
          System.exit(-1);
        }

        MimeMessage decryptedMessage = utils.decryptMessage(mailSession, msg, privateKey);
        decryptedMessage.writeTo(System.out);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
*/
  /**
   * Usage for main method.
   */
  public static void printUsage() {
    System.err.println("Usage:  java net.suberic.crypto.EncryptionManager [type] [action] [messagefile] [keystore] [password] [keyname]");
  }
}
