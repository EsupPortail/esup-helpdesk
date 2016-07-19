/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.Version;
import org.esupportail.helpdesk.domain.beans.Action;
import org.esupportail.helpdesk.domain.beans.Alert;
import org.esupportail.helpdesk.domain.beans.ArchivedAction;
import org.esupportail.helpdesk.domain.beans.ArchivedFileInfo;
import org.esupportail.helpdesk.domain.beans.ArchivedInvitation;
import org.esupportail.helpdesk.domain.beans.ArchivedTicket;
import org.esupportail.helpdesk.domain.beans.Bookmark;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.CategoryMember;
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
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.TicketMonitoring;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.userManagement.UserStore;
import org.esupportail.helpdesk.exceptions.ArchivedTicketNotFoundException;
import org.esupportail.helpdesk.exceptions.CategoryMemberNotFoundException;
import org.esupportail.helpdesk.exceptions.CategoryNotFoundException;
import org.esupportail.helpdesk.exceptions.DepartmentManagerNotFoundException;
import org.esupportail.helpdesk.exceptions.DepartmentNotFoundException;
import org.esupportail.helpdesk.exceptions.FaqNotFoundException;
import org.esupportail.helpdesk.exceptions.TicketNotFoundException;

/**
 * The domain service interface.
 */
public interface DomainService extends Serializable {

	/** The default priority. */
	int DEFAULT_PRIORITY_VALUE = 0;

	//////////////////////////////////////////////////////////////
	// Properties
	//////////////////////////////////////////////////////////////

	/**
	 * @return the default ticket scope for the departments.
	 */
	String getDepartmentDefaultTicketScope();

	/**
	 * @return the default priority level for the departments.
	 */
	int getDepartmentDefaultTicketPriorityLevel();

	/**
	 * @return the default scope for the FAQs.
	 */
	String getDepartmentDefaultFaqScope();

	/**
	 * @return the names of the assignment algorithms.
	 */
	List<String> getAssignmentAlgorithmNames();

	/**
	 * @param name
	 * @param locale
	 * @return the description of an assignment algorithm.
	 */
	String getAssignmentAlgorithmDescription(
			String name,
			Locale locale);

	/**
	 * @return the default assignment algorithm name.
	 */
	String getDefaultAssignmentAlgorithmName();

	/**
	 * @return the names of the computer url builders.
	 */
	List<String> getComputerUrlBuilderNames();

	/**
	 * @param name
	 * @param locale
	 * @return the description of a computer url builder.
	 */
	String getComputerUrlBuilderDescription(
			String name,
			Locale locale);

	/**
	 * @return the default computer url builder name.
	 */
	String getDefaultComputerUrlBuilderName();

	//////////////////////////////////////////////////////////////
	// Priorities
	//////////////////////////////////////////////////////////////

	/**
	 * @return the priorities
	 */
	List<Integer> getPriorities();

	//////////////////////////////////////////////////////////////
	// Origins
	//////////////////////////////////////////////////////////////

	/**
	 * @return the origins
	 */
	List<String> getOrigins();

	/**
	 * @return the WEB origin
	 */
	String getWebOrigin();

	/**
	 * @return true to use LDAP, false otherwise.
	 */
	boolean isUseLdap();

	//////////////////////////////////////////////////////////////
	// User
	//////////////////////////////////////////////////////////////

	/**
	 * @return the user store.
	 */
	UserStore getUserStore();

	/**
	 * @return the list of all the users.
	 */
	List<User> getUsers();

	/**
	 * @return the number of users.
	 */
	int getUsersNumber();

	/**
	 * @return the number of CAS users.
	 */
	int getCasUsersNumber();

	/**
	 * @return the number of Shibboleth users.
	 */
	int getShibbolethUsersNumber();

	/**
	 * @return the number of application users.
	 */
	int getApplicationUsersNumber();

	/**
	 * Update a user.
	 * @param user
	 */
	void updateUser(User user);

	/**
	 * @return the administrators.
	 */
	List<User> getAdmins();

	/**
	 * Add an administrator.
	 * @param user
	 */
	void addAdmin(User user);

	/**
	 * Delete an administrator.
	 * @param user
	 */
	void deleteAdmin(User user);

	/**
	 * Test the information added by the application when users create tickets.
	 */
	void testUserInfo();

	/**
	 * @param user
	 * @param locale
	 * @return information on a given user.
	 */
	String getUserInfo(
			User user,
			Locale locale);

	/**
	 * For local users, change the owner of the tickets created with the email.
	 * @param user
	 */
	void transformEntitiesCreatedWithEmail(User user);

	//////////////////////////////////////////////////////////////
	// Department
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the Department instance that corresponds to an id.
	 * @throws DepartmentNotFoundException
	 */
	Department getDepartment(long id) throws DepartmentNotFoundException;

	/**
	 * @return the departments.
	 */
	List<Department> getDepartments();

	/**
	 * @return the number of departments.
	 */
	int getDepartmentsNumber();

	/**
	 * @return the number of real departments.
	 */
	int getRealDepartmentsNumber();

	/**
	 * @return the number of virtual departments.
	 */
	int getVirtualDepartmentsNumber();

	/**
	 * Add a department.
	 * @param department
	 */
	void addDepartment(Department department);

	/**
	 * Update a department.
	 * @param department
	 */
	void updateDepartment(Department department);

	/**
	 * Delete a department.
	 * @param department
	 * @param archivedTicketsNewDepartment
	 */
	void deleteDepartment(Department department, Department archivedTicketsNewDepartment);

	/**
	 * @param label
	 * @return 'true' if a department has the same label.
	 */
	boolean isDepartmentLabelUsed(String label);

	/**
	 * Change the order of a department.
	 * @param department
	 */
	void moveDepartmentUp(Department department);

	/**
	 * Change the order of a department.
	 * @param department
	 */
	void moveDepartmentDown(Department department);

	/**
	 * Change the order of a department.
	 * @param department
	 */
	void moveDepartmentFirst(Department department);

	/**
	 * Change the order of a department.
	 * @param department
	 */
	void moveDepartmentLast(Department department);

	/**
	 * Reorder a list of departments.
	 * @param departments
	 */
	void reorderDepartments(List<Department> departments);

	/**
	 * @param user
	 * @param client
	 * @return the departments visible by a user on ticket creation.
	 */
	List<Department> getTicketCreationDepartments(
			User user,
			InetAddress client);

	/**
	 * @param user
	 * @param client
	 * @return the departments visible by a user on ticket view.
	 */
	List<Department> getTicketViewDepartments(
			User user,
			InetAddress client);

	/**
	 * @param user
	 * @param client
	 * @return the departments visible by a user on faq view.
	 */
	List<Department> getFaqViewDepartments(
			User user,
			InetAddress client);

	/**
	 * @return the enabled departments.
	 */
	List<Department> getEnabledDepartments();

	/**
	 * @param user
	 * @param department
	 * @param client
	 * @return true if the department is visible by the user for ticket creation.
	 */
	boolean isDepartmentVisibleForTicketCreation(
			User user,
			Department department,
			InetAddress client);

	/**
	 * @param user
	 * @param department
	 * @param client
	 * @return true if the department is visible by the user for ticket view.
	 */
	boolean isDepartmentVisibleForTicketView(
			User user,
			Department department,
			InetAddress client);

	/**
	 * @param user
	 * @param department
	 * @param client
	 * @return true if the department is visible by the user for faq view.
	 */
	boolean isDepartmentVisibleForFaqView(
			User user,
			Department department,
			InetAddress client);

	/**
	 * @param user
	 * @param client
	 * @return the departments managed or visible by a user for ticket view.
	 */
	List<Department> getManagedOrTicketViewVisibleDepartments(
			User user,
			InetAddress client);

	/**
	 * @param user
	 * @param client
	 * @return the departments managed or visible by a user for search.
	 */
	List<Department> getSearchVisibleDepartments(
			User user,
			InetAddress client);

	/**
	 * @param user
	 * @param department
	 * @param client
	 * @return true if the department is visible by the user for search.
	 */
	boolean isDepartmentVisibleForSearch(
			User user,
			Department department,
			InetAddress client);

	/**
	 * @param filter
	 * @return the departments with the given filter.
	 */
	List<Department> getDepartmentsByFilter(String filter);

	/**
	 * @param label
	 * @return the department with the given label.
	 */
	Department getDepartmentByLabel(String label);

	/**
	 * @return the effective assignment algorithm name of a department.
	 * @param department
	 */
	String getDepartmentEffectiveAssignmentAlgorithmName(
			Department department);

	//////////////////////////////////////////////////////////////
	// DepartmentManager
	//////////////////////////////////////////////////////////////

