/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans.departmentSelection; 

import java.net.InetAddress;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedCondition;
import org.esupportail.helpdesk.web.beans.AbstractFirstLastNode;

/** 
 * The node of a user-defined condition.
 */ 
public class UserDefinedConditionNode extends AbstractFirstLastNode {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3681462627888021471L;
	
	/**
	 * The user-defined condition.
	 */
	private UserDefinedCondition userDefinedCondition;
	
	/**
	 * The node index.
	 */
	private int index;

	/**
	 * Bean constructor.
	 * @param index 
	 * @param userDefinedCondition
	 */
	@SuppressWarnings("unchecked")
	public UserDefinedConditionNode(
			final int index,
			final UserDefinedCondition userDefinedCondition) {
		super("userDefinedCondition", userDefinedCondition.getName(), true);
		this.userDefinedCondition = userDefinedCondition;
		this.index = index;
		ConditionNode conditionNode = new ConditionNode(userDefinedCondition.getCondition());
		getChildren().add(conditionNode);
		markFirstAndLastChildNodes();
   		setLeaf(false);
	}
	
	/**
	 * @return the condition node
	 */
	public ConditionNode getConditionNode() {
		return (ConditionNode) getChildren().get(0);
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
		getConditionNode().eval(domainService, user, client);
	}
	
	/**
	 * Reset the evaluation.
	 */
	public void resetEval() {
		getConditionNode().resetEval();
	}
	
	/**
	 * @return the evalResult
	 */
	public Boolean getEvalResult() {
		return getConditionNode().getEvalResult();
	}

	/**
	 * @return the userDefinedCondition
	 */
	public UserDefinedCondition getUserDefinedCondition() {
		return userDefinedCondition;
	}

	/**
	 * @param userDefinedCondition the userDefinedCondition to set
	 */
	protected void setUserDefinedCondition(final UserDefinedCondition userDefinedCondition) {
		this.userDefinedCondition = userDefinedCondition;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

}

