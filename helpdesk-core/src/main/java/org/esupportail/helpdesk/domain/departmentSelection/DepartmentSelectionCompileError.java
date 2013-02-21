/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import org.esupportail.commons.exceptions.EsupException;


/**
 * An exception thrown on department selection compile errors.
 */
public class DepartmentSelectionCompileError extends EsupException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = 8753384673448371150L;

	/**
	 * Bean constructor.
	 * @param message
	 */
	public DepartmentSelectionCompileError(final String message) {
		super(message);
	}
	
	/**
	 * Bean constructor.
	 * @param message
	 * @param cause 
	 */
	public DepartmentSelectionCompileError(
			final String message,
			final Throwable cause) {
		super(message, cause);
	}
	
}
