package org.esupportail.helpdesk.domain.reporting;

import org.esupportail.helpdesk.domain.DomainService;


/**
 * For beans that need a domain service.
 */
public interface DomainServiceSettable {

	/**
	 * Set the domain service, should be called before any other method.
	 * @param domainService the domainService to set.
	 */
	void setDomainService(DomainService domainService);

}