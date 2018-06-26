/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.taglib.html.ext.HtmlPanelGridTag;
import org.esupportail.commons.web.tags.config.TagsConfigurator;

/**
 * ESUP-Portail implementation of the 'panelGrid' tag.
 */
public class PanelGridTag extends HtmlPanelGridTag {

	/**
	 * 'true' if the panelGrid contains alternate colors.
	 */
	private boolean alternateColors;

	/**
	 * Constructor.
	 */
	public PanelGridTag() {
		super();
	}

	/**
	 * @see org.apache.myfaces.taglib.html.ext.HtmlPanelGridTag#setProperties(javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(final UIComponent component) {
		TagsConfigurator tagsConfigurator = TagsConfigurator.getInstance();
		String styleClass = null;
		if (alternateColors) {
			styleClass = tagsConfigurator.getDataTableRowClass() 
			+ ", " + tagsConfigurator.getDataTableRowAlternateClass();
		} else {
			styleClass = tagsConfigurator.getDataTableRowClass();
		}
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.ROW_CLASSES_ATTR, styleClass);
		}

		styleClass = tagsConfigurator.getDataTableHeaderClass();
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.HEADER_CLASS_ATTR, styleClass);
		}
		styleClass = tagsConfigurator.getDataTableFooterClass();
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.FOOTER_CLASS_ATTR, styleClass);
		}
		styleClass = tagsConfigurator.getDataTableColumnClass();
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.COLUMN_CLASSES_ATTR, styleClass);
		}
		super.setProperties(component);
	}

	/**
	 * @param alternateColors the alternateColors to set
	 */
	public void setAlternateColors(final boolean alternateColors) {
		this.alternateColors = alternateColors;
	}

}
