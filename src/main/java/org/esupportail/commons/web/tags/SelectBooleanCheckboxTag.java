/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.taglib.html.HtmlSelectBooleanCheckboxTag;
import org.esupportail.commons.web.tags.config.TagsConfigurator;

/**
 * ESUP-Portail implementation of the 'selectBooleanCheckBox' tag.
 */
public class SelectBooleanCheckboxTag extends HtmlSelectBooleanCheckboxTag {

	/**
	 * Constructor.
	 */
	public SelectBooleanCheckboxTag() {
		super();
	}

	/**
	 * @see org.apache.myfaces.shared_impl.taglib.html.HtmlSelectBooleanCheckboxTagBase#setProperties(
	 * javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(final UIComponent component) {

		String styleClass = TagsConfigurator.getInstance().getCheckboxStyleClass();
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.STYLE_CLASS_ATTR, styleClass);
		}
		super.setProperties(component);
	}
}
