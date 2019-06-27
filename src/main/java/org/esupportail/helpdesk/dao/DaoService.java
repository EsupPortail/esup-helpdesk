/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import org.esupportail.helpdesk.web.beans.StatisticsTicketEntry;
import org.esupportail.helpdesk.web.beans.UserTicketCreationStatisticEntry;
import org.hibernate.Query;

/**
 * The DAO service interface.
 */
public interface DaoService extends Serializable {

	//////////////////////////////////////////////////////////////
	// User
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the User instance that corresponds to an id.
	 */
	User getUser(String id);

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
	 * Add a user.
	 * @param user
	 */
	void addUser(User user);

	/**
	 * Delete a user.
	 * @param user
	 */
	void deleteUser(User user);

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
	 * @param authSecret
	 * @return the user with the given auth secret.
	 */
	User getUserWithAuthSecret(String authSecret);

	//////////////////////////////////////////////////////////////
	// Department
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the Department instance that corresponds to an id.
	 */
	Department getDepartment(long id);

	/**
	 * @return the list of all the departments.
	 */
	List<Department> getDepartments();

	/**
	 * @return the list of all the enabled departments.
	 */
	List<Department> getEnabledDepartments();

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
	 */
	void deleteDepartment(Department department);

	/**
	 * @param label
	 * @return 'true' if a department has the same name of label.
	 */
	boolean isDepartmentLabelUsed(String label);

	/**
	 * @param i
	 * @return the department that corresponds to an order.
	 */
	Department getDepartmentByOrder(int i);

	/**
	 * @param department
	 * @return the virtual departments of the department (pointed to itself).
	 */
	List<Department> getVirtualDepartments(Department department);

	/**
	 * @param department
	 * @return the number of virtual departments (pointed to itself).
	 */
	int getVirtualDepartmentsNumber(Department department);

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

	//////////////////////////////////////////////////////////////
	// DepartmentManager
	//////////////////////////////////////////////////////////////

	/**
	 * @param department
	 * @param user
	 * @return the departmentManager instance that corresponds to a
	 * department and a user, null if not found.
	 */
	DepartmentManager getDepartmentManager(
			Department department,
			User user);

	/**
	 * @param user
	 * @return true if the user can manage the FAQs.
	 */
	boolean isFaqDepartmentManager(
			User user);

	/**
	 * @return the list of all the department managers.
	 */
	List<DepartmentManager> getDepartmentManagers();

	/**
	 * @param department
	 * @return the managers of a department
	 */
	List<DepartmentManager> getDepartmentManagers(Department department);

	/**
	 * @param department
	 * @return the managers of a department who are available
	 */
	List<DepartmentManager> getAvailableDepartmentManagers(Department department);

	/**
	 * @param department
	 * @return the number of department managers of a department.
	 */
	int getDepartmentManagersNumber(Department department);

	/**
	 * @param department
	 * @param i
	 * @return the manager that corresponds to an order for a given department.
	 */
	DepartmentManager getDepartmentManagerByOrder(Department department, int i);

	/**
	 * Add a department manager.
	 * @param departmentManager
	 */
	void addDepartmentManager(DepartmentManager departmentManager);

	/**
	 * Update a departmentManager. (manage managers).
	 * @param departmentManager
	 */
	void updateDepartmentManager(DepartmentManager departmentManager);

	/**
	 * Delete a department manager.
	 * @param departmentManager
	 */
	void deleteDepartmentManager(DepartmentManager departmentManager);

	/**
	 * @param user
	 * @return the instances of DepartmentManager that corresponds to the user.
	 */
	List<DepartmentManager> getDepartmentManagers(User user);

	//////////////////////////////////////////////////////////////
	// Category
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the Category instance that corresponds to an id.
	 */
	Category getCategory(long id);

	/**
	 * @param department
	 * @return all the categories of a department.
	 */
	List<Category> getCategories(Department department);

	/**
	 * @param department
	 * @param scope
	 * @return the categories of a department with a given effective default ticket scope.
	 */
	List<Category> getCategoriesWithEffectiveDefaultTicketScope(Department department, String scope);

	/**
	 * @return all the categories.
	 */
	List<Category> getCategories();

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
	 * @param category
	 * @return the sub categories of a category.
	 */
	List<Category> getSubCategories(Category category);

	/**
	 * @param department
	 * @return the number of root categories of a department.
	 */
	int getRootCategoriesNumber(Department department);

