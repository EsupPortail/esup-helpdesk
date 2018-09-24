/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.User;


/**
 * The rules container.
 */
public class Rules {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -3905947268071280203L;
	
	/**
	 * The rules themselves.
	 */
	private List<Rule> rules;

	/**
	 * Constructor.
	 */
	public Rules() {
		super();
		rules = new ArrayList<Rule>();
	}
	
	/**
	 * Add a rule.
	 * @param userDefinedConditions 
	 * @param rule
	 * @throws DepartmentSelectionCompileError 
	 */
	public void compileAndAdd(
			final UserDefinedConditions userDefinedConditions,
			final Rule rule) 
	throws DepartmentSelectionCompileError {
		rule.compile(userDefinedConditions);
		rules.add(rule);
	}

	/**
	 * Evaluate the rules.
	 * @param domainService
	 * @param user the user
	 * @param client the client
	 * @param type 
	 * @return a set of departments.
	 */
	public Set<Department> eval(
			final DomainService domainService, 
			final User user, 
			final InetAddress client,
			final int type) {
		Result result = new Result();
		for (Rule rule : rules) {
			rule.eval(domainService, user, client, result, type);
			if (!result.evaluateNextRule()) {
				break;
			}
		}
		return result.getDepartments();
	}

	/**
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		String str = "<rules>";
		for (Rule rule : rules) {
			str += rule;
		}
		str += "</rules>";
		return str;
	}

	/**
	 * @return the rules
	 */
	public List<Rule> getRules() {
		return rules;
	}

}
