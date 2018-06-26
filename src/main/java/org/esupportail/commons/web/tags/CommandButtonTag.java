/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.taglib.html.HtmlCommandButtonTag;
import org.esupportail.commons.web.renderers.CommandButtonRenderer;
import org.esupportail.commons.web.tags.config.TagsConfigurator;

/**
 * ESUP-Portail implementation of the 'button' tag.
 */
public class CommandButtonTag extends HtmlCommandButtonTag {

	/**
	 * Bean constructor.
	 */
	public CommandButtonTag() {
		super();
	}

	/**
	 * @see org.apache.myfaces.taglib.html.HtmlOutputFormatTag#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return CommandButtonRenderer.RENDERER_TYPE;
	}

	/**
	 * @see org.apache.myfaces.shared_impl.taglib.html.HtmlCommandButtonTagBase#setProperties(
	 * javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(final UIComponent component) {
		String styleClass = TagsConfigurator.getInstance().getButtonStyleClass();
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.STYLE_CLASS_ATTR, styleClass);
		}
		super.setProperties(component);
	}
}
