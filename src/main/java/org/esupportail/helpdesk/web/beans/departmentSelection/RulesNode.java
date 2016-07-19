/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans.departmentSelection; 

import java.net.InetAddress;
import java.util.List;
import java.util.Set;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.Rule;
import org.esupportail.helpdesk.domain.departmentSelection.Rules;
import org.esupportail.helpdesk.web.beans.AbstractFirstLastNode;

/** 
 * The node of rules.
 */ 
public class RulesNode extends AbstractFirstLastNode {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6119701834161480488L;
	
	/**
	 * The rules.
	 */
	private Rules rules;
	
	/**
	 * The result of the evaluation.
	 */
	private Set<Department> evalResult;

	/**
	 * Bean constructor.
	 * @param rules
	 */
	@SuppressWarnings("unchecked")
	public RulesNode(
			final Rules rules) {
		super("rules", "", true);
		this.rules = rules;
		int index = 0;
		for (Rule rule : rules.getRules()) {
			getChildren().add(new RuleNode(index, rule));
    		setLeaf(false);
    		index++;
    	}
		markFirstAndLastChildNodes();
	}
	
	/**
	 * @return the sub nodes
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public List<RuleNode> getRuleNodes() {
		return (List<RuleNode>) getChildren();
	}

	/**
	 * Evaluate the rules.
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
		evalResult = rules.eval(domainService, user, client, type);
		for (RuleNode ruleNode  : getRuleNodes()) {
			ruleNode.eval(domainService, user, client, type);
			if (ruleNode.getEvalResult() != null && !ruleNode.getEvalResult().evaluateNextRule()) {
				break;
			}
		}
	}

	/**
	 * Reset the evaluation.
	 */
	public void resetEval() {
		evalResult = null;
		for (RuleNode ruleNode  : getRuleNodes()) {
			ruleNode.resetEval();
		}
	}

	/**
	 * @return the evalResult
	 */
	public Set<Department> getEvalResult() {
		return evalResult;
	}

	/**
	 * @param evalResult the evalResult to set
	 */
	protected void setEvalResult(final Set<Department> evalResult) {
		this.evalResult = evalResult;
	}
	
}

