/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.smtp;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.mail.internet.InternetAddress;

/** 
 * The interface of SMTP services, used to send emails.
 */
public interface SmtpService extends Serializable {

	/**
	 * Send an email. The email may be intercepted depending on the configuration.
	 * @param to 
	 * @param subject 
	 * @param htmlBody 
	 * @param textBody 
	 */
	void send(InternetAddress to, String subject, String htmlBody, String textBody);
	
	/**
	 * Send an email. The email may be intercepted depending on the configuration.
	 * @param to 
	 * @param subject 
	 * @param htmlBody 
	 * @param textBody
	 * @param messageId 
	 */
	void send(InternetAddress to, String subject, String htmlBody, String textBody, String messageId);
	
	/**
	 * Send an email with attach. The email may be intercepted depending on the configuration.
	 * @param to 
	 * @param subject 
	 * @param htmlBody 
	 * @param textBody 
	 * @param files 
	 */
	void send(InternetAddress to, String subject, String htmlBody, String textBody, List<File> files);
	
	/**
	 * Send an email with attach. The email may be intercepted depending on the configuration.
	 * @param to 
	 * @param subject 
	 * @param htmlBody 
	 * @param textBody 
	 * @param files
	 * @param messageId 
	 */
	void send(
			InternetAddress to, 
			String subject, 
			String htmlBody, 
			String textBody, 
			List<File> files, 
			String messageId);
	
	/**
	 * @param tos
	 * @param ccs
	 * @param bccs
	 * @param subject
	 * @param htmlBody
	 * @param textBody
	 * @param files
	 */
	void sendtocc(InternetAddress [] tos, InternetAddress [] ccs, InternetAddress [] bccs,
			String subject, String htmlBody, String textBody, List<File> files);
	
	/**
	 * @param tos
	 * @param ccs
	 * @param bccs
	 * @param subject
	 * @param htmlBody
	 * @param textBody
	 * @param files
	 * @param messageId
	 */
	void sendtocc(
			InternetAddress [] tos, 
			InternetAddress [] ccs, 
			InternetAddress [] bccs,
			String subject, 
			String htmlBody, 
			String textBody, 
			List<File> files, 
			String messageId);
	
	
	/**
	 * Send an email. Email will never be intercepted, even if configured so.
	 * @param to 
	 * @param subject 
	 * @param htmlBody 
	 * @param textBody 
	 */
	void sendDoNotIntercept(InternetAddress to, String subject, String htmlBody, String textBody);
	
	/**
	 * Send an email. Email will never be intercepted, even if configured so.
	 * @param to 
	 * @param subject 
	 * @param htmlBody 
	 * @param textBody
	 * @param messageId
	 */
	void sendDoNotIntercept(InternetAddress to, String subject, String htmlBody, String textBody, String messageId);
	
	/**
	 * Send an email with attach. Email will never be intercepted, even if configured so.
	 * @param to 
	 * @param subject 
	 * @param htmlBody 
	 * @param textBody 
	 * @param files 
	 */
	void sendDoNotIntercept(InternetAddress to, String subject, String htmlBody, String textBody, List<File> files);
	
	/**
	 * Send an email with attach. Email will never be intercepted, even if configured so.
	 * @param to 
	 * @param subject 
	 * @param htmlBody 
	 * @param textBody 
	 * @param files
	 * @param messageId 
	 */
	void sendDoNotIntercept(
			InternetAddress to, 
			String subject, 
			String htmlBody, 
			String textBody, 
			List<File> files, 
			String messageId);
	
	/**
	 * @return true if the class supports testing. If false, calls to method 
	 * test() should throw an exception.
	 */
	boolean supportsTest();
	
	/**
	 * Test the SMTP connection.
	 */
	void test();
	
}
