/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.web.beans.Paginator;
import org.esupportail.commons.web.controllers.LdapSearchCaller;
import org.esupportail.helpdesk.domain.beans.Icon;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.services.indexing.Indexer;

/**
 * Bean to present and manage administrators.
 */
public class AdministratorsController extends AbstractContextAwareController implements LdapSearchCaller {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7382184361261066040L;

	/**
	 * The id of the user to give administrator privileges.
	 */
	private String ldapUid;

	/**
	 * The user of whom the administrator's privileges will be revoked.
	 */
	private User userToDelete;

	/**
	 * The LDAP service.
	 */
	private LdapUserService ldapUserService;

    /**
     * The paginator.
     */
    private Paginator<User> paginator;

    /**
     * The indexer.
     */
    private Indexer indexer;

    /**
     * The icon to update/delete.
     */
    private Icon iconToUpdate;

    /**
     * The uploaded icon.
     */
    private UploadedFile uploadedIcon;

	/**
	 * Bean constructor.
	 */
	public AdministratorsController() {
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
		Assert.notNull(paginator,
				"property paginator of class " + getClass().getName()
				+ " can not be null");
		Assert.notNull(indexer,
				"property indexer of class " + getClass().getName()
				+ " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		ldapUid = null;
		userToDelete = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[ldapUid=[" + ldapUid
		+ "], userToDelete=" + userToDelete
		+ "]";
	}

	/**
	 * @return true if the current user is allowed to access the view.
	 */
	@RequestCache
	public boolean isPageAuthorized() {
		return getDomainService().userCanViewAdmins(getCurrentUser());
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
		return "navigationAdministrators";
	}

	/**
	 * @return true if the current user can add an admin.
	 */
	public boolean isCurrentUserCanAddAdmin() {
		return getDomainService().userCanAddAdmin(getCurrentUser());
	}

	/**
	 * @return true if the current user can delete an admin.
	 */
	public boolean isCurrentUserCanDeleteAdmin() {
		return getDomainService().userCanDeleteAdmin(getCurrentUser(), userToDelete);
	}

	/**
	 * @return true if the current user can add an admin.
	 */
	public boolean isCurrentUserCanManageIcons() {
		return getDomainService().userCanManageIcons(getCurrentUser());
	}

	/**
	 * @return the paginator.
	 */
	public Paginator<User> getPaginator() {
		return paginator;
	}

	/**
	 * @return the LDAP statistics.
	 */
	@RequestCache
	public List <String> getLdapStatistics() {
		if (!ldapUserService.supportStatistics()) {
			return new ArrayList<String>();
		}
		return ldapUserService.getStatistics(getLocale());
	}

	/**
	 * @return the total number of documents indexed.
	 */
	public int getIndexingDocumentsNumber() {
		return indexer.getDocumentsNumber();
	}

	/**
	 * @return the number of tickets indexed.
	 */
	public int getIndexingTicketsNumber() {
		return indexer.getTicketsNumber();
	}

	/**
	 * @return the number of archived tickets indexed.
	 */
	public int getIndexingArchivedTicketsNumber() {
		return indexer.getArchivedTicketsNumber();
	}

	/**
	 * @return the number of FAQs indexed.
	 */
	public int getIndexingFaqsNumber() {
		return indexer.getFaqsNumber();
	}

	/**
	 * @return the icon items.
	 */
	@RequestCache
	public List<SelectItem> getIconItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (Icon icon : getDomainService().getIcons()) {
			items.add(new SelectItem(icon, icon.getName()));
		}
		return items;
	}

	/**
	 * @return a String.
	 */
	public String addAdmin() {
		if (!isCurrentUserCanAddAdmin()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (ldapUid == null) {
			addErrorMessage(null, "ADMINISTRATORS.MESSAGE.ENTER_ID");
			return null;
		}
		try {
			User user = getUserStore().getUserFromRealId(ldapUid);
			if (user.getAdmin()) {
				addErrorMessage(
						null, "ADMINISTRATORS.MESSAGE.USER_ALREADY_ADMINISTRATOR",
						formatUser(user));
				return null;
			}
			getDomainService().addAdmin(user);
			ldapUid = "";
			addInfoMessage(null, "ADMINISTRATORS.MESSAGE.ADMIN_ADDED", formatUser(user));
			return "adminAdded";
		} catch (UserNotFoundException e) {
			addWarnMessage("ldapUid", "_.MESSAGE.USER_NOT_FOUND", ldapUid);
			return null;
		}
	}

	/**
	 * @return a String.
	 */
	public String confirmDeleteAdmin() {
		if (!isCurrentUserCanDeleteAdmin()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().deleteAdmin(userToDelete);
		addInfoMessage(null, "ADMINISTRATORS.MESSAGE.ADMIN_DELETED",
				formatUser(userToDelete));
		return "adminDeleted";
	}

	/**
	 * @return a String.
	 */
	public String deleteIcon() {
		if (!isCurrentUserCanManageIcons()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().deleteIcon(iconToUpdate);
		return "iconDeleted";
	}

	/**
	 * @return a String.
	 */
	public String addIcon() {
		if (!isCurrentUserCanManageIcons()) {
			addUnauthorizedActionMessage();
			return null;
		}
		String iconName = getString("ICON.NEW_NAME");
		Integer i = 1;
		while (getDomainService().getIconByName(iconName) != null) {
			i++;
			iconName = getString("ICON.NEW_NAME_WITH_NUM", i);
		}
		iconToUpdate = getDomainService().addIcon(iconName);
		return "iconAdded";
	}

	/**
	 * @return a String.
	 */
	public String updateIcon() {
		if (!org.springframework.util.StringUtils.hasText(iconToUpdate.getName())) {
			addErrorMessage(null, "ADMINISTRATORS.MESSAGE.ENTER_ICON_NAME");
			return null;
		}
		Icon icon = getDomainService().getIconByName(iconToUpdate.getName());
		if (icon != null) {
			if (!iconToUpdate.equals(icon)) {
				addErrorMessage(null, "ADMINISTRATORS.MESSAGE.ICON_NAME_ALREADY_USED");
				return null;
			}
		}
		if (uploadedIcon != null && uploadedIcon.getSize() > 0) {
			try {
				iconToUpdate.setData(uploadedIcon.getBytes());
				iconToUpdate.setContentType(uploadedIcon.getContentType());
			} catch (IOException e) {
				addErrorMessage(null, "ADMINISTRATORS.MESSAGE.ICON_UPLOAD_ERROR");
				return null;
			}
		}
		getDomainService().updateIcon(iconToUpdate);
		return "iconUpdated";
	}

	/**
	 * @return a permanent link to the page for application users.
	 */
	public String getApplicationPermLink() {
		return getUrlBuilder().getAdministratorsUrl(AuthUtils.APPLICATION);
	}

	/**
	 * @return a permanent link to the page for CAS users.
	 */
	public String getCasPermLink() {
		return getUrlBuilder().getAdministratorsUrl(AuthUtils.CAS);
	}

	/**
	 * @return a permanent link to the page for Shibboleth users.
	 */
	public String getShibbolethPermLink() {
		return getUrlBuilder().getAdministratorsUrl(AuthUtils.SHIBBOLETH);
	}

	/**
	 * @return a permanent link to the page for specific users.
	 */
	public String getSpecificPermLink() {
		return getUrlBuilder().getAdministratorsUrl(AuthUtils.SPECIFIC);
	}

	/**
	 * @param userToDelete the userToDelete to set
	 */
	public void setUserToDelete(final User userToDelete) {
		this.userToDelete = userToDelete;
	}

	/**
	 * @return the userToDelete
	 */
	public User getUserToDelete() {
		return userToDelete;
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
	 * @param paginator the paginator to set
	 */
	public void setPaginator(final Paginator<User> paginator) {
		this.paginator = paginator;
	}

	/**
	 * @return the indexer
	 */
	protected Indexer getIndexer() {
		return indexer;
	}

	/**
	 * @param indexer the indexer to set
	 */
	public void setIndexer(final Indexer indexer) {
		this.indexer = indexer;
	}

	/**
	 * @return the ldapUserService
	 */
	protected LdapUserService getLdapUserService() {
		return ldapUserService;
	}

	/**
	 * @return the last time tickets have been indexed
	 */
	public Timestamp getTicketsLastIndexTime() {
		return getDomainService().getTicketsLastIndexTime();
	}

	/**
	 * @return the last time archived tickets have been indexed
	 */
	public Timestamp getArchivedTicketsLastIndexTime() {
		return getDomainService().getArchivedTicketsLastIndexTime();
	}

	/**
	 * @return the last time FAQs have been indexed
	 */
	public Timestamp getFaqsLastIndexTime() {
		return getDomainService().getFaqsLastIndexTime();
	}

	/**
	 * @return the iconToUpdate
	 */
	public Icon getIconToUpdate() {
		return iconToUpdate;
	}

	/**
	 * @param iconToUpdate the iconToUpdate to set
	 */
	public void setIconToUpdate(final Icon iconToUpdate) {
		this.iconToUpdate = iconToUpdate;
	}

	/**
	 * @return the uploadedIcon
	 */
	public UploadedFile getUploadedIcon() {
		return uploadedIcon;
	}

	/**
	 * @param uploadedIcon the uploadedIcon to set
	 */
	public void setUploadedIcon(final UploadedFile uploadedIcon) {
		this.uploadedIcon = uploadedIcon;
	}

}
