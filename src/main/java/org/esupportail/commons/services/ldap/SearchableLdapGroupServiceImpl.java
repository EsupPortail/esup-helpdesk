/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sf.ehcache.CacheManager;

import org.esupportail.commons.exceptions.GroupNotFoundException;
import org.esupportail.commons.exceptions.ObjectNotFoundException;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.LdapTemplate;
import org.springframework.ldap.support.filter.Filter;
import org.springframework.ldap.support.filter.WhitespaceWildcardsFilter;
import org.springframework.util.StringUtils;

/**
 * An implementation of LdapGroupService that delegates to a CachingLdapEntityServiceImpl.
 */
public class SearchableLdapGroupServiceImpl implements LdapGroupService, InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6333810507403277702L;

	/**
	 * The default unique attribute.
	 */
	private static final String DEFAULT_ID_ATTRIBUTE = "gid";

	/**
	 * The default object class.
	 */
	private static final String DEFAULT_OBJECT_CLASS = "Group";

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The real LDAP entity service to delegate.
	 */
	private CachingLdapEntityServiceImpl service;
	
	/**
	 * The attribute used by method getLdapGroupsFromToken().
	 */
	private String searchAttribute;

	/**
	 * The attributes that will be shown when searching for a group.
	 */
	private List<String> searchDisplayedAttributes;

	/**
	 * Bean constructor.
	 */
	public SearchableLdapGroupServiceImpl() {
		super();
		service = new CachingLdapEntityServiceImpl();
		service.setIdAttribute(DEFAULT_ID_ATTRIBUTE);
		service.setObjectClass(DEFAULT_OBJECT_CLASS);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		if (searchAttribute == null) {
			logger.info("property searchAttribute is not set, method getLdapGroupsFromToken() will fail");
		} else {
			Assert.notEmpty(searchDisplayedAttributes, "property searchDisplayedAttribute is not set");
		}
		service.afterPropertiesSet();
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapGroupService#getLdapGroup(java.lang.String)
	 */
	@Override
	public LdapGroup getLdapGroup(final String id) throws LdapException, GroupNotFoundException {
		try {
			return LdapGroupImpl.createLdapGroup(service.getLdapEntity(id));
		} catch (ObjectNotFoundException e) {
			throw new GroupNotFoundException(e);
		}
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapGroupService#getLdapGroupsFromFilter(java.lang.String)
	 */
	@Override
	public List<LdapGroup> getLdapGroupsFromFilter(final String filterExpr) throws LdapException {
		return LdapGroupImpl.createLdapGroups(service.getLdapEntitiesFromFilter(filterExpr));
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapGroupService#getLdapGroupsFromToken(java.lang.String)
	 */
	@Override
	public List<LdapGroup> getLdapGroupsFromToken(final String token) throws LdapException {
		Filter filter = new WhitespaceWildcardsFilter(searchAttribute, token);
		return getLdapGroupsFromFilter(filter.encode());
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapGroupService#groupMatchesFilter(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean groupMatchesFilter(final String id, final String filter) throws LdapException {
		return service.entityMatchesFilter(id, filter);
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#getStatistics(java.util.Locale)
	 */
	@Override
	public List<String> getStatistics(final Locale locale) {
		return service.getStatistics(locale);
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#resetStatistics()
	 */
	@Override
	public void resetStatistics() {
		service.resetStatistics();
	}

	/**
	 * Set the cache manager.
	 * @param cacheManager
	 */
	public void setCacheManager(final CacheManager cacheManager) {
		service.setCacheManager(cacheManager);
	}

	/**
	 * Set the cache name.
	 * @param cacheName
	 */
	public void setCacheName(final String cacheName) {
		service.setCacheName(cacheName);
	}

	/**
	 * Set the dnSubPath.
	 * @param dnSubPath
	 */
	public void setDnSubPath(final String dnSubPath) {
		service.setDnSubPath(dnSubPath);
	}

	/**
	 * Set the i18nService.
	 * @param i18nService
	 */
	public void setI18nService(final I18nService i18nService) {
		service.setI18nService(i18nService);
	}

	/**
	 * Set the idAttribute.
	 * @param idAttribute
	 */
	public void setIdAttribute(final String idAttribute) {
		service.setIdAttribute(idAttribute);
	}

	/**
	 * Set the attributes.
	 * @param attributes
	 */
	public void setAttributes(final List<String> attributes) {
		service.setAttributes(attributes);
	}

	/**
	 * Set the ldapTemplate.
	 * @param ldapTemplate
	 */
	public void setLdapTemplate(final LdapTemplate ldapTemplate) {
		service.setLdapTemplate(ldapTemplate);
	}

	/**
	 * Set the objectClass.
	 * @param objectClass
	 */
	public void setObjectClass(final String objectClass) {
		service.setObjectClass(objectClass);
	}

	/**
	 * Set the testFilter.
	 * @param testFilter
	 */
	public void setTestFilter(final String testFilter) {
		service.setTestFilter(testFilter);
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#supportStatistics()
	 */
	@Override
	public boolean supportStatistics() {
		return service.supportStatistics();
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#supportsTest()
	 */
	@Override
	public boolean supportsTest() {
		return service.supportsTest();
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#test()
	 */
	@Override
	public void test() {
		service.test();
	}

	/**
	 * @see org.esupportail.commons.services.ldap.BasicLdapService#testLdapFilter(java.lang.String)
	 */
	@Override
	public String testLdapFilter(final String filterExpr) throws LdapException {
		return service.testLdapFilter(filterExpr);
	}

	/**
	 * @see org.esupportail.commons.services.ldap.LdapGroupService#getSearchDisplayedAttributes()
	 */
	@Override
	public List<String> getSearchDisplayedAttributes() {
		return searchDisplayedAttributes;
	}

	/**
	 * @param searchDisplayedAttributes the searchDisplayedAttributes to set
	 */
	public void setSearchDisplayedAttributes(final List<String> searchDisplayedAttributes) {
		this.searchDisplayedAttributes = searchDisplayedAttributes;
	}

	/**
	 * @param searchAttribute the searchAttribute to set
	 */
	public void setSearchAttribute(final String searchAttribute) {
		this.searchAttribute = searchAttribute;
	}

	/**
	 * Set the attributes.
	 * @param attributes
	 */
	public void setAttributesAsString(final String attributes) {
		List<String> list = new ArrayList<String>();
		for (String attribute : attributes.split(",")) {
			if (StringUtils.hasText(attribute)) {
				if (!list.contains(attribute)) {
					list.add(attribute);
				}
			}
		}
		setAttributes(list);
	}

	/**
	 * @param searchDisplayedAttributes the searchDisplayedAttributes to set
	 */
	public void setSearchDisplayedAttributesAsString(final String searchDisplayedAttributes) {
		List<String> list = new ArrayList<String>();
		for (String attribute : searchDisplayedAttributes.split(",")) {
			if (StringUtils.hasText(attribute)) {
				if (!list.contains(attribute)) {
					list.add(attribute);
				}
			}
		}
		setSearchDisplayedAttributes(list);
	}

}
