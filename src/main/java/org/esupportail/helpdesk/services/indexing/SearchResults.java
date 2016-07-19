/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A class to store search results.
 */
public class SearchResults implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7351259155123090522L;

	/**
	 * The results themselves.
	 */
	private List<SearchResult> results;
	
	/**
	 * The total results number (exact or estimated).
	 */
	private int totalResultsNumber;

	/**
	 * True if the total results number is estimated.
	 */
	private boolean totalResultsNumberEstimated;

	/**
	 * Bean constructor.
	 * @param results
	 * @param totalResultsNumber
	 * @param totalResultsNumberEstimated
	 */
	protected SearchResults(
			final List<SearchResult> results, 
			final int totalResultsNumber,
			final boolean totalResultsNumberEstimated) {
		super();
		this.results = results;
		this.totalResultsNumber = totalResultsNumber;
		this.totalResultsNumberEstimated = totalResultsNumberEstimated;
	}

	/**
	 * @param theResults
	 * @param theTotalResultsNumber
	 * @return an instance for an estimated results number.
	 */
	public static SearchResults estimated(
			final List<SearchResult> theResults, 
			final int theTotalResultsNumber) {
		return new SearchResults(theResults, theTotalResultsNumber, true);
	}

	/**
	 * @param theResults
	 * @return an instance for an exact results number.
	 */
	public static SearchResults exact(
			final List<SearchResult> theResults) {
		return new SearchResults(theResults, theResults.size(), false);
	}

	/**
	 * @return an instance for a zero result number.
	 */
	public static SearchResults empty() {
		return exact(new ArrayList<SearchResult>());
	}

	/**
	 * @return the results number
	 */
	public int getResultsNumber() {
		return results.size();
	}

	/**
	 * @return the results
	 */
	public List<SearchResult> getResults() {
		return results;
	}

	/**
	 * @return the totalResultsNumber
	 */
	public int getTotalResultsNumber() {
		return totalResultsNumber;
	}

	/**
	 * @return the totalResultsNumberEstimated
	 */
	public boolean isTotalResultsNumberEstimated() {
		return totalResultsNumberEstimated;
	}

}