	/**
	 * @param category
	 * @return the number of sub categories of a category.
	 */
	int getSubCategoriesNumber(Category category);

	/**
	 * @param department
	 * @param parent
	 * @param i
	 * @return the category that corresponds to an order at a given point of the hierarchy.
	 */
	Category getCategoryByOrder(Department department, Category parent, int i);

	/**
	 * @param category
	 * @return the virtual categories of the category (pointed to itself).
	 */
	List<Category> getVirtualCategories(Category category);

	/**
	 * @param category
	 * @return the number of virtual categories (pointed to itself).
	 */
	int getVirtualCategoriesNumber(Category category);

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
	 * @return the CategoryMember instance that corresponds to a
	 * category and a user, null if not found.
	 */
	CategoryMember getCategoryMember(
			Category category,
			User user);

	/**
	 * @param category
	 * @return the members of a category
	 */
	List<CategoryMember> getCategoryMembers(Category category);

	/**
	 * @param category
	 * @return the number of members of a category.
	 */
	int getCategoryMembersNumber(Category category);

	/**
	 * @return all the category members.
	 */
	List<CategoryMember> getCategoryMembers();

	/**
	 * Add a category member.
	 * @param categoryMember
	 */
	void addCategoryMember(CategoryMember categoryMember);

	/**
	 * Update a category member.
	 * @param categoryMember
	 */
	void updateCategoryMember(CategoryMember categoryMember);

	/**
	 * Delete a category member.
	 * @param categoryMember
	 */
	void deleteCategoryMember(CategoryMember categoryMember);

	/**
	 * @param user
	 * @return the instances of CategoryMember that corresponds to the user.
	 */
	List<CategoryMember> getCategoryMembers(User user);

	/**
	 * @param user
	 * @param department
	 * @return the instances of CategoryMember that corresponds to the user (limited to a department).
	 */
	List<CategoryMember> getCategoryMembers(User user, Department department);

	/**
	 * @param category
	 * @param i
	 * @return the department that corresponds to an order.
	 */
	CategoryMember getCategoryMemberByOrder(Category category, int i);

	/**
	 * @param departmentManager
	 * @return the category members that correspond to a department manager.
	 */
	List<CategoryMember> getCategoryMembers(DepartmentManager departmentManager);

	//////////////////////////////////////////////////////////////
	// Faq
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the Faq instance that corresponds to an id.
	 */
	Faq getFaq(long id);

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
	 * @param department
	 * @return the root FAQ of a department.
	 */
	List<Faq> getRootFaqs(Department department);

	/**
	 * @param faq
	 * @return the sub FAQs of a FAQ.
	 */
	List<Faq> getSubFaqs(Faq faq);

	/**
	 * @param department
	 * @return the number of root FAQs of a department.
	 */
	int getRootFaqsNumber(Department department);

	/**
	 * @param faq
	 * @return the number of sub FAQs of a FAQ.
	 */
	int getSubFaqsNumber(Faq faq);

	/**
	 * @param department
	 * @param parent
	 * @param i
	 * @return the FAQ that corresponds to an order at a given point of the hierarchy.
	 */
	Faq getFaqByOrder(Department department, Faq parent, int i);

	/**
	 * @param lastUpdate
	 * @param maxResults
	 * @return the FAQs that were changed after a given date.
	 */
	List<Faq> getFaqsChangedAfter(Timestamp lastUpdate, int maxResults);

	/**
	 * @param user
	 * @param faqVisibleDepartments
	 * @return true if at least one FAQ is visible for the user.
	 */
	boolean hasVisibleFaq(
			User user,
			List<Department> faqVisibleDepartments);

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
	 */
	Ticket getTicket(long id);

	/**
	 * @param category
	 * @return the tickets of the category.
	 */
	List<Ticket> getTickets(Category category);

	/**
	 * @param category
	 * @return the number of tickets in the category.
	 */
	int getTicketsNumber(Category category);

	/**
	 * @param department
	 * @return the tickets of the department.
	 */
	List<Ticket> getTickets(Department department);

	/**
	 * @param department
	 * @return the tickets of the user.
	 */
	List<Ticket> getTicketsByOwner(User user);

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
	 * Add a ticket.
	 * @param ticket
	 */
	void addTicket(Ticket ticket);

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
	 * @return the number of tickets.
	 */
	int getTicketsNumber();

