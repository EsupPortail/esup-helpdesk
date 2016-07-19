/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.helpdesk.domain.beans.ArchivedAction;
import org.esupportail.helpdesk.domain.beans.ArchivedFileInfo;
import org.esupportail.helpdesk.domain.beans.ArchivedInvitation;
import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Bookmark;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.exceptions.FileException;
import org.esupportail.helpdesk.web.beans.ArchivedFileInfoEntry;
import org.esupportail.helpdesk.web.beans.ArchivedTicketHistoryEntry;

/**
 * The controller for the archived tickets.
 */
public class ArchivedTicketController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3185181891414661960L;

	/**
	 * The number of minutes in a hour.
	 */
	private static final long MINUTES_PER_HOUR = 60;

	/**
	 * The number of (working) hours in a day.
	 */
	private static final long HOURS_PER_DAY = 8;

	/**
	 * The number of minutes in a day.
	 */
	private static final long MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;

	/**
	 * A back page value.
	 */
	private static final String BACK_PAGE_CONTROL_PANEL = "controlPanel";

	/**
	 * A back page value.
	 */
	private static final String BACK_PAGE_SEARCH = "search";

	/**
	 * A back page value.
	 */
	private static final String BACK_PAGE_JOURNAL = "journal";

	/**
	 * A back page value.
	 */
	private static final String BACK_PAGE_BOOKMARKS = "bookmarks";

	/**
	 * A back page value.
	 */
	private static final String BACK_PAGE_STATISTICS = "statistics";

    /**
     * The current archived ticket.
     */
    private ArchivedTicket archivedTicket;

    /**
     * The action entries of the current archived ticket.
     */
    private List<ArchivedTicketHistoryEntry> archivedHistoryEntries;

    /**
     * The file entries of the current archived ticket.
     */
    private List<ArchivedFileInfoEntry> archivedFileInfoEntries;

    /**
     * The spent time (days).
     */
    private long  ticketSpentTimeDays;

    /**
     * The spent time (hours).
     */
    private long ticketSpentTimeHours;

    /**
     * The spent time (minutes).
     */
    private long ticketSpentTimeMinutes;

	/**
	 * True if the current user is allowed to access the view.
	 */
	private boolean userCanViewArchivedTicket;

	/**
	 * True if the current user is allowed to view the connected ticket.
	 */
	private boolean userCanViewConnectionTicket;

	/**
	 * True if the current user is allowed to view the connected archived ticket.
	 */
	private boolean userCanViewConnectionArchivedTicket;

	/**
	 * True if the current user is allowed to view the connected FAQ.
	 */
	private boolean userCanViewConnectionFaq;

	/**
	 * True if the user is invited for the ticket.
	 */
	private boolean invited;

    /**
     * The ArchivedFileInfo to download.
     */
    private ArchivedFileInfo archivedFileInfoToDownload;

    /**
     * The page to go when clicking on the 'back' button'.
     */
    private String backPage;

    /**
     * The invitations of the current ticket.
     */
    private List<ArchivedInvitation> archivedInvitations;

    /**
     * The bookmark of the ticket for the current user.
     */
    private Bookmark bookmark;

    /**
     * The download id.
     */
    private Long downloadId;

	/**
	 * Bean constructor.
	 */
	public ArchivedTicketController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		setArchivedTicket(null);
		archivedFileInfoToDownload = null;
		backPage = BACK_PAGE_SEARCH;
		downloadId = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[archivedTicket=" + archivedTicket
		+ "]";
	}

	/**
	 * @return true if the current user is allowed to access the view.
	 */
	public boolean isPageAuthorized() {
		return getDomainService().userCanViewArchivedTicket(
				getCurrentUser(), getClient(), archivedTicket);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	protected String back() {
		if (BACK_PAGE_CONTROL_PANEL.equals(backPage)) {
			return "navigationControlPanel";
		}
		if (BACK_PAGE_JOURNAL.equals(backPage)) {
			return "navigationJournal";
		}
		if (BACK_PAGE_BOOKMARKS.equals(backPage)) {
			return "navigationBookmarks";
		}
		if (BACK_PAGE_STATISTICS.equals(backPage)) {
			return "navigationStatistics";
		}
		return "navigationSearch";
	}

	/**
	 * @param auth the authorization condition
	 * @return null if the action can be performed, the JSF result otherwise.
	 */
	protected String checkAuth(final boolean auth) {
		if (!auth) {
			addUnauthorizedActionMessage();
			if (!isPageAuthorized()) {
				return "navigationSearch";
			}
			return "view";
		}
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String download() {
		String checkResult = checkAuth(
				getDomainService().userCanDownload(
						getCurrentUser(),
						invited,
						archivedFileInfoToDownload));
		if (checkResult != null) {
			return checkResult;
		}
		try {
			byte[] content = getDomainService().getArchivedFileInfoContent(archivedFileInfoToDownload);
			downloadId = DownloadUtils.setDownload(
					content, archivedFileInfoToDownload.getFilename(), "application/octet-stream");
		} catch (FileException e) {
			addErrorMessage(
					null, "TICKET_ACTION.MESSAGE.DOWNLOAD_ERROR",
					archivedFileInfoToDownload.getFilename(), e.getMessage());
			ExceptionUtils.catchException(e);
		}
		return null;
	}

	/**
	 * @return the ticket
	 */
	public ArchivedTicket getArchivedTicket() {
		return archivedTicket;
	}

	/**
	 * Split the spent time into days, hours and minutes.
	 * @param spentTime
	 */
	private void setSpentTime(final long spentTime) {
		if (spentTime == -1) {
			ticketSpentTimeDays = 0;
			ticketSpentTimeHours = 0;
			ticketSpentTimeMinutes = 0;
		} else {
			ticketSpentTimeDays = spentTime / MINUTES_PER_DAY;
			long minutes = spentTime % MINUTES_PER_DAY;
			ticketSpentTimeHours = minutes / MINUTES_PER_HOUR;
			ticketSpentTimeMinutes = minutes % MINUTES_PER_HOUR;
		}
	}

	/**
	 * @param archivedTicket
	 */
	public void setArchivedTicket(final ArchivedTicket archivedTicket) {
		if (archivedTicket == null) {
			this.archivedTicket = null;
			setSpentTime(-1);
			archivedHistoryEntries = null;
			archivedFileInfoEntries = null;
			userCanViewArchivedTicket = false;
			userCanViewConnectionTicket = false;
			userCanViewConnectionArchivedTicket = false;
			userCanViewConnectionFaq = false;
			invited = false;
			archivedInvitations = null;
			bookmark = null;
		} else {
			this.archivedTicket = new ArchivedTicket(archivedTicket);
			getDomainService().addHistoryItem(getCurrentUser(), archivedTicket);
			setSpentTime(archivedTicket.getSpentTime());
			List<Department> ticketViewVisibleDepartments =
				getDomainService().getTicketViewDepartments(
					getCurrentUser(),  getClient());
			archivedHistoryEntries = new ArrayList<ArchivedTicketHistoryEntry>();
			invited = getDomainService().isInvited(getCurrentUser(), archivedTicket);
			for (ArchivedAction archivedAction : getDomainService().getArchivedActions(archivedTicket)) {
				archivedHistoryEntries.add(new ArchivedTicketHistoryEntry(
						archivedAction,
						getDomainService().userCanViewArchivedAction(
								getCurrentUser(), invited, archivedAction),
						getDomainService().getArchivedActionStyleClass(archivedAction)));
			}
			archivedFileInfoEntries = new ArrayList<ArchivedFileInfoEntry>();
			for (ArchivedFileInfo archivedFileInfo : getDomainService().getArchivedFileInfos(
					archivedTicket)) {
				archivedFileInfoEntries.add(new ArchivedFileInfoEntry(
						archivedFileInfo,
						getDomainService().userCanDownload(
								getCurrentUser(), invited, archivedFileInfo)));
			}
			archivedInvitations = getDomainService().getArchivedInvitations(archivedTicket);
			userCanViewArchivedTicket = getDomainService().userCanViewArchivedTicket(
					getCurrentUser(), archivedTicket, ticketViewVisibleDepartments);
			userCanViewConnectionTicket = getDomainService().userCanViewTicket(
					getCurrentUser(), archivedTicket.getConnectionTicket(),
					ticketViewVisibleDepartments);
			userCanViewConnectionArchivedTicket = getDomainService().userCanViewArchivedTicket(
					getCurrentUser(), archivedTicket.getConnectionArchivedTicket(),
					ticketViewVisibleDepartments);
			userCanViewConnectionFaq = false;
			if (archivedTicket.getConnectionFaq() != null) {
				List<Department> faqViewVisibleDepartments =
					getDomainService().getFaqViewDepartments(
						getCurrentUser(),  getClient());
				userCanViewConnectionFaq = getDomainService().userCanViewFaq(
						getCurrentUser(), archivedTicket.getConnectionFaq(),
						faqViewVisibleDepartments);
			}
			bookmark = getDomainService().getBookmark(getCurrentUser(), archivedTicket);
		}
	}

	/**
	 * @return a permanent link to the page for application users.
	 */
	public String getApplicationPermLink() {
		return getUrlBuilder().getTicketViewUrl(AuthUtils.APPLICATION, archivedTicket.getTicketId());
	}

	/**
	 * @return a permanent link to the page for CAS users.
	 */
	public String getCasPermLink() {
		return getUrlBuilder().getTicketViewUrl(AuthUtils.CAS, archivedTicket.getTicketId());
	}

	/**
	 * @return a permanent link to the page for Shibboleth users.
	 */
	public String getShibbolethPermLink() {
		return getUrlBuilder().getTicketViewUrl(AuthUtils.SHIBBOLETH, archivedTicket.getTicketId());
	}

	/**
	 * @return a permanent link to the page for specific users.
	 */
	public String getSpecificPermLink() {
		return getUrlBuilder().getTicketViewUrl(AuthUtils.SPECIFIC, archivedTicket.getTicketId());
	}

	/**
	 * refresh the current ticket.
	 */
	public void refreshArchivedTicket() {
		setArchivedTicket(archivedTicket);
	}

	/**
	 * JSF callback.
	 */
	public void addBookmark() {
		getDomainService().addBookmark(getCurrentUser(), getArchivedTicket());
		refreshArchivedTicket();
	}

	/**
	 * JSF callback.
	 */
	public void deleteBookmark() {
		getDomainService().deleteBookmark(bookmark);
		refreshArchivedTicket();
	}

	/**
	 * @return the ticketSpentTimeDays
	 */
	public long getTicketSpentTimeDays() {
		return ticketSpentTimeDays;
	}

	/**
	 * @return the ticketSpentTimeHours
	 */
	public long getTicketSpentTimeHours() {
		return ticketSpentTimeHours;
	}

	/**
	 * @return the ticketSpentTimeMinutes
	 */
	public long getTicketSpentTimeMinutes() {
		return ticketSpentTimeMinutes;
	}

	/**
	 * @return the backPage
	 */
	public String getBackPage() {
		return backPage;
	}

	/**
	 * @param backPage the backPage to set
	 */
	public void setBackPage(final String backPage) {
		this.backPage = backPage;
	}

	/**
	 * @return the userCanViewConnectionTicket
	 */
	public boolean isUserCanViewConnectionTicket() {
		return userCanViewConnectionTicket;
	}

	/**
	 * @return the userCanViewConnectionArchivedTicket
	 */
	public boolean isUserCanViewConnectionArchivedTicket() {
		return userCanViewConnectionArchivedTicket;
	}

	/**
	 * @return the userCanViewConnectionFaq
	 */
	public boolean isUserCanViewConnectionFaq() {
		return userCanViewConnectionFaq;
	}

	/**
	 * @return the archivedHistoryEntries
	 */
	public List<ArchivedTicketHistoryEntry> getArchivedHistoryEntries() {
		return archivedHistoryEntries;
	}

	/**
	 * @return the archivedFileInfoEntries
	 */
	public List<ArchivedFileInfoEntry> getArchivedFileInfoEntries() {
		return archivedFileInfoEntries;
	}

	/**
	 * @return the archivedFileInfoEntriesNumber
	 */
	public int getArchivedFileInfoEntriesNumber() {
		if (archivedFileInfoEntries == null) {
			return 0;
		}
		return archivedFileInfoEntries.size();
	}

	/**
	 * @return the userCanViewArchivedTicket
	 */
	public boolean isUserCanViewArchivedTicket() {
		return userCanViewArchivedTicket;
	}

	/**
	 * @param archivedFileInfoToDownload the archivedFileInfoToDownload to set
	 */
	public void setArchivedFileInfoToDownload(
			final ArchivedFileInfo archivedFileInfoToDownload) {
		this.archivedFileInfoToDownload = archivedFileInfoToDownload;
	}

	/**
	 * @return the archivedInvitations
	 */
	public List<ArchivedInvitation> getArchivedInvitations() {
		return archivedInvitations;
	}

	/**
	 * @return the archivedInvitations
	 */
	public int getArchivedInvitationsNumber() {
		if (archivedInvitations == null) {
			return 0;
		}
		return archivedInvitations.size();
	}

	/**
	 * @return the bookmark
	 */
	public Bookmark getBookmark() {
		return bookmark;
	}

	/**
	 * @param bookmark the bookmark to set
	 */
	protected void setBookmark(final Bookmark bookmark) {
		this.bookmark = bookmark;
	}

	/**
	 * @param downloadId the downloadId to set
	 */
	protected void setDownloadId(final Long downloadId) {
		this.downloadId = downloadId;
	}

	/**
	 * @return the downloadId
	 */
	public Long getDownloadId() {
		Long id = downloadId;
		downloadId = null;
		return id;
	}

}
