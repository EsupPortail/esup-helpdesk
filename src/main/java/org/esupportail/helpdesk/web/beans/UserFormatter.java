/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;

import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.userFormatting.UserFormattingService;
import org.esupportail.helpdesk.web.controllers.SessionController;
import org.esupportail.helpdesk.web.controllers.TicketController;
import org.springframework.beans.factory.InitializingBean;

/** 
 * A session-scoped bean used to format users.
 */ 
public class UserFormatter 
extends HashMap<User, String>
implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4306607204224535675L;

	/**
	 * The user formatting service.
	 */
	private UserFormattingService userFormattingService;
	
	/**
	 * The session controller.
	 */
	private SessionController sessionController;

	/**
	 * Bean constructor.
	 */
	public UserFormatter() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(sessionController, 
				"property sessionController of class " + this.getClass().getName() 
				+ " can not be null");
		Assert.notNull(userFormattingService, 
				"property userFormattingService of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object o) {
		if (o == null) {
			return null;
		}
		if (!(o instanceof User)) {
			return null;
		}
		return userFormattingService.format((User) o, false, sessionController.getLocale(), sessionController.getCurrentUser());
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

	/**
	 * @return the userFormattingService
	 */
	protected UserFormattingService getUserFormattingService() {
		return userFormattingService;
	}

	/**
	 * @param userFormattingService the userFormattingService to set
	 */
	public void setUserFormattingService(final UserFormattingService userFormattingService) {
		this.userFormattingService = userFormattingService;
	}

}

