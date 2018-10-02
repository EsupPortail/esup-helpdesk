/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.actions;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.Result;

/**
 * an Action implementation that returns the departments corresponding to a
 * label.
 */
public class AddByLabelAction extends AbstractAction {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8768334071452926323L;

	/**
	 * The label searched for.
	 */
	private String label;

	/**
	 * Empty constructor.
	 */
	public AddByLabelAction() {
		super();
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
		Department department = domainService.getDepartmentByLabel(label);
		// on vérifie si le département est déja dans result
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
			if(cate.getCateInvisible()) {
				categoriesNonVisibles.add(cate);
			}
		}
		if(categoriesNonVisibles.size() != 0) {
			department.addCategorieNotVisible(categoriesNonVisibles);
		}
		departments.add(department);
		return departments;
	}

	/**
	 * @param label
	 *            The label to set.
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @throws DepartmentSelectionCompileError
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.AbstractAction#compile()
	 */
	@Override
	public void compile() throws DepartmentSelectionCompileError {
		if (label == null) {
			throw new DepartmentSelectionCompileError("<add-by-label> tags should have a 'label' attribute");
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<add-by-label label=\"" + label + "\"" + forToString() + " />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "addByLabel";
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

}
