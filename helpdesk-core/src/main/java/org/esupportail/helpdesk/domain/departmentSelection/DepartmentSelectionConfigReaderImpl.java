/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.aop.monitor.Monitor;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.strings.XmlUtils;
import org.esupportail.helpdesk.domain.departmentSelection.actions.Action;
import org.esupportail.helpdesk.domain.departmentSelection.actions.DoNothingAction;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.FalseCondition;
import org.esupportail.helpdesk.web.controllers.DepartmentSelectionController;
import org.springframework.util.StringUtils;

/**
 * This class reads the configuration of the department selection.
 */
@Monitor
public class DepartmentSelectionConfigReaderImpl implements Serializable, DepartmentSelectionConfigReader {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7523371895705933609L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The description.
	 */
	private String description;

	/**
	 * Raw user-defined conditions.
	 */
	private List<UserDefinedCondition> rawUserDefinedConditions;

	/**
	 * Compiled user-defined conditions.
	 */
	private UserDefinedConditions userDefinedConditions;

	/**
	 * Raw rules.
	 */
	private List<Rule> rawRules;

	/**
	 * Compiles rules.
	 */
	private Rules rules;

	/**
	 * Raw actions.
	 */
	private List<Action> rawActions;

	/**
	 * Compiled actions.
	 */
	private Actions whenEmptyActions;

	/**
	 * Constructor, read the configuration from a string.
	 * @param config
	 * @throws DepartmentSelectionCompileError
	 */
	public DepartmentSelectionConfigReaderImpl(final String config)
	throws DepartmentSelectionCompileError {
		super();
		if (!StringUtils.hasText(config)) {
			throw new DepartmentSelectionCompileError("null config");
		}
		rawUserDefinedConditions = new ArrayList<UserDefinedCondition>();
		rawRules = new ArrayList<Rule>();
		rawActions = new ArrayList<Action>();
		DigesterUtils.parseConfigReader(config, this);
		compile();
	}

	/**
	 * Constructor, read the configuration from another config reader.
	 * @param configReader
	 * @throws DepartmentSelectionCompileError
	 */
	public DepartmentSelectionConfigReaderImpl(
			final DepartmentSelectionConfigReader configReader)
	throws DepartmentSelectionCompileError {
		this(configReader.toString());
	}

