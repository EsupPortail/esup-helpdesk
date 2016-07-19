/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.ldap.LdapException;
import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.web.controllers.LdapSearchCaller;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.portal.ws.client.PortalGroup;
import org.esupportail.portal.ws.client.PortalService;
import org.esupportail.portal.ws.client.PortalUser;
import org.esupportail.portal.ws.client.exceptions.PortalErrorException;
import org.esupportail.portal.ws.client.exceptions.PortalUserNotFoundException;

/**
 * Bean to present utilities.
 */
public class UtilsController extends AbstractContextAwareController implements LdapSearchCaller {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2998142044872273698L;

	/** A constant for the utilities. */
	private static final Integer UTILITY_NONE = 0;
	/** A constant for the utilities. */
	private static final Integer UTILITY_LDAP = 1;
	/** A constant for the utilities. */
	private static final Integer UTILITY_PORTAL = 2;
	/** A constant for the utilities. */
	private static final Integer UTILITY_USER_INFO = 3;

	/**
	 * The LDAP service.
	 */
	private LdapUserService ldapUserService;

	/**
	 * The portal service.
	 */
	private PortalService portalService;

	/**
	 * The utility.
	 */
	private int utility;

	/**
	 * The testUser id.
	 */
	private String ldapUid;

	/**
	 * The (resolved) testUser.
	 */
	private User testUser;

	/**
	 * The LDAP user.
	 */
	private LdapUser ldapUser;

	/**
	 * The portal user.
	 */
	private PortalUser portalUser;

	/**
	 * The portal groups.
	 */
	private List<PortalGroup> portalGroups;

	/**
	 * The user info.
	 */
	private String userInfo;

	/**
	 * Bean constructor.
	 */
	public UtilsController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.notNull(ldapUserService,
				"property ldapUserService of class " + getClass().getName()
				+ " can not be null");
		Assert.notNull(portalService,
				"property portalService of class " + getClass().getName()
				+ " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		utility = UTILITY_NONE;
		ldapUid = null;
		resetResults();
	}