	/**
	 * @param startIndex the index of the first ticket to retrieve
	 * @param num the maximum number of tickets to retrieve
	 * @return the tickets within a given range.
	 */
	List<Ticket> getTickets(long startIndex, int num);

	/**
	 * @param category
	 * @param user
	 * @return The opended tickets of the category managed by the user.
	 */
	List<Ticket> getOpenManagedTickets(Category category, User user);

	/**
	 * @param department
	 * @param user
	 * @return The opended tickets of the department managed by the user.
	 */
	List<Ticket> getOpenManagedTickets(Department department, User user);

	/**
	 * @param category
	 * @param user
	 * @return The number of opended tickets of the category managed by the user.
	 */
	int getOpenManagedTicketsNumber(Category category, User user);

	/**
	 * @param department
	 * @param user
	 * @return The number of opended tickets of the department managed by the user.
	 */
	int getOpenManagedTicketsNumber(Department department, User user);

	/**
	 * @param user
	 * @return the tickets owned by a user.
	 */
	List<Ticket> getOwnedTickets(User user);

	/**
	 * Update the effective scope of the tickets of a category.
	 * @param category
	 */
	void updateTicketsEffectiveScope(Category category);

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
	 * @param lastUpdate
	 * @param maxResults
	 * @return the ticket that were changed after a given date.
	 */
	List<Ticket> getTicketsChangedAfter(Timestamp lastUpdate, int maxResults);

	/**
	 * @return the postponed ticket to recall.
	 */
	List<Ticket> getTicketsToRecall();

	/**
	 * @return the oldest ticket date.
	 */
	Timestamp getOldestTicketDate();

	/**
	 * Delete all the tickets.
	 */
	void deleteAllTickets();

//	/**
//	 * Delete all the tickets older than x days.
//	 */
//	void deleteArchivedTickets(final Integer days);

	/**
	 * Retreive all the arcvided tickets older than a date.
	 */
	 List<ArchivedTicket> getArchivedTicketsOlderThan(final Date date);

	//////////////////////////////////////////////////////////////
	// Action
	//////////////////////////////////////////////////////////////

	/**
	 * @param ticket
	 * @param dateAsc true to get the date order, false to inverse
	 * @return the actions of the ticket.
	 */
	List<Action> getActions(Ticket ticket, boolean dateAsc);

	/**
	 * @param ticket
	 * @param dateAsc true to get the date order, false to inverse
	 * @return the actions of the ticket without ACTION_TYPE UPLOAD.
	 */
	List<Action> getActionsWithoutUpload(Ticket ticket, boolean dateAsc);

	/**
	 * @param ticket
	 * @param action type
	 * @param dateAsc true to get the date order, false to inverse
	 * @return the actions of the ticket.
	 */
	List<Action> getActionsByActionType(Ticket ticket, String actionType, boolean dateAsc);


	/**
	 * @param ticket
	 * @return the number of actions of the ticket.
	 */
	int getActionsNumber(Ticket ticket);

	/**
	 * @param ticket
	 * @return the last action of a ticket.
	 */
	Action getLastAction(Ticket ticket);

	/**
	 * @param ticket
	 * @return the last action of a ticket without UPLOAD ACTION_TYPE.
	 */
	Action getLastActionWithoutUpload(Ticket ticket);

	/**
	 * @param ticket
	 * @param action type
	 * @return the last action of a ticket.
	 */
	Action getLastActionByActionType(Ticket ticket, String actionType);

	/**
	 * Add an action.
	 * @param action
	 */
	void addAction(Action action);

	/**
	 * Update an action.
	 * @param action
	 */
	void updateAction(Action action);

	/**
	 * Delete an action.
	 * @param action
	 */
	void deleteAction(Action action);

	/**
	 * @return the number of actions.
	 */
	int getActionsNumber();

	/**
	 * @param startIndex the index of the action ticket to retrieve
	 * @param maxResults the maximum number of actions to retrieve
	 * @return the actions within a given range.
	 */
	List<Action> getActions(long startIndex, int maxResults);

	//////////////////////////////////////////////////////////////
	// FileInfo
	//////////////////////////////////////////////////////////////

	/**
	 * @param ticket
	 * @param dateAsc true to sort by date asc, false to inverse
	 * @return the attached files of the ticket.
	 */
	List<FileInfo> getFileInfos(Ticket ticket, boolean dateAsc);

	/**
	 * @param ticket
	 * @return the number of attached files of the ticket.
	 */
	int getFileInfosNumber(Ticket ticket);

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

