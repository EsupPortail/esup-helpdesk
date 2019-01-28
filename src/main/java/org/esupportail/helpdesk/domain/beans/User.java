/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Base64;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.ControlPanel;
import org.esupportail.helpdesk.domain.ControlPanelOrder;
import org.esupportail.helpdesk.domain.Search;
import org.esupportail.helpdesk.web.beans.ControlPanelPaginator;

/**
 * The class that represent users.
 */
public class User implements Serializable, Comparable<User> {

	/** A constant for the start page. */
	public static final String START_PAGE_WELCOME = "WELCOME";
	/** A constant for the start page. */
	public static final String START_PAGE_CONTROL_PANEL = "CONTROL_PANEL";
	/** A constant for the start page. */
	public static final String START_PAGE_SEARCH = "SEARCH";
	/** A constant for the start page. */
	public static final String START_PAGE_JOURNAL = "JOURNAL";
	/** A constant for the start page. */
	public static final String START_PAGE_FAQ = "FAQ";
	/** A constant for the start page. */
	public static final String START_PAGE_PREFERENCES = "PREFERENCES";
	/** A constant for the start page. */
	public static final String START_PAGE_ABOUT = "ABOUT";
	/** A constant for the start page. */
	public static final String START_PAGE_STATISTICS = "STATISTICS";
	/** A constant for the start page. */
	public static final String START_PAGE_BOOKMARKS = "BOOKMARKS";

	/** A constant for page transitions. */
	public static final String PAGE_TRANSITION_NONE = "NONE";
	/** A constant for page transitions. */
	public static final String PAGE_TRANSITION_TEXT = "TEXT";
	/** A constant for page transitions. */
	public static final String PAGE_TRANSITION_IMAGE = "IMAGE";
	/** A constant for page transitions. */
	public static final String PAGE_TRANSITION_TEXT_IMAGE = "TEXT_IMAGE";
	/** A constant for page transitions. */
	public static final String PAGE_TRANSITION_FREEZE = "FREEZE";
	/** A constant for page transitions. */
	public static final String PAGE_TRANSITION_FREEZE_TEXT = "FREEZE_TEXT";
	/** A constant for page transitions. */
	public static final String PAGE_TRANSITION_FREEZE_IMAGE = "FREEZE_IMAGE";
	/** A constant for page transitions. */
	public static final String PAGE_TRANSITION_FREEZE_TEXT_IMAGE = "FREEZE_TEXT_IMAGE";

	/**
	 * The maximum length of encoded attributes.
	 */
	private static final int ATTRIBUTES_ENCODING_MAX_LENGTH = 4096;

	/**
	 * The separator between attributes.
	 */
	private static final String ATTRIBUTES_ENCODING_SEPARATOR = ";";

	/**
	 * The equal sign between the name and value of the attributes.
	 */
	private static final String ATTRIBUTES_ENCODING_EQUAL_SIGN = ":";

//	/**
//	 * A logger.
//	 */
//	private static final Logger LOGGER = new LoggerImpl(User.class);

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 664045960340919141L;

	/**
	 * Indicates if the user is a local user, authenticated with uPortal or CAS (and existing in LDAP).
	 * False means that the user is authenticated with his email and a password.
	 */
	private boolean localUser;

	/**
	 * The password, null for local users.
	 */
	private String password;

	/**
	 * Id of the user.
	 */
	private String id;

	/**
	 * The auth type.
	 */
	private String authType;

	/**
	 * Real id of the user.
	 */
	private String realId;

	/**
	 * The attributes.
	 */
	private String encodedAttributes;

    /**
	 * Display Name of the user.
	 */
    private String displayName;

    /**
	 * Email of the user (memorized for Shibboleth users.
	 */
    private String email;

    /**
	 * True for administrators.
	 */
    private boolean admin;

    /**
     * The prefered language.
     */
    private String language;

    /**
     * The department filter of the user control panel.
     */
    private Department controlPanelUserDepartmentFilter;

    /**
     * The department filter of the manager control panel.
     */
    private Department controlPanelManagerDepartmentFilter;

    /**
     * The manager filter of the manager control panel.
     */
    private String controlPanelManagerManagerFilter;

    /**
     * The category filter of the control panel.
     */
    private Category controlPanelCategoryFilter;

    /**
     * True to select only category members tickets on the control panel.
     */
    private Boolean controlPanelCategoryMemberFilter;

