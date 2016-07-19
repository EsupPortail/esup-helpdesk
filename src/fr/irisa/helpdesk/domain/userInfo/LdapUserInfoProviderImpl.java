/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package fr.irisa.helpdesk.domain.userInfo;

import java.util.List;
import java.util.Locale;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.services.ldap.LdapException;
import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.userInfo.BasicUserInfoProviderImpl;

/**
 * IRISA customization to provide information on users:
 *     getInfo returns (only for local users):
 *       - A link to a web page.
 *       - The user ldap attributes.
 *       => no information about departments...
 */
public class LdapUserInfoProviderImpl extends BasicUserInfoProviderImpl {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6969609189036945208L;

	/**
	 * Constructor.
	 */
	public LdapUserInfoProviderImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.userInfo.BasicUserInfoProviderImpl#getInfo(
	 * org.esupportail.helpdesk.domain.beans.User, java.util.Locale)
	 */
	@Override
	public String getInfo(
			final User user,
			final Locale locale) {
		String info = "";
		I18nService i18nService = this.getI18nService();
		LdapUserService ldapUserService = this.getLdapUserService();
		List<String> ldapAttributeNames = this.getLdapAttributeNames();
		if (getDomainService().getUserStore().isCasUser(user)) {
			info += "<p>";
			try {
				LdapUser ldapUser = ldapUserService.getLdapUser(user.getRealId());
				info += "<b>"
					+ "<a target=\"_blank\" "
					+ "href=\"http://intranet.irisa.fr/intranet-fs/PHOTOS/FICHES_USERS/"
					+ user.getRealId()
					+ ".html\">"
					+ i18nService.getString("IRISA.USER_INFO.FICHE_USER",
							locale)
							+ "</a>"
							+ "</b>"
							+ "<br/><br/>";
				List<String> names;
				if (ldapAttributeNames == null) {
					names = ldapUser.getAttributeNames();
				} else {
					names = ldapAttributeNames;
				}
				if (names.isEmpty()) {
					info += "<em>"
						+ i18nService.getString(
								"USER_INFO.LDAP.NO_ATTRIBUTE", locale, user.getRealId())
								+ "</em>";
				} else {
					info += "<em>"
						+ i18nService.getString(
								"USER_INFO.LDAP.ATTRIBUTES", locale, user.getRealId())
								+ "</em>";
					for (String name : names) {
						info += "<br />" + name + "=";
						List<String> values = ldapUser.getAttributes(name);
						if (values.isEmpty()) {
							info += "<em>"
								+ i18nService.getString(
										"USER_INFO.LDAP.NO_VALUE", locale)
										+ "</em>";
						} else if (values.size() == 1) {
							info += "[<strong>" + values.get(0) + "</strong>]";
						} else {
							String separator = "{";
							for (String value : values) {
								info += separator + "[<strong>" + value + "</strong>]";
								separator = ", ";
							}
							info += "}";
						}
					}
				}
			} catch (UserNotFoundException e) {
				info += "<strong>"
					+ i18nService.getString(
							"USER_INFO.LDAP.USER_NOT_FOUND", locale, user.getRealId())
					+ "</strong></p>";
			} catch (LdapException e) {
				info += "<strong>"
					+ i18nService.getString(
							"USER_INFO.LDAP.ERROR", locale,
							user.getRealId(), e.getMessage())
							+ "</strong></p>";
			}
			info += "</p>";
		}

		return StringUtils.nullIfEmpty(info);
	}

}
