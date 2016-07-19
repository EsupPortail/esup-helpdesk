/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import org.esupportail.helpdesk.domain.beans.Icon;

/** 
 * An bean to give the urls of the icons of the departments.
 */ 
public class DepartmentIconUrlProvider  extends AbstractTicketContainerIconUrlProvider {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7695299373212509709L;
	
	/**
	 * Bean constructor.
	 */
	protected DepartmentIconUrlProvider() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.beans.AbstractTicketContainerIconUrlProvider#getDefaultIcon()
	 */
	@Override
	protected Icon getDefaultIcon() {
		return getDomainService().getDefaultDepartmentIcon();
	}

}

