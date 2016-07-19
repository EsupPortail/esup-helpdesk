/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed.imap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.io.IOUtils;
import org.apache.turbine.services.mimetype.util.MimeTypeMap;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.ActionScope;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.FileInfo;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.services.feed.ErrorHolder;
import org.esupportail.helpdesk.services.feed.imap.spam.SpamFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.sun.mail.imap.IMAPBodyPart;

/**
 * A basic TicketMessageReader implementation.
 */
public class TicketMessageReaderImpl implements InitializingBean, TicketMessageReader {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7866074442898790724L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The domain service.
	 */
	private DomainService domainService;

	/**
	 * The i18n service.
	 */
	private I18nService i18nService;

	/**
	 * Constructor.
	 */
	public TicketMessageReaderImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(domainService,
				"property domainService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(i18nService,
				"property i18nService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @return the recipients of a message.
	 * @param message
	 * @param errorHolder
	 */
	protected Set<Address> extractRecipients(
			final Message message,
			final ErrorHolder errorHolder) {
		if (logger.isDebugEnabled()) {
			logger.debug("extractRecipients(" + message + ")");
		}
		Set<Address> recipients = new HashSet<Address>();
		try {
			Address [] addresses;
			addresses = message.getRecipients(Message.RecipientType.TO);
			if (addresses != null) {
				for (Address address : addresses) {
					Address decodedAddress = address;
					if (address instanceof InternetAddress) {
						InternetAddress iAddress = (InternetAddress) address;
						try {
							decodedAddress = new InternetAddress(
									iAddress.getAddress(),
									decodeText(iAddress.getPersonal()));
						} catch (UnsupportedEncodingException e) {
							// keep as is
						}
					}
					errorHolder.addInfo("To: " + decodedAddress);
					recipients.add(decodedAddress);
				}
			}
			addresses = message.getRecipients(Message.RecipientType.CC);
			if (addresses != null) {
				for (Address address : addresses) {
					Address decodedAddress = address;
					if (address instanceof InternetAddress) {
						InternetAddress iAddress = (InternetAddress) address;
						try {
							decodedAddress = new InternetAddress(
									iAddress.getAddress(),
									decodeText(iAddress.getPersonal()));
						} catch (UnsupportedEncodingException e) {
							// keep as is
						}
					}
					errorHolder.addInfo("Cc: " + decodedAddress);
					recipients.add(decodedAddress);
				}
			}
		} catch (MessagingException e) {
			errorHolder.addError("could not get the recipients: " + e.getMessage());
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
		}
		return recipients;
	}

	/**
	 * @return the sender of a message.
	 * @param message
	 * @param errorHolder
	 */
	protected User extractSender(
			final Message message,
			final ErrorHolder errorHolder) {
		if (logger.isDebugEnabled()) {
			logger.debug("extractSender(" + message + ")");
		}
		Address [] fromAddresses;
		try {
			fromAddresses = message.getFrom();
		} catch (MessagingException e) {
			errorHolder.addError("could not get the From header: " + e.getMessage());
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			return null;
		}
		if (fromAddresses == null || fromAddresses.length == 0) {
			errorHolder.addError("no sender found.");
			return null;
		}
		Address senderAddress = fromAddresses[0];
		if (logger.isDebugEnabled()) {
			logger.debug("From: " + senderAddress);
		}
		InternetAddress internetAddress;
		try {
			internetAddress = new InternetAddress(senderAddress.toString());
		} catch (AddressException e) {
			errorHolder.addError("ill-formed sender [" + senderAddress + "]: " + e.getMessage());
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			return null;
		}
		String email = internetAddress.getAddress();
		String personal = internetAddress.getPersonal();
		User sender = getDomainService().getUserStore().getUserWithEmail(email);
		if (sender == null) {
			errorHolder.addError("could not find any user with email [" + email + "].");
			return null;
		}
		if (getDomainService().getUserStore().isApplicationUser(sender)
				&& sender.getId().equals(sender.getDisplayName())
				&& StringUtils.hasText(personal)) {
			sender.setDisplayName(personal);
			domainService.updateUser(sender);
		}
		errorHolder.addInfo("From: " + sender.getId() + " (" + sender.getDisplayName() + ")");
		return sender;
	}

	/**
	 * Used to decode the subject and filename.
	 * @param input
	 * @return the decoded text
	 */
	protected String decodeText(final String input) {
		if (input == null) {
			return null;
		}
		String decoded;
		try {
			decoded = MimeUtility.decodeText(input);
		} catch (UnsupportedEncodingException e) {
			return input;
		}
		if (input.equals(decoded)) {
			return input;
		}
		return decodeText(decoded);
	}

	/**
	 * @return the subject of a message.
	 * @param message
	 * @param errorHolder
	 */
	protected String extractSubject(
			final Message message,
			final ErrorHolder errorHolder) {
		if (logger.isDebugEnabled()) {
			logger.debug("extractsubject(" + message + ")");
		}
		String subject;
		try {
			subject = decodeText(message.getSubject());
		} catch (MessagingException e) {
			errorHolder.addError("invalid sender: " + e.getMessage());
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			return null;
		}
		if (subject == null) {
			subject = "(no subject)";
		}
		try {
			subject = MimeUtility.decodeText(subject);
		} catch (UnsupportedEncodingException e) {
			errorHolder.addInfo("could not decode the subject, keeping it encoded");
		}
		errorHolder.addInfo("Subject: " + subject);
		return subject;
	}

	/**
	 * @return the content-type of a message part.
	 * @param part
	 * @param errorHolder
	 */
	protected String extractContentType(
			final Part part,
			final ErrorHolder errorHolder) {
		if (logger.isDebugEnabled()) {
			logger.debug("extractContentType(" + part + ")");
		}
		String contentType;
		try {
			contentType = part.getContentType();
		} catch (MessagingException e) {
			errorHolder.addError("could not get part content-type: " + e.getMessage());
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			return null;
		}
		if (contentType == null) {
			errorHolder.addError("no content-type found.");
			return null;
		}
		errorHolder.addInfo("Content-Type: " + contentType);
		return contentType;
	}

	/**
	 * @return the content-type of a message part.
	 * @param message
	 * @param errorHolder
	 */
	protected byte[] extractData(
			final Message message,
			final ErrorHolder errorHolder) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			message.writeTo(os);
			byte[] data = os.toByteArray();
			errorHolder.addInfo("message data read.");
			return data;
		} catch (IOException e) {
			errorHolder.addError("could not read mail data: " + e.getMessage());
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			return null;
		} catch (MessagingException e) {
			errorHolder.addError("could not get mail data: " + e.getMessage());
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			return null;
		}
	}

	/**
	 * Build a part index.
	 * @param upperIndex
	 * @param i
	 * @return the index
	 */
	protected String getPartIndex(
			final String upperIndex,
			final int i) {
		return upperIndex + "-" + i;
	}

	/**
	 * Get the content of a message part.
	 * @param part
	 * @param errorHolder
	 * @return the content as an Object, or null.
	 */
	protected Object getPartContent(
			final Part part,
			final ErrorHolder errorHolder) {
		errorHolder.addInfo("getting the content of part " + part + "...");
		Object content = null;
		try {
			try {
				content = part.getContent();
			} catch (UnsupportedEncodingException e) {
				if (part.isMimeType("text/plain")) {
					errorHolder.addInfo(
							"could not get mail content because of an unknown charset: "
							+ e.getMessage());
					errorHolder.addInfo("trying to get the stream...");
					content = IOUtils.toString(part.getInputStream());
					errorHolder.addInfo("stream read.");
				} else {
					errorHolder.addError(
							"could not get mail content because of an unknown charset: "
							+ e.getMessage());
					return null;
				}
			}
		} catch (IOException e) {
			errorHolder.addError("could not get part content: " + e.getMessage());
			return null;
		} catch (MessagingException e) {
			errorHolder.addError("could not get part content: " + e.getMessage());
			return null;
		}
		if (content == null) {
			errorHolder.addError("content not found");
			return null;
		}
		if (content instanceof Message) {
			errorHolder.addInfo("content is a message itself: " + content);
			content = getPartContent((Message) content, errorHolder);
		}
		errorHolder.addInfo("content of part " + part + " extracted: " + content.getClass());
		return content;
	}

	/**
	 * Get the content of a message part as a String.
	 * @param part the message part
	 * @param errorHolder
	 * @return a String.
	 */
	protected String getPartContentAsString(
			final Part part,
			final ErrorHolder errorHolder) {
		Object content = getPartContent(part, errorHolder);
		if (content == null) {
			return null;
		}
		if (content instanceof String) {
			return (String) content;
		}
		errorHolder.addError(
				"getPartContentAsString(): unexpected class " + content.getClass().getName()
				+ " (" + String.class.getName() + " expected)");
		return null;
	}

	/**
	 * Get the content of a message part as a multipart or String.
	 * @param part the message part
	 * @param errorHolder
	 * @return a String.
	 */
	protected MimeMultipart getPartContentAsMultipart(
			final Part part,
			final ErrorHolder errorHolder) {
		Object content = getPartContent(part, errorHolder);
		if (content == null) {
			return null;
		}
		if (content instanceof MimeMultipart) {
			return (MimeMultipart) content;
		}
		errorHolder.addError(
				"getPartContentAsMultipart(): unexpected class " + content.getClass().getName()
				+ " (" + MimeMultipart.class.getName() + " expected)");
		return null;
	}

	/**
	 * Get the content of a message part as a multipart or String.
	 * @param part the message part
	 * @param errorHolder
	 * @return a String.
	 */
	protected Object getPartContentAsMultipartOrString(
			final Part part,
			final ErrorHolder errorHolder) {
		Object content = getPartContent(part, errorHolder);
		if (content == null) {
			return null;
		}
		if (content instanceof MimeMultipart) {
			return content;
		}
		if (content instanceof String) {
			return content;
		}
		errorHolder.addError(
				"getPartContentAsMultipartOrString(): unexpected class "
				+ content.getClass().getName()
				+ " (" + MimeMultipart.class.getName() + " or "
				+ String.class.getName() + " expected)");
		return null;
	}

	/**
	 * Convert a content to an array of bytes.
	 * @param content the content
	 * @param errorHolder
	 * @return an array of bytes.
	 */
	protected byte [] contentToByteArray(
			final Object content,
			final ErrorHolder errorHolder) {
		if (content instanceof String) {
			return ((String) content).getBytes();
		}
		if (content instanceof InputStream) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int ch;
				while ((ch = ((InputStream) content).read()) != -1) {
					baos.write(ch);
				}
				return baos.toByteArray();
			} catch (IOException e) {
				errorHolder.addError(
						"simpleContentToByteArray(): + " + e.getMessage());
				return null;
			}
		}
		if (content instanceof MimeMultipart) {
			try {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				((MimeMultipart) content).writeTo(os);
				return os.toByteArray();
			} catch (IOException e) {
				errorHolder.addError(
						"could not convert multipart content to an array of bytes: "
						+ e.getMessage());
				return null;
			} catch (MessagingException e) {
				errorHolder.addError(
						"could not convert multipart content to an array of bytes: "
						+ e.getMessage());
				return null;
			}
		}
		errorHolder.addError(
				"simpleContentToByteArray(): unexpected class "
				+ content.getClass().getName()
				+ " (" + String.class.getName() + ", "
				+ " (" + MimeMultipart.class.getName() + " or "
				+ InputStream.class.getName() + " expected)");
		return null;
	}

	/**
	 * Get the filename associated with a part.
	 * @param part the part
	 * @param ticket
	 * @param partIndex
	 * @param ext the extension to use (if not null).
	 * @param errorHolder
	 * @return a String.
	 */
	protected String getPartFilename(
			final Part part,
			final Ticket ticket,
			final String partIndex,
			final String ext,
			final ErrorHolder errorHolder) {
		String filename = null;
		try {
			filename = decodeText(part.getFileName());
		} catch (MessagingException e) {
			errorHolder.addInfo("could not get part filename: " + e.getMessage());
			errorHolder.addInfo("building a filename...");
		}
		if (filename == null) {
			String extension = ext;
			if (extension == null) {
				String contentType = extractContentType(part, errorHolder);
				if (contentType != null) {
					extension = new MimeTypeMap().getDefaultExtension(contentType.toLowerCase());
				}
				if (extension == null) {
					errorHolder.addInfo("unknown content-type " + contentType + ", using bin");
					extension = ".bin";
				}
			}
			filename = ticket.getId() + partIndex + extension;
		}
		errorHolder.addInfo("Filename: " + filename);
		return filename;
	}

	/**
	 * Get a plain/text part.
	 * @param part
	 * @param ticket
	 * @param partIndex
	 * @param errorHolder
	 */
	protected void getTextPart(
			final Part part,
			final Ticket ticket,
			final String partIndex,
			final ErrorHolder errorHolder) {
		errorHolder.addInfo("analysing text/plain part " + part + "...");
		String content = getPartContentAsString(part, errorHolder);
		if (!StringUtils.hasText(content)) {
			return;
		}
		byte [] contentByteArray = contentToByteArray(content, errorHolder);
		if (contentByteArray == null) {
			return;
		}
		String filename = getPartFilename(part, ticket, partIndex, ".txt", errorHolder);
		getDomainService().addFileInfo(new FileInfo(
				filename, contentByteArray, ticket, null, ActionScope.DEFAULT));
		errorHolder.addInfo("added attached file " + filename);
		content = content.replaceAll("\"", "&quot;");
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		content = content.replaceAll("[\\r\\n]+", "<br />");
		content = i18nService.getString("TICKET_ACTION.EMAIL_FEED.EXPAND_PART", filename) + content;
		domainService.giveInformation(
				null, ticket, content,
				ActionScope.DEFAULT, false);
	}

	/**
	 * Get a text/html part.
	 * @param part
	 * @param ticket
	 * @param partIndex
	 * @param errorHolder
	 */
	protected void getHtmlPart(
			final Part part,
			final Ticket ticket,
			final String partIndex,
			final ErrorHolder errorHolder) {
		errorHolder.addInfo("analysing text/html part " + part + "...");
		String content = getPartContentAsString(part, errorHolder);
		if (!StringUtils.hasText(content)) {
			return;
		}
		byte [] contentByteArray = contentToByteArray(content, errorHolder);
		if (contentByteArray == null) {
			return;
		}
		String filename = getPartFilename(part, ticket, partIndex, ".html", errorHolder);
		getDomainService().addFileInfo(new FileInfo(
				filename, contentByteArray, ticket, null, ActionScope.DEFAULT));
		errorHolder.addInfo("added attached file " + filename);
		MessageHtmlCleaner cleaner = new MessageHtmlCleaner(content);
		String output;
		try {
			cleaner.clean();
			output = i18nService.getString(
					"TICKET_ACTION.EMAIL_FEED.EXPAND_PART", filename)
					+ cleaner.getXmlAsString();
		} catch (IOException e) {
			errorHolder.addInfo("could not clean the HTML: " + e.getMessage());
			return;
		}
		domainService.giveInformation(
				null, ticket, output,
				ActionScope.DEFAULT, false);
	}

	/**
	 * Get the content of a non multipart message part.
	 * @param part
	 * @param errorHolder
	 * @return an object.
	 */
	protected Object getMiscPartContent(
			final Part part,
			final ErrorHolder errorHolder) {
		Object content = getPartContent(part, errorHolder);
		if (content == null) {
			return null;
		}
		if (content instanceof String) {
			return content;
		}
		if (content instanceof InputStream) {
			return content;
		}
		errorHolder.addError(
				"getSimplePartContent(): unexpected class " + content.getClass().getName()
				+ " (" + String.class.getName() + " or " + InputStream.class.getName() + " expected)");
		return null;
	}

	/**
	 * Get a misc part.
	 * @param part
	 * @param ticket
	 * @param partIndex
	 * @param errorHolder
	 */
	protected void getMiscPart(
			final Part part,
			final Ticket ticket,
			final String partIndex,
			final ErrorHolder errorHolder) {
		errorHolder.addInfo("analysing misc part " + part + "...");
		Object content = getMiscPartContent(part, errorHolder);
		if (content == null) {
			return;
		}
		byte [] contentByteArray = contentToByteArray(content, errorHolder);
		if (contentByteArray == null) {
			return;
		}
		String filename = getPartFilename(part, ticket, partIndex, ".bin", errorHolder);
		getDomainService().addFileInfo(
				new FileInfo(filename, contentByteArray, ticket, null, ActionScope.DEFAULT));
		errorHolder.addInfo("added attached file " + filename);
	}

	/**
	 * @return the number of parts of a multipart.
	 * @param multipart
	 * @param errorHolder
	 */
	protected int getMultipartCount(
			final MimeMultipart multipart,
			final ErrorHolder errorHolder) {
		try {
			int partCount = multipart.getCount();
			errorHolder.addInfo(partCount + " parts found");
			return partCount;
		} catch (MessagingException e) {
			errorHolder.addError("could not get the number of parts: " + e.getMessage());
			return 0;
		}
	}

	/**
	 * Get an alternative multipart.
	 * @param multipart
	 * @param ticket
	 * @param partIndex
	 * @param errorHolder
	 */
	protected void getAlternativeParts(
			final MimeMultipart multipart,
			final Ticket ticket,
			final String partIndex,
			final ErrorHolder errorHolder) {
		errorHolder.addInfo("analysing multipart/alternative " + multipart + "...");
		BodyPart textPart = null;
		BodyPart htmlPart = null;
		String htmlPartIndex = null;
		String textPartIndex = null;
		int partCount = getMultipartCount(multipart, errorHolder);
		for (int i = partCount - 1; i >= 0; i--) {
			BodyPart bodyPart = null;
			try {
				bodyPart = multipart.getBodyPart(i);
			} catch (MessagingException e) {
				errorHolder.addError("could not get part #" + i + ": " + e.getMessage());
			}
			if (bodyPart != null) {
				try {
					if (bodyPart.getContentType().toLowerCase().startsWith("text/plain")) {
						errorHolder.addInfo("found a text/plain part");
						textPart = bodyPart;
						textPartIndex = getPartIndex(partIndex, i);
					} else if (bodyPart.getContentType().toLowerCase().startsWith("text/html")) {
						errorHolder.addInfo("found a text/html part");
						htmlPart = bodyPart;
						htmlPartIndex = getPartIndex(partIndex, i);
					}
				} catch (MessagingException e) {
					errorHolder.addError(
							"could not get the content-type of part #"
							+ i + ": " + e.getMessage());
				}
			}
		}
		if (textPart == null && htmlPart == null) {
			errorHolder.addError(
					"could not get any text/plain or text/html part multipart/alternative mail");
			return;
		}
		if (htmlPart != null) {
			errorHolder.addInfo("adding the text/html part...");
			getParts(htmlPart, ticket, htmlPartIndex, errorHolder);
		} else if (textPart != null) {
			errorHolder.addInfo("adding the text/plain part...");
			getParts(textPart, ticket, textPartIndex, errorHolder);
		}
	}

	/**
	 * Get a mixed multipart.
	 * @param multipart
	 * @param ticket
	 * @param partIndex
	 * @param errorHolder
	 */
	protected void getMixedParts(
			final MimeMultipart multipart,
			final Ticket ticket,
			final String partIndex,
			final ErrorHolder errorHolder) {
		errorHolder.addInfo("analysing multipart/mixed " + multipart + "...");
		int partCount = getMultipartCount(multipart, errorHolder);
		for (int i = partCount - 1; i >= 0; i--) {
			BodyPart bodyPart = null;
			try {
				bodyPart = multipart.getBodyPart(i);
			} catch (MessagingException e) {
				errorHolder.addError("could not get part #" + i + ": " + e.getMessage());
			}
			if (bodyPart != null) {
				errorHolder.addInfo("adding part#" + i + "...");
				getParts(bodyPart, ticket, getPartIndex(partIndex, i), errorHolder);
			}
		}
	}

	/**
	 * Get a signed multipart.
	 * @param multipart
	 * @param ticket
	 * @param partIndex
	 * @param errorHolder
	 */
	protected void getSignedParts(
			final MimeMultipart multipart,
			final Ticket ticket,
			final String partIndex,
			final ErrorHolder errorHolder) {
		errorHolder.addInfo("analysing multipart/signed " + multipart + "...");
		int partCount = getMultipartCount(multipart, errorHolder);
		for (int i = partCount - 1; i >= 0; i--) {
			BodyPart bodyPart = null;
			try {
				bodyPart = multipart.getBodyPart(i);
			} catch (MessagingException e) {
				errorHolder.addError("could not get part #" + i + ": " + e.getMessage());
			}
			if (bodyPart != null) {
				errorHolder.addInfo("adding part#" + i + "...");
				getParts(bodyPart, ticket, getPartIndex(partIndex, i), errorHolder);
			}
		}
	}

	/**
	 * Get a message part.
	 * @param part
	 * @param ticket
	 * @param partIndex
	 * @param errorHolder
	 */
	protected void getMessageParts(
			final Part part,
			final Ticket ticket,
			final String partIndex,
			final ErrorHolder errorHolder) {
		errorHolder.addInfo("analysing message/xxx " + part + "...");
		byte [] contentByteArray;
		if (part instanceof MimeMultipart) {
			contentByteArray = contentToByteArray(part, errorHolder);
			if (contentByteArray == null) {
				return;
			}
		} else if (part instanceof IMAPBodyPart) {
			Object content = getPartContentAsMultipartOrString(part, errorHolder);
			if (content == null) {
				return;
			}
			contentByteArray = contentToByteArray(content, errorHolder);
			if (contentByteArray == null) {
				return;
			}
		} else {
			errorHolder.addError(
					"getMessagePart(): unexpected class " + part.getClass().getName()
					+ " (" + MimeMultipart.class.getName() + " or "
					+ IMAPBodyPart.class.getName() + " expected)");
			return;
		}
		String filename = getPartFilename(part, ticket, partIndex, ".elm", errorHolder);
		getDomainService().addFileInfo(new FileInfo(
				filename, contentByteArray, ticket, null, ActionScope.DEFAULT));
		errorHolder.addInfo("added attached file " + filename);
		if (part instanceof MimeMultipart) {
			getMixedParts((MimeMultipart) part, ticket, partIndex, errorHolder);
		}
	}

	/**
	 * Get the parts of a part.
	 * @param part
	 * @param ticket
	 * @param partIndex
	 * @param errorHolder
	 */
	protected void getParts(
			final Part part,
			final Ticket ticket,
			final String partIndex,
			final ErrorHolder errorHolder) {
		errorHolder.addInfo("analysing multipart " + part + "...");
		String contentType = extractContentType(part, errorHolder);
		if (contentType == null) {
			return;
		}
		if (contentType.toLowerCase().startsWith("multipart/alternative")) {
			MimeMultipart multipart = getPartContentAsMultipart(part, errorHolder);
			if (multipart == null) {
				return;
			}
			getAlternativeParts(multipart, ticket, partIndex, errorHolder);
		} else if (contentType.toLowerCase().startsWith("multipart/mixed")
				|| contentType.toLowerCase().startsWith("multipart/related")) {
			MimeMultipart multipart = getPartContentAsMultipart(part, errorHolder);
			if (multipart == null) {
				return;
			}
			getMixedParts(multipart, ticket, partIndex, errorHolder);
		} else if (contentType.toLowerCase().startsWith("multipart/signed")) {
			MimeMultipart multipart = getPartContentAsMultipart(part, errorHolder);
			if (multipart == null) {
				return;
			}
			getSignedParts(multipart, ticket, partIndex, errorHolder);
		} else if (contentType.toLowerCase().startsWith("multipart/appledouble")) {
			MimeMultipart multipart = getPartContentAsMultipart(part, errorHolder);
			if (multipart == null) {
				return;
			}
			getMixedParts(multipart, ticket, partIndex, errorHolder);
		} else if (contentType.toLowerCase().startsWith("multipart")) {
			errorHolder.addError("unknown multipart content-type: " + contentType);
		} else if (contentType.toLowerCase().startsWith("message")) {
			getMessageParts(part, ticket, partIndex, errorHolder);
		} else if (contentType.toLowerCase().startsWith("text/plain")) {
			getTextPart(part, ticket, partIndex, errorHolder);
		} else if (contentType.toLowerCase().startsWith("text/html")) {
			getHtmlPart(part, ticket, partIndex, errorHolder);
		} else {
			getMiscPart(part, ticket, partIndex, errorHolder);
		}
		errorHolder.addInfo("done with the analysis of part " + part + "...");
	}

	/**
	 * Split a message into multiple parts.
	 * @param message
	 * @param ticket
	 * @param errorHolder
	 */
	protected void getMessageParts(
			final Message message,
			final Ticket ticket,
			final ErrorHolder errorHolder) {
		getParts(message, ticket, "", errorHolder);
	}

	/**
	 * @see org.esupportail.helpdesk.services.feed.imap.TicketMessageReader#read(
	 * javax.mail.Message, java.lang.String, org.esupportail.helpdesk.domain.beans.Department,
	 * org.esupportail.helpdesk.domain.beans.Category, org.esupportail.helpdesk.services.feed.imap.spam.SpamFilter,
	 * boolean, org.esupportail.helpdesk.domain.beans.Category, org.esupportail.helpdesk.services.feed.ErrorHolder)
	 */
	@Override
	public Ticket read(
			final Message message,
			final String address,
			final Department creationDepartment,
			final Category category,
			final SpamFilter spamFilter,
			final boolean deleteSpam,
			final Category spamCategory,
			final ErrorHolder errorHolder) {
		errorHolder.addInfo("analysing message " + message + "...");
		Set<Address> recipients = extractRecipients(message, errorHolder);
		User sender = extractSender(message, errorHolder);
		String subject = extractSubject(message, errorHolder);
		String contentType = extractContentType(message, errorHolder);
		byte[] data = extractData(message, errorHolder);
		Ticket ticket = null;
		if (errorHolder.hasErrors()) {
			errorHolder.addInfo(errorHolder.getErrorNumber() + " error(s) found, skiping the mail");
		} else {
			boolean spam = spamFilter.isSpam(sender, recipients, subject, contentType, data);
			if (spam && deleteSpam) {
				errorHolder.addInfo("message [" + subject + "] from ["
						+ sender + "] was considered as SPAM and deleted");
			} else {
				Category targetCategory;
				if (spam) {
					targetCategory = spamCategory;
				} else {
					targetCategory = category;
				}
				ticket = domainService.addEmailTicket(
						sender, address, creationDepartment, targetCategory,
						subject);
				errorHolder.addInfo("added ticket #" + ticket.getId());
				getMessageParts(message, ticket, errorHolder);
				domainService.addFileInfo(new FileInfo(
						ticket.getId() + ".elm", data, ticket, sender, ActionScope.DEFAULT));
				if (spam) {
					String msg = "<font color=\"red\">"
						+ "This email was considered as SPAM by the application." + "</font>";
					domainService.closeTicket(null, ticket, msg, ActionScope.DEFAULT, false);
					errorHolder.addInfo("message [" + subject + "] from ["
							+ sender + "] was considered as SPAM and closed");
				}
			}
		}
		if (errorHolder.hasErrors()) {
			errorHolder.addInfo(
					errorHolder.getErrorNumber() + " error(s) found");
		}
		errorHolder.addInfo("done with message " + message + "...");
		if (ticket != null && errorHolder.hasErrors()) {
			String analysis = "";
			for (String string : errorHolder.getStrings()) {
				analysis += "\n" + string;
			}
			analysis = analysis.replaceAll("\"", "&quot;");
			analysis = analysis.replaceAll("<", "&lt;");
			analysis = analysis.replaceAll(">", "&gt;");
			analysis = analysis.replaceAll("[\\r\\n]+", "<br />");
			analysis =
				i18nService.getString(
						"TICKET_ACTION.EMAIL_FEED.ERRORS",
						String.valueOf(errorHolder.getErrorNumber()))
				+ "<font color=\"red\">" + analysis + "</font>";
			domainService.giveInformation(
					null, ticket, analysis,
					ActionScope.MANAGER, false);
		}
		return ticket;
	}

	/**
	 * @return the domainService
	 */
	protected DomainService getDomainService() {
		return domainService;
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @return the i18nService
	 */
	protected I18nService getI18nService() {
		return i18nService;
	}

	/**
	 * @param service the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}

}
