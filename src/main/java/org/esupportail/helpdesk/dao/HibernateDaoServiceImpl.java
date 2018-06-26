/**
* ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.aop.monitor.Monitor;
import org.esupportail.commons.dao.AbstractJdbcJndiHibernateDaoService;
import org.esupportail.commons.dao.HibernateSequenceUpdater;
import org.esupportail.commons.dao.HqlUtils;
import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.UninitializedDatabaseException;
import org.esupportail.commons.services.application.VersionningUtils;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.helpdesk.domain.ActionType;
import org.esupportail.helpdesk.domain.FaqScope;
import org.esupportail.helpdesk.domain.TicketScope;
import org.esupportail.helpdesk.domain.TicketStatus;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Alert;
import org.esupportail.helpdesk.domain.beans.ArchivedAction;
import org.esupportail.helpdesk.domain.beans.ArchivedFileInfo;
import org.esupportail.helpdesk.domain.beans.ArchivedInvitation;
import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Bookmark;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.CategoryMember;
import org.esupportail.helpdesk.domain.beans.Config;
import org.esupportail.helpdesk.domain.beans.DeletedItem;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentInvitation;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.DepartmentSelectionConfig;
import org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer;
import org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.FaqEvent;
import org.esupportail.helpdesk.domain.beans.FaqLink;
import org.esupportail.helpdesk.domain.beans.FileInfo;
import org.esupportail.helpdesk.domain.beans.HistoryItem;
import org.esupportail.helpdesk.domain.beans.Icon;
import org.esupportail.helpdesk.domain.beans.Invitation;
import org.esupportail.helpdesk.domain.beans.OldFaqEntry;
import org.esupportail.helpdesk.domain.beans.OldFaqPart;
import org.esupportail.helpdesk.domain.beans.OldFileInfo;
import org.esupportail.helpdesk.domain.beans.OldTicketTemplate;
import org.esupportail.helpdesk.domain.beans.Response;
import org.esupportail.helpdesk.domain.beans.State;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.TicketMonitoring;
import org.esupportail.helpdesk.domain.beans.TicketView;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.beans.VersionManager;
import org.esupportail.helpdesk.domain.beans.statistics.DayOfWeekTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.DayOfWeekTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.DayTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.DayTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.HourOfDayTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.HourOfDayTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.HourOfWeekTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.HourOfWeekTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.MonthTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.MonthTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.SpentTimeStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.StatusStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.YearTicketCreationStatistic;
import org.esupportail.helpdesk.domain.beans.statistics.YearTimeStatistic;
import org.esupportail.helpdesk.domain.userManagement.ApplicationUserManager;
import org.esupportail.helpdesk.domain.userManagement.CasUserManager;
import org.esupportail.helpdesk.domain.userManagement.ShibbolethUserManager;
import org.esupportail.helpdesk.exceptions.CategoryNotFoundException;
import org.esupportail.helpdesk.exceptions.TicketNotFoundException;
import org.esupportail.helpdesk.services.statistics.StatisticsExtractor;
import org.esupportail.helpdesk.services.statistics.StatisticsUtils;
import org.esupportail.helpdesk.web.beans.StatisticsTicketEntry;
import org.esupportail.helpdesk.web.beans.UserTicketCreationStatisticEntry;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

/**
 * The Hiberate implementation of the DAO service.
 *
 * See /properties/dao/dao-example.xml
 */
