/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.taglib.html.HtmlFormTag;
import org.esupportail.commons.web.component.UIForm;

/**
 * This class is a tag for esup-commons form element.
 */
public class FormTag extends HtmlFormTag {

	/**
	 * The text of the submit popup.
	 */
	private String submitPopupText = "#{msgs['FORM_SUBMIT_POPUP_TEXT']}";
	
	/**
	 * The image of the submit popup.
	 */
	private String submitPopupImage = "/media/images/please-wait.gif";
	
	/**
	 * True to show the submit popup text. 
	 */
	private String showSubmitPopupText = "true";
	
	/**
	 * True to show the submit popup image. 
	 */
	private String showSubmitPopupImage = "true";
	
	/**
	 * True to freeze the screen on submit. 
	 */
	private String freezeScreenOnSubmit = "true";
	
	/**
	 * The onsubmit property.
	 */
	private String onsubmit;
	
	/**
	 * Constructor.
	 */
	public FormTag() {
		super();
	}

	/**
	 * @see org.apache.myfaces.taglib.html.HtmlFormTag#getComponentType()
	 */
	@Override
	public String getComponentType() {
		return UIForm.COMPONENT_TYPE;
	}
	
	/**
	 * @return the boolean value of an expression.
	 * @param context
	 * @param application
	 * @param expr
	 * @param defaultValue 
	 */
	protected boolean getBooleanValue(
			final FacesContext context,
			final Application application,
			final String expr,
			final boolean defaultValue) {
        if (!UIComponentTag.isValueReference(expr)) {
        	if (expr == null) {
        		return defaultValue;
        	}
            return Boolean.valueOf(expr);
       	}
    	ValueBinding vb = application.createValueBinding(expr);
    	Object value = vb.getValue(context);
    	if (value == null) {
    		return defaultValue;
    	}
    	if (value instanceof Boolean) {
            return (Boolean) value;
        }
    	if (value instanceof String) {
            return Boolean.valueOf(value.toString());
    	}
    	throw new IllegalArgumentException("Boolean or String class expected for [" + expr + "]");
	}

	/**
	 * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void setProperties(final UIComponent component) {
		component.getAttributes().put(UIForm.SUBMIT_POPUP_TEXT, submitPopupText);
		component.getAttributes().put(UIForm.SUBMIT_POPUP_IMAGE, submitPopupImage);
		String theOnsubmit = "";
		if (onsubmit != null) {
			theOnsubmit += "{ " + onsubmit + " }; ";
		}
		theOnsubmit += "if (typeof(onFormSubmit2) == 'function') { onFormSubmit2(this, ";
		theOnsubmit += freezeScreenOnSubmit;
		theOnsubmit += ", ";
		theOnsubmit += showSubmitPopupText;
		theOnsubmit += ", ";
		theOnsubmit += showSubmitPopupImage;
		theOnsubmit += "); } else onFormSubmit();";
		super.setOnsubmit(theOnsubmit);
		super.setProperties(component);
	}

	/**
	 * @return the submitPopupText
	 */
	protected String getSubmitPopupText() {
		return submitPopupText;
	}

	/**
	 * @param submitPopupText the submitPopupText to set
	 */
	public void setSubmitPopupText(final String submitPopupText) {
		this.submitPopupText = submitPopupText;
	}

	/**
	 * @return the submitPopupImage
	 */
	protected String getSubmitPopupImage() {
		return submitPopupImage;
	}

	/**
	 * @param submitPopupImage the submitPopupImage to set
	 */
	public void setSubmitPopupImage(final String submitPopupImage) {
		this.submitPopupImage = submitPopupImage;
	}

	/**
	 * @return the showSubmitPopupText
	 */
	protected String getShowSubmitPopupText() {
		return showSubmitPopupText;
	}

	/**
	 * @param showSubmitPopupText the showSubmitPopupText to set
	 */
	public void setShowSubmitPopupText(final String showSubmitPopupText) {
		this.showSubmitPopupText = showSubmitPopupText;
	}

	/**
	 * @return the showSubmitPopupImage
	 */
	protected String getShowSubmitPopupImage() {
		return showSubmitPopupImage;
	}

	/**
	 * @param showSubmitPopupImage the showSubmitPopupImage to set
	 */
	public void setShowSubmitPopupImage(final String showSubmitPopupImage) {
		this.showSubmitPopupImage = showSubmitPopupImage;
	}

	/**
	 * @return the freezeScreenOnSubmit
	 */
	protected String getFreezeScreenOnSubmit() {
		return freezeScreenOnSubmit;
	}

	/**
	 * @param freezeScreenOnSubmit the freezeScreenOnSubmit to set
	 */
	public void setFreezeScreenOnSubmit(final String freezeScreenOnSubmit) {
		this.freezeScreenOnSubmit = freezeScreenOnSubmit;
	}

	/**
	 * @return the onsubmit
	 */
	protected String getOnsubmit() {
		return onsubmit;
	}

	/**
	 * @param onsubmit the onsubmit to set
	 */
	@Override
	public void setOnsubmit(final String onsubmit) {
		this.onsubmit = onsubmit;
	}

}
