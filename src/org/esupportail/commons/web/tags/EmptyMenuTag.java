/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.component.UIComponent;

/**
 * ESUP-Portail implementation of the 'emptyMenu' tag.
 */
public class EmptyMenuTag extends FooterTag {

	/**
	 * Constructor.
	 */
	public EmptyMenuTag() {
		super();
	}
	
	/**
	 * @see org.apache.myfaces.shared_impl.taglib.html.HtmlOutputFormatTagBase#setProperties(
	 * javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(final UIComponent component) {
		setValue("&nbsp;");
		setEscape("false");
		super.setProperties(component);
	}

	
}
