/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.controllers;

import java.util.List;

import org.esupportail.commons.beans.AbstractApplicationAwareBean;
import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.utils.Assert;
import org.springframework.util.StringUtils;

/**
 * A visual bean used to search LDAP directories.
 */
public class LdapSearchController extends AbstractApplicationAwareBean implements Resettable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -1144837446399018339L;

	/**
	 * see {@link LdapUserService}.
	 */
	private LdapUserService ldapUserService;

	/**
	 * The beans that called the LDAP search.
	 */
	private LdapSearchCaller caller;

	/**
	 * The string to search.
	 */
	private String searchInput; 

	/**
	 * the result of the search, as a list of LdapUser.
	 */
	private List<LdapUser> ldapUsers;

	/**
	 * the selection.
	 */
	private LdapUser selectedUser;

	/**
	 * The String that will be returned when the search is successful.
	 */
	private String successResult;
	
	/**
	 * The String that will be returned when the search is canceled.
	 */
	private String cancelResult;
	
	/**
	 * Bean constructor.
	 */
	public LdapSearchController() {
		super();
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	@Override
	public void reset() {
		caller = null;
		searchInput = null;
		ldapUsers = null;
		selectedUser = null;
		successResult = null;
		cancelResult = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[caller=" + caller
		+ ", searchInput=[" + searchInput + "], selectedUser=" + selectedUser
		+ ", successResult=[" + successResult + "], cancelResult=[" + cancelResult + "]]";
	}

	/**
	 * @see org.esupportail.commons.beans.AbstractApplicationAwareBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(ldapUserService, 
				"property ldapUserService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * Search the LDAP directory.
	 * @param firstSearch true to print a message if the input is empty
	 * @return a String.
	 */
	protected String search(final boolean firstSearch) {
		if (!StringUtils.hasText(searchInput)) {
			if (!firstSearch) {
				addErrorMessage("ldapSearchForm:searchInput", "LDAP_SEARCH.MESSAGE.EMPTY", searchInput);
			}
			return "ldapSearch";
		}
		this.ldapUsers = this.ldapUserService.getLdapUsersFromToken(searchInput);
		if (this.ldapUsers.isEmpty()) {
			addWarnMessage("ldapSearchForm:searchInput", "LDAP_SEARCH.MESSAGE.NO_RESULT", searchInput);
			return "ldapSearch";
		}
		// remove unneeded attributes
		for (LdapUser ldapUser : ldapUsers) {
			for (String attributeName : ldapUser.getAttributeNames()) {
				if (!ldapUserService.getSearchDisplayedAttributes().contains(attributeName)) {
					ldapUser.getAttributes().remove(attributeName);
				}
			}
		}
		return "ldapChoose";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String search() {
		return search(false);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String firstSearch() {
		return search(true);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String selectUser() {
		if (caller == null) {
			throw new UnsupportedOperationException("caller is null");
		}
		caller.setLdapUid(selectedUser.getId());
		if (successResult == null) {
			throw new UnsupportedOperationException("successResult is null");
		}
		String result = successResult;
		reset();
		return result;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String cancel() {
		if (caller == null) {
			throw new UnsupportedOperationException("caller is null");
		}
		if (cancelResult == null) {
			throw new UnsupportedOperationException("cancelResult is null");
		}
		String result = cancelResult;
		reset();
		return result;
	}

	/**
	 * @param ldapUserService
	 */
	public void setLdapUserService(final LdapUserService ldapUserService) {
		this.ldapUserService = ldapUserService;
	}

	/**
	 * @param searchInput the searchInput to set
	 */
	public void setSearchInput(final String searchInput) {
		this.searchInput = searchInput;
	}

	/**
	 * 
	 * @return searchInput
	 */
	public String getSearchInput() {
		return this.searchInput;
	}

	/**
	 * @return the ldapUsers
	 */
	public List<LdapUser> getLdapUsers() {
		return this.ldapUsers;
	}

	/**
	 * @param selectedUser the selectedUser to set
	 */
	public void setSelectedUser(final LdapUser selectedUser) {
		this.selectedUser = selectedUser;
	}

	/**
	 * @param caller the caller to set
	 */
	public void setCaller(final LdapSearchCaller caller) {
		this.caller = caller;
		this.searchInput = caller.getLdapUid();
	}

	/**
	 * @param cancelResult the cancelResult to set
	 */
	public void setCancelResult(final String cancelResult) {
		this.cancelResult = cancelResult;
	}

	/**
	 * @param successResult the successResult to set
	 */
	public void setSuccessResult(final String successResult) {
		this.successResult = successResult;
	}
	
}
