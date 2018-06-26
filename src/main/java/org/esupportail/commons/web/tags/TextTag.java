/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import org.esupportail.commons.web.renderers.TextRenderer;
import org.esupportail.commons.web.tags.config.TagsConfigurator;

/**
 * ESUP-Portail implementation of the 'paragraph' tag.
 */
public class TextTag extends AbstractStyleClassWrapperTag {

	/**
	 * Constructor.
	 */
	public TextTag() {
		super();
	}
	
	/**
	 * @see org.apache.myfaces.taglib.html.HtmlOutputFormatTag#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return TextRenderer.RENDERER_TYPE;
	}

	/**
	 * @see org.esupportail.commons.web.tags.AbstractStyleClassWrapperTag#getStyleClass()
	 */
	@Override
	protected String getStyleClass() {
		return TagsConfigurator.getInstance().getTextStyleClass();
	}
}
