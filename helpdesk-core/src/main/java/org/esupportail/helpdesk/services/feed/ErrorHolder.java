/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to hold errors.
 */
public class ErrorHolder implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 385088799968026345L;

	/** 
	 * the number of errors. 
	 */
	private int errorNumber;
	
	/** 
	 * the strings. 
	 */
	private List<String> strings;
	
	/**
	 * Constructor.
	 */
	public ErrorHolder() {
		super();
		strings = new ArrayList<String>();
		errorNumber = 0;
	}
	
	/**
	 * Add an info.
	 * @param theStrings
	 */
	public void addInfo(final List<String> theStrings) {
		for (String string : theStrings) {
			addInfo(string);
		}
	}

	/**
	 * Add an info.
	 * @param string
	 */
	public void addInfo(final String string) {
		strings.add(string);
	}

	/**
	 * Add an error.
	 * @param theStrings
	 */
	public void addError(final List<String> theStrings) {
		addInfo(theStrings);
		errorNumber++;
	}

	/**
	 * Add an error.
	 * @param string
	 */
	public void addError(final String string) {
		addInfo(string);
		errorNumber++;
	}

	/**
	 * Add another error holder.
	 * @param errorHolder
	 */
	public void add(final ErrorHolder errorHolder) {
		strings.addAll(errorHolder.getStrings());
		errorNumber += errorHolder.getErrorNumber();
	}

	/**
	 * @return true when errors were added.
	 */
	public boolean hasErrors() {
		return errorNumber > 0;
	}

	/**
	 * @return the errorNumber
	 */
	public int getErrorNumber() {
		return errorNumber;
	}

	/**
	 * @return the strings
	 */
	public List<String> getStrings() {
		return strings;
	}

}
