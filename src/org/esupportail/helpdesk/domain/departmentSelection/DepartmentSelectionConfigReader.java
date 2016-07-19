package org.esupportail.helpdesk.domain.departmentSelection;


/**
 * The interface of config readers for the department selection.
 */
public interface DepartmentSelectionConfigReader {

	/**
	 * @return the userDefinedConditions
	 */
	UserDefinedConditions getUserDefinedConditions();

	/**
	 * @return the rules
	 */
	Rules getRules();

	/**
	 * @return the whenEmptyActions
	 */
	Actions getWhenEmptyActions();

	/**
	 * @return a formatted XML representation.
	 */
	String export();

	/**
	 * Move a user-defined condition.
	 * @param index
	 * @param direction
	 * @throws DepartmentSelectionCompileError 
	 */
	void moveUserDefinedCondition(
			int index, String direction) throws DepartmentSelectionCompileError;

	/**
	 * Move a rule.
	 * @param index
	 * @param direction
	 * @throws DepartmentSelectionCompileError 
	 */
	void moveRule(
			int index, String direction) throws DepartmentSelectionCompileError;

	/**
	 * Add a new user-defined condition.
	 * @throws DepartmentSelectionCompileError 
	 */
	void addNewUserDefinedCondition() throws DepartmentSelectionCompileError;

	/**
	 * Replace a user-defined condition.
	 * @param index
	 * @param userDefinedCondition
	 * @throws DepartmentSelectionCompileError 
	 */
	void replaceUserDefinedCondition(
			int index, UserDefinedCondition userDefinedCondition) throws DepartmentSelectionCompileError;

	/**
	 * Replace a rule.
	 * @param index
	 * @param rule
	 * @throws DepartmentSelectionCompileError 
	 */
	void replaceRule(
			int index, Rule rule) throws DepartmentSelectionCompileError;

	/**
	 * Add a new rule.
	 * @throws DepartmentSelectionCompileError 
	 */
	void addNewRule() throws DepartmentSelectionCompileError;

	/**
	 * Remove the default actions.
	 */
	void removeWhenEmptyActions();

}