package org.esupportail.helpdesk.domain.reporting;

import org.esupportail.helpdesk.domain.beans.DepartmentManager;

/**
 * The interface of ticket reporters.
 */
public interface TicketReporter extends DomainServiceSettable {

	/**
	 * Send a ticket report to a manager.
	 * @param manager
	 */
	public void sendTicketReport(final DepartmentManager manager);

	/**
	 * Send ticket reports for the current hour.
	 */
	public void sendTicketReports();

}