/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.converters;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.springframework.util.StringUtils;

/**
 * A JSF converter to pass Long instances.
 */
public class LongConverter implements Converter, Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8709423975164637410L;

	/**
	 * Bean constructor.
	 */
	public LongConverter() {
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
		return Long.valueOf(value);
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
		if (!(value instanceof Long)) {
			throw new UnsupportedOperationException(
					"object " + value + " is not a Long.");
		}
		Long l = (Long) value;
		return l.toString();
	}

}
