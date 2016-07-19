/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esupportail.helpdesk.domain.beans.Department;

/**
 * The result of the evaluation of rules.
 */
public class Result {

	/**
	 * The resulting department set.
	 */
	private Set<Department> departments = new HashSet<Department>();
	
	/**
	 * A boolean set to true to skip the evaluation of the next rules.
	 */
	private boolean skipNextRules;

	/**
	 * Constructor.
	 */
	public Result() {
		super();
		skipNextRules = false;
	}

	/**
	 * @return Returns the department set.
	 */
	public Set<Department> getDepartments() {
		return departments;
	}
	
	/**
	 * Add a department to the result set.
	 * @param department The department to set.
	 */
	public void addDepartment(final Department department) {
		departments.add(department);
	}

	/**
	 * Add departments to the result set.
	 * @param deps
	 */
	public void addDepartments(final List<Department> deps) {
		departments.addAll(deps);
	}

	/**
	 * Tell whether the next rules should be evaluated or not. 
	 * @return a boolean.
	 */
	public boolean evaluateNextRule() {
		return !skipNextRules;
	}
	
	/**
	 * Tell whether the next rules should be evaluated or not. 
	 * @return a boolean.
	 */
	public boolean isSkipNextRule() {
		return skipNextRules;
	}
	
	/**
	 * Stop the evaluation of the next rules.
	 */
	public void stopAfterThisRule() {
		skipNextRules = true;
	}
}
