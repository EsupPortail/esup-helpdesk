/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import org.esupportail.commons.web.renderers.SubSectionRenderer;
import org.esupportail.commons.web.tags.config.TagsConfigurator;

/**
 * ESUP-Portail implementation of the 'subSection' tag.
 */
public class SubSectionTag extends AbstractStyleClassWrapperTag {

	/**
	 * Constructor.
	 */
	public SubSectionTag() {
		super();
	}
	
	/**
	 * @see org.apache.myfaces.taglib.html.HtmlOutputFormatTag#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return SubSectionRenderer.RENDERER_TYPE;
	}

	/**
	 * @see org.esupportail.commons.web.tags.AbstractStyleClassWrapperTag#getStyleClass()
	 */
	@Override
	protected String getStyleClass() {
		return TagsConfigurator.getInstance().getSubSectionStyleClass();
	}
}
