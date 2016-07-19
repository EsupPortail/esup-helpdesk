/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import org.esupportail.commons.dao.HibernateFixedQueryPaginator;
import org.esupportail.commons.dao.HqlUtils;
import org.esupportail.helpdesk.domain.beans.User;

/** 
 * A paginator for administrators.
 */ 
public class AdministratorPaginator extends HibernateFixedQueryPaginator<User> {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 5736360426076774550L;

	/**
	 * Constructor.
	 */
	public AdministratorPaginator() {
		super(HqlUtils.fromWhereOrderByAsc(
				User.class.getSimpleName(),
				HqlUtils.isTrue("admin"),
				"id"));
	}
	
}

