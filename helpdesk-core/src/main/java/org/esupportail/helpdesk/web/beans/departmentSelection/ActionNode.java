/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans.departmentSelection; 

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelector;
import org.esupportail.helpdesk.domain.departmentSelection.Result;
import org.esupportail.helpdesk.domain.departmentSelection.actions.Action;
import org.esupportail.helpdesk.web.beans.AbstractFirstLastNode;

/** 
 * The node of an action.
 */ 
public class ActionNode extends AbstractFirstLastNode {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2716474797166130997L;

	/**
	 * The action.
	 */
	private Action action;
	
	/**
	 * The result of the evaluation of the condition.
	 */
	private Result evalResult;

	/**
	 * Bean constructor.
	 * @param action
	 */
	public ActionNode(
			final Action action) {
		super(action.getNodeType(), "", true);
		this.action = action;
		this.evalResult = null;
	}
	
	/**
	 * Evaluate the associated condition.
	 * @param domainService 
	 * @param type 
	 */
	public void eval(
			final DomainService domainService,
			final int type) {
		if (action.evalForType(type)) {
			evalResult = new Result();
			action.eval(domainService, evalResult);
		}
	}
	
	/**
	 * Reset the evaluation.
	 */
	public void resetEval() {
		evalResult = null;
	}
	
	/**
	 * @return the evalResult
	 */
	public Result getEvalResult() {
		return evalResult;
	}

	/**
	 * @param evalResult the evalResult to set
	 */
	protected void setEvalResult(final Result evalResult) {
		this.evalResult = evalResult;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	protected void setAction(final Action action) {
		this.action = action;
	}

	/**
	 * @return true if the action is for ticket creation.
	 */
	public boolean isForTicketCreation() {
		return action.evalForType(DepartmentSelector.TICKET_CREATION_SELECTION);
	}
	
	/**
	 * @return true if the action is for ticket view.
	 */
	public boolean isForTicketView() {
		return action.evalForType(DepartmentSelector.TICKET_VIEW_SELECTION);
	}
	
	/**
	 * @return true if the action is for FAQ view.
	 */
	public boolean isForFaqView() {
		return action.evalForType(DepartmentSelector.FAQ_VIEW_SELECTION);
	}
	
}

