/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.assignment;

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.User;


/**
 * The result of a ticket assignment.
 */
public class AssignmentResult implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3909011851915433382L;

	/**
	 * The user the ticket should be assigned to.
	 */
	private User user;
	
	/**
	 * The next algorithm state (to update the category).
	 */
	private String nextAlgorithmState;

	/**
	 * Constructor.
	 * @param user
	 * @param nextAlgorithmState
	 */
	public AssignmentResult(
			final User user, 
			final String nextAlgorithmState) {
		super();
		this.user = user;
		this.nextAlgorithmState = nextAlgorithmState;
	}

	/**
	 * @return the nextAlgorithmState
	 */
	public String getNextAlgorithmState() {
		return nextAlgorithmState;
	}

	/**
	 * @param nextAlgorithmState the nextAlgorithmState to set
	 */
	protected void setNextAlgorithmState(final String nextAlgorithmState) {
		this.nextAlgorithmState = nextAlgorithmState;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	protected void setUser(final User user) {
		this.user = user;
	}
	

}
