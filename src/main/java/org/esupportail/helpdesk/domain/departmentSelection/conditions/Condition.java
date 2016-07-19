/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions;

/**
 * An interface that should be implemented by real conditions.
 */
public interface Condition extends Serializable {

	/**
	 * Tell if the condition is match.
	 * @param domainService
	 * @param user the user
	 * @param client the client
	 * @return a boolean.
	 */
	boolean isMatched(
			DomainService domainService,
			User user,
			InetAddress client);
	
	/**
	 * Compile the condition, i.e. find real conditions of named conditions.
	 * @param userDefinedConditions 
	 * @throws DepartmentSelectionCompileError 
	 */
	void compile(UserDefinedConditions userDefinedConditions) throws DepartmentSelectionCompileError;
	
	/**
	 * @return the sub-conditions.
	 */
	List<Condition> getSubConditions();

	/**
	 * @return the node type.
	 */
	String getNodeType();

	/**
	 * Refactor the sub named conditions.
	 * @param oldName
	 * @param newName
	 */
	void refactorNamedConditions(String oldName, String newName);

}