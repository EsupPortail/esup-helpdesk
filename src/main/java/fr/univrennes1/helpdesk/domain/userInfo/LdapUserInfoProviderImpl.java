/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package fr.univrennes1.helpdesk.domain.userInfo;

import java.util.List;
import java.util.Locale;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.ldap.LdapException;
import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.userInfo.BasicUserInfoProviderImpl;

/**
 * The Rennes1 extension of BasicUserInfoProviderImpl.
 */
public class LdapUserInfoProviderImpl extends BasicUserInfoProviderImpl {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2676469353139985414L;

	/**
	 * Constructor.
	 */
	public LdapUserInfoProviderImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userInfo.BasicUserInfoProviderImpl#getMoreInfo(
	 * org.esupportail.helpdesk.domain.beans.User, java.util.Locale)
	 */
	@Override
	protected String getMoreInfo(
			final User user, 
			final Locale locale) {
    	try {
			LdapUser ldapUser = getLdapUserService().getLdapUser(user.getRealId());
			String emailAttribute = "mail";
			String urlAttribute = "labeledUri";
			List<String> values;
			String moreInfo = "";
			values = ldapUser.getAttributes(emailAttribute);
			if (!values.isEmpty()) {
				moreInfo += "<br />" 
					+ getI18nService().getString("USER_INFO.LDAP.EMAIL_LINKS", locale) + " ";
				if (values.size() == 1) {
					moreInfo += "<strong><a href=\"mailto:" 
						+ values.get(0) + "\">" + values.get(0) + "</a></strong>";
				} else {
					String separator = "{";
					for (String value : values) {
						moreInfo += separator + "<strong><a href=\"mailto:" 
						+ value + "\">" + value + "</a></strong>";
						separator = ", ";
					}
					moreInfo += "}";
				}
			}
			values = ldapUser.getAttributes(urlAttribute);
			if (!values.isEmpty()) {
				moreInfo += "<br />" 
					+ getI18nService().getString("USER_INFO.LDAP.WEB_PAGES", locale) + " ";
				if (values.size() == 1) {
					moreInfo += "<strong><a href=\"" + values.get(0) 
						+ "\" target=\"_blank\">" + values.get(0) + "</a></strong>";
				} else {
					String separator = "{";
					for (String value : values) {
						moreInfo += separator + "<strong><a href=\"" + value 
							+ "\" target=\"_blank\">" + value + "</a></strong>";
						separator = ", ";
					}
					moreInfo += "}";
				}
			}
			return StringUtils.nullIfEmpty(moreInfo);
		} catch (UserNotFoundException e) {
			//
		} catch (LdapException e) {
			//
		}
		return null;
	}

}