	/**
	 * @param department
	 * @param user
	 * @return the departmentManager instance that corresponds to a department and a user.
	 * @throws DepartmentManagerNotFoundException
	 */
	DepartmentManager getDepartmentManager(Department department, User user)
	throws DepartmentManagerNotFoundException;

	/**
	 * @param department
	 * @param user
	 * @return 'true' if user is a manager of the department.
	 */
	boolean isDepartmentManager(Department department, User user);

	/**
	 * @param department
	 * @return the managers of a department.
	 */
	List<DepartmentManager> getDepartmentManagers(Department department);

	/**
	 * @param user
	 * @return the managers for a user.
	 */
	List<DepartmentManager> getDepartmentManagers(User user);

	/**
	 * @return the number of (distinct) managers.
	 */
	int getManagerUsersNumber();

	/**
	 * @param department
	 * @return the managers of a department who are available.
	 */
	List<DepartmentManager> getAvailableDepartmentManagers(Department department);

	/**
	 * Add a department manager.
	 * @param department
	 * @param user
	 * @return the department manager created.
	 */
	DepartmentManager addDepartmentManager(
			Department department,
			User user);

	/**
	 * Delete a department manager.
	 * @param author
	 * @param departmentManager the department manager to assign the tickets to.
	 * @param useAssignmentAlgorithm true to use the assignment algorithm (if false,
	 * assign to targetDepartmentManager if not null)
	 * @param newManager the new manager for the opened tickets managed
	 * by the department manager to delete
	 */
	void deleteDepartmentManager(
			User author,
			DepartmentManager departmentManager,
			boolean useAssignmentAlgorithm,
			User newManager);

	/**
	 * Update a departmentManager.
	 * @param departmentManager
	 */
	void updateDepartmentManager(DepartmentManager departmentManager);

	/**
	 * Change the order of a department manager.
	 * @param departmentManager
	 */
	void moveDepartmentManagerUp(DepartmentManager departmentManager);

	/**
	 * Change the order of a department manager.
	 * @param departmentManager
	 */
	void moveDepartmentManagerDown(DepartmentManager departmentManager);

	/**
	 * Change the order of a department manager.
	 * @param departmentManager
	 */
	void moveDepartmentManagerFirst(DepartmentManager departmentManager);

	/**
	 * Change the order of a department manager.
	 * @param departmentManager
	 */
	void moveDepartmentManagerLast(DepartmentManager departmentManager);

	/**
	 * @param department
	 * @return the virtual departments of the department (pointed to itself).
	 */
	List<Department> getVirtualDepartments(Department department);

	/**
	 * @param department
	 * @return true if the department has virtual departments (pointed to itself).
	 */
	boolean hasVirtualDepartments(Department department);

	/**
	 * @param user
	 * @return the departments managed by a user.
	 */
	List<Department> getManagedDepartments(User user);

	/**
	 * @param user
	 * @return the departments managed by a user or all the departments if administrator.
	 */
	List<Department> getManagedDepartmentsOrAllIfAdmin(User user);

	/**
	 * @param user
	 * @return 'true' if the user is a manager.
	 */
	boolean isDepartmentManager(User user);

	/**
	 * Reorder a list of department managers.
	 * @param managers
	 */
	void reorderDepartmentManagers(List<DepartmentManager> managers);

	//////////////////////////////////////////////////////////////
	// Category
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the Category instance that corresponds to an id.
	 * @throws CategoryNotFoundException
	 */
	Category getCategory(long id) throws CategoryNotFoundException;

	/**
	 * @param department
	 * @return all the categories of a department.
	 */
	List<Category> getCategories(Department department);

	/**
	 * @return all the categories.
	 */
	List<Category> getCategories();

	/**
	 * @return all the root categories as a map.
	 */
	Map<Department, List<Category>> getRootCategoriesMap();

	/**
	 * @return all the sub categories as a map.
	 */
	Map<Category, List<Category>> getSubCategoriesMap();

	/**
	 * Add a category.
	 * @param category
	 */
	void addCategory(Category category);

	/**
	 * Update a category.
	 * @param category
	 */
	void updateCategory(Category category);

	/**
	 * Delete a category.
	 * @param category
	 */
	void deleteCategory(Category category);

	/**
	 * @param department
	 * @return the root categories of a department.
	 */
	List<Category> getRootCategories(Department department);

	/**
	 * @param department
	 * @return true if the department has root categories.
	 */
	boolean hasRootCategories(Department department);

	/**
	 * @param category
	 * @return the sub categories of a category.
	 */
	List<Category> getSubCategories(Category category);

	/**
	 * @param category
	 * @return true if the category has sub categories.
	 */
	boolean hasSubCategories(Category category);

	/**
	 * Change the order of a category.
	 * @param category
	 */
	void moveCategoryUp(Category category);

	/**
	 * Change the order of a category.
	 * @param category
	 */
	void moveCategoryDown(Category category);

	/**
	 * Change the order of a category.
	 * @param category
	 */
	void moveCategoryFirst(Category category);

	/**
	 * Change the order of a category.
	 * @param category
	 */
	void moveCategoryLast(Category category);

	/**
	 * Reorder a list of categories.
	 * @param categories
	 */
	void reorderCategories(List<Category> categories);

	/**
	 * Move a category to another place.
	 * @param categoryToUpdate
	 * @param targetDepartment
	 * @param targetCategory
	 */
	void moveCategory(
			Category categoryToUpdate,
			Department targetDepartment,
			Category targetCategory);

	/**
	 * @param category
	 * @return the virtual categories of the category (pointed to itself).
	 */
	List<Category> getVirtualCategories(Category category);

	/**
	 * @param category
	 * @return true if the category has virtual categories (pointed to itself).
	 */
	boolean hasVirtualCategories(Category category);

	/**
	 * @return the effective assignment algorithm name of a category.
	 * @param category
	 */
	String getCategoryEffectiveAssignmentAlgorithmName(
			Category category);

	/**
	 * @return the default assignment algorithm name of a category.
	 * @param category
	 */
	String getCategoryDefaultAssignmentAlgorithmName(
			Category category);

	/**
	 * @param category
	 * @param targetCategory
	 * @param map
	 * @return true if category is in the virtual hierarchy of targetCategory.
	 */
	boolean detectRedirectionLoop(
			Category category,
			Category targetCategory,
			Map<Category, Boolean> map);

	/**
	 * @return the number of categories.
	 */
	int getCategoriesNumber();

	/**
	 * @return the number of real categories.
	 */
	int getRealCategoriesNumber();

	/**
	 * @return the number of virtual categories.
	 */
	int getVirtualCategoriesNumber();

	/**
	 * @return the target categories of a user.
	 * @param author
	 */
	List<Category> getTargetCategories(User author);

	//////////////////////////////////////////////////////////////
	// CategoryMember
	//////////////////////////////////////////////////////////////

	/**
	 * @param category
	 * @param user
	 * @return the CategoryMember instance that corresponds to a category and a user.
	 * @throws CategoryMemberNotFoundException
	 */
	CategoryMember getCategoryMember(Category category, User user)
	throws CategoryMemberNotFoundException;

	/**
	 * @param category
	 * @param user
	 * @return 'true' if user is a member of the category.
	 */
	boolean isCategoryMember(Category category, User user);

	/**
	 * @param category
	 * @return the members of a category.
	 */
	List<CategoryMember> getCategoryMembers(Category category);

	/**
	 * @return all the category members as a map.
	 */
	Map<Category, List<CategoryMember>> getCategoryMembersMap();

	/**
	 * @param user
	 * @param department
	 * @return the categories a user is member of (limited to a department).
	 */
	List<Category> getMemberCategories(User user, Department department);

	/**
	 * Add a category member.
	 * @param category
	 * @param user
	 * @return the category member created.
	 */
	CategoryMember addCategoryMember(
			Category category,
			User user);

	/**
	 * Delete a category member.
	 * @param categoryMember
	 */
	void deleteCategoryMember(
			CategoryMember categoryMember);

	/**
	 * Delete a category member and reassign the opened tickets managed.
	 * @param categoryMember
	 * @param useAssignmentAlgorithm true to use the assignment algorithm, ignored if reassignTickets is false
	 * @param newManager the new manager for the tickets (null to free the tickets, ignored
	 * if reassignTicket is false or useAssignmentAlgorithm is true
	 */
	void deleteCategoryMember(
			CategoryMember categoryMember,
			boolean useAssignmentAlgorithm,
			User newManager);

	/**
	 * Update a category member.
	 * @param categoryMember
	 */
	void updateCategoryMember(CategoryMember categoryMember);

