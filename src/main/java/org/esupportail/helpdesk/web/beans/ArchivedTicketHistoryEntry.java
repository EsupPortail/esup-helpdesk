/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.ArchivedAction;

/** 
 * An entry of the archived ticket history.
 */ 
public class ArchivedTicketHistoryEntry implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 15355468147629688L;

	/**
	 * The archived action.
	 */
	private ArchivedAction archivedAction;

	/**
	 * True if the archived action can be viewed by the user.
	 */
	private boolean canView;
	
	/**
	 * The style class.
	 */
	private String styleClass;

	/**
	 * @param archivedAction
	 * @param canView
	 * @param styleClass 
	 */
	public ArchivedTicketHistoryEntry(
			final ArchivedAction archivedAction, 
			final boolean canView,
			final String styleClass) {
		super();
		this.archivedAction = archivedAction;
		this.canView = canView;
		this.styleClass = styleClass;
	}

	/**
	 * @return the canView
	 */
	public boolean isCanView() {
		return canView;
	}

	/**
	 * @return the archivedAction
	 */
	public ArchivedAction getArchivedAction() {
		return archivedAction;
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

}

