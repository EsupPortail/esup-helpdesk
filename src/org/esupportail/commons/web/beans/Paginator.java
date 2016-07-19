/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.beans; 

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.faces.model.SelectItem;

/** 
 * The interface of paginators, used to display displaying results 
 * from a large result set over a number of pages without retrieving 
 * all the items from the database. 
 * 
 * Adapted from http://blog.hibernate.org/cgi-bin/blosxom.cgi/2004/08/14#fn.html and
 * http://blog.hibernate.org/cgi-bin/pollxn.cgi?storypath=/Gavin%20King/pagination.html
 * @param <E> the class of the results
 */ 
public interface Paginator<E> extends Serializable { 
	
	/**
	 * @return the page size.
	 */
	int getPageSize();

	/**
	 * @return the current page number.
	 */
	int getCurrentPage();
	
	/**
	 * Force the paginator to reload.
	 */
	void forceReload();

	/**
	 * Go to the next page.
	 */
	void gotoNextPage();

	/**
	 * Go to the previous page.
	 */
	void gotoPreviousPage();

	/**
	 * Go to the first page.
	 */
	void gotoFirstPage();

	/**
	 * Go to the last page.
	 */
	void gotoLastPage();

	/**
	 * @return true if the page is the first one.
	 */
	boolean isFirstPage();
	
	/**
	 * @return true if the page is the last one.
	 */
	boolean isLastPage();
	
	/**
	 * @return the number of the previous page.
	 */
	int getPreviousPage();

	/**
	 * @return the number of the next page.
	 */
	int getNextPage();

	/**
	 * @return the number of the first page (always 0).
	 */
	int getFirstPageNumber();

	/**
	 * @return the number of the last page.
	 */
	int getLastPageNumber();

	/**
	 * @return the number of the first result.
	 */
	int getFirstVisibleNumber();
	
	/**
	 * @return the number of the last result.
	 */
	int getLastVisibleNumber();
	
	/**
	 * @return the list of the results.
	 */
	List<E> getVisibleItems();
	
	/**
	 * @return the number of visible items.
	 */
	int getVisibleItemsCount();
	
	/**
	 * @return the total number of items.
	 */
	int getTotalItemsCount();
	
	/**
	 * @return The numbers of the near pages.
	 */
	List<Integer> getNearPages();
	
	/**
	 * @return the time the data was loaded.
	 */
	Timestamp getLoadTime();
	
	/**
	 * Set the page size.
	 * @param pageSize
	 */
	void setPageSize(int pageSize);

	/**
	 * Set the current page.
	 * @param currentPage 
	 */
	void setCurrentPage(final int currentPage);

	/**
	 * @return the default page size.
	 */
	int getDefaultPageSize();
	
	/**
	 * @return a list of the first pages. (up to MAX_NEAR_PAGES )
	 */
	List<Integer> getFirstPagesNumber();
	
	
	/**
	 * @return a list of the last pages. (down to MAX_NEAR_PAGES )
	 */
	List<Integer> getLastPagesNumber();
	
	/**
	 * @return a list with the currentPage +/- MAX_NEAR_PAGES.
	 */
	List<Integer> getMiddlePagesNumber();
	
	
	/**
	 * @return the pageSizeItems.
	 */
	List<SelectItem> getPageSizeItems();

} 

