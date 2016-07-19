/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.web.controllers.SessionController;
import org.springframework.beans.factory.InitializingBean;

/** 
 * A provider for assignment algorithms i18n descriptions.
 */ 
public class AssignmentAlgorithmI18nDescriptionProvider 
extends HashMap<String, String> 
implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5669636822643694992L;

	/**
	 * The domain service.
	 */
	private DomainService domainService;
	
	/**
	 * The session controller.
	 */
	private SessionController sessionController;
	
	/**
	 * Bean constructor.
	 */
	public AssignmentAlgorithmI18nDescriptionProvider() {
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
		Assert.notNull(sessionController, 
				"property sessionController of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object algorithmName) {
		return domainService.getAssignmentAlgorithmDescription(
				(String) algorithmName, sessionController.getLocale());
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
	 * @return the sessionController
	 */
	protected SessionController getSessionController() {
		return sessionController;
	}

	/**
	 * @param sessionController the sessionController to set
	 */
	public void setSessionController(final SessionController sessionController) {
		this.sessionController = sessionController;
	}
	
}

