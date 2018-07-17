/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.userInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.apache.turbine.util.BrowserDetector;
import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.ldap.LdapAttributesMapper;
import org.esupportail.commons.services.ldap.LdapEntity;
import org.esupportail.commons.services.ldap.LdapException;
import org.esupportail.commons.services.ldap.LdapStructureService;
import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.HttpUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.User;
import org.springframework.ldap.AttributesMapper;

import org.springframework.ldap.LdapTemplate;
import org.springframework.ldap.support.filter.AndFilter;
import org.springframework.ldap.support.filter.EqualsFilter;
import org.springframework.ldap.support.filter.NotFilter;


/**
 * A simple implementation of UserInfoProvider.
 */
public class BasicUserInfoProviderImpl extends AbstractUserInfoProvider {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4843564387023349739L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The LDAP service.
	 */
	private LdapUserService ldapUserService;

	
	
	private LdapTemplate ldapTemplate;
	/**
	 * The LDAP service.
	 */
	private LdapStructureService ldapStructureService;

	/**
	 * The names of the LDAP attributes to show, all if not set.
	 */
	private List<String> ldapAttributeNames;

	/**
	 * The names of the LDAP AMU attributes to show, all if not set.
	 */
	private List<String> ldapAmuAttributeNames;

	/**
	 * The attributes mapper.
	 */
	private LdapAttributesMapper attributesMapper;

	/**
	 * The names of the Shibboleth attributes to show, all if not set.
	 */
	private List<String> shibbolethAttributeNames;

	/**
	 * The user id used when running ant target test-user-info.
	 */
	private String testUserId;

	/**
	 * True to print something if the user is an administrator.
	 */
	private boolean showAdministrator;

	/**
	 * True to show the departments managed by the user.
	 */
	private boolean showManagedDepartments;

	/**
	 * True to show the departments seen by the user at ticket creation.
	 */
	private boolean showTicketCreationDepartments;

	/**
	 * True to show the departments seen by the user on the control panel.
	 */
	private boolean showTicketViewDepartments;

	/**
	 * True to show the departments seen by the user for the FAQ.
	 */
	private boolean showFaqViewDepartments;

	/**
	 * True to show the LDAP attributes.
	 */
	private boolean showLdapAttributes;

	/**
	 * True to show the Shibboleth attributes.
	 */
	private boolean showShibbolethAttributes;

	/**
	 * True to show the user's browser.
	 */
	private boolean showBrowser;

