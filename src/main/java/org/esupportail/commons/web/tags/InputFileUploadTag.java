/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.custom.fileupload.HtmlInputFileUploadTag;
import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.esupportail.commons.web.tags.config.TagsConfigurator;

/**
 * ESUP-Portail implementation of the 'inputFile' tag.
 */
public class InputFileUploadTag extends HtmlInputFileUploadTag {

	/**
	 * Constructor.
	 */
	public InputFileUploadTag() {
		super();
	}

	/**
	 * @see org.apache.myfaces.custom.fileupload.HtmlInputFileUploadTag#setProperties(
	 * javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(final UIComponent component) {
		String styleClass = TagsConfigurator.getInstance().getInputFieldStyleClass();
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.STYLE_CLASS_ATTR, styleClass);
		}
		super.setProperties(component);
	}
}
