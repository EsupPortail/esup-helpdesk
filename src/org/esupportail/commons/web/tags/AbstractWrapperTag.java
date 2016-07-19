/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.renderkit.html.HTML;
import org.apache.myfaces.taglib.html.HtmlOutputFormatTag;

/**
 * An abstract class for tags that wrap into a HTML tag.
 */
public abstract class AbstractWrapperTag extends HtmlOutputFormatTag {

	/**
	 * Constructor.
	 */
	public AbstractWrapperTag() {
		super();
	}
	
	/**
	 * @return the style to apply.
	 */
	protected abstract String getStyle();

	/**
	 * @return the style class to apply.
	 */
	protected abstract String getStyleClass();

	/**
	 * @see org.apache.myfaces.shared_impl.taglib.html.HtmlOutputFormatTagBase#setProperties(
	 * javax.faces.component.UIComponent)
	 */
	@Override
	protected void setProperties(final UIComponent component) {
		String style = getStyle();
		if (style != null) {
			setStringProperty(component, HTML.STYLE_ATTR, getStyle());
		}
		String styleClass = getStyleClass();
		if (styleClass != null) {
			setStringProperty(component, HTML.STYLE_CLASS_ATTR, getStyleClass());
		}
		super.setProperties(component);
	}

}