	/**
	 * Change the order of a category member.
	 * @param categoryMember
	 */
	void moveCategoryMemberUp(CategoryMember categoryMember);

	/**
	 * Change the order of a category member.
	 * @param categoryMember
	 */
	void moveCategoryMemberDown(CategoryMember categoryMember);

	/**
	 * Change the order of a category member.
	 * @param categoryMember
	 */
	void moveCategoryMemberFirst(CategoryMember categoryMember);

	/**
	 * Change the order of a category member.
	 * @param categoryMember
	 */
	void moveCategoryMemberLast(CategoryMember categoryMember);

	/**
	 * Reorder a list of category members.
	 * @param categoryMembers
	 */
	void reorderCategoryMembers(List<CategoryMember> categoryMembers);

	/**
	 * @return the available effective department managers of a category (with inheritance).
	 * @param category
	 */
	List<DepartmentManager> getEffectiveAvailableDepartmentManagers(Category category);

	/**
	 * @param category
	 * @return the inherited members of a category.
	 */
	List<DepartmentManager> getInheritedDepartmentManagers(Category category);

	/**
	 * @param category
	 * @return the effective (real or inherited) members of a category.
	 */
	List<DepartmentManager> getEffectiveDepartmentManagers(Category category);

	//////////////////////////////////////////////////////////////
	// Faq
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the Faq instance that corresponds to an id.
	 * @throws FaqNotFoundException
	 */
	Faq getFaq(long id) throws FaqNotFoundException;

	/**
	 * Add a FAQ.
	 * @param faq
	 */
	void addFaq(Faq faq);

	/**
	 * Update a FAQ.
	 * @param faq
	 */
	void updateFaq(Faq faq);

	/**
	 * Delete a FAQ.
	 * @param faq
	 */
	void deleteFaq(Faq faq);

	/**
	 * @return the root FAQs.
	 */
	List<Faq> getRootFaqs();

	/**
	 * @param department
	 * @return the root FAQs of a department.
	 */
	List<Faq> getRootFaqs(Department department);

	/**
	 * @param department
	 * @return the number of root FAQs of a department.
	 */
	int getRootFaqsNumber(Department department);

	/**
	 * @param department
	 * @return true the department has root FAQs.
	 */
	boolean hasRootFaqs(Department department);

	/**
	 * @param faq
	 * @return the sub FAQs of a FAQ.
	 */
	List<Faq> getSubFaqs(Faq faq);

	/**
	 * Change the order of a FAQ.
	 * @param faq
	 */
	void moveFaqUp(Faq faq);

	/**
	 * Change the order of a FAQ.
	 * @param faq
	 */
	void moveFaqDown(Faq faq);

	/**
	 * Change the order of a FAQ.
	 * @param faq
	 */
	void moveFaqFirst(Faq faq);

	/**
	 * Change the order of a FAQ.
	 * @param faq
	 */
	void moveFaqLast(Faq faq);

	/**
	 * Move a FAQ to another place.
	 * @param faqToUpdate
	 * @param targetDepartment
	 * @param targetFaq
	 */
	void moveFaq(
			final Faq faqToUpdate,
			final Department targetDepartment,
			final Faq targetFaq);

	/**
	 * @param faq
	 * @return true if the FAQs has sub FAQs.
	 */
	boolean hasSubFaqs(Faq faq);

	/**
	 * @param lastUpdate
	 * @param maxResults
	 * @return the FAQs that were changed after a given date.
	 */
	List<Faq> getFaqsChangedAfter(Timestamp lastUpdate, int maxResults);

	//////////////////////////////////////////////////////////////
	// FaqEvent
	//////////////////////////////////////////////////////////////

	/**
	 * Add a FAQ event.
	 * @param faqEvent
	 */
	void addFaqEvent(FaqEvent faqEvent);

	/**
	 * @return all the FAQ events.
	 */
	List<FaqEvent> getFaqEvents();

	/**
	 * Delete a FAQ event.
	 * @param faqEvent
	 */
	void deleteFaqEvent(FaqEvent faqEvent);

	//////////////////////////////////////////////////////////////
	// Ticket
	//////////////////////////////////////////////////////////////

	/**
	 * @return ticket with the given id.
	 * @param id
	 * @throws TicketNotFoundException
	 */
	Ticket getTicket(long id) throws TicketNotFoundException;

	/**
	 * @param category
	 * @return the tickets of a category.
	 */
	List<Ticket> getTickets(Category category);

	/**
	 * @param category
	 * @return true if there tickets in the category.
	 */
	boolean hasTickets(Category category);

	/**
	 * @param department
	 * @return the tickets of a department.
	 */
	List<Ticket> getTickets(Department department);

	/**
	 * @param department
	 * @return the opened tickets of the department.
	 */
	List<Ticket> getOpenedTicketsByLastActionDate(Department department);

	/**
	 * @param department
	 * @return the number of tickets in a department.
	 */
	int getTicketsNumber(Department department);

	/**
	 * Update a ticket.
	 * @param ticket
	 */
	void updateTicket(Ticket ticket);

	/**
	 * Reload a ticket.
	 * @param ticket
	 * @return the same ticket, up to date.
	 */
	Ticket reloadTicket(Ticket ticket);

	/**
	 * Delete a ticket.
	 * @param ticket
	 * @param deleteFiles
	 */
	void deleteTicket(Ticket ticket, boolean deleteFiles);

	/**
	 * Move a ticket.
	 * @param author
	 * @param ticket
	 * @param targetCategory
	 * @param message
	 * @param actionScope
	 * @param alerts
	 * @param monitor
	 * @param invite
	 * @param useAssignmentAlgorithm
	 */
	void moveTicket(
			User author,
			Ticket ticket,
			Category targetCategory,
			String message,
			String actionScope,
			boolean alerts,
			boolean monitor,
			boolean invite,
			final boolean useAssignmentAlgorithm);

	/**
	 * Move a ticket.
	 * @param ticket
	 * @param targetCategory
	 */
	void moveTicket(
			Ticket ticket,
			Category targetCategory);

	/**
	 * Set the creator of the ticket.
	 * @param ticket
	 */
	void setCreator(Ticket ticket);

	/**
	 * @return the number of tickets.
	 */
	int getTicketsNumber();

	/**
	 * @param startIndex the index of the first ticket to retrieve
	 * @param maxResults the maximum number of tickets to retrieve
	 * @return the tickets within a given range.
	 */
	List<Ticket> getTickets(long startIndex, int maxResults);

	/**
	 * @param category
	 * @param user
	 * @return true if the uses manages opened tickets in the category.
	 */
	boolean hasOpenManagedTickets(
			Category category,
			User user);

	/**
	 * @param departmentManager
	 * @return The number of opended tickets of the department managed by the user.
	 */
	int getOpenManagedTicketsNumber(
			DepartmentManager departmentManager);

	/**
	 * @param departmentManager
	 * @return true if a department manager manages opened tickets.
	 */
	boolean hasOpenManagedTickets(
			DepartmentManager departmentManager);

	/**
	 * @param user
	 * @return true if the user shows tickets after closure.
	 */
	boolean userShowsTicketAfterClosure(User user);

	/**
	 * Add a ticket through the web interface.
	 * @param author
	 * @param owner not null to set a different owner
	 * @param creationDepartment
	 * @param category
	 * @param label
	 * @param computer
	 * @param priorityLevel
	 * @param message
	 * @param ticketScope
	 * @param ticketOrigin
	 * @return the ticket created.
	 */
	Ticket addWebTicket(
			User author,
			User owner,
			Department creationDepartment,
			Category category,
			String label,
			String computer,
			int priorityLevel,
			String message,
			String ticketScope,
			String ticketOrigin);

	/**
	 * Add a ticket through the web interface.
	 * @param sender
	 * @param address
	 * @param creationDepartment
	 * @param category
	 * @param label
	 * @return the ticket created.
	 */
	Ticket addEmailTicket(
			User sender,
			String address,
			Department creationDepartment,
			Category category,
			String label);

	/**
	 * Take a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 */
	void takeTicket(User author, Ticket ticket, String message, String actionScope);

	/**
	 * Take a ticket and close it.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 * @param freeTicketAfterClosure
	 */
	void takeAndCloseTicket(
			User author,
			Ticket ticket,
			String message,
			String actionScope,
			boolean freeTicketAfterClosure);

	/**
	 * Take a ticket and request information.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 */
	void takeAndRequestTicketInformation(
			User author,
			Ticket ticket,
			String message,
			String actionScope);

	/**
	 * Close a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 * @param freeTicketAfterClosure
	 */
	void closeTicket(
			User author,
			Ticket ticket,
			String message,
			String actionScope,
			boolean freeTicketAfterClosure);

