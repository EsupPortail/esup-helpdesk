/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.helpdesk.domain.DomainService;
import org.esupportail.helpdesk.domain.beans.Category;
import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.departmentSelection.DepartmentSelectionCompileError;
import org.esupportail.helpdesk.domain.departmentSelection.Result;

/**
 * an Action implementation that returns the departments corresponding to a
 * label.
 */
public class AddByCateAction extends AbstractAction {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6444438890786204225L;

	/**
	 * The cateIds searched for.
	 */
	private String cateIds;

	/**
	 * The label searched for.
	 */
	private String label;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * Empty constructor.
	 */
	public AddByCateAction() {
		super();
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.AbstractAction#evalInternal(
	 *      org.esupportail.helpdesk.domain.DomainService,
	 *      org.esupportail.helpdesk.domain.departmentSelection.Result)
	 */
	@Override
	public List<Department> evalInternal(final DomainService domainService,
			@SuppressWarnings("unused") final Result result, final boolean evaluateCondition) {

		List<Department> departments = new ArrayList<Department>();
		Department department = domainService.getDepartmentByLabel(label);
		List<Category> categories = domainService.getCategories(department);
		String ids[] = cateIds.split(",");
		Boolean departementPresentResult = false;

		// la personne ne doit pas voir la/les catégories spécifiées
		if (evaluateCondition == false) {
			List<Category> categorieUndefinedRule = domainService.getCategories(department);
			boolean departementDejaDansResult = false;

			for (Department departmentResult : result.getDepartments()) {
				if (departmentResult.getLabel().equals(department.getLabel())) {
					departementDejaDansResult = true;
					for (String cateId : ids) {
						Category category = domainService.getCategory(Long.parseLong(cateId.trim()));
						List<Category> categoriesNotVisibles = departmentResult.getCategoriesNotVisibles();
						// ajout de la categorie indiquée dans la rule en tant que catégorie non
						// visibles
						categoriesNotVisibles.add(category);
						// ajout de ses categories enfants
						categoriesNotVisibles.addAll(domainService.getSubCategories(category));
					}
					break;
				}
			}
			// departement absent de result, il faut donc l'ajouter avec prise en compte de
			// la rule
			if (!departementDejaDansResult) {
				for (String cateId : ids) {
					Category category = domainService.getCategory(Long.parseLong(cateId.trim()));
					department.getCategoriesNotVisibles().add(category);
					department.getCategoriesNotVisibles().addAll(domainService.getSubCategories(category));

					List<Category> categoriesNotVisibles = domainService.getCategories(department);
					// suppression de la categorie indiquée dans la rule en tant que catégorie non
					// visibles
					categoriesNotVisibles.remove(category);
					// suppression de ses categories enfants
					categoriesNotVisibles.removeAll(domainService.getSubCategories(category));
					department.getCategoriesUndefinedRule().addAll(categoriesNotVisibles);
					departments.add(department);
				}
			}
		} // la personne doit voir le liste des catégories spécifiées
		else {
			List<Category> categorieUndefinedRule = domainService.getCategories(department);
			boolean departementDejaDansResult = false;
			for (Department departmentResult : result.getDepartments()) {
				if (departmentResult.getLabel().equals(department.getLabel())) {
					for (String cateId : ids) {
						Category category = domainService.getCategory(Long.parseLong(cateId.trim()));

						List<Category> categoriesNotVisibles = departmentResult.getCategoriesNotVisibles();
						// suppression de la categorie indiquée dans la rule en tant que catégorie non
						// visibles
						categoriesNotVisibles.remove(category);
						// suppression de ses categories enfants
						categoriesNotVisibles.removeAll(domainService.getSubCategories(category));
						departmentResult.getCategoriesUndefinedRule().remove(category);
						departementDejaDansResult = true;
					}
					break;
				}
			}
			// departement absent de result, il faut donc l'ajouter avec prise en compte de
			// la rule
			if (!departementDejaDansResult) {
				for (String cateId : ids) {
					Category category = domainService.getCategory(Long.parseLong(cateId.trim()));
					categorieUndefinedRule.remove(category);
					categorieUndefinedRule.removeAll(domainService.getSubCategories(category));
				}
			}
			department.addCategoriesUndefinedRule(categorieUndefinedRule);
			departments.add(department);
		}
		return departments;
	}

	private void removeParent(List<Category> categories, Category category) {
		if (category.getParent() != null) {
			categories.remove(category.getParent());
			removeParent(categories, category.getParent());
		}
	}

	private List<Category> getParents(List<Category> categories, Category category) {
		List<Category> categoriesParent = new ArrayList<Category>();
		if (category.getParent() != null) {
			categoriesParent.add(category.getParent());
			getParents(categories, category.getParent());
		}
		return categoriesParent;
	}

	public void setCateIds(String cateIds) {
		this.cateIds = cateIds;
	}

	/**
	 * @throws DepartmentSelectionCompileError
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.AbstractAction#compile()
	 */
	@Override
	public void compile() throws DepartmentSelectionCompileError {
		if (cateIds == null) {
			throw new DepartmentSelectionCompileError(
					"<add-by-cate> tags should have a 'cateIds' ");
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<add-by-cate label=\"" + label + "\"" + " cateIds=\"" + cateIds + "\""
				+ forToString() + " />";
	}

	/**
	 * @see org.esupportail.helpdesk.domain.departmentSelection.actions.Action#getNodeType()
	 */
	@Override
	public String getNodeType() {
		return "addByCate";
	}

	public String getCateIds() {
		return cateIds;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
