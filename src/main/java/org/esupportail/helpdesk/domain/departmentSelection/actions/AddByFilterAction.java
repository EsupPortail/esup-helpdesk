/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.departmentSelection.actions;

import java.util.List;

import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.helpdesk.domain.DomainService;
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
			@SuppressWarnings("unused") final Result result, final boolean evaluateCondition) {
		if (evaluateCondition == false) {
			return null;
		}
		List<Department> depts = domainService.getDepartmentsByFilter(filter);
		// on supprime les département qui sont potentiellement déja dans result afin de
		// conserver les
		// conditions d'autres rules précédement traitées
		for (Department departmentFiltre : depts) {
			for (Department departmentResult : result.getDepartments()) {
				if (departmentResult != null && departmentFiltre != null) {
					if (departmentResult.getLabel().equals(departmentFiltre.getLabel())) {
						// departement deja traité dans une regles précédente
						// on vide la liste des catégories qui n'ont pas de regle définie
						// du coup a ce niveau on sait qu'il faut afficher toutes les catégories sauf
						// celles déja présent dans les non visibles
						if (departmentResult.getCategoriesUndefinedRule() != null) {
							departmentResult.getCategoriesUndefinedRule().clear();
						}
						depts.remove(departmentResult);
					}
				}
			}
		}
		return depts;
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
