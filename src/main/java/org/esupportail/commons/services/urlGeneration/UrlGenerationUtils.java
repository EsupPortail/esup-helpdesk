/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.urlGeneration; 

import org.esupportail.commons.utils.BeanUtils;

/**
 * A class that provides static utilities for URL generation.
 */
public final class UrlGenerationUtils {
	
	/**
	 * The name of the URL generator bean.
	 */
	private static final String URL_GENERATOR_BEAN = "urlGenerator";

	/**
	 * Private constructor.
	 */
	private UrlGenerationUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return a URL generator.
	 */
	public static UrlGenerator createUrlGenerator() {
		return (UrlGenerator) BeanUtils.getBean(URL_GENERATOR_BEAN);
	}
	
}

