/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans.departmentSelection; 

import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.departmentSelection.Actions;
import org.esupportail.helpdesk.domain.departmentSelection.Result;
import org.esupportail.helpdesk.domain.departmentSelection.actions.Action;
import org.esupportail.helpdesk.web.beans.AbstractFirstLastNode;

/** 
 * The node of actions.
 */ 
@SuppressWarnings("serial")
public abstract class AbstractActionsNode extends AbstractFirstLastNode {
	
	/**
	 * The result of the evaluation of the condition.
	 */
	private Result evalResult;

	/**
	 * The actions.
	 */
	private Actions actions;

	/**
	 * Bean constructor.
	 * @param nodeType 
	 * @param actions 
	 */
	protected AbstractActionsNode(
			final String nodeType,
			final Actions actions) {
		super(nodeType, "", true);
		this.actions = actions;
		this.evalResult = null;
	}
	
	/**
	 * Add action nodes.
	 */
	@SuppressWarnings("unchecked")
	protected void addActionNodes() {
		for (Action action : actions.getActions()) {
			getChildren().add(new ActionNode(action));
    		setLeaf(false);
    	}
		markFirstAndLastChildNodes();
	}
	
	/**
	 * @return the sub action nodes
	 */
	protected abstract List<ActionNode> getActionNodes();

	/**
	 * Evaluate the associated condition.
	 * @param domainService 
	 * @param type 
	 * @param conditionMatched 
	 */
	protected void eval(
			final DomainService domainService,
			final int type,
			final boolean conditionMatched) {
		if (conditionMatched) {
			evalResult = actions.eval(domainService, type);
			for (ActionNode actionNode  : getActionNodes()) {
				actionNode.eval(domainService, type);
				if (actionNode.getEvalResult() != null) {
					if (!actionNode.getEvalResult().evaluateNextRule()) {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Reset the evaluation.
	 */
	public void resetEval() {
		evalResult = null;
		for (ActionNode actionNode  : getActionNodes()) {
			actionNode.resetEval();
		}
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
	
}

