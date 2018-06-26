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
	 * The visible searched for.
	 */
	private String visible;

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
			@SuppressWarnings("unused") final Result result) {
		// si la catégorie n'est pas visiblle, on l'alimente dans la liste du
		// départment lié. Ca va servir ensuite pour supprimer du Tree les
		// catégories non visibles
		List<Department> departments = new ArrayList<Department>();
		Department department = domainService.getDepartmentByLabel(label);
		List<Category> categories = domainService.getCategories(department);
		String ids[] = cateIds.split(",");

		// liste des catégories définies ne doivent pas etres visibles pour
		// Traitement de l'indicateur visible : si false, cela veux dire que le
		// l'utilisateur et inversement
		if (visible.equals("true")) {
			for (String cateId : ids) {
				Category category = domainService.getCategory(Long.parseLong(cateId.trim()));
				categories.remove(category);
				//cas ou la catégorie est deja dans les non visible -> rules an amont qui aurait deja été traitée
				if(department != null) {
					List<Category> categoriesNotVisibles = department.getCategoriesNotVisibles();
					if(categoriesNotVisibles != null){
						categoriesNotVisibles.remove(category);
						for (Category sousCategories : domainService.getSubCategories(category)) {
							categoriesNotVisibles.remove(sousCategories);
						}
					}
				}
				// on gère les catégories enfant
				categories.removeAll(domainService.getSubCategories(category));
				// on gére les catégories parentes
				removeParent(categories, category);
			}
		} else {
			for (Department departmentResult : result.getDepartments()) {
				List<Category> categoriesNotVisibles = new ArrayList<Category>();
				if(departmentResult.getLabel().equals(department.getLabel())){
					for (String cateId : ids) {
						Category category = domainService.getCategory(Long.parseLong(cateId.trim()));
						categoriesNotVisibles.add(category);
						categoriesNotVisibles.addAll(domainService.getSubCategories(category));
						
						logger.info("categorie Not Visible : " + category);
					}
				}
				//on supprime de result les catégories qui ne sont finalement pas visibles
				departmentResult.addCategorieNotVisible(categoriesNotVisibles);
				return null;
			}
		}
		if(categories.size() != 0){
			department.addCategorieNotVisible(categories);
		}
		if(department != null) {
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
					"<add-by-cate> tags should have a 'cateIds' and visible attribute");
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "<add-by-cate label=\"" + label + "\"" + " visible=\"" + visible + "\"" + " cateIds=\"" + cateIds + "\""
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

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