    /**
     * The status filter of the user control panel.
     */
    private String controlPanelUserStatusFilter;

    /**
     * The status filter of the manager control panel.
     */
    private String controlPanelManagerStatusFilter;

    /**
     * The involvement filter of the control panel for the user interface.
     */
    private String controlPanelUserInvolvementFilter;

    /**
     * The involvement filter of the control panel for the manager interface.
     */
    private String controlPanelManagerInvolvementFilter;

    /**
     * The interface of the control panel.
     */
    private Boolean controlPanelUserInterface;

    /**
     * The page size of the control panel.
     */
    private int controlPanelPageSize;

    /**
     * True for the advanced search, false for simple.
     */
    private Boolean advancedSearch;

    /**
     * The department filter of the search interface.
     */
    private Department searchDepartmentFilter;

    /**
     * The search type filter.
     */
    private String searchTypeFilter;

    /**
     * True to sort search results by date.
     */
    private Boolean searchSortByDate;

    /**
     * The auth secret.
     */
    private String authSecret;

    /**
     * The validity of the auth secret.
     */
    private Timestamp authLimit;

    /**
     * True if the user monitors the tickets (s)he owns.
     */
    private Boolean ownerMonitoring;

    /**
     * True if the user monitors the tickets (s)he is is invited for.
     */
    private Boolean invitedMonitoring;

    /**
     * True if the user monitors his bookmarked tickets.
     */
    private Boolean bookmarkMonitoring;

    /**
     * The start page.
     */
    private String startPage;

    /**
     * True to show help when entering tickets.
     */
    private Boolean showAddTicketHelp;

    /**
     * True to show a popup on ticket closure.
     */
    private Boolean showPopupOnClosure;

    /**
     * True to show tickets after closure.
     */
    private Boolean showTicketAfterClosure;

    /**
     * The department filter for the journal page.
     */
    private Department journalDepartmentFilter;

    /**
     * The page size for the journal page.
     */
    private Integer journalPageSize;

    /**
     * The columns to print on the control panel.
     */
    private String controlPanelColumns;

    /**
     * True to receive ticket reports.
     */
    private Boolean receiveTicketReports;

    /**
     * True to receive FAQ reports.
     */
    private Boolean receiveFaqReports;

    /**
     * True to receive all the ticket reports in one.
     */
    private Boolean receiveTicketReportsAllInOne;

    /**
     * True to receive manager monitoring.
     */
    private Boolean receiveManagerMonitoring;

    /**
     * The order of the control panel.
     */
    private String storedControlPanelOrder;

    /**
     * The order of the control panel.
     */
    private ControlPanelOrder controlPanelOrder;

    /**
     * The refresh delay of the control panel.
     */
    private Integer controlPanelRefreshDelay;

    /**
     * The transition between pages.
     */
    private String pageTransition;

    /**
     * The search date 1.
     */
    private Timestamp searchDate1;

    /**
     * The search date 2.
     */
    private Timestamp searchDate2;

    /**
     * The last time the department selection was valid (for the user).
     */
    private Timestamp departmentSelectionContextTime;

    /**
     * True to receive expiration emails.
     */
    private Boolean expirationMonitoring;

	/**
	 * Bean constructor.
	 */
	private User() {
		super();
		localUser = false;
		admin = false;
		controlPanelUserInterface = true;
		controlPanelCategoryMemberFilter = false;
		receiveTicketReports = true;
		receiveFaqReports = true;
		receiveTicketReportsAllInOne = true;
		receiveManagerMonitoring = true;
		controlPanelUserStatusFilter = ControlPanel.STATUS_FILTER_ANY;
		controlPanelManagerStatusFilter = ControlPanel.STATUS_FILTER_ANY;
		controlPanelUserInvolvementFilter = ControlPanel.USER_INVOLVEMENT_FILTER_OWNER;
		controlPanelManagerInvolvementFilter = ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE;
		searchTypeFilter = Search.TYPE_FILTER_ALL;
		bookmarkMonitoring = true;
		expirationMonitoring = true;
	}

