/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import java.util.Map;

import javax.faces.component.UIComponent;

import org.apache.myfaces.custom.htmlTag.HtmlTagTag;
import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.shared_impl.renderkit.html.HTML;

/**
 * ESUP-Portail implementation of the 'ul' tag.
 */
public class UlTag extends HtmlTagTag {
	
	/**
	 * Constructor.
	 */
	public UlTag() {
		super();
	}

	/**
	 * @see org.apache.myfaces.custom.htmlTag.HtmlTagTag#setProperties(javax.faces.component.UIComponent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void setProperties(final UIComponent component) {
		super.setProperties(component);
		@SuppressWarnings("rawtypes")
		Map componentAttributes = component.getAttributes(); 
		componentAttributes.put(JSFAttr.VALUE_ATTR, HTML.UL_ELEM);
	}

}
