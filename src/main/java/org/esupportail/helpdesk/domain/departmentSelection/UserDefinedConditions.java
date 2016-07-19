/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition;

/**
 * The user-defined conditions container.
 */
public class UserDefinedConditions extends LinkedHashMap<String, UserDefinedCondition> {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5945166677904044262L;
	
	/**
	 * Constructor.
	 */
	public UserDefinedConditions() {
		super();
	}
	
	/**
	 * Add a condition.
	 * @param userDefinedCondition
	 * @throws DepartmentSelectionCompileError 
	 */
	public void compileAndAdd(
			final UserDefinedCondition userDefinedCondition) 
	throws DepartmentSelectionCompileError {
		String name = userDefinedCondition.getName();
		if (name == null) {
			throw new DepartmentSelectionCompileError(
					"tag <define-condition> should have a 'name' attribute");
		}
		if (super.get(name) != null) {
			throw new DepartmentSelectionCompileError("duplicated condition [" + name + "]"); 
		}
		Condition condition = userDefinedCondition.getCondition();
		if (condition == null) {
			throw new DepartmentSelectionCompileError(
					"user-defined condition [" + name + "] should have one nested condition");
		}
		try {
			condition.compile(this);
		} catch (DepartmentSelectionCompileError e) {
			throw new DepartmentSelectionCompileError(
					"error while compiling user-defined condition [" 
					+ name + "]: " + e.getMessage(), e);
		}
		super.put(name, userDefinedCondition);
	}

	/**
	 * @param name
	 * @return the condition that corresponds to a name, or null.
	 * @throws DepartmentSelectionCompileError 
	 */
	public UserDefinedCondition getUserDefinedCondition(final String name) throws DepartmentSelectionCompileError {
		UserDefinedCondition userDefinedCondition = super.get(name);
		if (userDefinedCondition == null) {
			throw new DepartmentSelectionCompileError("user-defined condition [" + name + "] not found");
		}
		return userDefinedCondition;
	}

	/**
	 * @return the condition names.
	 */
	public List<String> getConditionNames() {
		return new ArrayList<String>(keySet());
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public UserDefinedCondition get(
			@SuppressWarnings("unused")
			final Object key) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public UserDefinedCondition put(
			@SuppressWarnings("unused")
			final String key, 
			@SuppressWarnings("unused")
			final UserDefinedCondition value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "";
		str += "<define-conditions>";
		for (String name : keySet()) {
			str += super.get(name);
		}
		str += "</define-conditions>";
		return str;
	}

	/**
	 * @param index
	 * @return the user-defined condition that corresponds to an index.
	 */
	public UserDefinedCondition getByOrder(final int index) {
		String name = getConditionNames().get(index);
		return super.get(name);
	}
	
}
