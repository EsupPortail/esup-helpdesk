/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;
import org.apache.myfaces.custom.tree2.TreeState;
import org.esupportail.commons.aop.cache.RequestCache;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.authentication.AuthUtils;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.web.beans.TreeModelBase;
import org.esupportail.commons.web.controllers.LdapSearchCaller;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.TicketScope;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.CategoryMember;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentInvitation;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.Faq;
import org.esupportail.helpdesk.domain.beans.FaqLink;
import org.esupportail.helpdesk.domain.beans.Icon;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.TicketContainer;
import org.esupportail.helpdesk.domain.beans.User;
import org.esupportail.helpdesk.domain.comparators.CategoryLabelComparator;
import org.esupportail.helpdesk.domain.comparators.CategoryMemberDisplayNameComparator;
import org.esupportail.helpdesk.domain.comparators.CategoryMemberIdComparator;
import org.esupportail.helpdesk.domain.comparators.CategoryMemberOrderComparator;
import org.esupportail.helpdesk.domain.comparators.CategoryOrderComparator;
import org.esupportail.helpdesk.domain.comparators.CategoryXlabelComparator;
import org.esupportail.helpdesk.domain.comparators.DepartmentLabelComparator;
import org.esupportail.helpdesk.domain.comparators.DepartmentManagerDisplayNameComparator;
import org.esupportail.helpdesk.domain.comparators.DepartmentManagerIdComparator;
import org.esupportail.helpdesk.domain.comparators.DepartmentManagerOrderComparator;
import org.esupportail.helpdesk.domain.comparators.DepartmentOrderComparator;
import org.esupportail.helpdesk.domain.comparators.DepartmentXlabelComparator;
import org.esupportail.helpdesk.web.beans.AbstractFirstLastNode;
import org.esupportail.helpdesk.web.beans.AuthTypeI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.CategoryMonitoringI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.CategoryNode;
import org.esupportail.helpdesk.web.beans.CategoryTreeModel;
import org.esupportail.helpdesk.web.beans.DepartmentInvitationPaginator;
import org.esupportail.helpdesk.web.beans.DepartmentManagerPaginator;
import org.esupportail.helpdesk.web.beans.FaqNode;
import org.esupportail.helpdesk.web.beans.FaqTreeModel;
import org.esupportail.helpdesk.web.beans.ManagedDepartmentPaginator;
import org.esupportail.helpdesk.web.beans.PriorityI18nKeyProvider;
import org.esupportail.helpdesk.web.beans.TicketScopeI18nKeyProvider;

/**
 * A bean to manage departments.
 */
public class DepartmentsController extends AbstractContextAwareController implements LdapSearchCaller {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5488948684229991537L;

	/**
	 * The root node type for trees.
	 */
	private static final String ROOT_NODE_TYPE = "root";

	/**
	 * A bean to compare categories by label.
	 */
	private static final Comparator<Category> CATEGORY_LABEL_COMPARATOR = new CategoryLabelComparator();

	/**
	 * A bean to compare categories by xlabel.
	 */
	private static final Comparator<Category> CATEGORY_XLABEL_COMPARATOR = new CategoryXlabelComparator();

	/**
	 * A bean to compare categories by order.
	 */
	private static final Comparator<Category> CATEGORY_ORDER_COMPARATOR = new CategoryOrderComparator();

	/**
	 * A bean to compare managers by display name.
	 */
	private static final Comparator<DepartmentManager> MANAGER_DISPLAY_NAME_COMPARATOR =
		new DepartmentManagerDisplayNameComparator();

	/**
	 * A bean to compare managers by id.
	 */
	private static final Comparator<DepartmentManager> MANAGER_ID_COMPARATOR =
		new DepartmentManagerIdComparator();

	/**
	 * A bean to compare managers by order.
	 */
	private static final Comparator<DepartmentManager> MANAGER_ORDER_COMPARATOR =
		new DepartmentManagerOrderComparator();

	/**
	 * A bean to compare departments by label.
	 */
	private static final Comparator<Department> DEPARTMENT_LABEL_COMPARATOR =
		new DepartmentLabelComparator();

	/**
	 * A bean to compare departments by xlabel.
	 */
	private static final Comparator<Department> DEPARTMENT_XLABEL_COMPARATOR =
		new DepartmentXlabelComparator();

	/**
	 * A bean to compare departments by order.
	 */
	private static final Comparator<Department> DEPARTMENT_ORDER_COMPARATOR =
		new DepartmentOrderComparator();

	/**
	 * A bean to compare category members by display name.
	 */
	private static final Comparator<CategoryMember> CATEGORY_MEMBER_DISPLAY_NAME_COMPARATOR =
		new CategoryMemberDisplayNameComparator();

	/**
	 * A bean to compare category members by id.
	 */
	private static final Comparator<CategoryMember> CATEGORY_MEMBER_ID_COMPARATOR =
		new CategoryMemberIdComparator();

	/**
	 * A bean to compare category members by order.
	 */
	private static final Comparator<CategoryMember> CATEGORY_MEMBER_ORDER_COMPARATOR =
		new CategoryMemberOrderComparator();

	/**
	 * The current department.
	 */
	private Department department;

	/**
	 * The department to add.
	 */
	private Department departmentToAdd;

	/**
	 * The department to update.
	 */
	private Department departmentToUpdate;

	/**
	 * The department manager to update.
	 */
	private DepartmentManager departmentManagerToUpdate;

	/**
	 * The department invitation to delete.
	 */
	private DepartmentInvitation departmentInvitationToDelete;

	/**
	 * The department paginator.
	 */
	private ManagedDepartmentPaginator departmentPaginator;

	/**
	 * The department manager paginator.
	 */
	private DepartmentManagerPaginator departmentManagerPaginator;

	/**
	 * The department invitation paginator.
	 */
	private DepartmentInvitationPaginator departmentInvitationPaginator;

	/**
	 * The id of the user to give manager privileges.
	 */
	private String ldapUid;

	/**
	 * The tree model for categories.
	 */
	private CategoryTreeModel categoryTree;

	/**
	 * The category to update.
	 */
	private Category categoryToUpdate;

	/**
	 * The category to add.
	 */
	private Category categoryToAdd;

	/**
	 * The target category (used to move categories and move tickets when deleting a category).
	 */
	private Category targetCategory;

	/**
	 * The target department (used to move categories).
	 */
	private Department targetDepartment;

	/**
	 * The tree model for moving categories or tickets (when deleting categories).
	 */
	private TreeModelBase moveCategoryTree;

	/**
	 * The tree model for setting real categories.
	 */
	private TreeModelBase setRealCategoryTree;

	/**
	 * The tree model for setting real departments.
	 */
	private TreeModelBase setRealDepartmentTree;

	/**
	 * The members of the category to update.
	 */
	private List<CategoryMember> members;

	/**
	 * The managers not member of the category to update.
	 */
	private List<DepartmentManager> notMembers;

	/**
	 * The inherited category members.
	 */
	private List<DepartmentManager> inheritedMembers;

	/**
	 * The user to add as a category member.
	 */
	private User memberToAdd;

	/**
	 * The member to remove.
	 */
	private CategoryMember memberToDelete;

	/**
	 * The member to move.
	 */
	private CategoryMember memberToMove;

	/**
	 * The FAQ links.
	 */
	private List<FaqLink> faqLinks;

	/**
	 * The inherited FAQ links.
	 */
	private List<FaqLink> inheritedFaqLinks;

	/**
	 * The FAQ to add as a link.
	 */
	private Faq faqToLink;

	/**
	 * The faq link to remove.
	 */
	private FaqLink faqLinkToDelete;

	/**
	 * The faq link to move.
	 */
	private FaqLink faqLinkToMove;

	/**
	 * The tree model for FAQs.
	 */
	private FaqTreeModel faqTree;

	/**
	 * True to use the assignment algorithm after having deleted a category member or a department manager.
	 */
	private boolean useAssignmentAlgorithm;

	/**
	 * The user to assign tickets when deleting a category member.
	 */
	private User targetUser;

	/**
	 * The number of tickets in the current department.
	 */
	private int ticketsNumber;

	/**
	 * The number of archived tickets in the current department.
	 */
	private int archivedTicketsNumber;

	/**
	 * The sort order for managers.
	 */
	private String managersSortOrder;

	/**
	 * The sort order for categories.
	 */
	private String categoriesSortOrder;

	/**
	 * The sort order for departments.
	 */
	private String departmentsSortOrder;

	/**
	 * The sort order for category members.
	 */
	private String membersSortOrder;

	/**
	 * The present order for not category members.
	 */
	private String notMembersPresentOrder;

	/**
	 * The action for categories.jsp.
	 */
	private String categoriesAction;

	/**
	 * The target departments for archived tickets when deleting a department.
	 */
	private List<Department> deleteTargetDepartments;

