/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.actions;

import java.io.Serializable;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.Result;

/**
 * The interface of actions.
 */
public interface Action extends Serializable {
	
	/**
	 * add departments to an existing set.
	 * @param domainService 
	 * @param result the result
	 */
	void eval(
			DomainService domainService,
			Result result,
			boolean evaluateCondition);

	/**
	 * @param type
	 * @return true if the action has to be evaluated for the given selection type.
	 */
	boolean evalForType(int type);
	
	/**
	 * Compile the action (check that it has been properly defined).
	 * @throws DepartmentSelectionCompileError 
	 */
	void compile() throws DepartmentSelectionCompileError;
	
	/**
	 * @return the node type.
	 */
	String getNodeType();

}