	/**
	 * Free a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 */
	void freeTicket(User author, Ticket ticket, String message, String actionScope);

	/**
	 * Change the scope of a ticket.
	 * @param author
	 * @param ticket
	 * @param ticketScope
	 * @param alerts
	 */
	void changeTicketScope(User author, Ticket ticket, String ticketScope, boolean alerts);

	/**
	 * Change the origin of a ticket.
	 * @param author
	 * @param ticket
	 * @param ticketOrigin
	 * @param alerts
	 */
	void changeTicketOrigin(User author, Ticket ticket, String ticketOrigin, boolean alerts);

	/**
	 * Change the priority of a ticket.
	 * @param author
	 * @param ticket
	 * @param ticketPriority
	 * @param alerts
	 */
	void changeTicketPriority(User author, Ticket ticket, int ticketPriority, boolean alerts);

	/**
	 * Change the computer of a ticket.
	 * @param author
	 * @param ticket
	 * @param ticketComputer
	 * @param alerts
	 */
	void changeTicketComputer(User author, Ticket ticket, String ticketComputer, boolean alerts);

	/**
	 * Change the spent time of a ticket.
	 * @param author
	 * @param ticket
	 * @param ticketSpentTime
	 * @param alerts
	 */
	void changeTicketSpentTime(User author, Ticket ticket, long ticketSpentTime, boolean alerts);

	/**
	 * Assign a ticket.
	 * @param author
	 * @param ticket
	 * @param manager
	 * @param message
	 * @param actionScope
	 */
	void assignTicket(
			User author,
			Ticket ticket,
			User manager,
			String message,
			String actionScope);

	/**
	 * Change the owner of a ticket.
	 * @param author
	 * @param ticket
	 * @param label
	 * @param alerts
	 */
	void changeTicketLabel(
			User author,
			Ticket ticket,
			String label,
			boolean alerts);

	/**
	 * Change the label of a ticket.
	 * @param author
	 * @param ticket
	 * @param owner
	 * @param message
	 * @param actionScope
	 * @param alerts
	 */
	void changeTicketOwner(
			User author,
			Ticket ticket,
			User owner,
			String message,
			String actionScope,
			boolean alerts);

	/**
	 * Add a comment to a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 * @param alerts
	 */
	void giveInformation(
			User author,
			Ticket ticket,
			String message,
			String actionScope,
			boolean alerts);

	/**
	 * Add a comment to a ticket.
	 * @param author
	 * @param ticket
	 * @param filename
	 * @param content
	 * @param actionScope
	 */
	void uploadFile(
			User author,
			Ticket ticket,
			String filename,
			byte[] content,
			String actionScope);

	/**
	 * Request information for a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 */
	void requestTicketInformation(
			User author,
			Ticket ticket,
			String message,
			String actionScope);

	/**
	 * Reopen a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 */
	void reopenTicket(
			User author,
			Ticket ticket,
			String message,
			String actionScope);

	/**
	 * Cancel a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 */
	void cancelTicket(
			User author,
			Ticket ticket,
			String message,
			String actionScope);

	/**
	 * Refuse a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 */
	void refuseTicket(
			User author,
			Ticket ticket,
			String message,
			String actionScope);

	/**
	 * Postpone a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 * @param recallDate
	 */
	void postponeTicket(
			User author,
			Ticket ticket,
			String message,
			String actionScope,
			Timestamp recallDate);

	/**
	 * Cancel the postponement of a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 */
	void cancelTicketPostponement(
			User author,
			Ticket ticket,
			String message,
			String actionScope);

	/**
	 * Refuse the closure of a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 */
	void refuseTicketClosure(
			User author,
			Ticket ticket,
			String message,
			String actionScope);

	/**
	 * Approve the closure of a ticket.
	 * @param author
	 * @param ticket
	 * @param message
	 * @param actionScope
	 */
	void approveTicketClosure(
			User author,
			Ticket ticket,
			String message,
			String actionScope);

	/**
	 * Expire a ticket.
	 * @param ticket
	 * @param alerts
	 */
	void expireTicket(
			Ticket ticket,
			boolean alerts);

	/**
	 * Connect a ticket to another ticket.
	 * @param ticket
	 * @param targetTicket
	 */
	void connectTicketToTicket(
			Ticket ticket,
			Ticket targetTicket);

	/**
	 * Connect a ticket to an archived ticket.
	 * @param ticket
	 * @param targetArchivedTicket
	 */
	void connectTicketToArchivedTicket(
			Ticket ticket,
			ArchivedTicket targetArchivedTicket);

	/**
	 * Connect a ticket to a FAQ.
	 * @param ticket
	 * @param targetFaq
	 */
	void connectTicketToFaq(
			Ticket ticket,
			Faq targetFaq);

	/**
	 * Invite a User for a ticket.
	 * @param actionOwner
	 * @param ticket
	 * @param invitedUser
	 * @param actionMessage
	 * @param actionScope
	 * @param alert
	 * @return true if an invitation email has been sent.
	 */
	boolean invite(
    		User actionOwner,
            Ticket ticket,
            User invitedUser,
            String actionMessage,
            String actionScope,
            boolean alert);

	/**
	 * Remove an invitation.
	 * @param actionOwner
	 * @param invitation
	 * @param alert
	 */
	void removeInvitation(
    		User actionOwner,
            Invitation invitation,
            boolean alert);

	/**
	 * @param timestamp
	 * @param maxResults
	 * @return the tickets closed before the given date.
	 */
	List<Ticket> getClosedTicketsBefore(Timestamp timestamp, int maxResults);

	/**
	 * @param timestamp
	 * @param maxResults
	 * @return the non approved tickets closed before the given date.
	 */
	List<Ticket> getNonApprovedTicketsClosedBefore(Timestamp timestamp, int maxResults);

	/**
	 * Archive a ticket.
	 * @param ticket
	 */
	void archiveTicket(Ticket ticket);

	/**
	 * @param lastUpdate
	 * @param maxResults
	 * @return the ticket that were changed after a given date.
	 */
	List<Ticket> getTicketsChangedAfter(Timestamp lastUpdate, int maxResults);

	/**
	 * Recall postponed tickets.
	 * @return the number of tickets recalled.
	 */
	int recallPostponedTickets();

	/**
	 * @param ticket
	 * @return the URL for the computer of a ticket.
	 */
	String getTicketComputerUrl(Ticket ticket);

	/**
	 * @return the oldest ticket date.
	 */
	Timestamp getOldestTicketDate();

	/**
	 * Delete all the tickets.
	 */
	void deleteAllTickets();

	/**
	 * Delete a ticket.
	 */
	void deleteTicketById(long ticketNumber);
	//////////////////////////////////////////////////////////////
	// Action
	//////////////////////////////////////////////////////////////

	/**
	 * @param ticket
	 * @return the actions of the ticket.
	 */
	List<Action> getActions(Ticket ticket);

	/**
	 * @param ticket
	 * @return the last action of the ticket.
	 */
	Action getLastAction(Ticket ticket);

	/**
	 * @param ticket
	 * @param action type
	 * @return the last action of the ticket.
	 */
	Action getLastActionByActionType(Ticket ticket, String actionType);

	/**
	 * @param startIndex the index of the action ticket to retrieve
	 * @param maxResults the maximum number of actions to retrieve
	 * @return the actions within a given range.
	 */
	List<Action> getActions(long startIndex, int maxResults);

	/**
	 * Update an action.
	 * @param action
	 */
	void updateAction(Action action);

	/**
	 * Add an action.
	 * @param action
	 */
	void addAction(Action action);

	/**
	 * @return the number of actions.
	 */
	int getActionsNumber();

	/**
	 * @param action
	 * @return the style class to apply to an action.
	 */
	String getActionStyleClass(Action action);

	//////////////////////////////////////////////////////////////
	// FileInfo
	//////////////////////////////////////////////////////////////

	/**
	 * @param ticket
	 * @return the files of the ticket.
	 */
	List<FileInfo> getFileInfos(Ticket ticket);

	/**
	 * Add a FileInfo.
	 * @param fileInfo
	 */
	void addFileInfo(FileInfo fileInfo);

	/**
	 * Update a FileInfo.
	 * @param fileInfo
	 */
	void updateFileInfo(FileInfo fileInfo);

	/**
	 * @return the content of a FileInfo.
	 * @param fileInfo
	 */
	byte[] getFileInfoContent(FileInfo fileInfo);

	//////////////////////////////////////////////////////////////
	// ArchivedTicket
	//////////////////////////////////////////////////////////////

	/**
	 * @return the archived ticket with the given id.
	 * @param id
	 * @throws ArchivedTicketNotFoundException
	 */
	ArchivedTicket getArchivedTicket(long id) throws ArchivedTicketNotFoundException;

