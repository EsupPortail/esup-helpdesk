/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.smtp;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.MethodNotSupportedException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.smtp.signing.MessageSigner;
import org.esupportail.commons.utils.BeanUtils;

/**
 * A class that provides utilities to send emails.
 */
public class SmtpUtils {

	/**
	 * The name of the SmtpService bean.
	 */
	private static final String SMTP_SERVICE_BEAN = "smtpService";

	/**
	 * A logger.
	 */
	private static final Logger LOG = new LoggerImpl(SmtpUtils.class);
	
	/**
	 * No instanciation.
	 */
	private SmtpUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Send an email.
	 * @param smtpServers 
	 * @param from InternetAddress
	 * @param to InternetAddress
	 * @param subject String
	 * @param htmlBody String
	 * @param textBody String
	 * @param files 
	 * @param charset String
	 * @throws SmtpException 
	 */
	public static void sendEmail(
			final List<SmtpServer> smtpServers,
			final InternetAddress from, 
			final InternetAddress to, 
			final String subject,
			final String htmlBody,
			final String textBody,
			final List<File> files,
			final String charset) throws SmtpException {
		SmtpUtils.sendEmail(smtpServers, from, to, subject, htmlBody, textBody, files, charset, null, null);
	}
	
	/**
	 * Send an email.
	 * @param smtpServers
	 * @param from InternetAddress
	 * @param to InternetAddress
	 * @param subject String
	 * @param htmlBody String
	 * @param textBody String
	 * @param files
	 * @param charset String
	 * @param messageSigner
	 * @throws SmtpException
	 */
	public static void sendEmail(
			final List<SmtpServer> smtpServers,
			final InternetAddress from,
			final InternetAddress to,
			final String subject,
			final String htmlBody,
			final String textBody,
			final List<File> files,
			final String charset,
			final MessageSigner messageSigner) throws SmtpException {
		SmtpUtils.sendEmail(smtpServers, from, to, subject, htmlBody, textBody, files, charset, messageSigner, null);
	}

	/**
	 * Send an email.
	 * @param smtpServers 
	 * @param from InternetAddress
	 * @param to InternetAddress
	 * @param subject String
	 * @param htmlBody String
	 * @param textBody String
	 * @param files 
	 * @param charset String
	 * @param messageSigner 
	 * @param messageId String - Message-ID to set
	 * @throws SmtpException 
	 */
	public static void sendEmail(
			final List<SmtpServer> smtpServers,
			final InternetAddress from, 
			final InternetAddress to, 
			final String subject,
			final String htmlBody,
			final String textBody,
			final List<File> files,
			final String charset,
			final MessageSigner messageSigner,
			final String messageId) throws SmtpException {
		String encodedSubject;
		try {
			encodedSubject = MimeUtility.encodeText(subject, charset, null);
		} catch (UnsupportedEncodingException e) {
			throw new SmtpException(e);
		}
		if (htmlBody == null && textBody == null) {
			throw new SmtpException("htmlBody and textBody should not be both null");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("preparing an email for '" + to + "'...");
		}
		try {
			for (SmtpServer smtpServer : smtpServers) {
				Properties props = new Properties();
				props.put("mail.smtp.host", smtpServer.getHost());
				props.put("mail.smtp.port", Integer.toString(smtpServer.getPort()));
				Session session = Session.getInstance(props, null);
				MimeMessage message = new MessageIdChangingMimeMessage(session, messageId);
				
				//	if files not null send multipart
				if (files != null) {
					Multipart multipart = addFilesAttach(htmlBody, textBody, files);
					//	 Associate multi-part with message and attach
					message.setContent(multipart);
				} else {
					// fill in the content
					if (htmlBody != null && textBody != null) {
						// Create a multi-part to combine the parts
						Multipart multipart = new MimeMultipart("alternative");
						// Create your text message part
						BodyPart textBodyPart = new MimeBodyPart();
						textBodyPart.setText(textBody);
						multipart.addBodyPart(textBodyPart);
						// Create your html message part
						BodyPart htmlBodyPart = new MimeBodyPart();
						htmlBodyPart.setContent(
								htmlBody, "text/html; charset=\"" + charset + "\"");
						multipart.addBodyPart(htmlBodyPart);
						// Associate multi-part with message
						message.setContent(multipart);
					} else if (htmlBody != null) {
						message.setContent(htmlBody, "text/html; charset=\"" + charset + "\"");
						message.setHeader("Content-Type", "text/html; charset=\"" + charset + "\"");
					} else {
						message.setText(textBody);
					}
				}
				// Fill in header
				message.setFrom(from);
				message.addRecipient(Message.RecipientType.TO, to);
				message.addHeader("Subject", encodedSubject);
				// Sign the message
				if (messageSigner != null) {
					message = messageSigner.sign(session, message);
				}
				// Send message
				MessagingException e1 = null;
				try {
					if (smtpServer.getUser() != null) {
						Transport transport = session.getTransport("smtp");
						transport.connect(
								smtpServer.getHost(), smtpServer.getPort(), 
								smtpServer.getUser(), smtpServer.getPassword());
						message.saveChanges();
						transport.sendMessage(message, message.getAllRecipients());
						transport.close();
					} else {
						Transport.send(message);
					}
					LOG.info("an email has been sent to '" + to + "'.");
					return;
				} catch (AuthenticationFailedException e) {
					e1 = e;
				} catch (MethodNotSupportedException e) {
					e1 = e;
				} catch (NoSuchProviderException e) {
					e1 = e;
				} catch (SendFailedException e) {
					e1 = e;
				} catch (MessagingException e) {
					if (e.getNextException() == null) {
						throw e;
					} else if (e.getNextException() instanceof UnknownHostException) {
						e1 = e;
					} else if (e.getNextException() instanceof ConnectException) {
						e1 = e;
					} else {
						throw e;
					}
				}
				LOG.error("an exception occured while sending the email to '" + to 
						+ "' using SMTP server '" + smtpServer.getHost() + ":" 
						+ smtpServer.getPort() + "'.", e1);
			}			
		} catch (MessagingException e) {
			LOG.error("could not send the email to '" + to + "'", e);
		}
	}
	
