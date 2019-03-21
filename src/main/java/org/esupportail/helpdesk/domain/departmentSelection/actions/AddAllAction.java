/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.actions;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.departmentSelection.Result;

/**
 * an Action implementation that returns all the enabled departments.
 */
public class AddAllAction extends AbstractAction {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4038208804020329285L;

	/**
	 * Constructor.
	 */
	public AddAllAction() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.AbstractAction#evalInternal(
	 *      org.esupportail.helpdesk.domain.DomainService,
	 *      org.esupportail.helpdesk.domain.departmentSelection.Result)
	 */
	@Override
	public List<Department> evalInternal(final DomainService domainService, final Result result) {

		result.stopAfterThisRule();
		List<Department> departments = new ArrayList<Department>();
		for (Department departmentAll : domainService.getEnabledDepartments()) {
			// on vérifie si le département est déja dans result
			// on retire toutes les catégories qui sont dans catNonVisible et qui n'ont pas
			// la propriété CateInvisible
			for (Department departmentResult : result.getDepartments()) {
				if (departmentResult != null && departmentAll != null) {
					if (departmentResult.getLabel().equals(departmentAll.getLabel())) {
						for (Category catNonVisibleResult : departmentResult.getCategoriesNotVisibles()) {
							if (!catNonVisibleResult.getCateInvisible()) {
								departmentResult.getCategoriesNotVisibles().remove(catNonVisibleResult);
							}
						}
						continue;
					}
				}
			}
			if (departmentAll == null) {
				continue;
			}
			//cas ou le service n'est pas encore traité dans les regles,
			//on va l'ajouter à la liste mais on va exclure les catégories invisibles avant tout
			List <Category> categoriesNonVisibles = new ArrayList<Category>();
			for(Category cate : domainService.getCategories(departmentAll)) {
				if(cate.getCateInvisible()) {
					categoriesNonVisibles.add(cate);
				}
			}
			if(categoriesNonVisibles.size() != 0) {
				departmentAll.addCategorieNotVisible(categoriesNonVisibles);
			}
			departments.add(departmentAll);
		}

		return departments;
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#compile()
	 */
	@Override
	public void compile() {
		// nothing to check here
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<add-all" + forToString() + " />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "addAll";
	}

}
