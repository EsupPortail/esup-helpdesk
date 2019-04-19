/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esupportail.commons.aop.monitor.Monitor;
import org.esupportail.commons.dao.HqlUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Invitation;
import org.esupportail.helpdesk.domain.beans.Ticket;
import org.esupportail.helpdesk.domain.beans.TicketMonitoring;
import org.esupportail.helpdesk.domain.beans.User;

/**
 * A bean to extract tickets for the control panel.
 */
@Monitor
public class TicketExtractorImpl extends AbstractTicketExtractor {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5779505084297323900L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * Bean constructor.
	 */
	public TicketExtractorImpl() {
		super();
	}

	/**
	 * @return the condition for opened tickets.
	 */
	protected String getStatusOpenedCondition() {
		return HqlUtils.stringIn("ticket.status", new String[] {
				TicketStatus.FREE,
				TicketStatus.INCOMPLETE,
				TicketStatus.INPROGRESS,
				TicketStatus.POSTPONED,
		});
	}

	/**
	 * @param user
	 * @return the condition regarding the status.
	 */
	protected String getStatusCondition(final User user) {
		String statusFilter;
		if (user.getControlPanelUserInterface()) {
			statusFilter = user.getControlPanelUserStatusFilter();
		} else {
			statusFilter = user.getControlPanelManagerStatusFilter();
		}
		String condition;
		if (ControlPanel.STATUS_FILTER_OPENED.equals(statusFilter)) {
			condition = getStatusOpenedCondition();
		} else if (ControlPanel.STATUS_FILTER_CLOSED.equals(statusFilter)) {
			condition = HqlUtils.not(getStatusOpenedCondition());
		} else {
			condition = HqlUtils.alwaysTrue();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("statusCondition = " + condition);
		}
		return condition;
	}
	
