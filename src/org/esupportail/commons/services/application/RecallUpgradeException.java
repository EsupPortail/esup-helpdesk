/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.services.application; 

import org.esupportail.commons.exceptions.EsupException;

/**
 * This esxception is thrown when the upgrade task should be recalled (a commit is needed before).
 */
public class RecallUpgradeException extends EsupException {

	/**
	 * the id for serialization.
	 */
	private static final long serialVersionUID = -3783878224084865299L;
	
	/**
	 * The new upgrade state.
	 */
	private final String newUpgradeState;

	/**
	 * Bean constructor.
	 * @param newUpgradeState 
	 */
	public RecallUpgradeException(final String newUpgradeState) {
		super();
		this.newUpgradeState = newUpgradeState;
	}

	/**
	 * @return the newUpgradeState
	 */
	public String getNewUpgradeState() {
		return newUpgradeState;
	}

}
