/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans.departmentSelection; 

import java.net.InetAddress;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.AndCondition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition;
import org.esupportail.helpdesk.domain.departmentSelection.conditions.OrCondition;
import org.esupportail.helpdesk.web.beans.AbstractFirstLastNode;

/** 
 * The node of a condition.
 */ 
public class ConditionNode extends AbstractFirstLastNode {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 496103317490409378L;

	/**
	 * The condition.
	 */
	private Condition condition;
	
	/**
	 * The result of the evualuation of the condition.
	 */
	private Boolean evalResult;

	/**
	 * Bean constructor.
	 * @param condition 
	 */
	@SuppressWarnings("unchecked")
	public ConditionNode(
			final Condition condition) {
		super(condition.getNodeType(), "", true);
		this.condition = condition;
		this.evalResult = null;
		if (condition.getSubConditions() != null) {
			for (Condition subCondition : condition.getSubConditions()) {
				getChildren().add(new ConditionNode(subCondition));
	    		setLeaf(false);
	    	}
			markFirstAndLastChildNodes();
		}
	}
	
	/**
	 * @return the sub nodes
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	protected List<ConditionNode> getSubConditionNodes() {
		return (List<ConditionNode>) getChildren();
	}

	/**
	 * Evaluate the associated condition.
	 * @param domainService 
	 * @param user 
	 * @param client 
	 */
	public void eval(
			final DomainService domainService,
			final User user,
			final InetAddress client) {
		evalResult = condition.isMatched(domainService, user, client);
		if (condition.getSubConditions() != null) {
			for (ConditionNode subConditionNode  : getSubConditionNodes()) {
				subConditionNode.eval(domainService, user, client);
				if (condition instanceof OrCondition && subConditionNode.getEvalResult()) {
					return;
				}
				if (condition instanceof AndCondition && !subConditionNode.getEvalResult()) {
					return;
				}
			}
		}
	}
	
	/**
	 * Reset the evaluation.
	 */
	public void resetEval() {
		evalResult = null;
		for (ConditionNode subConditionNode  : getSubConditionNodes()) {
			subConditionNode.resetEval();
		}
	}
	
	/**
	 * @return the condition
	 */
	public Condition getCondition() {
		return condition;
	}

	/**
	 * @param condition the condition to set
	 */
	protected void setCondition(final Condition condition) {
		this.condition = condition;
	}

	/**
	 * @return the evalResult
	 */
	public Boolean getEvalResult() {
		return evalResult;
	}

	/**
	 * @param evalResult the evalResult to set
	 */
	protected void setEvalResult(final Boolean evalResult) {
		this.evalResult = evalResult;
	}
	
}