@Monitor
public class HibernateDaoServiceImpl
extends AbstractJdbcJndiHibernateDaoService
implements DaoService {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6081277905929191788L;

	/**
	 * The name of the 'id' attribute.
	 */
	private static final String ID_ATTRIBUTE = "id";

	/**
	 * The name of the 'order' attribute.
	 */
	private static final String ORDER_ATTRIBUTE = "order";

	/**
	 * The name of the 'user' attribute.
	 */
	private static final String USER_ATTRIBUTE = "user";

	/**
	 * The name of the 'department' attribute.
	 */
	private static final String DEPARTMENT_ATTRIBUTE = "department";

	/**
	 * The name of the 'label' attribute.
	 */
	private static final String LABEL_ATTRIBUTE = "label";

	/**
	 * The name of the 'parent' attribute.
	 */
	private static final String PARENT_ATTRIBUTE = "parent";

	/**
	 * The name of the 'oldFaqPart' attribute.
	 */
	private static final String OLD_FAQ_PART_ATTRIBUTE = "oldFaqPart";

	/**
	 * The name of the 'category' attribute.
	 */
	private static final String CATEGORY_ATTRIBUTE = "category";

	/**
	 * The name of the 'category' attribute.
	 */
	private static final String CREATION_DATE_ATTRIBUTE = "creationDate";

	
	/**
	 * The name of the 'realCategory' attribute.
	 */
	private static final String REAL_CATEGORY_ATTRIBUTE = "realCategory";

	/**
	 * The name of the 'ticket' attribute.
	 */
	private static final String TICKET_ATTRIBUTE = "ticket";

	/**
	 * The name of the 'archivedTicket' attribute.
	 */
	private static final String ARCHIVED_TICKET_ATTRIBUTE = "archivedTicket";

	/**
	 * The name of the 'actionType' attribute.
	 */
	private static final String ACTION_TYPE_ATTRIBUTE = "actionType";


	/**
	 * The name of the 'action' attribute.
	 */
	private static final String ACTION_ATTRIBUTE = "action";

	/**
	 * A database type.
	 */
	private static final String DATABASE_TYPE_MYSQL = "mysql";

	/**
	 * A database type.
	 */
	private static final String DATABASE_TYPE_POSTGRES = "postgres";

	/**
	* A logger.
	*/
	private Logger logger = new LoggerImpl(getClass());

	/**
	 * The file manager.
	 */
	private FileManager fileManager;

	/**
	 * The database type.
	 */
	private String databaseType;

	/**
	 * Bean constructor.
	 */
	public HibernateDaoServiceImpl() {
		super();
	}

	/**
	 * @see org.esupportail.commons.dao.AbstractGenericHibernateDaoService#initDao()
	 */
	@Override
	public void initDao() {
		Assert.notNull(this.fileManager,
				"property fileManager of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.databaseType,
				"property databaseType of class " + this.getClass().getName()
				+ " can not be null (set property jdbcUrl)");
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________USER() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getUser(java.lang.String)
	 */
	@Override
	public User getUser(final String id) {
		return (User) getHibernateTemplate().get(User.class, id);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getUsers()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<User> getUsers() {
		return getHibernateTemplate().loadAll(User.class);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getUsersNumber()
	 */
	@Override
	@RequestCache
	public int getUsersNumber() {
		String queryStr = HqlUtils.selectCountAllFrom(
				User.class.getSimpleName());
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCasUsersNumber()
	 */
	@Override
	@RequestCache
	public int getCasUsersNumber() {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				User.class.getSimpleName(),
				HqlUtils.equals("authType", HqlUtils.quote(AuthUtils.CAS)));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getShibbolethUsersNumber()
	 */
	@Override
	@RequestCache
	public int getShibbolethUsersNumber() {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				User.class.getSimpleName(),
				HqlUtils.equals("authType", HqlUtils.quote(AuthUtils.SHIBBOLETH)));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getApplicationUsersNumber()
	 */
	@Override
	@RequestCache
	public int getApplicationUsersNumber() {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				User.class.getSimpleName(),
				HqlUtils.equals("authType", HqlUtils.quote(AuthUtils.APPLICATION)));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addUser(org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void addUser(final User user) {
		addObject(user);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteUser(org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void deleteUser(final User user) {
		deleteObject(user);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateUser(org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void updateUser(final User user) {
		updateObject(user);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getUserWithAuthSecret(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public User getUserWithAuthSecret(final String authSecret) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("authSecret", authSecret));
		List<User> users = getHibernateTemplate().findByCriteria(criteria);
		if (users.size() == 0) {
			return null;
		}
		return users.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getAdmins()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<User> getAdmins() {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("admin", Boolean.TRUE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________DEPARTMENT() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartment(long)
	 */
	@Override
	@RequestCache
	public Department getDepartment(final long id) {
		return (Department) this.getHibernateTemplate().get(Department.class, id);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartments()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Department> getDepartments() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Department.class);
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return  getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getEnabledDepartments()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Department> getEnabledDepartments() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Department.class);
		criteria.add(Restrictions.eq("enabled", Boolean.TRUE));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return  getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentsNumber()
	 */
	@Override
	@RequestCache
	public int getDepartmentsNumber() {
		String queryStr = HqlUtils.selectCountAllFrom(Department.class.getSimpleName());
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getRealDepartmentsNumber()
	 */
	@Override
	@RequestCache
	public int getRealDepartmentsNumber() {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Department.class.getSimpleName(),
				HqlUtils.isNull("realDepartment"));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getVirtualDepartmentsNumber()
	 */
	@Override
	@RequestCache
	public int getVirtualDepartmentsNumber() {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Department.class.getSimpleName(),
				HqlUtils.isNotNull("realDepartment"));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addDepartment(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public void addDepartment(final Department department) {
		addObject(department);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateDepartment(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public void updateDepartment(final Department department) {
		updateObject(department);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteDepartment(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	public void deleteDepartment(final Department department) {
		executeUpdate(HqlUtils.deleteWhere(
				DepartmentManager.class.getSimpleName() + " dm",
				HqlUtils.equals("dm.department.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				Ticket.class.getSimpleName() + " t",
				"t.creationDepartment = NULL",
				HqlUtils.equals("t.creationDepartment.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				Action.class.getSimpleName() + " a",
				"a.departmentBefore = NULL",
				HqlUtils.equals("a.departmentBefore.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				Action.class.getSimpleName() + " a",
				"a.departmentAfter = NULL",
				HqlUtils.equals("a.departmentAfter.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedTicket.class.getSimpleName() + " t",
				"t.creationDepartment = NULL",
				HqlUtils.equals("t.creationDepartment.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedAction.class.getSimpleName() + " a",
				"a.departmentBefore = NULL",
				HqlUtils.equals("a.departmentBefore.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedAction.class.getSimpleName() + " a",
				"a.departmentAfter = NULL",
				HqlUtils.equals("a.departmentAfter.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				User.class.getSimpleName() + " u",
				"u.controlPanelManagerDepartmentFilter = NULL, u.controlPanelCategoryFilter = NULL",
				HqlUtils.equals("u.controlPanelManagerDepartmentFilter.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				User.class.getSimpleName() + " u",
				"u.controlPanelUserDepartmentFilter = NULL",
				HqlUtils.equals("u.controlPanelUserDepartmentFilter.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				User.class.getSimpleName() + " u",
				"u.searchDepartmentFilter = NULL",
				HqlUtils.equals("u.searchDepartmentFilter.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				User.class.getSimpleName() + " u",
				"u.journalDepartmentFilter = NULL",
				HqlUtils.equals("u.journalDepartmentFilter.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedTicket.class.getSimpleName() + " at",
				"at.creationDepartment = NULL",
				HqlUtils.equals("at.creationDepartment.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedAction.class.getSimpleName() + " aa",
				"aa.departmentBefore = NULL",
				HqlUtils.equals("aa.departmentBefore.id", department.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedAction.class.getSimpleName() + " aa",
				"aa.departmentAfter = NULL",
				HqlUtils.equals("aa.departmentAfter.id", department.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				DepartmentInvitation.class.getSimpleName() + " di",
				HqlUtils.equals("di.department.id", department.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				FaqLink.class.getSimpleName() + " fl",
				HqlUtils.equals("fl.department.id", department.getId())));
		deleteObject(department);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#isDepartmentLabelUsed(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public boolean isDepartmentLabelUsed(final String label) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Department.class);
		criteria.add(Restrictions.eq(LABEL_ATTRIBUTE, label));
		List<Department> result = getHibernateTemplate().findByCriteria(criteria);
		return result.size() > 0;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentByOrder(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Department getDepartmentByOrder(final int i) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Department.class);
		criteria.add(Restrictions.eq(ORDER_ATTRIBUTE, new Integer(i)));
		List<Department> departments = getHibernateTemplate().findByCriteria(criteria);
		if (departments.isEmpty()) {
			return null;
		}
		return departments.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getVirtualDepartments(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Department> getVirtualDepartments(final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Department.class);
		criteria.add(Restrictions.eq("realDepartment", department));
		criteria.addOrder(Order.asc("label"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getVirtualDepartmentsNumber(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public int getVirtualDepartmentsNumber(final Department department) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Department.class.getSimpleName(),
				HqlUtils.equals("realDepartment.id", department.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentsByFilter(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Department> getDepartmentsByFilter(
			final String filter) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Department.class);
		if (StringUtils.hasText(filter)) {
			criteria.add(Restrictions.eq("filter", filter));
		} else {
			criteria.add(Restrictions.isNull("filter"));
		}
		criteria.add(Restrictions.eq("enabled", Boolean.TRUE));
		criteria.addOrder(Order.asc("order"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentByLabel(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public Department getDepartmentByLabel(
			final String label) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Department.class);
		criteria.add(Restrictions.eq("label", label));
		criteria.add(Restrictions.eq("enabled", Boolean.TRUE));
		List<Department> departments = getHibernateTemplate().findByCriteria(criteria);
		if (departments.size() == 0) {
			return null;
		}
		return departments.get(0);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________DEPARTMENT_MANAGER() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentManager(
	 * org.esupportail.helpdesk.domain.beans.Department, org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public DepartmentManager getDepartmentManager(
			final Department department,
			final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentManager.class);
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		@SuppressWarnings("unchecked")
		List<DepartmentManager> managers = getHibernateTemplate().findByCriteria(criteria);
		if (managers.isEmpty()) {
			return null;
		}
		return managers.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#isFaqDepartmentManager(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public boolean isFaqDepartmentManager(
			final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentManager.class);
		criteria.add(Restrictions.eq("manageFaq", Boolean.TRUE));
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		@SuppressWarnings("unchecked")
		List<DepartmentManager> managers = getHibernateTemplate().findByCriteria(criteria);
		return !managers.isEmpty();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentManagers()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<DepartmentManager> getDepartmentManagers() {
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentManager.class);
		return  getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentManagers(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<DepartmentManager> getDepartmentManagers(
			final Department department) {
		if (department == null) {
			return new ArrayList<DepartmentManager>();
		}
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentManager.class, "dm");
		criteria.createAlias("dm.user", "use");
		criteria.addOrder(Order.asc("use.displayName"));
		
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getAvailableDepartmentManagers(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<DepartmentManager> getAvailableDepartmentManagers(
			final Department department) {
		if (department == null) {
			return new ArrayList<DepartmentManager>();
		}
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentManager.class);
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		criteria.add(Restrictions.eq("available", Boolean.TRUE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentManagersNumber(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public int getDepartmentManagersNumber(final Department department) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				DepartmentManager.class.getSimpleName() + HqlUtils.AS_KEYWORD + "dm",
				HqlUtils.equals("department.id", department.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentManagerByOrder(
	 * org.esupportail.helpdesk.domain.beans.Department, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public DepartmentManager getDepartmentManagerByOrder(
			final Department department,
			final int i) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentManager.class);
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		criteria.add(Restrictions.eq(ORDER_ATTRIBUTE, new Integer(i)));
		List<DepartmentManager> departmentManagers = getHibernateTemplate().findByCriteria(criteria);
		if (departmentManagers.isEmpty()) {
			return null;
		}
		return departmentManagers.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addDepartmentManager(
	 * org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public void addDepartmentManager(
			final DepartmentManager departmentManager) {
		addObject(departmentManager);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateDepartmentManager(
	 * org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public void updateDepartmentManager(
			final DepartmentManager departmentManager) {
		updateObject(departmentManager);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteDepartmentManager(
	 * org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	public void deleteDepartmentManager(
			final DepartmentManager departmentManager) {
		deleteObject(departmentManager);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentManagers(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<DepartmentManager> getDepartmentManagers(final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentManager.class, "dm");
		criteria.createAlias("dm.department", "dep");
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		criteria.addOrder(Order.asc("dep.order"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________CATEGORY() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategory(long)
	 */
	@Override
	@RequestCache
	public Category getCategory(final long id) throws CategoryNotFoundException {
		return (Category) this.getHibernateTemplate().get(Category.class, id);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategories(org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Category> getCategories(final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategoriesWithEffectiveDefaultTicketScope(
	 * org.esupportail.helpdesk.domain.beans.Department, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Category> getCategoriesWithEffectiveDefaultTicketScope(
			final Department department,
			final String scope) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		criteria.add(Restrictions.eq("effectiveDefaultTicketScope", scope));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategories()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Category> getCategories() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addCategory(org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void addCategory(final Category category) {
		addObject(category);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateCategory(org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void updateCategory(final Category category) {
		updateObject(category);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteCategory(org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void deleteCategory(final Category category) {
		executeUpdate(HqlUtils.updateWhere(
				Action.class.getSimpleName() + " a",
				"a.categoryBefore = NULL",
				HqlUtils.equals("a.categoryBefore.id", category.getId())));
		executeUpdate(HqlUtils.updateWhere(
				Action.class.getSimpleName() + " a",
				"a.categoryAfter = NULL",
				HqlUtils.equals("a.categoryAfter.id", category.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				CategoryMember.class.getSimpleName() + " cm",
				HqlUtils.equals("cm.category.id", category.getId())));
		executeUpdate(HqlUtils.updateWhere(
				User.class.getSimpleName() + " u",
				"u.controlPanelCategoryFilter = NULL",
				HqlUtils.equals("u.controlPanelCategoryFilter.id", category.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				FaqLink.class.getSimpleName() + " fl",
				HqlUtils.equals("fl.category.id", category.getId())));
		deleteObject(category);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getRootCategories(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Category> getRootCategories(final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.add(Restrictions.isNull(PARENT_ATTRIBUTE));
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getSubCategories(org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Category> getSubCategories(final Category category) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.add(Restrictions.eq(PARENT_ATTRIBUTE, category));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getRootCategoriesNumber(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public int getRootCategoriesNumber(final Department department) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Category.class.getSimpleName() + HqlUtils.AS_KEYWORD + "category",
				HqlUtils.and(
						HqlUtils.equals("department.id", department.getId()),
						HqlUtils.isNull("parent")));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getSubCategoriesNumber(
	 * org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public int getSubCategoriesNumber(final Category category) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Category.class.getSimpleName() + HqlUtils.AS_KEYWORD + "category",
				HqlUtils.equals("parent.id", category.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategoryByOrder(
	 * org.esupportail.helpdesk.domain.beans.Department, org.esupportail.helpdesk.domain.beans.Category, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public Category getCategoryByOrder(
			final Department department,
			final Category parent,
			final int i) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		if (parent == null) {
			criteria.add(Restrictions.isNull(PARENT_ATTRIBUTE));
			criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		} else {
			criteria.add(Restrictions.eq(PARENT_ATTRIBUTE, parent));
		}
		criteria.add(Restrictions.eq(ORDER_ATTRIBUTE, new Integer(i)));
		List<Category> categories = getHibernateTemplate().findByCriteria(criteria);
		if (categories.isEmpty()) {
			return null;
		}
		return categories.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getVirtualCategories(
	 * org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Category> getVirtualCategories(final Category category) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.add(Restrictions.eq(REAL_CATEGORY_ATTRIBUTE, category));
		criteria.addOrder(Order.asc("department"));
		criteria.addOrder(Order.asc("label"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getVirtualCategoriesNumber(
	 * org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public int getVirtualCategoriesNumber(final Category category) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Category.class.getSimpleName(),
				HqlUtils.equals("realCategory.id", category.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategoriesNumber()
	 */
	@Override
	@RequestCache
	public int getCategoriesNumber() {
		String queryStr = HqlUtils.selectCountAllFrom(
				Category.class.getSimpleName());
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getRealCategoriesNumber()
	 */
	@Override
	@RequestCache
	public int getRealCategoriesNumber() {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Category.class.getSimpleName(),
				HqlUtils.isNull("realCategory"));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getVirtualCategoriesNumber()
	 */
	@Override
	@RequestCache
	public int getVirtualCategoriesNumber() {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Category.class.getSimpleName(),
				HqlUtils.isNotNull("realCategory"));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTargetCategories(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Category> getTargetCategories(final User author) {
		Query query = getQuery(HqlUtils.selectFromWhereOrderByDesc(
				"a.categoryAfter",
				Action.class.getSimpleName() + HqlUtils.AS_KEYWORD + "a",
				HqlUtils.and(
						HqlUtils.equals("a.user.id", HqlUtils.quote(author.getId())),
						HqlUtils.or(
								HqlUtils.equals("a.actionType", HqlUtils.quote(ActionType.CHANGE_CATEGORY)),
								HqlUtils.equals("a.actionType", HqlUtils.quote(ActionType.CHANGE_DEPARTMENT)))),
				"a.id"));
		return query.list();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________CATEGORY_MEMBER() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategoryMember(
	 * org.esupportail.helpdesk.domain.beans.Category, org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public CategoryMember getCategoryMember(
			final Category category,
			final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CategoryMember.class);
		criteria.add(Restrictions.eq(CATEGORY_ATTRIBUTE, category));
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		List<CategoryMember> members = getHibernateTemplate().findByCriteria(criteria);
		if (members.isEmpty()) {
			return null;
		}
		return members.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategoryMembers(
	 * org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<CategoryMember> getCategoryMembers(final Category category) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CategoryMember.class);
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		criteria.add(Restrictions.eq(CATEGORY_ATTRIBUTE, category));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategoryMembersNumber(
	 * org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public int getCategoryMembersNumber(final Category category) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				CategoryMember.class.getSimpleName() + HqlUtils.AS_KEYWORD + "categoryMember",
				HqlUtils.equals("category.id", category.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategoryMembers()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<CategoryMember> getCategoryMembers() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CategoryMember.class);
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addCategoryMember(
	 * org.esupportail.helpdesk.domain.beans.CategoryMember)
	 */
	@Override
	public void addCategoryMember(final CategoryMember categoryMember) {
		addObject(categoryMember);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateCategoryMember(
	 * org.esupportail.helpdesk.domain.beans.CategoryMember)
	 */
	@Override
	public void updateCategoryMember(final CategoryMember categoryMember) {
		updateObject(categoryMember);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteCategoryMember(
	 * org.esupportail.helpdesk.domain.beans.CategoryMember)
	 */
	@Override
	public void deleteCategoryMember(final CategoryMember categoryMember) {
		deleteObject(categoryMember);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategoryMembers(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<CategoryMember> getCategoryMembers(final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CategoryMember.class);
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategoryMembers(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<CategoryMember> getCategoryMembers(
			final User user,
			final Department department) {
		Query query = getQuery(HqlUtils.fromWhere(
				CategoryMember.class.getSimpleName() + HqlUtils.AS_KEYWORD + "cm",
				HqlUtils.and(
						HqlUtils.equals(
								"user.id",
								HqlUtils.quote(user.getId())),
						HqlUtils.equals(
								"category.department.id",
								department.getId()))));
		return query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategoryMemberByOrder(
	 * org.esupportail.helpdesk.domain.beans.Category, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public CategoryMember getCategoryMemberByOrder(
			final Category category,
			final int i) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CategoryMember.class);
		criteria.add(Restrictions.eq(CATEGORY_ATTRIBUTE, category));
		criteria.add(Restrictions.eq(ORDER_ATTRIBUTE, new Integer(i)));
		List<CategoryMember> categoryMembers = getHibernateTemplate().findByCriteria(criteria);
		if (categoryMembers.isEmpty()) {
			return null;
		}
		return categoryMembers.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getCategoryMembers(
	 * org.esupportail.helpdesk.domain.beans.DepartmentManager)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<CategoryMember> getCategoryMembers(final DepartmentManager departmentManager) {
		Query query = getQuery(HqlUtils.fromWhere(
				CategoryMember.class.getSimpleName() + HqlUtils.AS_KEYWORD + "cm",
				HqlUtils.and(
						HqlUtils.equals(
								"user.id",
								HqlUtils.quote(departmentManager.getUser().getId())),
						HqlUtils.equals(
								"category.department.id",
								departmentManager.getDepartment().getId()))));
		return query.list();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________FAQ() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaq(long)
	 */
	@Override
	@RequestCache
	public Faq getFaq(final long id) {
		return (Faq) this.getHibernateTemplate().get(Faq.class, id);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addFaq(
	 * org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void addFaq(final Faq faq) {
		addObject(faq);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateFaq(
	 * org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void updateFaq(final Faq faq) {
		updateObject(faq);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteFaq(
	 * org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	public void deleteFaq(final Faq faq) {
		executeUpdate(HqlUtils.updateWhere(
				Ticket.class.getSimpleName(),
				"connectionFaq = NULL",
				HqlUtils.equals("connectionFaq.id", faq.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedTicket.class.getSimpleName(),
				"connectionFaq = NULL",
				HqlUtils.equals("connectionFaq.id", faq.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				FaqLink.class.getSimpleName(),
				HqlUtils.equals("faq.id", faq.getId())));
		deleteObject(faq);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getRootFaqs(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Faq> getRootFaqs(final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Faq.class);
		criteria.add(Restrictions.isNull(PARENT_ATTRIBUTE));
		if (department == null) {
			criteria.add(Restrictions.isNull(DEPARTMENT_ATTRIBUTE));
		} else {
			criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		}
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getSubFaqs(
	 * org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Faq> getSubFaqs(final Faq faq) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Faq.class);
		criteria.add(Restrictions.eq(PARENT_ATTRIBUTE, faq));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getRootFaqsNumber(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public int getRootFaqsNumber(final Department department) {
		String nullParentCondition = HqlUtils.isNull("parent");
		String departmentCondition;
		if (department == null) {
			departmentCondition = HqlUtils.isNull("department");
		} else {
			departmentCondition = HqlUtils.equals("department", department.getId());
		}
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Faq.class.getSimpleName(),
				HqlUtils.and(nullParentCondition, departmentCondition));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getSubFaqsNumber(
	 * org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	@RequestCache
	public int getSubFaqsNumber(final Faq faq) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Faq.class.getSimpleName(),
				HqlUtils.equals("parent.id", faq.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaqByOrder(
	 * org.esupportail.helpdesk.domain.beans.Department, org.esupportail.helpdesk.domain.beans.Faq, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public Faq getFaqByOrder(
			final Department department,
			final Faq parent,
			final int i) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Faq.class);
		if (parent == null) {
			criteria.add(Restrictions.isNull(PARENT_ATTRIBUTE));
			criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		} else {
			criteria.add(Restrictions.eq(PARENT_ATTRIBUTE, parent));
		}
		criteria.add(Restrictions.eq(ORDER_ATTRIBUTE, new Integer(i)));
		List<Faq> faqs = getHibernateTemplate().findByCriteria(criteria);
		if (faqs.isEmpty()) {
			return null;
		}
		return faqs.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaqsChangedAfter(
	 * java.sql.Timestamp, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Faq> getFaqsChangedAfter(
			final Timestamp lastUpdate,
			final int maxResults) {
		Query query = getQuery(HqlUtils.fromWhereOrderByAsc(
				Faq.class.getSimpleName(),
				HqlUtils.ge("lastUpdate", HqlUtils.quote(lastUpdate.toString())),
				"lastUpdate"));
		query.setMaxResults(maxResults);
		return query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#hasVisibleFaq(
	 * org.esupportail.helpdesk.domain.beans.User, java.util.List)
	 */
	@Override
	@RequestCache
	public boolean hasVisibleFaq(
			final User user,
			final List<Department> faqVisibleDepartments) {
		List<String> rootScopes = new ArrayList<String>();
		rootScopes.add(FaqScope.ALL);
		if (user != null) {
			rootScopes.add(FaqScope.AUTHENTICATED);
			if (user.getAdmin()) {
				rootScopes.add(FaqScope.MANAGER);
			}
		}
		String rootCondition = HqlUtils.and(
				HqlUtils.stringIn("effectiveScope", rootScopes),
				HqlUtils.isNull("department"));
		// build managed and visible departments ids
		List<Department> managedDepartments = new ArrayList<Department>();
		for (DepartmentManager manager : getDepartmentManagers(user)) {
			managedDepartments.add(manager.getDepartment());
		}
		List<Long> managedDepartmentIds = new ArrayList<Long>();
		List<Long> visibleDepartmentIds = new ArrayList<Long>();
		for (Department department : getEnabledDepartments()) {
			if (managedDepartments.contains(department)) {
				managedDepartmentIds.add(department.getId());
			} else if (faqVisibleDepartments.contains(department)) {
				visibleDepartmentIds.add(department.getId());
			}
		}
		// look for FAQs in managed departments
		String managedCondition = HqlUtils.longIn("department", managedDepartmentIds);
		// look for FAQs in visible departments
		List<String> visibleScopes = new ArrayList<String>();
		visibleScopes.add(FaqScope.ALL);
		if (user != null) {
			visibleScopes.add(FaqScope.AUTHENTICATED);
		}
		visibleScopes.add(FaqScope.DEPARTMENT);
		String visibleCondition = HqlUtils.and(
				HqlUtils.stringIn("effectiveScope", visibleScopes),
				HqlUtils.longIn("department", visibleDepartmentIds));
		// mix conditions
		String condition = HqlUtils.and(
				HqlUtils.isNull("parent"),
				HqlUtils.or(rootCondition, managedCondition, visibleCondition));
		return getQueryIntResult(
				HqlUtils.selectCountAllFromWhere(
						Faq.class.getSimpleName(), condition)) > 0;
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________FAQ_EVENT() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addFaqEvent(
	 * org.esupportail.helpdesk.domain.beans.FaqEvent)
	 */
	@Override
	public void addFaqEvent(final FaqEvent faqEvent) {
		addObject(faqEvent);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaqEvents()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<FaqEvent> getFaqEvents() {
		DetachedCriteria criteria = DetachedCriteria.forClass(FaqEvent.class);
		criteria.addOrder(Order.desc("date"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteFaqEvent(
	 * org.esupportail.helpdesk.domain.beans.FaqEvent)
	 */
	@Override
	public void deleteFaqEvent(final FaqEvent faqEvent) {
		deleteObject(faqEvent);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________TICKET() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicket(long)
	 */
	@Override
	@RequestCache
	public Ticket getTicket(final long id) {
		return (Ticket) this.getHibernateTemplate().get(Ticket.class, id);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketsNumber(
	 * org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public int getTicketsNumber(final Category category) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				HqlUtils.equals("category.id", category.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTickets(org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Ticket> getTickets(final Category category) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Ticket.class);
		criteria.add(Restrictions.eq(CATEGORY_ATTRIBUTE, category));
		criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTickets(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Ticket> getTickets(final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Ticket.class);
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOpenedTicketsByLastActionDate(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Ticket> getOpenedTicketsByLastActionDate(final Department department) {
		Query query = getQuery(HqlUtils.fromWhereOrderByAsc(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "t",
				HqlUtils.and(
					HqlUtils.stringIn("status", new String [] {
							TicketStatus.INPROGRESS,
							TicketStatus.FREE,
							TicketStatus.INCOMPLETE,
							TicketStatus.POSTPONED,
					}),
					HqlUtils.equals("department.id", department.getId())),
				"lastActionDate"));
		return query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketsNumber(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public int getTicketsNumber(final Department department) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "t",
				HqlUtils.equals("department.id", department.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addTicket(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public void addTicket(final Ticket ticket) {
		addObject(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateTicket(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public void updateTicket(final Ticket ticket) {
		updateObject(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#reloadTicket(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	public Ticket reloadTicket(final Ticket ticket) {
		if (ticket == null) {
			return null;
		}
		return (Ticket) this.getHibernateTemplate().get(Ticket.class, ticket.getId());
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteTicket(
	 * org.esupportail.helpdesk.domain.beans.Ticket, boolean)
	 */
	@Override
	public void deleteTicket(
			final Ticket ticket,
			final boolean deleteFiles) {
		for (Action action : getActions(ticket, true)) {
			deleteAction(action);
		}
		for (FileInfo fileInfo : getFileInfos(ticket, true)) {
			deleteFileInfo(fileInfo, deleteFiles);
		}
		for (Invitation invitation : getInvitations(ticket)) {
			deleteInvitation(invitation);
		}
		executeUpdate(HqlUtils.updateWhere(
				Action.class.getSimpleName() + " a",
				"a.oldConnectionAfter = NULL",
				HqlUtils.equals("a.oldConnectionAfter.id", ticket.getId())));
		executeUpdate(HqlUtils.updateWhere(
				Ticket.class.getSimpleName() + " t",
				"t.connectionTicket = NULL",
				HqlUtils.equals("t.connectionTicket.id", ticket.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedTicket.class.getSimpleName() + " t",
				"t.connectionTicket = NULL",
				HqlUtils.equals("t.connectionTicket.id", ticket.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				TicketMonitoring.class.getSimpleName() + " tm",
				HqlUtils.equals("tm.ticket.id", ticket.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				TicketView.class.getSimpleName() + " tv",
				HqlUtils.equals("tv.ticket.id", ticket.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				Bookmark.class.getSimpleName() + " b",
				HqlUtils.equals("b.ticket.id", ticket.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				HistoryItem.class.getSimpleName() + " hi",
				HqlUtils.equals("hi.ticket.id", ticket.getId())));
		deleteObject(ticket);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketsNumber()
	 */
	@Override
	@RequestCache
	public int getTicketsNumber() {
		String queryStr = HqlUtils.selectCountAllFrom(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket");
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTickets(long, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Ticket> getTickets(
			final long startIndex,
			final int num) {
		Query query = getQuery(HqlUtils.fromWhereOrderBy(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				HqlUtils.ge("id", startIndex),
				"id"));
		query.setMaxResults(num);
		return query.list();
	}

	/**
	 * @param user
	 * @return the condition for opened managed tickets.
	 */
	private String getOpenManagedTicketsCondition(
			final User user) {
		return HqlUtils.and(
				HqlUtils.equals("ticket.manager.id", HqlUtils.quote(user.getId())),
				HqlUtils.stringIn("ticket.status", new String [] {
						TicketStatus.FREE,
						TicketStatus.INCOMPLETE,
						TicketStatus.INPROGRESS,
						TicketStatus.POSTPONED,
				}));
	}

	/**
	 * @param department
	 * @param user
	 * @return the condition for opened managed tickets.
	 */
	private String getOpenManagedTicketsCondition(
			final Department department,
			final User user) {
		return HqlUtils.and(
				HqlUtils.equals("ticket.department.id", department.getId()),
				getOpenManagedTicketsCondition(user));
	}

	/**
	 * @param category
	 * @param user
	 * @return the condition for opened managed tickets.
	 */
	private String getOpenManagedTicketsCondition(
			final Category category,
			final User user) {
		return HqlUtils.and(
				HqlUtils.equals("ticket.category.id", category.getId()),
				getOpenManagedTicketsCondition(user));
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOpenManagedTickets(
	 * org.esupportail.helpdesk.domain.beans.Department, org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Ticket> getOpenManagedTickets(
			final Department department,
			final User user) {
		String hqlQuery = HqlUtils.selectFromWhere(
				"ticket",
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				getOpenManagedTicketsCondition(department, user));
		return getQuery(hqlQuery).list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOpenManagedTickets(
	 * org.esupportail.helpdesk.domain.beans.Category, org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Ticket> getOpenManagedTickets(
			final Category category,
			final User user) {
		String hqlQuery = HqlUtils.selectFromWhere(
				"ticket",
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				getOpenManagedTicketsCondition(category, user));
		return getQuery(hqlQuery).list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOpenManagedTicketsNumber(
	 * org.esupportail.helpdesk.domain.beans.Category, org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public int getOpenManagedTicketsNumber(
			final Category category,
			final User user) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				getOpenManagedTicketsCondition(category, user));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOpenManagedTicketsNumber(
	 * org.esupportail.helpdesk.domain.beans.Department, org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@RequestCache
	public int getOpenManagedTicketsNumber(
			final Department department,
			final User user) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				getOpenManagedTicketsCondition(department, user));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOwnedTickets(org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Ticket> getOwnedTickets(final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Ticket.class);
		criteria.add(Restrictions.eq("owner.id", user.getId()));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateTicketsEffectiveScope(
	 * org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	public void updateTicketsEffectiveScope(final Category category) {
		executeUpdate(HqlUtils.updateWhere(
				Ticket.class.getSimpleName(),
				"effectiveScope = "
				+ HqlUtils.quote(category.getEffectiveDefaultTicketScope()),
				HqlUtils.and(
						HqlUtils.equals(
								"scope",
								HqlUtils.quote(TicketScope.DEFAULT)),
						HqlUtils.equals("category.id", category.getId()))));
		executeUpdate(HqlUtils.updateWhere(
				Ticket.class.getSimpleName(),
				"effectiveScope = scope",
				HqlUtils.and(
						HqlUtils.not(HqlUtils.equals(
								"scope",
								HqlUtils.quote(TicketScope.DEFAULT))),
						HqlUtils.equals("category.id", category.getId()))));
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketsChangedAfter(
	 * java.sql.Timestamp, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Ticket> getTicketsChangedAfter(
			final Timestamp lastUpdate,
			final int maxResults) {
		Query query = getQuery(HqlUtils.fromWhereOrderByAsc(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				HqlUtils.ge("lastActionDate", HqlUtils.quote(lastUpdate.toString())),
				"lastActionDate"));
		query.setMaxResults(maxResults);
		return query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getClosedTicketsBefore(java.sql.Timestamp, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Ticket> getClosedTicketsBefore(final Timestamp timestamp, final int maxResults) {
		Query query = getQuery(HqlUtils.fromWhereOrderByAsc(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				HqlUtils.and(
						HqlUtils.not(
								HqlUtils.stringIn("ticket.status",
										new String[] {
										TicketStatus.FREE,
										TicketStatus.INCOMPLETE,
										TicketStatus.INPROGRESS,
										TicketStatus.POSTPONED,
								})),
						HqlUtils.lt(
								"ticket.lastActionDate",
								HqlUtils.quote(timestamp.toString()))),
		"lastActionDate"));
		query.setMaxResults(maxResults);
		return query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getNonApprovedTicketsClosedBefore(
	 * java.sql.Timestamp, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Ticket> getNonApprovedTicketsClosedBefore(
			final Timestamp timestamp,
			final int maxResults) {
		Query query = getQuery(HqlUtils.fromWhereOrderByAsc(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				HqlUtils.and(
						HqlUtils.stringIn("ticket.status",
								new String[] {
								TicketStatus.CLOSED,
								TicketStatus.CONNECTED_TO_FAQ,
								TicketStatus.CONNECTED_TO_TICKET,
						}),
						HqlUtils.lt(
								"ticket.lastActionDate",
								HqlUtils.quote(timestamp.toString()))),
		"lastActionDate"));
		query.setMaxResults(maxResults);
		return query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketsToRecall()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Ticket> getTicketsToRecall() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Query query = getQuery(HqlUtils.fromWhereOrderByAsc(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				HqlUtils.and(
						HqlUtils.equals(
								"ticket.status",
								HqlUtils.quote(TicketStatus.POSTPONED)),
						HqlUtils.lt(
								"ticket.recallDate",
								HqlUtils.quote(now.toString()))),
				"recallDate"));
		return query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOldestTicketDate()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public Timestamp getOldestTicketDate() {
		Query query = getQuery(HqlUtils.fromOrderBy(Ticket.class.getSimpleName(), "id"));
		query.setMaxResults(1);
		List<Ticket> tickets = query.list();
		Timestamp oldestTicketDate = null;
		if (!tickets.isEmpty()) {
			oldestTicketDate = tickets.get(0).getCreationDate();
		}
		query = getQuery(HqlUtils.fromOrderBy(ArchivedTicket.class.getSimpleName(), "ticketId"));
		query.setMaxResults(1);
		List<ArchivedTicket> archivedTickets = query.list();
		Timestamp oldestArchivedTicketDate = null;
		if (!archivedTickets.isEmpty()) {
			oldestArchivedTicketDate = archivedTickets.get(0).getCreationDate();
		}
		if (oldestTicketDate == null) {
			return oldestArchivedTicketDate;
		}
		if (oldestArchivedTicketDate == null) {
			return oldestTicketDate;
		}
		if (oldestTicketDate.after(oldestArchivedTicketDate)) {
			return oldestArchivedTicketDate;
		}
		return oldestTicketDate;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteAllTickets()
	 */
	@Override
	public void deleteAllTickets() {
		logger.info("deleting alerts...");
		executeUpdate(HqlUtils.delete(Alert.class.getSimpleName()));

		logger.info("deleting ticket actions...");
		executeUpdate(HqlUtils.delete(DeletedItem.class.getSimpleName()));
		executeUpdate(HqlUtils.delete(Action.class.getSimpleName()));
		executeUpdate(HqlUtils.delete(ArchivedAction.class.getSimpleName()));
		logger.info("deleting bookmarks...");
		executeUpdate(HqlUtils.delete(Bookmark.class.getSimpleName()));
		logger.info("deleting history items...");
		executeUpdate(HqlUtils.delete(HistoryItem.class.getSimpleName()));
		executeUpdate(HqlUtils.delete(TicketView.class.getSimpleName()));
		logger.info("deleting uploaded files...");
		executeUpdate(HqlUtils.delete(FileInfo.class.getSimpleName()));
		executeUpdate(HqlUtils.delete(ArchivedFileInfo.class.getSimpleName()));
		logger.info("deleting invitations...");
		executeUpdate(HqlUtils.delete(Invitation.class.getSimpleName()));
		executeUpdate(HqlUtils.delete(ArchivedInvitation.class.getSimpleName()));
		logger.info("deleting monitoring information...");
		executeUpdate(HqlUtils.delete(TicketMonitoring.class.getSimpleName()));

		logger.info("deleting tickets...");
		executeUpdate(HqlUtils.update(Ticket.class.getSimpleName() + " t", "t.connectionTicket = NULL"));
		executeUpdate(HqlUtils.update(Ticket.class.getSimpleName() + " t", "t.connectionArchivedTicket = NULL"));
		executeUpdate(HqlUtils.update(ArchivedTicket.class.getSimpleName() + " t", "t.connectionTicket = NULL"));
		executeUpdate(HqlUtils.update(ArchivedTicket.class.getSimpleName() + " t", "t.connectionArchivedTicket = NULL"));
		executeUpdate(HqlUtils.delete(Ticket.class.getSimpleName()));
		executeUpdate(HqlUtils.delete(ArchivedTicket.class.getSimpleName()));

		logger.info("deleting uploaded data...");
		this.fileManager.deleteAllContents();
	}
	
	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________ACTION() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getActions(
	 * org.esupportail.helpdesk.domain.beans.Ticket, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Action> getActions(final Ticket ticket, final boolean dateAsc) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Action.class);
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		if (dateAsc) {
			criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		} else {
			criteria.addOrder(Order.desc(ID_ATTRIBUTE));
		}
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getActions(
	 * org.esupportail.helpdesk.domain.beans.Ticket, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Action> getActionsWithoutUpload(final Ticket ticket, final boolean dateAsc) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Action.class);
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		criteria.add(Restrictions.ne(ACTION_TYPE_ATTRIBUTE, ActionType.UPLOAD));
		if (dateAsc) {
			criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		} else {
			criteria.addOrder(Order.desc(ID_ATTRIBUTE));
		}
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getActions(
	 * org.esupportail.helpdesk.domain.beans.Ticket, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Action> getActionsByActionType(final Ticket ticket, final String actionType, final boolean dateAsc) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Action.class);
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		criteria.add(Restrictions.eq(ACTION_TYPE_ATTRIBUTE, actionType));
		if (dateAsc) {
			criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		} else {
			criteria.addOrder(Order.desc(ID_ATTRIBUTE));
		}
		return getHibernateTemplate().findByCriteria(criteria);
	}


	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getActionsNumber(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public int getActionsNumber(final Ticket ticket) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Action.class.getSimpleName() + HqlUtils.AS_KEYWORD + "action",
				HqlUtils.equals("ticket.id", ticket.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getLastAction(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public Action getLastAction(final Ticket ticket) {
		List<Action> actions = getActions(ticket, false);
		if (actions.isEmpty()) {
			return null;
		}
		return actions.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getLastAction(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public Action getLastActionWithoutUpload(final Ticket ticket) {
		List<Action> actions = getActionsWithoutUpload(ticket, false);
		if (actions.isEmpty()) {
			return null;
		}
		return actions.get(0);
	}

	
	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getLastActionByActionType(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public Action getLastActionByActionType(final Ticket ticket, final String actionType) {
		List<Action> actions = getActionsByActionType(ticket, actionType, false);
		if (actions.isEmpty()) {
			return null;
		}
		return actions.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addAction(
	 * org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	public void addAction(final Action action) {
		addObject(action);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateAction(
	 * org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	public void updateAction(final Action action) {
		updateObject(action);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteAction(
	 * org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	public void deleteAction(final Action action) {
		executeUpdate(HqlUtils.deleteWhere(
				Alert.class.getSimpleName() + " a",
				HqlUtils.equals("a.action.id", action.getId())));
		deleteObject(action);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getActionsNumber()
	 */
	@Override
	@RequestCache
	public int getActionsNumber() {
		String queryStr = HqlUtils.selectCountAllFrom(
				Action.class.getSimpleName() + HqlUtils.AS_KEYWORD + "action");
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getActions(long, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Action> getActions(
			final long startIndex,
			final int num) {
		Query query = getQuery(HqlUtils.fromWhereOrderBy(
				Action.class.getSimpleName() + HqlUtils.AS_KEYWORD + "action",
				HqlUtils.ge("id", startIndex),
				"id"));
		query.setMaxResults(num);
		return query.list();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________FILEINFO() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFileInfos(
	 * org.esupportail.helpdesk.domain.beans.Ticket, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<FileInfo> getFileInfos(final Ticket ticket, final boolean dateAsc) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FileInfo.class);
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		if (dateAsc) {
			criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		} else {
			criteria.addOrder(Order.desc(ID_ATTRIBUTE));
		}
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFileInfosNumber(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@RequestCache
	public int getFileInfosNumber(final Ticket ticket) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				FileInfo.class.getSimpleName() + HqlUtils.AS_KEYWORD + "fileInfo",
				HqlUtils.equals("ticket.id", ticket.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addFileInfo(
	 * org.esupportail.helpdesk.domain.beans.FileInfo)
	 */
	@Override
	public void addFileInfo(final FileInfo fileInfo) {
		addObject(fileInfo);
		fileManager.writeFileInfoContent(fileInfo);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateFileInfo(
	 * org.esupportail.helpdesk.domain.beans.FileInfo)
	 */
	@Override
	public void updateFileInfo(final FileInfo fileInfo) {
		updateObject(fileInfo);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFileInfoContent(
	 * org.esupportail.helpdesk.domain.beans.FileInfo)
	 */
	@Override
	@RequestCache
	public byte[] getFileInfoContent(final FileInfo fileInfo) {
		return fileManager.readFileInfoContent(fileInfo);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteFileInfo(
	 * org.esupportail.helpdesk.domain.beans.FileInfo, boolean)
	 */
	@Override
	public void deleteFileInfo(
			final FileInfo fileInfo,
			final boolean deleteContent) {
		deleteObject(fileInfo);
		if (deleteContent) {
			fileManager.deleteFileInfoContent(fileInfo);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteArchivedFileInfo(
	 * org.esupportail.helpdesk.domain.beans.FileInfo, boolean)
	 */
	@Override
	public void deleteArchivedFileInfo(
			final ArchivedFileInfo archivedFileInfo,
			final boolean deleteContent) {
		deleteObject(archivedFileInfo);
		if (deleteContent) {
			fileManager.deleteArchivedFileInfoContent(archivedFileInfo);
		}				
	}
	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________ARCHIVED_TICKET() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedTicket(long)
	 */
	@Override
	@RequestCache
	public ArchivedTicket getArchivedTicket(final long id) {
		return (ArchivedTicket) this.getHibernateTemplate().get(ArchivedTicket.class, id);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addArchivedTicket(
	 * org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void addArchivedTicket(final ArchivedTicket archivedTicket) {
		DetachedCriteria criteria;
		addObject(archivedTicket);
		criteria = DetachedCriteria.forClass(Ticket.class);
		criteria.add(Restrictions.eq("connectionTicket.id", archivedTicket.getTicketId()));
		List<Ticket> tickets = getHibernateTemplate().findByCriteria(criteria);
		for (Ticket ticket : tickets) {
			ticket.setConnectionTicket(null);
			ticket.setConnectionArchivedTicket(archivedTicket);
			updateTicket(ticket);
		}
		criteria = DetachedCriteria.forClass(ArchivedTicket.class);
		criteria.add(Restrictions.eq("connectionTicket.id", archivedTicket.getTicketId()));
		List<ArchivedTicket> archivedTickets = getHibernateTemplate().findByCriteria(criteria);
		for (ArchivedTicket archivedTicket2 : archivedTickets) {
			archivedTicket2.setConnectionTicket(null);
			archivedTicket2.setConnectionArchivedTicket(archivedTicket);
			updateArchivedTicket(archivedTicket2);
		}
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateArchivedTicket(
	 * org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	public void updateArchivedTicket(final ArchivedTicket archivedTicket) {
		updateObject(archivedTicket);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteArchivedTicket(
	 * org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	public void deleteArchivedTicket(final ArchivedTicket archivedTicket) {
		for (ArchivedAction archivedAction : getArchivedActions(archivedTicket, false)) {
			deleteArchivedAction(archivedAction);
		}
		for (ArchivedFileInfo archivedFileInfo : getArchivedFileInfos(archivedTicket)) {
			
			deleteArchivedFileInfo(archivedFileInfo);
			this.fileManager.deleteArchivedFileInfoContent(archivedFileInfo);
		}
		for (ArchivedInvitation archivedInvitation : getArchivedInvitations(archivedTicket)) {
			deleteArchivedInvitation(archivedInvitation);
		}
		executeUpdate(HqlUtils.deleteWhere(
				Bookmark.class.getSimpleName() + " b",
				HqlUtils.equals("b.archivedTicket.id", archivedTicket.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				HistoryItem.class.getSimpleName() + " hi",
				HqlUtils.equals("hi.archivedTicket.id", archivedTicket.getId())));
        
		executeUpdate(HqlUtils.updateWhere(
                Ticket.class.getSimpleName() + " t",
                "t.connectionArchivedTicket = NULL",
                HqlUtils.equals("t.connectionArchivedTicket.id", archivedTicket.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedTicket.class.getSimpleName() + " t",
				"t.connectionArchivedTicket = NULL",
				HqlUtils.equals("t.connectionArchivedTicket.id", archivedTicket.getId())));

		executeUpdate(HqlUtils.deleteWhere(
				ArchivedTicket.class.getSimpleName() + " a",
				HqlUtils.equals("a.id", archivedTicket.getId())));

		
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedTickets(long, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<ArchivedTicket> getArchivedTickets(
			final long startIndex,
			final int num) {
		Query query = getQuery(HqlUtils.fromWhereOrderBy(
				ArchivedTicket.class.getSimpleName(),
				HqlUtils.ge("id", startIndex),
				"id"));
		query.setMaxResults(num);
		return query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketsArchivedAfter(java.sql.Timestamp, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ArchivedTicket> getTicketsArchivedAfter(
			final Timestamp lastUpdate,
			final int maxResults) {
		Query query = getQuery(HqlUtils.fromWhereOrderByAsc(
				ArchivedTicket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "archivedTicket",
				HqlUtils.ge("archivingDate", HqlUtils.quote(lastUpdate.toString())),
				"archivingDate"));
		query.setMaxResults(maxResults);
		return query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedTicketByOriginalId(long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public ArchivedTicket getArchivedTicketByOriginalId(final long id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArchivedTicket.class);
		criteria.add(Restrictions.eq("ticketId", new Long(id)));
		List<ArchivedTicket> archivedTickets = getHibernateTemplate().findByCriteria(criteria);
		if (archivedTickets.isEmpty()) {
			return null;
		}
		return archivedTickets.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedTickets(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<ArchivedTicket> getArchivedTickets(final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArchivedTicket.class);
		criteria.add(Restrictions.eq("department", department));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedTicketsNumber(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public int getArchivedTicketsNumber(final Department department) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				ArchivedTicket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "at",
				HqlUtils.equals("department.id", department.getId()));
		return getQueryIntResult(queryStr);
	}

	//////////////////////////////////////////////////////////////
	// ArchivedAction
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedActions(
	 * org.esupportail.helpdesk.domain.beans.ArchivedTicket, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<ArchivedAction> getArchivedActions(
			final ArchivedTicket archivedTicket,
			final boolean dateAsc) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArchivedAction.class);
		criteria.add(Restrictions.eq("archivedTicket", archivedTicket));
		if (dateAsc) {
			criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		} else {
			criteria.addOrder(Order.desc(ID_ATTRIBUTE));
		}
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addArchivedAction(
	 * org.esupportail.helpdesk.domain.beans.ArchivedAction)
	 */
	@Override
	public void addArchivedAction(final ArchivedAction archivedAction) {
		addObject(archivedAction);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateArchivedAction(
	 * org.esupportail.helpdesk.domain.beans.ArchivedAction)
	 */
	@Override
	public void updateArchivedAction(final ArchivedAction archivedAction) {
		updateObject(archivedAction);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteArchivedAction(
	 * org.esupportail.helpdesk.domain.beans.ArchivedAction)
	 */
	@Override
	public void deleteArchivedAction(final ArchivedAction archivedAction) {
		deleteObject(archivedAction);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedTicketsOlderThan(Date)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public  List<ArchivedTicket> getArchivedTicketsOlderThan(
			final Date date) {

		DetachedCriteria criteria = DetachedCriteria.forClass(ArchivedTicket.class);
		criteria.add(Restrictions.lt(CREATION_DATE_ATTRIBUTE, date));
		criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
		
	}


	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedActions(long, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ArchivedAction> getArchivedActions(
			final long startIndex,
			final int num) {
		Query query = getQuery(HqlUtils.fromWhereOrderBy(
				ArchivedAction.class.getSimpleName() + HqlUtils.AS_KEYWORD + "archivedAction",
				HqlUtils.ge("id", startIndex),
				"id"));
		query.setMaxResults(num);
		return query.list();
	}

	//////////////////////////////////////////////////////////////
	// ArchivedFileInfo
	//////////////////////////////////////////////////////////////

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedFileInfos(
	 * org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<ArchivedFileInfo> getArchivedFileInfos(final ArchivedTicket archivedTicket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArchivedFileInfo.class);
		criteria.add(Restrictions.eq("archivedTicket", archivedTicket));
		criteria.addOrder(Order.desc(ID_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addArchivedFileInfo(
	 * org.esupportail.helpdesk.domain.beans.ArchivedFileInfo)
	 */
	@Override
	public void addArchivedFileInfo(final ArchivedFileInfo archivedFileInfo) {
		addObject(archivedFileInfo);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedFileInfoContent(
	 * org.esupportail.helpdesk.domain.beans.ArchivedFileInfo)
	 */
	@Override
	@RequestCache
	public byte[] getArchivedFileInfoContent(final ArchivedFileInfo archivedFileInfo) {
		return fileManager.readArchivedFileInfoContent(archivedFileInfo);
	}

	/**
	 * Delete an ArchivedFileInfo.
	 * @param archivedFileInfo
	 */
	protected void deleteArchivedFileInfo(final ArchivedFileInfo archivedFileInfo) {
		deleteObject(archivedFileInfo);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedTicketsNumber()
	 */
	@Override
	@RequestCache
	public int getArchivedTicketsNumber() {
		String queryStr = HqlUtils.selectCountAllFrom(
				ArchivedTicket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket");
		return getQueryIntResult(queryStr);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________TICKET_VIEW() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketView(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public TicketView getTicketView(
			final User user,
			final Ticket ticket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TicketView.class);
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		List<TicketView> ticketViews = getHibernateTemplate().findByCriteria(criteria);
		if (ticketViews.isEmpty()) {
			return null;
		}
		return ticketViews.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addTicketView(
	 * org.esupportail.helpdesk.domain.beans.TicketView)
	 */
	@Override
	public void addTicketView(
			final TicketView ticketView) {
		addObject(ticketView);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateTicketView(
	 * org.esupportail.helpdesk.domain.beans.TicketView)
	 */
	@Override
	public void updateTicketView(
			final TicketView ticketView) {
		updateObject(ticketView);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteTicketView(
	 * org.esupportail.helpdesk.domain.beans.TicketView)
	 */
	@Override
	public void deleteTicketView(
			final TicketView ticketView) {
		deleteObject(ticketView);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________TICKET_MONITORING() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketMonitoring(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public TicketMonitoring getTicketMonitoring(
			final User user,
			final Ticket ticket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TicketMonitoring.class);
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		List<TicketMonitoring> ticketMonitorings = getHibernateTemplate().findByCriteria(criteria);
		if (ticketMonitorings.isEmpty()) {
			return null;
		}
		return ticketMonitorings.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketMonitorings(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<TicketMonitoring> getTicketMonitorings(final Ticket ticket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TicketMonitoring.class);
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addTicketMonitoring(
	 * org.esupportail.helpdesk.domain.beans.TicketMonitoring)
	 */
	@Override
	public void addTicketMonitoring(
			final TicketMonitoring ticketMonitoring) {
		addObject(ticketMonitoring);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteTicketMonitoring(
	 * org.esupportail.helpdesk.domain.beans.TicketMonitoring)
	 */
	@Override
	public void deleteTicketMonitoring(
			final TicketMonitoring ticketMonitoring) {
		deleteObject(ticketMonitoring);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________ALERT() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addAlert(org.esupportail.helpdesk.domain.beans.Alert)
	 */
	@Override
	public void addAlert(final Alert alert) {
		addObject(alert);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getAlerts(org.esupportail.helpdesk.domain.beans.Action)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Alert> getAlerts(final Action action) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Alert.class);
		criteria.add(Restrictions.eq(ACTION_ATTRIBUTE, action));
		criteria.addOrder(Order.desc("user.id"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________INVITATION() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getInvitation(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Invitation getInvitation(
			final User user,
			final Ticket ticket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Invitation.class);
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		List<Invitation> invitations = getHibernateTemplate().findByCriteria(criteria);
		if (invitations.isEmpty()) {
			return null;
		}
		return invitations.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addInvitation(
	 * org.esupportail.helpdesk.domain.beans.Invitation)
	 */
	@Override
	public void addInvitation(
			final Invitation invitation) {
		addObject(invitation);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteInvitation(
	 * org.esupportail.helpdesk.domain.beans.Invitation)
	 */
	@Override
	public void deleteInvitation(
			final Invitation invitation) {
		deleteObject(invitation);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getInvitations(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Invitation> getInvitations(
			final Ticket ticket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Invitation.class);
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getInvitations(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Invitation> getInvitations(
			final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Invitation.class);
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedInvitation(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public ArchivedInvitation getArchivedInvitation(
			final User user,
			final ArchivedTicket archivedTicket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArchivedInvitation.class);
		criteria.add(Restrictions.eq("archivedTicket", archivedTicket));
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		List<ArchivedInvitation> archivedInvitations = getHibernateTemplate().findByCriteria(criteria);
		if (archivedInvitations.isEmpty()) {
			return null;
		}
		return archivedInvitations.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addArchivedInvitation(
	 * org.esupportail.helpdesk.domain.beans.ArchivedInvitation)
	 */
	@Override
	public void addArchivedInvitation(
			final ArchivedInvitation archivedInvitation) {
		addObject(archivedInvitation);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteArchivedInvitation(
	 * org.esupportail.helpdesk.domain.beans.ArchivedInvitation)
	 */
	@Override
	public void deleteArchivedInvitation(
			final ArchivedInvitation archivedInvitation) {
		deleteObject(archivedInvitation);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getArchivedInvitations(
	 * org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<ArchivedInvitation> getArchivedInvitations(
			final ArchivedTicket archivedTicket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArchivedInvitation.class);
		criteria.add(Restrictions.eq("archivedTicket", archivedTicket));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentInvitation(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public DepartmentInvitation getDepartmentInvitation(
			final User user,
			final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentInvitation.class);
		criteria.add(Restrictions.eq("department", department));
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		List<DepartmentInvitation> departmentInvitations = getHibernateTemplate().findByCriteria(criteria);
		if (departmentInvitations.isEmpty()) {
			return null;
		}
		return departmentInvitations.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addDepartmentInvitation(
	 * org.esupportail.helpdesk.domain.beans.DepartmentInvitation)
	 */
	@Override
	public void addDepartmentInvitation(
			final DepartmentInvitation departmentInvitation) {
		addObject(departmentInvitation);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteDepartmentInvitation(
	 * org.esupportail.helpdesk.domain.beans.DepartmentInvitation)
	 */
	@Override
	public void deleteDepartmentInvitation(
			final DepartmentInvitation departmentInvitation) {
		deleteObject(departmentInvitation);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentInvitations(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<DepartmentInvitation> getDepartmentInvitations(
			final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentInvitation.class);
		criteria.add(Restrictions.eq("department", department));
		criteria.addOrder(Order.asc("id"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getInvitedUsers(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<User> getInvitedUsers(final User author) {
		Query query = getQuery(HqlUtils.selectFromWhereOrderByDesc(
				"a.invitedUser",
				Action.class.getSimpleName() + HqlUtils.AS_KEYWORD + "a",
				HqlUtils.equals("a.user.id", HqlUtils.quote(author.getId())),
				"a.id"));
		return query.list();
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________DELETED_ITEM() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDeletedItems()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<DeletedItem> getDeletedItems() {
		return getHibernateTemplate().loadAll(DeletedItem.class);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteDeletedItem(
	 * org.esupportail.helpdesk.domain.beans.DeletedItem)
	 */
	@Override
	public void deleteDeletedItem(final DeletedItem deletedItem) {
		deleteObject(deletedItem);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteAllDeletedItems()
	 */
	@Override
	public void deleteAllDeletedItems() {
		executeUpdate(HqlUtils.delete(
				DeletedItem.class.getSimpleName()));
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addDeletedItem(
	 * org.esupportail.helpdesk.domain.beans.DeletedItem)
	 */
	@Override
	public void addDeletedItem(final DeletedItem deletedItem) {
		addObject(deletedItem);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________BOOKMARK() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getBookmarks(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Bookmark> getBookmarks(final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Bookmark.class);
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		criteria.addOrder(Order.desc(ID_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getBookmarks(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Bookmark> getBookmarks(final Ticket ticket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Bookmark.class);
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		criteria.addOrder(Order.desc(ID_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getBookmark(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public Bookmark getBookmark(
			final User user,
			final Ticket ticket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Bookmark.class);
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		List<Bookmark> bookmarks = getHibernateTemplate().findByCriteria(criteria);
		if (bookmarks.isEmpty()) {
			return null;
		}
		return bookmarks.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getBookmark(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.ArchivedTicket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public Bookmark getBookmark(
			final User user,
			final ArchivedTicket archivedTicket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Bookmark.class);
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		criteria.add(Restrictions.eq(ARCHIVED_TICKET_ATTRIBUTE, archivedTicket));
		List<Bookmark> bookmarks = getHibernateTemplate().findByCriteria(criteria);
		if (bookmarks.isEmpty()) {
			return null;
		}
		return bookmarks.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteBookmark(
	 * org.esupportail.helpdesk.domain.beans.Bookmark)
	 */
	@Override
	public void deleteBookmark(final Bookmark bookmark) {
		deleteObject(bookmark);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateBookmark(
	 * org.esupportail.helpdesk.domain.beans.Bookmark)
	 */
	@Override
	public void updateBookmark(final Bookmark bookmark) {
		updateObject(bookmark);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addBookmark(
	 * org.esupportail.helpdesk.domain.beans.Bookmark)
	 */
	@Override
	public void addBookmark(final Bookmark bookmark) {
		addObject(bookmark);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________HISTORY_ITEM() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getHistoryItems(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<HistoryItem> getHistoryItems(
			final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HistoryItem.class);
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		criteria.addOrder(Order.desc(ID_ATTRIBUTE));
		List<HistoryItem>  historyItems = getHibernateTemplate().findByCriteria(criteria);
		return historyItems;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getHistoryItems(
	 * org.esupportail.helpdesk.domain.beans.Ticket)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<HistoryItem> getHistoryItems(
			final Ticket ticket) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HistoryItem.class);
		criteria.add(Restrictions.eq(TICKET_ATTRIBUTE, ticket));
		List<HistoryItem>  historyItems = getHibernateTemplate().findByCriteria(criteria);
		return historyItems;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addHistoryItem(
	 * org.esupportail.helpdesk.domain.beans.HistoryItem)
	 */
	@Override
	public void addHistoryItem(
			final HistoryItem historyItem) {
		addObject(historyItem);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateHistoryItem(
	 * org.esupportail.helpdesk.domain.beans.HistoryItem)
	 */
	@Override
	public void updateHistoryItem(
			final HistoryItem historyItem) {
		updateObject(historyItem);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteHistoryItem(
	 * org.esupportail.helpdesk.domain.beans.HistoryItem)
	 */
	@Override
	public void deleteHistoryItem(
			final HistoryItem historyItem) {
		deleteObject(historyItem);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#clearHistoryItems(
	 * org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	public void clearHistoryItems(final User user) {
		executeUpdate(HqlUtils.deleteWhere(
				HistoryItem.class.getSimpleName() + " hi",
				HqlUtils.equals("hi.user.id", HqlUtils.quote(user.getId()))));
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#clearHistoryItems()
	 */
	@Override
	public void clearHistoryItems() {
		executeUpdate(HqlUtils.delete(
				HistoryItem.class.getSimpleName() + " hi"));
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________RESPONSE() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addResponse(org.esupportail.helpdesk.domain.beans.Response)
	 */
	@Override
	public void addResponse(
			final Response response) {
		addObject(response);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateResponse(org.esupportail.helpdesk.domain.beans.Response)
	 */
	@Override
	public void updateResponse(
			final Response response) {
		updateObject(response);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteResponse(org.esupportail.helpdesk.domain.beans.Response)
	 */
	@Override
	public void deleteResponse(
			final Response response) {
		deleteObject(response);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getUserResponses(org.esupportail.helpdesk.domain.beans.User)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Response> getUserResponses(
			final User user) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Response.class);
		criteria.add(Restrictions.eq(USER_ATTRIBUTE, user));
		criteria.addOrder(Order.asc("label"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getDepartmentResponses(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Response> getDepartmentResponses(
			final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Response.class);
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		criteria.addOrder(Order.asc("label"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getGlobalResponses()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Response> getGlobalResponses() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Response.class);
		criteria.add(Restrictions.isNull(DEPARTMENT_ATTRIBUTE));
		criteria.add(Restrictions.isNull(USER_ATTRIBUTE));
		criteria.addOrder(Order.asc("label"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________ICON() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getIcon(long)
	 */
	@Override
	@RequestCache
	public Icon getIcon(final long id) {
		return (Icon) this.getHibernateTemplate().get(Icon.class, id);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getIcons()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<Icon> getIcons() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Icon.class);
		criteria.addOrder(Order.asc("name"));
		return  getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addIcon(
	 * org.esupportail.helpdesk.domain.beans.Icon)
	 */
	@Override
	public void addIcon(final Icon icon) {
		addObject(icon);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateIcon(
	 * org.esupportail.helpdesk.domain.beans.Icon)
	 */
	@Override
	public void updateIcon(final Icon icon) {
		updateObject(icon);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteIcon(
	 * org.esupportail.helpdesk.domain.beans.Icon)
	 */
	@Override
	public void deleteIcon(final Icon icon) {
		executeUpdate(HqlUtils.updateWhere(
				Department.class.getSimpleName() + " d",
				"d.icon = NULL",
				HqlUtils.equals("d.icon.id", icon.getId())));
		executeUpdate(HqlUtils.updateWhere(
				Category.class.getSimpleName() + " c",
				"c.icon = NULL",
				HqlUtils.equals("c.icon.id", icon.getId())));
		deleteObject(icon);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getIconByName(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public Icon getIconByName(final String name) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Icon.class);
		criteria.add(Restrictions.eq("name", name));
		List<Icon> result = getHibernateTemplate().findByCriteria(criteria);
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________VERSION_MANAGER() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getVersionManager()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public VersionManager getVersionManager() {
		DetachedCriteria criteria = DetachedCriteria.forClass(VersionManager.class);
		criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		List<VersionManager> versionManagers;
		try {
			versionManagers = getHibernateTemplate().findByCriteria(criteria);
		} catch (BadSqlGrammarException e) {
			throw new UninitializedDatabaseException(
					"your database is not initialized, please run 'ant init-data'", e);
		}
		if (versionManagers.isEmpty()) {
			VersionManager versionManager = new VersionManager();
			versionManager.setVersion(VersionningUtils.VERSION_0);
			addObject(versionManager);
			return versionManager;
		}
		return versionManagers.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateVersionManager(
	 * org.esupportail.helpdesk.domain.beans.VersionManager)
	 */
	@Override
	public void updateVersionManager(final VersionManager versionManager) {
		updateObject(versionManager);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________CONFIG() {
		//
	}

	/**
	 * @return the configs of the application.
	 */
	@SuppressWarnings("unchecked")
	protected List<Config> getConfigs() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Config.class);
		criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getConfig()
	 */
	@Override
	public Config getConfig() {
		Config config;
		List<Config> configs = getConfigs();
		if (configs.isEmpty()) {
			config = new Config();
			addObject(config);
		} else {
			config = configs.get(0);
		}
		if (config.getInstallDate() == null) {
			config.setInstallDate(getOldestTicketDate());
			if (config.getInstallDate() == null) {
				config.setInstallDate(new Timestamp(System.currentTimeMillis()));
			}
			updateConfig(config);
		}
		return config;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateConfig(
	 * org.esupportail.helpdesk.domain.beans.Config)
	 */
	@Override
	public void updateConfig(final Config config) {
		updateObject(config);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________STATE() {
		//
	}

	/**
	 * @return the states of the application.
	 */
	@SuppressWarnings("unchecked")
	protected List<State> getStates() {
		DetachedCriteria criteria = DetachedCriteria.forClass(State.class);
		criteria.addOrder(Order.asc(ID_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getState()
	 */
	@Override
	public State getState() {
		List<State> states = getStates();
		if (states.isEmpty()) {
			State state = new State();
			addObject(state);
			return state;
		}
		return states.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateState(
	 * org.esupportail.helpdesk.domain.beans.State)
	 */
	@Override
	public void updateState(final State state) {
		updateObject(state);
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________STATISTICS() {
		//
	}

	/**
	 * Extract an int value from a native SQL query result.
	 * @param o
	 * @return an Integer.
	 */
	protected static Integer getNativeSqlIntegerResult(final Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof Integer) {
			return (Integer) o;
		}
		if (o instanceof Long) {
			return ((Long) o).intValue();
		}
		if (o instanceof BigInteger) {
			return ((BigInteger) o).intValue();
		}
		if (o instanceof BigDecimal) {
			return ((BigDecimal) o).intValue();
		}
		if (o instanceof Double) {
			return ((Double) o).intValue();
		}
		throw new UnsupportedOperationException(
				"can not convert SQL return type [" + o.getClass().getName() + "] to Integer");
	}

	/**
	 * Extract an int value from a native SQL query result.
	 * @param o
	 * @param defaultValue
	 * @return an Integer.
	 */
	protected static Integer getNativeSqlIntegerResult(
			final Object o,
			final Integer defaultValue) {
		if (o == null) {
			return defaultValue;
		}
		return getNativeSqlIntegerResult(o);
	}

	/**
	 * Extract a long value from a native SQL query result.
	 * @param o
	 * @return a Long.
	 */
	protected static Long getNativeSqlLongResult(final Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof Long) {
			return (Long) o;
		}
		if (o instanceof Integer) {
			return ((Integer) o).longValue();
		}
		if (o instanceof BigInteger) {
			return ((BigInteger) o).longValue();
		}
		if (o instanceof BigDecimal) {
			return ((BigDecimal) o).longValue();
		}
		if (o instanceof Double) {
			return ((Double) o).longValue();
		}
		throw new UnsupportedOperationException(
				"can not convert SQL return type [" + o.getClass().getName() + "] to Long");
	}

	/**
	 * Extract a long value from a native SQL query result.
	 * @param o
	 * @param defaultValue
	 * @return a Long.
	 */
	protected static Long getNativeSqlLongResult(
			final Object o,
			final Long defaultValue) {
		if (o == null) {
			return defaultValue;
		}
		return getNativeSqlLongResult(o);
	}

	/**
	 * Extract a string value from a native SQL query result.
	 * @param o
	 * @return a String.
	 */
	protected static String getNativeSqlStringResult(final Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof String) {
			return (String) o;
		}
		if (o instanceof Character) {
			return o.toString();
		}
		throw new UnsupportedOperationException(
				"can not convert SQL return type [" + o.getClass().getName() + "] to String");
	}

	/**
	 * Extract a string value from a native SQL query result.
	 * @param o
	 * @param defaultValue
	 * @return a String.
	 */
	protected static String getNativeSqlStringResult(
			final Object o,
			final String defaultValue) {
		if (o == null) {
			return defaultValue;
		}
		return getNativeSqlStringResult(o);
	}

	/**
	 * @param start
	 * @param end
	 * @param departments
	 * @param origins
	 * @return the condition for ticket creation statistics
	 */
	protected static String getTicketCreationsCondition(
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments,
			final List<String> origins) {
		String departmentCondition = HqlUtils.alwaysTrue();
		if (departments != null && !departments.isEmpty()) {
			List<Long> departmentIds = new ArrayList<Long>();
			for (Department department : departments) {
				departmentIds.add(department.getId());
			}
			departmentCondition = HqlUtils.longIn("crea_depa_id", departmentIds);
		}
		String originCondition = HqlUtils.alwaysTrue();
		if (origins != null && !origins.isEmpty()) {
			originCondition = HqlUtils.stringIn("orig_id", origins);
		}
    	String dateCondition = HqlUtils.and(
    			HqlUtils.ge("crea_date", HqlUtils.quote(start.toString())),
    			HqlUtils.lt("crea_date", HqlUtils.quote(end.toString())));
    	String condition = HqlUtils.and(dateCondition, departmentCondition, originCondition);
    	return condition;
	}

	/**
	 * @param statType
	 * @return the select suffix for ticket creation statistics
	 */
	protected static String getTicketCreationsSelectSuffix(
			final int statType) {
		if (statType == StatisticsExtractor.GLOBAL) {
			return "";
		}
		if (statType == StatisticsExtractor.PER_DEPARTMENT) {
			return ",crea_depa_id";
		}
		if (statType == StatisticsExtractor.PER_ORIGIN) {
			return ",orig_id";
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * @param columns
	 * @param statType
	 * @return the select clause for ticket creation statistics
	 */
	protected static String getTicketCreationsSelect(
			final String columns,
			final int statType) {
		return columns + getTicketCreationsSelectSuffix(statType);
	}

	/**
	 * @param statType
	 * @return the group by suffix for ticket creation statistics
	 */
	protected static String getTicketCreationsGroupBySuffix(
			final int statType) {
		return getTicketCreationsSelectSuffix(statType);
	}

	/**
	 * @param columns
	 * @param statType
	 * @return the group by clause for ticket creation statistics
	 */
	protected static String getTicketCreationsGroupBy(
			final String columns,
			final int statType) {
		return columns + getTicketCreationsGroupBySuffix(statType);
	}

	/**
	 * @param columns
	 * @param start
	 * @param end
	 * @param statType
	 * @param departments
	 * @param origins
	 * @return the SQL query for ticket creation statistics.
	 */
	protected static String getTicketCreationsSqlQuery(
			final String columns,
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins) {
		String select = getTicketCreationsSelect(columns, statType);
		String condition = getTicketCreationsCondition(start, end, departments, origins);
		String groupBy = getTicketCreationsGroupBy(columns, statType);
		String sqlQuery = "SELECT COUNT(*)," + select
			+ " FROM ("
			+ " SELECT id as tick_id," + select + " FROM h_tick WHERE " + condition
			+ " UNION "
			+ " SELECT tick_id," + select + " FROM h_arch_tick WHERE " + condition
			+ ") AS x"
			+ " GROUP BY " + groupBy;
    	return sqlQuery;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketCreationsByYear(
	 * java.sql.Timestamp, java.sql.Timestamp, int, java.util.List, java.util.List)
	 */
	@Override
	public List<YearTicketCreationStatistic> getTicketCreationsByYear(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins) {
    	String hqlQuery = getTicketCreationsSqlQuery(
    			"crea_year", start, end, statType, departments, origins);
    	SQLQuery query = getSqlQuery(hqlQuery);
    	List<YearTicketCreationStatistic> result = new ArrayList<YearTicketCreationStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer year = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Department department = null;
    		if (statType == StatisticsExtractor.PER_DEPARTMENT) {
        		Long departmentId = ((BigInteger) dataValues[fieldNum++]).longValue();
        		department = getDepartment(departmentId);
    		}
    		String origin = null;
    		if (statType == StatisticsExtractor.PER_ORIGIN) {
        		origin = (String) dataValues[fieldNum++];
    		}
    		result.add(new YearTicketCreationStatistic(
    				StatisticsUtils.getYearDate(year), department, origin, number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketCreationsByMonth(
	 * java.sql.Timestamp, java.sql.Timestamp, int, java.util.List, java.util.List)
	 */
	@Override
	public List<MonthTicketCreationStatistic> getTicketCreationsByMonth(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins) {
    	String hqlQuery = getTicketCreationsSqlQuery(
    			"crea_year,crea_month", start, end, statType, departments, origins);
    	SQLQuery query = getSqlQuery(hqlQuery);
    	List<MonthTicketCreationStatistic> result = new ArrayList<MonthTicketCreationStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer year = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer month = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Department department = null;
    		if (statType == StatisticsExtractor.PER_DEPARTMENT) {
        		Long departmentId = getNativeSqlLongResult(dataValues[fieldNum++]);
        		department = getDepartment(departmentId);
    		}
    		String origin = null;
    		if (statType == StatisticsExtractor.PER_ORIGIN) {
        		origin = getNativeSqlStringResult(dataValues[fieldNum++]);
    		}
    		result.add(new MonthTicketCreationStatistic(
    				StatisticsUtils.getMonthDate(year, month), department, origin, number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketCreationsByDay(
	 * java.sql.Timestamp, java.sql.Timestamp, int, java.util.List, java.util.List)
	 */
	@Override
	public List<DayTicketCreationStatistic> getTicketCreationsByDay(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins) {
    	String hqlQuery = getTicketCreationsSqlQuery(
    			"crea_year,crea_month,crea_day", start, end, statType, departments, origins);
    	SQLQuery query = getSqlQuery(hqlQuery);
    	List<DayTicketCreationStatistic> result = new ArrayList<DayTicketCreationStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer year = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer month = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer dayOfMonth = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Department department = null;
    		if (statType == StatisticsExtractor.PER_DEPARTMENT) {
        		Long departmentId = getNativeSqlLongResult(dataValues[fieldNum++]);
        		department = getDepartment(departmentId);
    		}
    		String origin = null;
    		if (statType == StatisticsExtractor.PER_ORIGIN) {
        		origin = getNativeSqlStringResult(dataValues[fieldNum++]);
    		}
    		result.add(new DayTicketCreationStatistic(
    				StatisticsUtils.getDayDate(year, month, dayOfMonth), department, origin, number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketCreationsByDayOfWeek(
	 * java.sql.Timestamp, java.sql.Timestamp, int, java.util.List, java.util.List)
	 */
	@Override
	public List<DayOfWeekTicketCreationStatistic> getTicketCreationsByDayOfWeek(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins) {
    	String hqlQuery = getTicketCreationsSqlQuery(
    			"crea_dow",
    			start, end, statType, departments, origins);
    	SQLQuery query = getSqlQuery(hqlQuery);
    	List<DayOfWeekTicketCreationStatistic> result = new ArrayList<DayOfWeekTicketCreationStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer dayOfWeek = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Department department = null;
    		if (statType == StatisticsExtractor.PER_DEPARTMENT) {
        		Long departmentId = getNativeSqlLongResult(dataValues[fieldNum++]);
        		department = getDepartment(departmentId);
    		}
    		String origin = null;
    		if (statType == StatisticsExtractor.PER_ORIGIN) {
        		origin = getNativeSqlStringResult(dataValues[fieldNum++]);
    		}
    		result.add(new DayOfWeekTicketCreationStatistic(dayOfWeek, department, origin, number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketCreationsByHourOfDay(
	 * java.sql.Timestamp, java.sql.Timestamp, int, java.util.List, java.util.List)
	 */
	@Override
	public List<HourOfDayTicketCreationStatistic> getTicketCreationsByHourOfDay(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins) {
    	String hqlQuery = getTicketCreationsSqlQuery(
    			"crea_hour", start, end, statType, departments, origins);
    	SQLQuery query = getSqlQuery(hqlQuery);
    	List<HourOfDayTicketCreationStatistic> result = new ArrayList<HourOfDayTicketCreationStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer hourOfDay = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Department department = null;
    		if (statType == StatisticsExtractor.PER_DEPARTMENT) {
        		Long departmentId = getNativeSqlLongResult(dataValues[fieldNum++]);
        		department = getDepartment(departmentId);
    		}
    		String origin = null;
    		if (statType == StatisticsExtractor.PER_ORIGIN) {
        		origin = getNativeSqlStringResult(dataValues[fieldNum++]);
    		}
    		result.add(new HourOfDayTicketCreationStatistic(hourOfDay, department, origin, number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketCreationsByHourOfWeek(
	 * java.sql.Timestamp, java.sql.Timestamp, int, java.util.List, java.util.List)
	 */
	@Override
	public List<HourOfWeekTicketCreationStatistic> getTicketCreationsByHourOfWeek(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins) {
    	String hqlQuery = getTicketCreationsSqlQuery(
    			"crea_dow,crea_hour", start, end, statType, departments, origins);
    	SQLQuery query = getSqlQuery(hqlQuery);
    	List<HourOfWeekTicketCreationStatistic> result = new ArrayList<HourOfWeekTicketCreationStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer dayOfWeek = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer hourOfDay = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Department department = null;
    		if (statType == StatisticsExtractor.PER_DEPARTMENT) {
        		Long departmentId = getNativeSqlLongResult(dataValues[fieldNum++]);
        		department = getDepartment(departmentId);
    		}
    		String origin = null;
    		if (statType == StatisticsExtractor.PER_ORIGIN) {
        		origin = (String) dataValues[fieldNum++];
    		}
    		result.add(new HourOfWeekTicketCreationStatistic(dayOfWeek, hourOfDay, department, origin, number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketCreationDepartments(
	 * java.sql.Timestamp, java.sql.Timestamp, java.util.List)
	 */
	@Override
	public List<Department> getTicketCreationDepartments(
			final Timestamp start,
			final Timestamp end,
			final List<String> origins) {
		String condition = getTicketCreationsCondition(start, end, null, origins);
		String sqlQuery = "SELECT crea_depa_id"
		+ " FROM ("
		+ " SELECT crea_depa_id FROM h_tick WHERE " + condition
		+ " UNION "
		+ " SELECT crea_depa_id FROM h_arch_tick WHERE " + condition
		+ ")";
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<Department> result = new ArrayList<Department>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Long number = getNativeSqlLongResult(dataValues[fieldNum++]);
    		result.add(getDepartment(number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketCreationOrigins(
	 * java.sql.Timestamp, java.sql.Timestamp, java.util.List)
	 */
	@Override
	public List<String> getTicketCreationOrigins(
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
		String condition = getTicketCreationsCondition(start, end, departments, null);
		String sqlQuery = "SELECT orig_id"
		+ " FROM ("
		+ " SELECT orig_id FROM h_tick WHERE " + condition
		+ " UNION "
		+ " SELECT orig_id FROM h_arch_tick WHERE " + condition
		+ ")";
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<String> result = new ArrayList<String>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		String origin = getNativeSqlStringResult(dataValues[fieldNum++]);
    		result.add(origin);
    	}
    	return result;
	}

	/**
	 * @param start
	 * @param end
	 * @param departments
	 * @return the condition for user ticket creation statistics
	 */
	protected static String getUserTicketCreationsCondition(
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
		String departmentCondition = HqlUtils.alwaysTrue();
		if (departments != null && !departments.isEmpty()) {
			List<Long> departmentIds = new ArrayList<Long>();
			for (Department department : departments) {
				departmentIds.add(department.getId());
			}
			departmentCondition = HqlUtils.longIn("depa_id", departmentIds);
		}
    	String dateCondition = HqlUtils.and(
    			HqlUtils.ge("crea_date", HqlUtils.quote(start.toString())),
    			HqlUtils.lt("crea_date", HqlUtils.quote(end.toString())));
    	String condition = HqlUtils.and(dateCondition, departmentCondition);
    	return condition;
	}

	/**
	 * @param start
	 * @param end
	 * @param departments
	 * @return the SQL query for ticket creation statistics.
	 */
	protected static String getUserTicketCreationsSqlQuery(
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
		String condition = getUserTicketCreationsCondition(start, end, departments);
		String sqlQuery = "SELECT COUNT(*) AS number,u.id,u.disp_name"
			+ " FROM ("
			+ " SELECT id AS tick_id,creator_id FROM h_tick WHERE " + condition
			+ " UNION "
			+ " SELECT tick_id,creator_id FROM h_arch_tick WHERE " + condition
			+ ") AS t, h_user u WHERE t.creator_id = u.id"
			+ " GROUP BY u.id,u.disp_name"
			+ " ORDER BY number DESC, u.disp_name";
    	return sqlQuery;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getUserTicketCreations(
	 * java.sql.Timestamp, java.sql.Timestamp, java.util.List, int)
	 */
	@Override
	public List<UserTicketCreationStatisticEntry> getUserTicketCreations(
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments,
			final int maxEntries) {
    	String sqlQuery = getUserTicketCreationsSqlQuery(
    			start, end, departments);
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<UserTicketCreationStatisticEntry> result = new ArrayList<UserTicketCreationStatisticEntry>();
    	int previousNumber = 0;
    	int index = 0;
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		if (index > maxEntries && number != previousNumber) {
    			break;
    		}
    		int rank;
    		if (previousNumber == number) {
    			rank = -1;
    		} else {
    			rank = index;
    		}
    		String userId = getNativeSqlStringResult(dataValues[fieldNum++]);
    		result.add(new UserTicketCreationStatisticEntry(rank, getUser(userId), number));
    		previousNumber = number;
    		index++;
    	}
    	return result;
	}

	/**
	 * @param start
	 * @param end
	 * @param departments
	 * @return the condition for charge time statistics
	 */
	protected static String getChargeOrClosureTimeCondition(
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
		return getUserTicketCreationsCondition(start, end, departments);
	}

	/**
	 * @param columns
	 * @param start
	 * @param end
	 * @param departments
	 * @return the SQL query for charge time statistics.
	 */
	protected static String getChargeTimeSqlQuery(
			final String columns,
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
		String condition = getChargeOrClosureTimeCondition(start, end, departments);
		String sqlQuery = "SELECT MIN(char_time),ROUND(AVG(char_time)),MAX(char_time),count(*)," + columns
			+ " FROM ("
			+ " SELECT id as tick_id,char_time," + columns + " FROM h_tick WHERE "
			+ HqlUtils.and(condition, "char_time IS NOT NULL")
			+ " UNION "
			+ " SELECT id as tick_id,TIME_TO_SEC(TIMEDIFF(NOW(),crea_date)) AS char_time,"
			+ columns + " FROM h_tick WHERE "
			+ HqlUtils.and(condition, "char_time IS NULL", "clos_time IS NULL")
			+ " UNION "
			+ " SELECT id as tick_id,clos_time AS char_time," + columns + " FROM h_tick WHERE "
			+ HqlUtils.and(condition, "char_time IS NULL", "clos_time IS NULL")
			+ " UNION "
			+ " SELECT tick_id,char_time," + columns + " FROM h_arch_tick WHERE "
			+ HqlUtils.and(condition, "char_time IS NOT NULL")
			+ " UNION "
			+ " SELECT tick_id, clos_time AS char_time," + columns + " FROM h_arch_tick WHERE "
			+ HqlUtils.and(condition, "char_time IS NULL")
			+ ") AS t"
			+ " GROUP BY " + columns;
    	return sqlQuery;
	}

	/**
	 * @param columns
	 * @param start
	 * @param end
	 * @param departments
	 * @return the SQL query for closure timestatistics.
	 */
	protected static String getClosureTimeSqlQuery(
			final String columns,
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
		String condition = getChargeOrClosureTimeCondition(start, end, departments);
		String sqlQuery = "SELECT MIN(clos_time),ROUND(AVG(clos_time)),MAX(clos_time),count(*)," + columns
			+ " FROM ("
			+ " SELECT id as tick_id,clos_time," + columns + " FROM h_tick WHERE "
			+ HqlUtils.and(condition, "clos_time IS NOT NULL")
			+ " UNION "
			+ " SELECT id as tick_id,TIME_TO_SEC(TIMEDIFF(NOW(),crea_date)) AS clos_time,"
			+ columns + " FROM h_tick WHERE "
			+ HqlUtils.and(condition, "clos_time IS NULL")
			+ " UNION "
			+ " SELECT tick_id,clos_time," + columns + " FROM h_arch_tick WHERE " + condition
			+ ") AS t"
			+ " GROUP BY " + columns;
    	return sqlQuery;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getChargeOrClosureTimeByYear(
	 * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
	 */
	@Override
	public List<YearTimeStatistic> getChargeOrClosureTimeByYear(
			final boolean charge,
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
    	String sqlQuery;
    	if (charge) {
    		sqlQuery = getChargeTimeSqlQuery(
        			"crea_year", start, end, departments);
    	} else {
    		sqlQuery = getClosureTimeSqlQuery(
        			"crea_year", start, end, departments);
    	}
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<YearTimeStatistic> result = new ArrayList<YearTimeStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer min = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer avg = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer max = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer year = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		result.add(new YearTimeStatistic(
    				StatisticsUtils.getYearDate(year), min, avg, max, number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getChargeOrClosureTimeByMonth(
	 * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
	 */
	@Override
	public List<MonthTimeStatistic> getChargeOrClosureTimeByMonth(
			final boolean charge,
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
    	String sqlQuery;
    	if (charge) {
    		sqlQuery = getChargeTimeSqlQuery(
    				"crea_year,crea_month", start, end, departments);
    	} else {
    		sqlQuery = getClosureTimeSqlQuery(
    				"crea_year,crea_month", start, end, departments);
    	}
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<MonthTimeStatistic> result = new ArrayList<MonthTimeStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer min = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer avg = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer max = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer year = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer month = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		result.add(new MonthTimeStatistic(
    				StatisticsUtils.getMonthDate(year, month), min, avg, max, number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getChargeOrClosureTimeByDay(
	 * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
	 */
	@Override
	public List<DayTimeStatistic> getChargeOrClosureTimeByDay(
			final boolean charge,
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
    	String sqlQuery;
    	if (charge) {
    		sqlQuery = getChargeTimeSqlQuery(
    				"crea_year,crea_month,crea_day", start, end, departments);
    	} else {
    		sqlQuery = getClosureTimeSqlQuery(
    				"crea_year,crea_month,crea_day", start, end, departments);
    	}
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<DayTimeStatistic> result = new ArrayList<DayTimeStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer min = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer avg = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer max = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer year = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer month = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer day = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		result.add(new DayTimeStatistic(StatisticsUtils.getDayDate(year, month, day), min, avg, max, number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getChargeOrClosureTimeByDayOfWeek(
	 * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
	 */
	@Override
	public List<DayOfWeekTimeStatistic> getChargeOrClosureTimeByDayOfWeek(
			final boolean charge,
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
    	String sqlQuery;
    	if (charge) {
    		sqlQuery = getChargeTimeSqlQuery(
    				"crea_dow", start, end, departments);
    	} else {
    		sqlQuery = getClosureTimeSqlQuery(
    				"crea_dow", start, end, departments);
    	}
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<DayOfWeekTimeStatistic> result = new ArrayList<DayOfWeekTimeStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer min = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer avg = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer max = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer dayOfWeek = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		result.add(new DayOfWeekTimeStatistic(dayOfWeek, min, avg, max, number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getChargeOrClosureTimeByHourOfDay(
	 * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
	 */
	@Override
	public List<HourOfDayTimeStatistic> getChargeOrClosureTimeByHourOfDay(
			final boolean charge,
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
    	String sqlQuery;
    	if (charge) {
    		sqlQuery = getChargeTimeSqlQuery(
    				"crea_hour", start, end, departments);
    	} else {
    		sqlQuery = getClosureTimeSqlQuery(
    				"crea_hour", start, end, departments);
    	}
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<HourOfDayTimeStatistic> result = new ArrayList<HourOfDayTimeStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer min = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer avg = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer max = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer hourOfDay = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		result.add(new HourOfDayTimeStatistic(hourOfDay, min, avg, max, number));
    	}
    	return result;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getChargeOrClosureTimeByHourOfWeek(
	 * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List)
	 */
	@Override
	public List<HourOfWeekTimeStatistic> getChargeOrClosureTimeByHourOfWeek(
			final boolean charge,
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
    	String sqlQuery;
    	if (charge) {
    		sqlQuery = getChargeTimeSqlQuery(
    				"crea_dow,crea_hour", start, end, departments);
    	} else {
    		sqlQuery = getClosureTimeSqlQuery(
    				"crea_dow,crea_hour", start, end, departments);
    	}
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<HourOfWeekTimeStatistic> result = new ArrayList<HourOfWeekTimeStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer min = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer avg = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer max = getNativeSqlIntegerResult(dataValues[fieldNum++], 0);
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer dayOfWeek = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Integer hourOfDay = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		result.add(new HourOfWeekTimeStatistic(dayOfWeek, hourOfDay, min, avg, max, number));
    	}
    	return result;
	}

	/**
	 * @param start
	 * @param end
	 * @param departments
	 * @param maxEntries
	 * @param hideCharged
	 * @return the SQL query for longest charge times.
	 */
	protected static String getLongestChargeTimeSqlQuery(
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments,
			final int maxEntries,
			final boolean hideCharged) {
		String condition = getChargeOrClosureTimeCondition(start, end, departments);
		String sqlQuery = "SELECT tick_id, char_time FROM ("
			+ " SELECT id as tick_id,TIME_TO_SEC(TIMEDIFF(NOW(),crea_date)) AS char_time"
			+ " FROM h_tick WHERE "
			+ HqlUtils.and(condition, "char_time IS NULL", "clos_time IS NULL");
		if (!hideCharged) {
			sqlQuery += " UNION "
				+ " SELECT id as tick_id,clos_time AS char_time FROM h_tick WHERE "
				+ HqlUtils.and(condition, "char_time IS NULL", "clos_time IS NOT NULL")
				+ " UNION "
				+ " SELECT id as tick_id,char_time FROM h_tick WHERE "
				+ HqlUtils.and(condition, "char_time IS NOT NULL")
				+ " UNION "
				+ " SELECT tick_id,char_time FROM h_arch_tick WHERE "
				+ HqlUtils.and(condition, "char_time IS NOT NULL")
				+ " UNION "
				+ " SELECT tick_id, clos_time AS char_time FROM h_arch_tick WHERE "
				+ HqlUtils.and(condition, "char_time IS NULL");
		}
		sqlQuery += ") AS t"
			+ " ORDER BY char_time DESC LIMIT " + maxEntries;
    	return sqlQuery;
	}

	/**
	 * @param start
	 * @param end
	 * @param departments
	 * @param maxEntries
	 * @param hideClosed
	 * @return the SQL query for longest closure times.
	 */
	protected static String getLongestClosureTimeSqlQuery(
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments,
			final int maxEntries,
			final boolean hideClosed) {
		String condition = getChargeOrClosureTimeCondition(start, end, departments);
		String sqlQuery = "SELECT tick_id, clos_time FROM ("
            + " SELECT id as tick_id,TIME_TO_SEC(TIMEDIFF(NOW(),crea_date)) AS clos_time FROM h_tick WHERE "
            + HqlUtils.and(condition, "clos_time IS NULL");
		if (!hideClosed) {
			sqlQuery += " UNION "
				+ " SELECT id as tick_id,clos_time FROM h_tick WHERE "
				+ HqlUtils.and(condition, "clos_time IS NOT NULL")
				+ " UNION "
				+ " SELECT tick_id,clos_time FROM h_arch_tick WHERE " + condition;
		}
		sqlQuery += ") AS t"
			+ " ORDER BY clos_time DESC LIMIT " + maxEntries;
    	return sqlQuery;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketsWithLongChargeOrClosureTime(
	 * boolean, java.sql.Timestamp, java.sql.Timestamp, java.util.List, int, boolean)
	 */
	@Override
	public List<StatisticsTicketEntry> getTicketsWithLongChargeOrClosureTime(
			final boolean charge,
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments,
			final int maxEntries,
			final boolean hideChargedOrClosed) {
    	String sqlQuery;
    	if (charge) {
    		sqlQuery = getLongestChargeTimeSqlQuery(start, end, departments, maxEntries, hideChargedOrClosed);
    	} else {
    		sqlQuery = getLongestClosureTimeSqlQuery(start, end, departments, maxEntries, hideChargedOrClosed);
    	}
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<StatisticsTicketEntry> result = new ArrayList<StatisticsTicketEntry>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Long id = getNativeSqlLongResult(dataValues[fieldNum++]);
    		StatisticsTicketEntry ticketEntry;
    		Ticket ticket = getTicket(id);
    		if (ticket != null) {
    			ticketEntry = new StatisticsTicketEntry(ticket);
    		} else {
    			ArchivedTicket archivedTicket = getArchivedTicketByOriginalId(id);
    			if (archivedTicket == null) {
    				throw new TicketNotFoundException("no ticket found with id [" + id + "]");
    			}
    			ticketEntry = new StatisticsTicketEntry(archivedTicket);
    		}
    		Integer time = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		if (charge) {
    			ticketEntry.setChargeTime(time);
    		} else {
    			ticketEntry.setClosureTime(time);
    		}
			result.add(ticketEntry);
    	}
    	return result;
	}

	/**
	 * @param start
	 * @param end
	 * @param departments
	 * @return the condition for status statistics
	 */
	protected static String getStatusCondition(
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
		String departmentCondition = HqlUtils.alwaysTrue();
		if (departments != null && !departments.isEmpty()) {
			List<Long> departmentIds = new ArrayList<Long>();
			for (Department department : departments) {
				departmentIds.add(department.getId());
			}
			departmentCondition = HqlUtils.longIn("depa_id", departmentIds);
		}
    	String dateCondition = HqlUtils.and(
    			HqlUtils.ge("crea_date", HqlUtils.quote(start.toString())),
    			HqlUtils.lt("crea_date", HqlUtils.quote(end.toString())));
    	String condition = HqlUtils.and(dateCondition, departmentCondition);
    	return condition;
	}

	/**
	 * @param start
	 * @param end
	 * @param statType
	 * @param departments
	 * @param ignoreArchivedTickets
	 * @return the SQL query for status statistics.
	 */
	protected static String getStatusStatisticsSqlQuery(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final boolean ignoreArchivedTickets) {
		String columns = "";
		if (statType == StatisticsExtractor.PER_DEPARTMENT) {
			columns += ",depa_id";
		}
		String condition = getStatusCondition(start, end, departments);
		String sqlQuery;
		if (ignoreArchivedTickets) {
			sqlQuery = "SELECT COUNT(*),stat" + columns
			+ " FROM h_tick WHERE " + condition
			+ " GROUP BY stat" + columns;
		} else {
			sqlQuery = "SELECT COUNT(*),stat" + columns
			+ " FROM ("
			+ " SELECT id as tick_id,stat" + columns + " FROM h_tick WHERE " + condition
			+ " UNION "
			+ " SELECT tick_id,'ARCHIVED' AS stat" + columns + " FROM h_arch_tick WHERE " + condition
			+ ") AS x"
			+ " GROUP BY stat" + columns;
		}
    	return sqlQuery;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getStatusStatistics(
	 * java.sql.Timestamp, java.sql.Timestamp, int, java.util.List, boolean)
	 */
	@Override
	public List<StatusStatistic> getStatusStatistics(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final boolean ignoreArchivedTickets) {
    	String sqlQuery = getStatusStatisticsSqlQuery(
    			start, end, statType, departments, ignoreArchivedTickets);
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<StatusStatistic> result = new ArrayList<StatusStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		String status = getNativeSqlStringResult(dataValues[fieldNum++]);
    		Long departmentId = null;
    		if (statType == StatisticsExtractor.PER_DEPARTMENT) {
    			departmentId = getNativeSqlLongResult(dataValues[fieldNum++]);
    		}
    		result.add(new StatusStatistic(departmentId, status, number));
    	}
    	return result;
	}

	/**
	 * @param start
	 * @param end
	 * @param departments
	 * @param zero
	 * @return the condition for spent time statistics
	 */
	protected static String getSpentTimeCondition(
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments,
			final boolean zero) {
		List<Long> neededDepartmentIds = new ArrayList<Long>();
		List<Long> notNeededDepartmentIds = new ArrayList<Long>();
		if (departments != null && !departments.isEmpty()) {
			for (Department department : departments) {
				if (department.isSpentTimeNeeded()) {
					neededDepartmentIds.add(department.getId());
				} else {
					notNeededDepartmentIds.add(department.getId());
				}
			}
		}
    	String dateCondition = HqlUtils.and(
    			HqlUtils.ge("crea_date", HqlUtils.quote(start.toString())),
    			HqlUtils.lt("crea_date", HqlUtils.quote(end.toString())));
    	String condition;
    	if (zero) {
    		condition = HqlUtils.or(
					HqlUtils.longIn("depa_id", notNeededDepartmentIds),
    				"spen_time <= 0");
    	} else {
    		condition = HqlUtils.and(
    				HqlUtils.longIn("depa_id", neededDepartmentIds),
    				"spen_time > 0");
    	}
		return HqlUtils.and(dateCondition, condition);
	}

	/**
	 * @param start
	 * @param end
	 * @param departments
	 * @return the SQL query for status statistics.
	 */
	protected static String getSpentTimeStatisticsSqlQuery(
			final Timestamp start,
			final Timestamp end,
			final List<Department> departments) {
		String sqlQuery = "SELECT"
			+ " crea_depa_id,depa_id,COUNT(*) AS number,SUM(spen_time) AS tota_spen_time"
			+ " FROM ("
			+ HqlUtils.selectFromWhere(
					"id AS tick_id,crea_depa_id,depa_id,spen_time",
					"h_tick",
					getSpentTimeCondition(start, end, departments, false))
			+ " UNION "
			+ HqlUtils.selectFromWhere(
					"id AS tick_id,crea_depa_id,depa_id,0 AS spen_time",
					"h_tick",
					getSpentTimeCondition(start, end, departments, true))
			+ " UNION "
			+ HqlUtils.selectFromWhere(
					"tick_id,crea_depa_id,depa_id,spen_time",
					"h_arch_tick",
					getSpentTimeCondition(start, end, departments, false))
			+ " UNION "
			+ HqlUtils.selectFromWhere(
					"tick_id,crea_depa_id,depa_id,0 AS spen_time",
					"h_arch_tick",
					getSpentTimeCondition(start, end, departments, true))
			+ ") AS x"
			+ " GROUP BY depa_id,crea_depa_id"
			+ " ORDER BY depa_id,tota_spen_time,number";
    	return sqlQuery;
	}

    /**
     * @see org.esupportail.helpdesk.dao.DaoService#getSpentTimeStatistics(
     * java.sql.Timestamp, java.sql.Timestamp, java.util.List)
     */
    @Override
	public List<SpentTimeStatistic> getSpentTimeStatistics(
    		final Timestamp start,
    		final Timestamp end,
    		final List<Department> departments) {
    	String sqlQuery = getSpentTimeStatisticsSqlQuery(start, end, departments);
    	SQLQuery query = getSqlQuery(sqlQuery);
    	List<SpentTimeStatistic> result = new ArrayList<SpentTimeStatistic>();
    	for (Object data : query.list()) {
    		Object[] dataValues = (Object[]) data;
    		int fieldNum = 0;
    		Long creationDepartmentId = getNativeSqlLongResult(dataValues[fieldNum++]);
    		Long finalDepartmentId = getNativeSqlLongResult(dataValues[fieldNum++]);
    		Integer number = getNativeSqlIntegerResult(dataValues[fieldNum++]);
    		Long spentTime = getNativeSqlLongResult(dataValues[fieldNum++]);
    		result.add(new SpentTimeStatistic(creationDepartmentId, finalDepartmentId, number, spentTime));
    	}
    	return result;
    }

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________DEPARTMENT_SELECTION_CONFIG() {
		//
	}

    /**
     * @see org.esupportail.helpdesk.dao.DaoService#getLatestDepartmentSelectionConfig()
     */
    @Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public DepartmentSelectionConfig getLatestDepartmentSelectionConfig() {
		DetachedCriteria criteria = DetachedCriteria.forClass(DepartmentSelectionConfig.class);
		criteria.addOrder(Order.desc("id"));
		List<DepartmentSelectionConfig> configs = getHibernateTemplate().findByCriteria(criteria);
		if (configs.isEmpty()) {
			return null;
		}
		return configs.get(0);
    }

    /**
     * @see org.esupportail.helpdesk.dao.DaoService#addDepartmentSelectionConfig(
     * org.esupportail.helpdesk.domain.beans.DepartmentSelectionConfig)
     */
    @Override
	public void addDepartmentSelectionConfig(final DepartmentSelectionConfig config) {
    	addObject(config);
    }

    /** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________FAQ_LINK() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaqLinks(org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<FaqLink> getFaqLinks(final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FaqLink.class);
		criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaqLinks(org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@RequestCache
	public List<FaqLink> getFaqLinks(final Category category) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FaqLink.class);
		criteria.add(Restrictions.eq(CATEGORY_ATTRIBUTE, category));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaqLinksNumber(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@RequestCache
	public int getFaqLinksNumber(final Department department)  {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				FaqLink.class.getSimpleName(),
				HqlUtils.equals("department.id", department.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaqLinksNumber(
	 * org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@RequestCache
	public int getFaqLinksNumber(final Category category) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				FaqLink.class.getSimpleName(),
				HqlUtils.equals("category.id", category.getId()));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaqLinkByOrder(
	 * org.esupportail.helpdesk.domain.beans.Department, org.esupportail.helpdesk.domain.beans.Category, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public FaqLink getFaqLinkByOrder(
			final Department department,
			final Category category,
			final int order) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FaqLink.class);
		if (department != null) {
			criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		}
		if (category != null) {
			criteria.add(Restrictions.eq(CATEGORY_ATTRIBUTE, category));
		}
		criteria.add(Restrictions.eq(ORDER_ATTRIBUTE, new Integer(order)));
		List<FaqLink> faqLinks = getHibernateTemplate().findByCriteria(criteria);
		if (faqLinks.isEmpty()) {
			return null;
		}
		return faqLinks.get(0);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addFaqLink(org.esupportail.helpdesk.domain.beans.FaqLink)
	 */
	@Override
	public void addFaqLink(final FaqLink faqLink) {
		addObject(faqLink);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateFaqLink(org.esupportail.helpdesk.domain.beans.FaqLink)
	 */
	@Override
	public void updateFaqLink(final FaqLink faqLink) {
		updateObject(faqLink);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteFaqLink(org.esupportail.helpdesk.domain.beans.FaqLink)
	 */
	@Override
	public void deleteFaqLink(final FaqLink faqLink) {
		deleteObject(faqLink);
	}

    /** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________MISC() {
		//
	}

	/**
	 * @return the fileManager
	 */
	protected FileManager getFileManager() {
		return fileManager;
	}

	/**
	 * @param fileManager the fileManager to set
	 */
	public void setFileManager(final FileManager fileManager) {
		this.fileManager = fileManager;
	}

	/**
	 * @param jdbcUrl the jdbcUrl to set
	 */
	public void setJdbcUrl(final String jdbcUrl) {
		if (jdbcUrl.startsWith("jdbc:mysql")) {
			databaseType = DATABASE_TYPE_MYSQL;
		} else if (jdbcUrl.startsWith("jdbc:postgresql")) {
			databaseType = DATABASE_TYPE_POSTGRES;
		} else {
			throw new ConfigException("unknown database type for JDBC URL [" + jdbcUrl + "]");
		}
	}

	/** Eclipse outline delimiter. */
	@SuppressWarnings("unused")
	private void _______________DEPRECATED() {
		//
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOldTicketTemplates(
	 * org.esupportail.helpdesk.domain.beans.Category)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<OldTicketTemplate> getOldTicketTemplates(final Category category) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OldTicketTemplate.class);
		criteria.add(Restrictions.eq(CATEGORY_ATTRIBUTE, category));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteOldTicketTemplate(
	 * org.esupportail.helpdesk.domain.beans.OldTicketTemplate)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteOldTicketTemplate(final OldTicketTemplate oldTicketTemplate) {
		deleteObject(oldTicketTemplate);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOldFaqParts(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<OldFaqPart> getOldFaqParts(final DeprecatedFaqContainer faqContainer) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OldFaqPart.class);
		criteria.add(Restrictions.eq(PARENT_ATTRIBUTE, faqContainer));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteOldFaqPart(
	 * org.esupportail.helpdesk.domain.beans.OldFaqPart)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteOldFaqPart(final OldFaqPart oldFaqPart) {
		deleteObject(oldFaqPart);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOldFaqEntries(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<OldFaqEntry> getOldFaqEntries(final DeprecatedFaqContainer faqContainer) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OldFaqEntry.class);
		criteria.add(Restrictions.eq(PARENT_ATTRIBUTE, faqContainer));
		criteria.add(Restrictions.isNull(OLD_FAQ_PART_ATTRIBUTE));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOldFaqEntries(
	 * org.esupportail.helpdesk.domain.beans.OldFaqPart)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<OldFaqEntry> getOldFaqEntries(final OldFaqPart oldFaqPart) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OldFaqEntry.class);
		criteria.add(Restrictions.eq(OLD_FAQ_PART_ATTRIBUTE, oldFaqPart));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteOldFaqEntry(
	 * org.esupportail.helpdesk.domain.beans.OldFaqEntry)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteOldFaqEntry(final OldFaqEntry oldFaqEntry) {
		deleteObject(oldFaqEntry);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketsConnectedToOldFaqEntry(
	 * org.esupportail.helpdesk.domain.beans.OldFaqEntry)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<Ticket> getTicketsConnectedToOldFaqEntry(final OldFaqEntry oldFaqEntry) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Ticket.class);
		criteria.add(Restrictions.eq("connectionOldFaqEntry.id", oldFaqEntry.getId()));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getTicketsConnectedToOldFaqPart(
	 * org.esupportail.helpdesk.domain.beans.OldFaqPart)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<Ticket> getTicketsConnectedToOldFaqPart(final OldFaqPart oldFaqPart) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Ticket.class);
		criteria.add(Restrictions.eq("connectionOldFaqPart.id", oldFaqPart.getId()));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOrphenTickets(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<Ticket> getOrphenTickets(final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Ticket.class);
		criteria.add(Restrictions.eq("department.id", department.getId()));
		criteria.add(Restrictions.isNull("category"));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOrphenTicketsNumber(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public int getOrphenTicketsNumber(final Department department) {
		String queryStr = HqlUtils.selectCountAllFromWhere(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				HqlUtils.and(
						HqlUtils.equals("department.id", department.getId()),
						HqlUtils.isNull("category")));
		return getQueryIntResult(queryStr);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getActionsConnectedToOldFaqEntry(
	 * org.esupportail.helpdesk.domain.beans.OldFaqEntry)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<Action> getActionsConnectedToOldFaqEntry(final OldFaqEntry oldFaqEntry) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Action.class);
		criteria.add(Restrictions.eq("oldFaqEntryConnectionAfter.id", oldFaqEntry.getId()));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getActionsConnectedToOldFaqPart(
	 * org.esupportail.helpdesk.domain.beans.OldFaqPart)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<Action> getActionsConnectedToOldFaqPart(final OldFaqPart oldFaqPart) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Action.class);
		criteria.add(Restrictions.eq("oldFaqPartConnectionAfter.id", oldFaqPart.getId()));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getV2ActionsToUpgradeToV3(long, int)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<Action> getV2ActionsToUpgradeToV3(
			final long startIndex,
			final int num) {
		String actionCondition1 = HqlUtils.or(
				HqlUtils.like("action.message", HqlUtils.quote("%&%")),
				HqlUtils.like("action.labelBefore", HqlUtils.quote("%&%")),
				HqlUtils.like("action.labelAfter", HqlUtils.quote("%&%")));
		String actionCondition2 = HqlUtils.or(
				HqlUtils.equals(
						"action.actionType",
						HqlUtils.quote(ActionType.CONNECT_TO_TICKET_V2)),
				HqlUtils.equals(
						"action.actionType",
						HqlUtils.quote(ActionType.CONNECT_TO_TICKET_APPROVE_V2)),
				HqlUtils.equals(
						"action.actionType",
						HqlUtils.quote(ActionType.CONNECT_TO_FAQ_V2)),
				HqlUtils.equals(
						"action.actionType",
						HqlUtils.quote(ActionType.CONNECT_TO_FAQ_APPROVE_V2)));
		String actionCondition3 = HqlUtils.or(
				HqlUtils.isNotNull("action.oldConnectionAfter"),
				HqlUtils.isNotNull("action.oldFaqPartConnectionAfter"),
				HqlUtils.isNotNull("action.oldFaqEntryConnectionAfter"));
		String actionCondition = HqlUtils.or(
				actionCondition1,
				actionCondition2,
				actionCondition3);
		Query query = getQuery(HqlUtils.fromWhereOrderBy(
				Action.class.getSimpleName() + HqlUtils.AS_KEYWORD + "action",
				HqlUtils.and(
						HqlUtils.ge("action.id", startIndex),
						actionCondition
						),
				"action.id"));
		query.setMaxResults(num);
		return query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getV2Invitations()
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<Action> getV2Invitations() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Action.class);
		criteria.add(Restrictions.eq("actionType", ActionType.MONITORING_INVITE_V2));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getOldFileInfoContent(
	 * org.esupportail.helpdesk.domain.beans.OldFileInfo)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public byte[] getOldFileInfoContent(final OldFileInfo oldFileInfo) {
		return fileManager.readOldFileInfoContent(oldFileInfo);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteOldFileInfo(
	 * org.esupportail.helpdesk.domain.beans.OldFileInfo)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteOldFileInfo(final OldFileInfo oldFileInfo) {
		fileManager.deleteOldFileInfoContent(oldFileInfo);
		deleteObject(oldFileInfo);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getV2ActionsWithAttachedFile(int)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "cast", "deprecation" })
	@Deprecated
	public List<Action> getV2ActionsWithAttachedFile(final int maxResults) {
		Query query = getQuery(HqlUtils.fromWhereOrderBy(
				Action.class.getSimpleName() + HqlUtils.AS_KEYWORD + "action",
				HqlUtils.isNotNull("oldFileInfo"),
				"id"));
		query.setMaxResults(maxResults);
		return (List<Action>) query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getV2ArchivedInvitations()
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<ArchivedAction> getV2ArchivedInvitations() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArchivedAction.class);
		criteria.add(Restrictions.eq("actionType", ActionType.MONITORING_INVITE_V2));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getInheritingMembersCategories()
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<Category> getInheritingMembersCategories() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.add(Restrictions.eq("inheritMembers", Boolean.TRUE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getLastArchivedTicketId()
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public long getLastArchivedTicketId() {
		Query query = getQuery(HqlUtils.fromOrderByDesc(ArchivedTicket.class.getSimpleName(), "id"));
		query.setMaxResults(1);
		List<ArchivedTicket> archivedTickets = query.list();
		long lastArchivedTicketId = -1;
		if (!archivedTickets.isEmpty()) {
			lastArchivedTicketId = archivedTickets.get(0).getId();
		}
		return lastArchivedTicketId;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getLastTicketId()
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public long getLastTicketId() {
		Query query = getQuery(HqlUtils.fromOrderByDesc(Ticket.class.getSimpleName(), "id"));
		query.setMaxResults(1);
		List<Ticket> tickets = query.list();
		long lastTicketId = -1;
		if (!tickets.isEmpty()) {
			lastTicketId = tickets.get(0).getId();
		}
		return lastTicketId;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#setDefaultOldPriorityLevelToCategories()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setDefaultOldPriorityLevelToCategories() {
		executeUpdate(HqlUtils.update(
				Category.class.getSimpleName() + " c",
				"c.oldDefaultPriorityLevel = 0"));
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getLastActionId()
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public long getLastActionId() {
		Query query = getQuery(HqlUtils.fromOrderByDesc(Action.class.getSimpleName(), "id"));
		query.setMaxResults(1);
		List<Action> actions = query.list();
		long lastActionId = -1;
		if (!actions.isEmpty()) {
			lastActionId = actions.get(0).getId();
		}
		return lastActionId;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getLastArchivedActionId()
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public long getLastArchivedActionId() {
		Query query = getQuery(HqlUtils.fromOrderByDesc(ArchivedAction.class.getSimpleName(), "id"));
		query.setMaxResults(1);
		List<ArchivedAction> archivedActions = query.list();
		long lastArchivedActionId = -1;
		if (!archivedActions.isEmpty()) {
			lastArchivedActionId = archivedActions.get(0).getId();
		}
		return lastArchivedActionId;
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#setToNullEmpyActionMessages()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void setToNullEmpyActionMessages() {
		executeUpdate(HqlUtils.updateWhere(
				Action.class.getSimpleName() + " a",
				"a.message = NULL",
				HqlUtils.equals("a.message", HqlUtils.quote("<p>&#160;</p>"))));
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateBeanSequence(String, String)
	 * @deprecated
	 */
	@Override
	@Deprecated
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void updateBeanSequence(
			final String beanName,
			final String sequenceName) {
		if (DATABASE_TYPE_POSTGRES.equals(databaseType)) {
            try {
                List<Long> ids = getQuery("SELECT MAX(id) FROM " + beanName).list();
                HibernateCallback callback = new HibernateSequenceUpdater(sequenceName, ids.get(0));
                getHibernateTemplate().execute(callback);
            } catch (Exception e) {
                logger.warn("Could not set sequence for bean " + beanName + ". " +
                        "Probably update on non-existing table.");
            }
		}
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getUsersWithNullAuthType(int)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	public List<User> getUsersWithNullAuthType(
			final int maxResults) {
		Query query = getQuery(HqlUtils.fromWhere(
				User.class.getSimpleName(),
				HqlUtils.isNull("authType")));
		query.setMaxResults(maxResults);
		return query.list();
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#upgradeUserKeys(String, java.lang.String)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void upgradeUserKeys(
			final String classname,
			final String field) {
		String commonCondition = HqlUtils.and(
				HqlUtils.not(HqlUtils.like(
						field + ".id",
						HqlUtils.quote(CasUserManager.USER_ID_PREFIX + "%"))),
				HqlUtils.not(HqlUtils.like(
						field + ".id",
						HqlUtils.quote(ShibbolethUserManager.USER_ID_PREFIX + "%"))),
				HqlUtils.not(HqlUtils.like(
						field + ".id",
						HqlUtils.quote(ApplicationUserManager.USER_ID_PREFIX + "%"))));
		String emailCondition = HqlUtils.like(field + ".id", HqlUtils.quote("%@%"));
		executeUpdate(HqlUtils.updateWhere(
				classname,
				field + ".id = concat(" + HqlUtils.quote(ApplicationUserManager.USER_ID_PREFIX)
				+ ", " + field + ".id)",
				HqlUtils.and(commonCondition, emailCondition)));
		executeUpdate(HqlUtils.updateWhere(
				classname,
				field + ".id = concat(" + HqlUtils.quote(CasUserManager.USER_ID_PREFIX)
				+ ", " + field + ".id)",
				HqlUtils.and(commonCondition, HqlUtils.not(emailCondition))));
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteUsersWithNoneAuthType()
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteUsersWithNoneAuthType() {
		executeUpdate(HqlUtils.deleteWhere(
				User.class.getSimpleName(),
				HqlUtils.equals("authType", HqlUtils.quote(AuthUtils.NONE))));
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaqContainers()
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	@RequestCache
	public List<DeprecatedFaqContainer> getFaqContainers() {
		return getHibernateTemplate().loadAll(DeprecatedFaqContainer.class);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addFaqContainer(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void addFaqContainer(final DeprecatedFaqContainer faqContainer) {
		addObject(faqContainer);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateFaqContainer(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void updateFaqContainer(final DeprecatedFaqContainer faqContainer) {
		updateObject(faqContainer);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteFaqContainer(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteFaqContainer(final DeprecatedFaqContainer faqContainer) {
		executeUpdate(HqlUtils.updateWhere(
				Ticket.class.getSimpleName(),
				"deprecatedConnectionFaqContainer = NULL",
				HqlUtils.equals("deprecatedConnectionFaqContainer.id", faqContainer.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedTicket.class.getSimpleName(),
				"deprecatedConnectionFaqContainer = NULL",
				HqlUtils.equals("deprecatedConnectionFaqContainer.id", faqContainer.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				FaqLink.class.getSimpleName(),
				HqlUtils.equals("deprecatedFaqContainer.id", faqContainer.getId())));
		deleteObject(faqContainer);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getRootFaqContainers(
	 * org.esupportail.helpdesk.domain.beans.Department)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	@RequestCache
	public List<DeprecatedFaqContainer> getRootFaqContainers(final Department department) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeprecatedFaqContainer.class);
		criteria.add(Restrictions.isNull(PARENT_ATTRIBUTE));
		if (department == null) {
			criteria.add(Restrictions.isNull(DEPARTMENT_ATTRIBUTE));
		} else {
			criteria.add(Restrictions.eq(DEPARTMENT_ATTRIBUTE, department));
		}
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getSubFaqContainers(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	@RequestCache
	public List<DeprecatedFaqContainer> getSubFaqContainers(final DeprecatedFaqContainer faqContainer) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeprecatedFaqContainer.class);
		criteria.add(Restrictions.eq(PARENT_ATTRIBUTE, faqContainer));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaqEntries()
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	@RequestCache
	public List<DeprecatedFaqEntry> getFaqEntries() {
		return getHibernateTemplate().loadAll(DeprecatedFaqEntry.class);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#addFaqEntry(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void addFaqEntry(final DeprecatedFaqEntry faqEntry) {
		addObject(faqEntry);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateFaqEntry(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void updateFaqEntry(final DeprecatedFaqEntry faqEntry) {
		updateObject(faqEntry);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#deleteFaqEntry(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void deleteFaqEntry(final DeprecatedFaqEntry faqEntry) {
		executeUpdate(HqlUtils.updateWhere(
				Ticket.class.getSimpleName(),
				"deprecatedConnectionFaqEntry = NULL",
				HqlUtils.equals("deprecatedConnectionFaqEntry", faqEntry.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedTicket.class.getSimpleName(),
				"deprecatedConnectionFaqEntry = NULL",
				HqlUtils.equals("deprecatedConnectionFaqEntry.id", faqEntry.getId())));
		executeUpdate(HqlUtils.deleteWhere(
				FaqLink.class.getSimpleName(),
				HqlUtils.equals("deprecatedFaqEntry.id", faqEntry.getId())));
		deleteObject(faqEntry);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#getFaqEntries(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Deprecated
	@RequestCache
	public List<DeprecatedFaqEntry> getFaqEntries(final DeprecatedFaqContainer faqContainer) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeprecatedFaqEntry.class);
		criteria.add(Restrictions.eq(PARENT_ATTRIBUTE, faqContainer));
		criteria.addOrder(Order.asc(ORDER_ATTRIBUTE));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#updateFaqEntriesEffectiveScope(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void updateFaqEntriesEffectiveScope(final DeprecatedFaqContainer faqContainer) {
		executeUpdate(HqlUtils.updateWhere(
				DeprecatedFaqEntry.class.getSimpleName(),
				"effectiveScope = "
				+ HqlUtils.quote(faqContainer.getEffectiveScope()),
				HqlUtils.and(
						HqlUtils.equals(
								"scope",
								HqlUtils.quote(FaqScope.DEFAULT)),
						HqlUtils.equals("parent.id", faqContainer.getId()))));
		executeUpdate(HqlUtils.updateWhere(
				DeprecatedFaqEntry.class.getSimpleName(),
				"effectiveScope = scope",
				HqlUtils.and(
						HqlUtils.not(HqlUtils.equals(
								"scope",
								HqlUtils.quote(FaqScope.DEFAULT))),
						HqlUtils.equals("parent.id", faqContainer.getId()))));
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#migrateFaqContainerRefs(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqContainer,
	 * org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void migrateFaqContainerRefs(
			final DeprecatedFaqContainer faqContainer,
			final Faq faq) {
		executeUpdate(HqlUtils.updateWhere(
				Ticket.class.getSimpleName(),
				"deprecatedConnectionFaqEntry = NULL, deprecatedConnectionFaqContainer = NULL, connectionFaq.id = " + faq.getId(),
				HqlUtils.equals("deprecatedConnectionFaqContainer.id", faqContainer.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedTicket.class.getSimpleName(),
				"deprecatedConnectionFaqEntry = NULL, deprecatedConnectionFaqContainer = NULL, connectionFaq.id = " + faq.getId(),
				HqlUtils.equals("deprecatedConnectionFaqContainer.id", faqContainer.getId())));
		executeUpdate(HqlUtils.updateWhere(
				FaqLink.class.getSimpleName(),
				"deprecatedFaqEntry = NULL, deprecatedFaqContainer = NULL, faq.id = " + faq.getId(),
				HqlUtils.equals("deprecatedFaqContainer.id", faqContainer.getId())));
	}

	/**
	 * @see org.esupportail.helpdesk.dao.DaoService#migrateFaqEntryRefs(
	 * org.esupportail.helpdesk.domain.beans.DeprecatedFaqEntry,
	 * org.esupportail.helpdesk.domain.beans.Faq)
	 */
	@Override
	@SuppressWarnings("deprecation")
	@Deprecated
	public void migrateFaqEntryRefs(
			final DeprecatedFaqEntry faqEntry,
			final Faq faq) {
		executeUpdate(HqlUtils.updateWhere(
				Ticket.class.getSimpleName(),
				"deprecatedConnectionFaqEntry = NULL, deprecatedConnectionFaqContainer = NULL, connectionFaq.id = " + faq.getId(),
				HqlUtils.equals("deprecatedConnectionFaqEntry.id", faqEntry.getId())));
		executeUpdate(HqlUtils.updateWhere(
				ArchivedTicket.class.getSimpleName(),
				"deprecatedConnectionFaqEntry = NULL, deprecatedConnectionFaqContainer = NULL, connectionFaq.id = " + faq.getId(),
				HqlUtils.equals("deprecatedConnectionFaqEntry.id", faqEntry.getId())));
		executeUpdate(HqlUtils.updateWhere(
				FaqLink.class.getSimpleName(),
				"deprecatedFaqEntry = NULL, deprecatedFaqContainer = NULL, faq.id = " + faq.getId(),
				HqlUtils.equals("deprecatedFaqEntry.id", faqEntry.getId())));
	}

}
