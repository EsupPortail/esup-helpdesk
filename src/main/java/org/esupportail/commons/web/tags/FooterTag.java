/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.taglib.html.HtmlOutputFormatTag;
import org.esupportail.commons.web.renderers.FooterRenderer;
import org.esupportail.commons.web.tags.config.TagsConfigurator;

/**
 * ESUP-Portail implementation of the 'section' tag.
 */
public class FooterTag extends HtmlOutputFormatTag {

	/**
	 * Constructor.
	 */
	public FooterTag() {
		super();
	}
	
	/**
	 * @see org.apache.myfaces.taglib.html.HtmlOutputFormatTag#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return FooterRenderer.RENDERER_TYPE;
	}

	/**
	 * @see org.apache.myfaces.shared_impl.taglib.html.HtmlOutputFormatTagBase#setProperties(
	 * javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(final UIComponent component) {
		TagsConfigurator tagsConfigurator = TagsConfigurator.getInstance();
		String footerText = tagsConfigurator.getFooterText();
		if (footerText != null) {
			setStringProperty(component, JSFAttr.VALUE_ATTR , footerText);
		}
		super.setProperties(component);
	}

	
}
