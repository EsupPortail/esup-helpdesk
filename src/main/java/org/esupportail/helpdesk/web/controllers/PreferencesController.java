/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.HttpUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.services.statistics.StatisticsUtils;
import org.esupportail.helpdesk.web.beans.TicketMonitoringI18nKeyProvider;

/**
 * A bean to manage user preferences.
 */
public class PreferencesController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8413538915740558036L;

	/**
	 * The prefix of i18n values for start pages.
	 */
	private static final String START_PAGE_PREFIX = "DOMAIN.START_PAGE.";

	/**
	 * The prefix of i18n values for page transitions.
	 */
	private static final String PAGE_TRANSITION_PREFIX = "DOMAIN.PAGE_TRANSITION.";

	/**
	 * The control panel default refresh delays.
	 */
	private static final String DEFAULT_CONTROL_PANEL_REFRESH_DELAYS = "1,2,5";

	/**
	 * A list of JSF components for the locales.
	 */
	private static List<SelectItem> localeItems;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(this.getClass());

	/**
	 * The department manager to update.
	 */
	private DepartmentManager departmentManagerToUpdate;

	/**
	 * The old password.
	 */
	private String oldPassword;

	/**
	 * The new password.
	 */
	private String newPassword1;

	/**
	 * The new password.
	 */
	private String newPassword2;

	/**
	 * The control panel refresh delays.
	 */
	private List<Integer> controlPanelRefreshDelays;

	/**
	 * Bean constructor.
	 */
	@SuppressWarnings("unchecked")
	public PreferencesController() {
		super();
		localeItems = new ArrayList<SelectItem>();
		Iterator<Locale> iter =
			FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
		while (iter.hasNext()) {
			Locale locale = iter.next();
			localeItems.add(new SelectItem(
					locale, locale.getLanguage() + " - "
					+ locale.getDisplayLanguage(locale)));
		}
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		if (controlPanelRefreshDelays == null) {
			setControlPanelRefreshDelays(DEFAULT_CONTROL_PANEL_REFRESH_DELAYS);
		}
	}

	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			return null;
		}
		getSessionController().setShowShortMenu(false);
		return "navigationPreferences";
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		departmentManagerToUpdate = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode();
	}

	/**
	 * @return true if the current user is allowed to view the page.
	 */
	public boolean isPageAuthorized() {
		return getCurrentUser() != null;
	}

	/**
	 * @return the user agent.
	 */
	public String getUserAgent() {
		return HttpUtils.getUserAgent();
	}

	/**
	 * @return true if the current user is a manager.
	 */
	public boolean isUserManager() {
		User user = getCurrentUser();
		if (user == null) {
			return false;
		}
		return getDomainService().isDepartmentManager(user);
	}

	/**
	 * @return true if the current user is a manager or administrator.
	 */
	public boolean isUserManagerOrAdmin() {
		User user = getCurrentUser();
		if (user == null) {
			return false;
		}
		return user.getAdmin() || getDomainService().isDepartmentManager(user);
	}

	/**
	 * @return the localeItems
	 */
	public List<SelectItem> getLocaleItems() {
		return localeItems;
	}

	/**
	 * Add a start page item.
	 * @param startPageItems
	 * @param item
	 */
	public void addStartPageItem(
			final List<SelectItem> startPageItems,
			final String item) {
		startPageItems.add(new SelectItem(item, getString(START_PAGE_PREFIX + item)));
	}

	/**
	 * @return the startPageItems
	 */
	public List<SelectItem> getStartPageItems() {
		List<SelectItem> startPageItems = new ArrayList<SelectItem>();
		addStartPageItem(startPageItems, User.START_PAGE_WELCOME);
		addStartPageItem(startPageItems, User.START_PAGE_BOOKMARKS);
		addStartPageItem(startPageItems, User.START_PAGE_FAQ);
		addStartPageItem(startPageItems, User.START_PAGE_CONTROL_PANEL);
		addStartPageItem(startPageItems, User.START_PAGE_SEARCH);
		addStartPageItem(startPageItems, User.START_PAGE_PREFERENCES);
		addStartPageItem(startPageItems, User.START_PAGE_ABOUT);
		if (getDomainService().isDepartmentManager(getCurrentUser())) {
			addStartPageItem(startPageItems, User.START_PAGE_JOURNAL);
			addStartPageItem(startPageItems, User.START_PAGE_STATISTICS);
		}
		return startPageItems;
	}

	/**
	 * Add a page transition item.
	 * @param pageTransitionItems
	 * @param item
	 */
	public void addPageTransitionItem(
			final List<SelectItem> pageTransitionItems,
			final String item) {
		pageTransitionItems.add(new SelectItem(item, getString(PAGE_TRANSITION_PREFIX + item)));
	}

	/**
	 * @return the startPageItems
	 */
	public List<SelectItem> getPageTransitionItems() {
		List<SelectItem> pageTransitionItems = new ArrayList<SelectItem>();
		addPageTransitionItem(pageTransitionItems, User.PAGE_TRANSITION_FREEZE_TEXT_IMAGE);
		addPageTransitionItem(pageTransitionItems, User.PAGE_TRANSITION_FREEZE_TEXT);
		addPageTransitionItem(pageTransitionItems, User.PAGE_TRANSITION_FREEZE_IMAGE);
		addPageTransitionItem(pageTransitionItems, User.PAGE_TRANSITION_FREEZE);
		addPageTransitionItem(pageTransitionItems, User.PAGE_TRANSITION_TEXT_IMAGE);
		addPageTransitionItem(pageTransitionItems, User.PAGE_TRANSITION_TEXT);
		addPageTransitionItem(pageTransitionItems, User.PAGE_TRANSITION_IMAGE);
		addPageTransitionItem(pageTransitionItems, User.PAGE_TRANSITION_NONE);
		return pageTransitionItems;
	}

	/**
	 * Add a report time item.
	 * @param reportTimeItems
	 * @param item
	 */
	public void addReportTimeItem(
			final List<SelectItem> reportTimeItems,
			final Integer item) {
		reportTimeItems.add(new SelectItem(
				item, getString("MANAGER_PREFERENCES.REPORT.TIME_ITEM.VALUE", item)));
	}

	/**
	 * @return the reportTimeItems
	 */
	public List<SelectItem> getReportTimeItems() {
		List<SelectItem> reportTimeItems = new ArrayList<SelectItem>();
		reportTimeItems.add(new SelectItem(
				-1, getString("MANAGER_PREFERENCES.REPORT.TIME_ITEM.NONE")));
		for (int i = 0; i < StatisticsUtils.HOURS_PER_DAY; i++) {
			addReportTimeItem(reportTimeItems, i);
		}
		return reportTimeItems;
	}

	/**
	 * Add a control panel refresh delay item.
	 * @param controlPanelRefreshDelayItems
	 * @param item
	 */
	public void addControlPanelRefreshDelayItem(
			final List<SelectItem> controlPanelRefreshDelayItems,
			final Integer item) {
		String i18nString = "PREFERENCES.CONTROL_PANEL_REFRESH_DELAY.";
		if (item == 0) {
			i18nString += "NEVER";
		} else if (item == 1) {
			i18nString += "ONE";
		} else {
			i18nString += "MORE";
		}
		controlPanelRefreshDelayItems.add(new SelectItem(
				item, getString(i18nString, item)));
	}

	/**
	 * @return the controlPanelRefreshDelayItems
	 */
	public List<SelectItem> getControlPanelRefreshDelayItems() {
		List<SelectItem> controlPanelRefreshDelayItems = new ArrayList<SelectItem>();
		addControlPanelRefreshDelayItem(controlPanelRefreshDelayItems, 0);
		for (Integer value : getControlPanelRefreshDelays()) {
			addControlPanelRefreshDelayItem(controlPanelRefreshDelayItems, value);
		}
		return controlPanelRefreshDelayItems;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(final Locale locale) {
		User currentUser = getCurrentUser();
		if (currentUser == null) {
			// store in the session
			setSessionLocale(locale);
		} else {
			// update the current user
			if (logger.isDebugEnabled()) {
				logger.debug("set language [" + locale + "] for user '" + currentUser.getId() + "'");
			}
			currentUser.setLanguage(locale.toString());
			setSessionLocale(locale);
			addInfoMessage(null, "PREFERENCES.MESSAGE.UPDATED");
		}
	}

	/**
	 * JSF callback.
	 */
	public void updateUser() {
		User currentUser = getCurrentUser();
		getDomainService().updateUser(currentUser);
		getSessionController().resetSessionLocale();
	}

	/**
	 * Add a ticket monitoring select item.
	 * @param ticketMonitoringItems
	 * @param key
	 */
	public void addTicketMonitoringItems(
			final List<SelectItem> ticketMonitoringItems,
			final int key) {
		ticketMonitoringItems.add(new SelectItem(
				new Integer(key),
				getString(TicketMonitoringI18nKeyProvider.getI18nKey(key))));
	}

	/**
	 * @return the localeItems
	 */
	public List<SelectItem> getTicketMonitoringItems() {
		List<SelectItem> ticketMonitoringItems = new ArrayList<SelectItem>();
		addTicketMonitoringItems(
				ticketMonitoringItems, DepartmentManager.TICKET_MONITORING_ALWAYS);
		addTicketMonitoringItems(
				ticketMonitoringItems, DepartmentManager.TICKET_MONITORING_CREATION);
		addTicketMonitoringItems(
				ticketMonitoringItems, DepartmentManager.TICKET_MONITORING_NEVER);
		return ticketMonitoringItems;
	}

	/**
	 * @return a permanent link to the page for application users.
	 */
	public String getApplicationPermLink() {
		return getUrlBuilder().getPreferencesUrl(AuthUtils.APPLICATION);
	}

	/**
	 * @return a permanent link to the page for CAS users.
	 */
	public String getCasPermLink() {
		return getUrlBuilder().getPreferencesUrl(AuthUtils.CAS);
	}

	/**
	 * @return a permanent link to the page for Shibboleth users.
	 */
	public String getShibbolethPermLink() {
		return getUrlBuilder().getPreferencesUrl(AuthUtils.SHIBBOLETH);
	}

	/**
	 * @return a permanent link to the page for specific users.
	 */
	public String getSpecificPermLink() {
		return getUrlBuilder().getPreferencesUrl(AuthUtils.SPECIFIC);
	}

	/**
	 * @return the department managers that correspond to the current user.
	 */
	@RequestCache
	public List<DepartmentManager> getDepartmentManagers() {
		return getDomainService().getDepartmentManagers(getCurrentUser());
	}

	/**
	 * JSF callback.
	 */
	public void updateManager() {
		getDomainService().updateDepartmentManager(departmentManagerToUpdate);
		addInfoMessage(null, "PREFERENCES.MESSAGE.UPDATED");
	}

	/**
	 * JSF callback.
	 */
	public void updateManagerAndTestReport() {
		getDomainService().updateDepartmentManager(departmentManagerToUpdate);
		getDomainService().sendTicketReport(departmentManagerToUpdate);
		addInfoMessage(null, "MANAGER_PREFERENCES.MESSAGE.REPORT_SENT");
	}

	/**
	 * JSF callback.
	 */
	public void toggleTicketReports() {
		User user = getCurrentUser();
		user.setReceiveTicketReports(!user.getReceiveTicketReports());
		getDomainService().updateUser(user);
		addInfoMessage(null, "PREFERENCES.MESSAGE.UPDATED");
	}

	/**
	 * JSF callback.
	 */
	public void toggleFaqReports() {
		User user = getCurrentUser();
		user.setReceiveFaqReports(!user.getReceiveFaqReports());
		getDomainService().updateUser(user);
		addInfoMessage(null, "PREFERENCES.MESSAGE.UPDATED");
	}

	/**
	 * JSF callback.
	 */
	public void toggleMonitoring() {
		User user = getCurrentUser();
		user.setReceiveManagerMonitoring(!user.getReceiveManagerMonitoring());
		getDomainService().updateUser(user);
		addInfoMessage(null, "PREFERENCES.MESSAGE.UPDATED");
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String gotoChangePassword() {
		if (!getDomainService().getUserStore().isApplicationUser(getCurrentUser())) {
			return null;
		}
		return "changePassword";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String doChangePassword() {
		if (oldPassword == null) {
			addErrorMessage(null, "PREFERENCES.MESSAGE.ENTER_OLD_PASSWORD");
			gotoChangePassword();
			return null;
		}
		if (newPassword1 == null || newPassword2 == null || !newPassword1.equals(newPassword2)) {
			addErrorMessage(null, "PREFERENCES.MESSAGE.ENTER_NEW_PASSWORD_TWICE");
			gotoChangePassword();
			return null;
		}
		if (!oldPassword.equals(getCurrentUser().getPassword())) {
			addErrorMessage(null, "PREFERENCES.MESSAGE.OLD_PASSWORD_ERROR");
			gotoChangePassword();
			return null;
		}
		getCurrentUser().setPassword(newPassword1);
		getDomainService().updateUser(getCurrentUser());
		oldPassword = null;
		newPassword1 = null;
		newPassword2 = null;
		addErrorMessage(null, "PREFERENCES.MESSAGE.PASSWORD_CHANGED");
		return "passwordChanged";
	}

	/**
	 * @param departmentManagerToUpdate the departmentManagerToUpdate to set
	 */
	public void setDepartmentManagerToUpdate(
			final DepartmentManager departmentManagerToUpdate) {
		this.departmentManagerToUpdate = departmentManagerToUpdate;
	}

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(final String oldPassword) {
		this.oldPassword = StringUtils.nullIfEmpty(oldPassword);
	}

	/**
	 * @return the newPassword1
	 */
	public String getNewPassword1() {
		return newPassword1;
	}

	/**
	 * @param newPassword1 the newPassword1 to set
	 */
	public void setNewPassword1(final String newPassword1) {
		this.newPassword1 = StringUtils.nullIfEmpty(newPassword1);
	}

	/**
	 * @return the newPassword2
	 */
	public String getNewPassword2() {
		return newPassword2;
	}

	/**
	 * @param newPassword2 the newPassword2 to set
	 */
	public void setNewPassword2(final String newPassword2) {
		this.newPassword2 = StringUtils.nullIfEmpty(newPassword2);
	}

	/**
	 * @return the controlPanelRefreshDelays
	 */
	protected List<Integer> getControlPanelRefreshDelays() {
		return controlPanelRefreshDelays;
	}

	/**
	 * @param controlPanelRefreshDelaysString the controlPanelRefreshDelays to set
	 */
	public void setControlPanelRefreshDelays(final String controlPanelRefreshDelaysString) {
		controlPanelRefreshDelays = new ArrayList<Integer>();
		for (String stringValue : controlPanelRefreshDelaysString.split(",")) {
			Integer intValue = Integer.parseInt(stringValue);
			if (!controlPanelRefreshDelays.contains(intValue)) {
				controlPanelRefreshDelays.add(intValue);
			}
		}
	}

}
