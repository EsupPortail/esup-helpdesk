/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import java.net.InetAddress;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.actions.Action;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition;


/**
 * A basic abstract class to implement rules (default and list).
 */
public class Rule extends Actions {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 4408485449355019432L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * The name.
	 */
	private String name;

	/**
	 * The description.
	 */
	private String description;

	/**
	 * The condition to match.
	 */
	private Condition condition;
	
	/**
	 * Empty constructor (for Digester).
	 */
	public Rule() {
		super();
	}
	
	/**
	 * Set the condition of the rule (should be called setCondition, hack for Digester).
	 * @param cond 
	 * @throws DepartmentSelectionCompileError 
	 */
	public void addCondition(final Condition cond) throws DepartmentSelectionCompileError {
		if (this.condition != null) {
			throw new DepartmentSelectionCompileError(
					"<rule> tags should be used with one nested condition only");
		}
		this.condition = cond;
	}
	
	/**
	 * Compile the condition of the rule.
	 * @param userDefinedConditions 
	 * @throws DepartmentSelectionCompileError 
	 */
	final void compile(final UserDefinedConditions userDefinedConditions) 
	throws DepartmentSelectionCompileError {
//		if (this.name == null) {
//			throw new DepartmentSelectionCompileError(
//					"<rule> tags should have a 'name' attribute");
//		}
		if (this.condition == null) {
			throw new DepartmentSelectionCompileError(
					"no nested condition for rule [" + name + "]");
		}
		try {
			this.condition.compile(userDefinedConditions);
		} catch (DepartmentSelectionCompileError e) {
			throw new DepartmentSelectionCompileError(
					"condition of rule [" + name + "]: " + e.getMessage());
		}
		if (!hasAction()) {
			throw new DepartmentSelectionCompileError(
					"no nested action for rule [" + name + "]");
		}
		int i = 0;
		for (Action action : getActions()) {
			try {
				action.compile();
			} catch (DepartmentSelectionCompileError e) {
				throw new DepartmentSelectionCompileError(
						"action #" + i + " of rule [" + name + "]: " 
						+ e.getMessage());
			}
			i++;
		}
	}
	
	/**
	 * Evaluate the rule and return a list of departments that the user will see.
	 * @param domainService
	 * @param user the user
	 * @param client the client
	 * @param result the result
	 * @param type 
	 */
	void eval(
			final DomainService domainService,
			final User user,
			final InetAddress client,
			final Result result,
			final int type) {
		boolean evaluateRule = false;
		boolean evaluateCondition = true;

		for (Action action : getActions()) {
			if (action.evalForType(type)) {
				evaluateRule = true;
				break;
			}
		}
		if (!evaluateRule) {
			if (logger.isDebugEnabled()) {
				logger.debug("no action to evaluate, skipping rule");
			}
			evaluateCondition = false;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("evaluating rule " + this + "...");
		}
		if (!condition.isMatched(domainService, user, client)) {
			if (logger.isDebugEnabled()) {
				logger.debug("condition of rule " + this + " is not matched");
			}
			evaluateCondition = false;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("condition of rule " + this + " is matched");
		}
		for (Action action : getActions()) {
			if (action.evalForType(type)) {
				action.eval(domainService, result, evaluateCondition);
				if (!result.evaluateNextRule()) {
					return;
				}
			}
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "<rule name=\"";
		if (name != null) {
			str += name;
		}
		str += "\" >";
		str += contentToString();
		str += "</rule>";
		return str;
	}

	/**
	 * @return the content as a string.
	 */
	@Override
	public String contentToString() {
		String str = "";
		str += "<description>";
		if (description != null) {
			str += description;
		}
		str += "</description>";
		str += "<condition>" + condition + "</condition>";
		str += "<actions>";
		for (Action action : getActions()) {
			str += action;
		}
		str += "</actions>";
		return str;
	}

	/**
	 * @return Returns the condition.
	 */
	public Condition getCondition() {
		return condition;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = StringUtils.nullIfEmpty(name);
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