	/**
	 * @param departmentFilter
	 * @return the condition regarding the selected department.
	 */
	protected String getSelectedDepartmentCondition(
			final Department departmentFilter,
			List <Department> visibleDepartments,
			User user) {
		String condition = "";
		//cas ou aucun filtre sur le département
		if (departmentFilter == null) {
			List<Long> departmentsIds = new ArrayList<Long>();
			List<Long> cateIds = new ArrayList<Long>();
			ArrayList<Long> distinctCateIds = new ArrayList<Long>();
			
			for (Department department : visibleDepartments) {
		
				//cas d'un dpt confidentiel
				//on va récupérer uniquement les catégories dont le user est membre 
				if(department.getSrvConfidential()) {
					List<Category> categories = getDomainService().getMemberCategories(user,department);
					for (Category category : categories) {
						if(category.getCategoryConfidential()) {
							cateIds.add(category.getId());
						} else {
							if(category.getParent()!=null) {
								if(getDomainService().isCategoryParentConfidential(category.getParent(), false)){;
									cateIds.add(category.getId());
								}
							}
						}
					}
					
					List<Category> categoriesAll = getDomainService().getCategories(department);
					for (Category category : categoriesAll) {
						//si la catégorie ou tous ces parents ne sont pas confidentiels
						if(category.getCategoryConfidential() || (category.getParent()==null ? false : getDomainService().isCategoryParentConfidential(category.getParent(), false)) ) {
							continue;
						} else {
							cateIds.add(category.getId());
						}
					}
			        Set<Long> set = new HashSet<Long>() ;
			        set.addAll(cateIds) ;
			        distinctCateIds.addAll(set);

				} else {
					departmentsIds.add(new Long(department.getId()));
				}
			}

			if(distinctCateIds.size() > 0) {
				condition = HqlUtils.or(
						HqlUtils.longIn("ticket.department.id", departmentsIds),
						HqlUtils.longIn("ticket.category.id", distinctCateIds));
			} else {
				condition = HqlUtils.alwaysTrue();
			}
		} else {
			//cas ou on filtre sur le département
			
			//cas d'un dpt confidentiel
			//on va récupérer uniquement les catégories dont le user est membre 
			if(departmentFilter.getSrvConfidential()) {
				//si filtre sur catégorie :
				Category categoryFilter = user.getControlPanelCategoryFilter();
				//si filtre sur catégorie
				if(categoryFilter != null) {
					//si catégorie confidentielle ou qu'un de ses parents l'est
					if(categoryFilter.getCategoryConfidential() || (categoryFilter.getParent()==null ? false : getDomainService().isCategoryParentConfidential(categoryFilter.getParent(), false)) ) {
						//si l'utilisateur est membre de al catégorie
						if(getDomainService().isCategoryMember(categoryFilter, user)) {
							List<Long> cateIds = new ArrayList<Long>();
							getAllChildCategories(cateIds, categoryFilter);
							cateIds.add(categoryFilter.getId());
							condition = HqlUtils.longIn("ticket.category.id", cateIds);						}
					} else {
						List<Long> cateIds = new ArrayList<Long>();
						getAllChildCategories(cateIds, categoryFilter);
						cateIds.add(categoryFilter.getId());
						condition = HqlUtils.longIn("ticket.category.id", cateIds);					}

				} else {
					List<Category> categories = getDomainService().getMemberCategories(user,departmentFilter);
					List<Long> cateIds = new ArrayList<Long>();
					for (Category category : categories) {
						//si catégorie confidentielle ou qu'un de ses parents l'est
						if(category.getCategoryConfidential()  || (category.getParent()==null ? false : getDomainService().isCategoryParentConfidential(category.getParent(), false))) {
							cateIds.add(category.getId());
						}
					}
					
					List<Category> categoriesAll = getDomainService().getCategories(departmentFilter);
					for (Category category : categoriesAll) {
						//si catégorie confidentielle ou qu'un de ses parents l'est
						if(category.getCategoryConfidential() || (category.getParent()==null ? false : getDomainService().isCategoryParentConfidential(category.getParent(), false)) ) {
							continue;
						} else {
							cateIds.add(category.getId());
						}
					}
			        Set<Long> set = new HashSet<Long>() ;
			        set.addAll(cateIds) ;
			        ArrayList<Long> distinctCateIds = new ArrayList<Long>(set) ;
	
					condition = HqlUtils.longIn("ticket.category.id", distinctCateIds);
				}
			} else {
				//si filtre sur catégorie :
				Category categoryFilter = user.getControlPanelCategoryFilter();
				if(categoryFilter != null) {
					List<Long> cateIds = new ArrayList<Long>();
					getAllChildCategories(cateIds, categoryFilter);
					cateIds.add(categoryFilter.getId());
					condition = HqlUtils.longIn("ticket.category.id", cateIds);
				}
				else {
					condition = HqlUtils.equals("ticket.department.id", departmentFilter.getId());
				}
			}			
		}
		if (logger.isDebugEnabled()) {
			logger.debug("selectedDepartmentCondition = " + condition);
		}
		return condition;
	}

	private void getAllChildCategories(List<Long>cateIds, Category categoryFilter) {
		for (Category category : getDomainService().getSubCategories(categoryFilter)) {
			cateIds.add(category.getId());
			getAllChildCategories(cateIds, category);
		}
		return;
	}

