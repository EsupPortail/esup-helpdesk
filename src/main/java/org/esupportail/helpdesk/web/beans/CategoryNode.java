/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.util.ArrayList;
import java.util.List;

import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.CategoryMember;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.DepartmentManager;
import org.esupportail.helpdesk.domain.beans.FaqLink;
import org.esupportail.helpdesk.domain.beans.User;

/** 
 * The node of a category.
 */ 
public class CategoryNode extends AbstractFirstLastNode {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6242238765263496402L;

	/**
	 * The department.
	 */
	private Department department;

	/**
	 * The category.
	 */
	private Category category;
	
	/**
	 * The virtual categories of the node.
	 */
	private List<Category> virtualCategories;

	/**
	 * The members of the node.
	 */
	private List<CategoryMember> members;

	/**
	 * The managers of the node.
	 */
	private List<User> managers;

	/**
	 * The faq links of the node.
	 */
	private List<FaqLink> faqLinks;

	/**
	 * Bean constructor.
	 */
	public CategoryNode() {
		super("root", "root", true);
	}
	
	/**
	 * Bean constructor.
	 * @param department
	 */
	public CategoryNode(final Department department) {
		super("department", department.getLabel(), true);
		this.department = department;
	}
	
	/**
	 * Bean constructor.
	 * @param category 
	 * @param categoryLabel 
	 */
	public CategoryNode(final Category category, final String categoryLabel) {
		this(category.getDepartment(), category, categoryLabel);
	}
	
	/**
	 * Bean constructor.
	 * @param department
	 * @param category 
	 * @param categoryLabel 
	 */
	public CategoryNode(
			final Department department,
			final Category category,
			final String categoryLabel) {
		super("category", categoryLabel, true);
		this.category = category;
		this.department = department;
	}
	
	/**
	 * @param category 
	 * @return the node identifier of a category.
	 */
	public static String getIdentifier(final Category category) {
		return "category-" + category.getId();
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @return the virtualCategories
	 */
	public List<Category> getVirtualCategories() {
		return virtualCategories;
	}

	/**
	 * @param virtualCategories the virtualCategories to set
	 */
	public void setVirtualCategories(final List<Category> virtualCategories) {
		this.virtualCategories = virtualCategories;
	}

	/**
	 * @return the number of virtual categories
	 */
	public int getVirtualCategoriesNumber() {
		if (virtualCategories == null) {
			return 0;
		}
		return virtualCategories.size();
	}

	/**
	 * @return the members
	 */
	public List<CategoryMember> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(final List<CategoryMember> members) {
		this.members = members;
	}

	/**
	 * @return the number of members
	 */
	public int getMembersNumber() {
		if (members == null) {
			return 0;
		}
		return members.size();
	}

	/**
	 * @return the faqLinks
	 */
	public List<FaqLink> getFaqLinks() {
		return faqLinks;
	}

	/**
	 * @param faqLinks the faqLinks to set
	 */
	public void setFaqLinks(final List<FaqLink> faqLinks) {
		this.faqLinks = faqLinks;
	}

	/**
	 * @return the number of faq links
	 */
	public int getFaqLinksNumber() {
		if (faqLinks == null) {
			return 0;
		}
		return faqLinks.size();
	}

	/**
	 * @return the managers
	 */
	public List<User> getManagers() {
		return managers;
	}

	/**
	 * @param departmentManagers the managers to set as a list of department managers.
	 */
	public void setManagersAsDepartmentManagers(final List<DepartmentManager> departmentManagers) {
		managers = new ArrayList<User>();
		for (DepartmentManager departmentManager : departmentManagers) {
			managers.add(departmentManager.getUser());
		}
	}

	/**
	 * @param categoryMembers the managers to set as a list of category members.
	 */
	public void setManagersAsCategoryMembers(final List<CategoryMember> categoryMembers) {
		managers = new ArrayList<User>();
		for (CategoryMember categoryMember : categoryMembers) {
			managers.add(categoryMember.getUser());
		}
	}

	/**
	 * @return the number of managers
	 */
	public int getManagersNumber() {
		if (managers == null) {
			return 0;
		}
		return managers.size();
	}

	/**
	 * @return the managers as a comma-separated String
	 */
	public String getManagersString() {
		if (managers == null || managers.isEmpty()) {
			return null;
		}
		String result = "";
		String separator = "";
		for (User user : managers) {
			result += separator + user.getRealId();
			separator = ",";
		}
		return result;
	}

}