	/**
	 * add a message and files in a Multipart.
	 * @param htmlBody
	 * @param textBody
	 * @param files
	 * @return Multipart
	 * @throws MessagingException 
	 */
	private static Multipart addFilesAttach(
			final String htmlBody,
			final String textBody,
			final List<File> files) throws MessagingException {
		
		Multipart multipart;
			// fill in the content
			if (htmlBody != null && textBody != null) {
				// Create a multi-part to combine the parts
				multipart = new MimeMultipart("mixed");
				// Create your text message part
				BodyPart textBodyPart = new MimeBodyPart();
				textBodyPart.setText(textBody);
				multipart.addBodyPart(textBodyPart);
				// Create your html message part
				BodyPart htmlBodyPart = new MimeBodyPart();
				htmlBodyPart.setContent(htmlBody, "text/html");
				multipart.addBodyPart(htmlBodyPart);
				
			} else if (htmlBody != null) {
				multipart = new MimeMultipart("mixed");
				// Create your html message part
				BodyPart htmlBodyPart = new MimeBodyPart();
				htmlBodyPart.setContent(htmlBody, "text/html");
				multipart.addBodyPart(htmlBodyPart);
				
			} else {
				multipart = new MimeMultipart("mixed");
				//Create your text message part
				BodyPart textBodyPart = new MimeBodyPart();
				textBodyPart.setText(textBody);
				multipart.addBodyPart(textBodyPart);
			}
			
			BodyPart fileBodyPart;
			for (File file : files) {
				fileBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(file);
				fileBodyPart.setDataHandler(new DataHandler(source));
				fileBodyPart.setFileName(file.getName());
				multipart.addBodyPart(fileBodyPart);
			}
			
			return multipart;
	}
	
	/**
	 * @return the SMTP service.
	 */
	public static SmtpService createSmtpService() {
		return (SmtpService) BeanUtils.getBean(SMTP_SERVICE_BEAN);
	}

	/**
	 * @param smtpServers
	 * @param from
	 * @param tos
	 * @param ccs
	 * @param bccs
	 * @param subject
	 * @param htmlBody
	 * @param textBody
	 * @param files
	 * @param charset
	 * @throws SmtpException
	 */
	public static void sendEmailtocc(
	           final List<SmtpServer> smtpServers,
	           final InternetAddress from,
	           final InternetAddress [] tos,
	           final InternetAddress [] ccs,
	           final InternetAddress [] bccs,
	           final String subject,
	           final String htmlBody,
	           final String textBody,
	           final List<File> files,
	           final String charset)throws SmtpException {
		SmtpUtils.sendEmailtocc(
				smtpServers, from, tos, ccs, bccs,
				subject, htmlBody, textBody, files, charset, null, null);
	}

	/**
	 * @param smtpServers
	 * @param from
	 * @param tos
	 * @param ccs
	 * @param bccs
	 * @param subject
	 * @param htmlBody
	 * @param textBody
	 * @param files
	 * @param charset
	 * @param messageSigner 
	 * @throws SmtpException
	 */
	public static void sendEmailtocc(
	           final List<SmtpServer> smtpServers,
	           final InternetAddress from,
	           final InternetAddress [] tos,
	           final InternetAddress [] ccs,
	           final InternetAddress [] bccs,
	           final String subject,
	           final String htmlBody,
	           final String textBody,
	           final List<File> files,
	           final String charset,
	           final MessageSigner messageSigner)throws SmtpException {
		    SmtpUtils.sendEmailtocc(
				smtpServers, from, tos, ccs, bccs,
				subject, htmlBody, textBody, files, charset, messageSigner, null);
	}

