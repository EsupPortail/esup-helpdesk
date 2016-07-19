/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.helpdesk.domain.beans.Bookmark;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.HistoryItem;
import org.esupportail.helpdesk.web.beans.BookmarkEntry;
import org.esupportail.helpdesk.web.beans.HistoryItemEntry;

/**
 * The bookmarks controller.
 */
public class BookmarksController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8125291010137712891L;

	/**
	 * The bookmark entries.
	 */
	private List<BookmarkEntry> bookmarkEntries;

	/**
	 * The history item entries.
	 */
	private List<HistoryItemEntry> historyItemEntries;

	/**
	 * The bookmark to delete.
	 */
	private Bookmark bookmarkToDelete;

	/**
	 * Bean constructor.
	 */
	public BookmarksController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		bookmarkEntries = null;
		bookmarkToDelete = null;
		historyItemEntries = null;
	}

	/**
	 * @return true if the current user is allowed to access the view.
	 */
	public boolean isPageAuthorized() {
		return getCurrentUser() != null;
	}

	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			return null;
		}
		bookmarkEntries = new ArrayList<BookmarkEntry>();
		List<Department> ticketViewVisibleDepartments =
			getDomainService().getTicketViewDepartments(
				getCurrentUser(), getClient());
		refreshBookmarks(ticketViewVisibleDepartments);
		refreshHistoryItems(ticketViewVisibleDepartments);
		return "navigationBookmarks";
	}

	/**
	 * Refresh the bookmarks.
	 * @param ticketViewVisibleDepartments
	 */
	protected void refreshBookmarks(final List<Department> ticketViewVisibleDepartments) {
		bookmarkEntries = new ArrayList<BookmarkEntry>();
		for (Bookmark bookmark : getDomainService().getBookmarks(getCurrentUser())) {
			boolean canRead = false;
			if (bookmark.isTicketBookmark()) {
				canRead = getDomainService().userCanViewTicket(
						getCurrentUser(), bookmark.getTicket(), ticketViewVisibleDepartments);
			} else if (bookmark.isArchivedTicketBookmark()) {
				canRead = getDomainService().userCanViewArchivedTicket(
						getCurrentUser(), bookmark.getArchivedTicket(),
						ticketViewVisibleDepartments);
			}
			bookmarkEntries.add(new BookmarkEntry(bookmark, canRead));
		}
	}

	/**
	 * Refresh the history items.
	 * @param ticketViewVisibleDepartments
	 */
	protected void refreshHistoryItems(final List<Department> ticketViewVisibleDepartments) {
		historyItemEntries = new ArrayList<HistoryItemEntry>();
		for (HistoryItem historyItem : getDomainService().getHistoryItems(getCurrentUser())) {
			boolean canRead = false;
			if (historyItem.isTicketHistoryItem()) {
				canRead = getDomainService().userCanViewTicket(
						getCurrentUser(), historyItem.getTicket(),
						ticketViewVisibleDepartments);
			} else if (historyItem.isArchivedTicketHistoryItem()) {
				canRead = getDomainService().userCanViewArchivedTicket(
						getCurrentUser(), historyItem.getArchivedTicket(),
						ticketViewVisibleDepartments);
			}
			historyItemEntries.add(new HistoryItemEntry(historyItem, canRead));
		}
	}

	/**
	 * JSF callback.
	 */
	public void deleteBookmark() {
		getDomainService().deleteBookmark(bookmarkToDelete);
		enter();
	}

	/**
	 * @return a permanent link to the page for application users.
	 */
	public String getApplicationPermLink() {
		return getUrlBuilder().getBookmarksUrl(AuthUtils.APPLICATION);
	}

	/**
	 * @return a permanent link to the page for CAS users.
	 */
	public String getCasPermLink() {
		return getUrlBuilder().getBookmarksUrl(AuthUtils.CAS);
	}

	/**
	 * @return a permanent link to the page for Shibboleth users.
	 */
	public String getShibbolethPermLink() {
		return getUrlBuilder().getBookmarksUrl(AuthUtils.SHIBBOLETH);
	}

	/**
	 * @return a permanent link to the page for specific users.
	 */
	public String getSpecificPermLink() {
		return getUrlBuilder().getBookmarksUrl(AuthUtils.SPECIFIC);
	}

	/**
	 * @return the bookmark entries number
	 */
	public int getBookmarkEntriesNumber() {
		return bookmarkEntries.size();
	}

	/**
	 * @return the bookmarkEntries
	 */
	public List<BookmarkEntry> getBookmarkEntries() {
		return bookmarkEntries;
	}

	/**
	 * @param bookmarkEntries the bookmarkEntries to set
	 */
	protected void setBookmarkEntries(final List<BookmarkEntry> bookmarkEntries) {
		this.bookmarkEntries = bookmarkEntries;
	}

	/**
	 * @param bookmarkToDelete the bookmarkToDelete to set
	 */
	public void setBookmarkToDelete(final Bookmark bookmarkToDelete) {
		this.bookmarkToDelete = bookmarkToDelete;
	}

	/**
	 * @return the historyItemEntries
	 */
	public List<HistoryItemEntry> getHistoryItemEntries() {
		return historyItemEntries;
	}

	/**
	 * @param historyItemEntries the historyItemEntries to set
	 */
	protected void setHistoryItemEntries(final List<HistoryItemEntry> historyItemEntries) {
		this.historyItemEntries = historyItemEntries;
	}

	/**
	 * JSF callback.
	 */
	public void clearHistory() {
		getDomainService().clearHistoryItems(getCurrentUser());
		enter();
	}

}
