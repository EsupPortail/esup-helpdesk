/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain;

/**
 * A part of the control panel order.
 */
public class ControlPanelOrderPart {
	
	/**
	 * The name.
	 */
	private String name;
	
	/**
	 * True if asc order.
	 */
	private boolean asc;

	/**
	 * Constructor.
	 * @param orderSpecPart 
	 */
	public ControlPanelOrderPart(
			final String orderSpecPart) {
		super();
		if (orderSpecPart.length() < 2) {
			throw new IllegalArgumentException(
					"bad order spec part [" + orderSpecPart + "]");
		}
		String sign = orderSpecPart.substring(0, 1);
		if ("+".equals(sign)) {
			asc = true;
		} else if ("-".equals(sign)) {
			asc = false;
		} else {
			throw new IllegalArgumentException(
					"bad order spec part [" + orderSpecPart + "]");
		}
		name = orderSpecPart.substring(1);
		if (!ControlPanelOrder.isAllowedPartName(name)) {
			throw new IllegalArgumentException(
					"bad order spec part [" + orderSpecPart + "]");
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result;
		if (asc) {
			result = "+";
		} else {
			result = "-";
		}
		result += name;
		return result;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the asc
	 */
	public boolean isAsc() {
		return asc;
	}

}
