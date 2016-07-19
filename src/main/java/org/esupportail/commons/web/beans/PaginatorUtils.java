/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.beans;

/**
 * This class static contains the names of methods used in the creation of component paginator.
 * @author cleprous
 */
public class PaginatorUtils {

	/*
	 ******************* PROPERTIES ******************* */

	/**
	 * The page size.
	 */
	public static final String PAGE_SIZE = "pageSize";
	
	/**
	 * Go to the previous page.
	 */
	public static final String GOTO_PREVIOUS = "gotoPreviousPage";
	
	/**
	 * Go to the first page.
	 */
	public static final String GOTO_FIRST = "gotoFirstPage";
	
	/**
	 * Go to the next page.
	 */
	public static final String GOTO_NEXT = "gotoNextPage";
	
	/**
	 * Go to the last page.
	 */
	public static final String GOTO_LAST = "gotoLastPage";
	
	
	/**
	 * Refresh the data.
	 */
	public static final String RELOAD_DATA = "forceReload";
	
	/**
	 * The current page number.
	 */
	public static final String CURRENT_PAGE = "currentPage";
	
	
	/**
	 * The number of the last page.
	 */
	public static final String LAST_PAGE_NUMBER = "lastPageNumber";
	
	
	/**
	 * The number of the first page (always 0).
	 */
	public static final String FIRST_PAGE_NUMBER = "firstPageNumber";
	
	
	/*
	 ******************* INIT ************************* */
	
	/**
	 * constructor.
	 */
	private PaginatorUtils() {
		super();
	}
	

	
}
