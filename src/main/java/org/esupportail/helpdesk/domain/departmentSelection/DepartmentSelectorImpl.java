/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.esupportail.commons.aop.monitor.Monitor;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.web.controllers.Resettable;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentSelectionConfig;
import org.esupportail.helpdesk.domain.beans.User;
import org.springframework.beans.factory.InitializingBean;

/**
 * This class is used to restrain the visibility of departments to users.
 */
@Monitor
public class DepartmentSelectorImpl
extends AbstractDepartmentSelector
implements Resettable, InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8418093316012816568L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The config reader.
	 */
	private DepartmentSelectionConfigReader configReader;

	/**
	 * The date when the config reader was loaded.
	 */
	private Timestamp configReaderDate;

	/**
	 * The cache manager.
	 */
	private CacheManager cacheManager;

	/**
	 * Constructor.
	 */
	DepartmentSelectorImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.cacheManager,
				"property cacheManager of class " + this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	@Override
	public void reset() {
		configReaderDate = null;
		configReader = null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.AbstractDepartmentSelector
	 * #getTicketCreationDepartmentsInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User,
	 * java.net.InetAddress)
	 */
	@Override
	public List<Department> getTicketCreationDepartmentsInternal(
			final DomainService domainService,
			final User user,
			final InetAddress client) {
		return getDepartmentsInternal(domainService, user, client, TICKET_CREATION_SELECTION);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.AbstractDepartmentSelector
	 * #getTicketViewDepartmentsInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User,
	 * java.net.InetAddress)
	 */
	@Override
	public List<Department> getTicketViewDepartmentsInternal(
			final DomainService domainService,
			final User user,
			final InetAddress client) {
		return getDepartmentsInternal(domainService, user, client, TICKET_VIEW_SELECTION);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.AbstractDepartmentSelector
	 * #getFaqViewDepartmentsInternal(
	 * org.esupportail.helpdesk.domain.DomainService, org.esupportail.helpdesk.domain.beans.User,
	 * java.net.InetAddress)
	 */
	@Override
	public List<Department> getFaqViewDepartmentsInternal(
			final DomainService domainService,
			final User user,
			final InetAddress client) {
		return getDepartmentsInternal(domainService, user, client, FAQ_VIEW_SELECTION);
	}

	/**
	 * @return the cache key.
	 */
	protected Cache getCache() {
		String cacheName = getClass().getName();
		if (!cacheManager.cacheExists(cacheName)) {
		  cacheManager.addCache(cacheName);
		}
		return cacheManager.getCache(cacheName);
	}

	/**
	 * @param user
	 * @param client
	 * @param type
	 * @return the cache key.
	 */
	protected String getCacheKey(
			final User user,
			final InetAddress client,
			final int type) {
		String userId = null;
		if (user != null) {
			userId = user.getId();
		}
		return userId + "-" + type + "-" + client;
	}

	/**
	 * @param domainService
	 * @param user
	 * @param client
	 * @param type
	 * @return a cached result.
	 */
	@SuppressWarnings("unchecked")
	protected List<Department> getCachedResult(
			final DomainService domainService,
			final User user,
			final InetAddress client,
			final int type) {
		Cache cache = getCache();
		String cacheKey = getCacheKey(user, client, type);
		Element element = cache.get(cacheKey);
		if (element == null) {
			return null;
		}
		long cacheTime = element.getCreationTime();
		long domainTime = 0;
		if (domainService.getDepartmentSelectionContextTime() != null) {
			domainTime = domainService.getDepartmentSelectionContextTime().getTime();
		}
		long userTime = 0;
		if ((user != null) && (user.getDepartmentSelectionContextTime() != null)) {
			userTime = user.getDepartmentSelectionContextTime().getTime();
		}
		if (cacheTime < domainTime || cacheTime < userTime) {
			cache.remove(cacheKey);
			return null;
		}
		return (List<Department>) element.getValue();
	}

	/**
	 * Cache a result.
	 * @param user
	 * @param client
	 * @param type
	 * @param departments
	 */
	protected void cacheResult(
			final User user,
			final InetAddress client,
			final int type,
			final List<Department> departments) {
		String cacheKey = getCacheKey(user, client, type);
		getCache().put(new Element(cacheKey, departments));
	}

	/**
	 * Return the list of the departments that a user will see.
	 * @param domainService
	 * @param user the user
	 * @param client the client
	 * @param type
	 * @return a list of departments.
	 */
	protected List<Department> getDepartmentsInternal(
			final DomainService domainService,
			final User user,
			final InetAddress client,
			final int type) {
		reloadConfigIfNeeded(domainService);
		List<Department> result = getCachedResult(domainService, user, client, type);
		if (result != null) {
			return result;
		}
		Set<Department> departments = configReader.getRules().eval(domainService, user, client, type);
		if (departments.isEmpty()) {
			departments = configReader.getWhenEmptyActions().eval(domainService, type).getDepartments();
		}
		result = new ArrayList<Department>(departments);
		Collections.sort(result);
		cacheResult(user, client, type, result);
		return result;
	}

	/**
	 * Clear the cache.
	 */
	protected void clearCache() {
		Cache cache = getCache();
		if (cache != null) {
			cache.removeAll();
		}
	}

	/**
	 * Reload the config if needed.
	 * @param domainService
	 */
	protected synchronized void reloadConfigIfNeeded(
			final DomainService domainService) {
		DepartmentSelectionConfig config = domainService.getDepartmentSelectionConfig();
		if (configReaderDate == null || configReaderDate.before(config.getDate())) {
			if (configReaderDate == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Loading the configuration of the department selection...");
				}
			} else {
				logger.info("The configuration of the department selection has changed, reloading...");
			}
			configReader = new DepartmentSelectionConfigReaderImpl(config.getData());
			configReaderDate = new Timestamp(System.currentTimeMillis());
			clearCache();
		}
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelector#exportConfig()
	 */
	@Override
	public String exportConfig() {
		return configReader.export();
	}

	/**
	 * @return the cacheManager
	 */
	protected CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

}
