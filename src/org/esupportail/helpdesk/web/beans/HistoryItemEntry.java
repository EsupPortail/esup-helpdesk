/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.HistoryItem;

/** 
 * An entry of the history items page.
 */ 
public class HistoryItemEntry implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1735289450676960459L;

	/**
	 * The history item.
	 */
	private HistoryItem historyItem;

	/**
	 * True if the history item can be read by the user.
	 */
	private boolean canRead;

	/**
	 * Bean constructor.
	 * @param historyItem
	 * @param canRead 
	 */
	public HistoryItemEntry(
			final HistoryItem historyItem, 
			final boolean canRead) {
		super();
		this.historyItem = historyItem;
		this.canRead = canRead;
	}

	/**
	 * @return the canRead
	 */
	public boolean isCanRead() {
		return canRead;
	}

	/**
	 * @return the historyItem
	 */
	public HistoryItem getHistoryItem() {
		return historyItem;
	}

}

