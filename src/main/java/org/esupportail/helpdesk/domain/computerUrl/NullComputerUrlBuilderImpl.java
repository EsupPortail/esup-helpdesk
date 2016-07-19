/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.computerUrl;

import java.util.Locale;

/**
 * A basic implementation of ComputerUrlBuilder that always returns null.
 *
 */
public class NullComputerUrlBuilderImpl extends AbstractComputerUrlBuilder {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -1820655345467735512L;

	/**
	 * Constructor.
	 */
	public NullComputerUrlBuilderImpl() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.computerUrl.ComputerUrlBuilder#getUrl(java.lang.String)
	 */
	@Override
	public String getUrl(
			@SuppressWarnings("unused")
			final String computer) {
		return null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.computerUrl.ComputerUrlBuilder#getDescription(java.util.Locale)
	 */
	@Override
	public String getDescription(final Locale locale) {
		return getI18nService().getString("DOMAIN.COMPUTER_URL_BUILDER.NULL", locale);
	}

}