	/**
	 * Delete a FileInfo.
	 * @param fileInfo
	 * @param deleteContent
	 */
	void deleteFileInfo(FileInfo fileInfo, boolean deleteContent);

	/**
	 * Delete a archivedFileInfo.
	 * @param archivedFileInfo
	 * @param deleteContent
	 */
	void deleteArchivedFileInfo(ArchivedFileInfo archivedFileInfo, boolean deleteContent);

	//////////////////////////////////////////////////////////////
	// ArchivedTicket
	//////////////////////////////////////////////////////////////

	/**
	 * @return the archived ticket with the given id.
	 * @param id
	 */
	ArchivedTicket getArchivedTicket(long id);

	/**
	 * Add an archived ticket.
	 * @param archivedTicket
	 */
	void addArchivedTicket(ArchivedTicket archivedTicket);

	/**
	 * Update an archived ticket.
	 * @param archivedTicket
	 */
	void updateArchivedTicket(ArchivedTicket archivedTicket);

	/**
	 * Delete an archived ticket.
	 * @param archivedTicket
	 */
	void deleteArchivedTicket(ArchivedTicket archivedTicket);

	/**
	 * @param startIndex the index of the first archived ticket to retrieve
	 * @param num the maximum number of archived tickets to retrieve
	 * @return the archived tickets within a given range.
	 */
	List<ArchivedTicket> getArchivedTickets(long startIndex, int num);

	/**
	 * @param lastUpdate
	 * @param maxResults
	 * @return the archived tickets that were created after a given date.
	 */
	List<ArchivedTicket> getTicketsArchivedAfter(Timestamp lastUpdate, int maxResults);

	/**
	 * @return the archived ticket with the given original id.
	 * @param id
	 */
	ArchivedTicket getArchivedTicketByOriginalId(long id);

	/**
	 * @param department
	 * @return The archived tickets of a department.
	 */
	List<ArchivedTicket> getArchivedTickets(Department department);

	/**
	 * @param department
	 * @return the number of archived tickets in a department.
	 */
	int getArchivedTicketsNumber(Department department);

	/**
	 * @return the number of archived tickets.
	 */
	int getArchivedTicketsNumber();

	//////////////////////////////////////////////////////////////
	// ArchivedAction
	//////////////////////////////////////////////////////////////

	/**
	 * @param archivedTicket
	 * @param dateAsc
	 * @return the actions of an archived ticket.
	 */
	List<ArchivedAction> getArchivedActions(
			ArchivedTicket archivedTicket,
			boolean dateAsc);

	/**
	 * Add an archived action.
	 * @param archivedAction
	 */
	void addArchivedAction(ArchivedAction archivedAction);

	/**
	 * Update an archived action.
	 * @param archivedAction
	 */
	void updateArchivedAction(ArchivedAction archivedAction);

	/**
	 * Delete an archived action.
	 * @param archivedAction
	 */
	void deleteArchivedAction(ArchivedAction archivedAction);

	/**
	 * @param startIndex the index of the archived action ticket to retrieve
	 * @param maxResults the maximum number of archived actions to retrieve
	 * @return the archived actions within a given range.
	 */
	List<ArchivedAction> getArchivedActions(long startIndex, int maxResults);

	//////////////////////////////////////////////////////////////
	// ArchivedFileInfo
	//////////////////////////////////////////////////////////////

	/**
	 * @param archivedTicket
	 * @return the files of an archived ticket.
	 */
	List<ArchivedFileInfo> getArchivedFileInfos(ArchivedTicket archivedTicket);

	/**
	 * Add an archived file.
	 * @param archivedFileInfo
	 */
	void addArchivedFileInfo(ArchivedFileInfo archivedFileInfo);

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
	 * @return the ticket view that corresponds to a user and a ticket.
	 */
	TicketView getTicketView(User user, Ticket ticket);

	/**
	 * Add a TicketView.
	 * @param ticketView
	 */
	void addTicketView(TicketView ticketView);

	/**
	 * Update a TicketView.
	 * @param ticketView
	 */
	void updateTicketView(TicketView ticketView);

	/**
	 * Delete a TicketView.
	 * @param ticketView
	 */
	void deleteTicketView(TicketView ticketView);

	//////////////////////////////////////////////////////////////
	// TicketMonitoring
	//////////////////////////////////////////////////////////////

