/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * A control panel order.
 */
public class ControlPanelOrder implements Serializable {

	/** An order part name. */
	public static final String ID = "id";
	/** An order part name. */
	public static final String CREATION_DEPARTMENT = "creationDepartment";
	/** An order part name. */
	public static final String DEPARTMENT = "department";
	/** An order part name. */
	public static final String CATEGORY = "category";
	/** An order part name. */
	public static final String LABEL = "label";
	/** An order part name. */
	public static final String STATUS = "status";
	/** An order part name. */
	public static final String PRIORITY = "priority";
	/** An order part name. */
	public static final String CREATION_DATE = "creationDate";
	/** An order part name. */
	public static final String LAST_ACTION_DATE = "lastActionDate";
	/** An order part name. */
	public static final String OWNER = "owner";
	/** An order part name. */
	public static final String MANAGER = "manager";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -2182251749612671014L;
	
	/**
	 * The order part names.
	 */
	private static final String [] ALLOWED_PART_NAMES = {
		ID,
		CREATION_DEPARTMENT,
		DEPARTMENT,
		CATEGORY,
		LABEL,
		STATUS,
		PRIORITY,
		CREATION_DATE,
		LAST_ACTION_DATE,
		OWNER,
		MANAGER
	};
	
	/**
	 * The order spec parts.
	 */
	private List<ControlPanelOrderPart> orderParts;
	
	/**
	 * Constructor.
	 * @param orderSpec 
	 */
	public ControlPanelOrder(
			final String orderSpec) {
		super();
		orderParts = new ArrayList<ControlPanelOrderPart>();
		if (StringUtils.hasText(orderSpec)) {
			for (String orderPartSpec : orderSpec.split(",")) {
				orderParts.add(new ControlPanelOrderPart(orderPartSpec));
			}
		}
		if (orderParts.size() == 0) {
			orderParts.add(new ControlPanelOrderPart("-" + LAST_ACTION_DATE));
		}
	}

	/**
	 * @param partName
	 * @return true if partName is allowed.
	 */
	public static boolean isAllowedPartName(final String partName) {
		for (String allowedPartName : ALLOWED_PART_NAMES) {
			if (allowedPartName.equals(partName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		String separator = "";
		String result = "";
		for (ControlPanelOrderPart part : orderParts) {
			result += separator + part.toString();
			separator = ",";
		}
		return result;
	}
	
	/**
	 * Set the first order part.
	 * @param orderPartSpec 
	 */
	public void setFirstOrderPart(final String orderPartSpec) {
		ControlPanelOrderPart part = new ControlPanelOrderPart(orderPartSpec);
		List<ControlPanelOrderPart> newOrderParts = new ArrayList<ControlPanelOrderPart>();
		newOrderParts.add(part);
		String name = part.getName();
		if (!ID.equals(name) && !LAST_ACTION_DATE.equals(name)) {
			for (ControlPanelOrderPart oldPart : orderParts) {
				if (!name.equals(oldPart.getName())) {
					newOrderParts.add(oldPart);
				}
			}
		}
		orderParts = newOrderParts;
	}

	/**
	 * @return the first order part.
	 */
	public ControlPanelOrderPart getFirstOrderPart() {
		return orderParts.get(0);
	}

	/**
	 * @return the orderParts
	 */
	public List<ControlPanelOrderPart> getOrderParts() {
		return orderParts;
	}

}
