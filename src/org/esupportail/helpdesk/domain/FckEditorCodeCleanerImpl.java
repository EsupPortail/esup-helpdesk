/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.ArchivedAction;
import org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer;
import org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry;
import org.esupportail.helpdesk.domain.beans.Faq;


/**
 * Utilities to clean the code given by FCK editor.
 */
public class FckEditorCodeCleanerImpl implements FckEditorCodeCleaner {
	
	/** A logger. */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * Constructor.
	 */
	public FckEditorCodeCleanerImpl() {
		super();
	}

	/**
	 * Remove a malicious tag from a String (before saving to the database).
	 * @param input
	 * @param tag
	 * @return the changed String.
	 */
	protected String removeMaliciousTag(
			final String input,
			final String tag) {
		String result = input;
		String thePattern = "</?" + tag + "[\\s\\S]*?>";
		Pattern pattern = Pattern.compile(thePattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(result);
		result = matcher.replaceAll("");
		return result;
	}
	
	/**
	 * Remove malicious tags such as script or iframe from a String (before saving to the database).
	 * @param input
	 * @return null if no malicious tags were found, the changed String if found.
	 */
	protected String removeMaliciousTags(final String input) {
		if (input == null) { 
			return null;
		}
		String result = input;
		result = removeMaliciousTag(result, "script");
		result = removeMaliciousTag(result, "iframe");
		result = removeMaliciousTag(result, "html");
		result = removeMaliciousTag(result, "body");
		return result;
	}
	
	/**
	 * @see org.esupportail.helpdesk.domain.FckEditorCodeCleaner#removeMaliciousTags(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public boolean removeMaliciousTags(final DeprecatedFaqContainer faqContainer) {
		if (faqContainer.getContent() == null) {
			return false;
		}
		String content = removeMaliciousTags(faqContainer.getContent());
		if (content.equals(faqContainer.getContent())) {
			return false;
		}
		logger.warn(
				"removed malicious tags from FAQ container content.\ninput:\n"
				+ faqContainer.getContent()
				+ "\noutput:\n"
				+ content);
		faqContainer.setContent(content);
		return true;
	}
	
	/**
	 * @see org.esupportail.helpdesk.domain.FckEditorCodeCleaner#removeMaliciousTags(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public boolean removeMaliciousTags(final DeprecatedFaqEntry faqEntry) {
		if (faqEntry.getContent() == null) {
			return false;
		}
		String content = removeMaliciousTags(faqEntry.getContent());
		if (content.equals(faqEntry.getContent())) {
			return false;
		}
		logger.warn(
				"removed malicious tags from FAQ entry content.\ninput:\n"
				+ faqEntry.getContent()
				+ "\noutput:\n"
				+ content);
		faqEntry.setContent(content);
		return true;
	}
	
	/**
	 * @see org.esupportail.helpdesk.domain.FckEditorCodeCleaner#removeMaliciousTags(
	 * org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public boolean removeMaliciousTags(final Faq faq) {
		if (faq.getContent() == null) {
			return false;
		}
		String content = removeMaliciousTags(faq.getContent());
		if (content.equals(faq.getContent())) {
			return false;
		}
		logger.warn(
				"removed malicious tags from FAQ content.\ninput:\n"
				+ faq.getContent()
				+ "\noutput:\n"
				+ content);
		faq.setContent(content);
		return true;
	}
	
	/**
	 * @see org.esupportail.helpdesk.domain.FckEditorCodeCleaner#removeMaliciousTags(
	 * org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	public boolean removeMaliciousTags(final Action action) {
		if (action.getMessage() == null) {
			return false;
		}
		String message = removeMaliciousTags(action.getMessage());
		if (message.equals(action.getMessage())) {
			return false;
		}
		logger.warn(
				"removed malicious tags from action message.\ninput:\n"
				+ action.getMessage()
				+ "\noutput:\n"
				+ message);
		action.setMessage(message);
		return true;
	}
	
	/**
	 * @see org.esupportail.helpdesk.domain.FckEditorCodeCleaner#removeMaliciousTags(
	 * org.esupportail.helpdesk.domain.beans.ArchivedAction)
	 */
	@Override
	public boolean removeMaliciousTags(final ArchivedAction archivedAction) {
		if (archivedAction.getMessage() == null) {
			return false;
		}
		String message = removeMaliciousTags(archivedAction.getMessage());
		if (message.equals(archivedAction.getMessage())) {
			return false;
		}
		logger.warn(
				"removed malicious tags from archived action message.\ninput:\n"
				+ archivedAction.getMessage()
				+ "\noutput:\n"
				+ message);
		archivedAction.setMessage(message);
		return true;
	}
	
}
