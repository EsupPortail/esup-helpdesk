/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.taglib.html.HtmlOutputLabelTag;
import org.esupportail.commons.web.tags.config.TagsConfigurator;

/**
 * ESUP-Portail implementation of the 'outputLabel' tag.
 */
public class OutputLabelTag extends HtmlOutputLabelTag {

	/**
	 * Bean constructor.
	 */
	public OutputLabelTag() {
		super();
	}

	/**
	 * @see org.apache.myfaces.shared_impl.taglib.html.HtmlOutputLabelTagBase#setProperties(
	 * javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(final UIComponent component) {
		String styleClass = TagsConfigurator.getInstance().getLabelStyleClass();
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.STYLE_CLASS_ATTR, styleClass);
		}
		super.setProperties(component);
	}

}
