/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.taglib.html.HtmlMessageTag;
import org.esupportail.commons.web.tags.config.TagsConfigurator;

/**
 * An extension of h:messages.
 */
public class MessageTag extends HtmlMessageTag {

	/**
	 * Constructor.
	 */
	public MessageTag() {
		super();
	}
	
	/**
	 * @see org.apache.myfaces.shared_impl.taglib.html.HtmlMessageTagBase#setProperties(
	 * javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(final UIComponent component) {
		TagsConfigurator tagsConfigurator = TagsConfigurator.getInstance();
		String styleClass = tagsConfigurator.getMessagesInfoClass();
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.INFO_CLASS_ATTR, styleClass);
		}
		styleClass = tagsConfigurator.getMessagesWarnClass();
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.WARN_CLASS_ATTR, styleClass);
		}
		styleClass = tagsConfigurator.getMessagesErrorClass();
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.ERROR_CLASS_ATTR, styleClass);
		}
		styleClass = tagsConfigurator.getMessagesFatalClass();
		if (styleClass != null) {
			setStringProperty(component, JSFAttr.FATAL_CLASS_ATTR, styleClass);
		}
        super.setProperties(component);
    }

}
