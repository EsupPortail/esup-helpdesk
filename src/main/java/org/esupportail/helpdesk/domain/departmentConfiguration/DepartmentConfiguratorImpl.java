/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentConfiguration;

import org.esupportail.helpdesk.domain.beans.Department;


/**
 * A basic implementation of DepartmentConfigurator.
 */
public class DepartmentConfiguratorImpl implements DepartmentConfigurator {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7595214729717021611L;

	/**
     * True if new departments are enabled.
     */
    private boolean enabled;

    /**
     * True if the managers of new departments must fill the time spent when closing a ticket.
     */
    private boolean spentTimeNeeded;

    /**
     * True if the service is confidential.
     */
    private boolean srvConfidential;
    
    /**
     * Bean constructor.
     */
    public DepartmentConfiguratorImpl() {
		super();
		this.enabled = false;
		this.spentTimeNeeded = false;
		this.srvConfidential = false;
	}
    
    /**
     * @see org.esupportail.helpdesk.domain.departmentConfiguration.DepartmentConfigurator#configure(
     * org.esupportail.helpdesk.domain.beans.Department)
     */
    @Override
	public void configure(final Department department) {
    	department.setEnabled(enabled);
    	department.setSpentTimeNeeded(spentTimeNeeded);
    	department.setSrvConfidential(srvConfidential);
    }

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param spentTimeNeeded the spentTimeNeeded to set
	 */
	public void setSpentTimeNeeded(final boolean spentTimeNeeded) {
		this.spentTimeNeeded = spentTimeNeeded;
	}

	public boolean isSrvConfidential() {
		return srvConfidential;
	}

	public void setSrvConfidential(boolean srvConfidential) {
		this.srvConfidential = srvConfidential;
	}

}