	/**
	 * @param user
	 * @param ticket
	 * @return the TicketMonitoring that corresponds to a user and a ticket.
	 */
	TicketMonitoring getTicketMonitoring(User user, Ticket ticket);

	/**
	 * @param ticket
	 * @return the TicketMonitorings that corresponds to a ticket.
	 */
	List<TicketMonitoring> getTicketMonitorings(Ticket ticket);

	/**
	 * Add a TicketMonitoring.
	 * @param ticketMonitoring
	 */
	void addTicketMonitoring(TicketMonitoring ticketMonitoring);

	/**
	 * Delete a TicketMonitoring.
	 * @param ticketMonitoring
	 */
	void deleteTicketMonitoring(TicketMonitoring ticketMonitoring);

	//////////////////////////////////////////////////////////////
	// Alert
	//////////////////////////////////////////////////////////////

	/**
	 * Add an alert.
	 * @param alert
	 */
	void addAlert(Alert alert);

	/**
	 * @param action
	 * @return the alerts of an action.
	 */
	List<Alert> getAlerts(Action action);

	//////////////////////////////////////////////////////////////
	// Invitation
	//////////////////////////////////////////////////////////////

	/**
	 * @param user
	 * @param ticket
	 * @return the invitation of the user for the ticket or null.
	 */
	Invitation getInvitation(
			User user,
			Ticket ticket);

	/**
	 * Add an invitation.
	 * @param invitation
	 */
	void addInvitation(
			Invitation invitation);

	/**
	 * Delete an invitation.
	 * @param invitation
	 */
	void deleteInvitation(
			Invitation invitation);

	/**
	 * @param ticket
	 * @return the invitations of a ticket.
	 */
	List<Invitation> getInvitations(
			Ticket ticket);

	/**
	 * @param user
	 * @return the invitations of a user.
	 */
	List<Invitation> getInvitations(
			User user);

	/**
	 * @return the users invited by a user.
	 * @param author
	 */
	List<User> getInvitedUsers(User author);

	//////////////////////////////////////////////////////////////
	// ArchivedInvitation
	//////////////////////////////////////////////////////////////

	/**
	 * @param user
	 * @param archivedTicket
	 * @return the invitation of the user for the ticket or null.
	 */
	ArchivedInvitation getArchivedInvitation(
			User user,
			ArchivedTicket archivedTicket);

	/**
	 * Add an invitation.
	 * @param archivedInvitation
	 */
	void addArchivedInvitation(
			ArchivedInvitation archivedInvitation);

	/**
	 * Delete an invitation.
	 * @param archivedInvitation
	 */
	void deleteArchivedInvitation(
			ArchivedInvitation archivedInvitation);

	/**
	 * @param archivedTicket
	 * @return the invitations of a ticket.
	 */
	List<ArchivedInvitation> getArchivedInvitations(
			ArchivedTicket archivedTicket);

	//////////////////////////////////////////////////////////////
	// Invitation
	//////////////////////////////////////////////////////////////

	/**
	 * @param user
	 * @param department
	 * @return the invitation of the user for the department or null.
	 */
	DepartmentInvitation getDepartmentInvitation(
			User user,
			Department department);

	/**
	 * Add a department invitation.
	 * @param departmentInvitation
	 */
	void addDepartmentInvitation(
			DepartmentInvitation departmentInvitation);

	/**
	 * Delete a department invitation.
	 * @param departmentInvitation
	 */
	void deleteDepartmentInvitation(
			DepartmentInvitation departmentInvitation);

	/**
	 * @param department
	 * @return the invitations of a department.
	 */
	List<DepartmentInvitation> getDepartmentInvitations(
			Department department);

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

	/**
	 * Add a deleted item.
	 * @param deletedItem
	 */
	void addDeletedItem(DeletedItem deletedItem);

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
	 * Update a Bookmark.
	 * @param bookmark
	 */
	void updateBookmark(Bookmark bookmark);

	/**
	 * Add a Bookmark.
	 * @param bookmark
	 */
	void addBookmark(Bookmark bookmark);

	//////////////////////////////////////////////////////////////
	// HistoryItem
	//////////////////////////////////////////////////////////////

	/**
	 * @param user
	 * @return the history items of a user.
	 */
	List<HistoryItem> getHistoryItems(User user);

	/**
	 * @param ticket
	 * @return the history items of a ticket.
	 */
	List<HistoryItem> getHistoryItems(Ticket ticket);

