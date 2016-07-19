/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.io.Serializable;

import org.esupportail.commons.utils.Assert;
import org.springframework.ldap.support.LdapContextSource;

/**
 * A class to allow the usage of simple or multiple LDAP URLs.
 */

public class MultiUrlLdapContextSource extends LdapContextSource implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3582374401002535963L;

	/**
	 * Constructor.
	 */
	public MultiUrlLdapContextSource() {
		super();
	}

	/**
	 * @see org.springframework.ldap.support.AbstractContextSource#setUrl(java.lang.String)
	 */
	@Override
	public void setUrl(final String url) {
		Assert.hasText(url, "property url of class " + getClass() + " can not be empty");
		if (url.contains(",")) {
			setUrls(url.split(","));
		} else {
			super.setUrl(url);
		}
	}


}

