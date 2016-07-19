/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.esupportail.helpdesk.domain.beans.User;
import org.springframework.util.StringUtils;

/**
 * A JSF converter to pass User instances.
 */
public class UserConverter extends AbstractDomainAwareConverter {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3611182213087090650L;

	/**
	 * Bean constructor.
	 */
	public UserConverter() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractDomainAwareBean#afterPropertiesSetInternal()
	 */
	@Override
	protected void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
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
		return getUserStore().getExistingUserFromId(value);
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
		if (!(value instanceof User)) {
			throw new UnsupportedOperationException(
					"object " + value + " is not a User.");
		}
		User user = (User) value;
		return user.getId();
	}

}
