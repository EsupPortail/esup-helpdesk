/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.smtp.signing;

import java.io.FileInputStream;
import java.io.Serializable;
import java.security.Key;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import net.suberic.crypto.EncryptionKeyManager;
import net.suberic.crypto.EncryptionManager;
import net.suberic.crypto.EncryptionUtils;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.smtp.SmtpServer;
import org.springframework.beans.factory.InitializingBean;

/**
 * Implementation of message signing with S/MIME
 */
public class SMIMEMessageSignerImpl implements InitializingBean, Serializable, MessageSigner {
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7251290723396763661L;

	/**
	 * The logger.
	 */
	private final Logger logger = new LoggerImpl(SmtpServer.class);

	/**
	 * Should signing be used?
	 */
	private boolean enabled = false;

	/**
	 * Path to file with personal certificate for signing.
	 */
	private String certificateFile;

	/**
	 * The password for personal certificate.
	 */
	private String certificatePassword;

	/**
	 * The alias for signing certificate.
	 */
	private String certificateAlias;

	/**
	 * The imported signing certificate.
	 */
	private Key privateKey;

	/**
	 * The encryption subsystem.
	 */
	private EncryptionUtils encUtils;

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
	public void afterPropertiesSet() {
        if (isEnabled()) {
            if (certificateFile == null) {
                logger.warn(getClass() + ": certificate file not set, disabling signing.");
                setEnabled(false);
            }
            if (certificateAlias == null) {
                logger.warn(getClass() + ": certificate alias not set, disabling signing.");
                setEnabled(false);
            }
            if (certificatePassword == null) {
                logger.warn(getClass() + ": certificate password not set.");
            }
            try {
                openCertificate();
            } catch(Exception ex) {
                logger.warn(getClass() + ": error reading certificate, disabling signing.", ex);
                setEnabled(false);
                System.exit(1);
            }
        }
    }

    /**
     * @see org.esupportail.commons.services.smtp.signing.MessageSigner#sign(javax.mail.Session, javax.mail.internet.MimeMessage)
     */
    @Override
	public MimeMessage sign(final Session session, final MimeMessage mimeMessage) {
        if (isEnabled()) {
            MimeMessage signedMessage;
            try {
                signedMessage = encUtils.signMessage(session, mimeMessage, privateKey);
                return signedMessage;
            } catch (Exception ex) {
                logger.error(getClass() + ": error signing message, sending unsigned.", ex);
            }
        }
        return mimeMessage;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the certificateFile
     */
    public String getCertificateFile() {
        return certificateFile;
    }

    /**
     * @param certificateFile the certificateFile to set
     */
    public void setCertificateFile(final String certificateFile) {
        this.certificateFile = certificateFile;
    }

    /**
     * @param certificatePassword the certificatePassword to set
     */
    public void setCertificatePassword(final String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }

    /**
     * @return the certificateAlias
     */
    public String getCertificateAlias() {
        return certificateAlias;
    }

    /**
     * @param certificateAlias the certificateAlias to set
     */
    public void setCertificateAlias(final String certificateAlias) {
        this.certificateAlias = certificateAlias;
    }

    /**
     * Opens the signing certificate.
     * @throws Exception Thrown on error reading or constructing certificate
     */
    private void openCertificate() throws Exception {
        // Getting of the S/MIME EncryptionUtilities.
        encUtils = EncryptionManager.getEncryptionUtils(EncryptionManager.SMIME);

        // Loading of the S/MIME keystore from the file (stored as resource).
        char[] keystorePass = certificatePassword.toCharArray();
        EncryptionKeyManager encKeyManager = encUtils.createKeyManager();
        encKeyManager.loadPrivateKeystore(new FileInputStream(certificateFile), keystorePass);

        // Getting of the S/MIME private key for signing.
        privateKey = encKeyManager.getPrivateKey(certificateAlias, keystorePass);
    }

}
