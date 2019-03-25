/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.actions;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.departmentSelection.Result;

/**
 * an Action implementation that returns the departments corresponding to a
 * filter.
 */
public class AddByFilterAction extends AbstractAction {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6488752060357904815L;
	/**
	 * The filter searched for.
	 */
	private String filter;
	/**
	 * Empty constructor.
	 */
	public AddByFilterAction() {
		super();
		filter = null;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.AbstractAction#evalInternal(
	 *      org.esupportail.helpdesk.domain.DomainService,
	 *      org.esupportail.helpdesk.domain.departmentSelection.Result)
	 */
	@Override
	public List<Department> evalInternal(final DomainService domainService,
			@SuppressWarnings("unused") final Result result) {

		List<Department> departments = new ArrayList<Department>();
		for (Department department : domainService.getDepartmentsByFilter(filter)) {		// on vérifie si le département est déja dans result
			// on retire toutes les catégories qui sont dans catNonVisible et qui n'ont pas
			// la propriété CateInvisible
			List<Category> categoryToRemove = new ArrayList<Category>();
			for (Department departmentResult : result.getDepartments()) {

				if (departmentResult != null && department != null) {
					if (departmentResult.getLabel().equals(department.getLabel())) {
						for (Category catNonVisibleResult : departmentResult.getCategoriesNotVisibles()) {
							if (!catNonVisibleResult.getCateInvisible()) {
								categoryToRemove.add(catNonVisibleResult);
							}
						}
						if(!categoryToRemove.isEmpty()) {
							departmentResult.getCategoriesNotVisibles().removeAll(categoryToRemove);
						}
						return null;
					}
				}
			}
			if (department == null) {
				return null;
			}
			//cas ou le service n'est pas encore traité dans les regles,
			//on va l'ajouter à la liste mais on va exclure les catégories invisibles avant tout
			List <Category> categoriesNonVisibles = new ArrayList<Category>();
			for(Category cate : domainService.getCategories(department)) {
				if(cate.getRealCategory() != null) {
					checkRealCateRules(cate.getRealCategory(), categoriesNonVisibles, domainService);
				}
				if(cate.getCateInvisible()) {
					categoriesNonVisibles.add(cate);
				}
			}
			if(categoriesNonVisibles.size() != 0) {
				department.addCategorieNotVisible(categoriesNonVisibles);
			}
			departments.add(department);
		}
		return departments;
	}
	
	private List <Category> checkRealCateRules(Category category, List <Category> categoriesNonVisibles, final DomainService domainService){
		for(Category cate : domainService.getCategories(category.getDepartment())) {
			if(cate.getCateInvisible()) {
				categoriesNonVisibles.add(cate);
			}
		}
		return categoriesNonVisibles;
	}
	
	
	
	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.AbstractAction#compile()
	 */
	@Override
	public void compile() {
		// nothing to check
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "<add-by-filter filter=\"";
		if (filter != null) {
			str += filter;
		}
		str += "\"" + forToString() + " />";
		return str;
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(final String filter) {
		this.filter = StringUtils.nullIfEmpty(filter);
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "addByFilter";
	}

}
