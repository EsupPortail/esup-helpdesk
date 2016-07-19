/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.deepLinking; 

import org.esupportail.commons.utils.BeanUtils;

/**
 * A class that provides static utilities for URL generation.
 */
public final class DeepLinkingUtils {
	
	/**
	 * The name of the redirector bean.
	 */
	private static final String DEEP_LINKING_REDIRECTOR_BEAN = "deepLinkingRedirector";

	/**
	 * Private constructor.
	 */
	private DeepLinkingUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return a URL generator.
	 */
	public static DeepLinkingRedirector createDeepLinkingRedirector() {
		return (DeepLinkingRedirector) BeanUtils.getBean(DEEP_LINKING_REDIRECTOR_BEAN);
	}
	
}

