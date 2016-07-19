/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.renderers;

import org.esupportail.commons.web.tags.config.TagsConfigurator;

/**
 * ESUP-Portail renderer for the section tag.
 */
public class ParagraphRenderer extends AbstractTagWrapperRenderer {

	/**
	 * The renderer type.
	 */
	public static final String RENDERER_TYPE = "org.esupportail.ParagraphRenderer";

	/**
	 * Constructor.
	 */
	public ParagraphRenderer() {
		super();
	}
	
	/**
	 * @see org.esupportail.commons.web.renderers.AbstractTagWrapperRenderer#getTag()
	 */
	@Override
	protected String getTag() {
		return TagsConfigurator.getInstance().getParagraphTag();
	}

}
