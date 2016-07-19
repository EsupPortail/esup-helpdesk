/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userManagement;

import java.util.List;
import java.util.Map;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.portal.ws.client.PortalGroup;
import org.esupportail.portal.ws.client.PortalService;
import org.esupportail.portal.ws.client.PortalUser;
import org.esupportail.portal.ws.client.exceptions.PortalGroupNotFoundException;
import org.esupportail.portal.ws.client.exceptions.PortalUserNotFoundException;

/**
 * A user manager that can talk to a portal.
 */
@SuppressWarnings("serial")
public abstract class AbstractPortalAwareUserManager extends AbstractUserManager {

	/**
	 * {@link PortalService}.
	 */
	private PortalService portalService;

	/**
	 * Constructor.
	 */
	public AbstractPortalAwareUserManager() {
		super(false);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.portalService,
				"property portalService of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.UserManager#getPortalAttributes(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public Map<String, List<String>> getPortalAttributes(final User user) {
		try {
			return portalService.getUserAttributes(user.getRealId());
		} catch (PortalUserNotFoundException e) {
			return null;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.AbstractUserManager#isMemberOfPortalGroup(
	 * org.esupportail.helpdesk.domain.beans.User, java.lang.String)
	 */
	@Override
	public boolean isMemberOfPortalGroup(
			final User user,
			final String groupId) {
		try {
			PortalGroup portalGroup = portalService.getGroupById(groupId);
			PortalUser portalUser = portalService.getUser(user.getRealId());
			return portalService.isUserMemberOfGroup(portalUser, portalGroup);
		} catch (PortalGroupNotFoundException e) {
			return false;
		} catch (PortalUserNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userManagement.AbstractUserManager#isMemberOfPortalDistinguishedGroup(
	 * org.esupportail.helpdesk.domain.beans.User, java.lang.String)
	 */
	@Override
	public boolean isMemberOfPortalDistinguishedGroup(
			final User user,
			final String groupName) {
		try {
			PortalGroup portalGroup = portalService.getGroupByName(groupName);
			PortalUser portalUser = portalService.getUser(user.getRealId());
			return portalService.isUserMemberOfGroup(portalUser, portalGroup);
		} catch (PortalGroupNotFoundException e) {
			return false;
		} catch (PortalUserNotFoundException e) {
			return false;
		}
	}

	/**
	 * @return the portalService
	 */
	protected PortalService getPortalService() {
		return portalService;
	}

	/**
	 * @param portalService the portalService to set
	 */
	public void setPortalService(final PortalService portalService) {
		this.portalService = portalService;
	}

}