	/**
	 * Add a history item.
	 * @param historyItem
	 */
	void addHistoryItem(HistoryItem historyItem);

	/**
	 * Update a history item.
	 * @param historyItem
	 */
	void updateHistoryItem(HistoryItem historyItem);

	/**
	 * Delete a history item.
	 * @param historyItem
	 */
	void deleteHistoryItem(HistoryItem historyItem);

	/**
	 * Clear all the history items of a user.
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
	void addResponse(
			Response response);

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
	// Icon
	//////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the Icon instance that corresponds to an id.
	 */
	Icon getIcon(long id);

	/**
	 * @return the list of all the icons.
	 */
	List<Icon> getIcons();

	/**
	 * Add an icon.
	 * @param icon
	 */
	void addIcon(Icon icon);

	/**
	 * Update an icon.
	 * @param icon
	 */
	void updateIcon(Icon icon);

	/**
	 * Delete an icon.
	 * @param icon
	 */
	void deleteIcon(Icon icon);

	/**
	 * @param name
	 * @return The icon with the given name or null.
	 */
	Icon getIconByName(String name);

	//////////////////////////////////////////////////////////////
	// VersionManager
	//////////////////////////////////////////////////////////////

	/**
	 * @return the VersionManager instance of the database.
	 */
	VersionManager getVersionManager();

	/**
	 * Update a VersionManager.
	 * @param versionManager
	 */
	void updateVersionManager(VersionManager versionManager);

	//////////////////////////////////////////////////////////////
	// Config
	//////////////////////////////////////////////////////////////

	/**
	 * @return the first (and only) Config instance of the database.
	 */
	Config getConfig();

	/**
	 * Update a Config.
	 * @param config
	 */
	void updateConfig(Config config);

	//////////////////////////////////////////////////////////////
	// State
	//////////////////////////////////////////////////////////////

	/**
	 * @return the first (and only) State instance of the database.
	 */
	State getState();

	/**
	 * Update a State.
	 * @param state
	 */
	void updateState(State state);

	//////////////////////////////////////////////////////////////
	// FaqLink
	//////////////////////////////////////////////////////////////

	/**
	 * @param department
	 * @return all the faq links of a department.
	 */
	List<FaqLink> getFaqLinks(Department department);

	/**
	 * @param department
	 * @return all the faq links of a department.
	 */
	List<Faq> getFaqsDepartment(Department department);

	/**
	 * @param category
	 * @return all the faq links of a category.
	 */
	List<FaqLink> getFaqLinks(Category category);

	/**
	 * @param department
	 * @return the number of faq links of a department.
	 */
	int getFaqLinksNumber(Department department);

	/**
	 * @param category
	 * @return the number of faq links of a category.
	 */
	int getFaqLinksNumber(Category category);

	/**
	 * @param department
	 * @param category
	 * @param order
	 * @return the faq link of a department or category at a given order.
	 */
	FaqLink getFaqLinkByOrder(
			Department department,
			Category category,
			int order);

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

	//////////////////////////////////////////////////////////////
	// Statistics
	//////////////////////////////////////////////////////////////

	/**
	 * @param start
	 * @param end
	 * @param statType
	 * @param departments
	 * @param origins
	 * @return the yearly ticket creations between two dates
	 */
	List<YearTicketCreationStatistic> getTicketCreationsByYear(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins);

	/**
	 * @param start
	 * @param end
	 * @param statType
	 * @param departments
	 * @param origins
	 * @return the monthly ticket creations between two dates
	 */
	List<MonthTicketCreationStatistic> getTicketCreationsByMonth(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins);

	/**
	 * @param start
	 * @param end
	 * @param statType
	 * @param departments
	 * @param origins
	 * @return the daily ticket creations between two dates
	 */
	List<DayTicketCreationStatistic> getTicketCreationsByDay(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins);

	/**
	 * @param start
	 * @param end
	 * @param statType
	 * @param departments
	 * @param origins
	 * @return the ticket creations between two dates
	 */
	List<DayOfWeekTicketCreationStatistic> getTicketCreationsByDayOfWeek(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins);

	/**
	 * @param start
	 * @param end
	 * @param statType
	 * @param departments
	 * @param origins
	 * @return the ticket creations between two dates
	 */
	List<HourOfDayTicketCreationStatistic> getTicketCreationsByHourOfDay(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins);

