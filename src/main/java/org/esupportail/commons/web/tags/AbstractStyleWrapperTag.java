/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags;


/**
 * An abstract class for tags that set a default style.
 */
public abstract class AbstractStyleWrapperTag extends AbstractWrapperTag {

	/**
	 * Constructor.
	 */
	public AbstractStyleWrapperTag() {
		super();
	}

	/**
	 * @see org.esupportail.commons.web.tags.AbstractWrapperTag#getStyleClass()
	 */
	@Override
	protected String getStyleClass() {
		return null;
	}
	
}
