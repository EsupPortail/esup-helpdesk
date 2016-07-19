/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.custom.navmenu.htmlnavmenu.HtmlPanelNavigationMenuTag;
import org.esupportail.commons.web.tags.config.TagsConfigurator;

/**
 * The ESUP-Portail implementation of the panelNavigationMenu tag.
 */
public class PanelNavigationMenuTag extends HtmlPanelNavigationMenuTag {

	/**
	 * Constructor.
	 */
	public PanelNavigationMenuTag() {
		super();
	}
	
	/**
	 * @see org.apache.myfaces.custom.navmenu.htmlnavmenu.HtmlPanelNavigationMenuTag#setProperties(
	 * javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(final UIComponent component) {
		TagsConfigurator tagsConfigurator = TagsConfigurator.getInstance(); 
		setLayout(tagsConfigurator.getMenuLayout());
		setStyleClass(tagsConfigurator.getMenuStyleClass());
		setActiveItemClass(tagsConfigurator.getMenuActiveItemStyleClass());
		setItemClass(tagsConfigurator.getMenuItemStyleClass());
		super.setProperties(component);
	}
}
