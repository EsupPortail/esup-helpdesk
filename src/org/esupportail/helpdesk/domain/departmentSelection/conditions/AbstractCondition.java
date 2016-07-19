/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.conditions;

import java.net.InetAddress;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions;

/**
 * This abstract condition logs the evaluation.
 */
@SuppressWarnings("serial")
public abstract class AbstractCondition implements Condition {

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * Constructor.
	 */
	protected AbstractCondition() {
		super();
	}

	/**
	 * Evaluate the condition.
	 * @param domainService 
	 * @param user 
	 * @param client 
	 * @return true if matched.
	 */
	protected abstract boolean isMatchedInternal(
			final DomainService domainService, 
			final User user,
			final InetAddress client);

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#isMatched(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User, 
	 * java.net.InetAddress)
	 */
	@Override
	public final boolean isMatched(
			final DomainService domainService, 
			final User user,
			final InetAddress client) {
		if (logger.isDebugEnabled()) {
			logger.debug("evaluating condition " + this + "...");
		}
		boolean result = isMatchedInternal(domainService, user, client);
		if (logger.isDebugEnabled()) {
			logger.debug("condition " + this + " returns " + result);
		}
		return result;
	}

	/**
	 * Check the condition (internal).
	 * @throws DepartmentSelectionCompileError 
	 */
	protected abstract void checkInternal() throws DepartmentSelectionCompileError;

	/**
	 * Compile the condition (internal).
	 * @param userDefinedConditions 
	 * @throws DepartmentSelectionCompileError 
	 */
	protected abstract void compileInternal(
			UserDefinedConditions userDefinedConditions) throws DepartmentSelectionCompileError;

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.conditions.Condition#compile(
	 * org.esupportail.helpdesk.domain.departmentSelection.UserDefinedConditions)
	 */
	@Override
	public void compile(final UserDefinedConditions userDefinedConditions) throws DepartmentSelectionCompileError {
		if (logger.isDebugEnabled()) {
			logger.debug("checking condition " + this + "...");
		}
		checkInternal();
		if (logger.isDebugEnabled()) {
			logger.debug("compiling condition " + this + "...");
		}
		compileInternal(userDefinedConditions);
	}

}
