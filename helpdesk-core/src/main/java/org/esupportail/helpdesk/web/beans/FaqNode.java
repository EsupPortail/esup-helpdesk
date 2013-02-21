/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import org.esupportail.helpdesk.domain.beans.Department;
import org.esupportail.helpdesk.domain.beans.Faq;

/** 
 * The node of a category.
 */ 
public class FaqNode extends AbstractFirstLastNode {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5789808795661277497L;

	/**
	 * The department.
	 */
	private Department department;

	/**
	 * The FAQ.
	 */
	private Faq faq;
	
	/**
	 * Bean constructor.
	 */
	public FaqNode() {
		super("root", "", true);
	}
	
	/**
	 * Bean constructor.
	 * @param department
	 */
	public FaqNode(final Department department) {
		super("department", department.getLabel(), true);
		setDepartment(department);
	}
	
	/**
	 * Bean constructor.
	 * @param faq
	 */
	public FaqNode(final Faq faq) {
		super("faq", faq.getLabel(), true);
		setDepartment(faq.getDepartment());
		setFaq(faq);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = getClass().getSimpleName() + "#" + hashCode() + "[";  
		result += "type=[" + getType() + "]";
		if (department != null) {
			result += ", department=[" + department.getLabel() + "]";
		}
		result += ", faq=[" + faq.getLabel() + "]";
		result += ", identifier=[" + getIdentifier() + "]";
		result += ", description=[" + getDescription() + "]";
		result += "]";
		return result;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @return the faq
	 */
	public Faq getFaq() {
		return faq;
	}

	/**
	 * @param faq the faq to set
	 */
	protected void setFaq(final Faq faq) {
		this.faq = faq;
	}

	/**
	 * @param department the department to set
	 */
	protected void setDepartment(final Department department) {
		this.department = department;
	}

}

