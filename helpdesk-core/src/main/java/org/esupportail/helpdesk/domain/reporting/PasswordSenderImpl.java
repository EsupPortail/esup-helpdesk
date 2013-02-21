/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.reporting;

import java.util.Locale;

import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.utils.SystemUtils;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * The basic implementation of PasswordSender.
 */
public class PasswordSenderImpl extends AbstractSender implements PasswordSender {

	/**
	 * Bean constructor.
	 */
	public PasswordSenderImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.reporting.PasswordSender#sendPasswordEmail(
	 * org.esupportail.helpdesk.domain.beans.User, java.util.Locale)
	 */
	@Override
	public void sendPasswordEmail(
			final User user,
			final Locale locale) {
		Locale theLocale = locale;
		if (theLocale == null && user.getLanguage() != null) {
			theLocale = new Locale(user.getLanguage());
		}
		if (theLocale == null) {
			theLocale = getI18nService().getDefaultLocale();
		}
		String subject = getI18nService().getString(
				"EMAIL.SEND_PASSWORD.SUBJECT",
				theLocale, getApplicationService().getName(), SystemUtils.getServer());
		String htmlContent = getI18nService().getString(
				"EMAIL.SEND_PASSWORD.BODY", theLocale,
				getApplicationService().getName(),
				SystemUtils.getServer(),
				user.getRealId(),
				user.getPassword(),
				getUrlBuilder().getChangePasswordUrl(user),
				getUrlBuilder().getWelcomeUrl(AuthUtils.APPLICATION),
				getUrlBuilder().getControlPanelUrl(AuthUtils.APPLICATION),
				getUrlBuilder().getPreferencesUrl(AuthUtils.APPLICATION),
				getUrlBuilder().getAboutUrl(AuthUtils.APPLICATION),
				getApplicationService().getVersion().toString(),
				getApplicationService().getCopyright());
		send(user.getRealId(), theLocale, genMessageId(), subject, htmlContent);
	}

}
