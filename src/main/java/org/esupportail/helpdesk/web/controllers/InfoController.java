/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.HttpUtils;
import org.esupportail.helpdesk.domain.DomainService;
import org.springframework.beans.factory.InitializingBean;


/**
 * A bean to manage files.
 */
public class InfoController implements InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -753817893852402682L;
	
	/**
	 * The domain service.
	 */
	private DomainService domainService;

	/**
	 * The application service.
	 */
	private ApplicationService applicationService;

	/**
	 * Bean constructor.
	 */
	public InfoController() {
		super();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		Assert.notNull(applicationService, "property applicationService of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(domainService, "property domainService of class " 
				+ this.getClass().getName() + " can not be null");
	}
	
	/**
	 * JSF callback.
	 * @return the application version.
	 */
	public String getApplicationVersion() {
		return applicationService.getVersion().toString();
	}

	/**
	 * JSF callback.
	 * @return the application name.
	 */
	public String getApplicationName() {
		return applicationService.getName();
	}

	/**
	 * JSF callback.
	 * @return true for a quick-start installation.
	 */
	public boolean getApplicationQuickStart() {
		return applicationService.isQuickStart();
	}

	/**
	 * JSF callback.
	 * @return the deploy type.
	 */
	public String getApplicationDeployType() {
		return applicationService.getDeployType();
	}

	/**
	 * JSF callback.
	 * @return the portal info.
	 */
	public String getApplicationPortalInfo() {
		if (ContextUtils.isPortlet()) {
			return HttpUtils.getPortalInfo();
		}
		return null;
	}

	/**
	 * JSF callback.
	 * @return the server info.
	 */
	public String getApplicationServerInfo() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext == null) {
			return "facesContext is null";
		}
		ExternalContext externalContext = facesContext.getExternalContext();
		if (externalContext == null) {
			return "externalContext is null";
		}
		Object context = externalContext.getContext();
		if (context == null) {
			return "context is null";
		}
		if (!(context instanceof ServletContext)) {
			return "context is " + context.getClass();
		}
		ServletContext servletContext = (ServletContext) context;
		return servletContext.getServerInfo();
	}

	/**
	 * JSF callback.
	 * @return the database driver.
	 */
	public String getDatabaseDriver() {
		return applicationService.getDatabaseDriver();
	}

	/**
	 * JSF callback.
	 * @return the database dialect.
	 */
	public String getDatabaseDialect() {
		return applicationService.getDatabaseDialect();
	}

	/**
	 * JSF callback.
	 * @return true when using JNDI.
	 */
	public boolean getDatabaseUseJndi() {
		return applicationService.isDatabaseUseJndi();
	}

	/**
	 * JSF callback.
	 * @return the number of real departments.
	 */
	public int getRealDepartmentsNumber() {
		return domainService.getRealDepartmentsNumber();
	}

	/**
	 * JSF callback.
	 * @return the number of virtual departments.
	 */
	public int getVirtualDepartmentsNumber() {
		return domainService.getVirtualDepartmentsNumber();
	}

	/**
	 * JSF callback.
	 * @return the total number of departments.
	 */
	public int getTotalDepartmentsNumber() {
		return domainService.getDepartmentsNumber();
	}

	/**
	 * JSF callback.
	 * @return the number of real categories.
	 */
	public int getRealCategoriesNumber() {
		return domainService.getRealCategoriesNumber();
	}

	/**
	 * JSF callback.
	 * @return the number of virtual categories.
	 */
	public int getVirtualCategoriesNumber() {
		return domainService.getVirtualCategoriesNumber();
	}

	/**
	 * JSF callback.
	 * @return the total number of categories.
	 */
	public int getTotalCategoriesNumber() {
		return domainService.getCategoriesNumber();
	}

	/**
	 * JSF callback.
	 * @return the number of application users.
	 */
	public int getApplicationUsersNumber() {
		return domainService.getApplicationUsersNumber();
	}

	/**
	 * JSF callback.
	 * @return the number of CAS users.
	 */
	public int getCasUsersNumber() {
		return domainService.getCasUsersNumber();
	}

	/**
	 * JSF callback.
	 * @return the number of Shibboleth users.
	 */
	public int getShibbolethUsersNumber() {
		return domainService.getShibbolethUsersNumber();
	}

	/**
	 * JSF callback.
	 * @return the total number of users.
	 */
	public int getTotalUsersNumber() {
		return domainService.getUsersNumber();
	}

	/**
	 * JSF callback.
	 * @return the number of managers.
	 */
	public int getManagerUsersNumber() {
		return domainService.getManagerUsersNumber();
	}

	/**
	 * JSF callback.
	 * @return the number of active tickets.
	 */
	public int getActiveTicketsNumber() {
		return domainService.getTicketsNumber();
	}

	/**
	 * JSF callback.
	 * @return the number of archived tickets.
	 */
	public int getArchivedTicketsNumber() {
		return domainService.getArchivedTicketsNumber();
	}

	/**
	 * JSF callback.
	 * @return the total number of tickets.
	 */
	public int getTotalTicketsNumber() {
		return getActiveTicketsNumber() + getArchivedTicketsNumber();
	}

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
	 * @return the applicationService
	 */
	protected ApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(final ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

}
