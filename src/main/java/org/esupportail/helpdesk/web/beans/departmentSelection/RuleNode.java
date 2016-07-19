/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans.departmentSelection; 

import java.net.InetAddress;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.Rule;

/** 
 * The node of a rule.
 */ 
public class RuleNode extends AbstractActionsNode {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 622169427186139115L;
	
	/**
	 * The rule.
	 */
	private Rule rule;

	/**
	 * The node index.
	 */
	private int index;

	/**
	 * Bean constructor.
	 * @param index 
	 * @param rule
	 */
	@SuppressWarnings("unchecked")
	public RuleNode(
			final int index,
			final Rule rule) {
		super("rule", rule);
		this.rule = rule;
		this.index = index;
		getChildren().add(new ConditionNode(rule.getCondition()));
   		setLeaf(false);
		addActionNodes();
	}
	
	/**
	 * @return the sub nodes
	 */
	@Override
	@SuppressWarnings({ "unchecked", "cast" })
	public List<ActionNode> getActionNodes() {
		return (List<ActionNode>) getChildren().subList(1, getChildCount());
	}

	/**
	 * Evaluate the associated condition.
	 * @param domainService 
	 * @param user 
	 * @param client 
	 * @param type 
	 */
	public void eval(
			final DomainService domainService,
			final User user,
			final InetAddress client,
			final int type) {
		ConditionNode conditionNode = (ConditionNode) getChildren().get(0);
		conditionNode.eval(domainService, user, client);
		super.eval(domainService, type, conditionNode.getEvalResult());
	}

	/**
	 * @see org.esupportail.helpdesk.web.beans.departmentSelection.AbstractActionsNode#resetEval()
	 */
	@Override
	public void resetEval() {
		super.resetEval();
		ConditionNode conditionNode = (ConditionNode) getChildren().get(0);
		conditionNode.resetEval();
	}

	/**
	 * @return the rule
	 */
	public Rule getRule() {
		return rule;
	}

	/**
	 * @param rule the rule to set
	 */
	protected void setRule(final Rule rule) {
		this.rule = rule;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
}

