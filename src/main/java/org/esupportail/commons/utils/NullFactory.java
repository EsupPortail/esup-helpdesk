/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils; 

/**
 * A class to create null beans.
 */
public abstract class NullFactory {

	/**
	 * Constructor.
	 */
	private NullFactory() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return a null object
	 */
	public static Object create() {
		return null;
	}

}