	/**
	 * Constructor.
	 */
	public BasicUserInfoProviderImpl() {
		super();
		showAdministrator = true;
		showManagedDepartments = true;
		showTicketCreationDepartments = true;
		showTicketViewDepartments = false;
		showFaqViewDepartments = false;
		showLdapAttributes = true;
		showShibbolethAttributes = true;
		showBrowser = true;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.ldapUserService,
				"property ldapUserService of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.ldapTemplate,
				"property ldapTemplate of class " + this.getClass().getName() + " can not be null");
		if (ldapAttributeNames != null && ldapAttributeNames.isEmpty()) {
			ldapAttributeNames = null;
		}
		if (ldapAmuAttributeNames != null && ldapAmuAttributeNames.isEmpty()) {
			ldapAmuAttributeNames = null;
		}
		if (shibbolethAttributeNames != null && shibbolethAttributeNames.isEmpty()) {
			shibbolethAttributeNames = null;
		}
	}

	/**
	 * @param str
	 * @return the string wrapped with em tags.
	 */
	protected static String em(final String str) {
		return "<em>" + str + "</em>";
	}

	/**
	 * @param str
	 * @return the string wrapped with li tags.
	 */
	protected static String li(final String str) {
		return "<li>" + str + "</li>";
	}

	/**
	 * @param str
	 * @return the string wrapped with strong tags.
	 */
	protected static String strong(final String str) {
		return "<strong>" + str + "</strong>";
	}

    /**
     * @see org.esupportail.helpdesk.domain.userInfo.UserInfoProvider#getInfo(
     * org.esupportail.helpdesk.domain.beans.User, java.util.Locale)
     */
	@Override
	@RequestCache
    public String getInfo(
    		final User user,
    		final Locale locale) {
    	String info = "";
    	if (showAdministrator) {
	    	if (user.getAdmin()) {
				info += "<p><em>"
					+ getI18nService().getString("USER_INFO.ADMINISTRATOR", locale, user.getRealId())
					+ "</em></p>";
	    	}
    	}
    	List<Department> departments;
    	if (showManagedDepartments) {
    		departments = getDomainService().getManagedDepartments(user);
    		if (!departments.isEmpty()) {
    			info += "<p>"
    				+ em(getI18nService().getString(
    						"USER_INFO.MANAGED_DEPARTMENTS", locale, user.getRealId()));
    			for (Department department : departments) {
    				info += li(strong(department.getLabel()));
    				List<Category> categories;
    				categories = getDomainService().getMemberCategories(user,department);
    				for (Category category : categories) {
    					info += "- "+category.getLabel()+"<br/>";
    				}
    			}
    			info += "</p>";
    		}
    	}
    	if (showTicketCreationDepartments) {
	    	departments = getDomainService().getTicketCreationDepartments(user, null);
			info += "<p>";
	    	if (!departments.isEmpty()) {
				info += em(getI18nService().getString("USER_INFO.TICKET_CREATION_VISIBLE_DEPARTMENTS",
							locale, user.getRealId()));
				for (Department department : departments) {
					info += li(strong(department.getLabel()));
				}
	    	} else {
				info += em(getI18nService().getString("USER_INFO.NO_TICKET_CREATION_VISIBLE_DEPARTMENT",
							locale, user.getRealId())) + "<br/>";
	    	}
			info += em(getI18nService().getString("USER_INFO.TICKET_CREATION_VISIBLE_DEPARTMENTS_NOTE", locale))
				+ "</p>";
    	}
    	if (showTicketViewDepartments) {
	    	departments = getDomainService().getTicketViewDepartments(user, null);
			info += "<p>";
	    	if (!departments.isEmpty()) {
				info += em(getI18nService().getString("USER_INFO.TICKET_VIEW_VISIBLE_DEPARTMENTS",
							locale, user.getRealId()));
				for (Department department : departments) {
					info += li(strong(department.getLabel()));
				}
	    	} else {
				info += em(getI18nService().getString("USER_INFO.NO_TICKET_VIEW_VISIBLE_DEPARTMENT",
							locale, user.getRealId())) + "<br/>";
	    	}
			info += em(getI18nService().getString("USER_INFO.TICKET_VIEW_VISIBLE_DEPARTMENTS_NOTE", locale))
				+ "</p>";
    	}
    	if (showFaqViewDepartments) {
	    	departments = getDomainService().getFaqViewDepartments(user, null);
			info += "<p>";
	    	if (!departments.isEmpty()) {
				info += em(getI18nService().getString(
							"USER_INFO.FAQ_VIEW_VISIBLE_DEPARTMENTS",
							locale, user.getRealId()));
				for (Department department : departments) {
					info += li(strong(department.getLabel()));
				}
	    	} else {
				info += em(getI18nService().getString("USER_INFO.NO_FAQ_VIEW_VISIBLE_DEPARTMENT",
							locale, user.getRealId())) + "<br/>";
	    	}
			info += em(getI18nService().getString("USER_INFO.FAQ_VIEW_VISIBLE_DEPARTMENTS_NOTE", locale))
				+ "</p>";
    	}
    	if (showLdapAttributes
    			&& getDomainService().getUserStore().isCasAuthAllowed()
    			&& getDomainService().getUserStore().isCasUser(user)) {
			info += getLdapInfo(user, locale);
    	}
    	if (showShibbolethAttributes
    			&& getDomainService().getUserStore().isShibbolethAuthAllowed()
    			&& getDomainService().getUserStore().isShibbolethUser(user)) {
			info += getShibbolethInfo(user, locale);
    	}
    	if (showBrowser) {
			info += getBrowserInfo(locale);
    	}
		String moreInfo = getMoreInfo(user, locale);
		if (moreInfo != null) {
			info += moreInfo;
		}
    	return StringUtils.nullIfEmpty(info);
    }

	
	
	private String getLibelleSupannEntiteAffectation(String valeur) {
		final List<String> sites = new ArrayList<String>();

		EqualsFilter filter = new EqualsFilter("supannCodeEntite", valeur);
		AndFilter andFilter = new AndFilter();
		andFilter.and(new EqualsFilter("objectclass", "supannEntite"));
		andFilter.and(filter);
		List<String> attributes = new ArrayList<String>();
		attributes.add("description");
		String[] attrs = {"description"};
		attributesMapper = new LdapAttributesMapper("description", attributes);
		
		List<LdapEntity> liste = ldapTemplate.search(
				"ou=structures", andFilter.encode(), SearchControls.SUBTREE_SCOPE, attrs, attributesMapper);

		if(liste.size() > 0) {
			return liste.get(0).getAttribute("description");
		}
		return "";
		 
	}

	
	
	
	
    /**
     * @param user
     * @param locale
     * @return the LDAP info.
     */
    protected String getLdapInfo(
    		final User user,
    		final Locale locale) {
    	String info = "<p>";
    	try {
			LdapUser ldapUser = ldapUserService.getLdapUser(user.getRealId());

			List<String> names;
			if (shibbolethAttributeNames == null) {
				names = ldapUser.getAttributeNames();
			} else {
				names = ldapAttributeNames;
			}
			if (names.isEmpty()) {
				info += em(getI18nService().getString(
							"USER_INFO.LDAP.NO_ATTRIBUTE",
							locale, user.getRealId()));
			} else {
				info += em(getI18nService().getString(
							"USER_INFO.LDAP.ATTRIBUTES",
							locale, user.getRealId()));
				for (String name : names) {
					info += "<br />" + name + "=";
					List<String> values = ldapUser.getAttributes(name);
					if (values.isEmpty()) {
						info += em(getI18nService().getString(
									"USER_INFO.LDAP.NO_VALUE",
									locale));
					} else if (values.size() == 1) {
						if(name.equals("memberOf")) {
							info += "[";
							for (String string : values) {
								info += "[" + strong(string.substring(0, string.indexOf(","))) + "] ";
							}
							info += "]";
						}
						else {
							info += "[" + strong(values.get(0));
							if(name.equals("supannEntiteAffectation") || name.equals("supannEntiteAffectationPrincipale")) {
								String libelle = getLibelleSupannEntiteAffectation(values.get(0));
								info += " : " + libelle;
							}
							info += "]";
						}
					} else {
						if(name.equals("memberOf")) {
							info += "[";
							for (String string : values) {
								info += "[" + strong(string.substring(0, string.indexOf(","))) + "] ";
							}
							info += "]";
						}
						else {
							String separator = "{";
							for (String value : values) {
								info += separator + "[" + strong(value);
								
								if(name.equals("supannEntiteAffectation") || name.equals("supannEntiteAffectationPrincipale")) {
									String libelle = getLibelleSupannEntiteAffectation(value);
									info += " : "
											+ libelle;
								}
								info += "]";
								separator = ", ";
							}
							info += "}";
						}
					}
				}
			}
		} catch (UserNotFoundException e) {
			info += strong(getI18nService().getString(
						"USER_INFO.LDAP.USER_NOT_FOUND", locale, user.getRealId()));
		} catch (LdapException e) {
			info += strong(getI18nService().getString(
						"USER_INFO.LDAP.ERROR", locale,
						user.getRealId(), e.getMessage()));
		}
		info += "</p>";
    	return info;
    }

    /**
     * @param user
     * @param locale
     * @return the Shibboleth info.
     */
    protected String getShibbolethInfo(
    		final User user,
    		final Locale locale) {
    	String info = "<p>";
    	Map<String, List<String>> attributes = user.getAttributes();
    	List<String> names = null;
    	if (attributes != null && !attributes.isEmpty()) {
    		if (shibbolethAttributeNames == null) {
    			names = new ArrayList<String>(attributes.keySet());
    			Collections.sort(names);
    		} else {
    			names = shibbolethAttributeNames;
    		}
    	}
    	if (names == null || names.isEmpty()) {
    		info += em(getI18nService().getString(
    				"USER_INFO.SHIBBOLETH.NO_ATTRIBUTE",
    				locale, user.getRealId()));
    	} else {
    		info += em(getI18nService().getString(
    				"USER_INFO.SHIBBOLETH.ATTRIBUTES",
    				locale, user.getRealId()));
    		for (String name : names) {
    			info += "<br />" + name + "=";
    			List<String> values = attributes.get(name);
    			if (values.isEmpty()) {
    				info += em(getI18nService().getString(
    						"USER_INFO.SHIBBOLETH.NO_VALUE",
    						locale));
    			} else if (values.size() == 1) {
    				info += "[" + strong(values.get(0)) + "]";
    			} else {
    				String separator = "{";
    				for (String value : values) {
    					info += separator + "["
    					+ strong(value) + "]";
    					separator = ", ";
    				}
    				info += "}";
    			}
    		}
    	}
		info += "</p>";
    	return info;
    }

    /**
     * @param locale 
     * @return the browser info.
     */
    protected String getBrowserInfo(
    		final Locale locale) {
    	String info = "<p>";
    	String agent = HttpUtils.getUserAgent();
    	if (StringUtils.nullIfEmpty(agent) == null) {
    		info += em(getI18nService().getString(
    				"USER_INFO.BROWSER.UNKNOWN",
    				locale));
    	} else {
    		BrowserDetector bd = new BrowserDetector(agent);
    		info += em(getI18nService().getString(
    				"USER_INFO.BROWSER.KNOWN",
    				locale, 
    				bd.getUserAgentString(), 
    				bd.getBrowserName(), 
    				bd.getBrowserVersion(), 
    				bd.getBrowserPlatform(),
    				bd.isCssOK() ? "on" : "off",
    			    bd.isJavascriptOK() ? "on" : "off",
    	    	    bd.isFileUploadOK() ? "on" : "off"
    				));
    	}
		info += "</p>";
    	return info;
    }

	/**
	 * @param user
	 * @param locale
	 * @return more info on a LDAP user.
	 */
	protected String getMoreInfo(
			@SuppressWarnings("unused") final User user,
			@SuppressWarnings("unused") final Locale locale) {
		return null;
	}

	/**
	 * @return the ldapUserService
	 */
	protected LdapUserService getLdapUserService() {
		return ldapUserService;
	}

	/**
	 * @param ldapUserService the ldapUserService to set
	 */
	public void setLdapUserService(final LdapUserService ldapUserService) {
		this.ldapUserService = ldapUserService;
	}

	/**
	 * @param attributeNames the attributeNames to set
	 */
	@Deprecated
	public void setAttributeNames(
			@SuppressWarnings("unused") final String attributeNames) {
		throw new ConfigException(
				"property attributeNames of class "
				+ getClass().getSimpleName()
				+ " is deprecated, use ldapAttributeNames and shibbolethAttributeNames instead");
	}

    /**
     * @see org.esupportail.helpdesk.domain.userInfo.UserInfoProvider#test()
     */
    @Override
	public void test() {
    	if (testUserId == null) {
    		logger.error("attribute [testUserId] not set");
    	}
    	try {
			User user = getDomainService().getUserStore().getUserFromRealId(testUserId);
			logger.info(getInfo(user, Locale.getDefault()));
		} catch (UserNotFoundException e) {
			logger.error(e.getMessage());
		}
    }

	/**
	 * @return the testUserId
	 */
	protected String getTestUserId() {
		return testUserId;
	}

	/**
	 * @param testUserId the testUserId to set
	 */
	public void setTestUserId(final String testUserId) {
		this.testUserId = StringUtils.nullIfEmpty(testUserId);
	}

	/**
	 * @return the showTicketCreationDepartments
	 */
	protected boolean isShowTicketCreationDepartments() {
		return showTicketCreationDepartments;
	}

	/**
	 * @param showTicketCreationDepartments the showTicketCreationDepartments to set
	 */
	public void setShowTicketCreationDepartments(
			final boolean showTicketCreationDepartments) {
		this.showTicketCreationDepartments = showTicketCreationDepartments;
	}

	/**
	 * @return the showTicketViewDepartments
	 */
	protected boolean isShowTicketViewDepartments() {
		return showTicketViewDepartments;
	}

	/**
	 * @param showTicketViewDepartments the showTicketViewDepartments to set
	 */
	public void setShowTicketViewDepartments(final boolean showTicketViewDepartments) {
		this.showTicketViewDepartments = showTicketViewDepartments;
	}

	/**
	 * @return the showFaqViewDepartments
	 */
	protected boolean isShowFaqViewDepartments() {
		return showFaqViewDepartments;
	}

	/**
	 * @param showFaqViewDepartments the showFaqViewDepartments to set
	 */
	public void setShowFaqViewDepartments(final boolean showFaqViewDepartments) {
		this.showFaqViewDepartments = showFaqViewDepartments;
	}

	/**
	 * @return the showAdministrator
	 */
	protected boolean isShowAdministrator() {
		return showAdministrator;
	}

	/**
	 * @param showAdministrator the showAdministrator to set
	 */
	public void setShowAdministrator(final boolean showAdministrator) {
		this.showAdministrator = showAdministrator;
	}

	/**
	 * @return the showManagedDepartments
	 */
	protected boolean isShowManagedDepartments() {
		return showManagedDepartments;
	}

	/**
	 * @param showManagedDepartments the showManagedDepartments to set
	 */
	public void setShowManagedDepartments(final boolean showManagedDepartments) {
		this.showManagedDepartments = showManagedDepartments;
	}

	/**
	 * @return the showLdapAttributes
	 */
	protected boolean isShowLdapAttributes() {
		return showLdapAttributes;
	}

	/**
	 * @param showLdapAttributes the showLdapAttributes to set
	 */
	public void setShowLdapAttributes(final boolean showLdapAttributes) {
		this.showLdapAttributes = showLdapAttributes;
	}

	/**
	 * @return the showShibbolethAttributes
	 */
	protected boolean isShowShibbolethAttributes() {
		return showShibbolethAttributes;
	}

	/**
	 * @param showShibbolethAttributes the showShibbolethAttributes to set
	 */
	public void setShowShibbolethAttributes(final boolean showShibbolethAttributes) {
		this.showShibbolethAttributes = showShibbolethAttributes;
	}

	/**
	 * @return the ldapAttributeNames
	 */
	protected List<String> getLdapAttributeNames() {
		return ldapAttributeNames;
	}

	/**
	 * @param ldapAttributeNames the ldapAttributeNames to set
	 */
	public void setLdapAttributeNames(final String ldapAttributeNames) {
		if (ldapAttributeNames == null || !org.springframework.util.StringUtils.hasLength(ldapAttributeNames)) {
			return;
		}
		this.ldapAttributeNames = new ArrayList<String>();
		for (String ldapAttributeName : ldapAttributeNames.split(",")) {
			if (!this.ldapAttributeNames.contains(ldapAttributeName)) {
				this.ldapAttributeNames.add(ldapAttributeName);
			}
		}
		Collections.sort(this.ldapAttributeNames);
	}

	/**
	 * @return the shibbolethAttributeNames
	 */
	protected List<String> getShibbolethAttributeNames() {
		return shibbolethAttributeNames;
	}

	/**
	 * @param shibbolethAttributeNames the shibbolethAttributeNames to set
	 */
	public void setShibbolethAttributeNames(final String shibbolethAttributeNames) {
		if (shibbolethAttributeNames == null
				|| !org.springframework.util.StringUtils.hasLength(shibbolethAttributeNames)) {
			return;
		}
		this.shibbolethAttributeNames = new ArrayList<String>();
		for (String shibbolethAttributeName : shibbolethAttributeNames.split(",")) {
			if (!this.shibbolethAttributeNames.contains(shibbolethAttributeName)) {
				this.shibbolethAttributeNames.add(shibbolethAttributeName);
			}
		}
		Collections.sort(this.shibbolethAttributeNames);
	}

	/**
	 * @return the showBrowser
	 */
	protected boolean isShowBrowser() {
		return showBrowser;
	}

	/**
	 * @param showBrowser the showBrowser to set
	 */
	public void setShowBrowser(final boolean showBrowser) {
		this.showBrowser = showBrowser;
	}
	
	/**
	 * @param ldapTemplate the ldapTemplate to set
	 */
	public void setLdapTemplate(final LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	public List<String> getLdapAmuAttributeNames() {
		return ldapAmuAttributeNames;
	}

	public void setLdapAmuAttributeNames(List<String> ldapAmuAttributeNames) {
		this.ldapAmuAttributeNames = ldapAmuAttributeNames;
	}
}
