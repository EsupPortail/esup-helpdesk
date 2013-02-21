/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.converters;

import javax.faces.convert.Converter;

import org.esupportail.helpdesk.web.controllers.AbstractDomainAwareBean;

/**
 * An abstract domain aware converter.
 */
@SuppressWarnings("serial")
public abstract class AbstractDomainAwareConverter extends AbstractDomainAwareBean implements Converter {

	/**
	 * Bean constructor.
	 */
	public AbstractDomainAwareConverter() {
		super();
	}

}
