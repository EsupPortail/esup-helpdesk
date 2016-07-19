/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.dao;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 * An abstract DAO implementation.
 */
public interface HibernateDaoService {

	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String SELECT_KEYWORD = " SELECT ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String COUNT_KEYWORD = " COUNT ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String AS_KEYWORD = " AS ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String DISTINCT_KEYWORD = " DISTINCT ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String STAR_KEYWORD = " * ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String OPEN_PAREN_KEYWORD = " ( ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String CLOSE_PAREN_KEYWORD = " ) ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String COMMA_KEYWORD = " , ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String COUNT_ALL_PHRASE = 
		COUNT_KEYWORD + OPEN_PAREN_KEYWORD + STAR_KEYWORD + CLOSE_PAREN_KEYWORD;
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String FROM_KEYWORD = " FROM ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String ORDER_BY_KEYWORD = " ORDER BY ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String DESC_KEYWORD = " DESC ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String ASC_KEYWORD = " ASC ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String WHERE_KEYWORD = " WHERE ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String EXISTS_KEYWORD = " EXISTS ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String DOT_KEYWORD = ".";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String EQUALS_KEYWORD = " = ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String LIKE_KEYWORD = " LIKE ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String TRUE_KEYWORD = " true ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String FALSE_KEYWORD = " false ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String QUOTE_KEYWORD = "'";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String AND_KEYWORD = " AND ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String OR_KEYWORD = " OR ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String NOT_KEYWORD = " NOT ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String IN_KEYWORD = " IN ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String IS_NULL_PHRASE = " IS NULL ";
	
	/** @deprecated Use {@link HqlUtils} */
	@Deprecated
	String IS_NOT_NULL_PHRASE = " IS NOT NULL ";
	
	/**
	 * @param hqlQuery 
	 * @return a Query object that corresponds to a query string.
	 */
	Query getQuery(String hqlQuery);
	
	/**
	 * @param sqlQuery 
	 * @return a Query object that corresponds to a native SQL query string.
	 */
	SQLQuery getSqlQuery(String sqlQuery);
	
}
