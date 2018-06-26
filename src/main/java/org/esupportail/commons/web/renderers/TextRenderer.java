/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.renderers;

import org.apache.myfaces.shared_impl.renderkit.html.HTML;

/**
 * ESUP-Portail renderer for the section tag.
 */
public class TextRenderer extends AbstractTagWrapperRenderer {

	/**
	 * The renderer type.
	 */
	public static final String RENDERER_TYPE = "org.esupportail.TextRenderer";

	/**
	 * Constructor.
	 */
	public TextRenderer() {
		super();
	}
	
	/**
	 * @see org.esupportail.commons.web.renderers.AbstractTagWrapperRenderer#getTag()
	 */
	@Override
	protected String getTag() {
		return HTML.SPAN_ELEM;
	}

}