	/**
	 * Delete all the tickets older than x days.
	 */
	void deleteArchivedTickets(final Integer days);
	
	/**
	 * @param lastUpdate
	 * @param maxResults
	 * @return the archived tickets that were created after a given date.
	 */
	List<ArchivedTicket> getTicketsArchivedAfter(Timestamp lastUpdate, int maxResults);

	/**
	 * @return the number of archived tickets.
	 */
	int getArchivedTicketsNumber();

	/**
	 * @param department
	 * @return the number of archived tickets in a department.
	 */
	int getArchivedTicketsNumber(Department department);

	/**
	 * @return the archived ticket with the given original id.
	 * @param id
	 * @throws ArchivedTicketNotFoundException
	 */
	ArchivedTicket getArchivedTicketByOriginalId(long id) throws ArchivedTicketNotFoundException;

	/**
	 * @param startIndex the index of the first archived ticket to retrieve
	 * @param maxResults the maximum number of archived tickets to retrieve
	 * @return the archived tickets within a given range.
	 */
	List<ArchivedTicket> getArchivedTickets(long startIndex, int maxResults);

	//////////////////////////////////////////////////////////////
	// ArchivedAction
	//////////////////////////////////////////////////////////////

	/**
	 * @param startIndex the index of the archived action ticket to retrieve
	 * @param maxResults the maximum number of archived actions to retrieve
	 * @return the archived actions within a given range.
	 */
	List<ArchivedAction> getArchivedActions(long startIndex, int maxResults);

	/**
	 * Update an archived action.
	 * @param archivedAction
	 */
	void updateArchivedAction(ArchivedAction archivedAction);

	/**
	 * @param archivedTicket
	 * @return the actions of an archived ticket.
	 */
	List<ArchivedAction> getArchivedActions(ArchivedTicket archivedTicket);

	/**
	 * @param archivedAction
	 * @return the style class to apply to an archived action.
	 */
	String getArchivedActionStyleClass(ArchivedAction archivedAction);

	//////////////////////////////////////////////////////////////
	// ArchivedFileInfo
	//////////////////////////////////////////////////////////////

	/**
	 * @param archivedTicket
	 * @return the files of an archived ticket.
	 */
	List<ArchivedFileInfo> getArchivedFileInfos(ArchivedTicket archivedTicket);

	/**
	 * @return the content of a ArchivedFileInfo.
	 * @param archivedFileInfo
	 */
	byte[] getArchivedFileInfoContent(ArchivedFileInfo archivedFileInfo);

	//////////////////////////////////////////////////////////////
	// TicketView
	//////////////////////////////////////////////////////////////

	/**
	 * @param user
	 * @param ticket
	 * @return the time the ticket was last viewed by the user, null if not.
	 */
	Timestamp getTicketLastView(User user, Ticket ticket);

	/**
	 * Set the last time that the ticket was viewed by the user (not viewed if ts is null).
	 * @param user
	 * @param ticket
	 * @param ts
	 */
	void setTicketLastView(User user, Ticket ticket, Timestamp ts);

	/**
	 * @param ticket
	 * @param date
	 * @return true if the ticket has changed since the given date.
	 */
	boolean hasTicketChangedSince(
			final Ticket ticket,
			final Timestamp date);

	/**
	 * @param ticket
	 * @param user
	 * @return true if the ticket has changed since the last time the user viewed it.
	 */
	boolean hasTicketChangedSinceLastView(
			final Ticket ticket,
			final User user);

	//////////////////////////////////////////////////////////////
	// TicketMonitoring
	//////////////////////////////////////////////////////////////

