/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.deepLinking;

import java.io.Serializable;
import java.util.Map;

/**
 * The interface of a page redirector, used for deep linking.
 */
public interface DeepLinkingRedirector extends Serializable {
	
	/**
	 * @return true if the director was never called before. 
	 */
	boolean firstCall();

	/**
	 * @param params the deep link parameters
	 * @return the view to redirect to, or null for the default view. 
	 */
	String redirect(
			final Map<String, String> params);

}
