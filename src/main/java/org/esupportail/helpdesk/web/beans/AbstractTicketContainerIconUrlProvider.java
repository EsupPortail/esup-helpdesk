/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Icon;
import org.esupportail.helpdesk.domain.beans.TicketContainer;
import org.springframework.beans.factory.InitializingBean;

/** 
 * An abstract bean to give the urls of the icons of the ticket containers.
 */ 
@SuppressWarnings("serial")
public abstract class AbstractTicketContainerIconUrlProvider 
extends HashMap<Department, String>  implements InitializingBean {
	
	/**
	 * The domain service.
	 */
	private DomainService domainService;
	
	/**
	 * The icon url provider.
	 */
	private IconUrlProvider iconUrlProvider;

	/**
	 * Bean constructor.
	 */
	protected AbstractTicketContainerIconUrlProvider() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(domainService, 
				"property domainService of class " + this.getClass().getName() 
				+ " can not be null");
		Assert.notNull(iconUrlProvider, 
				"property iconUrlProvider of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object o) {
		TicketContainer container = (TicketContainer) o;
		Icon icon;
		if (container == null || container.getEffectiveIcon() == null) {
			icon = getDefaultIcon();
		} else {
			icon = container.getEffectiveIcon();
		}
		return iconUrlProvider.get(icon);
	}

	/**
	 * @return the default icon to use.
	 */
	protected abstract Icon getDefaultIcon();

	/**
	 * @return the domainService
	 */
	protected DomainService getDomainService() {
		return domainService;
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @return the iconUrlProvider
	 */
	protected IconUrlProvider getIconUrlProvider() {
		return iconUrlProvider;
	}

	/**
	 * @param iconUrlProvider the iconUrlProvider to set
	 */
	public void setIconUrlProvider(final IconUrlProvider iconUrlProvider) {
		this.iconUrlProvider = iconUrlProvider;
	}

}

