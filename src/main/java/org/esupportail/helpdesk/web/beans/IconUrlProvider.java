/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

import org.esupportail.helpdesk.domain.beans.Icon;

/** 
 * An abstract bean to give the urls of the icons.
 */ 
public class IconUrlProvider 
extends HashMap<Icon, String> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5298009058773761628L;
	
	/**
	 * Bean constructor.
	 */
	protected IconUrlProvider() {
		super();
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object o) {
		Icon icon = (Icon) o;
		if (icon == null) {
			return "";
		}
		return "/icon?id=" + icon.getId();
	}

}

