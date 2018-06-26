/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.AttributesMapper;

/**
 * Class which allows to retrieve user attributes from a LDAP directory.
 */

public class LdapAttributesMapper implements AttributesMapper, Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 9222929097200132160L;

	/**
	 * The attributes. 
	 */
	private List<String> attributes;
	
	/**
	 * The name of the attirbute that contains the uid.
	 */
	private String uidAttribute;


	/**
	 * Bean constructor.
	 * @param uidAttribute 
	 * @param attributes 
	 */
	public LdapAttributesMapper(
			final String uidAttribute, 
			final List<String> attributes) {
		this.uidAttribute = uidAttribute;
		this.attributes = attributes;
	}
	
	/**
	 * @return the attributes names
	 */
	public List<String> getAttributes() {
		return attributes;
	}

	/**
	 * Performs mapping after an LDAP query for a set of user attributes. Takes each key in the ldap
	 * to ldapUser attribute Map and tries to find it in the returned Attributes set. For each found
	 * Attribute the value is added to the attribute Map as the value or in the value Set with the
	 * ldapUser attribute name as the key. String and byte[] may be values.
	 * @see org.springframework.ldap.AttributesMapper#mapFromAttributes(javax.naming.directory.Attributes)
	 */
	@Override
	public Object mapFromAttributes(final Attributes attrs) throws NamingException {
		LdapUserImpl ldapUser = new LdapUserImpl();
		Attribute uidAttr = attrs.get(uidAttribute);
		if (uidAttr == null) {
			return ldapUser;
		}
		ldapUser.setId(uidAttr.get().toString());
		for (String ldapAttributeName : attributes) {
			Attribute attribute = attrs.get(ldapAttributeName);
			List<String> listAttr = new ArrayList<String>();
			// The attribute exists
			if (attribute != null) {
				@SuppressWarnings("unchecked")
				NamingEnumeration<Object> attrValueEnum = 
					(NamingEnumeration<Object>) attribute.getAll();
				while (attrValueEnum.hasMore()) {
					Object attributeValue = attrValueEnum.next();
					// Convert everything except byte[] to String
					if (!(attributeValue instanceof byte[])) {
						attributeValue = attributeValue.toString();
						listAttr.add(attributeValue.toString());
					}
				}
				@SuppressWarnings("rawtypes")
				Set attributeNames = Collections.singleton(ldapAttributeName);
				// Run through the mapped attribute names
				for (@SuppressWarnings("rawtypes")
				Iterator attrNameItr = attributeNames .iterator(); attrNameItr.hasNext();) {
					String attributeName = (String) attrNameItr .next();
					ldapUser.getAttributes().put(attributeName.toString(), listAttr);
				}
			}
		}
		return ldapUser;
	}
}

