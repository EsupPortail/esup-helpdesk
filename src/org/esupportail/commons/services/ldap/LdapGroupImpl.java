/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.util.ArrayList;
import java.util.List;


/**
 * A basic implementation of LdapGroup.
 */
public class LdapGroupImpl extends LdapEntityImpl implements LdapGroup {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = -7065179465595505379L;

	/**
	 * Constructor.
	 */
	public LdapGroupImpl() {
		super();
	}
	
	/**
	 * @param ldapEntity
	 * @return a LdapGroup, created using a LdapEntity.
	 */
	public static LdapGroup createLdapGroup(final LdapEntity ldapEntity) {
		LdapGroupImpl ldapGroup = new LdapGroupImpl();
		ldapGroup.setId(ldapEntity.getId());
		ldapGroup.setAttributes(ldapEntity.getAttributes());
		return ldapGroup;
	}

	/**
	 * @param ldapEntities
	 * @return a list of LdapGroup, created using ldapEntities.
	 */
	public static List<LdapGroup> createLdapGroups(final List<LdapEntity> ldapEntities) {
		List<LdapGroup> ldapGroups = new ArrayList<LdapGroup>();
		for (LdapEntity ldapEntity : ldapEntities) {
			ldapGroups.add(LdapGroupImpl.createLdapGroup(ldapEntity));
		}
		return ldapGroups;
	}

}
