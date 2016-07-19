/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.User;

/** 
 * An entry for the user ticket creation statistics.
 */ 
public class UserTicketCreationStatisticEntry implements Serializable, Comparable<UserTicketCreationStatisticEntry> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 4439640744717018399L;

	/**
	 * The rank.
	 */
	private int rank;

	/**
	 * The user.
	 */
	private User user;

	/**
	 * The number.
	 */
	private int number;

	/**
	 * Bean constructor.
	 * @param rank 
	 * @param user 
	 * @param number 
	 */
	public UserTicketCreationStatisticEntry(
			final int rank,
			final User user, 
			final int number) {
		super();
		this.rank = rank;
		this.user = user;
		this.number = number;
	}

	/**
	 * @param o 
	 * @return an int.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final UserTicketCreationStatisticEntry o) {
		if (this.number != o.number) {
			return this.number - o.number;
		}
		return this.user.getDisplayName().compareTo(o.user.getDisplayName());
	}

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

}

