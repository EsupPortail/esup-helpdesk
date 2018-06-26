/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.util.ArrayList;
import java.util.List;


/**
 * A basic implementation of LdapUser.
 */
public class LdapUserImpl extends LdapEntityImpl implements LdapUser {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 4141419851049182209L;

	/**
	 * Constructor.
	 */
	public LdapUserImpl() {
		super();
	}
	
	/**
	 * @param ldapEntity
	 * @return a LdapUser, created using a LdapEntity.
	 */
	public static LdapUser createLdapUser(final LdapEntity ldapEntity) {
		LdapUserImpl ldapUser = new LdapUserImpl();
		ldapUser.setId(ldapEntity.getId());
		ldapUser.setAttributes(ldapEntity.getAttributes());
		return ldapUser;
	}

	/**
	 * @param ldapEntities
	 * @return a list of LdapUser, created using ldapEntities.
	 */
	public static List<LdapUser> createLdapUsers(final List<LdapEntity> ldapEntities) {
		List<LdapUser> ldapUsers = new ArrayList<LdapUser>();
		for (LdapEntity ldapEntity : ldapEntities) {
			ldapUsers.add(LdapUserImpl.createLdapUser(ldapEntity));
		}
		return ldapUsers;
	}

}
