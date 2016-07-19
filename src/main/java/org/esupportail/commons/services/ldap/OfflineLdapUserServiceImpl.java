/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * An implementation of LdapUserService to work offline.
 */
public class OfflineLdapUserServiceImpl extends AbstractLdapService implements LdapUserService, InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 4321523250419240094L;

	/**
	 * The default uid attribute.
	 */
	private static final String DEFAULT_UID_ATTRIBUTE = "uid";
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The name of the attirbute that contains the uid.
	 */
	private String uidAttribute;

	/**
	 * Bean constructor.
	 */
	public OfflineLdapUserServiceImpl() {
		super();
	}

	/**
	 * Set the default uid attribute.
	 */
	private void setDefaultUidAttribute() {
		uidAttribute = DEFAULT_UID_ATTRIBUTE;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		if (!StringUtils.hasText(uidAttribute)) {
			logger.info(getClass() + ": no uid attribute set, '" 
					+ DEFAULT_UID_ATTRIBUTE + "' will be used");
			setDefaultUidAttribute();
		}
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#getLdapUsersFromToken(java.lang.String)
	 */
	@Override
	public List<LdapUser> getLdapUsersFromToken(
			@SuppressWarnings("unused")
			final String token) throws LdapException {
		throw new UnsupportedOperationException(
				"class [" + getClass().getSimpleName() + "] does not support LDAP searches.");
	}
	
	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#getLdapUsersFromFilter(java.lang.String)
	 */
	@Override
	public List<LdapUser> getLdapUsersFromFilter(
			@SuppressWarnings("unused")
			final String filterExpr) throws LdapException {
		throw new UnsupportedOperationException(
				"class [" + getClass().getSimpleName() + "] does not support LDAP searches.");
	}
	
	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#testLdapFilter(java.lang.String)
	 */
	@Override
	public String testLdapFilter(
			@SuppressWarnings("unused")
			final String filterExpr) throws LdapException {
		throw new UnsupportedOperationException(
				"class [" + getClass().getSimpleName() + "] does not support LDAP filter testing.");
	}
	
	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#getLdapUser(java.lang.String)
	 */
	@Override
	public LdapUser getLdapUser(final String id) throws LdapException, UserNotFoundException {
		LdapUser ldapUser = new LdapUserImpl();
		ldapUser.setId(id);
		Map<String, List<String>> attributes = new HashMap<String, List<String>>();
		List<String> idValues = new ArrayList<String>();
		idValues.add(id);
		attributes.put(uidAttribute, idValues);
		ldapUser.setAttributes(attributes);
		return ldapUser;
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#userMatchesFilter(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean userMatchesFilter(
			@SuppressWarnings("unused")
			final String id, 
			@SuppressWarnings("unused")
			final String filterExpr) throws LdapException {
		throw new UnsupportedOperationException(
				"class [" + getClass().getSimpleName() + "] does not support LDAP filter matching.");
	}

	/**
	 * @param uidAttribute the uidAttribute to set
	 */
	public  void setUidAttribute(final String uidAttribute) {
		this.uidAttribute = uidAttribute;
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#getSearchDisplayedAttributes()
	 */
	@Override
	public List<String> getSearchDisplayedAttributes() {
		throw new UnsupportedOperationException(
				"class [" + getClass().getSimpleName() + "] does not support LDAP searches.");
	}

}
