/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.mock;

import javax.faces.context.FacesContext;

/**
 * A Faces context helper. 
 *
 */
public abstract class MockFacesContextHelper extends FacesContext {

	/**
	 * @param context
	 *            Set this context as the used singleton.
	 */
	public static void setMockContext(final FacesContext context) {
		setCurrentInstance(context);
	}

}