	/**
	 * @param user
	 * @return the condition for the user to own the tickets.
	 */
	protected String getOwnerCondition(final User user) {
		String condition = HqlUtils.equals("ticket.owner.id", HqlUtils.quote(user.getId()));
		if (logger.isDebugEnabled()) {
			logger.debug("ownerCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition regarding monitoring.
	 */
	protected String getMonitoringCondition(
			final User user) {
		String condition = HqlUtils.exists(
				HqlUtils.selectFromWhere(
						"monitoring",
						TicketMonitoring.class.getSimpleName()
						+ HqlUtils.AS_KEYWORD
						+ "monitoring",
						HqlUtils.and(
								HqlUtils.equals(
										"monitoring.ticket.id",
								"ticket.id"),
								HqlUtils.equals(
										"monitoring.user.id",
										HqlUtils.quote(user.getId()))
						)
				)
		);
		if (logger.isDebugEnabled()) {
			logger.debug("monitoringCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition regarding invitations.
	 */
	protected String getInvitedCondition(
			final User user) {
		String condition = HqlUtils.exists(
				HqlUtils.selectFromWhere(
						"invitation",
						Invitation.class.getSimpleName()
						+ HqlUtils.AS_KEYWORD
						+ "invitation",
						HqlUtils.and(
								HqlUtils.equals(
										"invitation.ticket.id",
								"ticket.id"),
								HqlUtils.equals(
										"invitation.user.id",
										HqlUtils.quote(user.getId()))
						)
				)
		);
		if (logger.isDebugEnabled()) {
			logger.debug("invitedCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition regarding invitations.
	 */
	public String getAllTicketsUserInvited(
			final User user) {
		
		String condition = HqlUtils.selectFromWhere(
				"invitation",
				Invitation.class.getSimpleName()
				+ HqlUtils.AS_KEYWORD
				+ "invitation",
				HqlUtils.equals(
						"invitation.user.id",
						HqlUtils.quote(user.getId()))
				);
		if (logger.isDebugEnabled()) {
			logger.debug("allTicketsUserInvited = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition regarding the managed departments.
	 */
	protected String getManagedDepartmentCondition(
			final List<Department> managedDepartments,
			final User user,
			final List<Long> depaIdConfidentials) {
		List<Long> departmentsIds = new ArrayList<Long>();
		List<Long> cateIds = new ArrayList<Long>();
		
		for (Department department : managedDepartments) {
			//cas d'un dpt confidentiel
			//on va récupérer uniquement les catégories dont le user est membre 
			if(department.getSrvConfidential()) {
				depaIdConfidentials.add(department.getId());
				List<Category> categories = getDomainService().getMemberCategories(user,department);
				for (Category category : categories) {
					//si la catégorie ou un de ces parent est confidentiel
					if(category.getCategoryConfidential() || (category.getParent()==null ? false : getDomainService().isCategoryParentConfidential(category.getParent(), false)) ) {
						cateIds.add(category.getId());
					}
				}
				
				List<Category> categoriesAll = getDomainService().getCategories(department);
				for (Category category : categoriesAll) {
					//si la catégorie ou un de ces parent est confidentiel
					if(category.getCategoryConfidential() || (category.getParent()==null ? false : getDomainService().isCategoryParentConfidential(category.getParent(), false)) ) {
						continue;
					} else {
						cateIds.add(category.getId());
					}
				}
			} else {
				departmentsIds.add(new Long(department.getId()));
			}
		}
		String condition;

		if(cateIds.size() > 0) {
	        Set<Long> set = new HashSet<Long>() ;
	        set.addAll(cateIds) ;
	        ArrayList<Long> distinctCateIds = new ArrayList<Long>(set) ;

			condition = HqlUtils.or(
					HqlUtils.longIn("ticket.department.id", departmentsIds),
					HqlUtils.longIn("ticket.category.id", distinctCateIds));
		} else {
			condition = HqlUtils.longIn("ticket.department.id", departmentsIds);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("managedDepartmentCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @return true if the user can see the ticket.
	 */
	protected String getUserVisibleTicketCondition() {
		String condition = HqlUtils.not(
				HqlUtils.equals(
						"ticket.effectiveScope",
						HqlUtils.quote(TicketScope.PRIVATE)));
		if (logger.isDebugEnabled()) {
			logger.debug("userVisibleCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @return true if the user can read the ticket.
	 */
	protected String getUserReadableTicketCondition() {
		String condition = HqlUtils.equals(
						"ticket.effectiveScope",
						HqlUtils.quote(TicketScope.PUBLIC));
		if (logger.isDebugEnabled()) {
			logger.debug("userReadableCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition for the user to manage the tickets.
	 */
	protected String getManagedCondition(final User user) {
		String condition = HqlUtils.equals("ticket.manager.id", HqlUtils.quote(user.getId()));
		if (logger.isDebugEnabled()) {
			logger.debug("managedCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @return the condition for the user to manage the tickets.
	 */
	protected String getDepartmentConfidentialCondition(List<Long> departmentsIds) {
		String condition = 	HqlUtils.longIn("ticket.department.id", departmentsIds);
		if (logger.isDebugEnabled()) {
			logger.debug("departmentConfidentialCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @return the condition for free tickets.
	 */
	protected String getAllManagedCondition() {
		String condition = HqlUtils.isNotNull("ticket.manager.id");
		if (logger.isDebugEnabled()) {
			logger.debug("allManagedCondition = " + condition);
		}
		return condition;
	}
	
	/**
	 * @return the condition for free tickets.
	 */
	protected String getFreeCondition() {
		String condition = HqlUtils.isNull("ticket.manager.id");
		if (logger.isDebugEnabled()) {
			logger.debug("freeCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @param selectedManager
	 * @return the condition regarding the involvement for managers.
	 */
	protected String getManagerInvolvementCondition(
			final User user,
			final User selectedManager,
			String implication) {
		String condition = null;
		String involvementFilter = user.getControlPanelManagerInvolvementFilter();
		
		if (ControlPanel.MANAGER_INVOLVEMENT_FILTER_FREE.equals(involvementFilter)) {
			condition = getFreeCondition();
		} else if (ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED.equals(involvementFilter)  ||
				   ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_INVITED.equals(involvementFilter)) {
			if (selectedManager != null) {
				condition = getManagedCondition(selectedManager);
			} else {
				condition = getAllManagedCondition();
			}
		} else if (ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_FREE.equals(involvementFilter)) {
			if (selectedManager != null) {
				condition = HqlUtils.or(
						getManagedCondition(selectedManager),
						getFreeCondition());
			} else {
				condition = HqlUtils.or(
						getAllManagedCondition(),
						getFreeCondition());
			}
		} else if (ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE.equals(involvementFilter)) {

			if(implication.equals("OTHER")){
				if (selectedManager != null) {
					condition = getManagedCondition(selectedManager);
				} else {
					condition = getAllManagedCondition();
				}
			}
			if(implication.equals("FREE")){
				condition = getFreeCondition();
			}
			if(implication.equals("INVITE")){
				condition = HqlUtils.alwaysTrue();
			}
		} else {
			//cas Implication = Tous tickets
			if (selectedManager != null) {
				condition = HqlUtils.or(
						getManagedCondition(selectedManager),
						getFreeCondition());
			} else {
				condition = HqlUtils.or(
						getAllManagedCondition(),
						getFreeCondition());
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("managerInvolvementCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @param departmentFilter
	 * @return the condition regarding the category membership.
	 */
	protected String getCategoryMemberCondition(
			final User user,
			final Department departmentFilter) {
		List<Long> categoryIds = new ArrayList<Long>();
		String condition = null;
		
		if(departmentFilter.getSrvConfidential()) {
			for (Category category : getDomainService().getMemberCategories(user, departmentFilter)) {
				//si la catégorie ou tous ces parents ne sont pas confidentiels
				if(category.getCategoryConfidential() || (category.getParent()==null ? false : getDomainService().isCategoryParentConfidential(category.getParent(), false)) ) {
					categoryIds.add(category.getId());
				}
				List<Category> categoriesAll = getDomainService().getCategories(departmentFilter);
				for (Category cate : categoriesAll) {
					//si la catégorie ou tous ces parents ne sont pas confidentiels
					if(cate.getCategoryConfidential() || (cate.getParent()==null ? false : getDomainService().isCategoryParentConfidential(cate.getParent(), false)) ) {
						continue;
					} else {
						categoryIds.add(cate.getId());
					}
				}
			}
	        Set<Long> set = new HashSet<Long>() ;
	        set.addAll(categoryIds) ;
	        ArrayList<Long> distinctCateIds = new ArrayList<Long>(set) ;
			condition = HqlUtils.and(
							HqlUtils.longIn("ticket.category.id", distinctCateIds),
							HqlUtils.or(
									getOwnerCondition(user),
									getManagedCondition(user)
						));
		} else {
			
			for (Category category : getDomainService().getMemberCategories(user, departmentFilter)) {
				categoryIds.add(new Long(category.getId()));
			}

			condition = HqlUtils.longIn("ticket.category.id", categoryIds);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("categoryMemberCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param category
	 * @param categoryIds
	 * @return the category tree of the given category, as a list
	 */
	protected List<Long> getCategoryTreeIds(final Category category, final List<Long> categoryIds) {
		List<Long> theCategoryIds = categoryIds;
		if (theCategoryIds == null) {
			theCategoryIds = new ArrayList<Long>();
		}
		theCategoryIds.add(category.getId());
		for (Category subCategory : getDomainService().getSubCategories(category)) {
			getCategoryTreeIds(subCategory, theCategoryIds);
		}
		return theCategoryIds;
	}

	/**
	 * @param user
	 * @return the condition regarding the visiblity for managers.
	 */
	protected String getManagerVisibleTicketCondition(
			final User user, 
			final User userSelected,
			String implication,
			List<Long> depaIdConfidentials) {
		
		String condition = null;
		
		Department departmentFilter = user.getControlPanelManagerDepartmentFilter();
		List<Department> managedDepartments = getDomainService().getManagedDepartments(userSelected);
		if (departmentFilter != null && !managedDepartments.contains(departmentFilter)) {
			departmentFilter = null;
		}
		if (departmentFilter == null) {
			if(implication.equals("OTHER")) {
				condition = getManagedDepartmentCondition(managedDepartments, userSelected, depaIdConfidentials);
			}			
			else if(implication.equals("FREE")) {
				condition = getManagedCategoryCondition(managedDepartments, userSelected);
				
			} else {
				condition = HqlUtils.alwaysTrue();
			}
				
		} else {
			if (user.getControlPanelCategoryMemberFilter()) {
				condition = getCategoryMemberCondition(userSelected, departmentFilter);
			} else {
				Category categoryFilter = user.getControlPanelCategoryFilter();
				if (categoryFilter != null
						&& !departmentFilter.equals(categoryFilter.getDepartment())) {
					categoryFilter = null;
				}
				
				if (categoryFilter == null) {
					//cas d'un dpt confidentiel
					//on va récupérer uniquement les catégories dont le user est membre 
					if(departmentFilter.getSrvConfidential()) {
						depaIdConfidentials.add(departmentFilter.getId());
						
						List<Category> categories = getDomainService().getMemberCategories(user,departmentFilter);
						List<Long> cateIds = new ArrayList<Long>();
						for (Category category : categories) {
							//cas ou l'on hérite de la catégorie parente ou service
							if(category.getCategoryConfidential() || (category.getParent()==null ? false : getDomainService().isCategoryParentConfidential(category.getParent(), false)) ) {
								cateIds.add(category.getId());
							}
						}
						
						List<Category> categoriesAll = getDomainService().getCategories(departmentFilter);
						for (Category category : categoriesAll) {
							//cas ou l'on hérite de la catégorie parente ou service
							if(category.getCategoryConfidential() || (category.getParent()==null ? false : getDomainService().isCategoryParentConfidential(category.getParent(), false)) ) {
								continue;
							} else {
								cateIds.add(category.getId());
							}
						}
				        Set<Long> set = new HashSet<Long>() ;
				        set.addAll(cateIds) ;
				        ArrayList<Long> distinctCateIds = new ArrayList<Long>(set) ;

						condition = HqlUtils.longIn("ticket.category.id", distinctCateIds);
					} else {
						if(implication.equals("FREE")) {
							List<Category> categories = getDomainService().getMemberCategories(user,departmentFilter);
							List<Long> cateIds = new ArrayList<Long>();
							for (Category category : categories) {
								cateIds.add(category.getId());
							}
							condition = getManagedDepartmentCondition(managedDepartments, userSelected, depaIdConfidentials);
							condition =	HqlUtils.and(
									HqlUtils.longIn("ticket.category.id", cateIds),
									HqlUtils.equals("ticket.department.id", departmentFilter.getId()));
						}						
						else {
							condition = HqlUtils.equals("ticket.department.id", departmentFilter.getId());
						}
					}
					
				} else {
					if(departmentFilter.getSrvConfidential()) {
						if(categoryFilter.getCategoryConfidential() || (categoryFilter.getParent()==null ? false : getDomainService().isCategoryParentConfidential(categoryFilter.getParent(), false)) ) {
							condition = HqlUtils.and(
									HqlUtils.longIn("ticket.category.id", getCategoryTreeIds(categoryFilter, null)),
									HqlUtils.or(
											getOwnerCondition(user),
											getManagedCondition(user)))
								;
						} else {
							condition =	HqlUtils.or(
											getOwnerCondition(user),
											getManagedCondition(user))
								;
						}
					} else {
						if(implication.equals("FREE")) {
							//on récupère les id de la categorie filtre et ses enfants
							List<Long> FilterCatAndsubCatIds = getCategoryTreeIds(categoryFilter, null);
							List<Category> categories = getDomainService().getMemberCategories(user,departmentFilter);
							List<Long> cateIds = new ArrayList<Long>();
							//si la cate filtre et enfants sont des catégorie membres de l'utilisateur, on les prend en compte 
							for (Category category : categories) {
								if(FilterCatAndsubCatIds.contains(category.getId())){
									cateIds.add(category.getId());
								}
							}
							condition =	HqlUtils.longIn("ticket.category.id", cateIds);
						} else {
							condition = HqlUtils.longIn("ticket.category.id", getCategoryTreeIds(categoryFilter, null));
						}
					}
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("managerControlPanelDepartmentCondition = " + condition);
		}
		return condition;
	}

	protected String getManagedCategoryCondition(final List<Department> managedDepartments, final User userSelected){

		List<Long> categoryIds = new ArrayList<Long>();
		List<Category> categories = new ArrayList<Category>();
		
		for (Department department : managedDepartments) {
			categories = getDomainService().getCategories(department);
			for (Category category : categories) {
				//cas ou l'on hérite de la catégorie parente ou service
				if(category.getInheritMembers() == true){
					//cas ou l'on hérite du service 
					if(category.getParent() == null){
						if(getDomainService().getDepartmentManager(department, userSelected) != null){
							categoryIds.add(new Long(category.getId()));
						}
					}
					//cas ou l'on hérite de la catégorie parente
					else if(getDomainService().isMembersOfThirstParentCategory(category, userSelected) == true){
						categoryIds.add(new Long(category.getId()));
					}
				} else {
					if(getDomainService().isCategoryMember(category, userSelected) == true){
						categoryIds.add(new Long(category.getId()));
					}
				}
			}
		}
		if(categoryIds.size() != 0){
			return HqlUtils.longIn("ticket.category.id", categoryIds);
		} else {
			return HqlUtils.alwaysFalse();
		}
	}
	/**
	 * @param user
	 * @return the order by string.
	 */
	protected String getControlPanelOrderByString(
			final User user) {
		String separator = "";
		String result = "";
		for (ControlPanelOrderPart orderPart : user.getControlPanelOrder().getOrderParts()) {
			String orderBy;
			if (ControlPanelOrder.ID.equals(orderPart.getName())) {
				orderBy = "ticket.id";
			} else if (ControlPanelOrder.DEPARTMENT.equals(orderPart.getName())) {
				orderBy = "ticket.department.label";
			} else if (ControlPanelOrder.CATEGORY.equals(orderPart.getName())) {
				orderBy = "ticket.category.label";
			} else if (ControlPanelOrder.CREATION_DEPARTMENT.equals(orderPart.getName())) {
				orderBy = "ticket.creationDepartment.label";
			} else if (ControlPanelOrder.LABEL.equals(orderPart.getName())) {
				orderBy = "ticket.label";
			} else if (ControlPanelOrder.LAST_ACTION_DATE.equals(orderPart.getName())) {
				orderBy = "ticket.lastActionDate";
			} else if (ControlPanelOrder.MANAGER.equals(orderPart.getName())) {
				orderBy = "ticket.managerDisplayName";
			} else if (ControlPanelOrder.OWNER.equals(orderPart.getName())) {
				orderBy = "ticket.owner.displayName";
			} else if (ControlPanelOrder.PRIORITY.equals(orderPart.getName())) {
				orderBy = "ticket.priorityLevel";
			} else if (ControlPanelOrder.STATUS.equals(orderPart.getName())) {
				orderBy = "ticket.status";
			} else {
				throw new IllegalArgumentException("bad order part [" + orderPart + "]");
			}
			result += separator + orderBy + " ";
			if (orderPart.isAsc()) {
				result += "ASC";
			} else {
				result += "DESC";
			}
			separator = ",";
		}
		return result;
	}

	/**
	 * @param user
	 * @param selectedManager
	 * @return the query string for managers.
	 */
	protected String getManagerControlPanelQueryString(
			final User user,
			final User selectedManager,
			String implication,
			String queryInvitation, 
			String queryLibre) {
		
		List<Long> depaIdConfidentials = new ArrayList<Long>();
		String managerCondition = null;
		if (user == null) {
			return null;
		}
		if(!implication.equals("INVITE")){
			managerCondition = HqlUtils.and(
					getStatusCondition(user),
					getManagerInvolvementCondition(user, selectedManager, implication),
					getManagerVisibleTicketCondition(user, selectedManager!=null?selectedManager:user, implication, depaIdConfidentials));
		} 
		if(implication.equals("INVITE")){
			managerCondition = HqlUtils.and(HqlUtils.and(
					getStatusCondition(user),
					getManagerVisibleTicketCondition(user, selectedManager!=null?selectedManager:user, implication, depaIdConfidentials)),
					getInvitedCondition(selectedManager!=null?selectedManager:user));
			
		}
		if (logger.isDebugEnabled()) {
			logger.debug("managerCondition = " + managerCondition);
		}
		//cas des dpt confidentiels
		if(!depaIdConfidentials.isEmpty()) {
			managerCondition = HqlUtils.or(
					managerCondition,
					HqlUtils.and(
							getStatusCondition(user),
							getOwnerCondition(user),
							getManagerInvolvementCondition(user, selectedManager, implication),
							getDepartmentConfidentialCondition(depaIdConfidentials)));
		}
		if (HqlUtils.isAlwaysFalse(managerCondition)) {
			if(user.getControlPanelManagerInvolvementFilter() != null && (
					user.getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE) 
					|| user.getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_INVITED))
					&& !implication.equals("INVITE")){
				if(queryInvitation != null){
					return HqlUtils.fromWhereOrderBy(
							Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
							queryInvitation,
							getControlPanelOrderByString(user));
				}
			}
			return null;
		}
		if(user.getControlPanelManagerInvolvementFilter() != null && (
				user.getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE) ||
				user.getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_INVITED))
				&& implication.equals("INVITE")) {
			return managerCondition;
		} else if(user.getControlPanelManagerInvolvementFilter() != null && (
					user.getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE))
					&& implication.equals("FREE")) {
				return managerCondition;			
		} else if(user.getControlPanelManagerInvolvementFilter() != null && (
				user.getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_INVITED_OR_FREE) ||
				user.getControlPanelManagerInvolvementFilter().equals(ControlPanel.MANAGER_INVOLVEMENT_FILTER_MANAGED_OR_INVITED))
				&& implication.equals("OTHER")){
			if(queryLibre != null){
				return HqlUtils.fromWhereOrderBy(
						Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
						HqlUtils.or(managerCondition, queryInvitation, queryLibre),
						getControlPanelOrderByString(user));
			} else {
				return HqlUtils.fromWhereOrderBy(
						Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
						HqlUtils.or(managerCondition, queryInvitation),
						getControlPanelOrderByString(user));
			}

		} else {
			return HqlUtils.fromWhereOrderBy(
					Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
					managerCondition,
					getControlPanelOrderByString(user));
		}
	}

	/**
	 * @param visibleDepartments
	 * @return the condition regarding the department for non managers.
	 */
	protected String getUserVisibleDepartmentCondition(
			final List<Department> visibleDepartments,
			User user) {
		String condition;
		List<Long> departmentsIds = new ArrayList<Long>();
		List<Long> cateIds = new ArrayList<Long>();

		for (Department department : visibleDepartments) {
			departmentsIds.add(new Long(department.getId()));
		}
		condition = HqlUtils.longIn("ticket.department.id", departmentsIds);
		if (logger.isDebugEnabled()) {
			logger.debug("userVisibleDepartmentCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param departmentFilter
	 * @param visibleDepartments
	 * @return the condition regarding the selected department for non managers.
	 */
	protected String getUserControlPanelVisibleDepartmentCondition(
			final Department departmentFilter,
			final List<Department> visibleDepartments,
			User user) {
		String condition;
		if (departmentFilter == null) {
			condition = getUserVisibleDepartmentCondition(visibleDepartments, user);
		} else {
			condition = HqlUtils.alwaysTrue();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("userControlPanelVisibleDepartmentCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @param departmentFilter
	 * @param visibleDepartments
	 * @return the condition regarding the involvement for non managers.
	 */
	protected String getUserControlPanelInvolvementCondition(
			final User user,
			final Department departmentFilter,
			final List<Department> visibleDepartments) {
		String condition;
		String involvementFilter = user.getControlPanelUserInvolvementFilter();
		if (ControlPanel.USER_INVOLVEMENT_FILTER_OWNER.equals(involvementFilter)) {
			condition = getOwnerCondition(user);
		} else if (ControlPanel.USER_INVOLVEMENT_FILTER_INVITED.equals(involvementFilter)) {
			condition = getInvitedCondition(user);
		} else if (ControlPanel.USER_INVOLVEMENT_FILTER_OWNER_OR_INVITED.equals(involvementFilter)) {
			condition = HqlUtils.or(
					getOwnerCondition(user),
					getInvitedCondition(user));
		} else if (ControlPanel.USER_INVOLVEMENT_FILTER_MONITORING.equals(involvementFilter)) {
			condition = HqlUtils.and(
					getMonitoringCondition(user),
					HqlUtils.or(
							getUserReadableTicketCondition(),
							getOwnerCondition(user),
							getInvitedCondition(user)
					)
			);
		} else {
			condition = HqlUtils.or(
					getOwnerCondition(user),
					getInvitedCondition(user),
					HqlUtils.and(
							getUserVisibleTicketCondition(),
							getUserControlPanelVisibleDepartmentCondition(
									departmentFilter, visibleDepartments, user)
							)
			);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("userInvolvementCondition = " + condition);
		}
		return condition;
	}

	/**
	 * @param user
	 * @param visibleDepartments
	 * @return the query string for non managers.
	 */
	protected String getUserControlPanelQueryString(
			final User user,
			final List<Department> visibleDepartments) {
		if (user == null) {
			return null;
		}
		Department departmentFilter = user.getControlPanelUserDepartmentFilter();
		if (departmentFilter != null && !visibleDepartments.contains(departmentFilter)) {
			departmentFilter = null;
		}
		
		String userCondition = HqlUtils.and(
				getStatusCondition(user),
				getSelectedDepartmentCondition(departmentFilter, visibleDepartments, user),
				getUserControlPanelInvolvementCondition(user, departmentFilter, visibleDepartments));
		if (logger.isDebugEnabled()) {
			logger.debug("userCondition = " + userCondition);
		}
		if (HqlUtils.isAlwaysFalse(userCondition)) {
			return null;
		}
		return HqlUtils.fromWhereOrderByDesc(
				Ticket.class.getSimpleName() + HqlUtils.AS_KEYWORD + "ticket",
				userCondition,
				getControlPanelOrderByString(user));
	}

	/**
	 * @see org.esupportail.helpdesk.domain.TicketExtractor#getControlPanelQueryString(
	 * org.esupportail.helpdesk.domain.beans.User, org.esupportail.helpdesk.domain.beans.User,
	 * java.util.List)
	 */
	@Override
	public String getControlPanelQueryString(
			final User user,
			final User selectedManager,
			final List<Department> visibleDepartments,
			String implication, 
			String queryInvitation,
			String queryLibres) {
		String queryString;
		if (user == null) {
			queryString = null;
		} else if (user.getControlPanelUserInterface() || !getDomainService().isDepartmentManager(user)) {
			queryString = getUserControlPanelQueryString(user, visibleDepartments);
		} else {
			User theSelectedManager = selectedManager;
			if (theSelectedManager != null) {
				if (user.getControlPanelManagerDepartmentFilter() == null) {
				//suppression de la ligne car mise en place du filtre getionnaire si service = Tous
				} else if (!getDomainService().isDepartmentManager(
							user.getControlPanelManagerDepartmentFilter(),
							theSelectedManager)) {
					theSelectedManager = null;
				}
			}
			queryString = getManagerControlPanelQueryString(user, theSelectedManager, implication, queryInvitation, queryLibres);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("queryString = " + queryString);
		}
		return queryString;
	}

}

