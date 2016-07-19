/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.renderers;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * An abstract class for renderers that wrap into a HTML and set a default style class.
 */
public abstract class AbstractTagWrapperRenderer extends AbstractHtmlFormatRenderer {
	
	/**
	 * Constructor.
	 */
	protected AbstractTagWrapperRenderer() {
		super();
	}

	/**
	 * @return the wrapping tag.
	 */
	protected abstract String getTag();

	/**
	 * @see org.esupportail.commons.web.renderers.AbstractHtmlFormatRenderer#internalEncodeBegin(
	 * javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	@Override
	public void internalEncodeBegin(
			final FacesContext facesContext, 
			final UIComponent uiComponent) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		writer.startElement(getTag(), uiComponent);
	}

	/**
	 * @see org.esupportail.commons.web.renderers.AbstractHtmlFormatRenderer#internalEncodeEnd(
	 * javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	@Override
	public void internalEncodeEnd(
			final FacesContext facesContext, 
			@SuppressWarnings("unused") final UIComponent component)
	throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		writer.endElement(getTag());
	}

}
