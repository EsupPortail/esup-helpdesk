/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * An abstract department selector that insures that all the departments returned are enabled.
 */
@SuppressWarnings("serial")
public abstract class AbstractDepartmentSelector implements DepartmentSelector {

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * Constructor.
	 */
	AbstractDepartmentSelector() {
		super();
	}
	
	/**
	 * Remove the disabled departments from a list.
	 * @param input 
	 * @return a list.
	 */
	protected List<Department> removeDisabledDepartments(
			final List<Department> input) {
		List<Department> output = new ArrayList<Department>();
		for (Department department : input) {
			if (department.isEnabled()) {
				output.add(department);
			}
		}
		return output;
	}
	
	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelector#getTicketCreationDepartments(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User, 
	 * java.net.InetAddress)
	 */
	@Override
	@RequestCache
	public final List<Department> getTicketCreationDepartments(
			final DomainService domainService, 
			final User user, 
			final InetAddress client) {
		if (logger.isDebugEnabled()) {
			logger.debug("getting the ticket creation departments for user [" 
					+ user.getId() + " from " + client + "...");
		}
		List<Department> result = removeDisabledDepartments(
				getTicketCreationDepartmentsInternal(domainService, user, client));
		if (logger.isDebugEnabled()) {
			logger.debug("ticket creation departments for user [" + user.getId() + " from " + client + ":");
			for (Department department : result) {
				logger.debug("- " + department.getLabel());
			}
		}
		return result;
	}
	
	/**
	 * return the list of the departments that a user will see on ticket creation.
	 * @param domainService
	 * @param user the user
	 * @param client the client
	 * @return a list of departments.
	 */
	protected abstract List<Department> getTicketCreationDepartmentsInternal(
			DomainService domainService, 
			User user, 
			InetAddress client);

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelector#getTicketViewDepartments(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User, 
	 * java.net.InetAddress)
	 */
	@Override
	@RequestCache
	public final List<Department> getTicketViewDepartments(
			final DomainService domainService, 
			final User user, 
			final InetAddress client) {
		if (logger.isDebugEnabled()) {
			logger.debug("getting the ticket view departments for user [" 
					+ user.getId() + " from " + client + "...");
		}
		List<Department> result = removeDisabledDepartments(
				getTicketViewDepartmentsInternal(domainService, user, client));
		if (logger.isDebugEnabled()) {
			logger.debug("ticket view departments for user [" + user.getId() + " from " + client + ":");
			for (Department department : result) {
				logger.debug("- " + department.getLabel());
			}
		}
		return result;
	}
	
	/**
	 * return the list of the departments that a user will see on the control panel.
	 * @param domainService
	 * @param user the user
	 * @param client the client
	 * @return a list of departments.
	 */
	protected abstract List<Department> getTicketViewDepartmentsInternal(
			DomainService domainService, 
			User user, 
			InetAddress client);

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelector#getFaqViewDepartments(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User, 
	 * java.net.InetAddress)
	 */
	@Override
	@RequestCache
	public final List<Department> getFaqViewDepartments(
			final DomainService domainService, 
			final User user, 
			final InetAddress client) {
		String userId = null;
		if (logger.isDebugEnabled()) {
			if (user != null) {
				userId = user.getId();
			}
			logger.debug("getting the faq view departments for user [" 
					+ userId + " from " + client + "...");
		}
		List<Department> result = removeDisabledDepartments(
				getFaqViewDepartmentsInternal(domainService, user, client));
		if (logger.isDebugEnabled()) {
			logger.debug("faq view departments for user [" + userId + "] from " + client + ":");
			for (Department department : result) {
				logger.debug("- " + department.getLabel());
			}
		}
		return result;
	}
	
	/**
	 * return the list of the departments that a user will see for the FAQs.
	 * @param domainService
	 * @param user the user
	 * @param client the client
	 * @return a list of departments.
	 */
	protected abstract List<Department> getFaqViewDepartmentsInternal(
			DomainService domainService, 
			User user, 
			InetAddress client);

}
