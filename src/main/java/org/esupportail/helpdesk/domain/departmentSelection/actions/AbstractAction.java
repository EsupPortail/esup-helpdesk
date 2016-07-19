/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.actions;

import java.util.List;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelector;
import org.esupportail.helpdesk.domain.departmentSelection.Result;
import org.springframework.util.StringUtils;

/**
 * an abstract Action implementation that logs the departments added..
 */
@SuppressWarnings("serial")
public abstract class AbstractAction implements Action {

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	/**
	 * True to evaluate the action when looking for departments visible on ticket creation.
	 */
	private boolean forTicketCreation;

	/**
	 * True to evaluate the action when looking for departments visible on ticket view.
	 */
	private boolean forTicketView;

	/**
	 * True to evaluate the action when looking for departments visible for FAQs.
	 */
	private boolean forFaqView;

	/**
	 * Constructor.
	 */
	protected AbstractAction() {
		super();
		forTicketCreation = true;
		forTicketView = true;
		forFaqView = true;
	}

	/**
	 * Evaluate the action.
	 * @param domainService
	 * @param result
	 * @return the departments that were added to result
	 */
	protected abstract List<Department> evalInternal(
			final DomainService domainService, 
			final Result result);

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#eval(
	 * org.esupportail.helpdesk.domain.DomainService, 
	 * org.esupportail.helpdesk.domain.departmentSelection.Result)
	 */
	@Override
	public final void eval(
			final DomainService domainService, 
			final Result result) {
		if (logger.isDebugEnabled()) {
			logger.debug("evaluating action " + this + "...");
		}
		List<Department> departments = evalInternal(domainService, result);
		if (departments != null) {
			result.addDepartments(departments);
		}
		if (logger.isDebugEnabled()) {
			String str = "departments for action " + this + " are: ";
			if (departments != null) {
				for (Department department : departments) {
					str += " " + department.getLabel();
				}
			}
			logger.debug(str);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#evalForType(int)
	 */
	@Override
	public boolean evalForType(final int type) {
		if (type == DepartmentSelector.TICKET_CREATION_SELECTION) {
			return forTicketCreation;
		}
		if (type == DepartmentSelector.TICKET_VIEW_SELECTION) {
			return forTicketView;
		}
		if (type == DepartmentSelector.FAQ_VIEW_SELECTION) {
			return forFaqView;
		}
		if (type == DepartmentSelector.SEARCH_SELECTION) {
			return forFaqView || forTicketView;
		}
		throw new UnsupportedOperationException("unsupported selection type [" + type + "]");
	}

	/**
	 * A setter for the 'for' attribute.
	 * @param value 
	 * @throws DepartmentSelectionCompileError 
	 */
	public void setFor(final String value) throws DepartmentSelectionCompileError {
		if (!StringUtils.hasText(value)) {
			return;
		}
		forTicketCreation = false;
		forTicketView = false;
		forFaqView = false;
		String [] values = value.split(",");
		for (String string : values) {
			if ("all".equals(string)) {
				forTicketCreation = true;
				forTicketView = true;
				forFaqView = true;
			} else if ("ticketCreation".equals(string)) {
				forTicketCreation = true;
			} else if ("ticketView".equals(string)) {
				forTicketView = true;
			} else if ("faqView".equals(string)) {
				forFaqView = true;
			} else if ("search".equals(string)) {
				forTicketView = true;
				forFaqView = true;
			} else {
				throw new DepartmentSelectionCompileError(
						"[" + value + "] is not valid for attribute [for]");
			}
		}
	}

	/**
	 * @return a textual representation of the for attribute.
	 */
	protected String forToString() {
		if (forTicketCreation && forTicketView && forFaqView) {
			return "";
		}
		String separator = "";
		String result = " for=\"";
		if (forTicketCreation) {
			result += separator + "ticketCreation";
			separator = ",";
		}
		if (forTicketView) {
			result += separator + "ticketView";
			separator = ",";
		}
		if (forFaqView) {
			result += separator + "faqView";
			separator = ",";
		}
		result += "\"";
		return result;
	}

}
