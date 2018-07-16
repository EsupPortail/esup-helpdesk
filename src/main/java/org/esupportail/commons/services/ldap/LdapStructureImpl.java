/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.util.ArrayList;
import java.util.List;


/**
 * A basic implementation of LdapUser.
 */
public class LdapStructureImpl extends LdapEntityImpl implements LdapStructure {

	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 2046831361903781867L;

	/**
	 * Constructor.
	 */
	public LdapStructureImpl() {
		super();
	}
	
	/**
	 * @param ldapEntity
	 * @return a LdapUser, created using a LdapEntity.
	 */
	public static LdapStructure createLdapStructure(final LdapEntity ldapEntity) {
		LdapStructureImpl ldapStructure = new LdapStructureImpl();
		ldapStructure.setId(ldapEntity.getId());
		ldapStructure.setAttributes(ldapEntity.getAttributes());
		return ldapStructure;
	}

	/**
	 * @param ldapEntities
	 * @return a list of LdapUser, created using ldapEntities.
	 */
	public static List<LdapStructure> createLdapStructures(final List<LdapEntity> ldapEntities) {
		List<LdapStructure> ldapStructures = new ArrayList<LdapStructure>();
		for (LdapEntity ldapEntity : ldapEntities) {
			ldapStructures.add(LdapStructureImpl.createLdapStructure(ldapEntity));
		}
		return ldapStructures;
	}

}