	/**
	 * @param smtpServers
	 * @param from
	 * @param tos
	 * @param ccs
	 * @param bccs
	 * @param subject
	 * @param htmlBody
	 * @param textBody
	 * @param files
	 * @param charset
	 * @param messageSigner 
	 * @param messageId
	 * @throws SmtpException
	 */
	public static void sendEmailtocc(
	           final List<SmtpServer> smtpServers,
	           final InternetAddress from,
	           final InternetAddress [] tos,
	           final InternetAddress [] ccs,
	           final InternetAddress [] bccs,
	           final String subject,
	           final String htmlBody,
	           final String textBody,
	           final List<File> files,
	           final String charset,
	           final MessageSigner messageSigner,
	           final String messageId)throws SmtpException {
		String encodedSubject;
		try {
			encodedSubject = MimeUtility.encodeText(subject, charset, null);
		} catch (UnsupportedEncodingException e) {
			throw new SmtpException(e);
		}
		if (htmlBody == null && textBody == null) {
			throw new SmtpException("htmlBody and textBody should not be both null");
		}
		if (LOG.isDebugEnabled()) {
			for (int i = 0; i < tos.length; i++ ) {
				InternetAddress iAdr = tos[i];
				LOG.debug("preparing an email for to '" + iAdr.getAddress() + "'...");
			}
		}
		try {
			for (SmtpServer smtpServer : smtpServers) {
				Properties props = new Properties();
				props.put("mail.smtp.host", smtpServer.getHost());
				props.put("mail.smtp.port", Integer.toString(smtpServer.getPort()));
				Session session = Session.getInstance(props, null);
				MimeMessage message = new MessageIdChangingMimeMessage(session, messageId);
				
				//	if files not null send multipart
				if (files != null) {
					Multipart multipart = addFilesAttach(htmlBody, textBody, files);
					//	 Associate multi-part with message and attach
					message.setContent(multipart);
				} else {
					// fill in the content
					if (htmlBody != null && textBody != null) {
						// Create a multi-part to combine the parts
						Multipart multipart = new MimeMultipart("alternative");
						// Create your text message part
						BodyPart textBodyPart = new MimeBodyPart();
						textBodyPart.setText(textBody);
						multipart.addBodyPart(textBodyPart);
						// Create your html message part
						BodyPart htmlBodyPart = new MimeBodyPart();
						htmlBodyPart.setContent(htmlBody, "text/html");
						multipart.addBodyPart(htmlBodyPart);
						// Associate multi-part with message
						message.setContent(multipart);
					} else if (htmlBody != null) {
						message.setContent(htmlBody, "text/html");
						message.setHeader("Content-Type", "text/html; charset=\"" + charset + "\"");
					} else {
						message.setText(textBody);
					}
				}
				// Fill in header
				message.setFrom(from);
				// direct recipients
				message.addRecipients(Message.RecipientType.TO, tos);
				// carbon copy recipients
				if (ccs != null) {
					message.addRecipients(Message.RecipientType.CC, ccs);
				}
				// blind carbon copy recipients
				if (bccs != null) {
					message.addRecipients(Message.RecipientType.BCC, bccs);
				}
				message.addHeader("Subject", encodedSubject);
				// Sign the message
				if (messageSigner != null) {
					message = messageSigner.sign(session, message);
				}
				// Send message
				MessagingException e1 = null;
				try {
					if (smtpServer.getUser() != null) {
						Transport transport = session.getTransport("smtp");
						transport.connect(
								smtpServer.getHost(), smtpServer.getPort(), 
								smtpServer.getUser(), smtpServer.getPassword());
						message.saveChanges();
						transport.sendMessage(message, message.getAllRecipients());
						transport.close();
					} else {
						Transport.send(message);
					}
					for (int i = 0; i < tos.length; i++ ) {
						InternetAddress iAdr = tos[i];
						LOG.info("an email has been sent to '" + iAdr.getAddress() + "'...");
					}
					return;
				} catch (AuthenticationFailedException e) {
					e1 = e;
				} catch (MethodNotSupportedException e) {
					e1 = e;
				} catch (NoSuchProviderException e) {
					e1 = e;
				} catch (SendFailedException e) {
					e1 = e;
				} catch (MessagingException e) {
					if (e.getNextException() == null) {
						throw e;
					} else if (e.getNextException() instanceof UnknownHostException) {
						e1 = e;
					} else if (e.getNextException() instanceof ConnectException) {
						e1 = e;
					} else {
						throw e;
					}
				}
				LOG.error("an exception occured while sending the email to '" + tos.toString() 
						+ "' using SMTP server '" + smtpServer.getHost() + ":" 
						+ smtpServer.getPort() + "'.", e1);
			}			
		} catch (MessagingException e) {
			LOG.error("could not send the email to '" + tos.toString() + "'", e);
		}
	} 
}