	/**
	 * Copy a user.
	 * @param u
	 */
	public User(final User u) {
		setAdmin(u.getAdmin());
		setAdvancedSearch(u.getAdvancedSearch());
		setAuthLimit(u.getAuthLimit());
		setAuthSecret(u.getAuthSecret());
		setAuthType(u.getAuthType());
		setBookmarkMonitoring(u.getBookmarkMonitoring());
		setControlPanelCategoryFilter(u.getControlPanelCategoryFilter());
		setControlPanelCategoryMemberFilter(u.getControlPanelCategoryMemberFilter());
		setControlPanelColumns(u.getControlPanelColumns());
		setControlPanelUserDepartmentFilter(u.getControlPanelUserDepartmentFilter());
		setControlPanelManagerDepartmentFilter(u.getControlPanelManagerDepartmentFilter());
		setControlPanelManagerManagerFilter(u.getControlPanelManagerManagerFilter());
		setControlPanelManagerInvolvementFilter(u.getControlPanelManagerInvolvementFilter());
		setControlPanelOrder(u.getControlPanelOrder());
		setControlPanelPageSize(u.getControlPanelPageSize());
		setControlPanelRefreshDelay(u.getControlPanelRefreshDelay());
		setControlPanelUserStatusFilter(u.getControlPanelUserStatusFilter());
		setControlPanelManagerStatusFilter(u.getControlPanelManagerStatusFilter());
		setControlPanelUserInterface(u.getControlPanelUserInterface());
		setControlPanelUserInvolvementFilter(u.getControlPanelUserInvolvementFilter());
		setDisplayName(u.getDisplayName());
		setEncodedAttributes(u.getEncodedAttributes());
		setId(u.getId());
		setInvitedMonitoring(u.getInvitedMonitoring());
		setJournalDepartmentFilter(u.getJournalDepartmentFilter());
		setJournalPageSize(u.getJournalPageSize());
		setLanguage(u.getLanguage());
		setOwnerMonitoring(u.getOwnerMonitoring());
		setPageTransition(u.getPageTransition());
		setPassword(u.getPassword());
		setRealId(u.getRealId());
		setReceiveManagerMonitoring(u.getReceiveManagerMonitoring());
		setReceiveTicketReports(u.getReceiveTicketReports());
		setReceiveFaqReports(u.getReceiveFaqReports());
		setReceiveTicketReportsAllInOne(u.getReceiveTicketReportsAllInOne());
		setSearchDepartmentFilter(u.getSearchDepartmentFilter());
		setSearchSortByDate(u.getSearchSortByDate());
		setSearchTypeFilter(u.getSearchTypeFilter());
		setShowAddTicketHelp(u.getShowAddTicketHelp());
		setShowPopupOnClosure(u.getShowPopupOnClosure());
		setShowTicketAfterClosure(u.getShowTicketAfterClosure());
		setStartPage(u.getStartPage());
		setStoredControlPanelOrder(u.getStoredControlPanelOrder());
		setEmail(u.getEmail());
		setSearchDate1(u.getSearchDate1());
		setSearchDate2(u.getSearchDate2());
		setDepartmentSelectionContextTime(u.getDepartmentSelectionContextTime());
		setExpirationMonitoring(u.getExpirationMonitoring());
	}

	/**
	 * Bean constructor.
	 * @param id
	 * @param authType
	 * @param realId
	 */
	public User(
			final String id,
			final String authType,
			final String realId) {
		this();
		this.id = id;
		this.authType = authType;
		this.realId = realId;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		return id.equals(((User) obj).getId());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * @param u
	 * @return an integer.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final User u) {
		if (u == null) {
			return 0;
		}
		return getId().compareTo(u.getId());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String passwordString = null;
		if (!localUser) {
			passwordString = "********";
		}
		return getClass().getSimpleName() + "#" + hashCode() + "["
		+ "id=[" + id + "]"
		+ ", displayName=[" + displayName + "]"
		+ ", email=[" + email + "]"
		+ ", localUser=[" + localUser + "]"
		+ ", password=[" + passwordString + "]"
		+ ", admin=[" + admin + "]"
		+ ", language=[" + language + "]"
		+ ", searchDepartmentFilter=" + searchDepartmentFilter
		+ ", searchTypeFilter=[" + searchTypeFilter + "]"
		+ "]";
	}

	/**
	 * @return  the id of the user.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(final String id) {
		this.id = StringUtils.nullIfEmpty(id);
	}

	/**
	 * @return the authType
	 */
	public String getAuthType() {
		return authType;
	}

	/**
	 * @param authType the authType to set
	 */
	public void setAuthType(final String authType) {
		this.authType = authType;
	}