	/**
	 * @param start
	 * @param end
	 * @param statType
	 * @param departments
	 * @param origins
	 * @return the ticket creations between two dates
	 */
	List<HourOfWeekTicketCreationStatistic> getTicketCreationsByHourOfWeek(
			final Timestamp start,
			final Timestamp end,
			final int statType,
			final List<Department> departments,
			final List<String> origins);

	/**
	 * @param start
	 * @param end
	 * @param origins
	 * @return the departments of ticket creations between two dates
	 */
	List<Department> getTicketCreationDepartments(
			Timestamp start,
			Timestamp end,
			final List<String> origins);

	/**
	 * @param start
	 * @param end
	 * @param departments
	 * @return the origins of ticket creations between two dates
	 */
	List<String> getTicketCreationOrigins(
			Timestamp start,
			Timestamp end,
			final List<Department> departments);

	/**
	 * @param start
	 * @param end
	 * @param departments
	 * @param maxEntries
	 * @return the creations between two dates.
	 */
	List<UserTicketCreationStatisticEntry> getUserTicketCreations(
			Timestamp start,
			Timestamp end,
			List<Department> departments,
			int maxEntries);

	/**
	 * @param charge
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time between two dates.
	 */
	List<DayTimeStatistic> getChargeOrClosureTimeByDay(
			boolean charge,
			Timestamp start,
			Timestamp end,
			List<Department> departments);

	/**
	 * @param charge
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time between two dates.
	 */
	List<MonthTimeStatistic> getChargeOrClosureTimeByMonth(
			boolean charge,
			Timestamp start,
			Timestamp end,
			List<Department> departments);

	/**
	 * @param charge
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time between two dates.
	 */
	List<YearTimeStatistic> getChargeOrClosureTimeByYear(
			boolean charge,
			Timestamp start,
			Timestamp end,
			List<Department> departments);

	/**
	 * @param charge
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time between two dates.
	 */
	List<DayOfWeekTimeStatistic> getChargeOrClosureTimeByDayOfWeek(
			boolean charge,
			Timestamp start,
			Timestamp end,
			List<Department> departments);

	/**
	 * @param charge
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time between two dates.
	 */
	List<HourOfDayTimeStatistic> getChargeOrClosureTimeByHourOfDay(
			boolean charge,
			Timestamp start,
			Timestamp end,
			List<Department> departments);

	/**
	 * @param charge
	 * @param start
	 * @param end
	 * @param departments
	 * @return the charge time between two dates.
	 */
	List<HourOfWeekTimeStatistic> getChargeOrClosureTimeByHourOfWeek(
			boolean charge,
			Timestamp start,
			Timestamp end,
			List<Department> departments);

	/**
	 * @param charge
	 * @param start
	 * @param end
	 * @param departments
	 * @param maxEntries
	 * @param hideChargedOrClosed
	 * @return a list of tickets.
	 */
	List<StatisticsTicketEntry> getTicketsWithLongChargeOrClosureTime(
			boolean charge,
			Timestamp start,
			Timestamp end,
			List<Department> departments,
			int maxEntries,
			boolean hideChargedOrClosed);

    /**
     * @param start
     * @param end
     * @param statType
     * @param departments
     * @param ignoreArchivedTickets
     * @return the status statistics for a list of departments and a period.
     */
    List<StatusStatistic> getStatusStatistics(
    		Timestamp start,
    		Timestamp end,
    		int statType,
    		List<Department> departments,
    		boolean ignoreArchivedTickets);

    /**
     * @param start
     * @param end
     * @param departments
     * @return the spent time statistics for a list of departments and a period.
     */
    List<SpentTimeStatistic> getSpentTimeStatistics(
    		Timestamp start,
    		Timestamp end,
    		List<Department> departments);

	//////////////////////////////////////////////////////////////
	// Department selection config
	//////////////////////////////////////////////////////////////

    /**
     * @return the latest department selection config.
     */
    DepartmentSelectionConfig getLatestDepartmentSelectionConfig();

    /**
     * Add a new department selection config.
     * @param config
     */
    void addDepartmentSelectionConfig(DepartmentSelectionConfig config);

	//////////////////////////////////////////////////////////////
	// Used by the control panel paginator
	//////////////////////////////////////////////////////////////

	/**
	 * @param queryString
	 * @return the Hibernate query that corresponds to a string.
	 */
	Query getQuery(final String queryString);

