/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import org.apache.myfaces.taglib.html.HtmlCommandButtonTag;
import org.esupportail.commons.web.renderers.MenuItemRenderer;
/**
 * ESUP-Portail implementation of the 'menuItem' tag.
 */
public class MenuItemTag extends HtmlCommandButtonTag {
	
	/**
	 * Constructor.
	 */
	public MenuItemTag() {
		super();
	}
	
	/**
	 * @see org.apache.myfaces.taglib.html.HtmlCommandButtonTag#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return MenuItemRenderer.RENDERER_TYPE;
	}
	
}
