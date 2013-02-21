/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans.departmentSelection; 

import java.net.InetAddress;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedCondition;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions;
import org.esupportail.helpdesk.web.beans.AbstractFirstLastNode;

/** 
 * The node of user-defined conditions.
 */ 
public class UserDefinedConditionsNode extends AbstractFirstLastNode {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3296479150461585008L;

	/**
	 * Bean constructor.
	 * @param userDefinedConditions
	 */
	@SuppressWarnings("unchecked")
	public UserDefinedConditionsNode(
			final UserDefinedConditions userDefinedConditions) {
		super("userDefinedConditions", "", true);
		int index = 0;
		for (String name : userDefinedConditions.getConditionNames()) {
			UserDefinedCondition userDefinedCondition = null;
			try {
				userDefinedCondition = userDefinedConditions.getUserDefinedCondition(name);
			} catch (DepartmentSelectionCompileError e) {
				// should never happen
			}
			UserDefinedConditionNode userDefinedConditionNode = 
				new UserDefinedConditionNode(index, userDefinedCondition);
			getChildren().add(userDefinedConditionNode);
    		setLeaf(false);
    		index++;
    	}
		markFirstAndLastChildNodes();
	}
	
	/**
	 * @return the sub nodes
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public List<UserDefinedConditionNode> getUserDefinedConditionNodes() {
		return (List<UserDefinedConditionNode>) getChildren();
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
		for (UserDefinedConditionNode userDefinedConditionNode : getUserDefinedConditionNodes()) {
			userDefinedConditionNode.eval(domainService, user, client);
		}
	}
	
	/**
	 * Reset the evaluation.
	 */
	public void resetEval() {
		for (UserDefinedConditionNode userDefinedConditionNode : getUserDefinedConditionNodes()) {
			userDefinedConditionNode.resetEval();
		}
	}
	
}

