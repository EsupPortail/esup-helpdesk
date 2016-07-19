/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentConfiguration;

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.Department;


/**
 * the interface of department configurators.
 */
public interface DepartmentConfigurator extends Serializable {
	
    /**
     * Configure a department (just after its creation).
     * @param department
     */
    void configure(final Department department);

}
