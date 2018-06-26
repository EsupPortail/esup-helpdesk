/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.component;

import java.io.IOException;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.apache.myfaces.component.html.ext.HtmlOutputText;
import org.apache.myfaces.custom.div.Div;
import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;


/**
 * This class represents e:form elements.
 */
public class UIForm extends javax.faces.component.UIForm {

	/**
	 * The type Component.
	 */
	public static final String COMPONENT_TYPE = "org.esupportail.commons.component.form";
	
	/**
	 * The id of the submit popup.
	 */
	public static final String SUBMIT_POPUP_ID = "submitPopup";
	
	/**
	 * The submitPopupText attribute.
	 */
	public static final String SUBMIT_POPUP_TEXT = "submitPopupText";
	
	/**
	 * The submitPopupImage attribute.
	 */
	public static final String SUBMIT_POPUP_IMAGE = "submitPopupImage";
	
	/**
	 * Constructor.
	 */
	public UIForm() {
		super();
	}

	/**
	 * @return true if the form already has a submit popup.
	 */
	@SuppressWarnings("unchecked")
	protected boolean hasSubmitPopup() {
		for (UIComponent c : (List<UIComponent>) getChildren()) {
			if (SUBMIT_POPUP_ID.equals(c.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Add the submitPopup.
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	protected void addSubmitPopupIfNeeded(final FacesContext context) {
		Application application = context.getApplication();
		// add a div
		Div div = new Div();
		div.setId("submitPopup");
		div.setStyleClass("form-submit-popup");
		div.setStyle("visibility: hidden");
		div.setRendered(true);
		getChildren().add(div);
		// add the image to the div
		HtmlGraphicImage img = new HtmlGraphicImage();
		img.setId("submitPopupImage");
		img.setValue(getAttributes().get(SUBMIT_POPUP_IMAGE));
		img.setStyle("visibility: hidden");
		img.setRendered(true);
		div.getChildren().add(img);
		// add the text to the div
		HtmlOutputText text = new HtmlOutputText();
		text.setId("submitPopupText");
		text.setValueBinding(JSFAttr.VALUE_ATTR,  
				application.createValueBinding(
						getAttributes().get(SUBMIT_POPUP_TEXT).toString()));
		text.setStyle("visibility: hidden");
		text.setRendered(true);
		div.getChildren().add(text);
	}

	/**
	 * @see javax.faces.component.UIComponentBase#encodeBegin(javax.faces.context.FacesContext)
	 */
	@Override
	public void encodeBegin(final FacesContext context) throws IOException {
		if (!hasSubmitPopup()) {
			addSubmitPopupIfNeeded(context);
		}
		super.encodeBegin(context);
	}

	/**
	 * @see javax.faces.component.UIComponentBase#getRendersChildren()
	 */
	@Override
	public boolean getRendersChildren() {
		return true;
	}

	/**
	 * @see javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context.FacesContext)
	 */
	@Override
	public void encodeChildren(final FacesContext context) throws IOException {
		RendererUtils.renderChildren(context, this);
	}
	
}