	/**
	 * @param ticket
	 * @return the monitorings of a ticket.
	 */
	List<TicketMonitoring> getTicketMonitorings(Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user monitors the ticket.
	 */
	boolean userMonitorsTicket(User user, Ticket ticket);

	/**
	 * Set a ticket monitoring.
	 * @param user
	 * @param ticket
	 */
	void setTicketMonitoring(User user, Ticket ticket);

	/**
	 * Unset a ticket monitoring.
	 * @param user
	 * @param ticket
	 */
	void unsetTicketMonitoring(User user, Ticket ticket);

	/**
	 * Send the alerts for a ticket.
	 * @param author
	 * @param ticket
	 * @param excludedUsers
	 * @param expiration 
	 */
	void ticketMonitoringSendAlerts(
			User author,
			Ticket ticket,
			List<User> excludedUsers,
			boolean expiration);

	/**
	 * @param ticket
	 * @return the users that monitor a ticket.
	 */
	List<User> getMonitoringUsers(Ticket ticket);

	//////////////////////////////////////////////////////////////
	// Alert
	//////////////////////////////////////////////////////////////

	/**
	 * @param action
	 * @return the alerts of an action.
	 */
	List<Alert> getAlerts(Action action);

	/**
	 * Add an alert for a user.
	 * @param action
	 * @param user
	 */
	void addAlert(
			Action action,
			User user);

	/**
	 * Add an alert for an email.
	 * @param action
	 * @param email
	 */
	void addAlert(
			Action action,
			String email);

	//////////////////////////////////////////////////////////////
	// Invitation
	//////////////////////////////////////////////////////////////

	/**
	 * @return true if the user is invited for the ticket.
	 * @param user
	 * @param ticket
	 */
	boolean isInvited(User user, Ticket ticket);

	/**
	 * @return the invitations of a ticket.
	 * @param ticket
	 */
	List<Invitation> getInvitations(Ticket ticket);

	/**
	 * @return true if the user is invited for the ticket.
	 * @param user
	 * @param archivedTicket
	 */
	boolean isInvited(User user, ArchivedTicket archivedTicket);

	/**
	 * @param archivedTicket
	 * @return the invitations of a ticket.
	 */
	List<ArchivedInvitation> getArchivedInvitations(ArchivedTicket archivedTicket);

	/**
	 * @param department
	 * @return the invitations for a department
	 */
	List<DepartmentInvitation> getDepartmentInvitations(
			Department department);

	/**
	 * @param department
	 * @param user
	 * @return true if the user is invited for the department
	 */
	boolean isDepartmentInvited(
			Department department,
			User user);

	/**
	 * Add a department invitation.
	 * @param department
	 * @param user
	 */
	void addDepartmentInvitation(
			Department department,
			User user);

	/**
	 * Delete a department invitation.
	 * @param departmentInvitation
	 */
	void deleteDepartmentInvitation(
			DepartmentInvitation departmentInvitation);

	/**
	 * @return the users invited by a user.
	 * @param author
	 */
	List<User> getInvitedUsers(User author);

	//////////////////////////////////////////////////////////////
	// DeletedItem
	//////////////////////////////////////////////////////////////

	/**
	 * @return all the DeletedItem instances of the database.
	 */
	List<DeletedItem> getDeletedItems();

	/**
	 * Delete a DeletedItem.
	 * @param deletedItem
	 */
	void deleteDeletedItem(DeletedItem deletedItem);

	/**
	 * Delete all the DeletedItem instances.
	 */
	void deleteAllDeletedItems();

	//////////////////////////////////////////////////////////////
	// Bookmark
	//////////////////////////////////////////////////////////////

	/**
	 * @param user
	 * @return the bookmarks of a user.
	 */
	List<Bookmark> getBookmarks(User user);

	/**
	 * @param ticket
	 * @return the bookmarks of a ticket.
	 */
	List<Bookmark> getBookmarks(Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return a bookmark, or null.
	 */
	Bookmark getBookmark(User user, Ticket ticket);

	/**
	 * @param user
	 * @param archivedTicket
	 * @return a bookmark, or null.
	 */
	Bookmark getBookmark(User user, ArchivedTicket archivedTicket);

	/**
	 * Delete a Bookmark.
	 * @param bookmark
	 */
	void deleteBookmark(Bookmark bookmark);

	/**
	 * Add a Bookmark.
	 * @param user
	 * @param ticket
	 */
	void addBookmark(User user, Ticket ticket);

	/**
	 * Add a Bookmark.
	 * @param user
	 * @param archivedTicket
	 */
	void addBookmark(User user, ArchivedTicket archivedTicket);

	//////////////////////////////////////////////////////////////
	// HistoryItem
	//////////////////////////////////////////////////////////////

	/**
	 * @param user
	 * @return the history items of a user.
	 */
	List<HistoryItem> getHistoryItems(User user);

	/**
	 * Add a history item.
	 * @param user
	 * @param ticket
	 */
	void addHistoryItem(User user, Ticket ticket);

	/**
	 * Add a history item.
	 * @param user
	 * @param archivedTicket
	 */
	void addHistoryItem(User user, ArchivedTicket archivedTicket);

	/**
	 * Clear the history items of a user.
	 * @param user
	 */
	void clearHistoryItems(User user);

	/**
	 * Clear all the history items.
	 */
	void clearHistoryItems();

	//////////////////////////////////////////////////////////////
	// Response
	//////////////////////////////////////////////////////////////

	/**
	 * Add a response.
	 * @param response
	 */
	void addResponse(Response response);

	/**
	 * Update a response.
	 * @param response
	 */
	void updateResponse(
			Response response);

	/**
	 * Delete a response.
	 * @param response
	 */
	void deleteResponse(
			Response response);

	/**
	 * @param user
	 * @return the responses of a user.
	 */
	List<Response> getUserResponses(
			User user);

	/**
	 * @param department
	 * @return the responses of a department.
	 */
	List<Response> getDepartmentResponses(
			Department department);

	/**
	 * @return the global responses.
	 */
	List<Response> getGlobalResponses();

	//////////////////////////////////////////////////////////////
	// Icons
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the icon for a given id.
	 */
	Icon getIcon(long id);

	/**
	 * @return the icons.
	 */
	List<Icon> getIcons();

	/**
	 * Add an icon.
	 * @param name
	 * @return the Icon created.
	 */
	Icon addIcon(String name);

	/**
	 * Create an icon from a local PNG file.
	 * @param iconName
	 * @return an icon.
	 */
	Icon createIconFromLocalPngFile(String iconName);

	/**
	 * Delete an icon.
	 * @param icon
	 */
	void deleteIcon(Icon icon);

	/**
	 * Update an icon.
	 * @param icon
	 */
	void updateIcon(Icon icon);

	/**
	 * @param name
	 * @return The icon with the given name or null.
	 */
	Icon getIconByName(String name);

	//////////////////////////////////////////////////////////////
	// VersionManager
	//////////////////////////////////////////////////////////////

	/**
	 * @return the database version.
	 * @throws ConfigException when the database is not initialized
	 */
	Version getDatabaseVersion() throws ConfigException;

	/**
	 * Set the database version.
	 * @param version
	 */
	void setDatabaseVersion(Version version);

	/**
	 * Set the database version.
	 * @param version
	 */
	void setDatabaseVersion(String version);

	//////////////////////////////////////////////////////////////
	// State
	//////////////////////////////////////////////////////////////

	/**
	 * Set the upgrade state.
	 * @param upgradeState
	 */
	void setUpgradeState(String upgradeState);

	/**
	 * @return the state of the upgrade process.
	 */
	String getUpgradeState();

	//////////////////////////////////////////////////////////////
	// Config
	//////////////////////////////////////////////////////////////

	/**
	 * Set the time when the tickets were indexed.
	 * @param lastIndexTime
	 */
	void setTicketsLastIndexTime(final Timestamp lastIndexTime);

	/**
	 * @return the time when the tickets were indexed.
	 */
	Timestamp getTicketsLastIndexTime();

	/**
	 * Set the time when the FAQs were indexed.
	 * @param lastIndexTime
	 */
	void setFaqsLastIndexTime(final Timestamp lastIndexTime);

	/**
	 * @return the time when the FAQs were indexed.
	 */
	Timestamp getFaqsLastIndexTime();

	/**
	 * Set the time when the archived tickets were indexed.
	 * @param lastIndexTime
	 */
	void setArchivedTicketsLastIndexTime(final Timestamp lastIndexTime);

	/**
	 * @return the time when the archived tickets were indexed.
	 */
	Timestamp getArchivedTicketsLastIndexTime();

	/**
	 * Reset all the index times.
	 */
	void resetIndexTimes();

	/**
	 * Set the default icon for departments.
	 * @param icon
	 */
	void setDefaultDepartmentIcon(Icon icon);

	/**
	 * Set the default icon for categories.
	 * @param icon
	 */
	void setDefaultCategoryIcon(Icon icon);

	/**
	 * @return the default icon for departments.
	 */
	Icon getDefaultDepartmentIcon();

	/**
	 * @return the default icon for categories.
	 */
	Icon getDefaultCategoryIcon();

	/**
	 * @return the install date.
	 */
	Timestamp getInstallDate();

	/**
	 * @return the department selection context time (the last time that the context of the department selection has changed).
	 */
	Timestamp getDepartmentSelectionContextTime();

	//////////////////////////////////////////////////////////////
	// Department selection config
	//////////////////////////////////////////////////////////////

    /**
     * @return the department selection config.
     */
    DepartmentSelectionConfig getDepartmentSelectionConfig();

    /**
     * Add a new department selection config.
     * @param author
     * @param data
     */
    void setDepartmentSelectionConfig(
    		User author,
    		String data);

	//////////////////////////////////////////////////////////////
	// FaqLink
	//////////////////////////////////////////////////////////////

	/**
	 * @param department
	 * @return all the faq links of a department.
	 */
	List<FaqLink> getFaqLinks(Department department);

	/**
	 * @param category
	 * @return all the faq links of a category.
	 */
	List<FaqLink> getFaqLinks(Category category);

	/**
	 * Add a faq link.
	 * @param faqLink
	 */
	void addFaqLink(FaqLink faqLink);

	/**
	 * Update a faq link.
	 * @param faqLink
	 */
	void updateFaqLink(FaqLink faqLink);

	/**
	 * Delete a faq link.
	 * @param faqLink
	 */
	void deleteFaqLink(FaqLink faqLink);

	/**
	 * Change the order of a faq link.
	 * @param faqLink
	 */
	void moveFaqLinkUp(FaqLink faqLink);

	/**
	 * Change the order of a faq link.
	 * @param faqLink
	 */
	void moveFaqLinkDown(FaqLink faqLink);

	/**
	 * Change the order of a faq link.
	 * @param faqLink
	 */
	void moveFaqLinkFirst(FaqLink faqLink);

	/**
	 * Change the order of a faq link.
	 * @param faqLink
	 */
	void moveFaqLinkLast(FaqLink faqLink);

	/**
	 * @return the effective faq links of a category.
	 * @param category
	 */
	List<FaqLink> getEffectiveFaqLinks(
			Category category);

	/**
	 * @return the inherited faq links of a category.
	 * @param category
	 */
	List<FaqLink> getInheritedFaqLinks(
			Category category);

	//////////////////////////////////////////////////////////////
	// Authorizations
	//////////////////////////////////////////////////////////////

	/**
	 * @param currentUser
	 * @return true if the user can view administrators.
	 */
	boolean userCanViewAdmins(User currentUser);

	/**
	 * @param user
	 * @return true if the user can grant the privileges of administrator.
	 */
	boolean userCanAddAdmin(User user);

	/**
	 * @param user
	 * @param admin
	 * @return true if the user can revoke the privileges of an administrator.
	 */
	boolean userCanDeleteAdmin(User user, User admin);

	/**
	 * @param currentUser
	 * @return true if the user can view the departments.
	 */
	boolean userCanViewDepartments(User currentUser);

	/**
	 * @param user
	 * @return true if the user can manage the departments.
	 */
	boolean userCanManageDepartments(User user);

	/**
	 * @param user
	 * @param department
	 * @return true if the user can edit the properties of a department.
	 */
	boolean userCanEditDepartmentProperties(User user, Department department);

	/**
	 * @param user
	 * @param department
	 * @return true if the user can edit the managers of a department.
	 */
	boolean userCanEditDepartmentManagers(User user, Department department);

	/**
	 * @param user
	 * @param department
	 * @return true if the user can edit the categories of a department.
	 */
	boolean userCanEditDepartmentCategories(User user, Department department);

	/**
	 * @param user
	 * @param department
	 * @return true if the user can view the properties of a department.
	 */
	boolean userCanViewDepartment(User user, Department department);

	/**
	 * @param user
	 * @param department
	 * @return true if the user can delete a department.
	 */
	boolean userCanDeleteDepartment(User user, Department department);

	/**
	 * @param user
	 * @param client
	 * @param ticket
	 * @return true if the user can view the ticket.
	 */
	boolean userCanViewTicket(User user, InetAddress client, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @param visibleDepartments
	 * @return true if the user can view the ticket.
	 */
	boolean userCanViewTicket(User user, Ticket ticket, List<Department> visibleDepartments);

	/**
	 * @param user
	 * @param invited true if the user is invited to see the ticket
	 * @param action
	 * @return true if the user can view the action message.
	 */
	boolean userCanViewActionMessage(User user, boolean invited, Action action);

	/**
	 * @param user
	 * @param invited true if the user is invited to see the ticket
	 * @param fileInfo
	 * @return true if the user can download the file.
	 */
	boolean userCanDownload(User user, boolean invited, FileInfo fileInfo);

	/**
	 * @param user
	 * @param invited true if the user is invited for the action
	 * @param action
	 * @return true if the action can be monitored by the user.
	 */
	boolean actionMonitorable(User user, boolean invited, Action action);

	/**
	 * @param user
	 * @param client
	 * @param archivedTicket
	 * @return true if the user can view the archived ticket.
	 */
	boolean userCanViewArchivedTicket(
			User user,
			InetAddress client,
			ArchivedTicket archivedTicket);

	/**
	 * @param user
	 * @param archivedTicket
	 * @param visibleDepartments
	 * @return true if the user can view the archived ticket.
	 */
	boolean userCanViewArchivedTicket(
			User user,
			ArchivedTicket archivedTicket,
			List<Department> visibleDepartments);

	/**
	 * @param user
	 * @param invited
	 * @param archivedAction
	 * @return true if the user can view the archived action.
	 */
	boolean userCanViewArchivedAction(
			User user,
			boolean invited,
			ArchivedAction archivedAction);

	/**
	 * @param user
	 * @param invited
	 * @param archivedFileInfo
	 * @return true if the user can download the archived file.
	 */
	boolean userCanDownload(
			User user,
			boolean invited,
			ArchivedFileInfo archivedFileInfo);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can approve the closure of the ticket.
	 */
	boolean userCanApproveClosure(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can refuse the closure of the ticket.
	 */
	boolean userCanRefuseClosure(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can add information to the ticket.
	 */
	boolean userCanGiveInformation(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can cancel the ticket.
	 */
	boolean userCanCancel(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can request information for the ticket.
	 */
	boolean userCanRequestInformation(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can close the ticket.
	 */
	boolean userCanClose(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can refuse the ticket.
	 */
	boolean userCanRefuse(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can connect the ticket.
	 */
	boolean userCanConnect(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can postpone the ticket.
	 */
	boolean userCanPostpone(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can cancel the postponement of the ticket.
	 */
	boolean userCanCancelPostponement(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can re-open the ticket.
	 */
	boolean userCanReopen(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can move the ticket.
	 */
	boolean userCanMove(User user, Ticket ticket);
	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can move the ticket to the before categorie.
	 */
	boolean userCanMoveBack(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can take the ticket.
	 */
	boolean userCanTake(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can take the ticket and close it.
	 */
	boolean userCanTakeAndClose(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can take the ticket and request information.
	 */
	boolean userCanTakeAndRequestInformation(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can Free the ticket.
	 */
	boolean userCanFree(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can assign the ticket.
	 */
	boolean userCanAssign(User user, Ticket ticket);

	/**
	 * @param user
	 * @param department
	 * @return true if the current user can set the owner of new tickets in the department.
	 */
	boolean userCanSetOwner(User user, Department department);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can change the owner of the ticket.
	 */
	boolean userCanChangeOwner(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can change the label of the ticket.
	 */
	boolean userCanChangeLabel(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can change the scope of the ticket.
	 */
	boolean userCanChangeScope(User user, Ticket ticket);

	/**
	 * @param user
	 * @param department
	 * @return true if the current user can manually set the origin of new tickets in the department
	 */
	boolean userCanSetOrigin(User user, Department department);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can change the origin of the ticket.
	 */
	boolean userCanChangeOrigin(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can change the computer of the ticket.
	 */
	boolean userCanChangeComputer(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can change the priority of the ticket.
	 */
	boolean userCanChangePriority(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can change the spent time of the ticket.
	 */
	boolean userCanChangeSpentTime(User user, Ticket ticket);

	/**
	 * @param user
	 * @param action
	 * @return true if the current user can change the scope of the action.
	 */
	boolean userCanChangeActionScope(User user, Action action);

	/**
	 * @param user
	 * @param fileInfo
	 * @return true if the current user can change the scope of the file.
	 */
	boolean userCanChangeFileInfoScope(User user, FileInfo fileInfo);

	/**
	 * @param user
	 * @param client
	 * @param faq
	 * @return true if the current user can view the FAQ.
	 */
	boolean userCanViewFaq(
			User user,
			InetAddress client,
			Faq faq);

	/**
	 * @param user
	 * @param faq
	 * @param visibleDepartments
	 * @return true if the current user can view the FAQ.
	 */
	boolean userCanViewFaq(
			User user,
			Faq faq,
			List<Department> visibleDepartments);

	/**
	 * @param user
	 * @return true if the user can edit the FAQs.
	 */
	boolean userCanEditFaqs(User user);

	/**
	 * @param user
	 * @param faq
	 * @return true if the current user can edit the FAQ.
	 */
	boolean userCanEditFaq(User user, Faq faq);

	/**
	 * @param user
	 * @param department
	 * @return true if the user can edit the FAQs of a department.
	 */
	boolean userCanEditDepartmentFaqs(User user, Department department);

	/**
	 * @param user
	 * @return true if the user can edit the root FAQs.
	 */
	boolean userCanEditRootFaqs(User user);

	/**
	 * @param user
	 * @param client
	 * @return true if the user can view one existing FAQ.
	 */
	boolean hasVisibleFaq(
			User user,
			InetAddress client);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can invite for the ticket.
	 */
	boolean userCanInvite(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can invite a group for the ticket.
	 */
	boolean userCanInviteGroup(User user, Ticket ticket);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the current user can remove invitations of the ticket.
	 */
	boolean userCanRemoveInvitations(User user, Ticket ticket);

	/**
	 * @param user
	 * @return true if the user can edit the department selection config.
	 */
	boolean userCanEditDepartmentSelection(User user);

	/**
	 * @param user
	 * @param ticket
	 * @return true if the user can use canned responses.
	 */
	boolean userCanUseResponses(User user, Ticket ticket);

	/**
	 * @param user
	 * @return true if the user can manage global responses.
	 */
	boolean userCanManageGlobalResponses(User user);

	/**
	 * @param user
	 * @param department
	 * @return true if the user can manage the responses of a department.
	 */
	boolean userCanManageDepartmentResponses(User user, Department department);

	/**
	 * @param user
	 * @return true if the user can manage the icons.
	 */
	boolean userCanManageIcons(User user);


	/**
	 * @param currentUser
	 * @param departmentManager
	 * @return true if the user can set the availability of a department manager.
	 */
	boolean userCanSetAvailability(
			User currentUser,
			DepartmentManager departmentManager);

	//////////////////////////////////////////////////////////////
	// Reports
	//////////////////////////////////////////////////////////////

	/**
	 * Send the ticket reports for the current hour.
	 */
	void sendTicketReports();

	/**
	 * Send a report to a department manager.
	 * @param manager
	 */
	void sendTicketReport(DepartmentManager manager);

	/**
	 * @return the ready-to-print content of a ticket.
	 * @param user
	 * @param ticket
	 */
	String getTicketPrintContent(
			User user,
			Ticket ticket);

	/**
	 * Send FAQ reports.
	 */
	void sendFaqReports();

	//////////////////////////////////////////////////////////////
	// Deprecated
	//////////////////////////////////////////////////////////////

	/**
	 * @param category
	 * @return the templates of a category.
	 * @deprecated
	 */
	@Deprecated
	List<OldTicketTemplate> getOldTicketTemplates(Category category);

	/**
	 * Delete a ticket template.
	 * @param oldTicketTemplate
	 * @deprecated
	 */
	@Deprecated
	void deleteOldTicketTemplate(OldTicketTemplate oldTicketTemplate);

	/**
	 * @param faqContainer
	 * @return the old parts of a FAQ container.
	 * @deprecated
	 */
	@Deprecated
	List<OldFaqPart> getOldFaqParts(DeprecatedFaqContainer faqContainer);

	/**
	 * Delete an old FAQ part.
	 * @param oldFaqPart
	 * @deprecated
	 */
	@Deprecated
	void deleteOldFaqPart(OldFaqPart oldFaqPart);

	/**
	 * @param faqContainer
	 * @return the old entries of a FAQ container.
	 * @deprecated
	 */
	@Deprecated
	List<OldFaqEntry> getOldFaqEntries(DeprecatedFaqContainer faqContainer);

	/**
	 * @param oldFaqPart
	 * @return the old entries of an old FAQ part.
	 * @deprecated
	 */
	@Deprecated
	List<OldFaqEntry> getOldFaqEntries(OldFaqPart oldFaqPart);

	/**
	 * Delete an old FAQ entry.
	 * @param oldFaqEntry
	 * @deprecated
	 */
	@Deprecated
	void deleteOldFaqEntry(OldFaqEntry oldFaqEntry);

	/**
	 * @param oldFaqEntry
	 * @return the tickets connected to an old FAQ entry.
	 * @deprecated
	 */
	@Deprecated
	List<Ticket> getTicketsConnectedToOldFaqEntry(OldFaqEntry oldFaqEntry);

	/**
	 * @param oldFaqPart
	 * @return the tickets contected to an old FAQ part.
	 * @deprecated
	 */
	@Deprecated
	List<Ticket> getTicketsConnectedToOldFaqPart(OldFaqPart oldFaqPart);

	/**
	 * @param department
	 * @return the orphen (v2 concept) tickets of a department.
	 * @deprecated
	 */
	@Deprecated
	List<Ticket> getOrphenTickets(Department department);

	/**
	 * @param department
	 * @return true if the department has orphen (v2 concept) tickets.
	 * @deprecated
	 */
	@Deprecated
	boolean hasOrphenTickets(Department department);

	/**
	 * @param oldFaqEntry
	 * @return the actions connected to an old FAQ entry.
	 * @deprecated
	 */
	@Deprecated
	List<Action> getActionsConnectedToOldFaqEntry(OldFaqEntry oldFaqEntry);

	/**
	 * @param oldFaqPart
	 * @return the actions connected to an old FAQ part.
	 * @deprecated
	 */
	@Deprecated
	List<Action> getActionsConnectedToOldFaqPart(OldFaqPart oldFaqPart);

	/**
	 * @param startIndex the index of the first action to retrieve
	 * @param maxResults the maximum number of actions to retrieve
	 * @return the actions within a given range.
	 * @deprecated
	 */
	@Deprecated
	List<Action> getV2ActionsToUpgradeToV3(long startIndex, int maxResults);

	/**
	 * @return the v2 invitations.
	 * @deprecated
	 */
	@Deprecated
	List<Action> getV2Invitations();

	/**
	 * Migrate a v2 invitation.
	 * @param action
	 * @deprecated
	 */
	@Deprecated
	void migrateV2Invitation(Action action);

	/**
	 * @return the content of a OldFileInfo.
	 * @param oldFileInfo
	 * @deprecated
	 */
	@Deprecated
	byte[] getOldFileInfoContent(OldFileInfo oldFileInfo);

	/**
	 * Delete a OldFileInfo.
	 * @param oldFileInfo
	 * @deprecated
	 */
	@Deprecated
	void deleteOldFileInfo(OldFileInfo oldFileInfo);

	/**
	 * @return the first actions with an (old) attached file.
	 * @param maxResults
	 * @deprecated
	 */
	@Deprecated
	List<Action> getActionsWithAttachedFile(int maxResults);

	/**
	 * @return the v2 archived invitations.
	 * @deprecated
	 */
	@Deprecated
	List<ArchivedAction> getV2ArchivedInvitations();

	/**
	 * Migrate a v2 archived invitation.
	 * @param archivedAction
	 * @deprecated
	 */
	@Deprecated
	void migrateV2ArchivedInvitation(ArchivedAction archivedAction);

	/**
	 * @return categories that inherit members.
	 * @deprecated
	 */
	@Deprecated
	List<Category> getInheritingMembersCategories();

	/**
	 * Upgrade a ticket to 3.4.0.
	 * @param ticket
	 * @deprecated
	 */
	@Deprecated
	void upgradeTicketTo3d4d0(
			Ticket ticket);

	/**
	 * @return the id of the last archived ticket.
	 * @deprecated
	 */
	@Deprecated
	long getLastArchivedTicketId();

	/**
	 * Upgrade an archived ticket to 3.4.0.
	 * @param archivedTicket
	 * @deprecated
	 */
	@Deprecated
	void upgradeArchivedTicketTo3d4d0(
			ArchivedTicket archivedTicket);

	/**
	 * @return the id of the last ticket.
	 * @deprecated
	 */
	@Deprecated
	long getLastTicketId();

	/**
	 * Upgrade categories for version 3.5.7.
	 * @deprecated
	 */
	@Deprecated
	void setDefaultOldPriorityLevelToCategories();

	/**
	 * @return the id of the last action.
	 * @deprecated
	 */
	@Deprecated
	long getLastActionId();

	/**
	 * @return the id of the last archived action.
	 * @deprecated
	 */
	@Deprecated
	long getLastArchivedActionId();

	/**
	 * Remove empty action messages (upgrade to 3.17.0).
	 * @deprecated
	 */
	@Deprecated
	void setToNullEmpyActionMessages();

	/**
	 * Sets sequence id for PostgreSQL according to bean IDs.
	 * @param beanName Name of bean from where IDs should be altered
	 * @param sequenceName Name of corresponding sequence
	 * @deprecated
	 */
	@Deprecated
	void updateBeanSequence(String beanName, String sequenceName);

	/**
	 * @param maxResults
	 * @return the old users (upgrade to 3.24.0).
	 * @deprecated
	 */
	@Deprecated
	List<User> getUsersWithNullAuthType(int maxResults);

	/**
	 * Add a user (upgrade to 3.24.0).
	 * @param user
	 * @deprecated
	 */
	@Deprecated
	void addUser(User user);

	/**
	 * Update a field of a class with the new users (upgrade to 3.24.0).
	 * @param classname
	 * @param field
	 * @deprecated
	 */
	@Deprecated
	void upgradeUserKeys(String classname, String field);

	/**
	 * Set new users (upgrade to 3.24.0).
	 * @deprecated
	 */
	@Deprecated
	void deleteUsersWithNoneAuthType();

	/**
	 * @return all the FAQ containers
	 * @deprecated
	 */
	@Deprecated
	List<DeprecatedFaqContainer> getFaqContainers();

	/**
	 * Add a FAQ container.
	 * @param faqContainer
	 * @deprecated
	 */
	@Deprecated
	void addFaqContainer(DeprecatedFaqContainer faqContainer);

	/**
	 * Update a FAQ container.
	 * @param faqContainer
	 * @deprecated
	 */
	@Deprecated
	void updateFaqContainer(DeprecatedFaqContainer faqContainer);

	/**
	 * Delete a FAQ container.
	 * @param faqContainer
	 * @deprecated
	 */
	@Deprecated
	void deleteFaqContainer(DeprecatedFaqContainer faqContainer);

	/**
	 * @return the root FAQ containers.
	 * @deprecated
	 */
	@Deprecated
	List<DeprecatedFaqContainer> getRootFaqContainers();

	/**
	 * @param department
	 * @return the root FAQ containers of a department.
	 * @deprecated
	 */
	@Deprecated
	List<DeprecatedFaqContainer> getRootFaqContainers(Department department);

	/**
	 * @param faqContainer
	 * @return the sub FAQ containers of a FAQ container.
	 * @deprecated
	 */
	@Deprecated
	List<DeprecatedFaqContainer> getSubFaqContainers(DeprecatedFaqContainer faqContainer);

	/**
	 * @return all the FAQ entries
	 * @deprecated
	 */
	@Deprecated
	List<DeprecatedFaqEntry> getFaqEntries();

	/**
	 * @param faqContainer
	 * @return all the FAQ entries of a FAQ container.
	 * @deprecated
	 */
	@Deprecated
	List<DeprecatedFaqEntry> getFaqEntries(DeprecatedFaqContainer faqContainer);

	/**
	 * Add a FAQ entry.
	 * @param faqEntry
	 * @deprecated
	 */
	@Deprecated
	void addFaqEntry(DeprecatedFaqEntry faqEntry);

	/**
	 * Update a FAQ entry.
	 * @param faqEntry
	 * @deprecated
	 */
	@Deprecated
	void updateFaqEntry(DeprecatedFaqEntry faqEntry);

	/**
	 * Delete a FAQ entry.
	 * @param faqEntry
	 * @deprecated
	 */
	@Deprecated
	void deleteFaqEntry(DeprecatedFaqEntry faqEntry);

	/**
	 * @return the time when the tickets were indexed (deprecated).
	 * @deprecated
	 */
	@Deprecated
	Timestamp getDeprecatedTicketsLastIndexTime();

	/**
	 * @return the time when the FAQ containers were indexed (deprecated).
	 * @deprecated
	 */
	@Deprecated
	Timestamp getDeprecatedFaqContainersLastIndexTime();

	/**
	 * @return the time when the FAQ entries were indexed (deprecated).
	 * @deprecated
	 */
	@Deprecated
	Timestamp getDeprecatedFaqEntriesLastIndexTime();

	/**
	 * @return the time when the archived tickets were indexed (deprecated).
	 * @deprecated
	 */
	@Deprecated
	Timestamp getDeprecatedArchivedTicketsLastIndexTime();

	/**
	 * Migrate the FAQ containers.
	 * @deprecated
	 */
	@Deprecated
	void migrateFaqContainers();

}
