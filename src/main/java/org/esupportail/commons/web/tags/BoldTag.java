/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;


/**
 * ESUP-Portail implementation of the 'bold' tag.
 */
public class BoldTag extends TextTag {

	/**
	 * The bold style.
	 */
	private static final String BOLD_STYLE = "font-weight: bold;"; 
	
	/**
	 * Constructor.
	 */
	public BoldTag() {
		super();
	}
	
	/**
	 * @see org.esupportail.commons.web.tags.AbstractStyleClassWrapperTag#getStyle()
	 */
	@Override
	protected String getStyle() {
		return BOLD_STYLE;
	}

}
