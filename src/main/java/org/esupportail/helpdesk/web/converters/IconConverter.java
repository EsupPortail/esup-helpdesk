/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.esupportail.helpdesk.domain.beans.Icon;
import org.springframework.util.StringUtils;

/**
 * A JSF converter to pass Icon instances.
 */
public class IconConverter extends AbstractDomainAwareConverter {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3141619214551565592L;

	/**
	 * The serialization id.
	 */

	/**
	 * Bean constructor.
	 */
	public IconConverter() {
		super();
	}

	/**
	 * @see javax.faces.convert.Converter#getAsObject(
	 * javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */ 
	@Override
	public Object getAsObject(
			@SuppressWarnings("unused") final FacesContext context, 
			@SuppressWarnings("unused") final UIComponent component, 
			final String value) {
		if (!StringUtils.hasText(value)) {
			return null;
		}
		try {
			long longValue = Long.valueOf(value);
			return getDomainService().getIcon(longValue);
		} catch (NumberFormatException e) {
			throw new UnsupportedOperationException(
					"could not convert String [" + value + "] to a Icon.", e);
		}
	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(
	 * javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(
			@SuppressWarnings("unused") final FacesContext context, 
			@SuppressWarnings("unused") final UIComponent component, 
			final Object value) {
		if (value == null || !StringUtils.hasText(value.toString())) {
			return "";
		}
		if (!(value instanceof Icon)) {
			throw new UnsupportedOperationException(
					"object " + value + " is not a Icon.");
		}
		Icon icon = (Icon) value;
		return Long.toString(icon.getId());
	}

}
