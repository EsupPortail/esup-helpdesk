/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Alert;

/** 
 * An entry of the ticket history.
 */ 
public class TicketHistoryEntry implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8696807415528992955L;
	
	/**
	 * The quote length.
	 */
	private static final int QUOTE_LENGTH = 80;

	/**
	 * The quote length.
	 */
	private static final String QUOTE_PREFIX = ">";

	/**
	 * The action.
	 */
	private Action action;
	
	/**
	 * The quoted message.
	 */
	private String quotedMessage;

	/**
	 * True if the action can be viewed by the user.
	 */
	private boolean canView;
	
	/**
	 * True if the scope of the action can be changed by the user.
	 */
	private boolean canChangeScope;

	/**
	 * True if the scope of the action can be updated by the user.
	 */
	private boolean canUpdateInformation;

	/**
	 * The alerts of the action.
	 */
	private List<Alert> alerts;
	
	/**
	 * The new scope.
	 */
	private String newScope;
	
	/**
	 * The style class.
	 */
	private String styleClass;

	/**
	 * @param action
	 * @param canChangeScope
	 * @param canView
	 * @param canChangeScope
	 * @param alerts
	 * @param styleClass 
	 */
	public TicketHistoryEntry(
			final Action action, 
			final boolean canUpdateInformation,
			final boolean canView, 
			final boolean canChangeScope, 
			final List<Alert> alerts,
			final String styleClass) {
		super();
		this.action = action;
		this.canUpdateInformation = canUpdateInformation;
		this.canView = canView;
		this.canChangeScope = canChangeScope;
		this.alerts = alerts;
		if (isCanView()) {
			quotedMessage = quoteMessage(action.getMessage());
		}
		this.newScope = action.getScope();
		this.styleClass = styleClass;
	}
	
	/**
	 * Format a string by wrapping a text with a given length.
	 * @param paragraph a string to format
	 * @param length the maximum length 
	 * @return an array of strings
	 */
	public static String[] wrap(
			final String paragraph, 
			final int length) { 
		List<String> formatted = new ArrayList<String>(); 
		StringTokenizer st = new StringTokenizer(paragraph);
		String next = ""; 
		while (st.hasMoreTokens()) { 
			String line = next;
			if (line.length() >= length) {
				next = st.nextToken();
			} else {
				while (line.length() < length) { 
					if (st.hasMoreTokens()) { 
						next = st.nextToken();
						if ((line.length() + next.length()) > length) { 
							break; 
						} 
						if (!line.equals("")) { 
							line += " "; 
						} 
						line += next; 
						next = ""; 
					} else { 
						break; 
					} 
				} 
			}
			formatted.add(line); 
		} 
		if (!next.equals("")) {
			formatted.add(next); 
		}
		return formatted.toArray(new String[0]); 
	}
	
	/**
	 * @param message
	 * @return the message quoted
	 */
	private String quoteMessage(final String message) {
		String input = message;
		if (!org.springframework.util.StringUtils.hasText(input)) {
			return null;
		}
		String result = "";
		input = input.replaceAll("[\\r\\n]+", " ");
		input = input.replaceAll("<br */*>", "\n");
		input = input.replaceAll("</p>", "\n");
		input = input.replaceAll("</li>", "\n");
		input = input.replaceAll("<[^>]*>", " ");
		input = input.replaceAll("[\"]", "&quot;");
		String [] paragraphs = input.split("\n");
		for (String paragraph : paragraphs) {
			String trimed = paragraph.trim();
			if (org.springframework.util.StringUtils.hasText(trimed)) {
				if (trimed.length() < QUOTE_LENGTH || trimed.startsWith(QUOTE_PREFIX)) {
					result += "<em>" + QUOTE_PREFIX + trimed + "</em><br />\\n";
				} else {
					String [] lines = wrap(trimed, QUOTE_LENGTH);
					for (int j = 0; j < lines.length; j++) {
						result += "<em>" + QUOTE_PREFIX + lines[j] + "</em><br />\\n";
					}
				}
			}
		}
		return result;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @return the canView
	 */
	public boolean isCanView() {
		return canView;
	}

	/**
	 * @return the canChangeScope
	 */
	public boolean isCanChangeScope() {
		return canChangeScope;
	}
	
	/**
	 * @return the canChangeScope
	 */
	public boolean isCanUpdateInformation() {
		return canUpdateInformation;
	}

	/**
	 * @return the alerts
	 */
	public List<Alert> getAlerts() {
		return alerts;
	}

	/**
	 * @return the quotedMessage
	 */
	public String getQuotedMessage() {
		return quotedMessage;
	}

	/**
	 * @return the newScope
	 */
	public String getNewScope() {
		return newScope;
	}

	/**
	 * @param newScope the newScope to set
	 */
	public void setNewScope(final String newScope) {
		this.newScope = newScope;
	}

	/**
	 * @return the styleClass
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * @param styleClass the styleClass to set
	 */
	protected void setStyleClass(final String styleClass) {
		this.styleClass = styleClass;
	}

	public void setCanUpdateInformation(boolean canUpdateInformation) {
		this.canUpdateInformation = canUpdateInformation;
	}

}

