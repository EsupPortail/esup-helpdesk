/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.smtp;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.mail.internet.InternetAddress;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.smtp.signing.MessageSigner;
import org.esupportail.commons.utils.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * A simple implementation of SmtpService.
 */
public class SimpleSmtpServiceImpl extends AbstractSmtpService implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1306585947271009432L;

	/**
	 * The default encoding charset.
	 */
	private static final String DEFAULT_CHARSET = "utf-8";
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The smtpServers to use.
	 */
	private List<SmtpServer> servers;

	/**
	 * The 'From' address to use.
	 */
	private InternetAddress fromAddress;
	
	/**
	 * True to intercept all the outgoing emails.
	 */
	private Boolean interceptAll;
	
	/**
	 * The address to which _all_ the emails should be sent (if null, all the
	 * emails are sent normally).
	 */
	private InternetAddress interceptAddress;

	/**
	 * The recipient of the test emails.
	 */
	private InternetAddress testAddress;
	
	/**
	 * The charset used to encode the headers.
	 */
	private String charset;

	/**
	 * The addresses that are never intercepted.
	 */
	private List<String> notInterceptedAddresses;

	/**
	 * The message signer.
	 */
	private MessageSigner messageSigner;

	/**
	 * Constructor.
	 */
	public SimpleSmtpServiceImpl() {
		super();
	}

	/**
	 * Set the default charset.
	 */
	public void setDefaultCharset() {
		setCharset(DEFAULT_CHARSET);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		if (CollectionUtils.isEmpty(servers)) {
			logger.info(getClass() + ": no SMTP server set, '" + SmtpServer.DEFAULT_HOST + ":" 
					+ SmtpServer.DEFAULT_PORT + "' will be used");
			SmtpServer server = new SmtpServer();
			server.setDefaultHost();
			server.setDefaultPort();
			if (this.servers == null) {
				this.servers = new Vector<SmtpServer>();
			}
			this.servers.add(server);
		}
		Assert.notNull(this.fromAddress, 
				"property fromAddress of class " + this.getClass().getName() + " can not be null");
		if (!StringUtils.hasText(charset)) {
			logger.info(getClass() + ": no encoding charset set, '" + DEFAULT_CHARSET + "' will be used");
			setDefaultCharset();
		}
		if (testAddress == null) {
			logger.info(getClass() + ": no testAddress attribute set, target ldap-smtp will not work."); 
		}
		Assert.notNull(this.interceptAll, 
				"property interceptAll of class " + this.getClass().getName() 
				+ " can not be null");
		if (interceptAll) {
			Assert.notNull(this.interceptAddress, 
					"property interceptAddress of class " + this.getClass().getName() 
					+ " can not be null when interceptAll is true");
		}
		if (notInterceptedAddresses == null) {
			notInterceptedAddresses = new ArrayList<String>();
		}
	}

	/**
	 * @return the real recipient of an email.
	 * @param to
	 * @param intercept
	 */
	protected InternetAddress getRealRecipient(final InternetAddress to, final boolean intercept) {
		InternetAddress recipient;
		if (intercept 
				&& interceptAll
				&& !notInterceptedAddresses.contains(to.getAddress().toLowerCase())) {
			try {
				recipient = new InternetAddress(
						interceptAddress.getAddress(),
						interceptAddress.getPersonal() + " (normally sent to " 
						+ to.getAddress() + ")");
			} catch (UnsupportedEncodingException e) {
				throw new SmtpException("could not send mail to '" + to.getAddress() + "'", e);
			}
		} else {
			recipient = to;
		}
		return recipient;
	}	
	
	/**
	 * Send an email.
	 * @param to
	 * @param subject
	 * @param htmlBody
	 * @param textBody
	 * @param files 
	 * @param intercept
	 * @param messageId
	 */
	protected void send(
			final InternetAddress to, 
			final String subject, 
			final String htmlBody, 
			final String textBody,
			final List<File> files,
			final boolean intercept,
			final String messageId) {
		InternetAddress recipient = getRealRecipient(to, intercept);
		SmtpUtils.sendEmail(
				this.servers, this.fromAddress, recipient, subject, 
				htmlBody, textBody, files, this.charset, this.getMessageSigner(), messageId);
	}
	
	/**
	 * Send an email to, cc, bcc.
	 * @param tos
	 * @param ccs
	 * @param bccs
	 * @param subject
	 * @param htmlBody
	 * @param textBody
	 * @param files
	 * @param messageId
	 */
	protected void send(
			final InternetAddress[] tos,
			final InternetAddress[] ccs, 
			final InternetAddress[] bccs,  
			final String subject, 
			final String htmlBody, 
			final String textBody,
			final List<File> files,
			final String messageId) {
	
		SmtpUtils.sendEmailtocc(
				this.servers, this.fromAddress, tos, ccs, bccs,
				subject, htmlBody, textBody, files, this.charset, this.getMessageSigner(), messageId);
	}
	
	
	/**
	 * @see org.esupportail.commons.services.smtp.SmtpService#send(
	 * javax.mail.internet.InternetAddress, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void send(
			final InternetAddress to, 
			final String subject, 
			final String htmlBody, 
			final String textBody,
			final String messageId) {
		send(to, subject, htmlBody, textBody, null, true, messageId);
	}	
	
	
	/**
	 * @see org.esupportail.commons.services.smtp.SmtpService#send(
	 * javax.mail.internet.InternetAddress, java.lang.String, java.lang.String, java.lang.String, java.util.List,
	 *  java.lang.String)
	 */
	@Override
	public void send(
			final InternetAddress to, 
			final String subject, 
			final String htmlBody, 
			final String textBody,
			final List<File> files,
			final String messageId) {
		send(to, subject, htmlBody, textBody, files, true, messageId);
	}	
	
	/**
	 * @see org.esupportail.commons.services.smtp.SmtpService#sendtocc
	 * (javax.mail.internet.InternetAddress[], 
	 * javax.mail.internet.InternetAddress[], 
	 * javax.mail.internet.InternetAddress[], 
	 * java.lang.String, 
	 * java.lang.String, java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public void sendtocc(
			final InternetAddress[] tos,
			final InternetAddress[] ccs, 
			final InternetAddress[] bccs, 
			final String subject, 
			final String htmlBody, 
			final String textBody, 
			final List<File> files,
			final String messageId) {
		send(tos, ccs, bccs, subject, htmlBody, textBody, files, messageId);
	}
	
	/**
	 * @see org.esupportail.commons.services.smtp.SmtpService#sendDoNotIntercept(
	 * javax.mail.internet.InternetAddress, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendDoNotIntercept(
			final InternetAddress to, 
			final String subject, 
			final String htmlBody, 
			final String textBody,
			final String messageId) {
		send(to, subject, htmlBody, textBody, null, false, messageId);
	}
	
	
	
	/**
	 * @see org.esupportail.commons.services.smtp.SmtpService#sendDoNotIntercept(
	 * javax.mail.internet.InternetAddress, java.lang.String, java.lang.String, 
	 * java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public void sendDoNotIntercept(
			final InternetAddress to,
			final String subject,
			final String htmlBody,
			final String textBody,
			final List<File> files,
			final String messageId) {
		send(to, subject, htmlBody, textBody, files, false, messageId);	
	}
	
	/**
	 * @see org.esupportail.commons.services.smtp.AbstractSmtpService#supportsTest()
	 */
	@Override
	public boolean supportsTest() {
		return true;
	}

	/**
	 * @see org.esupportail.commons.services.smtp.AbstractSmtpService#test()
	 */
	@Override
	public void test() {
		if (testAddress == null) {
			logger.error("can not test the SMTP connection when property testAddress is not set, " 
					+ "edit configuration file smtp.xml.");
			return;
		}
		sendDoNotIntercept(testAddress, "SMTP test", "<p>This is a <b>test</b>.</p>", "This is a test.");
	}

	/**
	 * @return The servers.
	 */
	public List<SmtpServer> getServers() {
		return this.servers;
	}
	/**
	 * @param servers The servers to set.
	 */
	public void setServers(
			final List<SmtpServer> servers) {
		this.servers = servers;
	}
	/**
	 * @return The fromAddress.
	 */
	public InternetAddress getFromAddress() {
		return this.fromAddress;
	}
	/**
	 * @param fromAddress The fromAddress to set.
	 */
	public void setFromAddress(
			final InternetAddress fromAddress) {
		this.fromAddress = fromAddress;
	}
	/**
	 * @return The interceptAddress.
	 */
	public InternetAddress getInterceptAddress() {
		return this.interceptAddress;
	}
	/**
	 * @param interceptAddress The interceptAddress to set.
	 */
	public void setInterceptAddress(
			final InternetAddress interceptAddress) {
		this.interceptAddress = interceptAddress;
	}
	/**
	 * @return The charset.
	 */
	public String getCharset() {
		return this.charset;
	}
	/**
	 * @param charset The charset to set.
	 */
	public void setCharset(final String charset) {
		this.charset = charset;
	}

	/**
	 * @return the testAddress
	 */
	public InternetAddress getTestAddress() {
		return testAddress;
	}

	/**
	 * @param testAddress the testAddress to set
	 */
	public void setTestAddress(final InternetAddress testAddress) {
		this.testAddress = testAddress;
	}

	/**
	 * @return the notInterceptedAddresses
	 */
	protected List<String> getNotInterceptedAddresses() {
		return notInterceptedAddresses;
	}

	/**
	 * @param addresses the addresses not to intercept, comma-separated.
	 */
	public void setNotInterceptedAddresses(final String addresses) {
		if (addresses == null) {
			return;
		}
		notInterceptedAddresses = new ArrayList<String>();
		for (String address : addresses.split(",")) {
			notInterceptedAddresses.add(address.toLowerCase());
		}
	}

	/**
	 * @return the interceptAll
	 */
	protected Boolean getInterceptAll() {
		return interceptAll;
	}

	/**
	 * @param interceptAll the interceptAll to set
	 */
	public void setInterceptAll(final Boolean interceptAll) {
		this.interceptAll = interceptAll;
	}

	/**
	 * @return the messageSigner
	 */
	public MessageSigner getMessageSigner() {
		return messageSigner;
	}

	/**
	 * @param messageSigner the MessageSigner to set
	 */
	public void setMessageSigner(MessageSigner messageSigner) {
		this.messageSigner = messageSigner;
	}

}
