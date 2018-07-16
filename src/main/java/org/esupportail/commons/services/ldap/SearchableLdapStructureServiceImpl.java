/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.ldap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sf.ehcache.CacheManager;

import org.esupportail.commons.exceptions.ObjectNotFoundException;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.LdapTemplate;
import org.springframework.ldap.support.filter.AndFilter;
import org.springframework.ldap.support.filter.OrFilter;
import org.springframework.ldap.support.filter.WhitespaceWildcardsFilter;
import org.springframework.util.StringUtils;

/**
 * An implementation of LdapUserService that delegates to a CachingLdapEntityServiceImpl.
 */
public class SearchableLdapStructureServiceImpl implements LdapStructureService, InitializingBean {

	/**
	 * The serialization id.
	 */

	private static final long serialVersionUID = -1046726124289772997L;

	/**
	 * The default unique attribute.
	 */
	private static final String DEFAULT_ID_ATTRIBUTE = "supannCodeEntite";


	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The real LDAP entity service to delegate.
	 */
	private CachingLdapEntityServiceImpl service;
	
	/**
	 * The attribute used by method getLdapUsersFromToken().
	 */
	private String searchAttribute;

    /**
    * The attribute filterDate.
    */
	private String filterDate;

    /**
    * The attribute filterEdupersonaffiliation.
    */
	private String filterEdupersonaffiliation;

    /**
    * The attribute filterEdupersonaffiliation.
    */
	private String filterEdupersonaffiliationValues;

	/**
	 * The attributes that will be shown when searching for a user.
	 */
	private List<String> searchDisplayedAttributes;

	

	public LdapStructure getLdapStructure(String id) throws LdapException, UserNotFoundException {
		try {
			return LdapStructureImpl.createLdapStructure(service.getLdapEntity(id));
		} catch (ObjectNotFoundException e) {
			throw new UserNotFoundException(e);
		}
	}

	public List<LdapStructure> getLdapStructuresFromToken(String token) throws LdapException {
		OrFilter filterOr1 = new OrFilter();
		OrFilter filterOr2 = new OrFilter();
		AndFilter filterAnd = new AndFilter();
		filterOr1.or(new WhitespaceWildcardsFilter(searchAttribute, token));
		filterOr1.or(new WhitespaceWildcardsFilter(service.getIdAttribute(), token));
		filterAnd.and(filterOr1);
		filterAnd.and(new WhitespaceWildcardsFilter(filterDate,""));
		
		String [] values = filterEdupersonaffiliationValues.split(",");
		for(String value:values){
			filterOr2.or(new WhitespaceWildcardsFilter(filterEdupersonaffiliation, value));
		}
		
		filterAnd.and(filterOr2);
	
		//WhitespaceWildcardsFilter filter = new WhitespaceWildcardsFilter(searchAttribute, token);
		return getLdapStructuresFromFilter(filterAnd.encode());
	}

	public List<LdapStructure> getLdapStructuresFromFilter(String filterExpr) throws LdapException {
		return LdapStructureImpl.createLdapStructures(service.getLdapEntitiesFromFilter(filterExpr));
	}
	
	
	/**
	 * Bean constructor.
	 */
	public SearchableLdapStructureServiceImpl() {
		super();
		service = new CachingLdapEntityServiceImpl();
		service.setIdAttribute(DEFAULT_ID_ATTRIBUTE);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		if (searchAttribute == null) {
			logger.info("property searchAttribute is not set, method getLdapUsersFromToken() will fail");
		} else {
			Assert.notEmpty(searchDisplayedAttributes, "property searchDisplayedAttribute is not set");
		}
		service.afterPropertiesSet();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode() + "[" 
		+ "searchDisplayedAttributes=[" + getSearchDisplayedAttributes() + "], " 
		+ "searchAttribute=[" + searchAttribute + "], " 
		+ "service=" + service  
		+ "]";
	}


	/**
	 * @see org.esupportail.commons.services.ldap.LdapUserService#userMatchesFilter(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean structureMatchesFilter(final String id, final String filter) throws LdapException {
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
	 * @see org.esupportail.commons.services.ldap.LdapUserService#getSearchDisplayedAttributes()
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

	/**
	 * @param searchAttribute the searchAttribute to set
	 */
	public void setSearchAttribute(final String searchAttribute) {
		this.searchAttribute = searchAttribute;
	}

	/**
	 * @return the unique id attribute
	 * @see org.esupportail.commons.services.ldap.SimpleLdapEntityServiceImpl#getIdAttribute()
	 */
	public String getIdAttribute() {
		return service.getIdAttribute();
	}

	public String getFilterDate() {
		return filterDate;
	}

	public void setFilterDate(String filterDate) {
		this.filterDate = filterDate;
	}

	public String getFilterEdupersonaffiliation() {
		return filterEdupersonaffiliation;
	}

	public void setFilterEdupersonaffiliation(String filterEdupersonaffiliation) {
		this.filterEdupersonaffiliation = filterEdupersonaffiliation;
	}

	public String getFilterEdupersonaffiliationValues() {
		return filterEdupersonaffiliationValues;
	}

	public void setFilterEdupersonaffiliationValues(String filterEdupersonaffiliationValues) {
		this.filterEdupersonaffiliationValues = filterEdupersonaffiliationValues;
	}

}
