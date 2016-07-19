/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.indexing; 

import org.esupportail.helpdesk.domain.beans.Faq;

/**
 * A class that represents FAQ search result.
 */
public class FaqSearchResult extends AbstractSearchResult {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 4587566137765273905L;

	/**
	 * The corresponding FAQ.
	 */
	private Faq faq;

	/**
	 * Constructor.
	 * @param indexId the unique index id
	 * @param score the score
	 * @param faq
	 */
	protected FaqSearchResult(
			final String indexId,
			final int score,
			final Faq faq) {
		super(indexId, score);
		this.faq = faq;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.SearchResult#isFaqSearchResult()
	 */
	@Override
	public boolean isFaqSearchResult() {
		return true;
	}

	/**
	 * @see org.esupportail.helpdesk.services.indexing.SearchResult#getFaq()
	 */
	@Override
	public Faq getFaq() {
		return faq;
	}

}