	/**
	 * Compile.
	 * @throws DepartmentSelectionCompileError
	 */
	public void compile()
	throws DepartmentSelectionCompileError {
		userDefinedConditions = new UserDefinedConditions();
		rules = new Rules();
		whenEmptyActions  = new Actions();
		for (UserDefinedCondition userDefinedCondition : rawUserDefinedConditions) {
			userDefinedConditions.compileAndAdd(userDefinedCondition);
		}
		for (Rule rule : rawRules) {
			rules.compileAndAdd(userDefinedConditions, rule);
		}
		int i = 0;
		for (Action action : rawActions) {
			try {
				action.compile();
				whenEmptyActions.addAction(action);
			} catch (DepartmentSelectionCompileError e) {
				throw new DepartmentSelectionCompileError(
						"default action #" + i + ": " + e.getMessage());
			}
			i++;
		}
		if (logger.isDebugEnabled()) {
			logger.debug(toString());
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		str += "<department-selection><description>";
		if (description != null) {
			str += description;
		}
		str += "</description>";
		str += userDefinedConditions;
		str += rules;
		str += whenEmptyActions;
		str += "</department-selection>";
		return str;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader#export()
	 */
	@Override
	public String export() {
		return XmlUtils.format(toString(), false);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader
	 * #moveUserDefinedCondition(int, java.lang.String)
	 */
	@Override
	public void moveUserDefinedCondition(
			final int index,
			final String direction)
	throws DepartmentSelectionCompileError {
		UserDefinedCondition userDefinedConditionToMove = rawUserDefinedConditions.get(index);
		rawUserDefinedConditions.remove(index);
		if (DepartmentSelectionController.FIRST.equals(direction)) {
			rawUserDefinedConditions.add(0, userDefinedConditionToMove);
		} else if (DepartmentSelectionController.UP.equals(direction)) {
			rawUserDefinedConditions.add(index - 1, userDefinedConditionToMove);
		} else if (DepartmentSelectionController.DOWN.equals(direction)) {
			rawUserDefinedConditions.add(index + 1, userDefinedConditionToMove);
		} else if (DepartmentSelectionController.LAST.equals(direction)) {
			rawUserDefinedConditions.add(userDefinedConditionToMove);
		}
		compile();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader
	 * #moveRule(int, java.lang.String)
	 */
	@Override
	public void moveRule(
			final int index,
			final String direction)
	throws DepartmentSelectionCompileError {
		Rule ruleToMove = rawRules.get(index);
		rawRules.remove(index);
		if (DepartmentSelectionController.FIRST.equals(direction)) {
			rawRules.add(0, ruleToMove);
		} else if (DepartmentSelectionController.UP.equals(direction)) {
			rawRules.add(index - 1, ruleToMove);
		} else if (DepartmentSelectionController.DOWN.equals(direction)) {
			rawRules.add(index + 1, ruleToMove);
		} else if (DepartmentSelectionController.LAST.equals(direction)) {
			rawRules.add(ruleToMove);
		}
		compile();
	}

	/**
	 * Set the condition.
	 * @param cond the condition to add
	 * @throws DepartmentSelectionCompileError
	 */
	public void addCondition(
			final Condition cond) throws DepartmentSelectionCompileError {
		throw new DepartmentSelectionCompileError("unexpected condition: " + cond);
	}

	/**
	 * add a user-defined condition.
	 * @param userDefinedCondition the condition
	 */
	public void addUserDefinedCondition(
			final UserDefinedCondition userDefinedCondition) {
		rawUserDefinedConditions.add(userDefinedCondition);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader
	 * #addNewUserDefinedCondition()
	 */
	@Override
	public void addNewUserDefinedCondition() throws DepartmentSelectionCompileError {
		UserDefinedCondition newUserDefinedCondition = new UserDefinedCondition();
		String name = "";
		while (true) {
			name += "?";
			boolean nameUsed = false;
			for (UserDefinedCondition userDefinedCondition : rawUserDefinedConditions) {
				if (name.equals(userDefinedCondition.getName())) {
					nameUsed = true;
					break;
				}
			}
			if (!nameUsed) {
				newUserDefinedCondition.setName(name);
				break;
			}
		}
		newUserDefinedCondition.addCondition(new FalseCondition());
		rawUserDefinedConditions.add(newUserDefinedCondition);
		compile();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader
	 * #replaceUserDefinedCondition(int, org.esupportail.helpdesk.domain.departmentSelection.UserDefinedCondition)
	 */
	@Override
	public void replaceUserDefinedCondition(
			final int index,
			final UserDefinedCondition userDefinedCondition)
	throws DepartmentSelectionCompileError {
		UserDefinedCondition oldUserDefinedCondition = rawUserDefinedConditions.get(index);
		rawUserDefinedConditions.remove(index);
		rawUserDefinedConditions.add(index, userDefinedCondition);
		if (!oldUserDefinedCondition.getName().equals(userDefinedCondition.getName())) {
			for (UserDefinedCondition theUserDefinedCondition : rawUserDefinedConditions) {
				theUserDefinedCondition.getCondition().refactorNamedConditions(
						oldUserDefinedCondition.getName(),
						userDefinedCondition.getName());
			}
			for (Rule rule : rawRules) {
				rule.getCondition().refactorNamedConditions(
						oldUserDefinedCondition.getName(),
						userDefinedCondition.getName());
			}
		}
		compile();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader
	 * #addNewRule()
	 */
	@Override
	public void addNewRule() throws DepartmentSelectionCompileError {
		Rule newRule = new Rule();
		newRule.addAction(new DoNothingAction());
		newRule.setName("?");
		newRule.addCondition(new FalseCondition());
		rawRules.add(newRule);
		compile();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader
	 * #replaceRule(int, org.esupportail.helpdesk.domain.departmentSelection.Rule)
	 */
	@Override
	public void replaceRule(
			final int index,
			final Rule rule)
	throws DepartmentSelectionCompileError {
		rawRules.remove(index);
		rawRules.add(index, rule);
		compile();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader
	 * #removeWhenEmptyActions()
	 */
	@Override
	public void removeWhenEmptyActions() {
		rawActions = new ArrayList<Action>();
	}

	/**
	 * add an action.
	 * @param action the action
	 */
	public void addAction(final Action action) {
		rawActions.add(action);
	}

	/**
	 * add a rule to compute the list of the departments that will be seen by a user.
	 * @param rule the rule to add
	 */
	public void addRule(final Rule rule) {
		rawRules.add(rule);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader
	 * #getUserDefinedConditions()
	 */
	@Override
	public UserDefinedConditions getUserDefinedConditions() {
		return userDefinedConditions;
	}

	/**
	 * @param userDefinedConditions the userDefinedConditions to set
	 */
	protected void setUserDefinedConditions(final UserDefinedConditions userDefinedConditions) {
		this.userDefinedConditions = userDefinedConditions;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader#getRules()
	 */
	@Override
	public Rules getRules() {
		return rules;
	}

	/**
	 * @param rules the rules to set
	 */
	protected void setRules(final Rules rules) {
		this.rules = rules;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionConfigReader
	 * #getWhenEmptyActions()
	 */
	@Override
	public Actions getWhenEmptyActions() {
		return whenEmptyActions;
	}

	/**
	 * @param whenEmptyActions the whenEmptyActions to set
	 */
	protected void setWhenEmptyActions(final Actions whenEmptyActions) {
		this.whenEmptyActions = whenEmptyActions;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

}
