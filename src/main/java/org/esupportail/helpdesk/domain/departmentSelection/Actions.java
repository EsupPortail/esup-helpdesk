/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.departmentSelection.actions.Action;


/**
 * The actions container.
 */
public class Actions {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6705327721262888377L;
	
	/**
	 * The actions themselves.
	 */
	private List<Action> actions;

	/**
	 * Constructor.
	 */
	public Actions() {
		super();
		actions = new ArrayList<Action>();
	}
	
	/**
	 * Add an action.
	 * @param action
	 */
	public void addAction(
			final Action action) {
		actions.add(action);
	}

	/**
	 * @return true if nested actions.
	 */
	protected boolean hasAction() {
		return !actions.isEmpty();
	}

	/**
	 * @return true if nested actions.
	 */
	public List<Action> getActions() {
		return actions;
	}

	/**
	 * Evaluate the actions.
	 * @param domainService
	 * @param type 
	 * @return a set of departments.
	 */
	public Result eval(
			final DomainService domainService,
			final int type) {
		Result result = new Result();
		for (Action action : actions) {
			if (action.evalForType(type)) {
				action.eval(domainService, result);
				if (!result.evaluateNextRule()) {
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		String str = "<actions>";
		str += contentToString();
		str += "</actions>";
		return str;
	}

	/**
	 * @return the content as a string.
	 */
	public String contentToString() {
		String str = "";
		for (Action action : actions) {
			str += action;
		}
		return str;
	}

}