	/**
	 * Bean constructor.
	 */
	public DepartmentsController() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.notNull(departmentPaginator,
				"property departmentPaginator of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(departmentManagerPaginator,
				"property departmentManagerPaginator of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(departmentInvitationPaginator,
				"property departmentInvitationPaginator of class "
				+ this.getClass().getName() + " can not be null");
	}

	/**
	 * @see org.esupportail.helpdesk.web.controllers.AbstractContextAwareController#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		department = null;
		departmentToAdd = new Department();
		departmentToUpdate = null;
		departmentManagerToUpdate = null;
		ldapUid = null;
		categoryTree = null;
		categoryToAdd = new Category();
		targetCategory = null;
		targetDepartment = null;
		moveCategoryTree = null;
		setRealCategoryTree = null;
		setRealDepartmentTree = null;
		members = null;
		notMembers = null;
		inheritedMembers = null;
		memberToAdd = null;
		memberToDelete = null;
		memberToMove = null;
		useAssignmentAlgorithm = false;
		targetUser = null;
		managersSortOrder = null;
		categoriesSortOrder = null;
		departmentsSortOrder = null;
		faqLinks = null;
		faqToLink = null;
		faqLinkToDelete = null;
		faqLinkToMove = null;
		faqTree = null;
		deleteTargetDepartments = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode()
		+ "[departmentToAdd=" + departmentToAdd
		+ ", departmentToUpdate=" + departmentToUpdate
		+ ", departmentManagerToUpdate=" + departmentManagerToUpdate
		+ ", departmentpaginator=" + departmentPaginator
		+ ", departmentManagerPaginator=" + departmentManagerPaginator
		+ ", departmentInvitationPaginator=" + departmentInvitationPaginator
		+ ", categoryToAdd=" + categoryToAdd
		+ ", categoryToUpdate=" + categoryToUpdate
		+ ", targetCategory=" + targetCategory
		+ ", targetDepartment=" + targetDepartment
		+ "]";
	}

	/**
	 * @return true if the current user is allowed to acces the view.
	 */
	public boolean isPageAuthorized() {
		return isCurrentUserCanViewDepartments();
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			return null;
		}
		getSessionController().setShowShortMenu(false);
		categoryTree = null;
		return "navigationDepartments";
	}

	/**
	 * Add a priority item.
	 * @param priorityItems
	 * @param level
	 */
	private void addPriorityItem(
			final List<SelectItem> priorityItems,
			final Integer level) {
		priorityItems.add(
				new SelectItem(
						level,
						getString(PriorityI18nKeyProvider.getI18nKey(level))));
	}

	/**
	 * @return the localeItems
	 */
	@RequestCache
	public List<SelectItem> getPriorityItems() {
		List<SelectItem> priorityItems = new ArrayList<SelectItem>();
		addPriorityItem(priorityItems, DomainService.DEFAULT_PRIORITY_VALUE);
		for (Integer level : getDomainService().getPriorities()) {
			addPriorityItem(priorityItems, level);
		}
		return priorityItems;
	}

	/**
	 * Add a scope item.
	 * @param scopeItems
	 * @param scope
	 */
	private void addScopeItem(
			final List<SelectItem> scopeItems,
			final String scope) {
		scopeItems.add(new SelectItem(
				scope,
				getString(TicketScopeI18nKeyProvider.getI18nKey(scope))));
	}

	/**
	 * @return the scopeItems
	 */
	@RequestCache
	public List<SelectItem> getScopeItems() {
		List<SelectItem> scopeItems = new ArrayList<SelectItem>();
		addScopeItem(scopeItems, TicketScope.DEFAULT);
		addScopeItem(scopeItems, TicketScope.PUBLIC);
		addScopeItem(scopeItems, TicketScope.PRIVATE);
		addScopeItem(scopeItems, TicketScope.SUBJECT_ONLY);
		return scopeItems;
	}

	/**
	 * Add an algorithm item.
	 * @param assignmentAlgorithmItems
	 * @param algorithmName
	 */
	private void addAssignmentAlgorithmItem(
			final List<SelectItem> assignmentAlgorithmItems,
			final String algorithmName) {
		assignmentAlgorithmItems.add(new SelectItem(
				algorithmName,
				getDomainService().getAssignmentAlgorithmDescription(
						algorithmName, getSessionController().getLocale())));
	}

	/**
	 * @return the assignmentAlgorithmItems
	 */
	@RequestCache
	public List<SelectItem> getAssignmentAlgorithmItems() {
		List<SelectItem> assignmentAlgorithmItems = new ArrayList<SelectItem>();
		assignmentAlgorithmItems.add(new SelectItem(
				"", getString("DOMAIN.ASSIGNMENT_ALGORITHM.DEFAULT")));
		for (String  algorithmName : getDomainService().getAssignmentAlgorithmNames()) {
			addAssignmentAlgorithmItem(assignmentAlgorithmItems, algorithmName);
		}
		return assignmentAlgorithmItems;
	}

	/**
	 * Add a builder item.
	 * @param computerUrlBuilderItems
	 * @param builderName
	 */
	private void addComputerUrlBuilderItem(
			final List<SelectItem> computerUrlBuilderItems,
			final String builderName) {
		computerUrlBuilderItems.add(new SelectItem(
				builderName,
				getDomainService().getComputerUrlBuilderDescription(
						builderName, getSessionController().getLocale())));
	}

	/**
	 * @return the computerUrlBuilderItems
	 */
	@RequestCache
	public List<SelectItem> getComputerUrlBuilderItems() {
		List<SelectItem> computerUrlBuilderItems = new ArrayList<SelectItem>();
		computerUrlBuilderItems.add(new SelectItem(
				"", getString("DOMAIN.COMPUTER_URL_BUILDER.DEFAULT")));
		for (String  builderName : getDomainService().getComputerUrlBuilderNames()) {
			addComputerUrlBuilderItem(computerUrlBuilderItems, builderName);
		}
		return computerUrlBuilderItems;
	}

	/**
	 * Add an monitoring level item.
	 * @param monitoringLevelItems
	 * @param monitoringLevel
	 */
	private void addMonitoringLevelItem(
			final List<SelectItem> monitoringLevelItems,
			final Integer monitoringLevel) {
		monitoringLevelItems.add(new SelectItem(
				monitoringLevel,
				getString(CategoryMonitoringI18nKeyProvider.getI18nKey(monitoringLevel))));
	}

	/**
	 * @return the monitoringLevelItems
	 */
	@RequestCache
	public List<SelectItem> getMonitoringLevelItems() {
		List<SelectItem> monitoringLevelItems = new ArrayList<SelectItem>();
		addMonitoringLevelItem(
				monitoringLevelItems,
				TicketContainer.MONITORING_NEVER);
		addMonitoringLevelItem(
				monitoringLevelItems,
				TicketContainer.MONITORING_CREATION);
		addMonitoringLevelItem(
				monitoringLevelItems,
				TicketContainer.MONITORING_CREATION_OR_RELEASE);
		addMonitoringLevelItem(
				monitoringLevelItems,
				TicketContainer.MONITORING_CREATION_OR_FREE);
		addMonitoringLevelItem(
				monitoringLevelItems, TicketContainer.MONITORING_ALWAYS);
		return monitoringLevelItems;
	}

	/**
	 * Add an monitoring email auth type item.
	 * @param monitoringEmailAuthTypeItems
	 * @param authType
	 */
	private void addMonitoringEmailAuthTypeItem(
			final List<SelectItem> monitoringEmailAuthTypeItems,
			final String authType) {
		monitoringEmailAuthTypeItems.add(new SelectItem(
				authType,
				getString(AuthTypeI18nKeyProvider.getI18nKey(authType))));
	}

	/**
	 * @return the monitoringEmailAuthTypeItems
	 */
	@RequestCache
	public List<SelectItem> getMonitoringEmailAuthTypeItems() {
		List<SelectItem> monitoringEmailAuthTypeItems = new ArrayList<SelectItem>();
		if (getDomainService().getUserStore().isApplicationAuthAllowed()) {
			addMonitoringEmailAuthTypeItem(
				monitoringEmailAuthTypeItems,
				AuthUtils.APPLICATION);
		}
		if (getDomainService().getUserStore().isCasAuthAllowed()) {
			addMonitoringEmailAuthTypeItem(
				monitoringEmailAuthTypeItems,
				AuthUtils.CAS);
		}
		if (getDomainService().getUserStore().isShibbolethAuthAllowed()) {
			addMonitoringEmailAuthTypeItem(
				monitoringEmailAuthTypeItems,
				AuthUtils.SHIBBOLETH);
		}
		return monitoringEmailAuthTypeItems;
	}

	/**
	 * @return the icon items.
	 */
	@RequestCache
	public List<SelectItem> getIconItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem("", getString("DEPARTMENTS.TEXT.DEFAULT_ICON")));
		for (Icon icon : getDomainService().getIcons()) {
			items.add(new SelectItem(icon, icon.getName()));
		}
		return items;
	}

	/**
	 * @return true if the current user can manage departments
	 */
	@RequestCache
	public boolean isCurrentUserCanViewDepartments() {
		return getDomainService().userCanViewDepartments(getCurrentUser());
	}

	/**
	 * @return true if the current user can delete the current department
	 */
	@RequestCache
	public boolean isCurrentUserCanDeleteDepartment() {
		return getDomainService().userCanDeleteDepartment(getCurrentUser(), department);
	}

	/**
	 * @return true if the current user can manage departments
	 */
	@RequestCache
	public boolean isCurrentUserCanManageDepartments() {
		return getDomainService().userCanManageDepartments(getCurrentUser());
	}

	/**
	 * @return true if the current user is allowed to view the current department.
	 */
	@RequestCache
	public boolean isCurrentUserCanViewDepartment() {
		return getDomainService().userCanViewDepartment(getCurrentUser(), department);
	}

	/**
	 * @return true if the current user is allowed to view the categories of the current department.
	 */
	@RequestCache
	public boolean isCurrentUserCanViewCategories() {
		return isCurrentUserCanViewDepartment() && !department.isVirtual();
	}

	/**
	 * @return true if the current user is allowed to view the faq links of the current department.
	 */
	@RequestCache
	public boolean isCurrentUserCanViewFaqLinks() {
		return isCurrentUserCanViewDepartment() && !department.isVirtual();
	}

	/**
	 * @return true if the current user can edit the department properties
	 */
	@RequestCache
	public boolean isCurrentUserCanEditDepartmentProperties() {
		return getDomainService().userCanEditDepartmentProperties(getCurrentUser(), department);
	}

	/**
	 * @return true if the current user can edit the department managers
	 */
	@RequestCache
	public boolean isCurrentUserCanManageDepartmentManagers() {
		return getDomainService().userCanEditDepartmentManagers(getCurrentUser(), department);
	}

	/**
	 * @return true if the current user can edit the department categories
	 */
	@RequestCache
	public boolean isCurrentUserCanManageDepartmentCategories() {
		return getDomainService().userCanEditDepartmentCategories(getCurrentUser(), department);
	}

	/**
	 * @return true if the current user can edit availability
	 */
	@RequestCache
	public boolean isCurrentUserCanSetAvailability() {
		return getDomainService().userCanSetAvailability(getCurrentUser(), departmentManagerToUpdate);
	}

	/**
	 * @return a String.
	 */
	public String addDepartment() {
		if (!isCurrentUserCanManageDepartments()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (!org.springframework.util.StringUtils.hasText(departmentToAdd.getLabel())) {
			addErrorMessage(null, "DEPARTMENTS.MESSAGE.ENTER_LABEL");
			return null;
		}
		if (!org.springframework.util.StringUtils.hasText(departmentToAdd.getXlabel())) {
			addErrorMessage(null, "DEPARTMENTS.MESSAGE.ENTER_XLABEL");
			return null;
		}
		if (getDomainService().isDepartmentLabelUsed(departmentToAdd.getLabel())) {
			addWarnMessage(null, "DEPARTMENTS.MESSAGE.LABEL_ALREADY_USED", departmentToAdd.getLabel());
			return null;
		}
		departmentToAdd.setDefaultTicketPriority(getDomainService().getDepartmentDefaultTicketPriorityLevel());
		getDomainService().addDepartment(departmentToAdd);
		setDepartment(departmentToAdd);
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_ADDED", departmentToAdd.getLabel());
		departmentToAdd = new Department();
		return "departmentAdded";
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String updateDepartment() {
		if (!isCurrentUserCanEditDepartmentProperties()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (!department.getLabel().equals(departmentToUpdate.getLabel())) {
			// the label has changed, check if it is not used
			if (getDomainService().isDepartmentLabelUsed(departmentToUpdate.getLabel())) {
				addWarnMessage("label", "DEPARTMENTS.MESSAGE.LABEL_ALREADY_USED",
						departmentToUpdate.getLabel());
				return null;
			}
		}
		if (departmentToUpdate.getDefaultTicketPriority() == DomainService.DEFAULT_PRIORITY_VALUE) {
			departmentToUpdate.setDefaultTicketPriority(
					getDomainService().getDepartmentDefaultTicketPriorityLevel());
		}
		getDomainService().updateDepartment(departmentToUpdate);
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_UPDATED", departmentToUpdate.getLabel());
		setDepartment(departmentToUpdate);
		return "departmentUpdated";
	}

	/**
	 * JSF callback.
	 */
	public void moveDepartmentUp() {
		getDomainService().moveDepartmentUp(departmentToUpdate);
	}

	/**
	 * JSF callback.
	 */
	public void moveDepartmentDown() {
		getDomainService().moveDepartmentDown(departmentToUpdate);
	}

	/**
	 * JSF callback.
	 */
	public void moveDepartmentFirst() {
		getDomainService().moveDepartmentFirst(departmentToUpdate);
	}

	/**
	 * JSF callback.
	 */
	public void moveDepartmentLast() {
		getDomainService().moveDepartmentLast(departmentToUpdate);
	}

	/**
	 * Sort the departments.
	 * @param comparator
	 * @param reverseOrder
	 */
	protected void reorderDepartments(
			final Comparator<Department> comparator,
			final boolean reverseOrder) {
		List<Department> departments = getDomainService().getDepartments();
		Collections.sort(departments, comparator);
		if (reverseOrder) {
			Collections.reverse(departments);
		}
		getDomainService().reorderDepartments(departments);
		departmentsSortOrder = null;
	}

	/**
	 * JSF callback.
	 */
	public void reorderDepartments() {
		if ("label".equals(departmentsSortOrder)) {
			reorderDepartments(DEPARTMENT_LABEL_COMPARATOR, false);
		} else if ("xlabel".equals(departmentsSortOrder)) {
			reorderDepartments(DEPARTMENT_XLABEL_COMPARATOR, false);
		} else if ("reverse".equals(departmentsSortOrder)) {
			reorderDepartments(DEPARTMENT_ORDER_COMPARATOR, true);
		}
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String deleteDepartment() {
		if (!isCurrentUserCanDeleteDepartment()) {
			addUnauthorizedActionMessage();
			return null;
		}
		deleteTargetDepartments = new ArrayList<Department>();
		if (getDomainService().getArchivedTicketsNumber(department) > 0) {
			for (Department dep : getDomainService().getDepartments()) {
				if (!dep.equals(department)) {
					deleteTargetDepartments.add(dep);
				}
			}
		}
		return "deleteDepartment";
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String doDeleteDepartment() {
		if (!isCurrentUserCanDeleteDepartment()) {
			addUnauthorizedActionMessage();
			return null;
		}
		Department currentDepartment = department;
		getDomainService().deleteDepartment(currentDepartment, targetDepartment);
		if (targetDepartment == null) {
			addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_DELETED", currentDepartment.getLabel());
		} else {
			addInfoMessage(null,
					"DEPARTMENTS.MESSAGE.DEPARTMENT_DELETED_ARCHIVED_TICKETS_MOVED",
					currentDepartment.getLabel(), targetDepartment.getLabel());
		}
		setDepartment(null);
		departmentPaginator.forceReload();
		return "departmentDeleted";
	}

	/**
	 * @return true if the department has virtual departments.
	 */
	public boolean isDepartmentToUpdateHasVirtualDepartments() {
		return getDomainService().hasVirtualDepartments(departmentToUpdate);
	}

	/**
	 * @return the virtual departments.
	 */
	public List<Department> getDepartmentToUpdateVirtualDepartments() {
		return getDomainService().getVirtualDepartments(departmentToUpdate);
	}

	/**
	 * @return true if the departments has categories.
	 */
	public boolean isDepartmentToUpdateHasCategories() {
		return getDomainService().hasRootCategories(departmentToUpdate);
	}

	/**
	 * @return the sub categories.
	 */
	public List<Category> getDepartmentToUpdateCategories() {
		return getDomainService().getRootCategories(departmentToUpdate);
	}

	/**
	 * Refresh the tree used to set the real category.
	 */
	@SuppressWarnings("unchecked")
	private void refreshSetRealDepartmentTree() {
		TreeNode rootNode = new TreeNodeBase(ROOT_NODE_TYPE, ROOT_NODE_TYPE, true);
		for (Department dep : getDomainService().getDepartments()) {
			if (!dep.equals(departmentToUpdate) && !dep.isVirtual()) {
		    	CategoryNode departmentNode = new CategoryNode(dep);
		    	rootNode.getChildren().add(departmentNode);
		    	rootNode.setLeaf(false);
			}
		}
		setRealDepartmentTree = new TreeModelBase(rootNode);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String gotoSetRealDepartment() {
		if (!isCurrentUserCanEditDepartmentProperties()) {
			addUnauthorizedActionMessage();
			return null;
		}
		refreshSetRealDepartmentTree();
		return "setRealDepartment";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String setRealDepartment() {
		if (!isCurrentUserCanEditDepartmentProperties()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (targetDepartment == null) {
			departmentToUpdate.setRealDepartment(null);
			getDomainService().updateDepartment(departmentToUpdate);
			addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_SET_REAL", departmentToUpdate.getLabel());
			return "realDepartmentSet";
		}
		if (targetDepartment.isVirtual()) {
			addErrorMessage(null, "DEPARTMENTS.MESSAGE.CAN_NOT_REDIRECT_TO_VIRTUAL_DEPARTMENT");
			return null;
		}
		departmentToUpdate.setRealDepartment(targetDepartment);
		getDomainService().updateDepartment(departmentToUpdate);
		for (FaqLink faqLink : getDomainService().getFaqLinks(departmentToUpdate)) {
			getDomainService().deleteFaqLink(faqLink);
		}
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_SET_VIRTUAL", targetDepartment.getLabel());
		return "realDepartmentSet";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String viewDepartment() {
		return "viewDepartment";
	}

	/**
	 * @return the members of the current department.
	 */
	public List<DepartmentManager> getManagers() {
		return getDomainService().getDepartmentManagers(department);
	}

	/**
	 * @return the number of managers of the current department.
	 */
	public int getManagersNumber() {
		return getManagers().size();
	}

	/**
	 * @return a String.
	 */
	public String addDepartmentManager() {
		if (!isCurrentUserCanManageDepartmentManagers()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (ldapUid == null) {
			addErrorMessage("ldapUid", "DEPARTMENTS.MESSAGE.ENTER_ID", ldapUid);
			return null;
		}
		User user;
		try {
			user = getUserStore().getUserFromRealId(ldapUid);
		} catch (UserNotFoundException e) {
			addErrorMessage("ldapUid", "_.MESSAGE.USER_NOT_FOUND", ldapUid);
			return null;
		}
		if (getDomainService().isDepartmentManager(department, user)) {
			addErrorMessage("ldapUid", "DEPARTMENTS.MESSAGE.USER_ALREADY_MANAGER",
					formatUser(user));
			return null;
		}
		DepartmentManager departmentManager = getDomainService().addDepartmentManager(department, user);
		departmentManagerPaginator.forceReload();
		addInfoMessage(
				null, "DEPARTMENTS.MESSAGE.DEPARTMENT_MANAGER_ADDED",
				formatUser(user), department.getLabel());
		setDepartmentManagerToUpdate(departmentManager);
		ldapUid = "";
		return "departmentManagerAdded";
	}

	/**
	 * @return a String.
	 */
	private String doDeleteDepartmentManager() {
		getDomainService().deleteDepartmentManager(
				getCurrentUser(), departmentManagerToUpdate, useAssignmentAlgorithm, targetUser);
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_MANAGER_DELETED",
				formatUser(departmentManagerToUpdate.getUser()),
				departmentManagerToUpdate.getDepartment().getLabel());
		departmentManagerPaginator.forceReload();
		return "departmentManagerDeleted";
	}

	/**
	 * @return a String.
	 */
	public String deleteDepartmentManager() {
		if (!isCurrentUserCanManageDepartmentManagers()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (getDomainService().hasOpenManagedTickets(departmentManagerToUpdate)) {
			return "deleteDepartmentManager";
		}
		return doDeleteDepartmentManager();
	}

	/**
	 * @return a String.
	 */
	public String confirmDeleteDepartmentManager() {
		if (!isCurrentUserCanManageDepartmentManagers()) {
			addUnauthorizedActionMessage();
			return null;
		}
		return doDeleteDepartmentManager();
	}

	/**
	 * @return a String.
	 */
	public String updateDepartmentManager() {
		if (!(isCurrentUserCanManageDepartmentManagers() || isCurrentUserCanSetAvailability())) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (departmentManagerToUpdate.getRate() < DepartmentManager.MIN_RATE
				|| departmentManagerToUpdate.getRate() > DepartmentManager.MAX_RATE ) {
			addWarnMessage(
					"rate", "DEPARTMENTS.MESSAGE.INCORRECT_RATE",
					String.valueOf(DepartmentManager.MIN_RATE),
					String.valueOf(DepartmentManager.MAX_RATE));
			return null;
		}
		getDomainService().updateDepartmentManager(departmentManagerToUpdate);
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_MANAGER_UPDATED",
				formatUser(departmentManagerToUpdate.getUser()),
				departmentManagerToUpdate.getDepartment().getLabel());
		return "departmentManagerUpdated";
	}

	/**
	 * JSF callback.
	 */
	public void moveDepartmentManagerUp() {
		getDomainService().moveDepartmentManagerUp(departmentManagerToUpdate);
	}

	/**
	 * JSF callback.
	 */
	public void moveDepartmentManagerDown() {
		getDomainService().moveDepartmentManagerDown(departmentManagerToUpdate);
	}

	/**
	 * JSF callback.
	 */
	public void moveDepartmentManagerFirst() {
		getDomainService().moveDepartmentManagerFirst(departmentManagerToUpdate);
	}

	/**
	 * JSF callback.
	 */
	public void moveDepartmentManagerLast() {
		getDomainService().moveDepartmentManagerLast(departmentManagerToUpdate);
	}

	/**
	 * Sort the managers.
	 * @param comparator
	 * @param reverseOrder
	 */
	protected void reorderManagers(
			final Comparator<DepartmentManager> comparator,
			final boolean reverseOrder) {
		List<DepartmentManager> managers = getManagers();
		Collections.sort(managers, comparator);
		if (reverseOrder) {
			Collections.reverse(managers);
		}
		getDomainService().reorderDepartmentManagers(managers);
		managersSortOrder = null;
	}

	/**
	 * JSF callback.
	 */
	public void reorderManagers() {
		if ("displayName".equals(managersSortOrder)) {
			reorderManagers(MANAGER_DISPLAY_NAME_COMPARATOR, false);
		} else if ("id".equals(managersSortOrder)) {
			reorderManagers(MANAGER_ID_COMPARATOR, false);
		} else if ("reverse".equals(managersSortOrder)) {
			reorderManagers(MANAGER_ORDER_COMPARATOR, true);
		}
	}

	/**
	 * @return a String.
	 */
	public String addDepartmentInvitation() {
		if (!isCurrentUserCanManageDepartmentManagers()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (ldapUid == null) {
			addErrorMessage("ldapUid", "DEPARTMENTS.MESSAGE.ENTER_ID", ldapUid);
			return null;
		}
		User user;
		try {
			user = getUserStore().getUserFromRealId(ldapUid);
		} catch (UserNotFoundException e) {
			addErrorMessage("ldapUid", "_.MESSAGE.USER_NOT_FOUND", ldapUid);
			return null;
		}
		if (getDomainService().isDepartmentInvited(department, user)) {
			addErrorMessage("ldapUid", "DEPARTMENTS.MESSAGE.USER_ALREADY_INVITED",
					formatUser(user));
			return null;
		}
		getDomainService().addDepartmentInvitation(department, user);
		departmentInvitationPaginator.forceReload();
		addInfoMessage(
				null, "DEPARTMENTS.MESSAGE.DEPARTMENT_INVITATION_ADDED",
				formatUser(user), department.getLabel());
		ldapUid = "";
		return "departmentInvitationAdded";
	}

	/**
	 * @return a String.
	 */
	public String deleteDepartmentInvitation() {
		if (!isCurrentUserCanManageDepartmentManagers()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().deleteDepartmentInvitation(departmentInvitationToDelete);
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.DEPARTMENT_INVITATION_DELETED",
				formatUser(departmentInvitationToDelete.getUser()),
				departmentInvitationToDelete.getDepartment().getLabel());
		departmentInvitationPaginator.forceReload();
		return "departmentInvitationDeleted";
	}

	/**
	 * JSF callback.
	 * @return a string.
	 */
	public String refreshCategoryTree() {
		TreeState treeState = null;
		if (categoryTree != null) {
			treeState = categoryTree.getTreeState();
		}
		categoryTree = new CategoryTreeModel(buildRootNode());
		if (treeState != null) {
			categoryTree.setTreeState(treeState);
		}
		return null;
	}

	/**
	 * @return the root node.
	 */
	private CategoryNode buildRootNode() {
    	CategoryNode rootNode = new CategoryNode(department);
    	addCategoryTreeSubCategories(rootNode, getDomainService().getRootCategories(department));
    	return rootNode;
    }

	/**
	 * Add sub categories to the category tree.
	 * @param categoryNode
	 * @param subCategories
	 */
    @SuppressWarnings("unchecked")
	private void addCategoryTreeSubCategories(
			final CategoryNode categoryNode,
			final List<Category> subCategories) {
    	for (Category subCategory : subCategories) {
        	CategoryNode subCategoryNode = new CategoryNode(subCategory, subCategory.getXlabel());
        	subCategoryNode.setVirtualCategories(getDomainService().getVirtualCategories(subCategory));
        	subCategoryNode.setMembers(getDomainService().getCategoryMembers(subCategory));
        	subCategoryNode.setFaqLinks(getDomainService().getFaqLinks(subCategory));
        	categoryNode.getChildren().add(subCategoryNode);
        	addCategoryTreeSubCategories(subCategoryNode, getDomainService().getSubCategories(subCategory));
    		categoryNode.setLeaf(false);
    	}
    	AbstractFirstLastNode.markFirstAndLastChildNodes(categoryNode);
    }

	/**
	 * @return a String.
	 */
	public String addCategory() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (!org.springframework.util.StringUtils.hasText(categoryToAdd.getLabel())) {
			addErrorMessage(null, "DEPARTMENTS.MESSAGE.ENTER_LABEL");
			return null;
		}
		if (!org.springframework.util.StringUtils.hasText(categoryToAdd.getXlabel())) {
			addErrorMessage(null, "DEPARTMENTS.MESSAGE.ENTER_XLABEL");
			return null;
		}
		getDomainService().addCategory(categoryToAdd);
		setCategoryToUpdate(categoryToAdd);
		if (categoryToAdd.getParent() != null) {
			getDomainService().updateCategory(categoryToAdd.getParent());
		}
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.CATEGORY_ADDED", categoryToAdd.getLabel());
		categoryToAdd = new Category();
		refreshCategoryTree();
		return "categoryAdded";
	}

	/**
	 * Collapse all the nodes at the same level as the node of a category.
	 * @param category
	 */
	@SuppressWarnings("unchecked")
	private void collapseNodesAtSameLevel(final Category category) {
		List<CategoryNode> nodesToCollapse;
		if (category.getParent() == null) {
			nodesToCollapse = categoryTree.getRootNode().getChildren();
		} else {
			String parentNodeId = categoryTree.getCategoryNodeId(category.getParent());
			nodesToCollapse = categoryTree.getNodeById(parentNodeId).getChildren();
		}
		TreeState treeState = categoryTree.getTreeState();
		for (CategoryNode nodeToCollapse : nodesToCollapse) {
			String nodeToCollapseId = categoryTree.getCategoryNodeId(nodeToCollapse.getCategory());
			if (treeState.isNodeExpanded(nodeToCollapseId)) {
				treeState.toggleExpanded(nodeToCollapseId);
			}
		}
	}

	/**
	 * JSF callback.
	 */
	public void moveCategoryUp() {
		collapseNodesAtSameLevel(categoryToUpdate);
		getDomainService().moveCategoryUp(categoryToUpdate);
		refreshCategoryTree();
	}

	/**
	 * JSF callback.
	 */
	public void moveCategoryDown() {
		collapseNodesAtSameLevel(categoryToUpdate);
		getDomainService().moveCategoryDown(categoryToUpdate);
		refreshCategoryTree();
	}

	/**
	 * JSF callback.
	 */
	public void moveCategoryFirst() {
		collapseNodesAtSameLevel(categoryToUpdate);
		getDomainService().moveCategoryFirst(categoryToUpdate);
		refreshCategoryTree();
	}

	/**
	 * JSF callback.
	 */
	public void moveCategoryLast() {
		collapseNodesAtSameLevel(categoryToUpdate);
		getDomainService().moveCategoryLast(categoryToUpdate);
		refreshCategoryTree();
	}

	/**
	 * Reorder a list of categories.
	 * @param categories
	 * @param comparator
	 * @param reverseOrder
	 */
	protected void reorderSubCategories(
			final List<Category> categories,
			final Comparator<Category> comparator,
			final boolean reverseOrder) {
		Collections.sort(categories, comparator);
		if (reverseOrder) {
			Collections.reverse(categories);
		}
		getDomainService().reorderCategories(categories);
		for (Category category : categories) {
			reorderCategories(getDomainService().getSubCategories(category), comparator, reverseOrder);
		}
	}

	/**
	 * Reorder a list of categories.
	 * @param categories
	 * @param comparator
	 * @param reverseOrder
	 */
	protected void reorderCategories(
			final List<Category> categories,
			final Comparator<Category> comparator,
			final boolean reverseOrder) {
		reorderSubCategories(categories, comparator, reverseOrder);
		refreshCategoryTree();
		categoriesSortOrder = null;
	}

	/**
	 * JSF callback.
	 */
	public void reorderCategories() {
		if ("label".equals(categoriesSortOrder)) {
			reorderCategories(
					getDomainService().getRootCategories(department),
					CATEGORY_LABEL_COMPARATOR, false);
		} else if ("xlabel".equals(categoriesSortOrder)) {
			reorderCategories(
					getDomainService().getRootCategories(department),
					CATEGORY_XLABEL_COMPARATOR, false);
		} else if ("reverse".equals(categoriesSortOrder)) {
			reorderCategories(
					getDomainService().getRootCategories(department),
					CATEGORY_ORDER_COMPARATOR, true);
		}
	}

	/**
	 * JSF callback.
	 */
	private void deleteCategoryInternal() {
		collapseNodesAtSameLevel(categoryToUpdate);
		getDomainService().deleteCategory(categoryToUpdate);
		refreshCategoryTree();
	}

	/**
	 * Refresh the tree used to move tickets or categories.
	 */
	@SuppressWarnings("unchecked")
	private void refreshMoveCategoryTree() {
		TreeNode rootNode = new TreeNodeBase(ROOT_NODE_TYPE, ROOT_NODE_TYPE, true);
		for (Department dep : getDomainService().getManagedDepartments(getCurrentUser())) {
			if (!dep.isVirtual()
					&& dep.isEnabled()
					&& getDomainService().getDepartmentManager(
							dep, getCurrentUser()).getManageCategories()) {
		    	CategoryNode departmentNode = new CategoryNode(dep);
		    	addMoveCategoryTreeSubCategories(departmentNode, getDomainService().getRootCategories(dep));
	    		rootNode.getChildren().add(departmentNode);
	    		rootNode.setLeaf(false);
			}
		}
		moveCategoryTree = new TreeModelBase(rootNode);
	}

	/**
	 * Add sub categories to the category tree used to move categories and tickets.
	 * @param categoryNode
	 * @param subCategories
	 */
    @SuppressWarnings("unchecked")
	private void addMoveCategoryTreeSubCategories(
			final CategoryNode categoryNode,
			final List<Category> subCategories) {
    	for (Category subCategory : subCategories) {
       		if (!categoryToUpdate.equals(subCategory)
       				&& !getDomainService().detectRedirectionLoop(subCategory, categoryToUpdate, null)) {
	        	CategoryNode subCategoryNode = new CategoryNode(subCategory, subCategory.getXlabel());
	        	categoryNode.getChildren().add(subCategoryNode);
	        	addMoveCategoryTreeSubCategories(
	        			subCategoryNode,
	        			getDomainService().getSubCategories(subCategory));
	    		categoryNode.setLeaf(false);
    		}
    	}
//    	if (categoryNode.getChildCount() > 0) {
//    		((CategoryNode) categoryNode.getChildren().get(0)).setFirst(true);
//    		((CategoryNode) categoryNode.getChildren().get(categoryNode.getChildCount() - 1)).setLast(true);
//    	}
    }

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String updateCategory() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().updateCategory(categoryToUpdate);
		refreshCategoryTree();
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.CATEGORY_UPDATED", categoryToUpdate.getLabel());
		return "categoryUpdated";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String deleteCategory() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (getDomainService().hasSubCategories(categoryToUpdate)) {
			refreshCategoryTree();
			return null;
		}
		if (getDomainService().hasVirtualCategories(categoryToUpdate)) {
			refreshCategoryTree();
			return null;
		}
		if (getDomainService().hasTickets(categoryToUpdate)) {
			refreshMoveCategoryTree();
			return "moveTicketsBeforeDeletion";
		}
		deleteCategoryInternal();
		addInfoMessage(null, "DEPARTMENTS.MESSAGE.CATEGORY_DELETED", categoryToUpdate.getLabel());
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String moveTicketsAndDeleteCategory() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		for (Ticket ticket : getDomainService().getTickets(categoryToUpdate)) {
			if (targetCategory == null) {
				getDomainService().deleteTicket(ticket, true);
			} else {
				getDomainService().moveTicket(ticket, targetCategory);
			}
		}
		deleteCategoryInternal();
		if (targetCategory == null) {
			addInfoMessage(null, "DEPARTMENTS.MESSAGE.TICKETS_AND_CATEGORY_DELETED",
					categoryToUpdate.getLabel());
		} else {
			addInfoMessage(null, "DEPARTMENTS.MESSAGE.TICKETS_MOVED_AND_CATEGORY_DELETED",
					categoryToUpdate.getLabel(),
					targetCategory.getLabel());
		}
		return "categoryDeleted";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String gotoMoveCategory() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		refreshMoveCategoryTree();
		return "moveCategory";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String moveCategory() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveCategory(categoryToUpdate, targetDepartment, targetCategory);
		refreshCategoryTree();
		if (targetCategory == null) {
			addInfoMessage(null, "DEPARTMENTS.MESSAGE.CATEGORY_MOVED_TO_DEPARTMENT",
					categoryToUpdate.getLabel(), targetDepartment.getLabel());
		} else {
			addInfoMessage(null, "DEPARTMENTS.MESSAGE.CATEGORY_MOVED_TO_CATEGORY",
					categoryToUpdate.getLabel(), targetCategory.getLabel());
		}
		return "categoryMoved";
	}

	/**
	 * @return true if the category has sub categories.
	 */
	public boolean isCategoryToUpdateHasSubCategories() {
		return getDomainService().hasSubCategories(categoryToUpdate);
	}

	/**
	 * @return the sub categories.
	 */
	public List<Category> getCategoryToUpdateSubCategories() {
		return getDomainService().getSubCategories(categoryToUpdate);
	}

	/**
	 * @return the link to add a ticket in the category to update for application users.
	 */
	public String getCategoryToUpdateApplicationAddLink() {
		return getUrlBuilder().getTicketAddUrl(AuthUtils.APPLICATION, categoryToUpdate);
	}

	/**
	 * @return the link to add a ticket in the category to update for CAS users.
	 */
	public String getCategoryToUpdateCasAddLink() {
		return getUrlBuilder().getTicketAddUrl(AuthUtils.CAS, categoryToUpdate);
	}

	/**
	 * @return the link to add a ticket in the category to update for CAS users.
	 */
	public String getCategoryToUpdateShibbolethAddLink() {
		return getUrlBuilder().getTicketAddUrl(AuthUtils.SHIBBOLETH, categoryToUpdate);
	}

	/**
	 * Refresh the tree used to set the real category.
	 */
	@SuppressWarnings("unchecked")
	private void refreshSetRealCategoryTree() {
		TreeNode rootNode = new TreeNodeBase(ROOT_NODE_TYPE, ROOT_NODE_TYPE, true);
		Map<Category, Boolean> map = new HashMap<Category, Boolean>();
		for (Department dep : getDomainService().getManagedOrTicketViewVisibleDepartments(
				getCurrentUser(), getClient())) {
			if (!dep.isVirtual()) {
		    	CategoryNode departmentNode = new CategoryNode(dep);
		    	addSetRealCategoryTreeSubCategories(
		    			departmentNode, getDomainService().getRootCategories(dep), map);
		    	if (departmentNode.getChildCount() > 0) {
		    		rootNode.getChildren().add(departmentNode);
		    		rootNode.setLeaf(false);
		    	}
			}
		}
		setRealCategoryTree = new TreeModelBase(rootNode);
	}

	/**
	 * Add sub categories to the category tree used to set the real category.
	 * @param categoryNode
	 * @param subCategories
	 * @param map
	 */
    @SuppressWarnings("unchecked")
	private void addSetRealCategoryTreeSubCategories(
			final CategoryNode categoryNode,
			final List<Category> subCategories,
			final Map<Category, Boolean> map) {
    	for (Category subCategory : subCategories) {
    		if (!getDomainService().detectRedirectionLoop(categoryToUpdate, subCategory, map)) {
	        	CategoryNode subCategoryNode = new CategoryNode(subCategory, subCategory.getXlabel());
	        	categoryNode.getChildren().add(subCategoryNode);
	        	addSetRealCategoryTreeSubCategories(
	        			subCategoryNode,
	        			getDomainService().getSubCategories(subCategory),
	        			map);
	    		categoryNode.setLeaf(false);
    		}
    	}
    	AbstractFirstLastNode.markFirstAndLastChildNodes(categoryNode);
    }

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String gotoSetRealCategory() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		refreshSetRealCategoryTree();
		return "setRealCategory";
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String setRealCategory() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		if (targetCategory == null) {
			categoryToUpdate.setRealCategory(null);
			getDomainService().updateCategory(categoryToUpdate);
			addInfoMessage(null, "DEPARTMENTS.MESSAGE.CATEGORY_SET_REAL", categoryToUpdate.getLabel());
			return "realCategorySet";
		}
		if (getDomainService().detectRedirectionLoop(categoryToUpdate, targetCategory, null)) {
			addErrorMessage(null, "DEPARTMENTS.MESSAGE.REDIRECT_LOOP");
			return null;
		}
		categoryToUpdate.setRealCategory(targetCategory);
		getDomainService().updateCategory(categoryToUpdate);
		if (getDomainService().hasTickets(categoryToUpdate)) {
			for (Ticket ticket : getDomainService().getTickets(categoryToUpdate)) {
				getDomainService().moveTicket(ticket, targetCategory);
			}
			addInfoMessage(null, "DEPARTMENTS.MESSAGE.TICKETS_MOVED_AND_CATEGORY_SET_VIRTUAL",
					targetCategory.getLabel());
		} else {
			addInfoMessage(null, "DEPARTMENTS.MESSAGE.CATEGORY_SET_VIRTUAL", targetCategory.getLabel());
		}
		refreshCategoryTree();
		return "realCategorySet";
	}

	/**
	 * Reset the members of the category to update (to force them to be computed again).
	 */
	public void resetMembers() {
		members = null;
		notMembers = null;
		inheritedMembers = null;
	}

	/**
	 * Compute the members of the category to update.
	 */
	protected void computeMembers() {
		members = getDomainService().getCategoryMembers(categoryToUpdate);
		notMembers = new ArrayList<DepartmentManager>();
		for (DepartmentManager departmentManager
				: getDomainService().getDepartmentManagers(categoryToUpdate.getDepartment())) {
			if (!getDomainService().isCategoryMember(categoryToUpdate, departmentManager.getUser())) {
				notMembers.add(departmentManager);
			}
		}
		if ("displayName".equals(notMembersPresentOrder)) {
			Collections.sort(notMembers, MANAGER_DISPLAY_NAME_COMPARATOR);
		} else if ("id".equals(notMembersPresentOrder)) {
			Collections.sort(notMembers, MANAGER_ID_COMPARATOR);
		} else {
			Collections.sort(notMembers, MANAGER_ORDER_COMPARATOR);
		}
		inheritedMembers = getDomainService().getInheritedDepartmentManagers(categoryToUpdate);
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String editCategoryMembers() {
		resetMembers();
		return "editCategoryMembers";
	}

	/**
	 * @return the members of the category to update.
	 */
	public List<CategoryMember> getMembers() {
		if (members == null) {
			computeMembers();
		}
		return members;
	}

	/**
	 * @return the number of members of the category to update.
	 */
	public int getMembersNumber() {
		return getMembers().size();
	}

	/**
	 * @return the non members of the category to update.
	 */
	public List<DepartmentManager> getNotMembers() {
		if (notMembers == null) {
			computeMembers();
		}
		return notMembers;
	}

	/**
	 * @return the number of non members of the category to update.
	 */
	public int getNotMembersNumber() {
		return getNotMembers().size();
	}

	/**
	 * @return the inheritedMembers
	 */
	public List<DepartmentManager> getInheritedMembers() {
		if (inheritedMembers == null) {
			computeMembers();
		}
		return inheritedMembers;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String toggleInheritMembers() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		categoryToUpdate.setInheritMembers(!categoryToUpdate.getInheritMembers());
		getDomainService().updateCategory(categoryToUpdate);
		if (categoryToUpdate.getInheritMembers()) {
			for (CategoryMember categoryMember : getDomainService().getCategoryMembers(categoryToUpdate)) {
				getDomainService().deleteCategoryMember(categoryMember);
			}
		}
		refreshCategoryTree();
		resetMembers();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String addCategoryMember() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().addCategoryMember(categoryToUpdate, memberToAdd);
		refreshCategoryTree();
		resetMembers();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String addAllCategoryMembers() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		for (DepartmentManager departmentManager
				: getDomainService().getDepartmentManagers(categoryToUpdate.getDepartment())) {
			if (!getDomainService().isCategoryMember(categoryToUpdate, departmentManager.getUser())) {
				getDomainService().addCategoryMember(categoryToUpdate, departmentManager.getUser());
			}
		}
		refreshCategoryTree();
		resetMembers();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String deleteCategoryMember() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().deleteCategoryMember(
				memberToDelete,
				useAssignmentAlgorithm, targetUser);
		refreshCategoryTree();
		resetMembers();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveCategoryMemberDown() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveCategoryMemberDown(memberToMove);
		refreshCategoryTree();
		resetMembers();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveCategoryMemberUp() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveCategoryMemberUp(memberToMove);
		refreshCategoryTree();
		resetMembers();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveCategoryMemberFirst() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveCategoryMemberFirst(memberToMove);
		refreshCategoryTree();
		resetMembers();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveCategoryMemberLast() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveCategoryMemberLast(memberToMove);
		refreshCategoryTree();
		resetMembers();
		return null;
	}

	/**
	 * Sort the members.
	 * @param comparator
	 * @param reverseOrder
	 */
	protected void reorderMembers(
			final Comparator<CategoryMember> comparator,
			final boolean reverseOrder) {
		Collections.sort(members, comparator);
		if (reverseOrder) {
			Collections.reverse(members);
		}
		getDomainService().reorderCategoryMembers(members);
		membersSortOrder = null;
		resetMembers();
	}

	/**
	 * JSF callback.
	 */
	public void reorderMembers() {
		if ("displayName".equals(membersSortOrder)) {
			reorderMembers(CATEGORY_MEMBER_DISPLAY_NAME_COMPARATOR, false);
		} else if ("id".equals(membersSortOrder)) {
			reorderMembers(CATEGORY_MEMBER_ID_COMPARATOR, false);
		} else if ("reverse".equals(membersSortOrder)) {
			reorderMembers(CATEGORY_MEMBER_ORDER_COMPARATOR, true);
		}
	}

	/**
	 * Add a categories action item.
	 * @param items
	 * @param value
	 */
	private void addCategoriesActionItem(
			final List<SelectItem> items,
			final String value) {
		items.add(new SelectItem(
				value, getString("CATEGORIES.TEXT.ACTION." + value)));
	}

	/**
	 * @return the categoriesActionItems
	 */
	@RequestCache
	public List<SelectItem> getCategoriesActionItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		addCategoriesActionItem(items, "PROPERTIES");
		if (isCurrentUserCanManageDepartmentCategories()) {
			addCategoriesActionItem(items, "ADD_DELETE");
			addCategoriesActionItem(items, "MOVE");
		}
		addCategoriesActionItem(items, "MEMBERS");
		addCategoriesActionItem(items, "FAQ_LINKS");
		return items;
	}

	/**
	 * @return the departments visible for the FAQs.
	 */
	@RequestCache
	protected List<Department> getFaqVisibleDepartments() {
		return getDomainService().getFaqViewDepartments(
				getCurrentUser(), getSessionController().getClient());
	}

	/**
	 * @param faq
	 * @return true if the current user can view a FAQ.
	 */
	@RequestCache
	protected boolean userCanViewFaq(final Faq faq) {
		return getDomainService().userCanViewFaq(
				getCurrentUser(), faq, getFaqVisibleDepartments());
	}

	/**
	 * Add FAQs to the view tree.
	 * @param parentNode
	 * @param faqs
	 */
    @SuppressWarnings("unchecked")
	protected void addTreeFaqs(
			final FaqNode parentNode,
			final List<Faq> faqs) {
    	for (Faq faq : faqs) {
    		boolean alreadyLinked = false;
    		for (FaqLink faqLink : faqLinks) {
    			if (faq.equals(faqLink.getFaq())) {
    				alreadyLinked = true;
    				break;
    			}
    		}
    		if (alreadyLinked) {
    			continue;
    		}
    		if (userCanViewFaq(faq)) {
	        	FaqNode faqNode = new FaqNode(faq);
	        	addTreeFaqs(
	        			faqNode,
	        			getDomainService().getSubFaqs(faq));
	        	AbstractFirstLastNode.markFirstAndLastChildNodes(faqNode);
	    		parentNode.getChildren().add(faqNode);
        		parentNode.setLeaf(false);
    		}
		}
    }

	/**
	 * @return the root FAQ node.
	 */
    @SuppressWarnings("unchecked")
	protected FaqNode buildRootFaqNode() {
    	FaqNode rootNode = new FaqNode();
    	addTreeFaqs(rootNode, getDomainService().getRootFaqs());
    	for (Department theDepartment : getDomainService().getEnabledDepartments()) {
        	FaqNode departmentNode = new FaqNode(theDepartment);
        	addTreeFaqs(
        			departmentNode,
        			getDomainService().getRootFaqs(theDepartment));
    		if (departmentNode.getChildCount() > 0) {
            	rootNode.getChildren().add(departmentNode);
        		rootNode.setLeaf(false);
    		}
    	}
    	AbstractFirstLastNode.markFirstAndLastChildNodes(rootNode);
    	return rootNode;
    }

	/**
	 * Refresh the faq tree for the category to update.
	 */
	protected void refreshFaqTree() {
		TreeState treeState = null;
		if (faqTree != null) {
			treeState = faqTree.getTreeState();
		}
		FaqNode rootNode = buildRootFaqNode();
		faqTree = new FaqTreeModel(rootNode);
		if (treeState != null) {
			faqTree.setTreeState(treeState);
		}
	}

	/**
	 * Refresh the faq links of the category to update.
	 */
	protected void refreshCategoryFaqLinks() {
		faqLinks = getDomainService().getFaqLinks(categoryToUpdate);
		inheritedFaqLinks = getDomainService().getInheritedFaqLinks(categoryToUpdate);
		if (!categoryToUpdate.getInheritFaqLinks()) {
			refreshFaqTree();
		}
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String editCategoryFaqLinks() {
		refreshCategoryFaqLinks();
		return "editCategoryFaqLinks";
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String toggleCategoryInheritFaqLinks() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		categoryToUpdate.setInheritFaqLinks(!categoryToUpdate.getInheritFaqLinks());
		getDomainService().updateCategory(categoryToUpdate);
		if (categoryToUpdate.getInheritFaqLinks()) {
			for (FaqLink faqLink : getDomainService().getFaqLinks(categoryToUpdate)) {
				getDomainService().deleteFaqLink(faqLink);
			}
		}
		refreshCategoryTree();
		refreshCategoryFaqLinks();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String addCategoryFaqLink() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().addFaqLink(new FaqLink(categoryToUpdate, faqToLink));
		refreshCategoryTree();
		refreshCategoryFaqLinks();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String deleteCategoryFaqLink() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().deleteFaqLink(faqLinkToDelete);
		refreshCategoryTree();
		refreshCategoryFaqLinks();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveCategoryFaqLinkDown() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveFaqLinkDown(faqLinkToMove);
		refreshCategoryTree();
		refreshCategoryFaqLinks();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveCategoryFaqLinkUp() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveFaqLinkUp(faqLinkToMove);
		refreshCategoryTree();
		refreshCategoryFaqLinks();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveCategoryFaqLinkFirst() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveFaqLinkFirst(faqLinkToMove);
		refreshCategoryTree();
		refreshCategoryFaqLinks();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveCategoryFaqLinkLast() {
		if (!isCurrentUserCanManageDepartmentCategories()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveFaqLinkLast(faqLinkToMove);
		refreshCategoryTree();
		refreshCategoryFaqLinks();
		return null;
	}

	/**
	 * @return the members of the current department.
	 */
	public List<FaqLink> getDepartmentFaqLinks() {
		return getDomainService().getFaqLinks(department);
	}

	/**
	 * Refresh the faq links of the department.
	 */
	protected void refreshDepartmentFaqLinks() {
		faqLinks = getDomainService().getFaqLinks(department);
		refreshFaqTree();
	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String editDepartmentFaqLinks() {
		refreshDepartmentFaqLinks();
		return "editDepartmentFaqLinks";
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String addDepartmentFaqLink() {
		if (!isCurrentUserCanEditDepartmentProperties()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().addFaqLink(new FaqLink(department, faqToLink));
		refreshDepartmentFaqLinks();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String deleteDepartmentFaqLink() {
		if (!isCurrentUserCanEditDepartmentProperties()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().deleteFaqLink(faqLinkToDelete);
		refreshDepartmentFaqLinks();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveDepartmentFaqLinkDown() {
		if (!isCurrentUserCanEditDepartmentProperties()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveFaqLinkDown(faqLinkToMove);
		refreshDepartmentFaqLinks();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveDepartmentFaqLinkUp() {
		if (!isCurrentUserCanEditDepartmentProperties()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveFaqLinkUp(faqLinkToMove);
		refreshDepartmentFaqLinks();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveDepartmentFaqLinkFirst() {
		if (!isCurrentUserCanEditDepartmentProperties()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveFaqLinkFirst(faqLinkToMove);
		refreshDepartmentFaqLinks();
		return null;
	}

	/**
	 * JSF callback.
	 * @return a String
	 */
	public String moveDepartmentFaqLinkLast() {
		if (!isCurrentUserCanEditDepartmentProperties()) {
			addUnauthorizedActionMessage();
			return null;
		}
		getDomainService().moveFaqLinkLast(faqLinkToMove);
		refreshDepartmentFaqLinks();
		return null;
	}

	/**
	 * @param authType
	 * @return the params for deep links on the page.
	 */
	protected String getPermLink(final String authType) {
		if (department == null) {
			return getUrlBuilder().getDepartmentsUrl(authType);
		}
		return getUrlBuilder().getDepartmentViewUrl(authType, department.getId());
	}

	/**
	 * @return a permanent link to the page for application users.
	 */
	public String getApplicationPermLink() {
		return getPermLink(AuthUtils.APPLICATION);
	}

	/**
	 * @return a permanent link to the page for CAS users.
	 */
	public String getCasPermLink() {
		return getPermLink(AuthUtils.CAS);
	}

	/**
	 * @return a permanent link to the page for Shibboleth users.
	 */
	public String getShibbolethPermLink() {
		return getPermLink(AuthUtils.SHIBBOLETH);
	}

	/**
	 * @return a permanent link to the page for specific users.
	 */
	public String getSpecificPermLink() {
		return getPermLink(AuthUtils.SPECIFIC);
	}

	/**
	 * @return the departmentToAdd
	 */
	public Department getDepartmentToAdd() {
		return departmentToAdd;
	}

	/**
	 * @return the departmentToUpdate
	 */
	public Department getDepartmentToUpdate() {
		return departmentToUpdate;
	}

	/**
	 * @param departmentToUpdate the departmentToUpdate to set
	 */
	public void setDepartmentToUpdate(final Department departmentToUpdate) {
		this.departmentToUpdate = new Department(departmentToUpdate);
	}

	/**
	 * @return the departmentManagerToUpdate
	 */
	public DepartmentManager getDepartmentManagerToUpdate() {
		return departmentManagerToUpdate;
	}

	/**
	 * @param departmentManagerToUpdate the departmentManagerToUpdate to set
	 */
	public void setDepartmentManagerToUpdate(
			final DepartmentManager departmentManagerToUpdate) {
		this.departmentManagerToUpdate = new DepartmentManager(departmentManagerToUpdate);
	}

	/**
	 * @see org.esupportail.commons.web.controllers.LdapSearchCaller#getLdapUid()
	 */
	@Override
	public String getLdapUid() {
		return ldapUid;
	}

	/**
	 * @see org.esupportail.commons.web.controllers.LdapSearchCaller#setLdapUid(java.lang.String)
	 */
	@Override
	public void setLdapUid(final String ldapUid) {
		this.ldapUid = StringUtils.nullIfEmpty(ldapUid);
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(final Department department) {
		this.department = department;
		departmentManagerPaginator.setCurrentPage(0);
		departmentInvitationPaginator.setCurrentPage(0);
		if (department == null) {
			ticketsNumber = 0;
			archivedTicketsNumber = 0;
		} else {
			ticketsNumber = getDomainService().getTicketsNumber(department);
			archivedTicketsNumber = getDomainService().getArchivedTicketsNumber(department);
			refreshCategoryTree();
		}
		departmentManagerPaginator.setDepartment(department);
		departmentInvitationPaginator.setDepartment(department);
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @return the departmentManagerPaginator
	 */
	public DepartmentManagerPaginator getDepartmentManagerPaginator() {
		return departmentManagerPaginator;
	}

	/**
	 * @return the departmentPaginator
	 */
	public ManagedDepartmentPaginator getDepartmentPaginator() {
		return departmentPaginator;
	}

	/**
	 * @return the categoryTree
	 */
	public TreeModelBase getCategoryTree() {
		return categoryTree;
	}

	/**
	 * @param categoryToUpdate the categoryToUpdate to set
	 */
	public void setCategoryToUpdate(final Category categoryToUpdate) {
		this.categoryToUpdate = new Category(categoryToUpdate);
	}

	/**
	 * @return the categoryToUpdate
	 */
	public Category getCategoryToUpdate() {
		return categoryToUpdate;
	}

	/**
	 * @return the name of the assignment algorithm to use by default for the category to update.
	 */
	public String getCategoryToUpdateDefaultAssignmentAlgorithmName() {
		return getDomainService().getCategoryDefaultAssignmentAlgorithmName(categoryToUpdate);
	}

	/**
	 * @return the categoryToAdd
	 */
	public Category getCategoryToAdd() {
		return categoryToAdd;
	}

	/**
	 * @param targetCategory the targetCategory to set
	 */
	public void setTargetCategory(final Category targetCategory) {
		this.targetCategory = targetCategory;
	}

	/**
	 * @return the moveCategoryTree
	 */
	public TreeModelBase getMoveCategoryTree() {
		return moveCategoryTree;
	}

	/**
	 * @param targetDepartment the targetDepartment to set
	 */
	public void setTargetDepartment(final Department targetDepartment) {
		this.targetDepartment = targetDepartment;
	}

	/**
	 * @return the setRealCategoryTree
	 */
	public TreeModelBase getSetRealCategoryTree() {
		return setRealCategoryTree;
	}

	/**
	 * @return the setRealdepartmentTree
	 */
	public TreeModelBase getSetRealdepartmentTree() {
		return setRealDepartmentTree;
	}

	/**
	 * @param memberToAdd the memberToAdd to set
	 */
	public void setMemberToAdd(final User memberToAdd) {
		this.memberToAdd = memberToAdd;
	}

	/**
	 * @param memberToDelete the memberToDelete to set
	 */
	public void setMemberToDelete(final CategoryMember memberToDelete) {
		this.memberToDelete = memberToDelete;
	}

	/**
	 * @param memberToMove the memberToMove to set
	 */
	public void setMemberToMove(final CategoryMember memberToMove) {
		this.memberToMove = memberToMove;
	}

	/**
	 * @return the targetUser
	 */
	public User getTargetUser() {
		return targetUser;
	}

	/**
	 * @param targetUser the targetUser to set
	 */
	public void setTargetUser(final User targetUser) {
		this.targetUser = targetUser;
	}

	/**
	 * @return the useAssignmentAlgorithm
	 */
	public boolean isUseAssignmentAlgorithm() {
		return useAssignmentAlgorithm;
	}

	/**
	 * @param useAssignmentAlgorithm the useAssignmentAlgorithm to set
	 */
	public void setUseAssignmentAlgorithm(final boolean useAssignmentAlgorithm) {
		this.useAssignmentAlgorithm = useAssignmentAlgorithm;
	}

	/**
	 * @param departmentPaginator the departmentPaginator to set
	 */
	public void setDepartmentPaginator(
			final ManagedDepartmentPaginator departmentPaginator) {
		this.departmentPaginator = departmentPaginator;
	}

	/**
	 * @param departmentManagerPaginator the departmentManagerPaginator to set
	 */
	public void setDepartmentManagerPaginator(
			final DepartmentManagerPaginator departmentManagerPaginator) {
		this.departmentManagerPaginator = departmentManagerPaginator;
	}

	/**
	 * @param departmentInvitationPaginator the departmentInvitationPaginator to set
	 */
	public void setDepartmentInvitationPaginator(
			final DepartmentInvitationPaginator departmentInvitationPaginator) {
		this.departmentInvitationPaginator = departmentInvitationPaginator;
	}

	/**
	 * @return the ticketsNumber
	 */
	public int getTicketsNumber() {
		return ticketsNumber;
	}

	/**
	 * @return the archivedTicketsNumber
	 */
	public int getArchivedTicketsNumber() {
		return archivedTicketsNumber;
	}

	/**
	 * @return the setRealDepartmentTree
	 */
	public TreeModelBase getSetRealDepartmentTree() {
		return setRealDepartmentTree;
	}

	/**
	 * @return the managersSortOrder
	 */
	public String getManagersSortOrder() {
		return managersSortOrder;
	}

	/**
	 * @param managersSortOrder the managersSortOrder to set
	 */
	public void setManagersSortOrder(final String managersSortOrder) {
		this.managersSortOrder = managersSortOrder;
	}

	/**
	 * @return the categoriesSortOrder
	 */
	public String getCategoriesSortOrder() {
		return categoriesSortOrder;
	}

	/**
	 * @param categoriesSortOrder the categoriesSortOrder to set
	 */
	public void setCategoriesSortOrder(final String categoriesSortOrder) {
		this.categoriesSortOrder = categoriesSortOrder;
	}

	/**
	 * @return the departmentsSortOrder
	 */
	public String getDepartmentsSortOrder() {
		return departmentsSortOrder;
	}

	/**
	 * @param departmentsSortOrder the departmentsSortOrder to set
	 */
	public void setDepartmentsSortOrder(final String departmentsSortOrder) {
		this.departmentsSortOrder = departmentsSortOrder;
	}

	/**
	 * @return the membersSortOrder
	 */
	public String getMembersSortOrder() {
		return membersSortOrder;
	}

	/**
	 * @param membersSortOrder the membersSortOrder to set
	 */
	public void setMembersSortOrder(final String membersSortOrder) {
		this.membersSortOrder = membersSortOrder;
	}

	/**
	 * @return the notMembersPresentOrder
	 */
	public String getNotMembersPresentOrder() {
		return notMembersPresentOrder;
	}

	/**
	 * @param notMembersPresentOrder the notMembersPresentOrder to set
	 */
	public void setNotMembersPresentOrder(final String notMembersPresentOrder) {
		this.notMembersPresentOrder = notMembersPresentOrder;
	}

	/**
	 * @return the departmentInvitationToDelete
	 */
	protected DepartmentInvitation getDepartmentInvitationToDelete() {
		return departmentInvitationToDelete;
	}

	/**
	 * @param departmentInvitationToDelete the departmentInvitationToDelete to set
	 */
	public void setDepartmentInvitationToDelete(
			final DepartmentInvitation departmentInvitationToDelete) {
		this.departmentInvitationToDelete = departmentInvitationToDelete;
	}

	/**
	 * @return the departmentInvitationPaginator
	 */
	public DepartmentInvitationPaginator getDepartmentInvitationPaginator() {
		return departmentInvitationPaginator;
	}

	/**
	 * @return the faqLinks
	 */
	public List<FaqLink> getFaqLinks() {
		return faqLinks;
	}

	/**
	 * @return the number of faq links
	 */
	public int getFaqLinksNumber() {
		return faqLinks.size();
	}

	/**
	 * @param faqToLink the faqToLink to set
	 */
	public void setFaqToLink(final Faq faqToLink) {
		this.faqToLink = faqToLink;
	}

	/**
	 * @param faqLinkToDelete the faqLinkToDelete to set
	 */
	public void setFaqLinkToDelete(final FaqLink faqLinkToDelete) {
		this.faqLinkToDelete = faqLinkToDelete;
	}

	/**
	 * @param faqLinkToMove the faqLinkToMove to set
	 */
	public void setFaqLinkToMove(final FaqLink faqLinkToMove) {
		this.faqLinkToMove = faqLinkToMove;
	}

	/**
	 * @return the inheritedFaqLinks
	 */
	public List<FaqLink> getInheritedFaqLinks() {
		return inheritedFaqLinks;
	}

	/**
	 * @return the faqTree
	 */
	public FaqTreeModel getFaqTree() {
		return faqTree;
	}

	/**
	 * @return the categoriesAction
	 */
	@RequestCache
	public String getCategoriesAction() {
		if (isCurrentUserCanManageDepartmentCategories()) {
			if ("ADD_DELETE".equals(categoriesAction)) {
				return categoriesAction;
			}
			if ("MOVE".equals(categoriesAction)) {
				return categoriesAction;
			}
		}
		if ("MEMBERS".equals(categoriesAction)) {
			return categoriesAction;
		}
		if ("FAQ_LINKS".equals(categoriesAction)) {
			return categoriesAction;
		}
		categoriesAction = "PROPERTIES";
		return categoriesAction;
	}

	/**
	 * @param categoriesAction the categoriesAction to set
	 */
	public void setCategoriesAction(final String categoriesAction) {
		this.categoriesAction = categoriesAction;
	}

	/**
	 * @return the deleteTargetDepartments
	 */
	public List<Department> getDeleteTargetDepartments() {
		return deleteTargetDepartments;
	}

	/**
	 * @param deleteTargetDepartments the deleteTargetDepartments to set
	 */
	protected void setDeleteTargetDepartments(final List<Department> deleteTargetDepartments) {
		this.deleteTargetDepartments = deleteTargetDepartments;
	}

}
