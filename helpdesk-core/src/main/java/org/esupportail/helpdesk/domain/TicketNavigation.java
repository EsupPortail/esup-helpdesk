/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

import org.esupportail.helpdesk.domain.beans.Ticket;

/**
 * A ticket navigation.
 */
public class TicketNavigation {

	/**
	 * The previous unread ticket.
	 */
	private Ticket previousUnread;

	/**
	 * The previous visible ticket.
	 */
	private Ticket previousVisible;

	/**
	 * The next visible ticket.
	 */
	private Ticket nextVisible;

	/**
	 * The next unread ticket.
	 */
	private Ticket nextUnread;

	/**
	 * Constructor.
	 * @param previousUnread
	 * @param previousVisible
	 * @param nextVisible
	 * @param nextUnread
	 */
	public TicketNavigation(
			final Ticket previousUnread,
			final Ticket previousVisible,
			final Ticket nextVisible,
			final Ticket nextUnread) {
		super();
		this.previousUnread = previousUnread;
		this.previousVisible = previousVisible;
		this.nextVisible = nextVisible;
		this.nextUnread = nextUnread;
	}

	/**
	 * @return the nextVisible
	 */
	public Ticket getNextVisible() {
		return nextVisible;
	}

	/**
	 * @param nextVisible the nextVisible to set
	 */
	protected void setNextVisible(final Ticket nextVisible) {
		this.nextVisible = nextVisible;
	}

	/**
	 * @return the previousVisible
	 */
	public Ticket getPreviousVisible() {
		return previousVisible;
	}

	/**
	 * @param previousVisible the previousVisible to set
	 */
	protected void setPreviousVisible(final Ticket previousVisible) {
		this.previousVisible = previousVisible;
	}

	/**
	 * @return the previousUnread
	 */
	public Ticket getPreviousUnread() {
		return previousUnread;
	}

	/**
	 * @param previousUnread the previousUnread to set
	 */
	protected void setPreviousUnread(final Ticket previousUnread) {
		this.previousUnread = previousUnread;
	}

	/**
	 * @return the nextUnread
	 */
	public Ticket getNextUnread() {
		return nextUnread;
	}

	/**
	 * @param nextUnread the nextUnread to set
	 */
	protected void setNextUnread(final Ticket nextUnread) {
		this.nextUnread = nextUnread;
	}

}

