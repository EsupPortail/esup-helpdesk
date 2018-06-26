/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.deepLinking;

import org.esupportail.commons.beans.AbstractI18nAwareBean;
import org.esupportail.commons.web.controllers.Resettable;

/**
 * A deep linking redirector that does nothing.
 */
@SuppressWarnings("serial")
public abstract class AbstractDeepLinkingRedirector 
extends AbstractI18nAwareBean implements DeepLinkingRedirector, Resettable {

	/**
	 * A parameter name.
	 */
	public static final String ENTER_PARAM = "enter";
	
	/**
	 * True until firstCall() is called().
	 */
	private boolean called;
	
	/**
	 * Bean constructor.
	 */
	public AbstractDeepLinkingRedirector() {
		super();
		reset();
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	@Override
	public void reset() {
		called = false;
	}

	/**
	 * @see org.esupportail.commons.web.deepLinking.DeepLinkingRedirector#firstCall()
	 */
	@Override
	public boolean firstCall() {
		boolean oldCalled = called;
		called = true;
		return !oldCalled;
	}

	/**
	 * Print an error message when a parameter is missing.
	 * @param param
	 */
	protected void addErrorMessageMissingParameter(
			final String param) {
		addErrorMessage(null, "DEEP_LINKS.MESSAGE.MISSING_PARAMETER", param);
	}

	/**
	 * Print an error message when a parameter is invalid.
	 * @param param
	 * @param value 
	 */
	protected void addErrorMessageInvalidParameter(
			final String param,
			final String value) {
		addErrorMessage(null, "DEEP_LINKS.MESSAGE.INVALID_PARAMETER", param, value);
	}

}
