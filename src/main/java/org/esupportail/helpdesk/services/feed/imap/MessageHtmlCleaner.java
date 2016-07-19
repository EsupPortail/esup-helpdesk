/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed.imap;

import org.htmlcleaner.HtmlCleaner;

/**
 * an HTML CLEANER that applies to message.
 */
public class MessageHtmlCleaner extends HtmlCleaner {

	/**
	 * Constructor.
	 * @param htmlContent 
	 */
	public MessageHtmlCleaner(final String htmlContent) {
		super("<div>" + htmlContent + "</div>", MessageHtmlTagProvider.getInstance());
		setOmitXmlDeclaration(true);
		setOmitDoctypeDeclaration(true);
		setOmitDeprecatedTags(false);
		setTreatDeprecatedTagsAsContent(true);
		setOmitUnknownTags(true);
//		setTreatUnknownTagsAsContent(true);
		setOmitHtmlEnvelope(true);
		setIgnoreQuestAndExclam(true);
		setNamespacesAware(false);
		setPruneTags("head,script,style");
	}

}