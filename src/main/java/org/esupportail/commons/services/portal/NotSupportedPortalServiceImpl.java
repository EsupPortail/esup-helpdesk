/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.portal;

import java.io.Serializable;
import java.util.List;

import org.esupportail.portal.ws.client.PortalGroup;
import org.esupportail.portal.ws.client.PortalGroupHierarchy;
import org.esupportail.portal.ws.client.PortalUser;
import org.esupportail.portal.ws.client.exceptions.PortalErrorException;
import org.esupportail.portal.ws.client.exceptions.PortalGroupNotFoundException;
import org.esupportail.portal.ws.client.exceptions.PortalUserNotFoundException;
import org.esupportail.portal.ws.client.support.AbstractPortalService;

/**
 * A void implementation of PortalService (for application that do not
 * use portal services, for instance portlet installations).
 */
public class NotSupportedPortalServiceImpl extends AbstractPortalService implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7399929419364841341L;

	/**
	 * Bean constructor.
	 */
	public NotSupportedPortalServiceImpl() {
		super();
	}

	/**
	 * @return an exception.
	 */
	private UnsupportedOperationException notSupported() {
		return new UnsupportedOperationException("do not call the methods of class [" + getClass() + "]");
	}

	//////////////////////////////////////////////////////////
	// user methods
	//////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getUser(java.lang.String)
	 */
	@Override
	public PortalUser getUser(
			@SuppressWarnings("unused")
			final String userId) {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#searchUsers(java.lang.String)
	 */
	@Override
	public List<PortalUser> searchUsers(
			@SuppressWarnings("unused")
			final String token)
	throws PortalErrorException, PortalUserNotFoundException {
		throw notSupported();
	}

	//////////////////////////////////////////////////////////
	// group methods
	//////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupById(java.lang.String)
	 */
	@Override
	public PortalGroup getGroupById(
			@SuppressWarnings("unused")
			final String groupId) {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupByName(java.lang.String)
	 */
	@Override
	public PortalGroup getGroupByName(
			@SuppressWarnings("unused")
			final String groupName) {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#searchGroupsByName(java.lang.String)
	 */
	@Override
	public List<PortalGroup> searchGroupsByName(
			@SuppressWarnings("unused")
			final String token) {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getSubGroupsById(java.lang.String)
	 */
	@Override
	public List<PortalGroup> getSubGroupsById(
			@SuppressWarnings("unused")
			final String arg0)
			throws PortalErrorException, PortalGroupNotFoundException {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getSubGroupsByName(java.lang.String)
	 */
	@Override
	public List<PortalGroup> getSubGroupsByName(
			@SuppressWarnings("unused")
			final String arg0)
			throws PortalErrorException, PortalGroupNotFoundException {
		throw notSupported();
	}

	//////////////////////////////////////////////////////////
	// group hierarchy methods
	//////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupHierarchyById(java.lang.String)
	 */
	@Override
	public PortalGroupHierarchy getGroupHierarchyById(
			@SuppressWarnings("unused")
			final String arg0)
	throws PortalErrorException, PortalGroupNotFoundException {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupHierarchyByName(java.lang.String)
	 */
	@Override
	public PortalGroupHierarchy getGroupHierarchyByName(
			@SuppressWarnings("unused")
			final String arg0)
	throws PortalErrorException, PortalGroupNotFoundException {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getRootGroup()
	 */
	@Override
	public PortalGroup getRootGroup() {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupHierarchy()
	 */
	@Override
	public PortalGroupHierarchy getGroupHierarchy() {
		throw notSupported();
	}

	//////////////////////////////////////////////////////////
	// group membership methods
	//////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getUserGroups(java.lang.String)
	 */
	@Override
	public List<PortalGroup> getUserGroups(
			@SuppressWarnings("unused")
			final String userId) {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getGroupUsers(java.lang.String)
	 */
	@Override
	public List<PortalUser> getGroupUsers(
			@SuppressWarnings("unused")
			final String groupId) {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#isUserMemberOfGroup(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isUserMemberOfGroup(
			@SuppressWarnings("unused")
			final String userId,
			@SuppressWarnings("unused")
			final String groupId) {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getContainingGroupsById(java.lang.String)
	 */
	@Override
	public List<PortalGroup> getContainingGroupsById(
			@SuppressWarnings("unused")
			final String groupId)
			throws PortalErrorException, PortalGroupNotFoundException {
		throw notSupported();
	}

	/**
	 * @see org.esupportail.portal.ws.client.PortalService#getContainingGroupsByName(java.lang.String)
	 */
	@Override
	public List<PortalGroup> getContainingGroupsByName(
			@SuppressWarnings("unused")
			final String groupName)
			throws PortalErrorException, PortalGroupNotFoundException {
		throw notSupported();
	}

}