	//////////////////////////////////////////////////////////////
	// Deprecated
	//////////////////////////////////////////////////////////////

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<OldTicketTemplate> getOldTicketTemplates(Category x);

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void deleteOldTicketTemplate(OldTicketTemplate x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<OldFaqPart> getOldFaqParts(DeprecatedFaqContainer x);

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void deleteOldFaqPart(OldFaqPart x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<OldFaqEntry> getOldFaqEntries(DeprecatedFaqContainer x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<OldFaqEntry> getOldFaqEntries(OldFaqPart x);

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void deleteOldFaqEntry(OldFaqEntry x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<Ticket> getTicketsConnectedToOldFaqEntry(OldFaqEntry x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<Ticket> getTicketsConnectedToOldFaqPart(OldFaqPart x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@Deprecated
	List<Ticket> getOrphenTickets(Department x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@Deprecated
	int getOrphenTicketsNumber(Department x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<Action> getActionsConnectedToOldFaqEntry(OldFaqEntry x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<Action> getActionsConnectedToOldFaqPart(OldFaqPart x);

	/**
	 * @param x
	 * @param y
	 * @return x
	 * @deprecated
	 */
	@Deprecated
	List<Action> getV2ActionsToUpgradeToV3(long x, int y);

	/**
	 * @return x
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	byte[] getOldFileInfoContent(OldFileInfo x);

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void deleteOldFileInfo(OldFileInfo x);

	/**
	 * @return x
	 * @deprecated
	 */
	@Deprecated
	List<Action> getV2Invitations();

	/**
	 * @return x
	 * @param x
	 * @deprecated
	 */
	@Deprecated
	List<Action> getV2ActionsWithAttachedFile(int x);

	/**
	 * @return x
	 * @deprecated
	 */
	@Deprecated
	List<ArchivedAction> getV2ArchivedInvitations();

	/**
	 * @return x
	 * @deprecated
	 */
	@Deprecated
	List<Category> getInheritingMembersCategories();

	/**
	 * @return x
	 * @deprecated
	 */
	@Deprecated
	long getLastArchivedTicketId();

	/**
	 * @return x
	 * @deprecated
	 */
	@Deprecated
	long getLastTicketId();

	/**
	 * @deprecated
	 */
	@Deprecated
	void setDefaultOldPriorityLevelToCategories();

	/**
	 * @return x
	 * @deprecated
	 */
	@Deprecated
	long getLastActionId();

	/**
	 * @return x
	 * @deprecated
	 */
	@Deprecated
	long getLastArchivedActionId();

	/**
	 * @deprecated
	 */
	@Deprecated
	void setToNullEmpyActionMessages();

	/**
	 * @param x
	 * @param y
	 * @deprecated
	 */
	@Deprecated
	void updateBeanSequence(String x, String y);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@Deprecated
	List<User> getUsersWithNullAuthType(int x);

	/**
	 * @param x
	 * @param y
	 * @deprecated
	 */
	@Deprecated
	void upgradeUserKeys(String x, String y);

	/**
	 * @deprecated
	 */
	@Deprecated
	void deleteUsersWithNoneAuthType();

	/**
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<DeprecatedFaqContainer> getFaqContainers();

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void addFaqContainer(DeprecatedFaqContainer x);

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void updateFaqContainer(DeprecatedFaqContainer x);

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void deleteFaqContainer(DeprecatedFaqContainer x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<DeprecatedFaqContainer> getRootFaqContainers(Department x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<DeprecatedFaqContainer> getSubFaqContainers(DeprecatedFaqContainer x);

	/**
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<DeprecatedFaqEntry> getFaqEntries();

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void addFaqEntry(DeprecatedFaqEntry x);

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void updateFaqEntry(DeprecatedFaqEntry x);

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void deleteFaqEntry(DeprecatedFaqEntry x);

	/**
	 * @param x
	 * @return x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	List<DeprecatedFaqEntry> getFaqEntries(DeprecatedFaqContainer x);

	/**
	 * @param x
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void updateFaqEntriesEffectiveScope(DeprecatedFaqContainer x);

	/**
	 * Migrate the references to a FAQ container before deletion.
	 * @param faqContainer
	 * @param faq
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void migrateFaqContainerRefs(
			DeprecatedFaqContainer faqContainer,
			Faq faq);

	/**
	 * Migrate the references to a FAQ entry before deletion.
	 * @param faqEntry
	 * @param faq
	 * @deprecated
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	void migrateFaqEntryRefs(
			DeprecatedFaqEntry faqEntry,
			Faq faq);

}