	/**
	 * @return the realId
	 */
	public String getRealId() {
		return realId;
	}

	/**
	 * @param realId the realId to set
	 */
	public void setRealId(final String realId) {
		this.realId = realId;
	}

    /**
	 * @return  Returns the displayName.
	 */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
	 * @param displayName  The displayName to set.
	 */
    public void setDisplayName(final String displayName) {
        this.displayName = StringUtils.nullIfEmpty(displayName);
    }

    /**
	 * @param admin  The admin to set.
	 */
    public void setAdmin(final boolean admin) {
        this.admin = admin;
    }
    /**
	 * @return  Returns the admin.
	 */
    public boolean getAdmin() {
        return this.admin;
    }

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(final String language) {
		this.language = StringUtils.nullIfEmpty(language);
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * @return the controlPanelUserDepartmentFilter
	 */
	public Department getControlPanelUserDepartmentFilter() {
		return controlPanelUserDepartmentFilter;
	}

	/**
	 * @param controlPanelUserDepartmentFilter the controlPanelUserDepartmentFilter to set
	 */
	public void setControlPanelUserDepartmentFilter(
			final Department controlPanelUserDepartmentFilter) {
		this.controlPanelUserDepartmentFilter = controlPanelUserDepartmentFilter;
	}

	/**
	 * @return the controlPanelManagerDepartmentFilter
	 */
	public Department getControlPanelManagerDepartmentFilter() {
		return controlPanelManagerDepartmentFilter;
	}

	/**
	 * @param controlPanelManagerDepartmentFilter the controlPanelManagerDepartmentFilter to set
	 */
	public void setControlPanelManagerDepartmentFilter(
			final Department controlPanelManagerDepartmentFilter) {
		this.controlPanelManagerDepartmentFilter = controlPanelManagerDepartmentFilter;
	}

	/**
	 * @return the controlPanelCategoryFilter
	 */
	public Category getControlPanelCategoryFilter() {
		if (controlPanelManagerDepartmentFilter == null && controlPanelUserDepartmentFilter == null) {
			return null;
		}
		return controlPanelCategoryFilter;
	}

	/**
	 * @param controlPanelCategoryFilter the controlPanelCategoryFilter to set
	 */
	public void setControlPanelCategoryFilter(final Category controlPanelCategoryFilter) {
		this.controlPanelCategoryFilter = controlPanelCategoryFilter;
	}

	/**
	 * @return the controlPanelUserStatusFilter
	 */
	public String getControlPanelUserStatusFilter() {
		if (controlPanelUserStatusFilter == null) {
			return ControlPanel.STATUS_FILTER_ANY;
		}
		return controlPanelUserStatusFilter;
	}

	/**
	 * @param controlPanelUserStatusFilter the controlPanelUserStatusFilter to set
	 */
	public void setControlPanelUserStatusFilter(final String controlPanelUserStatusFilter) {
		this.controlPanelUserStatusFilter = controlPanelUserStatusFilter;
	}

	/**
	 * @return the controlPanelManagerStatusFilter
	 */
	public String getControlPanelManagerStatusFilter() {
		if (controlPanelManagerStatusFilter == null) {
			return ControlPanel.STATUS_FILTER_ANY;
		}
		return controlPanelManagerStatusFilter;
	}

	/**
	 * @param controlPanelManagerStatusFilter the controlPanelManagerStatusFilter to set
	 */
	public void setControlPanelManagerStatusFilter(final String controlPanelManagerStatusFilter) {
		this.controlPanelManagerStatusFilter = controlPanelManagerStatusFilter;
	}

	/**
	 * @return the controlPanelUserInterface
	 */
	public Boolean getControlPanelUserInterface() {
		if (controlPanelUserInterface == null) {
			return true;
		}
		return controlPanelUserInterface;
	}

	/**
	 * @param controlPanelUserInterface the controlPanelUserInterface to set
	 */
	public void setControlPanelUserInterface(final Boolean controlPanelUserInterface) {
		this.controlPanelUserInterface = controlPanelUserInterface;
	}

	/**
	 * @return the controlPanelManagerInvolvementFilter
	 */
	public String getControlPanelManagerInvolvementFilter() {
		return controlPanelManagerInvolvementFilter;
	}

	/**
	 * @param controlPanelManagerInvolvementFilter the controlPanelManagerInvolvementFilter to set
	 */
	public void setControlPanelManagerInvolvementFilter(
			final String controlPanelManagerInvolvementFilter) {
		this.controlPanelManagerInvolvementFilter = controlPanelManagerInvolvementFilter;
	}

	/**
	 * @return the controlPanelUserInvolvementFilter
	 */
	public String getControlPanelUserInvolvementFilter() {
		return controlPanelUserInvolvementFilter;
	}

	/**
	 * @param controlPanelUserInvolvementFilter the controlPanelUserInvolvementFilter to set
	 */
	public void setControlPanelUserInvolvementFilter(
			final String controlPanelUserInvolvementFilter) {
		this.controlPanelUserInvolvementFilter = controlPanelUserInvolvementFilter;
	}

	/**
	 * @return the searchDepartmentFilter
	 */
	public Department getSearchDepartmentFilter() {
		return searchDepartmentFilter;
	}

	/**
	 * @param searchDepartmentFilter the searchDepartmentFilter to set
	 */
	public void setSearchDepartmentFilter(final Department searchDepartmentFilter) {
		this.searchDepartmentFilter = searchDepartmentFilter;
	}

	/**
	 * @return the searchTypeFilter
	 */
	public String getSearchTypeFilter() {
		return searchTypeFilter;
	}

	/**
	 * @param searchTypeFilter the searchTypeFilter to set
	 */
	public void setSearchTypeFilter(final String searchTypeFilter) {
		this.searchTypeFilter = searchTypeFilter;
	}

	/**
	 * @return the controlPanelPageSize
	 */
	public int getControlPanelPageSize() {
		return controlPanelPageSize;
	}

	/**
	 * @param controlPanelPageSize the controlPanelPageSize to set
	 */
	public void setControlPanelPageSize(final int controlPanelPageSize) {
		this.controlPanelPageSize = controlPanelPageSize;
	}

	/**
	 * @return the authLimit
	 */
	public Timestamp getAuthLimit() {
		return authLimit;
	}

	/**
	 * @param authLimit the authLimit to set
	 */
	public void setAuthLimit(final Timestamp authLimit) {
		this.authLimit = authLimit;
	}

	/**
	 * @return the authSecret
	 */
	public String getAuthSecret() {
		return authSecret;
	}

	/**
	 * @param authSecret the authSecret to set
	 */
	public void setAuthSecret(final String authSecret) {
		this.authSecret = authSecret;
	}

	/**
	 * @return the ownerMonitoring
	 */
	public Boolean getOwnerMonitoring() {
		if (ownerMonitoring == null) {
			return true;
		}
		return ownerMonitoring;
	}

	/**
	 * @param ownerMonitoring the ownerMonitoring to set
	 */
	public void setOwnerMonitoring(final Boolean ownerMonitoring) {
		this.ownerMonitoring = ownerMonitoring;
	}

	/**
	 * @return the invitedMonitoring
	 */
	public Boolean getInvitedMonitoring() {
		if (invitedMonitoring == null) {
			return true;
		}
		return invitedMonitoring;
	}

	/**
	 * @param invitedMonitoring the invitedMonitoring to set
	 */
	public void setInvitedMonitoring(final Boolean invitedMonitoring) {
		this.invitedMonitoring = invitedMonitoring;
	}

	/**
	 * @return the startPage
	 */
	public String getStartPage() {
		return startPage;
	}

	/**
	 * @param startPage the startPage to set
	 */
	public void setStartPage(final String startPage) {
		this.startPage = startPage;
	}

	/**
	 * @return the showAddTicketHelp
	 */
	public Boolean getShowAddTicketHelp() {
		if (showAddTicketHelp == null) {
			return true;
		}
		return showAddTicketHelp;
	}

	/**
	 * @param showAddTicketHelp the showAddTicketHelp to set
	 */
	public void setShowAddTicketHelp(final Boolean showAddTicketHelp) {
		this.showAddTicketHelp = showAddTicketHelp;
	}

	/**
	 * @return the showPopupOnClosure
	 */
	public Boolean getShowPopupOnClosure() {
		if (showPopupOnClosure == null) {
			return true;
		}
		return showPopupOnClosure;
	}

	/**
	 * @param showPopupOnClosure the showPopupOnClosure to set
	 */
	public void setShowPopupOnClosure(final Boolean showPopupOnClosure) {
		this.showPopupOnClosure = showPopupOnClosure;
	}

	/**
	 * @return the showTicketAfterClosure
	 */
	public Boolean getShowTicketAfterClosure() {
		if (showTicketAfterClosure == null) {
			return true;
		}
		return showTicketAfterClosure;
	}

	/**
	 * @param showTicketAfterClosure the showPopupOnClosure to set
	 */
	public void setShowTicketAfterClosure(final Boolean showTicketAfterClosure) {
		this.showTicketAfterClosure = showTicketAfterClosure;
	}

	/**
	 * @return the advancedSearch
	 */
	public Boolean getAdvancedSearch() {
		if (advancedSearch == null) {
			return false;
		}
		return advancedSearch;
	}

	/**
	 * @param advancedSearch the advancedSearch to set
	 */
	public void setAdvancedSearch(
			final Boolean advancedSearch) {
		this.advancedSearch = advancedSearch;
	}

	/**
	 * @return the journalPageSize
	 */
	public Integer getJournalPageSize() {
		return journalPageSize;
	}

	/**
	 * @param journalPageSize the journalPageSize to set
	 */
	public void setJournalPageSize(final Integer journalPageSize) {
		this.journalPageSize = journalPageSize;
	}

	/**
	 * @return the journalDepartmentFilter
	 */
	public Department getJournalDepartmentFilter() {
		return journalDepartmentFilter;
	}

	/**
	 * @param journalDepartmentFilter the journalDepartmentFilter to set
	 */
	public void setJournalDepartmentFilter(final Department journalDepartmentFilter) {
		this.journalDepartmentFilter = journalDepartmentFilter;
	}

	/**
	 * @return the searchSortByDate
	 */
	public Boolean getSearchSortByDate() {
		if (searchSortByDate == null) {
			return Boolean.FALSE;
		}
		return searchSortByDate;
	}

	/**
	 * @param searchSortByDate the searchSortByDate to set
	 */
	public void setSearchSortByDate(final Boolean searchSortByDate) {
		this.searchSortByDate = searchSortByDate;
	}

	/**
	 * @return the controlPanelColumns
	 */
	public String getControlPanelColumns() {
		return controlPanelColumns;
	}

	/**
	 * @param controlPanelColumns the controlPanelColumns to set
	 */
	public void setControlPanelColumns(final String controlPanelColumns) {
		this.controlPanelColumns = controlPanelColumns;
	}

	/**
	 * @return the controlPanelCategoryMemberFilter
	 */
	public Boolean getControlPanelCategoryMemberFilter() {
		if (controlPanelCategoryMemberFilter == null) {
			return false;
		}
		return controlPanelCategoryMemberFilter;
	}

	/**
	 * @param controlPanelCategoryMemberFilter the controlPanelCategoryMemberFilter to set
	 */
	public void setControlPanelCategoryMemberFilter(
			final Boolean controlPanelCategoryMemberFilter) {
		this.controlPanelCategoryMemberFilter = controlPanelCategoryMemberFilter;
	}

	/**
	 * @return the receiveTicketReports
	 */
	public Boolean getReceiveTicketReports() {
		if (receiveTicketReports == null) {
			return true;
		}
		return receiveTicketReports;
	}

	/**
	 * @param receiveTicketReports the receiveTicketReports to set
	 */
	public void setReceiveTicketReports(final Boolean receiveTicketReports) {
		this.receiveTicketReports = receiveTicketReports;
	}

	/**
	 * @return the receiveFaqReports
	 */
	public Boolean getReceiveFaqReports() {
		if (receiveFaqReports == null) {
			return true;
		}
		return receiveFaqReports;
	}

	/**
	 * @param receiveFaqReports the receiveFaqReports to set
	 */
	public void setReceiveFaqReports(final Boolean receiveFaqReports) {
		this.receiveFaqReports = receiveFaqReports;
	}

	/**
	 * @return the receiveManagerMonitoring
	 */
	public Boolean getReceiveManagerMonitoring() {
		if (receiveManagerMonitoring == null) {
			return true;
		}
		return receiveManagerMonitoring;
	}

	/**
	 * @param receiveManagerMonitoring the receiveManagerMonitoring to set
	 */
	public void setReceiveManagerMonitoring(final Boolean receiveManagerMonitoring) {
		this.receiveManagerMonitoring = receiveManagerMonitoring;
	}

	/**
	 * @return the storedControlPanelOrder
	 */
	public String getStoredControlPanelOrder() {
		return storedControlPanelOrder;
	}

	/**
	 * @param storedControlPanelOrder the storedControlPanelOrder to set
	 */
	public void setStoredControlPanelOrder(final String storedControlPanelOrder) {
		this.storedControlPanelOrder = storedControlPanelOrder;
	}

	/**
	 * @return the controlPanelOrder
	 */
	public ControlPanelOrder getControlPanelOrder() {
		if (controlPanelOrder == null) {
			controlPanelOrder = new ControlPanelOrder(storedControlPanelOrder);
		}
		return controlPanelOrder;
	}

	/**
	 * @param controlPanelOrder the controlPanelOrder to set
	 */
	public void setControlPanelOrder(final ControlPanelOrder controlPanelOrder) {
		this.controlPanelOrder = controlPanelOrder;
	}

	/**
	 * @return the controlPanelRefreshDelay
	 */
	public Integer getControlPanelRefreshDelay() {
		if (controlPanelRefreshDelay == null) {
			return 0;
		}
		return controlPanelRefreshDelay;
	}

	/**
	 * @param controlPanelRefreshDelay the controlPanelRefreshDelay to set
	 */
	public void setControlPanelRefreshDelay(final Integer controlPanelRefreshDelay) {
		if (controlPanelRefreshDelay == null || controlPanelRefreshDelay <= 0) {
			this.controlPanelRefreshDelay = null;
		}
		this.controlPanelRefreshDelay = controlPanelRefreshDelay;
	}

	/**
	 * @return the receiveTicketReportsAllInOne
	 */
	public Boolean getReceiveTicketReportsAllInOne() {
		if (this.receiveTicketReportsAllInOne == null) {
			return true;
		}
		return receiveTicketReportsAllInOne;
	}

	/**
	 * @param receiveTicketReportsAllInOne the receiveTicketReportsAllInOne to set
	 */
	public void setReceiveTicketReportsAllInOne(final Boolean receiveTicketReportsAllInOne) {
		this.receiveTicketReportsAllInOne = receiveTicketReportsAllInOne;
	}

	/**
	 * @return the pageTransition
	 */
	public String getPageTransition() {
		return pageTransition;
	}

	/**
	 * @param pageTransition the pageTransition to set
	 */
	public void setPageTransition(final String pageTransition) {
		this.pageTransition = pageTransition;
	}

	/**
	 * @return true to show the submit popup image.
	 */
	public boolean isShowSubmitPopupText() {
		return pageTransition == null
		|| PAGE_TRANSITION_TEXT.equals(pageTransition)
		|| PAGE_TRANSITION_TEXT_IMAGE.equals(pageTransition)
		|| PAGE_TRANSITION_FREEZE_TEXT.equals(pageTransition)
		|| PAGE_TRANSITION_FREEZE_TEXT_IMAGE.equals(pageTransition);
	}

	/**
	 * @return true to show the submit popup image.
	 */
	public boolean isShowSubmitPopupImage() {
		return pageTransition == null
		|| PAGE_TRANSITION_IMAGE.equals(pageTransition)
		|| PAGE_TRANSITION_TEXT_IMAGE.equals(pageTransition)
		|| PAGE_TRANSITION_FREEZE_IMAGE.equals(pageTransition)
		|| PAGE_TRANSITION_FREEZE_TEXT_IMAGE.equals(pageTransition);
	}

	/**
	 * @return true to freeze the screen on submit.
	 */
	public boolean isFreezeScreenOnSubmit() {
		return pageTransition == null
		|| PAGE_TRANSITION_FREEZE.equals(pageTransition)
		|| PAGE_TRANSITION_FREEZE_TEXT.equals(pageTransition)
		|| PAGE_TRANSITION_FREEZE_IMAGE.equals(pageTransition)
		|| PAGE_TRANSITION_FREEZE_TEXT_IMAGE.equals(pageTransition);
	}

	/**
	 * @return the encodedAttributes
	 */
	protected String getEncodedAttributes() {
		return encodedAttributes;
	}

	/**
	 * @param encodedAttributes the encodedAttributes to set
	 */
	protected void setEncodedAttributes(final String encodedAttributes) {
		this.encodedAttributes = encodedAttributes;
	}

	/**
	 * @return the encodedAttributes
	 */
	public Map<String, List<String>> getAttributes() {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		if (encodedAttributes == null) {
			return result;
		}
		for (String nameValue : encodedAttributes.split(ATTRIBUTES_ENCODING_SEPARATOR)) {
			String[] parts = nameValue.split(ATTRIBUTES_ENCODING_EQUAL_SIGN);
			if (parts != null && parts.length == 2) {
				String name = StringUtils.nullIfEmpty(parts[0]);
				String value = StringUtils.nullIfEmpty(parts[1]);
				if (name != null && value != null) {
					List<String> values = result.get(name);
					if (values == null) {
						values = new ArrayList<String>();
						result.put(name, values);
					}
					String decodedValue = new String(Base64.decode(value));
					values.add(decodedValue);
				}
			}
		}
		return result;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(final Map<String, List<String>> attributes) {
		encodedAttributes = null;
		if (attributes != null) {
			Set<String> keys = attributes.keySet();
			if (!keys.isEmpty()) {
				String separator = "";
				for (String key : keys) {
					if (key != null) {
						List<String> values = attributes.get(key);
						for (String value : values) {
							if (org.springframework.util.StringUtils.hasText(value)) {
								if (encodedAttributes == null) {
									encodedAttributes = "";
								}
								encodedAttributes +=
									separator + key
									+ ATTRIBUTES_ENCODING_EQUAL_SIGN
									+ Base64.encodeBytes(
                                            value.getBytes(), Base64.DONT_BREAK_LINES);
								separator = ATTRIBUTES_ENCODING_SEPARATOR;
							}
						}
					}
				}
			}
		}
		if (encodedAttributes != null && encodedAttributes.length() >= ATTRIBUTES_ENCODING_MAX_LENGTH) {
			throw new IllegalArgumentException("too many Shibboleth attributes!");
		}
	}

	/**
	 * @return the bookmarkMonitoring
	 */
	public Boolean getBookmarkMonitoring() {
		if (bookmarkMonitoring == null) {
			return true;
		}
		return bookmarkMonitoring;
	}

	/**
	 * @param bookmarkMonitoring the bookmarkMonitoring to set
	 */
	public void setBookmarkMonitoring(final Boolean bookmarkMonitoring) {
		this.bookmarkMonitoring = bookmarkMonitoring;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * @return the searchDate1
	 */
	public Timestamp getSearchDate1() {
		return searchDate1;
	}

	/**
	 * @param searchDate1 the searchDate1 to set
	 */
	public void setSearchDate1(final Timestamp searchDate1) {
		this.searchDate1 = searchDate1;
	}

	/**
	 * @return the searchDate2
	 */
	public Timestamp getSearchDate2() {
		return searchDate2;
	}

	/**
	 * @param searchDate2 the searchDate2 to set
	 */
	public void setSearchDate2(final Timestamp searchDate2) {
		this.searchDate2 = searchDate2;
	}

	/**
	 * @return the departmentSelectionContextTime
	 */
	public Timestamp getDepartmentSelectionContextTime() {
		return departmentSelectionContextTime;
	}

	/**
	 * @param departmentSelectionContextTime the departmentSelectionContextTime to set
	 */
	public void setDepartmentSelectionContextTime(
			final Timestamp departmentSelectionContextTime) {
		this.departmentSelectionContextTime = departmentSelectionContextTime;
	}

	/**
	 * Update the department selection context time.
	 */
	public void updateDepartmentSelectionContextTime() {
		setDepartmentSelectionContextTime(new Timestamp(System.currentTimeMillis()));
		new LoggerImpl(User.class).info("updated the department selection context time for user [" + getId() + "]");
	}

	/**
	 * @return the expirationMonitoring
	 */
	public Boolean getExpirationMonitoring() {
		if (expirationMonitoring == null) {
			return true;
		}
		return expirationMonitoring;
	}

	/**
	 * @param expirationMonitoring the expirationMonitoring to set
	 */
	public void setExpirationMonitoring(final Boolean expirationMonitoring) {
		this.expirationMonitoring = expirationMonitoring;
	}

	public String getControlPanelManagerManagerFilter() {
		return controlPanelManagerManagerFilter;
	}

	public void setControlPanelManagerManagerFilter(String controlPanelManagerManagerFilter) {
		this.controlPanelManagerManagerFilter = controlPanelManagerManagerFilter;
	}

}
