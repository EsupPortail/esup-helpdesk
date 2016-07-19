/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.web.beans; 

import java.io.Serializable;

import org.esupportail.helpdesk.domain.beans.Bookmark;

/** 
 * An entry of the bookmarks page.
 */ 
public class BookmarkEntry implements Serializable {
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 7362303189689622345L;

	/**
	 * The bookmark.
	 */
	private Bookmark bookmark;

	/**
	 * True if the bookmark can be read by the user.
	 */
	private boolean canRead;

	/**
	 * Bean constructor.
	 * @param bookmark
	 * @param canRead 
	 */
	public BookmarkEntry(
			final Bookmark bookmark, 
			final boolean canRead) {
		super();
		this.bookmark = bookmark;
		this.canRead = canRead;
	}

	/**
	 * @return the canRead
	 */
	public boolean isCanRead() {
		return canRead;
	}

	/**
	 * @return the bookmark
	 */
	public Bookmark getBookmark() {
		return bookmark;
	}

}

