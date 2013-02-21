package org.esupportail.helpdesk.domain.reporting;


/**
 * The interface of FAQ reporters.
 */
public interface FaqReporter extends DomainServiceSettable {

	/**
	 * Send Faq reports.
	 */
	public void sendFaqReports();

}