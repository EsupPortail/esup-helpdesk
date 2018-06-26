/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.renderers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.shared_impl.renderkit.html.HTML;
import org.apache.myfaces.shared_impl.renderkit.html.HtmlButtonRendererBase;
import org.apache.myfaces.shared_impl.renderkit.html.HtmlFormRendererBase;
import org.apache.myfaces.shared_impl.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_impl.renderkit.html.util.JavascriptUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

/**
 * ESUP-Portail renderer for the 'commandButton' tag.
 */
public class CommandButtonRenderer extends HtmlButtonRendererBase {

	/**
	 * The renderer type.
	 */
	public static final String RENDERER_TYPE = "org.esupportail.CommandButtonRenderer";

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
	public CommandButtonRenderer() {
		super();
	}

	/**
	 * @see org.apache.myfaces.shared_impl.renderkit.html.HtmlButtonRendererBase#encodeEnd(
	 * javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeEnd(
			final FacesContext facesContext, 
			final UIComponent uiComponent) 
	throws IOException {
        org.apache.myfaces.shared_impl.renderkit.RendererUtils.checkParamValidity(
        		facesContext, uiComponent, UICommand.class);

        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);

        writer.writeAttribute(HTML.ID_ATTR, clientId, org.apache.myfaces.shared_impl.renderkit.JSFAttr.ID_ATTR);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, JSFAttr.ID_ATTR);

        String image = getImage(uiComponent);

        ExternalContext externalContext = facesContext.getExternalContext();

        if (image != null) {
            writer.writeAttribute(
            		HTML.TYPE_ATTR, 
            		HTML.INPUT_TYPE_IMAGE, 
            		org.apache.myfaces.shared_impl.renderkit.JSFAttr.TYPE_ATTR);
            String src = facesContext.getApplication().getViewHandler().getResourceURL(
                facesContext, image);
            writer.writeURIAttribute(HTML.SRC_ATTR, externalContext.encodeResourceURL(src),
                                     org.apache.myfaces.shared_impl.renderkit.JSFAttr.IMAGE_ATTR);
        } else {
            String type = getType(uiComponent);

            if (type == null) {
                type = HTML.INPUT_TYPE_SUBMIT;
            }
            writer.writeAttribute(HTML.TYPE_ATTR, type, org.apache.myfaces.shared_impl.renderkit.JSFAttr.TYPE_ATTR);
            Object value = myGetValue(facesContext, uiComponent);
            if (value != null) {
                writer.writeAttribute(
                		org.apache.myfaces.shared_impl.renderkit.html.HTML.VALUE_ATTR, 
                		value, 
                		org.apache.myfaces.shared_impl.renderkit.JSFAttr.VALUE_ATTR);
            }
        }
        if (JavascriptUtils.isJavascriptAllowed(externalContext)) {
            StringBuffer onClick = buildOnClick(uiComponent, facesContext, writer);
            writer.writeAttribute(HTML.ONCLICK_ATTR, onClick.toString(), null);
            HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent,
                                                   HTML.BUTTON_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED_AND_ONCLICK);
        } else {
            HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent,
                                                   HTML.BUTTON_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        }

        if (isDisabled(facesContext, uiComponent)) {
            writer.writeAttribute(
            		HTML.DISABLED_ATTR, Boolean.TRUE, 
            		org.apache.myfaces.shared_impl.renderkit.JSFAttr.DISABLED_ATTR);
        }

        writer.endElement(HTML.INPUT_ELEM);

        HtmlFormRendererBase.renderScrollHiddenInputIfNecessary(
            findNestingForm(uiComponent, facesContext).getForm(), facesContext, writer);
	}
	
    /**
     * The same as the ancestor, accessible. 
     * @param uiComponent
     * @return the same as the ancestor.
     */
    private String getImage(final UIComponent uiComponent) {
        if (uiComponent instanceof HtmlCommandButton) {
            return ((HtmlCommandButton) uiComponent).getImage();
        }
        return (String) uiComponent.getAttributes().get(JSFAttr.IMAGE_ATTR);
    }

    /**
     * The same as the ancestor, accessible. 
     * @param uiComponent
     * @return the same as the ancestor.
     */
    private String getType(final UIComponent uiComponent) {
        if (uiComponent instanceof HtmlCommandButton) {
            return ((HtmlCommandButton) uiComponent).getType();
        }
        return (String) uiComponent.getAttributes().get(org.apache.myfaces.shared_impl.renderkit.JSFAttr.TYPE_ATTR);
    }

	/**
	 * A modified version of getValue that does calls 
	 * MessageFormat.format when the component has parameters.
     * @param facesContext
     * @param commandButton
     * @return the result string.
     */
    @SuppressWarnings("unchecked")
	private String myGetValue(
    		final FacesContext facesContext,
			final UIComponent commandButton) {
    	Object value;
        if (commandButton instanceof ValueHolder) {
            value = ((ValueHolder) commandButton).getValue();
        } else {
        	value = commandButton.getAttributes().get(JSFAttr.VALUE_ATTR);
        }
        if (value == null) {
        	return null;
        }
        String pattern = value.toString();
		Object[] args;
		if (commandButton.getChildCount() == 0) {
			args = EMPTY_ARGS;
		} else {
			@SuppressWarnings("rawtypes")
			List argsList = new ArrayList();
			for (Object child : commandButton.getChildren()) {
				if (child instanceof UIParameter) {
					Object val = ((UIParameter) child).getValue();
					if (val != null) {
						if (val instanceof Number) {
							val = ((Number) val).toString();
						}
					}
					argsList.add(val);
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
					+ commandButton.getClientId(facesContext));
			return "";
		}
	}

}
