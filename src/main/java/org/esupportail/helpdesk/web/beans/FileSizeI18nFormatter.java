/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.HashMap;
import java.util.Locale;

import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.web.controllers.SessionController;
import org.springframework.beans.factory.InitializingBean;

/** 
 * A formatter for file sizes.
 */ 
public class FileSizeI18nFormatter 
extends HashMap<Integer, String> 
implements InitializingBean {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7827250627299863794L;

	/**
	 * Magic number.
	 */
	private static final int BYTES_PER_KBYTES = 1024;
	
	/**
	 * Magic number.
	 */
	private static final int KBYTES_PER_MBYTES = 1024;
	
	/**
	 * Magic number.
	 */
	private static final int BYTES_PER_MBYTES = BYTES_PER_KBYTES * KBYTES_PER_MBYTES;
	
	/**
	 * The i18n service.
	 */
	private I18nService i18nService;
	
	/**
	 * The session controller.
	 */
	private SessionController sessionController;
	
	/**
	 * Bean constructor.
	 */
	public FileSizeI18nFormatter() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		Assert.notNull(sessionController, 
				"property sessionController of class " + this.getClass().getName() 
				+ " can not be null");
		Assert.notNull(i18nService, 
				"property i18nService of class " + this.getClass().getName() 
				+ " can not be null");
	}

	/**
	 * @param service 
	 * @param value
	 * @param locale 
	 * @return a formatted elapsed time.
	 */
	public static String format(
			final I18nService service, 
			final Object value, 
			final Locale locale) {
		int size;
		if (value == null) {
			size = 0;
		} else {
			size = Integer.valueOf(value.toString());
		}
		if (size < BYTES_PER_KBYTES) {
			Integer bytes = new Integer(size);
			return service.getString("FILE_SIZE.BYTES", locale, bytes);
		}
		if (size < BYTES_PER_MBYTES) {
			Integer kbytes = new Integer(size / BYTES_PER_KBYTES);
			return service.getString("FILE_SIZE.KBYTES", locale, kbytes);
		}
		Integer mbytes = new Integer(size / BYTES_PER_MBYTES);
		return service.getString("FILE_SIZE.MBYTES", locale, mbytes);
	}

	/**
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
	public String get(final Object o) {
		return format(i18nService, o, sessionController.getLocale());
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
	 * @return the i18nService
	 */
	protected I18nService getI18nService() {
		return i18nService;
	}

	/**
	 * @param service the i18nService to set
	 */
	public void setI18nService(final I18nService service) {
		i18nService = service;
	}
	
}

