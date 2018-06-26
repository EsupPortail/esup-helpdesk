/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.renderers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlOutputFormat;
import javax.faces.context.FacesContext;

import org.apache.myfaces.renderkit.html.HtmlFormatRenderer;
import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.shared_impl.renderkit.RendererUtils;
import org.apache.myfaces.shared_impl.renderkit.html.HtmlTextRendererBase;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
/**
 * An HtmlFormatRenderer that escapes single quotes when no param is passed.
 */
public abstract class AbstractHtmlFormatRenderer extends HtmlFormatRenderer {
	
    /**
     * An empty array.
     */
    private static final Object[] EMPTY_ARGS = new Object[0];

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * Constructor.
	 */
	protected AbstractHtmlFormatRenderer() {
		super();
	}

	/**
	 * Do the specific job for encodeBegin().
	 * @param facesContext 
	 * @param uiComponent 
	 * @throws IOException 
	 */
	protected abstract void internalEncodeBegin(
			FacesContext facesContext, 
			UIComponent uiComponent) 
	throws IOException;

	/**
	 * @see org.apache.myfaces.renderkit.html.HtmlFormatRenderer#encodeBegin(
	 * javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeBegin(final FacesContext facesContext, final UIComponent uiComponent) throws IOException {
		if ((facesContext == null) || (uiComponent == null)) {
			throw new NullPointerException();
		}
		internalEncodeBegin(facesContext, uiComponent);
		super.encodeBegin(facesContext, uiComponent);
	}

	/**
	 * Do the specific job for encodeEnd().
	 * @param facesContext 
	 * @param uiComponent 
	 * @throws IOException 
	 */
	protected abstract void internalEncodeEnd(
			FacesContext facesContext, 
			UIComponent uiComponent) 
	throws IOException;

	/**
	 * A modified version that calls mygetOutputFormatText and adds the 
	 * wrapping tag.
	 * @see org.apache.myfaces.renderkit.html.HtmlFormatRenderer#encodeEnd(
	 * javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeEnd(
			final FacesContext facesContext, 
			final UIComponent component)
	throws IOException {
		RendererUtils.checkParamValidity(facesContext, component, UIOutput.class);
		String text = myGetOutputFormatText(facesContext, component);
		boolean isEscape;
		if (component instanceof HtmlOutputFormat) {
			isEscape = ((HtmlOutputFormat) component).isEscape();
		} else {
			isEscape = RendererUtils.getBooleanAttribute(component, JSFAttr.ESCAPE_ATTR, true);
		}
		HtmlTextRendererBase.renderOutputText(facesContext, component, text, isEscape);
		// added
		internalEncodeEnd(facesContext, component);
	}

	/**
	 * A modified version of getOutputFormatText that does not call 
	 * MessageFormat.format when the component has no parameter.
     * @param facesContext
     * @param htmlOutputFormat
     * @return the result string.
     */
    @SuppressWarnings("unchecked")
	private String myGetOutputFormatText(
    		final FacesContext facesContext,
			final UIComponent htmlOutputFormat) {
		String pattern = RendererUtils.getStringValue(facesContext, htmlOutputFormat);
		Object[] args;
		if (htmlOutputFormat.getChildCount() == 0) {
			args = EMPTY_ARGS;
		} else {
			@SuppressWarnings("rawtypes")
			List argsList = new ArrayList();
			for (Object child : htmlOutputFormat.getChildren()) {
				if (child instanceof UIParameter) {
					Object value = ((UIParameter) child).getValue();
					if (value != null) {
						if (value instanceof Number) {
							value = ((Number) value).toString();
						}
					}
					argsList.add(value);
				}
			}
			args = argsList.toArray(new Object[argsList.size()]);
		}
		if (args.length == 0) {
			// do not call MessageFormat in this case.
			return pattern;
		}
		MessageFormat format = new MessageFormat(pattern, facesContext.getViewRoot().getLocale());
		try {
			return format.format(args);
		} catch (Exception e) {
			logger.error("Error formatting message of component " 
					+ htmlOutputFormat.getClientId(facesContext));
			return "";
		}
	}

}