	/**
	 * Reset the results.
	 */
	public void resetResults() {
		testUser = null;
		ldapUser = null;
		portalUser = null;
		portalGroups = null;
		userInfo = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[ldapUid=[" + ldapUid + "]"
		+ ", utility=[" + utility + "]"
		+ "]";
	}

	/**
	 * @return true if the current testUser is allowed to access the view.
	 */
	@RequestCache
	public boolean isPageAuthorized() {
		if (getCurrentUser() == null) {
			return false;
		}
		if (getCurrentUser().getAdmin()) {
			return true;
		}
		if (!getDomainService().isDepartmentManager(getCurrentUser())) {
			return false;
		}
		return true;
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
		test();
		return "navigationUtils";
	}

	/**
	 * @return the utilitiesItems
	 */
	@RequestCache
	public List<SelectItem> getUtilitiesItems() {
		List<SelectItem> utilitiesItems = new ArrayList<SelectItem>();
		utilitiesItems.add(new SelectItem(
				UTILITY_NONE,
				getString("UTILS.TEXT.UTILITY.NONE")));
		if (getDomainService().isUseLdap()) {
			utilitiesItems.add(new SelectItem(
					UTILITY_LDAP,
					getString("UTILS.TEXT.UTILITY.LDAP")));
		}
		utilitiesItems.add(new SelectItem(
				UTILITY_PORTAL,
				getString("UTILS.TEXT.UTILITY.PORTAL")));
		utilitiesItems.add(new SelectItem(
				UTILITY_USER_INFO,
				getString("UTILS.TEXT.UTILITY.USER_INFO")));
		return utilitiesItems;
	}

	/**
	 * Resolve the user.
	 * @return true if resolved.
	 */
	protected boolean resolveTestUser() {
		testUser = null;
		try {
			if (ldapUid == null) {
				addErrorMessage(null, "UTILS.MESSAGE.ENTER_ID");
				return false;
			}
			testUser = getUserStore().getUserFromRealId(ldapUid);
			return true;
		} catch (UserNotFoundException e) {
			addErrorMessage(null, "_.MESSAGE.USER_NOT_FOUND", ldapUid);
			return false;
		}
	}

	/**
	 * Test LDAP.
	 */
	protected void testLdap() {
		ldapUser = null;
		if (!resolveTestUser()) {
			return;
		}
		if (!getDomainService().getUserStore().isCasUser(testUser)) {
			addErrorMessage(null, "UTILS.MESSAGE.NOT_CAS_USER", testUser.getId());
			return;
		}
		try {
			ldapUser = ldapUserService.getLdapUser(testUser.getRealId());
		} catch (LdapException e) {
			addErrorMessage(null, "UTILS.MESSAGE.LDAP_ERROR", e.getMessage());
		} catch (UserNotFoundException e) {
			addErrorMessage(null, "UTILS.MESSAGE.LDAP_USER_NOT_FOUND", testUser.getRealId());
		}
	}

	/**
	 * Test the portal.
	 */
	protected void testPortal() {
		portalUser = null;
		portalGroups = null;
		if (!resolveTestUser()) {
			return;
		}
		if (getDomainService().getUserStore().isApplicationUser(testUser)) {
			addErrorMessage(null, "UTILS.MESSAGE.APPLICATION_USER", testUser.getId());
			return;
		}
		try {
			portalUser = portalService.getUser(testUser.getId());
			portalGroups = portalService.getUserGroups(portalUser);
		} catch (PortalErrorException e) {
			addErrorMessage(null, "UTILS.MESSAGE.PORTAL_ERROR", e.getMessage());
			return;
		} catch (PortalUserNotFoundException e) {
			addErrorMessage(null, "UTILS.MESSAGE.PORTAL_USER_NOT_FOUND", testUser.getId());
			return;
		}
	}

	/**
	 * Test the user info retrieval.
	 */
	protected void testUserInfo() {
		userInfo = null;
		if (!resolveTestUser()) {
			return;
		}
		userInfo = getDomainService().getUserInfo(testUser, getLocale());
	}

	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String test() {
		if (utility == UTILITY_LDAP) {
			testLdap();
		}
		if (utility == UTILITY_PORTAL) {
			testPortal();
		}
		if (utility == UTILITY_USER_INFO) {
			testUserInfo();
		}
		return "tested";
	}

	/**
	 * @see org.esupportail.commons.web.controllers.LdapSearchCaller#getLdapUid()
	 */
	@Override
	public String getLdapUid() {
		return ldapUid;
	}

	/**
	 * @see org.esupportail.commons.web.controllers.LdapSearchCaller#setLdapUid(java.lang.String)
	 */
	@Override
	public void setLdapUid(final String ldapUid) {
		this.ldapUid = StringUtils.nullIfEmpty(ldapUid);
	}

	/**
	 * @param ldapUserService the ldapUserService to set
	 */
	public void setLdapUserService(final LdapUserService ldapUserService) {
		this.ldapUserService = ldapUserService;
	}

	/**
	 * @return the ldapUserService
	 */
	protected LdapUserService getLdapUserService() {
		return ldapUserService;
	}

	/**
	 * @return the utility
	 */
	public int getUtility() {
		return utility;
	}

	/**
	 * @param utility the utility to set
	 */
	public void setUtility(final int utility) {
		this.utility = utility;
	}

	/**
	 * @return the testUser
	 */
	public User getTestUser() {
		return testUser;
	}

	/**
	 * @param testUser the testUser to set
	 */
	protected void setTestUser(final User testUser) {
		this.testUser = testUser;
	}

	/**
	 * @return the ldapUser
	 */
	public LdapUser getLdapUser() {
		return ldapUser;
	}

	/**
	 * @return the number of attributes for ldapUser
	 */
	public int getLdapUserAttributesNumber() {
		if (ldapUser == null) {
			return -1;
		}
		return ldapUser.getAttributeNames().size();
	}

	/**
	 * @param ldapUser the ldapUser to set
	 */
	protected void setLdapUser(final LdapUser ldapUser) {
		this.ldapUser = ldapUser;
	}

	/**
	 * @return the portalService
	 */
	public PortalService getPortalService() {
		return portalService;
	}

	/**
	 * @param portalService the portalService to set
	 */
	public void setPortalService(final PortalService portalService) {
		this.portalService = portalService;
	}

	/**
	 * @return the portalUser
	 */
	public PortalUser getPortalUser() {
		return portalUser;
	}

	/**
	 * @return the number of attributes for portalUser
	 */
	public int getPortalUserAttributesNumber() {
		if (portalUser == null) {
			return -1;
		}
		return portalUser.getAttributeNames().size();
	}

	/**
	 * @param portalUser the portalUser to set
	 */
	protected void setPortalUser(final PortalUser portalUser) {
		this.portalUser = portalUser;
	}

	/**
	 * @return the portalGroups
	 */
	public List<PortalGroup> getPortalGroups() {
		return portalGroups;
	}

	/**
	 * @return the number of portal groups
	 */
	public int getPortalGroupsNumber() {
		if (portalGroups == null) {
			return -1;
		}
		return portalGroups.size();
	}

	/**
	 * @param portalGroups the portalGroups to set
	 */
	protected void setPortalGroups(final List<PortalGroup> portalGroups) {
		this.portalGroups = portalGroups;
	}

	/**
	 * @return the userInfo
	 */
	public String getUserInfo() {
		return userInfo;
	}

	/**
	 * @param userInfo the userInfo to set
	 */
	protected void setUserInfo(final String userInfo) {
		this.userInfo = userInfo;
	}

}
