/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.io.Serializable;

import org.springframework.ldap.support.filter.AbstractFilter;

/**
 * A LdapTemplate filter, build with a string (that represents an already-encoded filter).
 */
public class StringFilter extends AbstractFilter implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7312355712619088000L;

	/**
	 * The filter string.
	 */
	private String filterExpr;

	/**
	 * Bean constructor.
	 * @param filterExpr an already-encoded LDAP filter
	 */
	public StringFilter(final String filterExpr) {
		super();
		this.filterExpr = "(" + filterExpr + ")";
	}

	/**
	 * @see org.springframework.ldap.support.filter.AbstractFilter#encode(java.lang.StringBuffer)
	 */
	@Override
	public StringBuffer encode(final StringBuffer buff) {
		buff.append(filterExpr);
		return buff;
	}

}
