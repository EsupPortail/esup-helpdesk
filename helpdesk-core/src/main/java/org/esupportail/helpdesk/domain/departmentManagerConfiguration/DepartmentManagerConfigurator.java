/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentManagerConfiguration;

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.DepartmentManager;


/**
 * the interface of department managers configurators.
 */
public interface DepartmentManagerConfigurator extends Serializable {
	
    /**
     * Configure a department manager (just after its creation).
     * @param departmentManager
     */
    void configure(final DepartmentManager departmentManager);

    /**
     * Configure ticket monitoring for a department manager.
     * @param departmentManager
     */
    void configureTicketMonitoring(final DepartmentManager departmentManager);

}
