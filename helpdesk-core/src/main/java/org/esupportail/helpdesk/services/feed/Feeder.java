/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed;

import java.io.Serializable;

/**
 * The interface of database feeders.
 *
 */
public interface Feeder extends Serializable {

	/**
	 * Feed the database.
	 * @param errorHolder 
	 * @return true if the database should be committed.
	 */
	boolean feed(ErrorHolder errorHolder);

}