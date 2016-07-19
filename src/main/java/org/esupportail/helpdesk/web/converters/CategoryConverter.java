/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.esupportail.helpdesk.domain.beans.Category;
import org.springframework.util.StringUtils;

/**
 * A JSF converter to pass Category instances.
 */
public class CategoryConverter extends AbstractDomainAwareConverter {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -1679804930670933814L;

	/**
	 * Bean constructor.
	 */
	public CategoryConverter() {
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
			return getDomainService().getCategory(longValue);
		} catch (NumberFormatException e) {
			throw new UnsupportedOperationException(
					"could not convert String [" + value + "] to a Category.", e);
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
		if (!(value instanceof Category)) {
			throw new UnsupportedOperationException(
					"object " + value + " is not a Category.");
		}
		Category category = (Category) value;
		return Long.toString(category.getId());
	}

}
