/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.domain.beans;

import java.io.Serializable;

/**
 * The representation of a deleted item, to be deleted from the index. */
public final class DeletedItem implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 1749162911166339392L;

	/**
     * Primary key.
     */
    private long id;

    /**
     * The id used for the indexation.
     */
    private String indexId;

	/**
	 * Constructor.
	 */
	public DeletedItem() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param indexId
	 */
	public DeletedItem(
			final String indexId) {
		this();
		this.indexId = indexId;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DeletedItem)) {
			return false;
		}
		return ((DeletedItem) obj).getId() == getId();
	}

	/**
	 * @see java.lang.Object#hashCode() 
	 */
	@Override
	public int hashCode() {
		return (int) getId();
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @return the indexId
	 */
	public String getIndexId() {
		return indexId;
	}

	/**
	 * @param indexId the indexId to set
	 */
	public void setIndexId(final String indexId) {
		this.indexId = indexId;
	}
	
}
